package org.digijava.module.aim.action;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.XLSExporter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.IndicatorGridItem;
import org.digijava.module.aim.helper.IndicatorGridRow;
import org.digijava.module.aim.util.*;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * Creates Excel file from Indicators list
 * 
 * @author Irakli Kobiashvili ikobiashvili@picktek.com
 * 
 */
public class ExportIndicators2XSLAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        NpdForm npdForm = (NpdForm) form;
         
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=AMPIndicatorsExport.xls");

        AmpTheme mainProg = ProgramUtil.getThemeById(npdForm.getProgramId());
        Collection<IndicatorGridRow> rows = getGridRows(mainProg, npdForm.getRecursive(), npdForm
                .getSelYears(), npdForm.getSelIndicators());

        //XLSExporter.resetStyles();

        HSSFWorkbook wb = new HSSFWorkbook();

        int pictureIndex = wb.addPicture(getGraphBytes(request, npdForm), HSSFWorkbook.PICTURE_TYPE_PNG);

        String sheetName = mainProg.getName();
        if (sheetName.length() > 31){
            sheetName = sheetName.substring(0, 31);
                }
                else {
                if (sheetName.length() == 0) {
                    // should not be possible, but still...
                    sheetName = "blank";
                }
               }
                 
                // replacing odd symbols for sheet name...
                sheetName=sheetName.replace("/","|");
                sheetName=sheetName.replace("*","+");
                sheetName=sheetName.replace("?", " ");
                sheetName=sheetName.replace("\\", "|");
                sheetName=sheetName.replace("[", "(");
                sheetName=sheetName.replace("]", ")");
                sheetName =sheetName.replace(":", "-");
          
        HSSFSheet sheet = wb.createSheet(sheetName);

        
        HSSFCellStyle csHeader = wb.createCellStyle();
        csHeader.setFillBackgroundColor(HSSFColor.BROWN.index);
        HSSFFont font = wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints((short)12);          
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        csHeader.setFont(font);             

        HSSFCellStyle csSubHeader = wb.createCellStyle();
        csSubHeader.setFillBackgroundColor(HSSFColor.BROWN.index);
        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        //fontSubHeader.setFontHeightInPoints((short)12);           
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        csSubHeader.setFont(fontSubHeader);             

        
        short rowNum = 0;
        short cellNum = 0;

        HSSFRow row = sheet.createRow(rowNum++);

        HSSFCell cell = row.createCell(cellNum);
                String hierarchyName=ProgramUtil.getHierarchyName(mainProg);
                String header=TranslatorWorker.translateText("Indicators for");

        cell.setCellValue(header+"  "+hierarchyName);
        cell.setCellStyle(csHeader);


        if (npdForm.getSelYears() != null && npdForm.getSelYears().length > 0) {

            // table header 1
            row = sheet.createRow(rowNum++);
            cell = row.createCell(cellNum++);
            cell.setCellValue(" ");
            
            cell = row.createCell(cellNum++);
            cell.setCellValue(" ");
            
            cell = row.createCell(cellNum++);
            cell.setCellValue(" ");
            
            for (int i = 0; i < npdForm.getSelYears().length; i++) {
                cell = row.createCell(cellNum++);
                cellNum++;
                cellNum++;
                cell.setCellValue(npdForm.getSelYears()[i]);
                cell.setCellStyle(csSubHeader);
            }

            // table header 2
            cellNum = 0;
            row = sheet.createRow(rowNum++);
            
            cell = row.createCell(cellNum++);
            cell.setCellValue(TranslatorWorker.translateText("indicator Name"));            
            cell.setCellStyle(csSubHeader);         
            
            cell=row.createCell(cellNum++);
            cell.setCellValue(TranslatorWorker.translateText("indicator Description"));         
            cell.setCellStyle(csSubHeader);

            for (int i = 0; i < npdForm.getSelYears().length; i++) {
                                cell = row.createCell(cellNum++);
                cell.setCellValue(TranslatorWorker.translateText("Base"));
                cell.setCellStyle(csSubHeader);
                cell = row.createCell(cellNum++);
                cell.setCellValue(TranslatorWorker.translateText("Actual"));
                cell.setCellStyle(csSubHeader);
                cell = row.createCell(cellNum++);
                cell.setCellValue(TranslatorWorker.translateText("Target"));
                cell.setCellStyle(csSubHeader);
            }

            // rows
            if (rows != null && rows.size() > 0) {
                for (IndicatorGridRow indic : rows) {
                    cellNum = 0;

                    row = sheet.createRow(rowNum++);
                    
                    cell = row.createCell(cellNum++);
                    cell.setCellValue(indic.getName());
                    
                    
                    cell = row.createCell(cellNum++);
                    cell.setCellValue(indic.getDescription());
                    
                    
                    List<IndicatorGridItem> values = indic.getValues();
                    if (values!=null){
                        for (IndicatorGridItem item: values) {
                            cell = row.createCell(cellNum++);
                            cell.setCellValue(item.getBaseValue());
                            cell = row.createCell(cellNum++);
                            cell.setCellValue(item.getActualValue());
                            cell = row.createCell(cellNum++);
                            cell.setCellValue(item.getTargetValue());
                        }
                    }
                }
            }
        }




        //HSSFPictureData imgData =  wb.getAllPictures().get(0);

         HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFPicture pic1 =  patriarch.createPicture(new HSSFClientAnchor(0, 0, 0, 0, (short)0, rowNum+2, (short)1, rowNum+3),
                pictureIndex);

        pic1.resize();

        wb.write(response.getOutputStream());

        return null;
    }

    private byte[] getGraphBytes (HttpServletRequest request, NpdForm npdForm) {
        byte[] retVal = null;
        getNPDgraph getNPDgraphObj = new getNPDgraph();

        try {
            Long currentThemeId = npdForm.getProgramId();
            long[] selIndicators = npdForm.getSelIndicators();
            String[] selYears = npdForm.getSelYears();
            
            if (selYears!=null){
                Arrays.sort(selYears);
            }

            //session for storing latest map for graph
            HttpSession session = request.getSession();

            CategoryDataset dataset = null;
            if (currentThemeId != null && currentThemeId.longValue() > 0) {
                AmpTheme currentTheme = ProgramUtil.getThemeById(currentThemeId);


                dataset = getNPDgraphObj.createPercentsDataset(currentTheme, selIndicators, selYears,request);
            }
            JFreeChart chart = ChartUtil.createChart(dataset, ChartUtil.CHART_TYPE_BAR);
            ChartRenderingInfo info = new ChartRenderingInfo();

            Long teamId= TeamUtil.getCurrentTeam(request).getAmpTeamId();
            NpdSettings npdSettings= NpdUtil.getCurrentSettings(teamId);
            Double angle=null;

            if(npdSettings.getAngle()!=null){
                CategoryPlot categoryplot = (CategoryPlot)chart.getPlot();
                CategoryAxis categoryaxis = categoryplot.getDomainAxis();
                angle=npdSettings.getAngle().intValue()*3.1415926535897931D/180D;
                categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(angle));
            }

            ByteArrayOutputStream ostr = new ByteArrayOutputStream();

            ChartUtilities.writeChartAsPNG(ostr, chart, npdSettings.getWidth().intValue(),
                    npdSettings.getHeight().intValue(), info);

            retVal =  ostr.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }




        return retVal;
    }


    private Collection<IndicatorGridRow> getGridRows(AmpTheme prog, boolean recursive,String[] years, long[] inds) throws DgException{
        List<IndicatorGridRow> result = null;
        if (prog != null && prog.getAmpThemeId() != null) {
            //get all indicators and if recursive=true then all sub indicators too 
            Set<IndicatorTheme> indicators = IndicatorUtil.getIndicators(prog, recursive);
            if (indicators != null && indicators.size() > 0) {
                //convert to list
                List<IndicatorTheme> indicatorsList = new ArrayList<IndicatorTheme>(indicators);
                //sort
                Collections.sort(indicatorsList,new IndicatorUtil.IndThemeIndciatorNameComparator());
                result = new ArrayList<IndicatorGridRow>(indicatorsList.size());
                //create row object for each indicator connection
                for (IndicatorTheme connection : indicatorsList) {
                    IndicatorGridRow row = new IndicatorGridRow(connection,years);
                    if (inds != null) {
                        for (long selIndId : inds){
                            if (selIndId == row.getId().longValue()) {
                                result.add(row);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    public static class IndicatorsByyear2XLS extends XLSExporter {

        public IndicatorsByyear2XLS(Exporter parent, Viewable item) {
            super(parent, item);
        }

        public IndicatorsByyear2XLS(HSSFWorkbook wb, HSSFSheet sheet,
                HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
                Viewable item) {
            super(wb, sheet, row, rowId, colId, ownerId, item);
        }

        public void generate() {

            // TODO Auto-generated method stub

        }

    }

}
