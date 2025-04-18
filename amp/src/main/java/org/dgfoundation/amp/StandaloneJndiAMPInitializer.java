/**
 * 
 */
package org.dgfoundation.amp;

import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.naming.*;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

/**
 * @author mihai
 * 
 */
public class StandaloneJndiAMPInitializer {
    private static Logger logger = Logger
            .getLogger(StandaloneJndiAMPInitializer.class);
    private static Context ctx = null;
    private static final String REAL_JNDI = "org.dgfoundation.amp.StandaloneAMPJNDIInitializer.RealJndi";

    private static class AMPInitialContext extends InitialContext {
        private static Logger logger = Logger
        .getLogger(AMPInitialContext.class);

        private String realJNDI;

        public AMPInitialContext(Hashtable<?, ?> environment)
                throws NamingException {
            super(environment);
            realJNDI = (String) environment.get(REAL_JNDI);
        }

        public AMPInitialContext() throws NamingException {
            super();

        }

        @Override
        public Object lookup(String name) throws NamingException {
            try {
                if (Constants.UNIFIED_JNDI_ALIAS.equals(name)) {
                    logger.info("JNDI lookup for " + name
                            + " has been translated into " + realJNDI);
                    return super.lookup(realJNDI);
                }
                return super.lookup(name);
            } catch (CommunicationException e) {
                logger
                        .error("Cannot communicate with the naming server. Is JBoss running?");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Object lookup(Name name) throws NamingException {
            try {
                if (Constants.UNIFIED_JNDI_ALIAS.equals(name.toString())) {
                    logger.info("JNDI lookup for " + name.toString()
                            + " has been translated into " + realJNDI);
                    return super.lookup(realJNDI);
                }
                return super.lookup(name);
            } catch (CommunicationException e) {
                logger
                        .error("Cannot communicate with the naming server. Is JBoss running?");
                e.printStackTrace();
                return null;
            }
        }

    }

    private static class AMPNamingContextFactory implements
            InitialContextFactory {

    
        public Context getInitialContext(Hashtable<?, ?> environment)
                throws NamingException {
            return ctx;
        }

    }

    private static class AMPInitialContextFactoryBuilder implements
            InitialContextFactoryBuilder {
        
        public InitialContextFactory createInitialContextFactory(
                Hashtable<?, ?> environment) throws NamingException {
            return new AMPNamingContextFactory();
        }

    }

    /**
     * Fetches the real JNDI datasource name out of WEB-INF/jboss-web.xml This
     * is used to create an ampDS alias when JBoss is not managing the Naming Context directly
     * 
     * @return the real JNDI datasource name
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private static String parseJbossRealJndiName() throws SAXException,
            IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();

        // Parse the file into a DOM Document (org.w3c.dom)
        File f = new File("WEB-INF/jboss-web.xml");
        Document doc = parser.parse(f);

        NodeList elementsByTagName = doc.getElementsByTagName("jndi-name");
        if (elementsByTagName.getLength() != 1)
            throw new RuntimeException(
                    "None or more than one jndi-name found in jboss-web.xml. This file should have one jndi-name!");
        Node item = elementsByTagName.item(0);
        String nodeValue = item.getFirstChild().getNodeValue();
        logger.info("jboss-web.xml targets " + nodeValue
                + " as the real JNDI datasource");
        return nodeValue;
    }

    /**
     * Initializes the ampDS datasource alias that is available only in the
     * application context. This alias is usually set by JBoss when it parses
     * jboss-web.xml, to the value java:comp/env/ampDS. However when running in
     * standalone java mode, there is no JBoss naming factory service started
     * thus this datasource info is unavailable. This method overcomes this
     * problem by hardcoding an alias inside the naming factory. The real
     * datasource JNDI access is fetched from JBoss through JNP. This means that
     * even when we run this in standalone mode we ought to have a JBoss running
     * on the same machine and with the JNP port open (this is the default JBoss
     * configuration). This alias can be then used by any tool inside AMP
     * (Quartz, Junit, JackRabbit, Hibernate) to refer to the real
     * datasource and thus to access the database.
     * 
     * @throws NamingException
     */
    public static synchronized void initAMPUnifiedJndiAlias()
            throws NamingException {
        Properties env = new Properties();
        env.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.jnp.interfaces.NamingContextFactory");
        env.setProperty(Context.PROVIDER_URL, Constants.JNP_URL);
        env.setProperty(Context.URL_PKG_PREFIXES,
                "org.jboss.naming:org.jnp.interfaces");

        String realJndiName = null;
        try {
            realJndiName = parseJbossRealJndiName();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }

        env.setProperty(REAL_JNDI, realJndiName);

        ctx = new AMPInitialContext(env);

        NamingManager
                .setInitialContextFactoryBuilder(new AMPInitialContextFactoryBuilder());
    }

}
