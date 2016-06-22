package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.RDFTerm.ReferencingObjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.vocabularies.CRMLVocabulary;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RML Condition Handler
 *
 * @author andimou
 */
public class FallbackMapExtractor {
    
    // Log
    static final Logger log = 
            LoggerFactory.getLogger(
            FallbackMapExtractor.class.getSimpleName());
    
    public RepositoryResult<Statement> extractFallbackObjectMap(
            Repository repository, Resource object, Set<GraphMap> graphMaps, 
            Map<Resource,TriplesMap> triplesMapResources, TriplesMap triplesMap,
            Resource triplesMapSubject, Resource predicateObject) {

        RepositoryResult<Statement> fallbackStatements = null;
        try {
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();

            if (connection.hasStatement(object,
                    vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                    + CRMLVocabulary.cRMLTerm.FALLBACK),
                    null, true)) {
                log.debug("Term Map with fallback Map");
                URI p = vf.createURI(CRMLVocabulary.CRML_NAMESPACE
                        + CRMLVocabulary.cRMLTerm.FALLBACK);
                fallbackStatements =
                        connection.getStatements(object, p, null, true);
                
                
            }
            else
                log.debug("Referencing Object Map without fallback");
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        } finally {
            return fallbackStatements;
        }
    }

}
