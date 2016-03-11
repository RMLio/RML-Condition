package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
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
            LogManager.getLogger(StdCondition.class.getSimpleName());
    
    protected String reference;
    protected String condition;
    
    protected Set<Condition> nestedConditions ;
    protected Set<BindingCondition> bindingConditions ;
    protected ReferencingObjectMap refObjMap;
    
    /**
     *
     * @param reference
     */
    protected void setReference(String reference) {
        if (reference == null) {
            log.error(
                    "A condition must "
                    + "have a condition value.");
        }
        this.reference = reference;
    }
    
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
    
    public void setBindingConditions(Set<BindingCondition> nestedConditions) {
        this.bindingConditions = bindingConditions;
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
    
    public Set<BindingCondition> getBindingConditions() {
        return bindingConditions;
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
    public String getReference() {
        return reference;
    }   
    
    /**
     *
     * @return
     */
    public ReferencingObjectMap getReferencingObjectMap(){
        return this.refObjMap;
    }
}
