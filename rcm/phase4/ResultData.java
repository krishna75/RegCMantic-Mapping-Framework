/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rcm.phase4;

import api.xml.XmlWriter;
import helper.util.MyReader;
import helper.util.Print;
import java.util.ArrayList;
import rcm.main.Settings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Krishna Sapkota,  Jan 27, 2012,  5:53:29 AM
 * A PhD project at Oxford Brookes University
 */
public class ResultData {
    private String resultId;
    private boolean complied;
    private String regulation;
    private String statement;
    private String task;
    private ArrayList<String> employeeList = new ArrayList<String>();   
    private boolean completed;
    private String report;
    private boolean reportDetailOK; // it needs to go through every detail 
    private String remark;
    private ArrayList<ResultData> resultDataList = new ArrayList<ResultData>();
    // files related
    private String XML_FILENAME1 = Settings.FILES_PATH + "ComplianceResult.xml";
    private String XML_FILENAME2 = Settings.FILES_PATH + "ComplianceResult2.xml";

    public ResultData() {
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isComplied() {
        return complied;
    }

    public void setComplied(boolean complied) {
        this.complied = complied;
    }

    public ArrayList<String> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(ArrayList<String> employeeList) {
        this.employeeList = employeeList;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public boolean isReportDetailOK() {
        return reportDetailOK;
    }

    public void setReportDetailOK(boolean reportDetailOK) {
        this.reportDetailOK = reportDetailOK;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    
    public ArrayList<ResultData> getResultDataList() {
        return resultDataList;
    }

    public void setResultDataList(ArrayList<ResultData> resultDataList) {
        this.resultDataList = resultDataList;
    }
        
    public void addData(ResultData data){
       this.resultDataList.add(data);
    }
    
    public ResultData getDataById(String id){
        ResultData newData = null;
        for ( ResultData data: resultDataList){
            if (id.equals(data.getResultId())){
               newData = data;
            }
        }
        return newData;
   }
    
 /**
     *  reads an xml file and populates the list of result data.
     */
    public void readFile(){
        Print.prln("ResultData: reading result data from a file ...");
        ArrayList<Element> elementList = MyReader.getXmlDomElements(XML_FILENAME1, "result");
        resultDataList.clear();
        for (Element element: elementList ){
            ResultData data = new ResultData();
            resultDataList.add(data);

            /* reading  element values */ 
            Element result_id = (Element)element.getElementsByTagName("result_id").item(0);
            data.setResultId(result_id.getTextContent());
            Element complied = (Element)element.getElementsByTagName("complied").item(0);
            if (complied.getTextContent().toLowerCase().equals("true")){
               data.setComplied(true); 
            } else {
               data.setComplied(false); 
            }
            Element regulation = (Element)element.getElementsByTagName("regulation").item(0); 
            data.setRegulation(regulation.getTextContent());
            Element statement = (Element)element.getElementsByTagName("statement").item(0);
            data.setStatement(statement.getTextContent());
            Element task = (Element)element.getElementsByTagName("task").item(0);
            data.setTask(task.getTextContent());
            Element employees = (Element)element.getElementsByTagName("employees").item(0);
                NodeList employeeNodeList = employees.getElementsByTagName("employee");
                ArrayList<String> mployeeList1 = new ArrayList<String>();
                for (int i=0; i<employeeNodeList.getLength(); i++){
                    Element employee = (Element)employeeNodeList.item(i);
                    mployeeList1.add(employee.getTextContent());
                }
                data.setEmployeeList(mployeeList1);
            Element completed = (Element)element.getElementsByTagName("completed").item(0);     
            if (completed.getTextContent().toLowerCase().equals("true")){
                data.setCompleted(true);
            } else {
                data.setCompleted(false);
            }
            Element report = (Element)element.getElementsByTagName("report").item(0);
            data.setReport(report.getTextContent());
            Element report_detail = (Element)element.getElementsByTagName("report_detail").item(0);
            if (report_detail.getTextContent().toLowerCase().equals("true")){
                data.setReportDetailOK(true);
            } else {
                data.setReportDetailOK(false);
            }
            Element remark = (Element)element.getElementsByTagName("remark").item(0);
            data.setRemark(remark.getTextContent());
                
        }
    }
    
 /**
     *  writes an xml file and dumps its properties
     */
    public void writeFile(){
        Print.prln("ResultData: writing result data to a file ...");
        XmlWriter xml = new XmlWriter(XML_FILENAME2);
        xml.startElement("result_data");
        for ( ResultData rd: resultDataList){

           /* getting values  */
           String result_id    = rd.getResultId();
           boolean complied1  = rd.isComplied();
           String regulation1   = rd.getRegulation();
           String statement1 = rd.getStatement();
           String task1 = rd.getTask();
           ArrayList<String> employeeList1 = rd.getEmployeeList();
           boolean completed1 = rd.isCompleted();
           String report1 = rd.getReport();
           boolean report_detail = rd.isReportDetailOK();
           String remark1 = rd.getRemark();

        // dumping values to xml   
        xml.startElement("result");
            // result id
            xml.startElement("result_id");
                xml.characters(result_id);
            xml.endElement("result_id");
            // complied
            xml.startElement("complied");
                xml.characters(String.valueOf(complied1));
            xml.endElement("complied");
            //regulation
            xml.startElement("regulation");
                xml.characters(regulation1);
            xml.endElement("regulation");
            // statement
             xml.startElement("statement");
                xml.characters(statement1);
            xml.endElement("statement");
            // task
            xml.startElement("task");
                xml.characters(task1);
            xml.endElement("task");
            // employee
            xml.startElement("employees");
            for (String emp: employeeList1){
                xml.startElement("employee");
                xml.characters(emp);
            xml.endElement("employee");
            }
            xml.endElement("employees");
            // completed
            xml.startElement("completed");
                xml.characters(String.valueOf(completed1));
            xml.endElement("completed");
            // report
            xml.startElement("report");
                xml.characters(report1);
            xml.endElement("report");
            // report detail
            xml.startElement("report_detail");
                xml.characters(String.valueOf(report_detail));
            xml.endElement("report_detail");
            // remark
            xml.startElement("remark");
                xml.characters(remark1);
            xml.endElement("remark");
        xml.endElement("result");
        
        }
        xml.endElement("mapping_data");
        xml.write(); 
    }
    
    
}
