/*
 *   DigiTestBase.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: June 1, 2004
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import servletunit.struts.MockStrutsTestCase;

public class DigiTestBase
    extends MockStrutsTestCase {
  private static Properties commonProperties = null;
  private Properties testProperties = null;
  public static final String COMMON_CONFIG_PATH
      = "org/digijava/test/conf/digitest.properties";

  public DigiTestBase(String name) {
    super(name);
    init();
  }

  public DigiTestBase(String name, String config) {
    super(name);
    init();
    testProperties = new Properties();
    loadProperties(testProperties, config);
  }

  public String getCommonProperty(String prop) {
    return commonProperties.getProperty(prop);
  }

  public String getTestProperty(String prop) {
    return testProperties.getProperty(prop);
  }

  private void init() {
    if (commonProperties == null) {
      commonProperties = new Properties();
      if (loadProperties(commonProperties, COMMON_CONFIG_PATH)) {
        initPersistance(commonProperties.getProperty("repositoryPath"));
      }
    }
  }

  /**
   * Fills the passed Properties object according specified property file
   * @param prop Properties object to be filled
   * @param path Path of the properties file to get parameters form
   * @return boolean status. True if operatin complited successfuly, otherwise false
   */
  private boolean loadProperties(Properties prop, String path) {
    boolean success = true;
    try {
      InputStream inStream = this.getClass().getClassLoader().
          getResourceAsStream(path);
      if (inStream != null) {
        prop.load(inStream);
      }
      else {
        success = false;
      }
    }
    catch (IOException ex) {
      success = false;
    }
    return success;
  }

  /**
   * Initializes DiGi configuration and Hibernate objects
   * @param repositoryPath - Path of the repository, where modules are located
   */
  private void initPersistance(String repositoryPath) {
    try {
      DigiConfigManager.initialize(repositoryPath);
    }
    catch (DgException ex1) {
      ex1.printStackTrace();
    }
    PersistenceManager.initialize(false);
  }

  /**
   * Fills specified bean according testProperties object and test name
   * @param form bean to be filled
   * @param testName name of bean property set
   * @throws InvocationTargetException
   * @throws java.lang.IllegalArgumentException
   * @throws java.lang.IllegalAccessException
   * @throws java.lang.NoSuchMethodException
   */
  public void populateFormParameters(Object form, String testName) throws
      InvocationTargetException,
      IllegalArgumentException,
      IllegalAccessException,
      NoSuchMethodException {
    Enumeration propKeys = testProperties.propertyNames();
    Set beanParamSet = new HashSet();
    Map beanParams = new HashMap();
    while (propKeys.hasMoreElements()) {
      String key = (String) propKeys.nextElement();
      if (key.startsWith("beanParameter")) {
        String paramTestName = key.substring(14, key.indexOf(".", 14));
        if (testName != null &&
            testName.length() > 0 &&
            testName.equals(paramTestName)) {
          String parameterName = key.substring(key.indexOf(".",
              paramTestName.length() + 14) + 1,
                                               key.length());
          String value = testProperties.getProperty(key);
          beanParams.put(parameterName, value);
        }
      }
    }
    Method[] methods = form.getClass().getDeclaredMethods();
    Set keySet = beanParams.keySet();
    Iterator it = keySet.iterator();

    while (it.hasNext()) {
      String propName = (String) it.next();
      BeanUtils.setProperty(form, propName, beanParams.get(propName));

/*
      for (int methodIndex = 0; methodIndex < methods.length; methodIndex++) {
        Method method = methods[methodIndex];
        String methodName = method.getName();
        if (methodName.startsWith("set")) {
          String beanPropertyName = methodName.substring(4, methodName.length());
          beanPropertyName = methodName.substring(3, 4).toLowerCase() +
              beanPropertyName;
          if (propName.equals(beanPropertyName)) {
            Class[] parTypes = method.getParameterTypes();

//            if (parTypes[0] instanceof Boolean.TYPE) {
//            }
            Object[] params = {
                beanParams.get(propName)};
            method.invoke(form, params);
          }
        }
      } */
    }
  }
}