package org.digijava.kernel.viewmanager.reposimpl;

/*
*   DefaultViewConfigImpl.java
*   @Author Mikheil Kapanadze mikheil@digijava.org
*   Created: Apr 13, 2004
*   CVS-ID: $Id: TemplateViewConfigImpl.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

import java.util.Map;
import javax.servlet.ServletContext;

import org.digijava.kernel.viewmanager.ViewConfigException;

public class TemplateViewConfigImpl extends ViewConfigUtil {
    private String template;

    public TemplateViewConfigImpl(String template, ServletContext servletContext) throws ViewConfigException {
        super(servletContext);
        this.template = template;
        reload();
    }

    public void reload() throws ViewConfigException {
        super.reload();
        this.siteConfig = createConfiguration(template, true);
    }

    public String getMainLayoutPath(String layoutName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getMainLayoutPath() not yet implemented.");
    }
    public String getFilePath(String moduleName, String moduleInstance, String pageName, String teaserName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getFilePath() not yet implemented.");
    }
    public Map getMasterContextAttributes(String layoutName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getMasterContextAttributes() not yet implemented.");
    }
    public String getPagePath(String pageName, boolean appendExtension) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getPagePath() not yet implemented.");
    }
    public String getTeaserAction(String moduleName, String moduleInstance, String teaserName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getTeaserAction() not yet implemented.");
    }
    public String getFilePath(String fileName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getFilePath() not yet implemented.");
    }

    public String getTemplateName() {
        return null;
    }
}
