package org.dgfoundation.amp.ar.viewfetcher;

import java.util.*;

/**
 * description of an i18n view: contains a description of all the columns which should be overridden by the translation
 * @author Dolghier Constantin
 *
 */
public class I18nViewDescription {
	
	public final String viewName;
	protected final Map<String, I18nViewColumnDescription> columns = new HashMap<String, I18nViewColumnDescription>();
	
	public I18nViewDescription(String viewName)
	{
		this.viewName = viewName;
	}
	
	public I18nViewDescription addColumnDef(I18nViewColumnDescription column)
	{
		this.columns.put(column.columnName, column);
		return this;
	}
	
	/**
	 * returns the view description or null, of none found
	 * @param columnName
	 * @return
	 */
	public I18nViewColumnDescription getColumnDescription(String columnName)
	{
		return columns.get(columnName);
	}
	
	/**
	 * clones a view
	 * @param newViewName
	 * @return
	 */
	public I18nViewDescription cloneView(String newViewName)
	{
		I18nViewDescription res = new I18nViewDescription(newViewName);
		res.columns.putAll(this.columns); // I18nViewColumnDescription is immutable, so this is a safe way to copy
		return res;
	}
}
