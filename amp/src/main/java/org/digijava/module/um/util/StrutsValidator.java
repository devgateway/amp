/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.um.util;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.Resources;

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
      ActionMessages errors,
      HttpServletRequest request) {

    logger.debug("RUNNING CUSTOM VALIDATION");

    String value = ValidatorUtils.getValueAsString(bean,
        field.getProperty());
    String sProperty2 = field.getVarValue("secondProperty");
    String value2 = ValidatorUtils.getValueAsString(bean, sProperty2);

    if (!GenericValidator.isBlankOrNull(value)) {
      try {
        if (!value.equals(value2)) {
          errors.add(null,
                     new ActionMessage("error.registration.NoPasswordMatch"));

          return false;
        }
      }
      catch (Exception e) {
        errors.add(null,
                   new ActionMessage("error.registration.NoPasswordMatch"));
        return false;
      }
    }
    return true;
  }

  public static boolean validateURL(Object bean, ValidatorAction va,
                                    Field field, ActionMessages errors,
                                    HttpServletRequest request) {
    String value = (String) ValidatorUtils.getValueAsString(bean,
        field.getProperty());
    //UrlValidator validator = new UrlValidator();
    if (!GenericValidator.isBlankOrNull(value) 
            //&& !validator.isValid(value)
            ) {
      errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
      return false;
    } else {
      return true;
    }
  }
}
