/*
// $Id: ResultLimitExceededException.java,v 1.1 2008-11-10 10:06:17 mpostelnicu Exp $
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2004-2005 TONBELLER AG
// Copyright (C) 2005-2007 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
*/
package mondrian.olap;

/**
 * Abstract base class for exceptions that indicate some limit was exceeded.
 *
 * @version $Id: ResultLimitExceededException.java,v 1.1 2008-11-10 10:06:17 mpostelnicu Exp $
 */
public abstract class ResultLimitExceededException extends MondrianException {

    /**
     * Creates a ResultLimitExceededException.
     *
     * @param message Localized message
     */
    public ResultLimitExceededException(String message) {
        super(message);
    }
}

// End ResultLimitExceededException.java
