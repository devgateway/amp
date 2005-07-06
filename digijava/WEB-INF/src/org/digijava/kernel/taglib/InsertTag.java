/*
 *   InsertTag.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: InsertTag.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.siteconfig.PutItem;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.kernel.taglib.util.RefLinkManager;
import org.apache.struts.taglib.tiles.PutTag;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.kernel.util.RequestUtils;

/**
 * Digi insert tag
 *
 * @author Lasha Dolidze
 * @version 1.0
 */
public class InsertTag
    extends org.apache.struts.taglib.tiles.InsertTag {

    private static Logger logger = I18NHelper.getKernelLogger(InsertTag.class);

    private String layout;
    private boolean skipPage;

    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.
            getResponse();
        ComponentContext context = ComponentContext.getContext(request);
        String strPath = null;
        skipPage = false;
        RefLinkManager refLinkManager = null;
        boolean cached = DgUtil.isResourceCached();

        // Get attribute from tiles context and check from instanceof PutItem
        Object object = (Object) context.getAttribute(attribute);

        SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
            CURRENT_SITE);

        ServletContext servletContext = request.getSession().getServletContext();

        String folderName = siteDomain.getSite().getFolder();

        // get file link instance if is first call then new
        refLinkManager = RefLinkManager.getInstance();

        if (object instanceof PutItem) {
            PutItem item = (PutItem) object;

            if (logger.isDebugEnabled()) {
                logger.l7dlog(Level.DEBUG,
                              "InsertTag.processingPutItem",
                              new Object[] {item}
                              , null);
            }

            this.setControllerType(null);
            try {
                switch (item.getItemType()) {
                    case PutItem.INVALID_ITEM:
                        throw new JspException("invalid put-item tag: " +
                                               item.getName());
                    case PutItem.EMPTY_ITEM: {
                        /** @todo must be removed*/
                            /*                        strPath = SiteConfigUtils.getFilePath(servletContext,
                                                    folderName, null, null,
                                                    attribute, null);*/
                        strPath = refLinkManager.getFilePath(siteDomain.getSite(),
                            cached,  null, null, attribute, null);
                        break;
                    }
                    case PutItem.FILE_ITEM: {
                        /** @todo implement this, must be removed */
//                        strPath = SiteConfigUtils.getPagePath(servletContext, folderName, item.getFile(), false);
                        strPath = refLinkManager.getPagePath(siteDomain.getSite(), cached,
                            item.getFile(), false);
                        break;
                    }

                    case PutItem.LAYOUT_ITEM: {
                        strPath = "/showLayout.do?layout=" + item.getLayout();
                        break;
                    }

                    case PutItem.MOD_PAGE_ITEM: {
                        ModuleInstance moduleInstance = RequestUtils.
                            getModuleInstance(request);
                        if (moduleInstance == null) {
                            throw new Exception(
                                "Error in digi:insert tag: unable to render secondary page " +
                                item.getModule() + ":" + item.getPage() +
                                " without instance information");

                        }
                        else {
                            if (!moduleInstance.getModuleName().equals(item.
                                getModule())) {
                                throw new Exception(
                                    "Error in digi:insert tag: unable to render secondary page " +
                                    item.getModule() + ":" + item.getPage() +
                                    ", because current module is " +
                                    moduleInstance.getModuleName() +
                                    " and instance is " +
                                    moduleInstance.getInstanceName());
                            }
                        }
                        /** @todo implement this, must be removed */
                            /* strPath = SiteConfigUtils.getFilePath(servletContext,
                                                    folderName,
                             item.getModule(), moduleInstance.getInstanceName(),
                                                    item.getPage(), null);*/
                            strPath = refLinkManager.getFilePath(siteDomain.getSite(), cached,

                                 item.getModule(), moduleInstance.getInstanceName(),
                                                        item.getPage(), null);


                        break;
                    }
                    case PutItem.MOD_INST_ITEM:
                    case PutItem.MOD_INST_TEASER_ITEM: {
                        ModuleInstance requiredInstance = DgUtil.
                            getRequiredInstance(request, item.getModule(),
                                                item.getInstance());
                        if ( (requiredInstance != null) &&
                            (requiredInstance.isPermitted())) {
                            if (logger.isDebugEnabled()) {
                                logger.l7dlog(Level.DEBUG,
                                    "InsertTag.insertTeaserForModInst",
                                    new Object[] {requiredInstance}
                                    , null);
                            }

                            if (item.getItemType() == PutItem.MOD_INST_ITEM) {
                                this.setControllerUrl("/" + item.getModule() +
                                    "/" +
                                    item.getInstance() +
                                    "/renderTeaser.do");

/*                                strPath = SiteConfigUtils.getFilePath(request.
                                    getSession().
                                    getServletContext(),
                                    siteDomain.getSite().getFolder(),
                                    item.getModule(),
                                    item.getInstance(), null, null);*/

                                strPath = refLinkManager.getFilePath(
                                    siteDomain.getSite(), cached,
                                    item.getModule(),
                                    item.getInstance(), null, null);

                            }
                            else {
                                ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(siteDomain.getSite());

                                String action = viewConfig.getTeaserAction(
                                    item.getModule(),
                                    item.getInstance(), item.getTeaser());

                                this.setControllerUrl(action);

                                /** @todo implement this, must be removed */
                                    /* strPath = SiteConfigUtils.getFilePath(
                                    servletContext,
                                    folderName,
                                    item.getModule(),
                                    item.getInstance(), null, item.getTeaser());
                                    */
                                   strPath = refLinkManager.getFilePath(
                                       siteDomain.getSite(), cached,
                                       item.getModule(),
                                       item.getInstance(), null, item.getTeaser());

                                      if (strPath == null) {
                                          // just something to prevent tiles go insine
                                          strPath = "null";
                                          skipPage = true;
                                      }
                            }

                        }
                        else {
                            logger.l7dlog(Level.WARN,
                                          "InsertTag.insertTeaserFailed",
                                          new Object[] {requiredInstance,
                                          item.getModule(),
                                          item.getInstance()}
                                          , null);

                            strPath = "/404.jsp";
                        }

                        break;
                    }

                }

                if (logger.isDebugEnabled()) {
                    logger.l7dlog(Level.DEBUG,
                                  "InsertTag.includingFile",
                                  new Object[] {strPath}
                                  , null);
                }

                // now put attribute in tiles context
                context.putAttribute(attribute, strPath);

            }
            catch (Exception ex) {
                logger.l7dlog(Level.ERROR,
                              "InsertTag.error",
                              null, ex);

                // now put attribute in tiles context to
                // report error in page
                context.putAttribute(attribute,
                                     "<H3><font color=\"red\">WARNING:</font> " +
                                     ex.getMessage() + "</H3>");
            }
        }
        else {
            throw new JspException(
                "Error - Digi tag Insert : Unknow object in ComponentContext " +
                attribute);
        }

        return super.doStartTag();

    }

    protected void doInclude(String page) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();
        ComponentContext context = ComponentContext.getContext(request);
        if (context != null) {
            Boolean roleProcessResult = (Boolean) context.getAttribute(
                Constants.ACTION_ROLES_PROCESS_RESULT);
            if (roleProcessResult == null ||
                roleProcessResult.equals(Boolean.TRUE)) {
                if (!skipPage) {
                    super.doInclude(page);
                }
            }
            else {
                super.doInclude("/403.jsp");
            }
        }
        else {
            if (!skipPage) {
                super.doInclude(page);
            }
        }

    }
}