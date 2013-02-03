/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.nlp;

import rcm.main.Settings;
import api.xml.XmlReader;
import api.xml.XmlWriter;
import helper.util.Converter;
import helper.util.Print;
import java.util.ArrayList;
import org.w3c.dom.Element;
import rcm.phase34.ui._2_MappingData;

/**
 *
 * @author Krishna Sapkota,  May 16, 2012,  3:29:12 PM
 * A PhD project at Oxford Brookes University
 */
public abstract class AbstractSentenceReader {
    
    XmlReader reader ; 
    String elementName;
    ArrayList<Element> senElementList;
    ArrayList<String> senTextList;
    
    // i/o files
    String inFile;
    String outFile ;

    public AbstractSentenceReader(String inFile, String outFile) {
        this.inFile = inFile;
        this.outFile = outFile;
        init();
        Print.prln("infile ="+inFile);
        readFile();
  
        
//        printList();

    }
    public void init(){

    }
    
    public void printList(){
        Print.prlnArrayList(senTextList);
    }
 
   public void readFile(){
       Print.prln("infile inside read ="+inFile);
       senElementList = reader.getXmlDomElements(inFile, elementName);
       for (Element sen: senElementList){
           String senText = sen.getTextContent();
           senTextList.add(senText);
       }
   }
   /**
     *  writes an xml file and dumps its properties
     */
    public void writeFile(ArrayList<String> senTextList, String filename){
        Print.prln("SentenceReader: writing sentences to a file ...");
        XmlWriter xml = new XmlWriter(filename);
        xml.addAttribute("size", String.valueOf(senTextList.size()));
        xml.startElement("sentences");
        int count = 1;
        for (String sen: senTextList){
            if (!(sen.equals(""))){
                xml.addAttribute("count", String.valueOf(count++));
                xml.startElement("sentence");
                xml.characters(sen);
                xml.endElement("sentence");
            }
        } 
        xml.endElement("sentences");
        xml.write(); 
    }

    public ArrayList<String> getSenTextList() {
        return senTextList;
    }
    
    

}
