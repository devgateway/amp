package org.digijava.kernel.ampapi.endpoints.scorecard.service;

import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.GREEN;
import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.GREY_40_PERCENT;
import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.LIGHT_BLUE;
import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.RED;
import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.YELLOW;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
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
     */
    private void initScorecardStyles() {
        
        blueHeaderCellStyle = wb.createCellStyle();
        blueHeaderCellStyle.setFillForegroundColor(LIGHT_BLUE.getIndex());
        blueHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        grayCellStyle = wb.createCellStyle();
        grayCellStyle.setFillForegroundColor(GREY_40_PERCENT.getIndex());
        grayCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        greenCellStyle = wb.createCellStyle();
        greenCellStyle.setFillForegroundColor(GREEN.getIndex());
        greenCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        redCellStyle = wb.createCellStyle();
        redCellStyle.setFillForegroundColor(RED.getIndex());
        redCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        yellowCellStyle = wb.createCellStyle();
        yellowCellStyle.setFillForegroundColor(YELLOW.getIndex());
        yellowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
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
