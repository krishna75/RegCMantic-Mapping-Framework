
package api.gt;

import java.io.*;
import java.net.*;

import gate.*;
import gate.Corpus;
import gate.creole.*;
import gate.util.*;
import rcm.main.Settings;
import helper.util.Converter;
import helper.util.MyWriter;
import helper.util.Print;

/**
 *
 * @author Krishna Sapkota, 19-Sep-2011,   10:45:47
 * A PhD project at Oxford Brookes University
 */
public abstract class AbstractAnnotator {
        /**  gate related: */
        private String docName          = "";
        private String regBody          = "";
        private String japeSentence     = "";
        private String japeEntity       = "";
        private String gazetteer        = "";
        private String reqAnnFileName   = "";
        private String outCorpusFile    = "";

        private SerialAnalyserController annieController;
        private Corpus corpus;

public AbstractAnnotator() {
    init();
}
/**
* Initialization process five processes which are commented below.
*/
private void init() {
    
    /* 1. initialisze GATE library */
    initGate();     
    /* 2. load Annie plugin */
    loadAnnie();   
    /* 3. add processing resources */
    addPR();        
    /* 4. set corpus and files. */
    setCorpus();   
    /* 5. run the PR through the corpus in the ANNIE */
    execute();     
    
    Out.prln("annotation completed ....");
    MyWriter.write(corpus, outCorpusFile);
    Out.prln("*** ENTITY ANNOTATION COMPLETED *** ");
  }
/**
   * Initializes the GATE library
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
* Initialize the ANNIE system.
* This is where we add all the required processing resources e.g. jape, gazetteer etc.
*/
private void addPR(){

    try {
        Print.prln("Initialising ANNIE...");

        /*create a serial analyser controller to run ANNIE with */
        annieController = (SerialAnalyserController) Factory.createResource(
                        "gate.creole.SerialAnalyserController",
                        Factory.newFeatureMap(),
                        Factory.newFeatureMap(),
                        "ANNIE_" + Gate.genSym());
        /* use default parameters */
        FeatureMap features = Factory.newFeatureMap(); 

        /*adding Annie  PRs  : i. document reset */
        features.clear();
        ProcessingResource documentReset = (ProcessingResource)
                Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR", features);
        annieController.add(documentReset);

        /* adding Annie  PR  : ii. english tokeniser */
        features.clear();
        ProcessingResource englishTokeniser = (ProcessingResource)
                Factory.createResource("gate.creole.tokeniser.DefaultTokeniser", features);
        annieController.add(englishTokeniser);

        /* adding Annie  PRs  : vi.. regex sentence splitter */
        features.clear();
        ProcessingResource regexSentenceSplitter = (ProcessingResource)
                Factory.createResource("gate.creole.splitter.RegexSentenceSplitter", features);
        annieController.add(regexSentenceSplitter);

        /* adding inhouse built PR: v.  jape to identify sentences */
        features.clear();
        features.put("grammarURL", Converter.getURL(japeSentence));
        ProcessingResource prSentence = (ProcessingResource)
                Factory.createResource("gate.creole.Transducer", features);
        annieController.add(prSentence);

        /* ading inhouse built PR:  iii..   gazetteer */
        features.clear();
        features.put("listsURL", Converter.getURL(gazetteer));
        ProcessingResource myAnnieGaz = (ProcessingResource)
                Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", features);
        annieController.add(myAnnieGaz);

        /* adding Annie  PRs  :vii. pos tagger */
        features.clear();
        ProcessingResource posTagger = (ProcessingResource)
                Factory.createResource("gate.creole.POSTagger", features);
        annieController.add(posTagger);

        /* ading inhouse built PR: viii.  jape grammar 2 */
        features.clear();
        features.put("grammarURL", Converter.getURL(japeEntity));
        ProcessingResource prEntity = (ProcessingResource)
                Factory.createResource("gate.creole.Transducer", features);
        annieController.add(prEntity);
        Print.prln("...PR loaded");
        features.clear();
    }
    catch (ResourceInstantiationException ex) {
        Out.prln(ex);
    }
  } 

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
