/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.ont;

import java.util.ArrayList;
import rcm.main.Settings;

/**
 *
 * @author Krish
 */
public interface IOntology {
    /* Connection  related    */
    public String getNSURI();
    public void connect(String pathName, String fileName);

    /*  Ontology reasoning    */
    public boolean isConsistent();
    
     /*   Classes related  */
    public ArrayList<String> listClasses();
    public String getClass(String indName);
    public ArrayList<String> listClasses(String indName);
    public String getSuperClass(String className);
    public ArrayList<String> listSuperClasses(String className);
    public ArrayList<String> listSubClasses(String className);
    public ArrayList<String> listDisjointClasses(String className);
    public boolean isIndOfClass(String indName, String className);
 
    /* Individuals related    */
    public void createInd(String clsName, String indName);
    public ArrayList<String> listIndividuals(String className); // different in JenaOntModel
    public ArrayList<String> getIndsOfRegulation();//TODO consider removing ind of reg and task
    public ArrayList<String> getIndsOfTask();

    /* Properties related    */
    public String getDataPropertyValue(String subName, String propName);
    public ArrayList<String> listDataPropertyValues(String subName, String propName);
    public String getObjectPropertyValue(String subName, String propName);
    public ArrayList<String> listObjectPropertyValues(String subName, String propName);
    public int getPropertyValuesCount(String subName, String propName);
    public ArrayList<String> listRelatedProperties(String className, boolean direct);
    
    /* Cardinality Related */
    public int getMinCardinality(String clsName, String propName);
    public int getMaxCardinality(String clsName, String propName);
    public int getCardinality(String clsName, String propName);
    public String getSomeValuesFrom(String clsName, String propName);
    public String getAllValuesFrom(String clsName, String propName);
    public String getHasValue(String clsName, String propName);
    
    /*  Statement related   */
    public void addObjectStatement(String subName, String propName, String objName);
    public void addDataTypeStatement(String subName, String propName, String dataTypeValue);

    /*  Annotation related   */
    public void addComment(String subName, String comment);
    public String getComment(String resName);
    public ArrayList<String> listComments(String resName);
    public void addLabel(String subName, String label);
    public String getLabel(String resName);
    public ArrayList<String> listLabels(String resName);

   /* Ontology finalisers     */
    public void saveOntology();
}
