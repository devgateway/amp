package org.digijava.module.aim.action.dataimporter;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.dbentity.ImportedFilesRecord;
import org.digijava.module.admin.dbentity.ImportedProject;
import org.digijava.module.admin.util.model.ImportDataModel;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.digijava.module.aim.action.dataimporter.ImporterUtil.*;

public class TxtDataImporter {
    private static final int BATCH_SIZE = 1000;
    private static final Logger logger = LoggerFactory.getLogger(TxtDataImporter.class);


    private void processTxtFileInBatches(ImportedFilesRecord importedFilesRecord, File file, HttpServletRequest request, Map<String, String> config)
    {
        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(file))) {
            List<Map<String, String>> batch = new ArrayList<>();
            Map<String, String> values;

            while ((values = reader.readMap()) != null) {
                batch.add(values);

                if (batch.size() == BATCH_SIZE) {
                    // Process the batch
                    processBatch(batch, request,config,importedFilesRecord);
                    // Clear the batch for the next set of rows
                    batch.clear();
                }
            }

            // Process any remaining rows in the batch
            if (!batch.isEmpty()) {
                processBatch(batch, request,config,importedFilesRecord);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }


    private static void processBatch(List<Map<String, String>> batch,  HttpServletRequest request,Map<String, String> config, ImportedFilesRecord importedFilesRecord) {
        SessionUtil.extendSessionIfNeeded(request);
        Session session = PersistenceManager.getRequestDBSession();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        for (Map<String, String> row : batch) {
            ImportedProject importedProject= new ImportedProject();
            importedProject.setImportedFilesRecord(importedFilesRecord);

            ImportDataModel importDataModel = new ImportDataModel();
            importDataModel.setModified_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
            importDataModel.setCreated_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
            importDataModel.setTeam(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeam().getAmpTeamId());
            importDataModel.setIs_draft(true);
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            importDataModel.setCreation_date(now.format(formatter));
            setStatus(importDataModel, session);
            for (Map.Entry<String, String> entry : config.entrySet()) {
                switch (entry.getValue()) {
                    case "{projectTitle}":
                        importDataModel.setProject_title(row.get(entry.getKey()));
                        break;
                    case "{projectDescription}":
                        importDataModel.setDescription(row.get(entry.getKey().trim()));
                        break;
                    case "{projectLocation}":
//                        ampActivityVersion.addLocation(new AmpActivityLocation());
                        break;
                    case "{primarySector}":
                        updateSectors(importDataModel, row.get(entry.getKey().trim()), session, true);
                        break;
                    case "{secondarySector}":
                        updateSectors(importDataModel, row.get(entry.getKey().trim()), session, false);
                        break;
                    case "{donorAgency}":
                        updateOrgs(importDataModel,row.get(entry.getKey().trim()), session, "donor");
                        break;
                    case "{fundingItem}":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())),true,true, "Actual");
                        break;
                    case "{plannedCommitment}":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())),true,false, "Planned");
                        break;
                    case "{plannedDisbursement}":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())),false,true, "Planned");
                        break;
                    case "{actualCommitment}":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())),true,false, "Actual");
                        break;
                    case "{actualDisbursement}":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())),false,true, "Actual");
                        break;
                    default:
                        logger.error("Unexpected value: " + entry.getValue());
                        break;
                }
            }
        }
    }
}
