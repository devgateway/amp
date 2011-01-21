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

package org.digijava.kernel.request.searchfriendly;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.digijava.kernel.config.moduleconfig.Action;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.config.moduleconfig.Param;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteCache;
import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;

/**
 * This class processes search friendly URLs
 */
public class FriendlyRequestProcessor {

    private static Logger logger = Logger.getLogger(FriendlyRequestProcessor.class);

    public final static String ACTION_END = ".do";
    public static final String INCLUDE_REQUEST_URI =
        "javax.servlet.include.request_uri";

    public static final String FORWARD_REQUEST_URI =
        "javax.servlet.forward.request_uri";

    public static final char PARAM_SEPARATOR = DgUtil.getParamSeparator().
        charAt(0);

    public static final String UNNAMED_PARAM_NAME = "unnamedparameter";

    private static final String CHARACTER_ENCODING = "UTF-8";

    private static SearchFriendlyConfiguration config = new
        SearchFriendlyConfigurationImpl();

    /**
     * Rewrites the current URL to "normal" form and forward to it, if it is
     * needed.
     * @param sourceRequest HttpServletRequest
     * @param response HttpServletResponse
     * @return true, if URL was rewritten and forward was performed.
     * Otherwise - false.
     * @throws DgException on excaption
     * @throws IOException on excaption
     * @throws ServletException on excaption
     */
    public static boolean forwardRequest(HttpServletRequest sourceRequest,
                                         HttpServletResponse response) throws
        DgException, IOException, ServletException {

        // Included and forwarded requests are not processed
        if (sourceRequest.getAttribute(INCLUDE_REQUEST_URI) != null ||
            sourceRequest.getAttribute(FORWARD_REQUEST_URI) != null) {
            return false;
        }

        String uri = sourceRequest.getRequestURI();
        String query = sourceRequest.getQueryString();
        int endPosition = uri.indexOf(ACTION_END);
        // We are processing only actions. Not JSPs, images and so
        if (endPosition < 0) {
            return false;
        }

        // If there are no param separators (~) after action, then no special
        // processing is required.
        // Query part is commented now - in some parts of the system,
        // URLs are passed w/o encoding. So, it's dangerous
        if (uri.indexOf(PARAM_SEPARATOR, endPosition) < 0 /*&&
            (query == null || query.indexOf(PARAM_SEPARATOR) < 0) */) {
            return false;
        }

        SearchFriendlyConfiguration config = getSearchFriendlyConfiguration();
        ActionInformation actionInfo = createActionInformation(sourceRequest,
            endPosition);

        sourceRequest.setAttribute(Constants.ACTION_INFORMATION, actionInfo);

        String urlModify = FriendlyRequestProcessor.decodeURL(uri,
            query, endPosition + ACTION_END.length(),
            config, actionInfo);

        if (logger.isDebugEnabled()) {
            logger.debug("Source URI " + uri + " and Query, " +
                         sourceRequest.getQueryString() + " new URL is " +
                         urlModify);
        }

        RequestDispatcher rd = sourceRequest.getRequestDispatcher(urlModify);
        rd.forward(sourceRequest, response);

        return true;
    }

    public static String decodeURL(String uri, String query, int startPosition,
                                   SearchFriendlyConfiguration config,
                                   ActionInformation actionInfo) {
        String paramLine;
        int questionMarkPosition = uri.indexOf("?");
        if (questionMarkPosition >= 0) {
            throw new IllegalArgumentException("uri must not contain question mark");
        }

        paramLine = uri.substring(startPosition);
        if (query != null && query.indexOf(PARAM_SEPARATOR) >= 0) {
            paramLine = paramLine + PARAM_SEPARATOR + query;
            query = null;
        }

        String[] paramArray = DgUtil.fastSplit(paramLine, PARAM_SEPARATOR);
        StringBuffer newQuery = new StringBuffer(512);
        int unnamedIndex = 1;
        int arraySize = paramArray.length;
        for (int i = 1; i < arraySize; i++) {
            if (paramArray[i].indexOf('=') >= 0) {
                newQuery.append(paramArray[i]);
            }
            else {
                String paramName = config.getParameterName(actionInfo.getModule(),
                    actionInfo.getAction(), unnamedIndex);
                if (paramName == null) {
                    paramName = UNNAMED_PARAM_NAME + unnamedIndex;
                }
                newQuery.append(paramName + "=" + paramArray[i]);
                unnamedIndex++;
            }
            if (i < arraySize - 1) {
                newQuery.append("&");
            }
        }
        StringBuffer newUri = new StringBuffer(512);
        String newUriStr;
        if (actionInfo.getPrefix() != null) {
            newUriStr = uri.substring(actionInfo.getPrefix().length(),
                                      startPosition);
        }
        else {
            newUriStr = uri.substring(0, startPosition);
        }

        newUri.append(newUriStr);

        boolean queryAppended = false;
        if (query != null) {
            query = query.trim();
            if (query.length() > 0) {
                newUri.append("?").append(query);
                queryAppended = true;
            }
        }

        if (arraySize > 1) {
            if (queryAppended) {
                newUri.append("&");
            }
            else {
                newUri.append("?");
            }
            newUri.append(newQuery);
        }
        return newUri.toString();
    }

    /**
     * createActionInformation
     *
     * @param sourceRequest HttpServletRequest
     * @param endPosition int
     * @return ActionInformation
     * @throws DgException
     */
    private static ActionInformation createActionInformation(HttpServletRequest
        sourceRequest, int endPosition) throws DgException {
        return new ActionInformationImpl(sourceRequest, endPosition);
    }

    /**
     * getSearchFriendlyConfiguration
     *
     * @return SearchFriendlyConfiguration
     */
    private static SearchFriendlyConfiguration getSearchFriendlyConfiguration() {
        return config;
    }
}

class ActionInformationImpl
    implements ActionInformation {
    private String uri, module, instance, action, prefix;

    public ActionInformationImpl(HttpServletRequest request, int endPosition) throws
        DgException {
        this.uri = request.getRequestURI().substring(0, endPosition);

        SiteDomain siteDomain = SiteCache.getInstance().getSiteDomain(request.
            getServerName(), uri);
        if (siteDomain == null) {
            throw new DgException("Unable to find site which matches to url: " +
                                  request.getRequestURL());
        }

        String contextPath = request.getContextPath();
        int contextPathLen = contextPath.length();
        int sitePathLen = siteDomain.getSitePath() == null ? 0 :
            siteDomain.getSitePath().length();

        if (contextPathLen == 0) {
            prefix = null;
        } else {
            prefix = contextPath;
        }

        String actionLine = uri.substring(contextPathLen + sitePathLen);
        String[] actionInfo = DgUtil.parseUrltoModuleInstanceAction(actionLine);

        this.module = actionInfo[0];
        this.instance = actionInfo[1];
        this.action = actionInfo[2];
    }

    public String getModule() {
        return module;
    }

    public String getInstance() {
        return instance;
    }

    public String getAction() {
        return action;
    }

    public String getPrefix() {
        return prefix;
    }
}

class SearchFriendlyConfigurationImpl
    implements SearchFriendlyConfiguration {
    private static String getParamName(Action action,
                                       int paramIndex) {

        String paramName = null;
        List lists = null;

        // get module xml config by module name,
        // see DigiConfigManager getModuleConfig for more details
        if (action == null) {
            return null;
        }
        if (action != null)
            lists = action.getParams();

        if (lists == null || lists.size() == 0 || lists.size() < paramIndex) {
            return null;
        }

        Param param = (Param) lists.get(paramIndex - 1);
        return param.getName();
    }

    public String getParameterName(String module, String action, int index) {
        Action actionConfig = null;
        ModuleConfig moduleConfig = DigiConfigManager.getModuleConfig(module);
        if (moduleConfig != null) {
            actionConfig = moduleConfig.getAction(action);
            if (actionConfig != null) {
                return getParamName(actionConfig, index);
            }
        }
        return null;
    }

}
