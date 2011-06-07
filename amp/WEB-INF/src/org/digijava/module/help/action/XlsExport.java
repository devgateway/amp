/**
 * 
 */
package org.digijava.module.help.action;

import java.io.StringReader;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.util.GlossaryUtil;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.simpleparser.HTMLWorker;

/**
 * @author mouhamad
 *
 */
public class XlsExport extends Action {

    private static Logger logger = Logger.getLogger(XlsExport.class);

    /**
     * 
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        //
    	ServletContext ampContext = getServlet().getServletContext();
        response.setContentType("application/msexcel");
        response.setHeader("content-disposition", "attachment;filename=glossary.xls");
        //
        try {
    		Integer[] r = {0};
    		Integer[] c = {0};    	
    		//
            String language = RequestUtils.getNavigationLanguage(request).getCode();
    		String moduleInstance = RequestUtils.getModuleInstance(request).getInstanceName();
    		String siteId = RequestUtils.getSite(request).getSiteId();
            //
    		HSSFWorkbook workbook = new HSSFWorkbook();
    		HSSFSheet sheet = workbook.createSheet("Glossary");
    		//
			List<HelpTopic> helpTopics = GlossaryUtil.getChildTopics(siteId, moduleInstance, null);
			//
			writechild(r, c, helpTopics, moduleInstance, siteId, language, sheet);
			//
    		ServletOutputStream out = response.getOutputStream();
    		workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception ex) {
            logger.error("error", ex);
            ex.printStackTrace();
        }
        //
        return null;        
    }
    
    /**
     * 
     * @param r
     * @param c
     * @param helpTopics
     * @param moduleInstance
     * @param siteId
     * @param language
     * @param sheet
     */
	@SuppressWarnings("unchecked")
	private void writechild (Integer[] r, Integer[] c, List<HelpTopic> helpTopics, String moduleInstance, String siteId, String language, HSSFSheet sheet) {
  		//
    	try {
    		List<HelpTopic> childs = null;
			String body = null;
			HSSFRow row = null;
			HSSFCell cell = null;
			List<Element> objects = null;
			for (HelpTopic helpTopic : helpTopics) {
				//
				row = sheet.createRow(r[0].intValue());
				cell = row.createCell(c[0].intValue());
	    		//
	    		cell.setCellValue(TranslatorWorker.translateText(helpTopic.getTopicKey(), language, siteId));
	            //    		
	    		r[0]++;
				row = sheet.createRow(r[0].intValue());
				cell = row.createCell(c[0].intValue());
	    		//
				body = DbUtil.getEditorBody(siteId, helpTopic.getBodyEditKey(), language);
				if (body == null) body = "";
				//
				objects = HTMLWorker.parseToList(new StringReader(TranslatorWorker.translateText(body, language, siteId)), null);
	            for (Element element : objects) {
	            	
	            	//
	            	cell.setCellValue(element.toString());
	            }
				//cell.setCellValue(TranslatorWorker.translateText(body, language, siteId));
	            //	   		
	    		r[0]++;
	    		//
	            childs = GlossaryUtil.getChildTopics(siteId, moduleInstance, helpTopic.getHelpTopicId());
	            if ((childs != null) && (!childs.isEmpty())) {
	            	c[0]++;
		            writechild(r, c, childs, moduleInstance, siteId, language, sheet);	
	            }
			}
    	} catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
