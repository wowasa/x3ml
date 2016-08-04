/*==============================================================================
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
==============================================================================*/

package eu.delving.x3ml;

import static eu.delving.x3ml.X3MLEngine.exception;
import eu.delving.x3ml.engine.Generator;
import gr.forth.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

/** This class instantiates X3ML Engine and handles the ingestion of user-defined details 
 * in a straightforward manner. Furthermore it can execute the X3ML engine for producing 
 * the desired output. The rationale of this class is to act as an abstraction layer, hiding the 
 * complex details of instantiating and executing the X3ML engine. This means that it requires 
 * only the basic information (mappings and input) and all the rest can be either provided by the user 
 * if needed or some default values are used (i.e. if any value is given for the UUID size then a default value is being used).
 * 
 * The following block shows how the X3MLEngineFactory is being exploited for instantiating and executing 
 * the X3MLEngine a very simple configuration.
 * 
 * X3MLEngineFactory.create()
 *                  .withMappings("mappings.x3ml")
 *                  .withInputFiles("input1.xml", "input2.xml")
 *                  .execute();
 * 
 * The following list enumerates the details that can be configured, their description, and if 
 * they are mandatory or optional; mandatory resources should be provided for the proper execution 
 * of the X3ML Engine, while for the optional resources their default values can be exploited.
 * <ul>
 * <li><b>X3ML mappings file</b>: it is the file that contains the X3ML mappings and 
 * it exists in the form of an XML document. This resource is <u>mandatory</u></li>
 * <li><b>input file(s) or input folder(s)</b>: X3ML engine uses as input a set of files in XML format. This 
 * means that either a list of files or entire folders should be used as input. 
 * This resource is <u>mandatory</u></li>
 * <li><b>generator policy file</b>: the XML file containing the details of the generator policies. 
 * This resource is optional, unless the user defines a generator policy file, only the built-in
 * generators will be exploited.</li>
 * <li><b>UUID length</b>: the value of the length for the generated UUIDs. This resource is optional, 
 * so unless the user defines a value the default value that will be used is 4. </li>
 * <li><b>output file and format</b>: the name/path of the file containing the transformed results or 
 * alternatively the System.out stream. The format can be one of the following (RDF/XML, NTRIPLES, TURTLE).
 * This resource is optional, so unless the user defines them the results will be exported in the System.out stream 
 * in RDF/XML format.</li>
 * <li><b>association table contents</b>: the contents of the association table between the XML input and the 
 * produced RDF output. The contents are exported as an XML file with a user-defined filename/path. 
 * This resource is optional, so unless the user defines it the contents of the association will not be exported. </li>
 * </ul>
 *
 * @author Yannis Marketakis &lt;marketak@ics.forth.gr&gt;
 * @author Nikos Minadakis &lt;minadakn@ics.forth.gr&gt;
 */
public class X3MLEngineFactory {
    private File mappingsFile;
    private Set<File> inputFiles;
    private Set<Pair<File, Boolean>> inputFolders;
    private File generatorPolicyFile;
    private int uuidSize;
    private String associationTableFile;
    private Pair<String,OutputFormat> output;
    private static final Logger LOGGER=Logger.getLogger(X3MLEngineFactory.class);
    
    public enum OutputFormat{
    RDF_XML, 
    NTRIPLES, 
    TURTLE
    }
    
    /* Instantiate the factory with the default values */
    private X3MLEngineFactory(){
        this.mappingsFile=null;
        this.inputFiles=new HashSet<>();
        this.inputFolders=new HashSet<>();
        this.generatorPolicyFile=null;
        this.uuidSize=4;
        this.associationTableFile=null;
        this.output=Pair.of(null, OutputFormat.RDF_XML);
    }
    
    /**Creates a new instance of the X3MLEngineFactory class. After the creation the new 
     * instance contains the default values for many of the configuration details and requires 
     * only the addition of the mappings (the X3ML mappings file) and the input file (or alternatively 
     * the input files, or the folder containing the input files).
     * 
     * @return a new instance of the X3MLEngineFactory class
     */
    public static X3MLEngineFactory create(){
        LOGGER.setLevel(Level.INFO);
        LOGGER.debug("Created an instance of X3MLEngineFactory");
        return new X3MLEngineFactory();
    }
    
    /**Adds the mappings file in the X3MLEngineFactory. 
     * 
     * @param mappingsFile the file with the mappings (X3ML)
     * @return the updated X3MLEngineFactory instance
     */
    public X3MLEngineFactory withMappings(File mappingsFile){
        LOGGER.debug("Added the X3ML mappings file ("+mappingsFile.getAbsolutePath()+")");
        this.mappingsFile=mappingsFile;
        return this;
    }
    
    /** Adds the input files in the X3MLEngineFactory. The methods accepts more than one files 
     * that will be concatenated for producing a single input file. 
     * 
     * @param inputFiles the input (XML) files
     * @return the updated X3MLEngineFactory instance
     */
    public X3MLEngineFactory withInputFiles(File ... inputFiles){
        for(File f : inputFiles){
            LOGGER.debug("Added the XML input file ("+f.getAbsolutePath()+")");
        }
        this.inputFiles.addAll(Arrays.asList(inputFiles));
        return this;
    }
    
    /**Adds the folder that contains the input files (in XML format). 
     * 
     * @param inputFolder the folder that contains the (XML) input files
     * @param recursiveSearch if true it will search in the closure of the folder for XML files, 
     * otherwise it will return only the direct contents of the given directory
     * @return the updated X3MLEngineFactory instance */
    public X3MLEngineFactory withInputFolder(File inputFolder, boolean recursiveSearch){
        LOGGER.debug("Added the XML input folder ("+inputFolder.getAbsolutePath()+"), recursive search: "+recursiveSearch);
        this.inputFolders.add(Pair.of(inputFolder, recursiveSearch));
        return this;
    }
    
    /**Adds the generator policy file.
     * 
     * @param generatorPolicyFile the file (in XML) that contains the generator policy (for URIs and Literals)
     * @return the updated X3MLEngineFactory instance */
    public X3MLEngineFactory withGeneratorPolicy(File generatorPolicyFile){
        LOGGER.debug("Added the Generator policy file ("+generatorPolicyFile.getAbsolutePath()+")");
        this.generatorPolicyFile=generatorPolicyFile;
        return this;
    }
    
    /**Sets the size of the UUID generator. The value denotes the length of the generated UUID value. 
     * For example if the value is set to 2 the it will generate UUIDs like uuid:AD, uuid:AY, 
     * if it is set to 3, it will generate UUIDs like uuid:ACB, etc.
     * The default value that is used is 4, if no other value is provided.
     * 
     * @param uuidLength value of the length for the produced UUIDs
     * @return the updated X3MLEngineFactory instance */
    public X3MLEngineFactory withUuidSize(int uuidLength){
        LOGGER.debug("Set the UUID size to "+uuidLength);
        this.uuidSize=uuidLength;
        return this;
    }
    
    /**Sets the details about the transformed resources. More specifically it allows 
     * defining the name of the file that will be exported, as well as the desired 
     * format (one of RDF/XML, NTRIPLES, TURTLE).
     * If the filename is left intentionally or is left null then instead of exporting
     * the resources on a file, they will be exported @ System.out
     * The default behavior is to export transformed data to System.out in RDF/XML format.
     * 
     * @param filename the name of the file containing the exported data
     * @param format the format of the exported data 
     * @return the updated X3MLEngineFactory instance */
    public X3MLEngineFactory withOutput(String filename, OutputFormat format){
        String output=(filename==null || filename.isEmpty())? "System.out":"File("+filename+")";
        LOGGER.debug("Set the output to "+output+" and the format to "+format);
        this.output=Pair.of(filename, format);
        return this;
    }
    
    /**Sets the name of the file where the contents of the association table will be exported, 
     * as a file in XML format. If the value is left intensionally left or null then the 
     * contents of the association table will not be exported. 
     * The default behavior is NOT to export the contents of the association table 
     *
     * @param associationTableFilename the filename of the XML file containing the contents of the 
     * association table
     * @return the updated X3MLEngineFactory instance */
    public X3MLEngineFactory withAssociationTable(String associationTableFilename){
        if(associationTableFilename==null || associationTableFilename.isEmpty()){
            LOGGER.debug("Disabled the export of the association table");
        }else{
            LOGGER.debug("Enabled the export of the association table in the file "+associationTableFilename);
        }
        this.associationTableFile=associationTableFilename;
        return this;
    }
    
    /** Sets the level of logging to verbose for more informative logging messages.
     * This option enables the debug messages to appear. 
     * 
     * @return the updated X3MLEngineFactory instance */
    public X3MLEngineFactory withVerboseLogging(){
        LOGGER.debug("Changed the logging level to verbose");
        LOGGER.setLevel(Level.DEBUG);
        return this;
    }
    
    /** Execute the X3ML Engine with the given configuration. If the mandatory resources 
     * have not been defined (the X3ML mappings file and the XML input file(s)/folder) then 
     * an exception is thrown, and the execution is terminated.
     * If the optional resources have not been defined then the default values are used 
     * (for more information about the resources and their default values see the description of the
     * X3MLEngineFactory class). */
    public void execute(){
        this.validateConfig();
        this.informUserAboutConfiguration();
        X3MLEngine engine=this.createX3MLEngine();
        Generator policy=X3MLGeneratorPolicy.load(this.getGeneratorPolicy(), X3MLGeneratorPolicy.createUUIDSource(this.uuidSize));
        Element sourceRoot=this.getInput();
        X3MLEngine.Output output = engine.execute(sourceRoot, policy);
        this.outputResults(output);
    }
    
    /* creates an instance of the X3ML engine using the provided X3ML mappings file */
    private X3MLEngine createX3MLEngine(){
        try{
            return X3MLEngine.load(new FileInputStream(this.mappingsFile));
        }catch(FileNotFoundException ex){
            throw exception("Cannot find the X3ML mappings file (\""+this.mappingsFile.getAbsolutePath()+"\")", ex);
        }
    }
    
    /* creates an input stream using the provided generator policy otherwise null 
    (no generator polixy file will be used)*/
    private InputStream getGeneratorPolicy(){
        try{
            return this.generatorPolicyFile==null?null:new FileInputStream(this.generatorPolicyFile);
        }catch(FileNotFoundException ex){
            throw exception("Cannot find the generator policy file (\""+this.generatorPolicyFile.getAbsolutePath()+"\")", ex);
        }
    }
    
    /* parses the input (either it is a single file, multiple files, single folder or multiple folders).
    It uses all the given resources to produce a single input element (DOM) */
    private Element getInput(){
        List<InputStream> inputCollections=new ArrayList<>();
        try{
            for(String filepath : this.getInputFilesListing()){
                inputCollections.add(new FileInputStream(new File(filepath)));
            }
        }catch(FileNotFoundException ex){
            throw exception("Cannot find XML input file",ex);
        }
        if(inputCollections.isEmpty()){
            throw exception("The XML input file list is empty");
        }
        return Utils.parseMultipleXMLFiles(inputCollections);
    }
    
    /* Validates that the mandatory elements (input and mappings) have been provided */
    private void validateConfig(){
        if(this.mappingsFile==null){
            throw exception("The mappings file (x3ml) is missing.");
        }
        if(this.inputFiles.isEmpty() && this.inputFolders.isEmpty()){
            throw exception("The input file(s) or folder(s) are missing.");
        }
    }
    
    private void outputResults(X3MLEngine.Output output){
        try{
            switch(this.output.getRight()){
                case RDF_XML:
                    if(this.output.getLeft()==null || this.output.getLeft().isEmpty()){
                        output.writeXML(System.out);
                    }else{
                        output.write(new PrintStream(new File(this.output.getLeft())), "application/rdf+xml");
                    }
                    break;
                case NTRIPLES:
                    if(this.output.getLeft()==null || this.output.getLeft().isEmpty()){
                        output.write(System.out,"application/n-triples");
                    }else{
                        output.write(new PrintStream(new File(this.output.getLeft())), "application/n-triples");
                    }
                    break;
                case TURTLE:
                    if(this.output.getLeft()==null || this.output.getLeft().isEmpty()){
                        output.write(System.out,"text/turtle");
                    }else{
                        output.write(new PrintStream(new File(this.output.getLeft())), "text/turtle");
                    }
                    break;
            }
        }catch(FileNotFoundException ex){
            throw exception("An error occured while exporting results",ex);
        }
    }
    
    /* prints - using logger - the configuration details */
    private void informUserAboutConfiguration(){
        LOGGER.info("X3ML Engine configuration details");
        LOGGER.info("X3ML Engine Mappings file: "+this.mappingsFile.getAbsolutePath());
        LOGGER.info("Input files: "+this.getInputFilesListing());
        LOGGER.info("UUID size: "+this.uuidSize);
        String generatorPolicyMsg=(this.generatorPolicyFile==null)?"none":this.generatorPolicyFile.getAbsolutePath();
        LOGGER.info("Generator policy file: "+generatorPolicyMsg);
        String outputMsg=(this.output.getLeft()==null || !this.output.getLeft().isEmpty())?"System.out":this.output.getLeft();
        LOGGER.info("Output: "+outputMsg);
        LOGGER.info("Output format: "+this.output.getRight());
        String associationTableExportMsg=(this.associationTableFile==null || !this.associationTableFile.isEmpty())?"Disabled":"Enabled, file: "+this.associationTableFile;
        LOGGER.info("Export sssociation table: "+associationTableExportMsg);
    }
    
    /* returns the input files (their absolute paths) taking into account all the files and the folders
    that has been provided by the user */
    private Collection<String> getInputFilesListing(){
        Set<String> retSet=new HashSet<>();
        for(File inputFile : this.inputFiles){
            retSet.add(inputFile.getAbsolutePath());
        }
        for(Pair<File,Boolean> folder : this.inputFolders){
            for(File f : Utils.retrieveXMLfiles(folder.getLeft(), folder.getRight())){
                retSet.add(f.getAbsolutePath());
            }
        }
        return retSet;
    }
}