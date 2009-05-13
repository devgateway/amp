/**
 * 
 */
package org.digijava.module.aim.ar.impexp;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.ar.impexp.annotations.TransformerAnn;

/**
 * @author Alex Gartner
 *
 */
public abstract class AbstractExpTransformer<Entity, Root> implements
		ExpTransformer<Entity, Root> {
	
		private static Logger logger 		= Logger.getLogger(AbstractExpTransformer.class);
		
		private HashMap<String, ExpTransformerFactory<Object, Object>> factoryMap	= 
			new HashMap<String, ExpTransformerFactory<Object,Object>>();
		
		private ExpTransformerFactory<Object,Object> getExpTransformerFactory(String className) throws Exception {
			ExpTransformerFactory<Object, Object> factory		= factoryMap.get(className);
			if ( factory == null ) {
				Class<ExpTransformerFactory> factoryClass		= (Class<ExpTransformerFactory>)Class.forName(className);
				factory						= factoryClass.newInstance();
				factoryMap.put(className, factory);
			}
			return factory;
		}	
	
		/**
		 * This function iterates through all the properties of e. If it finds a the TransformerAnn annotation on a property 
		 * it is used to get a ExpTransformer object and it call its transform() method.
		 * 
		 * If you extends this class be sure to call this.iterateProperties() from within the transform() function.
		 * 
		 * @param e
		 * @throws Exception
		 */
		public void iterateProperties(Entity e) throws Exception {
			Class myClass		= e.getClass();
			Field[] fields		= myClass.getDeclaredFields();
			for (int i=0; i<fields.length; i++) {
				TransformerAnn t	= fields[i].getAnnotation( TransformerAnn.class );
				if ( t != null) {
					if ( t.expTransformerFactoryClass() != null && t.expTransformerFactoryClass().length() > 0 ) {
						PropertyDescriptor beanProperty		= new PropertyDescriptor(fields[i].getName(), myClass);
						Object property								= beanProperty.getReadMethod().invoke(e, new Object[0]);
						Object result									= null;
						String propertyName						= fields[i].getName();
						if ( t.overwrittenPropertyName() != null && t.overwrittenPropertyName().length() > 0 )
							propertyName		= t.overwrittenPropertyName();
						if ( property instanceof Collection ) {
							List<Object> resultsList				= new ArrayList<Object>();
							result										= resultsList;
							Collection<Object> c					= ( Collection<Object> ) property;
							if ( c != null ) {
								Iterator<Object> iter				= c.iterator();
								while ( iter.hasNext() ) {
									ExpTransformer transformer			= getExpTransformerFactory( t.expTransformerFactoryClass() ).generateExpTransformer();
									Object r											= transformer.transform(iter.next(), propertyName );
									resultsList.add(r);
								}
							}
						}
						else {
							ExpTransformer transformer			= getExpTransformerFactory( t.expTransformerFactoryClass() ).generateExpTransformer();
							result											= transformer.transform( property, propertyName );
						}
						if ( result != null )
							this.processJaxb(fields[i].getName(), result);
						else
							logger.error("Transformed JAXB element is null for property name: " + propertyName + ", in class: " + myClass );
					}
				}
			}
		}
		
		public abstract void processJaxb( String fieldName, Object result );
}
