package eu.delving.x3ml.engine;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class TerminologyModel {
    public static SailRepository repository;
    
    public TerminologyModel(File file) {
        try{
            File skosFile=new File("example/skos.rdf");
            repository=new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
            repository.initialize();
            RepositoryConnection repoConn=repository.getConnection();
            repoConn.add(file, "http://base" , RDFFormat.NTRIPLES, repository.getValueFactory().createURI("http://base"));
            repoConn.add(skosFile, "http://base" , RDFFormat.RDFXML, repository.getValueFactory().createURI("http://base"));
            
            RepositoryResult<Statement> resutls=repoConn.getStatements( 
                   
                    null, 
                    null, 
                     repository.getValueFactory().createURI("http://v1"),
                    true);
            while(resutls.hasNext()){
                System.out.println(resutls.next());
            }
            repoConn.close();
            
        }catch(IOException | RDFParseException | RepositoryException ex){
            ex.printStackTrace();
        }
    }
    
    public static List<String> getBroaderTerms(String term ){
        List<String> broaderTerms=new ArrayList<>();
        try{
            RepositoryConnection repoConn=repository.getConnection();
            
            RepositoryResult<Statement> results=repoConn.getStatements(null, repository.getValueFactory().createURI("http://www.w3.org/2000/01/rdf-schema#label"), repository.getValueFactory().createLiteral(term), false);
            String termUri=results.next().getSubject().stringValue();
            System.out.println("Term URI: "+termUri);

            results=repoConn.getStatements(null, repository.getValueFactory().createURI("http://www.w3.org/2004/02/skos/core#broaderTransitive"), repository.getValueFactory().createURI(termUri), true);
            List<String> broaderTermsUris=new ArrayList<>();
            while(results.hasNext()){
                broaderTermsUris.add(results.next().getSubject().stringValue());
            }
            
            for(String broaderTermUri : broaderTermsUris){
                results=repoConn.getStatements(repository.getValueFactory().createURI(broaderTermUri), repository.getValueFactory().createURI("http://www.w3.org/2000/01/rdf-schema#label"), null, true);
                broaderTerms.add(results.next().getObject().stringValue());
            }

            repoConn.close();
            
        }catch(RepositoryException ex){
            ex.printStackTrace();
        }
        return broaderTerms;
    }
    
    public static List<String> getExactMatchTerms(String term ){
        List<String> exactMatchTerms=new ArrayList<>();
        try{
            RepositoryConnection repoConn=repository.getConnection();
            
            RepositoryResult<Statement> results=repoConn.getStatements(null, repository.getValueFactory().createURI("http://www.w3.org/2000/01/rdf-schema#label"), repository.getValueFactory().createLiteral(term), false);
            String termUri=results.next().getSubject().stringValue();
            System.out.println("Term URI: "+termUri);

            results=repoConn.getStatements(null, repository.getValueFactory().createURI("http://www.w3.org/2004/02/skos/core#exactMatch"), repository.getValueFactory().createURI(termUri), true);
            List<String> exactTermsUris=new ArrayList<>();
            while(results.hasNext()){
                exactTermsUris.add(results.next().getSubject().stringValue());
            }
            results=repoConn.getStatements(repository.getValueFactory().createURI(termUri), repository.getValueFactory().createURI("http://www.w3.org/2004/02/skos/core#exactMatch"), null, true);
            while(results.hasNext()){
                exactTermsUris.add(results.next().getObject().stringValue());
            }
            
            for(String broaderTermUri : exactTermsUris){
                results=repoConn.getStatements(repository.getValueFactory().createURI(broaderTermUri), repository.getValueFactory().createURI("http://www.w3.org/2000/01/rdf-schema#label"), null, true);
                exactMatchTerms.add(results.next().getObject().stringValue());
            }

            repoConn.close();
            
        }catch(RepositoryException ex){
            ex.printStackTrace();
        }
        return exactMatchTerms;
    }

}
