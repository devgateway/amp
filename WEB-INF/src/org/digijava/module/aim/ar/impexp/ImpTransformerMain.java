package org.digijava.module.aim.ar.impexp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
/**
 * 
 * A class extending this should have a constructor that initializes rootJaxbClass and tFactory.
 * 
 * @author Alex Gartner
 *
 * @param <Entity> The database entity that will be instantiated
 * @param <Root> The Root JAXB Element.
 * @param <FRoot> The JAXB Element that corresponds  1-to-1 to an Entity. The Root element would contain 1 or more FRoot elements.
 * @param <TFactory> Is the factory type that creates the ImpTransformer that will generate an Entity. From its point of view the root element is an FRoot. 
 */
public abstract class ImpTransformerMain<Entity, Root, FRoot, TFactory extends ImpTransformerFactory<Entity, FRoot>> {
	
	protected Class<Root> rootJaxbClass;
	protected Root root;
	protected TFactory tFactory;
	protected Collection<FRoot> entityRoots;
	
	public List<Entity> transform() {
		ArrayList<Entity> returnList		= new ArrayList<Entity>();
		if ( entityRoots != null ) {
			for ( FRoot fRoot: entityRoots ) {
				ImpTransformer<Entity> transformer	= tFactory.generateImpTransformer(fRoot, null);
				Entity e		= transformer.transform();
				if (e != null)
					returnList.add(e);
			}
		}
		return returnList;
	}
	
	public Root unmarshall (InputStream is) throws JAXBException {
		JAXBContext jaxbContext		= JAXBContext.newInstance( this.rootJaxbClass );
		Unmarshaller unmarshaller		= jaxbContext.createUnmarshaller();
		this.root								=  (Root)unmarshaller.unmarshal(is);
		this.processEntityRoots();
		return this.root;
	}
	
	/**
	 *  Based on the "root" property this method needs to populate the entityRoots collection.
	 */
	protected abstract void processEntityRoots(); 
	
	
}
