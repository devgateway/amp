/**
 * 
 */
package org.digijava.module.aim.ar.impexp;

/**
 * @author Alex Gartner
 *
 */
public interface ImpTransformerFactory<Entity, Root> {
	public ImpTransformer<Entity> generateImpTransformer(Root r, String propertyName);
}
