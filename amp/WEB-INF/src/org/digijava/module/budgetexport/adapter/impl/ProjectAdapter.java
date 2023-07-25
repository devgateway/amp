package org.digijava.module.budgetexport.adapter.impl;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.hibernate.query.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 2/22/12
 * Time: 6:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectAdapter implements MappingEntityAdapter {
    @Override
    public List<HierarchyListable> getAllObjects() throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        StringBuilder objQueryStr = new StringBuilder("from ");
        objQueryStr.append(AmpActivity.class.getName());
        objQueryStr.append(" act where act.name is not null");
        Query objQuery = sess.createQuery(objQueryStr.toString());
        objQuery.setCacheable(true);
        return objQuery.list();
    }

    @Override
    public int getObjectCount() throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        StringBuilder queryStr = new StringBuilder("select distinct activity.amp_activity_id from v_titles activity");
        SQLQuery q = sess.createNativeQuery(queryStr.toString());
        List<Long> ids = q.list();
        return ids != null?ids.size():0;
    }

    @Override
    public HierarchyListable getObjectByID(Long id) throws DgException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasAddidionalLabelColumn() {
        return true;
    }
}
