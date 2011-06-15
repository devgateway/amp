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
import org.hibernate.criterion.Restrictions;

/**
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public class AmpOrganisationSearchModel extends
		AbstractAmpAutoCompleteModel<AmpOrganisation> {

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
			if(input.trim().length()>0)
			crit.add(Restrictions.disjunction()
					.add(Restrictions.ilike("name", "%" + input + "%"))
					.add(Restrictions.ilike("acronym", "%" + input + "%")));

			if (params != null) {
				Integer maxResults = (Integer) getParams().get(
						PARAM.MAX_RESULTS);
				if (maxResults != null && maxResults.intValue() != 0)
					crit.setMaxResults(maxResults);
			}
			ret = crit.list();
			
		} catch (HibernateException e) {
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
