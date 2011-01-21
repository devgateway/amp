/**
 * ImportChaptersAction.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.dataExchange.action;

import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpChapter;
import org.digijava.module.aim.dbentity.AmpImputation;
import org.digijava.module.aim.util.ChapterUtil;
import org.digijava.module.dataExchange.form.ImportChaptersForm;

import com.ibm.wsdl.OutputImpl;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class ImportChaptersAction extends Action {
	private static Logger logger = Logger.getLogger(ImportChaptersAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ImportChaptersForm icform = (ImportChaptersForm) form;
		if (request.getParameter("importPerform") != null && icform.getUploadedFile()!=null && icform.getUploadedFile().getFileSize()>0) {
			InputStream inp = icform.getUploadedFile().getInputStream();
            POIFSFileSystem poifs=null;
            Sheet sheet=null;
			try {
                poifs = new POIFSFileSystem(inp);
                HSSFWorkbook wb = new HSSFWorkbook(poifs);
                sheet = wb.getSheetAt(0);
            }
           catch (Exception ex) {
               logger.error("invalid file", ex);
               ActionMessages errors = new ActionMessages();
               errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.dataExchange.invalidFile"));
               saveErrors(request, errors);
               icform.setImportPerform(false);
               return mapping.findForward("forward");
            }
			
			boolean header = true;
			int chaptersInserted = 0;
			int chaptersUpdated = 0;
			int imputationsInserted = 0;
			int imputationsUpdated = 0;
			int errorNumber = 0;
			for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {
				try {
					Row row = rit.next();
					if (header) {
						header = false;
						continue;
					}
					String chapterCode = ChapterUtil.getNumberFromCell(row.getCell(1));
					AmpChapter chapter = ChapterUtil
							.getChapterByCode(chapterCode);
					if (chapter == null) {
						chapter = new AmpChapter(chapterCode);
						chaptersInserted++;
					} else
						chaptersUpdated++;
					if(row.getCell(2)!=null) chapter.setDescription(row.getCell(2).getStringCellValue()); else chapter.setDescription(null);
					String year =  ChapterUtil.getNumberFromCell(row.getCell(0));
					chapter.setYear(new Integer(year));
					ChapterUtil.saveChapter(chapter);
					logger.info("Processed chapter with code " + chapter.getCode());
					String impCode = ChapterUtil.getNumberFromCell(row.getCell(3));
					AmpImputation imp = ChapterUtil.getImputationByCode(impCode);
					if (imp == null) {
						imp = new AmpImputation(impCode);
						imputationsInserted++;
					} else
						imputationsUpdated++;
					imp.setChapter(chapter);
					if(row.getCell(4)!=null) imp.setDescription(row.getCell(4).getStringCellValue());else imp.setDescription(null);
					ChapterUtil.saveImputation(imp);
					logger.info("Processed imputation with code " + imp.getCode());
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e);
					errorNumber++;
				}
			}
			icform.setChaptersInserted(chaptersInserted);
			icform.setChaptersUpdated(chaptersUpdated);
			icform.setImputationsInserted(imputationsInserted);
			icform.setImputationsUpdated(imputationsUpdated);
			icform.setErrorNumber(errorNumber);
			icform.setImportPerform(true);
		}

		return mapping.findForward("forward");
	}
}
