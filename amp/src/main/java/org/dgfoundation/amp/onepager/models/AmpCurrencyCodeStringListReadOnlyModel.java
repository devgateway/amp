package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

public class AmpCurrencyCodeStringListReadOnlyModel extends AbstractReadOnlyModel<List<String>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9099736891908405978L;

	@Override
	public List<String> getObject() {
		List<AmpCurrency> tmp = (List<AmpCurrency>) CurrencyUtil.getActiveAmpCurrencyByCode();
		ArrayList<String> ret = new ArrayList<String>();

		Iterator<AmpCurrency> it = tmp.iterator();
		while (it.hasNext()) {
			AmpCurrency c = (AmpCurrency) it.next();
			ret.add(c.getCurrencyCode());
		}
		return ret;
	}

}
