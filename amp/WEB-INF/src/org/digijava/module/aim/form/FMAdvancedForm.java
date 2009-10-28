package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.helper.FMAdvancedDisplay;

import java.util.*;

public class FMAdvancedForm extends ActionForm {
	
	private LinkedList<FMAdvancedDisplay> fmeList = new LinkedList<FMAdvancedDisplay>();
	
	private int paddingOffset = 0; 
	
	
	public LinkedList<FMAdvancedDisplay> getFmeList() {
		return fmeList;
	}

	public void setFmeList(LinkedList<FMAdvancedDisplay> fmeList) {
		this.fmeList = fmeList;
	}

	public int getPaddingOffset() {
		return paddingOffset;
	}

	public void setPaddingOffset(int paddingOffset) {
		this.paddingOffset = paddingOffset;
	}

	public FMAdvancedDisplay createFMAdvancedDisplay(Long id, String name, String type, boolean visible , int paddingLeft){
		return new FMAdvancedDisplay(id, name, type, visible , paddingLeft);
	}

}
