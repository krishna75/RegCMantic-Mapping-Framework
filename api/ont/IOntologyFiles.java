/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.ont;

import rcm.main.Settings;

/**
 *
 * @author Krish
 */
public interface IOntologyFiles {

     public String path = Settings.ONTOLOGY_PATHNAME;
//     public String ontoFile = Settings.ONTOREG_FILENAME;
     public String ontoFile = "OntoReg_v3.owl";
     public String ontoPrefix = Settings.ONTOREG_URI_PREFIX;
//     public String semFile = Settings.SEMREG_FILENAME;
     public String semFile = "SemReg_v4.owl";
     public String semPrefix = Settings.SEMREG_URI_PREFIX;
     public String regBody = "Eudralex";    

}
