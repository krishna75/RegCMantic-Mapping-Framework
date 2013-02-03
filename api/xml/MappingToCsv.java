/*                |            @author Krishna Sapkota,  Mar 2, 2012                  |
 *                 |             A PhD project at Oxford Brookes University          |
 *                 | ________________________________________  | 
 */

package api.xml;

import helper.util.Converter;
import helper.util.Print;
import java.util.ArrayList;
import org.w3c.dom.Element;
import rcm.main.Settings;

/**
 *  Mapping Result To CSV Converter :
 *  This class is used to convert the mapping result, an XML file to CSV format.
 *  It processes the  input as the XML data and extends the abstract class to write the output.
*/
public class MappingToCsv extends Abstract_ConvertToCsv{

    
public MappingToCsv() {
    init();
    populateDataList();
    writeCSV();
}
    
    
    
private void init(){
    xmlFile = Settings.FILES_PATH+"mapping4.xml";
    csvFile = Settings.FILES_PATH+"mapping4.csv";
    reader = new XmlReader();
    dataList = new ArrayList< ArrayList<String>>();
    String[] columns = {"regulation_statement","validation_task", "reg_id", "reg_text","stmt_id","stmt_text","reg_subject", "reg_action",
                        "task_id","task_description", "task_subject","task_action",
                        "maping_id","subject_score","action_score","matched"};
    columnList = Converter.arrayToArrayList(columns) ; 
}
private void populateDataList(){
        Print.prln("MappingToCsv: reading data from the xml file ...");
        ArrayList<Element> mappingElementList = reader.getXmlDomElements(xmlFile, "mapping");
        for (Element mappingElement: mappingElementList ){
            data = new ArrayList<String>();
            dataList.add(data);
            
            /* reading  regulation values */ 
            Element regulation = (Element)mappingElement.getElementsByTagName("regulation").item(0);
                Element reg_id = (Element)regulation.getElementsByTagName("reg_id").item(0);
                Element reg_text = (Element) regulation.getElementsByTagName("reg_text").item(0);
                Element stmt_id = (Element)regulation.getElementsByTagName("stmt_id").item(0);
                Element stmt_text = (Element) regulation.getElementsByTagName("stmt_text").item(0);
                Element reg_subject = (Element)regulation.getElementsByTagName("reg_subject").item(0);
                Element reg_action = (Element)regulation.getElementsByTagName("reg_action").item(0);  
                
            /* reading  task values */
            Element task = (Element)mappingElement.getElementsByTagName("validation_task").item(0);
                Element task_id = (Element) task.getElementsByTagName("task_id").item(0);
                Element task_desc = (Element) task.getElementsByTagName("task_description").item(0);
                Element task_subject = (Element) task.getElementsByTagName("task_subject").item(0);
                Element task_action = (Element) task.getElementsByTagName("task_action").item(0);  

            /* reading  mapping values */
            Element mapping_id = (Element)mappingElement.getElementsByTagName("mapping_id").item(0);
            Element similarity_score = (Element)mappingElement.getElementsByTagName("similarity_score").item(0);
                Element subject_score = (Element) similarity_score.getElementsByTagName("subject_score").item(0);
                Element action_score = (Element) similarity_score.getElementsByTagName("action_score").item(0);
                Element accepted1 = (Element)mappingElement.getElementsByTagName("accepted").item(0);
                Element remark1 = (Element)mappingElement.getElementsByTagName("remark").item(0);
                
                /*  setting first two columns*/
                String r = stmt_id.getTextContent()+
                        ": \n"+ stmt_text.getTextContent();
                String t = task_id.getTextContent()+
                        ": \n"+task_desc.getTextContent();
                data.add(r) ;
                data.add(t);

                /* setting  regulation values */
                data.add(reg_id.getTextContent());
                data.add(reg_text.getTextContent());
                data.add(stmt_id.getTextContent());
                data.add(stmt_text.getTextContent());
                data.add(reg_subject.getTextContent());
                data.add(reg_action.getTextContent());
                
                /* setting  task values */
                data.add(task_id.getTextContent());
                data.add(task_desc.getTextContent());
                data.add(task_subject.getTextContent());
                data.add(task_action.getTextContent());
                
                /* setting mapping values */
                data.add(mapping_id.getTextContent());
                data.add(subject_score.getTextContent());
                data.add(action_score.getTextContent());
                data.add(accepted1.getTextContent().trim());
                
        }
        
    }
}
