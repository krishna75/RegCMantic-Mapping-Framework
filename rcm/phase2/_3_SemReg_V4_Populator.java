/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase2;

import api.ont.JenaAbstractOntology;
import gate.util.GateException;
import gate.util.Out;
import helper.reg.*;
import helper.util.DOS;
import helper.util.MyReader;
import helper.util.Print;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import rcm.main.Settings;


/**
 * updates the SemReg ontology with regulation from the text via GATE
 * 
 * @author Krishna Sapkota on 12-Aug-2010 at 09:27:03
 */
public class _3_SemReg_V4_Populator extends JenaAbstractOntology implements Serializable {
    
    /* IO Files*/
    String IN_FILE = Settings.PHASE2 + "topic_list.ser";
    
    /* Adapter Classes*/
    _2_EntityReader_V3 reader;
    
    /* Local Variables */
    RegulationBody body;
    RegulationDocument doc;
    ArrayList<Topic> topicList1;
    ArrayList<Topic> topicList2;
    Topic topic1;
    Topic topic2;
    
    String bodyInd;
    String docInd;
    String topicID;
    String topic2Ind;
    String regID ;
    String regInd;
    String stmtID;
    String stmtInd;
/**
     * populates the SemReg ontology with the details of the regulation from the text using GATE
     * @throws gate.util.GateException
     * @throws java.io.IOException
     */
public _3_SemReg_V4_Populator() {
    DOS.makeSemRegEmpty();
    init();
    populate();
    saveOntology();
    Out.prln("*** SEMREG ONTOLOGY POPULATION COMPLETED *** ");
    DOS.openSemReg();
}

private void init(){
    body = (RegulationBody)MyReader.fileToObject(IN_FILE);
    doc = body.getDocumentList().get(0);
    topicList1 = doc.getTopicList();
    Out.prln("id ="+ body.getId());
    Out.prln("name ="+ body.getName());
    Out.prln("description ="+ body.getDescription());
    System.out.println("topicList1 size = "+ topicList1.size());

    /*  initialises the ontology: this constructor should be replaced with appropriate signatures */
    //ont = new Jena_Ontology(path,semFile, semPrefix);
    
}
/**
* updates the SemReg ontology with regulation from the text via GATE
*/
private void populate(){
    Out.println("\nPopulating ontology ..........");
    populateBody();
    populateDocument();
    
    for (Topic topic: doc.getTopicList()){
        populateTopic(topic);
    }
    
    Out.println("\n .......... ontology population completed");   
}

private void populateBody(){
    bodyInd = body.getId();
    // create individual
    semReg.createInd("Regulation_Body", bodyInd);
    // add id
    semReg.addDataTypeStatement(bodyInd, "id", body.getId());
    // add name
    semReg.addDataTypeStatement(bodyInd, "hasName", body.getName());
    // add description
    semReg.addDataTypeStatement(bodyInd, "description", body.getDescription());
}

/**
 *
 */
private void populateDocument(){
    docInd = doc.getId();
    // create individual
    semReg.createInd("Regulation_Document", docInd);
    // add id 
    semReg.addDataTypeStatement(docInd, "id", doc.getId());
    // add name
    semReg.addDataTypeStatement(docInd, "hasName", doc.getName());
    // add description
    semReg.addDataTypeStatement(docInd, "description", doc.getDescription());
    // add version
    semReg.addDataTypeStatement(docInd, "version", doc.getVersion());
    // relate body to document
    semReg.addObjectStatement(bodyInd, "hasDocument", docInd);
    semReg.addObjectStatement(docInd, "isDocumentOf", bodyInd);

}

/**
 *  populateTopic()
 *      |_ populateRegulation()
 *           |_ populateStetement()
 *               |_ populateEntity()
 *  It populates the topic which involves populating others as shown above.
 */
private void populateTopic(Topic topic){
    String id = topic.getId();
    
    String structure = topic.getStructure();
    String title = topic.getTitle();
    ArrayList<String> annList = topic.getAnnotationList();
    
    // get all the required variables
        String topicInd = "topic_"+id;
        // cread ind
        semReg.createInd("Topic", topicInd);
        // add id
        semReg.addDataTypeStatement(topicInd, "id", id);
//        // ad description
//        ont.addDataTypeStatement(topic1Ind, "description", desc);
//         ont.addComment(topic1Ind, desc);
        // add title
        semReg.addDataTypeStatement(topicInd, "title", title);
        // add structure
        semReg.addDataTypeStatement(topicInd, "documentStructure", structure);
        // relate document to topic
        semReg.addObjectStatement(docInd, "hasTopic", topicInd);
        semReg.addObjectStatement(topicInd, "isTopicOf", docInd);
        
       // adding annotation
        for (String ann: annList){
            Print.prln( "ann = "+ann);
            semReg.addDataTypeStatement(topicInd, "hasAnnotation", ann);       
        }        
    Print.prln("id="+id+", structure="+structure + ", title=" +title);
    ArrayList<Topic> lowerTopicList = topic.getTopicList();
    //Print.prln("size="+lowerTopicList.size());
    if (lowerTopicList.size()>0){
        for (Topic lowerTopic: lowerTopicList){
            populateTopic(lowerTopic);
            String lowerTopicInd = "topic_"+lowerTopic.getId();
            semReg.addObjectStatement(topicInd, "lowerTopic", lowerTopicInd);
            semReg.addObjectStatement(lowerTopicInd, "higherTopic", topicInd);
        }
    } 
    populateRegulation(topic, topicInd);
}

/**
  * populateRegulation()
 *           |_ populateStetement()
 *               |_ populateEntity()
 *  It populates the regulation which involves populating others as shown above.
 */
private void populateRegulation(Topic topic, String topicInd){

    // for each regulation
    for (Regulation reg: topic.getRegList()){
        // get variables
        String regID = reg.getId();
        String regInd = regBody + "_" + regID;
        String desc = reg.getDescription();
        
        //  create individual
        semReg.createInd("Regulation", regInd);
        //  add id
        semReg.addDataTypeStatement(regInd, "id", regID);
        //  add description
        semReg.addDataTypeStatement(regInd, "description", desc);
        semReg.addComment(regInd, desc);
        //  relate topic with  regulation
        semReg.addObjectStatement(topicInd, "hasRegulation", regInd);
        semReg.addObjectStatement(regInd, "isRegulationOf", topicInd);

        populateStatement(reg, regInd);
    }
}
/**
*  populateStetement()
 *               |_ populateEntity()
 *  It populates the regulation statement which involves populating others as shown above.
 */
private void populateStatement(Regulation reg, String regInd){

    // for each statement
    int stmtCounter = 1;
    for (Statement stmt: reg.getStmtList()){
        String stmtID = reg.getId() +"_"+stmtCounter;
        stmtCounter++;
        String stmtInd = regBody + "_"+stmtID;
        String desc = stmt.getDescription();
        ArrayList<String> annList = stmt.getAnnotationList();

        // create individual
        semReg.createInd("Statement", stmtInd);
        // add id
        semReg.addDataTypeStatement(stmtInd, "id", stmtID);
        // add description
        semReg.addDataTypeStatement(stmtInd, "description", desc);
        semReg.addComment(stmtInd, desc);  
        // adding annotation
        for (String ann: annList){
            semReg.addDataTypeStatement(stmtInd, "hasAnnotation", ann);       
        }

        //  relate regulation with statement
        semReg.addObjectStatement(regInd, "hasStatement", stmtInd);
        semReg.addObjectStatement(stmtInd, "isStatementOf", regInd);

        // ENTITY:
        populateEntity(stmt, stmtInd);
    }
}
/**
 *  It populates the regulatory entities  within a statement.
 */
private void populateEntity(Statement stmt, String stmtInd){
    
    // SUBJECT: updates individual, object property and  comment
    for (String subjectInd: stmt.getSubAnnList()){
        if (!subjectInd.equals("")){
            subjectInd = getFormatedName(subjectInd);
            String desc = subjectInd.replace("_", " ");
            subjectInd = "subject_"+subjectInd;
            semReg.createInd("Subject", subjectInd);
            semReg.addDataTypeStatement(subjectInd, "id", subjectInd);
            semReg.addDataTypeStatement(subjectInd, "description", desc);
           // semReg.addComment(regInd, desc);
            semReg.addObjectStatement(stmtInd, "hasSubject", subjectInd);
            semReg.addObjectStatement(subjectInd, "isSubjectOf", stmtInd);
        }
    }


    // ACTION: updates individual, object property and  comment
    for (String actionInd: stmt.getActAnnList()){
        if (!actionInd.equals("")){
            actionInd = getFormatedName(actionInd);
            String desc = actionInd.replace("_", " ");
            actionInd = "action_"+actionInd;
            semReg.createInd("Action", actionInd);
            semReg.addObjectStatement(stmtInd, "hasAction", actionInd);
            semReg.addObjectStatement(actionInd, "isActionOf", stmtInd);
            semReg.addDataTypeStatement(actionInd, "id", actionInd);
            semReg.addDataTypeStatement(actionInd, "description", desc);
            //semReg.addComment(actionInd, desc);
        }
//        wordnet.getAllSet(actionInd);
//        ArrayList<String> synList = wordnet.getSynList();
//        Print.printArrayList(Sorter.getSortedUniqueList(synList));
    }

     //OBLIGATION: updates individual, object property and  comment
    Obligation obl = stmt.getObligation();
    String oblInd = getFormatedName(obl.getDescription());
    if (!oblInd.equals("")){
        String desc = oblInd.replace("_", " ");
        semReg.createInd("Obligation", oblInd);
        semReg.addObjectStatement(stmtInd, "hasObligation", oblInd);
        semReg.addObjectStatement(oblInd, "isObligationOf", stmtInd);
        // fills the object properties; type and strength
        semReg.addObjectStatement(oblInd, "hasType", obl.getType());
        semReg.addObjectStatement(oblInd, "hasStrength", obl.getStrength());
        semReg.addDataTypeStatement(oblInd, "id", oblInd +"_obligation");
        semReg.addDataTypeStatement(oblInd, "description", desc);
        //semReg.addComment(oblInd, desc);
    }
    

    // HOW
    for (String howInd: stmt.getHowAnnList()){
        if (!howInd.equals("")){
            howInd = getFormatedName(howInd);
            String desc = howInd.replace("_", " ");
            semReg.createInd("Evaluative_Expression", howInd);
            semReg.addDataTypeStatement(howInd, "id", howInd+"_how");
            semReg.addDataTypeStatement(howInd, "description", desc);
           // semReg.addComment(howInd, desc);
            // retating statement and the evaluative expression (how)
            semReg.addObjectStatement(stmtInd, "hasHow", howInd);
            semReg.addObjectStatement(howInd, "isHowOf", stmtInd);
        }
    }

    //WHEN
    for (String whenInd: stmt.getWhenAnnList()){
        if (!whenInd.equals("")){
            whenInd = getFormatedName(whenInd);
            String desc = whenInd.replace("_", " ");
            semReg.createInd("Time", whenInd);
            semReg.addDataTypeStatement(whenInd, "id", whenInd+"_when");
            semReg.addDataTypeStatement(whenInd, "description", desc);
            //semReg.addComment(whenInd, desc);
            // retating statement and the evaluative expression (how)
            semReg.addObjectStatement(stmtInd, "hasWhen", whenInd);
            semReg.addObjectStatement(whenInd, "isWhenOf", stmtInd);
        }
    }

    //WHY
    for (String whyInd: stmt.getWhyAnnList()){
        if (!whyInd.equals("")){
            whyInd = getFormatedName(whyInd);
            String desc = whyInd.replace("_", " ");
            semReg.createInd("Intention", whyInd);
            semReg.addDataTypeStatement(whyInd, "id", whyInd+"_when");
            semReg.addDataTypeStatement(whyInd, "description", desc);
            //semReg.addComment(whyInd, desc);
            // retating statement and the evaluative expression (how)
            semReg.addObjectStatement(stmtInd, "hasWhy", whyInd);
            semReg.addObjectStatement(whyInd, "isWhyOf", stmtInd);
        }
    }

    //WHERE
    for (String whereInd: stmt.getWhereAnnList()){
        if (!whereInd.equals("")){
            //Out.prln(" ind ===== "+ whereInd);
            whereInd = getFormatedName(whereInd);
            String desc = whereInd.replace("_", " ");
            semReg.createInd("Place", whereInd);
            semReg.addDataTypeStatement(whereInd, "id", whereInd+"_when");
            semReg.addDataTypeStatement(whereInd, "description",desc);
            //semReg.addComment(whereInd, desc);
            // retating statement and the evaluative expression (how)
            semReg.addObjectStatement(stmtInd, "hasWhere", whereInd);
            semReg.addObjectStatement(whereInd, "isWhereOf", stmtInd);
        }
    }

}
/**
 *  Saves the populated ontology
 */
private void saveOntology(){
    Out.println("saving ontology ........");
    semReg.saveOntology();
    Out.println("........... saving ontology completed.");
}
/**
 *  takes a string and replaces spaces and other characters with underscore
 * @param text1 text to be replaced
 * @return the replaced string (text)
 */
private String getFormatedName(String text1){
    text1 = text1.trim();
    String text2 = "";
    for (int i = 0; i< text1.length(); i++){
        Character ch = text1.charAt(i);
        if (Character.isLetter(ch)){
           text2 +=ch;
        }else{
           text2 +="_";
        }
    }
    return text2;
}

}
