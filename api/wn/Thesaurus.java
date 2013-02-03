
package api.wn;

import java.util.ArrayList;
import helper.util.Converter;
import helper.util.Print;

/**
 *
 * @author Krishna Sapkota, 25-Nov-2011,   09:14:38
 * A PhD project at Oxford Brookes University
 */
public class Thesaurus {
    private String searchWord;
    private String rootWord ;
    private ArrayList<String> synonyms;
    private ArrayList<String> hyponyms;
    private ArrayList<String> hypernyms;
/**
 * This class gets a root word, syn, hypo and hyper-nyms, makes them unique and removes null value.
 * This can be particularly useful, if you are just using the above terms. 
 *  Note: The methods are made static to make the process faster. However,  you will still need to
 * instantiate the Wordnet class once from your class.
 * @param searchWord
 */
    public Thesaurus(String searchWord) {
        this.searchWord = searchWord;
        /* local processes */
        init();
        process();
        refine();
    }
    /**  initializes  the fields*/
    private void init(){
        Print.prln("Thesaurus initialised...");
        rootWord = "";
        synonyms = new ArrayList<String>();
        hyponyms = new ArrayList<String>();
        hypernyms = new ArrayList<String>();   
    }

    /** Connects to wordnet to get synonyms, hyponyms and hypernyms. */
    private void process(){
            Wordnet.process(searchWord);
            ArrayList<String> rootList = Wordnet.getRootList();
            if (rootList.size()>0){
                rootWord = rootList.get(0);
            }
            synonyms = Wordnet.getSynList();
            hyponyms = Wordnet.getHypoList();
            hypernyms = Wordnet.getHyperList();
    }

    /**  makes the word lists unique and removes duplication */
    private void refine(){
        /*  making unique*/
        synonyms = Converter.makeUnique(synonyms);
        hyponyms = Converter.makeUnique(hyponyms);
        hypernyms = Converter.makeUnique(hypernyms);
        /*  removing duplicate words in furhter synsets */
        synonyms = Converter.remove(synonyms, rootWord);
        synonyms = Converter.remove(synonyms, searchWord);
        hypernyms = Converter.remove(hypernyms, synonyms);
        hyponyms = Converter.remove(hyponyms, synonyms);
    }

    public ArrayList<String> getHypernyms() {
        return hypernyms;
    }

    public void setHypernyms(ArrayList<String> hypernyms) {
        this.hypernyms = hypernyms;
    }

    public ArrayList<String> getHyponyms() {
        return hyponyms;
    }

    public void setHyponyms(ArrayList<String> hyponyms) {
        this.hyponyms = hyponyms;
    }

    public String getRootWord() {
        return rootWord;
    }

    public void setRootWord(String rootWord) {
        this.rootWord = rootWord;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }
    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }
    @Override
    public String toString(){
        String resultWord = "";
        resultWord = "SEARCH WORD = "+ searchWord +"; ROOT WORD = " + rootWord +
                "; SYNONYMS = "+ Converter.listToString(synonyms, ",") +
                "; HYPONYMS = "+ Converter.listToString(hyponyms, ",")+
                "; HYPERNYMS = " + Converter.listToString(hypernyms, ",") ;
        return resultWord;
    }
}
