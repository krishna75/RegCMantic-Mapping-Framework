
package api.ont;

import helper.util.Print;

/**
 *
 * @author Krishna Sapkota, 13-Dec-2011,   09:23:25
 * A PhD project at Oxford Brookes University
 */
public abstract class JenaAbstractOntology implements IOntologyFiles {
    public IOntology ontoReg;
    public IOntology semReg;
    public JenaAbstractOntology(){
        init();
    }
    private void init(){
        
    /*  initialise ontologies  */
    String ontoFile1 = "OntoReg_v3.owl";
    String semFile1 = "SemReg_v4.owl";  
    ontoReg = new Jena_Ontology(path, ontoFile, ontoPrefix);
    semReg = new Jena_Ontology(path, semFile1, semPrefix);
//        ontoReg = new OwlApi_Pellet_Ontology(path, "OntoReg_d.owl", ontoPrefix);
    
    Print.prln("SemReg NS URI = "+semReg.getNSURI());
    Print.prln("SemReg URL = "+path+semFile1);
    Print.prln("OntoReg URL = "+path+ ontoFile1);


    }

}
