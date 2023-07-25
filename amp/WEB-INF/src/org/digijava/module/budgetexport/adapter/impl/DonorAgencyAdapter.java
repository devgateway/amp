package org.digijava.module.budgetexport.adapter.impl;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.OrganisationManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.hibernate.query.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 2/4/12
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DonorAgencyAdapter implements MappingEntityAdapter {
    public List<HierarchyListable> getAllObjects() throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        StringBuilder queryStr = new StringBuilder("select distinct donor.amp_donor_org_id from v_donors donor");
        SQLQuery q = sess.createSQLQuery(queryStr.toString());
        //q.setCacheable(true);
        List<Long> ids = q.list();

        StringBuilder objQueryStr = new StringBuilder("from ");
        objQueryStr.append(AmpOrganisation.class.getName());
        objQueryStr.append(" donorOrg where (donorOrg.deleted is null or donorOrg.deleted = false)  and donorOrg.ampOrgId in(");
        objQueryStr.append(MappingEntityAdapterUtil.generateIdWhereClause(ids));
        objQueryStr.append(")");
        Query objQuery = sess.createQuery(objQueryStr.toString());
        objQuery.setCacheable(true);
        return objQuery.list();
    }

    public int getObjectCount() throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        StringBuilder queryStr = new StringBuilder("select distinct donor.amp_donor_org_id from v_donors donor");
        SQLQuery q = sess.createSQLQuery(queryStr.toString());
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
