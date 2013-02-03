/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zzz_unused;

import gate.util.GateException;
import gate.util.Out;
import java.io.IOException;
import java.util.ArrayList;
import zzz_unused.zzz_RegulationStatement_unused;
import zzz_unused.zzz_MyGate_unused;
import api.ont.IOntology;
import api.ont.IOntology;
import api.ont.IOntologyFiles;
import api.ont.Jena_Ontology;
import api.ont.Jena_Ontology;

/**
 * updates the RegCMantic ontology with regulation from the text via GATE
 * 
 * @author Krishna Sapkota on 12-Aug-2010 at 09:27:03
 */
public class SemReg_V0_Populator implements IOntologyFiles{
    IOntology ont;
    zzz_MyGate_unused gate;
    ArrayList<zzz_RegulationStatement_unused> stmtList;
   // String regulationName = "Eudralex";
    
    /**
     *
     * @throws gate.util.GateException
     * @throws java.io.IOException
     */
    public SemReg_V0_Populator() throws GateException, IOException {
        gate = new zzz_MyGate_unused();
        stmtList = gate.getRegulationList();
        ont = new Jena_Ontology(path,semFile, semPrefix);
    }

    /**
     * updates the RegCMantic ontology with regulation from the text via GATE
     */
    public void populate(){

        Out.println("\nPopulating ontology ..........");
        // for each regulation statement
        for (zzz_RegulationStatement_unused stmt : stmtList){
            String regID = stmt.getRegID();
            String regInd = stmt.getRegBody() + "_" + regID;

            // creates a resource (individual) under the class Regulation
            ont.createInd("Regulation", regInd);
            
            // fills the datatype property regID with regID
            ont.addDataTypeStatement(regInd, "regID", regID);

            // fill object property hasRegSubject with values from subjectList
            // for each subjectList
            for (String subject: stmt.getSubjectList()){

                // creates a resource (individual) under the class RegSubject
                ont.createInd("RegSubject", subject);

                // fills the object property hasRegSubject with the subject
                ont.addObjectStatement(regInd, "hasRegSubject", subject);
            }

            // fill object property hasObligation with values from actionList
            // for each actionList
            for (String action: stmt.getActionList()){

                // creates a resource (individual) under the class RegAction
                ont.createInd( "RegAction", action);

                // fills the object property hasObligation with the action
                ont.addObjectStatement(regInd, "hasObligation", action);
            }
        }// for each regulation
        Out.println("........ ontology population completed");
        Out.prln("*** ONTOREG ONTOLOGY COMPLETED *** ");
    }


}
