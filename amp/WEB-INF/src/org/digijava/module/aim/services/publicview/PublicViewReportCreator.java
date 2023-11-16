package org.digijava.module.aim.services.publicview;

import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.services.publicview.conf.Configuration;
import org.digijava.module.aim.services.publicview.conf.ConfigurationUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
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


            ServletContext ctx = (ServletContext) jec.getScheduler().getContext().get(Constants.AMP_SERVLET_CONTEXT);
            Configuration conf = ConfigurationUtil.initConfig(ctx);

            if (conf != null && conf.getTables() != null) {
                for (Configuration.Table tbl : conf.getTables()) {
                    if (!tbl.isBudgetExport()) {
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
             }


        } catch (JAXBException e) {
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SchedulerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
