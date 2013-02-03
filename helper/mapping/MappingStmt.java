/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.mapping;

/**
 *
 * @author Krishna Sapkota,  Jul 24, 2012,  3:29:12 PM
 * A PhD project at Oxford Brookes University
 */
public class MappingStmt {
    AbstractMappingEntity topic = new MappingTopic();
    MappingCore core = new MappingCore();
    AbstractMappingEntity aux = new MappingAux();
    
    String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AbstractMappingEntity getAux() {
        return aux;
    }

    public void setAux(AbstractMappingEntity aux) {
        this.aux = aux;
    }

    public MappingCore getCore() {
        return core;
    }

    public void setCore(MappingCore core) {
        this.core = core;
    }

    public AbstractMappingEntity getTopic() {
        return topic;
    }

    public void setTopic(AbstractMappingEntity topic) {
        this.topic = topic;
    }
    

}
