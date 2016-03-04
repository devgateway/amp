/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
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
import org.digijava.module.aim.dbentity.AmpFundingAmount;
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
				readFieldValue(field, activity, activity, resultJson, null, new ArrayDeque<Interchangeable>());
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
	 * @param field the instance of the field
	 * @param fieldInstance the object of the field
	 * @param parentObject the object that contain the field
	 * @param resultJson result JSON object which will be filled with the values of the fields 
	 * @param fieldPath the underscorified path to the field currently exported
	 * @return
	 */
	private void readFieldValue(Field field, Object fieldInstance, Object parentObject, JsonBean resultJson, 
			String fieldPath, Deque<Interchangeable> intchStack) throws IllegalArgumentException, 
	IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, EditorException {
		
		Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
		InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
		
		if (interchangeable != null && !InterchangeUtils.isAmpActivityVersion(field.getType()) 
				&& FMVisibility.isVisible(interchangeable.fmPath(), intchStack)) {
			intchStack.push(interchangeable);
			field.setAccessible(true);
			String fieldTitle = InterchangeUtils.underscorify(interchangeable.fieldTitle());
			
			String filteredFieldPath = fieldPath == null ? fieldTitle : fieldPath + "~" + fieldTitle;
			Object fieldValue = field.get(fieldInstance);
			
			if (!interchangeable.pickIdOnly()) {
				// check if the member is a collection
				
				if (InterchangeUtils.isCompositeField(field)) {
					generateCompositeValues(field, fieldValue, fieldPath, intchStack, resultJson);
				} else if(isFiltered(filteredFieldPath)) {
					if (InterchangeUtils.isCollection(field)) {
						Collection<Object> collectionItems = (Collection<Object>) fieldValue;
						// if the collection is not empty, it will be parsed and a JSON with member details will be generated
						List<JsonBean> collectionJson = new ArrayList<JsonBean>();
						if (collectionItems != null) {
							// iterate over the objects of the collection
							for (Object item : collectionItems) {
								collectionJson.add(getObjectJson(item, filteredFieldPath, intchStack));
							}
						}
						// put the array with object values in the result JSON
						resultJson.set(fieldTitle, collectionJson);
					} else {

						if (InterchangeableClassMapper.containsSupportedClass(field.getType()) || fieldValue == null) {
							Class<? extends Object> parentClassName = parentObject == null ? field.getDeclaringClass() : parentObject.getClass();
							resultJson.set(fieldTitle, InterchangeUtils.getTranslationValues(field, parentClassName, fieldValue, InterchangeUtils.getId(parentObject)));
						} else {
							resultJson.set(fieldTitle, getObjectJson(fieldValue, filteredFieldPath, intchStack));
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
						resultJson.set(fieldTitle, InterchangeUtils.getId(fieldValue));
					}
				}
			}
			intchStack.pop();
		}		
	}
	
	/**
	 * 
	 * @param item 
	 * @param fieldPath the underscorified path to the field currently exported 
	 * @return itemJson object JSON containing the value of the item
	 */
	private JsonBean getObjectJson(Object item, String fieldPath, Deque<Interchangeable> intchStack) 
			throws IllegalArgumentException, IllegalAccessException, 
	NoSuchMethodException, SecurityException, InvocationTargetException, EditorException {
		
		Field[] itemFields = item.getClass().getDeclaredFields();
		JsonBean itemJson = new JsonBean();
		
		// iterate the fields of the object and generate the JSON
		for (Field itemField : itemFields) {
			readFieldValue(itemField, item, item, itemJson, fieldPath, intchStack);	
		}
		
		return itemJson;
	}
	
	/**
	 * Generate the composite values. E.g: we have a list of sectors, 
	 * in JSON the list should be written by classification 
	 * (primary programs, secondary programs, etc.)
	 * @param field the instance of the field
	 * @param fieldInstance the object of the field
	 * @param resultJson object JSON containing the value of the item
	 * @param fieldPath the underscorified path to the field currently exported 
	 */
	private void generateCompositeValues(Field field, Object object, String fieldPath, 
			Deque<Interchangeable> intchStack, JsonBean resultJson) throws IllegalArgumentException, 
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
		
		intchStack.push(interchangeable);
		
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
				boolean isRealCollection = true;
				Object ref = compositeCollection.iterator().next();
				if (ref instanceof AmpCategoryValue && compositeMapSettings.get(((AmpCategoryValue) ref).getAmpCategoryClass().getKeyName()).pickIdOnly()) {
					isRealCollection = false;
				}
				for (Object obj : compositeCollection) {
					String discOption = null;
					if (obj instanceof AmpActivitySector) {
						discOption = ((AmpActivitySector) obj).getClassificationConfig().getName();
					} else if (obj instanceof AmpActivityProgram) {
						discOption = ((AmpActivityProgram) obj).getProgramSetting().getName();
					} else if (obj instanceof AmpCategoryValue) {
						AmpCategoryValue catVal = (AmpCategoryValue) obj;
						discOption = catVal.getAmpCategoryClass().getKeyName();
						// we may need to move up for all composites, but so far applies to ACV, 
						// so keeping here to avoid side effects in rush changes
						if (!isRealCollection) {
							compositeMap.put(catVal.getAmpCategoryClass().getKeyName(), catVal.getId());
						}
						//TODO we have to manage when the ActivityBudet is not present (Budget Unallocated)
					} else if (obj instanceof AmpOrgRole) {
						discOption = ((AmpOrgRole) obj).getRole().getRoleCode();
					} else if (obj instanceof AmpActivityContact) {
						discOption = ((AmpActivityContact) obj).getContactType();
					} else if (obj instanceof AmpFundingAmount) {
						discOption = "" + ((AmpFundingAmount) obj).getFunType().ordinal();
					}
					
					String filteredFieldPath = filteredFieldsMap.get(discOption);
					if (isRealCollection) {
						Interchangeable current = compositeMapSettings.get(discOption);
						if (current != null) intchStack.push(current);
						((List<JsonBean>) compositeMap.get(discOption)).add(
								getObjectJson(obj, filteredFieldPath, intchStack));
						if (current != null) intchStack.pop();
					}
				}
			}
		}
		
		// put in the result JSON the generated structure
		for (Interchangeable setting : settings) {
			intchStack.push(setting);
			String fieldTitle = InterchangeUtils.underscorify(setting.fieldTitle());
			if (isFiltered(fieldTitle) && FMVisibility.isVisible(setting.fmPath(), intchStack)) {
				resultJson.set(fieldTitle, compositeMap.get(setting.discriminatorOption()));
			}
			intchStack.pop();
		}
		intchStack.pop();
	}
	
	/**
	 * 
	 * @param filteredFieldPath the underscorified path to the field
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
