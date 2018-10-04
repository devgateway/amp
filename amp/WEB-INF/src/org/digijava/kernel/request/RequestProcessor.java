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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.DynaBean;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.action.InvalidCancelException;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.Constants;
import org.digijava.kernel.config.moduleconfig.Security;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.exception.ExceptionHelper;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.AccessLogger;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.ModuleUtils;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * This class works as Struts request processor
 *
 * Struts invokes first processPath() and then processMap()
 *
 * @author Mikheil Kapanadze, Lasha Dolidze
 * @version 1.0
 *
 */
public class RequestProcessor
    extends TilesRequestProcessor {

    private static Logger logger = I18NHelper.getKernelLogger(RequestProcessor.class);

    private HashMap actionPermissions;

    private static Set<String> bypassRefererCheckActions;
    static {
        bypassRefererCheckActions = new HashSet<>();
        bypassRefererCheckActions.add("/aim/confirmRegisteration.do");
        bypassRefererCheckActions.add("/aim/csvExport.do");
        bypassRefererCheckActions.add("/aim/xlsExport.do");
        bypassRefererCheckActions.add("/aim/viewNewAdvancedReport.do");
        bypassRefererCheckActions.add("/exception/showExceptionReport.do");
        bypassRefererCheckActions.add("/aim/viewActivityPreview.do");
        bypassRefererCheckActions.add("/aim/default/previewActivity.do");
    }
    
    /**
     * Initialize RequestProcessor. Populate required permissions for Struts
     * actions
     * @param servlet
     * @param moduleConfig
     * @throws ServletException
     */
    public void init(ActionServlet servlet,
                     ModuleConfig moduleConfig) throws ServletException {

        super.init(servlet, moduleConfig);

        String modulePrefix = moduleConfig.getPrefix();
        actionPermissions = new HashMap();

        if ( (modulePrefix == null) || (modulePrefix.trim().length() == 0)) {

            ActionConfig[] actionConfigs = moduleConfig.findActionConfigs();
            if (actionConfigs != null) {
                for (int i = 0; i < actionConfigs.length; i++) {
                    ActionConfig actionConfig = actionConfigs[i];

                    int slashPos = actionConfig.getPath().substring(1).indexOf(
                        "/");
                    if (actionConfig.getPath().startsWith("/") &&
                        slashPos >= 0) {

                        String moduleName = actionConfig.getPath().substring(1,
                            slashPos + 1);
                        String actionPath = actionConfig.getPath().substring(
                            slashPos + 1);
                        initModuleActionSecurity(moduleName, actionPath, true);
                    }
                }
            }
        }
        else {
            throw new java.lang.UnsupportedOperationException(
                "Struts modules are not supported yet");
        }

        if (logger.isDebugEnabled()) {
            logger.l7dlog(Level.DEBUG, "RequestProcessor.requiredPermissions",
                          new Object[] {actionPermissions}, null);
        }
    }

    /**
     * Populate requred permissions for particular Struts action
     * @param moduleName name of the DiGi module
     * @param actionPath Struts action path
     * @param addModule true, if action path contains module name. In the
     * current implementation all DiGi module-specific actions start with
     * module name. For example, /admin/showEditSite, etc. and Struts modules
     * are not supported. So, this parameter must always be true.
     */
    private void initModuleActionSecurity(String moduleName, String actionPath,
                                          boolean addModule) {
        org.digijava.kernel.config.moduleconfig.ModuleConfig
            digiModuleConfig =
            DigiConfigManager.getModuleConfig(moduleName);

        if (digiModuleConfig == null)
            return;
        Security digiModuleSecurity = digiModuleConfig.
            getSecurity();
        if (digiModuleSecurity == null)
            return;

        if (logger.isDebugEnabled()) {
            logger.l7dlog(Level.DEBUG,
                          "RequestProcessor.parsingActionPermissions",
                          new Object[] {moduleName, actionPath}, null);
        }

        String key = addModule ? "/" + moduleName + actionPath :
            actionPath;
        boolean matched = false;
        Iterator iter = digiModuleSecurity.getActions().
            iterator();
        while (iter.hasNext()) {
            org.digijava.kernel.config.moduleconfig.Action
                digiAction = (org.digijava.kernel.config.
                              moduleconfig.Action) iter.
                next();

            if (actionPath.matches(digiAction.getPattern())) {
                matched = true;

                if (!digiAction.isLoginRequired()) {
                    // If login is not required, no action permissions are
                    // required too
                    actionPermissions.put(key, null);
                }
                else if ( (digiAction.getValue() == null) ||
                         (digiAction.getValue().trim().length() == 0)) {
                    // If login is required and nothing more
                    actionPermissions.put(key, new ArrayList());
                }
                else {
                    // If some action permissions are required
                    StringTokenizer st = new StringTokenizer(digiAction.
                        getValue(), ",");
                    ArrayList list = new ArrayList();
                    while (st.hasMoreElements()) {
                        list.add(new Integer(ResourcePermission.
                                             getActionCode(st.nextToken().
                            trim())));
                    }
                    actionPermissions.put(key, list);
                }
                break;
            }
        }
        if (!matched) {
            if (!digiModuleSecurity.isLoginRequired()) {
                // permit to all
                actionPermissions.put(key, null);
            }
            else {
                String defaultAction = digiModuleSecurity.
                    getDefaultAction();
                if (defaultAction.trim().length() == 0) {
                    // permit to logged in users
                    actionPermissions.put(key, new ArrayList());
                }
                else {
                    ArrayList list = new ArrayList();
                    list.add(new Integer(ResourcePermission.
                                         getActionCode(defaultAction)));
                    actionPermissions.put(key, list);
                }
            }
        }

    }

    private boolean checkForIdInQuery(String url){
        if (url.indexOf('~') > -1 || url.indexOf("id=") > -1 || url.indexOf("Id=") > -1)
            return true;
        return false;
    }
    
    public void process(HttpServletRequest request,
                        HttpServletResponse response) throws IOException,
        ServletException {
        request.setCharacterEncoding("UTF-8");
        String referrer = request.getHeader("referer"); // Yes, with the legendaric misspelling

        //AMP Security Issues - AMP-12638
        if (false)
        {
            String commonURL = new String(request.getRequestURL());
            String headCommonURL = commonURL.substring(0, commonURL.indexOf("://") + 3);
            commonURL = commonURL.substring(commonURL.indexOf("://") + 3);
            int idx = commonURL.indexOf('/');
            if (idx > -1){
                commonURL = commonURL.substring(0, idx);
            }
            String oldCommonURL = new String(commonURL);


            String actionPath = request.getRequestURL().substring(request.getRequestURL().indexOf("/", request.getRequestURL().indexOf("://") + 3));
            boolean bypassRefererCheck = false;
            if (bypassRefererCheckActions.contains(actionPath)) {
                referrer = request.getRequestURL().toString();
                bypassRefererCheck = true;
            }

            if (referrer != null){
                String commonREF = new String(referrer);
                commonREF = commonREF.substring(commonREF.indexOf("://") + 3);
                idx = commonREF.indexOf('/');
                if (idx > -1){
                    commonREF = commonREF.substring(0, idx);
                }
                
                if (commonREF.compareTo(commonURL) != 0 ){
                    commonURL = new String(request.getRequestURL());
                    if (request.getQueryString() != null)
                        commonURL += "?" + request.getQueryString();
                    if (checkForIdInQuery(commonURL)){
                        if (!bypassRefererCheck) {
                            throw new RuntimeException("Access denied for url: " + request.getRequestURL());
                        }
                    }
                }
            }
            else{
                commonURL = new String(request.getRequestURL());
                if (request.getQueryString() != null)
                    commonURL += "?" + request.getQueryString();
                if (!bypassRefererCheck && checkForIdInQuery(commonURL)){
                    throw new RuntimeException("Access denied for url: " + request.getRequestURL());
                    //response.sendRedirect(response.encodeRedirectURL(headCommonURL + oldCommonURL));
                }
            }
        }
        
        super.process(request, response);

    }

    @Override
    public boolean processPreprocess(HttpServletRequest request, HttpServletResponse response)
    {
        TLSUtils.populate(request);
        TranslatorUtil.insertAvailableLanguages(request);
        request.setAttribute("currentLocale", TLSUtils.getEffectiveLangCode());
        return true;
    }
        
    /**
     * Select the mapping used to process the selection path for this request.
     * If no mapping can be identified, create an error response and return
     * <code>null</code>.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param path The portion of the request URI for selecting a mapping
     *
     * @exception IOException if an input/output error occurs
     */
    protected ActionMapping processMapping(HttpServletRequest request,
                                           HttpServletResponse response,
                                           String path) throws IOException {

        ActionMapping mapping = super.processMapping(request, response, path);
        String moduleInstance = null;
        String moduleName = null;

        ComponentContext context = ComponentContext.getContext(request);
        if (context != null) {
            moduleInstance = (String) context.getAttribute(Constants.
                MODULE_INSTANCE);
        }
        else {
            moduleInstance = (String) request.getAttribute(Constants.
                MODULE_INSTANCE);
        }

        if (mapping != null) {
            // Clone mapping
            ActionMapping newMapping = new ActionMapping();
            newMapping.setForward(mapping.getForward());
            newMapping.setInclude(mapping.getInclude());
            newMapping.setInput(mapping.getInput());
            newMapping.setModuleConfig(mapping.getModuleConfig());
            newMapping.setMultipartClass(mapping.getMultipartClass());
            newMapping.setParameter(mapping.getParameter());
            newMapping.setPath(mapping.getPath());
            newMapping.setPrefix(mapping.getPrefix());
            newMapping.setRoles(mapping.getRoles());
            newMapping.setSuffix(mapping.getSuffix());
            newMapping.setType(mapping.getType());
            newMapping.setUnknown(mapping.getUnknown());
            newMapping.setValidate(mapping.getValidate());
            newMapping.setName(mapping.getName());
            newMapping.setScope(mapping.getScope());

            ExceptionConfig[] exceptionConfigs = mapping.findExceptionConfigs();
            for (int i = 0; i < exceptionConfigs.length; i++) {
                newMapping.addExceptionConfig(exceptionConfigs[i]);
            }

            ForwardConfig[] forwardConfigs = mapping.findForwardConfigs();
            for (int i = 0; i < forwardConfigs.length; i++) {
                newMapping.addForwardConfig(forwardConfigs[i]);
            }

            String actionFormName = mapping.getAttribute();
            SiteDomain currentSite = (SiteDomain) request.getAttribute(
                Constants.
                CURRENT_SITE);

            // Modify attribute parameter and store old one in request scope
            newMapping.setAttribute("site" + currentSite.getSite().getSiteId() +
                                    (moduleInstance == null ? actionFormName :
                                     moduleInstance) + actionFormName);
            if (logger.isDebugEnabled()) {
                logger.l7dlog(Level.DEBUG, "RequestProcessor.actionFormName",
                              new Object[] {newMapping.getAttribute()}, null);
            }
            request.setAttribute(Globals.MAPPING_KEY, newMapping);
            request.setAttribute(Constants.ORIGINAL_MAPPING, mapping);
            return newMapping;
        }
        else {
            return null;
        }

    }

    /**
     * Modify action path. Remove module instances from it and pass
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    protected String processPath(HttpServletRequest request,
                                 HttpServletResponse response) throws
        IOException {

        logger.debug("processPath() called for servletPath: " +
                     request.getServletPath() + " pathTranslated(): " +
                     request.getPathTranslated());

        String mainPath = super.processPath(request, response);
        if (logger.isDebugEnabled()) {
            logger.l7dlog(Level.DEBUG, "RequestProcessor.sourceActionPath",
                          new Object[] {mainPath}
                          , null);
        }

        Site currentSite = null;

        String alreadyParsed = RequestUtils.getSourceURL(request);
        boolean newRequest = (alreadyParsed == null);

        PathParseResult parseResult = null;
        if (newRequest) {
            SiteDomain siteDomain = null;
            siteDomain = SiteCache.getInstance().getSiteDomain(request.
                getServerName(),
                mainPath);

            if (siteDomain == null) {
                logger.error("Unable to get site, which matches to domain=" +
                             request.getServerName() + ", path=" + mainPath);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }

            if (siteDomain.getSitePath() != null) {
                mainPath = mainPath.substring(siteDomain.getSitePath().
                                              length());
                request.setAttribute(Constants.DIGI_CONTEXT,
                                     request.getContextPath() +
                                     siteDomain.getSitePath());
            }
            else {
                request.setAttribute(Constants.DIGI_CONTEXT,
                                     request.getContextPath());
            }

            request.setAttribute(Constants.CURRENT_SITE, siteDomain);

            try {
              parseResult = determineModuleInstance(request, mainPath,
                                        siteDomain.getSite().getDefaultInstance());
            } catch (UnknownInstanceException ex) {
                try {
                    response.sendError(response.SC_NOT_FOUND, ex.getMessage());
                }
                catch (IOException ex2) {
                    throw ex2;
                }
                logger.warn(ex.getMessage());
                return null;
            }

            // Process login
            String mainUrl = DgUtil.getFullURL(request);
            request.setAttribute(Constants.REQUEST_ALREADY_PROCESSED, mainUrl);

            request.setAttribute(Constants.REQUEST_URL,
                                 DgUtil.generateReferrer(request, null));

            try {
                HttpLoginManager.processErrors(request);

                HttpLoginManager.validateUserFromRequest(request);
                Set nonSSOPath = DgUtil.getNoneSSOPath();
                /*
                if (nonSSOPath == null || !nonSSOPath.contains(parseResult.getPath())) {
                    if (HttpLoginManager.processLogin(request, response)) {
                        return null;
                    }
                } */

            }
            catch (DgException ex1) {
                throw new RuntimeException(ex1);
            }

            // Process navigation language
            if (siteDomain.getLanguage() != null) {
                logger.debug("Site domain has language" +
                             siteDomain.getLanguage().getCode() +
                             " assigned. Performing switch");
                DgUtil.switchLanguage(siteDomain.getLanguage(), request,
                                      response);
            }
            else {
                logger.debug("Site domain does not have language assigned");
                DgUtil.setUserLanguage(request, response);
            }

            // Verify navigation language
            Locale currentLocale = RequestUtils.getNavigationLanguage(request);
            if (currentLocale == null) {
                logger.error("Navigation language was not determined correctly");
                throw new IllegalStateException(
                    "Navigation language was not determined correctly");
            }
            else {
                logger.debug("The current navigation language is: " +
                             currentLocale.getName());
            }

            // Check permissions
            Subject subject = RequestUtils.getSubject(request);
            if (!DgSecurityManager.permitted(subject, siteDomain.getSite(),
                                             SitePermission.INT_READ)) {
                if (!HttpLoginManager.isNewSessionAction(mainPath)) {
                    try {
                        // Forward to login and stop processing
                        doLogin(request, response);
                        return null;
                    }
                    catch (ServletException ex) {
                        throw new SecurityException();
                    }
                }
            }
            currentSite = siteDomain.getSite();
        }
        else {
            SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
                CURRENT_SITE);
            currentSite = siteDomain.getSite();
        }

        RequestUtils.setTranslationAttribute(request, "sitename",
                                             currentSite.getName());

        ModuleInstance requiredInstance = null;
        try {
            if (parseResult == null) {
                parseResult =
                    determineModuleInstance(request, mainPath, currentSite.getDefaultInstance());
            }
            requiredInstance = parseResult.getModuleInstance();
            mainPath = parseResult.getPath();
        }  catch (UnknownInstanceException ex3) {
            try {
                response.sendError(response.SC_NOT_FOUND, ex3.getMessage());
            }
            catch (IOException ex2) {
                throw ex3;
            }
            logger.warn(ex3.getMessage());
            return null;
        }

        if (requiredInstance != null) {
            if (!requiredInstance.isPermitted()) {
                throw new UnknownInstanceException(
                    "The required instance " +
                    requiredInstance.getModuleName() + ":" +
                    requiredInstance.getInstanceName() +
                    " is not permitted");
            }
            registerCurrentInstance(request, requiredInstance);
        }

        // Log request
        if (newRequest) {
            AccessLogger.log(request);
        }

        return mainPath;
    }

    private void registerCurrentInstance(HttpServletRequest request,
                                         ModuleInstance requiredInstance) {
        ComponentContext context = ComponentContext.getContext(request);

        if (context == null) {
            request.setAttribute(Constants.MODULE_INSTANCE_OBJECT,
                                 requiredInstance);
            request.setAttribute(Constants.MODULE_NAME,
                                 requiredInstance.getModuleName());
            request.setAttribute(Constants.MODULE_INSTANCE,
                                 requiredInstance.getInstanceName());
        }
        else {
            context.putAttribute(Constants.MODULE_INSTANCE_OBJECT,
                                 requiredInstance);
            context.putAttribute(Constants.MODULE_NAME,
                                 requiredInstance.getModuleName());
            context.putAttribute(Constants.MODULE_INSTANCE,
                                 requiredInstance.getInstanceName());
        }
    }

    private PathParseResult determineModuleInstance(HttpServletRequest request,
                                               String mainPath,
                                               ModuleInstance defaultInstance) throws
        UnknownInstanceException {

        ModuleInstance requiredInstance = null;
        String[] modinst = DgUtil.parseUrltoModuleInstanceAction(mainPath, false);
        if (modinst != null) {
            String moduleName = modinst[0];
            String moduleInstance = modinst[1];

            if (moduleName != null) {
                if (logger.isDebugEnabled()) {
                    logger.l7dlog(Level.DEBUG, "RequestProcessor.moduleName",
                                  new Object[] {moduleName}
                                  , null);
                }

                if (moduleInstance != null) {
                    if (modinst[3] == null) {
                        mainPath = "/" + moduleName +
                            mainPath.substring(2 + moduleName.length() +
                                               moduleInstance.length());
                    }
                } else {
                        ModuleInstance modInst = getModuleInstance(request);
                        if (modInst != null) {
                            if (modInst.getModuleName().equals(moduleName)) {
                                moduleInstance = modInst.getInstanceName();
                            }
                        }
                        // If still null
                        if (moduleInstance == null) {
                            moduleInstance = ModuleUtils.getModuleDefaultInstance(moduleName);
                        }
                    }

                requiredInstance = DgUtil.getRequiredInstance(request, moduleName,
                    moduleInstance);

                if (requiredInstance== null) {
                    String errorMessage = "The required instance " +  moduleName + ":" +  moduleInstance + " was not found";
                    throw new UnknownInstanceException(errorMessage);
                }
            }
        }
        //Default module processing
        if (defaultInstance != null) {
            String moduleName = defaultInstance.getModuleName();
            String instanceName = defaultInstance.getInstanceName();
            String contextPath = request.getContextPath();
            int actionSlashPos = mainPath.indexOf('/', 1);
            String uri = null;

            boolean isAction = false;
            if (actionSlashPos != -1) {
                uri = mainPath.substring(1, actionSlashPos);
            }
            else {
                uri = mainPath.substring(1);
                isAction = true;
            }
            if (isAction &&
                !uri.equals("exceptionHandle") &&
                !uri.equals("showLayout")) {
                mainPath = "/" + moduleName + "/" + uri;

                requiredInstance = defaultInstance;
                logger.debug("Action forwarderd to default module: '" +
                            moduleName +
                            "' instance: '" + instanceName + "'");
            }
        }
        return new PathParseResult(mainPath, requiredInstance);
    }

    /**
     * Check privilegies on action
     * @param request
     * @param response
     * @param mapping
     * @return true, if action is permitted
     * @throws IOException
     * @throws ServletException
     */
    protected boolean processRoles(HttpServletRequest request,
                                   HttpServletResponse response,
                                   ActionMapping mapping) throws IOException,
        ServletException {
        boolean result = false;
        ComponentContext context = ComponentContext.getContext(request);

        ArrayList requiredPermissions = (ArrayList) actionPermissions.get(
            mapping.getPath());
        if (requiredPermissions == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Action " + mapping.getPath() + " does not have security constraints");
            }
            // Allow everyone
            result = true;
        }
        else {
            // Allow only logged in
            if (logger.isDebugEnabled()) {
                logger.debug("Action " + mapping.getPath() + " has security constraints");
            }

            // If user is not logged in
            if (RequestUtils.getUser(request) == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Action " + mapping.getPath() + " has empty security constraints and user is not logged in");
                }
                result = false;
            }
            else {
                // If user is logged in and there are no additional requirements
                if (requiredPermissions.isEmpty()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Action " + mapping.getPath() + " has empty security constraints and user is logged in");
                    }
                    result = true;
                }
                else {
                    ModuleInstance moduleInstance;

                    if (context != null) {
                        moduleInstance = (ModuleInstance) context.getAttribute(
                            Constants.
                            MODULE_INSTANCE_OBJECT);
                    }
                    else {
                        moduleInstance = (ModuleInstance) request.getAttribute(
                            Constants.
                            MODULE_INSTANCE_OBJECT);
                    }
                    Site currentSite = RequestUtils.getSite(request);
                    Subject subject = RequestUtils.getSubject(request);

                    ModuleInstance realInstance = moduleInstance.
                        getRealInstance() == null ?
                        moduleInstance : moduleInstance.getRealInstance();

                    Iterator iter = requiredPermissions.iterator();
                    boolean permitted = false;
                    while (iter.hasNext()) {
                        Integer item = (Integer) iter.next();
                        permitted = DgSecurityManager.permitted(subject,
                            currentSite, realInstance,
                            item.intValue());
                        if (permitted) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Required action #" + item.intValue() + " is set");
                            }
                            break;
                        }
                    }
                    if (permitted) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Action " + mapping.getPath() + " is permitted");
                        }
                        result = true;
                    }
                    else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Action " + mapping.getPath() + " is not permitted");
                        }
                        result = false;
                    }
                }
            }
            if (result == false) {
                logger.debug("Action " + mapping.getPath() + " is redirecting to login...");
                doLogin(request, response);
            }
        }

        if (context != null) {
            context.putAttribute(Constants.ACTION_ROLES_PROCESS_RESULT,
                                 new Boolean(result));
        }
        else {
            request.setAttribute(Constants.ACTION_ROLES_PROCESS_RESULT,
                                 new Boolean(result));
        }
        return result;

    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    protected void doLogin(HttpServletRequest request,
                           HttpServletResponse response) throws IOException,
        ServletException {

        User user = RequestUtils.getUser(request);
        if (user == null && !DgUtil.isIgnoredUserAgent(request) /* &&
            ComponentContext.getContext(request) == null */) {
/*
            AuthenticationEntryPoint authEntry =
                (AuthenticationEntryPoint)springContext.getBean(
                    "digiAuthenticationProcessingFilterEntryPoint",
                    AuthenticationEntryPoint.class);
            PortResolver pr = new PortResolverImpl();
            SavedRequest savedRequest = new SavedRequest(request, pr);
            request.getSession().setAttribute(
                AbstractProcessingFilter.ACEGI_SAVED_REQUEST_KEY, savedRequest);

            authEntry.commence(request, response, null);
 */
            doForward("/showLayout.do?layout=login", request, response);
        }
        else
            doForward("/403.jsp", request, response);
    }

    /**
     * Handler of Action class's execute()/perform() results. If such method
     * returns forward on some module's action, this add instance name to the
     * action path
     * @param request
     * @param response
     * @param forward
     * @throws IOException
     * @throws ServletException
     */
    protected void processForwardConfig(HttpServletRequest request,
                                        HttpServletResponse response,
                                        ForwardConfig forward) throws
        IOException, ServletException {

        if (forward == null) {
            return;
        }

        log.debug("processForwardConfig("
                  + forward.getPath() + ", "
                  + forward.getModule() + ")");

        String sitePath = "";
        // For redirects, add site path element
        SiteDomain currSiteDomain = RequestUtils.getSiteDomain(
            request);
        if (forward.getRedirect()) {
            sitePath = currSiteDomain.getSitePath() == null ? "" :
                currSiteDomain.getSitePath();
        }

        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(request);
        String currentPath = forward.getPath();
        ForwardConfig newForwardConfig = forward;
        if (moduleInstance != null) {

            // Add instance parts to forwards, if action does forward
            // to one of the struts actions, which belong to the current module
            if (currentPath.startsWith("/" + moduleInstance.getModuleName() +
                                       "/")) {

                int slashPos = currentPath.indexOf('/', 1);
                String newPath = sitePath + "/" + moduleInstance.getModuleName() +
                    "/" +
                    moduleInstance.getInstanceName() +
                    currentPath.substring(slashPos);
                newForwardConfig = new ForwardConfig(
                    forward.getName(),
                    newPath,
                    forward.getRedirect(),
                    forward.getModule());
            }
        }

        if (newForwardConfig == forward && forward.getRedirect() &&
            currentPath.startsWith("/")) {
            newForwardConfig = new ForwardConfig(
                forward.getName(),
                sitePath + currentPath,
                forward.getRedirect(),
                forward.getModule());
        }

        if (ExceptionHelper.checkForInfiniteRecursion(request, response))
            return;

        super.processForwardConfig(request, response, newForwardConfig); //namnamu

    }

    /**
     * This method is simple stub. It passes original mapping to Struts's
     * procesValidate() method to process validator plugin correctly.
     * @param request
     * @param response
     * @param form
     * @param mapping
     * @return
     * @throws IOException
     * @throws ServletException
     */
    protected boolean processValidate(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ActionForm form,
                                      ActionMapping mapping) throws IOException,
        ServletException {
        ActionMapping originalMapping = (ActionMapping) request.getAttribute(
            Constants.ORIGINAL_MAPPING);

        try {
            return super.processValidate(request, response, form, originalMapping);
        } catch (InvalidCancelException e) {
            logger.error(e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Stores ActionForm of the "main" action and then passes it to included
     * actions througn Tiles context
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @param mapping ActionMapping mapping to process
     * @return ActionForm
     */
    protected ActionForm processActionForm(HttpServletRequest request,
                                           HttpServletResponse response,
                                           ActionMapping mapping) {
        ActionForm actionForm = processActionFormInstance(request,
            response,
            mapping);

        ComponentContext context = ComponentContext.getContext(request);
        Object form = request.getAttribute(Constants.MAIN_ACTION_FORM);
        if (form == null) {
            request.setAttribute(Constants.MAIN_ACTION_FORM,
                                 actionForm == null ? (Object) Boolean.FALSE :
                                 (Object) actionForm);
        }
        else {
            if ( (context != null) && (form instanceof ActionForm)) {
                context.putAttribute(Constants.MAIN_ACTION_FORM, form);
            }
        }

        return actionForm;

    }

    public HashMap getActionPermissions() {
        return actionPermissions;
    }

    protected ActionForward
        processActionPerform(HttpServletRequest request,
                             HttpServletResponse response,
                             Action action,
                             ActionForm form,
                             ActionMapping mapping) throws IOException,
        ServletException {
        ActionForward result = null;
        try {
            result = super.processActionPerform(request, response,
                action, form, mapping);
        } catch (ServletException se) {
            Throwable root = getRootException(se);
            if (root instanceof SecurityException) {
                doLogin(request, response);
                return null;
            } else {
                throw se;
            }
        }
        return result;
    }
    private Throwable getRootException(ServletException ex) {
        ServletException tmp = ex;
        while (tmp.getRootCause() != null) {
            if (tmp.getRootCause() instanceof ServletException) {
                tmp = (ServletException)tmp.getRootCause();
            } else {
                return tmp.getRootCause();
            }
        }
        return tmp;
    }

    protected Action processActionCreate(HttpServletRequest request,
                                         HttpServletResponse response,
                                         ActionMapping mapping) throws
        IOException {

        // Acquire the Action instance we will be using (if there is one)
        String className = mapping.getType();
        if (log.isDebugEnabled()) {
            log.debug(" Looking for Action instance for class " + className);
        }

        Site currentSite = RequestUtils.getSite(request);

        Action instance = null;
        if (currentSite != null) {
            instance = createAction(request, response,
                                    currentSite.getSiteId() + "." + className,
                                    mapping, false);
        }
        if (instance == null) {
            instance = createAction(request, response, className, mapping, true);
        }
        return (instance);
    }

    private Action createAction(HttpServletRequest request,
                                HttpServletResponse response, String className,
                                ActionMapping mapping,
                                boolean sendError) throws IOException {
        synchronized (actions) {

            // Return any existing Action instance of this class
            Action instance = (Action) actions.get(className);
            if (instance != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Returning existing Action instance");
                }
                return (instance);
            }

            // Create and return a new Action instance
            if (logger.isDebugEnabled()) {
                logger.debug("  Creating new Action instance");
            }

            try {
                instance = (Action) org.apache.struts.util.RequestUtils.
                    applicationInstance(className);
                // TODO Maybe we should propagate this exception instead of returning
                // null.
            }
            catch (Exception e) {
                if (sendError) {
                    logger.error(
                        getInternal().getMessage("actionCreate",
                                                 mapping.getPath()),
                        e);

                    response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        getInternal().getMessage("actionCreate",
                                                 mapping.getPath()));
                }
                else {
                    logger.debug("Can not find action class " + className);
                }

                return (null);
            }

            instance.setServlet(this.servlet);
            actions.put(className, instance);

            return instance;
        }
    }

    private ActionForm processActionFormInstance(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 ActionMapping mapping) {

        // Create (if necessary a form bean to use
        ActionForm instance = createActionForm
            (request, mapping, moduleConfig, servlet);
        if (instance == null) {
            return (null);
        }

        // Store the new instance in the appropriate scope
        if (log.isDebugEnabled()) {
            log.debug(" Storing ActionForm bean instance in scope '" +
                      mapping.getScope() + "' under attribute key '" +
                      mapping.getAttribute() + "'");
        }
        if ("request".equals(mapping.getScope())) {
            request.setAttribute(mapping.getAttribute(), instance);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute(mapping.getAttribute(), instance);
        }
        return (instance);

    }

    /**
     * Imported from org.apache.struts.util.RequestUtils
     * @param request
     * @param mapping
     * @param moduleConfig
     * @param servlet
     * @return
     */
    private static ActionForm createActionForm(
        HttpServletRequest request,
        ActionMapping mapping,
        ModuleConfig moduleConfig,
        ActionServlet servlet) {

        Site currentSite = RequestUtils.getSite(request);

        // Is there a form bean associated with this mapping?
        String attribute = mapping.getAttribute();
        if (attribute == null) {
            return (null);
        }

        // Look up the form bean configuration information to use
        String name = mapping.getName();
        FormBeanConfig config = moduleConfig.findFormBeanConfig(name);
        if (config == null) {
            return (null);
        }

        // Look up any existing form bean instance
        if (log.isDebugEnabled()) {
            log.debug(
                " Looking for ActionForm bean instance in scope '"
                + mapping.getScope()
                + "' under attribute key '"
                + attribute
                + "'");
        }
        ActionForm instance = null;
        HttpSession session = null;
        if ("request".equals(mapping.getScope())) {
            instance = (ActionForm) request.getAttribute(attribute);
        }
        else {
            session = request.getSession();
            instance = (ActionForm) session.getAttribute(attribute);
        }

        // Can we recycle the existing form bean instance (if there is one)?
        if (instance != null) {
            if (config.getDynamic()) {
                String className = ( (DynaBean) instance).getDynaClass().
                    getName();
                if (className.equals(config.getName())) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                            " Recycling existing DynaActionForm instance "
                            + "of type '"
                            + className
                            + "'");
                        log.trace(" --> " + instance);
                    }
                    return (instance);
                }
            }
            else {
                try {
                    Class configClass = createBrandedClass(config.getType(),
                        currentSite);
                    if (configClass.isAssignableFrom(instance.getClass())) {
                        if (log.isDebugEnabled()) {
                            log.debug(
                                " Recycling existing ActionForm instance "
                                + "of class '"
                                + instance.getClass().getName()
                                + "'");
                            log.trace(" --> " + instance);
                        }
                        return (instance);
                    }
                }
                catch (Throwable t) {
                    log.error(servlet.getInternal().getMessage("formBean",
                        config.getType()), t);
                    return (null);
                }
            }
        }

        // Create and return a new form bean instance
        if (config.getDynamic()) {
            try {
                DynaActionFormClass dynaClass =
                    DynaActionFormClass.createDynaActionFormClass(config);
                instance = (ActionForm) dynaClass.newInstance();
                ( (DynaActionForm) instance).initialize(mapping);
                if (log.isDebugEnabled()) {
                    log.debug(
                        " Creating new DynaActionForm instance "
                        + "of type '"
                        + config.getType()
                        + "'");
                    log.trace(" --> " + instance);
                }
            }
            catch (Throwable t) {
                log.error(servlet.getInternal().getMessage("formBean",
                    config.getType()), t);
                return (null);
            }
        }
        else {
            try {
                instance = (ActionForm) createBrandedClassInstance(config.
                    getType(), currentSite);
                if (log.isDebugEnabled()) {
                    log.debug(
                        " Creating new ActionForm instance "
                        + "of type '"
                        + config.getType()
                        + "'");
                    log.trace(" --> " + instance);
                }
            }
            catch (Throwable t) {
                log.error(servlet.getInternal().getMessage("formBean",
                    config.getType()), t);
                return (null);
            }
        }
        instance.setServlet(servlet);
        return (instance);

    }

    private static Class createBrandedClass(String className, Site site) throws
        ClassNotFoundException {
        Class configClass = null;
        if (site != null) {
            try {
                configClass = org.apache.struts.util.RequestUtils.
                    applicationClass(
                        site.getSiteId() + "." + className);
            }
            catch (ClassNotFoundException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Unable to find branded class " + className +
                                 " for site " + site.getSiteId());
                }
            }
        }
        if (configClass == null) {
            configClass = org.apache.struts.util.RequestUtils.
                applicationClass(className);

        }
        return configClass;
    }

    private static Object createBrandedClassInstance(String className,
        Site site) throws
        ClassNotFoundException, IllegalAccessException,
        java.lang.InstantiationException {
        Object configClass = null;
        if (site != null) {
            try {
                configClass = org.apache.struts.util.RequestUtils.
                    applicationInstance(
                        site.getSiteId() + "." + className);
            }
            catch (Exception ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Unable to find branded class " + className +
                                 " for site " + site.getSiteId());
                }
            }
        }
        if (configClass == null) {
            configClass = org.apache.struts.util.RequestUtils.
                applicationInstance(className);

        }
        return configClass;
    }

    public static ModuleInstance getModuleInstance(HttpServletRequest request) {
        ComponentContext context = ComponentContext.getContext(request);
        ModuleInstance moduleInstance = null;

        if (context != null) {
            moduleInstance = (ModuleInstance) context.getAttribute(
                Constants.
                MODULE_INSTANCE_OBJECT);
        } else {
            moduleInstance = (ModuleInstance) request.getAttribute(
                Constants.
                MODULE_INSTANCE_OBJECT);
        }
        return moduleInstance;
    }
}

class PathParseResult {
    private String path;
    private ModuleInstance moduleInstance;

    public PathParseResult(String path, ModuleInstance moduleInstance) {
        this.path = path;
        this.moduleInstance = moduleInstance;
    }

    public String getPath() {
        return path;
    }

    public ModuleInstance getModuleInstance() {
        return moduleInstance;
    }
}
