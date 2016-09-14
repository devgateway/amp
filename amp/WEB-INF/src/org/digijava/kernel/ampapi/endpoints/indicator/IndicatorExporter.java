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
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
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

        HSSFCell cell = getCell(categoryValue.getValue() , titleCS, cellIndex++, titleRow);
        cell = getCell("ID_TITLE" , titleCS, cellIndex++, titleRow);
        cell = getCell(name , titleCS, cellIndex++, titleRow);

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

    private static HSSFCell getCell(String title, HSSFCellStyle titleCS, int index, HSSFRow titleRow) {
        HSSFCell cell = titleRow.createCell(index);
        HSSFRichTextString nameTitle = new HSSFRichTextString(title);
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);
        return cell;
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
            HSSFCell cell = getCell(location.getName() , null, cellIndex++, row);
            cell = getCell(String.valueOf(location.getId()) , null, cellIndex++, row);

            List<AmpLocationIndicatorValue> values = DynLocationManagerUtil.getLocationIndicatorValueByLocationAndIndicatorName(location,name);
            for (AmpLocationIndicatorValue value:values) {
                cell = getCell(String.valueOf(value.getValue()) , null, cellIndex++, row);
            }

        }

    }

}
