/**
 * TextCellXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 *
 */
public class TextCellXLS extends XLSExporter {

	/**
	 * @param parent
	 * @param item
	 */
	public TextCellXLS(Exporter parent, Viewable item) {
		super(parent, item);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sheet
	 * @param row
	 * @param rowId
	 * @param colId
	 * @param ownerId
	 * @param item
	 */
	public TextCellXLS(HSSFWorkbook wb ,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
			IntWrapper colId, Long ownerId, Viewable item) {
		super(wb, sheet, row, rowId, colId, ownerId, item);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Exporter#generate()
	 */
	public void generate() {
		TextCell c=(TextCell) item;
		HSSFCell cell=this.getRegularCell();
		if(c.getColumn().getName().compareTo("Status")==0)
		{
			String actualStatus=c.toString();
			
			ReportData parent=(ReportData)c.getColumn().getParent();
			while (parent.getReportMetadata()==null)
			{
				parent=parent.getParent();
			}
			//when we get to the top of the hierarchy we have access to AmpReports
			
			//requirements for translation purposes
			TranslatorWorker translator=TranslatorWorker.getInstance();
			String siteId=parent.getReportMetadata().getSiteId();
			String locale=parent.getReportMetadata().getLocale();
			
			String finalStatus=new String();//the actual text to be added to the column
			
			String translatedStatus=null;
			String prefix="aim:";
			try{
				translatedStatus=TranslatorWorker.translate(prefix+actualStatus,locale,siteId);
			}catch (WorkerException e)
				{
				e.printStackTrace();
				}
			if (translatedStatus.compareTo("")==0)
				translatedStatus=actualStatus;
			finalStatus+=translatedStatus;

			cell.setCellValue( finalStatus);
		}
		else 
			cell.setCellValue(c.toString());
		
		colId.inc();
	}

}
