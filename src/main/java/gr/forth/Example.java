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
package gr.forth;

import eu.delving.x3ml.X3MLEngine;
import static eu.delving.x3ml.X3MLEngine.exception;
import eu.delving.x3ml.X3MLGeneratorPolicy;
import eu.delving.x3ml.engine.Generator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class Example {
    
    public static void main(String[] args) throws FileNotFoundException,  IOException{       
        
        X3MLEngine engine = engine("example/mappings.x3ml");        
        Generator policy = X3MLGeneratorPolicy.load(new FileInputStream(new File("example/generator-policy.xml")), X3MLGeneratorPolicy.createUUIDSource(4));
        X3MLEngine.Output output = engine.execute(document("example/input.xml"), policy);
        
        String[] mappingResult = output.toStringArray();
        output.writeXML(System.out);
    }
    
    private static X3MLEngine engine(String path) throws FileNotFoundException {
        return X3MLEngine.load(new FileInputStream(new File(path)));
    }
    
    private static Element document(String path) {
        try {
            return documentBuilderFactory().newDocumentBuilder().parse(path).getDocumentElement();
        }
        catch (Exception e) {
            throw exception("Unable to parse " + path+"\n"+e.toString());
        }
    }
    
    private static DocumentBuilderFactory documentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory;
    }
}