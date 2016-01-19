
package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.NegationCondition;
import java.util.Set;

/**
 * RML Processor
 *
 * @author andimou
 */
public class StdNegationCondition extends StdBooleanCondition implements NegationCondition{
    public StdNegationCondition(String condition, 
            Set<BindingCondition> bindingConditions) throws Exception {
        super(condition, bindingConditions);
    }
}
