
package api.xml;

import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota, 11-Aug-2011,   21:53:40
 * A PhD project at Oxford Brookes University
 */
public class XmlTester {

    String filename = Settings.FILES_PATH + "xml_writer_2.xml";
    XmlWriter xml ;
    public XmlTester() {
        xml = new XmlWriter(filename);
        xml.startElement("family");
        
            // attributes are added in later/following element
            xml.addAttribute("sex", "male");
            xml.addAttribute("age", "35");
            xml.startElement("member");
            xml.characters("Krishna Sapkota");
            xml.startElement("test");
            xml.characters("test description");
            xml.endElement("test");
            xml.endElement("member");
            xml.addAttribute("sex", "female");
            xml.addAttribute("age", "30");
            xml.startElement("member");
            xml.characters("Bindu Sapkota");
            xml.endElement("member");
        xml.endElement("family");
        xml.write();
    }




}
