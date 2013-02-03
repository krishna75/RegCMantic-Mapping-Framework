/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import gate.util.GateException;
import gate.util.Out;
import java.io.IOException;
import java.util.ArrayList;
import api.ont.IOntology;
import api.ont.IOntology;
import api.ont.IOntologyFiles;
import api.ont.Jena_Ontology;
import api.ont.Jena_Ontology;
import helper.reg.Obligation;
import helper.reg.Regulation;
import helper.reg.Statement;
import helper.reg.Topic;
import helper.util.Print;
import helper.util.Sorter;
import api.wn.Wordnet;


/**
 * updates the SemReg ontology with regulation from the text via GATE
 * 
 * @author Krishna Sapkota on 12-Aug-2010 at 09:27:03
 */
public class SemReg_V2_Populator implements IOntologyFiles {

    // using ontology, gate and wordnet api
    IOntology ont;
    EntityReader_V2 gate;
    Wordnet wordnet;

    // entities to be populated
    ArrayList<Topic> topicList;
    String topicID;
    String topicInd;
    String regID ;
    String regInd;
    String stmtID;
    String stmtInd;
    

    
  /**
     * populates the SemReg ontology with the details of the regulation from the text using GATE
     * @throws gate.util.GateException
     * @throws java.io.IOException
     */
    public SemReg_V2_Populator() {
        //TODO populate body and document classes

       // gets information from GATE
        gate = new EntityReader_V2();

      // getting the topicList from the gate
        topicList = gate.getTopicList();
        System.out.println("topicList size = "+ topicList.size());

        /*  initialises the ontology: this constructor should be replaced with appropriate signatures */
        ont = new Jena_Ontology(path, semFile, semPrefix);
    }

/**
* updates the SemReg ontology with regulation from the text via GATE
*/
public void populate(){
    Out.println("\nPopulating ontology ..........");
    populateTopic();
    Out.println("\n .......... ontology population completed");
    saveOntology();
    Out.prln("*** SEMREG ONTOLOGY COMPLETED *** ");
}

/**
 *  populateTopic()
 *      |_ populateRegulation()
 *           |_ populateStetement()
 *               |_ populateEntity()
 *  It populates the topic which involves populating others as shown above.
 */
public void populateTopic(){

    // for each topic
    int countTopic = 1;
    for (Topic topic : topicList){
        topicInd = regBody + "_Topic_" + countTopic++;

        // 1. INDIVIDUAL : creates a resource {individual} under the class Topic
        ont.createInd( "Topic", topicInd);

        // 2. ID: fills the datatype property {id} with regID
        ont.addDataTypeStatement(topicInd, "id", topicInd);

        // 3. TITLE:  fills the datatype property { title}
        if (topic.getTitle() != null){
            ont.addDataTypeStatement(topicInd, "title", topic.getTitle());
        }

         // 4. DESCRIPTION:  fills the datatype property {description}
        if(topic.getDescription()!= null){
        ont.addDataTypeStatement(topicInd, "description", topic.getDescription());
        }

        // 5. REGULATION:
        populateRegulation(topic);
    }
}

/**
  * populateRegulation()
 *           |_ populateStetement()
 *               |_ populateEntity()
 *  It populates the regulation which involves populating others as shown above.
 */
public void populateRegulation(Topic topic){

    // for each regulation
    for (Regulation reg: topic.getRegList()){
        regID = reg.getId();
        regInd = regBody + "_" + regID;

        // proceed only if the regulation text is not empty
        if(reg.getDescription()!= null){

            //  1. INDIVIDUAL: creates a resource {individual} under the class Regulation
            ont.createInd("Regulation", regInd);

             // 2. ID:  fills the datatype property {id}
            ont.addDataTypeStatement(regInd, "id", regID);

            //  3. OBJECT PROPERTY: fills the object property {isRegulationOf} and {hasRegulation}
            ont.addObjectStatement(regInd, "isRegulationOf", topicInd);
            ont.addObjectStatement(topicInd, "hasRegulation", regInd);

            // 4. DESCRIPTION:  fills the datatype property {description}

            ont.addDataTypeStatement(regInd, "description", reg.getDescription());
            }

            // 5. STATEMENT:
            populateStatement(reg);
    }
}

/**
*  populateStetement()
 *               |_ populateEntity()
 *  It populates the regulation statement which involves populating others as shown above.
 */
public void populateStatement(Regulation reg){

    // for each statement
    int countStmt = 1;
    for (Statement stmt: reg.getStmtList()){
        stmtID = regID +"_"+countStmt++;
        stmtInd = regBody + "_"+stmtID;
        //stmtInd = getFormatedName(stmtInd);

        // 1. INDIVIDUAL: creates a resource {individual} under the class Statement
        ont.createInd("Statement", stmtInd);

        // 2. ID: fills the datatype property {id}
        ont.addDataTypeStatement(stmtInd, "id", stmtID);

        //  3. OBJECT PROPERTY: fills the object property {hasStatement} and {isStatementOf}
        ont.addObjectStatement(regInd, "hasStatement", stmtInd);
        ont.addObjectStatement(stmtInd, "isStatementOf", regInd);

        // 4. DESCRIPTION:  fills the datatype property {description}
        if(stmt.getDescription()!= null){
        ont.addDataTypeStatement(stmtInd, "description", stmt.getDescription());
        }

        // 5. ENTITY:
        populateEntity(stmt);
    }
}

/**
 *  It populates the regulatory entities  within a statement.
 */
public void populateEntity(Statement stmt){
    
    // SUBJECT: updates individual, object property and  comment
    for (String subjectInd: stmt.getSubAnnList()){
        subjectInd = getFormatedName(subjectInd);
        ont.createInd("Subject", subjectInd);
        ont.addObjectStatement(stmtInd, "hasSubject", subjectInd);
        ont.addObjectStatement(subjectInd, "isSubjectOf", stmtInd);
        ont.addDataTypeStatement(subjectInd, "description", subjectInd.replace("_", " "));
//        wordnet.getAllSet(subjectInd);
//        ArrayList<String> synList = wordnet.getSynList();
//        Print.printArrayList(Sorter.getSortedUniqueList(synList));

    }

    // ACTION: updates individual, object property and  comment
    for (String actionInd: stmt.getActAnnList()){
        actionInd = getFormatedName(actionInd);
        ont.createInd("Action", actionInd);
        ont.addObjectStatement(stmtInd, "hasAction", actionInd);
        ont.addObjectStatement(actionInd, "isActionOf", stmtInd);
        ont.addDataTypeStatement(actionInd, "description", actionInd.replace("_", " "));
//        wordnet.getAllSet(actionInd);
//        ArrayList<String> synList = wordnet.getSynList();
//        Print.printArrayList(Sorter.getSortedUniqueList(synList));
    }

    // OBLIGATION: updates individual, object property and  comment
    Obligation obl = stmt.getObligation();
    String oblInd = obl.getDescription();
    if (oblInd!=(null)){
        oblInd = getFormatedName(oblInd);
        ont.createInd("Obligation", oblInd);
        ont.addObjectStatement(stmtInd, "hasObligation", oblInd);
        ont.addObjectStatement(oblInd, "isObligationOf", stmtInd);
        // fills the object properties; type and strength
        ont.addObjectStatement(oblInd, "hasType", obl.getType());
        ont.addObjectStatement(oblInd, "hasStrength", obl.getStrength());
        ont.addDataTypeStatement(oblInd, "description", oblInd.replace("_", " "));
    }
}

/**
 *  Saves the populated ontology
 */
public void saveOntology(){
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
        if (ch.isLetter(ch)){
           text2 +=ch;
        }else{
           text2 +="_";
        }
    }
    return text2;
}

}
