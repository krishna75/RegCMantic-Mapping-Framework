
package helper.style;

import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota, 13-Aug-2011,   01:51:22
 * A PhD project at Oxford Brookes University
 */
public class HeadBody {
    ArrayList<StyleHead> headList ;
    ArrayList<StyleBody> bodyList ;

    public HeadBody(ArrayList<StyleHead> headList, ArrayList<StyleBody> bodyList) {
        this.headList = headList;
        this.bodyList = bodyList;
    }

    public HeadBody() {
    }

    public ArrayList<StyleBody> getBodyList() {
        return bodyList;
    }

    public void setBodyList(ArrayList<StyleBody> bodyList) {
        this.bodyList = bodyList;
    }

    public ArrayList<StyleHead> getHeadList() {
        return headList;
    }

    public void setHeadList(ArrayList<StyleHead> headList) {
        this.headList = headList;
    }
    

}
