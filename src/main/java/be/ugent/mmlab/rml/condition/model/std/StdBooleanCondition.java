package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.condition.model.BooleanCondition;
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
public class StdBooleanCondition extends StdCondition implements BooleanCondition {
    
    // Log
    private static final Logger log = 
            LogManager.getLogger(StdBooleanCondition.class.getSimpleName());
    
    private PredicateObjectMap fallback;
    private FunctionTermMap funTermMap ;
    
    /**
     *
     * @param condition
     * @param value
     * @throws Exception
     */
    //TODO: Remove, deprecated
    public StdBooleanCondition(String condition, String value) throws Exception {
        setCondition(condition);
        setValue(value);
    }
    
    public StdBooleanCondition(String condition, 
            Set<BindingCondition> bindingConditions) throws Exception {
        setCondition(condition);
        setNestedBindingConditions(bindingConditions);
        //setNestedConditions(bindingConditions);
        //setValue(value);
    }

    public StdBooleanCondition(String condition,
                               Set<BindingCondition> bindingConditions,
                               Set<FunctionTermMap> functionTermMaps) throws Exception {
        setCondition(condition);
        setNestedBindingConditions(bindingConditions);
        setFunctionTermMaps(functionTermMaps);
    }

    public StdBooleanCondition(Set<FunctionTermMap> functionTermMaps){
        setFunctionTermMaps(functionTermMaps);
    }
    
    /**
     *
     * @param condition
     * @param value
     * @param nestedConditions
     * @throws Exception
     */
    public StdBooleanCondition(String condition, String value, Set<Condition> nestedConditions) 
            throws Exception {
        setCondition(condition);
        setValue(value);
        setNestedConditions(nestedConditions);
    }
    
    private void setValue(String value) throws Exception {
        if (value == null) {
            throw new Exception("Exception: "
                    + "An equal condition must "
                    + "have a value.");
        }
        this.reference = value;
    }
    
    @Override
    public Set<BindingCondition> getBinding() {
        return this.bindingConditions;
    } 
    
    public void setNestedBindingConditions(Set<BindingCondition> bindingConditions) {
        this.bindingConditions = bindingConditions;
    }
    
    @Override
    public PredicateObjectMap getFallback() {
        return this.fallback;
    }
    
    @Override
    public void setFallback(PredicateObjectMap fallback){
        this.fallback = fallback;
    }

    @Override
    public Set<FunctionTermMap> getFunctionTermMaps() {
        return this.functionTermMaps;
    }

    public  void setFunctionTermMaps(Set<FunctionTermMap> functions) { this.functionTermMaps = functions; }

    public void setFunTermMaps(FunctionTermMap funTermMap) {
        this.funTermMap = funTermMap;
    }
}
