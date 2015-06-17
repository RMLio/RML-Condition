package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.condition.model.BindCondition;
import be.ugent.mmlab.rml.condition.model.BooleanCondition;
import be.ugent.mmlab.rml.condition.model.ProcessCondition;
import be.ugent.mmlab.rml.condition.model.SplitCondition;
import be.ugent.mmlab.rml.input.model.InputSource;
import be.ugent.mmlab.rml.model.std.StdLogicalSource;
import be.ugent.mmlab.rml.vocabulary.QLVocabulary;
import java.util.Set;

/**
 * RML - Conditions
 *
 * @author andimou
 */
public class ConditionalStdLogicalSource extends StdLogicalSource{
    public ConditionalStdLogicalSource(String iterator, InputSource inputSource, QLVocabulary.QLTerm referenceFormulation,
            Set<BooleanCondition> booleanCondition, Set<ProcessCondition> processCondition,
            Set<SplitCondition> splitCondition, Set<BindCondition> bindCondition) {
        super(iterator, inputSource, referenceFormulation,
            booleanCondition, processCondition, splitCondition, bindCondition);
    }

}
