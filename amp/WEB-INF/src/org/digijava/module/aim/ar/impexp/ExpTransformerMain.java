package org.digijava.module.aim.ar.impexp;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.dgfoundation.amp.Util;

public abstract class ExpTransformerMain<Root, FRoot, Entity, TFactory extends ExpTransformerFactory<Entity, FRoot>, IdType> {
	
	protected Class<Entity> entityClass				= null;
	protected TFactory tfactory							= null;
	protected Root root										= null;
	
	/** 
	 * In case IdType is not Long, this method should be overwritten
	 * 
	 * @param ids
	 * @return a collection of the entities with id in ids
	 * @throws Exception
	 */
	protected Collection<Entity> getSelectedObjects( Object [] ids ) throws Exception{
		return Util.getSelectedObjects( entityClass, ids );
	}
	
	
	/**
	 *  This method adds the resulted "child" to the "root" object
	 * @param child
	 */
	protected abstract void process(FRoot child);
	
	public Root transform(Collection<IdType> entitiesIds) throws Exception{
		Collection<Entity> entities				= this.getSelectedObjects( entitiesIds.toArray() );
		if ( entities != null ) {
			Iterator<Entity> iter										= entities.iterator();
				while (iter.hasNext()) {
				ExpTransformer<Entity, FRoot> transformer	= 
					tfactory.generateExpTransformer();
				FRoot result													= transformer.transform(iter.next(), null);
				
				this.process(result);
			}
		}
		
		return root;
	}
	
	public void marshall (OutputStream ps) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance( root.getClass() );
	    Marshaller m = jc.createMarshaller();
	    m.marshal( root, ps );

	}
	
}
