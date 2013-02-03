/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import rcm.phase34.ui._2_MappingData;
import gate.util.Out;
import java.util.ArrayList;
import api.ont.IOntologyFiles;
import api.ont.IOntology;
import api.ont.Jena_Ontology;
import api.ont.OntologicalConcept;
import helper.reg.Regulation;
import helper.reg.Statement;
import helper.util.Converter;
import helper.util.Print;
import helper.util.RegEx;
import helper.util.Splitter;
import api.wn.Thesaurus;
import api.wn.Wordnet;

/**
 * maps the regulations with the validation tasks in the OntoReg ontology
 *  - uses SemReg to get the annotations of regulations
 * - uses hasAction and hasPatient  of validation tasks to get the action and subjects of the task repectively.
 *  - uses all the classes and superclasses of the action and patient to get more meaning
 *  - uses wordnet to get root words, synonyms, hypernyms and hyponyns.
 * @author Krishna Sapkota
 */
public class MappingAlgorithm1 implements IOntologyFiles{

    /*  using ontologies and wordnet  */
    private IOntology semReg;
    private IOntology ontoReg;
    private Wordnet wn ;
    private _2_MappingData mappingData;
    private ArrayList<_2_MappingData> mdList;

    private int mappingId = 0;
    /* a list of names of regulations (i.e. regulation id)    */
    private ArrayList<String> regStringList;
    /*  a list of names of validation tasks (i.e task id)  */
    private ArrayList<String> taskStringList;
    /*   a list of java classes to hold subject and action annotations of validation tasks  */
    private ArrayList<Statement> taskStmtList;
    /*  a list of java classes to hold subject and action annotations of regulations  */
    private ArrayList<Regulation> regRegList;
    private ArrayList<Statement> regStmtList;

    /*  priliminary hypothesis of scores based on the distance*/
    private static final double ROOT_SCORE = 50;
    private static final double SYNO_SCORE = 45 ;
    private static final double HYPO_SCORE = 40;
    private static final double HYPER_SCORE = 35;

    /*  no. of validation tasks and regulations to process */
    private static final int NO_OF_TASK = 5;
    private static final int NO_OF_REG = 5;
    /* minimum score to be accepted for updating mapping data */
    private static final double ACCEPTING_SCORE = 0;
   
public MappingAlgorithm1() {
        Print.prln("MappingAlgorithm initialised...");

//        ontoReg = new Jena_Ontology(path, ontoFile, ontoPrefix);
//        semReg = new Jena_Ontology(path, semFile, semPrefix);
//        Print.prln(new OntologicalConcept("Tank101", ontoReg).toString());
//        Print.prln(new OntologicalConcept("Eudralex_5.12", semReg).toString());

        init();
        process();
        finish();
    }

private void init(){
    /*  initialise ontologies  */
        ontoReg = new Jena_Ontology(path, ontoFile, ontoPrefix);
        semReg = new Jena_Ontology(path, semFile, semPrefix);
//        Print.prln(new OntologicalConcept("Tank101", ontoReg).toString());
//        Print.prln(new OntologicalConcept("Eudralex_5.12", semReg).toString());

        /*  initialise the wordnet   */
        wn = new Wordnet();
        mappingData = new _2_MappingData();
        mdList = new ArrayList<_2_MappingData>();
        mappingData.setMappingList(mdList);

        /*  initialise the other attributes   */
        regStringList = semReg.getIndsOfRegulation();
        taskStringList = ontoReg.getIndsOfTask();
        regRegList = new ArrayList<Regulation>();
        taskStmtList = new ArrayList<Statement>();
        regStmtList = new ArrayList<Statement>();
}
private void finish(){
        mappingData.writeFile();
        mappingData.readFile();
        mappingData.sortMappingList();
        mappingData.generateId();
        mappingData.writeFile();
        Out.prln("*** MAPPING  COMPLETED *** ");
}

/**
 *  executes the whole mapping operations
 *  - it integrates all the methods to perform mapping
 */
private void process(){

    /* finds the actions and subjects and populates the relevant java classes */
    taskStmtList = interpreteTaskList(taskStringList);
    regRegList = interpreteRegList(regStringList);

    /*   for each regulation, check whether it is matched with the validation tasks */
    for (Regulation clsReg: regRegList){ 
        /*  check with each validation task, if  the rgulation matches   */
        for (Statement clsTask: taskStmtList){
            /*  represents the subject score  */
             double subjectScore = 0;
             /*   represents the action score */
             double actionScore = 0;
             String regText = "";
             /*  computes and represents the subject annotations of the validation task   */
             ArrayList<String> taskSubList = clsTask.getSubAnnList();
             /*   computes and represents the action annotations of the validation task */
             ArrayList<String> taskActList = clsTask.getActAnnList();

             ArrayList<String> regSubList1 = new ArrayList<String>();
             ArrayList<String> regActList1 = new ArrayList<String>();

             /*  a regulation may contain multiple statements. It checks each statement with the validation task  */
             for (Statement regStmt: clsReg.getStmtList()){
                regText += regStmt.getDescription();
                /*  computes and represents the subject annotations of the statement  */
                ArrayList<String> regSubList = regStmt.getSubAnnList();
                /*  computes and represents the action annotations of the statement  */
                ArrayList<String> regActList = regStmt.getActAnnList();
                /*  get calculated scores */
                subjectScore = calculateListScore(regSubList, taskSubList);
                actionScore = calculateListScore(regActList, taskActList);
                regSubList1.addAll(regSubList);
                regActList1.addAll(regActList);
                /* populate mapping data */
                if (subjectScore > ACCEPTING_SCORE || actionScore > ACCEPTING_SCORE){
                    _2_MappingData m = new _2_MappingData();
                    mdList.add(m);
                    mappingId ++;
                    m.setMappingId("mapping_"+mappingId);
                    m.setRegId(clsReg.getId());
                    m.setRegText(regText);
                    m.setRegSubjectList(regSubList1);
                    m.setRegActionList(regActList1);
                    m.setTaskId(clsTask.getId());
                    m.setTaskSubjectList(taskSubList);
                    m.setTaskActionList(taskActList);
                    m.setSubjectScore(subjectScore);
                    m.setActionScore(actionScore);
                    m.setAccepted(false);
                    m.setRemark("Simple mapping algorithm applied");
                }
            }
        }
    }
}
/**
 * Purpose: computes the subject and action annotations of the validation task.
 * interpretes the meaning of the validation task. It uses the semantic web technologies.
 * it uses the OntoReg ontology and traces the values of  hasPatient and hasAction object properties and
 * infers the the classes the values belong to. It utilises wordnet to get the synonyms of the words.
 * @param taskIndList is the list of the validation task to be interpreted.
 * @return subject and action annotations organised into a list of java classes.
 */
private ArrayList<Statement> interpreteTaskList(ArrayList<String> taskIndList){
    /*  interprete each validation task and add it to thes task list */
    for (int i=0; i< NO_OF_TASK ; i++){
        if (i< taskIndList.size()){
            String taskInd = taskIndList.get(i);
            Statement taskStmt = new Statement();
            ArrayList<String> subjectList = new ArrayList<String>();
            ArrayList<String> actionList = new ArrayList<String>();
            /*   get the value of hasAction (i.e individuals of the class Action in the OntoReg ontology) */
            ArrayList<String> actionIndList = ontoReg.listDataPropertyValues(taskInd, "hasAction");
            /*  avoids the null pointer exceptions   */
            if (actionIndList == null){
//                    actionIndList.add("");
            }
            /*  every action individual is interpreted  ontologically */
            for (String actionInd: actionIndList){
                OntologicalConcept ocAction = new OntologicalConcept(actionInd, ontoReg);
                actionList.addAll(ocAction.getAllConcepts());
            }
            /*   get the value of hasPatient (i.e. patients) */
            ArrayList<String> patientIndList = ontoReg.listDataPropertyValues(taskInd, "hasPatient");
            if (patientIndList == null){
                patientIndList.add("");
            }
            /*  every patient is interpreted ontologically */
            for (String patientInd: patientIndList){
                OntologicalConcept ocSubject = new OntologicalConcept(patientInd, ontoReg);
                subjectList.addAll(ocSubject.getAllConcepts());
            }
            /* add the interpreted eintities (i.e.subject and action annotations) to the task statement */
            taskStmt.setId(taskInd);
            taskStmt.setActAnnList(actionList);
            taskStmt.setSubAnnList(subjectList);
            /*  add the task statement to the statement list */
            taskStmtList.add(taskStmt);
       }
    }
    return taskStmtList;
}

private ArrayList<Regulation> interpreteRegList(ArrayList<String> regIndList){

    /*  interprete each regulation and add it to the regulation list  */
     for (int i=0; i< NO_OF_REG ; i++){
        if (i< regIndList.size()){
            String regInd = regIndList.get(i);
            Regulation reg = new Regulation();

            /*  interprete each statement within the regulation  */
            ArrayList<String> stmtIndList = semReg.listDataPropertyValues(regInd, "hasStatement");
            /*    */
            for (String stmtInd: stmtIndList){
                Statement regStmt = new Statement();
                ArrayList<String> actionList = new ArrayList<String>();
                ArrayList<String> subjectList = new ArrayList<String>();
                String description = Converter.listToString(semReg.listDataPropertyValues(stmtInd, "description"),",");
                /* get the value of hasAction (i.e actions) */
                ArrayList<String> actionIndList = semReg.listDataPropertyValues(stmtInd, "hasAction");
                if (actionIndList== null){
                    actionIndList.add("");
                }
                /*   every action is interpreted ontologically */
                for (String actionInd: actionIndList){
                    OntologicalConcept ocAction = new OntologicalConcept(actionInd, semReg);
                    actionList.addAll(ocAction.getAllConcepts());
                }
                /*  get the values of hasSubject (i.e subjects)  */
                ArrayList<String> subjectIndList = semReg.listDataPropertyValues(stmtInd, "hasSubject");
                if (subjectIndList.isEmpty()){
                    subjectIndList.add("");
                }
                /*  every subject is interpreted ontologically   */
                for (String subjectInd: subjectIndList){
                    OntologicalConcept ocSubject = new OntologicalConcept(subjectInd, semReg);
                    subjectList.addAll(ocSubject.getAllConcepts());
                }
                /*  add the interpreted eintities (i.e.subject and action annotations) to the statement */
                regStmt.setId(stmtInd);
                regStmt.setActAnnList(actionList);
                regStmt.setSubAnnList(subjectList);
                regStmt.setDescription(description);
                /*  add the statement to the regulation  */
                reg.setId(regInd);
                reg.getStmtList().add(regStmt);
            }
            /* add the regulation to the regulation list  */
            regRegList.add(reg);
        }
    }
    return regRegList;
}

private double calculateListScore(ArrayList<String> xList, ArrayList<String> yList){
    double score = 0;
    double newScore = 0;
    for (String x: xList){
        for(String y: yList){
            newScore = calculateScore(x, y);
            if (newScore> score){
                score = newScore;
            }
        }
    }
    return score;
}


private double calculateScore(String x, String y){
    double score = 0;
    if (x != null & !x.equals("")){
        Thesaurus t1 = new Thesaurus(x);
        Thesaurus t2 = new Thesaurus(y);
        score = calculateThesaurusMatchingScore(t1, t2);
    }
    return score;
}

/**
 *  calculates the score based on the distance from the root word
 * @param t1 is the thesaurus of the first word
 * @param t2 is the thesaurus of the second word
 * @return the calculated score.
 */
private double calculateThesaurusMatchingScore(Thesaurus t1, Thesaurus t2){
    double score = 0;
    double newScore = 0;

    /*  compares root word with others*/
    if (t1.getRootWord().equalsIgnoreCase(t2.getRootWord())){
        newScore = ROOT_SCORE + ROOT_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (t1.getSearchWord().equalsIgnoreCase(t2.getSearchWord())){
        newScore = ROOT_SCORE + ROOT_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (isInList(t1.getRootWord(), t2.getSynonyms())){
        newScore = ROOT_SCORE + SYNO_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (isInList(t1.getRootWord(), t2.getHyponyms())){
        newScore = ROOT_SCORE + HYPO_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (isInList(t1.getRootWord(), t2.getHypernyms())){
        newScore = ROOT_SCORE + HYPER_SCORE;
        if (newScore> score){
            score = newScore;
        }
    /*  compares synonyms with others*/
    }else if (isInList(t1.getSynonyms(), t2.getRootWord()) ){
        newScore = SYNO_SCORE + ROOT_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (listsMatched(t1.getSynonyms(), t2.getSynonyms())){
        newScore = SYNO_SCORE + SYNO_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (listsMatched(t1.getSynonyms(), t2.getHyponyms())){
        newScore = SYNO_SCORE + HYPO_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (listsMatched(t1.getSynonyms(), t2.getHypernyms())){
        newScore = SYNO_SCORE + HYPER_SCORE;
        if (newScore> score){
            score = newScore;
        }
    /*  compares hyponyms with others*/
    }else if (isInList(t1.getHyponyms(), t2.getRootWord() )){
        newScore = HYPO_SCORE + ROOT_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (listsMatched(t1.getHyponyms(), t2.getSynonyms())){
        newScore = HYPO_SCORE + SYNO_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (listsMatched(t1.getHyponyms(), t2.getHyponyms())){
        newScore = HYPO_SCORE + HYPO_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (listsMatched(t1.getHyponyms(), t2.getHypernyms())){
        newScore = HYPO_SCORE + HYPER_SCORE;
        if (newScore> score){
            score = newScore;
        }
    /*  compares hypernyms with others*/
    }else if (isInList(t1.getHypernyms(), t2.getRootWord() )){
        newScore = HYPER_SCORE + ROOT_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (listsMatched(t1.getHypernyms(), t2.getSynonyms())){
        newScore = HYPER_SCORE + SYNO_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (listsMatched(t1.getHypernyms(), t2.getHyponyms())){
        newScore = HYPER_SCORE + HYPO_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }else if (listsMatched(t1.getHypernyms(), t2.getHypernyms())){
        newScore = HYPER_SCORE + HYPER_SCORE;
        if (newScore> score){
            score = newScore;
        }
    }
    return score;
}

/**
 * checks if any of the words in one list matches with any of the words in the other list.
 * @param xList is a list to be matched
 * @param yList is the other list to be matched with.
 * @return a boolean value of the result.
 */
private boolean listsMatched(ArrayList<String> xList, ArrayList<String> yList){
    /* represents whether the match found   */
    boolean found = false;
    /*   each string in the first list is checked with each string in the second list */
    for (String x: xList){
           if (isInList(x, yList)){
               found = true;
               break;
           }
    }
    return found;
}

/**
 *  checks if a word is in a list
 * @param xList is the list where the word will be checked with.
 * @param y is the word to be checked
 * @return a boolean value of the result.
 */
private boolean isInList (ArrayList<String> xList, String y){
    return isInList( y, xList);
}

/**
 *  checks if a word is in a list
 * @param x is the word to be checked
 * @param yList is the list where the word will be checked with.
 * @return a boolean value of the result.
 */
private boolean isInList(String x, ArrayList<String> yList){
    /* represents whether the match found   */
    boolean found = false;
    for (String y: yList){
        if (!x.equals("")&& !y.equals("")){
            if (x.equalsIgnoreCase(y)){
                found=true ;
               break ;
            }
        }
    }
    return found;
}

}

