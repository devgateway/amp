package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

/**
 * Validates that both parent and child items are not present in the collection
 *
 * @author Viorel Chihai
 */
public class TreeCollectionValidator extends InputValidator {

    private static final Logger logger = Logger.getLogger(TreeCollectionValidator.class);

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.FIELD_PARENT_CHILDREN_NOT_ALLOWED;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent, APIField fieldDescription,
            String fieldPath) {
        String fieldName = fieldDescription.getFieldName();
        boolean treeCollectionField = Boolean.TRUE.equals(fieldDescription.getTreeCollectionConstraint());
        String uniqueField = fieldDescription.getUniqueConstraint();

        if (treeCollectionField) {
            Collection<Map<String, Object>> fieldValue = (Collection<Map<String, Object>>) newFieldParent
                    .get(fieldName);
            Set<Long> idValues = new HashSet<Long>();

            if (fieldValue != null && fieldValue.size() > 1) {
                if (StringUtils.isBlank(uniqueField)) {
                    // See AMP-29028 status or ensure to clarify if it is valid to allow duplicate entries
                    throw new RuntimeException("Cannot validate the tree collection for '" + fieldPath
                            + "'. Please enable 'unique' validator for this field or implement support for "
                            + "tree collection validation without unique constraint.");
                }

                idValues = getUniqueValues(fieldValue, uniqueField);

                return isTreeCollectionValid(fieldDescription.getApiType().getType(), idValues);
            }
        }

        return true;
    }

    /**
     * Returns a Set of unique id values from the Collection <JsonBean>
     * containing the fieldPathToId
     *
     * @param fieldValues the Collection <JsonBean> with a list of fields from
     * which the
     * @param fieldPathToId String path of the parent.
     * @return collection with unique id values
     */
    private Set<Long> getUniqueValues(Collection<Map<String, Object>> fieldValues, String fieldPathToId) {
        Set<Long> idValuesSet = new HashSet<Long>();
        for (Map<String, Object> child : fieldValues) {
            if (child.get(fieldPathToId) != null) {
                String childInput = String.valueOf(child.get(fieldPathToId));
                if (NumberUtils.isNumber(childInput)) {
                    idValuesSet.add(new Long(childInput));
                }
            }
        }

        return idValuesSet;
    }

    /**
     * @param clazz collection element type
     * @param idValuesSet the Set containing unique id values
     * @return boolean if the collection does not contains children and parents
     */
    private boolean isTreeCollectionValid(Class<?> clazz, Set<Long> idValuesSet) {
        Set<Long> tmpIdValues = new HashSet<Long>();
        tmpIdValues.addAll(idValuesSet);

        for (Long id : idValuesSet) {
            tmpIdValues.remove(id);

            if (clazz.equals(AmpActivitySector.class) && isPresentParentSectorInCollection(id, tmpIdValues)) {
                return false;
            } else if (clazz.equals(AmpActivityProgram.class) && isPresentParentProgramInCollection(id, tmpIdValues)) {
                return false;
            } else if (clazz.equals(AmpActivityLocation.class)
                    && isPresentParentLocationInCollection(id, tmpIdValues)) {
                return false;
            }

            tmpIdValues.add(id);
        }

        return true;
    }

    /**
     * @param sectorId of the sector
     * @param idValues the collection containing sectors id form the request
     * @return boolean if isPresent parents of the location in collection
     */
    private boolean isPresentParentSectorInCollection(Long sectorId, Set<Long> idValues) {
        Collection<AmpSector> parentSectors = SectorUtil.getAmpParentSectors(sectorId);

        for (AmpSector t : parentSectors) {
            if (idValues.contains(t.getAmpSectorId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param programId of the program
     * @param idValues the collection containing programs id form the request
     * @return boolean if isPresent parents of the program in collection
     */
    private boolean isPresentParentProgramInCollection(Long programId, Set<Long> idValues) {
        try {
            Collection<AmpTheme> parentThemes = ProgramUtil.getAncestorThemes(ProgramUtil.getThemeById(programId));

            for (AmpTheme t : parentThemes) {
                if (idValues.contains(t.getAmpThemeId())) {
                    return true;
                }
            }
        } catch (DgException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return false;
    }

    /**
     * @param locationId of the location
     * @param idValues the collection containing locations id form the request
     * @return boolean if isPresent parents of the location in collection
     */
    private boolean isPresentParentLocationInCollection(Long locationId, Set<Long> idValues) {
        Session session = InterchangeUtils.getSessionWithPendingChanges();
        AmpLocation oldLocation = null;
        try {
            oldLocation = (AmpLocation) session.load(AmpLocation.class, locationId);
        } catch (ObjectNotFoundException ex) {
            // invalid location cannot be checked
            logger.error(ex.getMessage());
            return false;
        }

        Collection<AmpCategoryValueLocations> parentLocations = getParentLocations(oldLocation.getLocation());

        for (AmpCategoryValueLocations t : parentLocations) {
            if (idValues.contains(t.getId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param location of type AmpCategoryValueLocations
     * @return returnList the ArrayList<AmpCategoryValueLocations> with all
     * parents of the location
     */
    private static List<AmpCategoryValueLocations> getParentLocations(AmpCategoryValueLocations location) {
        ArrayList<AmpCategoryValueLocations> returnList = new ArrayList<AmpCategoryValueLocations>();

        if (location != null) {
            AmpCategoryValueLocations temp = location;
            while (temp.getParentLocation() != null) {
                temp = temp.getParentLocation();
                returnList.add(0, temp);
            }
        }

        return returnList;
    }
}
