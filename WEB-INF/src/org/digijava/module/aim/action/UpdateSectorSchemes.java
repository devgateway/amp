
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;

public class UpdateSectorSchemes extends Action {

	private static Logger logger = Logger.getLogger(UpdateSectorSchemes.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
		logger.debug("Comes into the  UpdateSectorSchemes action");
		AddSectorForm sectorsForm = (AddSectorForm) form;
		//sectorsForm.setDeleteScheme("");
		String event = request.getParameter("event");
		String sId = request.getParameter("ampSecSchemeId");
		if(sId != null && !"".equals(sId)) session.setAttribute("ampSecSchemeId", sId);
		Integer id = new Integer(0);
		if(sId!=null)
	    id = new Integer(sId);
		
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		Long siteId = site.getId();
		String locale = navigationLanguage.getCode();
		logger.info("FinalID=============================="+id);
		String sortByColumn = request.getParameter("sortByColumn");		
		if(event!=null){
			if(event.equalsIgnoreCase("edit")){
				Collection schemeGot = SectorUtil.getEditScheme(id);
	
				Collection sectors = SectorUtil.getSectorLevel1(id);
				if(sortByColumn==null || sortByColumn.compareTo("sectorCode")==0){
					List<AmpSector> sec = new ArrayList<AmpSector>(sectors);
					Collections.sort(sec, new Comparator<AmpSector>(){
						public int compare(AmpSector a1, AmpSector a2) {
							String s1	= a1.getSectorCodeOfficial();
							String s2	= a2.getSectorCodeOfficial();
							if ( s1 == null )
								s1	= "";
							if ( s2 == null )
								s2	= "";
						
							return s1.toUpperCase().trim().compareTo(s2.toUpperCase().trim());
						}
					});
				
					sectorsForm.setFormFirstLevelSectors(sec);
				}
				else if(sortByColumn!=null && sortByColumn.compareTo("sectorName")==0){
					sectorsForm.setFormFirstLevelSectors(sectors);
				}
				
				Iterator itr = schemeGot.iterator();
				while (itr.hasNext()) {
					AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
					sectorsForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
					sectorsForm.setSecSchemeName(ampScheme.getSecSchemeName());
					sectorsForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
					sectorsForm.setParentId(ampScheme.getAmpSecSchemeId());
				}
	
				//session.setAttribute("Id",null);
				return mapping.findForward("viewSectorSchemeLevel1");
			}
			
			else if (event.equals("addscheme")) {
				logger.debug("now add a new  scheme");
				return mapping.findForward("addSectorScheme");
			}
			else if (event.equals("saveScheme")) {
				logger.debug("saving the scheme");
				AmpSectorScheme ampscheme = new AmpSectorScheme();
				logger.debug(" the name is...."	+ sectorsForm.getSecSchemeName());
				logger.debug(" the code is ...."+ sectorsForm.getSecSchemeCode());
				
				if(checkSectorNameCodeIsNull(sectorsForm)){
					//request.setAttribute("event", "view");
					ActionErrors errors = new ActionErrors();
	        		errors.add("title", new ActionError("error.aim.addScheme.emptyTitleOrCode", TranslatorWorker.translateText("The name or code of the scheme is empty. Please enter a title and a code for the scheme.",locale,siteId)));
	        		if (errors.size() > 0)
	        			{
	        				saveErrors(request, errors);
	        				session.setAttribute("managingSchemes",errors);
	        			}
					return mapping.findForward("viewSectorSchemes");
				}
				
				if(existScheme(sectorsForm) == 1){
					request.setAttribute("event", "view");
					ActionErrors errors = new ActionErrors();
	        		errors.add("title", new ActionError("error.aim.addScheme.wrongTitle", TranslatorWorker.translateText("The name of the scheme already exist in database. Please enter another title",locale,siteId)));
	        		if (errors.size() > 0)
        			{
        				saveErrors(request, errors);
        				session.setAttribute("managingSchemes",errors);
        			}
					return mapping.findForward("viewSectorSchemes");
				}
				
				if(existScheme(sectorsForm) == 2){
					request.setAttribute("event", "view");
					ActionErrors errors = new ActionErrors();
	        		errors.add("title", new ActionError("error.aim.addScheme.wrongCode", TranslatorWorker.translateText("The code of the scheme already exist in database. Please enter another code",locale,siteId)));
	        		if (errors.size() > 0)
        			{
        				saveErrors(request, errors);
        				session.setAttribute("managingSchemes",errors);
        			}
					return mapping.findForward("viewSectorSchemes");
				}
				
				ampscheme.setSecSchemeCode(sectorsForm.getSecSchemeCode());
				ampscheme.setSecSchemeName(sectorsForm.getSecSchemeName());
				DbUtil.add(ampscheme);
				request.setAttribute("event", "view");
				session.setAttribute("managingSchemes",null);
				logger.debug("done kutte");
				//scheme = SectorUtil.getSectorSchemes();
				//sectorsForm.setFormSectorSchemes(scheme);
				if (SectorUtil.getAllSectorSchemes().size() == 1) {
					Collection schemes = SectorUtil.getAllSectorSchemes();
					Iterator it = schemes.iterator();
					AmpSectorScheme ampScheme = null;
					while (it.hasNext()) {
						ampScheme = (AmpSectorScheme) it.next();
					}
					List<AmpClassificationConfiguration> classificationsList = SectorUtil.getAllClassificationConfigs();
					Iterator iter = classificationsList.iterator();
					AmpClassificationConfiguration classification = null;
					while (iter.hasNext()) {
						AmpClassificationConfiguration classificationTemp = (AmpClassificationConfiguration) iter.next();
						if (classificationTemp.getName().equalsIgnoreCase("Primary")) {
							classification = classificationTemp;
						}
					}
					if (classification != null) {
						SectorUtil.saveClassificationConfig(classification.getId(), classification.getName(), classification.isMultisector(), ampScheme);
					}else{
						SectorUtil.saveClassificationConfig(0l, "Primary", true, ampScheme);
					}
					ActionErrors errors = new ActionErrors();
	        		errors.add("title", new ActionError("error.aim.addScheme.setDefConfig", TranslatorWorker.translateText("Scheme added was set to default Primary Configuration.",locale,siteId)));
	        		if (errors.size() > 0)
        			{
        				saveErrors(request, errors);
        				session.setAttribute("managingSchemes",errors);
        			}
				}	
					
				return mapping.findForward("viewSectorSchemes");
			}
			else if (event.equals("updateScheme")) {
				logger.debug(" updating Scheme");
				String editId = (String) request.getParameter("editSchemeId");
				Long Id;
				AmpSectorScheme ampscheme = new AmpSectorScheme();
				if(editId == null || "".equals(editId))
					Id = new Long((String)session.getAttribute("ampSecSchemeId"));
				else Id = new Long(editId);
				logger.debug(" the name is...."	+ sectorsForm.getSecSchemeName());
				logger.debug(" the code is ...."+ sectorsForm.getSecSchemeCode());
				logger.debug(" this is the id......" + Id);
				
				if(checkSectorNameCodeIsNull(sectorsForm)){
					request.setAttribute("event", "view");
					ActionErrors errors = new ActionErrors();
	        		errors.add("title", new ActionError("error.aim.addScheme.emptyTitleOrCode", TranslatorWorker.translateText("The name or code of the scheme is empty. Please enter a title and a code for the scheme.",locale,siteId)));
	        		if (errors.size() > 0)
        			{
        				saveErrors(request, errors);
        				session.setAttribute("managingSchemes",errors);
        			}
					return mapping.findForward("viewSectorSchemes");
				}
				
				if(existSchemeForUpdate(sectorsForm,Id) == 1){
					request.setAttribute("event", "view");
					ActionErrors errors = new ActionErrors();
	        		errors.add("title", new ActionError("error.aim.addScheme.wrongTitle", TranslatorWorker.translateText("The name of the scheme already exist in database. Please enter another title",locale,siteId)));
	        		if (errors.size() > 0)
        			{
        				saveErrors(request, errors);
        				session.setAttribute("managingSchemes",errors);
        			}
					return mapping.findForward("viewSectorSchemes");
				}
				
				if(existSchemeForUpdate(sectorsForm,Id) == 2){
					request.setAttribute("event", "view");
					ActionErrors errors = new ActionErrors();
	        		errors.add("title", new ActionError("error.aim.addScheme.wrongCode", TranslatorWorker.translateText("The code of the scheme already exist in database. Please enter another code",locale,siteId)));
	        		if (errors.size() > 0)
        			{
        				saveErrors(request, errors);
        				session.setAttribute("managingSchemes",errors);
        			}
					return mapping.findForward("viewSectorSchemes");
				}
				session.setAttribute("managingSchemes",null);
				ampscheme.setSecSchemeCode(sectorsForm.getSecSchemeCode());
				ampscheme.setSecSchemeName(sectorsForm.getSecSchemeName());
				ampscheme.setAmpSecSchemeId(Id);
				DbUtil.update(ampscheme);
				logger.debug(" updated!!");
				return mapping.findForward("viewSectorSchemes");
			}
			else if(event.equalsIgnoreCase("deleteScheme"))	{
				logger.debug("in the delete Scheme");
				Long id1 = new Long(request.getParameter("ampSecSchemeId"));
				Integer a = new Integer(request.getParameter("ampSecSchemeId"));
				sectorsForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(a));
				if (sectorsForm.getFormFirstLevelSectors().size() >= 1) {
					logger.debug("no deletion");
					//ActionErrors errors = new ActionErrors();
					//errors.add("title", new ActionError("error.aim.deleteScheme.schemeSelected"));
					//saveErrors(request, errors);
					session.setAttribute("schemeDeletedError", "true");
					return mapping.findForward("viewSectorSchemes");
				}
				else
					SectorUtil.deleteScheme(id1);
				return mapping.findForward("viewSectorSchemes");
				
			}
		}
		

		//scheme = SectorUtil.getSectorSchemes();
		//sectorsForm.setFormSectorSchemes(scheme);

		return mapping.findForward("viewSectorSchemes");
	}
	
	private boolean checkSectorNameCodeIsNull(AddSectorForm sectorsForm){
		if(sectorsForm.getSecSchemeCode() == null || sectorsForm.getSecSchemeName() == null ||
				"".equals(sectorsForm.getSecSchemeCode()) || "".equals(sectorsForm.getSecSchemeName()) )
			return true;
		return false;
	}
	
	private int existScheme (AddSectorForm sectorsForm){
		Collection<AmpSectorScheme> schemes = (Collection<AmpSectorScheme>)SectorUtil.getAllSectorSchemes();
		for (Iterator it = schemes.iterator(); it.hasNext();) {
			AmpSectorScheme scheme = (AmpSectorScheme) it.next();
			if(scheme.getSecSchemeName() != null && sectorsForm.getSecSchemeName().equals(scheme.getSecSchemeName())) return 1;
			if(scheme.getSecSchemeCode() != null && sectorsForm.getSecSchemeCode().equals(scheme.getSecSchemeCode())) return 2;
		}
		return 0;
	}
	
	private int existSchemeForUpdate (AddSectorForm sectorsForm, Long Id){
		Collection<AmpSectorScheme> schemes = (Collection<AmpSectorScheme>)SectorUtil.getAllSectorSchemes();
		for (Iterator it = schemes.iterator(); it.hasNext();) {
			AmpSectorScheme scheme = (AmpSectorScheme) it.next();
			if(!Id.equals(scheme.getAmpSecSchemeId())){
				if( scheme.getSecSchemeName() != null && sectorsForm.getSecSchemeName().equals(scheme.getSecSchemeName()) ) 
					return 1;
				if( scheme.getSecSchemeCode() != null && sectorsForm.getSecSchemeCode().equals(scheme.getSecSchemeCode()) ) 
					return 2;
			}
		}
		return 0;
	}
	
}


