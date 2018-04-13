package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.Condition;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.Repository;

/**
 * RML Processor
 *
 * @author andimou
 */
public class AbstractConditionExtractor {

    //Log
    private static final Logger log =
            LogManager.getLogger(
            AbstractConditionExtractor.class.getSimpleName());

    public static Set<Condition> extractConditions(
            Repository repository, Resource object) {
        Set<Condition> conditions = new HashSet<Condition>();

        try {
            log.debug("Extracting Conditions...");
            
            try {
                //Extract Binding Conditions
                BindingConditionExtractor bindingConditionsExtractor =
                        new BindingConditionExtractor();
                Set<BindingCondition> bindingConditions =
                        bindingConditionsExtractor.extractBindCondition(
                        repository, object);
                
                //Add Binding Conditions to the total
                if (bindingConditions != null && bindingConditions.size() > 0) {
                    conditions.addAll(bindingConditions);
                    log.debug("Binding Conditions were extracted");
                }
            } catch (Exception ex) {
                log.error("Exception " + ex + " because of Binding Conditions");
            }

            try {
                //Extract Equal Conditions
                StdConditionExtractor conditionsExtractor =
                        new BooleanConditionExtractor();
                Condition booleanCondition = 
                        conditionsExtractor.extractCondition(
                        repository, conditions, object);
                
                if(booleanCondition != null)
                    conditions.add(booleanCondition);
            } catch (Exception ex) {
                log.error("Exception " + ex + " because of Boolean Conditions");
            }

            //Extract Negation Conditions
            /*try {
                StdConditionExtractor conditionsExtractor =
                        new NegationConditionExtractor();
                //Set<Condition> negationConditions =
                Condition negationCondition =
                        conditionsExtractor.extractCondition(
                        repository, conditions, (Resource) object);
                if(negationCondition != null)
                conditions.add(negationCondition);
                //Add Boolean Conditions to the total
                if (negationConditions != null && negationConditions.size() > 0) {
                    conditions.addAll(negationConditions);
                    log.debug("Negation Conditions were extracted");
                }
            } catch (Exception ex) {
                log.error("Exception " + ex + " because of Negation Conditions");
            }*/
            
            //Extract Process Conditions
            
            //Extract 'Split' Conditions
            
            //Extract 


        } catch (ClassCastException e) {
            log.error("Class cast exception " + e 
                    + " A resource was expected in object of predicateMap of "
                    + object.stringValue());
        }
        log.debug("Extracting conditions completed.");

        return conditions;
    }
}
