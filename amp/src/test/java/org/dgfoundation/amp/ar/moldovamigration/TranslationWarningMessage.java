package org.dgfoundation.amp.ar.moldovamigration;

import org.dgfoundation.amp.ar.viewfetcher.InternationalizedPropertyDescription;

public class TranslationWarningMessage 
{
	public final InternationalizedPropertyDescription property;
	public final String locale;
	public final String errMessage;
	
	public TranslationWarningMessage(InternationalizedPropertyDescription property, String locale, String errMessage)
	{
		this.property = property;
		this.locale = locale;
		this.errMessage = errMessage;
	}
	
	@Override
	public String toString()
	{
		return String.format("property %s of entity %s in language %s %s", property.propertyName, property.className.substring(property.className.lastIndexOf('.') + 1), locale, errMessage);
	}
}
