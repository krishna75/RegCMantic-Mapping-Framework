/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.xml;

import helper.util.MyWriter;
import java.util.ArrayList;


/**
 *
 * @author Krishna Sapkota,  Mar 2, 2012,  3:55:55 AM
 * A PhD project at Oxford Brookes University
 */
public abstract class Abstract_ConvertToCsv {
    ArrayList<String> columnList;
    ArrayList<String> data; 
    ArrayList< ArrayList<String>> dataList; 
    String xmlFile;
    String csvFile;
    XmlReader reader;

    public Abstract_ConvertToCsv() {
    }

    public void writeCSV(){
        String content = "";
        // write column lists 
        for (Object col: columnList){
            content += wrap(col.toString());  
        }
        content += "\n";
        // for each row
        for (ArrayList<String> row: dataList){
            for (String value: row){
                content += wrap(value);
            }
            content += "\n";
        }
        // write 
        MyWriter.write(content, csvFile);
    }
    
    private String wrap(String text){
        String wrapper = "\"";
        String separator = ",";
        String text1 = wrapper + text + wrapper + separator;
        return text1;
    }
    
}
