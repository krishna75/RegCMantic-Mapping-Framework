
package zzz_unused;

import helper.gui.RcmFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.swixml.SwingEngine;

/**
 *
 * @author Krishna Sapkota, 19-Nov-2011,   09:37:11
 * A PhD project at Oxford Brookes University
 */
public class zzz_MapDetails {
    public JLabel lblSouth1;
    public JLabel lblRegId;
    public JLabel lblRegSubject;
    public JLabel lblRegAction;
    public JProgressBar scoreBar ;
    public zzz_MapDetails() {
        init();   
        process();
        finish();
    }
    private void init(){
       // lblSouth1;    
    }
    private void process(){
        try {
            SwingEngine swix = new SwingEngine(this);
            swix.getTaglib().registerTag("RcmFrame", RcmFrame.class);
            swix.render("xml/MySwix.xml").setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(zzz_MapDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        scoreBar.setValue(90);
        lblRegId.setText("Eudralex3.1");
        lblRegSubject.setText("Equpement");
        lblRegAction.setText("clean");
        lblSouth1.setText("test");

    }
    private void finish(){

    }

}
