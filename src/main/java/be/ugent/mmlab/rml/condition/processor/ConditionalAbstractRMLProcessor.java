package be.ugent.mmlab.rml.condition.processor;

import be.ugent.mmlab.rml.core.BindRMLPerformer;
import be.ugent.mmlab.rml.core.ConditionalJoinRMLPerformer;
import be.ugent.mmlab.rml.core.JoinRMLPerformer;
import be.ugent.mmlab.rml.core.RMLPerformer;
import be.ugent.mmlab.rml.core.SimpleReferencePerformer;
import be.ugent.mmlab.rml.model.GraphMap;
import be.ugent.mmlab.rml.model.JoinCondition;
import be.ugent.mmlab.rml.model.ObjectMap;
import be.ugent.mmlab.rml.model.PredicateMap;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.ReferencingObjectMap;
import be.ugent.mmlab.rml.model.SubjectMap;
import be.ugent.mmlab.rml.model.TermMap;
import be.ugent.mmlab.rml.model.TermType;
import static be.ugent.mmlab.rml.model.TermType.BLANK_NODE;
import static be.ugent.mmlab.rml.model.TermType.IRI;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.condition.model.BindCondition;
import be.ugent.mmlab.rml.model.reference.ReferenceIdentifierImpl;
import be.ugent.mmlab.rml.processor.concrete.ConcreteRMLProcessorFactory;
import be.ugent.mmlab.rml.input.processor.AbstractInputProcessor;
import be.ugent.mmlab.rml.input.processor.InputProcessor;
import be.ugent.mmlab.rml.condition.model.conditionalTermMap;
import be.ugent.mmlab.rml.processor.AbstractRMLProcessor;
import be.ugent.mmlab.rml.processor.RMLProcessor;
import be.ugent.mmlab.rml.processor.RMLProcessorFactory;
import be.ugent.mmlab.rml.processor.termmap.TermMapProcessorFactory;
import be.ugent.mmlab.rml.processor.termmap.concrete.ConcreteTermMapFactory;
import be.ugent.mmlab.rml.sesame.RMLSesameDataSet;
import be.ugent.mmlab.rml.tools.R2RMLToolkit;
import be.ugent.mmlab.rml.vocabulary.QLVocabulary.QLTerm;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import java.util.regex.Pattern;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * RML - Conditions
 *
 * @author andimou
 */
public abstract class ConditionalAbstractRMLProcessor extends AbstractRMLProcessor implements RMLProcessor {

    /**
     * Gets the globally defined identifier-to-path map
     *
     * @param ls the current LogicalSource
     * @return the location of the file or table
     */
    
    // Log
    private static final Logger log = LogManager.getLogger(ConditionalAbstractRMLProcessor.class);

    /*protected String getIdentifier(LogicalSource ls) {
        return RMLEngine.getFileMap().getProperty(ls.getIdentifier());
    }*/

    /**
     *
     * Process the subject map
     *
     * @param dataset
     * @param subjectMap
     * @param node
     * @return the created subject
     */
    @Override
     public Resource processSubjectMap(RMLSesameDataSet dataset, SubjectMap subjectMap, Object node) { 
        log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Generating conditional SubjectMap... ");
        
        //Get the uri
        List<String> values = processTermMap((conditionalTermMap) subjectMap, node);
        
        log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "values " + values);
        
        //log.info("Abstract RML Processor Graph Map" + subjectMap.getGraphMaps().toString());
        if (values == null || values.isEmpty()) 
            if(subjectMap.getTermType() != BLANK_NODE){
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                        + "No subject was generated for conditional Subject Map " + subjectMap.toString());
                return null;
            }
            
        String value = null;
        if(subjectMap.getTermType() != BLANK_NODE){
            //Since it is the subject, more than one value is not allowed. 
            //Only return the first one. Throw exception if not?
            value = values.get(0);

            if ((value == null) || (value.equals(""))) {
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                        + "No subject was generated for conditional Subject Map " + subjectMap.toString());
                return null;
            }
        }
        
        Resource subject ;
                
        //TODO: doublicate code from ObjectMap - they should be handled together
        switch (subjectMap.getTermType()) {
            case IRI:
                if (value != null && !value.equals("")) 
                    if (value.startsWith("www.")) {
                        value = "http://" + value;
                    }
                    subject = new URIImpl(value);
                break;
            case BLANK_NODE:
                subject = new BNodeImpl(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(10));
                break;
            default:
                subject = new URIImpl(value);
        }
        return subject;
    }
    
    /**
     *
     * @param dataset
     * @param subject
     * @param subjectMap
     * @param node
     */
    @Override
    public void processSubjectTypeMap(
            RMLSesameDataSet dataset, Resource subject, SubjectMap subjectMap, Object node) {

        //Add the type triples
        Set<org.openrdf.model.URI> classIRIs = subjectMap.getClassIRIs();
        if(subject != null)
            for (org.openrdf.model.URI classIRI : classIRIs) 
                if(subjectMap.getGraphMaps().isEmpty())
                    dataset.add(subject, RDF.TYPE, classIRI);
                else
                    for (GraphMap map : subjectMap.getGraphMaps()) 
                        if (map.getConstantValue() != null) 
                            dataset.add(subject, RDF.TYPE, classIRI, new URIImpl(map.getConstantValue().toString()));
    }

    /**
     * Process any Term Map
     *
     * @param map current term map
     * @param node current node in iteration
     * @return the resulting value
     */

    public List<String> processTermMap(conditionalTermMap map, Object node) {
        List<String> values = new ArrayList<>(), valueList = new ArrayList<>();
        List<String> validValues = new ArrayList<>();
        
        TermMapProcessorFactory factory = new ConcreteTermMapFactory();
        this.termMapProcessor = 
                    factory.create(map.getOwnTriplesMap().getLogicalSource().getReferenceFormulation());

        switch (map.getTermMapType()) {
            case REFERENCE_VALUED:
                //Get the expression and extract the value
                ReferenceIdentifierImpl identifier = (ReferenceIdentifierImpl) map.getReferenceValue();
                values = this.termMapProcessor.extractValueFromNode(node, identifier.toString().trim());
                for (String value : values) {
                    valueList.addAll(ConditionProcessor.processAllConditions(map, value));

                    if (valueList.isEmpty()) {
                        if (map.getSplit() != null
                                || map.getProcess() != null
                                || map.getReplace() != null) {
                            List<String> tempValueList =
                                    ConditionProcessor.postProcessTermMap(map, value, null);
                            if (tempValueList != null) {
                                for (String tempVal : tempValueList) {
                                    valueList.add(tempVal);
                                }
                            }
                        } else {
                            valueList.add(value.trim().replace("\n", " "));
                        }
                    }
                }
                return valueList;

            case CONSTANT_VALUED:
                //Extract the value directly from the mapping
                values.add(map.getConstantValue().stringValue().trim());
                return values;

            case TEMPLATE_VALUED:
                //Resolve the template
                String template = map.getStringTemplate();
                Set<String> tokens = R2RMLToolkit.extractColumnNamesFromStringTemplate(template);
                for (String expression : tokens) {
                    
                    List<String> replacements = 
                            this.termMapProcessor.extractValueFromNode(node, expression);
                    if (replacements != null) {
                        for (int i = 0; i < replacements.size(); i++) {
                            if (values.size() < (i + 1)) {
                                values.add(template);
                            }
                            String replacement = replacements.get(i);
                            if (replacement != null || !replacement.equals("")) {
                                //process equal conditions
                                List<String> list;

                                //process split, process and replace conditions
                                if (map.getSplit() != null || map.getProcess() != null || map.getReplace() != null) {
                                    list = ConditionProcessor.postProcessTermMap(map, replacement, null);
                                    if (list != null) {
                                        for (String val : list) {
                                            String temp = this.termMapProcessor.processTemplate(map, expression, template, val);
                                            if (R2RMLToolkit.extractColumnNamesFromStringTemplate(temp).isEmpty()) {
                                                validValues.add(temp);
                                            }
                                        }
                                    }
                                } else {
                                    list = ConditionProcessor.processAllConditions(map, replacement);

                                    if (!list.isEmpty()) {
                                        for (String value : list) {
                                            values.add(template);
                                            String temp = this.termMapProcessor.processTemplate(map, expression, template, value);

                                            if (R2RMLToolkit.extractColumnNamesFromStringTemplate(temp).isEmpty()) {
                                                validValues.add(temp);
                                            } else {
                                                template = temp;
                                            }
                                        }
                                    } else if (!replacement.isEmpty()) {
                                        String temp = this.termMapProcessor.processTemplate(map, expression, template, replacement);
                                        template = temp;
                                        if (R2RMLToolkit.extractColumnNamesFromStringTemplate(temp).isEmpty()) {
                                            validValues.add(temp);
                                        }
                                    }

                                }
                            } else {
                                log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                                        + "No suitable replacement for template " + template + ".");
                                return null;
                            }
                        }
                    } else {
                        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                                + "No replacements found for template " + template + ".");
                        return null;
                    }
                }

                //Check if there are any placeholders left in the templates and remove uris that are not
                for (String uri : values) {
                    if (R2RMLToolkit.extractColumnNamesFromStringTemplate(uri).isEmpty()) {
                        validValues.add(uri);
                    }
                }
                return validValues;

            default:
                return values;
        }

    }
    
    /**
     *
     * @param map
     * @param replacements
     * @param expression
     * @return
     */
    public List<String> processTemplate(
            conditionalTermMap map, List<String> replacements, String expression) {
        List<String> values = new ArrayList<>(), validValues = new ArrayList<>();
        String template = map.getStringTemplate();
        
        for (int i = 0; i < replacements.size(); i++) {
            if (values.size() < (i + 1)) {
                values.add(template);
            }
            String replacement = replacements.get(i);
            if (replacement != null || !replacement.equals("")) {
                List<String> list;

                //process split, process and replace conditions
                if (map.getSplit() != null || map.getProcess() != null || map.getReplace() != null) {
                    list = ConditionProcessor.postProcessTermMap(map, replacement, null);
                    if (list != null) {
                        for (String val : list) {
                            String temp = this.termMapProcessor.processTemplate(map, expression, template, val);
                            if (R2RMLToolkit.extractColumnNamesFromStringTemplate(temp).isEmpty()) {
                                validValues.add(temp);
                            }
                        }
                    }
                } else {
                    list = ConditionProcessor.processAllConditions(map, replacement);

                    if (!list.isEmpty()) {
                        for (String value : list) {
                            values.add(template);
                            String temp = this.termMapProcessor.processTemplate(map, expression, template, value);

                            if (R2RMLToolkit.extractColumnNamesFromStringTemplate(temp).isEmpty()) {
                                validValues.add(temp);
                            } else {
                                template = temp;
                            }
                        }
                    } else if (!replacement.isEmpty()) {
                        String temp = this.termMapProcessor.processTemplate(map, expression, template, replacement);
                        template = temp;
                        if (R2RMLToolkit.extractColumnNamesFromStringTemplate(temp).isEmpty()) {
                            validValues.add(temp);
                        }
                    }

                }
            } else {
                log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                        + "No suitable replacement for template " + template + ".");
                return null;
            }
        }
        
        return validValues;
    }
    
    /**
     *
     * @param expression
     * @param template
     * @param termType
     * @param referenceFormulation
     * @param replacement
     * @return
     */
    @Override
    public String processTemplate(String expression, String template, String termType,
            QLTerm referenceFormulation, String replacement) {
        if (expression.contains("[")) {
            expression = expression.replaceAll("\\[", "").replaceAll("\\]", "");
            template = template.replaceAll("\\[", "").replaceAll("\\]", "");
        }
        //JSONPath expression cause problems when replacing, remove the $ first
        if ((referenceFormulation == QLTerm.JSONPATH_CLASS)
                && expression.contains("$")) {
            expression = expression.replaceAll("\\$", "");
            template = template.replaceAll("\\$", "");
        }
        try {
            if (termType.equals(TermType.IRI.toString())) {
                //TODO: replace the following with URIbuilder
                template = template.replaceAll("\\{" + Pattern.quote(expression) + "\\}",
                        URLEncoder.encode(replacement, "UTF-8")
                        .replaceAll("\\+", "%20")
                        .replaceAll("\\%21", "!")
                        .replaceAll("\\%27", "'")
                        .replaceAll("\\%28", "(")
                        .replaceAll("\\%29", ")")
                        .replaceAll("\\%7E", "~"));
            } else {
                template = template.replaceAll("\\{" + expression + "\\}", replacement);
            }
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
        }
        return template.toString();
    }
    
    /**
     * Process a predicate object map
     *
     * @param dataset
     * @param subject   the subject from the triple
     * @param pom       the predicate object map
     * @param node      the current node
     */
    @Override
    public void processPredicateObjectMap(
            RMLSesameDataSet dataset, Resource subject, PredicateObjectMap pom, Object node, TriplesMap map) {

        Set<PredicateMap> predicateMaps = pom.getPredicateMaps();
        //Go over each predicate map
        for (PredicateMap predicateMap : predicateMaps) {
            //Get the predicate
            List<URI> predicates = processPredicateMap(predicateMap, node);
            
            URI predicate = predicates.get(0);
            
            //Process the joins first
            processPredicateObjectMap_RefObjMap(dataset, subject, predicate, pom, node, map);
            
            //process the objectmaps
            processPredicateObjectMap_ObjMap(dataset, subject, predicate, pom, node);
            
        }
    }
    
    private void processPredicateObjectMap_RefObjMap(
            RMLSesameDataSet dataset, Resource subject, URI predicate,
            PredicateObjectMap pom, Object node, TriplesMap map) {
        String template ;
        Set<ReferencingObjectMap> referencingObjectMaps = pom.getReferencingObjectMaps();
        for (ReferencingObjectMap referencingObjectMap : referencingObjectMaps) {
            Set<JoinCondition> joinConditions = referencingObjectMap.getJoinConditions();
            
            TriplesMap parentTriplesMap = referencingObjectMap.getParentTriplesMap();
            
            Set<BindCondition> bindConditions = referencingObjectMap.getBindConditions();
            if(bindConditions != null & bindConditions.size() > 0 )
                template = processBindConditions(node, parentTriplesMap, bindConditions);
            else 
                template = parentTriplesMap.getLogicalSource().getInputSource().getSource();

            //Create the processor based on the parent triples map to perform the join
            RMLProcessorFactory factory = new ConcreteRMLProcessorFactory();
            QLTerm referenceFormulation = parentTriplesMap.getLogicalSource().getReferenceFormulation();

            InputProcessor inputProcessor = new AbstractInputProcessor();
            InputStream input = inputProcessor.getInputStream(parentTriplesMap, template);
            
            RMLProcessor processor = factory.create(referenceFormulation);
            RMLPerformer performer = null;
            
            //different Logical Source and no Conditions
            if(bindConditions != null & bindConditions.size() > 0 ){
                performer = new BindRMLPerformer(processor, subject, predicate);
                processor.execute(dataset, parentTriplesMap, performer, input);
            }
            else if (joinConditions.isEmpty()
                    & !parentTriplesMap.getLogicalSource().getInputSource().getSource().equals(
                    map.getLogicalSource().getInputSource().getSource())) {
                performer = new JoinRMLPerformer(processor, subject, predicate);
                processor.execute(dataset, parentTriplesMap, performer, input);
            } //same Logical Source and no Conditions
            else if (joinConditions.isEmpty()
                    & parentTriplesMap.getLogicalSource().getInputSource().getSource().equals(
                    map.getLogicalSource().getInputSource().getSource())) {
                performer = new SimpleReferencePerformer(processor, subject, predicate);
                if ((parentTriplesMap.getLogicalSource().getReferenceFormulation().toString().equals("CSV"))
                        || (parentTriplesMap.getLogicalSource().getReference().equals(map.getLogicalSource().getReference()))) {
                    performer.perform(node, dataset, parentTriplesMap);
                } else {
                    int end = map.getLogicalSource().getReference().length();
                    //log.info("RML:AbstractRMLProcessor " + parentTriplesMap.getLogicalSource().getReference().toString());
                    String expression = "";
                    //TODO:merge it with the performer's switch-case
                    switch (parentTriplesMap.getLogicalSource().getReferenceFormulation().toString()) {
                        case "XPath":
                            expression = parentTriplesMap.getLogicalSource().getReference().toString().substring(end);
                            break;
                        case "JSONPath":
                            expression = parentTriplesMap.getLogicalSource().getReference().toString().substring(end + 1);
                            break;
                        case "CSS3":
                            expression = parentTriplesMap.getLogicalSource().getReference().toString().substring(end);
                            break;
                    }
                    processor.execute_node(dataset, expression, parentTriplesMap, performer, node, null);
                }
            } //Conditions
            else {
                //Build a join map where
                //  key: the parent expression
                //  value: the value extracted from the child
                processJoinConditions(node, performer, processor, subject, predicate, 
                        dataset, input, parentTriplesMap, joinConditions);
            }
        }
    }
    
    @Override
    public void processJoinConditions(Object node, RMLPerformer performer, RMLProcessor processor, 
            Resource subject, URI predicate, RMLSesameDataSet dataset, InputStream input, 
            TriplesMap parentTriplesMap, Set<JoinCondition> joinConditions) {
        HashMap<String, String> joinMap = new HashMap<>();

        for (JoinCondition joinCondition : joinConditions) {
            List<String> childValues = this.termMapProcessor.extractValueFromNode(node, joinCondition.getChild());
            //Allow multiple values as child - fits with RML's definition of multiple Object Maps
            for (String childValue : childValues) {               
                joinMap.put(joinCondition.getParent(), childValue);
                if (joinMap.size() == joinConditions.size()) {
                    performer = new ConditionalJoinRMLPerformer(processor, joinMap, subject, predicate);
                    processor.execute(dataset, parentTriplesMap, performer, input);
                }
            }
        }
    }
    
    /**
     *
     * @param node
     * @param parentTriplesMap
     * @param bindConditions
     * @return
     */
    public String processBindConditions(Object node, TriplesMap parentTriplesMap, 
            Set<BindCondition> bindConditions) {
        String finalTemplate = null;
        
        for (BindCondition bindCondition : bindConditions) {
            List<String> bindReferenceValues = this.termMapProcessor.extractValueFromNode(node, bindCondition.getReference());
            String template = parentTriplesMap.getLogicalSource().getInputSource().getSource();
            String termType = TermType.IRI.toString();
            QLTerm referenceFormulation = parentTriplesMap.getLogicalSource().getReferenceFormulation();
            finalTemplate = processTemplate(bindCondition.getValue(), template, termType,
                               referenceFormulation, bindReferenceValues.get(0));
        }
        return finalTemplate;
    }
    
    /**
     *
     * @param dataset
     * @param subject
     * @param predicate
     * @param pom
     * @param node
     */
    @Override
    public void processPredicateObjectMap_ObjMap(
            RMLSesameDataSet dataset, Resource subject, URI predicate,
            PredicateObjectMap pom, Object node) {
        Set<ObjectMap> objectMaps = pom.getObjectMaps();
        for (ObjectMap objectMap : objectMaps) {
            //Get the one or more objects returned by the object map
            List<Value> objects = processObjectMap(objectMap, node);
            if (objects != null) {
                for (Value object : objects) {
                    if (object.stringValue() != null) {
                        Set<GraphMap> graphs = pom.getGraphMaps();
                        if (graphs.isEmpty() && subject != null) {
                            dataset.add(subject, predicate, object);
                        } else {
                            for (GraphMap graph : graphs) {
                                Resource graphResource = new URIImpl(graph.getConstantValue().toString());
                                dataset.add(subject, predicate, object, graphResource);
                            }
                        }

                    }
                }
            } else {
                log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                        + "No object created. No triple will be generated.");
            }
        }
    }

    /**
     * process a predicate map
     *
     * @param predicateMap
     * @param node
     * @return the uri of the extracted predicate
     */
    @Override
    public List<URI> processPredicateMap(PredicateMap predicateMap, Object node) {
        // Get the value
        //TODO:fix the following
        //List<String> values = processTermMap(predicateMap, node);
        List<String> values = new ArrayList<>();
        values.add(predicateMap.getConstantValue().stringValue().trim());
        List<URI> uris = new ArrayList<>();
        for (String value : values) {
            //TODO: add better control
            if(value.startsWith("www."))
                value = "http://" + value;
            uris.add(new URIImpl(value));
        }
        //return the uri
        return uris;
    }

    
    /**
     *
     * @param value
     * @param valueList
     * @param termMap
     * @return
     */
    @Override
    public List<Value> applyTermType(String value, List<Value> valueList, TermMap termMap){
        TermType termType = termMap.getTermType();
        String languageTag = termMap.getLanguageTag();
        URI datatype = termMap.getDataType();
        
        switch (termType) {
            case IRI:
                if (value != null && !value.equals("")) {
                    if (value.startsWith("www.")) {
                        value = "http://" + value;
                    }
                    if (valueList == null) {
                        valueList = new ArrayList<Value>();
                    }
                    try {
                        new URIImpl(cleansing(value));
                    } catch (Exception e) {
                        return valueList;
                    }
                    valueList.add(new URIImpl(cleansing(value)));
                } 
                break;
            case BLANK_NODE:
                valueList.add(new BNodeImpl(cleansing(value)));
                break;
            case LITERAL:
                if (languageTag != null && !value.equals("")) {
                    if (valueList == null) {
                        valueList = new ArrayList<Value>();
                    }
                    value = cleansing(value);
                    valueList.add(new LiteralImpl(value, languageTag));
                } else if (value != null && !value.equals("") && datatype != null) {
                    valueList.add(new LiteralImpl(value, datatype));
                } else if (value != null && !value.equals("")) {
                    valueList.add(new LiteralImpl(value.trim()));
                }
        }
        return valueList;
    }
    
    /**
     *
     * @param split
     * @param node
     * @return
     */
     public List<String> postProcessLogicalSource(String split, Object node) {
        String[] list;
        List<String> valueList = null;

        if (split != null) {
            list = node.toString().split(split);

            for (String item : list) {
                if (valueList == null) {
                    valueList = new ArrayList<String>();
                }
                valueList.add(cleansing(item));
            }
        }
        return valueList;
    }
}
