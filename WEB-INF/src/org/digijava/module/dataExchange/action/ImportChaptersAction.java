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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpChapter;
import org.digijava.module.aim.dbentity.AmpImputation;
import org.digijava.module.aim.util.ChapterUtil;
import org.digijava.module.dataExchange.form.ImportChaptersForm;

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
			POIFSFileSystem poifs = new POIFSFileSystem(inp);
			HSSFWorkbook wb = new HSSFWorkbook(poifs);
			HSSFSheet sheet = wb.getSheetAt(0);
			boolean header = true;
			int chaptersInserted = 0;
			int chaptersUpdated = 0;
			int imputationsInserted = 0;
			int imputationsUpdated = 0;
			int errorNumber = 0;
			for (Iterator<HSSFRow> rit = sheet.rowIterator(); rit.hasNext();) {
				try {
					HSSFRow HSSFRow = rit.next();
					if (header) {
						header = false;
						continue;
					}
					String chapterCode = ChapterUtil.getNumberFromCell(HSSFRow.getCell((short)1));
					AmpChapter chapter = ChapterUtil
							.getChapterByCode(chapterCode);
					if (chapter == null) {
						chapter = new AmpChapter(chapterCode);
						chaptersInserted++;
					} else
						chaptersUpdated++;
					if(HSSFRow.getCell((short)2)!=null) chapter.setDescription(HSSFRow.getCell((short)2).getStringCellValue()); else chapter.setDescription(null);
					String year =  ChapterUtil.getNumberFromCell(HSSFRow.getCell((short)0));
					chapter.setYear(new Integer(year));
					ChapterUtil.saveChapter(chapter);
					logger.info("Processed chapter with code "
							+ chapter.getCode());
					String impCode = ChapterUtil.getNumberFromCell(HSSFRow.getCell((short)3));
					AmpImputation imp = ChapterUtil.getImputationByCode(impCode);
					if (imp == null) {
						imp = new AmpImputation(impCode);
						imputationsInserted++;
					} else
						imputationsUpdated++;
					imp.setChapter(chapter);
					if(HSSFRow.getCell((short)4)!=null) imp.setDescription(HSSFRow.getCell((short)4).getStringCellValue());else imp.setDescription(null);
					ChapterUtil.saveImputation(imp);
					logger.info("Processed imputation with code "
							+ imp.getCode());
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
