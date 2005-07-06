/*
*   SiteDomainMap.java
*   @Author Philipp Anokhin
*   Created:
*   CVS-ID: $Id: SiteDomainMap.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.request;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SiteDomainMap
{
  private Long id;
  private String siteId;
  private String domain;
  public SiteDomainMap() {
  }
  public Long getId()
  {
    return id;
  }
  public String getSiteId()
  {
    return siteId;
  }
  public String getDomain()
  {
    return domain;
  }

  public void setId(Long lId)
  {
     id = lId;
  }
  public void setSiteId(String lSiteId)
  {
     siteId = lSiteId;
  }
  public void setDomain(String lDomain)
  {
     domain = lDomain;
  }

}
