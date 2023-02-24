/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

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

    private static final long serialVersionUID = 1L;
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
