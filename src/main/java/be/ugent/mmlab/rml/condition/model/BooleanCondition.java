package be.ugent.mmlab.rml.condition.model;

import java.util.Set;

/**
 * RML - Conditions
 *
 * @author andimou
 */
public interface BooleanCondition extends Condition{

    public Set<BindingCondition> getBinding();
}
