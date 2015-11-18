package import_drc_locations

import au.com.bytecode.opencsv.CSVReader
import grails.transaction.Transactional
import org.hibernate.SQLQuery
import org.hibernate.Session
import org.springframework.web.multipart.commons.CommonsMultipartFile

@Transactional
class ImportStructuresService {

    def sessionFactory

    def processStructures(def params) {
        log.info("process")
        Set<String> processedIds = new HashSet<String>()
        List<String> generatedSQL = new ArrayList<String>()
        List<String> errors = new ArrayList<String>()
        generatedSQL << "INSERT INTO amp_structure_type(typeid, name, graphictype, iconfile) VALUES(99, 'Imported', NULL, NULL);"
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
                    importStructures(currentAMPId, locationsForCurrentId, generatedSQL, errors)
                }
            }
            i++
        }
        return [generatedSQL: generatedSQL, errors: errors]
    }

    def importStructures(String id, List<String[]> locations, List<String> generatedSQL, List<String> errors) {
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

                activityGeneratedSQL << "/* Activity ID: ${activity} - AMP ID: ${id} */"

                locations.each { newLoc ->
                    // Check if this amp_structure is already in the DB.
                    boolean dontCreateStructure = false
                    strQuery = "SELECT amp_structure_id FROM amp_structure WHERE title ILIKE '${newLoc[2]}' AND latitude LIKE '${newLoc[3].replace(",", ".")}' and longitude LIKE '${newLoc[3].replace(",", ".")}'"
                    def sameStructures = session.createSQLQuery(strQuery).uniqueResult()
                    if (sameStructures != null) {
                        dontCreateStructure = true
                    }
                    if (dontCreateStructure) {
                        activityGeneratedSQL << "INSERT INTO amp_activity_structures(amp_activity_id, amp_structure_id) VALUES(${activity}, ${sameStructures});"
                    } else {
                        activityGeneratedSQL << "INSERT INTO amp_structure(amp_structure_id, title, description, latitude, longitude, shape, creation_date, type) VALUES(NEXTVAL('amp_structure_seq'), '${newLoc[2]}', '', '${newLoc[3].replace(",", ".")}', '${newLoc[4].replace(",", ".")}', '', NULL, 99);"
                        activityGeneratedSQL << "INSERT INTO amp_activity_structures(amp_activity_id, amp_structure_id) VALUES(${activity}, CURRVAL('amp_structure_seq'));"
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
}