package api.nlp;


import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.*;
import helper.util.Converter;
import helper.util.Print;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rcm.main.Settings;

public class StanfordParser {
    /* IO Files*/
    String path = Settings.PHASE2+ "after_parsing/parser/";
    String IN_SELECTED_SENTENCE_FILE = path + "selected_sentences.xml";
    String OUT_PARSED_SELECTED_SENTENCE_FILE = path + "parsed_selected_sentences.xml";
    
    /* External Classes */
    LexicalizedParser lp ;
    TokenizerFactory<CoreLabel> tokenizerFactory ; 
    List<CoreLabel> tokenList ;
    List<TypedDependency> tdList ;
    
    /* Local Variables */
    AbstractSentenceReader reader;
    SentenceWrapper wrapper;
    ArrayList<SentenceWrapper> wpList;
    
    int rootIndex ;
    int oblStart, oblEnd ;
    int actStart, actEnd;
    int subStart, subEnd;
    int objStart, objEnd;
    int modStart, modEnd;
    int condStart, condEnd;
    
    // testing
    int MAX_SEN = 50;
    boolean SHOW_DEPENDENCY = false;
    int test  = 0;
    
  public StanfordParser() {
      init();
      process();
      finish();
  }
  private void init(){
      // for the test purpose
      if (test ==1){
          MAX_SEN = 2;
          SHOW_DEPENDENCY = true;
      }
    lp = LexicalizedParser.loadModel("d:/NetBeansProjects/jar_library/"
            + "stanford-parser-2012-03-09/stanford-parser-2012-03-09-models/"
            + "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
    tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), ""); 
    tokenList = null;
    tdList = null;
    reader = new RefinedSentenceReader(IN_SELECTED_SENTENCE_FILE,null);
    wrapper = new SentenceWrapper(OUT_PARSED_SELECTED_SENTENCE_FILE);
    wpList = wrapper.getWrapperList();
    
  }
  private void finish(){
      wrapper.write(wrapper.getOutFile());
  }
  
  private void process(){
      processSentenceList(reader.getSenTextList());
  }
  public ArrayList<SentenceWrapper> processSentenceList(ArrayList<String> senList){
      wpList.clear();
      oblStart = -1;
      oblEnd = -1;
      actStart = -1;
      actEnd = -1;
      subStart = -1;
      subEnd = -1;
      int count = 0;
      Print.pr("Processing "+ MAX_SEN +" sentences = ");
      for (String sen: senList){
          
          // a sentence must have at least a subject, an action and an obligation ( total of at least three words)
          if (sen.length()>3 && count++ < MAX_SEN){
              Print.pr(" "+ count);
            processSentence(sen);
          }
      }
      return wpList;
  }

  public  void processSentence(String sen) {

    /*  tokeniser to tree   */
    tokenList = tokenizerFactory.getTokenizer(new StringReader(sen)).tokenize();
    Tree parse = lp.apply(tokenList);

    /*  tree to grammar   */
    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
    
    /*  grammar to dependency  */
    tdList = gs.typedDependenciesCCprocessed();
    
    /* populating the   wrapper */
    SentenceWrapper wp = new SentenceWrapper(OUT_PARSED_SELECTED_SENTENCE_FILE);
    wpList.add(wp);
    
    
    // this must follow the sequence of root, action, obl and subject
    getRoot();
    wp.setAction(getAction());
    wp.setObl(getObligation());
    wp.setSubject(getSubject());
    wp.setObject(getObject());
    wp.setModifier(getModifier());
    wp.setCondition(getCondition());
    
    // displays the dependency and the three
    if (SHOW_DEPENDENCY){
        TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
        tp.printTree(parse);
    }
  }
  
  public int getRoot(){
      int i = -1;
      Iterator<TypedDependency> tdIter = tdList.iterator();
      while (tdIter.hasNext()){
          TypedDependency td = tdIter.next();
          if(td.gov().nodeString().equals("ROOT")){
            i = td.dep().index();  
          }
      } 
      rootIndex = i;
      return i;
  }
  
  public String getAction(){
      actStart = rootIndex;
      actEnd = rootIndex;
      ArrayList<String> actList = new ArrayList<String>();
      Iterator<TypedDependency> tdIter = tdList.iterator();
      while (tdIter.hasNext()){
        TypedDependency td = tdIter.next();
        
        // determines the high index
        if(  td.gov().index() == rootIndex && td.reln().toString().contains("conj") ){
            int i = td.dep().index();  
            if (i > actEnd || actEnd == rootIndex){
                actEnd=i;
            }
        } 
        // determines the low index
        String reln = td.reln().toString();
        String[] parts = {"det","nn","mod"};
         if(  td.gov().index() == rootIndex 
                 && contains(reln, parts)){
            int i = td.dep().index();  
            if (i<actStart || actStart == rootIndex){
                actStart=i;
            }
        }     
     }// end while
      // gets the string in between
      for (int i = actStart; i<=actEnd; i++){
          actList.add(getString(i));
      }
      return Converter.arrayListToString(actList);
  }
  
    public String getObligation(){
      oblStart= actStart;
      oblEnd = actStart;
      ArrayList<String> oblList = new ArrayList<String>();
      Iterator<TypedDependency> tdIter = tdList.iterator();
      while (tdIter.hasNext()){
        TypedDependency td = tdIter.next();
        
        // determines the start of the obligation
        if(  td.reln().toString().contains("aux") &&
            td.gov().index() == rootIndex ){
            int i = td.dep().index();  
            if (i<oblStart){
                oblStart = i;
            }
       }
     }
      // if there exist  something before obl then process
      if (oblStart>1){
        for (int i = oblStart; i< oblEnd; i++){
            oblList.add(getString(i));
        }
      }
      return Converter.arrayListToString(oblList);
  }
  
  public String getSubject(){
      subStart = 0;
      int refineStart = 0;
      subEnd = 0;
      ArrayList<String> subList = new ArrayList<String>();
      Iterator<TypedDependency> tdIter1 = tdList.iterator();
      while(tdIter1.hasNext()){
         TypedDependency td1 = tdIter1.next();
         
         // determines the  end of the subject
         String reln = td1.reln().toString();
         String[] parts = {"nsubj"};
         if( td1.gov().index() == rootIndex && contains( reln, parts) ){
            int i = td1.dep().index();  
            if (i < subStart || subStart == 0){
             subStart = i;
            }
            refineStart = subStart;
            subEnd = subStart;
           
        
            Iterator<TypedDependency> tdIter2 = tdList.iterator();
            while (tdIter2.hasNext()){
                TypedDependency td2 = tdIter2.next();
                
                // determines the start of the subject
                if(  td2.gov().index() == subStart ){
                    int j = td2.dep().index();  
                    if (j > subEnd){
                        subEnd = j;
                    }
                    if (j< refineStart){
                        refineStart = j;
                    }
                }
            }
          }
        }
      subStart = refineStart;
      for (int i = subStart; i <= subEnd; i++){
          subList.add(getString(i));
      }
      
      return Converter.arrayListToString(subList);
  }
  
  
  
  // object
    public String getObject(){
      objStart = 0;
      int refineStart = 0;
      objEnd = 0;
      ArrayList<String> objList = new ArrayList<String>();
      Iterator<TypedDependency> tdIter1 = tdList.iterator();
      while(tdIter1.hasNext()){
         TypedDependency td1 = tdIter1.next();
         
         // determines the  end of the object
         String reln = td1.reln().toString();
         String[] parts = {"dobj"};
         if( td1.gov().index() == actEnd && contains( reln, parts) ){
            int i = td1.dep().index();  
            if (i < objStart || objStart == 0){
             objStart = i;
            }
            refineStart = objStart;
            objEnd = objStart;
           
        
            Iterator<TypedDependency> tdIter2 = tdList.iterator();
            while (tdIter2.hasNext()){
                TypedDependency td2 = tdIter2.next();
                
                // determines the start of the subject
                if(  td2.gov().index() == objStart ){
                    int j = td2.dep().index();  
                    if (j > objEnd){
                        objEnd = j;
                    }
                    if (j< refineStart){
                        refineStart = j;
                    }
                }
            }
          }
        }
      objStart = refineStart;
      for (int i = objStart; i <= objEnd; i++){
          objList.add(getString(i));
      }
      
      return Converter.arrayListToString(objList);
  }
  
  public String getModifier(){
      modStart = 0;
      int refinedStart = 0;
      modEnd = 0;
      ArrayList<String> modList = new ArrayList<String>();
      Iterator<TypedDependency> tdIter1 = tdList.iterator();
      while(tdIter1.hasNext()){
         TypedDependency td1 = tdIter1.next();
         
         // determines the  start
         String reln = td1.reln().toString();
         String[] parts = {"xcomp","prep"};
         if( td1.gov().index() == actEnd && contains(reln, parts)  ){
            int i = td1.dep().index();  
            if (i < modStart || modStart == 0){
             modStart = i;
            }
            
            // attaches the preposition type
            if ( reln.contains("prep_")){
                String prepType = reln.substring("prep_".length(), reln.length());
                modList.add(prepType);
            }
            
            // needs to set these values
            refinedStart = modStart;
            modEnd = modStart;
        
            Iterator<TypedDependency> tdIter2 = tdList.iterator();
            while (tdIter2.hasNext()){
                TypedDependency td2 = tdIter2.next();
                
                // determines the start end
                if(  td2.gov().index() == modStart ){
                    int j = td2.dep().index();  
                    if (j > modEnd){
                        modEnd = j;
                    }
                    // refines start
                    if (j< refinedStart){
                        refinedStart = j;
                    }
                }
            }
          }
        }
      // get the value in between
      modStart = refinedStart;
      for (int i = modStart; i <= modEnd; i++){
          modList.add(getString(i));
      }
      
      return Converter.arrayListToString(modList);
  }
  
  public String getCondition(){
      condStart = 0;
      int refinedStart = 0;
      condEnd = 0;
      ArrayList<String> condList = new ArrayList<String>();
      Iterator<TypedDependency> tdIter1 = tdList.iterator();
      while(tdIter1.hasNext()){
         TypedDependency td1 = tdIter1.next();
         
         // determines the  start
         String reln = td1.reln().toString();
         String[] parts = {"advcl"};
         if( td1.gov().index() == rootIndex && contains(reln, parts)  ){
            int i = td1.dep().index();  
            if (i < condStart || condStart == 0){
             condStart = i;
            }
            
            // needs to set these values
            refinedStart = condStart;
            condEnd = condStart;
        
            Iterator<TypedDependency> tdIter2 = tdList.iterator();
            while (tdIter2.hasNext()){
                TypedDependency td2 = tdIter2.next();
                
                // refines
                if(  td2.gov().index() == condStart ){
                    int j = td2.dep().index();  
                    // refines start
                    if (j< refinedStart){
                        refinedStart = j;
                    }
                }
            }
          }
        }
      // get the value in between
      condStart = refinedStart;
      condEnd = subStart-2;
      for (int i = condStart; i <= condEnd; i++){
          condList.add(getString(i));
      }
      
      return Converter.arrayListToString(condList);
  }
  

  
  public int getObjectRoot(){
      int i = -1;
      Iterator<TypedDependency> tdIter = tdList.iterator();
      while (tdIter.hasNext()){
          TypedDependency td = tdIter.next();
          //Print.prln(" reln = "+ td.reln().toString()+ " index = "+td.gov().index());
          if(td.reln().toString().equals("dobj") && td.gov().index() == rootIndex ){
            i = td.dep().index();  
          }
      }
      return i ;
  }
  
  
  public int getDep(String rel, int gov){
      int i = -1;
      Iterator<TypedDependency> tdIter = tdList.iterator();
      while (tdIter.hasNext()){
        TypedDependency td = tdIter.next();
        if(td.reln().toString().contains(rel) && td.gov().index() == gov ){
            i = td.dep().index();  
        }
     }
     return i;
  }
  
  /*  Utilities*/
  
  public boolean contains(String reln, String[] words){
      boolean yes = false;
      for (int i = 0; i < words.length; i++){
          if (reln.contains(words[i])){
              yes = true;
          }
      }
      return yes;
  }
  public String getString(int i){
      String text = "";
      if (i>0 && i< tokenList.size()){
          String text1 = tokenList.get(i-1).originalText();
          text = text1;
          }
      return text;
  }

}
