/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

/**
 * @author aartimon@dginternational.org
 * @since Mar 31, 2011
 */
public class AmpPIOrganisationSearchModel extends
		AbstractAmpAutoCompleteModel<AmpOrganisation> {

	public AmpPIOrganisationSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
	}

	private static final long serialVersionUID = 8211300754918658812L;
	private Session session;

	@Override
	protected List<AmpOrganisation> load() {
		List<AmpOrganisation> ret = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			Integer maxResults = (Integer) getParams().get(PARAM.MAX_RESULTS);
			Criteria crit = session.createCriteria(AmpOrganisation.class);
			crit.setCacheable(true);

			Junction junction = Restrictions
					.conjunction()
					.add(Restrictions.disjunction()
							.add(getTextCriterion("name", input))
							.add(getTextCriterion("acronym", input)))
					.add(Restrictions
							.disjunction()
							.add(Restrictions.like(
									"orgGrpId.orgType.orgTypeCode", "BIL"))
							.add(Restrictions.like(
									"orgGrpId.orgType.orgTypeCode", "MUL")));

			crit.add(junction);
			if (maxResults != null)
				crit.setMaxResults(maxResults);

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
