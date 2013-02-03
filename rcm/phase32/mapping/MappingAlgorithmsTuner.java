/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase32.mapping;

import helper.mapping.WeightTunner;
import helper.util.Print;
import java.util.ArrayList;
import rcm.phase33.evaluation.ComputedMappingCollector;
import rcm.phase33.evaluation.MappingResultProcessor;
import rcm.phase33.evaluation.MappingResultProcessorTunner;

/**
 *
 * @author Krishna Sapkota,  Jul 26, 2012,  9:21:24 PM
 * A PhD project at Oxford Brookes University
 */
public class MappingAlgorithmsTuner {
    ThreeScores threeScores = new ThreeScores(); 
    ArrayList<ThreeScores> tsList ;
    WeightTunner weightTunner  = new WeightTunner();
    ArrayList<WeightTunner> tunnerList = weightTunner.getTunnerList();
    WeightTunner tunner = new WeightTunner();
    double ALPHA ;
    double BETA ;
    double GAMMA;
    double tu = 0.1;

    public MappingAlgorithmsTuner() {
        init();
        process();
        finish();
    }
    
    private void init(){
        // initialize
        threeScores.read();
        tsList = threeScores.getThreeScoresList();
    }
    private void finish(){
        //write
//        threeScores.write();
//        threeScores.writeCsv(); 
        weightTunner.write();
//        weightTunner.read();
        WeightTunner t = weightTunner.getBestTunner();
        Print.prln ("alpha="+t.getAlpha() +", beta="+t.getBeta()+", gamma="+t.getGamma()+", threshold="+ t.getThreshold()+"f-measure="+t.getfMeasure());
    }
    private void process(){
        // compute
        tune();
    }
    
    private void tune(){
        for ( ALPHA = 1.0; ALPHA >0.0; ALPHA = ALPHA - tu ){    
            for ( BETA = 1.0; BETA >0.0; BETA = BETA - tu ){
                for ( GAMMA = 1.0; GAMMA >0.0; GAMMA = GAMMA - tu ){
                    // new tunner
                        tunner = new WeightTunner();
                        tunnerList.add(tunner);
                        tunner.setAlpha(ALPHA);
                        tunner.setBeta(BETA);
                        tunner.setGamma(GAMMA);
                    
                    for (ThreeScores ts: tsList){                      
                        // apply algorithm 
                        //applyCascadingPriority(ts);
//                        applyCascadingPriority2(ts);
                        applyCascadingPriority3(ts);
//                        applyWeightedAverage(ts);
                        //tunner.setFinalScore(ts.getFinalScore());
                    }
                    
                    threeScores.write();
                    //collect and compute f-measure
                    new ComputedMappingCollector();
                    MappingResultProcessorTunner  result = new MappingResultProcessorTunner();
                    tunner.setPrecision(result.getPrecision());
                    tunner.setRecall(result.getRecall());
                    tunner.setfMeasure(result.getfMeasure());
                    tunner.setThreshold(result.getThreshold());
                    Print.prln ("alpha="+tunner.getAlpha() +", beta="+tunner.getBeta()+", gamma="+tunner.getGamma()+"f-measure="+tunner.getfMeasure());
                }    
            }
        }
    }
    
 /**
     * 
     * @param ts is the class bearing three scores to be processed
     * @return the three scores class updated with the calculated final score.
     */
    private ThreeScores applyCascadingPriority(ThreeScores ts){
        
        // weight assignment
//        double ALPHA    = 0.9;
//        double BETA    = 0.8;
//        double GAMMA   = 0.7;
        
        // get the three scores
        double topic = ts.getTopicScore();
        double core = ts.getCoreScore();
        double aux = ts.getAuxScore();
        double finalScore = 0.0;
        
        /* apply the heuristics (cascading priority)*/
        // if topic score is the highest
        if (topic>=core && topic>=aux){
           finalScore = (ALPHA * topic) + ((1-ALPHA)* Math.max(core, aux));
        }
        // if core score is the highest
        if (core>=topic && core>=aux){
           finalScore = (BETA * core) + ((1-BETA)* Math.max(topic, aux));
        }
        // if aux score is the highest
        if (aux>=topic && aux>=core){
           finalScore = (GAMMA * aux) + ((1-GAMMA)* Math.max(topic, core));
        }  
        ts.setFinalScore(finalScore);
        
        return ts;
    }
    
    
     /**
     * 
     * @param ts is the class bearing three scores to be processed
     * @return the three scores class updated with the calculated final score.
     */
    private ThreeScores applyCascadingPriority2(ThreeScores ts){
        // weight assignment
//        double ALPHA    = 0.9;
//        double BETA    = 0.8;
//        double GAMMA   = 0.7;
        
        // get the three scores
        double topic = ts.getTopicScore();
        double core = ts.getCoreScore();
        double aux = ts.getAuxScore();
        double finalScore = 0.0;
        
        /* apply the heuristics (cascading priority)*/
        // if topic score is the highest
        if (topic>=core && topic>=aux){
           finalScore = (ALPHA * topic) + ((1-ALPHA)* ((BETA * core) + (GAMMA * aux))/(BETA + GAMMA));
        }
        // if core score is the highest
        if (core>=topic && core>=aux){
           finalScore = (BETA * core) + ((1-BETA)* ((ALPHA * topic) + (GAMMA * aux))/(ALPHA + GAMMA));
        }
        // if aux score is the highest
        if (aux>=topic && aux>=core){
           finalScore = (GAMMA * aux) + ((1-GAMMA)* ((ALPHA * topic) + (BETA * core))/(ALPHA + BETA));
        }  
        ts.setFinalScore(finalScore);
        return ts;
    }
    
       /**
     * 
     * @param ts is the class bearing three scores to be processed
     * @return the three scores class updated with the calculated final score.
     */
    private ThreeScores applyCascadingPriority3(ThreeScores ts){
        // weight assignment
//        double ALPHA    = 0.9;
//        double BETA    = 0.8;
//        double GAMMA   = 0.7;
        
        // get the three scores
        double topic = ts.getTopicScore();
        double core = ts.getCoreScore();
        double aux = ts.getAuxScore();
        double finalScore = 0.0;
        
        double wT = ALPHA * topic;
        double wC= BETA * core;
        double wA = GAMMA * aux;
        double wSum = (wT+wC+wA)/(ALPHA + BETA + GAMMA) ;
        
        /* apply the heuristics (cascading priority)*/
        // if topic score is the highest
        if (topic>=core && topic>=aux){
           finalScore = wT + ((1-ALPHA)* wSum);
        }
        // if core score is the highest
        if (core>=topic && core>=aux){
           finalScore = (BETA * core) + ((1-BETA)* wSum);
        }
        // if aux score is the highest
        if (aux>=topic && aux>=core){
           finalScore = (GAMMA * aux) + ((1-GAMMA)* wSum);
        }  
        ts.setFinalScore(finalScore);
        return ts;
    }
    
    /**
     * 
     * @param ts is the class bearing three scores to be processed
     * @return the three scores class updated with the calculated final score.
     */
    private ThreeScores applyWeightedAverage(ThreeScores ts){
        // weight assignment
//        double ALPHA    = 0.9;
//        double BETA    = 0.8;
//        double GAMMA   = 0.7;
        
        // get the three scores
        double topic = ts.getTopicScore();
        double core = ts.getCoreScore();
        double aux = ts.getAuxScore();
        double finalScore = 0.0;
        
        // computation of weighted average
        finalScore = ((ALPHA * topic) + (BETA * core) + (GAMMA * aux))/(ALPHA + BETA + GAMMA);
        
        ts.setFinalScore(finalScore);
        return ts;
    }
    
    

}
