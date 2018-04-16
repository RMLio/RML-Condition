package be.ugent.mmlab.rml.processor;

import be.ugent.mmlab.rml.condition.model.BindingCondition;
import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.fno.java.ConcreteFunctionProcessor;
import be.ugent.mmlab.rml.logicalsourcehandler.termmap.TermMapProcessor;
import be.ugent.mmlab.rml.logicalsourcehandler.termmap.concrete.*;
import be.ugent.mmlab.rml.model.PredicateObjectMap;

import java.util.*;
import java.util.regex.Pattern;

import be.ugent.mmlab.rml.model.RDFTerm.FunctionTermMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.vocabularies.FnVocabulary;
import be.ugent.mmlab.rml.vocabularies.QLVocabulary;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.BooleanLiteral;
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

    private final ConcreteFunctionProcessor fnProcessor = ConcreteFunctionProcessor.getInstance();

    @Override
    public boolean processConditions(Object node, TermMapProcessor termMapProcessor, 
            Set<Condition> conditions) {
        Set<BindingCondition> bindings = new HashSet<BindingCondition>();
        Map<String, Object> parameters;
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
                        replacement = (String) parameters.get(binding.getVariable());
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
                    } else if (expression.contains("!hasField")) {
                       return parameters.size() == 0;
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
            } else {
                Set<FunctionTermMap> funTermMaps = condition.getFunctionTermMaps();

                for(FunctionTermMap functionTermMap : funTermMaps){
                    parameters = retrieveParameters(node, functionTermMap.getFunctionTriplesMap());
                    String function = functionTermMap.getFunction().toString();

                    List<Value> values = termMapProcessor.processFunctionTermMap(
                            functionTermMap, node, function, parameters);
                    if (values.size() == 0) {
                        return false;
                    }
                    else {
                        result = ((BooleanLiteral) values.get(0)).booleanValue();
                        //TODO:wmaroy fix to multiple values
                    }
                }
            }
        }
        return result;
    }

    public Map<String, Object> processBindingConditions(Object node,
                                                        TermMapProcessor termMapProcessor, Set<BindingCondition> bindingConditions) {
        Map<String, Object> parameters = new HashMap<>();

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

    private Map<String, Object> retrieveParameters(Object node, TriplesMap functionTriplesMap) {
        Map<String, Object> parameters = new HashMap<>();
        TermMapProcessor termMapProcessor = create(functionTriplesMap.getLogicalSource().getReferenceFormulation());

        String referenceValue;
        String constantValue;
        Set<PredicateObjectMap> poms = functionTriplesMap.getPredicateObjectMaps();
        for (PredicateObjectMap pom : poms) {
            Value property = pom.getPredicateMaps().iterator().next().getConstantValue();
            String executes = FnVocabulary.FNO_NAMESPACE + FnVocabulary.FnTerm.EXECUTES;
            if (!property.stringValue().equals(executes)) {
                Value parameter = pom.getPredicateMaps().iterator().next().getConstantValue();
                try {
                    referenceValue = pom.getObjectMaps().iterator().next().getReferenceMap().getReference();
                } catch (Exception e) {
                    referenceValue = null;
                    log.debug("No reference");
                }
                try {
                    constantValue = pom.getObjectMaps().iterator().next().getConstantValue().stringValue();
                } catch (Exception e) {
                    constantValue = null;
                    log.debug("No constant value");
                }
                if (referenceValue != null) {
                    List<String> value = termMapProcessor.extractValueFromNode(node, referenceValue);
                    if (value.size() != 0) {
                        parameters.put(parameter.stringValue(), value.get(0));
                    }
                } else if (constantValue != null) {
                    parameters.put(parameter.stringValue(), constantValue);
                } else {
                    // no value is present for this parameter, enter null
                    parameters.put(parameter.stringValue(), "null"); //TODO wmaroy: change to proper uri for null
                }
                //TODO from wmaroy: how to avoid this check?
            }
        }

        return parameters;
    }

    public TermMapProcessor create(QLVocabulary.QLTerm term) {
        //TODO: Make CSVTermMap more generic
        switch (term){
            case XPATH_CLASS:
                return new XPathTermMapProcessor();
            case CSV_CLASS:
                return new CSVTermMapProcessor();
            case JSONPATH_CLASS:
                return new JSONPathTermMapProcessor();
            case CSS3_CLASS:
                return new CSS3TermMapProcessor();
            case XLS_CLASS:
                return new CSVTermMapProcessor();
            case XLSX_CLASS:
                return new CSVTermMapProcessor();
            case DBPEDIA_CLASS:
                return new DBpediaTermMapProcessor();
            default:
                log.error("The term " + term + "was not defined.");
                return null;
        }
    }
}
