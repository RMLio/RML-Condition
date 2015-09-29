package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.model.Source;
import be.ugent.mmlab.rml.model.std.StdLogicalSource;
import be.ugent.mmlab.rml.vocabularies.QLVocabulary.QLTerm;
import java.util.HashSet;
import java.util.Set;

/**
 * RML - Conditions
 *
 * @author andimou
 */
public class ConditionalStdLogicalSource extends StdLogicalSource{

    private Set<BindingCondition> bindConditions;
    
    /**
     *
     * @param iterator
     * @param inputSource
     * @param referenceFormulation
     * @param booleanCondition
     * @param processCondition
     * @param splitCondition
     * @param bindCondition
     */
    
    
    public ConditionalStdLogicalSource(String iterator, Source inputSource, QLTerm referenceFormulation,
            Set<BindingCondition> bindCondition) {
        
        super(iterator, inputSource,referenceFormulation);        
        setBindConditions(bindCondition);
    }

    public ConditionalStdLogicalSource(String iterator, Source inputSource, QLTerm referenceFormulation) {
        super(iterator, inputSource,referenceFormulation);
    }

    private void setBindConditions(Set<BindingCondition> bindConditions) {
        this.bindConditions = new HashSet<BindingCondition>();
        this.bindConditions.addAll(bindConditions);
    }

    /**
     *
     * @return
     */
    public Set<BindingCondition> getBindConditions() {
        return this.bindConditions;
    }

}
