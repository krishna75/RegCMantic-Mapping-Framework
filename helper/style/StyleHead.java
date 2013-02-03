/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.style;

import java.awt.Font;
import java.io.Serializable;

/**
 *
 * @author Krishna Sapkota 30-Dec-2010  07:38:25
 */
public class StyleHead implements Serializable {

    /** style name implemented in html e.g. ft01, ft02 etc */
    private String name;

    /** font style e.g. italic */
    private String  style;

    /** font size e.g. 12px */
    private int size;

    /** font color e.g. black */
    private String color;

    /** font family e.g. arial */
    private String family;

    /** font weight e.g. bold */
    private String weight;

    /** structure e.g paragraph, section, chapter etc. */
    private String structure;

    /** score based on font size, bold, italic etc. */
    private int score=0;

    /** level of the style based on the score */
    private int styleLevel;

    private double content ;

    private double distance ;

    private double oblStatement;

    private double multiSentence;

    private double relativePosition;

    private double predictionIndex ;

    private String predictedStructure;

    /* weights  constants for style score calculation */
    private static final int WEIGHT_FONTSIZE    = 100;
    private static final int WEIGHT_FONTWEIGHT  = 10;
    private static final int WEIGHT_FONTSTYLE   = 1;



    public StyleHead() {
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getScore() {
        if (score==0){
            setScore();
        }
        return score;
    }

    public void setScore(int manualScore) {
        this.score = manualScore;
    }

    public int getStyleLevel() {
        return styleLevel;
    }

    public void setStyleLevel(int styleLevel) {
        this.styleLevel = styleLevel;
    }

    public double getContent() {
        return content;
    }

    public void setContent(double content) {
        this.content = content;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getMultiSentence() {
        return multiSentence;
    }

    public void setMultiSentence(double multiSentence) {
        this.multiSentence = multiSentence;
    }

    public double getOblStatement() {
        return oblStatement;
    }

    public void setOblStatement(double oblStatement) {
        this.oblStatement = oblStatement;
    }

    public double getPredictionIndex() {
        return predictionIndex;
    }

    public void setPredictionIndex(double predictionIndex) {
        this.predictionIndex = predictionIndex;
    }

    public double getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(double relativePosition) {
        this.relativePosition = relativePosition;
    }

    public String getPredictedStructure() {
        return predictedStructure;
    }

    public void setPredictedStructure(String predictedStructure) {
        this.predictedStructure = predictedStructure;
    }

    

    


    
    
  /**=========================|       SCORE CALCULATOR               |===============*/

    public void setScore() {
        int fontStyle = 0;
        int fontWeight = 0;
        int fontSize = getSize();
        if (getStyle().equals("italic")){
            fontStyle= Font.ITALIC;
        }
        if (getWeight().equals("bold")){
            fontWeight= Font.BOLD;
        }
        this.score = fontSize * WEIGHT_FONTSIZE + fontWeight * WEIGHT_FONTWEIGHT + fontStyle * WEIGHT_FONTSTYLE;
    }




}
