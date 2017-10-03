/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpAutoCompleteModelParam;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author dan
 *
 */
public class AmpPMUserSearchModel extends AbstractAmpAutoCompleteModel<User> {

    private Session session;
    
    public AmpPMUserSearchModel(String input,String language, Map<AmpAutoCompleteModelParam, Object> params) {
        super(input, language, params);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.model.LoadableDetachableModel#load()
     */
    
    @Override
    protected Collection<User> load() {
        List<User> ret = null;
        try {
            
            session = PersistenceManager.getRequestDBSession();
            Criteria crit = session.createCriteria(User.class);
            crit.setCacheable(true);
            TreeSet<User> users = new TreeSet<User>();
                
            if(input.trim().length()>0){
                String[] chunks = input.trim().replaceAll(" - "," ").split(" ");
                for(int i =0; i < chunks.length ; i++){
                    crit.add(Restrictions.disjunction()
                            .add(Restrictions.ilike("firstNames", "%" + chunks[i] + "%"))
                            .add(Restrictions.ilike("lastName", "%" + chunks[i] + "%"))
                            .add(Restrictions.ilike("email", "%" + chunks[i] + "%")))
                            .addOrder(Order.asc("firstNames"));
        
                    if (params != null) {
                        Integer maxResults = (Integer) getParams().get(
                                PARAM.MAX_RESULTS);
                        if (maxResults != null && maxResults.intValue() != 0)
                            crit.setMaxResults(maxResults);
                    }
                    users.addAll(crit.list());
                }
            }
            if(!users.isEmpty())
                {
                    ret = new ArrayList<User>();
                    ret.addAll(users);
                }
            else ret = crit.list();
            
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        
        return ret;
    }
    
}
