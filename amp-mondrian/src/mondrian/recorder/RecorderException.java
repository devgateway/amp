/*
// $Id: RecorderException.java,v 1.1 2008-11-10 10:07:32 mpostelnicu Exp $
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2005-2006 Julian Hyde and others.
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
*/
package mondrian.recorder;

import mondrian.olap.MondrianException;

/**
 * Exception thrown by {@link MessageRecorder} when too many errors
 * have been reported.
 *
 * @author Richard M. Emberson
 * @version $Id: RecorderException.java,v 1.1 2008-11-10 10:07:32 mpostelnicu Exp $
 */
public final class RecorderException extends MondrianException {
     protected RecorderException(String msg) {
        super(msg);
    }
}

// End RecorderException.java
