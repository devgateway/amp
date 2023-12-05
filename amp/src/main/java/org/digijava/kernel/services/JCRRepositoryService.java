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

package org.digijava.kernel.services;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.service.Service;
import org.digijava.kernel.service.ServiceException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public interface JCRRepositoryService extends Service {
    /**
     * Service identifier in digi.xml
     */
    public static final String SERVICE_ID = "jcrRespositoryService";

    /**
     * Returs pre-configured session to JCR repository
     * @return pre-configured session to JCR repository
     * @throws ServiceException if error occurs
     */
    public Session getSession() throws ServiceException ;

    /**
     * Get site's root node, configured by this service
     * @param site Site
     * @return Node root node of this site
     * @throws DgException if error occurs during accessing Digijava DM
     * infrastructure
     * @throws RepositoryException if Repository access error occurs
     */
    public Node getRootNode(Site site) throws DgException, RepositoryException;
}
