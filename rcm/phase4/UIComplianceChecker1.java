/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RcmExtendedFrame.java
 *
 * Created on 15-Dec-2011, 09:39:50
 */

package rcm.phase4;

import helper.gui.*;
import helper.util.Print;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import rcm.main.Settings;
import rcm.phase34.ui._5_UIMappingDetails;

/**
 *
 * @author Krish
 */
public class UIComplianceChecker1 extends RcmFrame {
    // variable declaration are below the generated codes.

    /** Creates new form RcmExtendedFrame */
    public UIComplianceChecker1() {
        init();
        process();
        setVisible(true);
    }
    
 /**
     *  initializes the objects and components values.
     */
    private void init(){
        Print.prln("ComplianceChecker initialised...");
        initComponents();     
        setCells();
    }
    
 /**
     *  all the processes are called from this method.
     */
    private void process(){
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e){
                int row=table.rowAtPoint(e.getPoint());
                int col= table.columnAtPoint(e.getPoint());
                //if (table.getColumnName(col).equalsIgnoreCase("Detail")){
                    showDetail(row, col);
               // }
        }});
    }
    
       private void showDetail(int row, int col){       
           String indName = (String)table.getValueAt(row, 0);
           new UIViolationDetails(indName, tableModel.getIndViolation());
       }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pnHeader = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btnCancel = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnHeader.setBackground(java.awt.Color.darkGray);

        lblImage.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        lblImage.setForeground(new java.awt.Color(204, 255, 204));
        lblImage.setHorizontalAlignment(JLabel.RIGHT);
        lblImage.setIcon(Settings.BANNER_IMAGE);

        lblTitle.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(204, 255, 204));
        lblTitle.setIcon(Settings.ICON_SMALL);
        lblTitle.setText(titleOnLabel);

        javax.swing.GroupLayout pnHeaderLayout = new javax.swing.GroupLayout(pnHeader);
        pnHeader.setLayout(pnHeaderLayout);
        pnHeaderLayout.setHorizontalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnHeaderLayout.setVerticalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
        );

        table.setModel(tableModel);
        jScrollPane1.setViewportView(table);

        btnCancel.setText("Cancel");
        btnCancel.setActionCommand("btnCancel");
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addGap(266, 266, 266))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancel)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        dispose();
    }//GEN-LAST:event_btnCancelMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnHeader;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
   
    private String titleOnLabel = " Compliance Checker";
    private ResultTableModel1 tableModel = new ResultTableModel1();
    private static final String IMG_TRUE = Settings.IMAGE_PATH + "true.png";
    private static final String IMG_FALSE = Settings.IMAGE_PATH + "false.png";       
    
/**
   *  sets the appearances of the table and its cells.
   */  
  private void setCells(){
        table.setRowHeight(40);
        table.setBackground(new java.awt.Color(0, 255, 204));
        
        // making result id column distinct.
        TableColumn vTask  = table.getColumn("ValidationTask");
        vTask.setCellRenderer(new TextCellRenderer());
        
        // making boolean cells (columns) to display custom object (i.e. check and cross marks)
        setBooleanColumn("Time");
        setBooleanColumn("WrittenProcedure");
        setBooleanColumn("ValidationPlan");
        setBooleanColumn("ValidationTest");
        setBooleanColumn("Responsibility");
    }
/**
   *  Coding Saver (Shorthand). 
   *  applies boolean cell renderer (custom built). 
   * @param columnName 
   */
  private void setBooleanColumn(String columnName){
      TableColumn col  = table.getColumn(columnName);
//      col.setMaxWidth(100);
      col.setWidth(150);
      col.setCellRenderer(new BooleanCellRenderer());
  }
  
/**
   *  modifies the appearance of a table cell whose value is text.
   */
    class TextCellRenderer implements TableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JTextArea t = new JTextArea();
            t.setText(value.toString());
            t.setBackground(Color.GRAY);
            t.setForeground(Color.white);
            t.setFont(new java.awt.Font("Georgia", 1, 14));
            return t;
        }
    }
    
 /**
     *  modifies the appearance of a table cell whose value is a boolean.
     */
    class BooleanCellRenderer implements TableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel lblImage = new JLabel();
            String image ="";
            if ((Boolean)value){
                image = IMG_TRUE;
            }else {
                image = IMG_FALSE;
            }
            ImageIcon imgIcon = new ImageIcon(image);  
            Image img = imgIcon.getImage();
            // image resize
            Image newimg = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);  
            ImageIcon newIcon = new ImageIcon(newimg); 
            lblImage.setIcon(newIcon);          
            return lblImage;
        }
    }  
}
