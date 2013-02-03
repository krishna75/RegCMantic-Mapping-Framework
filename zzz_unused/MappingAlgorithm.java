/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import api.ont.OntStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import api.ont.IOntologyFiles;
import net.didion.jwnl.JWNLException;
import api.ont.IOntology;
import api.ont.Jena_Ontology;
import helper.util.Sorter;

import api.wn.Wordnet;

/**
 * Contains different kinds of matching algorithms
 * @author Krish
 */
public class MappingAlgorithm implements IOntologyFiles {
    
    public IOntology ont ;
    public Wordnet wn ;

    public MappingAlgorithm() {
        ont = new Jena_Ontology(path, ontoFile, ontoPrefix);
        wn = new Wordnet();
    }

 /**
    *
    * @param regulation is a name of an individual under the class Regulation in OntoReg
    * @param task is a  name of an individual under the class ValidationTask in OntoReg
    * @param n is the minimum number of words to be matched
    * @return boolean value of matched.
    */
    public boolean minMatched(String regulation, String task, int n) {
       boolean matched = false;
       int n1=0;
       ArrayList<String> annList = ont.listDataPropertyValues(regulation, "hasAnnotation");
       ArrayList<String> keywordList = ont.listDataPropertyValues(task, "hasKeywords");
       ArrayList<String> suKeywordList= getSortedUniqueSynset(keywordList);
       for (String annot: annList){
           //System.out.println(annot + "==================   ");//-----------------------------
           for (String suKeyword: suKeywordList){
               //System.out.println(suKeyword );//-----------------------------
               if (annot.toUpperCase().equals(suKeyword.toUpperCase())){
                   n1++;
                  // JOptionPane.showMessageDialog(null, "n1 = "+n1);//-----------------------------
               }
           }     
       }
      // JOptionPane.showMessageDialog(null, "n1 = "+n1);//-----------------------------
       if (n1>=n) matched = true ;
       return matched;
    }


 /**
    * it is based on grammar  matching algorithm, where it checks if the subject and the action are matched.
    * @param regulation is a name of an individual under the class Regulation in OntoReg
    * @param task is a  name of an individual under the class ValidationTask in OntoReg
    * @param n is the minimum number of words to be matched
    * @return boolean value of matched.
    */
    public boolean detailMatched(String regulation, String task) {
       boolean matched = false;
       int n=0;
       ArrayList<String> topicList = ont.listDataPropertyValues(regulation, "topicAnnotation");
       ArrayList<String> subtopicList = ont.listDataPropertyValues(regulation, "subtopicAnnotation");
       ArrayList<String> subjectList = ont.listDataPropertyValues(regulation, "subjectAnnotation");
       ArrayList<String> actionList = ont.listDataPropertyValues(regulation, "actionAnnotation");
       ArrayList<String> taskAnnotList= ont.listDataPropertyValues(task, "hasAnnotation");
       subjectList.addAll(topicList);
       subjectList.addAll(topicList);
       subjectList = getSortedUniqueSynset(subjectList);

       // checking subjects
       int n1=0;
       for (String subject: subjectList){
           for (String taskAnnot: taskAnnotList){
               if (subject.toUpperCase().equals(taskAnnot.toUpperCase())){
                   n1++;
               }
           }
       }

       // checking actions
       int n2=0;
       for (String action: actionList){
           for (String taskAnnot: taskAnnotList){
               if (action.toUpperCase().equals(taskAnnot.toUpperCase())){
                   n2++;
               }
           }
       }

       // this can be done separately i.e. if (n1>=2 and n2>=1)
       n=n1+n2;
       if (n>=3) matched = true ;
       return matched;
    }

 /**
     * This method will make the unique (not repeated) combination of words and sort them
     * in an ascending order.
     * @param keywords to be sorted and made unique.
     * @return sorted and unique set of words.
     */
     public ArrayList<String> getSortedUniqueSynset(ArrayList<String> keywords) {
        ArrayList<String> words = new ArrayList<String>();
        for (String keyword: keywords){
          
           words.addAll(wn.getAllList(keyword));
        }
        return Sorter.sortUnique(words);
    }

     public void addStatementsToOntology(ArrayList<OntStatement> statements){
         for (OntStatement stmt: statements){
             addStatementToOntology(stmt.getSubject(), stmt.getProperty(), stmt.getObject());
         }   
     }

     public void addStatementToOntology(String subject, String predicate, String object){
        ont.addObjectStatement(subject, predicate, object);
     }

     public ArrayList<String> getIndsOfTask(){
         return ont.getIndsOfTask();
     }

     public ArrayList<String> getIndsOfRegulation(){
         return ont.getIndsOfRegulation();
     }

     public void saveOntology(){
         ont.saveOntology();
     }

   //============================== Utilities ===================================

  

    /**
     *  does as the name suggests
     * @param arr is an Array
     * @return ArrayList
     */
     public static ArrayList<String> arrayToArrayList(Object[] arr){
            ArrayList<String> al = new ArrayList<String>();
            for (int i=0;i<arr.length;i++ ){
                al.add(arr[i].toString());
            }
            return al;
    }

}
