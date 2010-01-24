package org.digijava.module.aim.ar.impexp;

public interface ExpTransformerFactory<Entity, Root> {
		public ExpTransformer<Entity, Root> generateExpTransformer();
}
