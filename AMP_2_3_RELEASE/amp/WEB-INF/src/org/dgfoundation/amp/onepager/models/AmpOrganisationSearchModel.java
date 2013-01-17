/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public class AmpOrganisationSearchModel extends
		AbstractAmpAutoCompleteModel<AmpOrganisation> {
	private static final Logger logger = LoggerFactory.getLogger(AmpOrganisationSearchModel.class);
	public enum PARAM implements AmpAutoCompleteModelParam {
		TYPE_FILTER, GROUP_FILTER
	};

	public AmpOrganisationSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 8211300754918658832L;
	private Session session;

	@Override
	protected List<AmpOrganisation> load() {
		List<AmpOrganisation> ret = null;
		try {

			session = PersistenceManager.getRequestDBSession();
			Criteria crit = session.createCriteria(AmpOrganisation.class);
			crit.setCacheable(true);
			if (input.trim().length() > 0)
				crit.add(
						Restrictions.disjunction()
								.add(getTextCriterion("name", input))
								.add(getTextCriterion("acronym", input)));	
			if(params != null && getParams().get(PARAM.GROUP_FILTER) !=null) 
				crit.add(Restrictions.conjunction().add(Restrictions.eq("orgGrpId", getParams().get(PARAM.GROUP_FILTER))));
			crit.add(Restrictions.or( Restrictions.isNull("deleted"), Restrictions.eq( "deleted", null )));
			crit.addOrder(Order.asc("name"));

			if (params != null) {
				Integer maxResults = (Integer) getParams().get(
						AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
				if (maxResults != null && maxResults.intValue() != 0)
					crit.setMaxResults(maxResults);
			}
			ret = crit.list();

		} catch (HibernateException e) {
			logger.error("Organization " +input + " not found.");
			throw new RuntimeException(e);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(session);
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
