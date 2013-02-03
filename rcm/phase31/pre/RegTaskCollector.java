/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase31.pre;

import api.ont.JenaAbstractOntology;
import api.ont.OntologicalConcept;
import api.xml.XmlWriter;
import helper.mapping.*;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.Print;
import java.util.ArrayList;
import org.w3c.dom.Element;
import rcm.main.Settings;

/**
 * Provided two lists of regulations and tasks, this class collects all the information about these concepts such as annotations
 * bag of words and topics. The process is called interpretation, and the interpreted informations are saved in XML files.
 * @author Krishna Sapkota,  Jul 24, 2012,  4:34:55 PM
 * A PhD project at Oxford Brookes University
 */
public class RegTaskCollector extends JenaAbstractOntology {
    /* IO Files*/
    String PHASE3 = Settings.FILES_PATH + "phases_files/PHASE3/";// remove PHASE3, its for temporary to work around the static final problemes
    String path = PHASE3+"1_pre/";
    String IN_STOPWORD_FILE =  Settings.STOPWORDS_GENERAL;
    String IN_SELECTED_REG_FILE = path +"selected_regulation.txt";
    String IN_SELECTED_TASK_FILE = path +"selected_task.txt";
    String IN_REG_FILE = path + "manual_mapping_reg.xml"; // refined manually 
    String IN_TASK_FILE = path +"mapping_task.xml";
    String OUT_REG_FILE = path + "mapping_reg.xml";
    String OUT_TASK_FILE = path +"mapping_task.xml";;
   
    /* Local Variables */
    ArrayList<RegTaskCollector> regTaskList= new ArrayList<RegTaskCollector>();
    ArrayList<MappingRegulation> regList= new ArrayList<MappingRegulation>();
    ArrayList<MappingTask> taskList = new ArrayList<MappingTask>() ;
    
    ArrayList<String> selectedRegList ;
    ArrayList<String> selectedTaskList;
    ArrayList<String> stopList;

    public RegTaskCollector() {
        init();
//        interpreteTaskList(selectedTaskList);
//        interpreteRegList(selectedRegList);
//        write();      
        read();
    }
    
    private void init(){
        // selected reg and task list
        selectedRegList = MyReader.fileToArrayList(IN_SELECTED_REG_FILE);
        selectedTaskList = MyReader.fileToArrayList(IN_SELECTED_TASK_FILE);
        stopList = MyReader.fileToArrayList(IN_STOPWORD_FILE);
    }
    
    private ArrayList<String>  getSelectedRegulations(){   
    return MyReader.fileToArrayList(IN_SELECTED_REG_FILE);
}
    private ArrayList<String> getSelectedTasks(){
    return MyReader.fileToArrayList(IN_SELECTED_TASK_FILE);
}
    

    
    
/**
 * Purpose: computes the subject and action annotations of the validation task.
 * it uses the OntoReg ontology and traces the values of  hasPatient and hasAction object properties and
 * infers the the classes the values belong to. 
 * @param taskIndList is the list of the validation task to be interpreted.
 * @return subject and action annotations organised into a list of java classes.
 */
private ArrayList<MappingTask> interpreteTaskList(ArrayList<String> taskIndList){
    String[] subject_stopwords = {"concept","domain"};
    String[] action_stopwords = {"department","assistant","disapproved","approved","unknown"};
    /*  interprete each validation task and add it to thes task list */
    for (String taskInd: taskIndList){
            MappingTask task = new MappingTask();
            ArrayList<String> subjectList = new ArrayList<String>();
            ArrayList<String> actionList = new ArrayList<String>();
            ArrayList<String> annList = new ArrayList<String>();
            
            OntologicalConcept ocTask = new OntologicalConcept(taskInd, ontoReg);
            annList.addAll(ocTask.getAllConcepts());
           
           /*   Subject */
           String patientInd =  ontoReg.getDataPropertyValue(taskInd, "hasPatient");
           OntologicalConcept ocSubject = new OntologicalConcept(patientInd, ontoReg);           
           subjectList.addAll(ocSubject.getAllConcepts());
           //subjectList.add(ontoReg.getLabel(patientInd));
            
            /*  Action  */
           // role of the person 
            String personInd = ontoReg.getDataPropertyValue(taskInd, "isResponsibilityOf");
            OntologicalConcept ocAction1 = new OntologicalConcept(personInd, ontoReg);
            actionList.addAll(ocAction1.getAllConcepts());
            //actionList.add(ontoReg.getLabel(personInd));
            // action that the person performs
            String actionInd = ontoReg.getDataPropertyValue(personInd, "performs");
            OntologicalConcept ocAction2 = new OntologicalConcept(actionInd, ontoReg);
            actionList.addAll(ocAction2.getAllConcepts());
            
            subjectList = Converter.remove(subjectList, subject_stopwords);
            actionList = Converter.remove(actionList, action_stopwords);
            annList = Converter.remove(annList, subject_stopwords);
            annList = Converter.remove(annList, action_stopwords);
            
            /* add the interpreted eintities (i.e.subject and action annotations) to the task statement and to the list */
            String desc = "(subject=" + patientInd+ " (person="+personInd+
                    ") (action=" + actionInd+")";            
            Print.prln(desc);
            Print.prlnArrayList(subjectList);
            Print.prlnArrayList(actionList);
            
            // annotation contains action and subjects as well
            annList.addAll(actionList);
            annList.addAll(subjectList);
            
            // make unique
            annList = Converter.makeUnique(annList);
            actionList = Converter.makeUnique(actionList);
            subjectList = Converter.makeUnique(subjectList);
            
            // update the mapping task
            task.setId(taskInd);
            task.setDescription(desc);
            task.setActList(actionList);
            task.setSubList(subjectList);
            
            task.setAnnotationList(annList);
            taskList.add(task); 
           
    }
    return taskList;

}

private ArrayList<MappingRegulation> interpreteRegList(ArrayList<String> regIndList){
    

    /*  interprete each regulation and add it to the regulation list  */
    for (String regInd: regIndList)  {      
            MappingRegulation reg = new MappingRegulation();
            AbstractMappingEntity topic = new MappingTopic();
            //String regDescription = Converter.listToString(semReg.listDataPropertyValues(regInd, "description"),",");
            
            // process topic
//            Print.prln("===============>> processing .."+regInd);
            String topicInd = semReg.getDataPropertyValue(regInd, "isRegulationOf");
            topic = processTopic(topic, topicInd);
            topic = processBow(topic);

            /*  interprete each statement within the regulation  */
            ArrayList<String> stmtIndList = semReg.listDataPropertyValues(regInd, "hasStatement");
            for (String stmtInd: stmtIndList){
                MappingCore core = new MappingCore();
                AbstractMappingEntity aux = new MappingAux();
//                Print.prln("===============>> stmt .."+ stmtInd);
                MappingStmt stmt = new MappingStmt();                

                /* core*/
                // get
                ArrayList<String> actionList = new ArrayList<String>();
                ArrayList<String> subjectList = new ArrayList<String>();           
                ArrayList<String> actionIndList = semReg.listDataPropertyValues(stmtInd, "hasAction");
                for (String actionInd: actionIndList){
                    OntologicalConcept ocAction = new OntologicalConcept(actionInd, semReg);
                    actionList.addAll(ocAction.getAllConcepts());
                }
                ArrayList<String> subjectIndList = semReg.listDataPropertyValues(stmtInd, "hasSubject");
                for (String subjectInd: subjectIndList){
                    OntologicalConcept ocSubject = new OntologicalConcept(subjectInd, semReg);
                    subjectList.addAll(ocSubject.getAllConcepts());
                }  
                // set
                core.setActList(actionList);
                core.setSubList(subjectList);
                
                /* aux */
                // get
                String stmtDescription = Converter.listToString(semReg.listDataPropertyValues(stmtInd, "description"),",");
                ArrayList<String> annList = semReg.listDataPropertyValues(stmtInd, "hasAnnotation");
                //set
                aux.setText(stmtDescription);
                aux.setAnnList(annList);
                aux = processBow(aux);
                
//                Print.prln("===============>> aux text .."+ stmtDescription);
                // stmt
                stmt.setId(stmtInd);
                stmt.setTopic(topic);
                stmt.setCore(core);
                stmt.setAux(aux);
                //reg
                reg.setId(regInd);
                reg.getStmtList().add(stmt);
            }
            /* add the regulation to the regulation list  */
            regList.add(reg);
         
    }
    return regList;
} 
/**
 *  
 * @param topic
 * @param topicInd
 * @return 
 */
private AbstractMappingEntity processTopic(AbstractMappingEntity topic, String topicInd) {
    
    String text = semReg.getDataPropertyValue(topicInd, "title");
    ArrayList<String> annList = semReg.listDataPropertyValues(topicInd, "hasAnnotation");
    
    text += topic.getText();
    annList.addAll(topic.getAnnList());
    topic.setText(text);
    topic.setAnnList(annList);
    ArrayList<String> higherTopicList = semReg.listObjectPropertyValues(topicInd, "higherTopic");
    for (String higherTopicInd: higherTopicList){
        processTopic(topic, higherTopicInd);
    }
    return topic;
}

private AbstractMappingEntity processBow(AbstractMappingEntity entity){
    String[] stopwords = {"chapter","production","section","paragraph","topic","title"};
    ArrayList<String> bowList = new ArrayList<String>();
    String text = entity.getText();
    ArrayList<String> annList = entity.getAnnList();
    for (String ann: annList){
        text = text.replace(ann, "");
    }
    ArrayList<String> wordList = Converter.lineToArrayList(text, " ");
    wordList.addAll(annList);
    wordList = Converter.getPureLetters(wordList);
    // remove stopwords from the bow list
    bowList = Converter.remove(wordList, stopList);
    bowList = Converter.remove(bowList, stopwords);
    entity.setBowList(bowList);
    return entity;
}

    public ArrayList<RegTaskCollector> getRegTaskList() {
        return regTaskList;
    }

    public void setRegTaskList(ArrayList<RegTaskCollector> regTaskList) {
        this.regTaskList = regTaskList;
    }
    
    private void read(){
       Print.prln("RegTaskCollector: reading mapping-regulation and mapping-task from files ...");  
       readReg();
       readTask();
    }

    private void readReg(){

        ArrayList<Element> regElementList = MyReader.getXmlDomElements(IN_REG_FILE, "regulation");
        regList.clear();
        for (Element regElement: regElementList ){
            MappingRegulation reg = new MappingRegulation();
            regList.add(reg);

            // processing regulation id
            String reg_id = regElement.getAttribute("id");
            reg.setId(reg_id);
            ArrayList<Element> stmtElementList = MyReader.getElementsByTagName(regElement, "statement");
            for (Element stmtElement: stmtElementList){
                // initilizes stmt and other entities
                MappingStmt stmt = new MappingStmt();
                MappingTopic topic = new MappingTopic();
                MappingCore core = new MappingCore();
                MappingAux aux = new MappingAux();
                reg.getStmtList().add(stmt);
                stmt.setTopic(topic);
                stmt.setCore(core);
                stmt.setAux(aux);
                
                /* reading  values */ 
                String stmt_id = stmtElement.getAttribute("id");
                Element topicElement = (Element) stmtElement.getElementsByTagName("topic").item(0);
                Element coreElement = (Element) stmtElement.getElementsByTagName("core").item(0);
                Element auxElement = (Element) stmtElement.getElementsByTagName("aux").item(0);
                // get topic values
                String topic_text = topicElement.getElementsByTagName("text").item(0).getTextContent();
                String topic_ann = topicElement.getElementsByTagName("annotation").item(0).getTextContent();
                String topic_bow = topicElement.getElementsByTagName("bow").item(0).getTextContent();
                // get core values
                String subject = coreElement.getElementsByTagName("subject").item(0).getTextContent();
                String action = coreElement.getElementsByTagName("action").item(0).getTextContent();
                // get aux values
                String aux_text = auxElement.getElementsByTagName("text").item(0).getTextContent();
                String aux_ann = auxElement.getElementsByTagName("annotation").item(0).getTextContent();
                String aux_bow = auxElement.getElementsByTagName("bow").item(0).getTextContent();

                /* setting  values */   
                stmt.setId(stmt_id);
                //topic
                topic.setText(topic_text);
                topic.setAnnList(Converter.lineToArrayList(topic_ann, ","));
                topic.setBowList(Converter.lineToArrayList(topic_bow, ","));
                //core
                core.setSubList(Converter.lineToArrayList(subject, ","));
                core.setActList(Converter.lineToArrayList(action, ","));
                //aux
                aux.setText(aux_text);
                aux.setAnnList(Converter.lineToArrayList(aux_ann, ","));
                aux.setBowList(Converter.lineToArrayList(aux_bow, ","));
            }
        }
        Print.prln("=================== reglist size = "+regList.size());
    }
    
        private void readTask(){
        ArrayList<Element> taskElementList = MyReader.getXmlDomElements(IN_TASK_FILE, "task");
        taskList.clear();
        for (Element taskElement: taskElementList ){
            MappingTask task = new MappingTask();
            taskList.add(task);

                /* reading   values */ 
                String id = taskElement.getAttribute("id");
                String subject = taskElement.getElementsByTagName("subject").item(0).getTextContent();
                String action = taskElement.getElementsByTagName("action").item(0).getTextContent();
                String ann = taskElement.getElementsByTagName("annotation").item(0).getTextContent();

                /* setting  values */
                task.setId(id);
                task.setSubList(Converter.lineToArrayList(subject, ","));
                task.setActList(Converter.lineToArrayList(action, ","));
                task.setAnnotationList(Converter.lineToArrayList(ann, ","));
        }
        Print.prln("=================== tasklist size = "+taskList.size());
    }
        
    private void write(){
        Print.prln("RegTaskCollector: writing mapping-regulation and mapping-task data to xml files ...");
        writeReg();
        writeTask();
    }
    
    private void writeReg(){
        XmlWriter xml = new XmlWriter(OUT_REG_FILE);
        xml.addAttribute("size", String.valueOf(regList.size()));
        xml.startElement("regulation_data");
        for (MappingRegulation reg: regList){
            String reg_id    = reg.getId();
            xml.addAttribute("id", reg_id);
            xml.startElement("regulation");
            ArrayList<MappingStmt> stmtList = reg.getStmtList();
            for (MappingStmt stmt: stmtList){
                String stmt_id = stmt.getId();
                String separator = ",";
                // get entities
                AbstractMappingEntity topic = stmt.getTopic();
                MappingCore core = stmt.getCore();
                AbstractMappingEntity aux = stmt.getAux();
                // get topic
                String topic_text = topic.getText();
                String topic_ann = Converter.listToString(topic.getAnnList(),separator);
                String topic_bow = Converter.listToString(topic.getBowList(), separator);
                // get core
                String subject_ann = Converter.listToString(core.getSubList(),separator);
                String action_ann = Converter.listToString(core.getActList(),separator);
                // get aux
                String aux_text = aux.getText();
                String aux_ann = Converter.listToString(aux.getAnnList(),separator);
                String aux_bow = Converter.listToString(aux.getBowList(),separator);

                // dump values
                xml.addAttribute("id", stmt_id);
                xml.startElement("statement");
                xml.startElement("topic");
                    xml.addElement("text", topic_text);
                    xml.addElement("annotation", topic_ann);
                    xml.addElement("bow", topic_bow);
                xml.endElement("topic");
                xml.startElement("core");
                    xml.addElement("subject", subject_ann);
                    xml.addElement("action", action_ann);
                xml.endElement("core");
                xml.startElement("aux");
                    xml.addElement("text", aux_text);
                    xml.addElement("annotation", aux_ann);
                    xml.addElement("bow", aux_bow);
                xml.endElement("aux");
                xml.endElement("statement");
            }
            xml.endElement("regulation");
        }
        xml.endElement("regulation_data");
        xml.write(); 
    }
    private void writeTask(){
        String separator = ",";
        XmlWriter xml = new XmlWriter(OUT_TASK_FILE);
        xml.addAttribute("size", String.valueOf(taskList.size()));
        xml.startElement("task_data");
        for (MappingTask task: taskList){
            // get  values
            String task_id    = task.getId();
            String subject_ann = Converter.listToString(task.getSubList(),separator);
            String action_ann = Converter.listToString(task.getActList(),separator);
            String ann = Converter.listToString(task.getAnnotationList(), separator);
            // dump values
            xml.addAttribute("id", task_id);
            xml.startElement("task");
                xml.addElement("subject", subject_ann);
                xml.addElement("action", action_ann);
                xml.addElement("annotation", ann);
            xml.endElement("task");
        }
        xml.endElement("task_data");
        xml.write(); 
    }

    public ArrayList<MappingRegulation> getRegList() {
        return regList;
    }

    public void setRegList(ArrayList<MappingRegulation> regList) {
        this.regList = regList;
    }

    public ArrayList<MappingTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<MappingTask> taskList) {
        this.taskList = taskList;
    }

    
}
