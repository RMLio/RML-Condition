package be.ugent.mmlab.rml.processor;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.logicalsourcehandler.termmap.TermMapProcessor;
import java.util.Set;

/**
 *
 * @author andimou
 */
public interface ConditionProcessor {
    public boolean processConditions(Object node, 
            TermMapProcessor termMapProcessor, Set<Condition> conditions);
}
