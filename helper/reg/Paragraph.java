/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.reg;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota on 24-Aug-2010 at 21:17:36
 */
public class Paragraph extends AbstractEntity implements Serializable {
    String paragraphNum;
    ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();

public Paragraph() {
}
public String getParagraphNum() {
    return paragraphNum;
}

public void setParagraphNum(String paragraphNum) {
        this.paragraphNum = paragraphNum;
    }

}
