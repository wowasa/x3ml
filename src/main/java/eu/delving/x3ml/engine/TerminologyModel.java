package eu.delving.x3ml.engine;


import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class TerminologyModel {
    public static InfModel infModel;
    private static final Property BROADER_TRANSITIVE_PROPERTY=new PropertyImpl("http://www.w3.org/2004/02/skos/core#broaderTransitive");
    private static final Property EXACT_MATCH_PROPERTY=new PropertyImpl("http://www.w3.org/2004/02/skos/core#exactMatch");
    private static final Property RDFS_LABEL_PROPERTY=new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#label");
    
    public TerminologyModel(File file) {
        try{
        File skosFile=new File("example/skos.rdf");
           
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        Model model=ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, new FileInputStream(new File("example/terms.nt")), Lang.NT);
        RDFDataMgr.read(model, new FileInputStream(skosFile), Lang.RDFXML);

        infModel=ModelFactory.createInfModel(reasoner, model);

        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
    }
    
    public static List<String> getBroaderTerms(String term){
        List<String> broaderTerms=new ArrayList<>();
        
        /* First retrieve the URIs of the skos:concepts  instances that have the corresponding label */
        ResIterator initialTermUriIterator=infModel.listSubjectsWithProperty(RDFS_LABEL_PROPERTY, term);
        Set<Resource> initialTermUris=new HashSet<>();
        while(initialTermUriIterator.hasNext()){
            initialTermUris.add(initialTermUriIterator.next());
        }
        System.out.println("Initial term URIs: "+initialTermUris);
        
        /* Find the URIs of the broader terms */
        Set<Resource> broaderTermUris=new HashSet<>();
        for(Resource initialTermUri : initialTermUris){
            ResIterator broaderTermUrisIterator=infModel.listSubjectsWithProperty(BROADER_TRANSITIVE_PROPERTY, initialTermUri);
            while(broaderTermUrisIterator.hasNext()){
                broaderTermUris.add(broaderTermUrisIterator.next());
            }
        }
        System.out.println("Broader term URIs: "+broaderTermUris);
        
        /* Find the labels of the of the broader term URIs */ 
        for(Resource broaderTermUri : broaderTermUris){
            NodeIterator broaderTermLabelsIterator=infModel.listObjectsOfProperty(broaderTermUri, RDFS_LABEL_PROPERTY);
            while(broaderTermLabelsIterator.hasNext()){
                broaderTerms.add(broaderTermLabelsIterator.next().toString());
            }
        }
        System.out.println("Broader term labels: "+broaderTerms);
        return broaderTerms;
    }
    
    public static List<String> getExactMatchTerms(String term ){
        List<String> exactMatchTerms=new ArrayList<>();
        
        /* First retrieve the URIs of the skos:concepts  instances that have the corresponding label */
        ResIterator initialTermUriIterator=infModel.listSubjectsWithProperty(RDFS_LABEL_PROPERTY, term);
        Set<Resource> initialTermUris=new HashSet<>();
        while(initialTermUriIterator.hasNext()){
            initialTermUris.add(initialTermUriIterator.next());
        }
        System.out.println("Initial term URIs: "+initialTermUris);
        
        /* Find the URIs of the broader terms */
        Set<Resource> exactTermUris=new HashSet<>();
        for(Resource initialTermUri : initialTermUris){
            ResIterator exactTermUrisIterator=infModel.listSubjectsWithProperty(EXACT_MATCH_PROPERTY, initialTermUri);
            while(exactTermUrisIterator.hasNext()){
                exactTermUris.add(exactTermUrisIterator.next());
            }
        }
        System.out.println("Exact term URIs: "+exactTermUris);
        
        /* Find the labels of the of the broader term URIs */ 
        for(Resource exactTermUri : exactTermUris){
            NodeIterator exactTermLabelsIterator=infModel.listObjectsOfProperty(exactTermUri, RDFS_LABEL_PROPERTY);
            while(exactTermLabelsIterator.hasNext()){
                exactMatchTerms.add(exactTermLabelsIterator.next().toString());
            }
        }
        System.out.println("Exact term labels: "+exactMatchTerms);

        return exactMatchTerms;
    }
}
