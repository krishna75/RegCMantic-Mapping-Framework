
package helper.reg;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota, 19-Sep-2011,   10:20:10
 * A PhD project at Oxford Brookes University
 */
public abstract  class AbstractEntity implements Serializable {
    String id = "";
    String description  = "";
    ArrayList<String> annotationList = new ArrayList<String>();

    public ArrayList<String> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(ArrayList<String> annotationList) {
        this.annotationList = annotationList;
    }
    
public AbstractEntity() {
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

}
