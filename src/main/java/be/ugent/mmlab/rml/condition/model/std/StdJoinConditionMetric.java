
package be.ugent.mmlab.rml.condition.model.std;

import be.ugent.mmlab.rml.model.std.StdJoinCondition;
import org.eclipse.rdf4j.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RML Processor
 *
 * @author andimou
 */
public class StdJoinConditionMetric extends StdJoinCondition {
    
    // Log
    private static final Logger log = 
            LoggerFactory.getLogger(StdJoinConditionMetric.class.getSimpleName());
    private Resource metric;
    
    public StdJoinConditionMetric(String child, String parent, Resource metric) {
        super(child, parent);
        setMetric(metric);
    }

    public void setMetric(Resource metric) {
        this.metric = metric;
    }
    
    public Resource getMetric() {
        return metric;
    }


}
