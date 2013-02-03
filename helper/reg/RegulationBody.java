
package helper.reg;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota, 19-Sep-2011,   10:25:36
 * A PhD project at Oxford Brookes University
 */
public class RegulationBody extends AbstractEntity implements Serializable {

    private String name ;
    private ArrayList<RegulationDocument> documentList = new ArrayList<RegulationDocument>();

    /**
     * Get the value of documentList
     *
     * @return the value of documentList
     */
    public ArrayList<RegulationDocument> getDocumentList() {
        return documentList;
    }

    /**
     * Set the value of documentList
     *
     * @param documentList new value of documentList
     */
    public void setDocumentList(ArrayList<RegulationDocument> documentList) {
        this.documentList = documentList;
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


}
