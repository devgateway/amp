package org.digijava.module.ampharvester.exception;

import org.digijava.kernel.exception.DgException;

public class AmpHarvesterException extends DgException {
  public static final String UNKNOWN = "ampharvester.exception.unknown";
  public static final String UNKNOWN_KEY = "ampharvester.exception.unknown.key";
  public static final String UNKNOWN_STATUS = "ampharvester.exception.unknown.status";
  public static final String UNKNOWN_IMPLEMENTATION_LEVEL = "ampharvester.exception.unknown.implementationLevel";
  public static final String UNKNOWN_SECTOR = "ampharvester.exception.unknown.sector";
  public static final String UNKNOWN_PROGRAM = "ampharvester.exception.unknown.program";
  public static final String UNKNOWN_ORGANIZATION = "ampharvester.exception.unknown.organization";
  public static final String UNKNOWN_ASSIST = "ampharvester.exception.unknown.assist";
  public static final String UNKNOWN_CURRENCY = "ampharvester.exception.unknown.currency";
  public static final String UNKNOWN_PERSPECTIVE = "ampharvester.exception.unknown.perspective";
  public static final String UNKNOWN_COMPONENT = "ampharvester.exception.unknown.component";
  public static final String UNKNOWN_LOCATION = "ampharvester.exception.unknown.location";
  public static final String UNKNOWN_REGION = "ampharvester.exception.unknown.region";
  public static final String UNKNOWN_XML_VALIDATION = "ampharvester.exception.unknown.xml.validation";

  private String errorKey = null;

  public AmpHarvesterException() {
//    this(errorMessage);
  }

  public AmpHarvesterException(String message) {
    super(message);
  }

  public AmpHarvesterException(String message, String errorKey) {
    super(message);
    this.errorKey = errorKey;
  }

  public AmpHarvesterException(String message, Throwable cause) {
    super(message, cause);
  }

  public AmpHarvesterException(Throwable cause) {
    super(cause);
  }

  public String getErrorKey() {
    return errorKey;
  }

  public String getErrorMessage(){
    StringBuffer retValue = new StringBuffer();

    retValue.append(getMessage());
    retValue.append(" [");
    retValue.append(errorKey);
    retValue.append("]");
    retValue.append("\n");

    return retValue.toString();
  }
}
