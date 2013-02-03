
package rcm.phase4;

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
public class OntoRegOrdering extends JenaAbstractOntology{

        private ArrayList<String> opTaskList = new ArrayList<String>(); // tasks and operation list (final product)
        private ArrayList<String> unitProcedureList= new ArrayList<String>(); // list of unit procedures only in a time order       
        private ArrayList<String> firstUnitProcedureList = new ArrayList<String>();// temporary , just to find out the first unit procedures.
        private String outFile = Settings.FILES_PATH+"validation_plan.txt";
        
    public OntoRegOrdering(){  
        createUPList();
        createOTList();
        sortOT();
        write();
    }
      
/**
  *  creates a list of unit procedures
  */   
   private void  createUPList(){
        findFirstUP();
        for (String firstUP: firstUnitProcedureList){  
           String currentUP = firstUP;
           addNextUP(currentUP);   
        }
   }
   
 /**
     *  finds the first unit procedures  ( the unit procedure that have nothing before it)
     */    
    private void findFirstUP(){
        ArrayList<String> pList = ontoReg.listIndividuals("Process");
        for (String p : pList){
            ArrayList<String> upList = ontoReg.listObjectPropertyValues(p, "hasUnitProcedure");
            // for each unit procedure
            for(String up: upList){
                // find the first unit procedure i.e. no inlet stream
                if ( ontoReg.getPropertyValuesCount(up, "hasInletStream")== 0){
                    firstUnitProcedureList.add(up);  
                    unitProcedureList.add(up);
                }
            }          
        }
    }
  
/**
 *  adds all the following unit procedure to the list
 * @param currentUP  is the current unit procedure
 */
   private void addNextUP(String currentUP){
       ArrayList<String> outletList = ontoReg.listObjectPropertyValues( currentUP, "hasOutletStream");
           for (String outlet: outletList){

                // finds the next unit procedure
                if (ontoReg.getPropertyValuesCount(outlet, "hasDownstreamPort") > 0){
                    String downPort = ontoReg.getObjectPropertyValue(outlet, "hasDownstreamPort");
                    String equip = ontoReg.getObjectPropertyValue(downPort, "isportOf");
                    String nextUP = ontoReg.getObjectPropertyValue(equip, "isEquipmentOf");

                    // check if this unit procedure is already in the ordered unit procedrue list, add to the list if the next unit procedure is  the previous one (repeating).
                    if ( nextUP != null && !RegEx.isInList(nextUP, unitProcedureList)) {
                        unitProcedureList.add(nextUP);        
                        addNextUP(nextUP); // recursive call
                    }
                }  
            } 
   }
 
 /**
    *  creates a list of operations and tasks (OT)
    */
   private void createOTList(){
       for (String up: unitProcedureList){
           
           ArrayList<String> oList = ontoReg.listObjectPropertyValues(up, "hasOperation");
           // tasks associated with operations
           for (String o: oList){
              ArrayList<String> tList = ontoReg.listObjectPropertyValues(o, "isPatientOf");
              for (String t: tList){
                  if (t != null && !t.equals("")){
                      opTaskList.add(t);
                  }
              }  
           }
           // tasks associated with equipments
           String equip = ontoReg.getObjectPropertyValue(up, "hasEquipment");
           ArrayList<String> tList = ontoReg.listObjectPropertyValues(equip, "isPatientOf");
           opTaskList.addAll(oList);
           opTaskList.addAll(tList);
       }
   }
   
 /**
    *  sorts the operations and tasks based on their time
    */
   private void sortOT(){
       ArrayList<String> timeList = new ArrayList<String>();
       ArrayList<String> otList = new ArrayList<String>();
       for (String ot: opTaskList){
           timeList.add(ontoReg.getObjectPropertyValue(ot, "hasTime"));
       }
       timeList = RegEx.removeEmpty(timeList);
       timeList = sortTimeList(timeList);
       for (String t: timeList){
           otList.add(ontoReg.getObjectPropertyValue(t, "correspondsTo"));
       }
       opTaskList = otList;
   }
   
/**
 *  sorts the list of times
 * @param timeList is the list of the time to be sorted
 * @return the sorted list of the times
 */
   private ArrayList<String> sortTimeList(ArrayList<String> timeList){
       boolean repeat = true;
       Object[] times = Converter.arrayListToArray(timeList);
       int counter = 0;
       while (repeat){
           counter++;
            repeat = false;
            for (int i=0 ; i<times.length-2 ; i++){
                boolean swap = false;
                String t1 = times[i].toString();
                String t2 = times[i+1].toString();
                
                // place order:  checks if the equipments are arranged one afer another in a place
                if(  isSpacialBefore(t2, t1)){
                        swap = true;            
                }
                if ( !swap){
                    // time order:  checks if one is after another in the ontology
                    for (int j=i+1;j<times.length-2; j++ ){
                        String t3 = times[j].toString();
                        if (t3.equals( ontoReg.getObjectPropertyValue(t1, "isAfter"))){
                                swap = true;
                                break;
                        }  
                    } 
                }
                
                if (swap){       
                    String temp1 = t1;
                    String temp2 = t2;
                    times[i]    = temp2;
                    times[i+1]  = temp1;
                    repeat = true;
                }
            }     
        }
       timeList = Converter.arrayToArrayList(times);
       RegEx.removeEmpty(timeList);
       return timeList;
   }
   
 /**
    *  determines whether a time is carried out before another time, based on the arrangement of the equipments in a space
    * @param time1 is a time to be compared
    * @param time2 is another time to be compared
    * @return  true is time1 is holds before time2
    */
   private boolean isSpacialBefore(String time1, String time2){
       boolean isBefore = false;
      
       String up1 = getUP(time1);
       String up2 = getUP(time2);
       int i1 = 0;
       int i2 = 0;
       for (String up: unitProcedureList){
           if (up.equals(up1)){
               i1= unitProcedureList.indexOf(up);
           }
           if (up.equals(up2)){
              i2= unitProcedureList.indexOf(up);
           }  
       }
       if (i1<i2){
           isBefore = true;
       }
       return isBefore;
   }
   
 /**
    *  determines the unit procedure of a time
    * @param time is the time, of which the unit procedure is determined
    * @return the unit procedure
    */
   private String getUP(String time){
       String up = "";    
       String x = ontoReg.getObjectPropertyValue(time, "correspondsTo");
       // unit procedure
       if (ontoReg.isIndOfClass(x, "UnitProcedure")){
           up = x;
       }
       // operation
       else if (ontoReg.isIndOfClass(x, "Operation")){
           up = ontoReg.getObjectPropertyValue(x, "isOperationOfUnitProcedure");
       }
       // validation task
       else if (ontoReg.isIndOfClass(x, "ValidationTask")){
           // a validation task may be an operation, equipment, document, third-party or substance.
           String y = ontoReg.getObjectPropertyValue(x, "hasPatient");
           if (ontoReg.isIndOfClass(y, "Operation")){
               up = ontoReg.getObjectPropertyValue(y, "isOperationOfUnitProcedure");
           }
           else if (ontoReg.isIndOfClass(y, "Equipment")){
               up = ontoReg.getObjectPropertyValue(y, "isEquipmentOf");
           }           
       }
       return up;
   }


/**
 *  writes the list of unit procedures, operations and the validation tasks
 */
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

}
