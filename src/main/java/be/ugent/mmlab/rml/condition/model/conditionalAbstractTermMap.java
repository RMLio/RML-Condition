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

import be.ugent.mmlab.rml.model.AbstractTermMap;
import be.ugent.mmlab.rml.model.TermMap;
import be.ugent.mmlab.rml.model.reference.ReferenceIdentifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

public abstract class conditionalAbstractTermMap extends AbstractTermMap implements TermMap {

        // Log
        private static final Logger log = LogManager.getLogger(conditionalAbstractTermMap.class);

        private String split;
        private String process;
        private String replace;
        private HashSet<Condition> conditions;
        private HashSet<BooleanCondition> booleanConditions;
        private HashSet<ProcessCondition> processConditions;
        private HashSet<SplitCondition> splitConditions;
        private HashSet<BindCondition> bindConditions;
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
     */
    protected conditionalAbstractTermMap(Value constantValue, URI dataType,
                String languageTag, String stringTemplate, URI termType,
                String inverseExpression, ReferenceIdentifier referenceValue,
                String split, String process, String replace) {
                super(constantValue, dataType, languageTag, stringTemplate, 
                        termType, inverseExpression, referenceValue);

                setSplit(split);
                setProcess(process);
                setReplace(replace);
        }
        
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
    protected conditionalAbstractTermMap(Value constantValue, URI dataType,
                String languageTag, String stringTemplate, URI termType,
                String inverseExpression, ReferenceIdentifier referenceValue,
                String split, String process, String replace, 
                Set<BooleanCondition> booleanConditions, Set<ProcessCondition> processConditions, 
                Set<SplitCondition> splitConditions, Set<BindCondition> bindConditions) {
                super(constantValue, dataType, languageTag, stringTemplate, 
                        termType, inverseExpression, referenceValue);
                setSplit(split);
                setProcess(process);
                setReplace(replace);
                setBooleanConditions(booleanConditions);
                setProcessConditions(processConditions);
                setSplitConditions(splitConditions);
                setBindConditions(bindConditions);
        }
        
        private void setSplit(String split) {
        //TODO:add control for regex, its value MUST be a valid regex
            this.split = split;
        }
        
        private void setProcess(String process) {
        //TODO:add control for regex, its value MUST be a valid regex
            this.process = process;
        }
        
        private void setReplace(String replace) {
        //TODO:add control for regex, its value MUST be a valid regex
            this.replace = replace;
        }

        private void setBooleanConditions(Set<BooleanCondition> booleanConditions) {
            this.booleanConditions = new HashSet<BooleanCondition>();
            this.booleanConditions.addAll(booleanConditions);
        }

        private void setProcessConditions(Set<ProcessCondition> processConditions) {
            this.processConditions = new HashSet<ProcessCondition>();
            this.processConditions.addAll(processConditions);
        }

        private void setSplitConditions(Set<SplitCondition> splitConditions) {
            this.splitConditions = new HashSet<SplitCondition>();
            this.splitConditions.addAll(splitConditions);
        }
        
        private void setBindConditions(Set<BindCondition> bindConditions) {
            this.bindConditions = new HashSet<BindCondition>();
            this.bindConditions.addAll(bindConditions);
        }
        
        /**
        *
        * @return return this.split
        */
        public String getSplit(){
            return this.split;
        }
        
        /**
        *
        * @return this.process
        */
        public String getProcess(){
            return this.process;
        }
        
        /**
        *
        * @return return this.replace
        */
        public String getReplace(){
            return this.replace;
        }
        
        /**
        *
        * @return this.condition
        */
        public Condition getCondition(){
            return this.condition;
        }
        
        /**
        *
        * @return this.conditions
        */
        public HashSet<Condition> getConditions(){
            return this.conditions;
        }
        
        /**
        *
        * @return this.booleanConditions
        */
       public HashSet<BooleanCondition> getBooleanConditions() {
           return this.booleanConditions;
       }

    /**
     *
     * @return this.processConditions
     */
    public HashSet<ProcessCondition> getProcessConditions() {
        return this.processConditions;
    }

    /**
     *
     * @return this.splitConditions
     */
    public HashSet<SplitCondition> getSplitConditions() {
        return this.splitConditions;
    }

    /**
     *
     * @return return this.bindConditions
     */
    public HashSet<BindCondition> getBindConditions() {
        return this.bindConditions;
    }

}
