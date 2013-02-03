/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.reg;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Krish
 */
public class Statement extends AbstractEntity implements Serializable {
    Obligation obligation;
    ArrayList<String> subAnnList = new ArrayList<String>();
    ArrayList<String> actAnnList = new ArrayList<String>();
    ArrayList<String> howAnnList = new ArrayList<String>();
    ArrayList<String> whenAnnList = new ArrayList<String>();
    ArrayList<String> whyAnnList = new ArrayList<String>();
    ArrayList<String> whereAnnList = new ArrayList<String>();

    public Obligation getObligation() {
        return obligation;
    }

    public void setObligation(Obligation obligation) {
        this.obligation = obligation;
    }

    public ArrayList<String> getActAnnList() {
        return actAnnList;
    }

    public void setActAnnList(ArrayList<String> actAnnList) {
        this.actAnnList = actAnnList;
    }

    public ArrayList<String> getSubAnnList() {
        return subAnnList;
    }

    public void setSubAnnList(ArrayList<String> subAnnList) {
        this.subAnnList = subAnnList;
    }

    public ArrayList<String> getHowAnnList() {
        return howAnnList;
    }

    public void setHowAnnList(ArrayList<String> howAnnList) {
        this.howAnnList = howAnnList;
    }

    public ArrayList<String> getWhenAnnList() {
        return whenAnnList;
    }

    public void setWhenAnnList(ArrayList<String> whenAnnList) {
        this.whenAnnList = whenAnnList;
    }

    public ArrayList<String> getWhereAnnList() {
        return whereAnnList;
    }

    public void setWhereAnnList(ArrayList<String> whereAnnList) {
        this.whereAnnList = whereAnnList;
    }

    public ArrayList<String> getWhyAnnList() {
        return whyAnnList;
    }

    public void setWhyAnnList(ArrayList<String> whyAnnList) {
        this.whyAnnList = whyAnnList;
    }   
}
