
package rcm.phase2;


import gate.*;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import gate.util.Out;
import helper.util.Converter;
import helper.util.MyWriter;
import helper.util.Print;
import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;


/**
 * Extracts the information for the classes under the package reg
 * (i.e. topic, regulation, statement, subject, obligation, action etc.)
 *  Expected information flow : 1. EntityAnnotator -> 2. EntityReader-> 3.SemReg_Populator
 * @author Krishna Sapkota on 13-Jul-2010 at 22:26:04
 */
public class _1_EntityAnnotator_V3 implements Serializable {

        /* IO FILES */
        private String IN_DOC_FILE          = Settings.PHASE1+"styles/reg_xml.xml";
        private String IN_REQUIRED_ANN_FILE   = Settings.PHASE2+ "required_annotation_list.txt";
        private String OUT_CORPUS_FILE    = Settings.PHASE2+"entity_annotator_v3.ser";
        
        // gate related
        private String japeSentence     = Settings.JAPE_PATH+"sentence/main.jape";
        private String japeEntity       = Settings.JAPE_PATH+"entity/main.jape";
        private String gazetteer        = Settings.GAZETTEER_PATH+"gazetteer_general/lists.def";
        
        /* external variables */
        private SerialAnalyserController annieController;
        private Corpus corpus;
        private FeatureMap features;
        
        /* local varibales*/
        private String regBody          = "Eudralex";

/**
*  This uses GATE and NLP  to extract information for SemReg ontology.
*/
public _1_EntityAnnotator_V3(){
      init();
  }

/**
* Initialisation process five processes which are commented below.
*/
private void init() {
    initGate();     // 1. initialize Gate library
    loadAnnie();    // 2. load Annie plugin
    addProcessingRecourses();        // 3. initialise ANNIE
    setCorpus();    // 4. set corpus and extract with files.
    execute();      // 5. run the PR through the corpus in the ANNIE
    Out.prln("annotation completed ....");
    MyWriter.write(corpus, OUT_CORPUS_FILE);
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
* Initialize the ANNIE system.
* This is where we add all the required processing resources e.g. jape, gazetteer etc.
*/
private void addProcessingRecourses(){
        
    try {
        Print.prln("Initialising ANNIE...");

        // create a serial analyser controller to run ANNIE with
        annieController = (SerialAnalyserController) Factory.createResource(
                        "gate.creole.SerialAnalyserController",
                        Factory.newFeatureMap(),
                        Factory.newFeatureMap(),
                        "ANNIE_" + Gate.genSym());
        features = Factory.newFeatureMap(); // use default parameters

        // adding Annie  PRs  : i. document reset
        addPR("gate.creole.annotdelete.AnnotationDeletePR", features);  
        
        // adding Annie  PR  : ii. english tokeniser
        addPR("gate.creole.tokeniser.DefaultTokeniser", features);
        
        // adding Annie  PRs  : vi.. regex sentence splitter
        addPR("gate.creole.splitter.RegexSentenceSplitter", features);
        
        //. ading inhouse built PR: v.  jape to identify sentences
        features.put("grammarURL", Converter.getURL(japeSentence));
        addPR("gate.creole.Transducer", features);
        
        // ading inhouse built PR:  iii..   gazetteer
        features.put("listsURL", Converter.getURL(gazetteer));
        addPR("gate.creole.gazetteer.DefaultGazetteer", features);
        
        // adding Annie  PRs  :vii. pos tagger
        addPR("gate.creole.POSTagger", features);
        
        // ading inhouse built PR: viii.  jape grammar 2
        features.put("grammarURL", Converter.getURL(japeEntity));
        addPR("gate.creole.Transducer", features);

        Print.prln("...PR loaded");
    }
    catch (ResourceInstantiationException ex) {
        Out.prln(ex);
    }
  } // addPR()
  
/**
*  sets the corpus adds the documents
*/
private void setCorpus(){
    
      // (1.)  set features for the document
      URL u = Converter.getURL(IN_DOC_FILE);
      features.put("sourceUrl", u);
      features.put("markupAware", true);
      features.put("preserveOriginalContent", true);
      features.put("collectRepositioningInfo", true);
      features.put("encoding","utf-8");
      
      // ( 2. ) creates the document
      Print.prln("Creating doc for " + u);
      Document doc = null;
      try {
        corpus = Factory.newCorpus("");
        doc = (Document)
                Factory.createResource("gate.corpora.DocumentImpl", features);
      } catch (ResourceInstantiationException ex) {
        ex.printStackTrace();
      }
        
      // (3. ) add doc and corpus to the annie  
      corpus.add(doc);
      annieController.setCorpus(corpus);
      features.clear();
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

// reusable method.  used repeatedly in addProcessingResources (up)
private void addPR(String prName, FeatureMap features){
    try {
        ProcessingResource pr = (ProcessingResource) Factory.createResource(prName, features);
        annieController.add(pr);
        features.clear();
    } catch (ResourceInstantiationException ex) {
        Out.prln(ex);;
    }
}

}