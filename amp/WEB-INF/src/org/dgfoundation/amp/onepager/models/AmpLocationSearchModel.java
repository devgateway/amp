/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author mpostelnicu@dgateway.org since Oct 13, 2010
 */
public class AmpLocationSearchModel extends
		AbstractAmpAutoCompleteModel<AmpCategoryValueLocations> {

	public static final String PARENT_DELIMITER="\\] \\[";
	
	public enum PARAM implements AmpAutoCompleteModelParam {
		LAYER, LEVEL
	};

	public AmpLocationSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
	}

	private static final long serialVersionUID = -1967371789152747599L;

	@Override
	protected Collection<AmpCategoryValueLocations> load() {
		List<AmpCategoryValueLocations> ret = new ArrayList<AmpCategoryValueLocations>();
		IModel<Set<AmpCategoryValue>> layerModel = (IModel<Set<AmpCategoryValue>>) getParam(PARAM.LAYER);
		IModel<Set<AmpCategoryValue>> levelModel = (IModel<Set<AmpCategoryValue>>) getParam(PARAM.LEVEL);
		if (layerModel == null || layerModel.getObject().size() < 1 || levelModel==null || levelModel.getObject().size()<1)
			return ret;
		AmpCategoryValue cvLayer = layerModel.getObject().iterator().next();
		AmpCategoryValue cvLevel= levelModel.getObject().iterator().next();
		
		if (!CategoryManagerUtil.equalsCategoryValue(cvLevel,
				CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL)
				&& CategoryManagerUtil.equalsCategoryValue(cvLayer,
						CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY)) {
			// then we can only return the current default country of the system
			try {
				AmpCategoryValueLocations defCountry = DynLocationManagerUtil
						.getLocationByIso(
								FeaturesUtil.getDefaultCountryIso(),
								CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
				ret = new ArrayList<AmpCategoryValueLocations>();
				ret.add(defCountry);
				return ret;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
		
		Integer maxResults = (Integer) getParam(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);

		Session dbSession = null;
		try {
			dbSession = PersistenceManager.getRequestDBSession();

			Criteria crit = dbSession
					.createCriteria(AmpCategoryValueLocations.class);
			crit.setCacheable(true);
			Junction junction = Restrictions.conjunction().add(
					Restrictions.eq("parentCategoryValue", cvLayer));
			if (input.trim().length() > 0) {
				if(isExactMatch()) {
					String[] strings = input.split(PARENT_DELIMITER);
					
					if(strings.length>1) {					
						String locName = strings[strings.length-1].substring(0,strings[strings.length-1].length()-2);
						junction.add( Restrictions.eq("name",locName));
						String parentName=null;
						if(strings.length==2)
							parentName = strings[0].substring(1);
						else
							parentName=strings[strings.length-2];
						crit.createCriteria("parentLocation").add(Restrictions.eq("name", parentName));
					} else {
						junction.add(Restrictions.eq("name", input.substring(1, input.length()-2)));
					}
					
					
				} else junction.add(getTextCriterion("name", input));
			}
			crit.add(junction);
			crit.addOrder(Order.asc("name"));
			if (maxResults != null && maxResults != 0)
				crit.setMaxResults(maxResults);
			ret = crit.list();

		} catch (DgException e) {
			// TODO Auto-genrated catch block
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
	}

}
