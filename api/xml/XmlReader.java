/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.xml;


import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import java.io.IOException;

/**
 *
 * @author Krishna Sapkota,  Mar 2, 2012,  3:40:48 AM
 * A PhD project at Oxford Brookes University
 */
public class XmlReader {

    public XmlReader() {
    }
    
    public  ArrayList<Element> getXmlDomElements(String fileName, String elementName){
        ArrayList<Element> elementList = new ArrayList<Element>();
        Document dom = getXmlDom(fileName);
        Element rootElement = dom.getDocumentElement();
        NodeList nodeList = rootElement.getElementsByTagName(elementName);
        if(nodeList != null && nodeList.getLength() > 0) {
                for(int i = 0 ; i < nodeList.getLength();i++) {
                        Element element = (Element)nodeList.item(i);
                        elementList.add(element);
                }
        }
        return elementList;
}
    
    public Document getXmlDom(String fileName){
        Document dom = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            dom = builder.parse(fileName);
            }catch(ParserConfigurationException pcEx) {
                    pcEx.printStackTrace();
            }catch(SAXException saxEx) {
                    saxEx.printStackTrace();
            }catch(IOException ioEx) {
                    ioEx.printStackTrace();
            }

    return  dom;
}

}
