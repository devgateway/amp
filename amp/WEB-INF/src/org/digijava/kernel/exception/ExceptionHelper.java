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

import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.error.AMPTaggedExceptions;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
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

    public static ActionForward processExceptionInfo(ExceptionInfo info, HttpServletRequest request) {
        ComponentContext context = ComponentContext.getContext(request);
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
