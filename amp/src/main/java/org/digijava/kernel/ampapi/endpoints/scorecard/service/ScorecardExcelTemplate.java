package org.digijava.kernel.ampapi.endpoints.scorecard.service;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ColoredCell.Colors;

/**
 * Class containing excel styles used in donor scorecard exports
 * 
 * @author Viorel Chihai
 *
 */
public class ScorecardExcelTemplate {
    
    private Workbook wb;
    
    private CellStyle blueHeaderCellStyle;
    private CellStyle grayCellStyle;
    private CellStyle greenCellStyle;
    private CellStyle redCellStyle;
    private CellStyle yellowCellStyle;
    private CellStyle wrapTextCellStyle;
    
    public ScorecardExcelTemplate(Workbook wb) {
        this.wb = wb;
        initScorecardStyles();
    }

    /**
     * Define all styles used in the scorecard workbook.
     * 
     * @param wb
     */
    private void initScorecardStyles() {
        
        blueHeaderCellStyle = wb.createCellStyle();
        blueHeaderCellStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        blueHeaderCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        grayCellStyle = wb.createCellStyle();
        grayCellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        grayCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        greenCellStyle = wb.createCellStyle();
        greenCellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        greenCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        redCellStyle = wb.createCellStyle();
        redCellStyle.setFillForegroundColor(HSSFColor.RED.index);
        redCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        yellowCellStyle = wb.createCellStyle();
        yellowCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        yellowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        wrapTextCellStyle = wb.createCellStyle();
        wrapTextCellStyle.setWrapText(true);
    }
    
    public CellStyle getBlueHeaderCellStyle() {
        return blueHeaderCellStyle;
    }

    public CellStyle getWrapTextCellStyle() {
        return wrapTextCellStyle;
    }

    public CellStyle getColoredStyle(Colors color) {
        switch (color) {
            case GRAY:
                return grayCellStyle;
            case GREEN:
                return greenCellStyle;
            case YELLOW:
                return yellowCellStyle;
            case RED:
            default:
                return redCellStyle;
        }
    }
}
