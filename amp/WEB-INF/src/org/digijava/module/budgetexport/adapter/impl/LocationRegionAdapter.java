package org.digijava.module.budgetexport.adapter.impl;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.hibernate.query.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.List;

/**
 * User: flyer
 * Date: 2/8/12
 * Time: 5:30 PM
 */
public class LocationRegionAdapter implements MappingEntityAdapter {
    public List<HierarchyListable> getAllObjects() throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        StringBuilder queryStr = new StringBuilder("select distinct loc.adm_level_1_id from v_adm_level_1 loc");
        SQLQuery q = sess.createNativeQuery(queryStr.toString());
        List<Long> ids = q.list();

        StringBuilder objQueryStr = new StringBuilder("from ");
        objQueryStr.append(AmpCategoryValueLocations.class.getName());
        objQueryStr.append(" loc where loc.id in(");
        objQueryStr.append(MappingEntityAdapterUtil.generateIdWhereClause(ids));
        objQueryStr.append(")");
        Query objQuery = sess.createQuery(objQueryStr.toString());
        objQuery.setCacheable(true);
        return objQuery.list();
    }

    public int getObjectCount() throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        StringBuilder queryStr = new StringBuilder("select distinct loc.adm_level_1_id from v_adm_level_1 loc");
        SQLQuery q = sess.createNativeQuery(queryStr.toString());
        List<Long> ids = q.list();
        return ids != null?ids.size():0;
    }

    public HierarchyListable getObjectByID(Long id) throws DgException {
        return null;
    }

    @Override
    public boolean hasAddidionalLabelColumn() {
        return false;
    }
}
