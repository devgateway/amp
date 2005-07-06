/*
*   EnumerationWrapper.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: EnumerationWrapper.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
*
*   This file is part of DiGi project (www.digijava.org).
*   DiGi is a multi-site portal system written in Java/J2EE.
*
*   Confidential and Proprietary, Subject to the Non-Disclosure
*   Agreement, Version 1.0, between the Development Gateway
*   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
*   Gateway Foundation, Inc.
*
*   Unauthorized Disclosure Prohibited.
*
*************************************************************************/

package org.digijava.kernel.request;

import java.util.Enumeration;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class EnumerationWrapper implements Enumeration {

    private Iterator interator;

    public EnumerationWrapper( Set set ) {

        if( set == null )
            set = new HashSet();

            this.interator = set.iterator();
    }
    public boolean hasMoreElements() {
        return this.interator.hasNext();
    }
    public Object nextElement() {
        return this.interator.next();
    }

}