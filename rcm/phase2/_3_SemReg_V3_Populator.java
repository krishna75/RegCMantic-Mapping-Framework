/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase2;

import api.ont.IOntology;
import api.ont.IOntologyFiles;
import api.ont.Jena_Ontology;
import gate.util.GateException;
import gate.util.Out;
import helper.reg.*;
import helper.util.DOS;
import helper.util.MyReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import rcm.main.Settings;


/**
 * updates the SemReg ontology with regulation from the text via GATE
 * 
 * @author Krishna Sapkota on 12-Aug-2010 at 09:27:03
 */
public class _3_SemReg_V3_Populator implements IOntologyFiles, Serializable {

    /* IO Files*/
    String IN_FILE = Settings.PHASE2 + "topic_list.ser";
    
    /* Adapter Classes*/
    IOntology ont;
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
    String topic1Ind;
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
public _3_SemReg_V3_Populator() {
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
    ont = new Jena_Ontology(path,semFile, semPrefix);
}
/**
* updates the SemReg ontology with regulation from the text via GATE
*/
private void populate(){
    Out.println("\nPopulating ontology ..........");
    populateBody();
    populateDocument();
    populateTopic();
    Out.println("\n .......... ontology population completed");   
}

private void populateBody(){
    bodyInd = body.getId();
    // create individual
    ont.createInd("Regulation_Body", bodyInd);
    // add id
    ont.addDataTypeStatement(bodyInd, "id", body.getId());
    // add name
    ont.addDataTypeStatement(bodyInd, "hasName", body.getName());
    // add description
    ont.addDataTypeStatement(bodyInd, "description", body.getDescription());
}
/**
 *
 */
private void populateDocument(){
    docInd = doc.getId();
    // create individual
    ont.createInd("Regulation_Document", docInd);
    // add id 
    ont.addDataTypeStatement(docInd, "id", doc.getId());
    // add name
    ont.addDataTypeStatement(docInd, "hasName", doc.getName());
    // add description
    ont.addDataTypeStatement(docInd, "description", doc.getDescription());
    // add version
    ont.addDataTypeStatement(docInd, "version", doc.getVersion());
    // relate body to document
    ont.addObjectStatement(bodyInd, "hasDocument", docInd);
    ont.addObjectStatement(docInd, "isDocumentOf", bodyInd);

}
/**
 *  populateTopic()
 *      |_ populateRegulation()
 *           |_ populateStetement()
 *               |_ populateEntity()
 *  It populates the topic which involves populating others as shown above.
 */
private void populateTopic(){
    // get the first topic
    for (Topic t1 : topicList1){
        topic1= t1;
        // get all the required variables
        String id =topic1.getId();
        String desc = topic1.getDescription();
        String title = topic1.getTitle();
        String structure = topic1.getStructure();
        topic1Ind = "topic_"+id;
        // cread ind
        ont.createInd("Topic", topic1Ind);
        // add id
        ont.addDataTypeStatement(topic1Ind, "id", id);
        // ad description
        ont.addDataTypeStatement(topic1Ind, "description", desc);
         ont.addComment(topic1Ind, desc);
        // add title
        ont.addDataTypeStatement(topic1Ind, "title", title);
        // add structure
        ont.addDataTypeStatement(topic1Ind, "documentStructure", structure);
        // relate document to topic
        ont.addObjectStatement(docInd, "hasTopic", topic1Ind);
        ont.addObjectStatement(topic1Ind, "isTopicOf", docInd);

        // get lower topic list
        topicList2 = topic1.getTopicList();
        for (Topic t2 : topicList2){
            topic2 = t2;
            // get all the required variables
            String id2 =(topic2.getId());
            String desc2 = topic2.getDescription();
            String title2 = topic2.getTitle();
            String structure2 = topic2.getStructure();
            topic2Ind = "topic_"+id2;

            // cread ind
            ont.createInd("Topic", topic2Ind);
            // add id
            ont.addDataTypeStatement(topic2Ind, "id", id2);
            // ad description
            ont.addDataTypeStatement(topic2Ind, "description", desc2);
            ont.addComment(topic2Ind, desc2);
            // add title
            ont.addDataTypeStatement(topic2Ind, "title", title2);
            // add structure
            ont.addDataTypeStatement(topic2Ind, "documentStructure", structure2);

            // relate document to topic
            ont.addObjectStatement(docInd, "hasTopic", topic2Ind);
            ont.addObjectStatement(topic2Ind, "isTopicOf", docInd);
            // relate topic to upper lopic
            ont.addObjectStatement(topic1Ind, "lowerTopic", topic2Ind);
            ont.addObjectStatement(topic2Ind, "higherTopic", topic1Ind);

            populateRegulation(topic2);
    }
    }
  
}
/**
  * populateRegulation()
 *           |_ populateStetement()
 *               |_ populateEntity()
 *  It populates the regulation which involves populating others as shown above.
 */
private void populateRegulation(Topic topic){

    // for each regulation
    for (Regulation reg: topic.getRegList()){
        // get variables
        regID = reg.getId();
        regInd = regBody + "_" + regID;
        String desc = reg.getDescription();
        
        //  create individual
        ont.createInd("Regulation", regInd);
        //  add id
        ont.addDataTypeStatement(regInd, "id", regID);
        //  add description
        ont.addDataTypeStatement(regInd, "description", desc);
        ont.addComment(regInd, desc);
        //  relate topic with  regulation
        ont.addObjectStatement(topic2Ind, "hasRegulation", regInd);
        ont.addObjectStatement(regInd, "isRegulationOf", topic2Ind);

        populateStatement(reg);
    }
}
/**
*  populateStetement()
 *               |_ populateEntity()
 *  It populates the regulation statement which involves populating others as shown above.
 */
private void populateStatement(Regulation reg){

    // for each statement
    int stmtCounter = 1;
    for (Statement stmt: reg.getStmtList()){
        stmtID = regID +"_"+stmtCounter;
        stmtCounter++;
        stmtInd = regBody + "_"+stmtID;
        String desc = stmt.getDescription();

        // create individual
        ont.createInd("Statement", stmtInd);
        // add id
        ont.addDataTypeStatement(stmtInd, "id", stmtID);
        // add description
        ont.addDataTypeStatement(stmtInd, "description", desc);
        ont.addComment(stmtInd, desc);

        //  relate regulation with statement
        ont.addObjectStatement(regInd, "hasStatement", stmtInd);
        ont.addObjectStatement(stmtInd, "isStatementOf", regInd);

        // ENTITY:
        populateEntity(stmt);
    }
}
/**
 *  It populates the regulatory entities  within a statement.
 */
private void populateEntity(Statement stmt){
    
    // SUBJECT: updates individual, object property and  comment
    for (String subjectInd: stmt.getSubAnnList()){
        if (!subjectInd.equals("")){
            subjectInd = getFormatedName(subjectInd);
            String desc = subjectInd.replace("_", " ");
            ont.createInd("Subject", subjectInd);
            ont.addDataTypeStatement(subjectInd, "id", subjectInd+"_subject");
            ont.addDataTypeStatement(subjectInd, "description", desc);
            ont.addComment(regInd, desc);
            ont.addObjectStatement(stmtInd, "hasSubject", subjectInd);
            ont.addObjectStatement(subjectInd, "isSubjectOf", stmtInd);
        }
    }


    // ACTION: updates individual, object property and  comment
    for (String actionInd: stmt.getActAnnList()){
        if (!actionInd.equals("")){
            actionInd = getFormatedName(actionInd);
            String desc = actionInd.replace("_", " ");
            ont.createInd("Action", actionInd);
            ont.addObjectStatement(stmtInd, "hasAction", actionInd);
            ont.addObjectStatement(actionInd, "isActionOf", stmtInd);
            ont.addDataTypeStatement(actionInd, "id", actionInd+"_action");
            ont.addDataTypeStatement(actionInd, "description", desc);
            ont.addComment(actionInd, desc);
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
        ont.createInd("Obligation", oblInd);
        ont.addObjectStatement(stmtInd, "hasObligation", oblInd);
        ont.addObjectStatement(oblInd, "isObligationOf", stmtInd);
        // fills the object properties; type and strength
        ont.addObjectStatement(oblInd, "hasType", obl.getType());
        ont.addObjectStatement(oblInd, "hasStrength", obl.getStrength());
        ont.addDataTypeStatement(oblInd, "id", oblInd +"_obligation");
        ont.addDataTypeStatement(oblInd, "description", desc);
        ont.addComment(oblInd, desc);
    }
    

    // HOW
    for (String howInd: stmt.getHowAnnList()){
        if (!howInd.equals("")){
            howInd = getFormatedName(howInd);
            String desc = howInd.replace("_", " ");
            ont.createInd("Evaluative_Expression", howInd);
            ont.addDataTypeStatement(howInd, "id", howInd+"_how");
            ont.addDataTypeStatement(howInd, "description", desc);
            ont.addComment(howInd, desc);
            // retating statement and the evaluative expression (how)
            ont.addObjectStatement(stmtInd, "hasHow", howInd);
            ont.addObjectStatement(howInd, "isHowOf", stmtInd);
        }
    }

    //WHEN
    for (String whenInd: stmt.getWhenAnnList()){
        if (!whenInd.equals("")){
            whenInd = getFormatedName(whenInd);
            String desc = whenInd.replace("_", " ");
            ont.createInd("Time", whenInd);
            ont.addDataTypeStatement(whenInd, "id", whenInd+"_when");
            ont.addDataTypeStatement(whenInd, "description", desc);
            ont.addComment(whenInd, desc);
            // retating statement and the evaluative expression (how)
            ont.addObjectStatement(stmtInd, "hasWhen", whenInd);
            ont.addObjectStatement(whenInd, "isWhenOf", stmtInd);
        }
    }

    //WHY
    for (String whyInd: stmt.getWhyAnnList()){
        if (!whyInd.equals("")){
            whyInd = getFormatedName(whyInd);
            String desc = whyInd.replace("_", " ");
            ont.createInd("Intention", whyInd);
            ont.addDataTypeStatement(whyInd, "id", whyInd+"_when");
            ont.addDataTypeStatement(whyInd, "description", desc);
            ont.addComment(whyInd, desc);
            // retating statement and the evaluative expression (how)
            ont.addObjectStatement(stmtInd, "hasWhy", whyInd);
            ont.addObjectStatement(whyInd, "isWhyOf", stmtInd);
        }
    }

    //WHERE
    for (String whereInd: stmt.getWhereAnnList()){
        if (!whereInd.equals("")){
            //Out.prln(" ind ===== "+ whereInd);
            whereInd = getFormatedName(whereInd);
            String desc = whereInd.replace("_", " ");
            ont.createInd("Place", whereInd);
            ont.addDataTypeStatement(whereInd, "id", whereInd+"_when");
            ont.addDataTypeStatement(whereInd, "description",desc);
            ont.addComment(whereInd, desc);
            // retating statement and the evaluative expression (how)
            ont.addObjectStatement(stmtInd, "hasWhere", whereInd);
            ont.addObjectStatement(whereInd, "isWhereOf", stmtInd);
        }
    }

}
/**
 *  Saves the populated ontology
 */
private void saveOntology(){
    Out.println("saving ontology ........");
    ont.saveOntology();
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
