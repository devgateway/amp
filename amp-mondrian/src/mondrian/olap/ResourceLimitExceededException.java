/*
// $Id: ResourceLimitExceededException.java,v 1.1 2008-11-10 10:06:16 mpostelnicu Exp $
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2004-2005 TONBELLER AG
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
*/
package mondrian.olap;

/**
 * Exception which indicates some resource limit was exceeded.
 *
 * @version $Id: ResourceLimitExceededException.java,v 1.1 2008-11-10 10:06:16 mpostelnicu Exp $
 */
public class ResourceLimitExceededException
    extends ResultLimitExceededException
{
    /**
     * Creates a ResourceLimitExceededException
     *
     * @param message Localized message
     */
    public ResourceLimitExceededException(String message) {
        super(message);
    }
}

// End ResourceLimitExceededException.java
