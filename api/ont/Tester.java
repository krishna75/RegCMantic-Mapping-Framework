
package api.ont;

import helper.util.Print;
import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota, 13-Dec-2011,   09:20:38
 * A PhD project at Oxford Brookes University
 */
public class Tester extends JenaAbstractOntology {

    public Tester(){
//        semReg = new OwlApi_Pellet_Ontology(path, semFile, semPrefix);
//        testAll();
//        testDisjuctClasses();
        testOntologicalConcepts();
    }
    private void testDisjuctClasses(){
       Print.prArrayList( ontoReg.listDisjointClasses("Substance"));
//       Print.prArrayList( ontoReg.listDisjointClasses("EquipmentModule"));
//       Print.prArrayList( ontoReg.listDisjointClasses("Cleaning"));
    }

    private void testOntologicalConcepts(){
        Print.prln(new OntologicalConcept("T101CleanlinessTestTask", ontoReg).toString());
        Print.prln(new OntologicalConcept("PharmaSupplierAssess_1", ontoReg).toString());
        
        Print.prln(new OntologicalConcept("InvestigationTask_1", ontoReg).toString());        
    }
    private void testAll(){
        String text = "";
        ArrayList<String> aList = new ArrayList<String>();
//        Print.prln(" consistant = " + semReg.isConsistent());
      
//        Print.prArrayList( semReg.listClasses("clean"));
//        Print.prln( semReg.getClass("Eudralex_5.10_1"));
//        Print.prln( semReg.getSuperClass("Regulation"));
//         Print.prln( semReg.getSuperClass("Regulation"));
        aList = semReg.listDisjointClasses("Substance");
//        Print.prArrayList( semReg.listSubClasses("Medium"));
//        Print.prArrayList( semReg.listComments("Regulation"));
//        Print.prln( semReg.getComment("Eudralex_5.10_1"));
//        semReg.addObjectStatement("Samples", "isSubjectOf", "eudralex_5.29_3");
//        semReg.addDataTypeStatement("Attention", "similarWords", "pay_attention");              
//        Print.prArrayList(semReg.getDataPropertyValuesString("Attention", "similarWords"));       
//        text = semReg.getDataPropertyValue("Attention", "similarWords");
//        Print.prArrayList(semReg.getObjectPropertyValuesString("Eudralex_5.18", "hasStatement"));
//        semReg.createInd("Subject", "Apple");
//        aList = semReg.listComments("Attention");       
//      text = semReg.getComment("Attention");
//                 aList = semReg.listLabels("Attention");
//              text = semReg.getLabel("Attention");
//        semReg.addComment("Containers", "Containers are ...");
//        semReg.addLabel("Containers", "Kribin knows about it.");
//        aList = semReg.listClasses("clean");
//        aList = ontoReg.listClasses("Tank102Procedure");
//        text = "card "+ontoReg.getCardinality("ValidationTask", "isResponsibilityOf");
//        aList = ontoReg.listRelatedProperties("ValidationTask", false);
//        aList = ontoReg.listIndividuals("ValidationTask");
//        aList = semReg.listRelatedProperties("Statement");
//      text = String.valueOf(ontoReg.isIndOfClass("T101CleaningTask", "ValidationTask"));
        
        
//        
//        semReg.saveOntology();
//       printing
        Print.prln("Printing...");
        Print.prln("text = "+ text);
        Print.prArrayList(aList);
//        Print.prArrayList(ontoReg.getObjectPropertyValuesString( "TimeTank102Procedure", "isImmediatelyBefore"));

    }
}
