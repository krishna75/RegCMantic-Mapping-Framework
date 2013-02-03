
package api.gt;

import gate.creole.ResourceInstantiationException;
import java.net.*;
import java.io.*;

import gate.*;
import gate.persist.SerialDataStore;
import gate.util.*;
import gate.security.*;
import helper.util.MyWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcm.main.Settings;

/**
 * It reads corpus from serial data-store
 * @author Krishna Sapkota 22-Mar-2011  09:53:57
 */
public class SerialDatastoreIO {
    
  /* corpus path and id */
  public static final String DS_DIR         =   "file:///"+ Settings.SERIAL_DATASTORE_PATH;
  public static final String CORP_ID        =   "selected_corpus___1342780213481___1091";
  
  public static final String DATASTORE_TYPE =   "gate.persist.SerialDataStore";
  public static final String CORP_RESOURCE  =   "gate.corpora.SerialCorpusImpl";
  public static final String DOC_RESOURCE   =   "gate.corpora.SerialCorpusImpl";
  Corpus corpus = null;

  public SerialDatastoreIO() {
      //readCorpusFromSerialDatastore(null);

  }

  public  Corpus readCorpusFromSerialDatastore(String corpusId) {

    /*   1. Init gate */
    try {
      Gate.init();
    }
    catch (GateException gex) {
      Err.prln("Cannot initialuze GATE...");
      gex.printStackTrace();
    }

    /*  2. Open An Existing Serial Data Store */
    SerialDataStore datastore = null;
    try { 
        
      /* pass the datastore class and path as parameteres */
      datastore  = (SerialDataStore)Factory.
                                openDataStore(DATASTORE_TYPE, DS_DIR);
      datastore.open();
      }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    Out.prln("serial datastore opened...");

    /* 3. Load document from datastore */
    FeatureMap features = Factory.newFeatureMap();
    features.put(DataStore.LR_ID_FEATURE_NAME, CORP_ID);
    features.put(DataStore.DATASTORE_FEATURE_NAME, datastore);
    try {
        corpus = (Corpus) Factory.createResource(CORP_RESOURCE, features);
    } catch (ResourceInstantiationException ex) {
        ex.printStackTrace();
    }
    Out.prln("corpus loaded from datastore...");
    return corpus;
  }
  
  public void writeCorpus(String outfile){
      MyWriter.write(corpus, outfile);
      
  }
}