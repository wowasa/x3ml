package eu.delving.x3ml;

import static eu.delving.x3ml.X3MLEngine.exception;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

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
    private static File mappingsFile;
    private static Set<File> inputFiles;
    private static String inputFolder;
    private static String configurationFile;
    private static int uuidSize;
    private static String associationTableFile;
    private static boolean associationTableMergeWithOutput;
    private static Pair<String,OutputFormat> output;
    
    /* Instantiate it with the default values */
    private X3MLEngineFactory(){
        mappingsFile=null;
        inputFiles=new HashSet<>();
        inputFolder=null;
        configurationFile=null;
        uuidSize=4;
        associationTableFile=null;
        associationTableMergeWithOutput=false;
        output=null;
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
    
    public X3MLEngineFactory withMappings(File mappingsFile){
        X3MLEngineFactory.mappingsFile=mappingsFile;
        return this;
    }
    
    public X3MLEngineFactory withInputFiles(File ... inputFiles){
        X3MLEngineFactory.inputFiles.addAll(Arrays.asList(inputFiles));
        return this;
    }
    
    public X3MLEngineFactory withInputFolder(){
        return null;
    }
    
    public X3MLEngineFactory withGeneratorPolicy(){
        return null;
    }
    
    public X3MLEngineFactory withUuidSize(){
        return null;
    }
    
    public X3MLEngineFactory withOutput(){
        return null;
    }
    
    /*for general details (i.e. associaton table)*/
    public X3MLEngineFactory with(){
        return null;
    }
    
    public void execute(){
        this.validateConfig();
    }
    
    private void validateConfig(){
        if(X3MLEngineFactory.mappingsFile==null){
            throw exception("The mappings file (x3ml) is missing.");
        }
        if(X3MLEngineFactory.inputFiles.isEmpty() && X3MLEngineFactory.inputFolder==null){
            throw exception("The input file(s) or folder is missing.");
        }
    }
}

enum OutputFormat{
    RDF_XML, 
    NTRIPLES, 
    TURTLE
}
