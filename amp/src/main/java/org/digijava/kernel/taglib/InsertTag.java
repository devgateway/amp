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

package org.digijava.kernel.taglib;

import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.siteconfig.PutItem;
import org.digijava.kernel.taglib.util.RefLinkManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Digi insert tag
 *
 * @author Lasha Dolidze
 * @version 1.0
 */
public class InsertTag
    extends org.apache.struts.tiles.taglib.InsertTag {

    private static final long serialVersionUID = 1L;

    private static Logger logger = I18NHelper.getKernelLogger(InsertTag.class);

    private static final String COMP_ID_KEY = "org.digijava.kernel.taglib.insert.component_key";

    private String layout;
    private boolean skipPage;
    private InsertItemInfo itemInfo = null;
    private Integer componentId;

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

        //String folderName = siteDomain.getSite().getFolder();

        // get file link instance if is first call then new
        refLinkManager = RefLinkManager.getInstance();

        if (object instanceof PutItem) {
            PutItem item = (PutItem) object;
            String itemType = "invalid";
            String controllerUrl = null;
            if (logger.isDebugEnabled()) {
                logger.debug("Processing <put-item> tag: " + item);
            }

            this.setControllerType(null);
            try {
                switch (item.getItemType()) {
                    case PutItem.INVALID_ITEM:
                        throw new JspException("invalid put-item tag: " +
                                               item.getName());
                    case PutItem.EMPTY_ITEM: {
                        itemType = "empty";
                        strPath = refLinkManager.getFilePath(siteDomain.getSite(),
                            cached,  null, null, attribute, null);
                        break;
                    }
                    case PutItem.FILE_ITEM: {
                        itemType = "file";
                        strPath = refLinkManager.getPagePath(siteDomain.getSite(), cached,
                            item.getFile(), false);
                        break;
                    }

                    case PutItem.LAYOUT_ITEM: {
                        itemType="layout";
                        strPath = "/showLayout.do?layout=" + item.getLayout();
                        break;
                    }

                    case PutItem.MOD_PAGE_ITEM: {
                        itemType="page";
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
                            strPath = refLinkManager.getFilePath(siteDomain.getSite(), cached,

                                 item.getModule(), moduleInstance.getInstanceName(),
                                                        item.getPage(), null);


                        break;
                    }
                    case PutItem.MOD_INST_ITEM:
                    case PutItem.MOD_INST_TEASER_ITEM: {
                        itemType="teaser";
                        ModuleInstance requiredInstance = DgUtil.
                            getRequiredInstance(request, item.getModule(),
                                                item.getInstance());
                        if ( (requiredInstance != null) &&
                            (requiredInstance.isPermitted())) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("digi:insert -> inserting teaser. Module instance is : "
                                        + requiredInstance);
                            }

                            if (item.getItemType() == PutItem.MOD_INST_ITEM) {
                                controllerUrl = "/" + item.getModule() +
                                    "/" +
                                    item.getInstance() +
                                    "/renderTeaser.do";


                                strPath = refLinkManager.getFilePath(
                                    siteDomain.getSite(), cached,
                                    item.getModule(),
                                    item.getInstance(), null, null);

                            }
                            else {
                                ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(siteDomain.getSite());

                                controllerUrl = viewConfig.getTeaserAction(
                                    item.getModule(),
                                    item.getInstance(), item.getTeaser());

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
                            this.setControllerUrl(controllerUrl);

                        }
                        else {
                            itemType="error";
                            logger.warn(MessageFormat.format("digi:insert -> unable to insert teaser. "
                                            + "Module instance {0} is not permitted. "
                                            + "Required module: {1}, instance: {2}",
                                    requiredInstance,
                                    item.getModule(),
                                    item.getInstance()));

                            strPath = "/404.jsp";
                        }

                        break;
                    }

                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Including file: " + strPath);
                }

                // now put attribute in tiles context
                context.putAttribute(attribute, strPath);

            }
            catch (Exception ex) {
                logger.error("Error in digi:insert tag");

                // now put attribute in tiles context to
                // report error in page
                context.putAttribute(attribute,
                                     "<H3><font color=\"red\">WARNING:</font> " +
                                     ex.getMessage() + "</H3>");
            }
            if (RequestUtils.isDevelopmentModeActive(request)) {
                itemInfo = new InsertItemInfo();
                itemInfo.itemId = attribute;
                itemInfo.filePath = strPath;
                itemInfo.itemType = itemType;
                itemInfo.controllerAction = controllerUrl;
                itemInfo.definition = DgUtil.dehtmlize(item.toString());
            }
        }
        else {
            throw new JspException(
                "Error - Digi tag Insert : Unknown object in ComponentContext " +
                attribute);
        }
        return super.doStartTag();

    }

    @Override
    protected void doInclude(String page,boolean flush) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();
        if (RequestUtils.isDevelopmentModeActive(request)) {
            String componentKey = getComponentKey(attribute);
            if (itemInfo == null) {
                pageContext.getOut().write("<fieldset><legend>{" + attribute +
                                           "}</legend>");
            } else {
                pageContext.getOut().write(
                    "<fieldset><legend title=\"Click here for more details\" onclick=\"document.getElementById('" +
                    componentKey + "').style.display='block'\">{" + attribute +
                    "}</legend>");
                pageContext.getOut().write("<div title=\"close\" id=\"" +
                                           componentKey +
                                           "\" onClick=\"this.style.display='none'\" style=\"border:1px solid black; position:absolute;z-index:10;display:none;\" align=\"right\">");
                pageContext.getOut().write(itemInfo.toString());
                pageContext.getOut().write("</div>");
            }
        }
        ComponentContext context = ComponentContext.getContext(request);
        if (context != null) {
            Boolean roleProcessResult = (Boolean) context.getAttribute(
                Constants.ACTION_ROLES_PROCESS_RESULT);
            if (roleProcessResult == null ||
                roleProcessResult.equals(Boolean.TRUE)) {
                if (!skipPage) {
                    super.doInclude(page,false);
                }
            }
            else {
                super.doInclude("/403.jsp",false);
            }
        }
        else {
            if (!skipPage) {
                super.doInclude(page,false);
            }
        }
        if (RequestUtils.isDevelopmentModeActive(request)) {
            pageContext.getOut().write("</fieldset>");
        }
    }

    private static String getComponentKey(String attribute) {
        return "insert_" + attribute + "_" + String.valueOf(Math.random()).replaceAll("\\p{Punct}", "");
    }
}
class InsertItemInfo {
    String itemId;
    String filePath;
    String itemType;
    String controllerAction;
    String definition;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<table bgcolor=\"white\" border=\"1\" bordercolor=\"silver\" style=\"border-collapse:collapse\">");
        sb.append("<tr><td colspan=\"2\" align=\"center\">Details</td></tr>");
        sb.append("<tr><td>Item Id</td><td>").append(itemId).append("</td></tr>");
        sb.append("<tr><td>File path</td><td>").append(filePath).append("</td></tr>");
        sb.append("<tr><td>Item Type</td><td>").append(itemType).append("</td></tr>");
        sb.append("<tr><td>Controller</td><td>").append(controllerAction).append("</td></tr>");
        sb.append("<tr><td colspan=\"2\" align=\"left\">Definition</td></tr>");
        sb.append("<tr><td colspan=\"2\" align=\"left\">").append(definition).append("</td></tr>");
        sb.append("</table>");

        return sb.toString();
    }
}
