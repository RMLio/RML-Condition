package be.ugent.mmlab.rml.condition.model;

import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.FunctionTermMap;
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
    
    /**
     *
     * @return
     */
    public Set<BindingCondition> getBindingConditions();
    
    /**
     *
     * @param nestedConditions
     */
    public void setBindingConditions(Set<BindingCondition> nestedConditions);
    
    /**
     *
     * @return
     */
    public PredicateObjectMap getFallback();
    
    /**
     *
     * @param predicateObjectMap
     */
    public void setFallback(PredicateObjectMap predicateObjectMap);

    public Set<FunctionTermMap> getFunctionTermMaps();

    public void setFunctionTermMaps(Set<FunctionTermMap> functionTermMaps);
    
}
