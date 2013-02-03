/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase2;

import api.ont.JenaAbstractOntology;
import helper.util.*;
import java.util.ArrayList;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota,  May 24, 2012,  4:37:00 PM
 * A PhD project at Oxford Brookes University
 */
public class _0_OntoGazCreator extends JenaAbstractOntology {
    
    /* IO FILES*/
    String  PATH = Settings.GAZETTEER_PATH + "stop_list/";
    String  IN_ONTOREG_STOPWORDS_FILE = PATH + "ontoreg_stopwords.txt";
    String  IN_GENERAL_STOPWORDS_FILE = PATH + "general_stopwords.txt";
    String  OUT_ONTO_CONCEPT_LIST_FILE = Settings.GAZETTEER_PATH + "gazetteer_ont/" + "ontology_concept.lst";
    
    /* local variables*/
    ArrayList<String> wordList = new ArrayList<String>();
    
    public _0_OntoGazCreator() {
        process();
        write();
    }

    private void process(){
        
        // creat list
        createList();
        
        // split concets (split by comma, split 123)
        wordList = splitConcepts(wordList);
        
        //  remove unwanted (debris and stop words, trim)
        wordList = removeUnwanted(wordList);
        
        // refine (sort, unique)
        wordList = refine(wordList);
        
        Print.prlnArrayList(wordList);
    }
    
    private void createList(){
      ArrayList<String> clsList =   ontoReg.listSubClasses("PhysicalConcepts");
      wordList.addAll(clsList);
      for (String cls: clsList){
          String clsLabel = ontoReg.getLabel(cls);
          wordList.add(clsLabel);
      }
    }
    
    private ArrayList<String>  splitConcepts(ArrayList<String> givenList){
        ArrayList<String> newList = new ArrayList<String>();
        //   split comma
        for (String word: givenList){
            newList.addAll(RegEx.getTokenizedList(word, ","));
        }
        givenList = newList;
        newList = new ArrayList<String>();
        
        // split camel case
        for (String word: givenList){
            newList.add(RegEx.splitCamelcase(word).toLowerCase());
        }
        givenList = newList;
        newList = new ArrayList<String>();
        
        // split underscore
        for (String word: givenList){
            newList.add(RegEx.getTokenizedFlat(word, "_"));
        }
        givenList = newList;
        newList = new ArrayList<String>();
        
        // split 321
        for (String word: givenList){
            newList.addAll(split321(word));
        }
        return newList;
       
    }

    private ArrayList<String> removeUnwanted(ArrayList<String> givenList){
        ArrayList<String> newList = new ArrayList<String>();
        // remove debries
        newList  = removeDebries(givenList);
        
        // remove stop words
        newList = removeStopwords(newList);
        return newList;
    }
    
    private ArrayList<String> removeDebries(ArrayList<String> givenList){
        ArrayList newList = new ArrayList<String>();
        // remove "_", camel case , trim
        for (String word: givenList){
            boolean add_ok = true;
            // remove  concepts with "_" , ","
            if ( word.contains("_") ||word.contains(",") ){
                add_ok = false;
            }
            
            // revmove the concepts with camel case
            int count = 0;
            for (int i= 0; i< word.length(); i++){
                if (Character.isUpperCase(word.charAt(i))){
                    count ++;
                }  
            }
            if (count >1){
                add_ok = false;
            }
            
            // lower case, trim and add
            if ( add_ok){
                word = word.toLowerCase().trim();
                newList.add(word);
            }
        }
        return newList;    
    }
    
    private ArrayList<String>  removeStopwords(ArrayList<String> givenList){
        ArrayList<String> newList = new ArrayList<String>();
        ArrayList<String> stopList = readStopList();
        for (String word: givenList){          
            if ( !RegEx.isInList(word, stopList)){
                newList.add(word);
            }
        }
        return newList;
    }
    
    private ArrayList<String> readStopList(){
        ArrayList<String> newList = new ArrayList<String>();
        newList.addAll(MyReader.fileToArrayList(IN_ONTOREG_STOPWORDS_FILE));
        newList.addAll(MyReader.fileToArrayList(IN_GENERAL_STOPWORDS_FILE));

        // split
        newList = splitConcepts(newList);

        // remove debries
        newList = removeDebries(newList);

        // refine
        newList = refine(newList);
        return newList;
    }
    
    private ArrayList<String> refine(ArrayList<String> givenList){
        ArrayList<String> newList = new ArrayList<String>();
        newList = Sorter.sortUnique(givenList);
        //        Print.prlnArrayList(newList);
        return newList;

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
    
    private ArrayList<String> trim(ArrayList<String> givenList){
        ArrayList<String> newList = new ArrayList<String>();
        for (String b: givenList){
            b = b.trim();
            newList.add(b);        
        }
        return newList;
    }
    
    private void write(){
        MyWriter.write(wordList, OUT_ONTO_CONCEPT_LIST_FILE);
    }
    
}
