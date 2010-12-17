/**
 * 
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel.PARAM;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author dan
 *
 */
public class AmpContactSearchModel extends AbstractAmpAutoCompleteModel<AmpContact> {

	private Session session;
	
	/**
	 * @param input
	 * @param params
	 */
	public AmpContactSearchModel(String input, Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected List<AmpContact> load() {
		try {
			List<AmpContact> ret = new ArrayList<AmpContact>();
			session = PersistenceManager.getSession();
			Query query = session.createQuery("from "+ AmpContact.class.getName()+ " o WHERE o.name like :name OR o.lastname like :name ORDER BY o.name");
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
