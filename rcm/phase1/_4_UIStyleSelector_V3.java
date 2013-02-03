
package rcm.phase1;


import api.xml.XmlWriter;
import gate.util.Out;
import helper.gui.RcmExtendedFrame;
import helper.reg.Paragraph;
import helper.style.HeadBody;
import helper.style.StyleBody;
import helper.style.StyleHead;
import helper.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rcm.main.Settings;
import zzz_unused.ParagraphReader;

/**
 *  This class saves the extracted text in an XML file and some xml related files.
 *  It requires preprocessed structure predictor serialised object or instantiation
 *  of the structure predictor class.
 * @author Krishna Sapkota 04-Jan-2011  22:28:46
 */
public class _4_UIStyleSelector_V3 extends RcmExtendedFrame implements ActionListener {
    
     /*  IO FILES */
    String STYLES_PATH = Settings.PHASE1+"styles/";
    String IN_STRUCTURE_FILENAME       = STYLES_PATH +"structures.txt";
    String OUT_LAST_STRUCTURE_FILENAME = STYLES_PATH +"recent_structures.txt";
    String OUT_SCHEMA_FILENAME         = STYLES_PATH +"reg_schema.xml";
    String OUT_XML_FILENAME            = STYLES_PATH +"reg_xml.xml";
    String OUT_XSL_FILENAME            = STYLES_PATH +"reg_xsl.xsl";
    String OUT_CSS_FILENAME            = STYLES_PATH +"reg_css.css";
    String OUT_NEW_SCHEMA_FILE         = STYLES_PATH +"reg_xml.xsd";
    String OUT_PREDICTOR_FILENAME      = Settings.PHASE1 +"structure_predictor.ser";
    String OUT_TEMP_PATH               = Settings.PHASE1 +"temp/";
    
    /*  gui button names */
    String SCHEMA  = "schema";
    String PROCESS = "process" ;
    String XML     = "xml";
    String CANCEL  = "cancel";

    String SELECT_TEXT  = "Select a document structure type";
    String PROCESS_TEXT = "Process Dcoument";
    String SCHEMA_TEXT  = "Create Schema";
    String XML_TEXT     = "Create XML";
    String CANCEL_TEXT  = "Cancel";

    /*  about the details of the regulation document */
    String REGULATION_NAME = "Eudralex";
    String DESCRIPTION     = "EU regulation for the pharmaceutical industry";
    String REGULATION_BODY = "EMEA";
    String VERSION         = "1.0";
    String PUBLISHED_ON    = "2007";


    private ArrayList<StyleHead> headList;
    private ArrayList<StyleBody> bodyList;
    private ArrayList<JComboBox> comboList;
    private ArrayList<String> structureList ;
    private ArrayList<String> lastStructureList;
    private boolean processed;
    private ArrayList<String> removableList;
    private String title = "Document Structure Selector";


public _4_UIStyleSelector_V3()  {
    super();
    init();

    /* reads a structure predicutor object from serialisation and provides head list and REGULATION_BODY list */
    _3_StructurePredictor predictor = (_3_StructurePredictor) MyReader.fileToObject(OUT_PREDICTOR_FILENAME);
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
    Out.prln("*** STYLE SELECTOR GUI CREATION COMPLETED *** ");
}
private void init(){
     getLblTitle().setText(title);
    comboList = new ArrayList<JComboBox>();
    removableList = new ArrayList<String>();
    removableList.add("page_no");
    removableList.add("other");
    removableList.add("ignored");
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

            // finds the first example text from the body
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
        northPanel.setPreferredSize(new Dimension(100, 100));
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
        structureList = MyReader.fileToArrayList( IN_STRUCTURE_FILENAME);
}
/**
* It loads a list of structure entity from a text file, and will be used to display
* in combo boxes.
*/
private void getRecentStructuresFromFile(){
    lastStructureList = MyReader.fileToArrayList(OUT_LAST_STRUCTURE_FILENAME);
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
        if (processed){
            createSchema();
        }
    }
    // if convert to XML button is pressed
    if (comm.equals(XML)){
        processDocument();
        if (processed){
//            writeSelectedList();
            createXML2();
//            createXSL();
//            createCSS();
        }
    }
    // closes the frame
    if (comm.equals(CANCEL)) {
       this.dispose();
    }
}
/*  ---------------|           POST ACTION  PROCESSES      | ----------------- */
/**
 * The newly selected structure list will be updated in the head list and the body list.
 * it updates the system with  the user selection. It should be used before writing  any files.
 */
private void processDocument(){
    processed = true;

    /*  updates the structures in the head list */
    for (JComboBox combo: comboList){

        /*  gets the selected structure from the combo box */
        String structure = combo.getSelectedItem().toString();
        int index =comboList.indexOf(combo);
        
        /*  form validation (makes sure every option is selected) */
        if (structure.equals(SELECT_TEXT) ){
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
        /*  actual update takes place here */
        headList.get(index).setStructure(structure);
        System.out.println(structure);
     }
    /*  updates the structures in the body list*/
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
        String fileName =  OUT_LAST_STRUCTURE_FILENAME;
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
private void createSchema() {
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
      FileWriter writer;
      try {
        writer = new FileWriter(new File(OUT_SCHEMA_FILENAME));
        writer.write(xmlContent);
        writer.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }

      JOptionPane.showMessageDialog(null, "A schema file is created successfully !");
      openFile(OUT_SCHEMA_FILENAME);
}
private void createXML(){      
        XmlWriter xml = new XmlWriter (OUT_XML_FILENAME);
        xml.startElement("document");
            xml.startElement("meta");
                xml.startElement("name"); xml.characters(REGULATION_NAME); xml.endElement("name");
                xml.startElement("description"); xml.characters(DESCRIPTION); xml.endElement("description");
                xml.startElement("body"); xml.characters(REGULATION_BODY); xml.endElement("body");
                xml.startElement("version"); xml.characters(VERSION); xml.endElement("version");
                xml.startElement("published_on"); xml.characters(PUBLISHED_ON); xml.endElement("published_on");
            xml.endElement("meta");
            xml.startElement("content");

//      ArrayList<String> strucList = selectStringList();
      HeadBody hb = selectLists();
//      HeadBody hb = new HeadBody(headList,bodyList);
      ArrayList<StyleHead> headList1 = hb.getHeadList();
      ArrayList<StyleBody> bodyList1 = hb.getBodyList();
      Stack<StyleHead> stack = new Stack<StyleHead>();
      Print.prln("head size ="+headList1.size()+"  body size = "+bodyList1.size());
      StyleHead headCurrent;
      StyleHead headNext;

      /*  The first body tag */
      StyleBody body1 = bodyList1.get(0);
      xml.startElement(body1.getStructure());
      xml.startElement("text");xml.characters(body1.getText());xml.endElement("text");
      headCurrent = headList.get(body1.getStyleLevel()-1);
      stack.push(headCurrent);
      Print.prln("<"+headCurrent.getStructure());

      for (int i = 0; i<bodyList1.size()-1;i++){
          StyleBody bodyCurrent = bodyList1.get(i);
          StyleBody bodyNext = bodyList1.get(i+1);
          int levelCurrent = bodyCurrent.getStyleLevel();
          int levelNext = bodyNext.getStyleLevel();
          String structureCurrent = bodyCurrent.getStructure();
          String structureNext = bodyNext.getStructure();
          String textCurrent = bodyCurrent.getText();
          String textNext = bodyNext.getText();

              /* Note: Higher the level no, the smallar the text
                                           CASE 1: If the next style level is higer (smaller text) than  the current one, write start tag and text of the current level and
                                                        add  add the end tag to the stack */
              if (levelCurrent<levelNext){
                  headNext = headList.get(levelNext-1);
                 // StyleHead headCurrent1 = headCurrent; // can be initialiezed with null

                  /* from previous level to current level -1 */
                  int indexPrevious = headList1.indexOf(headCurrent)+1;
                  int indexCurrent = headList1.indexOf(headNext)-2;
                  for ( int j =indexPrevious; j <indexCurrent; j++){

                      /* write start tags */
                      headCurrent = headList1.get(j);
                      xml.startElement(headCurrent.getStructure());
                      Print.prln("<"+headCurrent.getStructure());

                      /* add each headstyle to the stack */
                      stack.push(headCurrent);
                  }// for

                  headCurrent = headNext;
                  xml.startElement(structureNext);
                  Print.prln("<"+headCurrent.getStructure());//---------
                    xml.startElement("text"); xml.characters(textNext); xml.endElement("text");
                  stack.push(headCurrent);
//                  headCurrent = headNext;
                  
              }// if n1<n2

              /* CASE 2: If the next style level is the same as the current one,
                                                        write start tag, text and end tag of the current. No stack involved */
              if (levelCurrent==levelNext){
                  xml.endElement(structureNext);
                  Print.prln("..."+headCurrent.getStructure()+ ">");//---------
                  xml.startElement(structureNext);
                    xml.startElement("text");xml.characters(textNext);xml.endElement("text");
                    Print.prln("<"+headCurrent.getStructure()+ ".. ");//---------
                  }

              /* CASE 3: If the next style level is lower (bigger text) than the  current one, write start tag, text and end tag of the current level.
                                                       Add all required end tags from the stack. */
              if (levelCurrent>levelNext){
                  headCurrent = stack.pop();
                  xml.endElement(headCurrent.getStructure());
                  Print.prln("..."+headCurrent.getStructure()+ ">");//---------
                  headNext = headList.get(levelNext-1);
                  StyleHead running = headCurrent;

                  /* write end tags */
                  for (int j = headList1.indexOf(headCurrent); j >headList1.indexOf(headNext); j --){
                      if (!stack.empty()){
                          running = stack.pop();
                          xml.endElement(running.getStructure());
                          Print.prln("..."+running.getStructure()+ ">");//---------
                      }
                  }
                  headCurrent = headNext;
                  stack.push(headCurrent);
                  xml.startElement(structureNext);
                  Print.prln("<"+headCurrent.getStructure());//---------
                  xml.startElement("text");xml.characters(textNext);xml.endElement("text");
              }
      }// for each body
      while (!stack.empty()){ 
          xml.endElement(headCurrent.getStructure());
          Print.prln("..."+headCurrent.getStructure()+ ">");//---------
          headCurrent= stack.pop();
      }
      xml.endElement("content");
      xml.endElement("document");
      xml.write();
      openFile(OUT_XML_FILENAME);
}
private void createXML2(){
        XmlWriter xml = new XmlWriter (OUT_XML_FILENAME);
        xml.setDtd(OUT_NEW_SCHEMA_FILE);
        xml.startElement("document");
            xml.startElement("meta");
                xml.startElement("name"); xml.characters(REGULATION_NAME); xml.endElement("name");
                xml.startElement("description"); xml.characters(DESCRIPTION); xml.endElement("description");
                xml.startElement("body"); xml.characters(REGULATION_BODY); xml.endElement("body");
                xml.startElement("version"); xml.characters(VERSION); xml.endElement("version");
                xml.startElement("published_on"); xml.characters(PUBLISHED_ON); xml.endElement("published_on");
            xml.endElement("meta");
            xml.startElement("content");

//      ArrayList<String> strucList = selectStringList();
      HeadBody hb = selectLists();
//      HeadBody hb = new HeadBody(headList,bodyList);
      ArrayList<StyleHead> headList1 = hb.getHeadList();
      ArrayList<StyleBody> bodyList1 = hb.getBodyList();
      Stack<StyleHead> stack = new Stack<StyleHead>();
      Print.prln("head size ="+headList1.size()+"  body size = "+bodyList1.size());
      StyleHead headCurrent;
      StyleHead headNext;

      /*  The first body tag */
      StyleBody body1 = bodyList1.get(0);
      if (body1.getStructure().equals("paragraph")){
                      ArrayList<Paragraph> pList = splitParagraph(body1.getText());
                      for (Paragraph p: pList){
                        xml.addAttribute("paraNum", p.getParagraphNum());
                        xml.startElement("paragraph");
                        xml.characters(p.getDescription());
                        xml.endElement("paragraph");
                      }
                  }else{
                    xml.addAttribute("title", body1.getText());
                    xml.startElement(body1.getStructure());
                    xml.characters(body1.getText());
                    Print.prln("<"+body1.getStructure());//---------
                  }
//      xml.startElement(body1.getStructure());
//      xml.characters(body1.getText());
      headCurrent = headList.get(body1.getStyleLevel()-1);
      stack.push(headCurrent);
      Print.prln("<"+headCurrent.getStructure());

      for (int i = 0; i<bodyList1.size()-1;i++){
          StyleBody bodyCurrent = bodyList1.get(i);
          StyleBody bodyNext = bodyList1.get(i+1);
          int levelCurrent = bodyCurrent.getStyleLevel();
          int levelNext = bodyNext.getStyleLevel();
          String structureNext = bodyNext.getStructure();
          String textNext = bodyNext.getText();

              /* Note: Higher the level no, the smallar the text
                                           CASE 1: If the next style level is higer (smaller text) than  the current one, write start tag and text of the current level and
                                                        add  add the end tag to the stack */
              if (levelCurrent<levelNext){
                  headNext = headList.get(levelNext-1);
                 // StyleHead headCurrent1 = headCurrent; // can be initialiezed with null

                  /* from previous level to current level -1 */
                  int indexPrevious = headList1.indexOf(headCurrent)+1;
                  int indexCurrent = headList1.indexOf(headNext)-2;
                  for ( int j =indexPrevious; j <indexCurrent; j++){

                      /* write start tags */
                      headCurrent = headList1.get(j);
                      xml.startElement(headCurrent.getStructure());
                      Print.prln("<"+headCurrent.getStructure());

                      /* add each headstyle to the stack */
                      stack.push(headCurrent);
                  }// for

                  headCurrent = headNext;
                  if (headCurrent.getStructure().equals("paragraph")){
                      ArrayList<Paragraph> pList = splitParagraph(textNext);
                      for (Paragraph p: pList){
                        xml.addAttribute("paraNum", p.getParagraphNum());
                        xml.startElement("paragraph");
                        xml.characters(p.getDescription());
                        if (pList.indexOf(p)!= pList.size()-1){
                            xml.endElement("paragraph");
                        }
                      }
                  }else{
                    xml.addAttribute("title", textNext);
                    xml.startElement(structureNext);
                    Print.prln("<"+headCurrent.getStructure());//---------
                    xml.characters(textNext);
                  }
                  stack.push(headCurrent);
//                  headCurrent = headNext;

              }// if n1<n2

              /* CASE 2: If the next style level is the same as the current one,
                                                        write start tag, text and end tag of the current. No stack involved */
              if (levelCurrent==levelNext){
                  
                  Print.prln("..."+headCurrent.getStructure()+ ">");//---------
                  if (headCurrent.getStructure().equals("paragraph")){
                      ArrayList<Paragraph> pList = splitParagraph(textNext);
                      for (Paragraph p: pList){
                        xml.addAttribute("paraNum", p.getParagraphNum());
                        xml.startElement("paragraph");
                        xml.characters(p.getDescription());
                        if (pList.indexOf(p)!= pList.size()-1){
                            xml.endElement("paragraph");
                        }
                      }
                  }else{
                    xml.endElement(structureNext);
                    xml.addAttribute("title", textNext);
                    xml.startElement(structureNext);
                    Print.prln("<"+headCurrent.getStructure());//---------
                    xml.characters(textNext);
                  }
          }

              /* CASE 3: If the next style level is lower (bigger text) than the  current one, write start tag, text and end tag of the current level.
                                                       Add all required end tags from the stack. */
              if (levelCurrent>levelNext){
                  headCurrent = stack.pop();
                  xml.endElement(headCurrent.getStructure());
                  Print.prln("..."+headCurrent.getStructure()+ ">");//---------
                  headNext = headList.get(levelNext-1);
                  StyleHead running = headCurrent;

                  /* write end tags */
                  for (int j = headList1.indexOf(headCurrent); j >headList1.indexOf(headNext); j --){
                      if (!stack.empty()){
                          running = stack.pop();
                          xml.endElement(running.getStructure());
                          Print.prln("..."+running.getStructure()+ ">");//---------
                      }
                  }
                  headCurrent = headNext;
                  stack.push(headCurrent);
                  if (headCurrent.getStructure().equals("paragraph")){
                      ArrayList<Paragraph> pList = splitParagraph(textNext);
                      for (Paragraph p: pList){
                        xml.addAttribute("paraNum", p.getParagraphNum());
                        xml.startElement("paragraph");
                        xml.characters(p.getDescription());
                        xml.endElement("paragraph");
                      }
                  }else{
                    xml.addAttribute("title", textNext);
                    xml.startElement(structureNext);
                    Print.prln("<"+headCurrent.getStructure());//---------
                    xml.characters(textNext);
                  }
              }
      }// for each body
      while (!stack.empty()){
          xml.endElement(headCurrent.getStructure());
          Print.prln("..."+headCurrent.getStructure()+ ">");//---------
          headCurrent= stack.pop();
      }
      xml.endElement("content");
      xml.endElement("document");
      xml.write();
      openFile(OUT_XML_FILENAME);
}

/**
 *  creates a xsl file to display xml file with styles.
 * @throws IOException
 */
private void createXSL(){
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

      String fileName = OUT_XSL_FILENAME;
      try {
          FileWriter writer = new FileWriter(new File(fileName));
          writer.write(xslContent);
          writer.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      //openFile(fileName);

    }
/**
 * creates a css file to render formating to xml file with the help of xsl.
 * @throws IOException
 */
private void createCSS(){
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

      String fileName =  OUT_CSS_FILENAME;
      try {
          FileWriter writer = new FileWriter(new File(fileName));
          writer.write(cssContent);
          writer.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
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
    Logger.getLogger(_4_UIStyleSelector_V3.class.getName()).log(Level.SEVERE, null, ex);
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
*  NOTE: not been used yet. should not remove any index in array list, rather use iterator instead.
*  removes style head from head and body list if the structure is ignored
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
/**
 *  It selects only the wanted list of structure from the head list.
 *  In other words, it removes the unwanted structures from the list.
 * @return a list of selected (wanted) structure list.
 */
private ArrayList<String> selectStringList(){
    ArrayList<String> selectedList = new ArrayList<String>();
    for (StyleHead head: headList){
        String structure = head.getStructure();
        if (RegEx.countPattern(removableList, structure)==0){
          selectedList.add(structure);
        }
    } 
    return selectedList;
}
/**
 *  It removes unwanted structures (tags) from the head list and body list
 * @return a head body having no unwanted structures
 */
private HeadBody selectLists(){
    ArrayList<StyleHead> headList1 = new ArrayList<StyleHead>();
    ArrayList<StyleBody> bodyList1 = new ArrayList<StyleBody>();

    /* removes unwated structures from the head list  */
    for (StyleHead head: headList){
        String structure = head.getStructure();
        if (RegEx.countPattern(removableList, structure)==0){ // structure is not in the removable list
          headList1.add(head);
        }
    }
    /*  remooves unwanted structures from the body list */
    for (StyleBody body: bodyList){
        String structure = body.getStructure();
        int count = RegEx.countPattern(removableList, structure);
//        Out.prln("structure = "+structure + " count = "+count+ " text = "+body.getText());
        if (count==0){ // structure is not in the removable list
//            Out.prln("added ......");
            bodyList1.add(body);
        }else {
//            Out.prln("removed ......");
        }
    }
    bodyList1 = Sorter.spanStyle(bodyList1);
    bodyList1 = Splitter.splitParagraphs(bodyList1);
    //bodyList1 = splitParagraph(bodyList1);
    return new HeadBody(headList1, bodyList1);
}
/**
 *  It  splits the paragraphs based on the jape grammars defined in gate, thus uses the class   paragraph finder.
 * @param bodyList1 which contains un-split paragraphs 
 * @return new body list with split paragraphs
 */
private ArrayList<Paragraph> splitParagraph(String paragraph){
    ArrayList<Paragraph> paragraphList = new ArrayList<Paragraph>();
            String filename = OUT_TEMP_PATH +"temp_paragraph.txt";
            MyWriter.write(paragraph, filename);
            paragraph = MyReader.fileToText(filename);
            MyReader.deleteFile(filename);

            paragraphList = new ParagraphReader(paragraph).getParagraphList();// initialise gate only one time.
    return paragraphList;
}
/**
 *  It  splits the paragraphs based on the jape grammars defined in gate, thus uses the class   paragraph finder.
 * @param bodyList1 which contains un-split paragraphs
 * @return new body list with split paragraphs
 */
private ArrayList<StyleBody> splitParagraph_1(ArrayList<StyleBody> bodyList1){
    ArrayList<StyleBody> bodyList2 = new ArrayList<StyleBody>();
    int count = 1;

    for (StyleBody body1: bodyList1){
        if (body1.getStructure().equals("paragraph")){
            String filename = OUT_TEMP_PATH + count+".txt";
            count++;
            Out.prln ("paragarph no ="+count);
            String text = body1.getText();
            MyWriter.write(text, filename);
            text = MyReader.fileToText(filename);
            MyReader.deleteFile(filename);

            ArrayList<Paragraph> splittedParagraphList = new ParagraphReader(text).getParagraphList();// initialise gate only one time.

            for (Paragraph para: splittedParagraphList){
                StyleBody body2 = new StyleBody();
                body2.setScore(body1.getScore());
                body2.setText(para.getDescription());
                //body2.setParagraphNum(para.getParagraphNum());
                body2.setStyleLevel(body1.getStyleLevel());
                body2.setStructure(body1.getStructure());
                bodyList2.add(body2);
            }//for
        } else {
            bodyList2.add(body1);
        }// if else
    }//for
    return bodyList2;
}
private String startTag(String tagName){
   return "<"+tagName+">";
}
private String endTag(String tagName){
       return "</"+tagName+">";
   }
}