/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.ont;

import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota on 12-Jul-2010 at 12:38:58
 */
public class OntStatement {
    public String subject = "";
    public String property= "";
    public String object= "";


    public OntStatement(String subject, String property, String object) {
        this.subject = subject;
        this.property = property;
        this.object = object;
    }

    

   
    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }




}
