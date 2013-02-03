/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import java.util.*;
import java.io.*;
import java.net.*;

import gate.*;
import gate.Corpus;
import gate.creole.*;
import gate.util.*;
import gate.corpora.RepositioningInfo;
import gate.persist.PersistenceException;
//import gui.UIStyleSelector_zzz;
import rcm.phase1._4_UIStyleSelector_V3;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;
import helper.reg.Paragraph;
import helper.style.StyleBody;
import helper.style.StyleHead;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.MyWriter;
import helper.util.Sorter;

/**
 *
 * @author Krishna Sapkota on 29-June-2011
 */
public class ParagraphReader implements Serializable {

        /*  annotation related */
        private Corpus corpus; 
        private AnnotationSet annSet;
        private ParagraphAnnotator annotator;

        /* local attributes */
        private ArrayList<Paragraph> paragraphList;
/**
* Constructor
*/
public ParagraphReader(String paragraphContent) {
       corpus = (Corpus)new ParagraphAnnotator(paragraphContent).getCorpus();
       extract();
       printParagraphList();
       Out.prln("*** PARAGRAPH READING COMPLETED *** ");
  }
/**
  * Extracts the gate annotated information into java.
  * @throws IOException
  */
private void extract() {


   // i. iterate for each document in the corpus
    Iterator iterDocs = corpus.iterator();
    int docCount = 0;
    while(iterDocs.hasNext()) {
          Document annDoc = (Document) iterDocs.next();
          annSet = annDoc.getAnnotations();
          ++docCount;
          System.out.println("document no. = "+docCount);

          //*** paragraph extractor ***
          Iterator<Annotation> paraIter = Sorter.getSortedAnnIter(annSet.get("paragraph"));
          paragraphList = new ArrayList<Paragraph>();

          while (paraIter.hasNext()){
            Annotation paraAnn = (Annotation) paraIter.next();
            String paraText = paraAnn.getFeatures().get("text").toString();
            String paraNum = paraAnn.getFeatures().get("paraNum").toString();
            Paragraph para = new Paragraph();
            para.setParagraphNum(paraNum);
            para.setDescription(paraText);
            paragraphList.add(para);
           }// iterator
    }//while iterDocs 
}
/**
*
* @return the list of the style body
*/
public ArrayList<Paragraph> getParagraphList() {
    printParagraphList();
    return paragraphList;
}
/*=========================|  UTILITIES         |============================*/
/**
* testing: in console
* prints out all the elements of the body list
*/
private void printParagraphList(){
      Out.prln("printing body list ...");
      for (Paragraph paragraph: paragraphList){
         System.out.println( paragraph.getDescription() + "\n\n");
      }
  }
}