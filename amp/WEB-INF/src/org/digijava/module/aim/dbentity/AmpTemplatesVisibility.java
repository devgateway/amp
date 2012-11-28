/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.*;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class AmpTemplatesVisibility extends AmpObjectVisibility implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4765301740400470276L;
	/**
	 * 
	 */
	private Set features;
	private Set<AmpFieldsVisibility> fields;
	private String visible;
	
	public Set getFeatures() {
		return features;
	}

	public void setFeatures(Set features) {
		this.features = features;
	}

	public Set<AmpFieldsVisibility> getFields() {
		return fields;
	}

	public void setFields(Set<AmpFieldsVisibility> fields) 
	{
		namesCache = null;
		this.fields = fields;
	}

	public AmpObjectVisibility getParent() {
		// TODO Auto-generated method stub
		//if(getVisible()) return this;
 		return this;
	}

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		AmpTemplatesVisibility obj=(AmpTemplatesVisibility) arg0;
		
		return this.getId().compareTo(obj.getId());
		//return 0;
	}

	public String getVisible() {
			return visible;
	}

	public AmpTemplatesVisibility getTemplate() {
		return this;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
	
	public boolean isDefault(){
		return this.getId().equals(FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.VISIBILITY_TEMPLATE));
	}

	@Override
	public String[] getImplementedActions() {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public Class getPermissibleCategory() {
	    return AmpTemplatesVisibility.class;

	}
	
	/**
	 * fields indexed by name, for easy lookup: we don't want to iterate through 450 items for each and every of the 220 columns
	 */
	private transient Map<String, AmpFieldsVisibility> namesCache = null;
	private void buildNamesCache()
	{
		namesCache = new HashMap<String, AmpFieldsVisibility>();
		for(AmpFieldsVisibility vis:getFields())
			namesCache.put(vis.getName(), vis);
	}
	
	/**
	 * returns true iff a field with a given name exists
	 * @param name
	 * @return
	 */
	public boolean fieldExists(String name)
	{
		if (namesCache == null)
			buildNamesCache();
		return namesCache.containsKey(name);
	}
	
}