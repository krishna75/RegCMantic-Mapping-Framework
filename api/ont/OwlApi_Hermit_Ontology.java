/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.ont;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.UnloadableImportException;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Configuration.TableauMonitorType;

import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import org.semanticweb.owlapi.util.DefaultPrefixManager;


/**
 *
 * @author Krishna Sapkota on 07-Jul-2010 at 23:46:15
 */
public class OwlApi_Hermit_Ontology {
  
    //for Hermit
    public OWLReasoner reasoner;

    public OWLOntologyManager manager;
    public OWLOntology ont;
    public OWLDataFactory dataFactory;
    public PrefixManager pm;

    private static String nsURI;
    private static String pathName;
    private static String fileName;

  
/**
 * constructor with out parameters
 */
    public OwlApi_Hermit_Ontology(String pathName, String fileName, String nsURI) {
        connect(pathName, fileName);
      //  dataFactory = manager.getOWLDataFactory();
        pm = new DefaultPrefixManager(nsURI);
        this.fileName = fileName;
        this.pathName = pathName;
        this.nsURI = nsURI;
    }


/**
 * constructor with all the parameters
 * @param pathName of the ontology
 * @param fileName of the ontology
 */
     public OwlApi_Hermit_Ontology( String pathName, String fileName){
        connect(pathName,fileName);
        pm = new DefaultPrefixManager(nsURI);
    }

    public String getNSURI() {     
        return nsURI;
    }

    public void connect(String pathName, String fileName) {
        try {

            //================ loads ontology file ==============================
            File file = new File(pathName+fileName);
            manager = OWLManager.createOWLOntologyManager();        
            ont = manager.loadOntologyFromOntologyDocument(file);
            System.out.println("Loaded ontology: " + ont); 

           //===================== loads HermIT Reasoner =====================
           OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();

            // Create a console progress monitor. This will print the reasoner progress out to the console.
            ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
            // Specify the progress monitor via a configuration. We could also specify other setup parameters in
            // the configuration, and different reasoners may accept their own defined parameters this way.
            OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
            // Create a reasoner that will reason over our ontology and its imports closure. Pass in the configuration.
            reasoner = reasonerFactory.createReasoner(ont, config);
           
            // loading HermiT reasoner
            //reasoner = new Reasoner(ont);
            dataFactory = manager.getOWLDataFactory();
           //---------------------- X ----------------------------------------
        


        }catch (OWLOntologyCreationIOException e) {
            // IOExceptions during loading get wrapped in an OWLOntologyCreationIOException
            IOException ioException = e.getCause();
            if(ioException instanceof FileNotFoundException) {
                System.out.println("Could not load ontology. File not found: " + ioException.getMessage());
            }
            else if(ioException instanceof UnknownHostException) {
                System.out.println("Could not load ontology. Unknown host: " + ioException.getMessage());
            }
            else {
                System.out.println("Could not load ontology: " + ioException.getClass().getSimpleName() + " " + ioException.getMessage());
            }
        }catch (UnparsableOntologyException e) {
            // If there was a problem loading an ontology because there are syntax errors in the document (file) that
            // represents the ontology then an UnparsableOntologyException is thrown
            System.out.println("Could not parse the ontology: " + e.getMessage());
            // A map of errors can be obtained from the exception
            Map<OWLParser, OWLParserException> exceptions = e.getExceptions();
            // The map describes which parsers were tried and what the errors were
            for(OWLParser parser : exceptions.keySet()) {
                System.out.println("Tried to parse the ontology with the " + parser.getClass().getSimpleName() + " parser");
                System.out.println("Failed because: " + exceptions.get(parser).getMessage());
            }
        }catch (UnloadableImportException e) {
            // If our ontology contains imports and one or more of the imports could not be loaded then an
            // UnloadableImportException will be thrown (depending on the missing imports handling policy)
            System.out.println("Could not load import: " + e.getImportsDeclaration());
            // The reason for this is specified and an OWLOntologyCreationException
            OWLOntologyCreationException cause = e.getOntologyCreationException();
            System.out.println("Reason: " + cause.getMessage());
        }catch (OWLOntologyCreationException e) {
            System.out.println("Could not load ontology: " + e.getMessage());
        }
 	}



    public ArrayList<String> getIndsOfRegulation() {
       return getIndividualNames("Regulation");
    }

    public ArrayList<String> getIndsOfTask() {
       return getIndividualNames("ValidationTask");
    }

    public ArrayList<String> getPropertyValuesString(String subName, String propName) {
        ArrayList<String> al = new ArrayList<String>();
        OWLNamedIndividual ind =    dataFactory.getOWLNamedIndividual(IRI.create(nsURI+subName));
        OWLDataProperty p = dataFactory.getOWLDataProperty(IRI.create(nsURI+propName));
        Set<OWLLiteral> values =  ind.getDataPropertyValues(p , ont);
        for (OWLLiteral value : values){
            al.add(value.getLiteral());
        }
        return al;
    }

    public ArrayList<String> getIndividualNames(String className) {
        ArrayList<String> al = new ArrayList<String>();
       OWLClass cls = dataFactory.getOWLClass(IRI.create(nsURI+className));
       
      // ============ For HermiT reasoner =========================================
      // It takes more than 4 minutes to process
        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(cls, false);
        // The reasoner returns a NodeSet again. This time the NodeSet contains individuals.
        // Again, we just want the individuals, so get a flattened set.
        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
        for(OWLNamedIndividual ind : individuals) {
            al.add(IRI.create(ind.toStringID()).getFragment());
        }
      //--------------------- X --------------------------------------------------
        return al;

    }

    public void saveOntology() {
        try {
            manager.saveOntology(ont);
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(OwlApi_Pellet_Ontology.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addObjectStatement(String subName, String propName, String objName) {
        OWLNamedIndividual individual = dataFactory.getOWLNamedIndividual(IRI.create(nsURI+subName));
        OWLNamedIndividual object = dataFactory.getOWLNamedIndividual(IRI.create(nsURI+objName));
        OWLObjectPropertyExpression property = dataFactory.getOWLObjectProperty(IRI.create(nsURI+propName));

        OWLObjectPropertyAssertionAxiom assertionAxiom  = dataFactory.getOWLObjectPropertyAssertionAxiom(property, individual, object);
        manager.addAxiom(ont, assertionAxiom);
        saveOntology();
    }

    public void createInd(String indName, String className) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addDataTypeStatement(String subName, String propName, String dataTypeValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<String> listClasses() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addComment(String subName, String comment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<String> listSubClasses(String className) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<String> listIndsOfAClass(String className) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getComment(String subName, String comment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<String> listComments(String resName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
