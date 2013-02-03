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
import gate.corpora.DocumentContentImpl;
import gate.creole.*;
import gate.util.*;
import rcm.main.Settings;
import helper.util.Converter;
import helper.util.MyWriter;

/**
 *  It extracts GATE annotated information
 *  Expected information flow: 1. FeatureAnnotation -> 2. FeatureReader -> 3. StructurePredictor -> 4. StyleSelector
 *  @author Krishna Sapkota on 13-Jul-2010 at 22:26:04
 */
public class ParagraphAnnotator implements Serializable {

        /* The Corpus Pipeline application to contain ANNIE */
        private SerialAnalyserController annieController;
        private Corpus corpus;
        private String paragraphContent;

        /* it will be used to read all  files names starting with 'pg_000' and ending with '.htm' e.g. pg_0001.htm */
//        private String filePrefix = Settings.GATE_FILES_PATH+"eu5_multiple/pg_000";
//        private String fileSuffix = ".htm";
//        private int totalPages = 7 ;
//        private String regBody = "Eudralex";
//        private String outCorpusFile = Settings.FILES_PATH+"feature_annotated_corpus.ser";
//        private String gazetterDefinitionFile = Settings.GAZETTEER_PATH+"gazetteer_html/lists.def";
        private String japeMainFile = Settings.JAPE_PATH+"paragraph/main.jape";

  /**
   * Constructor, which starts with "init()" method.
   * @throws gate.util.GateException
   * @throws java.io.IOException
   */
  public ParagraphAnnotator(String paragraphContent) {
      this.paragraphContent = paragraphContent;
      init();

     
  }

  /**
   * initialises the gate and its required plugins and executes the processes.
   * @throws gate.util.GateException
   * @throws java.net.MalformedURLException
   * @throws java.io.IOException
   */
  private void init() {
    initGate();     // 1. itigialize Gate library
    loadAnnie();    // 2. load Annie plugin
    loadPR();       // 3. loads PR
    setCorpus();    // 4. set corpus and extract with files.
    execute();      // 5. run the PR through the corpus in the ANNIE
    //Out.prln("annotation completed ....");
   //MyWriter.write(corpus, outCorpusFile);
    Out.prln("*** PARAGRAPH ANNOTATION COMPLETED *** ");
  }

  /**
   * sets the path of the gate installation and initialise the gate library.
   * @throws gate.util.GateException
   */
  private void initGate() {
    if (!Gate.isInitialised() && Gate.getGateHome() == null){
        Out.prln("Initialising GATE...");
        Gate.setGateHome(new File(Settings.GATE_HOME));
        try {
            Gate.init();
        } catch (GateException ex) {
            ex.printStackTrace();
        }
    }
  }

  /**
   * set the path for ANNIE plugin and loads it.
   * @throws java.net.MalformedURLException
   * @throws gate.util.GateException
   */
  private void loadAnnie() {
    try {
        File gateHome = Gate.getGateHome();
        File pluginsHome = new File(gateHome, "plugins");
        Gate.getCreoleRegister().registerDirectories
                (new File(pluginsHome, "ANNIE").toURL());
    } catch (MalformedURLException ex) {
        ex.printStackTrace();;
    } catch (GateException ex) {
        ex.printStackTrace();
    }
  }

  /**
   * Initialise the ANNIE system. This creates a "corpus pipeline"
   * application that can be used to run sets of documents through
   * the extraction system.
   */
  private void loadPR()  {

        try {
            Out.prln("Initialising ANNIE...");
            // create a serial analyser controller to run ANNIE with
            annieController = (SerialAnalyserController) Factory.createResource(
                            "gate.creole.SerialAnalyserController",
                            Factory.newFeatureMap(),
                            Factory.newFeatureMap(),
                            "ANNIE_" + Gate.genSym() );
            FeatureMap features = Factory.newFeatureMap(); // use default parameters

            // adding Annie  PRs  : i. document reset
//            features.clear();
//            ProcessingResource documentReset = (ProcessingResource)
//                    Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR", features);
//            annieController.add(documentReset);

            // adding Annie  PRs  : ii. english tokeniser
            features.clear();
            ProcessingResource englishTokeniser = (ProcessingResource)
                    Factory.createResource("gate.creole.tokeniser.DefaultTokeniser", features);
            annieController.add(englishTokeniser);

            
            features.clear();
            features.put("grammarURL", Converter.getURL(japeMainFile));
            ProcessingResource myJape = (ProcessingResource)
                    Factory.createResource("gate.creole.Transducer", features);
            annieController.add(myJape);
        } // initAnnie()
        catch (ResourceInstantiationException ex) {
            ex.printStackTrace();
        }

  } // initAnnie()
  
  /**
   * sets a corpus with all the files  to be annotated
   * @throws gate.creole.ResourceInstantiationException
   * @throws java.net.MalformedURLException
   */
  private void setCorpus() {
        try {
            
            corpus = Factory.newCorpus("");
            
                try {
                    
                    FeatureMap params = Factory.newFeatureMap();
                    params.put("preserveOriginalContent", false);
                    params.put("collectRepositioningInfo", false);
                    params.put("markupAware", false);
                    Out.prln("Creating gate.document "); //console
                    Document doc = (Document)
                            Factory.createResource("gate.corpora.DocumentImpl", params);
                    doc.setContent(new DocumentContentImpl(this.paragraphContent));
                    corpus.add(doc);
                } catch (ResourceInstantiationException ex) {
                    ex.printStackTrace();
                }
            annieController.setCorpus(corpus);
        } catch (ResourceInstantiationException ex) {
            ex.printStackTrace();
        }
  }

  /**
   * Run ANNIE over the corpus
   */
  private void execute(){
        try {
            Out.prln("Running ANNIE..."); // console
            annieController.execute();
            Out.prln("...ANNIE executtion completed"); //console
        } // execute()
        catch (ExecutionException ex) {
            ex.printStackTrace();
        }
  }// execute()

  /**
   *  It returns the annotated corpus
   * @return the corpus
   */
  public  Corpus getCorpus(){
      return this.corpus;
  }

//  /**
//   *  It returns the name of the regulatory body
//   * @return the regulatory body
//   */
//  public String getRegBody() {
//        return regBody;
//    }
}