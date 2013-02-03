
package api.wn;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import helper.util.Converter;
import helper.util.Print;
import helper.util.Sorter;


/**
 * This class connects wordnet installation in this computer via JWNL api. The JWNL
 * needs a configuration file which describes the specifications for wordnet installations
 * and libraries. Class Settings has name of this configuration file.
 * @author Krish
 */
public class Wordnet  {
    static ArrayList<String> synList ;
    static ArrayList<String> hypoList;
    static ArrayList<String> hyperList;
    static ArrayList<String> rootList ;
    static ArrayList<String> allList ;

public  Wordnet(){
    init(); 
}

/**
 * Initialises all the lists
 */
private static void init(){
     Print.prln("Wordnet initialised...");
     synList = new ArrayList<String>();
     hypoList= new ArrayList<String>();
     hyperList= new ArrayList<String>();
     rootList = new ArrayList<String>();
     allList = new ArrayList<String>();
}

/**
* Creates a synset of synonym, hypername and hyponame of a given word.
* @param givenWord is the word whose synset will be created.
* @return an array of synset words.
*/
public static void process(String searchWord) {
     init();
     //TODO get seperate synonyms for verb, noun and adjective
   
    // only carries operation if not empty
    if (searchWord != null){
        searchWord = searchWord.toLowerCase();
            try {
                Dictionary dictionary = Dictionary.getInstance();
                //  Index word is a form of POS (i.e. verb, noun, adj or adv)
                IndexWordSet indexWords = dictionary.lookupAllIndexWords(searchWord);
                IndexWord[] words = indexWords.getIndexWordArray();

                /*          
                                            A Synset, or synonym set, represents a line of a WordNet pos.data file. A Synset represents a concept,
                                            and contains a set of Words, each of which has a sense that names that concept (and each of which is
                                            therefore synonymous with the other words in the Synset).
                                            Synset's are linked by Pointers into a network of related concepts; this is the Net in WordNet.
                                            getTargets retrieves the targets of these links, and getPointers retrieves the pointers themselves.
                                             */
                // for earch index word (i.e. the given word as a verb, noun, adj or adv)
                for (int l = 0; l < words.length; l++) {
                    IndexWord word = words[l];
                    Synset[] senses = word.getSenses();

                    // only carries operation if it has senses
                    if (senses.length > 0) {
                        for (int i = 0; i < senses.length; i++) {
                            Synset sense = senses[i];

                            //Root words
                            String rootWord = sense.getWord(0).getLemma().toLowerCase();
                            rootList.add(rootWord);

                            //Synonyms
                            Word[] synonyms = sense.getWords();
                            for (int k = 0; k < synonyms.length; k++) {
                                Word synonym = synonyms[k];
                                synList.add(synonym.getLemma().toLowerCase());
                            }

                            //Hyponyms
                            Pointer[] hypos = sense.getPointers(PointerType.HYPONYM);
                            for (int j = 0; j < hypos.length; j++) {
                                Synset targetWords = (Synset) (hypos[j].getTarget());
                                Word targetWord = targetWords.getWord(0);
                                hypoList.add(targetWord.getLemma().toLowerCase());
                            }

                            //Hypernames
                            Pointer[] hypers = sense.getPointers(PointerType.HYPERNYM);
                            for (int j = 0; j < hypers.length; j++) {
                                Synset targetWords = (Synset) (hypers[j].getTarget());
                                Word targetWord = targetWords.getWord(0);
                                hyperList.add(targetWord.getLemma().toLowerCase());
                            }
                        } //for senses
                        //for senses
                    } // if senses exist
                } // for each words
                // for each words
            } // if word is not empty
            catch (JWNLException ex) {
                Logger.getLogger(Wordnet.class.getName()).log(Level.SEVERE, null, ex);
            }
    }// if word is not empty

     allList.addAll(synList);
     allList.addAll(hypoList);
     allList.addAll(hyperList);
     refine();
    
}

private static void refine(){
    /* unique values  */
    allList = Sorter.getUniqueValues(allList);
    synList = Sorter.getUniqueValues(synList);
    hypoList = Sorter.getUniqueValues(hypoList);
    hyperList = Sorter.getUniqueValues(hyperList);

    /*  pure letters*/
    allList = Converter.getPureLetters(allList);
    synList = Converter.getPureLetters(synList);
    hypoList = Converter.getPureLetters(hypoList);
    hyperList = Converter.getPureLetters(hyperList);
}

public static ArrayList<String> getAllList(String searchWord){
    process(searchWord);
    return  Sorter.sortUnique(allList);
}

    public static ArrayList<String> getHyperList(String searchWord){
        process(searchWord);
        return Sorter.sortUnique(hyperList);
    }

    public static ArrayList<String> getHypoList(String searchWord){
            process(searchWord);
            return Sorter.sortUnique(hypoList);
    }

    public static ArrayList<String> getSynList(String searchWord){
        process(searchWord);
        return Sorter.sortUnique(synList);
    }

    public static ArrayList<String> getRootList(String searchWord){
        process(searchWord);
        return Sorter.sortUnique(rootList);
    }

    public static ArrayList<String> getRootList(){
        return rootList;
    }
    public  void setRootList(ArrayList<String> rootList) {
        this.rootList = rootList;
    }

    public static ArrayList<String> getAllList() {
        return allList;
    }

    public void setAllList(ArrayList<String> allList) {
        this.allList = allList;
    }

    public static ArrayList<String> getHyperList() {
        return hyperList;
    }

    public void setHyperList(ArrayList<String> hyperList) {
        this.hyperList = hyperList;
    }

    public static ArrayList<String> getHypoList() {
        return hypoList;
    }

    public void setHypoList(ArrayList<String> hypoList) {
        this.hypoList = hypoList;
    }

    public static  ArrayList<String> getSynList() {
        return synList;
    }

    public void setSynList(ArrayList<String> synList) {
        this.synList = synList;
    }
    

}