import { ActivityConstants } from "amp-ui";

export default class HydratorHelper {
    static fetchRequestDataForHydration(object, fieldsManager, parent) {
        const requestData = {};
        HydratorHelper.hydrateObject(object, fieldsManager, parent, requestData);
        return requestData;
    }
    static hydrateObject(objectToHydrate, fieldsManager, parent, requestData, valuesForHydration) {
        function _hydratorHelper(objectToHydrate, key) {
            let newParent = '';
            if (parent !== '') {
                newParent = parent + "~";
            }
            newParent = newParent + key;
            HydratorHelper.hydrateObject(objectToHydrate, fieldsManager, newParent, requestData, valuesForHydration);
        }
        Object.keys(objectToHydrate).forEach(objectField => {
            if (objectToHydrate[objectField] instanceof Object) {
                if (Array.isArray(objectToHydrate[objectField])) {
                    objectToHydrate[objectField].forEach(child => _hydratorHelper(child, objectField));
                } else {
                    _hydratorHelper(objectToHydrate[objectField], objectField);
                }
            } else {
                let fieldToHydrate = '';
                if (parent !== '') {
                    fieldToHydrate = parent + "~";
                }
                fieldToHydrate = fieldToHydrate + objectField;

                const fieldDefinition = fieldsManager.getFieldDef(fieldToHydrate);
                if (fieldDefinition) {
                    if (fieldDefinition['id_only'] === true) {
                        if (objectToHydrate[objectField]) {
                            if (!valuesForHydration) {
                                //If we don't have values for hydration, means that we need to fill on RequestData what fields we need to hydrate
                                if (requestData[fieldToHydrate]) {
                                    requestData[fieldToHydrate].push(objectToHydrate[objectField]);
                                } else {
                                    requestData[fieldToHydrate] = [(objectToHydrate[objectField])];
                                }
                            } else {
                                objectToHydrate[objectField] = valuesForHydration[fieldToHydrate]
                                    .find(field => field.id === objectToHydrate[objectField]);
                            }

                            if (objectToHydrate[objectField]['ancestor-values']) {
                                objectToHydrate[objectField][ActivityConstants.HIERARCHICAL_VALUE] =
                                    objectToHydrate[objectField]['ancestor-values'].map(i=>'[' + i + ']').join('');
                                objectToHydrate[objectField][ActivityConstants.HIERARCHICAL_VALUE_DEPTH] =
                                    objectToHydrate[objectField]['ancestor-values'] ? objectToHydrate[objectField]['ancestor-values'].length : 0;
                            }
                            if (objectToHydrate[objectField].translatedValue) {
                                objectToHydrate[objectField]['translated-value'] = objectToHydrate[objectField].translatedValue;
                            }
                        }
                    }
                } else {
                    console.warn('no field def: ' + fieldToHydrate);
                }
            }
        });
    }
}
