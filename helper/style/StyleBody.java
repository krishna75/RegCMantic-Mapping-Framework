/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.style;

import java.io.Serializable;

/**
 *
 * @author Krishna Sapkota 30-Dec-2010  07:42:02
 */
public class StyleBody implements Serializable {
    /** style name implemented in html e.g. ft01, ft02 etc */
    private String name;

    /** content text within a style */
    private String text;

    /** structure of the text e.g. paragraph, section, part etc */
    private String structure;

    /** score based on font size, bold, italic etc. */
    private int score;

    /** level of the style based on the score */
    private int styleLevel;

    /**  whether it contains an obligatory word */
    private boolean obl;

public int getScore() {
    return score;
}

public void setScore(int score) {
    this.score = score;
}

public StyleBody() {
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getStructure() {
    return structure;
}

public void setStructure(String structure) {
    this.structure = structure;
}

public String getText() {
    return text;
}

public void setText(String text) {
    this.text = text;
}

public int getStyleLevel() {
    return styleLevel;
}

public void setStyleLevel(int styleLevel) {
    this.styleLevel = styleLevel;
}

public boolean isObl() {
    return obl;
}

public void setObl(boolean obl) {
        this.obl = obl;
    }
}
