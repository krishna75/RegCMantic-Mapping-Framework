/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.mapping;

import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota,  Jul 24, 2012,  9:20:36 PM
 * A PhD project at Oxford Brookes University
 */
public class AbstractMappingEntity {
        String text = "";
        ArrayList<String> annList = new ArrayList<String>();
        ArrayList<String> bowList = new ArrayList<String>();

    public ArrayList<String> getAnnList() {
        return annList;
    }

    public void setAnnList(ArrayList<String> annList) {
        this.annList = annList;
    }

    public ArrayList<String> getBowList() {
        return bowList;
    }

    public void setBowList(ArrayList<String> bowList) {
        this.bowList = bowList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
