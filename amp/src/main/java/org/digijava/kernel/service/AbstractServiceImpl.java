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

public abstract class AbstractServiceImpl
    implements Service {

    protected String description;
    protected int level;

    public final void init(ServiceContext serviceContext) throws
        ServiceException {
        processInitEvent(serviceContext);
    }

    protected void processInitEvent(ServiceContext serviceContext) throws
        ServiceException {

    }

    public final void create() throws ServiceException {
        processCreateEvent();
    }

    protected void processCreateEvent() throws ServiceException {
    }

    public final void destroy() throws ServiceException {
        processDestroyEvent();
    }

    protected void processDestroyEvent() throws ServiceException {
    }

    public final void start() throws ServiceException {
        processStartEvent();
    }

    protected void processStartEvent() throws ServiceException {

    }

    public final void stop() throws ServiceException {
        processStopEvent();
    }

    protected void processStopEvent() throws ServiceException {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
