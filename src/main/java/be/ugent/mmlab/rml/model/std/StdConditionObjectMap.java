package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.RDFTerm.ObjectMap;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.TermMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.termMap.ReferenceMap;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

/**
 *************************************************************************
 *
 * RML - Model : ObjectMap Implementation
 *
 * An Object Map is a specific Term Map used for generating an RDF object. 
 * 
 * @author mielvandersande, andimou
 * 
 ***************************************************************************
 */
public class StdConditionObjectMap extends StdObjectMap implements TermMap, ObjectMap {
    private Set<Condition> conditions;
    
    // Log
    private static final Logger log = 
            LoggerFactory.getLogger(StdConditionObjectMap.class.getSimpleName());

    public StdConditionObjectMap(TriplesMap triplesMap, PredicateObjectMap predicateObjectMap,
                                 Value constantValue, IRI dataType, String languageTag,
                                 String stringTemplate, IRI termType, String inverseExpression,
                                 ReferenceMap referenceValue, Set<Condition> condition, GraphMap graphMap) {
        super(triplesMap, predicateObjectMap, constantValue, dataType, languageTag,
                stringTemplate, termType, inverseExpression, referenceValue, graphMap);
        setCondition(condition);
    }
    
    protected final void setCondition(Set<Condition> booleanConditions){
        this.conditions = booleanConditions;
}
    
    public Set<Condition> getConditions(){
        return this.conditions;
    }
}
