/*                |            @author Krishna Sapkota,  Mar 27, 2012                  |
 *                 |             A PhD project at Oxford Brookes University          |
 *                 | ________________________________________  | 
 */

package helper.compliance;

import api.xml.XmlWriter;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.Print;
import java.util.ArrayList;
import org.w3c.dom.Element;
import rcm.main.Settings;
import rcm.phase34.ui._2_MappingData;

/**
 *  Compliance Violation Collector:
 * This class is used to collect the information about the compliance violation.
 */
public class Violation {
    String id;
    String type;
    String domainClass;
    String rangeClass;
    String domainInd;
    String rangeInd;
    String property;
    String expectedValue;
    String actualValue;
    String reason;
    String solution;
    String regulation;
    String regText;
    ArrayList<Violation> violationList;
      /*  i/o related */
    private String XML_FILENAME = Settings.FILES_PATH + "violation.xml";

    public Violation() {
        init();
        process();
        finish();
    }
    
    private void init(){
        id = "";
        type = "";
        domainClass = "";
        rangeClass = "";
        domainInd = "";
        rangeInd = "";
        property = "";
        expectedValue = "";
        actualValue = "";
        reason = "";
        solution = ""; 
        violationList = new ArrayList<Violation>();
    }
    private void finish(){
        
    }
    private void process(){
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getDomainClass() {
        return domainClass;
    }

    public void setDomainClass(String domainClass) {
        this.domainClass = domainClass;
    }

    public String getDomainInd() {
        return domainInd;
    }

    public void setDomainInd(String domainInd) {
        this.domainInd = domainInd;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getRangeClass() {
        return rangeClass;
    }

    public void setRangeClass(String rangeClass) {
        this.rangeClass = rangeClass;
    }

    public String getRangeInd() {
        return rangeInd;
    }

    public void setRangeInd(String rangeInd) {
        this.rangeInd = rangeInd;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Violation> getViolationList() {
        return violationList;
    }

    public void setViolationList(ArrayList<Violation> violationList) {
        this.violationList = violationList;
    }

    public String getRegText() {
        return regText;
    }

    public void setRegText(String regText) {
        this.regText = regText;
    }
    
    
    
    public void readFile(){
        Print.prln("Violation: reading violations a from file ...");
        ArrayList<Element> vElementList = MyReader.getXmlDomElements(XML_FILENAME, "violation");
        violationList.clear();
        for (Element vElement: vElementList ){
            Violation v = new Violation();
            violationList.add(v);

            /* reading  xml values */ 
            Element vId = (Element)vElement.getElementsByTagName("violation_id").item(0);
            Element vType = (Element) vElement.getElementsByTagName("type").item(0);
            
            Element dClass = (Element)vElement.getElementsByTagName("domain_class").item(0);          
            Element rClass = (Element) vElement.getElementsByTagName("range_class").item(0);
            Element dInd = (Element)vElement.getElementsByTagName("domain_ind").item(0);
            Element rInd = (Element)vElement.getElementsByTagName("range_ind").item(0);            
            Element prop = (Element)vElement.getElementsByTagName("property").item(0);
            
            Element expected = (Element) vElement.getElementsByTagName("expected_value").item(0);
            Element actual = (Element)vElement.getElementsByTagName("actual_value").item(0);
            Element rsn = (Element) vElement.getElementsByTagName("reason").item(0);
            Element sln = (Element)vElement.getElementsByTagName("solution").item(0);
            Element reg = (Element)vElement.getElementsByTagName("regulation").item(0);
            Element reg_text = (Element)vElement.getElementsByTagName("reg_text").item(0);
            
            /* populating the values*/
            v.setId(vId.getTextContent());
            v.setType(vType.getTextContent());
            
            v.setDomainClass(dClass.getTextContent());
            v.setRangeClass(rClass.getTextContent());
            v.setDomainInd(dInd.getTextContent());
            v.setRangeInd(rInd.getTextContent());
            v.setProperty(prop.getTextContent());
            
            v.setExpectedValue(expected.getTextContent());
            v.setActualValue(actual.getTextContent());
            v.setReason(rsn.getTextContent());
            v.setSolution(sln.getTextContent());
            v.setRegulation(reg.getTextContent());
            v.setRegText(reg_text.getTextContent());
        }    
        Print.prln("... reading completed. total violations = "+ violationList.size());   
    }
    
    
 /**
     *  writes an xml file and dumps its properties
     */
    public void writeFile(){
        Print.prln("Violation: writing violation list to a file ...");
        Print.prln("Total Violations = " + violationList.size());
        XmlWriter xml = new XmlWriter(XML_FILENAME);
        xml.addAttribute("size", String.valueOf(violationList.size()));
        xml.startElement("violation_data"); 
        for (Violation v: violationList){

           /*  getting values from the object */
           String id1               = v.getId();
           String type1             = v.getType();
           String domainClass1      = v.getDomainClass();
           String rangeClass1       = v.getRangeClass();
           String domainInd1        = v.getDomainInd();
           String rangeInd1         = v.getRangeInd();
           String property1         = v.getProperty();
           String expectedValue1    = v.getExpectedValue();
           String actualValue1      = v.getActualValue();           
           String reason1           = v.getReason();
           String solution1         = v.getSolution();  
           String regulation1       = v.getRegulation();
           String regText1          = v.getRegText();
        
         /*  writing the values in xml file */
        xml.startElement("violation");
            xml.startElement("violation_id");
                xml.characters(id1);
            xml.endElement("violation_id");
            xml.startElement("type");
                xml.characters(type1);
            xml.endElement("type");
            xml.startElement("domain_class");
                xml.characters(domainClass1);
            xml.endElement("domain_class");
            xml.startElement("range_class");
                xml.characters(rangeClass1);
            xml.endElement("range_class");
            xml.startElement("domain_ind");
                xml.characters(domainInd1);
            xml.endElement("domain_ind");
            xml.startElement("range_ind");
                xml.characters(rangeInd1);
            xml.endElement("range_ind");
            xml.startElement("property");
                xml.characters(property1);
            xml.endElement("property");       
            xml.startElement("expected_value");
                xml.characters(expectedValue1);
            xml.endElement("expected_value");
            xml.startElement("actual_value");
                xml.characters(actualValue1);
            xml.endElement("actual_value");
            xml.startElement("reason");
                xml.characters(reason1);
            xml.endElement("reason");
            xml.startElement("solution");
                xml.characters(solution1);
            xml.endElement("solution");
            xml.startElement("regulation");
                xml.characters(regulation1);
            xml.endElement("regulation");
            xml.startElement("reg_text");
                xml.characters(regText1);
            xml.endElement("reg_text");           
        xml.endElement("violation");       
        }
        xml.endElement("violation_data");
        xml.write(); 
    }
    
     public void generateId(){
        int vid = 1;
        for (Violation v1 : this.getViolationList()){
            v1.setId("vid_"+vid);
            vid++;
        }
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }
    
     
    
    
}
