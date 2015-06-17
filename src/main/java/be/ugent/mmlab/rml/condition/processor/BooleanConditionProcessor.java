package be.ugent.mmlab.rml.condition.processor;

import be.ugent.mmlab.rml.model.LogicalSource;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.condition.model.BooleanCondition;
import be.ugent.mmlab.rml.condition.model.conditionalTermMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * RML - Conditions
 *
 * @author andimou
 */
public class BooleanConditionProcessor extends ConditionProcessor{
    
    // Log
    private static final Logger log = LogManager.getLogger(BooleanConditionProcessor.class);
    
    /**
     *
     * @param map
     * @param replacement
     * @return
     */
    public static List<String> processConditions(conditionalTermMap map, String replacement) {
        HashSet<BooleanCondition> booleanConditions = map.getBooleanConditions();
        List<String> stringList, newStringList = null;
        
        if (booleanConditions != null) {
            for (BooleanCondition booleanCondition : booleanConditions) {
                replacement = processCondition(booleanCondition, replacement);
                newStringList = new  ArrayList<String>();
                stringList = new  ArrayList<String>();
                stringList.add(replacement);
                newStringList.addAll(processNestedConditions(booleanCondition, stringList));

            }
        }
        return newStringList;
    }
    
    /**
     *
     * @param source
     * @param replacement
     * @return
     */
    public static List<String> processConditions(LogicalSource source, String replacement) {
        Set<BooleanCondition> booleanConditions = source.getBooleanConditions();
        List<String> stringList, newStringList = null;
        
        if (booleanConditions != null) {
            for (Condition booleanCondition : booleanConditions) {
                replacement = processCondition(booleanCondition, replacement);
                newStringList = new  ArrayList<String>();
                stringList = new  ArrayList<String>();
                stringList.add(replacement);
                newStringList.addAll(processNestedConditions(booleanCondition, stringList));

            }
        }
        return newStringList;
    }
    
    /**
     *
     * @param booleanCondition
     * @param replacement
     * @return
     */
    public static String processCondition(Condition booleanCondition, String replacement) {

        String condition = booleanCondition.getCondition();
        String value = booleanCondition.getValue();
        if (replacement != null && replacement.equals(condition)) {
            replacement = value;
        }
        
        return replacement;
    }
    
}
