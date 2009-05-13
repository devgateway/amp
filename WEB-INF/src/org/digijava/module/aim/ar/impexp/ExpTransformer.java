package org.digijava.module.aim.ar.impexp;

import java.util.Collection;

public interface ExpTransformer<Entity, Root> {
		/**
		 * 
		 * @param e
		 * @param propertyName null if this transformer will transform the entire entity e, otherwise it is the name of the property to be trasnsformed
		 * @return
		 */
		public Root transform (Entity e, String propertyName);
		
}
