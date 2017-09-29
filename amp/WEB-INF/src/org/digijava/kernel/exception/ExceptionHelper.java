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

package org.digijava.kernel.exception;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.error.AMPTaggedExceptions;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.action.ActionForward;
import org.digijava.kernel.util.DgUtil;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessages;

public class ExceptionHelper {

    public static final String EXCEPTION_INFO =
        "org.digijava.kernel.exception.exception_info";
    public static final String LAST_EXCEPTION_INFO_ID =
        "org.digijava.kernel.exception.last_exception_info_id";
    public static final String EXCEPTION_INFO_MAP =
        "org.digijava.kernel.exception.exception_info_map";

    private static Logger logger = Logger.getLogger(ExceptionHelper.class);
    
    public static ExceptionInfo populateExceptionInfo(Integer code,
        Throwable cause, HttpServletRequest request) {
        ExceptionInfo exceptionInfo = new ExceptionInfo();

        ModuleInstance modInst = RequestUtils.getRealModuleInstance(request);
        Site site = RequestUtils.getSite(request);

        exceptionInfo.setExceptionCode(code);
        exceptionInfo.setTimestamp(System.currentTimeMillis());
        if (site != null) {
            exceptionInfo.setSiteId(site.getId().longValue());
            exceptionInfo.setSiteKey(site.getSiteId());
            exceptionInfo.setSiteName(site.getName());
        }

        if (modInst != null) {
            exceptionInfo.setModuleName(modInst.getModuleName());
            exceptionInfo.setInstanceName(modInst.getInstanceName());
        }

        exceptionInfo.setSourceURL(RequestUtils.getSourceURL(request));
        exceptionInfo.setException(cause);
        if (cause != null) {
            exceptionInfo.setErrorMessage(cause.toString());
            exceptionInfo.setUserMessage(cause.getMessage());
            
            if (cause instanceof AMPTaggedExceptions){ //we have AMP errors either checked or unchecked
                AMPTaggedExceptions taggedEx = ((AMPTaggedExceptions)cause);
                if (taggedEx.getMainCause() != null){
                    String msg = taggedEx.getMainCause().getMessage();
                    if (msg != null)
                        taggedEx.getMainCause().getMessage();
                    exceptionInfo.setUserMessage(msg);
                    
                }
                    
                LinkedList<String> tags = (taggedEx).getTags();
                if (tags != null && tags.size() > 0){
                    exceptionInfo.setMainTag(tags.get(0));
                }
                exceptionInfo.setTags(tags);
            }

            CharArrayWriter cw = new CharArrayWriter();
            PrintWriter pw = new PrintWriter(cw, true);
            cause.printStackTrace(pw);
            exceptionInfo.setStackTrace(cw.toString());
        }

        return exceptionInfo;
    }

    public static void storeExceptioinInfo(HttpServletRequest request,
                                           ExceptionInfo info) {
        RequestUtils.setDigiContextAttribute(request, EXCEPTION_INFO, info);
    }

    public static ExceptionInfo getExceptioinInfo(HttpServletRequest request) {
        return (ExceptionInfo) RequestUtils.getDigiContextAttribute(request,
            EXCEPTION_INFO);
    }

    public static long storeTilesExceptioinInfo(HttpServletRequest request,
                                                ExceptionInfo info) {
        HttpSession session = request.getSession();
        Long currentId = (Long) session.getAttribute(
            LAST_EXCEPTION_INFO_ID);
        if (currentId == null) {
            currentId = new Long(0);
        }
        else {
            currentId = new Long(currentId.longValue() + 1);
        }
        session.setAttribute(LAST_EXCEPTION_INFO_ID, currentId);

        HashMap exceptionInfos = (HashMap) session.getAttribute(
            EXCEPTION_INFO_MAP);
        if (exceptionInfos == null) {
            exceptionInfos = new HashMap();
        }
        exceptionInfos.put(currentId, info);
        session.setAttribute(EXCEPTION_INFO_MAP, exceptionInfos);

        return currentId.longValue();
    }

    public static ExceptionInfo getTilesExceptioinInfo(HttpServletRequest
        request, long infoId) {
        HttpSession session = request.getSession();
        Long id = new Long(infoId);
        HashMap exceptionInfos = (HashMap) session.getAttribute(
            EXCEPTION_INFO_MAP);
        if (exceptionInfos == null) {
            return null;
        }
        ExceptionInfo result = (ExceptionInfo) exceptionInfos.get(id);
        if (result != null) {
            exceptionInfos.remove(id);
            session.setAttribute(EXCEPTION_INFO_MAP, exceptionInfos);
        }

        return result;
    }

    /**
     * returns true IFF the current stacktrace looks like being an infinity loop with a depth of >= maxAllowed
     * @param maxAllowed
     * @return
     */
    public static boolean getExceptionDepth(int maxAllowed)
    {
        int res = 0;
        StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
        for (StackTraceElement elem:stackTrace)
        {
            boolean thisIsIt = elem.getClassName().contains("org.digijava.kernel.request.RequestProcessor") &&
                    elem.getMethodName().equals("processForwardConfig");
            if (thisIsIt)
                res ++;
        }
        boolean ret = (res > maxAllowed) ||
                stackTrace.length >= 1020; // Java only keeps 1024 of them anyway
        //if (ret)
            //System.out.println("res = " + res + ", stackDepth = " + stackTrace.length + ", dying");
        return ret;
    }
    
    /**
     * outputs to a Logger the last <b>few</b> lines of the stacktrace
     * @param logger
     */
    public static void printReducedStacktrace(org.apache.log4j.Logger logger)
    {
        StringBuilder bld = new StringBuilder();
        StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
        for(int i = 0; i < Math.min(10, stackTrace.length); i++)
            bld.append("\t" + stackTrace[i] + "\n");
        logger.error(bld.toString());
    }
    
    protected static void printReducedStacktrace(HttpServletResponse response, String message)
    {
        try
        {
            PrintWriter writer = response.getWriter();
            writer.write(message);
            ////System.out.println(message);
            logger.error(message);
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for(int i = 0; i < 200; i++)
            {           
                writer.write(stackTrace[i].toString() + "\n");
                logger.error(stackTrace[i].toString());
            }
            writer.write("\n....\n");
            //System.out.println("\n....\n");
            for(int i = stackTrace.length - 200; i < stackTrace.length; i++)
            {
                writer.write(stackTrace[i].toString() + "\n");
                logger.error(stackTrace[i].toString());
            }
            writer.close();
        }
        catch(Exception e)
        {
            // swallow
        }
    }

    /**
     * returns true IFF an infinite recursion was detected and an error message war written to the output file
     * @param request
     * @param response
     * @return
     */
    public static boolean checkForInfiniteRecursion(HttpServletRequest request, HttpServletResponse response)
    {
        boolean deep = getExceptionDepth(20);
        return deep;
    }
    
    /**
     * checks if a request, whose rendering has just generated an Exception, was generated while trying to report an another exception<br />
     * returns true IFF the answer is "yes"
     * @param request
     * @return
     */
    public static boolean isRenderingAnException(HttpServletRequest request)
    {
        String DOUBLE_EXCEPTION_MARKER_ATTR = "###doubleExceptionMarker###";
        Object b = request.getAttribute(DOUBLE_EXCEPTION_MARKER_ATTR);
        if (b != null)
        {
            return true;
        }
        request.setAttribute(DOUBLE_EXCEPTION_MARKER_ATTR, new Object());
        return false;
    }
    
    public static String getFullRequestURI(HttpServletRequest request){
        String uri = request.getScheme() + "://" +
                request.getServerName() + 
                ("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort() ) +
                request.getRequestURI() +
               (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        return uri;
    }
    /**
     * called when an exception is on its way of being processed
     * @param info
     * @param request
     * @param response
     * @return
     */
    public static ActionForward processExceptionInfo(ExceptionInfo info, HttpServletRequest request, HttpServletResponse response) {
        ComponentContext context = ComponentContext.getContext(request);
        
        boolean isDoubleException = isRenderingAnException(request);
        if (isDoubleException)
        {
            printReducedStacktrace(response, "an exception was caught; moreover, an exception was generated while trying to generate the error page. Stopping. The webpage was: " + getFullRequestURI(request) + "\n");
            return null;
        }
        
        if (checkForInfiniteRecursion(request, response))
        {
            printReducedStacktrace(response, "looks like AMP went into an infinite jsp:include loop. The webpage was: " + getFullRequestURI(request));
            return null;
        }
        
        if (context == null) {
            ExceptionHelper.storeExceptioinInfo(request, info);
            return new ActionForward("/exception/showExceptionReport.do");
        } else {
            long exceptionId = ExceptionHelper.storeTilesExceptioinInfo(request, info);
            String relativeUrl = "/exception/showExceptionReport.do?reportId=" + exceptionId;
            String url = DgUtil.getCurrRootUrl(request) + relativeUrl;
            ActionMessage error = new ActionMessage("errors.detail", url);
            String property = error.getKey();

            storeException(context, request, property, error);
            return new ActionForward(relativeUrl);
        }
    }

    private static void storeException(
        ComponentContext context,
        HttpServletRequest request,
        String property,
        ActionMessage error) {
        ActionMessages errors =
            (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
        if (errors == null) {
            errors = new ActionMessages();
        }
        errors.add(property, error);
        context.putAttribute(Globals.ERROR_KEY, errors);
    }
}
