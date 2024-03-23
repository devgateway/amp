/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    public AmpPMWorkspaceSearchModel(String input, String language, Map params) {
        super(input, language, params);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.model.LoadableDetachableModel#load()
     */

    @Override
    protected Collection<AmpTeam> load() {
        List<AmpTeam> ret = null;
        try {
            
            session = PersistenceManager.getSession();
            Criteria crit = session.createCriteria(AmpTeam.class);
            crit.setCacheable(true);
                
            if(input.trim().length()>0){
                    crit.add(Restrictions.disjunction()
                            .add(getTextCriterion("name", input)))
                            //.add(Restrictions.ilike("name", "%" + input + "%")))
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
        }
        
        return ret;
    }
    
    
    
}
