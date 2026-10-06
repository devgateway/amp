package org.digijava.module.fundingpledges.action;

import org.digijava.module.aim.helper.KeyValue;

/**
 * class holding a KeyValue and a boolean which either enabled or disabled it
 * Used for disabling / filtering out values in SELECT boxes
 * @author Dolghier Constantin
 *
 */
public class DisableableKeyValue implements Comparable<DisableableKeyValue>
{
    private boolean enabled;
    private KeyValue keyValue;
    
    public DisableableKeyValue(KeyValue keyValue, boolean enabled)
    {
        this.keyValue = keyValue;
        this.enabled = enabled;
    }
    
    public DisableableKeyValue(String key, String value, boolean enabled)
    {
        this(new KeyValue(key, value), enabled);
    }
    
    public DisableableKeyValue(Long key, String value, boolean enabled)
    {
        this(new KeyValue(key, value), enabled);
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
    public int compareTo(DisableableKeyValue oth){
        int res = this.keyValue.compareTo(oth.keyValue);
        if (res == 0)
            return Boolean.valueOf(this.enabled).compareTo(Boolean.valueOf(oth.enabled));
        return res;
    }
    
    @Override
    public boolean equals(Object oth){
        return this.compareTo((DisableableKeyValue) oth) == 0;
    }
    
    @Override public int hashCode()
    {
        return this.keyValue.hashCode() * 3 + Boolean.valueOf(this.enabled).hashCode();
    }
    
    @Override
    public String toString()
    {
        return String.format("%s: %s", this.enabled ? "enabled" : "disabled", this.keyValue);
    }
}
