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
import gate.annotation.AnnotationSetImpl;
import gate.corpora.RepositioningInfo;
import gate.creole.*;
import gate.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;
import helper.reg.Obligation;
import helper.reg.Regulation;
import helper.reg.Statement;
import helper.reg.Topic;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.MyWriter;
import helper.util.Print;
import helper.util.Sorter;


/**
 * Extracts the information for the classes under the package reg
 * (i.e. topic, regulation, statement, subject, obligation, action etc.)
 * @author Krishna Sapkota on 13-Jul-2010 at 22:26:04
 */
public class EntityReader_V2 implements Serializable{

        /**  gate related: */
        private String docName = Settings.STYLES_PATH+"reg_v2.xml";
        private String regBody = "Eudralex";
        private String jape1 = Settings.JAPE_PATH+"jape4xmlregulation/main.jape";
        private String jape2 = Settings.JAPE_PATH+"jape4xmlregulation2/main.jape";
        private String gazetteer = Settings.GAZETTEER_PATH+"gazetteer_general/lists.def";
        private String reqAnnFileName = Settings.GATE_FILES_PATH + "required_annotation_list.txt";
        private String inCorpusFile = Settings.FILES_PATH+"entity_annotator_v2.ser";
        private String outFile = Settings.FILES_PATH+"topic_list.ser";

        private SerialAnalyserController annieController;
        private Corpus corpus;

        /** reg package related */
        private AnnotationSet docAS;
        private AnnotationSet reqAS;
        private Topic topic;
        private ArrayList<Topic> topicList = new ArrayList<Topic>();
        private Regulation reg;
        private ArrayList<Regulation> regList;
        private Statement stmt;
        private ArrayList<Statement> stmtList;
        private ArrayList<String> subList;
        private ArrayList<String> actList;
        private Obligation obl;
        private Sorter sorter = new Sorter();

 /**
   *  This uses GATE and NLP  to extract information for SemReg ontology.
   */
  public EntityReader_V2(){
       corpus = (Corpus)MyReader.fileToObject(inCorpusFile);
       extract();
       MyWriter.write(topicList, outFile);
       Out.prln("*** ENTITY READING COMPLETED *** ");
    }

/**
   * extract()
   * |_extractTopicList()
   *   |_extractRegulationList()
   *     |_extractStatementList()
   * It populates the topic list which involves populating the other separate lists as structured above.
   */
  public void extract(){
   // i. iterate for each document in the corpus
    Iterator<Document> iterDocs = corpus.iterator();

    // counts no of documents
    int countDoc = 0;
    while(iterDocs.hasNext()) {
      Document doc = (Document) iterDocs.next();
      ++countDoc;
      docAS   = doc.getAnnotations();
      reqAS = getSelectedAnnType(docAS);
      
    Print.pr("\n SORTING TOPIC:  ");
    Iterator<Annotation> topicIter = Sorter.getSortedAnnIter(reqAS.get("Topic"));
    extractTopicList(topicIter);
    }
}

/**
 *  extracts topics
 * @param topicIter
 */
 private void extractTopicList(Iterator<Annotation> topicIter){

    //  iterates through each topic
    while (topicIter.hasNext()){

        //  1. Updating Reg Objects: adding topic list, topic and reg list
        topic = new Topic();
        topicList.add(topic);
        regList = new ArrayList<Regulation>();
        topic.setRegList(regList);

        // 2. getting annotations within a topic annotation
        Annotation topicAnn = (Annotation) topicIter.next();
        AnnotationSet topicContainedAS = reqAS.get(topicAnn.getStartNode().
                            getOffset(),topicAnn.getEndNode().getOffset());

        // 3. extracting title
        Iterator<Annotation> titleIter = topicContainedAS.get("Title").iterator();
        if (titleIter.hasNext()){
            String text = titleIter.next().getFeatures().get("text").toString();
            topic.setTitle(text);
            Print.prln("title = "+ text);
        }//if

        // 4. extracting topic text
        Annotation textAnn = topicContainedAS.get("TopicText").iterator().next();
        topic.setDescription(textAnn.getFeatures().get("text").toString());

        //  5. getting annotations within a topic text
        AnnotationSet textContainedAS = reqAS.getContained(textAnn.getStartNode().
                            getOffset(),textAnn.getEndNode().getOffset());

        // 6. getting all the regulations within the topic text
        Print.pr("\n SORTING REGULATION:  ");
        Iterator<Annotation> regIter = Sorter.getSortedAnnIter (textContainedAS.get("Regulation"));
        extractRegulationList(regIter);
    }//while
}

/**
 *  extracts regulations
 * @param regIter
 */
private void extractRegulationList(Iterator<Annotation> regIter) {

    /*  Iterates through each regulation */
    while (regIter.hasNext()){

        //  1. Updating Reg Objects: adding reg list, reg and  stmt list
        reg = new Regulation();
        regList.add(reg);
        stmtList = new ArrayList<Statement>();
        reg.setStmtList(stmtList);

        // 2. getting  a set of annotations within a reg annotation
        Annotation regAnn = regIter.next();
        AnnotationSet regContainedAS = reqAS.get(regAnn.getStartNode().
                                getOffset(),regAnn.getEndNode().getOffset());

        // 3. extracting reg_id
        Annotation idAnn = regContainedAS.get("RegID").iterator().next();
        reg.setId(idAnn.getFeatures().get("text").toString());
        Iterator<Annotation> regTextIter = regContainedAS.get("RegText").iterator();

        if (regTextIter.hasNext()){

            // 4. extracting reg text. reg text contains regulations
            Annotation regTextAnn = regContainedAS.get("RegText").iterator().next();
            reg.setDescription(regTextAnn.getFeatures().get("text").toString());



            //  5. getting a set of annotations within a reg text
            AnnotationSet senContainedAS = docAS.getContained(regTextAnn.getStartNode().
                                    getOffset()-10,regTextAnn.getEndNode().getOffset());

            // 6. getting all the statements within the reg text
            Iterator<Annotation> senIter = sorter.getSortedAnnIter(senContainedAS.get("Statement"));
            extractStatementList(senIter);
        }

    }//while reg
}

/**
 *  extracts statements
 * @param senIter
 */
private void extractStatementList(Iterator<Annotation> senIter) {

    /*  Iterates through each statement */
    while (senIter.hasNext()){

        //  1. Updating Reg Objects: adding stmt list, stmt, obl, subject list and action list
        stmt = new Statement();
        stmtList.add(stmt);
        obl = new Obligation();
        stmt.setObligation(obl);
        subList = new ArrayList<String>();
        stmt.setSubAnnList(subList);
        actList = new ArrayList<String>();
        stmt.setActAnnList(actList);

        // 2. getting a set of annotations within a  sentenence annotation
        Annotation senAnn = senIter.next();
        AnnotationSet entityContainedAS = docAS.getContained(senAnn.getStartNode().
                            getOffset(),senAnn.getEndNode().getOffset());

        // 3. extracting stmt text
        stmt.setDescription(senAnn.getFeatures().get("text").toString());

        // 4. extracting obligation
        Iterator<Annotation> oblIter = entityContainedAS.get("Obligation").iterator();
        if (oblIter.hasNext()){
            Annotation oblAnn = oblIter.next();
            obl.setDescription(oblAnn.getFeatures().get("text").toString());
            obl.setStrength(oblAnn.getFeatures().get("strength").toString());
            obl.setType(oblAnn.getFeatures().get("type").toString());
        }//if obl

        // 5. extracting subjects
        Iterator<Annotation> subIter = entityContainedAS.get("Subject").iterator();
        while (subIter.hasNext()){
            Annotation subAnn = subIter.next();
            subList.add(subAnn.getFeatures().get("text").toString());
        }//while sub

        // 6. extracting actions
        Iterator<Annotation> actIter = entityContainedAS.get("Action").iterator();
        while (actIter.hasNext()){
            Annotation actAnn = actIter.next();
            actList.add(actAnn.getFeatures().get("text").toString());
        }// while act
    }// while sen
}
     
/**
 *
 * @return the topic list
 */
public ArrayList<Topic> getTopicList() {
    return topicList;
}

/**
 * @return the name of the regulatory body
 */
public String getRegBody() {
    return regBody;
}

 /**
   *
   * @param annSet
   * @return
   */
  private AnnotationSet getSelectedAnnType(AnnotationSet annSet){

      // it reads the list of the required annotations from a text file and adds into a hashmap.
      ArrayList<String> reqAnnList = MyReader.fileToArrayList(reqAnnFileName);
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
public void display(){
    for (Topic topic2: topicList){
        Print.prln( "title = " +topic2.getTitle());//
        for (Regulation regulation: topic2.getRegList()){
            Print.prln("reg_id = " + regulation.getId());
            Print.prln("reg text = " +regulation.getDescription());
            for (Statement statement: regulation.getStmtList()){
                Print.prln( "--- statement = " +statement.getDescription());
                String entities = "[";
                for (String str: statement.getSubAnnList()){
                    entities += " "+str;
                    }
                entities += " "+statement.getObligation().getDescription();
                for (String str: statement.getActAnnList()){
                    entities += " "+str;
                }
            }
        }
    }
}

}