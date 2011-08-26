/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
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
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpMEIndicatorSearchModel extends
		AbstractAmpAutoCompleteModel<AmpIndicator> {

	public AmpMEIndicatorSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 8211300754918658832L;
	private Session session;

	
	@Override
	protected ArrayList<AmpIndicator> load() {
		try {
			ArrayList<AmpIndicator> ret = new ArrayList<AmpIndicator>();
			session = PersistenceManager.getSession();
			Query query = session
					.createQuery("from "
							+ AmpIndicator.class.getName()
							+ " o");
			Integer maxResults = (Integer) getParams().get(
					AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
			List<AmpSector> list = query.list();
			//session.close();
			return new ArrayList(list);
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
