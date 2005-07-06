/*
 *   DigiConfig.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: DigiConfig.java,v 1.1 2005-07-06 10:34:15 rahul Exp $
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

package org.digijava.kernel.config;

import java.util.HashMap;
import java.util.HashSet;

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
    private String serverType;

    private ParamSafeHTML paramSafehtml;
    private ParamBbTag paramBbTag;
    private ForwardEmails forwardEmails;
    private ExceptionEmails exceptionEmails;
    private HashSet ignoredAgents;
    private boolean enableLogging;
    private Integer accessLogPoolSize;
    private Integer accessLogBuffSize;
    private String domainPrefix;
    private String siteConfigImpl;
    private long jobDelaySec;
    private boolean trackSessions;
    private SingleServerJobs singleServerJobs;

    public DigiConfig() {
        this.modules = new HashMap();
        this.ignoredAgents = new HashSet();
        this.enableLogging = false;
        this.domainPrefix = "";
        this.siteConfigImpl = ViewConfigFactory.DEFAULT_IMPL;
        this.jobDelaySec = 15;
        this.trackSessions = false;
        this.accessLogBuffSize = null;
        this.accessLogPoolSize = null;
        this.aggregation = false;
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

    public HashSet getIgnoredAgents() {
        return ignoredAgents;
    }

    public void setIgnoredAgents(HashSet ignoredAgents) {
        this.ignoredAgents = ignoredAgents;
    }

    public void addIgnoredAgent(String ignoredAgent) {
        ignoredAgents.add(ignoredAgent);
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

    public void setSingleServerJobs(SingleServerJobs singleServerJobs) {
        this.singleServerJobs = singleServerJobs;
    }

    public void setAggregation(boolean aggregation) {
        this.aggregation = aggregation;
    }

}
