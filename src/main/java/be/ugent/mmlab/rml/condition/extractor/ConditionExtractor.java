package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.condition.model.Condition;
import java.util.Set;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.Repository;

/**
 *
 * @author andimou
 */
public interface ConditionExtractor {
    
    /**
     *
     * @param repository
     * @param condition
     * @param object
     * @return
     */
    public Condition extractCondition(
            Repository repository, Set<Condition> condition, Resource object);
    
}
