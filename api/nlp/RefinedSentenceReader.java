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
import helper.util.RegEx;
import java.util.ArrayList;
import org.w3c.dom.Element;
import rcm.phase34.ui._2_MappingData;

/**
 *
 * @author Krishna Sapkota,  May 16, 2012,  3:29:12 PM
 * A PhD project at Oxford Brookes University
 */
public  class RefinedSentenceReader extends AbstractSentenceReader{

/**
 * 
 * @param inFile
 * @param outFile  provide null if this class is just for reading
 */
    public RefinedSentenceReader(String inFile, String outFile) {
        super(inFile, outFile); 
        if (outFile != null){
            refine();
            writeFile(senTextList, outFile);
        }  
    }
    @Override
    public void init(){
        elementName = "sentence";
        reader = new XmlReader() ; 
        senElementList = new ArrayList<Element>();
        senTextList = new ArrayList<String>();
    }

    public void refine(){
        ArrayList<String> refinedList = new ArrayList<String>();
        for (String s: senTextList){
            refinedList.add(RegEx.removeSentencePrefix(s));
        }
        senTextList = refinedList;
    }
    

    
    

}
