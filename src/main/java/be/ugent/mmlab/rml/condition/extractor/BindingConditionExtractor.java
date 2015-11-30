package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.std.StdBindingCondition;
import be.ugent.mmlab.rml.vocabularies.CRMLVocabulary;
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
public class BindingConditionExtractor extends StdConditionExtractor {
    
    //Log
    private static final Logger log = 
            LogManager.getLogger(BindingConditionExtractor.class);
    
    public static Set<BindingCondition> extractBindCondition(
            Repository repository, Resource object) {

        Set<BindingCondition> result = new HashSet<BindingCondition>();
        List<String> variables = new ArrayList<String>();

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
                variables = extractVariable(
                        repository, object, statement);
                String reference = extractReference(
                        repository, object, statement);

                for (String variable : variables) {
                    if (variable == null) {
                        log.error(object.stringValue()
                                + " must have exactly two properties"
                                + " condition and value. ");
                    }
                    try {
                        result.add(new StdBindingCondition(variable, reference));
                    } catch (Exception ex) {
                        log.error(ex);
                    }
                }
            }
        } catch (ClassCastException e) {
            log.error("A resource was expected in object of predicateMap of "
                    + object.stringValue());
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        }
        return result;
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
