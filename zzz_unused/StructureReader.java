
package zzz_unused;


import gate.persist.PersistenceException;
import java.util.*;
import java.io.*;
import java.net.*;

import gate.*;
import gate.Corpus;
import gate.creole.*;
import gate.util.*;
import gate.corpora.RepositioningInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota on 13-Jul-2010 at 22:26:04
 */
public class StructureReader {

        /** The Corpus Pipeline application to contain ANNIE */
        private SerialAnalyserController annieController;
        private Corpus corpus;
        private String docName = "eu.pdf";
        private String regBody = "Eudralex";
        private AnnotationSet annotSet;     
        private String id;
        
        private StringBuffer editableContent;
        private String originalContent;
        private RepositioningInfo info;
        private String docContent;
        private int reposition;

        private File outXmlFile = new File (Settings.GATE_FILES_PATH+"SemiAnnotatedRegulation.xml");
        private String SECTION = "section";
        private String SECTION_TITLE = "section_title";
        private String SUBSECTION = "subsection";
        private String SUBSECTION_TITLE = "subsection_title";
        private String REGULATION = "regulation";
        private String STATEMENT = "statement";
public String getRegBody() {
        return regBody;
    }
/**
   * 
   * @throws gate.util.GateException
   * @throws java.io.IOException
   */
public StructureReader() throws GateException, IOException{
  init();
}
/**
*
* @throws gate.util.GateException
* @throws java.net.MalformedURLException
* @throws java.io.IOException
*/
private void init() throws GateException, MalformedURLException, IOException{
    initGate();     // 1. itigialize Gate library
    loadAnnie();    // 2. load Annie plugin
    initAnnie();    // 3. initialise ANNIE 
    setCorpus();    // 4. set corpus and extract with files.
    execute();      // 5. run the PR through the corpus in the ANNIE
    Out.prln("*** STRUCTURE READING COMPLETED *** ");
  }
/**
*
* @throws gate.util.GateException
*/
private void initGate() throws GateException{
   // initialise the GATE library
    Out.prln("Initialising GATE...");
    Gate.setGateHome(new File(Settings.GATE_HOME));
    Gate.init();
  }
/**
*
* @throws java.net.MalformedURLException
* @throws gate.util.GateException
*/
private void loadAnnie() throws MalformedURLException, GateException{
    // Load ANNIE plugin
    File gateHome = Gate.getGateHome();
    File pluginsHome = new File(gateHome, "plugins");
    Gate.getCreoleRegister().registerDirectories(new File(pluginsHome, "ANNIE").toURL());
  }
/**
* Initialise the ANNIE system. This creates a "corpus pipeline"
* application that can be used to run sets of documents through
* the extraction system.
*/
private void initAnnie() throws GateException, MalformedURLException {
    Out.prln("Initialising ANNIE...");

    // create a serial analyser controller to run ANNIE with
    annieController =(SerialAnalyserController) Factory.createResource(
                        "gate.creole.SerialAnalyserController",
                        Factory.newFeatureMap(),
                        Factory.newFeatureMap(),
                        "ANNIE_" + Gate.genSym() );
     FeatureMap features = Factory.newFeatureMap(); // use default parameters

     // adding Annie  PRs  : i. document reset
    features.clear();
    ProcessingResource documentReset = (ProcessingResource)
            Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR", features);
    annieController.add(documentReset);

    // adding Annie  PRs  : ii. english tokeniser
    features.clear();
    ProcessingResource englishTokeniser = (ProcessingResource)
            Factory.createResource("gate.creole.tokeniser.DefaultTokeniser", features);
    annieController.add(englishTokeniser);

    // adding Annie  PRs  : iii. regex sentence splitter
    features.clear();
    ProcessingResource regexSentenceSplitter = (ProcessingResource)
            Factory.createResource("gate.creole.splitter.RegexSentenceSplitter", features);
    annieController.add(regexSentenceSplitter);

    // adding Annie  PRs  : iv. pos tagger
    features.clear();
    ProcessingResource posTagger = (ProcessingResource)
            Factory.createResource("gate.creole.POSTagger", features);
    annieController.add(posTagger);

    // adding Annie  PRs  : v. annie gazetteer
    features.clear();
    ProcessingResource annieGazetteer = (ProcessingResource)
            Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", features);
    annieController.add(annieGazetteer);

    Out.prln("...ANNIE loaded");
    Out.prln("Loading other PR...");

    // 1. adding default annie gazetteer with my lists for obligation
    features.clear();
    features.put("listsURL", getURL("gazetteer/lists.def"));
    ProcessingResource myAnnieGaz = (ProcessingResource)
            Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", features);
    annieController.add(myAnnieGaz);

    //2. adding  jape transducer for my jape grammar
    features.clear();
    features.put("grammarURL", getURL("jape/main.jape"));
    ProcessingResource myJape = (ProcessingResource)
            Factory.createResource("gate.creole.Transducer", features);
    annieController.add(myJape);

} // initAnnie()
/**
* 
* @throws gate.creole.ResourceInstantiationException
* @throws java.net.MalformedURLException
*/
private void setCorpus() throws ResourceInstantiationException, MalformedURLException{
  corpus = Factory.newCorpus("");
  URL u = getURL(docName);
  FeatureMap params = Factory.newFeatureMap();
  params.put("sourceUrl", u);
  params.put("preserveOriginalContent", true);
  params.put("collectRepositioningInfo", true);
  Out.prln("Creating doc for " + u);
  Document doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);
  corpus.add(doc);
  annieController.setCorpus(corpus);
}
/**
* Run ANNIE over the corpus
*/
private void execute() throws GateException {
    Out.prln("Running ANNIE...");
    annieController.execute();
    Out.prln("...ANNIE executtion completed");
        
  } // execute()

//------------------------------- X (End of initialization ) --------------------

/**
   * extract()
   * |_extractTopicList()
   *   |_extractSubtopicList()
   *     |_extractRegulationList()
   * It populates the topic list which involves populating lists inside it in
   * separate methods as structured above.
   */
public void extract() throws IOException{
  //System.out.println("inside getRegulationList......................");//------------ test--------
   // i. iterate for each document in the corpus
    Iterator iterDocs = corpus.iterator();
    int count = 0;  
    while(iterDocs.hasNext()) {
        //System.out.println("inside iterDocs......................");//------------ test--------
          Document annotatedDoc        = (Document) iterDocs.next();
          annotSet   = annotatedDoc.getAnnotations();
          docContent = annotatedDoc.toString();
          DocumentContent content = annotatedDoc.getContent(); //TODO write document content in html

          FeatureMap features      = annotatedDoc.getFeatures();
          originalContent   = (String) features.get(GateConstants.ORIGINAL_DOCUMENT_CONTENT_FEATURE_NAME);
          info   = (RepositioningInfo) features.get(GateConstants.DOCUMENT_REPOSITIONING_INFO_FEATURE_NAME);
          ++count;
          editableContent = new StringBuffer(content.toString());
          insertText(0,"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?> \n" +
                  "<?xml-stylesheet type=\"text/xsl\" href=\"standard_reg.xsl\"?>\n"+
                  "<document>\n" +
                  "<meta>\n"+
                    "<name> Eudralex</name>\n"+
                    "<description> EU regulation for the pharmaceutical industry </description>\n"+
                    "<body>EMEA</body>\n"+
                    "<version> 1.0 </version>\n"+
                    "<published_on> 2007 </published_on>\n"+
                "</meta>\n");
         

          Iterator<Annotation> topicIter = getSortedAnnIter(annotSet.get("TopicSection"));
          extractTopicList(topicIter);
//          insertText(editableContent.length()-2, "</document>");
          editableContent.append("</document>");

          FileWriter writer = new FileWriter(outXmlFile);
          writer.write(editableContent.toString());
          writer.close();
                      
    }//while
}
private void extractTopicList(Iterator<Annotation> topicIter) throws IOException{

      // ii. iterates through each topic
          while (topicIter.hasNext()){
              Annotation topicAnnot = (Annotation) topicIter.next();
              insertStartTag(SECTION,topicAnnot);
                  AnnotationSet topicContainedSet = annotSet.getContained(topicAnnot.getStartNode().getOffset(),
                          topicAnnot.getEndNode().getOffset());
                  Iterator<Annotation> topicTitleIter = getSortedAnnIter(topicContainedSet.get("TopicTitle"));
                  Annotation topicTitle = null;
                  if (topicTitleIter.hasNext()){
                      topicTitle = topicTitleIter.next();                      
                      insertStartTag(SECTION_TITLE, topicTitle);
                      insertEndTag(SECTION_TITLE, topicTitle);
                  }
                  Iterator<Annotation> subtopicIter = getSortedAnnIter(topicContainedSet.get("SubtopicSection"));
                  extractSubtopicList(subtopicIter);
               insertEndTag(SECTION,topicAnnot);
           }// topic iterator
  }
private void extractSubtopicList(Iterator<Annotation> subtopicIter){
      // iii. iterates through each subtopic
      while (subtopicIter.hasNext()){
          //System.out.println("                    inside subtopicIter......................");//------------ test--------
          Annotation subtopicAnnot = (Annotation) subtopicIter.next();
          insertStartTag(SUBSECTION, subtopicAnnot);
              AnnotationSet subtopicContainedSet = annotSet.getContained(subtopicAnnot.getStartNode().getOffset(),
                      subtopicAnnot.getEndNode().getOffset());
              Iterator<Annotation> subTitleIter = getSortedAnnIter(subtopicContainedSet.get("SubtopicTitle"));
              if (subTitleIter.hasNext()){
                   Annotation subtopicTitle = subTitleIter.next();
                   insertStartTag(SUBSECTION_TITLE, subtopicTitle);
                   insertEndTag(SUBSECTION_TITLE, subtopicTitle);
              }// if
              Iterator<Annotation> regIter = getSortedAnnIter(subtopicContainedSet.get("RegSection"));
              extractRegulationList(regIter);
          insertEndTag(SUBSECTION, subtopicAnnot);
      }
  }
private void extractRegulationList(Iterator<Annotation> regIter){

      // iv. iterates through each regulation paragraph
      while (regIter.hasNext()){
          Annotation regAnnot = (Annotation) regIter.next();

          // specifies the section of the text to be used for searching the annotations
          AnnotationSet regContainedSet = annotSet.getContained(regAnnot.getStartNode().getOffset(),
                  regAnnot.getEndNode().getOffset());
          Iterator<Annotation> stmtIter = getSortedAnnIter(regContainedSet.get("Modified_Sentence"));
          insertStartTag(REGULATION, regAnnot);
            extractRegStatementList(stmtIter);
         insertEndTag(REGULATION, regAnnot);

      }
  }
private void extractRegStatementList(Iterator<Annotation> stmtIter) {
    // iterates through each regulation statement
    while (stmtIter.hasNext()){
        Annotation stmtAnnot = stmtIter.next();
        insertStartTag(STATEMENT, stmtAnnot);
        insertEndTag(STATEMENT, stmtAnnot);
    }// stmt iterator
} 
/**
 * 
 * @param fileName
 * @return
 * @throws java.net.MalformedURLException
 */
private URL getURL(String fileName) throws MalformedURLException{
    String filePathAndName = Settings.GATE_FILES_PATH+fileName;
    File textFile = new File(filePathAndName);
    return textFile.toURI().toURL();
  }
public ArrayList getSortedUniqueList(ArrayList aList){
    // making a unique array list
    HashSet h = new HashSet(aList);
    aList.clear();
    aList.addAll(h);

    // sorting
    Collections.sort(aList);

    return aList;
}
/**
   *
   * @param annSet
   * @return
   */
private Iterator<Annotation> getSortedAnnIter(Set<Annotation> annSet){
      // iii. sorting selected annotation
        Iterator iterAnnot = annSet.iterator();
        SortedAnnotationList sortedAnnotations = new SortedAnnotationList();
        Annotation currAnnot = null;
        System.out.println("Sorting annotations ....");
        while(iterAnnot.hasNext()) {
              currAnnot = (Annotation) iterAnnot.next();
              sortedAnnotations.addSortedExclusive(currAnnot);
            } // while

        System.out.println(".... sorting completed. Total annotations = "+sortedAnnotations.size());
        return sortedAnnotations.iterator();
  }
/**
 *  Why sorting?
 *  It helps to distinguish reg no, subject and action of a regulation from others. It is not an
 *  appropriate way of achieving this, but it does the job as it sorts by the starting 
 *  node of the annotations.
 */
private static class SortedAnnotationList extends Vector {
        private SortedAnnotationList() {
          super();
        } // SortedAnnotationList

        private boolean addSortedExclusive(Annotation annot) {
          Annotation currAnot = null;
          long annotStart = annot.getStartNode().getOffset().longValue();
          long currStart;
          // insert
          for (int i=0; i < size(); ++i) {
                currAnot = (Annotation) get(i);
                currStart = currAnot.getStartNode().getOffset().longValue();
                if(annotStart < currStart) {
                      insertElementAt(annot, i);
                      return true;
                } // if
          } // for
          int size = size();
          insertElementAt(annot, size);
          return true;
        } // addSorted
  } // SortedAnnotationList
private void insertStartTag(String tagName, Annotation annot){
      int startNode = annot.getStartNode().getOffset().intValue();
      String startTag = "<"+tagName+"> ";
      insertText(startNode, startTag);
 }
private void insertEndTag(String tagName, Annotation annot){
  int endNode = annot.getEndNode().getOffset().intValue();
  String endTag = " </"+tagName+">\n";
  insertText(endNode, endTag);
}
private void insertText(int position, String text){
        editableContent.insert(reposition+ position,text);
        reposition = reposition + text.length();
  }
}