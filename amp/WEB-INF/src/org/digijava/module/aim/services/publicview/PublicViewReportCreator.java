package org.digijava.module.aim.services.publicview;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.digijava.module.aim.services.publicview.conf.Configuration;
import org.digijava.module.aim.services.publicview.conf.ConfigurationUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 3/12/12
 * Time: 5:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class PublicViewReportCreator implements Job {
    private static Logger logger = Logger.getLogger(PublicViewReportCreator.class);
    private static Properties transformationConfig = null;
    private static Configuration conf;

    public static final String TRANSFORM_CONF_XSLT = "xslTransformerPath";
    public static final String TRANSFORM_CONF_HTML = "outputHtmlPath";
    public static final String TRANSFORM_CONF_REPORT_URL = "reportURL";
    
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {



            Configuration conf = ConfigurationUtil.initConfig();

            if (conf != null && conf.getTables() != null) {
                for (Configuration.Table tbl : conf.getTables()) {
                    String xmlScrUrl = conf.getBaseUrl() + tbl.getReportUrl();
                    URL xmlScr = new URL(xmlScrUrl);

                    URLConnection connection = xmlScr.openConnection();
                    connection.addRequestProperty("Referer",xmlScrUrl);
                    connection.connect();

                    InputStream in = connection.getInputStream();

                    TransformerFactory tFactory = TransformerFactory.newInstance();
                    try {
                        Transformer transformer = tFactory.newTransformer(new StreamSource(conf.getWorkFileDir() + tbl.getXslFile()));
                        transformer.transform (new StreamSource (in), new StreamResult (new FileOutputStream(conf.getWorkFileDir() + tbl.getHtmlFile())));
                    } catch (TransformerConfigurationException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (TransformerException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
             }


        } catch (JAXBException e) {
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static synchronized Properties getTransformationConfig() {
        Properties retVal = null;
        String propertyFileResourcePath = "reportcreator.properties";

        InputStream is = PublicViewReportCreator.class.getResourceAsStream(propertyFileResourcePath);
        Properties props = new Properties();
        try {
            props.load(is);
            retVal = props;
        } catch (Exception e) {
            logger.warn("Error parsing property file", e);
        }
        return retVal;
    }

    public static String getTransformationConfigProperty (String property) {
        Configuration conf = null;
        try {
            conf = ConfigurationUtil.getConfiguration();
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String retVal = null;
        if (transformationConfig == null) {
            transformationConfig = getTransformationConfig();
        }

        if (transformationConfig != null) {
            retVal = transformationConfig.getProperty(property);
        }
        return retVal;
    }


}
