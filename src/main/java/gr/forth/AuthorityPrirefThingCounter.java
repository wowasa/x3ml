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
package gr.forth;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static eu.delving.x3ml.X3MLGeneratorPolicy.CustomGenerator;
import static eu.delving.x3ml.X3MLGeneratorPolicy.CustomGeneratorException;
import java.util.Date;

/**
 * an excample date interpreter
 */
public class AuthorityPrirefThingCounter implements CustomGenerator {

    private String authority;
    private String priref;
    private static String prirefOld;
    

    private String thing;
    private static String thingOld;

    private static int counterInt = 0;

    @Override
    public void setArg(String name, String value) throws CustomGeneratorException {

        if ("authority".equals(name)) {

            authority = value;
        } else if ("priref".equals(name)) {
            priref = value;
        } else if ("thing".equals(name)) {
            thing = value;
        } else {
            throw new CustomGeneratorException("Unrecognized argument name: " + name);
        }
    }

    @Override
    public String getValue() throws CustomGeneratorException {
        if (authority == null) {
            throw new CustomGeneratorException("Missing text argument");
        }
        if (priref == null) {
            throw new CustomGeneratorException("Missing bounds argument");
        }

        if (thing == null) {
            throw new CustomGeneratorException("Missing text argument");
        }

        return getCounterObject(authority, priref, thing);
    }

    @Override
    public String getValueType() throws CustomGeneratorException {
        return  "URI";
    }

    private String getCounterObject(String authority, String priref, String thing) {
        
        String counterObject = "";

        if (counterInt == 0) {
            setOldValues();
        }

        if (prirefOld.equals(priref) && thingOld.equals(thing)){
                counterInt++;
        } 
        
        else {
            counterInt = 1;
            setOldValues();
        }
        
        counterObject = authority + "/" + priref + "/" + thing + "/" + counterInt;

        return counterObject;

    }

    private void setOldValues() {
        prirefOld = priref;
        thingOld = thing;
    }

}
