package org.digijava.module.budgetexport.adapter;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.util.HierarchyListable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 2/4/12
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MappingEntityAdapter {
    public List<HierarchyListable> getAllObjects() throws DgException;
    public int getObjectCount() throws DgException;
    public HierarchyListable getObjectByID(Long id) throws DgException;
    public boolean hasAddidionalLabelColumn();
}
