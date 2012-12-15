package org.digijava.module.dataExchange.action;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.form.ExportForm;
import org.digijava.module.dataExchange.jaxb.Activities;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.jaxb.ObjectFactory;
import org.digijava.module.dataExchange.util.ExportBuilder;
import org.digijava.module.dataExchange.util.ExportHelper;
import org.digijava.module.dataExchange.util.ExportUtil;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;


public class ExportWizardAction extends DispatchAction {

	private static Logger log = Logger.getLogger(ExportWizardAction.class);

	public ActionForward prepear(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		log.debug("ExportWizardAction.prepear call");

		ExportForm eForm = (ExportForm)form;

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

						eBuilder = new ExportBuilder(ampActivity, RequestUtils.getSite(request));
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
			ServletOutputStream outputStream = null;
			try {
				// package name
				JAXBContext	jc = JAXBContext.newInstance("org.digijava.module.dataExchange.jaxb");
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				outputStream = response.getOutputStream();
				m.marshal(activities, outputStream);

			} catch (javax.xml.bind.JAXBException jex) {
				log.error("dataExchange.export.error JAXB Exception!",jex);
			} catch (java.io.FileNotFoundException fex) {
				log.error("dataExchange.export.error File not Found!",fex);
			} catch (Throwable ex) {
				log.error("dataExchange.export.error", ex);
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

//		response.setHeader("content-disposition","attachment; filename=exportLog.txt"); // file name will generate by date
		
		response.setContentType("text/xml");
		response.setHeader("content-disposition","attachment; filename=exportActivities.xml");
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			if (eForm.getExportLog() != null ){
				outputStream.println("AmpID\tActivity Name\tError");

				for (String[] row : eForm.getExportLog()) {
					outputStream.print(row[0]+"\t");
					outputStream.print(row[1]+"\t");
					outputStream.println(row[2]);
				}

			} else {
				outputStream.println("There is no error while Export");
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
	
	
	
	
	public ActionForward amplifyExport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		HttpSession session = request.getSession();
		//String email = "atl@amp.org";
		String email = (String)request.getParameter("j_username");
		ArrayList<AmpTeam> allTeams = (ArrayList<AmpTeam>) TeamMemberUtil.getAllTeamsForUser(email);
		List<AmpActivity> activities = ExportUtil.getActivitiesForTeams(allTeams);
		TeamMember tm = null;
        if (session.getAttribute("currentMember") != null)
        	tm = (TeamMember) session.getAttribute("currentMember");
        if(tm!=null)
        	System.out.println("++++++++++++++++"+tm.getEmail());
		
        ExportForm eForm = (ExportForm)form;
		response.setContentType("text/xml");
		response.setHeader("content-disposition","attachment; filename=exportActivities"+email+".xml"); // file name will generate by date
		ServletOutputStream outputStream = null;
		try {
			
			//outputStream.println("There is no error while Export");
			JAXBContext jc;
			try {
				jc = JAXBContext.newInstance(org.digijava.module.dataExchange.utils.Constants.IDML_JAXB_INSTANCE);
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				//java.io.StringWriter sw = new StringWriter();
				outputStream = response.getOutputStream();
				m.marshal(DataExchangeUtils.generateAmplifyExport(activities),outputStream);
				//outputStream.println(sw.toString()); 
			} catch (JAXBException e) {
				e.printStackTrace();
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

}
