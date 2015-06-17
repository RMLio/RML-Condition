package be.ugent.mmlab.rml.condition.processor;

import be.ugent.mmlab.rml.core.RMLMappingFactory;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.processor.RMLProcessor;
import be.ugent.mmlab.rml.processor.termmap.TermMapProcessor;
import be.ugent.mmlab.rml.processor.termmap.TermMapProcessorFactory;
import be.ugent.mmlab.rml.processor.termmap.concrete.ConcreteTermMapFactory;
import be.ugent.mmlab.rml.sesame.RMLSesameDataSet;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 * RML - Conditions
 * 
 * Performer to do joins with rr:joinCondition
 *
 * @author mielvandersande, andimou
 */
public class ConditionalJoinRMLPerformer extends ConditionalNodeRMLPerformer{
    
    private static Log log = LogFactory.getLog(RMLMappingFactory.class);
    private HashMap<String, String> conditions;
    private Resource subject;
    private URI predicate;

    public ConditionalJoinRMLPerformer(RMLProcessor processor, HashMap<String, String> conditions, Resource subject, URI predicate) {
        super(processor);
        this.conditions = conditions;
        this.subject = subject;
        this.predicate = predicate;
    }
    
    public ConditionalJoinRMLPerformer(RMLProcessor processor, Resource subject, URI predicate) {
        super(processor);
        this.subject = subject;
        this.predicate = predicate;
    }

    /**
     * Compare expressions from join to complete it
     * 
     * @param node current object in parent iteration
     * @param dataset
     * @param map 
     */
    @Override
    public void perform(Object node, RMLSesameDataSet dataset, TriplesMap map) {
        Value object;
        
        //iterate the conditions, execute the expressions and compare both values
        if(conditions != null){
            boolean flag = true;

            iter: for (String expr : conditions.keySet()) {
                String cond = conditions.get(expr).trim();
                TermMapProcessorFactory factory = new ConcreteTermMapFactory();
                TermMapProcessor termMapProcessor = 
                factory.create(map.getLogicalSource().getReferenceFormulation());
                
                List<String> values = termMapProcessor.extractValueFromNode(node, expr);

                for(String value : values){
                    if(value == null || !value.toLowerCase().trim().equals(cond.toLowerCase())){
                            flag = false;
                            break iter;
                    }
                }
            }
            if (flag) {
                object = processor.processSubjectMap(dataset, map.getSubjectMap(), node);
                if (subject != null && object != null) {
                    dataset.add(subject, predicate, object);
                    log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                            + "[ConditionalJoinRMLPerformer:addTriples] Subject "
                            + subject + " Predicate " + predicate + "Object " + object.toString());
                } else {
                    log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                            + "[ConditionalJoinRMLPerformer:addTriples] triple for Subject "
                            + subject + " Predicate " + predicate + "Object " + object
                            + "was not created");
                }
                //TODO: need to merge the following with the supoer.perform
                processor.processSubjectTypeMap(dataset, (Resource) object, map.getSubjectMap(), node);
                if (object == null) {
                    log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                            + "[NodeRMLPerformer:perform] No subject was generated for " + map.getName() + "triple Map and row " + node.toString());
                } else {
                    for (PredicateObjectMap pom : map.getPredicateObjectMaps()) {
                        processor.processPredicateObjectMap(dataset, (Resource) object, pom, node, map);
                    }
                }
            }
        }       
    }
}
