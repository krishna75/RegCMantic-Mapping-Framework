/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase33.evaluation;

import helper.mapping.Alignment;
import helper.util.Print;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import rcm.main.Settings;
import rcm.phase32.mapping.ThreeScores;

/**
 *
 * @author Krishna Sapkota,  Jul 27, 2012,  12:02:55 PM
 * A PhD project at Oxford Brookes University
 */
public class ComputedMappingCollector {
    
    /* IO Files*/
    String PHASE3 = Settings.FILES_PATH + "phases_files/PHASE3/";// remove PHASE3, its for temporary to work around the static final problemes
    String OUT_FILE = PHASE3 +"3_evaluation/computed_mapping.txt";

    /* Local Variables */
    ThreeScores threeScores = new ThreeScores();
    Alignment alignment = new Alignment();    
    ArrayList<ThreeScores> tsList;
    ArrayList<Alignment> alList = alignment.getAlignmentList();
    CopyOnWriteArrayList alCowList = new CopyOnWriteArrayList();    // concurrent modifications are allowed in n COW list
    
    double ACCEPTABLE_MAPPING = .85;

    public ComputedMappingCollector() {
        init();
        process();
        finish();    
    }
    

    private void init(){
        threeScores.read();
        tsList = threeScores.getThreeScoresList();
        
    }
    private void finish(){
        alList.addAll(alCowList);
        alignment.sortAlignmentList();
        alignment.write(OUT_FILE);
    }
    private void process(){
        for (ThreeScores ts: tsList){
            // gettting
            String mId = ts.getMappingId();
            String rId = ts.getRegId();
            String tId = ts.getTaskId();
            double score = ts.getFinalScore();
            // setting
            Alignment al = new Alignment();
            al.setMappingId(mId);
            al.setRegId(rId);
            al.setTaskId(tId);
            al.setMappingScore(score);
            if (al.getMappingScore()>=ACCEPTABLE_MAPPING){
                addAlignment(al);
            }
        }
    }
    
    private void addAlignment(Alignment a2){ 
        if (alCowList.isEmpty()){
            alCowList.add(a2);
        }
        boolean found = false;
        Iterator iter = alCowList.iterator();
        while (iter.hasNext()){
            Alignment a1 = (Alignment)iter.next();
            if (areSame(a1,a2)) {
                found = true;
                if (a2.getMappingScore()>=a1.getMappingScore()){
                    a1.setMappingScore(a2.getMappingScore());
                }
            }  
        }
        if (!found){
            alCowList.add(a2);
            //Print.prln(" alCowList size ="+ alCowList.size());
        }
        
    }
    private boolean areSame(Alignment a1, Alignment a2){
        boolean found = false;
        if (a1.getRegId().equals(a2.getRegId()) && a1.getTaskId().equals(a2.getTaskId())){
            found = true;
        }
        return found;
    }
    
}
