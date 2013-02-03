
package api.nlp;

import helper.util.MyWriter;
import helper.util.Print;
import helper.util.RegEx;
import java.util.ArrayList;
import rcm.main.Settings;

/**
 *  It creates gazetteers for from the parsed sentences.
 * @author Krishna Sapkota,  May 21, 2012,  12:18:47 PM
 * A PhD project at Oxford Brookes University
 */
public class GazGenerator {
    /* IO Files*/
    // input file
    String path = Settings.PHASE2+ "after_parsing/parser/";
    String PARSED_FILE = path + "parsed_selected_sentences.xml";
    // output files
    String gaz_path = Settings.GAZETTEER_PATH + "gazetteer_parser/";
    String DEF_FILE = gaz_path + "lists.def";
    String SUB_FILE =  "parsed_subject.lst";
    String ACT_FILE = "parsed_action.lst";
    String OBL_FILE = "parsed_obligation.lst";
    String OBJ_FILE = "parsed_object.lst";
    String MOD_FILE = "parsed_modifier.lst";
    String CON_FILE = "parsed_condition.lst";

    /* Local Variables */
    ArrayList<String> subList = new ArrayList<String>();
    ArrayList<String> oblList = new ArrayList<String>();
    ArrayList<String> actList = new ArrayList<String>();
    ArrayList<String> objList = new ArrayList<String>();
    ArrayList<String> modList = new ArrayList<String>();
    ArrayList<String> conList = new ArrayList<String>();

    public GazGenerator() {
        readParsed();
        writeDef();
        writeLists();
    }
    
    private void readParsed(){
        SentenceWrapper wrapper = new SentenceWrapper(null);
        wrapper.read(PARSED_FILE);
        ArrayList<SentenceWrapper> wpList = wrapper.getWrapperList();
        for (SentenceWrapper wp: wpList){
            String sub = wp.getSubject();
            if ( sub != null && !sub.trim().isEmpty()){
               addUnique(subList, sub);
            }
            String obl = wp.getObl();
            if (obl != null && !obl.trim().isEmpty()){
                addUnique(oblList, obl);
            }
            String act = wp.getAction();
            if (act != null && !act.trim().isEmpty()){
                addUnique(actList, act);
            }
            String obj = wp.getObject();
            if (obj != null && !obj.trim().isEmpty()){
                addUnique(objList, obj);
            }
            String mod = wp.getModifier();
            if ( mod != null && !mod.trim().isEmpty()){
                addUnique(modList, mod);
            }
            String con = wp.getCondition();
            if ( con != null && !con.trim().isEmpty()){
                addUnique(conList, con);  
            }         
        }
    }
    
     private void writeDef(){
         Print.prln("GazGenerator: creating the lists.def file ...");
        String text = SUB_FILE+":parsed_concept:parsed_subject\n"+
                OBL_FILE+":parsed_concept:parsed_obligation\n" +
                ACT_FILE+":parsed_concept:parsed_action\n" +
                OBJ_FILE+":parsed_concept:parsed_object\n" +
                MOD_FILE+":parsed_concept:parsed_modifier\n" +
                CON_FILE+":parsed_concept:parsed_condition";
        MyWriter.write(text, DEF_FILE);
    }
    
    private void writeLists(){
        Print.prln("GazGenerator: creating the gazetteer list files ...");
        MyWriter.write(subList, gaz_path + SUB_FILE);
        MyWriter.write(oblList, gaz_path + OBL_FILE);
        MyWriter.write(actList, gaz_path + ACT_FILE);
        MyWriter.write(objList, gaz_path + OBJ_FILE);
        MyWriter.write(modList, gaz_path + MOD_FILE);
        MyWriter.write(conList, gaz_path + CON_FILE);
    }
    
    private void addUnique(ArrayList<String> aList, String text){
        boolean exist = false;
        text = text.trim();
        for (String a: aList){
            if (a.equals(text)){
                exist = true;
            }
        }
        if (!exist){
            text = RegEx.correctPunctuation(text);
            aList.add(text);
        }
    }
    
   

}
