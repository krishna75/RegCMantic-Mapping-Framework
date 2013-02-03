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
public class Regulation extends AbstractEntity implements Serializable {
    ArrayList<Statement> stmtList = new ArrayList<Statement>();
    Topic  topic = new Topic();

public ArrayList<Statement> getStmtList() {
    return stmtList;
}

public void setStmtList(ArrayList<Statement> stmtList) {
        this.stmtList = stmtList;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

}
