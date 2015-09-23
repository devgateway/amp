package import_drc_locations

import au.com.bytecode.opencsv.CSVReader
import grails.transaction.Transactional
import org.hibernate.SQLQuery
import org.hibernate.Session
import org.springframework.web.multipart.commons.CommonsMultipartFile

@Transactional
class ImportLocationsService {

    def sessionFactory

    // IMPORTANT: Before starting check these strings in the DB your are going to update.
    private static String IMPLEMENTATION_LEVEL_KEY = "implementation_level"
    private static String IMPLEMENTATION_LOCATION_KEY = "implementation_location"
    private static String IMPLEMENTATION_LEVEL_STATE = "Provincial"
    private static String ADM1_CATEGORY = "Region"
    private static String ADM2_CATEGORY = "Zone"
    private static String ADM3_CATEGORY = "District"
    private static String ADM4_CATEGORY = "Sector Location"

    def processADM1(def params) {
        log.info("process")
        // Get ids for these category values.
        Integer implementationLevelCategoryId = getImplementationLevelCategoryId()
        Integer implementationLocationCategoryId = getImplementationLocationCategoryId()
        Integer implementationLevelProvinceId = getImplementationLevelProvinceId(implementationLevelCategoryId)
        Integer implementationLocationAMD1Id = getImplementationLocationADM1(implementationLocationCategoryId)
        Integer implementationLocationAMD2Id = getImplementationLocationADM2(implementationLocationCategoryId)
        Integer implementationLocationAMD3Id = getImplementationLocationADM3(implementationLocationCategoryId)
        Integer implementationLocationAMD4Id = getImplementationLocationADM4(implementationLocationCategoryId)

        Set<String> processedIds = new HashSet<String>()
        List<String> generatedSQL = new ArrayList<String>()
        List<String> errors = new ArrayList<String>()
        CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(((CommonsMultipartFile) params.myFile).getInputStream())), ";".toCharacter())
        String[] currentRow;
        int i = 0;
        while ((currentRow = reader.readNext()) != null) {
            if (i > 0) {
                List<String[]> locationsForCurrentId = new ArrayList<String[]>()
                String currentAMPId = currentRow[0]
                if (!processedIds.contains(currentAMPId)) {
                    processedIds << currentAMPId
                    // Look for the same id anywhere in the source file (most of the time is not sorted).
                    int j = 0;
                    CSVReader secondReader = new CSVReader(new BufferedReader(new InputStreamReader(((CommonsMultipartFile) params.myFile).getInputStream())), ";".toCharacter())
                    String[] secondAuxRow;
                    while ((secondAuxRow = secondReader.readNext()) != null) {
                        if (j >= i) {
                            if (currentAMPId.equals(secondAuxRow[0])) {
                                locationsForCurrentId << secondAuxRow
                            }
                        }
                        j++
                    }
                    // Having all locations for a project, now do the import.
                    importLocations(currentAMPId, locationsForCurrentId, implementationLevelCategoryId, implementationLocationCategoryId,
                            implementationLevelProvinceId, implementationLocationAMD1Id, implementationLocationAMD2Id, implementationLocationAMD3Id, implementationLocationAMD4Id, generatedSQL, errors)
                }
            }
            i++
        }
        return [generatedSQL: generatedSQL, errors: errors]
    }

    // NOTES: Table amp_location doesnt have the structure for ADM3 locations, only for ADM1 and ADM2 by mapping location_id to region_location_id. For ADM3 structure use amp_categoryvalues_location table.
    def importLocations(String id, List<String[]> locations, Integer implementationLevel, Integer implementationLocation, Integer implementationLevelProvinceId,
                        Integer implementationLocationAMD1Id, Integer implementationLocationAMD2Id, Integer implementationLocationAMD3Id, Integer implementationLocationAMD4Id, List<String> generatedSQL, List<String> errors) {
        def activity = null
        Boolean error
        List<String> activityGeneratedSQL
        try {
            Session session = sessionFactory.currentSession
            // 1) Look for the project in amp_activity.
            String strQuery = "SELECT amp_activity_id FROM amp_activity WHERE amp_id LIKE '{1}'"
            strQuery = strQuery.replace("{1}", id)
            SQLQuery sqlQuery = session.createSQLQuery(strQuery)
            activity = sqlQuery.uniqueResult()
            if (activity) {
                activityGeneratedSQL = new ArrayList<String>()
                error = false
                log.info(activity + " - locations in file: " + locations.size())
                // 2) Get current implementation/location levels.
                strQuery = "SELECT amp_categoryvalue_id FROM amp_activities_categoryvalues WHERE amp_activity_id = ${activity} AND amp_categoryvalue_id IN (SELECT id FROM amp_category_value WHERE amp_category_class_id = ${implementationLevel});"
                Integer currentImplementationLevelId = session.createSQLQuery(strQuery).uniqueResult()
                strQuery = "SELECT amp_categoryvalue_id FROM amp_activities_categoryvalues WHERE amp_activity_id = ${activity} AND amp_categoryvalue_id IN (SELECT id FROM amp_category_value WHERE amp_category_class_id = ${implementationLocation});"
                Integer currentImplementationLocationId = session.createSQLQuery(strQuery).uniqueResult()

                // 3) Look for existing locations.
                strQuery = "SELECT al.location_id, al.name, al.amp_location_id FROM amp_activity_location aal, amp_location al WHERE aal.amp_location_id = al.amp_location_id AND amp_activity_id = {1}"
                strQuery = strQuery.replace("{1}", activity.toString())
                sqlQuery = session.createSQLQuery(strQuery)
                List ampActivityLocations = sqlQuery.list()

                // 4) Cleanup the list of locations to import: As always we can not trust anything done by a user, that includes checking if the new locations have both child and parent locations (WRONG),
                // new locations that are parent of existing locations (WRONG), etc, etc.
                locations = cleanupLocations(locations, ampActivityLocations, errors)
                if (locations.size() == 0) {
                    errors << "WARNING: No locations to add after cleanup. ID: ${activity} - AMP ID: ${id}"
                    return
                }
                activityGeneratedSQL << "/* Activity ID: ${activity} - AMP ID: ${id} */"

                // 5) Decide if we are going to add locations up to ADM1 or ADM2.
                int ADMLevelToAdd = 0
                if (locations?.find { it[5].equals("ADM2") }?.size() > 0) {
                    ADMLevelToAdd = 2
                } else if (locations?.find { it[5].equals("ADM1") }?.size() > 0) {
                    ADMLevelToAdd = 1
                }

                // 6) Get actual ADM level for this activity.
                int currentADMLevel = 0
                if (currentImplementationLevelId?.equals(implementationLevelProvinceId)) {
                    if (currentImplementationLocationId?.equals(implementationLocationAMD2Id)) {
                        currentADMLevel = 2
                    } else if (currentImplementationLocationId?.equals(implementationLocationAMD1Id)) {
                        currentADMLevel = 1
                    }
                }

                // 7) Iterate the list of locations to add, ignoring existent ones and keep a total count to calculate the percentage.
                // Get list of existing locations with location_id (it comes in the source file so we dont have to match by location name).
                strQuery = "SELECT al.location_id, aal.* FROM amp_activity_location aal, amp_location al WHERE aal.amp_location_id = al.amp_location_id AND amp_activity_id = ${activity}"
                //TODO: Check why we get the list of current locations twice.
                List currentLocations = session.createSQLQuery(strQuery).list()
                int totalLocations = currentLocations.size()
                Long ampLocationId = null
                locations.each { newLoc ->
                    if (!currentLocations.find { oldLoc -> newLoc[7].toString().equals(oldLoc[0].toString()) }) {
                        // Find amp_location_id.
                        strQuery = "SELECT amp_location_id FROM amp_location WHERE location_id = ${newLoc[7].toString()};"
                        ampLocationId = session.createSQLQuery(strQuery).uniqueResult()
                        if (!ampLocationId) {
                            throw new Exception("ERROR: Cant find amp_location with location_id: ${newLoc[7].toString()}")
                        }

                        // 8) If the current new location has its own parent in the project then we need to delete the parent or the ActivityForm will complain. The potential problem is the existing parent location might already have regional fundings,
                        // in that case we cant continue since regional fundings are linked to ADM1 only.
                        Boolean isParentLocationPresent = false
                        def oldLocationToDelete = null
                        ampActivityLocations?.each { oldLocation ->
                            if (findParentLocationRecursively(oldLocation[0].toString(), newLoc[7])) {
                                isParentLocationPresent = true
                                oldLocationToDelete = oldLocation
                                //No need to insert here the DELETE clause over the parent because by definition we cant find more than one parent by any location in the database (with different ADMx levels).
                            }
                        }
                        if (!isParentLocationPresent) {
                            activityGeneratedSQL << "INSERT INTO amp_activity_location(amp_activity_location_id, amp_activity_id, amp_location_id, location_percentage, location_latitude, location_longitude) VALUES(nextval('amp_activity_location_seq'), ${activity}, ${ampLocationId}, 0, ${newLoc[3].replace(",", ".")}, ${newLoc[4].replace(",", ".")});"
                            totalLocations++
                        } else {
                            // Ok, the parent is in the activity too, lets check if the parent REGION (ADM1) location has regional fundings.
                            Long parentRegionLocation = findParentLocationAtRegionLevel(ampLocationId)
                            def fundings = findRegionalFundings(((BigInteger) activity).toLong(), parentRegionLocation)
                            if (!fundings) {
                                activityGeneratedSQL << "INSERT INTO amp_activity_location(amp_activity_location_id, amp_activity_id, amp_location_id, location_percentage, location_latitude, location_longitude) VALUES(nextval('amp_activity_location_seq'), ${activity}, ${ampLocationId}, 0, ${newLoc[3].replace(",", ".")}, ${newLoc[4].replace(",", ".")});"
                                activityGeneratedSQL << "DELETE FROM amp_activity_location WHERE amp_activity_location_id = ${activity} AND amp_location_id = ${oldLocationToDelete[2].toString()};"
                            } else {
                                errors << "WARNING: Can not insert location because it has a parent ADM1 (region) with Regional Fundings: ${newLoc}"
                                error = true
                            }
                        }
                    } else {
                        errors << "WARNING: This location exists in the database: ${newLoc}"
                    }
                }
                if (totalLocations > currentLocations.size()) {
                    int newPercentage = 100 / totalLocations
                    float round = (newPercentage * totalLocations != 100) ? (100 - (newPercentage * totalLocations)) : 0
                    activityGeneratedSQL << "UPDATE amp_activity_location SET location_percentage = ${newPercentage} WHERE amp_activity_id = ${activity};"
                    if (round > 0) {
                        activityGeneratedSQL << "UPDATE amp_activity_location SET location_percentage = ${newPercentage + round} WHERE amp_activity_id = ${activity} AND amp_location_id = ${ampLocationId};"
                    }
                }

                // 8) Update Implementation Level if needed.
                if (ADMLevelToAdd != currentADMLevel) {
                    if (!currentImplementationLevelId.equals(implementationLevelProvinceId)) {
                        activityGeneratedSQL << "DELETE FROM amp_activities_categoryvalues WHERE amp_activity_id = ${activity} AND amp_categoryvalue_id = ${currentImplementationLevelId};"
                        activityGeneratedSQL << "INSERT INTO amp_activities_categoryvalues(amp_activity_id, amp_categoryvalue_id) VALUES(${activity}, ${implementationLevelProvinceId});"
                    }
                    int auxVal = 0
                    switch (ADMLevelToAdd) {
                        case 1:
                            auxVal = implementationLocationAMD1Id
                            break
                        case 2:
                            auxVal = implementationLocationAMD2Id
                            break
                        default:
                            throw new Exception("Wrong ADMLevelToAdd with value: " + ADMLevelToAdd)
                    }
                    if (currentImplementationLocationId != auxVal) {
                        activityGeneratedSQL << "DELETE FROM amp_activities_categoryvalues WHERE amp_activity_id = ${activity} AND amp_categoryvalue_id = ${currentImplementationLocationId};"
                        activityGeneratedSQL << "INSERT INTO amp_activities_categoryvalues(amp_activity_id, amp_categoryvalue_id) VALUES(${activity}, ${auxVal});"
                    }
                }
            } else {
                String warning = "ERROR: Cant find id: " + id + " nothing will be added."
                log.warn(warning)
                errors << warning
                error = true
            }
        } catch (Exception e) {
            String message = "ERROR: id: ${activity} - ampId: '${id}' - " + e
            log.error(message)
            errors << message
            error = true
        }
        if (!error) {
            generatedSQL.addAll(activityGeneratedSQL)
        }
    }

    Integer getImplementationLevelCategoryId() {
        Session session = sessionFactory.currentSession
        def implementationLevelId = session.createSQLQuery("SELECT id FROM amp_category_class WHERE keyname LIKE '%${IMPLEMENTATION_LEVEL_KEY}%'").uniqueResult()
        return implementationLevelId
    }

    Integer getImplementationLocationCategoryId() {
        Session session = sessionFactory.currentSession
        def implementationLocationId = session.createSQLQuery("SELECT id FROM amp_category_class WHERE keyname LIKE '%${IMPLEMENTATION_LOCATION_KEY}%'").uniqueResult()
        return implementationLocationId
    }

    Integer getImplementationLevelProvinceId(Integer id) {
        Session session = sessionFactory.currentSession
        def provinceId = session.createSQLQuery("SELECT id FROM amp_category_value WHERE category_value LIKE '${IMPLEMENTATION_LEVEL_STATE}' AND amp_category_class_id = " + id).uniqueResult()
        return provinceId
    }

    Integer getImplementationLocationADM1(Integer id) {
        Session session = sessionFactory.currentSession
        def provinceId = session.createSQLQuery("SELECT id FROM amp_category_value WHERE category_value LIKE '${ADM1_CATEGORY}' AND amp_category_class_id = " + id).uniqueResult()
        return provinceId
    }

    Integer getImplementationLocationADM2(Integer id) {
        Session session = sessionFactory.currentSession
        def provinceId = session.createSQLQuery("SELECT id FROM amp_category_value WHERE category_value LIKE '${ADM2_CATEGORY}' AND amp_category_class_id = " + id).uniqueResult()
        return provinceId
    }

    Integer getImplementationLocationADM3(Integer id) {
        Session session = sessionFactory.currentSession
        def provinceId = session.createSQLQuery("SELECT id FROM amp_category_value WHERE category_value LIKE '${ADM3_CATEGORY}' AND amp_category_class_id = " + id).uniqueResult()
        return provinceId
    }

    Integer getImplementationLocationADM4(Integer id) {
        Session session = sessionFactory.currentSession
        def provinceId = session.createSQLQuery("SELECT id FROM amp_category_value WHERE category_value LIKE '${ADM4_CATEGORY}' AND amp_category_class_id = " + id).uniqueResult()
        return provinceId
    }

    /**
     * This method will cleanup the list of new locations by deleting from that list those new locations that are A) parent of other new locations or B) parent of existing locations.
     * NOTE: This method WILL NOT generate any sql sentence.
     * @param locations
     * @param existingLocations
     * @param errors
     * @return
     */
    List<String> cleanupLocations(List<String[]> locations, List existingLocations, List<String> errors) {
        List<String[]> auxLocations = locations
        List<String[]> deletedLocations = new ArrayList<String[]>()
        // 1) Find parent/child locations, notice table amp_location can not be used to get the parent/child structure for ADM3 or above, only for ADM2 (by checking against region_location_id)
        // If location is ADM3 or above you need to link to amp_categoryvalue_location by amp_location.location_id.
        locations?.sort { it[5].trim().toUpperCase() }?.reverse()?.each {
            boolean found = false
            auxLocations?.each { it2 ->
                // Now we need to check if these 2 locations are related.
                if (findParentLocationRecursively(it[7], it2[7])) {
                    found = true
                }
            }
            if (found) {
                // We can not safely add the new location because it is a parent of other existing location.
                errors << "WARNING: Location will be IGNORED because a child ADM2 was also coded in the source file: ${it.toArrayString()}"
                deletedLocations << it
            }
        }
        deletedLocations.each {
            locations.remove(it)
        }

        // 2) If a new location have less precision than an existing location (ie: new location is ADM2 from existing ADM3 location) then we have to ignore it.
        locations.each { newLocation ->
            // Same check but recursively through all the structure (ie: to detect if ADM2: Tanganika is parent of ADM4: Nyemba... spoiler alert, it is).
            boolean found = false
            Integer newLocationLevel = sessionFactory.currentSession.createSQLQuery("SELECT acv.index_column FROM amp_category_value acv, amp_category_value_location acvl WHERE acvl.parent_category_value = acv.id AND acvl.id = ${newLocation[7]};").uniqueResult()
            existingLocations?.each { existingLocation ->
                Integer existingLocationLevel = sessionFactory.currentSession.createSQLQuery("SELECT acv.index_column FROM amp_category_value acv, amp_category_value_location acvl WHERE acvl.parent_category_value = acv.id AND acvl.id = ${existingLocation[0]};").uniqueResult()
                // These check is for decreasing the calls to the recursive function.
                if (newLocationLevel < existingLocationLevel) {
                    // Now we need to check if the new location is parent of the existent location, in that case remove it.
                    if (findParentLocationRecursively(newLocation[7], existingLocation[0].toString())) {
                        found = true
                    }
                }
            }
            if (found) {
                // We can not safely add the new location because it is a parent of other existing location.
                errors << "WARNING: Location will be IGNORED because is a parent of an existent location (2): ${newLocation.toArrayString()}"
                deletedLocations << newLocation
            }
        }
        deletedLocations.each {
            auxLocations.remove(it)
        }
        return auxLocations
    }

    Long findParentLocation(Long id) {
        Long parent = null
        parent = sessionFactory.currentSession.createSQLQuery("SELECT acvl.parent_location FROM amp_category_value_location acvl WHERE acvl.id = ${id};").uniqueResult()
        return parent
    }

    Long findParentLocationAtRegionLevel(Long id) {
        Long parent = null
        parent = sessionFactory.currentSession.createSQLQuery("SELECT region_location_id FROM amp_location WHERE amp_location_id = ${id};").uniqueResult()
        return parent
    }

    // IMPORTANT: Only ADM1 locations can have regional fundings, so we cant relink to higher levels of precision like ADM2/3.
    def findRegionalFundings(Long id, Long region) {
        def fundings = sessionFactory.currentSession.createSQLQuery("SELECT * FROM amp_regional_funding WHERE activity_id = ${id} AND region_location_id = ${region};").uniqueResult()
        return fundings
    }

    /**
     * This method returns true if locA is parent of locB (not only the immediate parent but on any level).
     * @param locA is the location that could be parent.
     * @param locB is the location that could be child.
     * @return
     */
    Boolean findParentLocationRecursively(String locA, String locB) {
        Long parentLocationId = findParentLocation(new Long(locB))
        if (parentLocationId == null || parentLocationId == 0l) {
            return false
        }
        if (parentLocationId.toString().equals(locA)) {
            return true
        } else {
            return findParentLocationRecursively(locA, parentLocationId.toString())
        }
    }
}