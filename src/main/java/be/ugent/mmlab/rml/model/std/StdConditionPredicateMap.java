package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.termMap.ReferenceMap;
import java.util.Set;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

/**
 * RML Processor
 *
 * @author andimou
 */
public class StdConditionPredicateMap extends StdPredicateMap {
    Set<Condition> conditions;
    
    public StdConditionPredicateMap(TriplesMap triplesMap, PredicateObjectMap predicateObjectMap,
            Value constantValue, String stringTemplate,String inverseExpression, 
            ReferenceMap referenceValue, IRI termType, Set<Condition> conditions, GraphMap graphMap) {

        super(triplesMap, predicateObjectMap, constantValue, stringTemplate, 
                inverseExpression, referenceValue, termType, graphMap);
        
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
