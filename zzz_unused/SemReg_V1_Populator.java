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
import zzz_unused.zzz_SubTopic;
import zzz_unused.zzz_Topic;

/**
 * updates the SemReg ontology with regulation from the text via GATE
 * 
 * @author Krishna Sapkota on 12-Aug-2010 at 09:27:03
 */
public class SemReg_V1_Populator implements IOntologyFiles{
    IOntology ont;
    EntityReader_V0 gate;
    ArrayList<zzz_Topic> topicList;
    String regBody;
   // String regulationName = "Eudralex";
    
  /**
     *
     * @throws gate.util.GateException
     * @throws java.io.IOException
     */
    public SemReg_V1_Populator() throws GateException, IOException {
        gate = new EntityReader_V0();
        gate.extract();
        topicList = gate.getTopicList();

        /* this constructor should be replaced with appropriate signatures */
        ont = new Jena_Ontology(path,semFile, semPrefix);
    }

  /**
     * updates the SemReg ontology with regulation from the text via GATE
     */
    public void populate(){

        Out.println("\nPopulating ontology ..........");

        // for each topic and its contained sections; subtopic, regulation and statement
        // ========================= Topic =====================================
        for (zzz_Topic topic : topicList){
            regBody = topic.getRegBody();
            String topicID = topic.getId();
            String topicInd = regBody + "_" + topicID;

            // creates a resource (individual) under the class Topic
            ont.createInd("Topic", topicInd);
            
            // fills the datatype property regulationNumber with regID
            ont.addDataTypeStatement(topicInd, "id", topicID);

           // fills the datatype property title
            ont.addDataTypeStatement(topicInd, "title", topic.getTitle());

            // fills the comment of the regulation with paragraph annotation
           ont.addComment(topicInd, topic.getDesc());

           
           // creates a subject individual, adds comments and fills hasSubject
           // property of the topic.
           for (String subjectInd: topic.getSubjectAnnotationList()){
               ont.createInd( "Subject", subjectInd);
               ont.addComment(subjectInd, subjectInd);
               ont.addObjectStatement(topicInd, "hasSubject", subjectInd);
           }
           System.out.println("\n Eudralex "+topicID);
           ArrayList<zzz_SubTopic> subtopicList = topic.getSubtopicList();

           // for each subtopic and its contents; regulation and statement
           // ========================= SubTopic ===============================
           for (zzz_SubTopic subtopic: subtopicList){
                String stID = subtopic.getId();
                String subtopicInd = regBody + "_" + stID;

                // creates a resource (individual) under the class SubTopic
                ont.createInd("SubTopic", subtopicInd);

               // fills the object property hasTopic and isTopicOf
                ont.addObjectStatement(subtopicInd, "hasTopic", topicInd);
                ont.addObjectStatement(topicInd, "isTopicOf", subtopicInd);

                // fills the datatype property id
                ont.addDataTypeStatement(subtopicInd, "id", stID);

               // fills the datatype property title
                ont.addDataTypeStatement(subtopicInd, "title", subtopic.getTitle());

                // fills the comment of the regulation with paragraph annotation
                ont.addComment(subtopicInd, subtopic.getDesc());

                
               // creates a subject individual, adds comments and fills hasSubject
               // property of the subtopic.
               for (String subjectInd: subtopic.getSubjectAnnotationList()){
                   ont.createInd("Subject", subjectInd);
                   ont.addComment(subjectInd, subjectInd);
                   ont.addObjectStatement(subtopicInd, "hasSubject", subjectInd);
                }
                System.out.println(" "+ stID);
                ArrayList<Regulation> regList = subtopic.getRegulationList();

                // for each regulation and its contents; statement
                // ========================= Regulation ========================
                for (Regulation reg: regList){
                    String regID = reg.getId();
                    String regInd = regBody + "_" + regID;
                    regInd = getFormatedName(regInd);

                    // creates a resource (individual) under the class Regulation
                    ont.createInd("Regulation", regInd);

                    // fills the object property hasSubTopic and isSubTopicOf
                   ont.addObjectStatement(regInd, "hasSubTopic", subtopicInd);
                   ont.addObjectStatement(subtopicInd, "isSubTopicOf", regInd);

                    // fills the datatype property id
                    ont.addDataTypeStatement(regInd, "id", regID);

                    // fills the comment of the regulation
                    ont.addComment(regInd, reg.getDescription());



                    System.out.println(" "+ regID);
                    ArrayList<Statement> stmtList = reg.getStmtList();

                    // for each statement
                    // ========================= Statement =====================
                    for (Statement stmt: stmtList){
                        String stmtID = stmt.getId();
                        String stmtInd = regBody+"_"+stmtID;
                        stmtInd = getFormatedName(stmtInd);

                        // creates a resource (individual) under the class Statement
                        ont.createInd("Statement", stmtInd);

                        // fills the object property hasStatement and isStatementOf
                       ont.addObjectStatement(regInd, "hasStatement", stmtInd);
                       ont.addObjectStatement(stmtInd, "isStatementOf", regInd);
                       
                        // fills the datatype property id
                        ont.addDataTypeStatement(stmtInd, "id", stmtID);

                        // fills the comment of the statement
                        ont.addComment(stmtInd, stmt.getDescription());

                       // creates a subject individual, add comments and fills hasSubject
                       // property of the statement.
                       for (String subjectInd: stmt.getSubAnnList()){
                           ont.createInd("Subject", subjectInd);
                           ont.addComment(subjectInd, subjectInd);
                           ont.addObjectStatement(stmtInd, "hasSubject", subjectInd);
                       }

                       // creates an action individual, add comments and fills hasAction
                       // property of the statement.
                       for (String actionInd: stmt.getActAnnList()){
                           ont.createInd( "Action", actionInd);
                           ont.addComment(actionInd, actionInd);
                           ont.addObjectStatement(stmtInd, "hasAction", actionInd);
                       }

                        // ========================= Obligation ================
                        Obligation obl = stmt.getObligation();
                        String oblInd = obl.getDescription();
                        if (oblInd!=(null)){
                            oblInd = getFormatedName(oblInd);
                            // creates a resource (individual) under the class Obligation
                            ont.createInd("Obligation", oblInd);

                            // fills the object property hasObligation
                            ont.addObjectStatement(stmtInd, "hasObligation", oblInd);

                            // fills the comment of the obligation
                            ont.addComment(oblInd, oblInd.replace("_", " "));

                            // fills the object properties; type and strength
                            ont.addObjectStatement(stmtInd, "hasType", obl.getType());
                            ont.addObjectStatement(stmtInd, "hasStrength", obl.getStrength());
                        }
                    }
                }
           }
        }// for each topic
        Out.println("........ ontology population completed");
        Out.println("saving ontology ........");
        ont.saveOntology();
        Out.println("........... saving ontology completed.");
        Out.prln("*** SEMREG ONTOLOGY COMPLETED *** ");
    }

    private String getFormatedName(String text){
        text = text.trim();
        text = text.replace(" ", "_");
        text = text.replace("\r\n", "");
        return text;
    }

}
