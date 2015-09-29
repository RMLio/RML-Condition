package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.model.JoinCondition;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.std.StdReferencingObjectMap;
import java.util.Set;

/**
 * RML - Binding Condition: Binding Referencing Object Map
 *
 * @author andimou
 */
public class BindingReferencingObjectMap extends StdReferencingObjectMap{
    
    Set<BindingCondition> bindingConditions;
    
    public BindingReferencingObjectMap(PredicateObjectMap predicateObjectMap,
            TriplesMap parentTriplesMap, Set<JoinCondition> joinConditions, 
            Set<BindingCondition> bindingConditions) {
        super(predicateObjectMap, parentTriplesMap, joinConditions);
        setBindingConditions(bindingConditions);
    }
    
    protected final void setBindingConditions(
            Set<BindingCondition> bindingConditions){
        this.bindingConditions = bindingConditions;
    }
    
    public Set<BindingCondition> getBindingConditions(){
        return this.bindingConditions;
    }

}
