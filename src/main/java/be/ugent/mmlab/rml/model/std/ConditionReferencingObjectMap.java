package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.model.JoinCondition;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import java.util.Set;

/**
 * RML - Binding Condition: Binding Referencing Object Map
 *
 * @author andimou
 */
public class ConditionReferencingObjectMap extends StdReferencingObjectMap{
    
    Set<Condition> conditions;
    
    public ConditionReferencingObjectMap(PredicateObjectMap predicateObjectMap,
            TriplesMap parentTriplesMap, Set<JoinCondition> joinConditions, 
            Set<Condition> bindingConditions) {
        super(predicateObjectMap, parentTriplesMap, joinConditions);
        setConditions(bindingConditions);
    }
    
    protected final void setConditions(
            Set<Condition> bindingConditions){
        this.conditions = bindingConditions;
    }
    
    public Set<Condition> getConditions(){
        return this.conditions;
    }

}
