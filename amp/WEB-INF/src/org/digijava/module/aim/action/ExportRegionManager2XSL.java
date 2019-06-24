package org.digijava.module.aim.action;

import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.BROWN;
import static org.apache.poi.ss.usermodel.ReadingOrder.LEFT_TO_RIGHT;
import static org.apache.poi.ss.usermodel.ReadingOrder.RIGHT_TO_LEFT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.ReadingOrder;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ExportRegionManager2XSL extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {
        
        HttpSession session = request.getSession();
        
        if(session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if(str.equals("no")) {
                return mapping.findForward("index");
            }
        }
        
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=Export.xls");
        
        DynLocationManagerForm myForm = (DynLocationManagerForm) form;
        String hideEmptyCountriesStr = request.getParameter("hideEmptyCountriesAction");
        
        if("false".equals(hideEmptyCountriesStr)) {
            myForm.setHideEmptyCountries(false);
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("export");
        sheet.setRightToLeft(SiteUtils.isEffectiveLangRTL());
        
        // title cells
        HSSFCellStyle titleCS = wb.createCellStyle();
        wb.createCellStyle();
        titleCS.setWrapText(true);
        titleCS.setFillForegroundColor(BROWN.getIndex());
        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 10);
        fontHeader.setBold(true);
        titleCS.setAlignment(HorizontalAlignment.CENTER);
        titleCS.setFont(fontHeader);
        int rowIndex = 0;
        int cellIndex = 0;

        Collection<AmpCategoryValue> values = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.IMPLEMENTATION_LOCATION_KEY);
        List<AmpCategoryValue> locationLevels = new ArrayList<AmpCategoryValue>();
        locationLevels.addAll(values);
        
        int firstLayerIndex = locationLevels.get(0).getIndex();
        int size = locationLevels.size();
        
        HSSFRow titleRow = sheet.createRow(rowIndex++);
        HSSFCell cell = titleRow.createCell(cellIndex++);
        HSSFRichTextString nameTitle = new HSSFRichTextString("Database ID");
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);
        
        for(AmpCategoryValue value : values) {
            cell = titleRow.createCell(cellIndex++);
            nameTitle = new HSSFRichTextString(TranslatorWorker.translateText(value.getValue()));
            cell.setCellValue(nameTitle);
            cell.setCellStyle(titleCS);
        }
        
        int lastImpLevelIndex=cellIndex;
        cell = titleRow.createCell(cellIndex++);
        nameTitle = new HSSFRichTextString(TranslatorWorker.translateText("Latitude"));
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);
        
        cell = titleRow.createCell(cellIndex++);
        nameTitle = new HSSFRichTextString(TranslatorWorker.translateText("Longitude"));
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);
        
        cell = titleRow.createCell(cellIndex++);
        nameTitle = new HSSFRichTextString("GeoID");
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);
        
        cell = titleRow.createCell(cellIndex++);
        nameTitle = new HSSFRichTextString("ISO");
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);
        
        cell = titleRow.createCell(cellIndex++);
        nameTitle = new HSSFRichTextString("ISO3");
        cell.setCellValue(nameTitle);
        cell.setCellStyle(titleCS);
        
        ActionMessages errors = new ActionMessages();
        Collection<AmpCategoryValueLocations> rootLocations = DynLocationManagerUtil.getHighestLayerLocations(myForm, errors);
        Integer[] rowIndexArr={rowIndex};
        generateLocationHierarchy(rootLocations, rowIndexArr, sheet,myForm.getHideEmptyCountries(), firstLayerIndex,lastImpLevelIndex);
        
        for (int i = 0; i < size; i++) {
            sheet.autoSizeColumn(i);
        }
        
        wb.write(response.getOutputStream());
        return null;

    }

    private void generateLocationHierarchy(Collection<AmpCategoryValueLocations> locations, Integer[] rowIndex, HSSFSheet sheet,
            boolean hideEmptyCountries,int countryLayerIndex,int lastImpLevelIndex ) {
    
        HSSFCellStyle numberStyle = sheet.getWorkbook().createCellStyle();
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        ReadingOrder readingOrder = SiteUtils.isEffectiveLangRTL() ? RIGHT_TO_LEFT : LEFT_TO_RIGHT;
        numberStyle.setReadingOrder(readingOrder.getCode());
        
        if (locations != null) {
            for (AmpCategoryValueLocations location : locations) {
                if (!location.isSoftDeleted()) {
                    int cellIndex=0;
                    Set<AmpCategoryValueLocations> childrenLocs = location.getChildLocations();
                    int currentLayer = location.getParentCategoryValue().getIndex();
                    
                    if(hideEmptyCountries && currentLayer == countryLayerIndex && childrenLocs.size() == 0) {
                        continue;
                    }
                    
                    HSSFRow row = sheet.createRow(rowIndex[0]++);
                    HSSFCell cell = row.createCell(cellIndex++);
                    cell.setCellValue(location.getId());
                    cell.setCellStyle(numberStyle);
                    
                    List<String> parents = DynLocationManagerUtil.getParents(location);
                    for(String parent : parents){
                        cell = row.createCell(cellIndex++);
                        cell.setCellValue(parent);
                    }
                    
                    cellIndex=lastImpLevelIndex;
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue(location.getGsLat());
                    cell.setCellStyle(numberStyle);
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue(location.getGsLong());
                    cell.setCellStyle(numberStyle);
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue(location.getGeoCode());
                    cell.setCellStyle(numberStyle);
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue(location.getIso());
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue(location.getIso3());
                    
                    generateLocationHierarchy(childrenLocs, rowIndex, sheet,hideEmptyCountries, countryLayerIndex, lastImpLevelIndex);
                }
            }
        }
    }
}
