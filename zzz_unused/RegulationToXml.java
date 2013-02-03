/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;


import api.gt.SerialDatastoreIO;
import java.util.*;
import java.io.*;

import gate.*;
import gate.Corpus;
import gate.creole.*;
import gate.util.*;
//import gui.StyleSelector_zzz;
import javax.swing.JOptionPane;
import rcm.main.Settings;
import helper.reg.Regulation;
import helper.reg.Topic;
import helper.util.Sorter;

/**
 * It extracts GATE annotated information
 * @author Krishna Sapkota on 15-March-2011 at 22:26:04
 */
public class RegulationToXml {

        /** GATE application related  */
        private SerialAnalyserController annieController;
        private Corpus corpus;
        private AnnotationSet docAS;

        /** reg package related */
        private ArrayList<Topic> topicList = new ArrayList<Topic>();
        private Topic topic = null;
        private ArrayList<Regulation> regList = null;
        private Regulation reg ;
        //Paragraph para = null;

        /** regulation related */
        private String xmlFileName = "reg_v2.xml";
        private String regName = "Eudralex";
        private String description = "EU regulation for the pharmaceutical industry";
        private String body = "EMEA";
        private String version = "1.0";
        private String published_date = "2007";

  /**
   * Constructor, which starts with "init()" method.
   * @throws gate.util.GateException
   * @throws java.io.IOException
   */
  public RegulationToXml(){
      
  }
  
 /**
  * Extracts the gate annotated information into java.
  * @throws IOException
  */
  public void extract() {
    corpus = new SerialDatastoreIO().readCorpusFromSerialDatastore(null);
    Iterator iterDocs = corpus.iterator();
    int countDoc = 0;
    boolean isTopic = false;
    while(iterDocs.hasNext()) {
          Document doc = (Document) iterDocs.next();
          docAS   = doc.getAnnotations();
          ++countDoc;
          System.out.println("total no of documents = "+countDoc);
          Iterator<Annotation> annIter = Sorter.getSortedAnnIter(docAS);
          while (annIter.hasNext()){
              Annotation ann = annIter.next();

              /** TOPIC EXTRACTION */
              if (ann.getType().equals("topic")){
                  int start = ann.getStartNode().getOffset().intValue();
                  int end = ann.getEndNode().getOffset().intValue();
                  String text = doc.getContent().toString().substring(start, end);
                  Out.prln(text);
                  topic = new Topic();
                  topic.setTitle(text);
                  regList = new ArrayList<Regulation>();
                  topic.setRegList(regList);
                  topicList.add(topic);
              }
              
              /** REG_ID EXTRACTION */
              if (ann.getType().equals("reg_id")){
                  int start = ann.getStartNode().getOffset().intValue();
                  int end = ann.getEndNode().getOffset().intValue();
                  String text = doc.getContent().toString().substring(start, end);
                  Out.prln(text);
                  reg = new Regulation();
                  regList.add(reg);
                  reg.setId(text);
              }

              /** REGULATION EXTRACTION */
              if (ann.getType().equals("regulation")){
                  int start = ann.getStartNode().getOffset().intValue();
                  int end = ann.getEndNode().getOffset().intValue();
                  String text = doc.getContent().toString().substring(start, end);
                  reg.setDescription(text);
              }                         
          }

    }//while iterDocs
    createXML();
}

  /**
 *  creates the xml file with regulatory information. This is the most important
 *  file created by the whole system.
 * @throws IOException
 */
private void createXML() {

    // meta data of the xml file
    String x =  "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?> \n" +
                "<document  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""+
                " xsi:noNamespaceSchemaLocation=\"xml_v2_schema.xsd\">\n" +
              "   <meta>\n"+
              "     <name> "+  regName +"</name>\n"+
              "     <description>"+ description +"</description>\n"+
              "     <body>"+ body + "</body>\n"+
              "     <version>"+  version + "</version>\n"+
              "     <published_date>"+  published_date + "</published_date>\n"+
              "   </meta>\n <content>\n";

    // content of the xml file
    for (Topic t: topicList){
        x += "\n<topic> \n <title>"+t.getTitle()+ "</title> \n <topic_text>";
        for (Regulation r: t.getRegList()){
            x+="\n<regulation> \n <reg_id>"+r.getId()+"</reg_id>";
            x+= "\n<reg_text>"+r.getDescription()+"</reg_text> \n </regulation>";
        }
        x+= "\n</topic_text> \n</topic>";
    }

    // closing the xml document
    x +=  "\n </content>\n</document>";

    /* write the xmlContent to the file */
    String fileName = Settings.STYLES_PATH+xmlFileName;
    FileWriter writer;
    try {
        writer = new FileWriter(new File(fileName));
        writer.write(x);
        writer.close();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    JOptionPane.showMessageDialog(null, "Converted to XML successfully !!!");
}

/**
 * @return the name of the regulatory body
 */
public String getRegBody() {
    return body;
}
      
 

}