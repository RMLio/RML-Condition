package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.vocabularies.CRMLVocabulary;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;

/**
 * RML - Conditions : ConditionExtractor
 *
 * @author andimou
 */
public class ConditionExtractor {

    //Log
    private static final Logger log = 
            LogManager.getLogger(ConditionExtractor.class);
    
    
    /*public static List<String> extractCondition(
            Repository repository, Resource object, Statement statement) {
        
        List<String> listConditions = new ArrayList<String>();

        try {
            RepositoryConnection connection = repository.getConnection();
            //assigns current boolean condtion
            Resource splitCond = (Resource) statement.getObject();           

        } catch (ClassCastException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "A resource was expected in object of predicateMap of "
                    + object.stringValue());
        } finally {
            return listConditions;
        }
    }*/
    
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
}
