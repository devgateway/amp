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
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Indicator Endpoint utility methods
 * 
 * @author apicca
 */
public class IndicatorExporter {

	protected static final Logger logger = Logger.getLogger(IndicatorExporter.class);

    public static StreamingOutput exportIndicatorById(long admLevelId, long indicatorId) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("export");
        AmpCategoryValue categoryValue =(AmpCategoryValue) DbUtil.getObject(AmpCategoryValue.class, admLevelId);
        AmpIndicatorLayer indicatorLayer =(AmpIndicatorLayer) DbUtil.getObject(AmpIndicatorLayer.class, indicatorId);
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
        nameTitle = new HSSFRichTextString("GEO_ID_TITLE");
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);

        List<AmpIndicatorLayer> indicatorLayers = DynLocationManagerUtil.getIndicatorByCategoryValueIdAndIndicatorId(admLevelId, indicatorId);
        for (AmpIndicatorLayer indicator : indicatorLayers) {
            cell = titleRow.createCell(cellIndex++);
            nameTitle = new HSSFRichTextString(indicator.getName());
            cell.setCellValue(nameTitle);
            cell.setCellStyle(titleCS);

        }
        populateIndicatorLayerTableValues(sheet, rowIndex, categoryValue, indicatorLayer);
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

    public static JsonBean importIndicator(long saveOption, InputStream uploadedInputStream, long indicatorId) {
        JsonBean result = new JsonBean();
        byte[] fileData = new byte[0];
        try {
            fileData = org.apache.commons.io.IOUtils.toByteArray(uploadedInputStream);
        } catch (IOException e) {
            logger.debug("importIndicator - IOException: ", e);
            throw new WebApplicationException(e);
        }

        InputStream inputStream = new ByteArrayInputStream(fileData);
        DynLocationManagerForm.Option option=(saveOption==1)? DynLocationManagerForm.Option.OVERWRITE: DynLocationManagerForm.Option.NEW;
        Map<Integer, ApiErrorMessage> errors = new HashMap<Integer, ApiErrorMessage>();
        DynLocationManagerUtil.ErrorWrapper errorWrapper = null;
        try {
            errorWrapper = DynLocationManagerUtil.importIndicatorTableExcelFile(inputStream, option, indicatorId);
        } catch (AimException e) {
            logger.debug("importIndicator - AimException: ", e);
            throw new WebApplicationException(e);
        }

        DynLocationManagerUtil.ErrorCode errorCode=errorWrapper.getErrorCode();
        ApiErrorMessage error = null;
        switch (errorCode) {
            case INCORRECT_CONTENT:
                error = IndicatorErrors.INCORRECT_CONTENT;
                break;
            case INEXISTANT_ADM_LEVEL:
                error = IndicatorErrors.INEXISTANT_ADM_LEVEL;
                break;
            case NUMBER_NOT_MATCH:
                error = IndicatorErrors.NUMBER_NOT_MATCH;
                break;
            case NAME_NOT_MATCH:
                error = IndicatorErrors.NAME_NOT_MATCH;
                break;
            case LOCATION_NOT_FOUND:
                error = IndicatorErrors.LOCATION_NOT_FOUND;
                break;
            case CORRECT_CONTENT:
                result.set(IndicatorEPConstants.RESULT,IndicatorEPConstants.IMPORTED);
        }
        if (errors.size()>0) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(error);
        }

        return result;
    }

    public static void populateIndicatorLayerTableValues(HSSFSheet sheet,int rowIndex, AmpCategoryValue categoryValue, AmpIndicatorLayer indicatorLayer) {
        Set<AmpCategoryValueLocations> locations = DynLocationManagerUtil
                .getLocationsByLayer(categoryValue);
        for (AmpCategoryValueLocations location : locations) {
            int cellIndex = 0;
            HSSFRow row = sheet.createRow(rowIndex++);
            HSSFCell cell = row.createCell(cellIndex++);
            cell.setCellValue(location.getName());
            cell = row.createCell(cellIndex++);
            cell.setCellValue(location.getGeoCode());
            List <AmpLocationIndicatorValue> values = DynLocationManagerUtil.getLocationIndicatorValueByLocationAndIndicator(location,indicatorLayer);
            for (AmpLocationIndicatorValue value:values) {
                cell = row.createCell(cellIndex++);
                cell.setCellValue(value.getValue());
            }

        }

    }

}
