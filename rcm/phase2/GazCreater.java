/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase2;

import api.wn.ISimilarity;
import api.wn.LinSimilarity;
import api.wn.Thesaurus;
import api.wn.Wordnet;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.MyWriter;
import helper.util.Print;
import helper.util.RegEx;
import helper.util.Sorter;
import java.util.ArrayList;
import rcm.main.Settings;

/**
 * it creates gazetteers for extracted terms, definition terms and stop words.
 * @author Krishna Sapkota,  May 30, 2012,  10:01:20 PM
 * A PhD project at Oxford Brookes University
 */
public class GazCreater {
    
    /* IO FILES */
    String IN_EXTRACTED_TERM_FILE = Settings.PHASE2+"extracted_terms.txt";
    String IN_DEFINITION_TERM_FILE = Settings.PHASE2+"definition_terms.txt";
    String IN_ONTOREG_STOPWORDS_FILE = Settings.GAZETTEER_PATH+"stop_list/general_stopwords.txt";;
    String IN_GENERAL_STOPWORDS_FILE = Settings.GAZETTEER_PATH+"stop_list/ontoreg_stopwords.txt";;
    String OUT_TERM_FILE=   Settings.PHASE2+"new_terms.txt";
    
    /* adaptors */
    Wordnet wn = new Wordnet();
    ISimilarity sm = new LinSimilarity(); // the word net was not working  so I used similarity, which also used built in wordnet   
    
    /* local variables */
    ArrayList<String> termList = new ArrayList<String>();
    ArrayList<String> stopList = new ArrayList<String>();
    ArrayList<String> termFileList = new ArrayList<String>();
    ArrayList<String> stopFileList = new ArrayList<String>();
     

    public GazCreater() {
        test();
    }
    
    private void test(){
        
        String[] termfiles = {IN_EXTRACTED_TERM_FILE, IN_DEFINITION_TERM_FILE};
        String[] stopfiles = {IN_ONTOREG_STOPWORDS_FILE, IN_GENERAL_STOPWORDS_FILE};
        ArrayList<String> tList = Converter.arrayToArrayList(termfiles);
        ArrayList<String> sList = Converter.arrayToArrayList(stopfiles);
        createGazetteer(tList, sList, OUT_TERM_FILE);
        
    }
    
    private void createGazetteer(ArrayList<String> termFileList,
                                 ArrayList<String> stopFileList, 
                                 String outFile){
        // update the holders
        this.termFileList = termFileList;
        this.stopFileList = stopFileList;
        this.OUT_TERM_FILE = outFile; 
        
        // process
        readFiles();
        split();
        addSynset();
        split();
        removeStopwords();
        sort();
        write();
    }
    
    private void readFiles(){
        for (String termFile: termFileList ){
            termList.addAll(MyReader.fileToArrayList(termFile));
        }
        for (String stopFile: stopFileList){
            stopList.addAll(MyReader.fileToArrayList(stopFile));
        }  
        trim(); 
        sort();
    }
    
    private void split(){
        splitConcepts();
        removeDebries();
    }
    
    private void addSynset(){
       
        ArrayList<String> newList = new ArrayList<String>();
        for (String term: termList){
            // adds the cocepts from word net         
            newList.addAll(wn.getRootList(term));      // root word
            newList.addAll(wn.getSynList(term));   // synonyms
            newList.addAll(wn.getHypoList());   // hyponyms
            
        }
        termList.addAll(newList);
    }  
    
    private void removeStopwords(){
        ArrayList<String> newList = new ArrayList<String>();
        for (String term: termList){        
            if ( !RegEx.isInList(term, stopList)){
                newList.add(term);
            }
        }
        termList = newList;   
    }
    
    private void write(){
        Print.prln("writing the file...");
        MyWriter.write(termList, OUT_TERM_FILE);
    }
  
    
  /*  UTILITIES */  
    
    
   private void  trim(){
        ArrayList<String> newList = new ArrayList<String>();
        for (String term: termList){
            term = term.trim();
            if (RegEx.isValidString(term)){
                newList.add(term);    
            }
        }
        termList = newList;
    }
    
   /**
     *  sorts and makes the list unique
     */
    private void sort(){
        ArrayList<String> newList = new ArrayList<String>();
        newList = Sorter.sortUnique(termList);
        termList = newList;
    }
    
    private void  splitConcepts(){
        ArrayList<String> newList = new ArrayList<String>();
        //   split comma
        for (String word: termList){
            newList.addAll(RegEx.getTokenizedList(word, ","));
        }
        termList = newList;

        // split camel case
        newList = new ArrayList<String>();
        for (String word: termList){
            newList.add(RegEx.splitCamelcase(word));
        }
        termList = newList;
        
        // trimPhrase
         newList = new ArrayList<String>();
        for (String word: termList){
            newList.add(RegEx.trimPhrase(word));
        }
        termList = newList;
        newList = new ArrayList<String>();
        
        // split 321
        for (String word: termList){
            newList.addAll(split321(word));
        }
       termList = newList;
       
    }
    
    private ArrayList<String>  split321(String text){
        ArrayList<String> aList = RegEx.getTokenizedList(text, " ");
        ArrayList<String> newList = new ArrayList<String>();
        String word = "";
        for (int i = 0; i < aList.size(); i++){
             String newWord = aList.get(aList.size()-i-1);
             newWord = RegEx.removeOtherCharacters(newWord);
             newWord = newWord.trim();
             word = newWord +" "+ word;
             word = word.trim();
             newList.add(word);
        } 
        return newList;
    }
    
    private void removeDebries(){
        ArrayList newList = new ArrayList<String>();
        // remove "_", camel case , trim
        for (String term: termList){
            boolean add_ok = true;
            // remove  concepts with "_" , ","
            if ( term.contains("_") ||term.contains(",") ){
                add_ok = false;
            }
            
            // revmove the concepts with camel case
            int count = 0;
            for (int i= 0; i< term.length(); i++){
                if (Character.isUpperCase(term.charAt(i))){
                    count ++;
                }  
            }
            if (count >1){
                add_ok = false;
            }
            
            // lower case, trim and add
            if ( add_ok){
                term = term.toLowerCase().trim();
                newList.add(term);
            }
        }
       termList = newList;    
    }

    
}
