/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rcm.phase4;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *  This table model is designed for the table in Compliance Checker.
 *  It takes result data  and converts the necessary data to fit in the table in  the class Compliance Checker.
 * @author Krishna Sapkota,  Jan 27, 2012,  5:53:29 AM
 * A PhD project at Oxford Brookes University
 */
public class ResultTableModel extends AbstractTableModel {
    // columns of a table
    private String[] columnNames  = {"Result ID","Complied","Regulation","Statement","Validation Task",
                                    "Employees","Completed","Report","Report Detail","Remark"};
    // rows of a table
    private Object[][] data ;
    
    // the data is obtained from the result data.
    private ResultData resultData;
    private ArrayList<ResultData> rdList;
    
    // 
    public ResultTableModel() {
        init();
        process();
    }
/**
 *  initializes the values.
 */    
    private void init(){
        resultData = new ResultData();
        resultData.readFile();
        rdList = resultData.getResultDataList();
    }
    
 /**
     *  fills the array of data from the result data.
     */
    private void process(){
            int cols = columnNames.length;
            int rows = rdList.size();
            int index = 0;
            data = new Object[rows][cols];
            for (ResultData rd1: rdList){
                Object[] row = new Object[cols];
                row[0]=rd1.getResultId();
                row[1]=rd1.isComplied();
                row[2] = rd1.getRegulation();
                row[3]= rd1.getStatement();
                row[4]= rd1.getTask();
                boolean employeeAssined = !rd1.getEmployeeList().isEmpty();
                row[5]= employeeAssined;
                row[6] = rd1.isCompleted();
                boolean isReported = !rd1.getReport().isEmpty();
                row[7] = isReported;
                row[8] = rd1.isReportDetailOK();
                row[9] = "see detail..";
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

    public ResultData getResultData() {
        return resultData;
    }

    public void setResultData(ResultData resultData) {
        this.resultData = resultData;
    }
    
    
    
}
