/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpMEIndicatorSearchModel extends
        AbstractAmpAutoCompleteModel<AmpIndicator> {

    public AmpMEIndicatorSearchModel(String input,String language,
            Map<AmpAutoCompleteModelParam, Object> params) {
        super(input, language, params);
        // TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 8211300754918658832L;
    private Session session;

    
    @Override
    protected Collection<AmpIndicator> load() {
        try {
            List<AmpIndicator> ret = null;
            session = AmpActivityModel.getHibernateSession();
            Integer maxResults = (Integer) getParams().get(
                    AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
            Criteria crit = session.createCriteria(AmpIndicator.class);
            
            crit.setCacheable(true);
            if (input.trim().length() > 0){
                Junction junction = Restrictions.conjunction().add(getTextCriterion("name", input));
                crit.add(junction);
            }
            crit.addOrder(Order.asc("name"));
            if (maxResults != null && maxResults != 0)
                crit.setMaxResults(maxResults);
            ret = crit.list();
            //session.close();
            return ret;
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        } 
    }

}
