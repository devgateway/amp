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

import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.config.moduleconfig.Action;
import org.digijava.kernel.config.moduleconfig.ModuleConfig;
import org.digijava.kernel.config.moduleconfig.Param;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.SiteCache;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DgUrlProcessor {

    private static Logger logger = I18NHelper.getKernelLogger(DgUrlProcessor.class);

    /**
     * Process parsing google frendly url and forward on new url
     *
     * /news/political/showNewsDelait.do1-2-refno=5-3-sub=1234567
     *
     * Its parameters should be parsed as:
     *   unnamedparameter1=1
     *   unnamedparameter2=2
     *   refno=5
     *   unnamedparameter3=3
     *   sub=1234567
     *
     * NOTE:
     *   if unnamedparameter defined in module xml file
     *   then retrive folowing param name by index
     *
     * @param uri
     * @return
     */
    public static boolean doProcess(HttpServletRequest request,
                    HttpServletResponse response) {

    String urlModify = processParams(request);
    if (urlModify != null) {
        DgHttpRequestWrapper newRequest = new DgHttpRequestWrapper(request);
        RequestDispatcher rd = request.getRequestDispatcher(urlModify);
        try {
        rd.forward(newRequest, response);
        return true;
        }
        catch (IOException ex) {
        logger.error("Unable to process URL", ex);
        }
        catch (ServletException ex) {
        logger.error("Unable to process URL", ex);
        }
    }
    return false;
    }

    /**
     * Process parsing google frendly url, same as doProcess
     *
     * /news/political/showNewsDelait.do1-2-refno=5-3-sub=1234567
     *
     * Its parameters should be parsed as:
     *   unnamedparameter1=1
     *   unnamedparameter2=2
     *   refno=5
     *   unnamedparameter3=3
     *   sub=1234567
     *
     * NOTE:
     *   if unnamedparameter defined in module xml file
     *   then retrive folowing param name by index
     *
     * @param request
     * @param uri
     * @return
     */
    private static String processParams(HttpServletRequest request) {

//        String uri ="/news/political/showNewsDelait.do1-2-refno=5-3-sub=1234567-sub=Ioldash";

    String urlPattern = ".do"; //
    String moduleName = null;
    String pattern = null;
    String url2 = null;
    String uri = getFullURI(request);

    StringBuffer url = null;

    int n = uri.indexOf(urlPattern);
    if (n != -1) {
        url = new StringBuffer(uri);
        String params = uri.substring(n + urlPattern.length(), uri.length());

        if (params.startsWith("?")) {
        return null;
        }

        url.delete(n + urlPattern.length(), uri.length());
        url.delete(0, request.getContextPath().length());

        SiteDomain siteDomain = SiteCache.getInstance().getSiteDomain(
          request.getServerName(), url.toString());
        if (siteDomain != null && siteDomain.getSitePath() != null) {
        url2 = url.substring(siteDomain.getSitePath().length(),
                     url.length());
        }
        else
        url2 = url.toString();

        // expand url , module name, instance name, action
        String[] modinst = DgUtil.parseUrltoModuleInstanceAction(url2);

        if (modinst != null) {
        moduleName = modinst[0];

        // if debuge enabled
        if (logger.isDebugEnabled()) {
            logger.info(String.format("Expand url module name %s, module instance %s, action name %s",
                    modinst[0], modinst[1], modinst[2]));
        }

        // remove .do from action name
        if (modinst[2] != null) {
            if (modinst[2].endsWith(urlPattern)) {
            StringBuffer actionName = new StringBuffer(modinst[2]);
            actionName.delete(actionName.length() -
                      urlPattern.length(),
                      actionName.length());
            pattern = actionName.toString();
            }
        }
        }

        String[] prms;
        int unammedParamIndex = 0;
        HashMap mapParams = new HashMap();
        String[] list = null;

        Action action = null;
        ModuleConfig moduleConfig = DigiConfigManager.getModuleConfig(
          moduleName);
        if (moduleConfig != null) {
        action = moduleConfig.getAction(pattern);
        if (action != null) {
            request.setAttribute(Constants.DG_ACTION_CONFIG, action);
        }
        }

        if (params == null || params.length() <= 0) {
        return null;
        }
        params = DgUtil.fastReplaceAll(params, '?',
                       DgUtil.getParamSeparator().charAt(0));
        prms = DgUtil.fastSplit(params, DgUtil.getParamSeparator().charAt(0));

        for (int i = 1; i < prms.length; i++) {

        String[] prmsNamed = prms[i].split("\\=");
        for (int a = 0; a < prmsNamed.length; a++) {

            // process named parameters
            if (prmsNamed.length > 1) {

            list = (String[]) mapParams.get(prmsNamed[a]);
            if (list == null) {
list = new String[1];
                list[0] = prmsNamed[a + 1];
            }
            else {
                String[] tmp = new String[list.length + 1];
                for (int r = 0; r < list.length; r++) {
                tmp[r] = list[r];
                }
                tmp[list.length] = prmsNamed[a + 1];
                list = tmp;
            }
            mapParams.put(prmsNamed[a], list);
            break;
            }
            // process unnamed parameter
            else {
            if (prmsNamed[a].length() > 0) {

                // get named parameter by module name and pattern
                String paramName = getParamName(action,
                  unammedParamIndex + 1);
                if (paramName != null) {
                unammedParamIndex++;
                String[] paramValue = new String[1];

                paramValue[0] = prmsNamed[a];
                mapParams.put(paramName, paramValue);
                }

            }
            else {
                unammedParamIndex++;
            }
            break;
            }
        }
        }

        // if any parameter then retrive url
        if (!mapParams.isEmpty()) {

        // set Parameter HashMap in request context
        // for future use in DgHttpRequestWrapper
        request.setAttribute(Constants.DIGI_PARAM_MAP, mapParams);
        }
    }

    return (url == null) ? null : url.toString();
    }

    /**
     * Get parameter name by index from ModuleConfig xml,
     * otherwise generate unnamedparam string
     *
     * if unamed parameter bind in xml file then retrive , binded param name
     * for example if paramIndex = 1 then retrive "activeNewsId" from module xml file
     *
     * <action pattern="/showNewsDetails">
     *       <param name="activeNewsId" />
     * </action>
     *
     * @param moduleName
     * @param pattern
     * @param paramIndex
     * @return
     */
    public static String getParamName(Action action,
                      int paramIndex) {

    String paramName = null;
    List lists = null;
    String unnamedParamPrefix = "unnamedparameter";

    // get module xml config by module name,
    // see DigiConfigManager getModuleConfig for more details
    if (action == null) {
        paramName = new String();
        paramName = unnamedParamPrefix + paramIndex;
    }
    else {
        if (action != null)
        lists = action.getParams();

        if (lists == null || lists.size() == 0 || lists.size() < paramIndex) {
        paramName = new String();
        paramName = unnamedParamPrefix + paramIndex;

        }
        else {
        Param param = (Param) lists.get(paramIndex - 1);
        if (param != null) {
            paramName = param.getName();
        }
        else {
            paramName = unnamedParamPrefix + paramIndex;
        }
        }
    }
    return paramName;
    }

    /**
     *
     * @param url
     * @return
     */
    public static String formatUrl(String url, String moduleName,
                   String pattern, HttpServletRequest request) {

    //String url = "/bla/bububu/showNewsDetails.do?id=1&recalc=2&refno=7&mid=3&sub=1234567";
    StringBuffer newUrl = new StringBuffer();
    boolean found = false;
    boolean hideAfterQMark = false;
    int n = 0;
    List list = null;
    int questionMarkPosition = -1;

    // get module xml config by module name,
    // see DigiConfigManager getModuleConfig for more details
    if (moduleName != null) {
        ModuleConfig moduleConfig = DigiConfigManager.getModuleConfig(
          moduleName);
        if (moduleConfig != null) {
        Action action = moduleConfig.getAction(pattern);
        if (action != null) {
            list = action.getParams();
            if (action.getQuestionMarkPosition() != null)
            questionMarkPosition = new Integer(action.
                  getQuestionMarkPosition()).
                  intValue();

            hideAfterQMark = action.isHideAfterQMark();
        }
        }
    }

    n = url.indexOf("?");
    ArrayList paramList = new ArrayList();
    if (n != -1) {
        String prmUrl = url.substring(n + 1, url.length());

        newUrl.append(url.substring(0, n));
        newUrl.append(DgUtil.getParamSeparator());

        String[] params = DgUtil.fastSplit(prmUrl, '&'); //prmUrl.split("\\&");
        for (int i = 0; i < params.length; i++) {
        String[] prms = params[i].split("\\=");
        if (prms.length == 2)
            paramList.add(prms);
        else {
            paramList.add(new String[] {prms[0], ""});
        }
        }
    }

    if (list != null && (newUrl.toString().length() > 0)) {
        for (int i = 0; i < list.size(); i++) {
        Param param = (Param) list.get(i);
        found = false;
        for (int z = 0; z < paramList.size(); z++) {
            String[] item = (String[]) paramList.get(z);
            if (param.getName().equalsIgnoreCase(item[0])) {
            found = true;
            newUrl.append(item[1]);
            if (i == (questionMarkPosition - 1))
                newUrl.append("?");
            else
                newUrl.append(DgUtil.getParamSeparator());

            paramList.remove(z);
            break;
            }
        }

        if (!found ) {
            newUrl.append("");
            if (i == (questionMarkPosition - 1))
            newUrl.append("?");
            else
            newUrl.append(DgUtil.getParamSeparator());
        }
        }
    }

    for (int z = 0; z < paramList.size(); z++) {
        String[] item = (String[]) paramList.get(z);
        newUrl.append(item[0]);
        newUrl.append("=");
        newUrl.append(item[1]);
        /*                if (z == (questionMarkPosition - 1))
                newUrl.append("?");
                else*/
        newUrl.append(DgUtil.getParamSeparator());
    }

    if (newUrl.toString().length() > 0) {
        if (newUrl.charAt(newUrl.length() - 1) ==
        DgUtil.getParamSeparator().charAt(0)) {
        newUrl.delete(newUrl.length() - 1, newUrl.length());
        }
    }

    int index = newUrl.indexOf("?");
    if (index != -1 && hideAfterQMark && DgUtil.isIgnoredUserAgent(request)) {
        return newUrl.substring(0, index);
    }

    return (newUrl.toString().length() <= 0) ? url : newUrl.toString();
    }

    /**
     * Get full URI with params
     *
     * @param request
     * @return
     */
    public static String getFullURI(HttpServletRequest request) {

    // Calculate full url with query string
    StringBuffer url = new StringBuffer(request.getRequestURI());
    if (request.getQueryString() != null) {
        url.append("?");
        url.append(request.getQueryString());
    }

    return url.toString();
    }

}
