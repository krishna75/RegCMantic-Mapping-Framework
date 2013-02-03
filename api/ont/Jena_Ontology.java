
package api.ont;

import com.hp.hpl.jena.ontology.*;
import rcm.main.Settings;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;

import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDFS;
import helper.util.Converter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import helper.util.Print;
import helper.util.RegEx;
import java.util.List;
import org.mindswap.pellet.jena.PelletReasonerFactory;

/**
 * This class utilises the functionalities of Jena API to deal with OWL ontology e.g
 * getting classes, individuals, properties from the ontology or adding extra features to
 * the ontology.
 * @author Krish
 */
public final class Jena_Ontology implements IOntology{

    /** It is a jena model object to collect and hold all the information from the owl file  */
    private  OntModel model;
    private  String nsURI;
    private  String pathName;
    private  String fileName;
    
    public Jena_Ontology(String pathName, String fileName, String nsURI) {
        connect(pathName, fileName);
        this.fileName = fileName;
        this.pathName = pathName;
        this.nsURI = nsURI;
    }



/*==========================                 Connection Related                   ============================== */
    
    @Override
    public String getNSURI(){
        return nsURI;
    };
    
    @Override
    public void connect(String pathName, String fileName){
       File owlFile = new File (pathName+fileName);
       try {           
        FileInputStream in = new FileInputStream(owlFile);
        // create an empty ontology model using Pellet spec
        //        model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC ); 
        model = ModelFactory.createOntologyModel(  );

        // read the file
        model.read(in,"" );

        in.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "The ontology file is not found");//if  the file is not found, it shows the message.
        } catch (IOException ex){
            ex.printStackTrace();
        }
    };
    
/*==========================                 Ontology Reasoning                   ============================== */
    
    @Override
    public boolean isConsistent() {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
/*===========================            Classes Related              ===================================== */
 
    @Override
    public ArrayList<String>  listClasses(){       
        ArrayList<String> newList = new ArrayList<String>();
        ExtendedIterator classes = model.listNamedClasses();
        while (classes.hasNext()){
                OntClass thisClass = (OntClass) classes.next();
                newList.add(thisClass.getLocalName());
        }
        return RegEx.removeEmpty(newList);
    }
    
    @Override
    public String getClass(String indName) {      
        String strClass ="";
        Individual ind = model.getIndividual( nsURI + indName );
        if (ind != null ){
//            ind.get
            ind.listOntClasses(true);
            OntClass cls = ind.getOntClass(true);
            if (cls != null){
                strClass = cls.getLocalName();
            }
        }
        return strClass;
    }   
    
    @Override
    public ArrayList<String> listClasses(String indName){
        ArrayList<String> newList= new ArrayList<String>();       
        String strClass = getClass(indName);
        newList.add(strClass);
        newList.addAll(listSuperClasses(strClass));
        return newList;
    }
    
    @Override
    public String getSuperClass(String className) {
        OntClass cls = model.getOntClass(nsURI+className);
        return cls.getSuperClass().getLocalName();
    }

    @Override
    public ArrayList<String> listSuperClasses(String strClass){
        ArrayList<String> newList= new ArrayList<String>();
        OntClass cls = model.getOntClass(nsURI + strClass);
        if (cls != null){
            if (cls.hasSuperClass()){       
            ExtendedIterator<OntClass> iterClasses =  cls.listSuperClasses(true) ;
            while (iterClasses.hasNext()){
                    OntClass superClass = (OntClass) iterClasses.next();
                    if (superClass != null){
                        newList.add(superClass.getLocalName());
                    }
                }
            }
        }
       // removes null items and returns the array 
       return RegEx.removeEmpty(newList);
    }
    
    @Override
    public ArrayList<String> listSubClasses(String className) {
        ArrayList<String> newList = new ArrayList<String>();
        OntClass cls = model.getOntClass(nsURI + className);   
        if (cls != null){
            if (cls.hasSubClass()){
            ExtendedIterator<OntClass> iterClasses =  cls.listSubClasses(false) ;
            while (iterClasses.hasNext()){
                    OntClass subClass = (OntClass) iterClasses.next();
                    if (subClass != null){
                        newList.add(subClass.getLocalName());
                    }
                }
            }
        }
       return RegEx.removeEmpty(newList);
    }
    
    @Override
    public ArrayList<String> listDisjointClasses(String clsName){
        ArrayList<String> newList= new ArrayList<String>();
        OntClass cls = model.getOntClass(nsURI + clsName);
        List<OntClass> disClassList =  cls.listDisjointWith().toList(); 
        for (OntClass disClass : disClassList){
            if (disClass !=null){
                newList.add(disClass.getLocalName());
            }
        }   
    return RegEx.removeEmpty(newList);
}
   
    @Override
    public boolean isIndOfClass(String indName, String className) {
        boolean found = false;
        for (String ind: listIndividuals(className)){
            if ( indName.equals(ind)){
                found = true;
            }
        }
        return found;
    }
    
        
/*===========================            Individual  Related              ===================================== */  
    
    @Override
    public void createInd(String clsName, String indName){
        OntClass cls = model.getOntClass(nsURI+clsName);
        cls.createIndividual(nsURI+ indName);        
    }
    
    @Override
    public ArrayList<String> listIndividuals(String className){
        ArrayList<String> newList= new ArrayList<String>();
        OntClass cls = model.getOntClass(nsURI+className);
        List<? extends OntResource> indList = cls.listInstances(false).toList();
        for (OntResource ind : indList){
                    newList.add(ind.getLocalName());
        }
       return RegEx.removeEmpty(newList);
    }   
    
    @Override
    public ArrayList<String> getIndsOfRegulation(){
        return listIndividuals("Regulation");
    }

    @Override
    public ArrayList<String> getIndsOfTask(){
        return listIndividuals("ValidationTask");
    }


/*==========================           Properties Related             ==================================== */  
    
    @Override
    public String getDataPropertyValue(String subName, String propName) {
        Individual ind = model.getIndividual(nsURI+subName);
        OntProperty p = model.getOntProperty(nsURI+propName);
        RDFNode valueNode = ind.getPropertyValue(p);    
        return getLocalName(valueNode);
    }      
    
    @Override
    public ArrayList<String> listDataPropertyValues(String indName, String prop){
        ArrayList<String> newList = new ArrayList<String>();
        Individual ind = model.getIndividual(nsURI+indName);
        Property p = model.getOntProperty(nsURI+prop);
        List<RDFNode> valueList = ind.listPropertyValues(p).toList();
        for (RDFNode value: valueList){
            newList.add(getLocalName(value));
        }
        return RegEx.removeEmpty(newList);
    }
    
    @Override
    public String getObjectPropertyValue(String subName, String propName) {
        Individual ind = model.getIndividual(nsURI+subName);
        OntProperty p = model.getOntProperty(nsURI+propName);
        RDFNode valueNode = ind.getPropertyValue(p);    
        return getLocalName(valueNode);
    }  
    
    @Override
    public ArrayList<String> listObjectPropertyValues(String subName, String propName) {
        ArrayList<String> newList = new ArrayList<String>();
        Individual ind = model.getIndividual(nsURI+subName);
        OntProperty p = model.getOntProperty(nsURI+propName);
        if (ind != null && p != null){
            List<RDFNode> valueList = ind.listPropertyValues(p).toList();
            for (RDFNode value: valueList){
                newList.add(getLocalName(value));
            }
        }
        return RegEx.removeEmpty(newList);
    }
    
    @Override
    public int getPropertyValuesCount(String subName, String propName) {
        int value = 0;
        Individual ind = model.getIndividual(nsURI+subName);
        OntProperty p = model.getOntProperty(nsURI+propName);
        value = listObjectPropertyValues(subName, propName).size();
        return value;
    }
    
    @Override
    public ArrayList<String> listRelatedProperties(String className, boolean direct){
        ArrayList<String> newList = new ArrayList<String>();
        OntClass cls1 = model.getOntClass(nsURI+className);
        List<ObjectProperty> propList =  model.listObjectProperties().toList();
        if (cls1 != null){
            List<OntClass> clsList = cls1.listSuperClasses().toList();
            clsList.add(cls1);
            for (OntClass cls: clsList){
                for (ObjectProperty p: propList){
                    if (p.hasDomain(cls)){                       
                        newList.add(p.getLocalName());
                    }
                }   
            }
        } 
        newList = Converter.makeUnique(newList);
        return RegEx.removeEmpty(newList);
    }

/*==========================           Cardinality Related             =================================== */ 

@Override
    public int getMinCardinality(String clsName, String propName) {
        int value =0 ;
        OntClass cls = model.getOntClass( nsURI + clsName );
        OntProperty p = model.getOntProperty( nsURI + propName);
        Iterator superClassIter = cls.listSuperClasses();
        while( superClassIter.hasNext() ) {
            OntClass c = (OntClass) superClassIter.next();
            if (c.isRestriction()) {
                Restriction r = c.asRestriction();
                if (r.isMinCardinalityRestriction()){
                    MinCardinalityRestriction minCard = r.asMinCardinalityRestriction();
                    if (minCard.onProperty(p)){
                        value = minCard.getMinCardinality();
                    }
                }
            }
        }
        return value;
    }
    
    @Override
    public int getMaxCardinality(String clsName, String propName) {
        int value =0 ;
        OntClass cls = model.getOntClass( nsURI + clsName );
        OntProperty p = model.getOntProperty( nsURI + propName);
        Iterator superClassIter = cls.listSuperClasses();
        while( superClassIter.hasNext() ) {
            OntClass c = (OntClass) superClassIter.next();
            if (c.isRestriction()) {
                Restriction r = c.asRestriction();
                if (r.isMaxCardinalityRestriction()){
                    MaxCardinalityRestriction maxCard = r.asMaxCardinalityRestriction();
                    if (maxCard.onProperty(p)){
                        value = maxCard.getMaxCardinality();
                    }
                }
            }
        }
        return value;
    }
    
    @Override
    public int getCardinality(String clsName, String propName) {
        int value =0 ;
        OntClass cls = model.getOntClass( nsURI + clsName );
        OntProperty p = model.getOntProperty( nsURI + propName);
        Iterator superClassIter = cls.listSuperClasses();
        while( superClassIter.hasNext() ) {
            OntClass c = (OntClass) superClassIter.next();
            if (c.isRestriction()) {
                Restriction r = c.asRestriction();
                if (r.isCardinalityRestriction()){
                    CardinalityRestriction card = r.asCardinalityRestriction();
                    if (card.onProperty(p)){
                        value = card.getCardinality();
                    }
                }
            }
        }
        return value;
    }
    
    @Override
    public String getSomeValuesFrom(String clsName, String propName) {
        String value = "" ;
        OntClass cls = model.getOntClass( nsURI + clsName );
        OntProperty p = model.getOntProperty( nsURI + propName);
        Iterator superClassIter = cls.listSuperClasses();
        while( superClassIter.hasNext() ) {
            OntClass c = (OntClass) superClassIter.next();
            if (c.isRestriction()) {
                Restriction r = c.asRestriction();
                if (r.isSomeValuesFromRestriction()){
                    SomeValuesFromRestriction svf = r.asSomeValuesFromRestriction();
                    if (svf.onProperty(p)){
                        value = svf.getSomeValuesFrom().getLocalName();
                    }
                }
            }
        }
        return value;
    }
    
    @Override
    public String getAllValuesFrom(String clsName, String propName) {
        String value = "" ;
        OntClass cls = model.getOntClass( nsURI + clsName );
        OntProperty p = model.getOntProperty( nsURI + propName);
        Iterator superClassIter = cls.listSuperClasses();
        while( superClassIter.hasNext() ) {
            OntClass c = (OntClass) superClassIter.next();
            if (c.isRestriction()) {
                Restriction r = c.asRestriction();
                if (r.isAllValuesFromRestriction()){
                    AllValuesFromRestriction avf = r.asAllValuesFromRestriction();
                    if (avf.onProperty(p)){
                        value = avf.getAllValuesFrom().getLocalName();
                    }
                }
            }
        }
        return value;
    }

    @Override
    public String getHasValue(String clsName, String propName) {
        String value = "" ;
        OntClass cls = model.getOntClass( nsURI + clsName );
        OntProperty p = model.getOntProperty( nsURI + propName);
        Iterator superClassIter = cls.listSuperClasses();
        while( superClassIter.hasNext() ) {
            OntClass c = (OntClass) superClassIter.next();
            if (c.isRestriction()) {
                Restriction r = c.asRestriction();
                if (r.isHasValueRestriction()){
                    HasValueRestriction hv = r.asHasValueRestriction();
                    if (hv.onProperty(p)){
                        RDFNode valueNode = hv.getHasValue();
                        value = valueNode.asNode().getLocalName();
                    }
                }
            }
        }
        return value;
    }
    
/*==========================           Statement Related             =================================== */   
    
/**
    * It adds a statement to the ontology.
    * @param sub is the subject(String) of the statement.
    * @param prop is the property/predicate(String) of the statement.
    * @param obj is the object (String) of the statement.
    */
    @Override
    public  void addObjectStatement(String sub, String prop, String obj){
        OntResource s = model.getOntResource(nsURI+sub);
        OntResource o = model.getOntResource(nsURI+obj);
        OntProperty p= model.getOntProperty(nsURI+prop);
        model.add(s, p,o);    
    }

    @Override
     public void addDataTypeStatement(String subName, String propName, String dataTypeValue){
         OntResource s = model.getOntResource(nsURI+subName);
         OntProperty p= model.getOntProperty(nsURI+propName);
         RDFNode o = model.createTypedLiteral(dataTypeValue);
         model.add(s, p,o);
     }    

/*=============================           Annotation Related         ================================== */     
    
      @Override
      public void addComment(String subName, String comment){
        OntResource s = model.getOntResource(nsURI+subName);
        s.addProperty(RDFS.comment, comment);   
     }
    
    @Override
    public String getComment(String resName) {
        String value = "";
        if (!listComments(resName).isEmpty()){
            value = listComments(resName).get(0);
        }
        return value;
    }

    @Override
    public ArrayList<String> listComments(String resName) {
        OntResource s = model.getOntResource(nsURI+resName);
        return listAnnotations(s, RDFS.comment);
    }
    
    @Override
    public void addLabel(String subName, String label) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLabel(String resName) {
        String value = "";
        if (!listLabels(resName).isEmpty()){
            value = listLabels(resName).get(0);
        }
        return value;
    }
    
    @Override
    public ArrayList<String> listLabels(String resName){
        OntResource s = model.getOntResource(nsURI+resName);
        return listAnnotations(s, RDFS.label);
    }

    // this methods will be used by the other annotation  methods.
    public ArrayList<String> listAnnotations(OntResource s, Property p){
        ArrayList<String> newList = new ArrayList<String>();
        NodeIterator iterNode= model.listObjectsOfProperty(s, p);
        while (iterNode.hasNext()){
            RDFNode node = iterNode.next();
            String value = getLocalName(node);
            if (value!=null){
                newList.add(value);
            }
        }
        return newList;
    }


/*=============================           Finaliser             ======================================== */    

    @Override
    public  void saveOntology(){
        OutputStream out;
        try {
           out = new FileOutputStream(new File(pathName+fileName));
           model.write(out);
        } catch (FileNotFoundException ex) {
          JOptionPane.showMessageDialog(null, "File writing error", "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public OntModel getModel() {
        return model;
    }

    public void setModel(OntModel model) {
        this.model = model;
    }
   
/*=============================           Tools/ Utilities            ==================================== */ 
    
    
 /**
     * RDFNode can be either a Resource or a Literal . The Resource and Literal
     * have different structures. It firstly detects the RDFNode and applying appropriate procedure.
     * @param node the RDFNode
     * @return local name (String) of the node.
     */
    public String getLocalName(RDFNode node) {
       String objName =  "";
       if (node != null){
            if (node.isLiteral()){                                     // resource or literal
                    objName =node.asNode().getLiteralLexicalForm();
            }else if (node.isURIResource()) {
                objName = node.asNode().getLocalName();
            } 
       }
       return objName;
     }
}
