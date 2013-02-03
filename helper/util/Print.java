/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.util;

import gate.Annotation;
import gate.AnnotationSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Krish
 */
public class Print {
/**
 *
 */
public Print() {
}
/**
 *
 * @param al
 */
public static void prArrayList(ArrayList<String> al){
    for (String str: al){
        pr("  "+ str);
    }
}
/**
 *
 * @param al
 */
public static void prlnArrayList(ArrayList<String> al){
    int i = 0;
    for (String str: al){
        prln(i +".  "+ str);
        i++;
    }
}
/**
 *
 * @param arr
 */
public static void printArray(Object[] arr){
    prln("size ="+arr.length);
    for (int i=0;i<arr.length;i++ ){

        prln(i+". "+arr[i].toString());
    }
}
/**
 *
 * @param map
 */
public static void printMap(HashMap map){
   Set keyset =  map.keySet();
   Iterator keyIter = keyset.iterator();
   while (keyIter.hasNext()){
       Object key = keyIter.next();
       Object value =  map.get(key);
       Print.prln("key = "+ key + " value = "+value);
   }

}

public static void printDoubleArray(Object[][] doubleArray){
    for (int i = 0; i<doubleArray.length;i++){
        Object[] array = doubleArray[i];
        for (int j= 0; j<array.length; j++){
            Print.pr(array[j]+", ");
        }
        Print.prln(" ");
    }
}
/**
 *
 * @param str
 */
public static void prln(String str){
    System.out.println(str);
}
/**
 *
 * @param str
 */
public static void pr(String str){
    System.out.print(str);
}
/**
 *
 * @param annSet
 */
public void printAnnSet(AnnotationSet annSet){
printAnnIter(annSet.iterator());
}
/**
 *
 * @param annIter
 */
public void printAnnIter(Iterator<Annotation> annIter){
    while (annIter.hasNext()){
        Annotation ann = annIter.next();
        Print.pr(ann.getType()+", ");
    }
   Print.prln("");
}
}
