
/**
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.jpedal.org
 * (C) Copyright 1997-2011, IDRsolutions and Contributors.
 *
 *   This file is part of JPedal
 *
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 *
 * ---------------
 * ExtractPagesAsHTML.java
 * ---------------
 */
package api.pdf;

import org.jpedal.PdfDecoder;
import org.jpedal.external.Options;
import org.jpedal.fonts.FontMappings;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.output.OutputDisplay;
import org.jpedal.render.output.OutputImageController;
import org.jpedal.render.output.html.HTMLDisplay;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import org.jpedal.examples.html.HTMLFontMapper;

/**
 *
 * This example opens a pdf file and extracts the HTML version of each page
 */
public class HelloJPedal {

  /**flag to show if we print messages*/
  public static boolean outputMessages = false;

  String output_dir="c:/jpedal/";;

  /**correct separator for OS */
  String separator = System.getProperty("file.separator");

  /**the decoder object which decodes the pdf and returns a data object*/
  PdfDecoder decode_pdf = null;

  /**sample file which can be setup - substitute your own.
   * If a directory is given, all the files in the directory will be processed*/
  private String test_file = "c:/eu5.pdf";

  /**used as part of test to limit pages to first 10*/
  public static boolean isTest=false;


    public HelloJPedal() {
      String params[]= new String[3];
      params[0] = "c:/eu5.pdf";
      params[1] = "c:/jpedal";
      params[2] = "";
      decodeFile(params, output_dir);
    }

  /**
   * routine to decode a file
   */
  private void decodeFile(String[] args,String output_dir) {
    String file_name = setParams(args);
    //create a directory if it doesn't exist
    File output_path = new File(output_dir);
    if (output_path.exists() == false){
      output_path.mkdirs();
    }
    try {
      decode_pdf = new PdfDecoder(true);
      decode_pdf.openPdfFile(file_name);
    } catch (Exception e) {
        System.err.println("8.Exception " + e + " in pdf code in "+file_name);
    }
    extractPageAsHTML(file_name, output_dir);
//    decode_pdf.closePdfFile();
  }

  private void extractPageAsHTML(String file_name, String output_dir) {
        
    int start = 1,  end = decode_pdf.getPageCount();
    try {
      HTMLFontMapper.setXMLTemplate(false);
      for (int page = start; page < end + 1; page++) { //read pages
        String pageAsString=String.valueOf(page);
        String maxPageSize=String.valueOf(end);
        int padding=maxPageSize.length()-pageAsString.length();
        for(int ii=0;ii<padding;ii++)
            pageAsString='0'+pageAsString;
        if (outputMessages)
          System.out.println("Page " + pageAsString);

        String outputName = pageAsString;
        outputName=StringUtils.makeHTMLNameSafe(outputName.toLowerCase());

        /* custom object so we write out to HTML for Form and PDF flat display   */
        int cropX = decode_pdf.getPdfPageData().getCropBoxX(page);
        int cropY = decode_pdf.getPdfPageData().getCropBoxY(page);
        int cropW = decode_pdf.getPdfPageData().getCropBoxWidth(page);
        int cropH = decode_pdf.getPdfPageData().getCropBoxHeight(page);
        //Create Rectangle object to match width and height of cropbox
        Rectangle cropBox = new Rectangle(0, 0, cropW, cropH);
        //Find middle of cropbox in Pdf Coordinates
        Point2D midPoint = new Point2D.Double((cropW / 2) + cropX, (cropH / 2) + cropY);
        DynamicVectorRenderer HTMLoutput=new HTMLDisplay(page, midPoint, cropBox ,false, 100, new ObjectStore());
        //have a scaling factor so we can alter the page size
        float scaling=1.0f;
        HTMLoutput.setValue(HTMLDisplay.PercentageScaling, (int) (scaling*100)); //set page scaling (default is 100%)
        HTMLoutput.writeCustom(HTMLDisplay.PAGEDATA,decode_pdf.getPdfPageData()); //pass in PageData object so we c
        HTMLoutput.setValue(HTMLDisplay.MaxNumberOfDecimalPlaces, 0);   //let use select max number of decimal places
        HTMLoutput.setOutputDir(output_dir,outputName,pageAsString); //root for output
        HTMLoutput.setBooleanValue(OutputDisplay.AddNavBar, true);
        decode_pdf.addExternalHandler(HTMLoutput, Options.CustomOutput); //custom object to draw PDF
        HTMLoutput.writeCustom(HTMLDisplay.SET_ENCODING_USED, new String[]{"UTF-16","utf-16"}); //java/html string value
        HTMLoutput.setValue(HTMLDisplay.FontMode, HTMLFontMapper.EMBED_ALL);
        /* get the current page as HTML */
        decode_pdf.decodePage(page);
      }
    } catch (Exception e) {
      decode_pdf.closePdfFile();
      throw new RuntimeException("Exception " + e.getMessage()+" with thumbnails on File="+file_name);
    }

  }


  private String setParams(String[] args) {
    //set to default
    String file_name = test_file;

    //check user has passed us a filename and use default if none
    int len=args.length;
    if (len == 0){
//      showCommandLineValues();
    }else if(len == 1){
      file_name = args[0];
    }else if(len <6){

      //input
      file_name = args[0];
      output_dir=args[1];       
    }
    return file_name;
  }

}
 