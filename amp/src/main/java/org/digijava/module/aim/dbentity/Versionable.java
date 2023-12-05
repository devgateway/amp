package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;

public interface Versionable {

    /**
     * Used to identify that's the same object when comparing 
     * two activity versions (eg. when comparing two sets in the
     * activities, you'll have to identify the items one by one
     * and compare their differences)
     * 
     * @param obj
     * @return
     */
    public abstract boolean equalsForVersioning(Object obj);

    /**
     * Used two compare two objects in versioning, 
     * return a combination of all the fields in the object
     * (see AmpFunding)
     * 
     * @return
     */
    public abstract Object getValue();

    /**
     * Formatted output that will be used to show the contents
     * of the object
     * @return
     */
    public abstract Output getOutput();

    /**
     * Implement this method to prepare a persistent object (it exists in the DB
     * and is associated to an activity) so a new copy can be saved in the DB
     * linked to newActivity.
     * 
     * @param newActivity
     * @return
     * @throws Exception
     */
    public abstract Object prepareMerge(AmpActivityVersion newActivity) throws Exception;
}
