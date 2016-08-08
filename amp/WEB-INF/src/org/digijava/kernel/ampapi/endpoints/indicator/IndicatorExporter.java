package org.digijava.kernel.ampapi.endpoints.indicator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Indicator Endpoint utility methods
 * 
 * @author apicca
 */
public class IndicatorExporter {

	protected static final Logger logger = Logger.getLogger(IndicatorExporter.class);
    private int cellIndex;

    public static StreamingOutput download(long admLevelId, String name) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("export");
        AmpCategoryValue categoryValue =(AmpCategoryValue) DbUtil.getObject(AmpCategoryValue.class, admLevelId);
        logger.debug("Exporting indicator table layer for adm level: " + categoryValue.getValue());
        // title cells
        HSSFCellStyle titleCS = wb.createCellStyle();
        wb.createCellStyle();
        titleCS.setWrapText(true);
        titleCS.setFillForegroundColor(HSSFColor.BROWN.index);
        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 10);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleCS.setFont(fontHeader);
        int rowIndex = 0;
        int cellIndex = 0;
        HSSFRow titleRow = sheet.createRow(rowIndex++);

        HSSFCell cell = titleRow.createCell(cellIndex++);
        HSSFRichTextString nameTitle = new HSSFRichTextString(categoryValue.getValue());
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);

        cell = titleRow.createCell(cellIndex++);
        nameTitle = new HSSFRichTextString("ID_TITLE");
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);

        cell = titleRow.createCell(cellIndex++);
        nameTitle = new HSSFRichTextString(name);
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);


        populateIndicatorLayerTableValues(sheet, rowIndex, categoryValue, name);
        for (int i = 0; i < cellIndex; i++) {
            sheet.autoSizeColumn(i);
        }

        StreamingOutput streamOutput = new StreamingOutput(){
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    wb.write(output);
                } catch (Exception e) {
                    logger.debug("exportIndicatorById - write: ", e);
                    throw new WebApplicationException(e);
                }
            }
        };
        return streamOutput;
    }

    public static JsonBean importIndicator(long saveOption, InputStream uploadedInputStream, String name) {
        JsonBean result = new JsonBean();
        byte[] fileData = new byte[0];
        try {
            fileData = org.apache.commons.io.IOUtils.toByteArray(uploadedInputStream);
        } catch (IOException e) {
            logger.debug("importIndicator - IOException: ", e);
            throw new WebApplicationException(e);
        }

        InputStream inputStream = new ByteArrayInputStream(fileData);

        IndicatorImporter importer = new IndicatorImporter();
        Collection<JsonBean> locationIndicatorValueList = importer.processExcelFile(inputStream);

        if (!importer.getApiErrors().isEmpty()) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(importer.getApiErrors().getAllErrors());
        }

        result.set(IndicatorEPConstants.VALUES, locationIndicatorValueList);
        return result;
    }

    public static void populateIndicatorLayerTableValues(HSSFSheet sheet,int rowIndex, AmpCategoryValue categoryValue, String name) {
        Set<AmpCategoryValueLocations> locations = new HashSet<AmpCategoryValueLocations>();
        if (CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(categoryValue)) {
            locations.add(DynLocationManagerUtil.getDefaultCountry());
        } else {
            locations = DynLocationManagerUtil.getLocationsByLayer(categoryValue);
        }

        for (AmpCategoryValueLocations location : locations) {
            int cellIndex =0;
            HSSFRow row = sheet.createRow(rowIndex++);
            HSSFCell cell = row.createCell(cellIndex++);
            cell.setCellValue(location.getName());
            cell = row.createCell(cellIndex++);
            cell.setCellValue(location.getId());
            List<AmpLocationIndicatorValue> values = DynLocationManagerUtil.getLocationIndicatorValueByLocationAndIndicatorName(location,name);
            for (AmpLocationIndicatorValue value:values) {
                cell = row.createCell(cellIndex++);
                cell.setCellValue(value.getValue());
            }

        }

    }

}
