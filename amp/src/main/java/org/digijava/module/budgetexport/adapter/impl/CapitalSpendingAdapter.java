package org.digijava.module.budgetexport.adapter.impl;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.adapter.DummyAmpEntity;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: flyer
 * Date: 2/13/12
 * Time: 7:00 PM
 */
public class CapitalSpendingAdapter implements MappingEntityAdapter {
    public List<HierarchyListable> getAllObjects() throws DgException {
        List<HierarchyListable> retVal = new ArrayList<HierarchyListable>();
        retVal.add(new DummyAmpEntity(new Long(1), "Capital"));
        retVal.add(new DummyAmpEntity(new Long(2), "Expenditure"));
        return retVal;
    }



    public int getObjectCount() throws DgException {
        return 2;
    }

    public HierarchyListable getObjectByID(Long id) throws DgException {
        return null;
    }

    @Override
    public boolean hasAddidionalLabelColumn() {
        return false;
    }
}
