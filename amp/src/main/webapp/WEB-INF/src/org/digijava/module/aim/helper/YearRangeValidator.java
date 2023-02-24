package org.digijava.module.aim.helper;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


public class YearRangeValidator
    implements Serializable {

  private static Logger logger = Logger.getLogger(YearRangeValidator.class);

  public static boolean validateYearRange(Object bean,
                                          ValidatorAction va,
                                          Field field,
                                          ActionMessages errors,
                                          HttpServletRequest request) {
    if ( logger.isDebugEnabled() )
        logger.debug("RUNNING YEAR RANGE VALIDATION");
    
    String value = ValidatorUtils.getValueAsString(bean,
                                                  field.getProperty());
    int v1 = Integer.parseInt(value);                                               
    String sProperty2 = field.getVarValue("secondProperty");
    String value2 = ValidatorUtils.getValueAsString(bean, sProperty2);
    int v2 = Integer.parseInt(value2);
    
     try {
        if ( v1 > v2 ) {
          errors.add(null,
                     new ActionMessage("error.aim.invalidYearRange"));
          return false;
        }
     }
     catch (Exception e) {
        errors.add(null,
                   new ActionMessage("error.aim.invalidYearRange"));
        return false;
      }
      
    if ( logger.isDebugEnabled() )  
        logger.debug("YEAR RANGE VALIDATION RETURNING TRUE");
    return true;
  }
}
