
package zzz_unused;

import api.wn.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import shef.nlp.wordnet.similarity.SimilarityMeasure;

/**
 *
 * @author Krishna Sapkota, 05-Dec-2011,   20:28:44
 * A PhD project at Oxford Brookes University
 */
public class SimilarityTest {

    public SimilarityTest() {
        init();
        process();
        finish();
    }
private void init(){

}
private void process(){
        try {
            
            // See the JWordnet documentation for details on the properties file
            JWNL.initialize(new FileInputStream(Settings.WN_CONFIG_FILE));
            //Create a map to hold the similarity config params
            Map<String, String> params = new HashMap<String, String>();
            //the simType parameter is the class name of the measure to use
            params.put("simType", "shef.nlp.wordnet.similarity.JCn");

            //this param should be the URL to an infocontent file (if required
            //by the similarity measure being loaded)
            params.put("infocontent", "file:"+Settings.FILES_PATH+"test/ic-bnc-resnik-add1.dat");
            //this param should be the URL to a mapping file if the
            //user needs to make synset mappings
            params.put("mapping", "file:"+Settings.FILES_PATH+"test/domain_independent.txt");
            //create the similarity measure
            SimilarityMeasure sim = SimilarityMeasure.newInstance(params);
            //Get two words from WordNet
            Dictionary dict = Dictionary.getInstance();
            IndexWord word1 = dict.getIndexWord(POS.NOUN, "dog");
            IndexWord word2 = dict.getIndexWord(POS.NOUN, "cat");

            //and get the similarity between the first senses of each word
            System.out.println(word1.getLemma() + "#" + word1.getPOS().getKey() + "#1  " + word2.getLemma() + "#" + word2.getPOS().getKey() + "#1  " + sim.getSimilarity(word1.getSense(1), word2.getSense(1)));
            //get similarity using the string methods (note this also makes use
            //of the fake root node)
            System.out.println(sim.getSimilarity("time#n", "cat#n"));
            //get a similarity that involves a mapping
            System.out.println(sim.getSimilarity("namperson", "organization"));
    

            
//            System.out.println(sim.getSimilarity("human#n", "god#n"));
//            System.out.println(sim.getSimilarity("human#n", "monkey#n"));
//            System.out.println(sim.getSimilarity("human#n", "chimp#n"));
//            System.out.println(sim.getSimilarity("human#n", "cat#n"));
//            System.out.println(sim.getSimilarity("human#n", "grass#n"));
//            System.out.println(sim.getSimilarity("human#n", "bus#n"));
//            System.out.println(sim.getSimilarity("equipment#n", "tank#n"));
//            System.out.println(sim.getSimilarity("equipment#n", "pipe#n"));
//            System.out.println(sim.getSimilarity("equipment#n", "document#n"));
//            System.out.println(sim.getSimilarity("equipment#n", "material#n"));
//            System.out.println(sim.getSimilarity("regulation#n", "rule#n"));
//            System.out.println(sim.getSimilarity("regulation#n", "act#n"));
//            System.out.println(sim.getSimilarity("regulation#n", "law#n"));
//            System.out.println(sim.getSimilarity("regulation#n", "legality#n"));
            System.out.println(sim.getSimilarity("run", "walk"));
            System.out.println(sim.getSimilarity("cat", "tiger"));
            System.out.println(sim.getSimilarity("run", "gym"));
            System.out.println(sim.getSimilarity("buy#v", "fruit"));
        } catch (JWNLException ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }

}
private void finish(){
    
}
}
