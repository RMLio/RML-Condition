
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
    private Set<PredicateObjectMap> fallbackPOMs;
    
    // Log
    private static final Logger log =
            LoggerFactory.getLogger(
            StdConditionPredicateObjectMap.class.getSimpleName());

    /**
     *
     * @param predicateMaps
     * @param objectMaps
     * @param referencingObjectMaps
     * @param condition
     * @param fallbackPOMs
     */
    public StdConditionPredicateObjectMap(Set<PredicateMap> predicateMaps,
            Set<ObjectMap> objectMaps,
            Set<ReferencingObjectMap> referencingObjectMaps,
            Set<Condition> condition, Set<PredicateObjectMap> fallbackPOMs) {
        super(predicateMaps, objectMaps, referencingObjectMaps);
        setCondition(condition);
        setFallbackPOMs(fallbackPOMs);
    }

    /**
     *
     * @param booleanConditions
     */
    protected final void setCondition(Set<Condition> booleanConditions) {
        this.conditions = booleanConditions;
    }

    /**
     *
     * @return
     */
    public Set<Condition> getConditions() {
        return this.conditions;
    }
    
    /**
     *
     * @param fallbackPOMs
     */
    protected final void setFallbackPOMs(Set<PredicateObjectMap> fallbackPOMs){
        this.fallbackPOMs = fallbackPOMs;
    }
    
    /**
     *
     * @return
     */
    public Set<PredicateObjectMap> getFallbackPOMs(){
        return this.fallbackPOMs;
    }

}
