package be.ugent.mmlab.rml.condition.processor;

import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.processor.RMLProcessor;
import be.ugent.mmlab.rml.sesame.RMLSesameDataSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 * RML - Conditions
 * 
 * Performer to do joins without any join conditions
 *
 * @author mielvandersande, andimou
 */
public class BindRMLPerformer extends ConditionalNodeRMLPerformer {

    // Log
    private static final Logger log = LogManager.getLogger(BindRMLPerformer.class);
    private Resource subject;
    private URI predicate;

    /**
     *
     * @param processor
     * @param subject
     * @param predicate
     */
    public BindRMLPerformer(RMLProcessor processor, Resource subject, URI predicate) {
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
        Value object = processor.processSubjectMap(dataset, map.getSubjectMap(), node);

        if (object == null) {
            return;
        } else {
            processor.processSubjectTypeMap(dataset, (Resource) object, map.getSubjectMap(), node);
            for (PredicateObjectMap pom : map.getPredicateObjectMaps()) {
                processor.processPredicateObjectMap(dataset, (Resource) object, pom, node, map);
            }
        }

        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "[JoinRMLPerformer:addTriples] Subject "
                + subject + " Predicate " + predicate + "Object " + object.toString());

        //add the join triple
        dataset.add(subject, predicate, object);
    }
}
