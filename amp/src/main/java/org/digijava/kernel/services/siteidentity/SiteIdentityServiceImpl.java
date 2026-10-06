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

package org.digijava.kernel.services.siteidentity;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.kernel.util.DigesterFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;

public class SiteIdentityServiceImpl
    extends AbstractServiceImpl implements SiteIdentityService {

    private static Logger logger = Logger.getLogger(SiteIdentityServiceImpl.class);

    private IdentityConfig identityConfig = null;
    private String config;
    private Digester digester;

    protected void processInitEvent(ServiceContext serviceContext) throws
        ServiceException {
        try {
            digester = getDigester();
        }
        catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    protected void processStartEvent() throws ServiceException {

        StringReader sr = new StringReader(config);
        try {
            this.identityConfig = (IdentityConfig) digester.parse(sr);
        }
        catch (Exception ex) {
            throw new ServiceException(
                "Unable to parse translation dropdown config[" +
                config + "]", ex);
        }
        sr.close();

    }

    public String getSiteId(String service, Site site) {
        if (identityConfig == null) {
            logger.warn(
                "Configuration for SiteIdentityServiceImpl is not initialized properly");
            return site.getSiteId();
        }
        ServiceConfig servConfig = (ServiceConfig) identityConfig.getServices().
            get(service);
        if (servConfig == null) {
            return site.getSiteId();
        }

        String result = (String) servConfig.getSiteConfigs().get(site.getSiteId());
        if (result == null || result.trim().length() == 0) {
            result = servConfig.getDefaultId();
            if (result == null || result.trim().length() == 0) {
                result = site.getSiteId();
            }

        }
        return result;
    }

    private static Digester getDigester() throws ParserConfigurationException,
        SAXException {
        Digester digester = DigesterFactory.newDigester();

        digester.addObjectCreate("config", IdentityConfig.class);
        digester.addObjectCreate("config/service", ServiceConfig.class);
        digester.addSetProperties("config/service", "id", "serviceId");
        digester.addSetProperties("config/service", "default", "defaultId");
        digester.addSetNext("config/service", "addService");

        digester.addCallMethod("config/service/site", "addSite", 2);
        digester.addCallParam("config/service/site", 0, "id");
        digester.addCallParam("config/service/site", 1);

        return digester;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

}
