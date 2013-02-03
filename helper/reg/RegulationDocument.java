
package helper.reg;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota, 19-Sep-2011,   10:26:24
 * A PhD project at Oxford Brookes University
 */
public class RegulationDocument extends AbstractEntity implements Serializable{

    private String version;
    private String name;
    private ArrayList<Topic> topicList = new ArrayList<Topic>();

    /**
     * Get the value of topicList
     *
     * @return the value of topicList
     */
    public ArrayList<Topic> getTopicList() {
        return topicList;
    }

    /**
     * Set the value of topicList
     *
     * @param topicList new value of topicList
     */
    public void setTopicList(ArrayList<Topic> topicList) {
        this.topicList = topicList;
    }


    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Get the value of version
     *
     * @return the value of version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the value of version
     *
     * @param version new value of version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
    return description;
}

}
