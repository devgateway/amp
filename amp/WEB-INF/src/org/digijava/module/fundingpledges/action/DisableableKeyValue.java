package org.digijava.module.fundingpledges.action;

import org.digijava.module.aim.helper.KeyValue;

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
}
