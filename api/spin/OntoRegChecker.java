/*******************************************************************************
 * Copyright (c) 2009 TopQuadrant, Inc.
 * All rights reserved. 
 *******************************************************************************/
package api.spin;

import api.ont.IOntologyFiles;
import api.ont.Jena_Ontology;
import java.util.List;

import org.topbraid.spin.constraints.ConstraintViolation;
import org.topbraid.spin.constraints.SPINConstraints;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.system.SPINLabels;
import org.topbraid.spin.system.SPINModuleRegistry;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.ReificationStyle;


/**
 * Loads the Kennedys SPIN ontology and runs inferences and then
 * constraint checks on it.
 * 
 * @author Holger Knublauch
 */
public class OntoRegChecker implements IOntologyFiles {
    public Jena_Ontology ontoReg;


public OntoRegChecker() {
        init();
        process();

}
private void init(){
    /*  initialise ontologies  */
    ontoReg = new Jena_Ontology("D:/dropbox/krishna/netbeansprojects/mapping_files/ontologies/backup/older/", "OntoReg_Krish.owl", "http://www.owl-ontologies.com/2008/6/17/Ontology1213712768.owl#");
}
private  void process(){
    // Initialize system functions and templates
		SPINModuleRegistry.get().init();

//		// Load main file
//		Model baseModel = ModelFactory.createDefaultModel(ReificationStyle.Minimal);
//		baseModel.read("http://topbraid.org/examples/kennedysSPIN");
		
		// Create OntModel with imports
//		OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, baseModel);
		OntModel ontoModel = ontoReg.getModel();
		// Create and add Model for inferred triples
//		Model newTriples = ModelFactory.createDefaultModel(ReificationStyle.Minimal);
//		ontoModel.addSubModel(newTriples);

		// Register locally defined functions
		SPINModuleRegistry.get().registerAll(ontoModel, null);

		// Run all inferences
//		SPINInferences.run(ontoModel, newTriples, null, null, false, null);
//		System.out.println("Inferred triples: " + newTriples.size());
                System.out.println("total classes = " + ontoModel.listClasses().toList().size());

		// Run all constraints
                System.out.println("Constraint violations:");
		List<ConstraintViolation> cvs = SPINConstraints.check(ontoModel, null);
		for(ConstraintViolation cv : cvs) {
			System.out.println(" - at " + SPINLabels.get().getLabel(cv.getRoot()) + ": " + cv.getMessage());
                        
		}

		// Run constraints on a single instance only
//		Resource person = cvs.get(0).getRoot();
//		List<ConstraintViolation> localCVS = SPINConstraints.check(person, null);
//		System.out.println("Constraint violations for " + SPINLabels.get().getLabel(person) + ": " + localCVS.size());
}
}
