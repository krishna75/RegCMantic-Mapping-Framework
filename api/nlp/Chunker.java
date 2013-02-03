

package api.nlp;

import api.xml.XmlReader;
import helper.util.Converter;
import helper.util.MyReader;
import helper.util.MyWriter;
import helper.util.Print;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import rcm.main.Settings;

/**
 * Chunk = small file
 * @author Krishna Sapkota,  May 17, 2012,  11:25:26 PM
 * A PhD project at Oxford Brookes University
 */
public class Chunker {
    /* IO Files*/
    String path = Settings.PHASE2 + "after_parsing/parser/";    
    String IO_SEN_INDEX_FILE = path + "sen_index.txt";
    String IO_SENTENCE_CHUNK = path + "sentence_chunk_";
    String IO_WRAPPER_INDEX_FILE = path + "wrapper_index.txt";
    String IO_WRAPPER_CHUNK = path + "wrapper_chunk_";
    
    /* Adapter Classes*/
    StanfordParser parser = new StanfordParser();
    
    /* Local Variables */
    AbstractSentenceReader reader = new RefinedSentenceReader(null, null);
    SentenceWrapper wrapper = new SentenceWrapper(null);
    XmlReader xml = new XmlReader();
    
    int CHUNK_SIZE = 50 ;
    ArrayList<String> senChunkList = new ArrayList<String>();
    ArrayList<String> wrapperChunkList = new ArrayList<String>();
    
    
    
    public Chunker() {
//       chunkSentence();
       readChunkedSentence();
    }
    
 /**
     *  Chunk = small file
     *  it divides a big file in to several small files of sentences.
     */
    public void chunkSentence(){
        
        ArrayList<String> senList =  reader.getSenTextList();
        int n = 1 + senList.size()/CHUNK_SIZE;
        Print.prln(" n= "+ n);
        for (int i = 0; i<n ; i++){
           int start = CHUNK_SIZE * i;
           int end ;
           if (i<n-1){
               end = CHUNK_SIZE * i+ CHUNK_SIZE;
           }else {
               end = senList.size()-1;
           }
           List<String> subList =  senList.subList( start, end);
           ArrayList<String> chunkList = Converter.listToArrayList(subList);
           String filename = IO_SENTENCE_CHUNK + (i+1)+".xml";
           senChunkList.add(filename);
           reader.writeFile(chunkList, filename);  
        }
        MyWriter.write(senChunkList, IO_SEN_INDEX_FILE);
    }
    
    public void readChunkedSentence(){
        ArrayList<String> fileList = MyReader.fileToArrayList(IO_SEN_INDEX_FILE);
        int count = 1;
        for (String file: fileList){
            
            // just process first file
            if (fileList.indexOf(file)>0){
                break;
            }
            String filename = IO_WRAPPER_CHUNK + (count++) +".xml";
            wrapperChunkList.add(filename);
            ArrayList<String> senList = new ArrayList<String>();           
            ArrayList<Element> senElementList =  xml.getXmlDomElements(file, "sentence");
            for (Element sen: senElementList){
                String senText = sen.getTextContent();
                senList.add(senText);
            }
            
            // get parsed list
            ArrayList<SentenceWrapper> wpList = parser.processSentenceList(senList);
            
            // write the wp list
            wrapper = new SentenceWrapper(null);
            wrapper.setWrapperList(wpList); 
            wrapper.write(filename);  
        }
         MyWriter.write(wrapperChunkList, IO_WRAPPER_INDEX_FILE);
    }
    

}
