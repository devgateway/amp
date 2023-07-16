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
import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.util.DigiConfigManager;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class MailSpoolManager {

    private static Logger logger = Logger.getLogger(MailSpoolManager.class);

    public MailSpoolManager() {
    }

    public static synchronized void initialize() throws DgException {
        JobDetail jobDetail = null;
        CronTrigger trigger = null;

        try {

            logger.info("Initialize Mail Scheduler");

            // Create a new instance of the Quartz Scheduler
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();

            jobDetail = JobBuilder.newJob(MailScheduler.class)
                    .withIdentity("MailScheduler", "MailSchedulerGroup")
                    .build();

            // fire every <error-cache>15</error-cache> minute.
            // see digi.xml
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity("MailSchedulerTrigger", "MailSchedulerGroup")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/" +
                            DigiConfigManager.getConfig().getSmtp().getCacheMinutes() +
                            " * * * ?"))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

        }
        catch (Exception ex) {
            throw new DgException("can't initialize mail scheduler", ex);
        }
    }
}

