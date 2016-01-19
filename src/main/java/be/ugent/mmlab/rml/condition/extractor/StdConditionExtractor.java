package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.condition.model.std.StdBooleanCondition;
import be.ugent.mmlab.rml.condition.model.std.StdNegationCondition;
import be.ugent.mmlab.rml.vocabularies.CRMLVocabulary;
import be.ugent.mmlab.rml.vocabularies.RMLVocabulary;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
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
            LogManager.getLogger(StdConditionExtractor.class);
       
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
                    + CRMLVocabulary.cRMLTerm.CONDITION);

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

    @Override
    public Set<Condition> extractCondition(Repository repository, 
        Set<Condition> conditions, Resource object) {
                
        try {
            RepositoryConnection connection = repository.getConnection();
            //extract boolean condition
            ValueFactory vf = connection.getValueFactory();
            String value; URI p = null;
            String conditionType = this.getClass().getSimpleName();
            
            switch (conditionType) {
                case "BooleanConditionExtractor":
                    log.debug("Extracting Boolean Conditions..");
                    // Extract boolean condition
                    p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.BOOLEAN_CONDITION);
                    extractBooleanCondition(
                            connection, repository, p, object,conditions);
                    break;
               case "NegationConditionExtractor":
                    log.debug("Extracting Negation Conditions..");
                    // Extract negation condition
                    p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.NEGATION_CONDITION);
                    extractBooleanCondition(
                            connection, repository, p, object,conditions);
                    break;
               case "ProcessConditionExtractor":
                    log.debug("Extracting Processing Conditions..");
                    // Extract processing condition
                    p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.PROCESS_CONDITION);
                    break;
            }
            
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        }
        return conditions;
    }
    
    private void extractBooleanCondition(RepositoryConnection connection, 
            Repository repository, URI p, Resource object, Set<Condition> conditions) {
        List<String> conditionExpressions = new ArrayList<String>(); 
        
        try {
            RepositoryResult<Statement> statements =
                    connection.getStatements(object, p, null, true);
            while (statements.hasNext()) {
                Statement statement = statements.next();

                conditionExpressions =
                        extractExpression(repository, object, statement);
                Set<BindingCondition> bindingConditions =
                        extractNestedBindingCondition(
                        repository, statement);

                for (String conditionExpression : conditionExpressions) {
                    if (bindingConditions == null || conditionExpression == null) {
                        log.error("Error: " + object.stringValue()
                                + " must have condition and bindingConditions. ");
                    }
                    extractConditionDetails(conditions, bindingConditions,
                            conditionExpression, p, object);
                }
            }
        } catch (ClassCastException e) {
            log.error("Class cast exception "
                    + "A resource was expected in object of predicateMap of "
                    + object.stringValue());
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        }
        
    }
    
    Set<BindingCondition> extractNestedBindingCondition(
            Repository repository, Statement statement) {
        log.debug("Extracting nested Binding Conditions...");
        BindingConditionExtractor bindingConditionsExtractor =
                new BindingConditionExtractor();
        Set<BindingCondition> bindingConditions =
                bindingConditionsExtractor.extractBindCondition(
                repository, (Resource) statement.getObject());
        return bindingConditions;
    }
    
    public void extractConditionDetails(Set<Condition> conditions, 
            Set<BindingCondition> bindingConditions, String conditionExpression, 
            URI p, Resource object) {
        Condition newCondition;
        try {
            if(p.toString().equals(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.NEGATION_CONDITION))
                newCondition =
                    new StdNegationCondition(conditionExpression, bindingConditions);
            else
                newCondition =
                    new StdBooleanCondition(conditionExpression, bindingConditions);
            conditions.add(newCondition);
        } catch (Exception ex) {
            log.error("Exception " + ex);
        }
    }
}
