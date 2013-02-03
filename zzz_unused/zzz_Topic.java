/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import zzz_unused.zzz_SubTopic;
import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota on 24-Aug-2010 at 21:17:06
 */
public class zzz_Topic {
    String id;
    String title;
    String desc;
    ArrayList<String> subjectAnnotationList = new ArrayList<String>();
    ArrayList<zzz_SubTopic> subtopicList = new ArrayList<zzz_SubTopic>();
    String regBody;

    public String getRegBody() {
        return regBody;
    }

    public void setRegBody(String regBody) {
        this.regBody = regBody;
    }

    public zzz_Topic() {
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

    public ArrayList<String> getSubjectAnnotationList() {
        return subjectAnnotationList;
    }

    public void setSubjectAnnotationList(ArrayList<String> subjectAnnotationList) {
        this.subjectAnnotationList = subjectAnnotationList;
    }

    public ArrayList<zzz_SubTopic> getSubtopicList() {
        return subtopicList;
    }

    public void setSubtopicList(ArrayList<zzz_SubTopic> subtopicList) {
        this.subtopicList = subtopicList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
}
