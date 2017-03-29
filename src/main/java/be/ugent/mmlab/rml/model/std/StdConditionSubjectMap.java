package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.RDFTerm.SubjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.TermMap;
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
public class StdConditionSubjectMap extends StdSubjectMap implements TermMap, SubjectMap {
    Set<Condition> conditions;

    public StdConditionSubjectMap(TriplesMap ownTriplesMap, Value constantValue,
            String stringTemplate, IRI termType, String inverseExpression,
            ReferenceMap referenceValue, Set<IRI> classIRIs,
            GraphMap graphMap, Set<Condition> conditions) {
        // No Literal term type
        // ==> No datatype
        // ==> No specified language tag
        super(ownTriplesMap, constantValue, stringTemplate, termType,
                inverseExpression, referenceValue, classIRIs, graphMap, null);
        
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
