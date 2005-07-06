/*
 *   ConfigListener.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ConfigListener.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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

package org.digijava.kernel.startup;

import org.digijava.kernel.config.DigiConfig;

/**
 * @todo COMMENT HERE
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface ConfigListener {

    public void initialize(DigiConfig config);

    public void destroy();
}

