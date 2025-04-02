package org.digijava.module.aim.services.publicview.conf;


import org.xml.sax.InputSource;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: flyer
 * Date: 9/12/12
 * Time: 1:07 PM
 */


public class ConfigurationUtil {
    private static Configuration configuration = null;
    private static final String configFilePath = "/org/digijava/module/aim/services/publicview/conf.xml";

    public static Configuration getConfiguration (ServletContext ctx) throws JAXBException, IOException {
        return (configuration != null ? configuration : initConfig(ctx));
    }

    public static Configuration initConfig (InputSource inputSource) throws JAXBException, FileNotFoundException {
        JAXBContext jc = JAXBContext.newInstance(Configuration.class);
        Unmarshaller um = jc.createUnmarshaller();
        Configuration retVal = (Configuration)um.unmarshal(inputSource);
        return retVal;
    }

    public static Configuration initConfig (ServletContext ctx) throws JAXBException, IOException {
        //Get config name
        String digijavaPropertiesPath  = ctx.getRealPath("/deployConfigs/selected.properties");

        Properties dgProps = new Properties();
        dgProps.load(new FileReader(digijavaPropertiesPath));
        String serverName = dgProps.getProperty("serverName");
        String configFilePath = new StringBuilder("/deployConfigs/").
                append(serverName).append("/ReportTransformer/conf.xml").toString();
        
        InputStream inStr = new FileInputStream(ctx.getRealPath(configFilePath));
        InputSource inSrc = new InputSource(inStr);
        Configuration conf = initConfig(inSrc);
        
        String workFileDir = new StringBuilder("/deployConfigs/").
                        append(serverName).append("/ReportTransformer/datadir/").toString();

        conf.setWorkFileDir(ctx.getRealPath(workFileDir) + "\\");

        configuration = conf;

        return conf;
    }

    public static String getHTMLPathForConfig(ServletContext ctx, String configName) throws JAXBException, IOException {
        String retVal = null;
        for (Configuration.Table table : getConfiguration(ctx).getTables()) {
            if (table.getName().trim().equals(configName)) {
                retVal = (new StringBuilder(getConfiguration(ctx).getWorkFileDir()).append(table.getHtmlFile())).toString();
                break;
            }
        }
        return retVal;
    }


}
