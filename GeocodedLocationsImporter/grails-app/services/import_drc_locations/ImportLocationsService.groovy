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

    def processADM1(def params) {
        log.info("process")
        // Get ids for these category values.
        Integer implementationLevelCategoryId = getImplementationLevelCategoryId()
        Integer implementationLocationCategoryId = getImplementationLocationCategoryId()
        Integer implementationLevelProvinceId = getImplementationLevelProvinceId(implementationLevelCategoryId)
        Integer implementationLocationAMD1Id = getImplementationLocationADM1(implementationLocationCategoryId)
        Integer implementationLocationAMD2Id = getImplementationLocationADM2(implementationLocationCategoryId)
        Integer implementationLocationAMD3Id = getImplementationLocationADM3(implementationLocationCategoryId)

        Set<String> processedIds = new HashSet<String>()
        List<String> generatedSQL = new ArrayList<String>()
        List<String> errors = new ArrayList<String>()
        CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(((CommonsMultipartFile) params.myFile).getInputStream())), ";".toCharacter())
        String[] currentRow;
        int i = 0;
        while ((currentRow = reader.readNext()) != null) {
            if (i > 0) {
                List<String> locationsForCurrentId = new ArrayList<String>()
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
                            implementationLevelProvinceId, implementationLocationAMD1Id, implementationLocationAMD2Id, implementationLocationAMD3Id, generatedSQL, errors)
                }
            }
            i++
        }
        return [generatedSQL: generatedSQL, errors: errors]
    }

    def importLocations(String id, List<String> locations, Integer implementationLevel, Integer implementationLocation, Integer implementationLevelProvinceId,
                        Integer implementationLocationAMD1Id, Integer implementationLocationAMD2Id, Integer implementationLocationAMD3Id, List<String> generatedSQL, List<String> errors) {
        def activity = null
        try {
            //TODO: Revisar que no esté cargando una location ya exixtente (para eso tengo q buscar en la tabla amp_location.location_id? los ids de estas locations).
            Session session = sessionFactory.currentSession
            // 1) Look for the project in amp_activity.
            String strQuery = "SELECT amp_activity_id FROM amp_activity WHERE amp_id LIKE '{1}'"
            strQuery = strQuery.replace("{1}", id)
            SQLQuery sqlQuery = session.createSQLQuery(strQuery)
            activity = sqlQuery.uniqueResult()
            if (activity) {
                log.info(activity + " - locations in file: " + locations.size())
                generatedSQL << "/* Activity ID: ${activity} - AMP ID: ${id} */"
                // 2) Get current implementation/location levels.
                strQuery = "SELECT amp_categoryvalue_id FROM amp_activities_categoryvalues WHERE amp_activity_id = ${activity} AND amp_categoryvalue_id IN (SELECT id FROM amp_category_value WHERE amp_category_class_id = ${implementationLevel});"
                Integer currentImplementationLevelId = session.createSQLQuery(strQuery).uniqueResult()
                strQuery = "SELECT amp_categoryvalue_id FROM amp_activities_categoryvalues WHERE amp_activity_id = ${activity} AND amp_categoryvalue_id IN (SELECT id FROM amp_category_value WHERE amp_category_class_id = ${implementationLocation});"
                Integer currentImplementationLocationId = session.createSQLQuery(strQuery).uniqueResult()

                // 3) Look for existing locations.
                strQuery = "SELECT * FROM amp_activity_location WHERE amp_activity_id = {1}"
                strQuery = strQuery.replace("{1}", activity.toString())
                sqlQuery = session.createSQLQuery(strQuery)
                List ampActivityLocations = sqlQuery.list()

                // 4) Decide if we are going to add locations up to ADM1 or ADM2.
                int ADMLevelToAdd = 0
                if (locations?.find { it[5].equals("ADM2") }?.size() > 0) {
                    ADMLevelToAdd = 2
                } else if (locations?.find { it[5].equals("ADM1") }?.size() > 0) {
                    ADMLevelToAdd = 1
                }

                // 5) Get actual ADM level for this activity.
                int currentADMLevel = 0
                if (currentImplementationLevelId?.equals(implementationLevelProvinceId)) {
                    if (currentImplementationLocationId?.equals(implementationLocationAMD2Id)) {
                        currentADMLevel = 2
                    } else if (currentImplementationLocationId?.equals(implementationLocationAMD1Id)) {
                        currentADMLevel = 1
                    }
                }

                // 6) Iterate the list of locations to add, ignoring existent ones and keep a total count to calculate the percentage.
                // Get list of existing locations with location_id (it comes in the source file so we dont have to match by location name).
                strQuery = "SELECT al.location_id, aal.* FROM amp_activity_location aal, amp_location al WHERE aal.amp_location_id = al.amp_location_id AND amp_activity_id = ${activity}"
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
                        generatedSQL << "INSERT INTO amp_activity_location(amp_activity_location_id, amp_activity_id, amp_location_id, location_percentage, location_latitude, location_longitude) VALUES(nextval('amp_activity_location_seq'), ${activity}, ${ampLocationId}, 0, ${newLoc[3].replace(",", ".")}, ${newLoc[4].replace(",", ".")});"
                        totalLocations++
                        // Find
                        //TODO: Complicar todo esto controlando q la location nueva no tenga cargado al padre, en ese caso:
                        // 1) si solo agrego 1 hijo de ese padre tengo q sacar el padre y meter el hijo nuevo y relinkear los fundings al hijo.
                        // 2) si agrego 2 hijos --> no se q hacer con los fundings, porque no los voy a linkear a los 2 hijos!
                        // Me imagino q si no hay regional fundings para el padre q voy a reemplazar --> no hay problema, else informarlo.
                        // Cuesta un poco más pero no es tan dificil controlar si el padre de lo q voy a agregar ahora esta presente en la actividad.
                    }
                }
                if (totalLocations > currentLocations.size()) {
                    int newPercentage = 100 / totalLocations
                    float round = (newPercentage * totalLocations != 100) ? (100 - (newPercentage * totalLocations)) : 0
                    generatedSQL << "UPDATE amp_activity_location SET location_percentage = ${newPercentage} WHERE amp_activity_id = ${activity};"
                    if (round > 0) {
                        generatedSQL << "UPDATE amp_activity_location SET location_percentage = ${newPercentage + round} WHERE amp_activity_id = ${activity} AND amp_location_id = ${ampLocationId};"
                    }
                }

                // 7) Update Implementation Level if needed.
                if (ADMLevelToAdd != currentADMLevel) {
                    if (!currentImplementationLevelId.equals(implementationLevelProvinceId)) {
                        generatedSQL << "DELETE FROM amp_activities_categoryvalues WHERE amp_activity_id = ${activity} AND amp_categoryvalue_id = ${currentImplementationLevelId};"
                        generatedSQL << "INSERT INTO amp_activities_categoryvalues(amp_activity_id, amp_categoryvalue_id) VALUES(${activity}, ${implementationLevelProvinceId});"
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
                        generatedSQL << "DELETE FROM amp_activities_categoryvalues WHERE amp_activity_id = ${activity} AND amp_categoryvalue_id = ${currentImplementationLocationId};"
                        generatedSQL << "INSERT INTO amp_activities_categoryvalues(amp_activity_id, amp_categoryvalue_id) VALUES(${activity}, ${auxVal});"
                    }
                }
            } else {
                log.warn("WARNING: Cant find id: " + id)
            }
        } catch (Exception e) {
            String message = "id: ${activity} - ampId: '${id}' - " + e
            log.error(message)
            errors << message
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
}