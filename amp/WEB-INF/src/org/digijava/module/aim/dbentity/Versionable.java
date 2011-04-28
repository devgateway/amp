package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;

public interface Versionable {

	public abstract boolean equalsForVersioning(Object obj);

	public abstract Object getValue();

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