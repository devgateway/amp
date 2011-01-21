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

package org.digijava.kernel.robots;

import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.digijava.kernel.robots.config.RobotsConfig;
import org.digijava.kernel.robots.config.RobotsConfigs;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceException;
import org.digijava.kernel.util.DigesterFactory;
import org.xml.sax.SAXException;

public class RobotsTxtService
    extends AbstractServiceImpl {
    private static Logger logger = Logger.getLogger(RobotsTxtService.class);

    private String config;
    private Digester digester;
    private RobotsConfigs robotsConfigs;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setRobotsConfigs(RobotsConfigs robotsConfigs) {
        this.robotsConfigs = robotsConfigs;
    }

    protected void processCreateEvent() throws ServiceException {
        logger.debug("Creating RobotsTxtService");
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
            this.robotsConfigs = (RobotsConfigs) digester.parse(sr);
        }
        catch (Exception ex) {
            throw new ServiceException(
                "Unable to parse translation dropdown config[" +
                config + "]", ex);
        }
        sr.close();

    }

    protected void processDestroyEvent() throws ServiceException {
        logger.debug("Destroying RobotsTxtService");
    }

    private Digester getDigester() throws ParserConfigurationException,
        SAXException {
        Digester digester = DigesterFactory.newDigester();

        digester.addObjectCreate("robots-configs", RobotsConfigs.class);
        digester.addObjectCreate("robots-configs/robots-config", RobotsConfig.class);
        digester.addSetNext("robots-configs/robots-config", "addRobotsConfig");

        digester.addBeanPropertySetter("robots-configs/robots-config/content",
                                       "content");
        digester.addBeanPropertySetter("robots-configs/robots-config/path",
                                       "path");
        digester.addCallMethod("robots-configs/robots-config/domain",
                               "addDomain", 0);
        digester.addSetProperties("robots-configs/robots-config", "default", "defaultConf");

        return digester;
    }

    public RobotsConfigs getRobotsConfigs() {
        return robotsConfigs;
    }
}
