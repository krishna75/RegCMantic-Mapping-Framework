/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.*;
import rcm.main.Settings;
import net.didion.jwnl.JWNLException;
import api.ont.IOntology;
import api.ont.Jena_Ontology;
import helper.style.StyleBody;
import helper.util.MyReader;
import helper.util.Print;
import helper.util.Sorter;
import api.wn.Wordnet;

/**
 *
 * @author Krishna Sapkota on 18-Aug-2010 at 00:55:24
 */
public class Splitter {
    private String fileName = Settings.GATE_FILES_PATH + "class_list.txt";
    private ArrayList<String> splittedClassList = new ArrayList<String>();
    private IOntology ontpReg;
/**
 *
 */
public Splitter() {
    ontpReg = new Jena_Ontology(Settings.ONTOLOGY_PATHNAME,
            Settings.ONTOREG_FILENAME,
            Settings.ONTOREG_URI_PREFIX);
//        Print.prlnArrayList(listSplittedCamelCase("MyNameIsKrishnaSapkota"));
}
/**
 *
 * @param s
 * @return
 */
public static String splitCamelCase(String s) {
return s.replaceAll(
  String.format("%s|%s|%s",
     "(?<=[A-Z])(?=[A-Z][a-z])",
     "(?<=[^A-Z])(?=[A-Z])",
     "(?<=[A-Za-z])(?=[^A-Za-z])"
  ),
  " "
);
}
/**
 *
 * @param s
 * @return
 */
public static ArrayList<String> listSplittedCamelCase(String s){
    String word =splitCamelCase(s);

    String[] words = word.split(" ");
    return Converter.arrayToArrayList(words);
}
/**
 *
 * @return
 */
public ArrayList<String> getSplittedList() {
    ArrayList<String> splittedList = getSplittedList(ontpReg.listSubClasses("ValidationTask"));
    ArrayList<String> classNameList = MyReader.fileToArrayList(fileName);

    for (String className: classNameList){
        splittedList.addAll(getSplittedList(ontpReg.listIndividuals(className)));
        splittedList.addAll(getSplittedList(ontpReg.listSubClasses(className)));
    }

    splittedList = Sorter.getSortedUniqueList(splittedList);
    System.out.println("================= first sorted unique ================");
    Print.prlnArrayList(splittedList);

    // removing unwanted words like "be", "such" "of" etc.
    splittedList.removeAll(getUnwantedWordList());

    writeFile(splittedList);

    return splittedList;
}
/**
 *
 * @param notSplittedList
 * @return
 */
public ArrayList<String> getSplittedList(ArrayList<String> notSplittedList) {
    ArrayList<String> splittedList = new ArrayList<String>();

    // Split input with the pattern
    for (String str: notSplittedList){
        Pattern pattern = Pattern.compile("([A-Z][a-z]*)");
        Matcher matcher = pattern.matcher(str);

        // Check all occurance
        while (matcher.find()) {
            String word = matcher.group();

            // checking if abbreviations are used and replacing with full form
            word = getFullForm(word, getAbbrMap());

            // adding to the arraylist
            splittedList.add(word);
        }
    }
    return splittedList;
}
/**
 *
 * @param stringList
 * @return
 */
public ArrayList<String> removeSeperator(ArrayList<String> stringList){
    ArrayList<String> aList = new ArrayList<String>();
    for (String string: stringList){
        string = string.replace("_", " ");

        aList.add(string);
    }


    return aList;
}
/**
 *
 * @param wordList
 * @return
 */
public ArrayList<String> getSynsetList(ArrayList<String> wordList) {
ArrayList<String> synsetList = new ArrayList<String>();
Wordnet wn = new Wordnet();
for (String word: wordList){
    ArrayList<String> synList = wn.getAllList(word);
    for (String synWord: synList){
        synsetList.add(synWord);
    }
}
return synsetList;
}
/**
 *
 * @param abbr
 * @param abbrMap
 * @return
 */
public String getFullForm(String abbr, HashMap abbrMap){
    String fullForm;
    if (abbrMap.containsKey(abbr)){
        fullForm = (String) abbrMap.get(abbr);
    } else {
        fullForm = abbr;
    }
    return fullForm;
}
/**
 *
 * @return
 */
public HashMap getAbbrMap(){

    HashMap hm = new HashMap();
    hm.put("Q", "Quality");
    hm.put("A", "Assurance");
    hm.put("C", "Control");
    hm.put("T", "Tank");
    return hm;
}
/**
 *
 * @param aList
 */
public void writeFile(ArrayList<String> aList)  {
BufferedWriter writer = null;
try {
    writer = new BufferedWriter(new FileWriter(Settings.GAZETTEER_URL));
    for (String string: aList){
        //Start writing to the output stream
        writer.write(string);
        writer.newLine();
    }
} catch (FileNotFoundException ex) {
    ex.printStackTrace();
} catch (IOException ex) {
    ex.printStackTrace();
} finally {
    //Close the BufferedWriter
    try {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
}
/**
 *
 * @return
 */
public ArrayList<String> getUnwantedWordList(){
        ArrayList<String> aList = new ArrayList<String>();
        BufferedReader reader = null;
          try {
            reader = new BufferedReader(new FileReader(Settings.GAZETTEER_PATH+"unwantedwords.txt"));
            String string = null;
            while ((string =reader.readLine()) != null){
                System.out.println("unwanted word = "+string);
                aList.add(string);
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }          
        return aList;
    }
/**
 * 
 * @param bodyList
 * @return
 */
public static ArrayList<StyleBody> splitParagraphs(ArrayList<StyleBody> bodyList){
//    String aheadPattern = "[0-9]+[.][0-9]+";
//    String behindPattern ="[0-9]+[.][0-9]+";
    String aheadPattern = "[0-9]+[.][0-9]+";
    String behindPattern ="[a-z][.]";
    Print.prln("---------------- I am inside splitter");
    for (StyleBody body: bodyList){
        String structure = body.getStructure();
        if (structure.equals("paragraph")){
            String text = body.getText();
             Print.prln("---------------- I am inside paragraph");
             Print.prln(">> PARAGRAPH =" + text);
             ArrayList<String> resultList = RegEx.getExtraction(behindPattern, aheadPattern, text);
             for (String result: resultList){
                 Print.prln("---------------- SPLITTED PARAGRAPH");
                 Print.prln(result);
                 Print.prln("");
             }
        }
    }
    return bodyList;
}
}
