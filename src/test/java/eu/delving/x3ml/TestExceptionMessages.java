package eu.delving.x3ml;

import static eu.delving.x3ml.AllTests.compareNTriples;
import static eu.delving.x3ml.AllTests.document;
import static eu.delving.x3ml.AllTests.engine;
import static eu.delving.x3ml.AllTests.errorFree;
import static eu.delving.x3ml.AllTests.xmlToNTriples;
import eu.delving.x3ml.engine.Generator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class TestExceptionMessages {
    private final Logger log = Logger.getLogger(getClass());
    private final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(1));

    @Test
    public void testEmptyElement() {
        X3MLEngine engine = engine("/exception_messages/mappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/exception_messages/input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/exception_messages/expectedResult.rdf");
        assertTrue(!X3MLEngine.exceptionMessagesList.isEmpty());
    }   
}