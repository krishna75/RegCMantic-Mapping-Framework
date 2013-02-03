//
//package rcm.phase4;
//
//import Test.Settings;
//import helper.gui.RcmExtendedFrame;
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.TextArea;
//import java.awt.event.ActionEvent;
//import javax.swing.JPanel;
//import ontology.OntologyModel;
//import ontology.jena.JenaOntModel;
//import java.awt.Label;
//import java.util.ArrayList;
//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import reg.Employee;
//import reg.Regulation;
//import reg.RegulationEntity;
//import reg.Report;
//import reg.Task;
//
///**
// * @author Krishna Sapkota
// * Created on 28-Apr-2010, 08:51:21
// */
//
//// later it will be an abstract class
//public  class EntityChecker extends RcmExtendedFrame{
//    TextArea                taReg;
//    JPanel                  centerPanel;
//    JButton                 btnShow;
//    static final String     SHOW   =   "show" ;
//    static final String     CANCEL =   "cancel";
//    GridBagConstraints      c;
//    static int              openFrameCount = 0;
//    static final int        xOffset = 30, yOffset = 30;
//    static int              layoutRow = 0;
//
//
//    public EntityChecker(){
//        super();
//
//
//        //Set the window's location.
//        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
//        setLayout(new BorderLayout());
//        //setLocationRelativeTo(this);
//        setTitle("Compliance Checker");
//
//        addCenter();
//        addNorth();
//        addSouth();
//        addEast();
//        addWest();
//
//        //Display the window.
//        setVisible(true);
//    }
//
//
//   // public abstract void actionPerformed(ActionEvent e) ;
//
//     private void addCenter(){
//       centerPanel    =   new JPanel();
//       centerPanel.setLayout(new GridBagLayout());
//       c = new GridBagConstraints();
//       add("Center",centerPanel);
//      
//    }
//
//    private void addNorth(){
//         // create four panels to add in the dialog
//        JPanel northPanel   =   new JPanel();
//        Label lblNorth= new Label("The result will be shown below");
//        lblNorth.setFont(Settings.HEADING_FONT);
//        northPanel.add(lblNorth);
//        add("North",northPanel);
//    }
//
//    private void addSouth(){
//        JPanel southPanel = new JPanel();
//        btnShow = new JButton("Show");
//        btnShow.addActionListener( this);
//        btnShow.setActionCommand(SHOW);
//        southPanel.add(btnShow);
//
//        JButton btnCancel = new JButton("Cancel");
//        btnCancel.addActionListener( this);
//        btnCancel.setActionCommand(CANCEL);
//        southPanel.add(btnCancel);
//        add("South",southPanel);
//    }
//
//    private void addEast(){
//        JPanel eastPanel    =   new JPanel();
//        add("East",eastPanel);
//    }
//
//    private void addWest(){
//        JPanel westPanel    =   new JPanel();
//        add("West",westPanel);
//    }
//
//    public void checkAndShowCompliance(){
//        String emps = "";
//        String tasks = "";
//        String regs = "";
//
//        //checking regulation ***********************************************
//        ArrayList<Regulation> allReg = model.getAllRegulations();
//        for (Regulation reg: allReg){
//            String tabSpace = " ";
//            pCaption("Checking regulation "+reg.getInd());
//            
//            //checking employee ***********************************************
//            ArrayList<String> allEmp = reg.getRespEmployees();
//            if (allEmp.isEmpty()){ // if there are no responsible employees
//                pWrong(tabSpace + "There are no assigned employees. Please assign someone");
//            }else {                
//                for (String emp: allEmp){
//                    Employee emp1 =  model.getEmployeeByInd(emp);
//                    emps += emp1.getForename()+ " " + emp1.getSurname()+", ";                       
//                }
//                pNormal(tabSpace + "Responsible employees are "+ emps);
//            }
//
//            //checking task ***************************************************
//            ArrayList<String> allTask = reg.getHasTasks();
//            if (allTask.isEmpty()){ // if no tasks are assigned
//                pWrong(tabSpace + "No tasks are assigned.");
//            } else {
//                 pNormal(tabSpace + "The assigned tasks are "+ allTask.toString());
//                for (String task: allTask){
//                    Task tsk = model.getTaskByInd(task);
//                    pCaption2("Checking "+task+ "................");
//                    tabSpace = "     ";
//
//                    if (tsk.getAchieved()==0){// 0 means not completed yet, 1 means completed
//                        pWrong(tabSpace + tsk.getInd()+ " has not been completed."+reg.getInd()+" is not compliant. Responible employees are " + emps);
//                    }else { // if the task is completed
//
//                        //checking report ***********************************************
//                        ArrayList<String> allReport = tsk.getHasReport();
//                        if (allReport.isEmpty()){ // if there are no reports
//                            pWrong(tabSpace + "No reports are created.");
//                        }else { // if there are reports associated with this task
//                            pNormal(tabSpace + "  The associated reports are are "+ allReport.toString());
//                            for (String report: allReport){
//                                Report rpt = model.getReportByInd(report);
//                                pCaption2(tabSpace + "Checking "+report + "................");
//                                tabSpace = tabSpace + "        ";
//
//                                if (rpt.getTaskResult()==0){
//                                    pWrong(tabSpace + "The task result is not supplied");
//                                }else if (rpt.getTaskResult()<tsk.getLoLimit()){
//                                    pWrong(tabSpace + "The task result is lower than its lower limit "+tsk.getLoLimit());
//                                }else if (rpt.getTaskResult()>tsk.getUpLimit()){
//                                    pWrong(tabSpace + "The task result is higher than its upper limit "+tsk.getUpLimit());
//                                }else {
//                                    pNormal(tabSpace + "Everything is in compliance with " + reg.getInd());
//                                }
//                            }
//
//                        }
//                    }
//
//                }
//               
//            }
//
//            ;
//
//        }
//    }
//
//////       public void allRegEntityPrinter(ArrayList<? extends RegulationEntity> regEntities){
//////        for (RegulationEntity regEntity: regEntities){
//////            regEntityPrinter(regEntity);
//////        }
//////    }
//////
//////   public abstract void regEntityPrinter(RegulationEntity regEntity);
//
//    public  void p(String prop, String value){
// 
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx = 0.5;
//        c.gridx = 1;
//        c.gridy = layoutRow;
//        centerPanel.add(new JLabel(prop), c);
//
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx = 0.5;
//        c.gridx = 2;
//        c.gridy = layoutRow;
//        if (value.length()<50 || value.equals(null)){
//            centerPanel.add(new JTextField(value), c);
//        }   else {
//            JTextArea area = new JTextArea(3,20);
//            area.setText(value);
//            area.setLineWrap(true);
//            area.setWrapStyleWord(true);
//            JScrollPane scrollPane = new JScrollPane(area);
//            centerPanel.add(scrollPane,c);
//        }
//
//        layoutRow++;
//        pack();
//       
//
//    }
//
//    public void pCaption(String caption){
//        //Displays caption if there are more than one records
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx = 2;
//        c.gridx = 2;
//        c.gridy = layoutRow;
//        JLabel lblCaption = new JLabel(caption);
//        lblCaption.setFont(Settings.CAPTION_FONT);
//        lblCaption.setBackground(Color.GRAY);
//        lblCaption.setForeground(Color.WHITE);
//        lblCaption.setOpaque(true);
//        centerPanel.add(lblCaption, c);
//        layoutRow ++;
//        pack();
//    }
//
//    public void pCaption2(String caption){
//        //Displays caption if there are more than one records
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx = 2;
//        c.gridx = 2;
//        c.gridy = layoutRow;
//        JLabel lblCaption = new JLabel(caption);
//        lblCaption.setFont(Settings.CAPTION2_FONT);
////        lblCaption.setBackground(Color.GRAY);
////        lblCaption.setForeground(Color.WHITE);
////        lblCaption.setOpaque(true);
//        centerPanel.add(lblCaption, c);
//        layoutRow ++;
//        pack();
//    }
//
//    public void pNormal(String text){
//
//
//        //Displays right image
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx = 2;
//        c.gridx = 1;
//        c.gridy = layoutRow;
//        JLabel lblRight = new JLabel();
//        lblRight.setIcon(Settings.RIGHT_IMAGE);
//        centerPanel.add(lblRight, c);
//
//
//        //Displays caption if there are more than one records
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx = 2;
//        c.gridx = 2;
//        c.gridy = layoutRow;
//        JLabel lblNormal = new JLabel(text);
//        //lblNormal.setFont(Settings.CAPTION_FONT);
//        centerPanel.add(lblNormal, c);
//        layoutRow ++;
//        pack();
//    }
//
//    public void pWrong(String text){
//
//        //Displays wrong image
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx = 2;
//        c.gridx = 1;
//        c.gridy = layoutRow;
//        JLabel lblWrongImage = new JLabel();
//        lblWrongImage.setIcon(Settings.WRONG_IMAGE);
//        centerPanel.add(lblWrongImage, c);
//        
//        //Displays caption if there are more than one records
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx = 2;
//        c.gridx = 2;
//        c.gridy = layoutRow;
//        JLabel lblWrong = new JLabel(text);
//        lblWrong.setForeground(Color.RED);
//        centerPanel.add(lblWrong, c);
//        layoutRow ++;
//        pack();
//    }
//
//        public void actionPerformed(ActionEvent e) {
//            String comm = e.getActionCommand();
//            if (comm.equals(SHOW)) {
//              btnShow.setVisible(false);
//             checkAndShowCompliance();
//            }
//            if (comm.equals(CANCEL)) {
//               this.dispose();
//            }
//
//    }
//
//}
