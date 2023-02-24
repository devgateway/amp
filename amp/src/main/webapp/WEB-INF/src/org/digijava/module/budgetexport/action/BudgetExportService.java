package org.digijava.module.budgetexport.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.services.publicview.conf.Configuration;
import org.digijava.module.aim.services.publicview.conf.ConfigurationUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: flyer
 * Date: 10/24/12
 * Time: 2:03 PM
 */
public class BudgetExportService extends Action {
    private static Logger logger    = Logger.getLogger(BudgetExportService.class);
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
        {
            Configuration conf = ConfigurationUtil.initConfig(request.getSession().getServletContext());
            String configName = request.getParameter("configName");
            response.setContentType("text/xml");
            
            if (conf != null && conf.getTables() != null) {
                for (Configuration.Table tbl : conf.getTables()) {
                    if (tbl.getName().equalsIgnoreCase(configName)) {
                        String xmlScrUrl = conf.getBaseUrl() + tbl.getReportUrl();
                        URL xmlScr = new URL(xmlScrUrl);

                        URLConnection connection = xmlScr.openConnection();
                        connection.addRequestProperty("Referer",xmlScrUrl);
                        connection.connect();

                        InputStream in = connection.getInputStream();
                        ServletOutputStream out = response.getOutputStream();
                        TransformerFactory tFactory = TransformerFactory.newInstance();
                        try {
                            Transformer transformer = tFactory.newTransformer(new StreamSource(conf.getWorkFileDir() + tbl.getXslFile()));
                            transformer.transform (new StreamSource (in), new StreamResult(out));
                        } catch (TransformerConfigurationException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (TransformerException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } finally {
                            in.close();
                            out.close();
                        }
                        break;
                    }
                }
             }
            return null;
        }

}
