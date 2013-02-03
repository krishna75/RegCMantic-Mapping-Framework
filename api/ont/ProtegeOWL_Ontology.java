package api.ont;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package ont;
//
//import edu.stanford.smi.protegex.owl.ProtegeOWL;
//import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
//import edu.stanford.smi.protegex.owl.model.OWLIndividual;
//import edu.stanford.smi.protegex.owl.model.OWLModel;
//import edu.stanford.smi.protegex.owl.model.RDFObject;
//
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import javax.swing.JOptionPane;
//
///**
// *
// * @author Krishna Sapkota on 07-Jul-2010 at 19:13:00
// */
//public class ProtegeOWLModel implements InterfaceOntologyModel {
//    public OWLModel model;
//    public String ns;
//    public String nsURI;
//
///**
// * constructor with out parameters
// */
//    public ProtegeOWLModel() {
//        connect(pathName, fileName);
//        ns = "";
//        nsURI= getNSURI();
//
//    }
//
///**
// * constructor with nsPrefix as a parameter
// * @param nsPrefix of the ontology.
// */
//    public ProtegeOWLModel(String nsPrefix){
//        connect(pathName,fileName);
//        ns = nsPrefix;
//        nsURI= getNSURI();
//    }
///**
// * constructor with all the parameters
// * @param nsPrefix of theontology.
// * @param pathName of the ontology
// * @param fileName of the ontology
// */
//     public ProtegeOWLModel(String nsPrefix, String pathName, String fileName){
//        connect(pathName,fileName);
//        ns = nsPrefix;
//        nsURI= getNSURI();
//    }
//
//
//
//    public String getNS() {
//        return ns ;
//    }
//
//    public String getNSURI() {
//       //return model.getNamespaceManager().getPrefix(ns);
//        return ("");
//    }
//
//    /**
//     * Reads model from owl file.
//     * @param pathName the location of the file.
//     * @param fileName name of the ontology file.
//     */
//    public void connect(String pathName, String fileName){
//        JOptionPane.showMessageDialog(null, "reading ontology");//if  the file is not found, it shows the message
//       File owlFile = new File (pathName+fileName);
//       FileInputStream in;
//       try {
//             in = new FileInputStream(owlFile);
//             model = ProtegeOWL.createJenaOWLModelFromInputStream(in);
//
//       }catch (FileNotFoundException ex) {
//            JOptionPane.showMessageDialog(null, "The ontology file is not found");//if  the file is not found, it shows the message.
//        } catch (IOException ex){
//            ex.printStackTrace();
//        }
//       JOptionPane.showMessageDialog(null, "ontology read succesfully");//if  the file is not found, it shows the message.
//    };
//
//    public ArrayList<String> getIndsOfRegulation() {
//       ArrayList<String> al = new ArrayList<String>();
//       Collection instances =  model.getOWLNamedClass("Regulation").getInstances(false);
//       for (Iterator iterInstances = instances.iterator(); iterInstances.hasNext();) {
//            OWLIndividual individual = (OWLIndividual) iterInstances.next();
//            al.add(individual.getLocalName());
//        }
//
//       return al;
//
//    }
//
//    public ArrayList<String> getIndsOfTask() {
//        ArrayList<String> al = new ArrayList<String>();
//       Collection instances =  model.getOWLNamedClass("ValidationTask").getInstances(false);
//       for (Iterator iterInstances = instances.iterator(); iterInstances.hasNext();) {
//            OWLIndividual individual = (OWLIndividual) iterInstances.next();
//            al.add(individual.getLocalName());
//        }
//       return al;
//    }
//
//    public ArrayList<String> getPropertyValuesString(String subName, String propName) {
//        ArrayList<String> al = new ArrayList<String>();
//        Iterator iterObjects = model.getOWLNamedClass(subName).listPropertyValues( model.getOWLProperty(propName));
//        while (iterObjects.hasNext()) {
//            RDFObject object = (RDFObject) iterObjects.next();
//            al.add(object.getBrowserText());
//        }
//        return al;
//    }
//
//    public ArrayList<String> getIndividualNames(String className) {
//        return getPropertyValuesString(className, "rdfs:type");
//    }
//
//    public void writeModel() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void addStatement(String subName, String propName, String objName) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//}
