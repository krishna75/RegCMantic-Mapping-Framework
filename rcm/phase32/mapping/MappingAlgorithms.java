/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase32.mapping;

import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota,  Jul 26, 2012,  9:21:24 PM
 * A PhD project at Oxford Brookes University
 */
public class MappingAlgorithms {
    ThreeScores threeScores = new ThreeScores(); 
    ArrayList<ThreeScores> tsList ;

    public MappingAlgorithms() {
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
        threeScores.write();
        threeScores.writeCsv();      
    }
    private void process(){
        // compute
        for (ThreeScores ts: tsList){
            //applyCascadingPriority(ts);
//            applyCascadingPriority2(ts);
//         applyCascadingPriority3(ts);
//            this.applyWeightedAverage(ts);
//                     applyCascadingPriority4(ts);
//             applyCascadingPriority5(ts);
             applyCascadingPriority6(ts);
        }
    }
    
 /**
     * 
     * @param ts is the class bearing three scores to be processed
     * @return the three scores class updated with the calculated final score.
     */
    private ThreeScores applyCascadingPriority(ThreeScores ts){
        // weight assignment
        double ALPHA    = 0.9;
        double BETA    = 0.8;
        double GAMMA   = 0.7;
        
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
        double ALPHA    = 0.9;
        double BETA    = 0.8;
        double GAMMA   = 0.7;
        
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
        double ALPHA    = 0.9;
        double BETA    = 0.8;
        double GAMMA   = 0.7;
        
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
        double ALPHA    = 0.9;
        double BETA    = 0.8;
        double GAMMA   = 0.7;
        
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
    
          /**
     * 
     * @param ts is the class bearing three scores to be processed
     * @return the three scores class updated with the calculated final score.
     */
    private ThreeScores applyCascadingPriority4(ThreeScores ts){
        // weight assignment
//        double ALPHA    = 0.9;
//        double BETA    = 0.8;
//        double GAMMA   = 0.7;
        
        // get the three scores
        double topic = ts.getTopicScore();
        double core = ts.getCoreScore();
        double aux = ts.getAuxScore();
        double finalScore = 0.0;
        
//        double wT = ALPHA * topic;
//        double wC= BETA * core;
//        double wA = GAMMA * aux;
//        double wSum = (wT+wC+wA)/(ALPHA + BETA + GAMMA) ;
        
        /* apply the heuristics (cascading priority)*/
        // if topic score is the highest
        if (topic>=core && topic>=aux){
           finalScore = topic;
        }
  
        // core is the highest
        if (core>=topic && topic>aux ){
           finalScore = (topic+core)/2;
        }
        
        // aux is the highest
        if (aux>=topic && topic>core ){
           finalScore = (topic+aux)/2;
        }
        // if aux score are the highest
        if (core>=topic && aux>=topic){
           finalScore = (topic+core+aux)/3;
        }  
        ts.setFinalScore(finalScore);
        return ts;
    }
    
        
          /**
     * 
     * @param ts is the class bearing three scores to be processed
     * @return the three scores class updated with the calculated final score.
     */
    private ThreeScores applyCascadingPriority5(ThreeScores ts){
        // weight assignment
//        double ALPHA    = 0.9;
//        double BETA    = 0.8;
//        double GAMMA   = 0.7;
        
        // get the three scores
        double topic = ts.getTopicScore();
        double core = ts.getCoreScore();
        double aux = ts.getAuxScore();
        double finalScore = 0.0;
        
//        double wT = ALPHA * topic;
//        double wC= BETA * core;
//        double wA = GAMMA * aux;
//        double wSum = (wT+wC+wA)/(ALPHA + BETA + GAMMA) ;
        
        /* apply the heuristics (cascading priority)*/
        // core is the highest
        if (core>=topic && core>=aux){
           finalScore = core;
        }
  
        if (topic>=core && core>aux ){
           finalScore = (topic+core)/2;
        }
        if (aux>=core && core>topic ){
           finalScore = (core+aux)/2;
        }
        // if aux score is the highest
        if (topic>=core && aux>=core){
           finalScore = (topic+core+aux)/3;
        }  
        ts.setFinalScore(finalScore);
        return ts;
    }
    
            /**
     * 
     * @param ts is the class bearing three scores to be processed
     * @return the three scores class updated with the calculated final score.
     */
    private ThreeScores applyCascadingPriority6(ThreeScores ts){
        // weight assignment
//        double ALPHA    = 0.9;
//        double BETA    = 0.8;
//        double GAMMA   = 0.7;
        
        // get the three scores
        double topic = ts.getTopicScore();
        double core = ts.getCoreScore();
        double aux = ts.getAuxScore();
        double finalScore = 0.0;
        
//        double wT = ALPHA * topic;
//        double wC= BETA * core;
//        double wA = GAMMA * aux;
//        double wSum = (wT+wC+wA)/(ALPHA + BETA + GAMMA) ;
        
        /* apply the heuristics (cascading priority)*/
        // core is the highest
        double topicCore = Math.max(topic, core);
        if (topicCore>= aux){
            finalScore = topicCore;
        }else{
            finalScore = (topicCore + aux)/2;
        } 
        ts.setFinalScore(finalScore);
        return ts;
    }
}
