package org.digijava.module.aim.dbentity;

public class AmpActivityVersion extends AmpActivity implements Cloneable {

	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			return (AmpActivityVersion) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
}