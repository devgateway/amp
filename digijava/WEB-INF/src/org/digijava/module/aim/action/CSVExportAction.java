/**
 * CSVExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.view.xls.GroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;

/**
 * CSV Export is actually using the XLS export API. The difference is only at
 * the output processing where XLS is simply converted to CSV
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 7, 2006
 * 
 */
public class CSVExportAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		GroupReportData rd = ARUtil.generateReport(mapping, form, request,
				response);

		rd.setCurrentView(GenericViews.XLS);

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"inline; filename=AMPExport.csv");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(rd.getName());

		IntWrapper rowId = new IntWrapper();
		IntWrapper colId = new IntWrapper();

		HSSFRow row = sheet.createRow(rowId.intValue());

		GroupReportDataXLS grdx = new GroupReportDataXLS(sheet, row, rowId,
				colId, null, rd);

		grdx.generate();

		// we now iterate the rows and create the csv

		StringBuffer sb = new StringBuffer();
		Iterator i = sheet.rowIterator();
		while (i.hasNext()) {
			HSSFRow crow = (HSSFRow) i.next();
			for(short ii=crow.getFirstCellNum();ii<=crow.getLastCellNum();ii++){
				HSSFCell ccell = crow.getCell(ii);
				String s="";
				if(ccell!=null) s=ccell.getStringCellValue();
				sb.append("\"").append(s).append("\"");
				if (ii<crow.getLastCellNum())
					sb.append(",");
			}
			sb.append("\n");

		}

		response.getWriter().write(sb.toString());

		return null;
	}

}
