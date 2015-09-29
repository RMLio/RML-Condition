package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.model.RDFTerm.ReferencingObjectMap;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * RML - Model
 *
 * @author andimou
 */
public class StdCondition {
    
    // Log
    private static final Logger log = 
            LogManager.getLogger(StdCondition.class);
    
    protected String condition;
    protected String value;
    protected Set<Condition> nestedConditions ;
    protected ReferencingObjectMap refObjMap;
    
    /**
     *
     * @param condition
     */
    protected void setCondition(String condition) {
        if (condition == null) {
            log.error(
                    "A condition must "
                    + "have a condition value.");
        }
        this.condition = condition;
    }
    
    /**
     *
     * @param nestedConditions
     */
    protected void setNestedConditions(Set<Condition> nestedConditions) {
        this.nestedConditions = nestedConditions;
    }
    
    protected void setReferencingObjectMap(ReferencingObjectMap refObjMap){
        this.refObjMap = refObjMap;
    }
    
    /**
     *
     * @return
     */
    public Set<Condition> getNestedConditions() {
        return nestedConditions;
    }
    
    /**
     *
     * @return
     */
    public String getCondition() {
        return condition;
    }
    
    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }   
    
    /**
     *
     * @return
     */
    public ReferencingObjectMap getReferencingObjectMap(){
        return this.refObjMap;
    }
}
