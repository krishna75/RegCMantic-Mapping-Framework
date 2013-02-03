
package api.ont;

import java.util.ArrayList;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.Print;
import helper.util.RegEx;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota, 25-Nov-2011,   18:12:53
 * A PhD project at Oxford Brookes University
 */
public class OntologicalConcept {
    
    String ONTOLOGY_STOPWORD_FILE =  Settings.STOPWORDS_GENERAL;
    String GENERAL_STOPWORD_FILE = Settings.STOPWORDS_ONTOREG;
    
    private String individualName ;
    private String directConcept;
    private ArrayList<String> directConceptLabels ;
    private ArrayList<String> splittedConcepts;
    private ArrayList<String> higherConceptLabels;
    private ArrayList<String> lowerConceptLabels;
    private ArrayList<String> allConcepts;
    private IOntology ont;
    private ArrayList<String> stopWordList;

    public OntologicalConcept(String individualName, IOntology ont) {
        /* field updates */
        this.individualName = individualName;
        this.ont = ont;
        /* local processes*/
        init();
        process();
        refine();
    }

    private void init(){
        Print.prln("OntologicalConcepts initialised...");
        directConcept = "";
        directConceptLabels = new ArrayList<String>();
        splittedConcepts= new ArrayList<String>();
        higherConceptLabels  = new ArrayList<String>();
        lowerConceptLabels   = new ArrayList<String>();
        allConcepts     = new ArrayList<String>();
        stopWordList    = MyReader.fileToArrayList(ONTOLOGY_STOPWORD_FILE);
        stopWordList.addAll(MyReader.fileToArrayList(GENERAL_STOPWORD_FILE));
    }

    private void process(){
        /*  populates the varibales */
        if (individualName != null & !individualName.equals("") ){
            directConcept   = ont.getClass(individualName);
            directConceptLabels = processClass(directConcept);
            //lowerConcepts   = ont.listSubClasses(directConcept);
            higherConceptLabels  = processSuperClasses(directConcept);
        }
        splittedConcepts = splitConcepts(individualName);

        /*  adding all to the all list */
        //allConcepts.add(individualName);
        //allConcepts.add(directConcept);
        allConcepts.addAll(splittedConcepts);
        allConcepts.addAll(directConceptLabels);
        //allConcepts.addAll(lowerConceptLabels);
        allConcepts.addAll(higherConceptLabels); 
        //allConcepts.addAll(listLabel(allConcepts));
        
    }

    private void refine(){
        /* making the words unique  */
       // makeUnique();
        /*  removing unwated words from all the lists*/
//        remove();
        /* split  the words in all the lists  and make unique and remove stopwords again*/
        // note:  add splitting to all the upper and lower concepts in future
        allConcepts = splitConcepts(allConcepts);
        remove();
        makeUnique();
    }

    private void makeUnique(){
        splittedConcepts =  Converter.makeUnique(splittedConcepts);
        lowerConceptLabels   =   Converter.makeUnique(lowerConceptLabels);
        higherConceptLabels  =   Converter.makeUnique(higherConceptLabels);
        allConcepts     =   Converter.makeUnique(allConcepts);
    }
    private void remove(){
        splittedConcepts =  Converter.remove(splittedConcepts, stopWordList);
        higherConceptLabels  =   Converter.remove(higherConceptLabels, stopWordList);
        lowerConceptLabels   =   Converter.remove(lowerConceptLabels, stopWordList);
        allConcepts     =   Converter.remove(allConcepts, stopWordList);
    }
    private ArrayList<String> splitConcepts(ArrayList<String> aList){
        ArrayList<String> newList = new ArrayList<String>();
        for(String a: aList){
            newList.addAll(splitConcepts(a));
        }
        return newList;
    }
    private ArrayList<String> splitConcepts(String concatenatedString){
        ArrayList<String> newList = new ArrayList<String>();
        newList = splitName(concatenatedString);
        return newList;
    }

    private ArrayList<String> processClass(String clsName){
        ArrayList<String> newList = new ArrayList<String>();
        newList.addAll(ont.listLabels(clsName));
        return newList;
    }
    
    private ArrayList<String> processSuperClasses(String clsName){
        ArrayList<String> newList = new ArrayList<String>();
        ArrayList<String> superList = ont.listSuperClasses(clsName);
        for (String superCls: superList){
            newList.addAll(processClass(superCls));
        }
        return newList;
    }
    
    
    public ArrayList<String> getAllConcepts() {
        return allConcepts;
    }

    public void setAllConcepts(ArrayList<String> allConcepts) {
        this.allConcepts = allConcepts;
    }

    public String getDirectConcept() {
        return directConcept;
    }

    public void setDirectConcept(String directConcept) {
        this.directConcept = directConcept;
    }

    public ArrayList<String> getHigherConcepts() {
        return higherConceptLabels;
    }

    public void setHigherConcepts(ArrayList<String> higherConcepts) {
        this.higherConceptLabels = higherConcepts;
    }

    public String getIndividualName() {
        return individualName;
    }

    public void setIndividualName(String individualName) {
        this.individualName = individualName;
    }

    public ArrayList<String> getLowerConcepts() {
        return lowerConceptLabels;
    }

    public void setLowerConcepts(ArrayList<String> lowerConcepts) {
        this.lowerConceptLabels = lowerConcepts;
    }

    public ArrayList<String> getSplittedConcepts() {
        return splittedConcepts;
    }

    public void setSplittedConcepts(ArrayList<String> splittedConcepts) {
        this.splittedConcepts = splittedConcepts;
    }
/**
 * It splits the concatenated names with underscore and camel case. It also removes numbers and
 * other character fro the name. And returns the split names in an array list.
 * @param complexName is the string to be split
 * @return a list of split names
 */
      private ArrayList<String> splitName(String complexName){
        ArrayList<String> newList = new ArrayList<String>();
//        Print.prln("complex name = "+complexName);
        complexName = RegEx.removeOtherCharacters(complexName);
        complexName = RegEx.splitCamelcase(complexName);
        newList.addAll(RegEx.getTokenizedList(complexName, " "));
        return newList;
    }

      private ArrayList<String> listLabel(ArrayList<String> resList){
          ArrayList<String> newList = new ArrayList<String>();
          for (String res: resList){
              newList.add(getLabel(res));
          }
          return newList;
      }
      private String getLabel(String res){
          return ont.getLabel(res);
      }
      
      

    @Override
    public String toString(){
        String resultWord = "";
        resultWord = "INDIVIDUAL NAME = "+ individualName +"; DIRECT CONCEPT = " + directConcept +
                "; SPLITTED CONCEPTS = "+ Converter.listToString(splittedConcepts, ",") +
                "; HIGHER CONCEPTS = "+ Converter.listToString(higherConceptLabels, ",") +
                "; LOWER CONCEPTS = "+ Converter.listToString(lowerConceptLabels, ",")+
                "; ALL CONCEPTS = " + Converter.listToString(allConcepts, ",") ;
        return resultWord;
    }
}
