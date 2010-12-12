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
import java.util.TreeSet;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public class AmpSectorSearchModel extends
		AbstractAmpAutoCompleteModel<AmpSector> {

	public enum PARAM implements AmpAutoCompleteModelParam {
		SECTOR_SCHEME
	};

	public AmpSectorSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 8211300754918658832L;
	private Session session;

	
	@Override
	protected List<AmpSector> load() {
		try {
			List<AmpSector> ret = new ArrayList<AmpSector>();
			session = PersistenceManager.getSession();
			Query query = session
					.createQuery("from "
							+ AmpSector.class.getName()
							+ " o WHERE o.name like :name AND o.ampSecSchemeId=:scheme ORDER BY o.name");
			Integer maxResults = (Integer) getParams().get(
					AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
			AmpSectorScheme scheme = (AmpSectorScheme) getParams().get(
					PARAM.SECTOR_SCHEME);
			if (maxResults != null && maxResults != 0)
				query.setMaxResults(maxResults);
			query.setString("name", "%" + input + "%");
			query.setParameter("scheme", scheme);
			List<AmpSector> list = query.list();
			
			ret=createTreeView(list);
			
			session.close();
			return ret;
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
