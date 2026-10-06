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
import org.digijava.kernel.entity.MailSpool;
import org.digijava.kernel.mail.exception.MailSpoolException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.CalendarType;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DbUtil {
  private static Logger logger = Logger.getLogger(DbUtil.class);


  /**
   *
   * @param to Address[]
   * @param from String
   * @param cc Address[]
   * @param bcc Address[]
   * @param subject String
   * @param text String
   * @param charset String
   * @param asHtml boolean
   * @throws MailSpoolException
   */
  public static void saveMail(Address[] to, String from, Address[] cc,
                              Address[] bcc, String subject,
                              String text,String charset,boolean asHtml) throws
      MailSpoolException {

      saveMail(to, from, cc,bcc,subject,text,charset,asHtml, null);

  }

  /**
   *
   * @param to Address[]
   * @param from String
   * @param cc Address[]
   * @param bcc Address[]
   * @param subject String
   * @param text String
   * @param charset String
   * @param asHtml boolean
   * @param error String
   * @throws MailSpoolException
   */
  public static void saveMail(Address[] to, String from, Address[] cc,
                              Address[] bcc, String subject,
                              String text,String charset,boolean asHtml, String error) throws
      MailSpoolException {

      Session session = null;
      MailSpool spool = new MailSpool();
      try {

          if( to != null )
              spool.setSenderAddress(InternetAddress.toString(to));

          if( cc != null )
              spool.setCc(InternetAddress.toString(cc));

          if( bcc != null )
              spool.setBcc(InternetAddress.toString(bcc));

          spool.setReplayToSender(from.toString());
          spool.setSubject(MailUtil.truncateSubject(subject));
          spool.setBody(text);
          spool.setHtml(asHtml);
          spool.setCharset(charset);
          if( error == null ) {
              spool.setDateSend(new Date());
          } else {
              spool.setError(error);
          }
          spool.setDateLastSend(new Date());

          session = PersistenceManager.getSession();
//beginTransaction();
          session.saveOrUpdate(spool);
          //tx.commit();

      }
      catch (Exception ex) {
          logger.debug(
              "Unable to update mail spool item information into database",
              ex);

          throw new MailSpoolException(
              "Unable to update mail spool item information into database",
              ex);
      }
  }


  /**
   *
   * @param mail MailSpool
   * @throws MailSpoolException
   */
  public static void saveMail(MailSpool mail) throws
      MailSpoolException {

      Session session = null;
      Transaction tx = null;
      try {
          session = PersistenceManager.getSession();
//beginTransaction();
          session.saveOrUpdate(mail);
          //tx.commit();

      }
      catch (Exception ex) {
          logger.debug(
              "Unable to update mail spool item information into database",
              ex);

          if (tx != null) {
              try {
                  tx.rollback();
              }
              catch (HibernateException ex1) {
                  logger.warn("rollback() failed", ex1);
              }
          }
          throw new MailSpoolException(
              "Unable to update mail spool item information into database",
              ex);
      }
  }

  /**
   *
   * @param days Long
   * @throws MailSpoolException
   */
  public static void deleteLast(Long days) throws
        MailSpoolException {

        Session session = null;
        String querryString;
        GregorianCalendar date = new GregorianCalendar();
        try {

            querryString = "delete from " +
            MailSpool.class.getName() +
            " rs where rs.dateSend <= :dateSend";

            session = PersistenceManager.getSession();
//beginTransaction();
            date.add(Calendar.DATE,(days.intValue()* -1));
            Query query = session.createQuery(querryString);
            query.setCalendar("dateSend", date);
            query.executeUpdate();
            //tx.commit();

        }
        catch (Exception ex) {
            logger.debug(
                "Unable to update mail spool item information into database",
                ex);
            throw new MailSpoolException(
                "Unable to update mail spool item information into database",
                ex);
        }
    }

    /**
     *
     * @param minute Long
     * @return List
     */
    public static List getLast(Long minute) {
      List spoolList = null;

      Session session = null;
      GregorianCalendar date = new GregorianCalendar();
      try {
        session = PersistenceManager.getSession();
        String querryString = "";
        querryString = "from " +
            MailSpool.class.getName() +
            " rs where rs.dateLastSend >= :dateParam and rs.dateSend is null order by rs.dateLastSend desc";

        Query q = session.createQuery(querryString);
        date.add(Calendar.MINUTE,(minute.intValue() * -1));
        q.setParameter("dateParam", date,CalendarType.INSTANCE);
        spoolList = q.list();
      }
      catch (Exception ex) {
      }
      return spoolList;
    }


    /**
     *
     * @param minute Long
     * @return List
     */
    public static List getMails() {
      List spoolList = null;

      Session session = null;
      try {
        session = PersistenceManager.getSession();
        String querryString = "";
        querryString = "from " +
            MailSpool.class.getName() +
            " rs where rs.dateSend is null order by rs.dateLastSend asc";

        Query q = session.createQuery(querryString);
        spoolList = q.list();
      }
      catch (Exception ex) {
      }
      return spoolList;
    }

}
