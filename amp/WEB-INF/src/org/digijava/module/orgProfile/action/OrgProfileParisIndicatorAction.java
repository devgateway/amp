package org.digijava.module.orgProfile.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocToOrgDAO;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.orgProfile.form.OrgProfilePIForm;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.helper.ParisIndicatorHelper;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.parisindicator.util.PIConstants;

/**
 * 
 * @author medea
 */
public class OrgProfileParisIndicatorAction extends Action {

	private static Logger logger = Logger
			.getLogger(OrgProfileParisIndicatorAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OrgProfilePIForm piForm = (OrgProfilePIForm) form;
		Collection<AmpAhsurveyIndicator> indicators = DbUtil.getAllAhSurveyIndicators();
		HttpSession session = request.getSession();
		FilterHelper filter = (FilterHelper) session
				.getAttribute("orgProfileFilter");
		filter.getOrgIds();
		Iterator<AmpAhsurveyIndicator> iter = indicators.iterator();
		List<ParisIndicatorHelper> indicatorHelpers = new ArrayList<ParisIndicatorHelper>();

		while (iter.hasNext()) {
			AmpAhsurveyIndicator piIndicator = iter.next();
			ParisIndicatorHelper piHelper = new ParisIndicatorHelper(
					piIndicator, filter);

			if (piIndicator.getIndicatorCode().equals("10b")) {
				TeamMember member = filter.getTeamMember();
				Long teamId=null;
				if(member!=null){
					teamId=member.getTeamId();
				}
				boolean fromPublicView=filter.getFromPublicView();
				List<NodeWrapper> nodeWrappers=OrgProfileUtil.getNodeWrappers(request, teamId, fromPublicView);
				piHelper.setNodesWrappers(nodeWrappers);
			}

			indicatorHelpers.add(piHelper);
			/*
			 * we should add indicator 5aii and indicator 5bii, these indicators
			 * don't exist in db so we add them manually
			 */

			if (piIndicator.getIndicatorCode().equals("5a")) {
				AmpAhsurveyIndicator ind5aii = new AmpAhsurveyIndicator();
				ind5aii.setIndicatorCode("5aii");
				ind5aii.setAmpIndicatorId(piIndicator.getAmpIndicatorId());
				ind5aii.setName("Number of donors using country PFM");
				ParisIndicatorHelper piInd5aHelper = new ParisIndicatorHelper(
						ind5aii, filter);
				indicatorHelpers.add(piInd5aHelper);
			}
			if (piIndicator.getIndicatorCode().equals("5b")) {
				AmpAhsurveyIndicator ind5bii = new AmpAhsurveyIndicator();
				ind5bii.setIndicatorCode("5bii");
				ind5bii.setAmpIndicatorId(piIndicator.getAmpIndicatorId());
				ind5bii.setName("Number of donors using country procurement system");
				ParisIndicatorHelper piInd5bHelper = new ParisIndicatorHelper(
						ind5bii, filter);
				indicatorHelpers.add(piInd5bHelper);
			}

		}
		piForm.setFiscalYear(filter.getYear());
		String name = "";
		if (filter.getOrgIds() != null) {
			if (filter.getOrgIds().length == 1) {
				name = filter.getOrganization().getName();
			} else {
				name = "Multiple Organizations Selected";
			}
		} else {
			if (filter.getOrgGroupId() != -1) {
				name = filter.getOrgGroup().getOrgGrpName();
			} else {
				name = "All";
			}
		}
		piForm.setName(name);
		piForm.setIndicators(indicatorHelpers);
		return mapping.findForward("forward");
	}

}
