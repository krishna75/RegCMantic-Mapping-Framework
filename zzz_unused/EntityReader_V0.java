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
import rcm.main.Settings;
import helper.reg.Obligation;
import helper.reg.Regulation;
import helper.reg.Statement;
import zzz_unused.zzz_SubTopic;
import zzz_unused.zzz_Topic;
import helper.util.Converter;
import helper.util.Sorter;

/**
 *
 * @author Krishna Sapkota on 13-Jul-2010 at 22:26:04
 */
public class EntityReader_V0 {

        /** The Corpus Pipeline application to contain ANNIE */
        private SerialAnalyserController annieController;
        private Corpus corpus;
//        private String docName = "eu.pdf";
        private String fileName = Settings.GATE_FILES_PATH+"eu.pdf";
        private String regBody = "Eudralex";
        private ArrayList<Statement> stmtList;
        private ArrayList<Regulation> regulationList;
        private ArrayList<zzz_SubTopic> subtopicList;
        private ArrayList<zzz_Topic> topicList = new ArrayList<zzz_Topic>();
        private AnnotationSet annotSet;
        private zzz_Topic topic;
        private zzz_SubTopic subtopic;
        private Regulation regulation;
        private Statement stmt;
        private String id;
public String getRegBody() {
        return regBody;

    }     
  /**
   *  This uses GATE application and NLP processes specifically designed to extract
   * information for SemReg ontology.
   * @throws gate.util.GateException
   * @throws java.io.IOException
   */
  public EntityReader_V0() throws GateException, IOException{
      init();
  }
/**
* Initialisation process five processes which are commented below.
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
    Out.prln("*** ENTITY READING COMPLETED *** ");
  }
/**
* Initialises the GATE library
* @throws gate.util.GateException
*/
private void initGate() throws GateException{
    Out.prln("Initialising GATE...");
    Gate.setGateHome(new File(Settings.GATE_HOME));
    Gate.init();
  }

/**
* Loads ANNIE plug-in
* @throws java.net.MalformedURLException
* @throws gate.util.GateException
*/
private void loadAnnie() throws MalformedURLException, GateException{
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
        features.put("listsURL", Converter.getURL("gazetteer/lists.def"));
        ProcessingResource myAnnieGaz = (ProcessingResource)
                Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", features);
        annieController.add(myAnnieGaz);

        //2. adding  jape transducer for my jape grammar 
        features.clear();
        features.put("grammarURL", Converter.getURL("jape/main.jape"));
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
      URL u = Converter.getURL(fileName);
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
*  separate methods as structured above.
*/
public void extract(){
  //System.out.println("inside getRegulationList......................");//------------ test--------
   // i. iterate for each document in the corpus
    Iterator iterDocs = corpus.iterator();
    int count = 0;  
    while(iterDocs.hasNext()) {
        //System.out.println("inside iterDocs......................");//------------ test--------
          Document annotDoc        = (Document) iterDocs.next();
          annotSet   = annotDoc.getAnnotations();
          FeatureMap features      = annotDoc.getFeatures();
          String originalContent   = (String) features.get(GateConstants.ORIGINAL_DOCUMENT_CONTENT_FEATURE_NAME);
          RepositioningInfo info   = (RepositioningInfo) features.get(GateConstants.DOCUMENT_REPOSITIONING_INFO_FEATURE_NAME);
          ++count;
   
          Iterator<Annotation> topicIter = Sorter.getSortedAnnIter(annotSet.get("TopicSection"));
          extractTopicList(topicIter);

                      
    }
}

private void extractTopicList(Iterator<Annotation> topicIter){

  // ii. iterates through each topic
      while (topicIter.hasNext()){
          topic = new zzz_Topic();
          Annotation topicAnnot = (Annotation) topicIter.next();
          AnnotationSet topicContainedSet = annotSet.getContained(topicAnnot.getStartNode().getOffset(),
                  topicAnnot.getEndNode().getOffset());
          Annotation topicNo = topicContainedSet.get("TopicNo").iterator().next();
          Iterator<Annotation> topicTitleIter = topicContainedSet.get("TopicTitle").iterator();
          String title = "na";
           if (topicTitleIter.hasNext()){
               Annotation topicTitle = topicTitleIter.next();
               title = topicTitle.getFeatures().get("string").toString();

             //====== extracting subject annotations =============================
              Iterator<Annotation> topicAnnotIter = annotSet.getContained(topicTitle.getStartNode().getOffset(),
                    topicTitle.getEndNode().getOffset()).get("PotentialSubject").iterator();
              ArrayList<String> topicAnnotationList = new ArrayList<String>();
              while (topicAnnotIter.hasNext()){
                  Annotation topicAnnotation = topicAnnotIter.next();
                  topicAnnotationList.add(topicAnnotation.getFeatures().get("string").toString());
              }
              topic.getSubjectAnnotationList().addAll(Sorter.getSortedUniqueList(topicAnnotationList));
             //--------------- X -----------------------------------------------
           }

          String id = topicNo.getFeatures().get("string").toString();
          String desc = topicAnnot.getFeatures().get("string").toString();

          topic.setId(id);
          topic.setTitle(title);
          topic.setDesc(desc);
          topic.setRegBody(regBody);



          Iterator<Annotation> subtopicSet = topicContainedSet.get("SubtopicSection").iterator();
          extractSubtopicList(subtopicSet);
          topicList.add(topic);
       }// topic iterator
}

private void extractSubtopicList(Iterator<Annotation> subtopicIter){
       subtopicList = new ArrayList<zzz_SubTopic>();

      // iii. iterates through each subtopic
      while (subtopicIter.hasNext()){
          subtopic = new zzz_SubTopic();
          //System.out.println("                    inside subtopicIter......................");//------------ test--------
          Annotation subtopicAnnot = (Annotation) subtopicIter.next();
          AnnotationSet subtopicContainedSet = annotSet.getContained(subtopicAnnot.getStartNode().getOffset(),
                  subtopicAnnot.getEndNode().getOffset());
          Annotation subtopicNo = subtopicContainedSet.get("SubtopicNo").iterator().next();
          Iterator<Annotation> subTitleIter = subtopicContainedSet.get("SubtopicTitle").iterator();
          
          String title = "na";
          if (subTitleIter.hasNext()){
               Annotation subtopicTitle = subTitleIter.next();
               title = subtopicTitle.getFeatures().get("string").toString();

                 //====== extracting subject annotations =============================
                  Iterator<Annotation> subtopicAnnotIter = annotSet.getContained(subtopicTitle.getStartNode().getOffset(),
                        subtopicTitle.getEndNode().getOffset()).get("PotentialSubject").iterator();
                  ArrayList<String> subtopicAnnotationList = new ArrayList<String>();
                  while (subtopicAnnotIter.hasNext()){
                      Annotation subtopicAnnotation = subtopicAnnotIter.next();
                      subtopicAnnotationList.add(subtopicAnnotation.getFeatures().get("string").toString());
                  }
                  subtopic.getSubjectAnnotationList().addAll(Sorter.getSortedUniqueList(subtopicAnnotationList));
                 //--------------- X -----------------------------------------------
          }// if

          String id = subtopicNo.getFeatures().get("string").toString();
          String desc = subtopicAnnot.getFeatures().get("string").toString();
          
          subtopic.setId(id);
          subtopic.setTitle(title);
          subtopic.setDesc(desc);

          Iterator<Annotation> regIter = subtopicContainedSet.get("RegSection").iterator();
          extractRegulationList(regIter);
          subtopicList.add(subtopic);
      }
        topic.setSubtopicList(subtopicList);
  }

private void extractRegulationList(Iterator<Annotation> regIter){
        regulationList = new ArrayList<Regulation>();

      // iv. iterates through each regulation paragraph
      while (regIter.hasNext()){
          regulation = new Regulation();
          Annotation regAnnot = (Annotation) regIter.next();

          // specifies the section of the text to be used for searching the annotations
          AnnotationSet regContainedSet = annotSet.getContained(regAnnot.getStartNode().getOffset(),
                  regAnnot.getEndNode().getOffset());
          Annotation regID = regContainedSet.get("RegNo").iterator().next();
          Iterator<Annotation> stmtIter = regContainedSet.get("Modified_Sentence").iterator();

          // defines id and description
          id = regID.getFeatures().get("string").toString();
          String desc = regAnnot.getFeatures().get("string").toString();

          // sets id and description
          regulation.setId(id);
          regulation.setDescription(desc);
          regulationList.add(regulation);
          extractRegStatementList(stmtIter);
      }
        subtopic.setRegulationList(regulationList);
  }

private void extractRegStatementList(Iterator<Annotation> stmtIter) {
    stmtList = new ArrayList<Statement>();
    Obligation obl = new Obligation();
    int n = 1;
    // iterates through each regulation statement
    while (stmtIter.hasNext()){
        stmt = new Statement();
        Annotation stmtAnnot = stmtIter.next();
        AnnotationSet stmtContainedSet = annotSet.getContained(stmtAnnot.getStartNode().getOffset(),
                stmtAnnot.getEndNode().getOffset());
        Iterator<Annotation> subjectIter = stmtContainedSet.get("Subject").iterator();
        Iterator<Annotation> actionIter = stmtContainedSet.get("Action").iterator();
        Iterator<Annotation> oblIter = stmtContainedSet.get("Obligation").iterator();

        if (oblIter.hasNext()){
            Annotation oblAnnot = oblIter.next();
            obl.setType(oblAnnot.getFeatures().get("type").toString());
            obl.setStrength(oblAnnot.getFeatures().get("strength").toString());
            obl.setDescription(oblAnnot.getFeatures().get("string").toString());
        }
        String stmt_id = id+"_S"+n++;
        String desc = stmtAnnot.getFeatures().get("string").toString();

        stmt.setId(stmt_id);

        stmt.setDescription(desc);
        stmt.setObligation(obl);

        ArrayList<String> subjectList = new ArrayList<String>();
        // v. iterates through each subject in a paragraph of regulation
        while (subjectIter.hasNext()){
            Annotation subjectAnnot = (Annotation) subjectIter.next();
            String subject = subjectAnnot.getFeatures().get("string").toString();
            subjectList.add(subject);
        }
        stmt.getSubAnnList().addAll(
          Sorter.getSortedUniqueList(subjectList));

        ArrayList<String> actionList = new ArrayList<String>();
        // vi. iterates through each action in a paragraph of regulation
        while (actionIter.hasNext()){
            Annotation actionAnnot = (Annotation) actionIter.next();
            String action = actionAnnot.getFeatures().get("string").toString();
            actionList.add(action);
        }
        stmt.getActAnnList().addAll(
                  Sorter.getSortedUniqueList(actionList));
        stmtList.add(stmt);
    }// stmt iterator
    regulation.setStmtList(stmtList);
}

public ArrayList<zzz_Topic> getTopicList() {
        return topicList;
    }
}