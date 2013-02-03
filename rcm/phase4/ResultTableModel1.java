/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rcm.phase4;

import helper.compliance.IndividualViolation;
import helper.compliance.Violation;
import helper.util.Print;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

/**
 *  This table model is designed for the table in Compliance Checker.
 *  It takes result data  and converts the necessary data to fit in the table in  the class Compliance Checker.
 * @author Krishna Sapkota,  Jan 27, 2012,  5:53:29 AM
 * A PhD project at Oxford Brookes University
 */
public class ResultTableModel1 extends AbstractTableModel {
    // columns of a table
    private String[] columnNames  = {"ValidationTask","Time","WrittenProcedure","Responsibility",
                                      "ValidationPlan","ValidationTest","Remark"};
    // rows of a table
    private Object[][] data ;
    
    // the data is obtained from the result data.
    private IndividualViolation indViolation;
    private Map vMap;
    
    // 
    public ResultTableModel1() {
        init();
        process();
    }
/**
 *  initializes the values.
 */    
    private void init(){
        indViolation = new IndividualViolation();    
        vMap = indViolation.getViolationMap();
    }
    
 /**
     *  fills the array of data from the result data.
     */
    private void process(){
            int cols = columnNames.length;
            int rows = vMap.size();
            int index = 0;
            data = new Object[rows][cols];
            Iterator<String> keyIter = vMap.keySet().iterator();
            while ( keyIter.hasNext()) {
                String key = keyIter.next();
                Object o = vMap.get(key);
                ArrayList<Violation> vList = (ArrayList<Violation>)o;
                //Print.prln("vList size = "+ vList.size());
                boolean  time = true, writtenProc = true, resp = true, vPlan =true, vTest = true;
                for (Violation v: vList){
                    String prop = v.getProperty();
                    Print.prln("property name  = "+ prop);
                    if (prop.equals("hasTime")){
                        
                        time = false;
                    }
                    if (prop.equals("hasWrittenProcedure")){
                        writtenProc = false;
                    }
                    if (prop.equals("isResponsibilityOf")){
                        resp = false;
                    }
                    if (prop.equals("isTaskOf")){
                        vPlan = false;
                    }
                    if (prop.equals("resultsIn")){
                        vTest = false;
                    }   
                }            
 
            Object[] row = new Object[cols];
            row[0]  = key;
            row[1]  = time;
            row[2]  = writtenProc;
            row[3]  = resp;
            row[4]  = vPlan;
            row[5]  = vTest;     
            row[6] = "see detail..";
            data[index]= row;
            index ++;
        }
}
    

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
       return columnNames.length;
    }
    
 @Override
        public String getColumnName(int col) {
            return columnNames[col];
 }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public IndividualViolation getIndViolation() {
        return indViolation;
    }

    public void setIndViolation(IndividualViolation indViolation) {
        this.indViolation = indViolation;
    }
    
    
    
}
