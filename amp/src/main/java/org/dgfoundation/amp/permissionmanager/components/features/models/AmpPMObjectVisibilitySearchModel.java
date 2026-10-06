/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.*;

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
    public AmpPMObjectVisibilitySearchModel(String input,String language, Map params) {
        super(input, language, params);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.model.LoadableDetachableModel#load()
     */
//  @Override
//  protected List<AmpObjectVisibility> load() {
//      try {
//          List<AmpObjectVisibility> ret = new ArrayList<AmpObjectVisibility>();
//          session = PersistenceManager.getSession();
//          Query query = session.createQuery("from "+ AmpModulesVisibility.class.getName()+ " o WHERE o.name like :name ORDER BY o.name");
//          Integer maxResults = (Integer) getParams().get(PARAM.MAX_RESULTS);
//          if (maxResults != null)
//              query.setMaxResults(maxResults);
//          query.setString("name", "%" + input + "%");
//          ret = query.list();
//          
//          query = session.createQuery("from "+ AmpFeaturesVisibility.class.getName()+ " o WHERE o.name like :name ORDER BY o.name");
//          if (maxResults != null)
//              query.setMaxResults(maxResults);
//          query.setString("name", "%" + input + "%");
//          ret.addAll(query.list());
//          
//          query = session.createQuery("from "+ AmpFieldsVisibility.class.getName()+ " o WHERE o.name like :name ORDER BY o.name");
//          if (maxResults != null)
//              query.setMaxResults(maxResults);
//          query.setString("name", "%" + input + "%");
//          ret.addAll(query.list());
//          
//          session.close();
//          return ret;
//      } catch (HibernateException e) {
//          throw new RuntimeException(e);
//      } catch (SQLException e) {
//          throw new RuntimeException(e);
//      }
//  }

    @Override
    protected Collection<AmpObjectVisibility> load() {
        List<AmpObjectVisibility> ret = null;
        try {
            
            session = PersistenceManager.getSession();
            TreeSet<AmpObjectVisibility> fields = new TreeSet<AmpObjectVisibility>();
            Criteria critFields = session.createCriteria(AmpFieldsVisibility.class);
            critFields.setCacheable(true);
            Criteria critFeatures = session.createCriteria(AmpFeaturesVisibility.class);
            critFeatures.setCacheable(true);
            Criteria critModules = session.createCriteria(AmpModulesVisibility.class);
            critModules.setCacheable(true);
            if(input.trim().length()>0){
                    critFields.add(Restrictions.disjunction()
                            .add(Restrictions.ilike("name", "%" + input + "%")))
                            .addOrder(Order.asc("name"));
                    critFeatures.add(Restrictions.disjunction()
                            .add(Restrictions.ilike("name", "%" + input + "%")))
                            .addOrder(Order.asc("name"));
                    critModules.add(Restrictions.disjunction()
                            .add(Restrictions.ilike("name", "%" + input + "%")))
                            .addOrder(Order.asc("name"));
                    if (params != null) {
                        Integer maxResults = (Integer) getParams().get(
                                PARAM.MAX_RESULTS);
                        if (maxResults != null && maxResults.intValue() != 0)
                            {
                                critFields.setMaxResults(maxResults);
                                critFeatures.setMaxResults(maxResults);
                                critModules.setMaxResults(maxResults);
                            }
                    }
            }
            fields.addAll(critFields.list());
            fields.addAll(critFeatures.list());
            fields.addAll(critModules.list());
            ret = new ArrayList<AmpObjectVisibility>();
            ret.addAll(fields);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return ret;
    }

    
    
}
