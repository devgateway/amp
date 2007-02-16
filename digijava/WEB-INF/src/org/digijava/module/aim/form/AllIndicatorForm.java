package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.Collection;

public class AllIndicatorForm extends ActionForm 
{
	private Collection prgIndicators;
	private Collection projIndicators;
	private boolean flag;
	/**
	 * @return Returns the prgIndicators.
	 */
	public Collection getPrgIndicators() {
		return prgIndicators;
	}
	/**
	 * @param prgIndicators The prgIndicators to set.
	 */
	public void setPrgIndicators(Collection prgIndicators) {
		this.prgIndicators = prgIndicators;
	}
	/**
	 * @return Returns the projIndicators.
	 */
	public Collection getProjIndicators() {
		return projIndicators;
	}
	/**
	 * @param projIndicators The projIndicators to set.
	 */
	public void setProjIndicators(Collection projIndicators) {
		this.projIndicators = projIndicators;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}