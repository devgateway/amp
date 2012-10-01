package org.digijava.module.aim.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.form.AuditLoggerManagerForm;
import org.digijava.module.aim.util.AuditLoggerUtil;

public class AuditLoggerManager extends MultiAction {
	
	private static Logger logger = Logger.getLogger(AuditLoggerManager.class);
	
	private ServletContext ampContext = null;
	
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AuditLoggerManagerForm vForm = (AuditLoggerManagerForm) form;

		if (request.getParameter("clean") != null) {
			if (vForm.getUseraction().equalsIgnoreCase("delete")) {
				AuditLoggerUtil.DeleteLogsByPeriod(vForm.getFrecuency());
			} else if (vForm.getUseraction().equalsIgnoreCase("export")) {

				OutputStream out = response.getOutputStream();
				try {
					XlsMaker(vForm, request, response).write(out);
					out.flush();
					out.close();
					return null;

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		
		Collection<AmpAuditLogger> logs=AuditLoggerUtil.getLogObjects();
		
		if (request.getParameter("sortBy")!=null){
			vForm.setSortBy(request.getParameter("sortBy"));
		}
		if(vForm.getSortBy()!=null){
                    String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
                    String langCode = RequestUtils.getNavigationLanguage(request).getCode();
			  if(vForm.getSortBy().equalsIgnoreCase("nameasc")){
	    		  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerNameComparator()) ;
			  }
			  else if(vForm.getSortBy().equalsIgnoreCase("namedesc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerNameComparator());
				  Collections.reverse((List<AmpAuditLogger>)logs);
			  }
			  else if(vForm.getSortBy().equalsIgnoreCase("typeasc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerTypeComparator());
				}
			  else if(vForm.getSortBy().equalsIgnoreCase("typedesc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerTypeComparator());
				  Collections.reverse((List<AmpAuditLogger>)logs);
			  }
			  else if(vForm.getSortBy().equalsIgnoreCase("teamasc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerTeamComparator());
				}
			  else if(vForm.getSortBy().equalsIgnoreCase("teamdesc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerTeamComparator());
				  Collections.reverse((List<AmpAuditLogger>)logs);
			  }
			  else if(vForm.getSortBy().equalsIgnoreCase("authorasc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerAuthorComparator());
				}
			  else if(vForm.getSortBy().equalsIgnoreCase("authordesc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerAuthorComparator());
				  Collections.reverse((List<AmpAuditLogger>)logs);
			  }
			  else if(vForm.getSortBy().equalsIgnoreCase("creationdateasc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerCreationDateComparator());
				}
			  else if(vForm.getSortBy().equalsIgnoreCase("creationdatedesc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerCreationDateComparator());
				  Collections.reverse((List<AmpAuditLogger>)logs);
			  }
			  else if(vForm.getSortBy().equalsIgnoreCase("editorasc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerEditorNameComparator());
				}
			  else if(vForm.getSortBy().equalsIgnoreCase("editordesc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerEditorNameComparator());
				  Collections.reverse((List<AmpAuditLogger>)logs);
			  }
			  else if(vForm.getSortBy().equalsIgnoreCase("actionasc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerActionComparator());
				}
			  else if(vForm.getSortBy().equalsIgnoreCase("actiondesc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerActionComparator());
				  Collections.reverse((List<AmpAuditLogger>)logs);
			  }
			  else if(vForm.getSortBy().equalsIgnoreCase("changedateasc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerChangeDateComparator());
				}
			  else if(vForm.getSortBy().equalsIgnoreCase("changedatedesc")){
				  Collections.sort((List<AmpAuditLogger>)logs, new AuditLoggerUtil.HelperAuditloggerChangeDateComparator());
				  Collections.reverse((List<AmpAuditLogger>)logs);
			  }
                          else {
                              if (vForm.getSortBy().equalsIgnoreCase("detaildesc")) {
                                  Collections.sort((List<AmpAuditLogger>) logs, new AuditLoggerUtil.HelperAuditloggerDetailComparator(langCode,siteId));
                                  Collections.reverse((List<AmpAuditLogger>) logs);
                              } else {
                                  if (vForm.getSortBy().equalsIgnoreCase("detailasc")) {
                                      Collections.sort((List<AmpAuditLogger>) logs, new AuditLoggerUtil.HelperAuditloggerDetailComparator(langCode,siteId));
                                  }

                              }

                          }
		}
		vForm.setPagesToShow(10);
		int totalrecords=20;
		int page = 0;
		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}
		int stIndex = ((page - 1) * totalrecords) + 1;
		int edIndex = page * totalrecords;
		Collection tempCol = new ArrayList();
		AmpAuditLogger[] tmplogs = (AmpAuditLogger[])logs.toArray(new AmpAuditLogger[0]);
		for (int i = (stIndex - 1); i < edIndex; i++) {
			if (logs.size() > i){
				tempCol.add(tmplogs[i]);
			}
			else{
				break;
			}
		 }
		
		Collection pages = new ArrayList();
		int numpages;
		numpages = logs.size() / totalrecords;
		numpages += (logs.size()  % totalrecords != 0) ? 1 : 0;
		
		if ((numpages) >= 1) {
	        for (int i = 0; i < (numpages); i++) {
	          Integer pageNum = new Integer(i + 1);
	          pages.add(pageNum);
	        }
	     }
	      
	    vForm.setPages(pages);  
		vForm.setCurrentPage(new Integer(page));
		vForm.setLogs(tempCol);
		if (!pages.isEmpty()){
			vForm.setPagesSize(pages.size());
		}
		return  modeSelect(mapping, form, request, response);
	}

	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("forward");
	}
	
	private HSSFWorkbook XlsMaker( AuditLoggerManagerForm form, HttpServletRequest request, HttpServletResponse response) {
		int interval = Integer.parseInt(form.getFrecuency());
		Collection<AmpAuditLogger> Xlslogs=AuditLoggerUtil.getLogByPeriod(interval);
		Collections.sort((List<AmpAuditLogger>) Xlslogs);
		HSSFWorkbook wb = new HSSFWorkbook();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline; filename=Audit-logger.xls");
		HSSFSheet sheet = wb.createSheet("Audit-logger.xls");
		
		HSSFRow row = sheet.createRow((short)(0));
		HSSFRichTextString str = null;
		HSSFFont titlefont = wb.createFont();

		titlefont.setFontHeightInPoints((short)10);
		titlefont.setBoldweight(titlefont.BOLDWEIGHT_BOLD);
		
		HSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short)8);
		font.setBoldweight(font.BOLDWEIGHT_NORMAL);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFCellStyle tstyle = wb.createCellStyle();
		tstyle .setFont(titlefont);
		tstyle .setAlignment(style.ALIGN_CENTER);
		
	    HSSFCell cell = row.createCell((short)0);
		str = new HSSFRichTextString("Name");
		cell.setCellValue(str);
		cell.setCellStyle(tstyle );
		
		cell = row.createCell((short)1);
		str = new HSSFRichTextString("Object Type");
		cell.setCellValue(str);
		cell.setCellStyle(tstyle );
		
		cell = row.createCell((short)2);
		str = new HSSFRichTextString("Team Name");
		cell.setCellValue(str);
		cell.setCellStyle(tstyle );
		
		cell = row.createCell((short)3);
		str = new HSSFRichTextString("Author Name");
		cell.setCellValue(str);
		cell.setCellStyle(tstyle);
		
		cell = row.createCell((short)4);
		str = new HSSFRichTextString("Creation Date");
		cell.setCellValue(str);
		cell.setCellStyle(tstyle);
		
		cell = row.createCell((short)5);
		str = new HSSFRichTextString("Editor Name");
		cell.setCellValue(str);
		cell.setCellStyle(tstyle);
		
		cell = row.createCell((short)6);
		str = new HSSFRichTextString("Change Date");
		cell.setCellValue(str);
		cell.setCellStyle(tstyle);
		
		cell = row.createCell((short)7);
		str = new HSSFRichTextString("Action");
		cell.setCellValue(str);
		cell.setCellStyle(tstyle);
		
		style.setFont(font);
		int i = 1;
		for (Iterator iterator = Xlslogs.iterator(); iterator.hasNext();) {
			AmpAuditLogger ampAuditLogger = (AmpAuditLogger) iterator.next();
			row = sheet.createRow((short)(i));
			
			cell = row.createCell((short)0);
			str = new HSSFRichTextString(ampAuditLogger.getObjectName());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			
			cell = row.createCell((short)1);
			str = new HSSFRichTextString(ampAuditLogger.getObjectTypeTrimmed());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			
			cell = row.createCell((short)2);
			str = new HSSFRichTextString(ampAuditLogger.getTeamName());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			
			cell = row.createCell((short)3);
			str = new HSSFRichTextString(ampAuditLogger.getAuthorName());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			
			cell = row.createCell((short)4);
			str = new HSSFRichTextString(ampAuditLogger.getSloggeddate());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			
			cell = row.createCell((short)5);
			str = new HSSFRichTextString(ampAuditLogger.getEditorName());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			
			cell = row.createCell((short)6);
			str = new HSSFRichTextString(ampAuditLogger.getSmodifydate());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			
			cell = row.createCell((short)7);
			str = new HSSFRichTextString(ampAuditLogger.getAction());
			cell.setCellValue(str);
			cell.setCellStyle(style);
			i++;
		}
		sheet.autoSizeColumn((short)0);
		sheet.autoSizeColumn((short)1);
		sheet.autoSizeColumn((short)2);
		sheet.autoSizeColumn((short)3);
		sheet.autoSizeColumn((short)4);
		sheet.autoSizeColumn((short)5);
		sheet.autoSizeColumn((short)6);
		sheet.autoSizeColumn((short)7);
		
		return wb;
		
		
	}
	
}