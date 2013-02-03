/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota on 11-Aug-2010 at 23:48:42
 */
public class zzz_RegulationStatement_unused {
    String regID;
    String regBody;
    ArrayList<String> subjectList = new ArrayList<String>();
    String obligation;
    ArrayList<String> actionList = new ArrayList<String>();
    String paragraph;

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getRegBody() {
        return regBody;
    }

    public void setRegBody(String regBody) {
        this.regBody = regBody;
    }

    public zzz_RegulationStatement_unused() {
    }

    public ArrayList<String> getActionList() {
        return actionList;
    }

    public void setActionList(ArrayList<String> actionList) {
        this.actionList = actionList;
    }

    public String getObligation() {
        return obligation;
    }

    public void setObligation(String obligation) {
        this.obligation = obligation;
    }

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    public ArrayList<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(ArrayList<String> subjectList) {
        this.subjectList = subjectList;
    }
    

}
