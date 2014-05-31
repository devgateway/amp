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
import org.hibernate.FetchMode;
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

	public AmpOrganisationSearchModel(String input,String language,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, language, params);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 8211300754918658832L;
	private Session session;

	@Override
	protected List<AmpOrganisation> load() {
		List<AmpOrganisation> ret = null;
		try {

			session = PersistenceManager.getRequestDBSession();
			Criteria crit = session.createCriteria(AmpOrganisation.class, "org");
			crit.setCacheable(true);
			
			crit.add(Restrictions.or( Restrictions.isNull("org.deleted"), Restrictions.eq( "org.deleted", Boolean.FALSE)));
			crit.addOrder(Order.asc("org.name"));
			
			if (input.trim().length() > 0)
				crit.add(Restrictions.disjunction()
							.add(getTextCriterion("org.name", input))
							.add(getTextCriterion("org.acronym", input)));
			
			if (params != null){
				
				if (getParams().get(PARAM.GROUP_FILTER) != null){
					crit.add(Restrictions.conjunction().add(Restrictions.eq("org.orgGrpId", getParams().get(PARAM.GROUP_FILTER))));
				}

				Integer maxResults = (Integer) getParams().get(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
				if (maxResults != null && maxResults.intValue() != 0){
					crit.setMaxResults(maxResults);
				}
				if (getParams().get(PARAM.TYPE_FILTER) != null){
					crit.setFetchMode("org.orgGrpId", FetchMode.JOIN).createAlias("org.orgGrpId", "orgGrp").setFetchMode("orgGrp.orgType", FetchMode.JOIN);//.createAlias("orgGrp.orgType", "orgTp");
					crit.add(Restrictions.conjunction().add(Restrictions.eq("orgGrp.orgType", getParams().get(PARAM.TYPE_FILTER))));
				}
			}
			ret = crit.list();

		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (DgException e){
			throw new RuntimeException(e);
		}

		return ret;
	}

}
