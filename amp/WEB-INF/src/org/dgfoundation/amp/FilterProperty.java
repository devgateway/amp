package org.dgfoundation.amp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class FilterProperty {

	String name;
	String description;
	Object value;
	PropertyDescPosition position;
	boolean showOnlyIfValueIsTrue;
	boolean hiddenValue;

	public boolean isHiddenValue() {
		return hiddenValue;
	}

	boolean showDescription;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		if (description == null) {
			return this.name;
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getValue() {

		if (hiddenValue)
			return "";

		if (value instanceof String) {
			String rt = (String) value;

			if (this.name.equalsIgnoreCase("approvalStatusSelected"))
				return getApprovalStatusValue((String)value);

			return rt.substring(0, 1).toUpperCase() + rt.substring(1);
		}
		if (value instanceof Boolean) {
			Boolean rt = (Boolean) value;
			if (rt)
				return "True";
			else
				return "False";
		}

		if (value instanceof Integer) {
			System.err.println("integer");
		}

		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public PropertyDescPosition getPosition() {
		return position;
	}

	public void setPosition(PropertyDescPosition position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return (value != null) ? value.toString() : "";
	}

	public void setShowOnlyIfValueIsTrue(boolean showOnlyIfValueIsTrue) {
		this.showOnlyIfValueIsTrue = showOnlyIfValueIsTrue;
	}

	public void setHiddenValue(boolean hiddenValue) {
		this.hiddenValue = hiddenValue;
	}

	public boolean isShowDescription() {
		if (this.description == null) {
			return true;
		}
		if ((value instanceof Boolean) && (showOnlyIfValueIsTrue)) {
			Boolean val = (Boolean) this.value;
			return val;
		}
		if (this.description.equalsIgnoreCase("")) {
			return false;
		}
		return true;

	}

	private String getApprovalStatusValue(String values) {
		String[] val;
		StringBuffer returnVal = new StringBuffer();
		if (values.indexOf(",") > -1) {
			val = values.split(",");
		} else {
			val = new String[] { values };
		}

		for (int i = 0; i < val.length; i++) {
			String string = val[i];
			if (returnVal.length() > 0) {
				returnVal.append(", ");
			}
			switch (Integer.parseInt(string.trim())) {
			case -1:
				returnVal.append("All");
				break;
			case 1:
				returnVal.append("New Draft");
				break;
			case 2:
				returnVal.append("New Un-validated");
				break;
			case 4:
				returnVal.append("Validated Activities");
				break;
			case 3:
				returnVal.append("Existing Draf");
				break;
			case 0:
				returnVal.append("Existing Un-validated");
				break;
			}
		}
		return returnVal.toString();

	}
}
