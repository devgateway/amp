package org.digijava.module.dataExchange.action;


import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.form.ExportForm;
import org.digijava.module.dataExchange.form.ExportForm.LogStatus;
import org.digijava.module.dataExchange.jaxb.Activities;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.jaxb.ObjectFactory;
import org.digijava.module.dataExchange.util.ExportBuilder;
import org.digijava.module.dataExchange.util.ExportHelper;
import org.digijava.module.dataExchange.util.ExportUtil;


public class ExportWizardAction extends DispatchAction {

	private static Logger log = Logger.getLogger(ExportWizardAction.class);

	public ActionForward prepear(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		log.debug("ExportWizardAction.prepear call");
		HttpSession session = request.getSession();
		String str = (String) session.getAttribute("ampAdmin");

		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
		ExportForm eForm = (ExportForm)form;
		eForm.logStatus = LogStatus.IS_NULL;
		eForm.setActivityTree(ExportHelper.getActivityStruct("activity","activityTree","activity",ActivityType.class,true));

		eForm.setDonorTypeList(DbUtil.getAllOrgTypesOfPortfolio());
		eForm.setDonorGroupList(ARUtil.filterDonorGroups(DbUtil.getAllOrgGroupsOfPortfolio()));
		eForm.setDonorAgencyList(ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_DONOR));
		eForm.setPrimarySectorsList(SectorUtil.getAmpSectorsAndSubSectors(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME));
		eForm.setSecondarySectorsList(SectorUtil.getAmpSectorsAndSubSectors(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME));

		eForm.setTeamList(TeamUtil.getAllTeams());
		eForm.setExportLog(null);

		String forward = "default";
		return mapping.findForward(forward);

	}


	public ActionForward export(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ExportForm eForm = (ExportForm)form;
		eForm.logStatus = LogStatus.PROCCESSING;

		if (eForm.getActivityTree() != null && 
				eForm.getSelectedTeamId() != null && eForm.getSelectedTeamId().longValue()>=0){
			// TODO: add search criteria. 

			Activities activities = (new ObjectFactory()).createActivities();

			List<AmpActivity> ampActivities  = ExportUtil.getActivities(
					eForm.getSelectedTeamId(),
					eForm.getDonorTypeSelected(),
					eForm.getDonorGroupSelected(),
					eForm.getDonorAgencySelected(),
					eForm.getPrimarySectorsSelected(),
					eForm.getSecondarySectorsSelected()
			);

			try {
				eForm.setExportLog(null);
				for (Iterator iterator = ampActivities.iterator(); iterator.hasNext();) {
					ExportBuilder eBuilder = null;
					try{
						AmpActivity ampActivity = (AmpActivity) iterator.next();

						eBuilder = new ExportBuilder(ampActivity, RequestUtils.getSite(request).getId());
						activities.getActivity().add(eBuilder.getActivityType(eForm.getActivityTree()));
					} catch (AmpExportException e) {
						if (eBuilder!= null && eBuilder.getEroor() != null){
							eForm.addExportLog(eBuilder.getEroor());
						}
						log.error(e);
					}
				}
			} catch (Exception e) {
				log.error(e);
				throw e;
			}
			response.setContentType("text/xml");
			response.setHeader("content-disposition","attachment; filename=exportActivities.xml"); // file name will generate by date
			OutputStreamWriter outputStream =  null;
			try {

				// package name
				JAXBContext	jc = JAXBContext.newInstance("org.digijava.module.dataExchange.jaxb");



				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

	            outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
				m.marshal(activities, outputStream);

				eForm.logStatus = LogStatus.READY;

			} catch (javax.xml.bind.JAXBException jex) {
				log.error("dataExchange.export.error JAXB Exception!",jex);
				eForm.logStatus = LogStatus.ERROR;
			} catch (java.io.FileNotFoundException fex) {
				log.error("dataExchange.export.error File not Found!",fex);
				eForm.logStatus = LogStatus.ERROR;
			} catch (Throwable ex) {
				log.error("dataExchange.export.error", ex);
				eForm.logStatus = LogStatus.ERROR;
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}

		} else {
			log.debug("Wrong information from Export wizard.");
		}

		return null;

	}


	public ActionForward log(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		ExportForm eForm = (ExportForm)form;

		response.setContentType("text/xml");
		response.setHeader("content-disposition","attachment; filename=exportLog.txt"); // file name will generate by date
		OutputStreamWriter outputStream =  null;

		try {
            outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
			if (eForm.getExportLog() != null && eForm.logStatus ==  LogStatus.READY){
				outputStream.write("AmpID\tActivity Name\tError\n");

				for (String[] row : eForm.getExportLog()) {
					outputStream.write(row[0]+"\t");
					outputStream.write(row[1]+"\t");
					outputStream.write(row[2]+"\n");
				}

			} else {
				outputStream.write("There is no error while Export");
			}
		} catch (Exception ex) {
			log.error("dataExchange.exportLog.error", ex);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}


		return null;

	}
	
	public ActionForward logAjax(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		ExportForm eForm = (ExportForm)form;
		
        JSONObject json = new JSONObject();
        json.put("identifier", "exportLoger");
        json.put("label", "json export logger");
        
        
        
        if (eForm.logStatus ==  LogStatus.IS_NULL){
    		json.put("status", "0"); // not ready
        } else if(eForm.logStatus ==  LogStatus.PROCCESSING){
    		json.put("status", "1"); // no proccesing
        } else if (eForm.logStatus ==  LogStatus.READY){
        	if (eForm.getExportLog() != null){
        		json.put("status", "2"); // ready
        		JSONArray logItems = new JSONArray();
//        		for (int i = 0; i < 20; i++) 
        		for (String[] row : eForm.getExportLog()) {
        			JSONObject jsonActivity = new JSONObject();
        			jsonActivity.put("AmpID", row[0]);
        			jsonActivity.put("ActivityName", row[1]);
        			jsonActivity.put("ErrorID", row[2]);

        			logItems.add(jsonActivity);
        		}
        		json.put("items", logItems);
        	} else {
        		json.put("status", "3"); // no error
        	}
        } else if (eForm.logStatus ==  LogStatus.ERROR){
    		json.put("status", "4"); // error
        }
        
		response.setContentType("text/json-comment-filtered");
		OutputStreamWriter outputStream =  null;
        
        try {
            outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
            outputStream.write(json.toString());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

        return null;
		
	}
	
}
