/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase33.evaluation;

import api.ont.JenaAbstractOntology;
import helper.mapping.Alignment;
import helper.util.MyWriter;
import java.util.ArrayList;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota,  Jul 27, 2012,  10:29:03 AM
 * A PhD project at Oxford Brookes University
 */
public class ExistingMappingCollector extends JenaAbstractOntology {
    
    /* IO Files*/
    String PHASE3 = Settings.FILES_PATH + "phases_files/PHASE3/";// remove PHASE3, its for temporary to work around the static final problemes 
    String file_path = PHASE3 + "3_evaluation/";
    String MAPPING_OUT_FILE = file_path +"existing_mapping.txt";
    String REG_OUT_FILE = file_path +"existing_regulations.txt";
    String TASK_OUT_FILE = file_path +"existing_tasks.txt";
    
    /* Local Variables */
    Alignment alignment = new Alignment();
    ArrayList<Alignment> alList;
    
    ArrayList<String> regList = new ArrayList<String>();
    ArrayList<String> taskList = new ArrayList<String>();
    

    public ExistingMappingCollector() {
        init();
        process();
        finish();
    }
    private void init(){
        alList = alignment.getAlignmentList();
    }
    private void finish(){
        alignment.write(MAPPING_OUT_FILE);
        MyWriter.write(regList, REG_OUT_FILE);
        MyWriter.write(taskList, TASK_OUT_FILE);      
    }
    private void process(){
       int counter = 0 ;
       
       //populates reg list
       regList = ontoReg.listIndividuals("Regulation");
       for (String reg: regList){
           ArrayList<String> tList = ontoReg.listObjectPropertyValues(reg, "necessitatesTask");
           for (String task: tList){
               counter ++;
               
               // pupulating  alignment list
               Alignment al = new Alignment();
               alList.add(al);
               al.setMappingId("em_"+ counter);
               al.setRegId(reg);
               al.setTaskId(task);
               
               // populates  task list
               taskList.add(task);
           }
           
       }
        
    }
    

    public ArrayList<Alignment> getAlList() {
        return alList;
    }

    public void setAlList(ArrayList<Alignment> alList) {
        this.alList = alList;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public ArrayList<String> getRegList() {
        return regList;
    }

    public void setRegList(ArrayList<String> regList) {
        this.regList = regList;
    }

    public ArrayList<String> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<String> taskList) {
        this.taskList = taskList;
    }

    
}
