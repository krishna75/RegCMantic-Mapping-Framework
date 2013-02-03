
package api.wn;

import helper.util.Converter;
import helper.util.Print;


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
        ISimilarity sim = new LinSimilarity();
        double score = sim.getSimilarity("cat", "dog");
        score = Converter.roundDouble(score,3);
        System.out.println(score);
        System.out.println(sim.getSimilarity("material", "material"));

}
private void finish(){
    
}
}
