
package zzz_unused;

import gate.Annotation;
import gate.AnnotationSet;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import helper.util.Print;

/**
 *
 * @author Krishna Sapkota, 30-Sep-2011,   14:23:57
 * A PhD project at Oxford Brookes University
 */
public class ExtendedAnnotationSet   {

    ArrayList<ExtendedAnnotation> annList ;
    Set annSet ;
    protected long firstNode;
    protected long lastNode;

    /**
     * Get the value of lastNode
     *
     * @return the value of lastNode
     */
    public long getLastNode() {
        return lastNode;
    }

    /**
     * Set the value of lastNode
     *
     * @param lastNode new value of lastNode
     */
    public void setLastNode(long lastNode) {
        this.lastNode = lastNode;
    }


    /**
     * Get the value of firstNode
     *
     * @return the value of firstNode
     */
    public long getFirstNode() {
        return firstNode;
    }

    /**
     * Set the value of firstNode
     *
     * @param firstNode new value of firstNode
     */
    public void setFirstNode(long firstNode) {
        this.firstNode = firstNode;
    }


    public ExtendedAnnotationSet() {
        
    }

    public ExtendedAnnotationSet(AnnotationSet as) {
        init();
        this.firstNode = as.firstNode().getOffset().longValue();
        this.lastNode = as.lastNode().getOffset().longValue();
        Iterator iterAS = as.iterator();
        while (iterAS.hasNext()){
            addAnnotation((Annotation)iterAS.next());
        }
    }
    private void init(){
        annList = new ArrayList<ExtendedAnnotation>();
//        annSet = annList.iterator().
    }
    public void addAnnotation(Annotation ann){
        this.addAnnotation(new ExtendedAnnotation(ann));
    }
    public void addAnnotation(ExtendedAnnotation ann){
        annList.add(ann);

    }

    public void sort(){
       Object[] annArray= annList.toArray();
       int n = annArray.length;
        for (int pass=1; pass < n; pass++) {  // count how many times
            // This next loop becomes shorter and shorter
            for (int i=0; i < n-pass; i++) {
                ExtendedAnnotation current = (ExtendedAnnotation) annArray[i];
                ExtendedAnnotation next = (ExtendedAnnotation) annArray[i+1];
                if (current.getStartNode() >= next.getStartNode()) {
                    // exchange elements
                    ExtendedAnnotation temp = current;
                    annArray[i] = next;
                    annArray[i+1] = temp;
                }
            }
        }
       annList.clear();
       for (int i = 0; i<annArray.length ; i++){
           annList.add((ExtendedAnnotation) annArray[i]);
       }
       print();
   }


   
    public Iterator iterator() {
        return this.iterator();
    }


    
    public int size() {
       return this.size();
    }

    public void print(){
        for (ExtendedAnnotation eAnn: annList){
            Print.prln(eAnn.toString());
        }
    }



}
