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


import helper.compliance.IndividualViolation;
import helper.compliance.Violation;
import helper.gui.*;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.*;
import rcm.main.Settings;

/**
 * Use this frame as a gui template for each frame I create in this project.
 * @author Krish
 */
public class UIViolationDetails extends RcmFrame {

    /** Creates new form RcmExtendedFrame */
    public UIViolationDetails(String indName, IndividualViolation iv) {
        this.iv = iv;
        init();
        process(indName);
        finish();         
    }
    private void init(){
        initComponents();
        tpContent.setDocument(document);
    }
    private void finish(){    
        setVisible(true);
    }
    private void process(String indName){    
        style.strong("Violation details of ");style.h1(indName+" \n\n");
        ArrayList<Violation> vList = iv.getIndViloations(indName);
        for (Violation v: vList){
            String vid = v.getId();
            String type = v.getType();
            String prop = v.getProperty();
            String expected = v.getExpectedValue();
            String actual = v.getActualValue();
            String reason = v.getReason();
            String solution = v.getSolution();
           
            style.strong("Property: ");style.h3(prop+"\n");
            style.strong("ID: ");style.normal(vid+ "\n");
            style.strong("Type: ");style.normal(type+ "\n");
            style.strong("Expected: ");style.normal(expected+ "\n");
            style.strong("Actual: ");style.normal(actual+ "\n");
            style.strong("Reason: ");style.normal(reason + "\n");
            style.strong("Solution: ");style.normal(solution + "\n"); 
            style.normal("\n\n");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        pnHeader = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tpContent = new javax.swing.JTextPane();
        btnClose = new javax.swing.JButton();

        jScrollPane1.setViewportView(jTextPane1);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnHeaderLayout.setVerticalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(tpContent);

        btnClose.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnClose.setText("Close");
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(336, 336, 336)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
         dispose();
    }//GEN-LAST:event_btnCloseMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnHeader;
    private javax.swing.JTextPane tpContent;
    // End of variables declaration//GEN-END:variables
    private String titleOnLabel = " Compliance Violation Details";    
    private IndividualViolation iv;
    
    //  styling similar to css in an external file.
    TextStyles style = new TextStyles();
    StyledDocument document = style.getDocument();

}