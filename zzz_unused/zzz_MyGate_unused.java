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

/**
 *
 * @author Krishna Sapkota on 13-Jul-2010 at 22:26:04
 */
public class zzz_MyGate_unused {

      /** The Corpus Pipeline application to contain ANNIE */
      private SerialAnalyserController annieController;
      private Corpus corpus;
      private String docName = "eu.pdf";
      private String regBody = "Eudralex";
      private ArrayList<zzz_RegulationStatement_unused> stmtList;

  /**
   * 
   * @throws gate.util.GateException
   * @throws java.io.IOException
   */
  public zzz_MyGate_unused() throws GateException, IOException{
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
    initAnnie();    // 3. initialise ANNIE (this may take several minutes)
    setCorpus();    // 4. set corpus and populate with files.
    execute();      // 5. run the PR through the corpus in the ANNIE
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
        // load each PR as defined in ANNIEConstants
        for(int i = 0; i < ANNIEConstants.PR_NAMES.length; i++) {
          features.clear();
          ProcessingResource pr = (ProcessingResource) 
                    Factory.createResource(ANNIEConstants.PR_NAMES[i], features);
          Out.prln(ANNIEConstants.PR_NAMES[i].toString());

          // add the PR to the pipeline controller
          annieController.add(pr);
        } // for each ANNIE PR

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
      params.put("preserveOriginalContent", new Boolean(true));
      params.put("collectRepositioningInfo", new Boolean(true));
      Out.prln("Creating doc for " + u);
      Document doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);
      doc.setCollectRepositioningInfo(true);
      doc.setPreserveOriginalContent(true);
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
   * 
   * @return
   */
  public ArrayList<zzz_RegulationStatement_unused> getRegulationList(){

   // i. iterate for each document in the corpus
    Iterator iterDocs = corpus.iterator();
    int count = 0;  
    while(iterDocs.hasNext()) {
          Document annotDoc        = (Document) iterDocs.next();
          AnnotationSet annotSet   = annotDoc.getAnnotations();
          FeatureMap features      = annotDoc.getFeatures();
          String originalContent   = (String) features.get(GateConstants.ORIGINAL_DOCUMENT_CONTENT_FEATURE_NAME);
          RepositioningInfo info   = (RepositioningInfo) features.get(GateConstants.DOCUMENT_REPOSITIONING_INFO_FEATURE_NAME);
          ++count;
          
        // ii. select required annotation types only
            Set<Annotation> selectedAnnotationSet = getSelectedAnnType(annotSet) ;

        // iii. sort selected annotation and get the values in an iterator
            Iterator iterAnnot = getSortedAnnIter(selectedAnnotationSet);

        // iv. Populate RegulationStatement with selected annotation
            populateRegStmtList(iterAnnot);
    }
      return stmtList;
}
  
  /**
   * 
   * @param annSet
   * @return
   */
  private Set<Annotation> getSelectedAnnType(AnnotationSet annSet){
      // ii. selecting required annotation types only
          Set annTypesRequired = new HashSet();
          annTypesRequired.add("RegID");
          annTypesRequired.add("Subject");
          annTypesRequired.add("Obligation");
          annTypesRequired.add("Action");
          annTypesRequired.add("Paragraph");
          return new HashSet<Annotation>(annSet.get(annTypesRequired));
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
   * 
   * @param annIter
   */
  private void populateRegStmtList(Iterator<Annotation> annIter){
      // iv. Populating RegulationStatement with selected annotation
        stmtList = new ArrayList<zzz_RegulationStatement_unused>();
        zzz_RegulationStatement_unused stmt = new zzz_RegulationStatement_unused();
        Annotation currAnnot = null;
        while(annIter.hasNext()) {
          currAnnot = (Annotation) annIter.next();
          String type = currAnnot.getType();
          String string = currAnnot.getFeatures().get("string").toString();

            if (type.equals("RegID")){
                stmt = new zzz_RegulationStatement_unused();
                stmtList.add(stmt);
                stmt.setRegID(string);
                stmt.setRegBody(regBody);
            } else if (type.equals("Subject")) {
                stmt.getSubjectList().add(string);
            }else if (type.equals("Obligation")){
                stmt.setObligation(string);
            }else if (type.equals("Action")){
                stmt.getActionList().add(string);
            }else if (type.equals("Paragraph")){
                stmt.setParagraph(string);
            }
        }
  }
  
/**
 * 
 */
  public void displayResult(){
      // displaying all the RegulationStatements to verify (test) the result
        Iterator<zzz_RegulationStatement_unused> iterStmt =stmtList.iterator();
        while(iterStmt.hasNext()){
            zzz_RegulationStatement_unused reg = iterStmt.next();
            Out.pr("\n"+reg.getRegID()+ "    ");
            Iterator iterSub = reg.getSubjectList().listIterator();
            while(iterSub.hasNext()){
                Out.pr(iterSub.next()+" ");
            }
            Out.pr(reg.getObligation()+" ");
            Iterator iterAct = reg.getActionList().listIterator();
            while(iterAct.hasNext()){
                Out.pr(iterAct.next()+" ");
            }
        }
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

}