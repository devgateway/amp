/*
*   digiStartUpServlet.java
*   @Author Ranjith Gopinath ranjith.gopinath@mphasis.com
*   Created:
*   CVS-ID: $Id: digiStartUpServlet.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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

/**
* File Name : digiStartUpServlet.java
*
* Purpose   : This startUp servlet class extends HttpServlet
* 			  to load the Hibernate's mapping file as a part of
* 			  StartUp servlet
*
*
*
* @version  :    0.1
*
* @author   :  Ranjith Gopinath,  July , 2003
*
*
*
* Copyright (c) 2001-2002 Joint Property of WorldBank and MphasiS-BFL
*
* MphasiS BFL Limited
* 139/1 Hosur Road, Koramangala
* Bangalore 560 095
*
*
*
*
* All Rights Reserved.
*
* <P> This software represents confidential and proprietary information of
* WorldBank and MphasiS-BFL. You shall not disclose such confidential
* Information and shall use it only in accordance with the terms of
* the license agreement you entered into with WorldBank and MphasiS-BFL.
*
* MODIFICATION DESCRIPTION:
*
* REVISION HISTORY:
* <PRE>
* S. No.      Name        Initials        Date        Bug          Fix No. Description
*
* </PRE>
*/


package org.digijava.kernel.startup;


//System Imports
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

// Application Imports





public class digiStartUpServlet extends HttpServlet{

	/* Default init method of the Servlet
	 * The Hibernate mapping files will be loaded as a part of the init method.
	 */

public void init( ServletConfig o_ServletConfig )throws ServletException
{
	super.init(o_ServletConfig );



}
public void doPost( HttpServletRequest o_request, HttpServletResponse o_response) throws ServletException
{
}




public void doGet( HttpServletRequest o_request, HttpServletResponse o_response) throws ServletException
{

	doPost(o_request,o_response);
}

}// End of the digiStartUpServletClass