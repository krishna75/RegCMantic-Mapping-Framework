
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
import shef.nlp.wordnet.similarity.SimilarityMeasure;
import helper.util.Print;

/**
 *
 * @author Krishna Sapkota, 06-Dec-2011,   16:18:26
 * A PhD project at Oxford Brookes University
 */
public class AbstractSimilarity extends AbstractWordnet implements ISimilarity{
    SimilarityMeasure sim ;
    Map<String, String> params = new HashMap<String, String>();
    
    public AbstractSimilarity() {
       
    }

    @Override
    public double getSimilarity(String word1, String word2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getRelatedness(String word1, String word2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
