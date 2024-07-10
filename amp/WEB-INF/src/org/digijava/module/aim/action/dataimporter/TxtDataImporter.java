package org.digijava.module.aim.action.dataimporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedFilesRecord;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedProject;
import org.digijava.module.aim.action.dataimporter.model.Funding;
import org.digijava.module.aim.action.dataimporter.model.ImportDataModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
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

import static org.digijava.module.aim.action.dataimporter.util.ImporterUtil.*;

public class TxtDataImporter {
    private static final int BATCH_SIZE = 1000;
    private static final Logger logger = LoggerFactory.getLogger(TxtDataImporter.class);


    public static int processTxtFileInBatches(ImportedFilesRecord importedFilesRecord, File file, HttpServletRequest request, Map<String, String> config)
    {
        logger.info("Processing txt file: " + file.getName());
        CSVParser parser = new CSVParserBuilder().withSeparator(request.getParameter("dataSeparator").charAt(0)).build();

        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAwareBuilder(new FileReader(file)).withCSVParser(parser).build()) {
            List<Map<String, String>> batch = new ArrayList<>();
            Map<String, String> values;
            int batchNumber =1;
            while ((values = reader.readMap()) != null) {
                batch.add(values);

                if (batch.size() == BATCH_SIZE) {
                    logger.info("Batch number here: {}",batchNumber);

                    // Process the batch
                    processBatch(batch, request,config,importedFilesRecord);
                    // Clear the batch for the next set of rows
                    batch.clear();
                    batchNumber+=1;
                }
            }

            // Process any remaining rows in the batch
            if (!batch.isEmpty()) {
                logger.info("Processing last batch of size {}", batch.size());
                processBatch(batch, request,config,importedFilesRecord);
            }
        } catch (IOException | CsvValidationException e) {
            logger.error("Error processing txt file "+e.getMessage(),e);
            return 0;
        }
        return 1;
    }


    private static void processBatch(List<Map<String, String>> batch,  HttpServletRequest request,Map<String, String> config, ImportedFilesRecord importedFilesRecord) throws JsonProcessingException {
        logger.info("Processing txt batch");
        SessionUtil.extendSessionIfNeeded(request);
        Session session = PersistenceManager.getRequestDBSession();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        for (Map<String, String> row : batch) {
            ImportedProject importedProject= new ImportedProject();
            importedProject.setImportedFilesRecord(importedFilesRecord);
            List<Funding> fundings= new ArrayList<>();


            ImportDataModel importDataModel = new ImportDataModel();
            importDataModel.setModified_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
            importDataModel.setCreated_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
            importDataModel.setTeam(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeam().getAmpTeamId());
            importDataModel.setIs_draft(true);
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            importDataModel.setCreation_date(now.format(formatter));
            setStatus(importDataModel);
            String componentName= row.get(getKey(config, "Component Name"));
            String componentCode= row.get(getKey(config, "Component Code"));
            String projectCode= row.get(getKey(config, "Project Code"));
            String projectTitle= row.get(getKey(config, "Project Title"));
            String projectDesc= row.get(getKey(config, "Project Description"));
            AmpActivityVersion existing = existingActivity(projectTitle,projectCode,session);

            importDataModel.setProject_title(projectTitle);
            importDataModel.setProject_code(projectCode);
            importDataModel.setDescription(projectDesc);

            String donorAgencyCode= row.get(getKey(config, "Donor Agency Code"));
            String responsibleOrgCode= row.get(getKey(config, "Responsible Organization Code"));
            Long responsibleOrgId=null;

            logger.info("Configuration: "+config);
            for (Map.Entry<String, String> entry : config.entrySet()) {
                Funding fundingItem = new Funding();
                switch (entry.getValue()) {
                    case "Project Location":
//                        ampActivityVersion.addLocation(new AmpActivityLocation());
                        break;
                    case "Primary Sector":
                        updateSectors(importDataModel, row.get(entry.getKey().trim()), session, true);
                        break;
                    case "Secondary Sector":
                        updateSectors(importDataModel, row.get(entry.getKey().trim()), session, false);
                        break;
                    case "Donor Agency":
                        updateOrgs(importDataModel,row.get(entry.getKey().trim()),donorAgencyCode, session, "donor");
                        break;
                    case "Responsible Organization":
                        responsibleOrgId=updateOrgs(importDataModel,row.get(entry.getKey().trim()),responsibleOrgCode, session, "responsibleOrg");
                        break;
                    case "Beneficiary Agency":
                        responsibleOrgId=updateOrgs(importDataModel,row.get(entry.getKey().trim()),responsibleOrgCode, session, "beneficiaryAgency");
                        break;
                    case "Funding Item":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())), true, true, "Actual", fundingItem, existing);
                        break;
                    case "Planned Commitment":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())), true, false, "Planned", fundingItem, existing);
                        break;
                    case "Planned Disbursement":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())), false, true, "Planned", fundingItem, existing);
                        break;
                    case "Actual Commitment":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())), true, false, "Actual", fundingItem, existing);
                        break;
                    case "Actual Disbursement":
                        setAFundingItemForTxt(config, row, entry, importDataModel, session, Double.parseDouble(row.get(entry.getKey().trim())), false, true, "Actual", fundingItem, existing);
                        break;
                    default:
                        logger.error("Unexpected value: " + entry.getValue());
                        break;
                }
                fundings.add(fundingItem);
                logger.info("Funding items :{}",fundings);

            }

            importTheData(importDataModel, session, importedProject, componentName, componentCode,responsibleOrgId,fundings,existing);

        }

    }
}
