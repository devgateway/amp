/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author dan
 *
 */
public class AmpPMObjectVisibilitySearchModel extends AbstractAmpAutoCompleteModel<AmpObjectVisibility> {

	private Session session;
	
	/**
	 * @param input
	 * @param params
	 */
	public AmpPMObjectVisibilitySearchModel(String input, Map params) {
		super(input, params);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected List<AmpObjectVisibility> load() {
		try {
			List<AmpObjectVisibility> ret = new ArrayList<AmpObjectVisibility>();
			session = PersistenceManager.getSession();
			Query query = session.createQuery("from "+ AmpModulesVisibility.class.getName()+ " o WHERE o.name like :name ORDER BY o.name");
			Integer maxResults = (Integer) getParams().get(PARAM.MAX_RESULTS);
			if (maxResults != null)
				query.setMaxResults(maxResults);
			query.setString("name", "%" + input + "%");
			ret = query.list();
			
			query = session.createQuery("from "+ AmpFeaturesVisibility.class.getName()+ " o WHERE o.name like :name ORDER BY o.name");
			if (maxResults != null)
				query.setMaxResults(maxResults);
			query.setString("name", "%" + input + "%");
			ret.addAll(query.list());
			
			query = session.createQuery("from "+ AmpFieldsVisibility.class.getName()+ " o WHERE o.name like :name ORDER BY o.name");
			if (maxResults != null)
				query.setMaxResults(maxResults);
			query.setString("name", "%" + input + "%");
			ret.addAll(query.list());
			
			session.close();
			return ret;
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
