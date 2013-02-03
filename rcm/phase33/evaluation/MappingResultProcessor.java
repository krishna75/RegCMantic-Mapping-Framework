/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase33.evaluation;

import api.xml.XmlWriter;
import helper.mapping.Alignment;
import helper.mapping.ResultTunner;
import helper.util.Print;
import helper.util.RegEx;
import java.util.ArrayList;
import rcm.main.Settings;
import rcm.phase32.mapping.ThreeScores;

/**
 *
 * @author Krishna Sapkota,  Jul 27, 2012,  2:28:32 PM
 * A PhD project at Oxford Brookes University
 */
public class MappingResultProcessor {
    
    /* IO Files*/
    String PHASE3 = Settings.FILES_PATH + "phases_files/PHASE3/";// remove PHASE3, its for temporary to work around the static final problemes
   
    String path = PHASE3 + "3_evaluation/";
    String IN_CM_FILE = path +"computed_mapping.txt";
    String IN_EM_FILE = path +"existing_mapping.txt";
    String OUT_FILE = path +"mapping_result.xml";
    
    /* Local Variables */
    Alignment computedMapping = new Alignment();
    Alignment existingMapping = new Alignment();
    ArrayList<Alignment> emList ;
    ArrayList<Alignment> cmList;
    ArrayList<Alignment> correctList = new ArrayList<Alignment>();
    ArrayList<Alignment> incorrectList = new ArrayList<Alignment>();
    ArrayList<Alignment> tnList = new ArrayList<Alignment>();// there are no validation task to show true negatives
    ArrayList<Alignment> missingList = new ArrayList<Alignment>(); // this is fn (false negative)
    ResultTunner tunner = new ResultTunner();
    ArrayList<ResultTunner> tunnerList = tunner.getTunnerList();
    
    
    int tp ;
    int fp ;
    int fn ;
    double precision;
    double recall;
    double fMeasure ;
    double ACCEPTABLE_MAPPING = 0.85; 
    
    /* tunning related:  */
    int tuningMode = 0;
    boolean tune = false;
    int tuneSize = 40;
    int tuneCount= 1;


    public MappingResultProcessor() {
        init();
        process();
        finish();
    }
    
   private void init(){
       computedMapping.read(IN_CM_FILE);
       existingMapping.read(IN_EM_FILE);
       cmList = computedMapping.getAlignmentList();
       emList = existingMapping.getAlignmentList();

       if (tuningMode == 1){
           tune = true;
       }
//       Print.prln(" cmLIst size - "+ cmList.size()+ " emList size = "+emList.size());
   }
   private void finish(){
       if (tuningMode ==1){
        tunner.print();
        ResultTunner rt = tunner.getHighestTunned();
        ACCEPTABLE_MAPPING = rt.getThreshold();
        Print.prln(" Best Acceptable Mapping Threshold = "+ ACCEPTABLE_MAPPING);
        process();
       }
      write();
   }
   private void process(){
       compare();
       evaluate();
       if (tune){
        tunning();
       }
   }
   private void compare(){
       for (Alignment cm: cmList){
           if (cm.getMappingScore()>= this.ACCEPTABLE_MAPPING){
               
                if ( cmIsInEmList(cm, emList)){
                    correctList.add(cm);
                }else {
                    incorrectList.add(cm);
                }   
           }
        }
       for (Alignment em: emList){
           Alignment map = getEquilvalentAlignment(em, cmList);
           if (map.getMappingScore()>= this.ACCEPTABLE_MAPPING){
           
           }else {
               missingList.add(map);
           }
       }
   }
   private void evaluate(){
       // get int values
       tp = correctList.size();
       fp = incorrectList.size();
       fn = missingList.size();
       Print.prln("tp="+tp+" fp="+fp+", fn="+fn);
       
       // convert to doubles
       double tpD = tp;
       double fpD = fp;
       double fnD = fn;
       
       // compute the accuracy measures
       precision =  tpD/( tpD + fpD );
       recall    =  tpD /( tpD + fnD );
       fMeasure  =  ( 2 * precision * recall ) / ( precision + recall );
       
       // formt to 5 decimal places
//       precision    = RegEx.formatDecimal(precision, 5);
//       recall       = RegEx.formatDecimal(recall, 5);
//       fMeasure     = RegEx.formatDecimal(fMeasure, 5);
       
       Print.prln("precision="+ precision+" recall="+recall+", fmeasure="+fMeasure);
   }
   
   private boolean cmIsInEmList(Alignment a2, ArrayList<Alignment> aList){
//       Print.prln("I am inside isInList");
       boolean found = false;
       for (Alignment a1: aList){
           String r1 = a1.getRegId();
           String r2 = a2.getRegId();
           String t1 = a1.getTaskId();
           String t2 = a2.getTaskId();
//           Print.prln(r1+"="+r2+",   "+t1+"="+t2);
           if (r1.equals(r2) && t1.equals(t2) ){
               found = true;
               break;
           }
       }
//       Print.prln("found =-"+ found);
       return found;
   }
   
      private Alignment getEquilvalentAlignment(Alignment a2, ArrayList<Alignment> aList){
//       Print.prln("I am inside isInList");
      // boolean found = false;
       for (Alignment a1: aList){
           String r1 = a1.getRegId();
           String r2 = a2.getRegId();
           String t1 = a1.getTaskId();
           String t2 = a2.getTaskId();
//           Print.prln(r1+"="+r2+",   "+t1+"="+t2);
           if (r1.equals(r2) && t1.equals(t2)){                
               a2 = a1;
               break;
           }
       }
       return a2;
   }

    public ArrayList<Alignment> getCorrectList() {
        return correctList;
    }

    public void setCorrectList(ArrayList<Alignment> correctList) {
        this.correctList = correctList;
    }

    public double getfMeasure() {
        return fMeasure;
    }

    public void setfMeasure(double fMeasure) {
        this.fMeasure = fMeasure;
    }

    public int getFn() {
        return fn;
    }

    public void setFn(int fn) {
        this.fn = fn;
    }

    public int getFp() {
        return fp;
    }

    public void setFp(int fp) {
        this.fp = fp;
    }

    public ArrayList<Alignment> getIncorrectList() {
        return incorrectList;
    }

    public void setIncorrectList(ArrayList<Alignment> incorrectList) {
        this.incorrectList = incorrectList;
    }

    public ArrayList<Alignment> getMissingList() {
        return missingList;
    }

    public void setMissingList(ArrayList<Alignment> missingList) {
        this.missingList = missingList;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public ArrayList<Alignment> getTnList() {
        return tnList;
    }

    public void setTnList(ArrayList<Alignment> tnList) {
        this.tnList = tnList;
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }
    
    private void tunning(){
        missingList.clear();
        correctList.clear();
        incorrectList.clear();
        ResultTunner rt = new ResultTunner();
        rt.setThreshold(ACCEPTABLE_MAPPING);
        rt.setfMeassure(fMeasure);
        Print.prln("k="+ACCEPTABLE_MAPPING+", f="+fMeasure);
        ACCEPTABLE_MAPPING = ACCEPTABLE_MAPPING -0.01;
        tunnerList.add(rt);
        
//        double highest = tunner.getHighestTunned().getfMeassure();
        if (tuneCount < tuneSize){
             tuneCount++;
             
             process();
        }else {
            tune = false;
        }
        
        
        
    }
  
    private void write(){
        Print.prln("MappingResultProcessor: writing mapping result in a file ...");  
        XmlWriter xml = new XmlWriter(OUT_FILE);
        xml.addAttribute("size", String.valueOf(tp+fp+fn+fp));
        xml.startElement("document");
        
        // summary
        xml.addAttribute("correct", String.valueOf(tp));
        xml.addAttribute("incorrect", String.valueOf(fp));
        xml.addAttribute("missing", String.valueOf(fn));
        xml.startElement("summary");
            xml.addElement("precision", String.valueOf(precision));
            xml.addElement("recall", String.valueOf(recall));
            xml.addElement("f_measure", String.valueOf(fMeasure));
        xml.endElement("summary");
        
        //correct
        xml.addAttribute("count", String.valueOf(tp));
        xml.startElement("correct");
        for (Alignment a: correctList){
            //get
            String mId = a.getMappingId();
            String rId =a.getRegId();
            String tId =a.getTaskId();
            String score =String.valueOf(a.getMappingScore());
            //set
            xml.addAttribute("id", mId);
            xml.startElement("mapping");
                xml.addElement("regulation", rId);
                xml.addElement("task", tId);
                xml.addElement("score", score);
            xml.endElement("mapping");
        }
        xml.endElement("correct");
        
        // incorrect
        xml.addAttribute("count", String.valueOf(fp));
        xml.startElement("incorrect");
        for (Alignment a: incorrectList){
            //get
            String mId = a.getMappingId();
            String rId =a.getRegId();
            String tId =a.getTaskId();
            String score =String.valueOf(a.getMappingScore());
            //set
            xml.addAttribute("id", mId);
            xml.startElement("mapping");
                xml.addElement("regulation", rId);
                xml.addElement("task", tId);
                xml.addElement("score", score);
            xml.endElement("mapping");
        }
        xml.endElement("incorrect");
        
         // missing
        xml.addAttribute("count", String.valueOf(fn));
        xml.startElement("missing");
        for (Alignment a: missingList){
            //get
            String mId = a.getMappingId();
            String rId =a.getRegId();
            String tId =a.getTaskId();
            String score =String.valueOf(a.getMappingScore());
            //set
            xml.addAttribute("id", mId);
            xml.startElement("mapping");
                xml.addElement("regulation", rId);
                xml.addElement("task", tId);
                xml.addElement("score", score);
            xml.endElement("mapping");
        }
        xml.endElement("missing");
        xml.endElement("document");
        xml.write(); 
    }
   

}
