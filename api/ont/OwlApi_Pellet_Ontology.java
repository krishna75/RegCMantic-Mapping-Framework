/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.ont;

//for Pellet 2.2.0
//import com.clarkparsia.pellet.owlapiv3.Reasoner;

import com.clarkparsia.pellet.owlapiv3.Reasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.clarkparsia.pellet.rules.rete.Node;
import helper.util.Converter;
import helper.util.Print;
import helper.util.RegEx;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.OWLEntityRemover;

/**
 *  This classes uses owl api with pellet reasoner. The basic methods to get the classes, individuals and properties are 
 *  simplified to one line.
 * @author Krishna Sapkota on 07-Jul-2010 at 23:46:15
 */
public class OwlApi_Pellet_Ontology implements IOntology {
    // ontology and reasoner related
    private OWLOntologyManager ontManager;
    private PelletReasoner pelletReasoner;
    private OWLOntology ont;
    private OWLDataFactory dataFactory;
    private PrefixManager prefixManager;
    private IRI documentIRI;

    // file and uri related
    private String nsURI;
    private String pathName;
    private String fileName;
  
/**
 *  Its a constructor.
 * @param pathName
 * @param fileName
 * @param nsURI 
 */
    public OwlApi_Pellet_Ontology(String pathName, String fileName, String nsURI) {
        connect(pathName, fileName);
        this.fileName = fileName;
        this.pathName = pathName;
        this.nsURI = nsURI;
    }
    
/**
*  Its a constructor.
* @param pathName of the ontology
* @param fileName of the ontology
*/
     public OwlApi_Pellet_Ontology( String pathName, String fileName){
        connect(pathName,fileName);
    }
     
     
/*==========================                 Connection Related                   ============================== */ 
/**
 *  
 * @return name space URI prefix of the ontology.
 */
    @Override
    public String getNSURI() {
        return nsURI;
    }     
     
/**
 *  It establishes the connection between ontology and java.
 * @param pathName of the ontology
 * @param fileName of the ontology
 */
    @Override
    public void connect(String pathName, String fileName) {
        try {
            //================ loads ontology file ==============================
            File file = new File(pathName+fileName);
            ontManager = OWLManager.createOWLOntologyManager();
            ont = ontManager.loadOntologyFromOntologyDocument(file);
            dataFactory = ontManager.getOWLDataFactory();
            documentIRI = ontManager.getOntologyDocumentIRI(ont);
            
            Print.prln("documentIRI = "+documentIRI.toString());

            // ================ loads Pellet reasoner ==========================
            PelletReasonerFactory reasonerFactory = new PelletReasonerFactory();
            pelletReasoner = reasonerFactory.createReasoner(ont);

        }catch (OWLOntologyCreationIOException e) {
            IOException ioException = e.getCause();
            if(ioException instanceof FileNotFoundException) {
                System.out.println("Could not load ontology. File not found: " + ioException.getMessage());
            }else if(ioException instanceof UnknownHostException) {
                System.out.println("Could not load ontology. Unknown host: " + ioException.getMessage());
            } else {
                System.out.println("Could not load ontology: " + ioException.getClass().getSimpleName() + " " + ioException.getMessage());
            }
        }catch (UnparsableOntologyException e) {
            System.out.println("Could not parse the ontology: " + e.getMessage());
            Map<OWLParser, OWLParserException> exceptions = e.getExceptions();
            for(OWLParser parser : exceptions.keySet()) {
                System.out.println("Tried to parse the ontology with the " + parser.getClass().getSimpleName() + " parser");
                System.out.println("Failed because: " + exceptions.get(parser).getMessage());
            }
        }catch (UnloadableImportException e) {
            System.out.println("Could not load import: " + e.getImportsDeclaration());
            OWLOntologyCreationException cause = e.getOntologyCreationException();
            System.out.println("Reason: " + cause.getMessage());
        }catch (OWLOntologyCreationException e) {
            System.out.println("Could not load ontology: " + e.getMessage());
        }
}    
    
/*===========================                 Ontology/Reasoner  Related          =========================== */  
    
/**
 *  The methods in this class can only work a consistent ontology.
 * @return if the ontology is consistent
 */
    @Override
    public boolean isConsistent() {
       return pelletReasoner.isConsistent();
    }
    
/*===========================                 Classes Related                   ================================ */   
    
/**
 * 
 * @return a list of all the named classes in the ontology.
 */
    @Override
    public ArrayList<String> listClasses() {     
       Set<OWLClass> classSet = ont.getClassesInSignature();
       return getNames(classSet);
    }
    
 /**
     *  It identifies the class which the given individual belongs to.
     * @param indName is the name of the individual to be computed.
     * @return  the class name
     */
    @Override
    public String getClass(String indName) {
        OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(IRI.create(nsURI+indName));
        NodeSet<OWLClass> clsSet =  pelletReasoner.getTypes(ind, true);
        return getNames(clsSet).get(0);
    }
    
 /**
     *  It determines all the classes the given individual belongs to.
     * @param indName is the name of the individual to be computed.
     * @return  a list of all the relevant classes.
     */
    @Override
    public ArrayList<String> listClasses(String indName) {
        OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(IRI.create(nsURI+indName));
        NodeSet<OWLClass> clsSet =  pelletReasoner.getTypes(ind, false);
        return getNames(clsSet);
    }
    
 /**
     *  It determines the direct superclass of the given class.
     * @param className
     * @return the superclass
     */
    @Override
    public String getSuperClass(String className) {
        return listSuperClasses(className).get(0);
    }
 /**
     *  Determines all the super classes the given class belongs to.
     * @param className is the name of the class to be computed.
     * @return  a list of all the relevant super classes.
     */
    @Override
    public ArrayList<String> listSuperClasses(String className) {
        OWLClass cls = dataFactory.getOWLClass((IRI.create(nsURI+className)));
        NodeSet<OWLClass> superClassSet = pelletReasoner.getSuperClasses(cls, true);
        return getNames(superClassSet);
    }
    
 /**
     *  It list all the sub classes of the given class.
     * @param className
     * @return 
     */
    @Override
    public ArrayList<String> listSubClasses(String className) {
        OWLClass cls = dataFactory.getOWLClass((IRI.create(nsURI+className)));
        NodeSet<OWLClass> subclassSet = pelletReasoner.getSubClasses(cls, false);
        return getNames(subclassSet);
    }
    
 /**
     *  It lists all the disjoint classes of the given class.
     * @param className
     * @return 
     */
    @Override
    public ArrayList<String> listDisjointClasses(String className) {
        OWLClass cls = dataFactory.getOWLClass((IRI.create(nsURI+className)));
        NodeSet<OWLClass> disjointSet = pelletReasoner.getDisjointClasses(cls);
        return getNames(disjointSet);
    }    
    
    @Override
    public boolean isIndOfClass(String indName, String className) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
/*==========================                   Individual Related                   ============================== */    
 
    /**
 *  It creates an individual of the given name and associates it with the given class.
 * @param clsName
 * @param indName 
 */
    @Override
    public void createInd(String clsName, String indName) {
        OWLClass cls = dataFactory.getOWLClass((IRI.create(nsURI+clsName)));
        OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(IRI.create(nsURI+indName));
        OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(cls, ind);      
        AddAxiom axiomChange = new AddAxiom(ont, axiom);
	ontManager.applyChange(axiomChange);      
    }
    
/**
 *  It lists all the individuals of the given class.
 * @param className
 * @return 
 */
   @Override
    public ArrayList<String> listIndividuals(String className) {
       OWLClass cls = dataFactory.getOWLClass(IRI.create(nsURI+className));
       NodeSet<OWLNamedIndividual> inds = pelletReasoner.getInstances(cls, true);
       return getNames(inds);
    }
   
/**
 *  It is a short hand for getIndividualNames("Regulation"). Its domain specific, and works only if the ontology has got 
 * a class named "Regulation".
 * @return a list of individuals under a class called "Regulation".
 */
    @Override
    public ArrayList<String> getIndsOfRegulation() {
       return listIndividuals("Regulation");
    }
    
/**
 * It is a short hand for getIndividualNames("ValidationTask"). Its domain specific, and works only if the ontology has got 
 * a class named "ValidationTask".
 * @return a list of individuals under a class called "ValidationTask".
 */
    @Override
    public ArrayList<String> getIndsOfTask() {
       return listIndividuals("ValidationTask");
    }
    
/*==========================                  Properties Related                   ============================== */
    
    @Override
    public String getDataPropertyValue(String subName, String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
/**
 *  Given an individual and its  datatype property, this method determines the property values.
 * @param subName
 * @param propName
 * @return a list of property values.
 */   
    @Override
    public ArrayList<String> listDataPropertyValues(String subName, String propName) {
        OWLNamedIndividual ind =    dataFactory.getOWLNamedIndividual(IRI.create(nsURI+subName));
        OWLDataProperty p = dataFactory.getOWLDataProperty(IRI.create(nsURI+propName)); 
        Set<OWLLiteral> values =  pelletReasoner.getDataPropertyValues(ind, p);
        return getLiteralNames(values);
    }
    
    @Override
    public String getObjectPropertyValue(String subName, String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
/**
 *  Given an individual and its object property, this method determines the property values.
 * @param subName
 * @param propName
 * @return  a list of individuals. 
 */
    @Override
    public ArrayList<String> listObjectPropertyValues(String subName, String propName) {
        OWLNamedIndividual ind =    dataFactory.getOWLNamedIndividual(IRI.create(nsURI+subName));
        OWLObjectPropertyExpression p = dataFactory.getOWLObjectProperty(IRI.create(nsURI+propName)); 
        NodeSet<OWLNamedIndividual> values =  pelletReasoner.getObjectPropertyValues(ind, p);
        return getNames(values);
    }

    @Override
    public int getPropertyValuesCount(String subName, String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public ArrayList<String> listRelatedProperties(String className, boolean direct) {
        OWLClass cls = dataFactory.getOWLClass((IRI.create(nsURI+className)));
        Set<OWLObjectProperty> pSet = cls.getObjectPropertiesInSignature();
        dataFactory.getOWLObjectAllValuesFrom(null, cls).asOWLClass();
        
        
        return getNames(pSet);
    }

/*===========================               Cardinality  Related                   ============================== */  
    
    @Override
    public int getMinCardinality(String clsName, String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMaxCardinality(String clsName, String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCardinality(String clsName, String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSomeValuesFrom(String clsName, String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAllValuesFrom(String clsName, String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getHasValue(String clsName, String propName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
/*===========================                Statement Related                   ============================== */  
    /**
 *  Given two individuals and their connecting relation (i.e. object property), it creates an assertion axiom adds onto 
 *  the ontology. It also determines the inverse property and inserts the inverse axiom in the ontology.
 * @param subName is the individual as a subject.
 * @param propName is the object property.
 * @param objName is the individual as an object.
 */
    @Override
    public void addObjectStatement(String subName, String propName, String objName) {
        // get individual, property , inverse property and object
        OWLNamedIndividual subInd = dataFactory.getOWLNamedIndividual(IRI.create(nsURI+subName));
        OWLNamedIndividual objInd = dataFactory.getOWLNamedIndividual(IRI.create(nsURI+objName));
        OWLObjectPropertyExpression objProperty = dataFactory.getOWLObjectProperty(IRI.create(nsURI+propName));
        OWLObjectPropertyAssertionAxiom axiom  = dataFactory.getOWLObjectPropertyAssertionAxiom(objProperty, subInd, objInd);        
        AddAxiom axiomChange = new AddAxiom(ont, axiom);
	ontManager.applyChange(axiomChange);
        // adding inverse property
        OWLObjectPropertyExpression invProperty = pelletReasoner.getInverseObjectProperties(objProperty).getRepresentativeElement();
        if (invProperty != null){
            OWLObjectPropertyAssertionAxiom invAxiom  = dataFactory.getOWLObjectPropertyAssertionAxiom(invProperty, objInd, subInd);
            AddAxiom invAxiomChange = new AddAxiom(ont, invAxiom);
            ontManager.applyChange(invAxiomChange);
        }
    }
    
/**
 *  Given an individual, its datatype property and the property value, it creates an axiom and adds it to the ontology.
 * @param subName  is the individual as a subject
 * @param propName is the datatype property  
 * @param dataTypeValue  is a literal value of the property
 */
    @Override
    public void addDataTypeStatement(String subName, String propName, String dataTypeValue) {
        OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(IRI.create(nsURI+subName));
        OWLDataProperty dataProperty = dataFactory.getOWLDataProperty(IRI.create(nsURI+propName));
        OWLLiteral  value= dataFactory.getOWLLiteral(dataTypeValue);
        OWLDataPropertyAssertionAxiom axiom = dataFactory.getOWLDataPropertyAssertionAxiom(dataProperty, ind, value);
        AddAxiom addAxiomChange = new AddAxiom(ont, axiom);
	ontManager.applyChange(addAxiomChange);
    }        
    
/*==========================                 Annotation   Related                   ============================== */  
        
        /**
 *  Provided an ontological resource (class or individual), its new comment, it creates a new axiom and adds to the ontology.
 * @param subName is a class or individual
 * @param comment is a new comment to be added
 */
    @Override
    public void addComment(String subName, String comment) {
        OWLAnnotationProperty ap = dataFactory.getRDFSComment(); 
        OWLAnnotationValue value = dataFactory.getOWLLiteral(comment);
        OWLAnnotation annot = dataFactory.getOWLAnnotation(ap, value);  
        OWLAnnotationAssertionAxiom axiom =  dataFactory.getOWLAnnotationAssertionAxiom(IRI.create(nsURI+subName), annot);
        AddAxiom axiomChange = new AddAxiom(ont, axiom);
	ontManager.applyChange(axiomChange);
    }
    
/**
 *  Provided an ontological resource (i.e. a class or an individual), it determines its comments .
 * @param subName is either a class or an individual
 * @return  a flattened list of comments
 */
    @Override
    public String getComment(String subName) {
        return Converter.arrayListToString(listComments(subName));
    }
    
/**
   *   Provided an ontological resource (i.e. a class or an individual), it determines its comments .
    *  Note:  In Jena, class and individual are defined under resource, which makes easier to define comment, but here in owl api, I couldn't find such a structure.
    * @param resName is either a class or an individual
    * @return a list of comments
    */
    @Override
    public ArrayList<String> listComments(String resName) {
        IRI resIRI = IRI.create(nsURI+resName);
        OWLAnnotationProperty ap = dataFactory.getRDFSComment(); 
        Set<OWLAnnotation> annSet = ont.getEntitiesInSignature(resIRI).iterator().next().getAnnotations(ont, ap);
        return getAnnotNames(annSet);
    }
    
/**
 *  Provided an ontological resource (class or individual), its new label, it creates a new axiom and adds to the ontology.
 * @param subName is either a class or an individual
 * @param label  is the new label to be added
 */
    @Override
    public void addLabel(String subName, String label) {
        OWLAnnotationProperty ap = dataFactory.getRDFSLabel(); 
        OWLAnnotationValue value = dataFactory.getOWLLiteral(label);
        OWLAnnotation annot = dataFactory.getOWLAnnotation(ap, value);  
        OWLAnnotationAssertionAxiom axiom =  dataFactory.getOWLAnnotationAssertionAxiom(IRI.create(nsURI+subName), annot);
        AddAxiom axiomChange = new AddAxiom(ont, axiom);
	ontManager.applyChange(axiomChange);
    }

/**
 *  Provided an ontological resource (i.e. a class or an individual), it determines its labels .
 * @param resName - a class or an individual
 * @return  a flattened value of a list of labels
 */    
    @Override
    public String getLabel(String resName) {
        return Converter.arrayListToString(listLabels(resName));
    }
    
/**
 *  Provided an ontological resource (i.e. a class or an individual), it determines its labels .
 * @param resName - a class or an individual
 * @return  a list of labels
 */   
    @Override
    public ArrayList<String> listLabels(String resName) {
        IRI resIRI = IRI.create(nsURI+resName);
        OWLAnnotationProperty ap = dataFactory.getRDFSLabel(); 
        Set<OWLAnnotation> annSet = ont.getEntitiesInSignature(resIRI).iterator().next().getAnnotations(ont, ap);
        return getAnnotNames(annSet);
    }
    
/*==========================                 Ontology Finalisers                   ============================== */ 
    
     /**
     *  saves ontology in a serialization format.
     */
    @Override
    public void saveOntology() {
        try {
            ontManager.saveOntology(ont);
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(OwlApi_Pellet_Ontology.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/*==========================                 Local Methods, Tools, Utilities                  ======================= */     
     
   /**
            *  This is a reusable method for all the methods which return arraylist of string 
            *  from a  node set of owl entity ( class, individuals)
            * @param entitySet - node set of class or individual
            * @return list of string values
            */
    private ArrayList<String> getNames(NodeSet nodeSet){
        Set<OWLEntity> entitySet = nodeSet.getFlattened();
        return getNames(entitySet);
    }
/**
 * This is a reusable method for all the methods which return arraylist of string 
 *  from a   set of owl entity ( class, individuals)
 * @param entitySet - set of class or individual
 * @return list of string values
 */
    private ArrayList<String> getNames(Set<? extends OWLEntity> entitySet){
        ArrayList<String> al = new ArrayList<String>();
        for (OWLEntity entity: entitySet){
            al.add(IRI.create(entity.toStringID()).getFragment());
        }
        return al;
    }
    
/**
 *  It converts literal values to string.
 * @param values - a set o f literal values
 * @return a list of strings
 */
    private ArrayList<String> getLiteralNames(Set<OWLLiteral> values){
        ArrayList<String> al = new ArrayList<String>();
        for (OWLLiteral value : values){
            al.add(value.getLiteral());
        }
        return al;
    }
    
/**
 *  It removes the unwanted  part of the literal values (annotation values).
 * @param annSet - a set of owl annotation
 * @return  a list of strings
 */
    private ArrayList<String> getAnnotNames(Set<OWLAnnotation> annSet){
        ArrayList<String> al = new ArrayList<String>();
        for (OWLAnnotation ann : annSet){
            String value =ann.getValue().toString().replace("^^xsd:string", "");
            value = RegEx.removeQuote(value);
            al.add(value);
        }
        return al;
    }  

}
