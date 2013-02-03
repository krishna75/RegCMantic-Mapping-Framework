
package zzz_unused;


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
import helper.util.Converter;
import helper.util.MyReader;
import api.xml.XmlWriter;
import helper.gui.RcmFrame;
import helper.util.RegEx;
import helper.style.HeadBody;
import helper.util.Print;
import rcm.phase1._3_StructurePredictor;

/**
 *  This class saves the extracted text in an XML file and some xml related files.
 *  It requires preprocessed structure predictor serialised object or instantiation
 *  of the structure predictor class.
 * @author Krishna Sapkota 04-Jan-2011  22:28:46
 */
public class StyleSelector_V3 extends RcmFrame implements ActionListener {
/*  gui button names */
    static final String SCHEMA  = "schema";
    static final String PROCESS = "process" ;
    static final String XML     = "xml";
    static final String CANCEL  = "cancel";

    static final String SELECT_TEXT  = "Select a document structure type";
    static final String PROCESS_TEXT = "Process Dcoument";
    static final String SCHEMA_TEXT  = "Create Schema";
    static final String XML_TEXT     = "Create XML";
    static final String CANCEL_TEXT  = "Cancel";

    /*  files name and path related fields */
    static final String STRUCTURE_FILENAME      = Settings.STYLES_PATH +"structures.txt";
    static final String LAST_STRUCTURE_FILENAME = Settings.STYLES_PATH +"recent_structures.txt";
    static final String SCHEMA_FILENAME         = Settings.STYLES_PATH +"reg_schema.xml";
    static final String XML_FILENAME            = Settings.STYLES_PATH +"reg_xml.xml";
    static final String XSL_FILENAME            = Settings.STYLES_PATH +"reg_xsl.xsl";
    static final String CSS_FILENAME            = Settings.STYLES_PATH +"reg_css.css";
    static final String PREDICTOR_FILENAME      = Settings.FILES_PATH+"structure_predictor.ser";
    static final String NEW_SCHEMA_FILE         = Settings.STYLES_PATH+"reg_xml.xsd";
    static final String TEMP_PATH               = Settings.GATE_FILES_PATH+"temp/";

    /*  about the details of the regulation document */
    static final String REGULATION_NAME = "Eudralex";
    static final String DESCRIPTION     = "EU regulation for the pharmaceutical industry";
    static final String REGULATION_BODY = "EMEA";
    static final String VERSION         = "1.0";
    static final String PUBLISHED_ON    = "2007";
    private ArrayList<StyleHead> headList;
    private ArrayList<StyleBody> bodyList;
    private ArrayList<JComboBox> comboList = new ArrayList<JComboBox>();
    private ArrayList<String> structureList = new ArrayList<String>() ;
    private ArrayList<String> lastStructureList = new ArrayList<String>() ;
    private boolean processed;

public StyleSelector_V3()  {
    super();

    /* reads a structure predicutor object from serialisation and provides head list and REGULATION_BODY list */
    _3_StructurePredictor predictor = (_3_StructurePredictor) MyReader.fileToObject(PREDICTOR_FILENAME);
    this.headList = predictor.getHeadList();
    this.bodyList = predictor.getBodyList();

    /* reads structure list and  last structure list from text files */
    getStructuresFromFile();
    getRecentStructuresFromFile();

    // sets the layout
    setLayout(new BorderLayout());
    setTitle("Structure Selector ");

    // adds the components
    addCenter();
    addNorth();
    addSouth();
    addEast();
    addWest();

    // displays the window of optimum size.
    setVisible(true);
    pack();
}
/**
*  It  adds a panel at the central  area of the frame.
*/
    private void addCenter(){
        JPanel panel = new JPanel();
        panel.setLayout(null);

        //  panel.setPreferredSize(new Dimension(1000,800));
        int vSpace = 25;
        int hSpace = 20;
        int height = 20;
        int x = 0;
        int y = 0;
        Iterator iterStructure = lastStructureList.iterator();
        for (StyleHead head: headList){

            // sets labels for levels
            String level = "Level "+ head.getStyleLevel();
            int length = level.length()*8;
             x = 50;
             y = y + 25;

            //adds labels
            int levelNo = headList.indexOf(head)+1;
            Label label= new Label(level + " : ");
            label.setName("label"+levelNo);
            label.setBounds(x, y, length, height);
            panel.add(label);

            // sets combo boxes for the selection
            JComboBox combo = new JComboBox();
            x = x + length + hSpace;
            String strCombo = SELECT_TEXT;
            combo.setName("combo"+levelNo);
            length = strCombo.length()*8;
            combo.addItem(strCombo);
            for (String str: lastStructureList){
                combo.addItem(str);
            }
            combo.addItem("other");
            combo.addItem("ignored");
            combo.setSelectedItem(head.getPredictedStructure());
            combo.setBounds(x, y, length, height);
            panel.add(combo);
            comboList.add(combo);

            // finds the first example text from the REGULATION_BODY
            int fontWeight=0;
            int fontStyle=0;
            int fontSize = head.getSize();
            String fontFamily = head.getFamily();
            if (head.getWeight().equals("bold")){
                fontWeight = Font.BOLD;
            }
            if (head.getStyle().equals("italic")){
                fontStyle = Font.ITALIC;
            }
            Font font = new Font(fontFamily,fontWeight+fontStyle,fontSize);
            String text1= " ";
            String text2= " ";
            int countText = 0;
            for (StyleBody body: bodyList){
                if ( body.getStyleLevel()==head.getStyleLevel()){
                    countText ++;
                    if (countText == 1){
                       text1 = body.getText();
                    }
                    if (countText == 2){
                        text2 = body.getText();
                    break;
                    }
                }
            }//for bodyList

            //adds label for first example text
            x = x + length + hSpace;
            Label lblExample1= new Label(text1);
            length = text1.length()*8;
            lblExample1.setBackground(Color.white);
            lblExample1.setFont(font);
            lblExample1.setBounds(x,y,length + (fontSize * 8),height );
            panel.add(lblExample1);

            //adds label for second example text
            y = y + vSpace ;
            Label lblExample2= new Label(text2);
            length = text2.length()*8;
            lblExample2.setBackground(Color.white);
            lblExample2.setFont(font);
            lblExample2.setBounds(x,y,length + (fontSize * 8),height  );
            panel.add(lblExample2);

        }//for headList

        // adding panel to the frame
        panel.setPreferredSize(new Dimension(x+500,y+50));
        add("Center",panel);
    }
    
/**
*  It  adds a panel at the north (upper) area of the frame.
*/
    private void addNorth(){
        JPanel northPanel   =   new JPanel();
        Label lblHeading= new Label("Document Structure Selector");
        northPanel.add(lblHeading);
        add("North",northPanel);
    }
    
/**
*  It  adds a panel at the south(bottom) area of the frame.
*/
    private void addSouth(){
        //creates panesl
        JPanel southPanel = new JPanel();

        //creates the process button
        JButton btnProcess = new JButton(PROCESS_TEXT);
        btnProcess.addActionListener(this);
        btnProcess.setActionCommand(PROCESS);
        southPanel.add(btnProcess);

        //creates the create schema button
        JButton btnCreateSchema = new JButton(SCHEMA_TEXT);
        btnCreateSchema.addActionListener(this);
        btnCreateSchema.setActionCommand(SCHEMA);
        southPanel.add(btnCreateSchema);

        //creates the create XML button
        JButton btnConvertToXML = new JButton(XML_TEXT);
        btnConvertToXML.addActionListener(this);
        btnConvertToXML.setActionCommand(XML);
        southPanel.add(btnConvertToXML);

        // creates cancel button
        JButton btnCancel = new JButton(CANCEL_TEXT);
        btnCancel.addActionListener(this);
        btnCancel.setActionCommand(CANCEL);
        southPanel.add(btnCancel);
        add("South",southPanel);
    }

/**
*  It  adds a panel at the east(right) area of the frame.
*/
    private void addEast(){
        JPanel eastPanel    =   new JPanel();
        add("East",eastPanel);
    }

/**
*  It  adds a panel at the west(left) area of the frame.
*/
    private void addWest(){
        JPanel westPanel    =   new JPanel();
        add("West",westPanel);
    }
/**
 * It loads a list of structure entity from a text file, and will be used to display
 * in combo boxes.
 */
    private void getStructuresFromFile(){
            structureList = MyReader.fileToArrayList( STRUCTURE_FILENAME);
    }

/**
* It loads a list of structure entity from a text file, and will be used to display
* in combo boxes.
*/
    private void getRecentStructuresFromFile(){
           lastStructureList = MyReader.fileToArrayList( LAST_STRUCTURE_FILENAME);
    }

/*  ---------------|           ACTION PERFORMED          | ----------------- */

/**
* when an action is performed the event is caught here to update the system.
* @param e
*/
    @Override
    public void actionPerformed(ActionEvent e) {
        String comm = e.getActionCommand();
        // if process button is pressed
        if (comm.equals(PROCESS)) {
             processDocument();
        }
        // if create schema button is pressed
        if (comm.equals(SCHEMA)) {
            processDocument();
            try {
                if (processed){
                    createSchema();
                }
            } catch (IOException ex) {
                Logger.getLogger(StyleSelector_V3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // if convert to XML button is pressed
        if (comm.equals(XML)){
            processDocument();
            try {
                if (processed){
                    writeSelectedList();
                    createXML();
                    createXSL();
                    createCSS();       
                }
            } catch (IOException ex) {
                Logger.getLogger(StyleSelector_V3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // closes the frame
        if (comm.equals(CANCEL)) {
           this.dispose();
        }
    }

/*  ---------------|           POST ACTION  PROCESSES      | ----------------- */

/**
 * it updates the system with  the user selection. It should be used before writing  any files.
 */
    private void processDocument(){
        processed = true;
        for (JComboBox combo: comboList){
                String structure = combo.getSelectedItem().toString();
                int index =comboList.indexOf(combo);
                if (structure.equals("Select a document structure type") ){
                    structure = "notSelected";
                    processed = false;
                    JOptionPane.showMessageDialog(null, "Level "+ (index+1)+ 
                          " is not selected !!! Please select all the options.",
                          "Selection Warning", JOptionPane.ERROR_MESSAGE);
                    break;

                }
                if ( structure.equals("ignore")){
                    structure = "ignored";
                }             
                    headList.get(index).setStructure(structure);
                    System.out.println(structure);                          
            }     
        for (StyleBody body: bodyList){
            for (StyleHead head: headList){
                if (body.getScore()== head.getScore()){
                    body.setStructure(head.getStructure());
                }
        }
       //removeIgnored();
    }
    }

    private void writeSelectedList(){
        ArrayList<String> selectedStrList = new ArrayList<String>();
        String fileName =  LAST_STRUCTURE_FILENAME;
        for (StyleHead head: headList){
            selectedStrList.add (head.getStructure());
        }
        new MyWriter().write(selectedStrList, fileName);
    }

/**
 * It is the definition of the  structure of the regulatory document based on the
 * font information.
 * @throws IOException
 */
    private void createSchema() throws IOException{
          String xmlContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?> \n" +
                  "<structure>\n";
          int level = 1;
          for (StyleHead head: headList){
               String str = head.getStructure();

              // creating xml tags
//              if (  !str.equals("ignored")){
                  xmlContent= xmlContent +
                     "  <"+ str+" level=\""+level+"\">\n"+
                        "       <style>"+ head.getStyle()+"</style>\n"+
                        "       <weight>"+head.getWeight()+" </weight>\n"+
                        "       <size>"+ head.getSize()+"</size>\n"+
                        "       <family>"+ head.getFamily()+ "</family>\n"+
                        "       <color>"+ head.getColor()+"</color>\n"+
                    "   </"+ str+">\n";
                  level++;
//               }//if
          }//for
          xmlContent = xmlContent + "</structure>";
          String fileName = SCHEMA_FILENAME;
          FileWriter writer = new FileWriter(new File(fileName));
          writer.write(xmlContent);
          writer.close();
          JOptionPane.showMessageDialog(null, "A schema file is created successfully !");
          openFile(fileName);
    }

    private void createXML(){      
        XmlWriter xml = new XmlWriter (XML_FILENAME);
        xml.startElement("document");
            xml.startElement("meta");
                xml.startElement("name"); xml.characters(REGULATION_NAME); xml.endElement("name");
                xml.startElement("description"); xml.characters(DESCRIPTION); xml.endElement("description");
                xml.startElement("body"); xml.characters(REGULATION_BODY); xml.endElement("body");
                xml.startElement("version"); xml.characters(VERSION); xml.endElement("version");
                xml.startElement("published_on"); xml.characters(PUBLISHED_ON); xml.endElement("published_on");
            xml.endElement("meta");
            xml.startElement("content");

      ArrayList<String> struList = selectStringList();
      HeadBody hb = selectLists();
      ArrayList<StyleHead> hList = hb.getHeadList();
      ArrayList<StyleBody> bList = hb.getBodyList();
      Stack<StyleHead> stack = new Stack<StyleHead>();
      Print.prln("head size ="+hList.size()+"  body size = "+bList.size());
      StyleHead current;
      StyleHead next;

      StyleBody b = bList.get(0);
      xml.startElement(b.getStructure());
      xml.startElement("text");xml.characters(b.getText());xml.endElement("text");
      current = headList.get(b.getStyleLevel()-1);
      stack.push(current);
      Print.prln("<"+current.getStructure());

      for (int i = 0; i<bList.size()-1;i++){
          StyleBody b1 = bList.get(i);
          StyleBody b2 = bList.get(i+1);
          int n1 = b1.getStyleLevel();
          int n2 = b2.getStyleLevel();
          String structure1 = b1.getStructure();
          String structure2 = b2.getStructure();
          String text1 = b1.getText();
          String text2 = b2.getText();

              /* Note: Higher the level no, the smallar the text
                            CASE 1: If the next style level is higer (smaller text) than
                             the current one, write start tag and text of the current level and
                             add the end tag to the stack */
              if (n1<n2){
                  next = headList.get(n2-1);
                   StyleHead running = current; // can be initialiezed with null

                  /* from previous level to current level -1 */
                  for ( int j = hList.indexOf(current)+1; j <hList.indexOf(next)-2; j++){

                      /* write start tags */
                      running = hList.get(j);
                      xml.startElement(running.getStructure());
                      Print.prln("<"+running.getStructure());

                      /* add each headstyle to the stack */
                      stack.push(running);
                  }// for

                  current = next;
                  xml.startElement(structure2);
                  Print.prln("<"+current.getStructure());//---------
                    xml.startElement("text"); xml.characters(text2); xml.endElement("text");
                  stack.push(current);
                  
              }// if n1<n2

              /* CASE 2: If the next style level is the same as the current one,
                             write start tag, text and end tag of the current. No stack involved */
              if (n1==n2){
                  xml.endElement(structure2);
                  Print.prln("..."+current.getStructure()+ ">");//---------
                  xml.startElement(structure2);
                    xml.startElement("text");xml.characters(text1);xml.endElement("text");
                    Print.prln("<"+current.getStructure()+ ".. ");//---------
                  }

              /* CASE 3: If the next style level is lower (bigger text) than the
                           current one, write start tag, text and end tag of the current level.
                           Add all required end tags from the stack. */
              if (n1>n2){
                  current = stack.pop();
                  xml.endElement(current.getStructure());
                  Print.prln("..."+current.getStructure()+ ">");//---------
                  next = headList.get(n2-1);
                  StyleHead running = current;

                  for (int j = hList.indexOf(current); j >hList.indexOf(next); j --){

                      /* write end tags */
                      running = stack.pop();
                      xml.endElement(running.getStructure());
                      Print.prln("..."+running.getStructure()+ ">");//---------
                  }
                  current = next;
                  stack.push(current);
                  xml.startElement(structure2);
                  Print.prln("<"+current.getStructure());//---------
                  xml.startElement("text");xml.characters(text2);xml.endElement("text");              
              }
      }// for each REGULATION_BODY
      while (!stack.empty()){ 
          xml.endElement(current.getStructure());
          Print.prln("..."+current.getStructure()+ ">");//---------
          current= stack.pop();
      }
      xml.endElement("content");
      xml.endElement("document");
      xml.write();
      openFile(XML_FILENAME);
    }

/**
 *  creates a xsl file to display xml file with styles.
 * @throws IOException
 */
    private void createXSL() throws IOException{
            String xslContent = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
            "<xsl:stylesheet version=\"1.0\"\n" +
            "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n\n" +

            "<xsl:template match=\"/\">\n" +
            " <html>\n" +
            " <head>\n" +
            "        <title>Regulatory Document : Annotated</title>\n" +
            "        <link rel=\"stylesheet\" type=\"text/css\" href=\"reg_css.css\"/>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "        <!-- meta information -->\n" +
            "        <xsl:for-each select=\"document/meta\">\n" +
            "        <div class=\"head\">\n" +
            "                <div class=\"name\"><xsl:value-of select=\"name\"/></div>\n" +
            "                <div class=\"description\">   <xsl:value-of select=\"description\"/></div>\n" +
            "                <div class=\"details\">\n" +
            "                        Version :  <xsl:value-of select=\"version\"/>\n" +
            "                        Published on : <xsl:value-of select=\"published_on\"/>\n" +
            "                        Body : <xsl:value-of select=\"body\"/>\n" +
            "                </div>\n" +
            "        </div>\n" +
            "        </xsl:for-each>\n\n" +

            "    <!-- content information -->\n"+
            "    <xsl:for-each select=\"document/body/*\">\n";
            for (StyleHead head: headList){
                   String str = head.getStructure();
                   String level = "level_"+head.getStyleLevel();
                  // creating xml tags
                      xslContent= xslContent +
                        "        <div class=\""+level+"\">" +
                        "                 " + "<xsl:value-of select=\"text\"/> " +
                        "        </div>\n"+
                        "        <xsl:for-each select=\"./*\">\n";
            }//for
            for (int i=0; i<headList.size();i++){
                xslContent = xslContent +
                        "       </xsl:for-each>\n";
            }
            xslContent = xslContent +
            "       </xsl:for-each>\n"+
            "  </body>\n" +
            "  </html>\n" +
            "</xsl:template>\n" +
            "</xsl:stylesheet>";

      String fileName = XSL_FILENAME;
      FileWriter writer = new FileWriter(new File(fileName));
      writer.write(xslContent);
      writer.close();
      //openFile(fileName);

    }
/**
 * creates a css file to render formating to xml file with the help of xsl.
 * @throws IOException
 */
    private void createCSS() throws IOException{
        String cssContent = "/* CSS Document */\n" +
                    ".head{\n"+
                    "text-align:center;\n"+
                    "background-color:#000000;\n"+
                    "color:#FFFFFF;\n"+
                    "margin-right:33%;\n"+
                    "margin-left:33%;\n"+
                    "padding: 5px 5px 5px 5px;\n"+
                    "border: 4px solid brown;\n"+
                    "}\n"+
                    ".name{\n"+
                    "font-size:36px;\n"+
                    "font-weight:bold;\n"+
                    "font-family:Verdana, Arial, Helvetica, sans-serif;\n"+
                    "}\n\n";
        for (StyleHead head: headList){
          cssContent = cssContent + ".level_"+ head.getStyleLevel() + "{\n"+
                  "font-style:"+head.getStyle() + ";\n"+
                  "font-weight:"+head.getWeight()+ ";\n"+
                  "font-size:"+head.getSize()+ ";\n"+
//                  "color:"+head.getColor()+ ";\n"+
                  "color:#000000;\n"+
                  "font-family:"+head.getFamily()+ ";\n"+
                  "}\n";
        }

      String fileName =  CSS_FILENAME;
      FileWriter writer = new FileWriter(new File(fileName));
      writer.write(cssContent);
      writer.close();
      //openFile(fileName);
    }
/**
 *  uses windows commands to open the file with associated application
 * @param fileName
 */
     private void openFile(String fileName){
         String[] commands = {"cmd", "/c", "start", "\"RegCMantic files\"",fileName};
        try {
            Runtime.getRuntime().exec(commands);
        } catch (IOException ex) {
            Logger.getLogger(StyleSelector_V3.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
     /**
      * creates a number of space characters using string concatenation
      * @param noOfSpaces
      * @return
      */
      private String space(int noOfSpaces){
          String space="";
          for (int i=1; i<=noOfSpaces; i++){
              space = space + " ";
          }
          return space;
      }

        /**
       * not been used yet
       *  removes style head from head and REGULATION_BODY list if the structure is ignored
       */
   private void removeIgnored(){
          ArrayList<Integer> indexList = new ArrayList<Integer>();
          for (StyleHead head: headList){
              if (  head.getStructure().equals("ignored")){
                  indexList.add(headList.indexOf(head));
              }
          }
          for (Integer i: indexList){
              headList.remove(i);
          }
          indexList.clear();
        //  ArrayList<Integer> indexList = new ArrayList<Integer>();
          for (StyleBody body: bodyList){
              if (  body.getStructure()== null){
                  indexList.add(bodyList.indexOf(body));
              }
          }
          for (Integer i: indexList){
              bodyList.remove(i);
          }
      }

private ArrayList<String> selectStringList(){
    String unwanted[] = {"page_no","other","ignored"};
    ArrayList<String> unwantedList = Converter.arrayToArrayList(unwanted);
    ArrayList<String> selectedList = new ArrayList<String>();
    for (StyleHead head: headList){
        String str = head.getStructure();
        if (RegEx.countPattern(unwantedList, str)==0){
          selectedList.add(str);
        }
    }
    return selectedList;
}

private HeadBody selectLists(){
    String unwanted[] = {"page_no","other","ignored"};
    ArrayList<String> unwantedList = Converter.arrayToArrayList(unwanted);
    ArrayList<StyleHead> shList = new ArrayList<StyleHead>();
    ArrayList<StyleBody> sbList = new ArrayList<StyleBody>();
    for (StyleHead head: headList){
        String str = head.getStructure();
        if (RegEx.countPattern(unwantedList, str)==0){
          shList.add(head);
        }
    }
    for (StyleBody body: bodyList){
        if (RegEx.countPattern(unwantedList, body.getStructure())==0){
            sbList.add(body);
        }
    }

    return new HeadBody(shList, sbList);
}

   private String startTag(String tagName){
       return "<"+tagName+">";
   }
   private String endTag(String tagName){
       return "</"+tagName+">";
   }

}