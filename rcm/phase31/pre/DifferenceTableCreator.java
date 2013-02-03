/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase31.pre;

import api.ont.JenaAbstractOntology;
import helper.util.Converter;
import helper.util.MyWriter;
import helper.util.Print;
import java.util.ArrayList;
import rcm.main.Settings;
import rcm.phase31.pre.DifferenceTable;

/**
 *
 * @author Krishna Sapkota,  Jul 11, 2012,  4:20:32 PM
 * A PhD project at Oxford Brookes University
 */
public class DifferenceTableCreator extends JenaAbstractOntology {
    
    /* Local Variables */
    DifferenceTable table = new DifferenceTable();
    ArrayList<DifferenceTable> tableList = table.getDifferenceTableList();
    
    double DIFF_SCORE = 1.0;
    
    public DifferenceTableCreator() {
        process();
        finish();
    }
    private void finish(){
        write();
    }
    
    private void process(){        
        String[] concepts = {"Substance","EquipmentModule","Position","Document"};
        ArrayList conceptList = Converter.arrayToArrayList(concepts);       
        for (Object o: conceptList){
            computeDiff(o.toString());
        }
    }
    
    private void computeDiff(String concept){
            Print.prln(concept);
            ArrayList<String> diffList = ontoReg.listDisjointClasses(concept);
            ArrayList<String> subList = ontoReg.listSubClasses(concept); 
            
            // C1   --vs.--  C2 , subclasses (C2)
            for (String diff: diffList){
                  DifferenceTable dt1 = new DifferenceTable();
                  dt1.setTermOne(concept);
                  dt1.setTermTwo(diff);
                  dt1.setDifference(DIFF_SCORE);
                  tableList.add(dt1); 
                
                //  subclass(C1)  --vs.--  C2 , subclasses (C2)
                for (String sub: subList){
                    DifferenceTable dt2 = new DifferenceTable();
                    dt2.setTermOne(sub);
                    dt2.setTermTwo(diff);
                    dt2.setDifference(DIFF_SCORE);
                    tableList.add(dt2); 
                }
            }
            
            // recursive
            for (String sub: subList){
                if (!sub.contains("Nothing")){
                    computeDiff( sub);
                }
            }
    }
    
     
     private void write(){
        table.makeTableUnique();
        table.generateId();
        table.writeFile(); 
         
     }
                
    

}
