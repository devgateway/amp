/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * @author mpostelnicu@dgateway.org since Oct 13, 2010
 */
public class AmpLocationSearchModel
		extends
		AbstractAmpAutoCompleteModel<AmpCategoryValueLocations> {
	
	public enum PARAM implements AmpAutoCompleteModelParam { LAYER } ;
	
	public AmpLocationSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
	}

	private static final long serialVersionUID = -1967371789152747599L;

	@Override
	protected List<AmpCategoryValueLocations> load() {
		List<AmpCategoryValueLocations> ret = new ArrayList<AmpCategoryValueLocations>();
		IModel<Set<AmpCategoryValue>> layerModel = (IModel<Set<AmpCategoryValue>>) getParam(PARAM.LAYER);
		if(layerModel==null || layerModel.getObject().size()<1) return ret;
		AmpCategoryValue cvLayer = layerModel.getObject().iterator().next();
		Integer maxResults = (Integer) getParam(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
		
		Set<AmpCategoryValueLocations> locationsByLayer = DynLocationManagerUtil
				.getLocationsByLayer(cvLayer);
		for (Iterator<AmpCategoryValueLocations> iterator = locationsByLayer
				.iterator(); iterator.hasNext();) {
			AmpCategoryValueLocations val = (AmpCategoryValueLocations) iterator
					.next();
			if (val.getName().toLowerCase().indexOf(input.toLowerCase()) != -1)
				ret.add(val);
			if (maxResults!=0 && ret.size() >= maxResults)
				break;
		}
		return ret;
	}

}
