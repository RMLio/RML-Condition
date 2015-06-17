package be.ugent.mmlab.rml.condition.processor;

import be.ugent.mmlab.rml.model.LogicalSource;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.condition.model.SplitCondition;
import be.ugent.mmlab.rml.condition.model.conditionalTermMap;
import java.util.ArrayList;
import java.util.Arrays;
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
public class SplitConditionProcessor extends ConditionProcessor{
    
    // Log
    private static final Logger log = LogManager.getLogger(SplitConditionProcessor.class);
    
    public static List <String> processConditions(conditionalTermMap map, String value) {
        HashSet<SplitCondition> splitConditions = map.getSplitConditions();
        List<String> stringList = null, newStringList = new ArrayList<String>();

        if (splitConditions != null) {
            for (SplitCondition splitCondition : splitConditions) {
                String condition = splitCondition.getCondition();
                stringList = new ArrayList<String>();
                stringList.addAll(Arrays.asList(value.split(condition)));
                
                newStringList.addAll(processNestedConditions(splitCondition, stringList));
            }
        }

        if(newStringList.size() > 0)
            return newStringList;
        else
            return stringList;
    }
    
    /**
     *
     * @param source
     * @param value
     * @return
     */
    public static List <String> processConditions(LogicalSource source, String value) {
        Set<SplitCondition> splitConditions = source.getSplitConditions();
        List<String> stringList = null, newStringList = new ArrayList<String>();

        if (splitConditions != null) {
            for (Condition splitCondition : splitConditions) {
                String condition = splitCondition.getCondition();
                stringList = new ArrayList<String>();
                stringList.addAll(Arrays.asList(value.split(condition)));
                
                newStringList.addAll(processNestedConditions(splitCondition, stringList));
            }
        }

        if(newStringList.size() > 0)
            return newStringList;
        else
            return stringList;
    }
    
    /**
     *
     * @param nestedCondition
     * @param value
     * @return
     */
    public static List<String> processCondition(Condition nestedCondition, String value) {
        List <String> stringList = new ArrayList<String>();
        String condition = nestedCondition.getCondition();
        
        stringList.addAll(Arrays.asList(value.split(condition)));
            return stringList;
    }
    
}
