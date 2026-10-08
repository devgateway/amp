package org.digijava.kernel.ampapi.endpoints.gis;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** REST endpoint for parsing structure workbooks before applying them to an activity form. */
@Path("gis/structures")
@Api("gis-structure-import")
public class StructureImportEndpoint {

    private static final long MAX_REQUEST_SIZE = 21L * 1024 * 1024;

    @POST
    @Path("import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(authTypes = AuthRule.AUTHENTICATED, id = "importStructureWorkbook", ui = false)
    @ApiOperation("Parse an uploaded structure workbook and return its rows")
    public List<StructureRow> importWorkbook(@FormDataParam("file") InputStream file,
            @Context HttpServletRequest request) {
        if (file == null) {
            throw badRequest("A workbook file is required.");
        }
        long contentLength = request.getContentLengthLong();
        if (contentLength < 0 || contentLength > MAX_REQUEST_SIZE) {
                    Response.Status.REQUEST_ENTITY_TOO_LARGE);
        }

        try (InputStream input = file; Workbook workbook = WorkbookFactory.create(input)) {
            if (workbook.getNumberOfSheets() == 0 || workbook.getSheetAt(0).getPhysicalNumberOfRows() == 0) {
                throw badRequest("The workbook does not contain a header row.");
            }

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            Row header = sheet.getRow(sheet.getFirstRowNum());
            Map<String, Integer> columns = getColumnIndexes(header);
            if (!columns.containsKey("title")) {
                throw badRequest("The workbook must contain a Title column.");
            }

            DataFormatter formatter = new DataFormatter();
            List<StructureRow> structures = new ArrayList<>();
            for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String title = getCellValue(row, columns.get("title"), formatter);
                String description = getCellValue(row, columns.get("description"), formatter);
                String latitude = getCellValue(row, columns.get("latitude"), formatter);
                String longitude = getCellValue(row, columns.get("longitude"), formatter);
                String shape = getCellValue(row, columns.get("shape"), formatter);
                if (isBlank(title) && isBlank(description) && isBlank(latitude) && isBlank(longitude)
                        && isBlank(shape)) {
                    continue;
                }
                if (isBlank(title)) {
                    throw badRequest("A structure title is required on row " + (rowIndex + 1) + ".");
                }

                structures.add(new StructureRow(title, description, latitude, longitude, shape));
            }
            return structures;
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new WebApplicationException("The uploaded file is not a readable Excel workbook.", e,
                    Response.Status.BAD_REQUEST);
        }
    }

    private Map<String, Integer> getColumnIndexes(Row header) {
        Map<String, Integer> columns = new HashMap<>();
        for (int column = 0; column < header.getLastCellNum(); column++) {
            String name = new DataFormatter().formatCellValue(header.getCell(column)).trim().toLowerCase(Locale.ROOT);
            if (!name.isEmpty()) {
                columns.put(name, column);
            }
        }
        return columns;
    }

    private String getCellValue(Row row, Integer column, DataFormatter formatter) {
        if (column == null || row.getCell(column) == null) {
            return null;
        }
        String value = formatter.formatCellValue(row.getCell(column));
        return value.isEmpty() ? null : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private WebApplicationException badRequest(String message) {
        return new WebApplicationException(message, Response.Status.BAD_REQUEST);
    }

    public static class StructureRow {
        private String title;
        private String description;
        private String latitude;
        private String longitude;
        private String shape;

        public StructureRow() {
        }

        public StructureRow(String title, String description, String latitude, String longitude, String shape) {
            this.title = title;
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
            this.shape = shape;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getShape() {
            return shape;
        }

        public void setShape(String shape) {
            this.shape = shape;
        }
    }
}