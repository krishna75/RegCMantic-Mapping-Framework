/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.mapping;

import helper.util.Converter;
import helper.util.MyReader;
import helper.util.MyWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import rcm.main.Settings;
import rcm.phase34.ui._2_MappingData;

/**
 *
 * @author Krishna Sapkota,  Jul 27, 2012,  10:35:19 AM
 * A PhD project at Oxford Brookes University
 */
public class Alignment {        
    
    String mappingId = "";
    String regId= "";
    String taskId = "";
    double mappingScore = 0.0;
    String accuracy = "";
    ArrayList<Alignment> alignmentList = new ArrayList<Alignment>();

    public Alignment() {
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public ArrayList<Alignment> getAlignmentList() {
        return alignmentList;
    }

    public void setAlignmentList(ArrayList<Alignment> alignmentList) {
        this.alignmentList = alignmentList;
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public double getMappingScore() {
        return mappingScore;
    }

    public void setMappingScore(double mappingScore) {
        this.mappingScore = mappingScore;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
 /**
     * 
     * @param filename  is a text file name
     */
    public void write(String filename){
        String content="";
        String header= "mapping_id, reg_id, task_id, score,accuracy \n";
        content += header;
        for (Alignment al: alignmentList){
            String mId = al.getMappingId();
            String rId = al.getRegId();
            String tId = al.getTaskId();
            double score = al.getMappingScore();
            String ac = al.getAccuracy();
            String data = mId+", "+rId+", "+tId+", "+score+", "+ac+"\n";
            content += data;         
        }
        MyWriter.write(content, filename);
    }

    public void read(String filename){
        alignmentList.clear();
        ArrayList<String> lineList= MyReader.fileToArrayList(filename);
        for (String line: lineList){
            // avoid the first line (column names)
            if (lineList.indexOf(line)>0){
               Alignment al = new Alignment();
               alignmentList.add(al);
               //get
               Object[] words = Converter.lineToArrayList(line, ",").toArray();
               String mId =   words[0].toString();
               String rId =   words[1].toString();
               String tId =   words[2].toString();
               double score =  Double.parseDouble(words[3].toString());
               String ac = "";
//               String ac =  words[4].toString();
               
               //st
               al.setMappingId(mId);
               al.setRegId(rId);
               al.setTaskId(tId);
               al.setMappingScore(score);
               al.setAccuracy(ac);
               
            }
        }
        
    }
    
   public void sortAlignmentList(){
        if (alignmentList.size()>=2){
            Collections.sort(alignmentList, new AlignmentComparator());
        }
    }
    
    public class AlignmentComparator implements Comparator<Alignment> {
    @Override
    public int compare(Alignment o1, Alignment o2) {
       Alignment a1 = (Alignment) o1;
                Alignment a2 = (Alignment) o2;
               return Double.valueOf(a2.getMappingScore()).compareTo(Double.valueOf(a1.getMappingScore()));
            }
    }    
}
