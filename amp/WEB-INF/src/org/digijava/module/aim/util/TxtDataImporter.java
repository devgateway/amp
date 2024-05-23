package org.digijava.module.aim.util;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import org.digijava.module.admin.dbentity.ImportedFilesRecord;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TxtDataImporter {
    private static final int BATCH_SIZE = 1000;

    private void processTxtFileInBatches(ImportedFilesRecord importedFilesRecord, File file, HttpServletRequest request, Map<String, String> config)
    {
        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(file))) {
            List<Map<String, String>> batch = new ArrayList<>();
            Map<String, String> values;

            while ((values = reader.readMap()) != null) {
                batch.add(values);

                if (batch.size() == BATCH_SIZE) {
                    // Process the batch
                    processBatch(batch);
                    // Clear the batch for the next set of rows
                    batch.clear();
                }
            }

            // Process any remaining rows in the batch
            if (!batch.isEmpty()) {
                processBatch(batch);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }


    private static void processBatch(List<Map<String, String>> batch,  HttpServletRequest request,Map<String, String> config, ImportedFilesRecord importedFilesRecord) {
        for (Map<String, String> row : batch) {
            // Access data by column names (headers)
            String header1Value = row.get("Header1");
            String header2Value = row.get("Header2");
            String header3Value = row.get("Header3");

            // Do something with the extracted data
            System.out.println("Header1: " + header1Value);
            System.out.println("Header2: " + header2Value);
            System.out.println("Header3: " + header3Value);
        }
    }
}
