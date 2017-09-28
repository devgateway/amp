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

package org.digijava.kernel.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.digijava.kernel.viewmanager.ViewConfigFactory;

/**
 * Data class, used by Digester to parse digi.xml.
 * See org.digijava.kernel.startup.ConfigLoaderListener,
 * org.digijava.kernel.persistence.HibernateClassLoader
 * for more details
 * @author Lasha Dolidze
 * @version 1.0
 */
public class DigiConfig {

    private HibernateClasses hibernateClasses;
    private Smtp smtp;
    private LogonSite logonSite;
    private HashMap modules;
    private ParamSeparator paramSeparator;
    private ResCache resCache;
    private boolean aggregation;
    private boolean search;
    private String serverType;
    
    private String ecsDisable;
    private String ecsServerName;
    private String propertiesFile;

    private int httpPort;
    private int httpsPort;
    
    private ParamSafeHTML paramSafehtml;    
    private ParamBbTag paramBbTag;
    private ForwardEmails forwardEmails;
    private ExceptionEmails exceptionEmails;
    private HashSet agents;
    private HashSet noneSSOPath;


    private boolean ignore;
    private boolean enableLogging;
    private boolean enableAutoLogin;
    private Integer accessLogPoolSize;
    private Integer accessLogBuffSize;
    private String domainPrefix;
    private boolean enableOmniture;
    private boolean caseSensitiveTranslatioKeys;
    private String siteConfigImpl;
    private long jobDelaySec;
    private boolean trackSessions;
    private SingleServerJobs singleServerJobs;
    private Map configBeans;
    private Map services;
    private LogonSite siteDomain;

    public DigiConfig() {
        this.modules = new HashMap();
        this.agents = new HashSet();
        this.noneSSOPath = new HashSet();
        this.ignore = false;
        this.enableLogging = false;
        this.domainPrefix = "";
        this.siteConfigImpl = ViewConfigFactory.DEFAULT_IMPL;
        this.jobDelaySec = 15;
        this.trackSessions = false;
        this.accessLogBuffSize = null;
        this.accessLogPoolSize = null;
        this.aggregation = false;
        this.search = false;
        this.configBeans = new HashMap();
        this.services = new HashMap();
        this.caseSensitiveTranslatioKeys=true;
        this.httpPort  = 0;
        this.httpsPort = 0;
    }

    public HibernateClasses getHibernateClasses() {
        return hibernateClasses;
    }

    public void setHibernateClasses(HibernateClasses hibernateClasses) {
        this.hibernateClasses = hibernateClasses;
    }

    public Smtp getSmtp() {
        return smtp;
    }

    public void setSmtp(Smtp smtp) {
        this.smtp = smtp;
    }

    public LogonSite getLogonSite() {
        return logonSite;
    }

    public void setLogonSite(LogonSite logonSite) {
        this.logonSite = logonSite;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(hibernateClasses).append(smtp).append(logonSite);
        return buf.toString();

        // return hibernateClasses.toString() + smtp.toString() +
        //     logonSite.toString();
    }

    public HashMap getModules() {
        return modules;
    }

    public void setModules(HashMap modules) {
        this.modules = modules;
    }

    public void addModule(Module module) {
        modules.put(module.getName(), module);
    }

    public ParamSeparator getParamSeparator() {
        return paramSeparator;
    }

    public void setParamSeparator(ParamSeparator paramSeparator) {
        this.paramSeparator = paramSeparator;
    }

    public ResCache getResCache() {
        return resCache;
    }

    public void setResCache(ResCache resCache) {
        this.resCache = resCache;
    }

    public ParamSafeHTML getParamSafehtml() {
        return paramSafehtml;
    }

    public void setParamSafehtml(ParamSafeHTML paramSafehtml) {
        this.paramSafehtml = paramSafehtml;
    }

    public ParamBbTag getParamBbTag() {
        return paramBbTag;
    }

    public void setParamBbTag(ParamBbTag paramBbTag) {
        this.paramBbTag = paramBbTag;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public HashSet getAgents() {
        return agents;
    }

    public void setAgents(HashSet agents) {
        this.agents = agents;
    }

    public void addAgent(String agent) {
        agents.add(agent);
    }

    public void addIgnoreAgent(String agent) {
        this.ignore = true;
        agents.add(agent);
    }

    public void addNoneSSOPath(String path) {
        noneSSOPath.add(path);
    }

    public HashSet getNoneSSOPath() {
        return this.noneSSOPath;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public String getDomainPrefix() {
        return domainPrefix;
    }

    public void setDomainPrefix(String domainPrefix) {
        this.domainPrefix = domainPrefix;
    }

    public ForwardEmails getForwardEmails() {
        return forwardEmails;
    }

    public void setForwardEmails(ForwardEmails forwardEmails) {
        this.forwardEmails = forwardEmails;
    }

    public ExceptionEmails getExceptionEmails() {
        return exceptionEmails;
    }

    public void setExceptionEmails(ExceptionEmails exceptionEmails) {
        this.exceptionEmails = exceptionEmails;
    }

    public String getSiteConfigImpl() {
        return siteConfigImpl;
    }

    public void setSiteConfigImpl(String siteConfigImpl) {
        this.siteConfigImpl = siteConfigImpl;
    }

    public long getJobDelaySec() {
        return jobDelaySec;
    }

    public void setJobDelaySec(long jobDelaySec) {
        this.jobDelaySec = jobDelaySec;
    }

    public boolean isTrackSessions() {
        return trackSessions;
    }

    public void setTrackSessions(boolean trackSessions) {
        this.trackSessions = trackSessions;
    }

    public Integer getAccessLogBuffSize() {
        return accessLogBuffSize;
    }

    public void setAccessLogBuffSize(Integer accessLogBuffSize) {
        this.accessLogBuffSize = accessLogBuffSize;
    }

    public Integer getAccessLogPoolSize() {
        return accessLogPoolSize;
    }

    public void setAccessLogPoolSize(Integer accessLogPoolSize) {
        this.accessLogPoolSize = accessLogPoolSize;
    }

    public SingleServerJobs getSingleServerJobs() {
        return singleServerJobs;
    }

    public boolean isAggregation() {
        return aggregation;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSingleServerJobs(SingleServerJobs singleServerJobs) {
        this.singleServerJobs = singleServerJobs;
    }

    public void setAggregation(boolean aggregation) {
        this.aggregation = aggregation;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public Map getConfigBeans() {
        return configBeans;
    }

    public void setConfigBeans(Map configBeans) {
        this.configBeans = configBeans;
    }

    public void addConfigBean(ConfigBean configBean) {
        this.configBeans.put(configBean.getBeanId(), configBean);
    }

    public boolean isIgnore() {
        return ignore;
    }

    public Map getServices() {
        return services;
    }

    public boolean isEnableOmniture() {
        return enableOmniture;
    }

    public void addService(ServiceConfig serviceConfig) {
        this.services.put(serviceConfig.getServiceId(), serviceConfig);
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public void setServices(Map services) {
        this.services = services;
    }

    public void setEnableOmniture(boolean enableOmniture) {
        this.enableOmniture = enableOmniture;
    }

    public boolean isCaseSensitiveTranslatioKeys() {
        return caseSensitiveTranslatioKeys;
    }

    public void setCaseSensitiveTranslatioKeys(boolean caseSensitiveTranslatioKeys) {
        this.caseSensitiveTranslatioKeys = caseSensitiveTranslatioKeys;
    }
    public LogonSite getSiteDomain() {
        return siteDomain;
    }

    public void setSiteDomain(LogonSite siteDomain) {
        this.siteDomain = siteDomain;
    }
    
    public boolean isEnableAutoLogin() {
        return enableAutoLogin;
    }

    public void setEnableAutoLogin(boolean enableAutoLogin) {
        this.enableAutoLogin = enableAutoLogin;
    }
    
    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(int httpsPort) {
        this.httpsPort = httpsPort;
    }

    public String getEcsDisable() {
        return ecsDisable;
    }

    public void setEcsDisable(String ecsDisable) {
        this.ecsDisable = ecsDisable;
    }

    public String getEcsServerName() {
        return ecsServerName;
    }

    public void setEcsServerName(String ecsServerName) {
        this.ecsServerName = ecsServerName;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

}
