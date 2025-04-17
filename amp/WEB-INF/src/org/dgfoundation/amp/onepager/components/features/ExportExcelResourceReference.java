package org.dgfoundation.amp.onepager.components.features;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.Component;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.PagingListEditor;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExportExcelResourceReference extends ResourceReference {

    private static final Logger logger = LoggerFactory.getLogger(ExportExcelResourceReference.class);
    PagingListEditor<AmpStructure> list;
    public void setList(PagingListEditor<AmpStructure> list)
    {
        this.list = list;
    }
    public PagingListEditor<AmpStructure> getList()
    {
        return list;
    }
    public ExportExcelResourceReference() {
        super("export-excel");
    }

    @Override
    public IResource getResource() {
        return new AbstractResource() {
            @Override
            protected ResourceResponse newResourceResponse(Attributes attributes) {
                ResourceResponse response = new ResourceResponse();

                // Set response metadata
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setContentDisposition(ContentDisposition.ATTACHMENT);
                response.setFileName("structures.xlsx");

                if (response.dataNeedsToBeWritten(attributes)) {
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                         XSSFWorkbook workbook = new XSSFWorkbook()) {
                        // Create Excel sheet
                        XSSFSheet sheet = workbook.createSheet("Structures");

                        // Create header row
                        XSSFRow headerRow = sheet.createRow(0);
                        headerRow.createCell(0).setCellValue("Title");
                        headerRow.createCell(1).setCellValue("Description");
                        headerRow.createCell(2).setCellValue("Latitude");
                        headerRow.createCell(3).setCellValue("Longitude");

                        int rowIndex = 1;
                        for (Component child : getList()) {
                            if (child instanceof ListItem) {
                                ListItem<AmpStructure> listItem = (ListItem<AmpStructure>) child;
                                AmpStructure structure = listItem.getModelObject();

                                // Create a new row for each structure
                                XSSFRow row = sheet.createRow(rowIndex);
                                if (structure != null) {
                                    createCellIfNotNull(row, 0, structure.getTitle());
                                    createCellIfNotNull(row, 1, structure.getDescription());
                                    createCellIfNotNull(row, 2, structure.getLatitude());
                                    createCellIfNotNull(row, 3, structure.getLongitude());
                                }
                                rowIndex++;
                            }
                        }

                        // Write workbook content to the output stream
                        workbook.write(outputStream);

                        // Respond with the written content
                        response.setWriteCallback(new WriteCallback() {
                            @Override
                            public void writeData(Attributes attributes) throws IOException {
                                attributes.getResponse().write(outputStream.toByteArray());
                            }
                        });
                    } catch (IOException e) {
                        logger.error("Error exporting data to Excel", e);
                        response.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                }

                return response;
            }
        };
    }

    // Helper method for creating cells
    private void createCellIfNotNull(XSSFRow row, int columnIndex, Object value) {
        if (value != null) {
            row.createCell(columnIndex).setCellValue(value.toString());
        }
    }

}
