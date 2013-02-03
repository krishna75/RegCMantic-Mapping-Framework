
package rcm.phase2;


import api.gt.SerialDatastoreIO;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.creole.SerialAnalyserController;
import gate.util.Out;
import helper.reg.*;
import helper.util.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import rcm.main.Settings;
import zzz_unused.ExtendedAnnotationSet;


/**
 * Extracts the information for the classes under the package reg
 * (i.e. topic, regulation, statement, subject, obligation, action etc.)
 * @author Krishna Sapkota on 13-Jul-2010 at 22:26:04
 */
public class _2_EntityReader_V3 implements Serializable{
    
    /* IO FILES */
    String IN_CORPUS_FILE = Settings.PHASE2+"entity_annotator_v3.ser";
    String IN_DOC_FILE      = Settings.PHASE1+"styles/reg_v2.xml";
    String IN_REQUIRED_ANN_FILE = Settings.PHASE2+ "required_annotation_list.txt";
    String IN_TOPIC_NAMES_FILE  = Settings.PHASE2 + "topic_list.txt";
    String OUT_TOPIC_FILE      = Settings.PHASE2+"topic_list.ser"; 
    
    //  gate related
    String jape1        = Settings.JAPE_PATH+"jape4xmlregulation/main.jape";
    String jape2        = Settings.JAPE_PATH+"jape4xmlregulation2/main.jape";
    String gazetteer    = Settings.GAZETTEER_PATH+"gazetteer_general/lists.def";
   
    /* external variables */
    Corpus corpus;
    SerialAnalyserController annieController;
    AnnotationSet   docAS;
    AnnotationSet   selectedAS;
    
    /* local vairables */
    RegulationBody      regBody ;
    String datastoreCorpusId = "selected_corpus___1342780213481___1091";
    String[] topicTypes = {"chapter","section","subsection"};
    ArrayList<String> topicTypeList = Converter.arrayToArrayList(topicTypes);
    String BODY_NAME           = "EMEA";
    String BODY_DESCRIPTION    = "European Medicines Agency";
    RegulationDocument  regDocument ;
    String DOC_NAME        = "EudraLex";
    String DOC_VERSION     = "5";
    String DOC_DESCRIPTION  = "EudraLex is the collection of "
            + "rules and regulations governing medicinal products in the European Union";

 /**
   *  This uses GATE and NLP  to extract information for SemReg ontology.
   */
    
public _2_EntityReader_V3(){
   init();
   readCorpus();
   extract();
   Print.prln("****************** Displaying *****************");
   displayTopic(regDocument.getTopicList().get(0));
   MyWriter.write(regBody, OUT_TOPIC_FILE);
//   Out.prln("*** ENTITY READING COMPLETED *** ");
}
private void init(){
   
     // regulation body
     regBody = new RegulationBody();
     regBody.setName(BODY_NAME);
     regBody.setDescription(BODY_DESCRIPTION);
     regBody.setId(BODY_NAME.toLowerCase());

     // regulatin document
     regDocument = new RegulationDocument();
     regDocument.setName(DOC_NAME);
     regDocument.setDescription(DOC_DESCRIPTION);
     regDocument.setVersion(DOC_VERSION);
     regDocument.setId((DOC_NAME+DOC_VERSION).toLowerCase());

     // assembling body, doc and the topic list together
     regBody.getDocumentList().add(regDocument);
     Out.prln("body= "+regBody.getId()+"doc = "+regDocument.getId());
 }

/** 
   * reads a pre-annotated corpus
   */
private void readCorpus(){
    // reads corpus from datastore (annotated in GATE IDE)
     SerialDatastoreIO datastore = new SerialDatastoreIO();
     corpus = datastore.readCorpusFromSerialDatastore(datastoreCorpusId);
     
     // reads corpus annoted by Entity Annotator
     //corpus = (Corpus)MyReader.fileToObject(inCorpusFile);
}
/** 
   * extract()
   * |_extractTopicList()
   *   |_extractRegulationList()
   *     |_extractStatementList()
   * It populates the topic list which involves populating the other separate lists as structured above.
   */
 private void extract(){
   // i. iterate for each document in the corpus
    Iterator<Document> iterDocs = corpus.iterator();
    while(iterDocs.hasNext()) {
      Document doc = (Document) iterDocs.next();
      docAS   = doc.getAnnotations();
      selectedAS = getSelectedAnnType(docAS);
      
      
      Iterator<Annotation> highestTopicIter = Sorter.getSortedAnnIter(selectedAS.get(topicTypeList.get(0)));
      while (highestTopicIter.hasNext()){
          
          //  1. Updating Reg Objects: adding topic list and  topic
            ArrayList<Topic> topicList = new ArrayList<Topic>();
            regDocument.setTopicList(topicList);
            Topic topic = new Topic();
            topicList.add(topic);
            Annotation highestTopicAnn = highestTopicIter.next();
          
          processTopic( highestTopicAnn, topic);
          
      }
      
//      selectedAS = docAS;
     // extractTopicList(selectedAS);

    }
}
 
 /**
 *  extracts topics
 * @param topicIter
 */
 private void processTopic(Annotation topicAnn, Topic topic){
        AnnotationSet topicContainedAS = selectedAS.getContained(topicAnn.getStartNode().
                                        getOffset(),topicAnn.getEndNode().getOffset());
        Annotation titleAnn = Sorter.getSortedAnnIter(topicContainedAS.get("title")).next();
        AnnotationSet titleContainedAS = topicContainedAS.getContained(titleAnn.getStartNode().
                                        getOffset(),titleAnn.getEndNode().getOffset());
        Iterator<Annotation> annIter= titleContainedAS.get("_ANNOTATION").iterator();
        
        
        // get values
        String title = titleAnn.getFeatures().get("text").toString();
        String topicType  = topicAnn.getType();
        String id = topicAnn.getFeatures().get("id").toString();
        ArrayList<String> annList = new ArrayList<String>();
        while (annIter.hasNext()){
            Annotation ann = annIter.next();
            String annString = ann.getFeatures().get("text").toString();
            annList.add(annString);
        }
        Print.prln("---------------------size of annList = "+ annList.size());
        
        //populateg topic with the values
        topic.setId(topicType+"_"+id);
        topic.setStructure(topicType);
        topic.setTitle(title);
        topic.setAnnotationList(annList);
           
        // if topic has lower topic ( or type of the topicAnn is not the  lowest type)
        int topicIndex = topicTypeList.indexOf(topicType);
        if (topicIndex < topicTypeList.size()-1 ){
            String lowerTopicType = topicTypeList.get(topicIndex+1);
            Iterator<Annotation> lowerTopicAnnIter = Sorter.getSortedAnnIter(topicContainedAS.get(lowerTopicType));
            if (lowerTopicAnnIter.hasNext()){
                
                // adding lower topic to the topic class
                ArrayList<Topic> lowerTopicList = new ArrayList<Topic>();
                topic.setTopicList(lowerTopicList);
                
                // get all the lower topics (recursive process)
                while (lowerTopicAnnIter.hasNext()){
                    Topic lowerTopic = new Topic();
                    lowerTopicList.add(lowerTopic);
                    Annotation lowerTopicAnn =   lowerTopicAnnIter.next();
                    processTopic(lowerTopicAnn, lowerTopic);
                } 
            }
        } else {
            Iterator<Annotation> regIter = Sorter.getSortedAnnIter (topicContainedAS.get("regulation"));
            extractRegulationList(topic, regIter);     
        }     
}

/**
 *  extracts regulations
 * @param regIter
 */
private void extractRegulationList(Topic topic, Iterator<Annotation> regIter) {
    ArrayList<Regulation> regList = new ArrayList<Regulation>();
    topic.setRegList(regList);
    /*  Iterates through each regulation */
    while (regIter.hasNext()){
        //  1. Updating Reg Objects: adding regulation list and regulation
 
        Regulation reg = new Regulation();
        regList.add(reg);
        // 2. getting  a set of annotations within a reg annotation
        Annotation regAnn = regIter.next();
        AnnotationSet regContainedAS = selectedAS.getContained(regAnn.getStartNode().
                                getOffset(),regAnn.getEndNode().getOffset());

        // 3. extracting reg_id and description
        reg.setId(regAnn.getFeatures().get("id").toString());
        reg.setDescription(regAnn.getFeatures().get("text").toString());
//        Print.prln("\n "+reg.getId() + "= (PARA)"+regAnn.getFeatures().get("text").toString());


        ExtendedAnnotationSet eAS = new ExtendedAnnotationSet(regContainedAS);
        eAS.sort();

        // 4. getting all the sentences within the reg text
        Print.prln("SORTING SENTENCE:  ");
        Iterator<Annotation> senIter = Sorter.getSortedAnnIter(regContainedAS.get("sentence"));
        extractStatementList(reg, senIter);
    }//while reg
}
/**
 *  extracts statements
 * @param senIter
 */
private void extractStatementList(Regulation reg, Iterator<Annotation> senIter) {

    ArrayList<Statement> stmtList = new ArrayList<Statement>();
    reg.setStmtList(stmtList);
    /*  Iterates through each statement */
    while (senIter.hasNext()){
        Print.prln("******************* inside senIter ***********************");

        //  1. Updating Reg Objects: adding stmt list, stmt, obl, subject list and action list
        
        Statement stmt = new Statement();
        stmtList.add(stmt);

        // 2. getting a set of annotations within a  sentenence annotation
        Annotation senAnn = senIter.next();
        AnnotationSet entityContainedAS = selectedAS.getContained(senAnn.getStartNode().
                            getOffset(),senAnn.getEndNode().getOffset());
        
        // gets and populates annotations in a statement
        Iterator<Annotation> annIter= entityContainedAS.get("_ANNOTATION").iterator();
        ArrayList<String> annList = new ArrayList<String>();
        while (annIter.hasNext()){
            Annotation ann = annIter.next();
            String annString = ann.getFeatures().get("text").toString();
            annList.add(annString);
        }
        stmt.setAnnotationList(annList);
        // 3. extracting stmt text
        stmt.setDescription(senAnn.getFeatures().get("text").toString());
        Print.prln(reg.getId() + "=------------------ "+senAnn.getFeatures().get("text").toString());

        // 4. extracting obligation
        Obligation obl = new Obligation();
        stmt.setObligation(obl);
        Iterator<Annotation> oblIter = entityContainedAS.get("obligation").iterator();
        if (oblIter.hasNext()){
            Annotation oblAnn = oblIter.next();
            obl.setDescription(oblAnn.getFeatures().get("text").toString());
            obl.setStrength(oblAnn.getFeatures().get("strength").toString());
            obl.setType(oblAnn.getFeatures().get("type").toString());
        }//if obl

        // 5. extracting subjects
        ArrayList<String> subList = new ArrayList<String>();
        stmt.setSubAnnList(subList);// add subject
        Iterator<Annotation> subIter = entityContainedAS.get("_SUBJECT").iterator();
        while (subIter.hasNext()){
            Annotation subAnn = subIter.next();
            subList.add(subAnn.getFeatures().get("text").toString());
        }//while sub

        // 6. extracting actions
        ArrayList<String> actList = new ArrayList<String>();
        stmt.setActAnnList(actList);//add action
        Iterator<Annotation> actIter = entityContainedAS.get("_ACTION").iterator();
        while (actIter.hasNext()){
            Annotation actAnn = actIter.next();
            actList.add(actAnn.getFeatures().get("text").toString());
        }// while act

        stmt.setHowAnnList(getWhEntityList(entityContainedAS, "how"));
        stmt.setWhenAnnList(getWhEntityList(entityContainedAS, "when"));
        stmt.setWhyAnnList(getWhEntityList(entityContainedAS, "why"));
        stmt.setWhereAnnList(getWhEntityList(entityContainedAS, "where"));
 
    }// while sen
}
/**
 *
 * @param entityContainedAS
 * @param whEntity
 */
private ArrayList<String> getWhEntityList(AnnotationSet entityContainedAS, String whEntity ){
    ArrayList<String> newList = new ArrayList<String>();
    Iterator<Annotation> whIter = entityContainedAS.get(whEntity).iterator();
    while (whIter.hasNext()){
        Annotation whAnn = whIter.next();
        String strAnn = whAnn.getFeatures().get("text").toString();
        if (!strAnn.equals("")){
            newList.add(strAnn);
        }
    }// while
    return newList;
}

public RegulationBody getBody(){
    return regBody;
}
public RegulationDocument getDocument(){
    return regDocument;
}

 /**
   *
   * @param annSet
   * @return
   */
private AnnotationSet getSelectedAnnType(AnnotationSet annSet){

  // it reads the list of the required annotations from a text file and adds into a hashmap.
  ArrayList<String> reqAnnList = MyReader.fileToArrayList(IN_REQUIRED_ANN_FILE);
  Print.prlnArrayList(reqAnnList);
  Set annotTypesRequired = new HashSet();
  for (String reqAnn: reqAnnList){
    annotTypesRequired.add(reqAnn);
  }
 return annSet.get(annotTypesRequired);
}
public int countAnn(AnnotationSet annSet, String annName){
       Print.pr("\n Counting "+annName+"...  ");
       Iterator<Annotation> regTextIter = annSet.get(annName).iterator();
       int size = 0;
       while (regTextIter.hasNext()){
           size ++;
           Annotation ann = regTextIter.next();
          // Print.prln (ann.getFeatures().get("text"));
        }
       Print.prln("size ="+ size);
      return size;
  }
/**
 *  prints out the extracted text
 */
public void displayTopic(Topic topic){
    String id = topic.getId();
    String structure = topic.getStructure();
    String title = topic.getTitle();
    ArrayList<String> annList = topic.getAnnotationList();
    Print.prln("id="+id+", structure="+structure + ", title=" +title);
    Print.pr("Annotation size = " + annList.size());
    Print.prArrayList(annList);
    ArrayList<Topic> lowerTopicList = topic.getTopicList();
    //Print.prln("size="+lowerTopicList.size());
    if (lowerTopicList.size()>0){
        for (Topic lowerTopic: lowerTopicList){
            displayTopic(lowerTopic);
        }
    }
    
    ArrayList<Regulation> regList = topic.getRegList();
    if (regList.size()>0) {
        
        for (Regulation regulation: regList){
                Print.prln("reg_id = " + regulation.getId());
//            Print.prln("i am inside regulation ......");
                Print.prln("reg text = " +regulation.getDescription());
                int counter = 1;
                for (Statement statement: regulation.getStmtList()){
//                   Print.prln("i am inside stmt ......");
                    Print.prln( "--- statement = "+regulation.getId()+"_"+ counter + ":  " +statement.getDescription());
                    counter ++;
                    String entities = "";
                    for (String str: statement.getSubAnnList()){
                        entities += " "+str;
                        }
                    entities += " "+statement.getObligation().getDescription();
                    for (String str: statement.getActAnnList()){
                        entities += " "+str;
                    }
//                    Print.prln ("entities("+entities+")");
                }
        }
    }
    
    
}

}