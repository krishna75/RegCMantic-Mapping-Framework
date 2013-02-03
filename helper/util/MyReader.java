/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.util;

import gate.util.Out;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import rcm.main.Settings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import helper.util.Converter;
import helper.util.Print;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Krish
 */
public class MyReader implements Serializable {
    private static ArrayList<String> lineList;
    private static StringBuilder contents ;
    private String fileName = Settings.GATE_FILES_PATH + "class_list.txt";
/**
 *
 */
public MyReader() {
//        fileToArrayList(fileName);
//    fileToText(fileName);
//    Print.pr(contents.toString());
    //printMap();
}
public static ArrayList<Element> getXmlDomElements(String fileName, String elementName){
    ArrayList<Element> elList = new ArrayList<Element>();
    Document dom = getXmlDom(fileName);
    //get the root element
    Element docEle = dom.getDocumentElement();

    //get a nodelist of elements
    NodeList nl = docEle.getElementsByTagName(elementName);
    if(nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength();i++) {
                    //get theelement
                    Element el = (Element)nl.item(i);
                    elList.add(el);
            }
    }
    return elList;
}

public static ArrayList<Element> getElementsByTagName(Element element, String tagname){
    ArrayList<Element> elList = new ArrayList<Element>();


    //get a nodelist of elements
    NodeList nl = element.getElementsByTagName(tagname);
    if(nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength();i++) {
                    //get theelement
                    Element el = (Element)nl.item(i);
                    elList.add(el);
            }
    }
    return elList;
    
}

public static org.w3c.dom.Document getXmlDom(String fileName){
    Document dom = null;
    //get the factory
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    try {

                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();
    //parse using builder to get DOM representation of the XML file
    dom = db.parse(fileName);
        }catch(ParserConfigurationException pce) {
                pce.printStackTrace();
        }catch(SAXException se) {
                se.printStackTrace();
        }catch(IOException ioe) {
                ioe.printStackTrace();
        }

    return  dom;
}
/**
 *
 * @param fileName
 * @return
 */
public static ArrayList<String>  fileToArrayList(String fileName){
    lineList = new ArrayList<String>();
    File textFile = new File(fileName);
    BufferedReader input = null;
    try {
        input = new BufferedReader(new FileReader(textFile));
    } catch (FileNotFoundException ex) {
        ex.printStackTrace();
    }
        String line = null;
    try {
        while ((line = input.readLine()) != null) {
            if (RegEx.isValidString(line)){
                line = RegEx.removeComment(line);
                if (RegEx.isValidString(line)){
                    lineList.add(line);
                }
            }
        }
    } catch (IOException ex) {
       ex.printStackTrace();
    }
    return lineList;
}
/**
 *
 * @param fileName
 * @param rows
 * @param cols
 * @return
 */
public static Object[][] csv2Arrays(String fileName, int rows, int cols){
    BufferedReader bufRdr = null;
    String[][] data = new String[rows][cols];
    try {
        ArrayList<ArrayList> oList = new ArrayList<ArrayList>();

        File file = new File(fileName);
        bufRdr = new BufferedReader(new FileReader(file));
        String line = null;
        int row = 0;
        int col = 0;
        //read each line of text file
        while ((line = bufRdr.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ",");
            while (st.hasMoreTokens()) {
                //get next token and store it in the array
                data[row][col] = st.nextToken();
                col++;
            }
            row++;
        }
        //close the file
        bufRdr.close();
    } catch (IOException ex) {
        Logger.getLogger(MyReader.class.getName()).log(Level.SEVERE, null, ex);
    }

    return data;

}
/**
 *
 * @param fileName
 * @return
 */
public static String fileToText(String fileName){
    contents = new StringBuilder();
    File textFile = new File(fileName);
    BufferedReader input = null;
    try {
        input = new BufferedReader(new FileReader(textFile));
    } catch (FileNotFoundException ex) {
        ex.printStackTrace();
    }
    String line = null;
    try {
        while ((line = input.readLine()) != null) {
            contents.append(line);
//                contents.append(System.getProperty("line.separator"));
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    return contents.toString();
}
/**
 *  !!! did not work for some reason
 * @param filename
 */
public static void deleteFile(String filename){
            File file = new File("filename");
            file.delete();
}
/**
 *
 * @param fileName
 * @return
 */
public static Object fileToObject(String fileName){
    Object o = null;
    Out.pr("Reading object  ..........");
   try{
      //use buffering
      InputStream file = new FileInputStream( fileName );
      InputStream buffer = new BufferedInputStream( file );
      ObjectInput input = new ObjectInputStream ( buffer );
      try{
        //deserialize the List
        o = input.readObject();
        Out.prln(" ** object retrieved ** ");
      }
      finally{
        input.close();
      }
    }
    catch(ClassNotFoundException ex){
        Out.prln("Unsuccess !!!..." + ex);
        ex.printStackTrace();
    }
    catch(IOException ex){
        Out.prln("Unsuccess !!!..." + ex);
        ex.printStackTrace();
    }
    return o;
}
/**
 *
 * @return
 */
public  HashMap getMap(){
    return Converter.textLineToHashMap(lineList);
}



public static BufferedImage getImage(String fileName){
    BufferedImage image = null;
    try {
        image = ImageIO.read(new File(fileName));
    } catch (IOException ex) {
        ex.printStackTrace();
    }
   return image;
}











/**
 *
 * @return
 */
public String getFileName() {
    return fileName;
}
/**
 *
 * @param fileName
 */
public void setFileName(String fileName) {
    this.fileName = fileName;
}
/**
 *
 * @return
 */
public ArrayList<String> getLineList() {
    return lineList;
}
/**
 *
 * @param lineList
 */
public void setLineList(ArrayList<String> lineList) {
    this.lineList = lineList;
}
/**
 *
 */
public void printList(){
    Print.prlnArrayList(lineList);
}
/**
 *
 */
public void printMap(){
        Print.printMap(getMap());
    }


}
