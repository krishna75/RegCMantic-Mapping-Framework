
package rcm.phase34.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import helper.util.Print;

/**
 *
 * @author Krishna Sapkota, 15-Nov-2011,   21:08:22
 * A PhD project at Oxford Brookes University
 */
public class _3_MappingTableModel extends AbstractTableModel {
   
    /* Local Variables */
    _2_MappingData mappingData;
    ArrayList<_2_MappingData> mappingDataList;
    
    String[] columnNames  = {"Mapping ID","Regulation","Validation Task",
                            "Subject Score","Action Score","Mapped","Detail"};
    Object[][] data ;
        
    public _3_MappingTableModel(){
        mappingData = new _2_MappingData();
        mappingData.readFile();
        mappingDataList = mappingData.getMappingList();
        fillArray();
    }

       public void fillArray(){
            int cols = columnNames.length;
            int rows = mappingDataList.size();
            int index = 0;
            data = new Object[rows][cols];
            for (_2_MappingData mappingData1: mappingDataList){
                Object[] row = new Object[cols];
                row[0]=mappingData1.getMappingId();
                row[1] = mappingData1.getStmtId();
                row[2]= mappingData1.getTaskId();
                row[3]= createBar(mappingData1.getSubjectScore());
                row[4]= createBar(mappingData1.getActionScore());
                row[5] = mappingData1.isAccepted();
                row[6] = "see detail..";
                data[index]= row;
                index ++;
            }
        }

    public JProgressBar createBar(double percentDone){
        JProgressBar progressBar = new JProgressBar(0,100);
        progressBar.setValue(Double.valueOf(percentDone*100).intValue());
        progressBar.setOrientation(SwingConstants.VERTICAL);
        progressBar.setForeground(Color.GRAY);
        return progressBar;
    }
    @Override
        public int getColumnCount() {
            return columnNames.length;
        }

    @Override
        public int getRowCount() {
            return data.length;
        }

    @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

    @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
    @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
    @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

   /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
        
    public _2_MappingData getMappingData() {
        return mappingData;
    }

    public void setMappingData(_2_MappingData mappingData) {
        this.mappingData = mappingData;
    }



}
