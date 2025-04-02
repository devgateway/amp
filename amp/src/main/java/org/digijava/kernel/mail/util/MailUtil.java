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

package org.digijava.kernel.mail.util;

import org.apache.log4j.Logger;
import javax.mail.Address;
import org.digijava.kernel.mail.exception.MailSpoolException;
import java.util.List;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.digijava.kernel.entity.MailSpool;
import javax.mail.internet.InternetAddress;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import javax.mail.internet.*;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.util.DigiConfigManager;

public class MailUtil {
    private static Logger logger = Logger.getLogger(MailUtil.class);
    public static final int SUBJECT_MAX_LENGTH = 255;

    /**
     *
     * @param mail MailSpool
     */
    public static boolean sendMail(MailSpool mail) {

        Address[] address = null;
        Address[] addressCC = null;
        Address[] addressBCC = null;

        try {
            if (mail.getSenderAddress() != null)
                address = new Address[] {
                    new InternetAddress(mail.getSenderAddress())};

            if (mail.getCc() != null)
                addressCC = new Address[] {
                    new InternetAddress(mail.getCc())};

            if (mail.getBcc() != null)
                addressBCC = new Address[] {
                    new InternetAddress(mail.getBcc())};

            DgEmailManager.sendMail(address
                                    , mail.getReplayToSender(), addressCC
                                    , addressBCC
                                    , mail.getSubject(), mail.getBody(),
                                    mail.getCharset(), mail.isHtml(), false);

        }
        catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }

        return true;
    }

    /**
     *
     * @return boolean
     */
    public static boolean isSmtp() {

        List list = DbUtil.getLast(new Long(DigiConfigManager.getConfig().
                                            getSmtp().getCacheMinutes()));
        if (list != null & list.size() > 0) {
            return false;
        }

        return true;
    }

    /**
     *
     * @return int
     */
    public static int getNumberOfErrorMails() {

        List list = DbUtil.getLast(new Long(DigiConfigManager.getConfig().
                                            getSmtp().getCacheMinutes()));
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static String truncateSubject(String subject) {
        if (subject.length() > SUBJECT_MAX_LENGTH) {
            return subject.substring(0, SUBJECT_MAX_LENGTH);
        } else {
            return subject;
        }
    }

}
