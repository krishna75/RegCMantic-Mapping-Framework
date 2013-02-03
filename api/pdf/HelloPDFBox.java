/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.pdf;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rcm.main.Settings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.ExtractText;
import org.apache.pdfbox.util.PDFText2HTML;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;


/**
 *
 * @author Krishna Sapkota 21-Dec-2010  19:56:57
 */
public class HelloPDFBox {
    private String docName="a.pdf";

    public HelloPDFBox() {
        test2();
    }

    private void test1() throws IOException {
//        Document doc = LucenePDFDocument.getDocument(getURL(docName));
//        ArrayList list = (ArrayList) doc.getFields();
//        for (Object o: list){
//            System.out.println (o.toString());
//        }
        String filePathAndName = Settings.GATE_FILES_PATH+docName;
        File textFile = new File(filePathAndName);
        PDDocument doc=PDDocument.load(textFile);

        PDFTextStripper textStripper=new PDFTextStripper();
        System.out.println(textStripper.getText(doc));
        doc.close();

    }
    private void test2() {
        try {
            String encoding = "UTF-8";
            Writer output = null;
            String outputFile = "c:\\eu5_pdfbox.htm";
            PDDocument document = null;
            String ext = ".htm";

            URL url = getURL( "c:\\eu5.pdf" );
            document = PDDocument.load(url, true);
            String fileName = url.getFile();
                    
            PDFTextStripper stripper = new PDFText2HTML(encoding);
//     stripper.setForceParsing( true );
            stripper.setSortByPosition(true);
            stripper.setShouldSeparateByBeads(true);
            stripper.setStartPage(1);
            stripper.setEndPage(2);
            output = new OutputStreamWriter(new FileOutputStream( outputFile ), encoding );
            stripper.writeText(document, output);
        } catch (IOException ex) {
            Logger.getLogger(HelloPDFBox.class.getName()).log(Level.SEVERE, null, ex);
        }
}


    
/**
 *
 * @param fileName
 * @return
 * @throws java.net.MalformedURLException
 */
  private URL getURL(String fileName) throws MalformedURLException{
    String filePathAndName = fileName;
    File textFile = new File(filePathAndName);
    return textFile.toURI().toURL();
  }
}
