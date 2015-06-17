package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.sesame.RMLSesameDataSet;
import be.ugent.mmlab.rml.vocabulary.CRMLVocabulary;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class ConditionExtractor {

    //Log
    private static final Logger log = LogManager.getLogger(ConditionExtractor.class);
    
   
    /**
     *
     * @param rmlMappingGraph
     * @param parentCondition
     * @return
     */
    public static Set<Condition> extractNestedConditions(
            RMLSesameDataSet rmlMappingGraph, Resource parentCondition) {
        Set<Condition> conditions = new HashSet<Condition>();
        try{
            //retrieves nested boolean conditions
            URI p = rmlMappingGraph.URIref(
                    CRMLVocabulary.CRML_NAMESPACE + CRMLVocabulary.cRMLTerm.BOOLEAN_CONDITION);
            List<Statement> statements = rmlMappingGraph.tuplePattern(parentCondition, p, null);
            
            for(Statement statement : statements){
                conditions.addAll(BooleanConditionExtractor.extractBooleanCondition(
                        rmlMappingGraph, (Resource) statement.getObject()));
            }    
            
            //retrieve nested process conditions
            p = rmlMappingGraph.URIref(
                    CRMLVocabulary.CRML_NAMESPACE + CRMLVocabulary.cRMLTerm.PROCESS_CONDITION);
            statements = rmlMappingGraph.tuplePattern(parentCondition, p, null);
            
            for(Statement statement : statements){
                conditions.addAll(ProcessConditionExtractor.extractProcessCondition(
                        rmlMappingGraph, (Resource) statement.getSubject()));
            }    
            
            //retrieve nested split conditions
            p = rmlMappingGraph.URIref(
                    CRMLVocabulary.CRML_NAMESPACE + CRMLVocabulary.cRMLTerm.SPLIT_CONDITION);
            statements = rmlMappingGraph.tuplePattern(parentCondition, p, null);
            
            for(Statement statement : statements){
                conditions.addAll(SplitConditionExtractor.extractSplitCondition(
                        rmlMappingGraph, (Resource) statement.getSubject()));
            }  
            
        } catch(Exception ex){
            log.error(ex);
        }
        return conditions;
    }

    /**
     *
     * @param rmlMappingGraph
     * @param object
     * @param statement
     * @return
     */
    public static List<String> extractCondition(
            RMLSesameDataSet rmlMappingGraph, Resource object, Statement statement) {
        
        List<String> listConditions = new ArrayList<String>();

        try {
            //assigns current boolean condtion
            Resource splitCond = (Resource) statement.getObject();

            //retrieves condition
            URI p = rmlMappingGraph.URIref(
                    CRMLVocabulary.CRML_NAMESPACE + CRMLVocabulary.cRMLTerm.CONDITION);

            List<Statement> conditionStatements = rmlMappingGraph.tuplePattern(splitCond, p, null);
            if (conditionStatements.size() > 0) {
                for (Statement conditionStatement : conditionStatements) {
                    String condition = conditionStatement.getObject().stringValue();
                    listConditions.add(condition);
                }
            }

        } catch (ClassCastException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "A resource was expected in object of predicateMap of "
                    + object.stringValue());
        } finally {
            return listConditions;
        }
    }
    
    /**
     *
     * @param rmlMappingGraph
     * @param object
     * @param statement
     * @return
     */
    public static List<String> extractValue(
            RMLSesameDataSet rmlMappingGraph, Resource object, Statement statement) {
        List<String> listValues = new ArrayList<String>();
        
        try {
            //assigns current boolean condtion
            Resource values = (Resource) statement.getObject();
            
            //retrieves value
            URI p = rmlMappingGraph.URIref(
                    CRMLVocabulary.CRML_NAMESPACE + CRMLVocabulary.cRMLTerm.VALUE);
            
            List<Statement> statements = rmlMappingGraph.tuplePattern(values, p, null);
            if (statements.size() > 0) {
                for (Statement valueStatement : statements) {
                    String condition = valueStatement.getObject().stringValue();
                    listValues.add(condition);
                }
            }
 
        }catch (ClassCastException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "A valid value was expected "
                    + object.stringValue());
        } finally {
            return listValues;
        }
    }
}
