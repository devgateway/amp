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

package org.digijava.kernel.service;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.config.ServiceConfig;
import org.digijava.kernel.util.DigiConfigManager;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServiceManager {
    private static Logger logger = Logger.getLogger(ServiceManager.class);
    private static ServiceManager _instance = new ServiceManager();

    private Boolean initialized;
    private ServiceContext serviceContext;
    private Map services;

    public static ServiceManager getInstance() {
        return _instance;
    }

    private ServiceManager() {
        initialized = Boolean.FALSE;
        services = Collections.synchronizedMap(new HashMap());
    }

    public void init(ServiceContext serviceContext, int level) {
        logger.info("Starting DiGi services");
        this.serviceContext = serviceContext;
        Map serviceDefinitions = DigiConfigManager.getConfig().getServices();
        Iterator serviceIter = serviceDefinitions.entrySet().iterator();
        while (serviceIter.hasNext()) {
            Map.Entry item = (Map.Entry) serviceIter.next();
            String serviceId = (String) item.getKey();
            ServiceConfig serviceConfig = (ServiceConfig) item.getValue();

            if (serviceConfig.getLevel() != level) {
                continue;
            }

            Service service = null;
            try {
                service = createService(serviceConfig);
            }
            catch (Exception ex) {
                logger.error("Error creating service class " + serviceId, ex);
                continue;
            }

            try {
                service.init(this.serviceContext);
            }
            catch (ServiceException ex1) {
                logger.error("Error initializing service " + serviceId, ex1);
                continue;
            }
            boolean stop = false;
            boolean destroy = false;
            try {
                service.create();
            }
            catch (ServiceException ex2) {
                logger.error("Error calling create() for service " + serviceId,
                             ex2);
                destroy = true;
            }
            if (!destroy) {
                try {
                    service.start();
                }
                catch (ServiceException ex2) {
                    logger.error("Error calling start() for service " +
                                 serviceId, ex2);
                    stop = true;
                    destroy = true;
                }
            }

            if (stop) {
                try {
                    logger.info("Stopping service " + serviceId);
                    service.stop();
                }
                catch (ServiceException ex2) {
                    logger.error("Error calling stop() for service " +
                                 serviceId, ex2);
                }
            }
            if (destroy) {
                try {
                    logger.info("Destroying service " + serviceId);
                    service.destroy();
                }
                catch (ServiceException ex2) {
                    logger.error("Error calling destroy() for service " +
                                 serviceId, ex2);
                }
            }
            else {
                services.put(serviceId, service);
                logger.info("Service " + serviceId + " was deployed successfully");
            }
        }
    }

    public void shutdown(int level) {
        logger.info("Stopping DiGi services");
        Iterator iter = services.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry) iter.next();
            String serviceId = (String) item.getKey();
            Service service = (Service) item.getValue();

            if (service.getLevel() != level) {
                continue;
            }

            try {
                service.stop();
            }
            catch (ServiceException ex2) {
                logger.error("Error calling stop() for service " + serviceId,
                             ex2);
            }
            try {
                service.destroy();
                logger.info("Service " + serviceId + " was undeployed successfully");
            }
            catch (ServiceException ex2) {
                logger.error("Error calling destroy() for service " + serviceId,
                             ex2);
            }
        }
    }

    public Service getService(String id) {
        return (Service)services.get(id);
    }

    private Service createService(ServiceConfig serviceConfig) throws
        ClassNotFoundException, IllegalAccessException, InstantiationException,
        InvocationTargetException {

        Service service = (Service) Class.forName(serviceConfig.getServiceClass()).
            newInstance();
        service.setDescription(serviceConfig.getDescription());
        service.setLevel(serviceConfig.getLevel());
        BeanUtils.populate(service, serviceConfig.getProperties());

        return service;
    }
}
