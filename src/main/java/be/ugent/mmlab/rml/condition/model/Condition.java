package be.ugent.mmlab.rml.condition.model;

import be.ugent.mmlab.rml.model.RDFTerm.ReferencingObjectMap;
import java.util.Set;

/**
 * *************************************************************************
 *
 * RML - Conditions : Condition
 *
 *
 * @author andimou
 *
 ***************************************************************************
 */
abstract public interface Condition {
    
    /**
     *
     * @return
     */
    public String getCondition();
    
    /**
     *
     * @return
     */
    public String getReference();
    
    /**
     *
     * @return
     */
    public Set<Condition> getNestedConditions();
    
    /**
     *
     * @return
     */
    public ReferencingObjectMap getReferencingObjectMap();
    
    public Set<BindingCondition> getBindingConditions();
    
    public void setBindingConditions(Set<BindingCondition> nestedConditions);
    
}
