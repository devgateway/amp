/*
*   Count.java
*   @Author Arvind Kumar S
*   Created: Apr 9, 2003
*   CVS-ID: $Id: Count.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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

/**
 * This class can be used as a Value Object for
 * sending the count of any entity.
*/
public class Count implements java.io.Serializable {

	/**
	* Stores the count
	*/
	private long count;

	public Count() {
	}

	public Count(long count) {
		this.count = count;
	}

	/**
	 * Gets the count value
	 * @return The count
	*/
	public long getCount() {
		return this.count;
	}

	/**
	 * Sets the count value
	 * @param count The new Count value
	*/

	public void setCount(long count) {
		this.count = count;
	}

	public String toString() {
		return "Count :" + count;
	}
}
