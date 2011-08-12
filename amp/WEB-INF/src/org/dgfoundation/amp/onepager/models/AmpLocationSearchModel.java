/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

/**
 * @author mpostelnicu@dgateway.org since Oct 13, 2010
 */
public class AmpLocationSearchModel extends
		AbstractAmpAutoCompleteModel<AmpCategoryValueLocations> {

	public enum PARAM implements AmpAutoCompleteModelParam {
		LAYER
	};

	public AmpLocationSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
	}

	private static final long serialVersionUID = -1967371789152747599L;

	@Override
	protected List<AmpCategoryValueLocations> load() {
		List<AmpCategoryValueLocations> ret = new ArrayList<AmpCategoryValueLocations>();
		IModel<Set<AmpCategoryValue>> layerModel = (IModel<Set<AmpCategoryValue>>) getParam(PARAM.LAYER);
		if (layerModel == null || layerModel.getObject().size() < 1)
			return ret;
		AmpCategoryValue cvLayer = layerModel.getObject().iterator().next();
		Integer maxResults = (Integer) getParam(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);

		Session dbSession = null;
		try {
			dbSession = PersistenceManager.getRequestDBSession();

			Criteria crit = dbSession
					.createCriteria(AmpCategoryValueLocations.class);
			crit.setCacheable(true);
			Junction junction = Restrictions.conjunction().add(
					Restrictions.eq("parentCategoryValue", cvLayer));
			if (input.trim().length() > 0)
				junction.add(getTextCriterion("name", input));
			crit.add(junction);
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
