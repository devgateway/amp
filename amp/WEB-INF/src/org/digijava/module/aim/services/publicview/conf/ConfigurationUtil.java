package org.digijava.module.aim.services.publicview.conf;


import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

/**
 * User: flyer
 * Date: 9/12/12
 * Time: 1:07 PM
 */


public class ConfigurationUtil {
    private static Configuration configuration = null;
    private static final String configFilePath = "/org/digijava/module/aim/services/publicview/conf.xml";

    public static Configuration getConfiguration () throws JAXBException, FileNotFoundException {
        return (configuration != null ? configuration : initConfig());
    }

    public static Configuration initConfig (InputSource inputSource) throws JAXBException, FileNotFoundException {
        JAXBContext jc = JAXBContext.newInstance(Configuration.class);
        Unmarshaller um = jc.createUnmarshaller();
        Configuration retVal = (Configuration)um.unmarshal(inputSource);
        return retVal;
    }

    public static Configuration initConfig () throws JAXBException, FileNotFoundException {
        InputStream inStr = ConfigurationUtil.class.
                            getResourceAsStream(configFilePath);
        InputSource inSrc = new InputSource(inStr);
        return initConfig(inSrc);
    }

    public static String getHTMLPathForConfig(String configName) throws JAXBException, FileNotFoundException {
        String retVal = null;
        for (Configuration.Table table : getConfiguration().getTables()) {
            if (table.getName().trim().equals(configName)) {
                retVal = (new StringBuilder(getConfiguration().getWorkFileDir()).append(table.getHtmlFile())).toString();
                break;
            }
        }
        return retVal;
    }


}
