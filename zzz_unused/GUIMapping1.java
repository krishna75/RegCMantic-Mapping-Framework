
package zzz_unused;


import gate.util.Out;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import rcm.main.Settings;
import helper.style.StyleBody;
import helper.style.StyleHead;
import helper.util.MyWriter;

import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.phase1._3_StructurePredictor;
import helper.gui.RcmFrame;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import helper.reg.Paragraph;
import helper.util.Converter;
import helper.util.MyReader;
import api.xml.XmlWriter;
import helper.util.RegEx;
import helper.style.HeadBody;
import helper.util.Print;
import helper.util.Sorter;
import helper.util.Splitter;

/**
 *  This class saves the extracted text in an XML file and some xml related files.
 *  It requires preprocessed structure predictor serialised object or instantiation
 *  of the structure predictor class.
 * @author Krishna Sapkota 04-Jan-2011  22:28:46
 */
public class GUIMapping1 extends RcmFrame {

    private ArrayList<StyleHead> headList;
    private ArrayList<StyleBody> bodyList;
    private ArrayList<JComboBox> comboList;
    private ArrayList<String> structureList ;
    private ArrayList<String> lastStructureList;
    private boolean processed;
    private ArrayList<String> removableList;

    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel centerPanel;
    private Label lblHeading;
    private JTable table;
    private JButton btnCancel;
    private JButton btnProcess;
    



public GUIMapping1()  {
    //super("Structure Selector");
    init();

    // adds the components
    setLayout(new BorderLayout());
    addCenter();
    addNorth();
    addSouth();
    addEast();
    addWest();

    finish();
}
private void init(){
    comboList = new ArrayList<JComboBox>();
    removableList = new ArrayList<String>();
    removableList.add("page_no");
    removableList.add("other");
    removableList.add("ignored");
    eastPanel = new JPanel();
    westPanel = new JPanel();
    northPanel = new JPanel();
    southPanel = new JPanel();
    centerPanel = new JPanel();
    lblHeading = new Label();
    table = new JTable();
    btnCancel = new JButton("cancel");
    btnProcess = new JButton("process");
}

private void finish(){
    // displays the window of optimum size.
    setVisible(true);
    pack();
    Out.prln("*** STYLE SELECTOR GUI CREATION COMPLETED *** ");
}
/**
*  It  adds a panel at the central  area of the frame.
*/
private void addCenter(){
        table.setModel(new MappingTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {
                    return;
                }
            }
        });
        table.getColumnModel().getSelectionModel().
            addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {
                    return;
                }
            }
        });       
        table.setVisible(true);
        centerPanel.add(new JScrollPane(table));
        centerPanel.setVisible(true);
        add("Center",centerPanel);
    }    
/**
*  It  adds a panel at the north (upper) area of the frame.
*/
private void addNorth(){
        lblHeading.setText("Document Structure Selector");
        northPanel.add(lblHeading);
        add("North",northPanel);
    }   
/**
*  It  adds a panel at the south(bottom) area of the frame.
*/
private void addSouth(){
        btnProcess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             processDocument();
            }
        });        
        btnCancel.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        southPanel.add(btnProcess);
        southPanel.add(btnCancel);
        add("South",southPanel);
    }
/**
*  It  adds a panel at the east(right) area of the frame.
*/
private void addEast(){
        add("East",eastPanel);
    }
/**
*  It  adds a panel at the west(left) area of the frame.
*/
private void addWest(){
        add("West",westPanel);
    }

/*  ---------------|           POST ACTION  PROCESSES      | ----------------- */
/**
 * The newly selected structure list will be updated in the head list and the body list.
 * it updates the system with  the user selection. It should be used before writing  any files.
 */
private void processDocument(){
    processed = true;  
}
}