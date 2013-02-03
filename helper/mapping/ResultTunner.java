/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.mapping;

import helper.util.Print;
import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota,  Aug 8, 2012,  4:32:27 PM
 * A PhD project at Oxford Brookes University
 */
public class ResultTunner {
    double fMeassure = 0.0;
    double threshold = 0.0;
    ArrayList<ResultTunner> tunnerList = new ArrayList<ResultTunner>();

    public double getfMeassure() {
        return fMeassure;
    }

    public void setfMeassure(double fMeassure) {
        this.fMeassure = fMeassure;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public ArrayList<ResultTunner> getTunnerList() {
        return tunnerList;
    }

    public void setTunnerList(ArrayList<ResultTunner> tunnerList) {
        this.tunnerList = tunnerList;
    }
    
    public ResultTunner getHighestTunned(){
        ResultTunner tunner = new ResultTunner();
        for (ResultTunner t: tunnerList){
           if( t.getfMeassure()> tunner.getfMeassure()){
               tunner = t;
           }
        }     
        return tunner;   
    }
    

    public void print(){
        for (ResultTunner t: tunnerList){
           Print.prln("k="+t.getThreshold()+", f="+ t.getfMeassure());
        }  
    }

}
