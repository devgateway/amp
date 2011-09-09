package org.digijava.module.translation.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.lucene.LangSupport;
import org.digijava.kernel.lucene.LucModule;
import org.digijava.kernel.lucene.LuceneWorker;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.module.translation.form.ImportExportForm;
import org.digijava.module.translation.importexport.ImportExportOption;
import org.digijava.module.translation.importexport.ImportType;
import org.digijava.module.translation.jaxb.ObjectFactory;
import org.digijava.module.translation.jaxb.Translations;
import org.digijava.module.translation.lucene.LucTranslationModule;
import org.digijava.module.translation.lucene.TrnLuceneModule;
import org.digijava.module.translation.util.ImportExportUtil;

/**
 * Handles all steps of translation import and export wizard in AMP admin menu.
 * Replaces struts action in AIM module with name TranslationManager.java
 * AMP-9085
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class ImportExportTranslations extends Action {
	
	
	public static final String SESSION_FILE = "dgfoundation.amp.translation.import.fileUploaded";
	public static final String SESSION_ROOT = "dgfoundation.amp.translation.import.xmlRoot";

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ImportExportForm ioForm = (ImportExportForm)form;
		List<String> languagesInDb = TranslatorWorker.getAllUsedLanguages();
		ioForm.setLanguages(languagesInDb);

		//these IF's are copied from old TranslationManager.java to not change JSP file.
		//except for Export step, code inside IF's are new - not copied from old file.
		//There are 3 steps for import and 1 for export.
		//This one works when user selects file and clicks import 
		if (request.getParameter("import") != null){
			//we need to read all languages in this file and display to user
			//to let him/her choose which languages should be imported.
			//uploaded file should be stored in session.
			FormFile uploadedFile = ioForm.getFileUploaded();
			byte[] fileData = uploadedFile.getFileData();
			InputStream inputStream = new ByteArrayInputStream(fileData);
			request.getSession().setAttribute(SESSION_FILE, uploadedFile);
			try {
				Unmarshaller unmarshaller = ImportExportUtil.getUnmarshaler();
				Translations root = (Translations) unmarshaller.unmarshal(inputStream);
				request.getSession().setAttribute(SESSION_ROOT, root);
				Set<String> languagesInFile = ImportExportUtil.extractUsedLangages(root);
				ioForm.setImportedLanguages(new ArrayList<String>(languagesInFile));
			}catch (Exception ex) {				
				ex.printStackTrace(System.out);
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.importErrorFileContentTranslation"));				
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
			
		}
		
		//This works when user selects which languages should be imported and how.
		//This is where actual work is done.
		if (request.getParameter("importLang") != null) {
			
			long startTime = System.currentTimeMillis();
			Translations translations = (Translations)request.getSession().getAttribute(SESSION_ROOT);
			
			if (translations == null) {
				ActionErrors errors = new ActionErrors();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.importErrorFileContentTranslation"));
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
			
			String[] selectedLanguages = ioForm.getSelectedImportedLanguages();
			Set<String> languagesToImport = new HashSet<String>(Arrays.asList(selectedLanguages));
			
			Map<String, ImportType> optionsByLang = getImportTypesByLanguage(request, selectedLanguages);
			//TODO check if map is empty - nothing has been selected.
			
			//Setup parameters for importing
			ImportExportOption option = new ImportExportOption();
			option.setLocalesToSave(languagesToImport);
			option.setSearcher(ImportExportUtil.getCacheSearcher());
			option.setTypeByLanguage(optionsByLang);
			
			//Do work - import translation
			ImportExportUtil.importTranslations(translations, option);
			
			//recreating indexes for help
			ServletContext context = getServlet().getServletContext();
			LangSupport[] langs = LangSupport.values();
			for (LangSupport lang : langs) {
				TrnLuceneModule module = new TrnLuceneModule(lang);
				for (String selectedLanguage : selectedLanguages) {
					if (lang.getLangCode().equals(selectedLanguage)) {
						LuceneWorker.recreateIndext(module, context);
					}
				}
			}
			
			request.getSession().removeAttribute(SESSION_FILE);
			request.getSession().removeAttribute(SESSION_ROOT);
			long endTime = System.currentTimeMillis();
			System.out.println("Export finished in "+((endTime-startTime))+" milliseconds");
		}
		
		
		//This works when user selects checkboxes for languages and then clicks Export.
		//Just copied from old file and dis small changes.
		if (request.getParameter("export") != null) {
			if (ioForm != null && ioForm.getSelectedLanguages() != null && ioForm.getSelectedLanguages().length > 0) {
				long startTime = System.currentTimeMillis();
				response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
				response.setHeader("content-disposition", "attachment; filename=exportLanguage.xml");

				Marshaller marshaller = ImportExportUtil.getMarshaller();
				ObjectFactory objFactory = new ObjectFactory();
				Translations translations = objFactory.createTranslations();
				
				Set<String> languagesToExport = new HashSet<String>(Arrays.asList(ioForm.getSelectedLanguages()));
				
				//Do work - export data in Translations instance
				ImportExportUtil.exportTranslations(translations, languagesToExport);
				
				marshaller.marshal(translations, response.getOutputStream());
				
				form = null;
				request.getSession().removeAttribute("aimTranslatorManagerForm"); //???
				long endTime = System.currentTimeMillis();
				System.out.println("Export finished in "+((endTime-startTime))+" milliseconds");
				return null;
			} else {
				ActionErrors errors = new ActionErrors();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.pleaseChooseALanguageForExport"));
				saveErrors(request, errors);
				return mapping.findForward("forward");
			}
			
		}
		return mapping.findForward("forward");
	}
	
	private Map<String,ImportType> getImportTypesByLanguage(HttpServletRequest request,String[] languagesToProcess){
		Map<String, ImportType> result = new HashMap<String, ImportType>();
		//for all specified language
		for (String lang : languagesToProcess) {
			//get import type parameter from request
			String typeName = request.getParameter("LANG:"+lang);
			if (typeName != null){
				//convert to real type
				ImportType type = null; //TODO embed this conversion from string directly in enum
				if (typeName.equalsIgnoreCase("update")) 			type = ImportType.UPDATE;
				else if (typeName.equalsIgnoreCase("overwrite")) 	type = ImportType.OVERWRITE;
				else if (typeName.equalsIgnoreCase("nonexisting")) 	type = ImportType.ONLY_NEW;
				else continue;
				//and put in result map
				result.put(lang, type);
			}
		}
		return result;
	}
}
