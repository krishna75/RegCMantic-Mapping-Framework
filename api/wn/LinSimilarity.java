
package api.wn;

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
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;
import shef.nlp.wordnet.similarity.SimilarityInfo;
import shef.nlp.wordnet.similarity.SimilarityMeasure;

/**
 *
 * @author Krishna Sapkota, 06-Dec-2011,   13:19:23
 * A PhD project at Oxford Brookes University
 */
public class LinSimilarity extends AbstractSimilarity implements ISimilarity  {
    
    public LinSimilarity() {
        init();
    }
private void init(){
    try {
            params.put("simType", "shef.nlp.wordnet.similarity.Lin");
            params.put("infocontent", "file:"+Settings.FILES_PATH+"test/ic-bnc-resnik-add1.dat");
            params.put("mapping", "file:"+Settings.FILES_PATH+"test/domain_independent.txt");
            sim = SimilarityMeasure.newInstance(params);
            
            
        } catch (JWNLException ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SimilarityTest.class.getName()).log(Level.SEVERE, null, ex);
        }
}

    @Override
    public  double getSimilarity(String word1, String word2) {
        double score = 0;
        SimilarityInfo simInfo = null ;
        try {
            simInfo = sim.getSimilarity(word1, word2);
        } catch (JWNLException ex) {
            Logger.getLogger(LinSimilarity.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (simInfo != null){
         score = simInfo.getSimilarity();
        }
        return score;
    }

    @Override
    public double getRelatedness(String word1, String word2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
