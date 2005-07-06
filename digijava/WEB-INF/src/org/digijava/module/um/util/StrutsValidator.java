/*
*   StrutsValidator.java
*   @Author Maka Kharalashvili maka@digijava.org
*   Created: Apr 21, 2004
* 	CVS-ID: $Id: StrutsValidator.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

package org.digijava.module.um.util;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorUtil;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.log4j.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class StrutsValidator
    implements Serializable {

  private static Logger logger = Logger.getLogger(StrutsValidator.class);

  public static boolean validateIdentical(
      Object bean,
      ValidatorAction va,
      Field field,
      ActionErrors errors,
      HttpServletRequest request) {

    logger.debug("RUNNING CUSTOM VALIDATION");

    String value = ValidatorUtil.getValueAsString(bean,
                                                  field.getProperty());
    String sProperty2 = field.getVarValue("secondProperty");
    String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);

    if (!GenericValidator.isBlankOrNull(value)) {
      try {
        if (!value.equals(value2)) {
          errors.add(null,
                     new ActionError("error.registration.NoPasswordMatch"));

          return false;
        }
      }
      catch (Exception e) {
        errors.add(null,
                   new ActionError("error.registration.NoPasswordMatch"));
        return false;
      }
    }
    return true;
  }

}