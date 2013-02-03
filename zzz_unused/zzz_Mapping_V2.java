/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import gate.util.Out;
import java.util.ArrayList;
import api.ont.IOntologyFiles;
import api.ont.IOntology;
import api.ont.Jena_Ontology;
import helper.reg.Regulation;
import helper.reg.Statement;
import helper.util.Converter;
import helper.util.Print;
import helper.util.Splitter;
import api.wn.Wordnet;

/**
 * maps the regulations with the validation tasks in the OntoReg ontology
 *  - uses SemReg to get the annotations of regulations
 * - uses hasAction and hasPatient  of validation tasks to get the action and subjects of the task repectively.
 *  - uses all the classes and superclasses of the action and patient to get more meaning
 *  - uses wordnet to get root words, synonyms, hypernyms and hyponyns.
 * @author Krishna Sapkota
 */
public class zzz_Mapping_V2 implements IOntologyFiles{

    /*  using ontologies and wordnet  */
    private IOntology semReg;
    private IOntology ontoReg;
    private Wordnet wn ;

    /* a list of names of regulations (i.e. regulation id)    */
    private ArrayList<String> regList;
    /*  a list of names of validation tasks (i.e task id)  */
    private ArrayList<String> taskList;
    /*   a list of java classes to hold subject and action annotations of validation tasks  */
    private ArrayList<Statement> taskStmtList;
    /*  a list of java classes to hold subject and action annotations of regulations  */
    private ArrayList<Regulation> regStmtList;

    private String[] removableClasses = {"Thing","Resource","PhysicalConcepts",
                                 "AbstractConcepts","ProcessDomain",
                                 "RegulatoryDomain", "Action"};
   
    public zzz_Mapping_V2() {

        /*  initialise ontologies  */
        ontoReg = new Jena_Ontology(path, ontoFile, ontoPrefix);
        semReg = new Jena_Ontology(path, semFile, semPrefix);
        /*  initialise the wordnet   */
        wn = new Wordnet();

        /*  initialise the other attributes   */
        regList = new ArrayList<String>();
        taskList = ontoReg.getIndsOfTask();
        regStmtList = new ArrayList<Regulation>();
        taskStmtList = new ArrayList<Statement>();

        /*  execute the whole operation  */
        execute();
        Out.prln("*** MAPPING  COMPLETED *** ");
    }

/**
 *  executes the whole mapping operations
 *  - it integrates all the methods to perform mapping
 */
private void execute(){

    /*  the prefixes or and sr represent OntoReg and SemReg respectively */
    ArrayList<String> orRegList = ontoReg.getIndsOfRegulation();
    ArrayList<String> srRegList = semReg.getIndsOfRegulation();

    /* 1. for each regulation in OntoReg, find the equivalent in SemReg */
    Print.prln("\n 1. for each regulation in OntoReg, find the equivalent in SemReg, -----------------");
     Print.prln("Processing ...");
    for (String orReg: orRegList){
        if( isInList(orReg, srRegList)){
            regList.add(orReg);
        }
    }

    /* finds the actions and subjects and populates the relevant java classes */
    taskStmtList = interpreteTaskList(taskList);
    regStmtList = interpreteRegList(regList);

    /*   counts the no. of matches  */
    int matchCounter = 0;

    /*  2. for each regulation, check whether it is matched with the validation tasks */
    Print.prln("\n 2. for each regulation,-------------");
    for (Regulation clsReg: regStmtList){
        
        /* 2.1 check with each validation task, if  the rgulation matches   */
        for (Statement clsTask: taskStmtList){
//            Print.prln("\n 2.1 check with each validation task, "+ clsReg.getId() +" if it matches, -----------------");
            /*   matching is represented in scores.
                            200 or above means matched, 100 means either subject or action is matched. */
            int matchingScore = getMatchScore(clsReg,clsTask);
            if( matchingScore>=200){
                matchCounter ++;
                Print.prln(matchCounter +".  "+ clsReg.getId() + " ** MATCHED ** "+ clsTask.getId());
                /*   add matched regulation and validation task in the OntoReg ontology */
                ontoReg.addObjectStatement(clsReg.getId(), "necessitatesTaskCandidates", clsTask.getId());
            }else if (matchingScore==100){
//                    Print.prln(clsReg.getId()+ " NOT matched "+ clsTask.getId());
            }
        }
    }
    ontoReg.saveOntology();
}

/**
 *  checks the regulation with the validation task if they are matched and returns a score.
 * @param clsReg regulation represented in a java class.
 * @param clsTask validation task represented in a java class.
 * @return the score in integer value.
 */
private int getMatchScore(Regulation clsReg, Statement clsTask){

    /*   represents whether the subject is matched */
     boolean subjectMatched = false;
     /*   represents whether the action is matched */
     boolean actionMatched = false;
     /*  represents the subject score  */
     int subjectScore = 0;
     /*   represents the action score */
     int actionScore = 0;
     /*    represents the total score*/
     int totalScore = 0;

     /*  computes and represents the subject annotations of the validation task   */
    ArrayList<String> taskSubList = clsTask.getSubAnnList();
    /*   computes and represents the action annotations of the validation task */
    ArrayList<String> taskActList = clsTask.getActAnnList();

    /*  a regulation may contain multiple statements. It checks each statement with the validation task  */
    for (Statement regStmt: clsReg.getStmtList()){
        /*  computes and represents the subject annotations of the statement  */
        ArrayList<String> regSubList = regStmt.getSubAnnList();
        /*  computes and represents the action annotations of the statement  */
        ArrayList<String> regActList = regStmt.getActAnnList();

        /*  assigns scores if subject match found  */
        if (matchFound(regSubList, taskSubList)){
            subjectMatched = true;
            subjectScore = 100;
        }
        /*   assings scores if action match found */
        if (matchFound(regActList, taskActList)){
            actionMatched = true;
            actionScore = 100;
        }
    }
    /*  computes the total match score  */
    totalScore = subjectScore + actionScore;
//    if (totalScore>=100){
//        Print.prln("subject matched = "+subjectMatched +" , action matched = "+actionMatched);
//    }
    return totalScore;
}

//TODO create a score depending on the level of word matched (e.g. direct synonym,hypernym, etc)
/**
 *  checks two lists of strings if any string between them is matched.
 * @param xList the first list to be checked matching
 * @param yList the second list to be checked for matching
 * @return whether or not the match found
 */
private boolean matchFound(ArrayList<String> xList, ArrayList<String> yList){
    /* represents whether the match found   */
    boolean found = false;
    /*   each string in the first list is checked with each string in the second list */
    for (String x: xList){
       if (isInList(x, yList)){
          found = true;
       }
    }
    return found;
}
/**
 *  checks if the text (string) is in the list of texts (string).
 * @param text to be checked if it is in the list.
 * @param textList is the list where the text is checked.
 * @return whether or not the text is found in the list
 */
private boolean isInList(String text, ArrayList<String> textList){
    /*   represtnts whether the text is found in the list */
    boolean found = false;
    /*  it is essential to lower the case in both the strings  */
    String text1 = text.toLowerCase();
    /*  for each text in the text list, perform the checking  */
    for (String text2: textList){
        text2 = text2.toLowerCase();
       
        if (!text1.equals("")&& !text2.equals("")){
//            Print.prln("text = "+text+" text2= "+text2);
            if (text1.equals(text2)||text1.contains(text2)||text2.contains(text1)){
                found=true ;
//                Print.prln(text2+ " found");
               break ;
            }
        }
    }
    return found;
}
/**
 * Purpose: computes the subject and action annotations of the validation task.
 * interpretes the meaning of the validation task. It uses the semantic web technologies.
 * it uses the OntoReg ontology and traces the values of  hasPatient and hasAction object properties and
 * infers the the classes the values belong to. It utilises wordnet to get the synonyms of the words.
 * @param taskList is the list of the validation task to be interpreted.
 * @return subject and action annotations organised into a list of java classes.
 */
private ArrayList<Statement> interpreteTaskList(ArrayList<String> taskList){

    /*  interprete each validation task and add it to thes task list */
    for (String strTask: taskList){
        Statement clsStmt = new Statement();

        /* ACTION:
                 *      i. get the value of hasAction (i.e actions)
                 *      ii. get all the classes of each action
                 *      iii. remove the unwanted classes
                 *      iv.  split the class names
                 *      v. remove unwanted part of the class names
                 *      vi. get all the synonyms, hypernyms and hyponyms of the class names
                 */

        /*   i. get the value of hasAction (i.e individuals of the class Action in the OntoReg ontology) */
        ArrayList<String> actionIndList = ontoReg.listDataPropertyValues(strTask, "hasAction");
        /*  avoids the null pointer exceptions   */
        if (actionIndList == null){
                actionIndList.add("");
        }
        /*  will hold the interpreted list of words for the action   */
        ArrayList<String> actionList = new ArrayList<String>();

        /*  every action individual is interpreted  */
        for (String action: actionIndList){
            /* ii. get all the classes of  each action  */
            ArrayList<String> actionClassList = ontoReg.listClasses(action);
            /* iii. remove the unwanted classes */
            actionClassList.removeAll(Converter.arrayToArrayList(removableClasses));

            /*  for each class and superclass the action individual belongs to  */
            for (String actionClass: actionClassList){
                /*  iv. split the class names */
                actionClass = Splitter.splitCamelCase(actionClass);
                /* v. reove the unwanted part of the class names*/
                actionClass.replace("individual", "");
                actionClass.replace("module", "");
                if (!actionClass.equals("")&& actionClass!=null){
                    /* vi. get all the synonyms, hypernyms and hyponyms */
                    actionList.addAll(wn.getRootList(actionClass));
                }
            }
        }

        /* SUBJECT:
                 *      i. get the value of hasPatient (i.e patients)
                 *      ii. get all the classes of  each patient
                 *      iii. remove the unwanted classes
                 *      iv.  split the class names
                 *      v. remove unwanted part of the class names
                 *      vi. get all the synonyms, hypernyms and hyponyms of the class names
                 */

        /*  i. get the value of hasPatient (i.e. patients) */
        ArrayList<String> patientIndList = ontoReg.listDataPropertyValues(strTask, "hasPatient");
        if (patientIndList == null){
            patientIndList.add("");
        }
        ArrayList<String> subjectList = new ArrayList<String>();

        /*    */
        for (String patient: patientIndList){
            /* ii. get all the classes of each action */
            ArrayList<String> patientClassList = ontoReg.listClasses(patient);
            /* iii. remove the unwanted classes */
            patientClassList.removeAll(Converter.arrayToArrayList(removableClasses));

            /*    */
            for (String patientClass: patientClassList){
                /*  iv. split the class names */
                patientClass = Splitter.splitCamelCase(patientClass);         
                /* v. reove the unwanted part of the class names*/
                patientClass.replace("individual", "");
                patientClass.replace("module", "");    
                if (!patientClass.equals("")&& patientClass!=null){
                    /* vi. get all the synonyms, hypernyms and hyponyms */
                    subjectList.addAll(wn.getAllList(patientClass));
                }
            }
        }

//        Print.prln("\n === ValidationTask ===");
//        Print.prln(strTask+ " -- hasAction --> "+ Converter.listToString(actionList2));
//        Print.prln(strTask+ " -- hasPatient --> "+ Converter.listToString(subjectList2));

        /* add the interpreted eintities (i.e.subject and action annotations) to the task statement */
        clsStmt.setId(strTask);
        clsStmt.setActAnnList(actionList);
        clsStmt.setSubAnnList(subjectList);

        /*  add the task statement to the statement list */
        taskStmtList.add(clsStmt);
    }
    return taskStmtList;
}

private ArrayList<Regulation> interpreteRegList(ArrayList<String> regList){

    /*  interprete each regulation and add it to the regulation list  */
    for (String strReg: regList){
        Regulation clsReg = new Regulation();
        /*  interprete each statement within the regulation  */
        ArrayList<String> stmtList = semReg.listDataPropertyValues(strReg, "hasStatement");

        /*    */
        for (String strStmt: stmtList){
            Statement clsStmt = new Statement();

          /* ACTION:
                     *      i. get the value of hasAction (i.e actions)
                     *      ii.. get all the root word and synonyms of the actions  */

            /*  i. get the value of hasAction (i.e actions) */
            ArrayList<String> actionIndList = semReg.listDataPropertyValues(strStmt, "hasAction");
            if (actionIndList== null){
                actionIndList.add("");
            }

            /*    */
            ArrayList<String> actionList = new ArrayList<String>();

            /*   ii.. get all the root word and synonyms of the actions */
            for (String actionInd: actionIndList){
                actionList.addAll(wn.getRootList(actionInd));
            }

          /* SUBJECT:
                     *      i. get the values of hasSubject (i.e subjects)
                     *      ii.. get all the root word and synonyms of the subjects */
            /* i. get the values of hasSubject (i.e subjects)  */
            ArrayList<String> subjectIndList = semReg.listDataPropertyValues(strStmt, "hasSubject");
            if (subjectIndList.isEmpty()){
                subjectIndList.add("");
            }
            /* ii.. get all the root word and synonyms of the subjects  */
            ArrayList<String> subjectList = new ArrayList<String>();

            /*    */
            for (String subjectInd: subjectIndList){
                subjectList.addAll(wn.getRootList(subjectInd));
            }

//            Print.prln("\n === Regulation ===");
//            Print.prln(strStmt+ " -- hasAction --> "+ Converter.listToString(actionList2));
//            Print.prln(strStmt+ " -- hasSubject --> "+ Converter.listToString(subjectList2));

            /*  add the interpreted eintities (i.e.subject and action annotations) to the statement */
            clsStmt.setId(strStmt);
            clsStmt.setActAnnList(actionList);
            clsStmt.setSubAnnList(subjectList);

            /*  add the statement to the regulation  */
            clsReg.setId(strReg);
            clsReg.getStmtList().add(clsStmt);
        }

        /* add the regulation to the regulation list  */
        regStmtList.add(clsReg);
    }
    return regStmtList;
}
}

