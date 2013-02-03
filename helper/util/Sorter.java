/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.util;

import gate.Annotation;
import gate.AnnotationSet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;
import helper.style.StyleBody;

/**
 *
 * @author Krish
 */
public class Sorter implements Serializable {
/**
 *
 */
public Sorter() {
    }
public static ArrayList<String> getUniqueValues(Collection<String> values)
{
    return new ArrayList(new HashSet(values));
}
/**
 *  It sorts an array list and makes it unique (no double entries)
 * @param aList is the array list to be sorted
 * @return  the array list which is sorted and unique.
 */
public static ArrayList getSortedUniqueList(ArrayList aList){
        // making a unique array list
        HashSet h = new HashSet(aList);
        aList.clear();
        aList.addAll(h);

        // sorting
        Collections.sort(aList);

        return aList;
    }
public static Set<Annotation> getSortedAnnSet(AnnotationSet annSet){

      Set<Annotation> annSet2 = new HashSet<Annotation>();
      Iterator<Annotation> annIter = getSortedAnnIter(annSet);
      while(annIter.hasNext()){
          Annotation ann = annIter.next();
          annSet2.add(ann);
      }
      return annSet2;
  }
/**
*  Sorts a set of annotations and returns its iterator.
* @param annSet is the set to be sorted
* @return an iterator of the annotations. which is sorted.
*/
public static Iterator<Annotation> getSortedAnnIter(AnnotationSet annSet){

      // iii. sorting selected annotation
        Iterator iterAnnot = annSet.iterator();
        SortedAnnotationList sortedAnnotations = new SortedAnnotationList();
        Annotation currAnnot = null;
        Print.pr("Sorting annotations ....");
        while(iterAnnot.hasNext()) {
              currAnnot = (Annotation) iterAnnot.next();
              sortedAnnotations.addSortedExclusive(currAnnot);
            } // while

        Print.prln(".... sorting completed. Total annotations = "+sortedAnnotations.size());
        return sortedAnnotations.iterator();
  }
/**
*  Sorts a set of annotations and returns its iterator.
* @param annSet is the set to be sorted
* @return an iterator of the annotations. which is sorted.
*/
public static Iterator<Annotation> getSortedAnnIter(Set<Annotation> annSet){

      // iii. sorting selected annotation
        Iterator iterAnnot = annSet.iterator();
        SortedAnnotationList sortedAnnotations = new SortedAnnotationList();
        Annotation currAnnot = null;
        Print.pr("Sorting annotations ....");
        while(iterAnnot.hasNext()) {
              currAnnot = (Annotation) iterAnnot.next();
              sortedAnnotations.addSortedExclusive(currAnnot);
            } // while

        Print.prln(".... sorting completed. Total annotations = "+sortedAnnotations.size());
        return sortedAnnotations.iterator();
  }
/**
*  Why sorting?
*  It helps to distinguish reg no, subject and action of a regulation from others. It is not an
*  appropriate way of achieving this, but it does the job as it sorts by the starting
*  node of the annotations.
*/
private static class SortedAnnotationList extends Vector {
        private SortedAnnotationList() {
          super();
        } // SortedAnnotationList

        private boolean addSortedExclusive(Annotation annot) {
          Annotation currAnot = null;

          //  the start node of the given annoation (the annotation to be inserted/ provided by the parameter)
          long annotStart = annot.getStartNode().getOffset().longValue();
          long currStart;

          // insert : for all the annotations
          for (int i=0; i < size(); ++i) {
                currAnot = (Annotation) get(i);

                // the start node of the current annotation
                currStart = currAnot.getStartNode().getOffset().longValue();

                // if  start node of the  given annoation is smallar than that of current annotation
                if(annotStart <= currStart) {
                      insertElementAt(annot, i);
                      return true;
                } // if
          } // for
          int size = size();
          insertElementAt(annot, size);
          return true;
        } // addSorted
  } // SortedAnnotationList
/**
  * It makes an arraylist sorted and unique.
  * @param al the arraylist to be sorted
  * @return the sorted and unique arraylist.
  */
public static ArrayList<String> sortUnique(ArrayList<String> al){
       //makes array unique
        Set<String> uniqueSet = new HashSet<String>(al);
        Object[] unique =  uniqueSet.toArray();
       // Sorting
        Arrays.sort(unique);
       return Converter.arrayToArrayList(unique);
   }
/**
 *  It spans the body list. It joins the two similar subsequent style structures  e.g. if <paragraph>i s followed by  <paragraph> it will combine them under one
 * @param bodyList to be spanned
 * @return a array list of spanned bodies
 */
public static ArrayList<StyleBody> spanStyle(ArrayList<StyleBody> bodyList){
int level1 ;
int level2;
LinkedList bodyLList = new LinkedList(bodyList);
Iterator iter = bodyLList.iterator();
StyleBody body1 = (StyleBody)iter.next();
while (iter.hasNext()){
    boolean repeat = true;
    StyleBody body2 = null;
    level1 = body1.getStyleLevel();
    while (iter.hasNext()&& repeat){
        body2 = (StyleBody)iter.next();
        level2= body2.getStyleLevel();
        if (level1 == level2){
            body1.setText(body1.getText()+"\n "+body2.getText());
            iter.remove();
            repeat = true;
        }else{
            repeat = false;
        }// if
    }// while second
body1 = body2;
}// while first
bodyList.clear();
bodyList.addAll(bodyLList);
return bodyList;
}
}
