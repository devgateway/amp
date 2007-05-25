/*
 *   DateTimeUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Oct 20, 2003
 * 	 CVS-ID: $Id$
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
package org.digijava.module.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DateTimeUtil {

    /**
     * Convert iso date to java.util.Calendar
     * example:
     *  isoDate = "20030101" -> return Calendar object
     *
     * @param isoDate
     * @return
     */
    public static java.util.Calendar iso2Calendar(String isoDate) throws
        ParseException {

        java.util.Calendar date = new java.util.GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMdd" );
        date.setTime(simpleDateFormat.parse(isoDate));

        return date;
    }
}