

package api.xml;

import org.apache.xml.serialize.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;

/**
 *
 * @author Krishna Sapkota, 04-Aug-2011,   12:33:20
 * A PhD project at Oxford Brookes University
 *
 *  This class utilises the power of sax and helpers to create xml files.
 *  The intention of creating this file is to make the whole process simpler and tailored to our project.
 */
public class XmlWriter {
    /* name of the start element */
    private String startElement;

    /* name of the end element */
    private String endElement;

    /* description of the element or the text value contained in an element */
    private String characters;

    /* holds the attributes names and their values that applies to an element */
    private AttributesImpl attribute ;

    /* the output format as defined in the sax. It is needed to write the xml file */
    private OutputFormat outputFormat ;

    /* it holds all the xml document content */
    private ContentHandler content;

    /*  it writes the xml document in a file */
private FileOutputStream fileOutputStream =null;
/**
*  Constructor without filename
*/
public XmlWriter()  {
    init();
}
/**
 *  Constructor with file name
 * @param outputFilename is the name of the xml file to be created.
 */
public XmlWriter(String outputFilename){
    setFilename(outputFilename);
    init();
}
/**
*  Initialises the document creation process
*/
private void init(){
    try {
        attribute = new AttributesImpl();
        outputFormat = new OutputFormat("XML", "ISO-8859-1", true);
        outputFormat.setIndent(1);
        outputFormat.setIndenting(true);
        XMLSerializer serializer = new XMLSerializer(fileOutputStream, outputFormat);
        content = serializer.asContentHandler();
        content.startDocument();
    } catch (IOException ex) {
        ex.printStackTrace();
    } catch (SAXException ex) {
        ex.printStackTrace();
    }
}
/**
 *  specifies the name of the xml file to be created.
 * @param outputFilename is the name of the xml file.
 */
public void setFilename(String outputFilename) {
    try {
        fileOutputStream = new FileOutputStream(outputFilename);
    } catch (FileNotFoundException ex) {
       ex.printStackTrace();
    }
}
/**
 *  specifies the associated dtd file of the xml file.
 * @param dtdFilename is the name of the dtd file.
 */
public void setDtd(String dtdFilename) {
    outputFormat.setDoctype(null, dtdFilename);
}
/**
 *  adds attributes to the next/following element.
 *  note: it doesn't add the attributes to the previous element.
 * @param name of the attribute
 * @param value of the attribute
 */
public void addAttribute(String name, String value) {
    attribute.addAttribute("", "", name, "CDATA", value);
}
/**
 *  adds a start element to the content handler (xml content)
 * @param elementName is the name of the start element
 */
public void startElement(String elementName) {
    try {
        content.startElement("", "", elementName, attribute);
        attribute.clear();
    } catch (SAXException ex) {
        ex.printStackTrace();
    }
    attribute.clear();
}
/**
 *
 * @param text
 */
public void characters(String text){
    try {
        if (text== null){
            text = "null";
        }
        content.characters(text.toCharArray(), 0, text.length());
    } catch (SAXException ex) {
        ex.printStackTrace();
    }
}
/**
 *  adds an end element to the content handler (xml content)
 * @param elementName is the name of the end element
 */
public void endElement(String elementName){
    try {
        content.endElement("", "", elementName);
    } catch (SAXException ex) {
        ex.printStackTrace();
    }

}

public void addElement(String elementName, String value){
    startElement(elementName);
    characters(value);
    endElement(elementName);
}
/**
 *  It completes the creation of the xml file and writes in an xml file.
 */
public void write(){
        try {
            content.endDocument();
            fileOutputStream.close();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
