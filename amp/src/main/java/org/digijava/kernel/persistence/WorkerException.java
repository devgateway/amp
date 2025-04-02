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

package org.digijava.kernel.persistence;

//Application import files
//import org.developmentgateway.core.exception.ApplicationException;

/**
 * The wrapper class around the java.lang.Exception for DevelopmentGateway. Thrown by all the methods of the components
 * @author Ashutosh Mishra
 * @version 1.0
 */

public class WorkerException extends Exception {

    /**
     * Default Constructor
     */
    public WorkerException() {
        super();
    }

    /**
     * Constructor. Creates a WorkerException object with a message
     * @param message the exception message
     */
    public WorkerException(String message) {
        super(message);
    }

    /**
     * Constructor. Creates a WorkerException object with a single message and a nested exception
     * @param message the exception message
     * @param cause java.lang.Throwable, presumably an exception you've already caught
     */

    public WorkerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor. Creates a WorkerException object with a nested exception
     * @param cause java.lang.Throwable, presumably an exception you've already caught
         */

    public WorkerException(Throwable cause) {
        super(cause);
    }

}
