package gr.forth.ics.isl.example;

import eu.delving.x3ml.X3MLEngineFactory;
import java.io.File;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class X3MLFactoryUser {
    
    /* The simplest possible senario. Add only the mandatory details */
    private static void simplestScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .execute();
    }
    
    /* Plaing with generator policies and UUID sizes */
    private static void withGeneratorPolicyScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappings.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withUuidSize(2)
                         .withGeneratorPolicy(new File("example/generator-policy.xml"))
                         .execute();
    }
    
    /* Playing with multiple files */
    private static void multipleFilesScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withInputFiles(new File("example/moreExamples/input1.xml"), new File("example/moreExamples/input2.xml"))
                         .execute();
    }
    
    /* Playing with multiple mapping files */
    private static void multipleMappingFilesScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"), new File("example/mappingsWithoutGenerator2.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .execute();
    }
    
    /* Playing with multiple folders */
    private static void multipleFilesAndFoldersScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withInputFolder(new File("example/moreExamples"), true)
                         .execute();
    }
    
    /* Playing with different output and output formats */
    private static void outputFormatsScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withOutput(null, X3MLEngineFactory.OutputFormat.NTRIPLES)
                         .execute();
        
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withOutput("target/output.rdf", X3MLEngineFactory.OutputFormat.RDF_XML)
                         .execute();
    }
    
    /* Export the contents of the association table */
    private static void exportAssocTableScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withAssociationTable("target/AssociationTable.xml")
                         .execute();
    }
    
    /* Produce more verbose logging output */
    private static void verboseOutputScenario(){
        X3MLEngineFactory.create()
                         .withVerboseLogging()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .execute();
    }
    
    public static void main(String[] args){
        simplestScenario();
        withGeneratorPolicyScenario();
        multipleFilesScenario();
        multipleMappingFilesScenario();
        multipleFilesAndFoldersScenario();
        outputFormatsScenario();
        exportAssocTableScenario();
        verboseOutputScenario();
    }
}