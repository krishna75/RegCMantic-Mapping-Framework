/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.mapping;

import helper.util.MyReader;
import helper.util.MyWriter;
import helper.util.RegEx;
import java.util.ArrayList;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota,  Aug 21, 2012,  5:02:40 PM
 * A PhD project at Oxford Brookes University
 */
public class WeightTunner {
    /* IO Files*/
    String PHASE3 = Settings.FILES_PATH + "phases_files/PHASE3/";// remove PHASE3, its for temporary to work around the static final problemes
    String path = PHASE3 + "3_evaluation/";
    String OUT_FILE = path + "weight_tunner.txt";
    String IN_FILE = OUT_FILE;
    
    String id;
    double alpha;
    double beta;
    double gamma;
    double threshold;
    double precision;
    double recall;
    double fMeasure;
    
    ArrayList<WeightTunner> tunnerList = new ArrayList<WeightTunner>();

    public WeightTunner() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getfMeasure() {
        return fMeasure;
    }

    public void setfMeasure(double fMeasure) {
        this.fMeasure = fMeasure;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
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

    public ArrayList<WeightTunner> getTunnerList() {
        return tunnerList;
    }

    public void setTunnerList(ArrayList<WeightTunner> tunnerList) {
        this.tunnerList = tunnerList;
    }
    
    public WeightTunner getBestTunner(){
        WeightTunner bestTunner = new WeightTunner();
        for (WeightTunner t: tunnerList){
           if( t.getfMeasure()> bestTunner.getfMeasure()){
               bestTunner = t;
           }
        }
        return bestTunner;
    }
    public void read(){
       tunnerList.clear();
       ArrayList<String> lineList =  MyReader.fileToArrayList(IN_FILE);
       for (String line: lineList){
           ArrayList<String> tokenList = RegEx.getTokenizedList(line, ",");
           if (lineList.indexOf(line)>0){
               WeightTunner tunner = new WeightTunner();
               tunner.setAlpha(Double.valueOf(tokenList.get(1).toString()));
               tunner.setBeta(Double.valueOf(tokenList.get(2).toString()));
               tunner.setGamma(Double.valueOf(tokenList.get(3).toString()));
               tunner.setThreshold(Double.valueOf(tokenList.get(4).toString()));
               tunner.setPrecision(Double.valueOf(tokenList.get(5).toString()));
               tunner.setRecall(Double.valueOf(tokenList.get(6).toString()));
               tunner.setfMeasure(Double.valueOf(tokenList.get(7).toString()));
               tunnerList.add(tunner);
           }
       }
        
    }
    public void write(){
       String content = "sn,alpha,beta,gamma,threshold,precision,recall,fmeasure\n";
       int counter = 1;
       for (WeightTunner tunner: tunnerList){
           content += counter+",";
           counter++;
           content += tunner.getAlpha()+",";
           content += tunner.getBeta()+",";
           content += tunner.getGamma()+",";
           content += tunner.getThreshold()+",";
           content += tunner.getPrecision()+",";
           content += tunner.getRecall()+",";
           content += tunner.getfMeasure()+"\n";      
        }
       MyWriter.write(content, OUT_FILE);
    }
            

}
