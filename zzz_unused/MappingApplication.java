/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import gate.util.Out;
import api.ont.OntStatement;
import java.util.ArrayList;

/**
 * This class tests the MappingApplication of algorithm and performs various tasks like filling
 * the values of necessitatesTaskCandidates of regulations with suggested tasks.
 * @author Krish
 */
public class MappingApplication {

    ArrayList<OntStatement> stmtList = new ArrayList<OntStatement>();
    public MappingAlgorithm alg = new MappingAlgorithm();
    public MappingApplication() {

    }
   /**
    * This method  takes a regulation and tests with tasks via annotation and
    * keywords matching, if it finds matching, it inserts the value of neccitatesTaskCandiates
    * @param regulation which requires matching with the tasks.
    */
   public void fillMatched(String regulation) {
       ArrayList<String> tasks = alg.getIndsOfTask();
       for (String task: tasks){
           if (alg.minMatched(regulation, task, 2)){
               System.out.println("task : "+ task + " is  matched");
               stmtList.add(new OntStatement(regulation, "necessitatesTaskCandidates", task));
           }
       }
       alg.addStatementsToOntology(stmtList);
       Out.prln("*** MAPPING APPLICATION COMPLETED *** ");

   }
   
   public void fillAllMatched() {
       ArrayList<String> regList = alg.getIndsOfRegulation();
       for (String reg: regList){
           ArrayList<String> taskList = alg.getIndsOfTask();
           for (String task: taskList){
               if (alg.detailMatched(reg, task)){
                   System.out.println("regulation: "+reg +"  and task : "+ task + " is  matched");
                   stmtList.add(new OntStatement(reg, "necessitatesTaskCandidates", task));
               }
           alg.addStatementsToOntology(stmtList);
            }
       }// for each reg
       alg.saveOntology();
       System.out.println("..... ontology update completed !!!");
       Out.prln("*** MAPPING APPLICATION COMPLETED *** ");
   }




}
