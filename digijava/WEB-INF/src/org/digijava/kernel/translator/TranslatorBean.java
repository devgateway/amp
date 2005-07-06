/*
 *   TranslatorBean.java
 * 	 @Author Shamanth Murthy shamanth.murthy@mphasis.com
 *   Created:
 *   CVS-ID: $Id: TranslatorBean.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.translator;
/**
 * @author shamanth.murthy
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 *
 * This class maps to message_lang table
 * @hibernate.class
 * table="ep_cpv_cats_en"
 * dynamic-update="true"
 */

import org.digijava.kernel.entity.Message;

public class TranslatorBean implements java.io.Serializable{

	Message srcMsg = new Message();
	Message tragetMsg = null;
	boolean needsUpdate = false;

	/**
	 *
	 */
	public TranslatorBean() {
		super();

	}

	public TranslatorBean(Message srcMsg, Message tarMsg) {

			this.srcMsg = srcMsg;
			this.tragetMsg = tarMsg;
		}

	public TranslatorBean(Message srcMsg, Message tarMsg, boolean needsUpdate) {

			this.srcMsg = srcMsg;
			this.tragetMsg = tarMsg;
			this.needsUpdate = needsUpdate;
		}

	/**
	 * @return
	 */
	public Message getSrcMsg() {
		return this.srcMsg;
	}

	/**
	 * @return
	 */
	public Message getTragetMsg() {
		return this.tragetMsg;
	}

	/* * @param message
	 */
	public void setSrcMsg(Message message) {
		srcMsg = message;
	}

	/**
	 * @param message
	 */
	public void setTragetMsg(Message message) {
		tragetMsg = message;
	}

	public boolean isNeedsUpdate(){
		return needsUpdate;
	}

	public void setNeedsUpdate(boolean value){
		this.needsUpdate = value;
	}

}