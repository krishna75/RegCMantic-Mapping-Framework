/*                |            @author Krishna Sapkota,  Mar 2, 2012                  |
 *                 |             A PhD project at Oxford Brookes University          |
 *                 | ________________________________________  | 
 */

package rcm.phase31.pre;

import api.xml.XmlWriter;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.Print;
import java.util.ArrayList;
import org.w3c.dom.Element;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota,  Jul 11, 2012,  5:11:30 PM
 * A PhD project at Oxford Brookes University
 */
public class DifferenceTable {
    
    /* IO Files*/
    String IN_DIFF_TABLE_FILE = Settings.PHASE3 + "/1_pre/difference_table.xml"; 
    String OUT_DIFF_TABLE_FILE = IN_DIFF_TABLE_FILE;
    
    /* Local Variables */
    ArrayList<DifferenceTable> differenceTableList = new ArrayList<DifferenceTable>();
    
    int counter = 0;
    double difference = 0.0;
    String termOne = "";
    String termTwo = "";
    
    
    

    public DifferenceTable() {
        
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public String getTermOne() {
        return termOne;
    }

    public void setTermOne(String termOne) {
        this.termOne = termOne;
    }

    public String getTermTwo() {
        return termTwo;
    }

    public void setTermTwo(String termTwo) {
        this.termTwo = termTwo;
    }

    public ArrayList<DifferenceTable> getDifferenceTableList() {
        return differenceTableList;
    }

    public void setDifferenceTableList(ArrayList<DifferenceTable> differenceTableList) {
        this.differenceTableList = differenceTableList;
    }

    public String getOutFile() {
        return OUT_DIFF_TABLE_FILE;
    }

    public void setOutFile(String outFile) {
        this.OUT_DIFF_TABLE_FILE = outFile;
    }
    
    
    public void add(DifferenceTable table){
        differenceTableList.add(table);
    }
    
    public double getDifference(String a1, String b1){
        double value = 0;
        for (DifferenceTable table: differenceTableList){
            String a2 = table.getTermOne();
            String b2 = table.getTermTwo();
            if (  (a1.equalsIgnoreCase(a2)& b1.equalsIgnoreCase(b2)) ||
                  (a1.equalsIgnoreCase(b2)& b1.equalsIgnoreCase(a2))){
                value = table.getDifference();
                break;
            }
        }
        return value;
    }
    
     public void generateId(){
        int count = 1;
        for (DifferenceTable table : this.getDifferenceTableList()){
            table.setCounter(count);
            count++;
        }
    }
    
    public void makeTableUnique(){
       //create new list
       ArrayList<DifferenceTable> newTableList = new ArrayList<DifferenceTable>();
       // for each table
       for (DifferenceTable table1: differenceTableList){
        // get  two terms
           String a1 = table1.getTermOne();
           String b1 = table1.getTermTwo();
           boolean found = false;
           for (DifferenceTable table2: newTableList){
               String a2 = table2.getTermOne();
               String b2 = table2.getTermTwo();
               if ( (a1.equalsIgnoreCase(a2) & b1.equalsIgnoreCase(b2)) ||
                    (a1.equalsIgnoreCase(b2) & b1.equalsIgnoreCase(a2))){
                    found = true;
               }
           }
           if (!found){
               newTableList.add(table1);
           }
       }
       // old list = new list
       differenceTableList = newTableList;
   }
 
/**
     *  reads an XML file and updates(populates) the properties
     */
    public void readFile(){
        Print.prln("DifferenceTable: reading difference table a from file ...");
        ArrayList<Element> rowElementList = MyReader.getXmlDomElements(IN_DIFF_TABLE_FILE, "row");
        differenceTableList.clear();
        for (Element rowElement: rowElementList ){
            DifferenceTable table = new DifferenceTable();
            differenceTableList.add(table);

            /* reading  regulation values */ 
            Element count = (Element)rowElement.getElementsByTagName("counter").item(0);
            Element one = (Element)rowElement.getElementsByTagName("word_one").item(0);
            Element two = (Element)rowElement.getElementsByTagName("word_two").item(0);
            Element diff = (Element)rowElement.getElementsByTagName("difference").item(0);
            
            table.setCounter(Integer.parseInt(count.getTextContent()));
            table.setTermOne(one.getTextContent());
            table.setTermTwo(two.getTextContent());
            table.setDifference(Double.parseDouble(diff.getTextContent()));                           
        }        
        Print.prln("size = " + differenceTableList.size());
    }    
    
    
    
    
    
    /**
     *  writes an xml file and dumps its properties
     */
    public void writeFile(){
        Print.prln("DifferenceTable: writing difference table to a file ...");
        XmlWriter xml = new XmlWriter(OUT_DIFF_TABLE_FILE);
        xml.addAttribute("size", String.valueOf(differenceTableList.size()));
        xml.startElement("table");
        for (DifferenceTable table: differenceTableList){

           /* collecting values  */
           int count    = table.getCounter();
           String one  = table.getTermOne();
           String two  = table.getTermTwo();
           double diff = table.getDifference();
           
           /* dumping values into xml tags */
            xml.startElement("row");
                xml.addElement("counter",String.valueOf(count));
                xml.addElement("word_one",one);
                xml.addElement("word_two",two);
                xml.addElement("difference",String.valueOf(diff));     
            xml.endElement("row");
        
        }
        xml.endElement("table");
        xml.write(); 
    }
    



}
