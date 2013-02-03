/*                |            @author Krishna Sapkota,  Feb 1, 2012                    |
 *                 |             A PhD project at Oxford Brookes University          |
 *                 | ________________________________________  | 
 */

package rcm.phase4;

//standard java imports
import api.ont.IOntologyFiles;
import api.ont.Jena_Ontology;
import helper.compliance.Violation;
import helper.util.Converter;
import helper.util.MyWriter;
import helper.util.Print;
import java.util.ArrayList;
import rcm.main.Settings;
import rcm.phase34.ui._2_MappingData;

/**
 *  Cardinality Violation Checker:
 *  This class is used to check the cardinality violations in an ontology.
 */
public class Cardinality implements IOntologyFiles {
    ArrayList<Violation> vList;
    Jena_Ontology ontoReg;
    ArrayList<String> resultList;
    String resultFile;
    
    String EMPTY = "Empty Value";
    String MIN = "Min Cardinality Violation";
    String MAX = "Max Cardinality Violation";
    String EXACT = "Exact Cardinality Violation";
    String SOME = "Some Values From Violation";
    String ALL = "All Values From Violation";
    
    String regulation ="";
    
    
    public Cardinality(){
        init();
        process();
        finish();     
    }
    
    private void init(){     
        ontoReg = new Jena_Ontology(path, ontoFile, ontoPrefix);
        resultList = new ArrayList<String>();
        resultFile = Settings.FILES_PATH+"cardinality_result.txt";
    
    }
    
private void process(){
   
}

private void finish(){

}

public ArrayList<Violation>  checkClass(String className){
    ArrayList<Violation> v1List = new ArrayList<Violation>();
    ArrayList<String> pList = ontoReg.listRelatedProperties(className, true);
    for (String p: pList){
        ArrayList<String> indList = ontoReg.listIndividuals(className);
        for (String ind: indList){
            v1List.addAll(checkAllViolations(ind,p));
       }
    } 
    return v1List; 
}

public ArrayList<Violation>  checkInd(String indName){
    ArrayList<Violation> v1List = new ArrayList<Violation>();
    String className = ontoReg.getClass(indName);
    ArrayList<String> pList = ontoReg.listRelatedProperties(className, true);
    for (String p: pList){
        if (!p.equals("isBasedOnRegulation")){
            v1List.addAll(checkAllViolations(indName, p));
        }
    }
    return v1List;   
}


public ArrayList<Violation> checkAllViolations(String indName, String propName){
    vList = new ArrayList<Violation>();
    if (! checkEmpty(indName,propName)){
        checkMin(indName, propName);
        checkMax(indName, propName);
        checkExact(indName,propName);
        checkSomeValues(indName, propName);
        checkAllValues(indName,propName);
    }
    return vList;
}

private boolean checkEmpty(String indName, String propName){
    boolean found = false;
    int count =  ontoReg.getPropertyValuesCount(indName, propName);
    if (count == 0){
        found = true;
        Violation v = new Violation();
        v.setType(EMPTY);
        v.setDomainInd(indName);
        v.setProperty(propName);
        v.setExpectedValue("");
        v.setActualValue("");
        String reason= "There is no propery value defined";
        String solution = "Please provide appropriate value to the individual '"
                + indName+"' on its property '"+propName+ "'.";
        v.setReason(reason);  
        v.setSolution(solution);
        v.setRegulation(regulation);        
       vList.add(v);
    }
    return found;
}
 
 
  private void checkMin(String indName, String propName) {     
     String className = ontoReg.getClass(indName);
     int minCard = ontoReg.getMinCardinality(className, propName);
     int count = ontoReg.getPropertyValuesCount(indName, propName);
     if (minCard != 0 && count < minCard){
        Violation v = new Violation();
        v.setType(MIN);
        v.setDomainInd(indName);
        v.setProperty(propName);
        v.setExpectedValue(String.valueOf(minCard));
        v.setActualValue(String.valueOf(count));
        String reason= "";
        if (minCard == 1){
            reason = "There is no propery value defined";
        }else {
            reason = "The number of property values is less than " +minCard;
        }
        v.setReason(reason);  
        String solution = "Please add "+minCard+" or more property values for '"
                + indName+"' on its property '"+propName+ "'.";
        v.setSolution(solution);
        v.setRegulation(regulation);
       vList.add(v);
     }
 }  

private void checkMax(String indName, String propName) {     
     String className = ontoReg.getClass(indName);
     int maxCard = ontoReg.getMaxCardinality(className, propName);
     int count = ontoReg.getPropertyValuesCount(indName, propName);
     if (maxCard != 0 && count > maxCard){
        Violation v = new Violation();
        v.setType(MAX);
        v.setDomainInd(indName);
        v.setProperty(propName);
        v.setExpectedValue(String.valueOf(maxCard));
        v.setActualValue(String.valueOf(count));
        String reason= "The number of property values is more than " +maxCard;        
        v.setReason(reason); 
        int diff = count - maxCard;
        String solution = "Please remove "+diff+" or more property values for '"
                            + indName+"' on its property '"+propName+ "'.";
        v.setSolution(solution);
        v.setRegulation(regulation);
       vList.add(v);
     }
 }
 
  
private void checkExact(String indName, String propName) {     
     String className = ontoReg.getClass(indName);
     int card = ontoReg.getCardinality(className, propName);
     int count = ontoReg.getPropertyValuesCount(indName, propName);
     if (card != 0 && count!= card){
        Violation v = new Violation();
        v.setType(EXACT);
        v.setDomainInd(indName);
        v.setProperty(propName);
        v.setExpectedValue(String.valueOf(card));
        v.setActualValue(String.valueOf(count));
        String reason;
        String solution;
        int diff = count - card;
        if (count < card){
            reason= "The number of property values is less than " +card; 
            solution = "Please add "+(-diff) +" more property value(s) to '"
                            + indName+"' on its property '"+propName+ "'.";
        }else {
            reason= "The number of property values is greater than " +card;
            solution = "Please remove "+ diff +" property value(s) from '"
                            + indName+"' on its property '"+propName+ "'.";
        }               
        v.setReason(reason); 
        v.setSolution(solution);
        v.setRegulation(regulation);
        vList.add(v);
     }
 }  


   
 private void checkSomeValues(String indName, String propName) {
     boolean targetFound = false;
     String className = ontoReg.getClass(indName);
     String targetClass = ontoReg.getSomeValuesFrom(className, propName);
     String violatingClass = "";
     if (targetClass != null && !targetClass.equals("")){
        ArrayList<String> targetIndList = ontoReg.listObjectPropertyValues(indName, propName);
        for (String targetInd: targetIndList){
            if (ontoReg.isIndOfClass(targetInd, targetClass)){
                targetFound = true;                
                break;
            }else {
                violatingClass = ontoReg.getClass(targetInd);
            }
        }
     
        if (!targetFound){
            Violation v = new Violation();
            v.setType(SOME);
            v.setDomainClass(className);
            v.setRangeClass(targetClass);
            v.setDomainInd(indName);
            v.setProperty(propName);
            v.setExpectedValue(targetClass);
            v.setActualValue(violatingClass);
            String reason = " There are no individuals of the class '"+targetClass +
                    "' as the values of the property '"+ propName+"'.";
            String solution = "Please add at least one individual of the class '" +
                    targetClass + "' to the '"+ indName+ "' on its property '"+ propName +"'.";                               
            v.setReason(reason); 
            v.setSolution(solution);
            v.setRegulation(regulation);
            vList.add(v);
        }   
    }
 }   
   

 private void checkAllValues(String indName, String propName) {
     boolean violationFound = false;
     String className = ontoReg.getClass(indName);
     String targetClass = ontoReg.getAllValuesFrom(className, propName);
     String violatingClass = "";
     String violatingInd ="";
     if (!targetClass.equals("") && targetClass != null){
        ArrayList<String> targetIndList = ontoReg.listObjectPropertyValues(indName, propName);
        for (String targetInd: targetIndList){
            if (!ontoReg.isIndOfClass(targetInd, targetClass)){
                violationFound = true; 
                violatingInd = targetInd;
                violatingClass = ontoReg.getClass(targetInd);
                break;
            }
        }
     }
    if (violationFound){
        Violation v = new Violation();
        v.setType(ALL);
        v.setDomainClass(className);
        v.setRangeClass(targetClass);
        v.setDomainInd(indName);
        v.setProperty(propName);
        v.setExpectedValue(targetClass);
        v.setActualValue(violatingClass);
        String reason = " There are individuals of the wrong class '"+violatingClass +
                "' as the values of the property '"+ propName+"'.";
        String solution = "Please remove the individual '" + violatingInd +"' belonging to the class '"+
                violatingClass + "' from the '"+ indName+ "' on its property '"+ propName +"'.";                               
        v.setReason(reason); 
        v.setSolution(solution);
        v.setRegulation(regulation);
        vList.add(v);
    }
 }

}
