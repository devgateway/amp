package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.editor.exception.EditorException;

/**
 * @author Octavian Ciubotaru
 */
public abstract class ObjectExporter<T> {

    public JsonBean export(T object) {
        JsonBean resultJson = new JsonBean();

        Field[] fields = FieldUtils.getAllFields(getClassOf(object));

        for (Field field : fields) {
            try {
                readFieldValue(field, object, object, resultJson, null, new FEContext());
            } catch (IllegalArgumentException | IllegalAccessException
                    | NoSuchMethodException | SecurityException
                    | InvocationTargetException | EditorException | NoSuchFieldException e) {
                throw new RuntimeException(String.format("Couldn't convert object %s to json.", object), e);
            }
        }

        return resultJson;
    }

    protected Class<?> getClassOf(Object object) {
        return object.getClass();
    }

    /**
     *
     * @param field the instance of the field
     * @param fieldInstance the object of the field
     * @param parentObject the object that contain the field
     * @param resultJson result JSON object which will be filled with the values of the fields
     * @param fieldPath the underscorified path to the field currently exported
     * @return
     * @throws NoSuchFieldException 
     */
    private void readFieldValue(Field field, Object fieldInstance, Object parentObject, JsonBean resultJson,
            String fieldPath, FEContext context) throws IllegalArgumentException, IllegalAccessException,
            NoSuchMethodException, SecurityException, InvocationTargetException, EditorException, NoSuchFieldException {

        Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);

        if (interchangeable != null && !InterchangeUtils.isAmpActivityVersion(field.getType())
                && FMVisibility.isVisible(interchangeable.fmPath(), context.getIntchStack())
                && isInContext(interchangeable, context)) {
            context.getIntchStack().push(interchangeable);
            field.setAccessible(true);
            String fieldTitle = InterchangeUtils.underscorify(interchangeable.fieldTitle());

            String filteredFieldPath = fieldPath == null ? fieldTitle : fieldPath + "~" + fieldTitle;
            Object fieldValue = field.get(fieldInstance);

            if (!interchangeable.pickIdOnly()) {
                // check if the member is a collection

                if (InterchangeUtils.isCompositeField(field)) {
                    generateCompositeValues(field, fieldValue, fieldPath, context, resultJson);
                } else if (isFiltered(filteredFieldPath)) {
                    if (InterchangeUtils.isCollection(field)) {
                        Collection<Object> collectionItems = (Collection<Object>) fieldValue;
                        // if the collection is not empty, it will be parsed and a JSON with member details
                        // will be generated
                        List<JsonBean> collectionJson = new ArrayList<JsonBean>();
                        if (collectionItems != null) {
                            // iterate over the objects of the collection
                            for (Object item : collectionItems) {
                                collectionJson.add(getObjectJson(item, filteredFieldPath, context));
                            }
                        }
                        // put the array with object values in the result JSON
                        resultJson.set(fieldTitle, collectionJson);
                    } else {
                        if (InterchangeableClassMapper.containsSupportedClass(field.getType()) || fieldValue == null) {
                            Class<? extends Object> parentClassName =
                                    parentObject == null ? field.getDeclaringClass() : parentObject.getClass();
                            Object values = InterchangeUtils.getTranslationValues(field, parentClassName, fieldValue, 
                                    parentObject);
                            resultJson.set(fieldTitle, values);
                        } else {
                            Class<? extends PossibleValuesProvider> providerClass =
                                    InterchangeUtils.getPossibleValuesProvider(field);
                            if (providerClass != null) {
                                resultJson.set(InterchangeUtils.underscorify(interchangeable.fieldTitle()),
                                        getJsonValue(providerClass, fieldValue));
                            } else {
                                resultJson.set(fieldTitle, getObjectJson(fieldValue, filteredFieldPath, context));
                            }
                        }
                    }
                }
            } else {
                if (isFiltered(filteredFieldPath)) {
                    Class<? extends PossibleValuesProvider> providerClass =
                            InterchangeUtils.getPossibleValuesProvider(field);
                    if (providerClass != null) {
                        resultJson.set(fieldTitle, getIdValue(providerClass, fieldValue));
                    } else {
                        resultJson.set(fieldTitle, InterchangeUtils.getId(fieldValue));
                    }
                }
            }
            context.getIntchStack().pop();
        }
    }

    /**
     *
     * @param item
     * @param fieldPath the underscorified path to the field currently exported
     * @return itemJson object JSON containing the value of the item
     * @throws NoSuchFieldException 
     */
    private JsonBean getObjectJson(Object item, String fieldPath, FEContext context)
            throws IllegalArgumentException, IllegalAccessException,
            NoSuchMethodException, SecurityException, InvocationTargetException, EditorException, NoSuchFieldException {

        JsonBean itemJson = new JsonBean();
        Field[] itemFields = FieldUtils.getAllFields(item.getClass());

        // iterate the fields of the object and generate the JSON
        for (Field itemField : itemFields) {
            readFieldValue(itemField, item, item, itemJson, fieldPath, context);
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
     * @throws NoSuchFieldException 
     */
    private void generateCompositeValues(Field field, Object object, String fieldPath,
            FEContext context, JsonBean resultJson) throws IllegalArgumentException, IllegalAccessException,
            NoSuchMethodException, SecurityException, InvocationTargetException, EditorException, NoSuchFieldException {

        Interchangeable interchangeable = field.getAnnotation(Interchangeable.class);
        InterchangeableDiscriminator discriminator = field.getAnnotation(InterchangeableDiscriminator.class);
        Interchangeable[] settings = discriminator.settings();

        context.getIntchStack().push(interchangeable);

        Map<String, Object> compositeMap = new HashMap<String, Object>();
        Map<String, Interchangeable> compositeMapSettings = new HashMap<String, Interchangeable>();
        Map<String, String> filteredFieldsMap = new HashMap<String, String>();

        // check that we need to initialize as a collection only real collections
        boolean initAsCollection = InterchangeUtils.isCollection(field)
                && !InterchangeUtils.getGenericClass(field).equals(AmpCategoryValue.class);

        // create the map containing the correlation between the discriminatorOption and the JSON generated objects
        for (Interchangeable setting : settings) {
            // TODO: init settings with defaults from interchangeable
            compositeMap.put(setting.discriminatorOption(), initAsCollection ? new ArrayList<JsonBean>() : null);
            compositeMapSettings.put(setting.discriminatorOption(), setting);
            String fieldTitle = InterchangeUtils.underscorify(setting.fieldTitle());
            String filteredFieldPath = fieldPath == null ? fieldTitle : fieldPath + "~" + fieldTitle;

            filteredFieldsMap.put(setting.discriminatorOption(), filteredFieldPath);
        }

        Class<?> classOfField = InterchangeUtils.getClassOfField(field);

        if (InterchangeUtils.isCollection(field) && object != null) {
            Collection<Object> compositeCollection = (Collection<Object>) object;
            if (compositeCollection.size() > 0) {
                boolean isRealCollection = true;
                Object ref = compositeCollection.iterator().next();
                if (ref instanceof AmpCategoryValue
                        && compositeMapSettings.get(((AmpCategoryValue) ref).getAmpCategoryClass().getKeyName())
                                .pickIdOnly()) {
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
                    } else if (obj instanceof AmpContactProperty) {
                        discOption = ((AmpContactProperty) obj).getName();
                    }

                    String filteredFieldPath = filteredFieldsMap.get(discOption);
                    if (isRealCollection) {
                        Interchangeable current = compositeMapSettings.get(discOption);
                        if (current != null) {
                            context.getIntchStack().push(current);
                            context.getDiscriminationInfoStack().push(new ImmutablePair<>(classOfField, discOption));
                        }
                        ((List<JsonBean>) compositeMap.get(discOption)).add(
                                getObjectJson(obj, filteredFieldPath, context));
                        if (current != null) {
                            context.getDiscriminationInfoStack().pop();
                            context.getIntchStack().pop();
                        }
                    }
                }
            }
        }

        // put in the result JSON the generated structure
        for (Interchangeable setting : settings) {
            context.getIntchStack().push(setting);
            String fieldTitle = InterchangeUtils.underscorify(setting.fieldTitle());
            if (isFiltered(fieldTitle) && FMVisibility.isVisible(setting.fmPath(), context.getIntchStack())) {
                resultJson.set(fieldTitle, compositeMap.get(setting.discriminatorOption()));
            }
            context.getIntchStack().pop();
        }
        context.getIntchStack().pop();
    }

    private boolean isInContext(Interchangeable intch, FEContext context) {
        try {
            ContextMatcher contextMatcher = intch.context().newInstance();
            return contextMatcher.inContext(context);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to check field context.");
        }
    }

    /**
     *
     * @param providerClass Provider class that will be used to load the values of the object
     * @param fieldValue Object which will be used to retrieve the custom value
     * @return object Custom value of the object
     */
    private Object getJsonValue(Class<? extends PossibleValuesProvider> providerClass, Object fieldValue) {
        try {
            PossibleValuesProvider providerObj = providerClass.newInstance();
            return providerObj.toJsonOutput(fieldValue);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to obtain json value.", e);
        }
    }

    private Object getIdValue(Class<? extends PossibleValuesProvider> providerClass, Object fieldValue) {
        try {
            PossibleValuesProvider providerObj = providerClass.newInstance();
            return providerObj.getIdOf(fieldValue);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to obtain id value.", e);
        }
    }

    protected boolean isFiltered(String fieldPath) {
        return true;
    }
}
