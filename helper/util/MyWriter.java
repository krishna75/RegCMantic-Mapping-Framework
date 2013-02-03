
package helper.util;

import gate.util.Out;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;

/**
 *
 * @author Krish
 */
public class MyWriter implements Serializable {
/**
 *
 */
public MyWriter() {
//        write("Hi this is my test", Settings.FILES_PATH+"test1.txt");

}
/**
 *
 * @param content
 * @param fileName
 */
public static void write(String content, String fileName){

  BufferedWriter bufferedWriter = null;
    try {
        //Construct the BufferedWriter object
        bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        bufferedWriter.write(content);
    } catch (FileNotFoundException ex) {
        ex.printStackTrace();
    } catch (IOException ex) {
        ex.printStackTrace();
    } finally {
        //Close the BufferedWriter
        try {
            if (bufferedWriter != null) {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
/**
 *
 * @param textList
 * @param fileName
 */
public static void write(ArrayList<String> textList, String fileName){
    BufferedWriter bufferedWriter = null;
    try {
        //Construct the BufferedWriter object
        bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        int count = 1;
        for (String text: textList){
               if ( count > 1) {
                   bufferedWriter.newLine();
               }
               count ++;
               bufferedWriter.write(text);
                
        }

    } catch (FileNotFoundException ex) {
        ex.printStackTrace();
    } catch (IOException ex) {
        ex.printStackTrace();
    } finally {
        //Close the BufferedWriter
        try {
            if (bufferedWriter != null) {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
/**
 *
 * @param o
 * @param fileName
 */
public static void write(Object o, String fileName) {
       Out.pr("Saving object  ..........");
       try{
          //use buffering
          OutputStream file = new FileOutputStream( fileName );
          OutputStream buffer = new BufferedOutputStream( file );
          ObjectOutput output = new ObjectOutputStream( buffer );
          try{
            output.writeObject(o);
            Out.prln(" ** object saved ** ");
          }
          finally{
            output.close();
          }
        }
        catch(IOException ex){
          Out.prln("Unsuccess !!!..." + ex);
          ex.printStackTrace();
      }
     
    }
}
