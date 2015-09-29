package be.ugent.mmlab.rml.condition.model;

/**
 * *************************************************************************
 *
 * RML - Conditions : BindingCondition
 *
 *
 * @author andimou
 *
 ***************************************************************************
 */
public interface BindingCondition extends Condition {

    public String getReference();
    
    public String getVariable();
}
