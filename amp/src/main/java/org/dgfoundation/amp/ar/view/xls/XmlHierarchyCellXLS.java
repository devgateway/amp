/**
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.XmlHierarchyCell;
import org.dgfoundation.amp.ar.helper.HierarchycalItem;

/**
 * @author Alex
 *
 */
public class XmlHierarchyCellXLS extends XLSExporter {

    private static String[] symbols = {"-","*","+","#","="};
    private static short[] colors = {HSSFColor.BLUE.index,HSSFColor.DARK_GREEN.index,HSSFColor.GREEN.index,HSSFColor.BLUE_GREY.index, HSSFColor.VIOLET.index};
    private static ThreadLocal<HashMap<String, HSSFFont>> localFontMap = new ThreadLocal<HashMap<String,HSSFFont>>() {
                                                                @Override 
                                                                protected HashMap<String,HSSFFont> initialValue() {
                                                                    return new HashMap<String, HSSFFont>();
                                                                }
                                                            };
                                                            
    private int numOfLines      = 0;
    
    /**
     * @param parent
     * @param item
     */
    public XmlHierarchyCellXLS(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param wb
     * @param sheet
     * @param row
     * @param rowId
     * @param colId
     * @param ownerId
     * @param item
     */
    public XmlHierarchyCellXLS(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
            IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    @Override
    public void generate() {
        XmlHierarchyCell c=(XmlHierarchyCell) item;
        if ( c.getRootItems() != null && c.getRootItems().size() > 0 ) {
            HSSFCell cell           = this.getRegularCell();
//          CellStyle cellStyle     = this.wb.createCellStyle();
//          cellStyle.setWrapText(true);
//          cell.setCellStyle(cellStyle);
            
            int lastIndexInString               = 0;
            StringBuilder finalString           = new StringBuilder();
            TreeMap<Integer, Font> fontApplyingMap      = new TreeMap<Integer, Font>();
            
            for (HierarchycalItem srcItem : c.getRootItems() ) {
                lastIndexInString = this.populateString(srcItem, 1, lastIndexInString, finalString, fontApplyingMap);
            }
            
            HSSFRichTextString richString = new HSSFRichTextString( finalString.toString() );
            
            int beginIdx                        = 0;
            Iterator<Entry<Integer,Font>> iter  = fontApplyingMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Integer,Font> entry = iter.next();
                richString.applyFont(beginIdx, entry.getKey(), entry.getValue() );
                beginIdx        = entry.getKey();
            }
            
            cell.setCellValue( richString );
            cell.getRow().setHeightInPoints(3*this.sheet.getDefaultRowHeightInPoints() );
            //this.sheet.autoSizeColumn( cell.getColumnIndex() );
        }
        
        colId.inc();
    }
    
    private int populateString(HierarchycalItem srcItem, int level, int lastIndexInString, StringBuilder sBuilder, Map<Integer, Font> fontApplyingMap) {
        int strlen              = 1 + symbols[level-1].length() + level *3;
        StringBuilder indent    = new StringBuilder(level * 3);
        for (int i = 1; i <= level; i++) {
            indent.append("   ");
        }
        this.numOfLines ++ ;
        sBuilder.append( "\n" );
        sBuilder.append( indent );
        sBuilder.append(symbols[level-1]);
        
        String title = srcItem.getDateString() + " " + srcItem.getName() + ": ";
        strlen          += title.length();
        sBuilder.append(title);
        
        fontApplyingMap.put(lastIndexInString + strlen, this.getFont(level+"TITLE"));
        
        strlen          += srcItem.getDescription().length();
        sBuilder.append(srcItem.getDescription() );
        fontApplyingMap.put(lastIndexInString + strlen, this.getFont(level+"BODY"));
        
        if ( srcItem.getChildren() != null && srcItem.getChildren().size() > 0 ) {
            strlen      +=lastIndexInString;
            for (HierarchycalItem srcChildItem : srcItem.getChildren() ) {
                strlen =    this.populateString(srcChildItem, level+1, strlen, sBuilder, fontApplyingMap);
            }
        }
        return lastIndexInString + strlen;
    }
    private HSSFFont getFont (String fontType) {
        HashMap<String,HSSFFont> fontMap    =  localFontMap.get();
        
        HSSFFont result     = fontMap.get(fontType);
        if ( result == null ) {
            result          = this.wb.createFont();
            int level       = 1;
            try {
                level       = Integer.parseInt(fontType.substring(0, 1));
            }
            catch(Exception e){
                e.printStackTrace();
            }
            result.setColor(colors[level-1]);
            if ( fontType.contains("TITLE") ) 
                result.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            
            fontMap.put(fontType, result);
        }
        
        return result;
    }

}
