/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.io.Serializable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

/**
 * Paris Indicator Question Answer Model
 * that will store the choice of the user as 
 * "yes" or "no" in the underlying object
 * 
 * @author aartimon@dginternational.org
 * @since Mar 31, 2011
 */
public class PIYesNoAnswerModel extends Model {
	private IModel sourceModel;
	
	public PIYesNoAnswerModel(IModel sourceModel) {
		this.sourceModel = sourceModel;
	}
	
	@Override
	public void setObject(Serializable object) {
		if (object == null){
			sourceModel.setObject(null);
		}
		else
			sourceModel.setObject(((String)object).toLowerCase());
	}
	
	@Override
	public Serializable getObject() {
		
		String val = (String) sourceModel.getObject();
		
		if (val == null)
			return null;
		else
			return Strings.capitalize(val);
	}
}
