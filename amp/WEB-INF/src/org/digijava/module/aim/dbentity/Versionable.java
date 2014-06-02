package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.error.AmpNotImplementedException;
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
	 * @throws AmpNotImplementedException 
	 */
	public abstract boolean equalsForVersioning(Object obj) throws AmpNotImplementedException;

	/**
	 * Used two compare two objects in versioning, 
	 * return a combination of all the fields in the object
	 * (see AmpFunding)
	 * 
	 * @return
	 * @throws AmpNotImplementedException 
	 */
	public abstract Object getValue() throws AmpNotImplementedException;

	/**
	 * Formatted output that will be used to show the contents
	 * of the object
	 * @return
	 * @throws AmpNotImplementedException 
	 */
	public abstract Output getOutput() throws AmpNotImplementedException;

	/**
	 * Implement this method to prepare a persistent object (it exists in the DB
	 * and is associated to an activity) so a new copy can be saved in the DB
	 * linked to newActivity.
	 * 
	 * @param newActivity
	 * @return
	 * @throws Exception, AmpNotImplementedException
	 */
	public abstract Object prepareMerge(AmpActivityVersion newActivity) throws Exception, AmpNotImplementedException;
}