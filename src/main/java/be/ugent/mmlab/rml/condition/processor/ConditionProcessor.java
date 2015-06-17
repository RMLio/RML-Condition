package be.ugent.mmlab.rml.condition.processor;

import be.ugent.mmlab.rml.model.LogicalSource;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.condition.model.BooleanCondition;
import be.ugent.mmlab.rml.condition.model.ProcessCondition;
import be.ugent.mmlab.rml.condition.model.SplitCondition;
import be.ugent.mmlab.rml.condition.model.conditionalTermMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openrdf.model.Value;

/**
 *
 * @author andimou
 */
public class ConditionProcessor {
    
    // Log
    private static final Logger log = LogManager.getLogger(ConditionProcessor.class);
    
    /**
     *
     * @param termMap
     * @param value
     * @param valueList
     * @return
     */
    public static List<String> postProcessTermMap(
            conditionalTermMap termMap, String value, List<Value> valueList) {
        String[] list ;
        String split = termMap.getSplit();
        String process = termMap.getProcess();
        String replace = termMap.getReplace();
        List<String> stringList = null;

        if (split != null) {
            list = value.split(split);
            if (replace != null && list != null) {
                Integer replaceOrder = Integer.parseInt(replace.substring(1));

                if ((replaceOrder - 1) > 0 && (replaceOrder - 1) < list.length) {
                    value = list[replaceOrder - 1];
                } else {
                    value = null;
                }
                if (stringList == null) {
                    //valueList = new ArrayList<Value>();
                    stringList = new ArrayList<String>();
                }
                if (value != null && !value.equals("")) {
                    //stringList.add(cleansing(value));
                    stringList.add(value);
                }
            }
            else {
                for (String item : list) {
                    if (stringList == null) {
                        //valueList = new ArrayList<Value>();
                        stringList = new ArrayList<String>();
                    }
                    stringList.add(item);
                }
            }
        }

        if (process != null && replace != null) {
            Pattern replacement = Pattern.compile(process);
            Matcher matcher = replacement.matcher(value);
            if (matcher.find() && matcher.matches()) {
                if (stringList == null) {
                    stringList = new ArrayList<String>();
                }
                try {
                    value = matcher.replaceAll(replace);
                } catch (Exception ex) {
                    log.debug(ex);
                    return stringList;
                }
                if (value != null && !value.equals("")) {
                    //stringList.add(cleansing(value));
                    stringList.add(value);
                } else {
                    return stringList;
                }
            }
        }
        return stringList;
    }
    
    /**
     *
     * @param map
     * @param value
     * @return
     */
    public static List <String> processAllConditions(conditionalTermMap map, String value) {
        HashSet<ProcessCondition> processConditions = map.getProcessConditions();
        HashSet<SplitCondition> splitConditions = map.getSplitConditions();
        HashSet<BooleanCondition> booleanConditions = map.getBooleanConditions();
        List <String> result = new ArrayList<String>();

        if (booleanConditions.size() > 0) {
            result.addAll(BooleanConditionProcessor.processConditions(map, value));
        }
        else if (processConditions.size() > 0){
            result.addAll(ProcessConditionProcessor.processConditions(map, value));
        }
        else if (splitConditions.size() > 0) {
            try{
            result.addAll(SplitConditionProcessor.processConditions(map, value));
            } catch(Exception ex){log.error(ex);}
        }

        return result;
    }
    
    /**
     *
     * @param map
     * @param value
     * @return
     */
    public static List <String> processAllConditions(LogicalSource source, String value) {
        Set<ProcessCondition> processConditions = source.getProcessConditions();
        Set<SplitCondition> splitConditions = source.getSplitConditions();
        Set<BooleanCondition> booleanConditions = source.getBooleanConditions();
        List <String> result = new ArrayList<String>();

        if (booleanConditions.size() > 0) {
            result.addAll(BooleanConditionProcessor.processConditions(source, value));
        }
        else if (processConditions.size() > 0){
            result.addAll(ProcessConditionProcessor.processConditions(source, value));
        }
        else if (splitConditions.size() > 0) {
            try{
            result.addAll(SplitConditionProcessor.processConditions(source, value));
            } catch(Exception ex){log.error(ex);}
        }

        return result;
    }
    
    /**
     *
     * @param nestedConditions
     * @param value
     * @return
     */
    public static List<String> processAllNestedConditions(Set<Condition> nestedConditions, String value) {
        List<String> stringList = null, newStringList = new ArrayList<String>();
        for (Condition nestedCondition : nestedConditions) {
            stringList = new ArrayList<String>();
            stringList.addAll(processNestedCondition(nestedCondition, value));
            newStringList.addAll(processNestedConditions(nestedCondition, stringList));
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
    public static List<String> processNestedCondition(Condition nestedCondition, String value) {
        List<String> stringList = new ArrayList<String>();
        
        switch (nestedCondition.getClass().getSimpleName()) {
            case "StdProcessCondition":
                stringList.add(ProcessConditionProcessor.processCondition(nestedCondition, value));
                break;
            case "StdSplitCondition":
                stringList.addAll(SplitConditionProcessor.processCondition(nestedCondition, value));
                break;
            case "StdBooleanCondition":
                stringList.add(BooleanConditionProcessor.processCondition(nestedCondition, value));
                break;
            default:
                log.error("unknown condition");
        }
        return stringList;
    }
    
    /**
     *
     * @param condition
     * @param list
     * @return
     */
    public static List<String> processNestedConditions(Condition condition, List<String> list) {
        List<String> newStringList = new ArrayList<String>();
        Set<Condition> nestedConditions = condition.getNestedConditions();
        if (nestedConditions != null && nestedConditions.size() > 0) {
            for (String value : list) {
                newStringList.addAll(processAllNestedConditions(nestedConditions, value));
            }
        } else {
            newStringList.addAll(list);
        }
        return newStringList;
    }
}
