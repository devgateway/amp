/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author dan
 *
 */
public class AmpPMWorkspaceSearchModel extends AbstractAmpAutoCompleteModel<AmpTeam> {

	private Session session;
	
	/**
	 * @param input
	 * @param params
	 */
	public AmpPMWorkspaceSearchModel(String input, Map params) {
		super(input, params);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected List<AmpTeam> load() {
		try {
			List<AmpTeam> ret = new ArrayList<AmpTeam>();
			session = PersistenceManager.getSession();
			Query query = session.createQuery("from "+ AmpTeam.class.getName()+ " o WHERE o.name like :name ORDER BY o.name");
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
