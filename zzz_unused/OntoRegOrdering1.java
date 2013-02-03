
package zzz_unused;

//to interact with OWL
import api.ont.JenaAbstractOntology;
import helper.util.Converter;
import helper.util.MyWriter;
import helper.util.Print;
import helper.util.RegEx;

// standard java imports
import java.awt.event.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.Collection;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Iterator;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;

//code to produce the validation plan, i.e. time order of processes and tasks
public class OntoRegOrdering1 extends JenaAbstractOntology{

        private ArrayList<String> opTaskList = new ArrayList<String>(); // tasks and operation list (final product)
        private ArrayList<String> unitProcedureList= new ArrayList<String>(); // list of unit procedures only in a time order       
        private ArrayList<String> firstUnitProcedureList = new ArrayList<String>();// temporary , just to find out the first unit procedures.
        
        private ArrayList<String> nextUnitProcedureList = new ArrayList<String>(); // useless
        private ArrayList<String> opsList = new ArrayList<String>();// useless ( completely unused)
        
	private int totalUnitProcedures = 0, unitProcedureCounter = 0;
        private String outFile = Settings.FILES_PATH+"validation_plan.txt";
        
    public OntoRegOrdering1(){
        ordering();
        write();
    }
        
    public void ordering(){
        ArrayList<String> pList = ontoReg.listIndividuals("Process");
        for (String p : pList){
            ArrayList<String> upList = ontoReg.listObjectPropertyValues(p, "hasUnitProcedure");
            totalUnitProcedures = upList.size();

            // for each unit procedure
            for(String up: upList){
                // find the first unit procedure i.e. no inlet stream
                if ( ontoReg.getPropertyValuesCount(up, "hasInletStream")== 0){
                    firstUnitProcedureList.add(up);
                    unitProcedureList.add(up);// upList
                    OpsAndTasks(up); // adds the operations of the first unit procedure  in the list
                    tasksAfter( ontoReg.getObjectPropertyValue(up, "hasTime")); // adds the tasks after the first unit procedures.
                }
            }
            unitList(firstUnitProcedureList);     
        }
    }
    
    //method that infers time order of processes and tasks given the starting processes Process[i] and how many of them:'start'
    public void unitList(ArrayList<String> aList){
        
        ArrayList<String> newList = new ArrayList<String>();
        //loops through each first unit procedures
        for (String unitProc: aList){
            ArrayList<String> outletList = ontoReg.listObjectPropertyValues( unitProc, "hasOutletStream");
            for (String outlet: outletList){
                
                // finds the next unit procedure
                if (ontoReg.getPropertyValuesCount(outlet, "hasDownstreamPort") > 0){
                    String downPort = ontoReg.getObjectPropertyValue(outlet, "hasDownstreamPort");
                    String equip = ontoReg.getObjectPropertyValue(downPort, "isportOf");
                    String nextUnitProc = ontoReg.getObjectPropertyValue(equip, "isEquipmentOf");
                    
                    // check if this unit procedure is already in the ordered unit procedrue list, add to the list if the next unit procedure is  the previous one (repeating).
                    if ( nextUnitProc != null && !RegEx.isInList(nextUnitProc, unitProcedureList)) {
                        unitProcedureList.add(nextUnitProc); // upList
                        newList.add(nextUnitProc);
                                              
//                        String time = ontoReg.getObjectPropertyValue(nextUnitProc, "hasTime"); 
//                        tasksBefore(time); 
//                        OpsAndTasks(nextUnitProc);   
//                        tasksAfter(time);//
                    }
                }                   
            }               
        }
        if (unitProcedureCounter <= totalUnitProcedures ){
            unitProcedureCounter++;
            unitList(newList);            
        }
    }
    
 /**
     *  Identifies the times before, its corresponding tasks and adds the tasks in the list.
     *  It also checks if the new task is already in the list. If   it is in the list,  the iteration stops.
     * @param time 
     */
    public void tasksBefore(String time){
        
        // temporary list to hold the tasks in Z-A order
        ArrayList<String> zaTaskList = new ArrayList<String>();                
        
        // finds all the times before the given time
        ArrayList<String> beforeTimeList = ontoReg.listObjectPropertyValues(time, "isImmediatelyAfter");
        for (String beforeTime: beforeTimeList){
            if ( beforeTime != null && !beforeTime.equals("TimeIntPreProcess")){
               String opTask = ontoReg.getObjectPropertyValue(beforeTime, "correspondsTo");
               
               // if this opTask is a validation task, add it to the list.
               if (opTask != null && ontoReg.isIndOfClass(opTask, "ValidationTask")){
                   if (!RegEx.isInList( opTask, opTaskList)){
                       zaTaskList.add(opTask);     
                       tasksBefore(beforeTime); // iterates until a duplicate task is found
                   }                   
               }             
            }            
        }  
        // converting the zaTaskList to A-Z time order, and adding to the opTaskList.
        for (int i=zaTaskList.size()-1 ; i>= 0; i--){
            String task = zaTaskList.get(i);
            if(!RegEx.isInList(task, opTaskList)){
                opTaskList.add(task);
            }
        }
    }

 /**
     *  Identifies the times after the given time, its tasks and adds the tasks in the list.
     *  It also checks if the new task is already in the list. If   it is in the list,  the iteration stops.
     * @param time 
     */
    public void tasksAfter(String time){
        
        // finds all the times after the given time
        ArrayList<String> afterTimeList = ontoReg.listObjectPropertyValues(time,"isImmediatelyBefore");
        for (String afterTime : afterTimeList){
            if ( afterTime != null && !afterTime.equals("TimeIntPostProcess")){      
                String afterTask =  ontoReg.getObjectPropertyValue(afterTime, "correspondsTo");
                
                // if this opTask is a validation task, add it to the list.
                if (afterTask != null && ontoReg.isIndOfClass(afterTask, "ValidationTask")){                     
                    if (!RegEx.isInList( afterTask, opTaskList)){
                        opTaskList.add(afterTask);
                        tasksAfter(afterTime); // iterates until a duplicate task is found
                    }                   
                }   
            }
        }
   }
        
//documenting operations and tasks related to operations within the unit procedure
//keeping track of all unit procedures for the next instant of time        
//method to find all operations in a unit procedure and tasks immediately after them
public void OpsAndTasks(String nextOpTask){
    String time = null, time1 = null; 

    //  If it is a unit procedure, it adds its operations to the list.
    if (ontoReg.isIndOfClass(nextOpTask, "UnitProcedure")){
        ArrayList<String> opList = ontoReg.listObjectPropertyValues(nextOpTask, "hasOperation");
        for (String op : opList){
            time = ontoReg.getObjectPropertyValue(op, "hasTime");
            
            // there is no time before (this is the starting time)
            if (ontoReg.getPropertyValuesCount(time, "isImmediatelyAfter")==0){
                addInList(op);
                time1 = time ;
            }
        }
    }  
    // if it is an operation and has time immidiately after, processes the tasks before and adds the operation in the list.
    else if (ontoReg.isIndOfClass(nextOpTask, "Operation")){ 
            time1 = ontoReg.getObjectPropertyValue(nextOpTask, "hasTime");
            if (ontoReg.getPropertyValuesCount(time1, "isImmediatelyAfter")>0){           
                    tasksBefore(time1);//finds all tasks before the operation
            }
            addInList(nextOpTask);
    }
    // if the given time is followed by any other times.
    if ( time1 != null && ontoReg.getPropertyValuesCount(time1, "isImmediatelyBefore")>0){
        tasksAfter(time1);
        
        // the taskAfter only deals with the task, if it is an operation, 
        ArrayList<String> afterTimeList  = ontoReg.listObjectPropertyValues(time1, "isImmediatelyBefore");
        for (String afterTime: afterTimeList){
            String afterOperation = ontoReg.getObjectPropertyValue(afterTime, "correspondsTo");
            
            // if next time corresponds to operation,  repeat the process, this time it will deal with the operation.
            if ( afterOperation != null && ontoReg.isIndOfClass(afterOperation, "Operation")){
                OpsAndTasks(afterOperation);
            }
        }
        
    }
}

private void write(){
    String txt ="\n";
    txt += "UNIT PROCEDURES \n";
    int upCounter = 1;
    txt += "_________________________________________________________________________________ \n";
    for (String unitProc: unitProcedureList){
        txt += " "+upCounter +". "+unitProc +"\n";
        upCounter++;
    }
    txt +="\n\n";
    txt += "OPERATIONS AND TASKS \n";
    txt += "_________________________________________________________________________________\n";
    int otCounter = 1;
    for (String opTask: opTaskList){
        txt += " "+ otCounter + ". "+opTask +"\n";
        otCounter++;
    }
    MyWriter.write(txt, outFile);
}


private void addInList(String opTask){
    if (! RegEx.isInList(opTask, opTaskList)){
                    opTaskList.add(opTask);
    }
}

}
