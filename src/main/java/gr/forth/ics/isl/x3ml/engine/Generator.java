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
package gr.forth.ics.isl.x3ml.engine;

import static gr.forth.ics.isl.x3ml.engine.X3ML.ArgValue;
import static gr.forth.ics.isl.x3ml.engine.X3ML.GeneratedValue;
import gr.forth.ics.isl.x3ml.engine.X3ML.GeneratorElement;
import static gr.forth.ics.isl.x3ml.engine.X3ML.SourceType;

/**
 * This is what a generator looks like to the internal code.
 *
 * @author Gerald de Jong &lt;gerald@delving.eu&gt;
 * @author Nikos Minadakis &lt;minadakn@ics.forth.gr&gt;
 * @author Yannis Marketakis &lt;marketak@ics.forth.gr&gt;
 */
public interface Generator {

    interface UUIDSource {

        String generateUUID();
    }

    void setDefaultArgType(SourceType sourceType);

    void setLanguageFromMapping(String language);

    void setNamespace(String prefix, String uri);

    String getLanguageFromMapping();

    public interface ArgValues {

        ArgValue getArgValue(String name, SourceType sourceType, boolean mergeMultipleValues);
    }

    GeneratedValue generate(GeneratorElement generator, ArgValues arguments);
}
