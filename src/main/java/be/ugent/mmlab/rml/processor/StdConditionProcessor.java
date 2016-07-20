package be.ugent.mmlab.rml.processor;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.grel.ConcreteGrelProcessor;
import be.ugent.mmlab.rml.logicalsourcehandler.termmap.TermMapProcessor;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import be.ugent.mmlab.rml.model.RDFTerm.FunctionTermMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import org.openrdf.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RML Processor
 *
 * @author andimou
 */
public class StdConditionProcessor implements ConditionProcessor {
    
    // Log
    private static final Logger log = 
            LoggerFactory.getLogger(StdConditionProcessor.class.getSimpleName());

    @Override
    public boolean processConditions(Object node, TermMapProcessor termMapProcessor, 
            Set<Condition> conditions) {
        Set<BindingCondition> bindings = new HashSet<BindingCondition>();
        Map<String, String> parameters;
        boolean result = false;
        log.debug("There are " + conditions.size() + " conditions...");
        iter: for (Condition condition : conditions) {
            if(condition.getClass().getSimpleName().equals("StdBindingCondition")){
                continue;
            }
            String expression = condition.getCondition();
            log.debug("expression " + expression);
            bindings = condition.getBindingConditions();
            if(bindings != null)
                log.debug("There are " + bindings.size() + " bindings.");
            if(bindings != null && bindings.size() > 0) {
                for (BindingCondition binding : bindings) {
                    String replacement;
                    parameters = processBindingConditions(node, termMapProcessor, bindings);

                    if (parameters.size() > 0) {
                        replacement = parameters.get(binding.getVariable());
                        expression = expression.replaceAll(
                                "%%" + Pattern.quote(binding.getVariable()) + "%%",
                                replacement);
                    }

                    //TODO: Properly handle the followings...
                    if (expression.contains("!match")) {
                        result = processNotMatch(expression);
                        if (!result) {
                            break iter;
                        }
                    } else if (expression.contains("match")) {
                        result = processMatch(expression);
                        if (condition.getClass().getSimpleName().equals("StdNegationCondition"))
                            return !result;
                    } else if (expression.contains("!contain")) {
                        result = processNotContains(expression);
                        if (!result) {
                            break iter;
                        }
                    } else if (expression.contains("contain")) {
                        result = processContains(expression);
                        if (!result) {
                            break iter;
                        }
                    } else if (expression.contains("!length")) {
                        result = processNotLength(expression);
                        if (!result) {
                            break iter;
                        }
                    } else if (expression.contains("hasField")) {
                        if (parameters.size() == 0)
                            return false;
                        else
                            return true;
                        //result = processHasField(expression);
                        //if (!result) {
                        //    break iter;
                        //}
                    }
                }
            }
            else{
                Set<FunctionTermMap> funTermMaps = condition.getFunctionTermMaps();
                for(FunctionTermMap funTermMap : funTermMaps){
                    TriplesMap funTM = funTermMap.getFunctionTriplesMap();
                    Set<PredicateObjectMap> funPOMs = funTM.getPredicateObjectMaps();
                    for(PredicateObjectMap funPOM : funPOMs){
                        Value predicate = funPOM.getPredicateMaps().iterator().next().getConstantValue();
                        if(predicate.stringValue().contains("executes")){
                            if(funPOM.getObjectMaps().iterator().next().getConstantValue().stringValue().contains("isSet")){
                                ConcreteGrelProcessor grel = new ConcreteGrelProcessor();
                                String resultString = grel.isSet(funTermMap.getParameterRefs());
                                result = Boolean.valueOf(resultString);
                            }

                        }
                    }
                }
            }
        }
        return result;
    }

    public Map<String, String> processBindingConditions(Object node, 
            TermMapProcessor termMapProcessor, Set<BindingCondition> bindingConditions) {
        Map<String, String> parameters = new HashMap<String, String>();
        
        for (BindingCondition bindingCondition : bindingConditions) {
            List<String> childValues = termMapProcessor.
                    extractValueFromNode(node, bindingCondition.getReference());

            for (String childValue : childValues) {
                parameters.put(
                        bindingCondition.getVariable(), childValue);
            }
        }

        return parameters;
    }
    
    /**
     *
     * @param condition
     * @return
     */
    @Override
    public PredicateObjectMap processFallback(Condition condition) {
        return condition.getFallback();
    }
    
    //TODO: Move them separately
    public boolean processMatch(String expression){
        expression = expression.replace("match(", "").replace(")", "");
        String[] strings = expression.split(",");
        log.info("strings[0] " + strings[0]);
        log.info("strings[1] " + strings[1]);
        if (strings != null && strings.length > 1) {
            if (strings[0].equals(strings[1].replaceAll("\"", ""))) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    public boolean processNotMatch(String expression) {
        expression = expression.replace("!match(", "").replace(")", "");
        log.debug("expression " + expression);
        String[] strings = expression.split(",");

        if (strings != null && strings.length > 1
                && strings[0].equals(strings[1].replaceAll("\"", ""))) {
            //log.debug("strings[0] " + strings[0]);
            //log.debug("strings[1] " + strings[1].replaceAll("\"", ""));
            return false;
        } else {
            return true;
        }
    }
    
    public boolean processContains(String expression){
        log.debug("Processing contains condition...");
        expression = expression.replace("contains(", "").replace(")", "");
        String[] strings = expression.split(",");
        
        if (strings != null && strings.length > 1) {
            if (strings[0].contains(strings[1].replaceAll("\"", ""))) {
                log.debug("strings[0] " + strings[0]);
                log.debug("strings[1] " + strings[1].replaceAll("\"", ""));
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    public boolean processNotContains(String expression){
        log.debug("Processing not contains condition...");
        expression = expression.replace("!contains(", "").replace(")", "");
        String[] strings = expression.split(",");
        
        if (strings != null && strings.length > 1) {
            if (strings[0].contains(strings[1].replaceAll("\"", ""))) {
                log.debug("strings[0] " + strings[0]);
                log.debug("strings[1] " + strings[1].replaceAll("\"", ""));
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
    
    public boolean processNotLength(String expression){
        log.debug("Processing not length condition...");
        expression = expression.replace("!length(", "").replace(")", "");
        String[] strings = expression.split(",");
        
        if (strings != null && strings.length > 1) {
            if (strings[0].length() ==  
                    Integer.parseInt(strings[1].replaceAll("\"", ""))) {
                log.debug("strings[0] " + strings[0]);
                log.debug("strings[1] " + strings[1].replaceAll("\"", ""));
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
    
    public boolean processHasField(String expression) {
        expression = expression.replace("hasField(", "").replace(")", "");
        
        if (expression != null) {
            return true;
        } else {
            return false;
        }
    }
}
