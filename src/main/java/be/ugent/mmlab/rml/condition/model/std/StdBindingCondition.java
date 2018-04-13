package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import java.util.Set;

import be.ugent.mmlab.rml.model.RDFTerm.FunctionTermMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * RML - Model
 *
 * @author andimou
 */
public class StdBindingCondition extends StdCondition implements BindingCondition{
    private String variable;
    
    // Log
    private static final Logger log = 
            LogManager.getLogger(StdBindingCondition.class.getSimpleName());
    
    /**
     *
     * @throws Exception
     */
    public StdBindingCondition(String variable, String reference) {
        setVariable(variable);
        setReference(reference);
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
        //setCondition(condition);
        //setVariable(value);
        setReference(reference);
        setNestedConditions(nestedConditions);
    }
    
    private void setVariable(String value) {
        if (value == null) {
            log.error("Error: "
                    + "A bind condition must "
                    + "have a value.");
        }
        this.variable = value;
    }
    
    /*private void setReference(String reference) {
        if (reference == null) {
            log.error("Error: "
                    + "A bind condition must "
                    + "have a value.");
        }
        this.reference = reference;
    }*/
    
    /**
     *
     * @return
     */
    @Override
    public String getReference() {
        return this.reference;
    }

    @Override
    public String getVariable() {
        return this.variable;
    }    

    @Override
    public PredicateObjectMap getFallback() {
        log.error("Not supported yet."); 
        return null;
    }

    @Override
    public void setFallback(PredicateObjectMap predicateObjectMap) {
        log.debug("Not supported yet."); 
    }

    @Override
    public Set<FunctionTermMap> getFunctionTermMaps() {
        log.error("Not supported yet.");
        return null;
    }

    @Override
    public void setFunctionTermMaps(Set<FunctionTermMap> functionTermMaps) {

    }

}
