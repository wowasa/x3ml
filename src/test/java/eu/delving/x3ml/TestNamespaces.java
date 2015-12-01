//===========================================================================
//    Copyright 2014 Delving B.V.
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//===========================================================================
package eu.delving.x3ml;

import eu.delving.x3ml.engine.Generator;
import org.apache.log4j.Logger;
import org.junit.Test;
import static eu.delving.x3ml.AllTests.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestNamespaces {
    private final Logger log = Logger.getLogger(getClass());
    private final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(1));

    @Test
    public void testMissingNamespace() {
        X3MLEngine engine = engine("/namespace/missingNSmappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/namespace/input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        /* just make sure that it will not fail */
        assertNotNull(mappingResult);
        assertTrue(mappingResult.length>0);
    }
}