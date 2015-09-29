package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.Condition;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * RML - Model
 *
 * @author andimou
 */
public class StdBindingCondition extends StdCondition implements BindingCondition{
    private String reference;
    private String variable;
    
    // Log
    private static final Logger log = 
            LogManager.getLogger(StdBindingCondition.class);
    
    /**
     *
     * @param condition
     * @param value
     * @throws Exception
     */
    public StdBindingCondition(String condition, String value) {
        setCondition(condition);
        setVariable(value);
    }
    
    /**
     *
     * @param condition
     * @param value
     * @param reference
     * @param nestedConditions
     * @throws Exception
     */
    public StdBindingCondition(
            String condition, String value, String reference, 
            Set<Condition> nestedConditions) 
            throws Exception {
        setCondition(condition);
        setVariable(value);
        setReference(reference);
        setNestedConditions(nestedConditions);
    }
    
    private void setVariable(String value) {
        if (value == null) {
            log.error(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "A bind condition must "
                    + "have a value.");
        }
        this.value = value;
    }
    
    private void setReference(String reference) {
        if (reference == null) {
            log.error(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "A bind condition must "
                    + "have a value.");
        }
        this.reference = reference;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getReference() {
        return this.reference;
    }    
    
    public String getVariable() {
        return this.variable;
    }    
    
}
