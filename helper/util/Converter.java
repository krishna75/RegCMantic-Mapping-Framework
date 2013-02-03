/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;

/**
 * The purpose of this class is to covert, information from one from to another form in order to make the processes in the system
 * easier..
 * @author Krish
 */
public class Converter {
/**
 *  If a text line has "key=value" in each line, it seperates the value and  creates a hashmap.
 * @param textLineList is the list of text line which contains "key=value" information
 * @return a hash map with the key and value from the text line.
 */
public static HashMap textLineToHashMap(ArrayList<String> textLineList){
    HashMap map = new HashMap();
    for (String textLine: textLineList){
        String key = null;
        String value = null;
        String[] texts = textLine.split("=");
        for ( int i= 0; i<texts.length; i++){
            if (i==0){
                key = texts[i];
            }
            if (i==1){
                value = texts[i];
            }
        }
        map.put(key, value);
    }
    return map;
}

/**
 *  it obtains a sublist of an array list from a point (an index). The sublist is between the start of the list and the provided index.
 * @param aList is the list to be processed
 * @param i is the index in the list from where it will obtain a sublist.
 * @return a sublist from the list.
 */
public static ArrayList<String> getSubListBefore(ArrayList<String> aList, int i){
    ArrayList<String> newList = new ArrayList<String>();
   if (i< aList.size()){
       List list = aList.subList(0, i);
       for (Object o: list){
           newList.add((String)o);
       }
    }else {
        List list = aList.subList(0, aList.size());
        for (Object o: list){
           newList.add((String)o);
       }
    }
   return newList;
}
/**
 *  it obtains a sublist of an array list from a point (an index). The sublist is after the provided index until the end of the list.
 * @param aList is the list to be processed
 * @param i is the index in the list from where it will obtain a sublist.
 * @return a sublist from the list.
 */
public static ArrayList<String> getSubListAfter(ArrayList<String> aList, int i){
   ArrayList<String> newList = new ArrayList<String>();
   if (i< aList.size()){
       List list= aList.subList(i, aList.size()-1);
       for (Object o: list){
           newList.add((String)o);
       }
    }
   return newList;
}

/**
 * it converts an array to an array list
 * @param arr is the array to be converted
 * @return the converted array list
 */
public static ArrayList arrayToArrayList(Object[] arr){
    ArrayList newList = new ArrayList();
    for (int i=0;i<arr.length;i++){
        newList.add(arr[i]);
    }
    return newList;
}
public static String arrayListToString (ArrayList<String> textList){
    String newText = "";
    for (String text: textList){
        newText = newText+" "+text;
    }    
    return newText;
}
/**
 *  it converts an arraylist into an array
 * @param oList is the list to be converted into an array
 * @return an array
 */
public static Object[] arrayListToArray(ArrayList oList){
    Object[] oArr = new Object[oList.size()];
    int count = 0;
    for (Object o: oList){
        oArr[count++]= o;
    }
    return oArr;
}

public static ArrayList<String> listToArrayList(List<String> list){
    ArrayList<String> aList = new ArrayList<String>();
    for (String s: list){
        aList.add(s);
    }
    return aList;
}
/**
 * it converts a double array list to a double array ( two dimensional array)
 * @param doubleList a list of lists
 * @return the converted double array
 */
public static Object[][] doubleArrayListToDoubleArray(ArrayList<ArrayList> doubleList){
    Object[][] dArr = new Object[doubleList.size()][doubleList.get(0).size()];
    int i = 0;
    for (ArrayList singleList: doubleList){
        int j = 0;
        for (Object o: singleList){
            dArr[i][j]=o;
        }
    }

    return dArr;
}

/**
 *  it converts the words in a line into an array list.
 * @param line has a number of words
 * @param separater separates the words in a line. (e.g. comma, space etc)
 * @return a list of words from the line.
 */
public static ArrayList<String> lineToArrayList(String line, String separater ){
    return RegEx.getTokenizedList(line, separater);
}
/**
 * it will convert a list into flat line of string
 * @param textList is the list to be processed
 * @return a flat line of string
 */
public static String listToString(ArrayList<String> textList, String separater){
    String text2 ="";
    for (String text: textList){
        // only adds separater until the second last text
       if (textList.indexOf(text) < textList.size()-1){
        text2 +=  ( text+ separater);
        } else {
           text2 += text;
        }
    }
    return text2;
}
/**
* converts a file name to a url
* @param fileName to be converted into url
* @return url
*/
public static URL getURL(String fileName) {
    File textFile = new File(fileName);
    URL url = null;
        try {
            url = textFile.toURI().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
    return url;
  }
/**
 *  removes any other characters and numbers from a list
 * @param strList is the list to be refined
 * @return a refined list
 */
public static ArrayList<String> getPureLetters(ArrayList<String> strList){
    ArrayList<String> newList = new ArrayList<String>();
    for (String str: strList){
        if (RegEx.hasOnlyLetters(str)){
            newList.add(str);
        }
    }
    return newList;
}
public static ArrayList<String> remove(ArrayList<String> removeList,String[] stopwords ){
   ArrayList<String> stopList =  Converter.arrayToArrayList(stopwords);
   return remove(removeList, stopList);
}



/**
 *  removes unwanted words from a list
 * @param removeList is the list to be refined
 * @param unwantedWordList is the list of the words to be removed
 * @return a refined list
 */
public static ArrayList<String> remove(ArrayList<String> removeList, ArrayList<String> stopWordList){
    ArrayList<String> newList = new ArrayList<String>();
    for (String checkableWord: removeList){
        boolean found = false;
        for (Object stopWord : stopWordList){
            if (checkableWord.equalsIgnoreCase(stopWord.toString())){
                found = true;
            }
        }
        /* adds the word to the new list only if it's not in the list already */
        if (!found){
            newList.add(checkableWord);
        }
    }
    return newList;
}
/**
 *  removes an unwanted word) from a list
 * @param removeList is the list which will be refined
 * @param unwantedWord is th word to be removed from the list
 * @return a refined list
 */
public static ArrayList<String> remove(ArrayList<String> removeList, String stopWord){
    ArrayList<String> newList = new ArrayList<String>();
    for (String checkableWord: removeList){
        boolean found = false;
        if (checkableWord.equalsIgnoreCase(stopWord.toString())){
            found = true;
        }
        /* adds the word to the new list only if it's not in the list already */
        if (!found){
            newList.add(checkableWord);
        }
    }
    return newList;
}
/**
 *  It removes the duplicate strings from a list and makes the list entry unique.
 * @param aList is the list to be refined.
 * @return the refined  unique list of string.
 */
public static ArrayList<String>  makeUnique(ArrayList<String> aList){
    ArrayList<String> newList = new ArrayList<String>();
    int counter = 0;
    for (String a: aList){
        counter ++;
        if (a != null){
            /* make the first entry */
            if (counter == 1){
                newList.add(a);
            }
            boolean found = false;
            for (String a1 : newList){
                if (a.equalsIgnoreCase(a1)){
                    found = true;
                }
            }
            /* adds the word to the new list only if it's not in the list already */
            if (!found){
                newList.add(a);
            }
        }
    }
    return newList;
}

public static double roundDouble(double d, int noOfDecimalDigits){
    double d1=1;
    for (int i=1; i<=noOfDecimalDigits; i++){
        d1 = d1 * 10;
    }
    long l = (int)Math.round(d * d1); // truncates
    d = l / d1;
    return d;
}

}
