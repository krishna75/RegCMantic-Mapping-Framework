/*                |            @author Krishna Sapkota,  Mar 2, 2012                  |
 *                 |             A PhD project at Oxford Brookes University          |
 *                 | ________________________________________  | 
 */

package rcm.phase34.ui;

import api.xml.XmlWriter;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.Print;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.w3c.dom.Element;
import rcm.main.Settings;

/**
 *   The Data For The Mapping Table :
 *  This class is used to hold the data about the mapping between the regulations and the validation tasks.
 *  It also reads and writes XML files related to the mapping.
 *   @author Krishna Sapkota                                           
 *   A PhD project at Oxford Brookes University          
*/
public class _2_MappingData {
    
    /* IO Files*/
    String path = Settings.PHASE3 + "4_ui";
    String XML_FILENAME1 = path + "mapping4.xml";
    String XML_FILENAME2 = path + "mapping4.xml";

    /* Local Variables */
    /* regulation related  */
    private String regId;
    private String stmtId;
    private String regText;
    private String stmtText;
    private ArrayList<String> regSubjectList;
    private ArrayList<String> regActionList;

    /* validation task related */
    private String taskId;
    private String taskDesc;
    private ArrayList<String> taskSubjectList;
    private ArrayList<String> taskActionList;

    /*  mapping related */
    private String mappingId;
    private ArrayList<_2_MappingData> mappingList;
    private double subjectScore;
    private double actionScore;
    private double totalScore;
    private boolean accepted;
    private String remark;


    public _2_MappingData() {
        init();
        process();
        finish();
    }

    private void init(){
        Print.prln("MappingData initialised...");
        regText = "";
        regId = "";
        regSubjectList = new ArrayList<String>();
        regActionList = new ArrayList<String>();
        taskId = "";
        taskSubjectList = new ArrayList<String>();
        taskActionList = new ArrayList<String>();
        subjectScore = 0;
        actionScore = 0;
        accepted = false;
        remark = "";
        mappingList = new ArrayList<_2_MappingData>();

    }
    private void process(){
        
    }
    private void finish(){

    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getTotalScore() {
        //totalScore = subjectScore + actionScore;
//        Print.prln("totalscore ="+totalScore);
        return totalScore;

    }


    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    
    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
    
    public ArrayList<_2_MappingData> getAcceptedList(){
        ArrayList<_2_MappingData> aList = new ArrayList<_2_MappingData>();
        for (_2_MappingData a: mappingList){
            if (a.isAccepted()== true){
                aList.add(a);
            }   
        }
        return aList;
    }
    
    public ArrayList<String> listRegulationsByTask(String taskId){
        ArrayList<String> stmtList = new ArrayList<String>();
        for (_2_MappingData m1 : mappingList){
            if (m1.getTaskId().equalsIgnoreCase(taskId)){
                stmtList.add(m1.getStmtId());
            }
        }
        return stmtList;
    }
    
    public ArrayList<String> listTasksByStmt(String stmtId){
        ArrayList<String> stmtList = new ArrayList<String>();
        for (_2_MappingData m1 : mappingList){
            if (m1.getStmtId().equalsIgnoreCase(stmtId)){
                stmtList.add(m1.getTaskId());
            }
        }
        return stmtList;
    }
    
     public ArrayList<String> listTasksByRegulation(String regId){
        ArrayList<String> stmtList = new ArrayList<String>();
        for (_2_MappingData m1 : mappingList){
            if (m1.getRegId().equalsIgnoreCase(regId)){
                stmtList.add(m1.getTaskId());
            }
        }
        return stmtList;
    }

    public double getActionScore() {
        return actionScore;
    }

    public void setActionScore(double actionScore) {
        this.actionScore = actionScore;
    }

    public ArrayList<String> getRegActionList() {
        return regActionList;
    }

    public void setRegActionList(ArrayList<String> regActionList) {
        this.regActionList = regActionList;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public ArrayList<String> getRegSubjectList() {
        return regSubjectList;
    }

    public void setRegSubjectList(ArrayList<String> regSubjectList) {
        this.regSubjectList = regSubjectList;
    }

    public String getRegText() {
        return regText;
    }

    public void setRegText(String regText) {
        this.regText = regText;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getSubjectScore() {
        return subjectScore;
    }

    public void setSubjectScore(double subjectScore) {
        this.subjectScore = subjectScore;
    }

    public ArrayList<String> getTaskActionList() {
        return taskActionList;
    }

    public void setTaskActionList(ArrayList<String> taskActionList) {
        this.taskActionList = taskActionList;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }
    

    public ArrayList<String> getTaskSubjectList() {
        return taskSubjectList;
    }

    public void setTaskSubjectList(ArrayList<String> taskSubjectList) {
        this.taskSubjectList = taskSubjectList;
    }


    public void addMapping(){
        mappingList.add(this);
    }

    public void addMapping(_2_MappingData mapping){
        mappingList.add(mapping);
    }

    public void setMappingList(ArrayList<_2_MappingData> mappingList){
        this.mappingList= mappingList;
    }

    public ArrayList<_2_MappingData> getMappingList(){
        return mappingList;
    }

    public _2_MappingData getMappingById(String mappingId){
        _2_MappingData m1 = null;
        for (_2_MappingData m: mappingList){
            if (mappingId.equals(m.getMappingId())){
               m1=m;
            }
        }
        return m1;
    }

    public String getStmtId() {
        return stmtId;
    }

    public void setStmtId(String stmtId) {
        this.stmtId = stmtId;
    }

    public String getStmtText() {
        return stmtText;
    }

    public void setStmtText(String stmtText) {
        this.stmtText = stmtText;
    }

    

  /**
     *  reads an XML file and updates(populates) the properties
     */
    public void readFile(){
        Print.prln("MappingData: reading mapping data a from file ...");
        ArrayList<Element> mappingElementList = MyReader.getXmlDomElements(XML_FILENAME1, "mapping");
        mappingList.clear();
        for (Element mappingElement: mappingElementList ){
            _2_MappingData m = new _2_MappingData();
            mappingList.add(m);

            /* reading  regulation values */ 
            Element regulation = (Element)mappingElement.getElementsByTagName("regulation").item(0);
                Element reg_id = (Element)regulation.getElementsByTagName("reg_id").item(0);
                Element reg_text = (Element) regulation.getElementsByTagName("reg_text").item(0);
                Element stmt_id = (Element)regulation.getElementsByTagName("stmt_id").item(0);
                Element stmt_text = (Element) regulation.getElementsByTagName("stmt_text").item(0);
                Element reg_subject = (Element)regulation.getElementsByTagName("reg_subject").item(0);
                Element reg_action = (Element)regulation.getElementsByTagName("reg_action").item(0);

                /* setting  regulation values */
                m.setRegId(reg_id.getTextContent());
                m.setRegText(reg_text.getTextContent());
                m.setStmtId(stmt_id.getTextContent());
                m.setStmtText(stmt_text.getTextContent());
                m.setRegSubjectList(Converter.lineToArrayList(reg_subject.getTextContent(), ","));
                m.setRegActionList(Converter.lineToArrayList(reg_action.getTextContent(), ","));

            /* reading  task values */
            Element task = (Element)mappingElement.getElementsByTagName("validation_task").item(0);
                Element task_id = (Element) task.getElementsByTagName("task_id").item(0);
                Element task_desc = (Element) task.getElementsByTagName("task_description").item(0);
                Element task_subject = (Element) task.getElementsByTagName("task_subject").item(0);
                Element task_action = (Element) task.getElementsByTagName("task_action").item(0);

                /* setting  task values */
                m.setTaskId(task_id.getTextContent());
                m.setTaskDesc(task_desc.getTextContent());
                m.setTaskSubjectList(Converter.lineToArrayList(task_subject.getTextContent(), ","));
                m.setTaskActionList(Converter.lineToArrayList(task_action.getTextContent(), ","));

            /* reading  mapping values */
            Element mapping_id = (Element)mappingElement.getElementsByTagName("mapping_id").item(0);
            Element similarity_score = (Element)mappingElement.getElementsByTagName("similarity_score").item(0);
                Element subject_score = (Element) similarity_score.getElementsByTagName("subject_score").item(0);
                Element action_score = (Element) similarity_score.getElementsByTagName("action_score").item(0);
                Element accepted1 = (Element)mappingElement.getElementsByTagName("accepted").item(0);
                Element remark1 = (Element)mappingElement.getElementsByTagName("remark").item(0);

                /* setting mapping values */
                m.setMappingId(mapping_id.getTextContent());
                m.setSubjectScore(Double.valueOf(subject_score.getTextContent()));
                m.setActionScore(Double.valueOf(action_score.getTextContent()));
                m.setTotalScore(Double.valueOf(subject_score.getTextContent()) + Double.valueOf(action_score.getTextContent()));
                m.setAccepted(Boolean.valueOf(accepted1.getTextContent().trim()));
                m.setRemark(remark1.getTextContent());
        }
    }    

 /**
     *  writes an xml file and dumps its properties
     */
    public void writeFile(){
        Print.prln("MappingData: writing mapping data to a file ...");
        XmlWriter xml = new XmlWriter(XML_FILENAME2);
        xml.startElement("mapping_data");
        for (_2_MappingData m: mappingList){

           /* regulation related  */
           String reg_id    = m.getRegId();
           String reg_text  = m.getRegText();
           String stmt_id   = m.getStmtId();
           String stmt_text = m.getStmtText();
           String rSubject  = Converter.listToString(m.getRegSubjectList(),",");
           String rAction   = Converter.listToString(m.getRegActionList(),",");

           /* task related */
           String tId       = m.getTaskId();
           String tDesc     = m.getTaskDesc();
           String tSubject  = Converter.listToString(m.getTaskSubjectList(),",");
           String tAction   = Converter.listToString(m.getTaskActionList(),",");

           /* mapping related  */
           String mapping_id  = m.getMappingId();
           double sScore = m.getSubjectScore();
           double aScore = m.getActionScore();
           boolean isAccepted = m.isAccepted();
           String rem = m.getRemark();

        xml.startElement("mapping");
            xml.startElement("mapping_id");
                xml.characters(mapping_id);
            xml.endElement("mapping_id");

            /*  regulation related */
            xml.startElement("regulation");
                xml.startElement("reg_id");
                    xml.characters(reg_id);
                xml.endElement("reg_id");
                xml.startElement("reg_text");
                    xml.characters(reg_text);
                xml.endElement("reg_text");
                xml.startElement("stmt_id");
                    xml.characters(stmt_id);
                xml.endElement("stmt_id");
                xml.startElement("stmt_text");
                    xml.characters(stmt_text);
                xml.endElement("stmt_text");
                xml.startElement("reg_subject");
                    xml.characters(rSubject);
                xml.endElement("reg_subject");
                xml.startElement("reg_action");
                    xml.characters(rAction);
                xml.endElement("reg_action");
            xml.endElement("regulation");

            /* task related  */
            xml.startElement("validation_task");
                xml.startElement("task_id");
                    xml.characters(tId);
                xml.endElement("task_id");
                xml.startElement("task_description");
                    xml.characters(tDesc);
                xml.endElement("task_description");
                xml.startElement("task_subject");
                    xml.characters(tSubject);
                xml.endElement("task_subject");
                xml.startElement("task_action");
                    xml.characters(tAction);
                xml.endElement("task_action");
            xml.endElement("validation_task");

            /* mapping related  */
            xml.startElement("similarity_score");
                xml.startElement("subject_score");
                    xml.characters(Double.toString(sScore));
                xml.endElement("subject_score");
                xml.startElement("action_score");
                    xml.characters(Double.toString(aScore));
                xml.endElement("action_score");
            xml.endElement("similarity_score");
            xml.startElement("accepted");
                xml.characters(String.valueOf(isAccepted));
            xml.endElement("accepted");
            xml.startElement("remark");
                xml.characters(rem);
            xml.endElement("remark");
        xml.endElement("mapping");
        
        }
        xml.endElement("mapping_data");
        xml.write(); 
    }
    
   public void generateId(){
        int mappingId1 = 1;
        for (_2_MappingData md : this.getMappingList()){
            md.setMappingId("mid_"+mappingId1);
            mappingId1++;
        }
    }

   public void makeMappingUnique(){
       //create new list
       ArrayList<_2_MappingData> newList = new ArrayList<_2_MappingData>();
       // for each mapping
       for (_2_MappingData m1: mappingList){
        // get reg id and task id
           String regId1 = m1.getRegId();
           String taskId1 = m1.getTaskId();
           boolean found = false;
           for (_2_MappingData m2: newList){
               String regId2 = m2.getRegId();
               String taskId2 = m2.getTaskId();
               if (regId1.equals(regId2)& taskId1.equals(taskId2)){
                found = true;
               }
           }
           if (!found){
               newList.add(m1);
           }

       }
       // old list = new list
       mappingList = newList;
   }
    public void sortMappingList(){
        if (mappingList.size()>=2){
            Collections.sort(mappingList, new MappingDataComparator());
        }
    }
    
    public class MappingDataComparator implements Comparator<_2_MappingData> {
    @Override
    public int compare(_2_MappingData o1, _2_MappingData o2) {
        _2_MappingData m1 = (_2_MappingData) o1;
                _2_MappingData m2 = (_2_MappingData) o2;
               return Double.valueOf(m2.getSubjectScore()).compareTo(Double.valueOf(m1.getSubjectScore()));
            }
    }
    

}
