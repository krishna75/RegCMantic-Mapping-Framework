/*                |            @author Krishna Sapkota,  Mar 9, 2012                  |
 *                 |             A PhD project at Oxford Brookes University          |
 *                 | ________________________________________  | 
 */

package helper.compliance;

import api.xml.XmlWriter;
import helper.util.Converter;
import helper.util.Print;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import rcm.main.Settings;
import rcm.phase34.ui._2_MappingData;

/**
 *  Compliance Violation Collector:
 * This class is used to collect the information about the compliance violation.
 */
public class IndividualViolation {
    Map<String, ArrayList<Violation>> violationMap = new HashMap<String, ArrayList<Violation>>();
    ArrayList<Violation> violationList = new ArrayList<Violation>();

    public IndividualViolation() {
        init();
        process();
        finish();
    }
    
    private void init(){
         Violation v =   new Violation();
         v.readFile();
         violationList = v.getViolationList();
    }
    private void finish(){
        
    }
    private void process(){
        populateMap();
    } 
    private void populateMap(){
        Print.prln("IndividualViolation: populating individual violations a from file ...");
        // get list of the ind names
        ArrayList<String> indList = new ArrayList<String>();
        for (Violation v : violationList){
            indList.add(v.getDomainInd());
        }
        indList = Converter.makeUnique(indList);
        
        // group ind names together
        for (String ind: indList){
            ArrayList<Violation> vList1 = new ArrayList<Violation>();
            for(Violation v: violationList){
                if (ind.equals(v.getDomainInd())){
                    vList1.add(v);
                }
            }
            violationMap.put(ind, vList1);
        }
        Print.prln("... population completed. total individuals = " + violationMap.size());
        
    }
    public ArrayList<Violation> getIndViloations(String indName ){
        return violationMap.get(indName);
    }
    public Violation getViolationByProperty(ArrayList<Violation> vList1, String property){
        Violation v = new Violation();
        for(Violation v1: vList1){
            if (property.equals(v1.getProperty())){
                v = v1;
            }
        }
        return v;
    }

    public ArrayList<Violation> getViolationList() {
        return violationList;
    }

    public void setViolationList(ArrayList<Violation> violationList) {
        this.violationList = violationList;
    }

    public Map<String, ArrayList<Violation>> getViolationMap() {
        return violationMap;
    }

    public void setViolationMap(Map<String, ArrayList<Violation>> violationMap) {
        this.violationMap = violationMap;
    }

  
    
    
}
