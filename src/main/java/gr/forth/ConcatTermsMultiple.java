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

import eu.delving.x3ml.X3MLGeneratorPolicy.CustomGeneratorException;
import eu.delving.x3ml.X3MLGeneratorPolicy.CustomGenerator;
import java.util.Map;
import java.util.TreeMap;

/** The generator is responsible for constructing values (either URIs, or literals)
 *  by concatenating multiple elements (that have the same tag name). More specifically ...TODO
 * 
 * @author Yannis Marketakis &lt;marketak@ics.forth.gr&gt;
 * @author Nikos Minadakis &lt;minadakn@ics.forth.gr&gt;
 */
public class ConcatTermsMultiple implements CustomGenerator{
    private String prefix;
    private String delimiter;
    private Map<String,String> text=new TreeMap<>();

    @Override
    public void setArg(String name, String value) throws CustomGeneratorException {
        if(name.equals(Labels.PREFIX)){
            this.prefix=value;
        }else if(name.startsWith(Labels.DELIMITER)){
            this.delimiter=value;
        }else if(name.startsWith(Labels.TEXT)){
            this.text.put(name, value);
        }else{
            throw new CustomGeneratorException("Unrecognized argument name: "+ name);
        }
    }
    
    @Override
    public String getValue() throws CustomGeneratorException {
        if(text.isEmpty()){
            throw new CustomGeneratorException("Missing text arguments");
        }
        String retValue="";
        for(String key : text.keySet()){
            retValue+=text.get(key)+this.delimiter;
        }
        retValue=retValue.substring(0, retValue.length()-this.delimiter.length());
        retValue=retValue.replaceAll(Labels.MERGING_DELIMITER, this.delimiter);
        if(this.getValueType().equals(Labels.URI)){
            return this.prefix+retValue;
        }else{
            return retValue;
        }
    }

    @Override
    public String getValueType() throws CustomGeneratorException {
        if(this.prefix!=null && this.prefix.startsWith(Labels.HTTP+":")){
            return Labels.URI;
        }else{
            return Labels.LITERAL;
        }
    }

    /** Returns a boolean flag (with value set to false) indicating that this 
     * generator supports merging values from similar elements
     * (elements having the same name). 
     * 
     * @return true*/    
    @Override
    public boolean mergeMultipleValues(){
        return true;
    }
}
