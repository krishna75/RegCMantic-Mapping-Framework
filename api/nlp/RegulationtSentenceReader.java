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
public  class RegulationtSentenceReader extends AbstractSentenceReader{
    
    /* IO Files*/
    String path = Settings.PHASE2+ "before_parsing/";
    String IN_ANNOTATED_REG_FILE = path+ "SemiAnnotatedRegulation.xml";
    String OUT_SEN_FILE = path + "Sentence.xml";

    public RegulationtSentenceReader(String inFile, String outFile) { 
        super (inFile, outFile);
        writeFile(senTextList, outFile);
    }
    @Override
    public void init(){
        elementName = "statement";
        inFile = IN_ANNOTATED_REG_FILE;
        outFile = OUT_SEN_FILE;
        reader = new XmlReader() ; 
        senElementList = new ArrayList<Element>();
        senTextList = new ArrayList<String>();
    }

}
