# Change Log

---
## 05 Feb 2016: V 1.7.0 (by FORTH-ICS)
* Fixed bug with the creation of Literal instances (i.e. text nodes), if the same XPATH has been used for creating another instance (#33)
* Fixed bug with the creation of xsd:DateTime instances (i.e. text nodes) (issue #37)
* Fixed issue with the type of custom generators (issue #36)
* Fixed issue with the exported values from a join operation in the association table (issue #38)
* Fixed issue with the generation of rdfs labels when the rdfs namespace is not declared  (issue #29)
* Fixed issue with misleading messages with generators (confuses instance with label generators) (issue #42)
* Fixed issue with NPE thrown when the arguments of a custom instance generator are invalid (issue #10)
* Added a new LabelGenerator (for range of dates) and fixed some bugs with the date generators
* NEW functionality for skipping entire mappings (issue #31) 
* NEW functionality for skipping entire links (issue #39) 
* NEW functionality for parsing multiple XML input files in a single run (issue #27)
* NEW functionality - introduced global_variable for supporting global scope for variables (issue #34)


## 11 Jan 2016: V 1.6.2 (by FORTH-ICS)

* Support for multiple instatiations (fixed issue #1)
* fixed bug with empty elements (issue #4, #21)
* fixed bug with namespaces not declared in the namespace section (issue #5)
* fixed bug with non-informative messages when a namespace is not declared in the namespaces section (issue #7)
* fixed bug with non-informative messages when the label generators contain non-valid attributes (issue #9) or empty expressions (issue #11)
* fixed bug with the creation of entities from additional and intermediate nodes (issue #14, #15, #16, #18)
* fixed bugs with engine failure when join operations (both simple and double) refered to empty or non-existing elements (issues #23, #24, #25)
* fixed issue with the creation of labels when they are found only in the domain (issue #3)
* support exporting the contents of the association table during runtime (issue #30)

## 19 Nov 2015: V 1.6.1 (by FORTH-ICS)

* Added an implementation of the association table
* Added junit tests (for testing nested mappings, multiple instantiations, preflabels)
* Removed System.out messages from junit tests
* Merged previous branches.

## 14 Oct 2015: V 1.6 (by FORTH-ICS)

* Exceptions are also added in a static variable so that they can be exploited after the execution (i.e. by the 3M Editor)
* Support for generation of skos:prefLabel labels
* removed a lot of debug messages (completely useless)

## 07 Aug 2015: V 1.5 (by FORTH-ICS)

* Elimination of relation2 elements. Multiple joins are supported by simply using sequences of relation elements.
* fixed bug with empty elements in the input.
* Added support for AuthorityPrirefCounter and generators based on XPATH position
* fixed bug to support fully qualified URIs

## 27 Feb 2014: V 1.4 (by FORTH-ICS)

* Added a new Relation (named relation2) to support double joins

## 23 Oct 2014: V 1.3 (by Delving BV)

* variables moved back to <entity> but passed into instance generator
* incorporated changes regarding getValueType in CustomGenerator
* integrated URIorUUID custom generator
* fixed bug involving creating new path instances when doing a join

## 2 Aug 2014: V 1.2.2 (by Delving BV)

* variables moved to <instance-generator>

## 21 July 2014: V 1.2.1 (by Delving BV)

* fixed bug with variables - labels and additionals were ignored if not in the first var usage

## 6 July 2014: V 1.2 (by Delving BV)

* Entity element now has *instance_info* attribute 
* *type* attribute added to instance_generator arg
* *ArgValues* interface moved inside *Generator* interface
* Introduced usage of *createTypedLiteral* 
* For specialized instance generation code, introduced *CustomGenerator* interface
* Added *custom* element to GeneratorPolicy, with *generatorClass* which implements *CustomGenerator*
* Better generation of test UUIDs, command line can specify size.
* *DomainContext(path)* syntax added to XPath for non-hierarchical source
* Removed *namespaces* from generator policy, inheriting from X3ML namespaces instead

## 29 April 2014: V 1.1 (by Delving BV)

* Major refactor for code legibility, licensing, and javadoc
* The *property* no longer contains *class* but instead itself holds the qualified name
* The tag *property* has been replaced with *relationship*.
* The *class* element is now *type*.
* The *value_generator* is now *instance_generator*
* Multiple *type* possible within entity, variables hold lists now
* Generator argument types and global *source_type* now lowercase
* Argument type *position* introduced, giving the index of the node in its node list
* Language handling
	* An XPath argument tries to find language from xml:lang in source
	* Generators can override using *language* attribute, language="" for none at all
* Command line formats now "application/rdf+xml", "text/turtle" and "application/n-triples"

## 27 Mar 2014: Initial release v1.0 (by Delving BV)

* XSD Schema validation of X3ML integrated
* Command line has -validation option
* Multiple <class> within <entity> needs some work
* Approach to define generator policy needs to be discussed
