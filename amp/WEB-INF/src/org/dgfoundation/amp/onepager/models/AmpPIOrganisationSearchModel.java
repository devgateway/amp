/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

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
		try {
			List<AmpOrganisation> ret = null;
			session = PersistenceManager.getSession();
			Query query = session.createQuery("from "
					+ AmpOrganisation.class.getName()
					+ " o WHERE (o.name like :name OR o.acronym like :name) and (o.orgGrpId.orgType.orgTypeCode like 'BIL' or o.orgGrpId.orgType.orgTypeCode like 'MUL')");
			
			Integer maxResults = (Integer) getParams().get(PARAM.MAX_RESULTS);
			if (maxResults != null)
				query.setMaxResults(maxResults);
			query.setString("name", "%" + input + "%");
			ret = query.list();
			session.close();
			return ret;
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
