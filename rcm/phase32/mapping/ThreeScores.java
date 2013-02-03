/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase32.mapping;

import api.xml.XmlWriter;
import helper.mapping.MappingTask;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.MyWriter;
import helper.util.Print;
import helper.util.RegEx;
import java.util.ArrayList;
import org.w3c.dom.Element;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota,  Jul 26, 2012,  12:17:50 PM
 * A PhD project at Oxford Brookes University
 */
public class ThreeScores {
    
    /* IO Files*/
    String PHASE3 = Settings.FILES_PATH + "phases_files/PHASE3/";// remove PHASE3, its for temporary to work around the static final problemes
    String path = PHASE3+"2_mapping/";
    String IN_FILE = path + "three_scores.xml" ;
    String OUT_FILE = path + "three_scores.xml" ;
    String OUT_CSV_FILE = path +"three_scores.csv";
    
    /* Local Variables */
    ArrayList<ThreeScores> threeScoresList = new ArrayList<ThreeScores>(); 
    
    int DECIMAL_PLACES = 5;
    double topicScore = 0.0;
    double coreScore= 0.0;
    double auxScore= 0.0;
    double finalScore = 0.0;
    String mappingId = "";
    String regId = "";
    String stmtId = "";
    String taskId = "";
    
    public ThreeScores() {
    }

    public double getAuxScore() {
        return auxScore;
    }

    public void setAuxScore(double auxScore) {
        this.auxScore = auxScore;
    }

    public double getCoreScore() {
        return coreScore;
    }

    public void setCoreScore(double coreScore) {
        this.coreScore = coreScore;
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getStmtId() {
        return stmtId;
    }

    public void setStmtId(String stmtId) {
        this.stmtId = stmtId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ArrayList<ThreeScores> getThreeScoresList() {
        return threeScoresList;
    }

    public void setThreeScoresList(ArrayList<ThreeScores> threeScoresList) {
        this.threeScoresList = threeScoresList;
    }

    public double getTopicScore() {
        return topicScore;
    }

    public void setTopicScore(double topicScore) {
        this.topicScore = topicScore;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }
    
    
    public void write(){
        Print.prln("ThreeScores: writing three scores in a file ...");  
        XmlWriter xml = new XmlWriter(OUT_FILE);
        xml.addAttribute("size", String.valueOf(threeScoresList.size()));
        xml.startElement("document");
        for (ThreeScores ts: threeScoresList){
            
            // get  values
            String mapping_id    = ts.getMappingId();
            String reg_id    = ts.getRegId();
            String stmt_id    = ts.getStmtId();
            String task_id    = ts.getTaskId();
            double topic_score    = ts.getTopicScore();
            double core_score    = ts.getCoreScore();
            double aux_score   = ts.getAuxScore();
            double final_score = ts.getFinalScore();
            
            // rounding up long decimal places
            topic_score = RegEx.formatDecimal(topic_score, DECIMAL_PLACES);
            core_score = RegEx.formatDecimal(core_score, DECIMAL_PLACES);
            aux_score = RegEx.formatDecimal(aux_score, DECIMAL_PLACES);
            final_score = RegEx.formatDecimal(final_score, DECIMAL_PLACES);
            
            // dump into xml
            xml.addAttribute("mapping_id", mapping_id);
            xml.startElement("mapping");
                xml.addElement("reg_id",reg_id);
                xml.addElement("stmt_id",stmt_id);
                xml.addElement("task_id",task_id);
                xml.addElement("topic_score",String.valueOf(topic_score));
                xml.addElement("core_score",String.valueOf(core_score));
                xml.addElement("aux_score",String.valueOf(aux_score)); 
                xml.addElement("final_score",String.valueOf(final_score));
            xml.endElement("mapping");    
        }
        xml.endElement("document");
        xml.write(); 
    }
    
    public void writeCsv(){
        String content ="";
        String columns = "mapping_id, reg_id, stmt_id, task_id, topic_score,core_score, aux_score,final_score\n";
        content += columns;
        for (ThreeScores ts : threeScoresList){
            String mapping_id = ts.getMappingId();
            String reg_id = ts.getRegId();
            String stmt_id = ts.getStmtId();
            String task_id = ts.getTaskId();
            double topic_score = ts.getTopicScore();
            double core_score = ts.getCoreScore(); 
            double aux_score = ts.getAuxScore(); 
            double final_score = ts.getFinalScore();
            String data = mapping_id +", "+
                    reg_id +", "+
                    stmt_id +", "+
                    task_id +", "+
                    topic_score +", "+
                    core_score +", "+
                    aux_score +", "+
                    final_score+"\n" ;
           content += data;
        }
        MyWriter.write(content, OUT_CSV_FILE);  
    }
    
    public void read(){
        ArrayList<Element> mappingElementList = MyReader.getXmlDomElements(IN_FILE, "mapping");
        threeScoresList.clear();
        for (Element mappingElement: mappingElementList ){
            ThreeScores ts = new ThreeScores();
            threeScoresList.add(ts);

                /* reading   values */ 
                String mapping_id = mappingElement.getAttribute("mapping_id");
                String reg_id = mappingElement.getElementsByTagName("reg_id").item(0).getTextContent();
                String stmt_id = mappingElement.getElementsByTagName("stmt_id").item(0).getTextContent();
                String task_id = mappingElement.getElementsByTagName("task_id").item(0).getTextContent();
                double topic_score = Double.valueOf(mappingElement.getElementsByTagName("topic_score").item(0).getTextContent());
                double core_score = Double.valueOf(mappingElement.getElementsByTagName("core_score").item(0).getTextContent());
                double aux_score = Double.valueOf(mappingElement.getElementsByTagName("aux_score").item(0).getTextContent());
                double final_score = Double.valueOf(mappingElement.getElementsByTagName("final_score").item(0).getTextContent());
                /* setting  values */
                ts.setMappingId(mapping_id);
                ts.setRegId(reg_id);
                ts.setStmtId(stmt_id);
                ts.setTaskId(task_id);
                ts.setTopicScore(topic_score);
                ts.setCoreScore(core_score);
                ts.setAuxScore(aux_score);
                ts.setFinalScore(final_score);
        }
        Print.prln("=================== three scores list size = "+threeScoresList.size());
    }
    
    public void generateMappingId(){
        int counter = 1 ;
        for (ThreeScores ts : threeScoresList){
            String mapping_id = "mid_"+counter;
            ts.setMappingId(mapping_id);
            counter++;
        }
    }
}
