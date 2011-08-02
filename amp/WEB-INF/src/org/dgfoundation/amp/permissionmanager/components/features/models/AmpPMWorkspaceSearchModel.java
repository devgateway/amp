/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel.PARAM;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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
		List<AmpTeam> ret = null;
		try {
			
			session = PersistenceManager.getRequestDBSession();
			Criteria crit = session.createCriteria(AmpTeam.class);
			crit.setCacheable(true);
				
			if(input.trim().length()>0){
					crit.add(Restrictions.disjunction()
							.add(Restrictions.ilike("name", "%" + input + "%")))
							.addOrder(Order.asc("name"));
		
					if (params != null) {
						Integer maxResults = (Integer) getParams().get(
								PARAM.MAX_RESULTS);
						if (maxResults != null && maxResults.intValue() != 0)
							crit.setMaxResults(maxResults);
					}
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
