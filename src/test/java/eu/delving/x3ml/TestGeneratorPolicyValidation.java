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

import gr.forth.ics.isl.x3ml.X3MLEngine;
import gr.forth.ics.isl.x3ml.X3MLGeneratorPolicy;
import static eu.delving.x3ml.AllTests.compareNTriples;
import static eu.delving.x3ml.AllTests.document;
import static eu.delving.x3ml.AllTests.engine;
import static eu.delving.x3ml.AllTests.errorFree;
import static eu.delving.x3ml.AllTests.xmlToNTriples;
import gr.forth.ics.isl.x3ml.engine.Generator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
//@RunWith(value = BlockJUnit4ClassRunner.class)
public class TestGeneratorPolicyValidation extends TestCase{
    
   /* The success scenario */
    @Test
    public void testGeneratorPolicyValidationSuccess() {
        try{
            X3MLGeneratorPolicy.validateGeneratorPolicyFile(AllTests.resource("/validation_generator_policy/generator_policy_correct.xml"));
            assertTrue("Successfully validated X3ML mappings file",true);
        }catch(X3MLEngine.X3MLException ex){
            fail("The given XML generator policy file is correct"+ex);
        }
    }
    
   /* Wrong generator element */
    @Test
    public void testGeneratorPolicyValidationIncorrect_1() {
        try{
            X3MLGeneratorPolicy.validateGeneratorPolicyFile(AllTests.resource("/validation_generator_policy/generator_policy_incorrect_1.xml"));
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Successfully validated X3ML mappings file",true);
        }
    }
    
   /* wrong attribute in generator element */
    @Test
    public void testGeneratorPolicyValidationIncorrect_3() {
        try{
            X3MLGeneratorPolicy.validateGeneratorPolicyFile(AllTests.resource("/validation_generator_policy/generator_policy_incorrect_3.xml"));
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Successfully validated X3ML mappings file",true);
        }
    }
    
   /* Missing children in generator element */
    @Test
    public void testGeneratorPolicyValidationIncorrect_4() {
        try{
            X3MLGeneratorPolicy.validateGeneratorPolicyFile(AllTests.resource("/validation_generator_policy/generator_policy_incorrect_4.xml"));
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Successfully validated X3ML mappings file",true);
        }
    }
    
   /* wrong co-existence of pattern and custom elements in generator element */
    @Test
    public void testGeneratorPolicyValidationIncorrect_5() {
        try{
            X3MLGeneratorPolicy.validateGeneratorPolicyFile(AllTests.resource("/validation_generator_policy/generator_policy_incorrect_5.xml"));
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Successfully validated X3ML mappings file",true);
        }
    }
    
   /* duplicate names declaration in generator elements */
    @Test
    public void testGeneratorPolicyValidationIncorrect_6() {
        try{
            X3MLGeneratorPolicy.validateGeneratorPolicyFile(AllTests.resource("/validation_generator_policy/generator_policy_incorrect_6.xml"));
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Successfully validated X3ML mappings file",true);
        }
    }
      
}