
package helper.gui;


import com.toedter.calendar.JCalendar;
import java.awt.Dimension;
import org.swixml.SwingEngine;

import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import zzz_unused.MappingTableModel;

/**
 *
 * @author Krishna Sapkota, 14-Nov-2011,   18:45:08
 * A PhD project at Oxford Brookes University
 */
public class SwixGUI {
   private int clicks;
   public JTextField tf;
   public JLabel cnt;
    
    public SwixGUI() {
        try {
            SwingEngine swix = new SwingEngine(this);
//            swix.getTaglib().registerTag("calendar", JCalendar.class);
//            swix.getTaglib().registerTag("table", JTable.class);
            swix.render("xml/MySwix.xml").setVisible(true);
            JTable table1 =  (JTable)swix.getIdMap().get("table1"); 
            table1.setModel(new MappingTableModel());
        } catch (Exception ex) {
            
        }
    }

    /** Action appends a '#' to the textfields content.  */
    public Action submit = new AbstractAction() {
            @Override
        public void actionPerformed( ActionEvent e ) {
            tf.setText( tf.getText() + '#' );
            cnt.setText(String.valueOf( ++clicks ));
        }
    };

    private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
//            output.append("ROW SELECTION EVENT. ");
//            outputSelection();
        }
    }

    private class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
//            output.append("COLUMN SELECTION EVENT. ");
//            outputSelection();
        }
}
}
