/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase1;

import zzz_unused.ParagraphAnnotator;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;
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
public class _2_FeatureReader implements Serializable {
    
        /* IO FILES */
        private String IN_CORPUS_FILE = Settings.PHASE1+"feature_annotated_corpus.ser";
        private String OUT_FEATURE_FILE = Settings.PHASE1+"feature_reader.ser";

        /*  annotation related */
        private Corpus corpus; 
        private AnnotationSet annSet;
        private ParagraphAnnotator annotator;

        /* local attributes */
        private String docContent;
        private ArrayList<StyleHead> headList;
        private ArrayList<StyleBody> bodyList;
        private ArrayList allHeadList;
        private ArrayList allBodyList;

/**
* Constructor
*/
public _2_FeatureReader() {
//       annotator = new ParagraphAnnotator();
//       corpus = annotator.getCorpus();
       corpus = (Corpus)MyReader.fileToObject(IN_CORPUS_FILE);
       extract();
       MyWriter.write(this, OUT_FEATURE_FILE);
       Out.prln("*** FEATURE READING COMPLETED *** ");
  }
/**
  * Extracts the gate annotated information into java.
  * @throws IOException
  */
private void extract() {    

   // i. iterate for each document in the corpus
    allHeadList = new ArrayList();
    allBodyList = new ArrayList();
    Iterator iterDocs = corpus.iterator();
    int docCount = 0;
    while(iterDocs.hasNext()) {
          Document annDoc        = (Document) iterDocs.next();
          annSet   = annDoc.getAnnotations();         
          ++docCount;
          System.out.println("document no. = "+docCount);

          //*** Style head extractor ***
          Iterator<Annotation> headIter = Sorter.getSortedAnnIter(annSet.get("StyleHead"));
          int headCount = 0;
          headList = new ArrayList<StyleHead>();

          while (headIter.hasNext()){
            Annotation annHead = (Annotation) headIter.next();
            headCount++;
            StyleHead head = new StyleHead();
            head.setName(annHead.getFeatures().get("name").toString());
            head.setStyle(annHead.getFeatures().get("font-style").toString());
            head.setWeight(annHead.getFeatures().get("font-weight").toString());
            head.setSize(Integer.parseInt(annHead.getFeatures().get("font-size").toString()));
            head.setFamily(annHead.getFeatures().get("font-family").toString());
            head.setColor(annHead.getFeatures().get("color").toString());
            headList.add(head);
           }// style head iterator
          sortHeadList();
          //setStyleStructure();
          System.out.println("Printing head list");
          printHeadList();

          //*** Style body extractor ***
          Iterator<Annotation> bodyIter = Sorter.getSortedAnnIter(annSet.get("StyleBody"));
          int bodyCount = 0;
          bodyList = new ArrayList<StyleBody>();

          while (bodyIter.hasNext()){
            Annotation annBody = (Annotation) bodyIter.next();
            bodyCount++;
            StyleBody body = new StyleBody();
            body.setName(annBody.getFeatures().get("style-name").toString());
            body.setText(annBody.getFeatures().get("text").toString());
            bodyList.add(body);
           }// style head iterator
          matchHeadBody();
          allHeadList.add(headList);
          allBodyList.add(bodyList);
    }//while iterDocs
    // sorting
    sortAllHeadList();
    sortAllBodyList();

    //printers
    printHeadList();
    printBodyList();
}
/*--------------------------------------------| AFTER EXTRACTION    |-----------------------------------------------*/
/**
* It sorts the styles of a head list.
*/
private void sortHeadList(){
int i, j;
int size = headList.size();
StyleHead temp;
for(i = 0; i < size; i++){
  for(j = 1; j < (size-i); j++){
    if(headList.get(j-1).getScore() < headList.get(j).getScore()){
      temp = headList.get(j-1);
      headList.set(j-1, headList.get(j));
      headList.set(j, temp);
    }//if
  }//for
}// for
}
/**
* it sorts the level of the style in all the documents (pages) in the head list.
* We assume that  the regulatory document exists in several html document with different names and
* level of styles.
*/
private void sortAllHeadList(){
//    *** adds all elements to a one list ***
  headList = new ArrayList<StyleHead>();
  for (Object o:allHeadList){
      headList.addAll((Collection<? extends StyleHead>) o);
  }//for
//    *** removes duplicate styles by comparing scores ***
  ArrayList<StyleHead> headList2 = new ArrayList<StyleHead>();
  for (StyleHead head: headList){
      boolean exists = false;
      for (StyleHead sh2: headList2){
          if (head.getScore()==sh2.getScore()){
              exists = true;
          }//if
      }//for
      if (!exists){
          headList2.add(head);
      }//if
  }//for
  headList = headList2;
  sortHeadList(); // sorts the list and sets the score.
//    *** assigns style level ***
  int level = 1;
  for (StyleHead head: headList){
      head.setStyleLevel(level);
      level++;
  }// for
}
/**
* it sorts the level of the style in all the documents (pages) in the body list.
* We assume that  the regulatory document exists in several html document with different names and
* level of styles.
*/
private void sortAllBodyList(){
//    *** adds all elements to one list ***
  bodyList = new ArrayList<StyleBody>();
  for (Object o: allBodyList){
      bodyList.addAll((Collection<? extends StyleBody>) o);
  }// for
//    *** finds match between style head and  style body and assigns level ***
  for (StyleBody styleBody: bodyList){
    for (StyleHead styleHead: headList){
        if (styleBody.getScore()==styleHead.getScore()){
            styleBody.setStyleLevel(styleHead.getStyleLevel());
        }//if
    }//for
  }//for
  spanStyle();
//      spanStyle();
}
/**
* Once head gets the score it matches the relevant body based on the name
* and sets the same score to the body.
*/
private void matchHeadBody(){
for (StyleBody styleBody: bodyList){
    for (StyleHead styleHead: headList){
        if (styleBody.getName().equals(styleHead.getName())){
            styleBody.setScore(styleHead.getScore());
        }
    }
}
}
/**
* It joins the two similar subsequent style structures  e.g. if <paragraph>
* is followed by <paragraph> it will combine them under one
*/
private void spanStyle(){
int level1 ;
int level2;
LinkedList bodyLList = new LinkedList(bodyList);
Iterator iter = bodyLList.iterator();
StyleBody body1 = (StyleBody)iter.next();
while (iter.hasNext()){
    boolean repeat = true;
    StyleBody body2 = null;
    level1 = body1.getStyleLevel();
    while (iter.hasNext()&& repeat){
        body2 = (StyleBody)iter.next();
        level2= body2.getStyleLevel();
        if (level1 == level2){
            body1.setText(body1.getText()+"\n "+body2.getText());
            iter.remove();
            repeat = true;
        }else{
            repeat = false;
        }// if
    }// while second
body1 = body2;
}// while first
bodyList.clear();
bodyList.addAll(bodyLList);
}
/**
*
* @return the list of the style body
*/
public ArrayList<StyleBody> getBodyList() {
    return bodyList;
}
/**
 *
 * @return the list of the style head
 */
public ArrayList<StyleHead> getHeadList() {
        return headList;
    }
/*=========================|  UTILITIES         |============================*/
/**
 * testing: in console
 * prints out all the elements of the head list
 */
private void printHeadList(){
  Out.prln("printing head list ...");
  for (StyleHead head:headList){
     System.out.println("[Score = " + head.getScore()+" "+
             head.getSize()+" "+
             head.getWeight()+" " +
             head.getStyle()+" Structure = "+
             head.getStructure()+" StyleLevel = level"+
             head.getStyleLevel());
  }
}
/**
* testing: in console
* prints out all the elements of the body list
*/
private void printBodyList(){
      Out.prln("printing body list ...");
      for (StyleBody body: bodyList){
         System.out.println("<level_0" + body.getStyleLevel()+"> "+
                 body.getText());
      }
  }
}