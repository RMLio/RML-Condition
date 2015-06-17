package be.ugent.mmlab.rml.condition.extractor;

import static be.ugent.mmlab.rml.condition.extractor.ConditionExtractor.extractNestedConditions;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.condition.model.BooleanCondition;
import be.ugent.mmlab.rml.model.std.StdBooleanCondition;
import be.ugent.mmlab.rml.sesame.RMLSesameDataSet;
import be.ugent.mmlab.rml.vocabulary.CRMLVocabulary;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;

/**
 * RML - Conditions
 *
 * @author andimou
 */
public class BooleanConditionExtractor extends ConditionExtractor{
    
    //Log
    private static final Logger log = LogManager.getLogger(BooleanConditionExtractor.class);
    
    public static Set<BooleanCondition> extractBooleanCondition(
            RMLSesameDataSet rmlMappingGraph, Resource object){
        
        Set<BooleanCondition> result = new HashSet<BooleanCondition>();
        List<String> conditions = new ArrayList<String>(), 
                values = new ArrayList<String>();
        
        log.debug(
                Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Extract boolean conditions..");
        
        // Extract boolean condition
        URI p = rmlMappingGraph.URIref(
                CRMLVocabulary.CRML_NAMESPACE + CRMLVocabulary.cRMLTerm.BOOLEAN_CONDITION);
        List<Statement> statements = rmlMappingGraph.tuplePattern(object, p, null);

        try {
            for (Statement statement : statements) {
                conditions = extractCondition(rmlMappingGraph, object, statement);
                values = extractValue(rmlMappingGraph, object, statement);

                for (String condition : conditions) {
                    Set<Condition> nestedConditions = 
                    extractNestedConditions(rmlMappingGraph, (Resource) statement.getObject());
                    
                    for (String value : values) {
                        if (value == null || condition == null) {
                            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                                    + object.stringValue()
                                    + " must have exactly two properties condition and value. ");
                        }
                        try {
                            result.add(new StdBooleanCondition(condition, value, nestedConditions));
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(
                                    BooleanConditionExtractor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (ClassCastException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "A resource was expected in object of predicateMap of "
                    + object.stringValue());
        } 
        log.debug("Extract boolean condition done.");
        return result;
    }
    
}
