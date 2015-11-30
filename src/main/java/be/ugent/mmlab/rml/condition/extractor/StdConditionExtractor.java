package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.condition.model.std.StdBooleanCondition;
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
        //Set<Condition> result = null;
                
        try {
            //extract boolean condition
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            String value; URI p = null;
            //result = new HashSet<Condition>();
            List<String> conditionExpressions = new ArrayList<String>(); 
            //        values = new ArrayList<String>();

            String conditionType = this.getClass().getSimpleName();
            
            switch (conditionType) {
                case "BooleanConditionExtractor":
                    log.debug("Extracting Boolean Conditions..");
                    // Extract boolean condition
                    p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.BOOLEAN_CONDITION);
                    break;
               case "ProcessConditionExtractor":
                    log.debug("Extracting Processing Conditions..");
                    // Extract processing condition
                    p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                            + CRMLVocabulary.cRMLTerm.PROCESS_CONDITION);
                    break;
            }
            
            RepositoryResult<Statement> statements = 
                connection.getStatements(object, p, null, true);
            
            try {
                while (statements.hasNext()) {
                    Statement statement = statements.next();
                    
                    conditionExpressions = 
                            extractExpression(repository, object, statement);
                    
                    log.debug("Extracting nested Binding Conditions...");
                    BindingConditionExtractor bindingConditionsExtractor =
                            new BindingConditionExtractor();
                    Set<BindingCondition> bindingConditions =
                            bindingConditionsExtractor.extractBindCondition(
                            repository, (Resource) statement.getObject());
                    
                    for (String conditionExpression : conditionExpressions) {
                        if (bindingConditions == null || conditionExpression == null) {
                            log.error("Error: " + object.stringValue()
                                    + " must have condition and bindingConditions. ");
                        }
                        extractConditionDetails(conditions, bindingConditions, 
                                conditionExpression, object);
                    }
                }
            } catch (ClassCastException e) {
                log.error("Class cast exception " + 
                        "A resource was expected in object of predicateMap of "
                        + object.stringValue());
            } 
            
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        }
        return conditions;
    }
    
    public void extractConditionDetails(Set<Condition> conditions, 
            Set<BindingCondition> bindingConditions, String conditionExpression, 
            Resource object) {
        
        try {
            StdBooleanCondition newCondition =
                    new StdBooleanCondition(conditionExpression, bindingConditions);
            conditions.add(newCondition);
        } catch (Exception ex) {
            log.error("Exception " + ex);
        }
    }
}
