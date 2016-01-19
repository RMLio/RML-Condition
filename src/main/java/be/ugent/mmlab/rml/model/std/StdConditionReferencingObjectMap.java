package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.model.JoinCondition;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import java.util.Set;

/**
 * RML Condition
 *
 * @author andimou
 */
public class StdConditionReferencingObjectMap extends StdReferencingObjectMap {
    private Set<Condition> conditions;
    
    StdConditionReferencingObjectMap(PredicateObjectMap predicateObjectMap,
            TriplesMap parentTriplesMap, Set<JoinCondition> joinConditions, 
            Set<Condition> conditions){
        super(predicateObjectMap,parentTriplesMap, joinConditions);
        setConditions(conditions);
    }
    
    protected final void setConditions(
            Set<Condition> conditions){
        this.conditions = conditions;
    }
    
    public Set<Condition> getConditions(){
        return this.conditions;
    }

}
