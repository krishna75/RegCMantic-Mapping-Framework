/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase32.mapping;

import api.ont.JenaAbstractOntology;
import api.wn.ISimilarity;
import api.wn.LinSimilarity;
import gate.util.Out;
import helper.mapping.*;
import helper.util.Print;
import helper.util.Validator;
import java.util.ArrayList;
import rcm.phase31.pre.DifferenceTable;
import rcm.phase31.pre.RegTaskCollector;

/**
 * maps the regulations with the validation tasks in the OntoReg ontology
 *  - uses SemReg to get the annotations of regulations
 * - uses hasAction and hasPatient  of validation tasks to get the action and subjects of the task respectively.
 *  - uses all the classes and superclasses of the action and patient to get more meaning
 *  - uses wordnet to get root words, synonyms, hypernyms and hyponyns.
 * @author Krishna Sapkota
 */
public class ThreeScoresGenerator extends JenaAbstractOntology{
    //local files uses
    private RegTaskCollector collector = new RegTaskCollector();
    private ThreeScores threeScores = new ThreeScores();
    private ArrayList<ThreeScores> tsList = threeScores.getThreeScoresList();

    private DifferenceTable diffTable;
    private ISimilarity sim;    
    private int mappingId = 0;

    /*   a list of java classes to hold subject and action annotations of validation tasks and regulations  */
    private ArrayList<MappingRegulation> regList;
    private ArrayList<MappingTask> taskList;
    private static final double DIFF_ACCEPTING_SCORE = 0.5;    
    
    /* for testing purpose only. */ 
    int test = 0;
    
public ThreeScoresGenerator() {
        init();
        
        /* seperate process for testing  a mapping */
        if (test == 1){
            test();
        }else{
           process();
           finish(); 
        }
}

private void init(){
    Print.prln("ThreeScoresComputer initialized...");
    
        /*  initialise the wordnet similarity and other classes  */
        diffTable = new DifferenceTable();
        diffTable.readFile();
        sim = new LinSimilarity();
        regList = collector.getRegList();
        taskList = collector.getTaskList(); 
}
private void finish(){
        threeScores.generateMappingId();
        threeScores.write();
        Out.prln("*** THREE SCORES COMPUTATION  COMPLETED *** ");
}

private void test(){
    // Eudralex_5.26_1 Eudralex_5.21_1
    // StartingMaterialPurchase_1  FilterCleaningTask
    // pair 1
    String stmtId = "Eudralex_8.14_3";
    String taskId = "InvestigationTask_1";
    // pair 2
    String stmtId1 = "Eudralex_5.21_1";
    String taskId1 = "FilterCleaningTask";
    
    // pair 3
    String stmtId2 = "Eudralex_5.26_1";
    String taskId2 = "StartingMaterialPurchase_1";
    computeThreeScores(stmtId, taskId);
    computeThreeScores(stmtId1, taskId1);
    computeThreeScores(stmtId2, taskId2);
}

// used only for test() method 
private void computeThreeScores(String stmtId, String taskId){
    // process stmt
    MappingStmt stmt = new MappingStmt();
    for (MappingRegulation reg2: regList){
        boolean found = false;
        for (MappingStmt stmt2: reg2.getStmtList()){
            if (stmt2.getId().equals(stmtId)){
                stmt = stmt2;
                found = true;
                break;
            }
        }
        if (found){
            break;
        }
    }
    
    // process task
    MappingTask task = new MappingTask();
    for (MappingTask t: taskList){
        if (t.getId().equals(taskId)){
            task = t;
            break;
        }
    }
    
    double t = computeTopic(stmt, task);
    double c = computeCore(stmt, task);
    double a = computeAux(stmt, task);
    Print.prln("stmt="+stmtId + ", task="+taskId);
    Print.prln("topic="+t+", core="+c+", aux="+a);
}
/**
 *  executes the whole mapping operations
 *  - it integrates all the methods to perform mapping
 */
private void process(){

    int counter = 0;
    /*   for each regulation, check whether it is matched with the validation tasks */
    for (MappingRegulation reg: regList){
        String reg_id = reg.getId();

         /*  a regulation may contain multiple statements. It checks each statement with the validation task  */
         for (MappingStmt stmt: reg.getStmtList()){
             String stmt_id = stmt.getId();

            /*  check with each validation task, if  the rgulation matches   */
            for (MappingTask task: taskList){
                String task_id = task.getId();
                double topicScore = 0.0;
                double coreScore = 0.0;
                double auxScore = 0.0;
                counter ++;
                
                // compute scores
                topicScore = computeTopic(stmt,task);
                coreScore = computeCore(stmt,task);
                auxScore = computeAux(stmt,task);    
                
                ThreeScores ts = new ThreeScores();
                tsList.add(ts);
                ts.setRegId(reg_id);
                ts.setStmtId(stmt_id);
                ts.setTaskId(task_id);
                ts.setTopicScore(topicScore);
                ts.setCoreScore(coreScore);
                ts.setAuxScore(auxScore);
                Print.prln(counter + "stmtid= "+ stmt_id+", task_id = "+task_id+", Scores = "+topicScore +", "+coreScore+", "+auxScore);            
            }
             
        }

    }
}

private double computeCore(MappingStmt stmt, MappingTask task){
    double score = 0.0;
    MappingCore core = stmt.getCore();
    ArrayList<String> coreSubList = core.getSubList();
    ArrayList<String> coreActList = core.getActList();
    ArrayList<String> taskSubList = task.getSubList();
    ArrayList<String> taskActList = task.getActList();
    double subScore = calculateHighestScore(coreSubList, taskSubList);
    double actScore = calculateHighestScore(coreActList, taskActList);
    score = (subScore + actScore)/2;
    return score;
}

private double computeTopic(MappingStmt stmt, MappingTask task){
    double score = 0.0;
    AbstractMappingEntity topic= stmt.getTopic();
    ArrayList<String> topicAnnList = topic.getAnnList();
    ArrayList<String> taskAnnList = task.getAnnotationList();
    score = calculateBowAverageScore(topicAnnList, taskAnnList);
    return score;
}

private double computeAux(MappingStmt stmt, MappingTask task){
    double score = 0.0;
    AbstractMappingEntity aux= stmt.getAux();
    ArrayList<String> auxAnnList = aux.getAnnList();
    ArrayList<String> taskAnnList = task.getAnnotationList();
    score = calculateBowAverageScore(auxAnnList, taskAnnList);
    return score;
}



private double calculateHighestScore(ArrayList<String> xList, ArrayList<String> yList){
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

// the average is concerned with xList and is used for BoW
private double calculateBowAverageScore(ArrayList<String> xList, ArrayList<String> yList){
    double score = 0;
    for (String x: xList){
            score = score + calculateHighestScore(x, yList);                        
    }
    //todo: do not add task size to compute average
    score = score/(xList.size());
    return score;
}

private double calculateHighestScore(String x, ArrayList<String> yList){
    double score = 0;
    double newScore = 0;
        for(String y: yList){
            newScore = calculateScore(x, y);            
            if (newScore> score){
                score = newScore;
            }
        }
    return score;
}


private double calculateScore(String x, String y){
    double score = 0;
    if (Validator.isValidString(x) & Validator.isValidString(y)){
       
       // checking in difference table, if the concepts are defined as  separate concepts. 
       if (diffTable.getDifference(x, y)<DIFF_ACCEPTING_SCORE) {
            score = sim.getSimilarity(x, y);
       }
    }
    return score;
}
}

