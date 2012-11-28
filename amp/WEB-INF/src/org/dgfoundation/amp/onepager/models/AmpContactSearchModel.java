/**
 * 
 */
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpContact;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author dan
 * 
 */
public class AmpContactSearchModel extends
		AbstractAmpAutoCompleteModel<AmpContact> {

	private Session session;

	/**
	 * @param input
	 * @param params
	 */
	public AmpContactSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected Collection<AmpContact> load() {
		try {
			session = AmpActivityModel.getHibernateSession();
			Criteria crit = session.createCriteria(AmpContact.class);
			crit.setCacheable(true);
			if (input.trim().length() > 0) {
				crit.add(
						Restrictions.disjunction().add(
								getTextCriterion("fullname", input)));
			}
			crit.addOrder(Order.asc("name"));
			Integer maxResults = (Integer) getParams().get(PARAM.MAX_RESULTS);
			if (maxResults != null && maxResults.intValue() != 0) {
				crit.setMaxResults(maxResults);
			}

			List<AmpContact> ret = null;
			ret = crit.list();
			if (ret == null) {
				ret = new ArrayList<AmpContact>();
			}
			AmpContact newContact = new AmpContact();
			newContact.setName(TranslatorUtil.getTranslation("Change Name"));
			newContact.setLastname(TranslatorUtil.getTranslation("Change Lastname"));
			newContact.setTemporaryId("_"+new Date().getTime());
			ret.add(newContact);
			return ret;
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		}
	}

}
