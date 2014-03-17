package org.digijava.module.fundingpledges.action;

import org.digijava.module.aim.helper.KeyValue;

/**
 * class holding a KeyValue and a boolean which either enabled or disabled it
 * Used for disabling / filtering out values in SELECT boxes
 * @author Dolghier Constantin
 *
 */
public class DisableableKeyValue
{
	private boolean enabled;
	private KeyValue keyValue;
	
	public DisableableKeyValue(KeyValue keyValue, boolean enabled)
	{
		this.keyValue = keyValue;
		this.enabled = enabled;
	}
	
	public KeyValue getKeyValue()
	{
		return keyValue;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public Long getKeyAsLong()
	{
		return keyValue == null ? null : keyValue.getKeyAsLong();
	}
	
	@Override
	public String toString()
	{
		return String.format("%s: %s", this.enabled ? "enabled" : "disabled", this.keyValue);
	}
}
