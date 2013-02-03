/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import api.ont.IOntologyFiles;
import gate.util.Out;
import helper.util.Splitter;
import java.util.ArrayList;
import net.didion.jwnl.JWNLException;
import api.ont.IOntology;
import api.ont.Jena_Ontology;

/**
 *
 * @author Krish
 */
public class TaskAnnotator implements IOntologyFiles {
    IOntology ontoReg;

    public TaskAnnotator(){
        ontoReg = new Jena_Ontology(path, ontoFile, ontoPrefix);
        annotate();
        Out.prln("*** TASK ANNOTATION COMPLETED *** ");
    }

    private void annotate()  {
        ArrayList<String> taskList = ontoReg.listIndividuals("ValidationTask");
        System.out.println("size = " + taskList.size());
        for (String task : taskList){
            String patient  = ontoReg.listDataPropertyValues(task, "hasPatient").get(0);
            System.out.println(" patient = "+ patient);

            ArrayList<String> annotList = new ArrayList<String>();
            annotList.add(task);

            ArrayList<String> docList = ontoReg.listIndividuals("Document");
            for (String doc : docList){
                if (doc.equals(patient)){
                    annotList.add("document");
                }
            }
            ArrayList<String> equipmentList = ontoReg.listIndividuals("Equipment");
            for (String equipment : equipmentList){
                if (equipment.equals(patient)){
                    annotList.add("Equipment");
                }
            }
            ArrayList<String> opList = ontoReg.listIndividuals("Operation");
            for (String op : opList){
                if (op.equals(patient)){
                    annotList.add("Operation");
                }
            }

            ArrayList<String> subsList = ontoReg.listIndividuals("Substance");
            for (String subs : subsList){
                if (subs.equals(patient)){
                    annotList.add("Substance");
                }
            }

            ArrayList<String> posList = ontoReg.listIndividuals("ThirdParty");
            for (String pos : posList){
                 if (pos.equals(patient)){
                    annotList.add("Position");
                }
            }

            Splitter splitter = new Splitter();
            annotList = splitter.getSplittedList(annotList);
 
            for (String annot: annotList){
                ontoReg.addDataTypeStatement(task, "hasAnnotation", annot);
            }
        }// for each task

            System.out.println("updating ontology ....");
            ontoReg.saveOntology();
            System.out.println("... completed.");
    }

    

}
