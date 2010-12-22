/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpAutoCompleteModelParam;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel.PARAM;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpContact;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author dan
 *
 */
public class AmpPMUserSearchModel extends AbstractAmpAutoCompleteModel<User> {

	private Session session;
	
	public AmpPMUserSearchModel(String input, Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected List<User> load() {
		try {
			List<User> ret = new ArrayList<User>();
			session = PersistenceManager.getSession();
			Query query = session.createQuery("from "+ User.class.getName()+ " o WHERE o.firstNames like :name OR o.lastName like :name OR o.email like :name ORDER BY o.firstNames");
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
