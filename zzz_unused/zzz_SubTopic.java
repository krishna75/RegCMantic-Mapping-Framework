/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import java.util.ArrayList;
import helper.reg.Regulation;
import helper.reg.Regulation;

/**
 *
 * @author Krishna Sapkota on 24-Aug-2010 at 21:17:25
 */
public class zzz_SubTopic {
    String id;
    String title;
    String desc;
    ArrayList<String> subjectAnnotationList = new ArrayList<String>();
    ArrayList<Regulation> regulationList = new ArrayList<Regulation>();

    public zzz_SubTopic() {
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Regulation> getRegulationList() {
        return regulationList;
    }

    public void setRegulationList(ArrayList<Regulation> regulationList) {
        this.regulationList = regulationList;
    }

    public ArrayList<String> getSubjectAnnotationList() {
        return subjectAnnotationList;
    }

    public void setSubjectAnnotationList(ArrayList<String> subjectAnnotationList) {
        this.subjectAnnotationList = subjectAnnotationList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    

}
