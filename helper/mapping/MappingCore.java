/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.mapping;

import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota,  Jul 24, 2012,  3:16:55 PM
 * A PhD project at Oxford Brookes University
 */
public class MappingCore {
    ArrayList<String> subList = new ArrayList<String>();
    ArrayList<String> actList = new ArrayList<String>();

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
}
