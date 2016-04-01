
package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.ObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.PredicateMap;
import be.ugent.mmlab.rml.model.RDFTerm.ReferencingObjectMap;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RML Processor
 *
 * @author andimou
 */
public class StdConditionPredicateObjectMap extends StdPredicateObjectMap implements PredicateObjectMap {
    private Set<Condition> conditions;
    
    // Log
    private static final Logger log =
            LoggerFactory.getLogger(
            StdConditionPredicateObjectMap.class.getSimpleName());

    public StdConditionPredicateObjectMap(Set<PredicateMap> predicateMaps,
            Set<ObjectMap> objectMaps,
            Set<ReferencingObjectMap> referencingObjectMaps,
            Set<Condition> condition) {
        super(predicateMaps, objectMaps, referencingObjectMaps);
        setCondition(condition);
    }

    protected final void setCondition(Set<Condition> booleanConditions) {
        this.conditions = booleanConditions;
    }

    public Set<Condition> getConditions() {
        return this.conditions;
    }

}
