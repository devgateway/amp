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

package org.digijava.kernel.mail.scheduler;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.MailSpool;
import org.digijava.kernel.mail.exception.MailSpoolException;
import org.digijava.kernel.mail.util.DbUtil;
import org.digijava.kernel.mail.util.MailUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MailScheduler implements Job {

    private static Logger logger = Logger.getLogger(MailScheduler.class);

    public MailScheduler() {
    }

    public void execute(JobExecutionContext cntxt) throws JobExecutionException {

        logger.info("Scheduler fire");

        try {
            // Cleaning the log from all records <clear-log>60</clear-log> deys old
            // see digi.xml
            DbUtil.deleteLast(DigiConfigManager.getConfig().getSmtp().
                              getClaerLogDays());

            // get every <error-cache>15</error-cache> minutes old record.
            // see digi.xml
            List list = DbUtil.getMails();
            if(list != null) {
                boolean mailSend = true;
                logger.info("Last " +
                            DigiConfigManager.getConfig().getSmtp().getCacheMinutes() +
                            " minutes old mail size: " + list.size());
                for (Object o : list) {
                    MailSpool item = (MailSpool) o;
                    item.setDateLastSend(new Date());
                    if (!MailUtil.sendMail(item)) {
                        DbUtil.saveMail(item);
                        mailSend = false;
                        continue;
                    }
                    item.setDateSend(new Date());
                    DbUtil.saveMail(item);
                }

                if(!mailSend){
                    logger.error("Mail can't send!");
                } else {
                    logger.info("Mails send success");
                }
            }

        }
        catch (MailSpoolException ex) {
            logger.error(ex.getMessage());
        }

    }

}
