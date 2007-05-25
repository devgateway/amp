package org.digijava.module.cms.exception;

import org.digijava.kernel.exception.DgException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CMSException
    extends DgException {

  public CMSException() {
  }

  public CMSException(String message) {
    super(message);
  }

  public CMSException(String message, Throwable cause) {
    super(message, cause);
  }

  public CMSException(Throwable cause) {
    super(cause);
  }

}