/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.nlp;

import api.xml.XmlReader;
import api.xml.XmlWriter;
import helper.util.Print;
import java.util.ArrayList;
import org.w3c.dom.Element;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota,  May 17, 2012,  10:19:20 AM
 * A PhD project at Oxford Brookes University
 */
public class SentenceWrapper {
    /* IO Files*/
    String OUT_FILE;

    /* Local Variables */
    ArrayList<SentenceWrapper> wrapperList = new ArrayList<SentenceWrapper>();
    String subject;
    String action;
    String obl;
    String object;
    String condition;
    String modifier;


    public SentenceWrapper(String outFile) {
        this.OUT_FILE = outFile;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getObl() {
        return obl;
    }

    public void setObl(String obl) {
        this.obl = obl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public ArrayList<SentenceWrapper> getWrapperList() {
        return wrapperList;
    }

    public void setWrapperList(ArrayList<SentenceWrapper> wrapperList) {
        this.wrapperList = wrapperList;
    }
    
    public void read(String filename){
        Print.prln("SentenceWrapper: reading parsed sentences from a file ...");
        XmlReader reader = new XmlReader();
        ArrayList<Element> elList = reader.getXmlDomElements(filename, "parsed_sentence");
        for (Element el: elList){
           String sub = el.getElementsByTagName("subject").item(0).getTextContent();
           String obl = el.getElementsByTagName("obligation").item(0).getTextContent();
           String act = el.getElementsByTagName("action").item(0).getTextContent();
           String obj = el.getElementsByTagName("object").item(0).getTextContent();
           String mod = el.getElementsByTagName("modifier").item(0).getTextContent();
           String con = el.getElementsByTagName("condition").item(0).getTextContent();
           
           SentenceWrapper wp = new SentenceWrapper(OUT_FILE);
           wp.setSubject(sub);
           wp.setObl(obl);
           wp.setAction(act);
           wp.setObject(obj);
           wp.setModifier(mod);
           wp.setCondition(con);
           wrapperList.add(wp);  
        }
        Print.prln(" total records = "+ wrapperList.size());
    }
    
    public void write(String filename){
        Print.prln("SentenceWrapper: writing parsed sentences to a file ...");
        XmlWriter xml = new XmlWriter(filename);
        xml.addAttribute("size", String.valueOf(wrapperList.size()));
        xml.startElement("parsed_sentences");
        int count = 1;
        for (SentenceWrapper wp: wrapperList){
                xml.addAttribute("count", String.valueOf(count++));
                xml.startElement("parsed_sentence");
                    xml.startElement("subject");
                        xml.characters(wp.getSubject());
                    xml.endElement("subject");
                    xml.startElement("obligation");
                        xml.characters(wp.getObl());
                    xml.endElement("obligation");
                    xml.startElement("action");
                        xml.characters(wp.getAction());
                    xml.endElement("action");  
                    xml.startElement("object");
                        xml.characters(wp.getObject());
                    xml.endElement("object");
                    xml.startElement("condition");
                        xml.characters(wp.getCondition());
                    xml.endElement("condition");
                    xml.startElement("modifier");
                        xml.characters(wp.getModifier());
                    xml.endElement("modifier");
                xml.endElement("parsed_sentence");
        } 
        xml.endElement("sentences");
        xml.write(); 
    }

    public String getOutFile() {
        return OUT_FILE;
    }

    public void setOutFile(String outFile) {
        this.OUT_FILE = outFile;
    }
    
    

    
}
