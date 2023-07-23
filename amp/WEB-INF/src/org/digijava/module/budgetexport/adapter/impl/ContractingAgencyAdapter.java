package org.digijava.module.budgetexport.adapter.impl;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.hibernate.query.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.List;

/**
 * User: flyer
 * Date: 11/13/12
 * Time: 6:19 PM
 */
public class ContractingAgencyAdapter implements MappingEntityAdapter {

    @Override
    public List<HierarchyListable> getAllObjects() throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        StringBuilder queryStr = new StringBuilder("select distinct contracting.amp_org_id from v_contracting_agency contracting");
        SQLQuery q = sess.createSQLQuery(queryStr.toString());
        List<Long> ids = q.list();

        StringBuilder objQueryStr = new StringBuilder("from ");
        objQueryStr.append(AmpOrganisation.class.getName());
        objQueryStr.append(" contractingOrg where contractingOrg.ampOrgId in(");
        objQueryStr.append(MappingEntityAdapterUtil.generateIdWhereClause(ids));
        objQueryStr.append(")");
        Query objQuery = sess.createQuery(objQueryStr.toString());
        objQuery.setCacheable(true);
        return objQuery.list();
    }

    @Override
    public int getObjectCount() throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        StringBuilder queryStr = new StringBuilder("select distinct contracting.amp_org_id from v_contracting_agency contracting");
        SQLQuery q = sess.createSQLQuery(queryStr.toString());
        List<Long> ids = q.list();
        return ids != null?ids.size():0;
    }

    @Override
    public HierarchyListable getObjectByID(Long id) throws DgException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasAddidionalLabelColumn() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
