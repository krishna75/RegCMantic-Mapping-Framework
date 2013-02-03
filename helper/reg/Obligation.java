/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.reg;

import java.io.Serializable;

/**
 *
 * @author Krish
 */
public class Obligation extends AbstractEntity implements Serializable {
    private String type;
    private String strength;

    public Obligation() {
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
