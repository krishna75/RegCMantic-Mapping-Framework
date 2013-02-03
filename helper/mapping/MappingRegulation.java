/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.mapping;

import java.util.ArrayList;

/**
 *
 * @author Krishna Sapkota,  Jul 24, 2012,  3:16:17 PM
 * A PhD project at Oxford Brookes University
 */
public class MappingRegulation {
    ArrayList<MappingStmt> stmtList = new ArrayList<MappingStmt>();
    String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    

    public ArrayList<MappingStmt> getStmtList() {
        return stmtList;
    }

    public void setStmtList(ArrayList<MappingStmt> stmtList) {
        this.stmtList = stmtList;
    }
    
    // not used yet
    public MappingStmt getStmt(String stmtId){
        MappingStmt stmt = new MappingStmt();
        for (MappingStmt stmt2:stmtList ){
            if (stmtId.equals(stmt2.getId())) {
                stmt = stmt2;
            }
        }
        return stmt;
    }
    
}
