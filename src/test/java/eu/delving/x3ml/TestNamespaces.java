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

import eu.delving.x3ml.engine.Generator;
import org.junit.Test;
import static eu.delving.x3ml.AllTests.*;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Yannis Marketakis <marketak@ics.forth.gr>
 * @author Nikos Minadakis <minadakn@ics.forth.gr>
 */
public class TestNamespaces {
    private final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));

    @Test
    public void testMissingNamespace() {
        X3MLEngine engine = engine("/namespace/missingNSmappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/namespace/input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        /* just make sure that it will not fail */
        assertNotNull(mappingResult);
        assertTrue(mappingResult.length>0);
    }
    
    @Test
    public void testMultipleNamespaces(){
        X3MLEngine engine = engine("/namespace/multipleNamespaces-mappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/namespace/multipleNamespaces-input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/namespace/multipleNamespaces-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    } 
    
    @Test
    public void testNamespacesInInfoBlock(){
        X3MLEngine engine = engine("/namespace/namespacesInInfoBlock.x3ml");
        X3MLEngine.Output output = engine.execute(document("/namespace/multipleNamespaces-input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/namespace/namespacesInInfoBlock-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    } 
}