/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase4;

import helper.compliance.Violation;
import helper.util.Converter;
import helper.util.Print;
import java.util.ArrayList;
import rcm.phase34.ui._2_MappingData;

/**
 *
 * @author Krishna Sapkota,  Apr 5, 2012,  12:00:12 PM
 * A PhD project at Oxford Brookes University
 */
public class ViolationCollector {
    Violation violation;
    ArrayList<Violation> vList;
    _2_MappingData m ;
    ArrayList<_2_MappingData> amList ;

    public ViolationCollector() {
        init();
        process();
        finish();   
    }
    
    private void init(){
        violation = new Violation();
        vList = violation.getViolationList();
        m = new _2_MappingData();
        m.readFile();
        amList = m.getAcceptedList();
    }
    
    private void process(){
        collectViolations();
        attachRegulation();
        
    }
       
    private void finish(){
    violation.generateId();
    violation.writeFile();
}
    
    private void collectViolations(){
        Print.prln("size of accepted list = "+ amList.size());
        for (_2_MappingData m1: amList){
            String stmtId = m1.getStmtId();
            String taskId = m1.getTaskId();
            checkValidationTask(taskId);
        }
        //checkValidationTask("FilterCleaningTask");
    }
    
private void checkValidationTask(String indName){
    String props[] = {"hasPatient","hasAction","hasTime","hasWrittenProcedure",
                        "isResponsibilityOf","isTaskOf","hasReport","resultsIn"};
    ArrayList<String> pList = Converter.arrayToArrayList(props);
    for (String p: pList){
        Cardinality card = new Cardinality();
           vList.addAll( card.checkAllViolations(indName, p));            
    }
}

private void attachRegulation(){
    for (Violation v : vList){
        String t1 = v.getDomainInd();
        for (_2_MappingData m1: amList ){
            String t2 = m1.getTaskId();
            if (t1==t2){
                v.setRegulation(m1.getStmtId());
                v.setRegText(m1.getRegText());
            }
        }
    }   
}
    
    
    
    

}
