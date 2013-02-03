/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm.main;

import api.gt.DatastoreReader;
import api.nlp.*;
import api.ont.JenaAbstractOntology;
import api.ont.Jena_Ontology;
import api.ont.OntologicalConcept;
import api.wn.Wordnet;
import helper.util.Print;
import helper.util.RegEx;
import rcm.phase31.pre.DifferenceTableCreator;
import rcm.phase2.GazCreater;
import rcm.phase2._0_OntoGazCreator;
import rcm.phase2._2_EntityReader_V3;
import rcm.phase2._3_SemReg_V4_Populator;
import rcm.phase31.pre.RegTaskCollector;
import rcm.phase32.mapping.MappingAlgorithms;
import rcm.phase32.mapping.MappingAlgorithmsTuner;
import rcm.phase32.mapping.Similarity;
import rcm.phase32.mapping.ThreeScoresGenerator;
import rcm.phase33.evaluation.ComputedMappingCollector;
import rcm.phase33.evaluation.ExistingMappingCollector;
import rcm.phase33.evaluation.MappingResultProcessor;
import rcm.phase33.evaluation.MappingResultProcessorTunner;




/**
 *
 * @author Krishna Sapkota
// */
public class Main {
    //TODO check library if it is complete

 /**
     * @param args the command line arguments
     */
public static void main(String[] args)   {
//    new rcm.main._Tester();//--------------------------------------------
   
//    new HelloPDFBox();
//    new HtmlDisplay();
//new HelloJPedal();
//    new api.ont.Tester();//--------------
//    new api.wn.Tester();
//    new helper.gui.Tester();
//new SimilarityTest();
//   Print.prln(RegEx.removeOtherCharacters("Hero123Honda_MotorBike#2partner_name is HeroHonda againMine"));
//    Print.prln(new Thesaurus("time").toString());
//    new MappingAlgorithm();
//    new UIMappingSelector();
//    new MappingData().readFile();
//    new Topic();
//         new Test();
//     new NewJFrame().setVisible(true);
//         new UIMain();

//
//
//    new UINodeMatrixPanel(new JFrame());
//    new DOS();
//
//
//                    new ParagraphAnnotator();
//                    new ParagraphReader();
//                    new StructurePredictor();
//                    new UIStyleSelector_V3();
//             ======================================
//                    1. SemReg population
//                     new EntityAnnotator_V3();
//                    new _2_EntityReader_V3();
//                    new _3_SemReg_V4_Populator();
//                    2. ontoReg population
//                    new SemToOnto();
//                    3. task annotation
//                    new TaskAnnotator();
//                    4. matching algorithm
//                    new MappingApplication().fillAllMatched();
//             ======================================
//                      new XmlWriter_1();
//                      new XmlWriter();
//                      new XmlTest();
//                    new Mapping_V2();
//                    new StyleExtractor_V3();
//                    new StructureReader().extract();
//                    new HelloPDFBox();
//                    new EntityReader_V2();
//                    new UIStyleSelector_V3();
//                    new WordNetTest();
//                    new RegulationToXml().extract();
//                    new MyReader();
//                    new MyWriter();
//                    new Splitter();
//                    new RegEx();
//
//    new zzz_MappingData().getDataArrays();
//new GUIMapping();
//        new SwixGUI();
//    new MapDetails();
//    new ComplianceChecker();
//   ResultData rd = new ResultData();
//   rd.readFile();
//   rd.writeFile();
//    new UIComplianceChecker1(); //----------------------------------------------
//    new OntoRegChecker(); // spin based
//    new OntoRegOrdering();
//        new api.ont.Tester();//---------------------
//    new Cardinality();//----------------------------------------------------------------
//    new SwrlOwlJess();
//    new OntoReg().create();
//        new MappingToCsv();
//    new ExtractionToCsv();
//    new Explanations();
//  new  _1_MappingAlgorithm();
//    new Violation().readFile();
//    new IndividualViolation();
//    new RcmExtendedFrame();
//    String indName = "TankCleanTask101";
//    new UIViolationDetails(indName);
//    new ViolationCollector();
//    new HelloCssParser();
//    new StanfordParser(); //----------------------------------------------------------------
//    new RegulationtSentenceReader();
//    new RefinedSentenceReader();
//    new Chunker();//  creates paresed chunk (small file)
//    new GazGenerator(); // creates gazetteers for the paresed sentences//----------------------------
//    new _0_OntoGazCreator();
//        new GazCreater();

//    new _Tester();
//    new api.wn._Tester();
//    new api.gt.SerialDatastoreIO();
//    new DatastoreReader();
//    new DifferenceTableCreator();
//  DifferenceTable table =  new DifferenceTable();
//  table.readFile();
//  double value = table.getDifference("Equipment", "name");
//  Print.prln("value = "+value)  ;
//    new RegTaskCollector();
//    new ThreeScoresGenerator();
//    new RegEx();
//  ThreeScores ts =   new ThreeScores();
//  ts.read();
//  ts.generateMappingId();
//  ts.writeCsv();
    
//    new ExistingMappingCollector();
//    new MappingAlgorithms();

    new ComputedMappingCollector();
    new MappingResultProcessor();
//    new MappingResultProcessorTunner();
//    new Similarity();
//  new MappingAlgorithmsTuner();
    
    
    }


}
