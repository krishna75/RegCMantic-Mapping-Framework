/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import helper.util.Print;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.xml.sax.helpers.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;
import rcm.main.Settings;


public class HelloiText {
        static StreamResult streamResult;
        static TransformerHandler handler;
        static AttributesImpl atts;

        public static void main(String[] args) throws IOException {

                try {
                    //Print.prln("result = " );
                        Document document = new Document();
                        document.open();
                        PdfReader reader = new PdfReader("c:/eu5.pdf");
                        PdfDictionary page = reader.getPageN(1);                        
                        PRIndirectReference objectReference = (PRIndirectReference) page.get(PdfName.CONTENTS);
                        PRStream stream = (PRStream) PdfReader.getPdfObject(objectReference);
                        byte[] streamBytes = PdfReader.getStreamBytes(stream);
                        PRTokeniser tokenizer = new PRTokeniser(streamBytes);
//Print.prln("result = "+ stream. )
                        //StringBuffer result = new StringBuffer();
                        //Print.prln("result = " );
                        //Print.prln("result = " +tokenizer.toString());
//                        while (tokenizer.nextToken()) {
//                   
//                            Print.prln( tokenizer.getTokenType().name());
//                                if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING) {
//                                        result.append(tokenizer.getStringValue());
//                                }
//                        }
//                        String test = result.toString();
//                        streamResult = new StreamResult( "c:\\eu5_itext.xml");
//                        initXML();
//                        process(test);
//                        closeXML();
//                        document.add(new Paragraph(".."));
//                        document.close();
                } catch (Exception e) {
                }
        }

        public static void initXML() throws ParserConfigurationException,
                        TransformerConfigurationException, SAXException {
                SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
                                .newInstance();

                handler = tf.newTransformerHandler();
                Transformer serializer = handler.getTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
                serializer.setOutputProperty(
                                "{http://xml.apache.org/xslt}indent-amount", "4");
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                handler.setResult(streamResult);
                handler.startDocument();
                atts = new AttributesImpl();
                handler.startElement("", "", "krish_document", atts);
        }

        public static void process(String s) throws SAXException {
                String[] elements = s.split("\\|");
                atts.clear();
                handler.startElement("", "", "Message", atts);
                handler.characters(elements[0].toCharArray(), 0, elements[0].length());
                handler.endElement("", "", "Message");
        }

        public static void closeXML() throws SAXException {
                handler.endElement("", "", "krish_document");
                handler.endDocument();
        }
}