package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.model.JoinCondition;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.RDFTerm.ReferencingObjectMap;
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
            Set<Condition> conditions, GraphMap graphMap) {
        super(predicateObjectMap, parentTriplesMap, joinConditions, graphMap);
        setConditions(conditions);
    }
    
    public ConditionReferencingObjectMap(PredicateObjectMap predicateObjectMap,
            TriplesMap parentTriplesMap, Set<JoinCondition> joinConditions, 
            Set<Condition> conditions, Set<ReferencingObjectMap> fallbackReferencingObjectMaps, GraphMap graphMap) {
        super(predicateObjectMap, parentTriplesMap, joinConditions, fallbackReferencingObjectMaps, graphMap);
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
