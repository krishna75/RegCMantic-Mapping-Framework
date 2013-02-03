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
import rcm.main.Settings;
import helper.reg.Obligation;
import helper.reg.Regulation;
import helper.reg.Statement;
import helper.reg.Topic;
import helper.util.Converter;
import helper.util.MyWriter;
import helper.util.Print;
import helper.util.Sorter;


/**
 * Extracts the information for the classes under the package reg
 * (i.e. topic, regulation, statement, subject, obligation, action etc.)
 *  Expected information flow : 1. EntityAnnotator_v2 -> 2. EntityExtractor_v2 -> 3.SemReg_OntologyPopulator_v3
 * @author Krishna Sapkota on 13-Jul-2010 at 22:26:04
 */
public class EntityAnnotator_V2 implements Serializable {

        /**  gate related: */
        private String docName = Settings.STYLES_PATH+"reg_v2.xml";
        private String regBody = "Eudralex";
        private String jape1 = Settings.JAPE_PATH+"jape4xmlregulation/main.jape";
        private String jape2 = Settings.JAPE_PATH+"jape4xmlregulation2/main.jape";
        private String gazetteer = Settings.GAZETTEER_PATH+"gazetteer_general/lists.def";
        private String reqAnnFileName = Settings.GATE_FILES_PATH + "required_annotation_list.txt";
        private String outCorpusFile = Settings.FILES_PATH+"entity_annotator_v2.ser";
        
        private SerialAnalyserController annieController;
        private Corpus corpus;

/**
*  This uses GATE and NLP  to extract information for SemReg ontology.
*/
public EntityAnnotator_V2(){
      init();
      //TODO modify reg_v2.xml to deal with paragraphs. at the moment, it only works if this file has only regulations.
  }

/**
* Initialisation process five processes which are commented below.
*/
private void init() {
    initGate();     // 1. initialize Gate library
    loadAnnie();    // 2. load Annie plugin
    addPR();        // 3. initialise ANNIE
    setCorpus();    // 4. set corpus and extract with files.
    execute();      // 5. run the PR through the corpus in the ANNIE
    Out.prln("annotation completed ....");
    MyWriter.write(corpus, outCorpusFile);
    Out.prln("*** ENTITY ANNOTATION COMPLETED *** ");
  }

  /**
   * Initialises the GATE library
   */
  private void initGate() {
    
    if (!Gate.isInitialised() && Gate.getGateHome() == null){
        Print.prln("Initialising GATE...");
        Gate.setGateHome(new File(Settings.GATE_HOME));
        try {
            Gate.init();
        } catch (GateException ex) {
            ex.printStackTrace();
        }
    }
  }

/**
* Loads ANNIE plug-in
*/
private void loadAnnie(){
    File gateHome = Gate.getGateHome();
    File pluginsHome = new File(gateHome, "plugins");
    try {
        Gate.getCreoleRegister().registerDirectories
                (new File(pluginsHome, "ANNIE").toURL());
    } catch (MalformedURLException ex) {
        ex.printStackTrace();
    } catch (GateException ex) {
        ex.printStackTrace();
    }
  }
/**
* Initialise the ANNIE system.
* This is where we add all the required processing resources e.g. jape, gazetteer etc.
*/
private void addPR(){
        
    try {
        Print.prln("Initialising ANNIE...");

        // create a serial analyser controller to run ANNIE with
        annieController = (SerialAnalyserController) Factory.createResource(
                        "gate.creole.SerialAnalyserController",
                        Factory.newFeatureMap(),
                        Factory.newFeatureMap(),
                        "ANNIE_" + Gate.genSym());
        FeatureMap features = Factory.newFeatureMap(); // use default parameters

        // adding Annie  PRs  : i. document reset
        features.clear();
        ProcessingResource documentReset = (ProcessingResource)
                Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR", features);
        annieController.add(documentReset);

        // adding Annie  PR  : ii. english tokeniser
        features.clear();
        ProcessingResource englishTokeniser = (ProcessingResource)
                Factory.createResource("gate.creole.tokeniser.DefaultTokeniser", features);
        annieController.add(englishTokeniser);

        // ading inhouse built PR:  iii..   gazetteer
        features.clear();
        features.put("listsURL", Converter.getURL(gazetteer));
        ProcessingResource myAnnieGaz = (ProcessingResource)
                Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", features);
        annieController.add(myAnnieGaz);

        // adding Annie  PR  : iv. fake sentence splitter
        features.clear();
        ProcessingResource fakeSentenceSplitter = (ProcessingResource)
                Factory.createResource("gate.clone.ql.FakeSentenceSplitter", features);
        annieController.add(fakeSentenceSplitter);

        //. ading inhouse built PR: v.  jape grammar 1
        features.clear();
        features.put("grammarURL", Converter.getURL(jape1));
        ProcessingResource myJape1 = (ProcessingResource)
                Factory.createResource("gate.creole.Transducer", features);
        annieController.add(myJape1);

        // adding Annie  PRs  : vi.. regex sentence splitter
        features.clear();
        ProcessingResource regexSentenceSplitter = (ProcessingResource)
                Factory.createResource("gate.creole.splitter.RegexSentenceSplitter", features);
        annieController.add(regexSentenceSplitter);

        // adding Annie  PRs  :vii. pos tagger
        features.clear();
        ProcessingResource posTagger = (ProcessingResource)
                Factory.createResource("gate.creole.POSTagger", features);
        annieController.add(posTagger);

        // ading inhouse built PR: viii.  jape grammar 2
        features.clear();
        features.put("grammarURL", Converter.getURL(jape2));
        ProcessingResource myJape2 = (ProcessingResource)
                Factory.createResource("gate.creole.Transducer", features);
        annieController.add(myJape2);
        Print.prln("...PR loaded");
        features.clear();
    }
    catch (ResourceInstantiationException ex) {
        Out.prln(ex);
    }
  } // addPR()
  
/**
*  sets the corpus adds the documents
*/
private void setCorpus(){
      URL u = Converter.getURL(docName);
      FeatureMap params = Factory.newFeatureMap();
      params.put("sourceUrl", u);
      params.put("markupAware", true);
      params.put("preserveOriginalContent", false);
      params.put("collectRepositioningInfo", false);
      params.put("encoding","windows-1252");
      Print.prln("Creating doc for " + u);
      Document doc = null;
        try {
            corpus = Factory.newCorpus("");
            doc = (Document)
                    Factory.createResource("gate.corpora.DocumentImpl", params);
        } catch (ResourceInstantiationException ex) {
            ex.printStackTrace();
        }
      corpus.add(doc);
      annieController.setCorpus(corpus);
      params.clear();
  }


/**
* Run ANNIE over the corpus
*/
private void execute() {
    Print.pr("Running ANNIE...");
        try {
            annieController.execute();
        } catch (ExecutionException ex) {
            Out.prln(ex);
      }
    Print.prln("...ANNIE execution completed");


    
  } // execute()
public Corpus getCorpus() {
        return corpus;
    }
public String getRegBody() {
    return regBody;
}



}