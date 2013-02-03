/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.reg;

import java.io.Serializable;
import java.util.ArrayList;
import helper.reg.Regulation;

/**
 *
 * @author Krishna Sapkota on 24-Aug-2010 at 21:17:25
 */
public class Topic extends AbstractEntity implements Serializable {
    String title;
    ArrayList<Regulation> regList = new ArrayList<Regulation>();
    ArrayList<Topic> topicList = new ArrayList<Topic>();
    Topic higherTopic ;
    Topic lowerTopic;
    int totalHierarchy = 0;
    int hierarchyNum = 0 ;
    private String structure;


public ArrayList<Regulation> getRegList() {
    return regList;
}

public void setRegList(ArrayList<Regulation> regList) {
    this.regList = regList;
}

public String getTitle() {
    return title;
}

public void setTitle(String title) {
        this.title = title;
    }
/**
 * Get the value of lowerTopic
 *
 * @return the value of lowerTopic
 */
public Topic getLowerTopic() {
    return lowerTopic;
}
/**
 * Set the value of lowerTopic
 *
 * @param lowerTopic new value of lowerTopic
 */
public void setLowerTopic(Topic lowerTopic) {
    this.lowerTopic = lowerTopic;
}

public Topic getHigherTopic() {
    return higherTopic;
}

public void setHigherTopic(Topic higherTopic) {
    this.higherTopic = higherTopic;
}

/**
 * Get the value of structure
 *
 * @return the value of structure
 */
public String getStructure() {
    return structure;
}

/**
 * Set the value of structure
 *
 * @param structure new value of structure
 */
public void setStructure(String structure) {
        this.structure = structure;
    }

public int getHierarchyNum() {
    return hierarchyNum;
}

public void setHierarchyNum(int hierarchyNum) {
    this.hierarchyNum = hierarchyNum;
}

public int getTotalHierarchy() {
    return totalHierarchy;
}

public void setTotalHierarchy(int totalHierarchy) {
        this.totalHierarchy = totalHierarchy;
    }

public ArrayList<Topic> getTopicList() {
    return topicList;
}

public void setTopicList(ArrayList<Topic> topicList) {
        this.topicList = topicList;
    }

}


