package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;

public interface Versionable {

	public abstract boolean equalsForVersioning(Object obj);
	
	public abstract Object getValue();
	
	public abstract Output getOutput();
}
