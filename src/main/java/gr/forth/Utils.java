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

import static eu.delving.x3ml.X3MLEngine.exception;
import eu.delving.x3ml.engine.X3ML;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import javax.xml.parsers.ParserConfigurationException;
import org.atteo.xmlcombiner.XmlCombiner;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class Utils {
    public static String produceLabelGeneratorMissingArgumentError(X3ML.GeneratorElement generator, String expectedValue){
        return new StringBuilder().append("LabelGenerator Error: ")
                                  .append("The attribute ")
                                  .append("\"")
                                  .append(expectedValue)
                                  .append("\"")
                                  .append(" is missing from the generator. ")
                                  .append("[Mapping #: ")
                                  .append(X3ML.RootElement.mappingCounter)
                                  .append(", Link #: ")
                                  .append(X3ML.RootElement.linkCounter)
                                  .append("]. ")
                                  .append(generator).toString();
    }
    
    public static String produceLabelGeneratorEmptyArgumentError(X3ML.GeneratorElement generator){
        return new StringBuilder().append("LabelGenerator Error: ") 
                                  .append("The label generator with name ")
                                  .append("\"")
                                  .append(generator.name)
                                  .append("\"")
                                  .append(" does not containg any value. ")
                                  .append("[Mapping #: ")
                                  .append(X3ML.RootElement.mappingCounter)
                                  .append(", Link #: ")
                                  .append(X3ML.RootElement.linkCounter)
                                  .append("]. ")
                                  .append(generator).toString();
    }
    
    public static void printErrorMessages(String ... messages){
        for(String msg : messages){
            System.out.println(msg);
        }
    }
    
    public static Element parseMultipleXMLFiles(Collection<InputStream> xmlFileInputStreams){
        try{
            XmlCombiner combiner = new XmlCombiner();
            for(InputStream xmlStream : xmlFileInputStreams){
                combiner.combine(xmlStream);
            }
            return combiner.buildDocument().getDocumentElement();
        }catch(ParserConfigurationException ex){
            throw exception("an error occurred while initializing xml combiner (for multiple files)",ex);
        }catch(IOException ex){
            throw exception("an error occured with the given input files", ex);
        }catch(SAXException ex){
            throw exception("an error occured while parsing XML input files",ex);
        }
    }
}