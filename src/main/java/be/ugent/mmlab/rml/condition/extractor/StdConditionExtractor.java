package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.condition.model.std.StdBooleanCondition;
import be.ugent.mmlab.rml.condition.model.std.StdNegationCondition;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.vocabularies.CRMLVocabulary;
import be.ugent.mmlab.rml.vocabularies.RMLVocabulary;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;

/**
 * RML - Conditions : ConditionExtractor
 *
 * @author andimou
 */
public class StdConditionExtractor implements ConditionExtractor {

    //Log
    private static final Logger log = 
            LogManager.getLogger(StdConditionExtractor.class.getSimpleName());
       
    public static List<String> extractValue(
            Repository repository, Resource object, Statement statement) {
        List<String> listValues = new ArrayList<String>();
        
        try {
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            //assigns current boolean condtion
            Resource values = (Resource) statement.getObject();
            
            //retrieves value
            URI p = vf.createURI(
                    CRMLVocabulary.CRML_NAMESPACE 
                    + CRMLVocabulary.cRMLTerm.VALUE);

            RepositoryResult<Statement> statements = 
                    connection.getStatements(values, p, null, true);

            while (statements.hasNext()) {
                    Statement valueStatement = statements.next();
                    String condition = valueStatement.getObject().stringValue();
                    listValues.add(condition);
                }
            
 
        }catch (ClassCastException e) {
            log.error("A valid value was expected "
                    + object.stringValue());
        } finally {
            return listValues;
        }
    }
    
    /**
     *
     * @param rmlMappingGraph
     * @param object
     * @param statement
     * @return
     */
    //TODO: Replace the following with a Reference Map
    public static String extractReference(
            Repository repository, Resource object, Statement statement) {
        String reference = null;
        
        try {
            //assigns current boolean condtion
            Resource values = (Resource) statement.getObject();
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            //retrieves value
            URI p = vf.createURI(RMLVocabulary.RML_NAMESPACE 
                    + RMLVocabulary.RMLTerm.REFERENCE);
            RepositoryResult<Statement> statements = 
                    connection.getStatements(values, p, null, true);

            while(statements.hasNext()) {
                Statement valueStatement = statements.next();
                reference = valueStatement.getObject().stringValue();
            }
 
        }catch (ClassCastException e) {
            log.error("Class cast exception " + 
                    "A valid value was expected "
                    + object.stringValue());
        } finally {
            return reference;
        }
    }
    
    //TODO: Merge it with extractVariable
    public static List<String> extractExpression(
            Repository repository, Resource object) { //, Statement statement
        List<String> listValues = new ArrayList<String>();
        
        try {
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            //assigns current boolean condtion
            //Resource values = (Resource) statement.getObject();
            
            //retrieves value
            URI p = vf.createURI(
                    CRMLVocabulary.CRML_NAMESPACE 
                    + CRMLVocabulary.cRMLTerm.EXPRESSION);

            RepositoryResult<Statement> statements = 
                    connection.getStatements(object, p, null, true);

            while (statements.hasNext()) {
                    Statement valueStatement = statements.next();
                    String condition = valueStatement.getObject().stringValue();
                    listValues.add(condition);
                }
            
 
        }catch (ClassCastException e) {
            log.error("A valid value was expected "
                    + object.stringValue());
        } finally {
            return listValues;
        }
    }

    @Override
    //TODO: Check if it's still in use
    public Condition extractCondition(Repository repository, 
        Set<Condition> conditions, Resource object) {
        Condition condition = null;        
        try {
            RepositoryConnection connection = repository.getConnection();
            //extract boolean condition
            ValueFactory vf = connection.getValueFactory();
            URI p = null;
            String conditionType = this.getClass().getSimpleName();
            
            switch (conditionType) {
                case "BooleanConditionExtractor":
                    log.debug("Extracting Equality Conditions..");
                    // Extract boolean condition
                    p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.BOOLEAN_CONDITION);
                    //condition = extractBooleanCondition(
                    //          connection, repository, p, object, conditions);
                    break;
               case "NegationConditionExtractor":
                    log.debug("Extracting Negation Conditions..");
                    // Extract negation condition
                    p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.NEGATION_CONDITION);
                    //extractBooleanCondition(
                    //        connection, repository, p, object,conditions);
                    break;
               case "ProcessConditionExtractor":
                    log.debug("Extracting Processing Conditions..");
                    // Extract processing condition
                    p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.PROCESS_CONDITION);
                    break;
               default:
                   log.error("No suitable condition extraction is found");
            }
            
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        }
        return condition;
    }
    
    /**
     *
     * @param repository
     * @param object
     * @return
     */
    public Set<Resource> extractConditionResources(
            Repository repository, Resource object) {
        Set<Resource> conditionResources = new HashSet<Resource>();
        try {
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            URI p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                                + CRMLVocabulary.cRMLTerm.BOOLEAN_CONDITION);
            
            RepositoryResult<Statement> statements =
                    connection.getStatements(object, p, null, true);
            while (statements.hasNext()) {
                Statement statement = statements.next();
                conditionResources.add((Resource) statement.getObject());
            }
            connection.close();
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        }
        return conditionResources;
    }
    
    /**
     *
     * @param repository
     * @param conditionResource
     * @return
     */
    public Condition extractBooleanCondition(
            Repository repository, Resource conditionResource) {
        List<String> conditionExpressions = new ArrayList<String>(); 
        Condition condition = null;
        log.debug("Extracting Boolean Conditions...");
        log.debug("condition resource is " + conditionResource);
        
        try {
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            
            //Process the Equality condition
                conditionExpressions =
                        extractExpression(repository, conditionResource);
                log.debug("Found " + conditionExpressions.size() +
                        "condition expressions.");
                
                Set<BindingCondition> bindingConditions =
                        extractNestedBindingCondition(repository, conditionResource); //statement
                log.debug("found " + bindingConditions.size() + 
                        " nested binding conditions." );
                for (String conditionExpression : conditionExpressions) {
                    if (bindingConditions == null || conditionExpression == null) {
                        log.error("Error: " + conditionResource.stringValue()
                                + " must have condition and bindingConditions. ");
                    }
                    //Now it considers only one fallback
                    //TODO: change it to accept multiple?
                    URI p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE + 
                            CRMLVocabulary.cRMLTerm.BOOLEAN_CONDITION);
                    condition = extractConditionDetails(repository, 
                            bindingConditions, conditionExpression, p, conditionResource);
                }
            
        } catch (ClassCastException e) {
            log.error("Class cast exception "
                    + "A resource was expected in object of predicateMap of "
                    + conditionResource.stringValue());
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        }
        return condition;
    }
    
    Set<BindingCondition> extractNestedBindingCondition(
            Repository repository, Resource object) { //, Statement statement
        log.debug("Extracting nested Binding Conditions...");
        BindingConditionExtractor bindingConditionsExtractor =
                new BindingConditionExtractor();
        Set<BindingCondition> bindingConditions =
                bindingConditionsExtractor.extractBindCondition(
                repository, object); //(Resource) statement.getObject()
        return bindingConditions;
    }
    
    /**
     *
     * @param repository
     * @param bindingConditions
     * @param conditionExpression
     * @param p
     * @param object
     * @return
     */
    public Condition extractConditionDetails(Repository repository, 
            Set<BindingCondition> bindingConditions, //Set<Condition> conditions, 
            String conditionExpression, URI p, Resource object) {
        Condition newCondition = null;
        log.debug("Extracting Condition Details...");
        try {
            if(p.toString().equals(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.NEGATION_CONDITION))
                newCondition =
                    new StdNegationCondition(conditionExpression, bindingConditions);
            else
                newCondition =
                    new StdBooleanCondition(conditionExpression, bindingConditions);
            
        } catch (Exception ex) {
            log.error("Exception " + ex);
        }
        return newCondition;
    }
    
    /**
     *
     * @param repository
     * @param object
     * @param condition
     * @return
     */
    public static List<Value> extractFallback(
            Repository repository, Resource object) {
        List<Value> listValues = new ArrayList<Value>();

        try {
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();

            //retrieves value
            URI p = vf.createURI(
                    CRMLVocabulary.CRML_NAMESPACE 
                    + CRMLVocabulary.cRMLTerm.FALLBACK);

            RepositoryResult<Statement> statements = 
                    connection.getStatements(object, p, null, true);

            while (statements.hasNext()) {
                    Statement fallbackStatement = statements.next();
                    Value fallback = fallbackStatement.getObject();
                    log.debug("fallback is " + fallback);
                    listValues.add(fallback);
                }
            connection.close();
 
        }catch (ClassCastException e) {
            log.error("A valid value was expected "
                    + object.stringValue());
        } finally {
            return listValues;
        }
    }
    
    public void setFallback(PredicateObjectMap predicateObjectMap){
        this.setFallback(predicateObjectMap) ;
    }
}
