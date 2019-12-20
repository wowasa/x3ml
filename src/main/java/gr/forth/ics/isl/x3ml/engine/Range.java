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

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import org.w3c.dom.Node;
import static gr.forth.ics.isl.x3ml.engine.X3ML.RangeElement;

/**
 * The range entity handled here. Resolution delegated.
 *
 * @author Gerald de Jong &lt;gerald@delving.eu&gt;
 * @author Nikos Minadakis &lt;minadakn@ics.forth.gr&gt;
 * @author Yannis Marketakis &lt;marketak@ics.forth.gr&gt;
 */
public class Range extends GeneratorContext {

    public final Path path;
    public final RangeElement range;
    public EntityResolver rangeResolver;

    public Range(Root.Context context, Path path, RangeElement range, Node node, int index) {
        super(context, path, node, index);
        this.path = path;
        this.range = range;
    }

    public boolean resolve() {
        if (conditionFails(range.target_node.condition, this)) {
            return false;
        }
        rangeResolver = new EntityResolver(context.output(), range.target_node.entityElement, this);
        if(path.property == null){  //this happens only when using the MERGE facility */
            return rangeResolver.resolve(0,0,false,Derivation.Range,"","",Domain.domainNode);
        }else{
            return rangeResolver.resolve(0,0,false,Derivation.Range,"","",null);
        }
    }

    public void link(String linkNamedgraph, String mappingNamedgraph) {
        path.link();
        if (rangeResolver.hasResources()) {
            rangeResolver.link(Derivation.Range);
            for (Resource lastResource : path.lastResources) {
                XPathInput.domainURIForNamedgraps=lastResource.getURI();
                for (Resource resolvedResource : rangeResolver.resources) {
                    if(path.lastProperty==null){    //this happens only when using the MERGE facility
                        break;
                    }else{
                        lastResource.addProperty(path.lastProperty, resolvedResource);
                        if(linkNamedgraph!=null){
                            X3ML.RootElement.hasNamedGraphs=true;
                            if(linkNamedgraph.isEmpty()){
                                X3ML.LinkElement.namedGraphProduced=lastResource.getURI();
                            }else if(linkNamedgraph.startsWith("http://") || linkNamedgraph.startsWith("https://") || linkNamedgraph.startsWith("uuid:") || linkNamedgraph.startsWith("urn:")){
                                X3ML.LinkElement.namedGraphProduced=linkNamedgraph+lastResource.getURI().replace("http://","_").replace("https://","_").replace("uuid:", "_").replace("urn:","_");
                            }else{
                                X3ML.LinkElement.namedGraphProduced="http://"+linkNamedgraph+lastResource.getURI().replace("http://","_").replace("https://","_").replace("uuid:", "_").replace("urn:","_");
                            }

/*                            ModelOutput.quadGraph.add(new ResourceImpl(X3ML.LinkElement.namedGraphProduced).asNode(),
                                    lastResource.asNode(), path.lastProperty.asNode(), resolvedResource.asNode());
                            ModelOutput.quadGraph.add(new ResourceImpl(X3ML.LinkElement.namedGraphProduced).asNode(), 
                                    resolvedResource.asNode(), new ResourceImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type").asNode(), 
    //                                new ResourceImpl(rangeResolver.entityElement.typeElements.get(0).namespaceUri+rangeResolver.entityElement.typeElements.get(0).getLocalName()).asNode());
                                    new ResourceImpl(EntityResolver.modelOutput.getNamespace(rangeResolver.entityElement.typeElements.get(0))).asNode());
                            rangeResolver.link(Derivation.Range);*/

                        }
/*                        if(mappingNamedgraph!=null){
                            X3ML.RootElement.hasNamedGraphs=true;
                            ModelOutput.quadGraph.add(new ResourceImpl(X3ML.Mapping.namedGraphProduced).asNode(), 
                                    lastResource.asNode(), path.lastProperty.asNode(), resolvedResource.asNode());
                            ModelOutput.quadGraph.add(new ResourceImpl(X3ML.Mapping.namedGraphProduced).asNode(), 
                                    resolvedResource.asNode(), new ResourceImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type").asNode(), 

    //                                new ResourceImpl(rangeResolver.entityElement.typeElements.get(0).namespaceUri+rangeResolver.entityElement.typeElements.get(0).getLocalName()).asNode());
                                    new ResourceImpl(EntityResolver.modelOutput.getNamespace(rangeResolver.entityElement.typeElements.get(0))).asNode());
                        }*/
                    }
                }
            }
        }else if (rangeResolver.hasLiteral()) {
            for (Resource lastResource : path.lastResources) {
                if(path.lastProperty==null){    //this happens only when using the MERGE facility
                        break;
                }else{
                    lastResource.addLiteral(path.lastProperty, rangeResolver.literal);
                    if(linkNamedgraph!=null){
/*                        String linkNamedGraphMerged=null;
                        if(linkNamedgraph.isEmpty()){
                            linkNamedGraphMerged=lastResource.getURI();
                        }else if(linkNamedgraph.startsWith("http://") || linkNamedgraph.startsWith("https://") || linkNamedgraph.startsWith("uuid:") || linkNamedgraph.startsWith("urn:")){
                            linkNamedGraphMerged=linkNamedgraph+lastResource.getURI().replace("http://","_").replace("https://","_").replace("uuid:", "_").replace("urn:","_");
                        }else{
                            linkNamedGraphMerged="http://"+linkNamedgraph+lastResource.getURI().replace("http://","_").replace("https://","_").replace("uuid:", "_").replace("urn:","_");
                        }
                            X3ML.RootElement.hasNamedGraphs=true;
                            ModelOutput.quadGraph.add(new ResourceImpl(linkNamedGraphMerged).asNode(), 
                                    lastResource.asNode(), path.lastProperty.asNode(), rangeResolver.literal.asNode());*/
                    }
                    if(mappingNamedgraph!=null){
/*                         X3ML.RootElement.hasNamedGraphs=true;
                         ModelOutput.quadGraph.add(new ResourceImpl(X3ML.Mapping.namedGraphProduced).asNode(), 
                                    lastResource.asNode(), path.lastProperty.asNode(), rangeResolver.literal.asNode());*/
                    }
                }   
            }
        }
    }
}
