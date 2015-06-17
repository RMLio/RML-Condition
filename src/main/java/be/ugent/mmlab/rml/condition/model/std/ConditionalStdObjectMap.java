/***************************************************************************
 *
 * RML - Conditions : ObjectMap Class with conditions
 *
 * 
 * @author andimou
 * 
 ****************************************************************************/
package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.model.ObjectMap;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.TermType;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.condition.model.BindCondition;
import be.ugent.mmlab.rml.condition.model.BooleanCondition;
import be.ugent.mmlab.rml.condition.model.ProcessCondition;
import be.ugent.mmlab.rml.condition.model.SplitCondition;
import be.ugent.mmlab.rml.model.reference.ReferenceIdentifier;
import be.ugent.mmlab.rml.model.std.StdObjectMap;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 *
 * @author andimou
 */
public final class ConditionalStdObjectMap extends StdObjectMap implements ObjectMap { //conditionalTermMap
    
    // Log
    private static final Logger log = LogManager.getLogger(ConditionalStdObjectMap.class);

    private PredicateObjectMap predicateObjectMap;

        
        /**
        *
        * @param predicateObjectMap
        * @param constantValue
        * @param dataType
        * @param languageTag
        * @param stringTemplate
        * @param termType
        * @param inverseExpression
        * @param referenceValue
        * @param split
        * @param process
        * @param replace
        */
       public ConditionalStdObjectMap(PredicateObjectMap predicateObjectMap,
			Value constantValue, URI dataType, String languageTag,
			String stringTemplate, URI termType, String inverseExpression,
			ReferenceIdentifier referenceValue, String split,
                        String process, String replace)  {
		super(predicateObjectMap, constantValue, dataType, languageTag,
			stringTemplate, termType, inverseExpression, referenceValue);
		setPredicateObjectMap(predicateObjectMap);
	}
        
        /**
        *
        * @param predicateObjectMap
        * @param constantValue
        * @param dataType
        * @param languageTag
        * @param stringTemplate
        * @param termType
        * @param inverseExpression
        * @param referenceValue
        * @param split
        * @param process
        * @param replace
        * @param booleanCondition
        * @param processCondition
        * @param splitCondition
        * @param bindCondition
        */
       public ConditionalStdObjectMap(PredicateObjectMap predicateObjectMap,
			Value constantValue, URI dataType, String languageTag,
			String stringTemplate, URI termType, String inverseExpression,
			ReferenceIdentifier referenceValue, String split,
                        String process, String replace, 
                        Set<BooleanCondition> booleanCondition, Set<ProcessCondition> processCondition, 
                        Set<SplitCondition> splitCondition, Set<BindCondition> bindCondition) {
		super(predicateObjectMap, constantValue, dataType, languageTag,
			stringTemplate, termType, inverseExpression, referenceValue);
		setPredicateObjectMap(predicateObjectMap);
	}

        @Override
        protected void checkSpecificTermType(TermType tt) {
            // If the term map is a subject map: rr:IRI or rr:BlankNode or
            // rr:Literal
            if ((tt != TermType.IRI) && (tt != TermType.BLANK_NODE)
                    && (tt != TermType.LITERAL)) {
                log.error("Invalid Structure "
                        + "[StdObjectMap:checkSpecificTermType] If the term map is a "
                        + "object map: only rr:IRI or rr:BlankNode is required");
            }
        }

        @Override
	public PredicateObjectMap getPredicateObjectMap() {
		return predicateObjectMap;
	}

        /**
        *
        * @param predicateObjectMap
        */
       @Override
	public void setPredicateObjectMap(PredicateObjectMap predicateObjectMap) {
		/*
		 * if (predicateObjectMap.getObjectMaps() != null) { if
		 * (!predicateObjectMap.getObjectMaps().contains(this)) throw new
		 * IllegalStateException( "[StdObjectMap:setPredicateObjectMap] " +
		 * "The predicateObject map parent " +
		 * "already contains another Object Map !"); } else {
		 */
		// Update predicateObjectMap if not contains this object map
		if (predicateObjectMap != null) {
			if (predicateObjectMap.getObjectMaps() == null)
				predicateObjectMap.setObjectMaps(new HashSet<ObjectMap>());
			predicateObjectMap.getObjectMaps().add(this);
			// }
		}
		this.predicateObjectMap = predicateObjectMap;
	}
        
        @Override
	public void setOwnTriplesMap(TriplesMap ownTriplesMap) {   
            this.ownTriplesMap = ownTriplesMap;
	}
}
