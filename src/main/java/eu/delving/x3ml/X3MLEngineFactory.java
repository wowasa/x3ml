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
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

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
 * @author Yannis Marketakis <marketak@ics.forth.gr>
 * @author Nikos Minadakis <minadakn@ics.forth.gr>
 */
public class X3MLEngineFactory {
    private File mappingsFile;
    private Set<File> inputFiles;
    private File inputFolder;
    private File generatorPolicyFile;
    private int uuidSize;
    private String associationTableFile;
    private Pair<String,OutputFormat> output;
    
    /* Instantiate the factory with the default values */
    private X3MLEngineFactory(){
        this.mappingsFile=null;
        this.inputFiles=new HashSet<>();
        this.inputFolder=null;
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
        return new X3MLEngineFactory();
    }
    
    /**Adds the mappings file in the X3MLEngineFactory. 
     * 
     * @param mappingsFile the file with the mappings (X3ML)
     * @return the updated X3MLEngineFactory instance
     */
    public X3MLEngineFactory withMappings(File mappingsFile){
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
        this.inputFiles.addAll(Arrays.asList(inputFiles));
        return this;
    }
    
    /**Adds the folder that contains the input files (in XML format). 
     * 
     * @param inputFolder the folder that contains the (XML) input files
     * @return the updated X3MLEngineFactory instance
     */
    public X3MLEngineFactory withInputFolder(File inputFolder){
        this.inputFolder=inputFolder;
        return this;
    }
    
    /**Adds the generator policy file.
     * 
     * @param generatorPolicyFile the file (in XML) that contains the generator policy (for URIs and Literals)
     * @return the updated X3MLEngineFactory instance */
    public X3MLEngineFactory withGeneratorPolicy(File generatorPolicyFile){
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
        this.uuidSize=uuidLength;
        return this;
    }
    
    /**Sets the details about the transformed resources. More specifically it allows 
     * defining the name of the file that will be exported, as well as the desired 
     * format (one of RDF/XML, NTRIPLES, TURTLE).
     * If the filename is left intentionally or is left null then instead of exporting
     * the resources on a file, they will be exported @ System.out
     * 
     * The default behavior is to export transformed data to System.out in RDF/XML format.
     * 
     * @param filename the name of the file containing the exported data
     * @param format the format of the exported data 
     * @return the updated X3MLEngineFactory instance */
    public X3MLEngineFactory withOutput(String filename, OutputFormat format){
        this.output=Pair.of(filename, format);
        return this;
    }
    
    /**Sets the name of the file where the contents of the association table will be exported, 
     * as a file in XML format. If the value is left intensionally left or null then the 
     * contents of the association table will not be exported. 
     * 
     * The default behavior is NOT to export the contents of the association table 
     *
     * @param associationTableFilename the filename of the XML file containing the contents of the 
     * association table
     * @return the updated X3MLEngineFactory instance */
    public X3MLEngineFactory withAssociationTable(String associationTableFilename){
        this.associationTableFile=associationTableFilename;
        return this;
    }
    
    /*TODO*/
    public void execute(){
        this.validateConfig();
    }
    
    private void validateConfig(){
        if(this.mappingsFile==null){
            throw exception("The mappings file (x3ml) is missing.");
        }
        if(this.inputFiles.isEmpty() && this.inputFolder==null){
            throw exception("The input file(s) or folder is missing.");
        }
    }
}

enum OutputFormat{
    RDF_XML, 
    NTRIPLES, 
    TURTLE
}