package rcm.phase4;

//standard imports
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

//needed for interaction with OWL
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;

//needed for interaction with jess
import edu.stanford.smi.protegex.owl.swrl.bridge.*; 

//needed for GUI
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import javax.swing.*;

//misc
import com.hp.hpl.jena.util.FileUtils;
import rcm.main.Settings;
//import sun.rmi.transport.proxy.CGIHandler;
 
public class OntoReg extends JPanel implements ActionListener {
    
     String CARDINALITY = "cardinality";
     String CONSISTENCY = "consistency";
     String VALIDATION_PLAN = "validation plan";
     
     JScrollPane scrollPane;
     String newline = "\n";
     JFileChooser fileChooser;
     JFrame frame  = new JFrame("OntoReg");
     Font boldFont = new Font("Serif", Font.BOLD, 18);
     Font plainFont = new Font("Serif", Font.PLAIN, 14);
     Border border1,border2,border3;
     public JMenuBar menuBar;
     public JMenu menu, submenu;
     public JMenuItem menuItem, sourceMenuItem;
     JRadioButtonMenuItem rbMenuItem;
     JCheckBoxMenuItem cbMenuItem;
     JSplitPane splitPane1,splitPane2;
     JScrollPane scrollPane1,scrollPane2;
     JTextArea textArea1,textArea2;
     JPanel mainPanel,bottomPanel,PicPanel;
     JButton refreshButton,incButton,validateButton;
     //the three text files for writing missing data, inconsistencies, and validation plan
     File file=new File(Settings.FILES_PATH+"cardinality_result.txt");
     File file2=new File("d:\\three files\\Inconsistent.txt");
     File file3=new File("d:\\three files\\ValReport.txt");
     boolean completed;
     public String s,fileName, ontoRegFilename;
     
     public OntoReg(){
         ontoRegFilename = Settings.ONTOLOGY_PATHNAME+Settings.ONTOREG_FILENAME;

     }
    
     //method that intearcts with Jess
     public void applyRules(String fileName){
         
    	 try{
    			FileInputStream fin = new FileInputStream(new File(ontoRegFilename)); 
    			OWLModel model=ProtegeOWL.createJenaOWLModelFromInputStream(fin);
    			SWRLRuleEngineBridge bridge = BridgeFactory.createBridge("SWRLJessBridge", model);
    			bridge.infer(); //this one statement transfers OWL+SWRL to Jess, infers, transfers back to OWL
    			Collection errors = new ArrayList();
                        
    			//saving OWL file after inferencing, alternatively one could using the same OWLModel for the rest of the code and then save OWL file at end 
//    		    ((JenaOWLModel) model).save(new File(fileName).toURI(), FileUtils.langXMLAbbrev, errors); 
//    		    System.out.println("File saved with " + errors.size() + " errors.");
    			}
    			catch(Exception err)
    			{
    				System.out.println("err:"+err);
    			}
     }
     //method that creates main panel of the GUI every time a menu option or button is selected
     public JPanel contentPane(String filePath){
         
         //this is to prevent multiple borders every time this method is called
         border1=null;
         border2=null;
         border3=null;
 
         // main panel
    	 mainPanel= new JPanel();
    	 mainPanel.setLayout(new GridLayout(3,1));
    	
         // scroll pane 1
    	 textArea1=new JTextArea();
         textArea1.setForeground(Color.RED);
    	 textArea1.setFont(plainFont);
         textArea1.setBackground(new Color(255,255,230));
         textArea1.setText("Loading file "+ ontoRegFilename+" ...");         
    	 border1 = BorderFactory.createTitledBorder(border1, "Missing Data and Cardinality violations",TitledBorder.LEFT, TitledBorder.ABOVE_TOP, boldFont,Color.BLUE);
    	 scrollPane1=new JScrollPane(textArea1);
         scrollPane1.setBorder(border1);
         mainPanel.add(scrollPane1); 
    	 
         // scroll pane 2
         textArea2=new JTextArea();
         textArea2.setFont(plainFont);
         textArea2.setBackground(new Color(255,255,230));
    	 border2 = BorderFactory.createTitledBorder(border2, "Inconsistencies",TitledBorder.LEFT, TitledBorder.ABOVE_TOP, boldFont,Color.BLUE);
    	 border3 = BorderFactory.createTitledBorder(border3, "Validation Plan",TitledBorder.LEFT, TitledBorder.ABOVE_TOP, boldFont,Color.BLUE);
    	 scrollPane2=new JScrollPane(textArea2);
         scrollPane2.setBorder(border2);     
         mainPanel.add(scrollPane2);
         
         // button panel   	 
    	 refreshButton=new JButton("Refresh Missing Data");
    	 refreshButton.addActionListener(this);
	 refreshButton.setActionCommand(CARDINALITY);
	 incButton=new JButton("Show Next Inconsistency");
    	 incButton.addActionListener(this);
	 incButton.setActionCommand(CONSISTENCY);
	 validateButton=new JButton("Show Validation Plan");
    	 validateButton.addActionListener(this);
	 validateButton.setActionCommand(VALIDATION_PLAN);
	 validateButton.setEnabled(false);//greys out or deactivates this button
         bottomPanel=new JPanel();
    	 bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,20));
    	 bottomPanel.add(refreshButton);
    	 bottomPanel.add(incButton);
    	 bottomPanel.add(validateButton);
    	 mainPanel.add(bottomPanel);
    	 
    	 mainPanel.repaint();
    	 return mainPanel;
     }
     
     
    @Override
     public void actionPerformed(ActionEvent e) { 
       String action =  e.getActionCommand();
    	 //this method if automatically called anytime a menu option or a button is selected, the ActionCommand is defined along with the component during time of definition
    	 if (action.equals(CARDINALITY)){
        	 getCardinality();
         }
    	 else if (action.equals(CONSISTENCY)){
    		 scrollPane2.setBorder(border2);//changing the border in case it was "Validation Plan"
    		 frame.validate();//validate() method resets the panel calling it along with all its subcomponents
    		 getConsistency();
         }
    	 else if (action.equals(VALIDATION_PLAN)){
    		 scrollPane2.setBorder(border3); //changing the border in case it was "Inconsistencies"
    		 frame.validate();
    		 getValidationPlan();
         }
    	 else {
    		 //a menu item made this call
    	 JMenuItem source = (JMenuItem)(e.getSource());
     	 final String menuText = source.getText(); 
     	 
    	 new Thread() {
    		 //a new thread is created to speed up the process...when this was removed, it was found that the GUI performance was quite slow
   		    public void run() {
    	 final JFileChooser fc = new JFileChooser();
    	 if (menuText.equals("Load Ontology"))
             
    	 {
    		        		  //Create a file chooser and set the default directory
//    		 				fc.setCurrentDirectory(new File("C:\\Documents and Settings\\labpsb\\OntoFromServer2"));       		  
//    		 				int returnVal = fc.showOpenDialog(OntoReg.this);  
                                                String filePath = fileName; //  Krishna added
//                                                filePath = fc.getSelectedFile().toString();
        		        	//  filePath=filePath.replaceAll("\", "\\");
        		        	  //frame.remove(PicPanel);
        		        	  frame.setContentPane(contentPane(filePath));
        		        	  frame.validate();
        		        	  getCardinality();
        		        	  frame.validate();
    	 }
    		         
    	if (menuText.equals("Exit")){
    		        	 frame.dispose();
    		             System.exit(0);
    	}
    	if (menuText.equals("Diagnose")){
    		        	 getConsistency();
    	 }
    		         
    		         
    		    }
    		  }.start(); //standard way to close a thread
    	 }
     }
    
        //method to interact with Pellet to diagnose the OWL ontology with logical inconsistencies if any
     void getConsistency(){
    	 try{
    		 applyRules(fileName);
    		 Explanations e=new Explanations();
    		 completed=e.Expl(fileName, file2); //returns 'true' if all inconsistencies have been repaired else returns 'false' 
    		 System.out.println("Written stuff in Inconsistent.txt");
    		 textArea2.read(new FileReader(file2), null);
    		 textArea2.setEditable(false);
    		 if (completed==true) validateButton.setEnabled(true); //activates Button 'Show Validation Plan' if all inconsistencies have been repaired
    		 else validateButton.setEnabled(false);
    		 frame.validate();
    	 }
    	 catch (Exception err)
    	 {
    		 System.out.println("File Handling Exception");
    	 }
     } 
    
    
    
     
     //method to detect all red-box errors or cardinality violations
     void getCardinality(){
    	try{
//    		 applyRules(fileName);//apply SWRL rules to generate any new knowledge
//    		 Cardinality c=new Cardinality(); //class defined in Cardinality.java
//    		 c.chkCardinality(fileName, file);
    		 System.out.println("Written stuff in Incomplete.txt");
    		 textArea1.read(new FileReader(file), null); //this method reads all the contents of the text file arguement and displays in the text area
    		 textArea1.setEditable(false);//disallows any user editing
    		
    	 }
    	 catch (Exception err)
    	 {
    		 System.out.println("File Handling Exception");
    	 }
     }
     
     //method to establish temporal order of processes and tasks 
     void getValidationPlan(){
    	try{
    	 OntoRegOrdering or=new OntoRegOrdering();//class defined in OntoRegOrdering.java
//    	 or.ordering(fileName, file3);
    	 textArea2.read(new FileReader(file3), null);
    	 textArea2.setEditable(false);
    	}
    	catch (Exception err)
   	 	{
   		 System.out.println("File Handling Exception");
   	 	}
     }
     

     
     //no particular reason this is a separate method
     void createGUI(){
		
    	 create();
    	 this.setSize(new java.awt.Dimension(211,137));
	
	 }
     
     //creates the GUI
     public void create(){
 		
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		 frame.setJMenuBar(createMenuBar()); //sets the menubar
	     frame.setSize(1200,800);
	     frame.setVisible(true); //needs to be explicitly stated
	     ImageIcon i=new ImageIcon("C:\\Documents and Settings\\labpsb\\My Documents\\OntoRegGUI.jpg");
	     frame.add(new JLabel(i));//image to be loaded on opening screen of GUI
	     frame.validate();
	}
	
	 public JMenuBar createMenuBar() {
	     	//The MenuBar has menuItems, Submenu, etc.,
	        //Create the menu bar.
	         menuBar = new JMenuBar();

	         //Build the "File" menu.
	         menu = new JMenu("File");
	         //Mnemonics are the shortCut keys to call those menuItems from keyBoard. Usaually its Contol and the key or ALt and the key (here its A)
	         menu.setMnemonic(KeyEvent.VK_A);
	         menu.getAccessibleContext().setAccessibleDescription(
	                 "The only menu in this program that has menu items");
	         menuBar.add(menu);
	         menu.addSeparator();
	         //a group of JMenuItems
	         menuItem = new JMenuItem("Load Ontology",
	                                  KeyEvent.VK_T);
	         //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
	         menuItem.setAccelerator(KeyStroke.getKeyStroke(
	                 KeyEvent.VK_1, ActionEvent.ALT_MASK));
	         menuItem.getAccessibleContext().setAccessibleDescription(
	                 "This doesn't really do anything");
	         menuItem.addActionListener(this);
	         menu.add(menuItem);
	         menu.addSeparator();
	         menuItem = new JMenuItem("Open File in Protege");
	         menuItem.addActionListener(this);
	         menu.add(menuItem);
	         menu.addSeparator();
	         
	         menuItem = new JMenuItem("Exit");
	         menuItem.setMnemonic(KeyEvent.VK_B);
	         menuItem.addActionListener(this);
	         menu.add(menuItem);
	         //For all the menuItems below only the actionListener is added.
	         
	        
	        

	         //Build "Validate" menu in the menu bar.
	         menu = new JMenu("Validate");
	         menu.setMnemonic(KeyEvent.VK_N);
	         
	         menuItem = new JMenuItem("Diagnose");
	         menuItem.addActionListener(this);
	         menu.add(menuItem);
	         menuBar.add(menu);
	         
//	       Build "Help" menu in the menu bar......right now this menu is unused
	         menu = new JMenu("Help");
	         menuItem = new JMenuItem("Welcome");
	         menuItem.addActionListener(this);
	         menu.add(menuItem);
	         menuItem = new JMenuItem("Help Content");
	         menuItem.addActionListener(this);
	         menu.add(menuItem);
	         menu.addSeparator();
	         menuItem = new JMenuItem("Tips");
	         menuItem.addActionListener(this);
	         menu.add(menuItem);
	         menuItem = new JMenuItem("About");
	         menuItem.addActionListener(this);
	         menu.add(menuItem);
	         menuBar.add(menu);
	         return menuBar;
	     }
	
	//and the one that starts it all :)
	public static void main (String[] args)
	{
		OntoReg e=new OntoReg();
		try
		{
			e.createGUI();
		}
		catch(Exception err){
			 System.out.println("Err:"+ err);
		 }
		
	}
}
