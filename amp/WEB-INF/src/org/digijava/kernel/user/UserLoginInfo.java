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

package org.digijava.kernel.user;

import java.util.Date;

public class UserLoginInfo {
    private Long id;
    private Date lastVisit;
    private Date secondToLastVisit;
    private long numberOfSessions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public long getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(long numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public Date getSecondToLastVisit() {
        return secondToLastVisit;
    }

    public void setSecondToLastVisit(Date secondToLastVisit) {
        this.secondToLastVisit = secondToLastVisit;
    }

}
