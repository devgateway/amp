/*
// $Id: QueryCanceledException.java,v 1.1 2008-11-10 10:06:16 mpostelnicu Exp $
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
 * Exception which indicates that a query was canceled by an end-user.
 *
 * <p>See also {@link mondrian.olap.QueryTimeoutException}, which indicates that
 * a query was canceled automatically due to a timeout.
 *
 * @version $Id: QueryCanceledException.java,v 1.1 2008-11-10 10:06:16 mpostelnicu Exp $
 */
public class QueryCanceledException extends ResultLimitExceededException {
    /**
     * Creates a QueryCanceledException.
     *
     * @param message Localized error message
     */
    public QueryCanceledException(String message) {
        super(message);
    }
}

// End QueryCanceledException.java
