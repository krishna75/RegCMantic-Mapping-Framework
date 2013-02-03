/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.wn;

import java.util.ArrayList;
import helper.util.Print;

/**
 *
 * @author Krish
 */
public class _Tester {
    // Wordnet wn;
     ISimilarity sim;

    public _Tester() {
        init();
        test();
    }
    
    private void init(){
        //wn =  new Wordnet();
        sim = new LinSimilarity();
    }
    private void test(){
//        testLinSimilarity();
        //testSynset();
    }

    private void testLinSimilarity(){
        Print.prln("equipment substance ="+sim.getSimilarity("Equipment", "Substance"));
        Print.prln("equipment tank ="+sim.getSimilarity("Equipment", "Tank"));
        Print.prln("equipment material ="+sim.getSimilarity("Equipment", "material"));
    }

    private void testSynset(){
        String searchWord = "api";
        Wordnet.process("cat");
//        printList("ROOTWORDS",wn.getRootList(searchWord));
//        printList("SYNONYMS", wn.getSynList(searchWord));
//        printList("HYPONYMS", wn.getHypoList(searchWord));
//        printList("HYPERNYMS", wn.getHyperList(searchWord));
//        printList("ALL-LIST",  wn.getAllList(searchWord));
    }

    private void printList(String caption, ArrayList<String> synList){
        Print.prln("\n"+caption);
        for (String word: synList){
            Print.pr(word+"  ");
        }
    }



}
