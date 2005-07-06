/*
 *   DigiMessageResourcesFactory.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created:
 * 	 CVS-ID: $Id: DigiMessageResourcesFactory.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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

package org.digijava.kernel.util;

import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.MessageResources;

import org.apache.log4j.Logger;

/**
 *
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DigiMessageResourcesFactory extends MessageResourcesFactory {

    private static Logger logger = Logger.getLogger(DigiMessageResourcesFactory.class);

    // --------------------------------------------------------- Public Methods



    public MessageResources createResources(String config) {

        return new DigiMessageResources(this, config, this.returnNull);

    }


}
