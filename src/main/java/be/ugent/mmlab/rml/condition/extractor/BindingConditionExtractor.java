package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.std.StdBindingCondition;
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
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;

/**
 * RML - Conditions: Bind Condition Extractor
 *
 * @author andimou
 */
public class BindingConditionExtractor extends ConditionExtractor {
    
    //Log
    private static final Logger log = 
            LogManager.getLogger(BindingConditionExtractor.class);
    
    public static Set<BindingCondition> extractBindCondition(
            Repository repository, Resource object) {

        Set<BindingCondition> result = new HashSet<BindingCondition>();
        List<String> values = new ArrayList<String>();

        log.debug("Extracting Binding Conditions..");
        
        try {
        //extract binding condition
        RepositoryConnection connection = repository.getConnection();
        ValueFactory vf = connection.getValueFactory();
        
        URI p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE 
                + CRMLVocabulary.cRMLTerm.BINDING_CONDITION);
        RepositoryResult<Statement> statements = 
                connection.getStatements(object, p, null, true);
        
            while (statements.hasNext()) {
                Statement statement = statements.next();
                log.debug("Binding Statement " + statement);
                values = extractVariable(
                        repository, object, statement);
                log.debug("Binding condition values " + values);
                String reference = extractReference(
                        repository, object, statement);
                log.debug("Binding condition reference " + reference);


                for (String value : values) {
                    if (value == null) {
                        log.error(object.stringValue()
                                + " must have exactly two properties"
                                + " condition and value. ");
                    }
                    try {
                        result.add(new StdBindingCondition(null, value, reference, null));
                    } catch (Exception ex) {
                        log.error(ex);
                    }
                }
            }
        } catch (ClassCastException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "A resource was expected in object of predicateMap of "
                    + object.stringValue());
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        }
        log.debug("Extract bind condition done.");
        return result;
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
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "A valid value was expected "
                    + object.stringValue());
        } finally {
            return reference;
        }
    }
    
    public static List<String> extractVariable(
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
                    + CRMLVocabulary.cRMLTerm.VARIABLE);

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
    
}
