
package helper.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Krishna Sapkota, 21-Sep-2011,   17:26:00
 * A PhD project at Oxford Brookes University
 */
public class DOS {
public DOS() {

//    makeSemRegEmpty();
//    openSemReg();

}

/**
 *  It deletes the file and  replace the file with an empty file .
 *  It only works if there are two files if one is full and the other is empty.
 *  You will also need a batch file for actual dos commands execution.
 */
public static void makeSemRegEmpty(){
    String[] command =  new String[3];
    command[0] = "cmd ";
    command[1] = "/C";
    command[2] = "D:\\Dropbox\\Krishna\\NetBeansProjects\\mapping_files\\ontologies\\z_SemReg_v4_empty.bat";
    run(command);
}

/**
 *  It deletes the file and  replace the file with an empty file .
 *  It only works if there are two files if one is full and the other is empty.
 *  You will also need a batch file for actual dos commands execution.
 */
public static void makeOntoRegEmpty(){
    String[] command =  new String[3];
    command[0] = "cmd ";
    command[1] = "/C";
    command[2] = "D:\\Dropbox\\Krishna\\NetBeansProjects\\mapping_files\\ontologies\\z_MakeEmpty_OntoReg_v2.bat";
    run(command);
}

public static void openSemReg(){
    String[] command =  new String[3];
    command[0] = "cmd ";
    command[1] = "/C";
    command[2] = "D:\\Dropbox\\Krishna\\NetBeansProjects\\mapping_files\\ontologies\\SemReg_v4.owl";
    run(command);
}
public static void openOntoReg(){
    String[] command =  new String[3];
    command[0] = "cmd ";
    command[1] = "/C";
    command[2] = "D:\\Dropbox\\Krishna\\NetBeansProjects\\mapping_files\\ontologies\\OntoReg_v2.pprj";
    run(command);
}

public static void test(){
          String[] command =  new String[3];
          command[0] = "cmd";
          command[1] = "/C";
          command[2] = "del D:\\Dropbox\\Krishna\\NetBeansProjects\\mapping_files\\ontologies\\SemReg_v3.owl";
          run(command);
}

/**
 *  The DOS runtime execution command surrounded by exception handlers and the message readers
 * @param commands
 */
private static  void run(String[] commands){
    try {
        // the main and the only line required to run the dos command.
        Process p = Runtime.getRuntime().exec(commands);

        // reading the messege
        InputStreamReader inMessage = new InputStreamReader(p.getInputStream());
        BufferedReader stdInput = new BufferedReader(inMessage);
        // reading the error
        InputStreamReader inError = new InputStreamReader(p.getErrorStream());
        BufferedReader stdError = new BufferedReader(inError);

        // read the output from the command
        String s = null;
        Print.prln("Input messeges:\n");
        while ((s = stdInput.readLine()) != null) {
          Print.prln(s);
        }

        // read any errors  from the attempted command
        Print.prln("Errors (if any):\n");
        while ((s = stdError.readLine()) != null) {
          Print.prln(s);
        }
    } catch (IOException ex) {
        Logger.getLogger(DOS.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}

