
package api.gt;

import gate.*;
import helper.util.Print;
import helper.util.Sorter;
import java.util.Iterator;
import java.util.Set;


/**
 * only used for style head (can be regarded as NOT USED)
 * @author Krishna Sapkota,  Jun 8, 2012,  11:05:17 PM
 * A PhD project at Oxford Brookes University
 */
public class DatastoreReader {
        /*  annotation related */
        private Corpus corpus; 
        private AnnotationSet annSet;
        private SerialDatastoreIO datastore ;

    public DatastoreReader() {
        datastore = new SerialDatastoreIO();
        corpus = datastore.readCorpusFromSerialDatastore(null);
        Iterator iterDocs = corpus.iterator();
        int docCount = 0;
        while(iterDocs.hasNext()) {
          Document doc  = (Document) iterDocs.next();
          annSet   = doc.getAnnotations();         
          ++docCount;
          System.out.println("document no. = "+docCount);

          /***  Style head extractor  ***/
          Iterator<Annotation> annIter = Sorter.getSortedAnnIter(annSet.get("StyleHead"));
          while (annIter.hasNext()){
            Annotation ann = annIter.next();
            String text = ann.getFeatures().get("text").toString();
            Print.prln(text);
          } 
        }
    }
}
