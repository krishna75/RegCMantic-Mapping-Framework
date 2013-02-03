/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.xml;

import gate.util.GateException;
import gate.util.Out;
import helper.reg.*;
import helper.util.Converter;
import helper.util.MyReader;
import java.io.IOException;
import java.util.ArrayList;
import rcm.main.Settings;


/**
 * ***************************************************************************************************************************
 *  Extraction Result To CSV Converter :
 * ______________________________________________________________________________________
 *  This class is used to convert the regulation extraction result, an XML file, to CSV format.
 *  It processes the  input as the XML data and extends the abstract class to write the output.
 * ***************************************************************************************************************************
 *                 |            @author Krishna Sapkota                                           |
 *                 |             A PhD project at Oxford Brookes University          |
 *                 | ________________________________________  | 
*/
public class ExtractionToCsv extends Abstract_ConvertToCsv {

    //_2_EntityReader_V3 reader;
//    Wordnet wordnet;

    // entities to be populated
    RegulationBody body;
    RegulationDocument doc;
    ArrayList<Topic> topicList1;
    ArrayList<Topic> topicList2;

    String topic;
    String topic_title;
    String topic_stru;
    String subTopic;
    String sub_title;
    String sub_stru;
    String reg;
    String stmt;
    private String OUT_FILE = Settings.FILES_PATH+"extraction.csv";
    private static final String IN_FILE = Settings.FILES_PATH + "topic_list.ser";
/**
     * populates the SemReg ontology with the details of the regulation from the text using GATE
     * @throws gate.util.GateException
     * @throws java.io.IOException
     */
public ExtractionToCsv() {
    init();
    populateDataList();
    writeCSV();
    Out.prln("*** EXTRACTION TO CSV COMPLETED *** ");
}
private void init(){
    
    csvFile = OUT_FILE;
    dataList = new ArrayList< ArrayList<String>>();
    String[] columns = {"topic_id","topic_title", "structure",
                        "subtopic_id","subtopic_title", "sub-structure",
                        "reg_id","stmt_id",
                        "sub_id","act_id","obl_id","obl_type", "obl_strength",
                        "why","how", "where", "when"};
    columnList = Converter.arrayToArrayList(columns) ;
    
    // not needed just yet
    body = (RegulationBody)MyReader.fileToObject(IN_FILE);
    doc = body.getDocumentList().get(0);
    topicList1 = doc.getTopicList();
    Out.prln("id ="+ body.getId());
    Out.prln("name ="+ body.getName());
    Out.prln("description ="+ body.getDescription());
    System.out.println("topicList1 size = "+ topicList1.size());


}
/**
* updates the SemReg ontology with regulation from the text via GATE
*/
private void populateDataList(){
    Out.println("\nPopulating ..........");
    //populateBody();
    //populateDocument();
    populateTopic();
    Out.println("\n .......... population completed");   
}

private void populateBody(){
    //bodyInd = body.getId();
}
/**
 *
 */
private void populateDocument(){
    //docInd = doc.getId();
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
       // data = new ArrayList<String>();
       // dataList.add(data);
        
        // get all the required variables
        String id =t1.getId();
        String title = t1.getTitle();
        String structure = t1.getStructure();
        
        // add the values to the data (row)
        topic = id;
        topic_title = title;
        topic_stru = structure;
        

        // get lower topic list
        topicList2 = t1.getTopicList();
        for (Topic t2 : topicList2){
            // get all the required variables
            String id2 =(t2.getId());
            String title2 = t2.getTitle();
            String structure2 = t2.getStructure();
            
            // add the values to the data (row)
            subTopic = id2;
            sub_title = title2;
            sub_stru = structure2;

            populateRegulation(t2);
    }
    }
  
}
/**
  * populateRegulation()
 *           |_ populateStetement()
 *               |_ populateEntity()
 *  It populates the regulation which involves populating others as shown above.
 */
private void populateRegulation(Topic topic1){

    // for each regulation
    for (Regulation regulation: topic1.getRegList()){
        reg = "Eudralex_"+regulation.getId();
        
        populateStatement(regulation);
    }
}
/**
*  populateStetement()
 *               |_ populateEntity()
 *  It populates the regulation statement which involves populating others as shown above.
 */
private void populateStatement(Regulation reg1){
    // for each statement
    int stmtCounter = 1;
    for (Statement stmt1: reg1.getStmtList()){
        stmt = reg +"_"+stmtCounter;
        stmtCounter++;
        // ENTITY:
        populateEntity(stmt1);
    }
}

/**
 *  It populates the regulatory entities  within a statement.
 */
private void populateEntity(Statement stmt1){
          data = new ArrayList<String>();
          dataList.add(data);

          data.add(topic);
          data.add(topic_title);
          data.add(topic_stru);
          data.add(subTopic);
          data.add(sub_title);
          data.add(sub_stru);
          data.add(reg);
          data.add(stmt);
                  
          data.add(Converter.arrayListToString(stmt1.getSubAnnList()));
          data.add(Converter.arrayListToString(stmt1.getActAnnList()));
          data.add(stmt1.getObligation().getDescription());
          data.add(stmt1.getObligation().getType());
          data.add(stmt1.getObligation().getStrength());
          
          data.add(Converter.arrayListToString(stmt1.getWhyAnnList()));
          data.add(Converter.arrayListToString(stmt1.getHowAnnList()));
          data.add(Converter.arrayListToString(stmt1.getWhereAnnList()));
          data.add(Converter.arrayListToString(stmt1.getWhenAnnList()));
}

}
