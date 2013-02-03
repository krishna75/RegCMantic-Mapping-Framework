/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.util;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rcm.main.Settings;
import helper.util.MyReader;
import helper.util.Print;
import java.math.BigDecimal;

/**
 * This class is designed to utilise the java regex related  methods with ease and convenience.
 * @author Krish
 */
public class RegEx {
/**
 *  The empty constructor
 */
public RegEx() {
 Print.prln("formated value = " + formatDecimal(5.678954, 5));
//        System.out.println(replace("a123cat&dog are 59` good"));
//        extract();
//        test();
//        testNWords();
    }
/**
 * It replaces words concatenated by space, control or any other symbols with underscore "_"
 * @param text is the words to be concatenated by underscore
 * @return the concatenated words.
 */
public String replace(String text){
    String concatWord = "";

    /*  for each character in the text */
    for (int i = 0; i< text.length(); i++){
       Character ch = text.charAt(i);

       /*  if it is letter just add it to the string otherwise (if space)  add the underscore */
       if (ch.isLetter(ch)){
           concatWord +=ch;
       }else{
           concatWord +="_";
       }
    }
    return concatWord;
}
/** redundant, for testing purpose only */
private void extract(){
        String aheadPattern = "[0-9]+[.][0-9]+";
        String behindPattern ="[0-9]+[.][0-9]+";
        String text = ("General "
                + "5.1 Production should be performed and supervised by competent people."
                + "5.2 All handling of materials and products, such as receipt and quarantine, sampling, storage,"
                + "labelling, dispensing, processing, packaging and distribution should be done in accordance"
                + "with written procedures or instructions and, where necessary, recorded."
                + "5.3 All incoming materials should be checked to ensure that the consignment corresponds to"
                + "the order. Containers should be cleaned where necessary and labelled with the prescribed"
                + "data."
                + "5.4 Damage to containers and any other problem which might adversely affect the quality of a"
                + "material should be investigated, recorded and reported to the Quality Control Department."
                + "5.5 Incoming materials and finished products should be physically or administratively"
                + "quarantined immediately after receipt or processing, until they have been released for use"
                + "or distribution."
                + "5.6 Intermediate and bulk products purchased as such should be handled on receipt as though"
                + "they were starting materials."
                + "5.7 All materials and products should be stored under the appropriate conditions established"
                + "by the manufacturer and in an orderly fashion to permit batch segregation and stock"
                + "rotation."
                + "5.8 Checks on yields, and reconciliation of quantities, should be carried out as necessary to"
                + "ensure that there are no discrepancies outside acceptable limits."
                + "5.9 Operations on different products should not be carried out simultaneously or consecutively"
                + "in the same room unless there is no risk of mix-up or cross-contamination."
                + "5.10 At every stage of processing, products and materials should be protected from microbial and"
                + "other contamination.");
         ArrayList<String> resultList = getExtraction(behindPattern, aheadPattern, text);
         for (String result: resultList){
             Print.prln(result);
         }
        
        }
/**
 *  extracts information provided a list of preceeding pattern and a list of following pattern and a text file for
 *  for the text content
 * @param behindPattern the list of preceeding pattern
 * @param aheadPattern the list of following pattern
 * @param fileName name of the file where the text content is.
 * @return the extracted information
 */
 public static ArrayList<String> getExtractionFromFile(ArrayList<String> behindPattern, ArrayList<String> aheadPattern, String fileName) {
     return getExtraction(behindPattern, aheadPattern, MyReader.fileToText(fileName));
 }
/**
 *  extracts information provided a list of preceeding pattern and a list of following patterns
 * @param behindPatternList the list of preceeding pattern
 * @param aheadPatternList the list of following pattern.
 * @param text is th original text where the information is looked for
 * @return the extracted information
 */
public static ArrayList<String> getExtraction(ArrayList<String> behindPatternList, ArrayList<String> aheadPatternList, String text)   {

    /*  the list to hold the result  */
    ArrayList<String> aList = new ArrayList<String>();

    /*  search the required text between  each pre and post pattern in the list */
    for (String behindPattern: behindPatternList){
        for (String aheadPattern: aheadPatternList){
             aList.addAll( getExtraction(behindPattern, aheadPattern, text));
        }
    }
    return aList ;
}
/**
 *  extracts information provided the patterns for preceeding and following text
 * @param behindPattern the pattern before the searched text
 * @param aheadPattern the pattern after the searched text
 * @param text the original text where the searched text is being looked for
 * @return the extracted information
 */
public static ArrayList<String> getExtraction(String behindPattern, String aheadPattern, String text){

    /*  the list to hold the extracted text  */
    ArrayList<String> aList = new ArrayList<String>();

    /*  the patterns and matchers for the ahead and behind text */
    Pattern pAhead      = Pattern.compile(aheadPattern);
    Pattern pBehind     = Pattern.compile(behindPattern);
    Matcher mAhead      = pAhead.matcher(text);
    Matcher mBehind     = pBehind.matcher(text);

    /*  the look ahead text should go one step forward as compared to the look behind  */
    mAhead.find();

    /*  searches through every look ahead and look behind */
    while (mBehind.find()){
        String xBehind = mBehind.group();
        if (mAhead.find()){
            String xAhead = mAhead.group();

            /* represents any text of any length */
            String pattern = ".*";

            /*  these are the concrete implementation of look ahead and look behind */
            String lookBehind = "(?="+xBehind+")";
            String lookAhead = "(?<="+xAhead+")";
            String regex = lookBehind + pattern + lookAhead;

            /*  the core pattern and matcher */
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(text);

            /* findsthe required text  */
            while (m.find()){
                String s = m.group();
                if (!s.equals("")){

                    /*  removes the pre and post patterns  */
                    //s= s.replaceAll(xBehind, "");
                    s= s.replaceAll(xAhead, "");

                    /*  adds to the result list */
                    aList.add(s);
                }
            }
        }
    }
    return aList;
}
/**
 *  Given a list of pattern and a list of text, it counts the no of  occurrences in the texts
 * @param patternList is the list of pattern to be matched
 * @param textList where to find the pattern
 * @return the no of pattern match found in the texts
 */
public static int countPattern (ArrayList<String> patternList, ArrayList<String> textList){
    int count = 0;
    for (String text: textList){
        count += countPattern(patternList, text);
    }
    return count;
}
/**
 *  Given a pattern and a list of  text, it counts the no of  occurrences in the texts
 * @param patternList is the list of pattern to be matched
 * @param text where to find the pattern
 * @return the no of pattern match found in the texts
 */
public static int countPattern (ArrayList<String> patternList, String text){
    int count = 0;
    for (String pattern: patternList){
        count += countPattern(pattern, text);
    }
    return count;
}
/**
 *  Given a pattern and a text, it counts the no of  occurrences in the text
 * @param pattern is the pattern to be matched
 * @param text where to find the pattern
 * @return the no of pattern match found in the text
 */
public static int countPattern (String pattern, String text){
    
    /*  Pattern and Matcher are the two important classes in regex */
    Pattern p = Pattern.compile(pattern.toLowerCase());
    Matcher matcher = p.matcher(text.toLowerCase());
    int count = 0;

    /*  counts if a match is found */
    while (matcher.find()) {
        count++;
    }
    return count;
}
/**
 *  It returns the specified no of words beginning the text in a flat string format
 * @param text where to find the words
 * @param n the no of words to be returned
 * @return  a string of concatenated specified no of words
 */
public static String getFirstNWordsFlat(String text, int n){
    String firstWord = "";
    String[] fwArr = getFirstNWords(text, n);

    /* concatenating the strings in an array */
    for (int i=0;i<fwArr.length;i++){
        firstWord += fwArr[i]+ " ";
    }
    return firstWord;
}
/**
 *  It returns the specified no of words beginning the text in an array
 * @param text where to find the words
 * @param noOfWords the no of words to be returned
 * @return  an array of specified no of words
 */
public static String[] getFirstNWords(String text, int noOfWords){
    String[] firstWordArr = new String[noOfWords];

    /* specifies an array of words split by spaces between them  */
    String[] arr = text.split("\\s+");

    /* selecting only the specified no of words from the start of the array */
    for (int i=0;i<noOfWords;i++){
        firstWordArr[i]= arr[i];
    }
    return firstWordArr;
}

public static ArrayList<String> getTokenizedList(String line, String separator){
        ArrayList<String> aList = new ArrayList<String>();
	StringTokenizer st = new StringTokenizer(line,separator);
	while (st.hasMoreTokens())
	{
		aList.add(st.nextToken());
	}
        return aList;
}
public static String getTokenizedFlat(String line, String separator){
        return Converter.arrayListToString(getTokenizedList(line, separator));
}

private void test(){
    String aheadPattern = "[0-9][.][0-9]";
    String behindPattern ="[0-9][.][0-9]";
    ArrayList<String> aList = new ArrayList<String>();
    ArrayList<String> bList = new ArrayList<String>();
    aList.add(aheadPattern);
    bList.add(aheadPattern);
    String fileName =Settings.GATE_FILES_PATH+"abcd.txt";
    
    ArrayList<String> resultList = getExtractionFromFile(bList, aList, fileName);
         for (String result: resultList){
             Print.prln(result);
         }
}
private void testNWords(){
    String text = "The quick brown fox jumped in to the river.";
    getFirstNWordsFlat(text, 1);
}

public static boolean hasOnlyLetters(String str){
    return (str.matches("^[a-zA-Z]+$"))  ;
}

public static String removeSentencePrefix(String sen){
    String prefix ="";
    char[] arr = sen.toCharArray();
    
    for ( int i = 0; i<arr.length; i++){
        char c = arr[i];
        if (Character.isUpperCase(c)){
            break;
        }
        prefix += Character.toString(c);    
    } 
    sen = sen.substring(prefix.length()-1);
//    if (prefix != " "){
//        sen = sen.replace(prefix, "");
//    }
    sen = sen.replaceAll("\n", " ");  
    sen = sen.replaceAll("\r", " "); 
    return sen;
}

public static String removeOtherCharacters(String str){
     String newString ="";
    for (int i = 0 ; i<str.length(); i++){
        char c = str.charAt(i);
        if (!Character.isLetter(c)){
            newString = newString + " ";
        }else {
            newString = newString + String.valueOf(c);
        }
    }
    return newString;
}
public static String removeQuote(String str){
    String newString = "";
    for (int i = 0 ; i<str.length(); i++){
        char c = str.charAt(i);
        if (Character.toString(c).equalsIgnoreCase("\"")){
            newString = newString + "";
        }else {
            newString = newString + String.valueOf(c);
        }
    }
    return newString;
}
public static String splitCamelcase(String phrase){
    String newString  ="";
    boolean lastUpper = false;
    if (!allUppercase(phrase)){
        for (int i = 0 ; i<phrase.length(); i++){
            char c = phrase.charAt(i);
            if (Character.isUpperCase(c)){
                if (lastUpper){
                    newString = newString+ Character.toString(c);
                }else{
                newString = newString + " "+ Character.toString(c);
                }
                lastUpper = true;
            }else {
                newString = newString + String.valueOf(c);
                lastUpper = false;
            }
            // consecutive upper case should not be separated
        }
    }else {
        newString = phrase.toLowerCase();
    }    
    newString  = newString.toLowerCase().trim();
    return newString;
}

public static boolean allUppercase(String phrase){
    boolean found = true;
    for (int i = 0 ; i<phrase.length(); i++){
            char c = phrase.charAt(i);
            if (Character.isLowerCase(c)){
               found = false;
               break;
            }
    }    
    return found;
}

/**
 * checks if any of the words in one list matches with any of the words in the other list.
 * @param xList is a list to be matched
 * @param yList is the other list to be matched with.
 * @return a boolean value of the result.
 */
public static boolean listsMatched(ArrayList<String> xList, ArrayList<String> yList){
    /* represents whether the match found   */
    boolean found = false;
    /*   each string in the first list is checked with each string in the second list */
    for (String x: xList){
           if (isInList(x, yList)){
               found = true;
               break;
           }
    }
    return found;
}

/**
 *  checks if a word is in a list
 * @param xList is the list where the word will be checked with.
 * @param y is the word to be checked
 * @return a boolean value of the result.
 */
public static boolean isInList (ArrayList<String> xList, String y){
    return isInList( y, xList);
}

/**
 *  checks if a word is in a list
 * @param x is the word to be checked
 * @param yList is the list where the word will be checked with.
 * @return a boolean value of the result.
 */
public static boolean isInList(String x, ArrayList<String> yList){
    /* represents whether the match found   */
    boolean found = false;
    x = x.trim();
    for (String y: yList){
        y = y.trim();
        if (Validator.isValidString(x) & Validator.isValidString(y)){
            if (x.equalsIgnoreCase(y)){
                found=true ;
                break ;
            }
        }
    }
    return found;
}

public static boolean  isValidString(String s){
    boolean valid = true;
    if (s == null || s.isEmpty() || s.trim().equals("")){
        valid = false;
    } else {
    }
    return valid;
}

public static String removeComment(String s){
    String comment = "";
    if (s.contains("//")){
        ArrayList<String> patternList = getExtraction("//", "", s);
        comment = patternList.get(0);
    }
    if (s.contains("/*")){
            ArrayList<String> patternList = getExtraction("/\\\\*", "\\\\*/", s);
            comment = "/"+patternList.get(0)+"/";
    }
    s = s.replace(comment, "").trim();
//    Print.prln(" text = "+s+ " comment = "+comment);

    return s;
}
/**
 *  removes null values (null items) from an array list.
 * @param aList
 * @return 
 */
public static ArrayList<String> removeEmpty(ArrayList<String> aList){
    ArrayList<String> newList = new ArrayList<String>();
    for (String item: aList){
        if (item != null && !"".equals(item.trim())){
            newList.add(item);
        }
    } 
    return newList;
}

public static String correctPunctuation(String text){
    
    text = text.replace(" ,", ",");
    text = text.replace(" .", ".");
    text = text.replace(" :", ":");
    text = text.replace(" ;", ";");
    
    return text;
}

public static String trimPhrase(String phrase){
     ArrayList<String> wordList = new ArrayList<String>();
     String newPhrase = "";
    // split phrase 

    phrase = getTokenizedFlat(phrase, "-");
    phrase = getTokenizedFlat(phrase, "_");
    wordList.addAll(getTokenizedList(phrase, " "));
    for (String word : wordList){
        newPhrase = newPhrase + " "+ word.trim();
    }
    newPhrase = newPhrase.toLowerCase().trim();
    return newPhrase;
}

public static boolean isEmpty(String item){
    boolean found = false;
        if (item == null || "".equals(item.trim())){
           found = true;
        }
    return found;
}

public static double formatDecimal(double number, int decimalPlaces){
    BigDecimal bd = new BigDecimal(String.valueOf(number)).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
    return bd.doubleValue();
}

}
