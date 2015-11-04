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