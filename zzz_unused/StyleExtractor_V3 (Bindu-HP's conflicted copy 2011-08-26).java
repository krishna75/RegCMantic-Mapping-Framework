package zzz_unused;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package gt;
//
//
//import java.util.*;
//import java.io.*;
//import java.net.*;
//
//import gate.*;
//import gate.Corpus;
//import gate.creole.*;
//import gate.util.*;
//import gate.corpora.RepositioningInfo;
//import gate.persist.PersistenceException;
//import gui.StyleSelector_zzz;
//import gui.StyleSelector_V3;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import main.Settings;
//import style.StyleBody;
//import style.StyleHead;
//import util.Converter;
//import util.Sorter;
//
//
///**
// * Process Illustration:
// * Please refer to the illustrations in Semantic Archetectures.ppt file
// * for  the insight into the  processes of this class.
// * ---------------------------------------------------------------------------
// * This class jumps to StyleSelector_zzz.java, a gui class
// * To complete processes in these two classes it needs
// * 1. to change the path of the files in the settings class.
// * 2. GATE: installation and its path setting in the settings class
// * 3. GATE: jape and gazetteer folders (e.g. jape4html,gazetteer_html )
// * 4. structure.txt : list of structures
// * 5. reg_schema.xml (generated)
// * 6. and of course! the processing html files.
// * ---------------------------------------------------------------------------
// * It extracts GATE annotated information
// * @author Krishna Sapkota on 13-Jul-2010 at 22:26:04
// */
//public class StyleExtractor_V3 {
//
//        /* The Corpus Pipeline application to contain ANNIE */
//        private Corpus corpus;
//        private AnnotationSet annSet;
//        private FeatureAnnotator annotator;
//
//        /* local attributes */
//        private String docContent;
//        private ArrayList<StyleHead> headList;
//        private ArrayList<StyleBody> bodyList;
//        private ArrayList allHeadList;
//        private ArrayList allBodyList;
//
//
//  /**
//   * Constructor, which starts with "init()" method.
//   * @throws gate.util.GateException
//   * @throws java.io.IOException
//   */
//  public StyleExtractor_V3() {
//       annotator = new FeatureAnnotator();
//       corpus = annotator.getCorpus();
//       extract();
//  }
//
//
// /**
//  * Extracts the gate annotated information into java.
//  * @throws IOException
//  */
//  private void extract() {
//
//   // i. iterate for each document in the corpus
//    allHeadList = new ArrayList();
//    allBodyList = new ArrayList();
//    Iterator iterDocs = corpus.iterator();
//    int docCount = 0;
//    while(iterDocs.hasNext()) {
//          Document annDoc        = (Document) iterDocs.next();
//          annSet   = annDoc.getAnnotations();
//          ++docCount;
//          System.out.println("document no. = "+docCount);
//
//          /** REGULATION EXTRACTION */
//          Iterator<Annotation> regIter = Sorter.getSortedAnnIter(annSet.get("regulation"));
//          int regCount = 0;
//          while (regIter.hasNext()){
//              Annotation annReg = (Annotation) regIter.next();
//              Out.prln(annReg.toString());
//          }
//
//          //*** Style head extractor ***
//          Iterator<Annotation> headIter = Sorter.getSortedAnnIter(annSet.get("StyleHead"));
//          int headCount = 0;
//          headList = new ArrayList<StyleHead>();
//
//          while (headIter.hasNext()){
//            Annotation annHead = (Annotation) headIter.next();
//            headCount++;
//            StyleHead head = new StyleHead();
//            head.setName(annHead.getFeatures().get("name").toString());
//            head.setStyle(annHead.getFeatures().get("font-style").toString());
//            head.setWeight(annHead.getFeatures().get("font-weight").toString());
//            head.setSize(Integer.parseInt(annHead.getFeatures().get("font-size").toString()));
//            head.setFamily(annHead.getFeatures().get("font-family").toString());
//            head.setColor(annHead.getFeatures().get("color").toString());
//            headList.add(head);
//           }// style head iterator
//
//          sortHeadList();
//          //setStyleStructure();
//          System.out.println("Printing head list");
//          printHeadList();
//
//          //*** Style body extractor ***
//          Iterator<Annotation> bodyIter = Sorter.getSortedAnnIter(annSet.get("StyleBody"));
//          int bodyCount = 0;
//          bodyList = new ArrayList<StyleBody>();
//
//          while (bodyIter.hasNext()){
//            Annotation annBody = (Annotation) bodyIter.next();
//            bodyCount++;
//            StyleBody body = new StyleBody();
//            body.setName(annBody.getFeatures().get("style-name").toString());
//            body.setText(annBody.getFeatures().get("text").toString());
//            bodyList.add(body);
//           }// style head iterator
//          matchHeadBody();
//          allHeadList.add(headList);
//          allBodyList.add(bodyList);
//    }//while iterDocs
//
//    // sorting
//    sortAllHeadList();
//    sortAllBodyList();
//
//    //printers
//    printHeadList();
//    printBodyList();
//
//    // gui for users
//    setStyleStructure();
//}
//
//
///*--------------------------------------------| AFTER EXTRACTION    |-----------------------------------------------*/
//
///**
// * It sorts the styles of a head list.
// */
//  private void sortHeadList(){
//    int i, j;
//    int size = headList.size();
//    StyleHead temp;
//    for(i = 0; i < size; i++){
//      for(j = 1; j < (size-i); j++){
//        if(headList.get(j-1).getScore() < headList.get(j).getScore()){
//          temp = headList.get(j-1);
//          headList.set(j-1, headList.get(j));
//          headList.set(j, temp);
//        }//if
//      }//for
//    }// for
//  }
//
///**
//   * it sorts the level of the style in all the documents (pages) in the head list.
//   * We assume that  the regulatory document exists in several html document with different names and
//   * level of styles.
//   */
//  private void sortAllHeadList(){
////    *** adds all elements to a one list ***
//      headList = new ArrayList<StyleHead>();
//      for (Object o:allHeadList){
//          headList.addAll((Collection<? extends StyleHead>) o);
//      }//for
////    *** removes duplicate styles by comparing scores ***
//      ArrayList<StyleHead> headList2 = new ArrayList<StyleHead>();
//      for (StyleHead head: headList){
//          boolean exists = false;
//          for (StyleHead sh2: headList2){
//              if (head.getScore()==sh2.getScore()){
//                  exists = true;
//              }//if
//          }//for
//          if (!exists){
//              headList2.add(head);
//          }//if
//      }//for
//      headList = headList2;
//      sortHeadList(); // sorts the list and sets the score.
////    *** assigns style level ***
//      int level = 1;
//      for (StyleHead head: headList){
//          head.setStyleLevel(level);
//          level++;
//      }// for
//  }
//
//  /**
//   * it sorts the level of the style in all the documents (pages) in the body list.
//   * We assume that  the regulatory document exists in several html document with different names and
//   * level of styles.
//   */
//  private void sortAllBodyList(){
////    *** adds all elements to one list ***
//      bodyList = new ArrayList<StyleBody>();
//      for (Object o: allBodyList){
//          bodyList.addAll((Collection<? extends StyleBody>) o);
//      }// for
////    *** finds match between style head and  style body and assigns level ***
//      for (StyleBody styleBody: bodyList){
//        for (StyleHead styleHead: headList){
//            if (styleBody.getScore()==styleHead.getScore()){
//                styleBody.setStyleLevel(styleHead.getStyleLevel());
//            }//if
//        }//for
//      }//for
//      spanStyle();
////      spanStyle();
//  }
//
///**
// * sets the structure of regulatory document based on the level of the styles
// * it calls another class to perform this task.
// */
//  private void setStyleStructure(){
//     // setStructureHere();
//      Out.prln("generating gui...........");
//      new StyleSelector_zzz(headList, bodyList);
//
//  }
//
//  /**
//   * It recommends the  list of structure for the the documents. It was used before
//   * for the testing purpose. Now it is replayed in Structure Selector  by allowing
//   * user to select it.
//   */
//  private void setStructureHere(){
//      headList.get(0).setStructure("Chapter");
//      headList.get(1).setStructure("Section Header");
//      headList.get(2).setStructure("Section");
//      headList.get(3).setStructure("Regulation");
//      headList.get(4).setStructure("Regulation");
//  }
//
//  /**
//   * Once head gets the score it matches the relevant body based on the name
//   * and sets the same score to the body.
//   */
//  private void matchHeadBody(){
//    for (StyleBody styleBody: bodyList){
//        for (StyleHead styleHead: headList){
//            if (styleBody.getName().equals(styleHead.getName())){
//                styleBody.setScore(styleHead.getScore());
//            }
//        }
//    }
//  }
//
//  /**
//   * It joins the two similar subsequent style structures  e.g. if <paragraph>
//   * is followed by <paragraph> it will combine them under one
//   */
//  private void spanStyle(){
//      ArrayList<StyleBody> bodyList2 = new ArrayList<StyleBody>();
//      int size = bodyList.size();
//      int level1 ;
//      int level2;
//      boolean repeat = true;
////
////      while (repeat){
//          repeat = false;
//          for (int i=0;i<size; i++ ){
//                  StyleBody body1 = bodyList.get(i);
//                  bodyList2.add(body1);
//                  level1 = body1.getStyleLevel();
//
//                  for (int j=i+1;j<size-1; j++ ){
//                      StyleBody body2 = bodyList.get(j);
//                      level2= body2.getStyleLevel();
//                      if (level1 == level2){
//                          body1.setText(body1.getText()+"\n"+body2.getText());
//                          repeat = true;
//                      }else{
//                          break;
//                      }
//                  }
//          }
////}
//          Out.prln("size of bodylist = "+bodyList2.size());
//          bodyList.clear();
//          bodyList = bodyList2;
//          //bodyList2.clear();
//           Out.prln("size of bodylist = "+bodyList.size());
//
//  }
//
///*=========================|  UTILITIES         |============================*/
//
//
///**
// * testing: in console
// * prints out all the elements of the head list
// */
//   private void printHeadList(){
//      for (StyleHead head:headList){
//         System.out.println("[Score = " + head.getScore()+" "+
//                 head.getSize()+" "+
//                 head.getWeight()+" " +
//                 head.getStyle()+" Structure = "+
//                 head.getStructure()+" StyleLevel = level"+
//                 head.getStyleLevel());
//      }
//  }
//
//   /**
//    * testing: in console
//    * prints out all the elements of the body list
//    */
//    private void printBodyList(){
//        Out.prln("printing body list");
//      for (StyleBody body: bodyList){
//         System.out.println("<level_0" + body.getStyleLevel()+"> "+
//                 body.getText());
//      }
//  }
//
//
//}