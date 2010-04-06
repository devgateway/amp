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

package org.digijava.kernel.quartz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import java.util.StringTokenizer;
import org.digijava.kernel.quartz.StartupJob;

public class SchedulerService
    extends AbstractServiceImpl {

    private static Logger logger = Logger.getLogger(SchedulerService.class);

    private String listener = null;
    private String startupJobFactory = null;
    private Scheduler schd;
    private StdSchedulerFactory factory;

    public SchedulerService() {}

    public void setListener(String listener) {
        this.listener = listener;
    }

    public void setStartupJobFactory(String startupJobFactory) {
        this.startupJobFactory = startupJobFactory;
    }

    public String getListener() {
        return listener;
    }

    public String getStartupJobFactory() {
        return startupJobFactory;
    }

    public void processInitEvent(ServiceContext serviceContext) throws
        ServiceException {
        factory = new StdSchedulerFactory();
        try {
            factory.initialize();
            logger.info("Initialized scheduler");
        }
        catch (SchedulerException ex) {
            logger.debug("Failed initializing scheduler");
            throw new ServiceException("Failed initializing scheduler", ex);
        }
    }

    public void processCreateEvent() throws ServiceException {
        try {
            schd = factory.getScheduler();

            List listeners = getListenersList();
            int lsnrSize = listeners.size();
            logger.debug("There are " + lsnrSize + " listener(s) configured");

            Iterator it = listeners.iterator();

            while (it.hasNext()) {

                JobListener listener = (JobListener) Class.forName( (String)
                    it.next()).newInstance();
                schd.addJobListener(listener);
            }
        }
        catch (Exception ex) {
            logger.debug("Failied to initialize scheduler");
            throw new ServiceException("Failied to initialize scheduler",
                                       ex);
        }
    }

    protected void processStartEvent() throws ServiceException {

        try {

            logger.info("Starting scheduler");

            schd.start();

            if (startupJobFactory != null && startupJobFactory.trim().length() != 0) {
                StartupJobFactory jobFactory = (StartupJobFactory)
                    Class.forName(startupJobFactory).newInstance();

                List jobs = jobFactory.getStartupJobs();

                if (jobs.size() > 0) {

                    Iterator it = jobs.iterator();

                    while (it.hasNext()) {
                        StartupJob job = (StartupJob) it.next();
                        schd.triggerJobWithVolatileTrigger(job.getJobName(),
                            job.getGroupName());
                    }

                }
            }
        }
        catch (Exception ex) {
            logger.debug("Failied to start up jobs");
            throw new ServiceException("Failied to start up jobs", ex);
        }

    }

    public void processStopEvent() throws ServiceException {
        try {
            //schd = StdSchedulerFactory.getDefaultScheduler();
            if (schd != null) schd.shutdown();
        }
        catch (Exception ex) {
            logger.debug("Failied to shutdown scheduler");
            throw new ServiceException("Failied to shutdown scheduler", ex);
        }
    }

    public void processDestroyEvent() throws ServiceException {
        schd = null;
    }

    public Scheduler getScheduler() {
        return this.schd;
    }

    private List getListenersList() {

        List listenersList = new ArrayList();

        if (listener != null && listener.trim().length() > 0) {

            StringTokenizer tokens = new StringTokenizer(listener, ",");

            while (tokens.hasMoreTokens()) {
                listenersList.add(tokens.nextToken().toString());
            }

        }

        return listenersList;

    }

}
