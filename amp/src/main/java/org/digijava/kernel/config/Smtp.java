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

package org.digijava.kernel.config;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Smtp {

    private String host;
    private String from;
    private String userName;
    private String userPassword;
    private String cacheMinutes;
    private Long claerLogDays;
    private boolean enable;
    private boolean logEmail;

    public Smtp() {
        this.enable = false;
        this.logEmail = false;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHost() {
        return host;
    }

    public String getFrom() {
        return from;
    }

    public String toString() {
        return "Smtp: host='" + host + "' from='" + from + "'";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getCacheMinutes() {
        return cacheMinutes;
    }

    public Long getClaerLogDays() {
        return claerLogDays;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean isLogEmail() {
        return logEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setCacheMinutes(String cacheMinutes) {
        this.cacheMinutes = cacheMinutes;
    }

    public void setClaerLogDays(Long claerLogDays) {
        this.claerLogDays = claerLogDays;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setLogEmail(boolean logEmail) {
        this.logEmail = logEmail;
    }
}
