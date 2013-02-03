
package api.pdf;

import helper.util.Print;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.render.output.html.HTMLDisplay;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.io.PdfReader;
import org.w3c.dom.Document;


/**
 *
 * @author Krishna Sapkota, 19-Dec-2011,   08:24:30
 * A PhD project at Oxford Brookes University
 */
public class HtmlDisplay {

    public HtmlDisplay() {
    useJPedal();
     
}
    private void useIText(){
//        com.itextpdf.text.Document d = new com.itextpdf.text.Document(new File(""));
    }
    private void useJPedal(){
           PdfDecoder d = new PdfDecoder();
        //DynamicVectorRenderer out = new HTMLouput();
        try {
            d.openPdfFile("c:\\eu.pdf");
        } catch (PdfException ex) {
            ex.printStackTrace();
        }
        try {

            d.decodePage(46);
           int count = d.getPdfData().getRawTextElementCount();

            Print.prln("total elements="+count);
           Print.printArray(d.getFileInformationData().getFieldValues());
           
//            String[] str=d.getPdfData().contents;
//            Print.printArray(str);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Document doc = d.getOutlineAsXML();
        // This method writes a DOM document to a file
 String filename = "C:\\pdftoxml.xml";
    try {
        // Prepare the DOM document for writing
        Source source = new DOMSource(doc);

        // Prepare the output file
        File file = new File(filename);
        Result result = new StreamResult(file);

        // Write the DOM document to the file
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, result);
    } catch (TransformerConfigurationException e) {
    } catch (TransformerException e) {
    }
    }



}
