package be.ugent.mmlab.rml.processor;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.logicalsourcehandler.termmap.TermMapProcessor;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import java.util.Set;

/**
 *
 * @author andimou
 */
public interface ConditionProcessor {
    
    /**
     *
     * @param node
     * @param termMapProcessor
     * @param conditions
     * @return
     */
    public boolean processConditions(Object node, 
            TermMapProcessor termMapProcessor, Set<Condition> conditions);
    
    /**
     *
     * @param condition
     * @return
     */
    public PredicateObjectMap processFallback(Condition condition);
}
