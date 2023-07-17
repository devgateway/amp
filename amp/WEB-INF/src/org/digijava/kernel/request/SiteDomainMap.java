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

package org.digijava.kernel.request;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import javax.persistence.*;

@Entity
@Table(name = "SiteDomainMap")

public class SiteDomainMap
{
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SiteDomainMap_seq")
  @SequenceGenerator(name = "SiteDomainMap_seq", sequenceName = "SiteDomainMap_seq", allocationSize = 1)  @Column(name = "id")
  private Long id;

  @Column(name = "domain")
  private String domain;

  @Column(name = "site")
  private String siteId;

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
