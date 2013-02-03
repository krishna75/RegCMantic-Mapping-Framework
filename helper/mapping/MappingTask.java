/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.mapping;

import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota,  Jul 24, 2012,  3:17:24 PM
 * A PhD project at Oxford Brookes University
 */
public class MappingTask {
    ArrayList<String> subList = new ArrayList<String>();
    ArrayList<String> actList = new ArrayList<String>();
    ArrayList<String> annotationList = new ArrayList<String>();
    
    String id = "";
    String description = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getActList() {
        return actList;
    }

    public void setActList(ArrayList<String> actList) {
        this.actList = actList;
    }

    public ArrayList<String> getSubList() {
        return subList;
    }

    public void setSubList(ArrayList<String> subList) {
        this.subList = subList;
    }

    public ArrayList<String> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(ArrayList<String> annotationList) {
        this.annotationList = annotationList;
    }
    
    

}
