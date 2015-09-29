/**
 * *************************************************************************
 *
 * RML - Conditions : conditional Abstract Term Map
 *
 *
 * @author andimou
 *
 ***************************************************************************
 */
package be.ugent.mmlab.rml.condition.model;

import be.ugent.mmlab.rml.model.RDFTerm.AbstractTermMap;
import be.ugent.mmlab.rml.model.RDFTerm.TermMap;
import be.ugent.mmlab.rml.model.termMap.ReferenceMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

public abstract class ConditionalAbstractTermMap extends AbstractTermMap implements TermMap {

    // Log
    private static final Logger log = LogManager.getLogger(ConditionalAbstractTermMap.class);
    private HashSet<Condition> conditions;
    private HashSet<BindingCondition> bindConditions;
    private Condition condition;


    /**
     *
     * @param constantValue
     * @param dataType
     * @param languageTag
     * @param stringTemplate
     * @param termType
     * @param inverseExpression
     * @param referenceValue
     * @param split
     * @param process
     * @param replace
     * @param booleanConditions
     * @param processConditions
     * @param splitConditions
     * @param bindConditions
     */
    protected ConditionalAbstractTermMap(Value constantValue, URI dataType,
            String languageTag, String stringTemplate, URI termType,
            String inverseExpression, ReferenceMap referenceValue,
            String split, String process, String replace, Set<BindingCondition> bindConditions) {
        super(constantValue, dataType, languageTag, stringTemplate,
                termType, inverseExpression, referenceValue);
        setBindConditions(bindConditions);
    }

    private void setBindConditions(Set<BindingCondition> bindConditions) {
        this.bindConditions = new HashSet<BindingCondition>();
        this.bindConditions.addAll(bindConditions);
    }

    /**
     *
     * @return this.condition
     */
    public Condition getCondition() {
        return this.condition;
    }

    /**
     *
     * @return this.conditions
     */
    public HashSet<Condition> getConditions() {
        return this.conditions;
    }

    /**
     *
     * @return return this.bindConditions
     */
    public HashSet<BindingCondition> getBindConditions() {
        return this.bindConditions;
    }
}
