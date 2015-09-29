/*package be.ugent.mmlab.rml.condition.extractor;

import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.JoinCondition;
import be.ugent.mmlab.rml.model.LogicalSource;
import be.ugent.mmlab.rml.model.RDFTerm.ObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.PredicateMap;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.ReferencingObjectMap;
import be.ugent.mmlab.rml.model.std.StdGraphMap;
import be.ugent.mmlab.rml.model.std.StdJoinCondition;
import be.ugent.mmlab.rml.model.std.StdLogicalSource;
import be.ugent.mmlab.rml.model.std.StdPredicateObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.SubjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.condition.model.BindCondition;
import be.ugent.mmlab.rml.condition.model.std.ConditionalStdLogicalSource;
import be.ugent.mmlab.rml.condition.model.std.ConditionalStdReferencingObjectMap;
import be.ugent.mmlab.rml.condition.model.std.ConditionalStdSubjectMap;
import be.ugent.mmlab.rml.mapdochandler.extraction.ConcreteSourceFactory;
import be.ugent.mmlab.rml.mapdochandler.extraction.RMLMappingExtractor;
import be.ugent.mmlab.rml.mapdochandler.extraction.concrete.PredicateMapExtractor;
import be.ugent.mmlab.rml.mapdochandler.extraction.source.concrete.LocalFileExtractor;
import be.ugent.mmlab.rml.model.Source;
import be.ugent.mmlab.rml.model.std.StdReferenceMap;
import be.ugent.mmlab.rml.model.termMap.ReferenceMap;
import be.ugent.mmlab.rml.sesame.RMLSesameDataSet;
import be.ugent.mmlab.rml.vocabularies.QLVocabulary;
import be.ugent.mmlab.rml.vocabularies.R2RMLVocabulary;
import be.ugent.mmlab.rml.vocabularies.RMLVocabulary;
import be.ugent.mmlab.rml.vocabularies.Term;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openrdf.model.BNode;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDF;

/**
 * RML - Conditions
 *
 * @author andimou
 */
/*public class ConditionalRMLUnValidatedMappingExtractor extends RMLUnValidatedMappingExtractor implements RMLMappingExtractor{
    
    // Log
    private static final Logger log = LogManager.getLogger(RMLMappingExtractor.class);
    // Value factory
    private static ValueFactory vf = new ValueFactoryImpl();
    
    /**
     * Constant-valued term maps can be expressed more concisely using the
     * constant shortcut properties rr:subject, rr:predicate, rr:object and
     * rr:graph. Occurrences of these properties must be treated exactly as if
     * the following triples were present in the mapping graph instead.
     *
     * @param rmlMappingGraph
     */
    /*@Override
    public RMLSesameDataSet replaceShortcuts(RMLSesameDataSet rmlMappingGraph) {
        Map<URI, URI> shortcutPredicates = new HashMap<URI, URI>();
        shortcutPredicates.put(
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.SUBJECT),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.SUBJECT_MAP));
        shortcutPredicates.put(
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.PREDICATE),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.PREDICATE_MAP));
        shortcutPredicates.put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.OBJECT), 
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.OBJECT_MAP));
        shortcutPredicates
                .put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.GRAPH),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.GRAPH_MAP));
        
        for (URI u : shortcutPredicates.keySet()) {
            List<Statement> shortcutTriples = rmlMappingGraph.tuplePattern(
                    null, u, null);
            log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "Number of RML shortcuts found "
                    + "for "
                    + u.getLocalName()
                    + " : "
                    + shortcutTriples.size());
            
            for (Statement shortcutTriple : shortcutTriples) {
                rmlMappingGraph.remove(shortcutTriple.getSubject(),
                        shortcutTriple.getPredicate(),
                        shortcutTriple.getObject());
                BNode blankMap = vf.createBNode();

                URI pMap = vf.createURI(shortcutPredicates.get(u).toString());
                URI pConstant = vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                        + R2RMLVocabulary.R2RMLTerm.CONSTANT);
                rmlMappingGraph.add(shortcutTriple.getSubject(), pMap,
                        blankMap);
                rmlMappingGraph.add(blankMap, pConstant,
                        shortcutTriple.getObject());
            }
        }

        return rmlMappingGraph;
    }
    
    /**
     *
     * @param rmlMappingGraph
     */
    /*@Override
    public RMLSesameDataSet skolemizeStatements(RMLSesameDataSet rmlMappingGraph) {
        Map<URI, URI> predicates = new HashMap<URI, URI>();
        predicates.put(
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.SUBJECT_MAP),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.SUBJECT_MAP));
        predicates.put(
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.PREDICATE_MAP),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.PREDICATE_MAP));
        predicates.put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.OBJECT_MAP),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.OBJECT_MAP));
        predicates.put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.PARENT_TRIPLES_MAP),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.PARENT_TRIPLES_MAP));
        predicates.put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.JOIN_CONDITION),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.JOIN_CONDITION));
        predicates
                .put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.GRAPH_MAP),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.GRAPH_MAP));
        predicates
                .put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.CLASS),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.CLASS));
        predicates
                .put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.CONSTANT),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.CONSTANT));

        predicates
                .put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.TEMPLATE),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.TEMPLATE));
        predicates
                .put(vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.TERM_TYPE),
                vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.TERM_TYPE));

        predicates
                .put(vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.LOGICAL_SOURCE),
                vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.LOGICAL_SOURCE));
        predicates
                .put(vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.SOURCE),
                vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.SOURCE));
        predicates
                .put(vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.REFERENCE_FORMULATION),
                vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.REFERENCE_FORMULATION));
        predicates
                .put(vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.REFERENCE),
                vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.REFERENCE));
        predicates
                .put(vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.ITERATOR),
                vf.createURI(RMLVocabulary.RML_NAMESPACE
                + RMLVocabulary.RMLTerm.ITERATOR));
        
        predicates
                .put(vf.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                + "type"),
                vf.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                + "type"));

        for (URI u : predicates.keySet()) {
            List<Statement> triples = rmlMappingGraph.tuplePattern(
                    null, u, null);
            log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "Number of statements found "
                    + "for "
                    + u.getLocalName()
                    + " : "
                    + triples.size());
        }
        return rmlMappingGraph;
    }

    
    
    
    public String extractInput(RMLSesameDataSet rmlMappingGraph, Resource resource) {
        
        //URI predicate = rmlMappingGraph.URIref("http://www.w3.org/ns/hydra/core#template");
        URI predicate = RDF.TYPE;
         List<Statement> statements = rmlMappingGraph.tuplePattern(
                        (Resource) resource, predicate, null);
         
         return statements.get(0).getObject().stringValue();
         
    }
    
    protected LogicalSource extractLogicalSources(
            RMLSesameDataSet rmlMappingGraph, Resource triplesMapSubject, TriplesMap triplesMap) {

        Resource blankLogicalSource = 
                extractLogicalSource(rmlMappingGraph, triplesMapSubject, triplesMap);
        
        QLVocabulary.QLTerm referenceFormulation =
                getReferenceFormulation(rmlMappingGraph, triplesMapSubject, blankLogicalSource, triplesMap);

        //Extract the iterator to create the iterator. Some formats have null, like CSV or SQL
        List<Statement> iterators = getStatements(
                rmlMappingGraph, blankLogicalSource,
                RMLVocabulary.RML_NAMESPACE, RMLVocabulary.RMLTerm.ITERATOR, triplesMap);
            
        List<Statement> sourceStatements = getStatements(
                rmlMappingGraph,blankLogicalSource,
                RMLVocabulary.RML_NAMESPACE, RMLVocabulary.RMLTerm.SOURCE, triplesMap);
        
        List<Statement> splitStatements = getStatements(
                rmlMappingGraph,blankLogicalSource,
                RMLVocabulary.RML_NAMESPACE, RMLVocabulary.RMLTerm.SPLIT, triplesMap);
        
        //TODO: add separate function for all conditions extraction both for Term Maps and Logical Source
        Set<BindCondition> bindCondition = null; 
        //bindCondition = BindConditionExtractor.extractBindCondition(
        //            rmlMappingGraph, blankLogicalSource);

        LogicalSource logicalSource = null;

        if (!sourceStatements.isEmpty()) {
            //Extract the file identifier
            for (Statement sourceStatement : sourceStatements) {
                String source ;
                Set<InputSource> inputSources;

                //string input
                //TODO: change to LocalInputFile
                if(sourceStatement.getObject().getClass().getSimpleName().equals("MemLiteral")){
                    source = sourceStatement.getObject().stringValue();
                    LocalFileExtractor input = new LocalFileExtractor();
                    inputSources = input.extractInput(rmlMappingGraph, source);
                }
                //object input
                else{
                    ConcreteSourceFactory inputFactory = new ConcreteSourceFactory();
                    inputSources = inputFactory.chooseSource(
                            rmlMappingGraph, (Resource) sourceStatement.getObject());
                }
                
                for (Source inputSource  : inputSources) {

                    if (!iterators.isEmpty()) {
                        if (!splitStatements.isEmpty()) {
                            logicalSource =
                                    new ConditionalStdLogicalSource(
                                    iterators.get(0).getObject().stringValue(), inputSource,
                                    referenceFormulation); //, splitStatements.get(0).getObject().stringValue());
                        } else if ( bindCondition != null) {
                            logicalSource =
                                    new ConditionalStdLogicalSource(
                                    iterators.get(0).getObject().stringValue(),
                                    inputSource, referenceFormulation, bindCondition);
                        } else {
                            logicalSource =
                                    new StdLogicalSource(
                                    iterators.get(0).getObject().stringValue(),
                                    inputSource, referenceFormulation);
                        }
                    } else if (!splitStatements.isEmpty()) {
                        logicalSource =
                                new StdLogicalSource(
                                iterators.get(0).getObject().stringValue(), inputSource,
                                referenceFormulation); //, splitStatements.get(0).getObject().toString());
                    } else {
                        logicalSource = new StdLogicalSource(inputSource, referenceFormulation);
                    }
                }
            }
        }
        
        log.debug(
                Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Logical source extracted : "
                + logicalSource);
        return logicalSource;
    }
    
    protected Resource extractLogicalSource(
            RMLSesameDataSet rmlMappingGraph, Resource triplesMapSubject, TriplesMap triplesMap) {

        List<Statement> logicalSourceStatements = getStatements(
                rmlMappingGraph, triplesMapSubject,
                RMLVocabulary.RML_NAMESPACE, RMLVocabulary.RMLTerm.LOGICAL_SOURCE, triplesMap);
        
        Resource blankLogicalSource = null;
        if (!logicalSourceStatements.isEmpty())
            blankLogicalSource = (Resource) logicalSourceStatements.get(0).getObject();
            //TODO:Check if I need to add another control here
                
        return blankLogicalSource;
    }
       
    protected QLVocabulary.QLTerm getReferenceFormulation(
            RMLSesameDataSet rmlMappingGraph, Resource triplesMapSubject, 
            Resource subject, TriplesMap triplesMap) 
    {       
        List<Statement> statements = getStatements(
                rmlMappingGraph, subject, 
                RMLVocabulary.RML_NAMESPACE, RMLVocabulary.RMLTerm.REFERENCE_FORMULATION, triplesMap);
        
        if (statements.isEmpty()) 
            return null;
        else
            return QLVocabulary.getQLTerms(statements.get(0).getObject().stringValue());
        }
    
    protected SubjectMap extractSubjectMap(
            RMLSesameDataSet rmlMappingGraph, Resource triplesMapSubject,
            Set<GraphMap> savedGraphMaps, TriplesMap triplesMap){
        log.debug(
                Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Extract subject map...");
        
        // Extract subject map
        List<Statement> statements = getStatements(rmlMappingGraph, triplesMapSubject,
                R2RMLVocabulary.R2RML_NAMESPACE, R2RMLVocabulary.R2RMLTerm.SUBJECT_MAP, triplesMap);
        
        Resource subjectMap ; 

        if(statements != null && statements.size() > 0)
            subjectMap = (Resource) statements.get(0).getObject();
        else
            return null;
        
        log.debug(
                Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Found subject map : "
                + subjectMap.stringValue());

        Value constantValue = extractValueFromTermMap(rmlMappingGraph,
                subjectMap, R2RMLVocabulary.R2RMLTerm.CONSTANT, triplesMap);
        String stringTemplate = extractLiteralFromTermMap(rmlMappingGraph,
                subjectMap, R2RMLVocabulary.R2RMLTerm.TEMPLATE, triplesMap);
        URI termType = (URI) extractValueFromTermMap(rmlMappingGraph,
                subjectMap, R2RMLVocabulary.R2RMLTerm.TERM_TYPE, triplesMap);
        String inverseExpression = extractLiteralFromTermMap(rmlMappingGraph,
                subjectMap, R2RMLVocabulary.R2RMLTerm.INVERSE_EXPRESSION, triplesMap);
        ReferenceMap referenceValue = 
                extractReferenceIdentifier(rmlMappingGraph, subjectMap, triplesMap);

        //TODO:Add check if the referenceValue is a valid reference according to the reference formulation
        Set<BindCondition> bindCondition = null; 
        //bindCondition = BindConditionExtractor.extractBindCondition(
        //            rmlMappingGraph, subjectMap);
        
        //AD: The values of the rr:class property must be IRIs. 
        //AD: Would that mean that it can not be a reference to an extract of the input or a template?
        Set<URI> classIRIs = extractURIsFromTermMap(rmlMappingGraph,
                subjectMap, R2RMLVocabulary.R2RMLTerm.CLASS);
        
        //AD:Move it a separate function that extracts the GraphMaps
        Set<GraphMap> graphMaps = new HashSet<GraphMap>();
        Set<Value> graphMapValues = extractValuesFromResource(
                rmlMappingGraph, subjectMap, R2RMLVocabulary.R2RMLTerm.GRAPH_MAP);
       
        if (graphMapValues != null) {
            graphMaps = extractGraphMapValues(rmlMappingGraph, graphMapValues, savedGraphMaps, triplesMap);
            log.info(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "graph Maps returned " + graphMaps);
        }
        
        SubjectMap result = null;
        try {
            result = new ConditionalStdSubjectMap(triplesMap, constantValue,
                    stringTemplate, termType, inverseExpression,
                    referenceValue, classIRIs, graphMaps, bindCondition);
        } catch (Exception ex) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + ex);
        }
        log.debug(
                Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Subject map extracted.");
        return result;
    }
    
          
    //@Override
    /**
     *
     * @param rmlMappingGraph
     * @param triplesMapSubject
     * @param predicateObject
     * @param savedGraphMaps
     * @param triplesMapResources
     * @param triplesMap
     * @return
     */
    /*public PredicateObjectMap extractPredicateObjectMap(
            RMLSesameDataSet rmlMappingGraph,
            Resource triplesMapSubject,
            Resource predicateObject,
            Set<GraphMap> savedGraphMaps,
            Map<Resource, TriplesMap> triplesMapResources,
            TriplesMap triplesMap) {
        URI p = rmlMappingGraph.URIref(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.PREDICATE_MAP);
        
        List<Statement> predicate_statements = rmlMappingGraph.tuplePattern(
                predicateObject, p, null);
        Set<PredicateMap> predicateMaps = new HashSet<PredicateMap>();
        for (Statement predicate_statement : predicate_statements) {
            PredicateMapExtractor predMapExtractor = new PredicateMapExtractor();
            PredicateMap predicateMap = predMapExtractor.extractPredicateMap(
                    rmlMappingGraph, predicate_statement,
                    savedGraphMaps, triplesMap);
            predicateMaps.add(predicateMap);
        }
        // Extract object maps
        URI o = rmlMappingGraph.URIref(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.OBJECT_MAP);
        List<Statement> object_statements = rmlMappingGraph.tuplePattern(predicateObject, o, null);

        Set<ObjectMap> objectMaps = new HashSet<ObjectMap>();
        Set<ReferencingObjectMap> refObjectMaps = new HashSet<ReferencingObjectMap>();
        for (Statement object_statement : object_statements) {
            refObjectMaps = processReferencingObjectMap(
                    rmlMappingGraph, object_statements, savedGraphMaps,
                    triplesMapResources, triplesMap, triplesMapSubject, predicateObject);
            if (refObjectMaps.isEmpty()) {
                ObjectMapExtractor objMapExtractor = new ObjectMapExtractor();
                ObjectMap objectMap = objMapExtractor.extractObjectMap(rmlMappingGraph,
                        (Resource) object_statement.getObject(), savedGraphMaps, triplesMap);
                try {
                    objectMap.setOwnTriplesMap(triplesMapResources.get(triplesMapSubject));
                } catch (Exception ex) {    
                    log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + ex);
                }
                objectMaps.add(objectMap);
            }
            PredicateObjectMap predicateObjectMap = new StdPredicateObjectMap(
                    predicateMaps, objectMaps, refObjectMaps);

            processGraphMaps(rmlMappingGraph, predicateObject, triplesMap, predicateObjectMap, savedGraphMaps);

            log.debug(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "Extract predicate-object map done.");
            return predicateObjectMap;
        }

        return null;
    }
    
    private Set<ReferencingObjectMap> processReferencingObjectMap(
            RMLSesameDataSet rmlMappingGraph, List<Statement> object_statements, Set<GraphMap> savedGraphMaps,
            Map<Resource, TriplesMap> triplesMapResources, TriplesMap triplesMap, Resource triplesMapSubject, Resource predicateObject) {
        Set<ReferencingObjectMap> refObjectMaps = new HashSet<ReferencingObjectMap>();
        try {
           log.debug(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "Try to extract object map..");
            ReferencingObjectMap refObjectMap = extractReferencingObjectMap(
                    rmlMappingGraph, (Resource) object_statements.get(0).getObject(),
                    savedGraphMaps, triplesMapResources, triplesMap);
            if (refObjectMap != null) {
                //refObjectMap.setOwnTriplesMap(triplesMapResources.get(triplesMapSubject));
                refObjectMaps.add(refObjectMap);
            }
            
        } catch (ClassCastException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    +  "A resource was expected in object of objectMap of "
                    + predicateObject.stringValue());
        } 
        return refObjectMaps;
    }
    
    private PredicateObjectMap processGraphMaps(
            RMLSesameDataSet rmlMappingGraph, Resource predicateObject, TriplesMap triplesMap, 
            PredicateObjectMap predicateObjectMap, Set<GraphMap> savedGraphMaps) {
        // Add graphMaps
        Set<GraphMap> graphMaps = new HashSet<GraphMap>();
        Set<Value> graphMapValues = extractValuesFromResource(
                rmlMappingGraph, predicateObject, R2RMLVocabulary.R2RMLTerm.GRAPH_MAP);

        if (graphMapValues != null) {
            graphMaps = extractGraphMapValues(
                    rmlMappingGraph, graphMapValues, savedGraphMaps, triplesMap);
            log.info(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "graph Maps returned " + graphMaps);
        }

        predicateObjectMap.setGraphMaps(graphMaps);
        return predicateObjectMap;
    }
    
    
    
    protected ReferencingObjectMap extractReferencingObjectMap(
            RMLSesameDataSet rmlMappingGraph, Resource object,
            Set<GraphMap> graphMaps,
            Map<Resource, TriplesMap> triplesMapResources, TriplesMap triplesMap){
        try {
            URI parentTriplesMap = (URI) extractValueFromTermMap(rmlMappingGraph,
                    object, R2RMLVocabulary.R2RMLTerm.PARENT_TRIPLES_MAP, triplesMap);
            Set<JoinCondition> joinConditions = extractJoinConditions(
                    rmlMappingGraph, object, triplesMap);

            Set<BindCondition> bindConditions = null; 
            //bindConditions = BindConditionExtractor.extractBindCondition(
            //        rmlMappingGraph, object);
            
            if (parentTriplesMap == null && !joinConditions.isEmpty()) {
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                        + object.stringValue()
                        + " has no parentTriplesMap map defined whereas one or more joinConditions exist"
                        + " : exactly one parentTripleMap is required.");
            }
            if (parentTriplesMap == null && joinConditions.isEmpty()) {
                return null;
            }
            // Extract parent
            boolean contains = false;
            TriplesMap parent = null;
            for (Resource triplesMapResource : triplesMapResources.keySet()) {
                if (triplesMapResource.stringValue().equals(
                        parentTriplesMap.stringValue())) {
                    contains = true;
                    parent = triplesMapResources.get(triplesMapResource);
                    log.debug(
                            Thread.currentThread().getStackTrace()[1].getMethodName() + ": " 
                            + "Parent triples map found : "
                            + triplesMapResource.stringValue());
                    break;
                }
            }
            if (!contains) {
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                        +  object.stringValue()
                        + " reference to parent triples maps is broken : "
                        + parentTriplesMap.stringValue() + " not found.");
            }
            // Link between this reerencing object and its triplesMap parent will be
            // performed
            // at the end f treatment.
            ReferencingObjectMap refObjectMap = new ConditionalStdReferencingObjectMap(null,
                    parent, joinConditions, bindConditions);
            log.debug(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "Extract referencing object map done.");
            return refObjectMap;
        } catch (Exception ex) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + ex);
        }
        return null;
    }
    
    
    
    /**
     *
     * @param rmlMappingGraph
     * @param termType
     * @param term
     * @param triplesMap
     * @return
     */
    /*protected Value extractValueFromTermMap(
            RMLSesameDataSet rmlMappingGraph, Resource termType,
            Enum term, TriplesMap triplesMap) {
        
        List<Statement> statements = 
                getStatements(rmlMappingGraph, term,  termType, triplesMap);
        
        if (statements.isEmpty()) 
            return null;
        else{
            log.debug(
                Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Extracted "
                + term + " : " + statements.get(0).getObject().stringValue());
            return statements.get(0).getObject();
        }
        
    }
    
    private Set<JoinCondition> extractJoinConditions(
            RMLSesameDataSet rmlMappingGraph, Resource object, TriplesMap triplesMap){
        log.debug(
                Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Extract join conditions..");
        Set<JoinCondition> result = new HashSet<JoinCondition>();
        // Extract predicate-object maps
        URI p = rmlMappingGraph.URIref(R2RMLVocabulary.R2RML_NAMESPACE
                + R2RMLVocabulary.R2RMLTerm.JOIN_CONDITION);
        List<Statement> statements = rmlMappingGraph.tuplePattern(object, p, null);
        try {
            for (Statement statement : statements) {
                Resource jc = (Resource) statement.getObject();
                String child = extractLiteralFromTermMap(rmlMappingGraph, jc,
                        R2RMLVocabulary.R2RMLTerm.CHILD, triplesMap);
                String parent = extractLiteralFromTermMap(rmlMappingGraph,
                        jc, R2RMLVocabulary.R2RMLTerm.PARENT, triplesMap);
                if (parent == null || child == null) {
                    log.error(
                            Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                            +  object.stringValue()
                            + " must have exactly two properties child and parent. ");
                }
                try {
                    result.add(new StdJoinCondition(child, parent));
                } catch (Exception ex) {
                    log.error(RMLMappingExtractor.class.getName() + ex);
                } 
            }
        } catch (ClassCastException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "A resource was expected in object of predicateMap of "
                    + object.stringValue());
        } 
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + " Extract join conditions done.");
        return result;
    }
    
    protected String extractLiteralFromTermMap(
            RMLSesameDataSet rmlMappingGraph, Resource termType, Enum term, TriplesMap triplesMap){

        List<Statement> statements = 
                getStatements(rmlMappingGraph, term,  termType, triplesMap);
        
        if (statements.isEmpty()) 
            return null;
        else {
            String result = statements.get(0).getObject().stringValue();
            if (log.isDebugEnabled()) 
                log.debug(
                        Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                        + "Extracted "
                        + term + " : " + result);
            return result;
        }
    }
    
     protected ReferenceMap extractReferenceIdentifier(
            RMLSesameDataSet rmlMappingGraph, Resource resource, TriplesMap triplesMap) {

        String columnValueStr = extractLiteralFromTermMap(
                rmlMappingGraph, resource, R2RMLVocabulary.R2RMLTerm.COLUMN, triplesMap);
        String referenceValueStr = extractLiteralFromTermMap(
                rmlMappingGraph, resource, RMLVocabulary.RMLTerm.REFERENCE, triplesMap);

        if (columnValueStr != null && referenceValueStr != null) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + resource
                    + " has a reference and column defined.");
        }

        if (columnValueStr != null) {
            ReferenceMap refMap = new StdReferenceMap(columnValueStr);
            return refMap.getReferenceValue(columnValueStr);
        }

        ReferenceMap refMap = new StdReferenceMap(referenceValueStr);
        return refMap.getReferenceValue(referenceValueStr);
    }
     
    protected static Set<URI> extractURIsFromTermMap(
            RMLSesameDataSet rmlMappingGraph, Resource termType,
            R2RMLVocabulary.R2RMLTerm term){
            
        URI p = getTermURI(rmlMappingGraph, term);

        List<Statement> statements = rmlMappingGraph.tuplePattern(termType,
                p, null);
        if (statements.isEmpty()) {
            return null;
        }
        Set<URI> uris = new HashSet<URI>();
        for (Statement statement : statements) {
            URI uri = (URI) statement.getObject();
            log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + term + " : " + uri.stringValue());
            uris.add(uri);
        }
        return uris;
    } 
    
    protected static Set<Value> extractValuesFromResource(
            RMLSesameDataSet rmlMappingGraph,
            Resource termType,
            Enum term){
            
        URI p = getTermURI(rmlMappingGraph, term);

        List<Statement> statements = rmlMappingGraph.tuplePattern(termType,
                p, null);
        if (statements.isEmpty()) {
            return null;
        }
        Set<Value> values = new HashSet<Value>();
        for (Statement statement : statements) {
            Value value = statement.getObject();
            log.debug(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + "Extracted "
                    + term + " : " + value.stringValue());
            values.add(value);
        }
        return values;
    }
    
    private Set<GraphMap> extractGraphMapValues(
            RMLSesameDataSet rmlMappingGraph, Set<Value> graphMapValues, 
            Set<GraphMap> savedGraphMaps, TriplesMap triplesMap) {
        
        Set<GraphMap> graphMaps = new HashSet<GraphMap>();
        
            for (Value graphMap : graphMapValues) {
                // Create associated graphMap if it has not already created
                boolean found = false;
                GraphMap graphMapFound = null;
                
                if (found) {
                    graphMaps.add(graphMapFound);
                } else {
                    GraphMap newGraphMap = null;
                    newGraphMap = extractGraphMap(rmlMappingGraph, (Resource) graphMap, triplesMap);
                    
                    savedGraphMaps.add(newGraphMap);
                    graphMaps.add(newGraphMap);
                }
            }
        
        return graphMaps;
    }
    
    protected GraphMap extractGraphMap(
            RMLSesameDataSet rmlMappingGraph,
            Resource graphMap, TriplesMap triplesMap) {
        log.debug(
                Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Extract graph map...");

        Value constantValue = extractValueFromTermMap(rmlMappingGraph,
                graphMap, R2RMLVocabulary.R2RMLTerm.CONSTANT, triplesMap);
        String stringTemplate = extractLiteralFromTermMap(rmlMappingGraph,
                graphMap, R2RMLVocabulary.R2RMLTerm.TEMPLATE, triplesMap);
        String inverseExpression = extractLiteralFromTermMap(rmlMappingGraph,
                graphMap, R2RMLVocabulary.R2RMLTerm.INVERSE_EXPRESSION, triplesMap);

        ReferenceMap referenceValue = 
                extractReferenceIdentifier(rmlMappingGraph, graphMap, triplesMap);

        URI termType = (URI) extractValueFromTermMap(rmlMappingGraph,
                graphMap, R2RMLVocabulary.R2RMLTerm.TERM_TYPE, triplesMap);

        GraphMap result = null;
        try {
            result = new StdGraphMap(constantValue, stringTemplate,
           inverseExpression, referenceValue, termType);
        } catch (Exception ex) {
            log.error(RMLMappingExtractor.class.getName() + ex);
        } 
        
        log.debug(
                Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Graph map extracted.");
        return result;
    }
    
    protected static URI getTermURI(
            RMLSesameDataSet rmlMappingGraph, Enum term) {
        String namespace = R2RMLVocabulary.R2RML_NAMESPACE;

        if (term instanceof RMLVocabulary.RMLTerm) {
            namespace = RMLVocabulary.RML_NAMESPACE;
        } else if (!(term instanceof R2RMLVocabulary.R2RMLTerm)) 
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + term + " is not valid.");

        return rmlMappingGraph
                .URIref(namespace + term);
    }
    
    protected List<Statement> getStatements(
            RMLSesameDataSet rmlMappingGraph, Resource triplesMapSubject, 
            String namespace, Term term, TriplesMap triplesMap){
        URI logicalSource = rmlMappingGraph.URIref(namespace
                + term);
        List<Statement> source = rmlMappingGraph.tuplePattern(
                triplesMapSubject, logicalSource, null);
        
        return source;
    }
    
    protected List<Statement> getStatements(
            RMLSesameDataSet rmlMappingGraph, Enum term,  Resource resource, TriplesMap triplesMap){
        URI p = getTermURI(rmlMappingGraph, term);

        List<Statement> statements = rmlMappingGraph.tuplePattern(resource,
                p, null);
        
        return statements;
    }

}
*/