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

import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.text.DgDateFormatSymbols;
import org.digijava.kernel.text.LocalizationUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;

public class DateTag
    extends TagSupport {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(DateTag.class);

    private String format;
    private String formatKey;
    private String name;
    private String property;
    private String scope;

    public int doStartTag() throws JspException {
        if (format != null && formatKey != null) {
            try {
                this.pageContext.getOut().write("Error in date tag: format and formatKey attributes can not be set simultaniously");
            }
            catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
                throw new JspException(ex.getMessage(), ex);
            }
        }
        // Look up the requested property value
        Object value =
            TagUtils.getInstance().lookup(pageContext, name,
            property, scope);
        if (value == null) {
            logger.debug("No value defined for bean name: " + name + " property: " + property + " scope: " + scope);
            return SKIP_BODY;
        }

        if (!(value instanceof Date)) {
            logger.debug("Value defined for bean name: " + name + " property: " + property + " scope: " + scope + " has type " + value.getClass() + " instead of java.util.Date");
            return SKIP_BODY;
        }
        Date date = (Date)value;

        String formatString = null;
        Locale currentLocale = RequestUtils.getNavigationLanguage((HttpServletRequest)pageContext.getRequest());
        java.util.Locale javaLocale = new java.util.Locale(currentLocale.getCode());

        if (formatKey != null) {
            Site currentSite = RequestUtils.getSite((HttpServletRequest)pageContext.getRequest());
            TranslatorWorker worker = TranslatorWorker.getInstance(formatKey);
            try {
                Message message = worker.getFromGroup(formatKey,
                    currentLocale.getCode(), currentSite, null);
                formatString = message.getMessage();
            }
            catch (WorkerException ex) {
                logger.error(ex.getMessage(), ex);
                throw new JspException(ex.getMessage(), ex);
            }
        } else {
            formatString = format;
        }

        DateFormat df;
        if (formatString == null) {
            df = LocalizationUtil.getDateTimeInstance(javaLocale);
        }
        else {
            DateFormatSymbols formatSymbols = new DgDateFormatSymbols(
                javaLocale);
            df = new SimpleDateFormat(formatString, formatSymbols);
        }

        /**
         * @todo determine time zone from request
         */
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getDefault());
        df.setCalendar(gregorianCalendar);

        try {
            this.pageContext.getOut().print(df.format(date));
        }
        catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new JspException(ex.getMessage(), ex);
        }

        return SKIP_BODY;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormatKey() {
        return formatKey;
    }

    public void setFormatKey(String formatKey) {
        this.formatKey = formatKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
