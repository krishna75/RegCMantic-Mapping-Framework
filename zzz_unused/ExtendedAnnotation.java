
package zzz_unused;

import gate.FeatureMap;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Krishna Sapkota, 30-Sep-2011,   14:23:17
 * A PhD project at Oxford Brookes University
 */
public class ExtendedAnnotation {
    protected long startNode;
    protected long endNode;
    protected String type;
    protected Integer id;
    protected HashMap features;

    public ExtendedAnnotation() {
        init();
    }

    public ExtendedAnnotation(gate.Annotation ann) {
        init();
        this.startNode = ann.getStartNode().getOffset().longValue();
        this.endNode = ann.getEndNode().getOffset().longValue();
        this.type = ann.getType();
        this.id = ann.getId();
        FeatureMap featureMap = ann.getFeatures();
        Iterator iterKeys = featureMap.keySet().iterator();
        while (iterKeys.hasNext()){
            String key = (String) iterKeys.next();
            features.put(key, featureMap.get(key));
        }

    }
    private void init(){
       features = new HashMap();
    }


    /**
     * Get the value of features
     *
     * @return the value of features
     */
    public HashMap getFeatures() {
        return features;
    }

    /**
     * Set the value of features
     *
     * @param features new value of features
     */
    public void setFeatures(HashMap features) {
        this.features = features;
    }


    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(String type) {
        this.type = type;
    }


    /**
     * Get the value of endNode
     *
     * @return the value of endNode
     */
    public long getEndNode() {
        return endNode;
    }

    /**
     * Set the value of endNode
     *
     * @param endNode new value of endNode
     */
    public void setEndNode(long endNode) {
        this.endNode = endNode;
    }


    /**
     * Get the value of startNode
     *
     * @return the value of startNode
     */
    public long getStartNode() {
        return startNode;
    }

    /**
     * Set the value of startNode
     *
     * @param startNode new value of startNode
     */
    public void setStartNode(long startNode) {
        this.startNode = startNode;
    }

    @Override
    public String toString(){
        String  text = "";
        text= "type="+type+", startNode="+startNode+", endNode="+endNode+", text="+features.get("text");
        return text;
    }

}
