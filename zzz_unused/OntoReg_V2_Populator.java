package zzz_unused;

import gate.util.Out;
import java.util.ArrayList;
import api.ont.IOntology;
import api.ont.IOntology;
import api.ont.IOntologyFiles;
import api.ont.Jena_Ontology;
import api.ont.Jena_Ontology;
import helper.util.DOS;

/**
 * It transfers the regulations from SemReg ontology to OntoReg ontology
 * @author Krish
 */
public class OntoReg_V2_Populator implements IOntologyFiles {
    private IOntology semReg;
    private IOntology ontoReg;
    private String regBody = "Eudralex";


    public OntoReg_V2_Populator() {
        DOS.makeOntoRegEmpty();
        ontoReg = new Jena_Ontology(path, ontoFile, ontoPrefix);
        semReg = new Jena_Ontology(path, semFile, semPrefix);
        populate();
        saveOntology();
        Out.prln("*** ONTOREG ONTOLOGY POPULATION COMPLETED *** ");
        DOS.openOntoReg();
    }

    private void populate(){
        ArrayList<String> regulationList = semReg.listIndividuals("Regulation");
        for (String regulation: regulationList){

            // 1. IND:  creates individual of class Regulation
            System.out.print(regulation);
            ontoReg.createInd("Regulation", regulation);

            // 2. DATATYPE regulationNumber: fills the value of a datatype property regulationNumber
            String regID = semReg.listDataPropertyValues(regulation, "id").get(0);
            System.out.println("    "+regID);
            ontoReg.addDataTypeStatement(regulation, "regulationNumber", regID);

            // 3. COMMENT : fills the value of comment in OntoReg from description in SemReg
            String comment = semReg.listDataPropertyValues(regulation, "description").get(0);
            System.out.println("    "+comment);
            ontoReg.addComment(regulation, comment);
           
        }// for each regulation        
    }

/**
 *  Saves the populated ontology
 */
private void saveOntology(){
    Out.println("saving ontology ........");
    ontoReg.saveOntology();
    Out.println("........... saving ontology completed.");
}



}
