/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.phase32.mapping;

import helper.util.Print;

/**
 *
 * @author Krishna Sapkota,  Aug 9, 2012,  4:00:19 PM
 * A PhD project at Oxford Brookes University
 */
public class Similarity {

    public Similarity() {
        double[]  vecA = {1, 1,0.5, 0., 0,0};
        double[] vecB = {0.5, 0.3,1,1, 0, 0};

        double sim = CalculateCosineSimilarity(vecA, vecB);
        Print.prln("similarity score = "+ sim);
    }    
    
    private  double CalculateCosineSimilarity(double[] vecA, double[] vecB){
        double dotProduct = DotProduct(vecA, vecB);
        double magnitudeOfA = Magnitude(vecA);
        double magnitudeOfB = Magnitude(vecB);

        return dotProduct/(magnitudeOfA*magnitudeOfB);
    }
    
    private  double DotProduct(double[] vecA, double[] vecB){    
        double dotProduct = 0;
        for (int i = 0; i < vecA.length; i++){
                dotProduct += (vecA[i] * vecB[i]);
        }
        return dotProduct;
  }
    
    private  double Magnitude(double[] vector){
        return Math.sqrt(DotProduct(vector, vector));
    }

}
