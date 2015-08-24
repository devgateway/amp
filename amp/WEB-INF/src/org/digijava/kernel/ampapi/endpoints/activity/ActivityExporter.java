/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.editor.exception.EditorException;

/**
 * Class used for exporting an activity as a JSON
 * 
 * @author Viorel Chihai
 */
public class ActivityExporter {
	
	private static final Logger logger = Logger.getLogger(ActivityImporter.class);
	
	private List<String> filteredFields = new ArrayList<String>();
	
	/**
	 * Activity Export as JSON 
	 * 
	 * @param activity actual activity to export
	 * @param filter is the JSON with a list of fields
	 * @param translations 
	 * @param language 
	 * @return
	 * @throws DgException 
	 */
	public JsonBean getActivity(AmpActivityVersion activity, JsonBean filter) throws DgException {
		JsonBean resultJson = new JsonBean();
	    
		if (!InterchangeUtils.validateFilterActivityFields(filter, resultJson)) {
			return resultJson;
		}
		
		if (filter != null) {
			this.filteredFields = (List<String>) filter.get(ActivityEPConstants.FILTER_FIELDS);
		}
		
		Field[] fields = activity.getClass().getSuperclass().getDeclaredFields();

		for (Field field : fields) {
			try {
				readFieldValue(field, activity, activity, resultJson, null);
			} catch (IllegalArgumentException | IllegalAccessException
					| NoSuchMethodException | SecurityException
					| InvocationTargetException e) {
				logger.error("Coudn't read activity fields with id: " + activity.getAmpActivityId() + ". " 	+ e.getMessage());
				throw new RuntimeException(e);
			}
		}
		
		return resultJson;
	}
	
	/**
	 * 
	 * @param field
	 * @param fieldInstance
	 * @param resultJson
	 * @param filteredFields
	 * @param fieldPath
	 * @return
	 */
	private void readFieldValue(Field field, Object fieldInstance, Object parentObject, JsonBean resultJson, String fieldPath) throws IllegalArgumentException, 
	IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, EditorException {
		
		Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
		InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
		
		if (interchangeable != null && !InterchangeUtils.isAmpActivityVersion(field.getType()) 
				&& FMVisibility.isVisible(interchangeable.fmPath())) {
			field.setAccessible(true);
			String fieldTitle = InterchangeUtils.underscorify(interchangeable.fieldTitle());
			
			String filteredFieldPath = fieldPath == null ? fieldTitle : fieldPath + "~" + fieldTitle;
			Object fieldValue = field.get(fieldInstance);
			
			if (!interchangeable.pickIdOnly()) {
				// check if the member is a collection
				
				if (InterchangeUtils.isCompositeField(field)) {
					generateCompositeValues(field, fieldValue, fieldPath, resultJson);
				} else if(isFiltered(filteredFieldPath)) {
					if (InterchangeUtils.isCollection(field)) {
						Collection<Object> collectionItems = (Collection<Object>) fieldValue;
						// if the collection is not empty, it will be parsed and a JSON with member details will be generated
						List<JsonBean> collectionJson = new ArrayList<JsonBean>();
						if (collectionItems != null) {
							// iterate over the objects of the collection
							for (Object item : collectionItems) {
								collectionJson.add(getObjectJson(item, filteredFieldPath));
							}
						}
						// put the array with object values in the result JSON
						resultJson.set(fieldTitle, collectionJson);
					} else {

						if (InterchangeableClassMapper.containsSupportedClass(field.getType()) || fieldValue == null) {
							Class<? extends Object> parentClassName = parentObject == null ? field.getDeclaringClass() : parentObject.getClass();
							resultJson.set(fieldTitle, InterchangeUtils.getTranslationValues(field, parentClassName, fieldValue, InterchangeUtils.getId(parentObject)));
						} else {
							resultJson.set(fieldTitle, getObjectJson(fieldValue, filteredFieldPath));
						}
					}
				}
			} else {
				if (isFiltered(filteredFieldPath)) {
					
					Long id;
					
					if (discriminator != null && discriminator.discriminatorClass().length() > 0) {
						try {
							Class<FieldsDiscriminator> discClass = (Class<FieldsDiscriminator>) Class.forName(discriminator.discriminatorClass());
							id = (Long) discClass.newInstance().getIdOf(fieldValue);
							resultJson.set(fieldTitle, id);	
						} catch (ClassNotFoundException | InstantiationException e) {
							throw new RuntimeException("Couldn't instantiate discriminator class "+ discriminator.discriminatorClass());
						}
					} else {
						id = fieldValue != null ? InterchangeUtils.getId(fieldValue) : null;
						resultJson.set(fieldTitle, id);
					}
				}
			}
		}		
	}
	
	/**
	 * 
	 * @param item
	 * @param filteredFields
	 * @param fieldPath
	 * @return itemJson
	 */
	private JsonBean getObjectJson(Object item, String fieldPath) throws IllegalArgumentException, IllegalAccessException, 
	NoSuchMethodException, SecurityException, InvocationTargetException, EditorException {
		
		Field[] itemFields = item.getClass().getDeclaredFields();
		JsonBean itemJson = new JsonBean();
		
		// iterate the fields of the object and generate the JSON
		for (Field itemField : itemFields) {
			readFieldValue(itemField, item, item, itemJson, fieldPath);	
		}
		
		return itemJson;
	}
	
	/**
	 * Generate the composite values. E.g: we have a list of sectors, 
	 * in JSON the list should be written by classification 
	 * (primary programs, secondary programs, etc.)
	 * @param field
	 * @param fieldInstance
	 * @param resultJson
	 * @param filteredFields
	 * @param fieldPath
	 */
	private void generateCompositeValues(Field field, Object object, String fieldPath, JsonBean resultJson) throws IllegalArgumentException, 
	IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, EditorException {
		
		Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
		InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
		Interchangeable[] settings = discriminator.settings();
		
		try {
			Class<? extends FieldsDiscriminator> discClass = InterchangeUtils.getDiscriminatorClass(field);
			
			if (discClass != null) {
				resultJson.set(InterchangeUtils.underscorify(interchangeable.fieldTitle()), getDiscriminatorValue(discClass, object));
				return;
			}
		} catch(ClassNotFoundException e) {
			logger.error("Discriminator class not found. " 	+ e.getMessage());
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			logger.error("Error in creating instance of class. " 	+ e.getMessage());
			throw new RuntimeException(e);
		}			
		
		
		Map<String, Object> compositeMap = new HashMap<String, Object>();
		Map<String, Interchangeable> compositeMapSettings = new HashMap<String, Interchangeable>();
		Map<String, String> filteredFieldsMap = new HashMap<String, String>();
		
		// check that we need to initialize as a collection only real collections
		boolean initAsCollection = InterchangeUtils.isCollection(field) &&
				!InterchangeUtils.getGenericClass(field).equals(AmpCategoryValue.class);
		
		// create the map containing the correlation between the discriminatorOption and the JSON generated objects
		for (Interchangeable setting : settings) {
			// TODO: init settings with defaults from interchangeable
			compositeMap.put(setting.discriminatorOption(), initAsCollection ? new ArrayList<JsonBean>() : null);
			compositeMapSettings.put(setting.discriminatorOption(), setting);
			String fieldTitle = InterchangeUtils.underscorify(setting.fieldTitle());
			String filteredFieldPath = fieldPath == null ? fieldTitle : fieldPath + "~" + fieldTitle;
			
			filteredFieldsMap.put(setting.discriminatorOption(), filteredFieldPath);
		}
		
		if (InterchangeUtils.isCollection(field) && object != null) {
			Collection<Object> compositeCollection = (Collection <Object>) object;
			if (compositeCollection.size() > 0) {
				for (Object obj : compositeCollection) {
					if (obj instanceof AmpActivitySector) {
						AmpActivitySector sector = (AmpActivitySector) obj;
						String filteredFieldPath = filteredFieldsMap.get(sector.getClassificationConfig().getName());
						((List<JsonBean>) compositeMap.get(sector.getClassificationConfig().getName())).add(getObjectJson(sector, filteredFieldPath));
					} else if (obj instanceof AmpActivityProgram) {
						AmpActivityProgram program = (AmpActivityProgram) obj;
						String filteredFieldPath = filteredFieldsMap.get(program.getProgramSetting().getName());
						((List<JsonBean>) compositeMap.get(program.getProgramSetting().getName())).add(getObjectJson(program, filteredFieldPath));
					} else if (obj instanceof AmpCategoryValue) {
						AmpCategoryValue catVal = (AmpCategoryValue) obj;
						String filteredFieldPath = filteredFieldsMap.get(catVal.getAmpCategoryClass().getKeyName());
						// we may need to move up for all composites, but so far applies to ACV, 
						// so keeping here to avoid side effects in rush changes
						if (compositeMapSettings.get(catVal.getAmpCategoryClass().getKeyName()).pickIdOnly()) {
							compositeMap.put(catVal.getAmpCategoryClass().getKeyName(), catVal.getId());
						} else {
							((List<JsonBean>) compositeMap.get(catVal.getAmpCategoryClass().getKeyName())).add(getObjectJson(catVal, filteredFieldPath));
						}
						//TODO we have to manage when the ActivityBudet is not present (Budget Unallocated)
					} else if (obj instanceof AmpOrgRole) {
						AmpOrgRole aor = (AmpOrgRole) obj;
						String filteredFieldPath = filteredFieldsMap.get(aor.getRole().getRoleCode());
						((List<JsonBean>) compositeMap.get(aor.getRole().getRoleCode())).add(getObjectJson(aor, filteredFieldPath));
					} else if (obj instanceof AmpActivityContact) {
						AmpActivityContact ac = (AmpActivityContact) obj;
						String filteredFieldPath = filteredFieldsMap.get(ac.getContactType());
						((List<JsonBean>) compositeMap.get(ac.getContactType())).add(getObjectJson(ac, filteredFieldPath));
					}
				}
			}
		}
		
		// put in the result JSON the generated structure
		for (Interchangeable setting : settings) {
			String fieldTitle = InterchangeUtils.underscorify(setting.fieldTitle());
			if (isFiltered(fieldTitle) && FMVisibility.isVisible(setting.fmPath())) {
				resultJson.set(fieldTitle, compositeMap.get(setting.discriminatorOption()));
			}
		}
	}
	
	/**
	 * 
	 * @param filteredFieldPath
	 * @param filteredFields
	 * @return boolean, if the field should be exported in the result Json 
	 */
	private boolean isFiltered(String filteredFieldPath) {
		if (filteredFields.isEmpty()) 
			return true;
			
		for (String s : filteredFields) {
			if (s.startsWith(filteredFieldPath) || filteredFieldPath.startsWith(s)) 
				return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param discClass Discriminator Class that will be used to load the values of the object
	 * @param fieldValue Object which will be used to retrieve the custom value
	 * @return object Custom value of the object  
	 */
	private Object getDiscriminatorValue(Class<? extends FieldsDiscriminator> discClass, Object fieldValue) 
			throws NoSuchMethodException, SecurityException, InstantiationException, 
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		FieldsDiscriminator discObj = discClass.newInstance();
		return discObj.toJsonOutput(fieldValue);
	}
}
