
package zzz_unused;

import zzz_unused.zzz_MappingData;
import javax.swing.table.AbstractTableModel;
import helper.util.Print;

/**
 *
 * @author Krishna Sapkota, 15-Nov-2011,   21:08:22
 * A PhD project at Oxford Brookes University
 */
public class MappingTableModel extends AbstractTableModel {
        private String[] columnNames = new zzz_MappingData().getColumnNames();
        private Object[][] data= new zzz_MappingData().getDataArrays();

        public MappingTableModel(){
            zzz_MappingData mData = new zzz_MappingData();
            columnNames = mData.getColumnNames();
            data= mData.getDataArrays();
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
//        Print.prln("row is "+row +" colum is "+col);
//             if (col == 9 ){
//                showDetail( row);
//            }
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

    private void showDetail(int row){
        Print.prln(row+", is the row ");
        for(int i=0; i<=8;i++){
            Object o = getValueAt(row, i);
            Print.pr("  "+o +", ");
        }
        Print.prln("  ");
    }

}
