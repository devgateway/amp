/*
*   InstanceTei.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: InstanceTei.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

package org.digijava.kernel.taglib;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author
 * @version 1.0
 */

public class InstanceTei
    extends TagExtraInfo {

  /**
   * Return information about the scripting variables to be created.
   */
  public VariableInfo[] getVariableInfo(TagData data) {

    String type = "java.lang.Object";

    return new VariableInfo[] {
        new VariableInfo(InstanceTag.PAGE_MODULE_INSTANCE,
                         type,
                         true,
                         VariableInfo.AT_END)

    };

  }

}