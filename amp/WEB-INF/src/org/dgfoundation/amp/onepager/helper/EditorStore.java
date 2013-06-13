package org.dgfoundation.amp.onepager.helper;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Store for editors to help with propagating the link between old keys and new keys + values for new keys
 * This is useful for versioning, to be able to clone the translations.
 *
 * @author aartimon@developmentgateway.org
 */
public class EditorStore implements Serializable{
	//map between new editor key and old editor key, in order to be able to copy values in other languages
	private HashMap<String, String> oldKey;
	//map between new editor key and editor body from the form
	private HashMap<String, String> values;
	
	public EditorStore() {
		oldKey = new HashMap<String, String>();
		values = new HashMap<String, String>();
	}

	public HashMap<String, String> getOldKey() {
		return oldKey;
	}
	public void setOldKey(HashMap<String, String> oldKey) {
		this.oldKey = oldKey;
	}
	public HashMap<String, String> getValues() {
		return values;
	}
	public void setValues(HashMap<String, String> values) {
		this.values = values;
	}
}
