package org.digijava.module.aim.ar.impexp;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.digijava.module.aim.ar.impexp.annotations.TransformerAnn;

public abstract class AbstractImpTransformer<Entity, Root> implements ImpTransformer<Entity> {
	
	private static Logger logger 		= Logger.getLogger(AbstractImpTransformer.class);
	
	private HashMap<String, ImpTransformerFactory<Object, Object>> factoryMap	= 
		new HashMap<String, ImpTransformerFactory<Object,Object>>();
	
	private ImpTransformerFactory<Object,Object> getImpTransformerFactory(String className) throws Exception {
		ImpTransformerFactory<Object, Object> factory		= factoryMap.get(className);
		if ( factory == null ) {
			Class<ImpTransformerFactory> factoryClass		= (Class<ImpTransformerFactory>)Class.forName(className);
			factory						= factoryClass.newInstance();
			factoryMap.put(className, factory);
		}
		return factory;
	}	

	/**
	 * This function iterates through all the properties of e. If it finds a the TransformerAnn annotation on a property 
	 * it is used to get a ImpTransformer object and it calls its transform() method.
	 * 
	 * If you extends this class be sure to call this.iterateProperties() from within the transform() function.
	 * 
	 * @param e
	 * @throws Exception
	 */
	public void iterateProperties(Entity e, Root r) throws Exception {
		Class myClass		= e.getClass();
		Field[] fields		= myClass.getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			TransformerAnn t	= fields[i].getAnnotation( TransformerAnn.class );
			if ( t != null) {
				if ( t.impTransformerFactoryClass() != null && t.impTransformerFactoryClass().length() > 0 ) {
					PropertyDescriptor beanProperty		= new PropertyDescriptor(fields[i].getName(), myClass);
					Class beanPropertyClass					= beanProperty.getPropertyType();
					String beanPropertyName				= fields[i].getName();
					String propertyName						= beanPropertyName;
					if ( t.overwrittenPropertyName() != null && t.overwrittenPropertyName().length() > 0 )
						propertyName		= t.overwrittenPropertyName();
					
					ImpTransformer transformer			= getImpTransformerFactory( t.impTransformerFactoryClass() ).generateImpTransformer(r, propertyName);
					if (transformer != null){
						Object result									= transformer.transform();
						Object [] args								= new Object[1];
						args[0]											= this.processResponse(result, beanPropertyName, beanPropertyClass);
						beanProperty.getWriteMethod().invoke(e, args);
					}
					else 
						logger.error("Transformer for property " + beanPropertyName + " in class " + myClass +" was null " );
				}
			}
		}
	}
	
	protected abstract Object processResponse(Object response, String beanPropertyName, Class beanPropertyClass) ;
}
