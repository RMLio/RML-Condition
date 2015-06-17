/***************************************************************************
 *
 * RML - Conditions : SubjectMap Class with conditions
 * 
 * @author andimou
 *
 ****************************************************************************/
package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.model.GraphMap;
import be.ugent.mmlab.rml.model.SubjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.condition.model.BindCondition;
import be.ugent.mmlab.rml.condition.model.BooleanCondition;
import be.ugent.mmlab.rml.condition.model.ProcessCondition;
import be.ugent.mmlab.rml.condition.model.SplitCondition;
import be.ugent.mmlab.rml.model.reference.ReferenceIdentifier;
import be.ugent.mmlab.rml.model.std.StdObjectMap;
import be.ugent.mmlab.rml.model.std.StdSubjectMap;

import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.openrdf.model.URI;
import org.openrdf.model.Value;

public class ConditionalStdSubjectMap extends StdSubjectMap implements SubjectMap {

    // Log
    private static final Logger log = LogManager.getLogger(StdObjectMap.class);

    /*public conditionalStdSubjectMap(TriplesMap ownTriplesMap, Value constantValue,
     String stringTemplate, URI termType, String inverseExpression,
     ReferenceIdentifier referenceValue, Set<URI> classIRIs, Set<GraphMap> graphMaps) {
     // No Literal term type
     // ==> No datatype
     // ==> No specified language tag
     super(constantValue, null, null, stringTemplate, termType,
     inverseExpression, referenceValue);
     setClassIRIs(classIRIs);
     setGraphMaps(graphMaps);
     setOwnTriplesMap(ownTriplesMap);
     }*/
    public ConditionalStdSubjectMap(TriplesMap ownTriplesMap, Value constantValue,
            String stringTemplate, URI termType, String inverseExpression,
            ReferenceIdentifier referenceValue, Set<URI> classIRIs, Set<GraphMap> graphMaps,
            String split, String process, String replace) {
        super(ownTriplesMap, constantValue,
                stringTemplate, termType, inverseExpression,
                referenceValue, classIRIs, graphMaps);
    }
        
     /**
     *
     * @param ownTriplesMap
     * @param constantValue
     * @param stringTemplate
     * @param termType
     * @param inverseExpression
     * @param referenceValue
     * @param classIRIs
     * @param graphMaps
     * @param split
     * @param process
     * @param replace
     * @param booleanCondition
     * @param processCondition
     * @param splitCondition
     * @param bindCondition
     */
    public ConditionalStdSubjectMap(TriplesMap ownTriplesMap, Value constantValue,
            String stringTemplate, URI termType, String inverseExpression,
            ReferenceIdentifier referenceValue, Set<URI> classIRIs, Set<GraphMap> graphMaps,
            String split, String process, String replace,
            Set<BooleanCondition> booleanCondition, Set<ProcessCondition> processCondition,
            Set<SplitCondition> splitCondition, Set<BindCondition> bindCondition) {
        super(ownTriplesMap, constantValue,
                stringTemplate, termType, inverseExpression,
                referenceValue, classIRIs, graphMaps);
        //setClassIRIs(classIRIs);
        //setGraphMaps(graphMaps);
        //setOwnTriplesMap(ownTriplesMap);
    }

}
