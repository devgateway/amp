/*
*   DependencyWalker.java
*   @Author Mikheil Kapanadze mikheil@digijava.org
*   Created: Apr 10, 2004
*   CVS-ID: $Id: DependencyWalker.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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
package org.digijava.kernel.viewmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DependencyWalker {
    Map files;

    public DependencyWalker() {
        files = Collections.synchronizedMap( new HashMap());
    }

    public void clear() {
        files.clear();
    }

    public void addFile(File file) {
        files.put(file.getAbsolutePath(), new Long(file.lastModified()));
    }

    public void addFile(String fileName) {
        File file = new File(fileName);
        addFile(file);
    }

    public Collection getFiles() {
        ArrayList result = new ArrayList();

        Iterator iter = files.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry)iter.next();
            result.add(new File((String)item.getKey()));
        }

        return result;
    }

    public boolean isUpToDate() {
        Iterator iter = files.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry)iter.next();
            File file = new File((String)item.getKey());
            Long timestamp = (Long)item.getValue();

            if (file.lastModified() != timestamp.longValue()) {
                return false;
            }
        }

        return true;
    }
}