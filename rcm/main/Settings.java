
package rcm.main;

import java.awt.Font;
import javax.swing.ImageIcon;

/**
 * @author Krishna Sapkota
 * Created on 06-July-2010, 22:04:18
 */
public class Settings {

 /**  root location of the project and required files
     *  The main locations to be noted are
     *  1.  NetBeansProject: This is usually kept inside DropBox
     *  2.  Required input output files: It is also kept in DropBox to work with   multiple computers.
     *  3.  Jar Library: where all the required library is kept, it is usually   under default NetBeans projects
     *  4.  Gate installation: 
     *  5.  WordNet installation:
     *  Besides, it needs the other libraries
     *  (e.g. Jena, Pellet, OWLAPI, HermiT, GATE,JWNL for WordNet)
     **/

    /** PROJECT HOME */
    public static  String PROJECT_HOME = "d:/Dropbox/Krishna/NetBeansProjects/";
    public static String FILES_PATH = PROJECT_HOME + "mapping_files/";
    public static String JAR_LIBRARY = "d:/NetBeansProjects/jar_library/";
    
    /** PHASES FILES */
    public static String PHASES_FILES = FILES_PATH + "phases_files/";
    public static String PHASE0 = PHASES_FILES + "phase0/";
    public static String PHASE1 = PHASES_FILES + "phase1/";
    public static String PHASE2 = PHASES_FILES + "phase2/";
    public static String PHASE3 = PHASES_FILES + "phase3/";
    public static String PHASE4 = PHASES_FILES + "phase4/";
    public static String STYLES_PATH = PHASE1+ "styles/";
    
    
    
    /** ONTOLOGY */
    public static String ONTOLOGY_PATHNAME = FILES_PATH + "ontologies/";
    // source ontology : SemReg ontology
    public static String SEMREG_FILENAME = "SemReg_v4.owl";
    public static String SEMREG_URL = ONTOLOGY_PATHNAME + SEMREG_FILENAME;
    public static String SEMREG_URI_PREFIX = "http://www.owl-ontologies.com/SemanticRegulation.owl#";
    // target ontology: OntoReg ontology
    public static String ONTOREG_FILENAME = "OntoReg_d.owl";
    public static String ONTOREG_URL = ONTOLOGY_PATHNAME + ONTOREG_FILENAME;
    public static String ONTOREG_URI_PREFIX = "http://www.owl-ontologies.com/2008/6/17/Ontology1213712768.owl#";

    /** GATE */
    public static String GATE_FILES_PATH = FILES_PATH + "gate/";
    public static String GATE_HOME = "p:/program files/gate-6.0";
    // parser
    public  static String PARSER_PATH = GATE_FILES_PATH + "parser/";
    // Gagetteer
    public  static String GAZETTEER_PATH = GATE_FILES_PATH +"gazetteer/";
    public  static String GAZETTEER_FILENAME = "ontology.lst";
    public  static String GAZETTEER_URL = GAZETTEER_PATH + GAZETTEER_FILENAME;
    
    /* STOP WORDS */
    public static String STOPWORDS_GENERAL = Settings.GAZETTEER_PATH+"stop_list/general_stopwords.txt";
    public static String STOPWORDS_ONTOREG = Settings.GAZETTEER_PATH+"stop_list/ontoreg_stopwords.txt";
    
    // Jape
    public  static String JAPE_PATH = GATE_FILES_PATH + "jape/";
    // Datastore
    public  static String SERIAL_DATASTORE_PATH = GATE_FILES_PATH +"datastore/serial";


    /** WORDNET AND JWNL  */
    public   static String WN_CONFIG_FILE = JAR_LIBRARY + "jwnl/config/file_properties.xml";

    /** FONT STYLES */
    public  static Font HEADING_FONT = new Font("Arial", Font.BOLD, 20);
    public  static Font CAPTION_FONT = new Font("Serif", Font.BOLD, 18);
    public  static Font CAPTION2_FONT = new Font("Arial", Font.BOLD, 14);

    /** IMAGES */
    public  static String IMAGE_PATH = FILES_PATH + "images/";
    public static ImageIcon WRONG_IMAGE = new ImageIcon(IMAGE_PATH +"wrong.gif");
    public static ImageIcon RIGHT_IMAGE = new ImageIcon(IMAGE_PATH +"right.gif");
    public static ImageIcon WALL_IMAGE =  new ImageIcon(IMAGE_PATH +"wall.jpg");
    public static ImageIcon BANNER_IMAGE =  new ImageIcon(IMAGE_PATH +"banner.png");

     /** ICON IMAGES  */
    public static String ICON_PATH = IMAGE_PATH;
    public static String ICON_FILENAME = "report_check_small.png";
    public static ImageIcon ICON_SMALL =  new ImageIcon(IMAGE_PATH +"report_check_small.png");
    public static String ICON_URL = ICON_PATH + ICON_FILENAME;
    public static ImageIcon ICON =  new ImageIcon(ICON_URL);


}
