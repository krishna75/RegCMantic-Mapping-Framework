/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.ont;


import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import helper.util.Print;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Krishna Sapkota,  Feb 15, 2012,  9:49:25 PM
 * A PhD project at Oxford Brookes University
 */
public class SwrlOwlJess implements IOntologyFiles  {
    OWLModel model;

    public SwrlOwlJess() {
        connect(path ,ontoFile);
    }
    
    public void connect(String pathName, String fileName){
       File owlFile = new File (pathName+fileName);
       try {           
        FileInputStream in = new FileInputStream(owlFile);
        model = ProtegeOWL.createJenaOWLModelFromInputStream(in);
        in.close();
        } catch (OntologyLoadException ex) {
            Logger.getLogger(SwrlOwlJess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "The ontology file is not found");//if  the file is not found, it shows the message.
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    
    
}
