package org.dgfoundation.amp.ecs.common;

import java.io.Serializable;

/**
 * 
 * @author Arty
 *
 */
public class ECSParameters implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int synchronizeDelay;
	private boolean runOnceCustom;
	
	
	public int getSynchronizeDelay() {
		return synchronizeDelay;
	}
	public void setSynchronizeDelay(int synchronizeDelay) {
		this.synchronizeDelay = synchronizeDelay;
	}
	public boolean isRunOnceCustom() {
		return runOnceCustom;
	}
	public void setRunOnceCustom(boolean runOnceCustom) {
		this.runOnceCustom = runOnceCustom;
	}
}
