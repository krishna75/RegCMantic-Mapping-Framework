
package rcm.phase1;

import rcm.phase1._2_FeatureReader;
import gate.util.Out;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import rcm.main.Settings;
import helper.util.RegEx;
import helper.style.StyleBody;
import helper.style.StyleHead;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.MyWriter;
import helper.util.Print;

/**
 *  It predicts the structure of a document. Based on the feature in the document, it computes all the font feature,
 * content, distance from the standard practice, and predicts the structure of the document. In the process, it needs
 * list of head and body styles, which was preprocessed by the pReader.
 * @author Krishna Sapkota, 30-Jun-2011,   11:57:00
 * A PhD project at Oxford Brookes University
 */
public class _3_StructurePredictor implements Serializable {
    
    /* IO FILES */
    private String IN_LAST_STRUCTURE_FILE = Settings.PHASE1+"styles/recent_structures.txt";
    private String IN_FEATURE_FILE = Settings.PHASE1 + "feature_reader.ser";
    private String OUT_PREDICTION_FILE = Settings.PHASE1 +"structure_predictor.ser";

    /*  head list retrieved after feature reader */
    ArrayList<StyleHead> headList;

    /*  body list retrieved after feature reader todo */
    ArrayList<StyleBody> bodyList;

    /*  processed feature reader  */
    _2_FeatureReader fReader;
    String[] components = new String[] {"chapter","section","subsection","page","page no"};
    
    /*  calculation related */
    private int standardParaSize = 12; // usually the font size of the normal paragraphs is 12 pt.
    private int weight_sentence = 1; // sometimes, some aspect is more important than others, therefore, we need to cosider the weight
    private int weight_content = 1;
    private int weight_obl = 1;
    private int weight_distance = 1;
   
/**
 * Constructor reads the head list and body list from  serialised  feature reader
 */
public _3_StructurePredictor() {
    fReader = (_2_FeatureReader) MyReader.fileToObject(IN_FEATURE_FILE);
//        fReader = new pReader();
    headList = fReader.getHeadList();
    bodyList = fReader.getBodyList();
    compute();
}
/**  OPTIONAL: This constructor reads the feature reader dynamically (not the serialised one)   */
public _3_StructurePredictor(_2_FeatureReader fReader) {
    this.fReader = fReader;
    this.headList = fReader.getHeadList();
    this.bodyList = fReader.getBodyList();
    compute();
}
/**  OPTIONAL: the constructure to be used when the head list and the body list are provided seperately   */
public _3_StructurePredictor(ArrayList<StyleHead> headList, ArrayList<StyleBody> bodyList) {
    this.headList = headList;
    this.bodyList = bodyList;
    compute();
}
/**  incorporates the overall methods  */
private void compute(){
    /* the three main processes */
    predictParagraph();
    predictOtherComponents();
    fillTheRest();

    /*  the extras */
    printHeadList();
    MyWriter.write(this, OUT_PREDICTION_FILE);
    Out.prln("*** STRUCTURE PREDICTION COMPLETED *** ");
}
/**
 *  predicts the structure as a paragraph based on the overall calculation
 */
private void predictParagraph(){
    computeSentence();
    computeContent();
    computeObl();
    computeDistance();
    computePredictionIndex();    
}
/**  computes the percentage of  sentence in each level  */
private void computeSentence(){
    String text = "";
    String pattern = "\\. "; // counts only those sentences which end with '.' and space (followed by another sentence)

    /*  finding the total size*/
    for (StyleBody body: bodyList){
        text += body.getText();
    }
    double totalSentence = RegEx.countPattern(pattern, text);

    /* calculating the content for each level */
    for (StyleHead head: headList){
        int headLevel = head.getStyleLevel();
        String text2 = "";

        /*  for each body text with the same level as the head, computes the total text */
        for (StyleBody body: bodyList){
            int bodyLevel = body.getStyleLevel();
            if (headLevel == bodyLevel){
                text2 += body.getText();
            }
        }
        /* calculating the percentage */
       double noOfSentence = RegEx.countPattern(pattern, text2);
       double percentage = (noOfSentence /totalSentence)*100;
       head.setMultiSentence(percentage);
    }
}
/**  computes the percentage of text in each level  */
private void computeContent(){
    String text = "";

    /*  finding the total size*/
    for (StyleBody body: bodyList){
        text += body.getText();
    }
    double totalSize = text.length();
    int totalLevels = headList.size();

    /* calculating the content for each level */
    for (StyleHead head: headList){
        int headLevel = head.getStyleLevel();
        String txt = "";

        /*  for each level, we need to get text from the body */
        for (StyleBody body: bodyList){
            int bodyLevel = body.getStyleLevel();

            /*  finds the body of the same level as the head */
            if (headLevel == bodyLevel){
                txt += body.getText();
            }
        }
       double size = txt.length();
       double content = (size /totalSize)*100;
       head.setContent(content);
    }
}
/**  computes  distance of the levels from the standard paragraph font size  */
/**  computes the percentage of obligatory words in each level  */
private void computeObl(){
    /*  finding the total size*/
    String text = "";

    /*  calculating the whole content */
    for (StyleBody body: bodyList){
        text += body.getText();
    }
    /*  defining obligatory words */
    String[] patternArray = {"must","should", "have to","has to","ought","shall",
                            "need","can","may","might","can","could"};
    ArrayList<String> patternList = Converter.arrayToArrayList(patternArray) ;
    ArrayList<String> pList = Converter.lineToArrayList("must,should,have to,has to,ought,shall,need,can,"
            + "may,might,can,could", ",");
    double totalObl = RegEx.countPattern(patternList, text);
//    int totalLevels = headList.size();

    /* calculating the content */
    for (StyleHead head: headList){
        int headLevel = head.getStyleLevel();
        String text2 = "";

        /*  for each level calculate the content*/
        for (StyleBody body: bodyList){
            int bodyLevel = body.getStyleLevel();

            /* find the same body level  and add texts together */
            if (headLevel == bodyLevel){
                text2 += body.getText();
            }
            /*  if it contains obligatory word, set obl to true*/
            if (RegEx.countPattern(patternList, body.getText())>0){
                body.setObl(true);
            }
        }
       /* calculating the percentage */
       double noOfObl = RegEx.countPattern(patternList, text2);
       double percentage = (noOfObl /totalObl)*100;
       head.setOblStatement(percentage);
    }
}
private void computeDistance(){
    /*  font size from teach head is needed to compute the distance */
    for (StyleHead head: headList){
        double size = head.getSize();
        double distance;

        /*  computes the distance in opposite direction to make the figure positive*/
        if (size>standardParaSize) {
            distance = size-standardParaSize;
        } else {
            distance = standardParaSize-size;
        }
        /*  the percentage */
        double percentage = 100- ((distance/standardParaSize)*100);
        head.setDistance(percentage);
    }
}
/**  computes the overall index for paragraph prediction */
private void computePredictionIndex(){
    StyleHead head2 = new StyleHead();
    head2.setPredictionIndex(0);

    /*  prediction index is computed as the average of all the following factors */
    for (StyleHead head: headList) {
        double content = head.getContent();
        double distance = head.getDistance();
        double obl = head.getOblStatement();
        double sentence = head.getMultiSentence();
        double index = (content * weight_content + distance * weight_distance +
                obl * weight_obl + sentence * weight_sentence)/4;
        head.setPredictionIndex(index);

        /*  computes the highest scoring index and sets the level as the "paragraph" */
        if (head.getPredictionIndex()>head2.getPredictionIndex()){
            head.setPredictedStructure("paragraph");
            head2.setPredictedStructure(null);
            head2 = head ;
        }    
    }
}
/**
 *  predicts the documents components other than the "paragraph"
 */
private void predictOtherComponents(){
    ArrayList<String> compList = Converter.arrayToArrayList(components);

    /*  looping through the head is needed to process each level */
    for (StyleHead head: headList){
        int headLevel = head.getStyleLevel();

        /*  make sure, only proceed if the structure  of the head has not been set up yet */
        if (head.getPredictedStructure()== null){

            /*  each component word is checked with each body text */
            for (String comp: compList) {
                boolean found = true;

                /*  each body text is checked whether they start with component words */
                for (StyleBody body: bodyList){
                    int bodyLevel = body.getStyleLevel();

                    /*  find the instanances of the style body with the same level as in the head
                                                            determines the structure of the level based on the first word of the text
                                                            e.g. chapter, section, subsection e.t.c. */
                    if (headLevel == bodyLevel ){
                        String firstWord = RegEx.getFirstNWordsFlat(body.getText(), 1).toLowerCase().trim();

                        /*  indicates that the text is not preceeded by any component words (document structure words) */
                        if (!firstWord.equals(comp)){
                            found = false;
                        }
                    }
                }// for body list
                /*  sets the predicted structure if it finds one of the component words preceeding the body text */
                if (found){
                    head.setPredictedStructure(comp);
                    break;
                }
            }// for comp list
        }// if
    }// for head list
}
private void fillTheRest(){
    ArrayList<String> compList = MyReader.fileToArrayList(IN_LAST_STRUCTURE_FILE);

    /*  for each document component (structure), check if it has been already used . It checks from highest to lowest*/
    for (String comp: compList ){
        boolean alreadyUsed = false;

        /*  check the component with each style head */
        for (StyleHead head: headList){
               if (head.getPredictedStructure() != null &&
                   head.getPredictedStructure().equals(comp)){
                    alreadyUsed = true;
               }
        }
        /*  sets the predicted structure value for each head which has not been filled with the structure value,
                           and  makes sure the component is not already used in one of the levels */
        for (StyleHead head: headList){
                if (head.getPredictedStructure() == null && !alreadyUsed){
                    head.setPredictedStructure(comp);
                    alreadyUsed = true;
                }
        }
    }
}
/**
 * gets the list of style body
 * @return a list of StyleBody
 */
public ArrayList<StyleBody> getBodyList() {
        return bodyList;
    }
/**
   *  sets the list of style body
   * @param  the list of style body
   */
public void setBodyList(ArrayList<StyleBody> bodyList) {
        this.bodyList = bodyList;
    }
/**
    *  gets the list of style head
    * @return a list of style head
    */
public ArrayList<StyleHead> getHeadList() {
        return headList;
    }
/**
 *  sets the list of style head
 * @param a list of style head
 */
public void setHeadList(ArrayList<StyleHead> headList) {
        this.headList = headList;
    }
/**
* testing: in console
* prints out all the elements of the head list
*/
public void printHeadList(){
  Out.prln("printing head list ...");
  DecimalFormat df = new DecimalFormat("0.00");

  /*  */
  for (StyleHead head:headList){
     System.out.println("[Score = " + head.getScore()+" "+
             head.getSize()+" "+
             head.getWeight()+" " +
             head.getStyle()+" Structure = "+
             head.getStructure()+" StyleLevel = level"+
             head.getStyleLevel()+ " Content = " +
             df.format(head.getContent())+ " Distance = " +
             df.format (head.getDistance()) + " Sentence count = "+
             df.format(head.getMultiSentence())+ " OBL count = "+
             df.format(head.getOblStatement())+ " PredictionIndex = "+
             df.format(head.getPredictionIndex())+ " PredictionStructure = "+
             head.getPredictedStructure() );
  }
}
/**
* testing: in console
* prints out all the elements of the body list
*/
public void printBodyList(){
  Out.prln("printing body list ...");

  /*  */
  for (StyleBody body: bodyList){
     System.out.println("<level_0" + body.getStyleLevel()+"> "+
             body.getText());
  }
}
}
