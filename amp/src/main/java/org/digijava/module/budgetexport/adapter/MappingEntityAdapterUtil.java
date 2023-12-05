package org.digijava.module.budgetexport.adapter;


import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 2/4/12
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MappingEntityAdapterUtil {
    private static Logger logger    = Logger.getLogger(MappingEntityAdapterUtil.class);
    private static Properties adapterProperties = null;

    private static String getEntityAdapterClassName (String extractorView) {
        String retVal = null;
        if (adapterProperties == null) {
            adapterProperties = getPropertyFileMapping();
        }

        if (adapterProperties != null) {
            retVal = adapterProperties.getProperty(extractorView);
        }

        return retVal;
    }

    public static Set getAvailEntityAdapterKeys () {
        Set retVal = null;
        if (adapterProperties == null) {
            adapterProperties = getPropertyFileMapping();
        }

        if (adapterProperties != null) {
            retVal = adapterProperties.keySet();
        }

        return retVal;
    }



    public static MappingEntityAdapter getEntityAdapter (String extractorView) {
        MappingEntityAdapter adapter = null;
        String className = getEntityAdapterClassName (extractorView);
        try {
            Class classDefinition = Class.forName(className);
            adapter = (MappingEntityAdapter)classDefinition.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return adapter;
    }
    
    private static synchronized Properties getPropertyFileMapping() {
        Properties retVal = null;
        String propertyFileResourcePath = "adapter.properties";

        InputStream is = MappingEntityAdapterUtil.class.getResourceAsStream(propertyFileResourcePath);
        Properties props = new Properties();
        try {
            props.load(is);
            retVal = props;
        } catch (Exception e) {
            logger.warn("Error parsing entity mapping property file", e);
        }
        return retVal;
    }


    
    public static String generateIdWhereClause(List<Long> ids) {
        StringBuilder retVal = new StringBuilder();
        for (int idx = 0; idx < ids.size(); idx ++) {
            retVal.append(ids.get(idx));
            if (idx < ids.size() - 1) {
                retVal.append(",");
            }

        }

        return retVal.toString();
    }

}
