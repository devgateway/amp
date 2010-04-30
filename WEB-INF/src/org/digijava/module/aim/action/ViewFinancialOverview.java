package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.FinancialOverviewForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.gateperm.core.GatePermConst;

public class ViewFinancialOverview extends TilesAction {
	private static Logger logger = Logger
			.getLogger(ViewFinancialOverview.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {
		
		HttpSession session = request.getSession();
		TeamMember teamMember = (TeamMember) session
				.getAttribute("currentMember");
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
		FinancialOverviewForm formBean = (FinancialOverviewForm) form;
		if (session.getAttribute("currentMember") == null) {
			formBean.setSessionExpired(true);
		} else {
			formBean.setSessionExpired(false);
			
			FilterParams fp = (FilterParams) session.getAttribute("filterParams");

			fp.setAmpFundingId(new Long(formBean.getAmpFundingId()));
			session.setAttribute("filterParams", fp);
			Long ampFundingId = new Long(formBean.getAmpFundingId());

			if (logger.isDebugEnabled())
				logger.debug("ampFundingId : " + ampFundingId);

			Long id = new Long(formBean.getAmpActivityId());
			AmpActivity ampActivity = ActivityUtil.getProjectChannelOverview(id);

			Collection temp = null;
			Iterator iter = null;
			String tempDateStore = "";
			Date tempDate = null;

			formBean.setCurrency(Constants.DEFAULT_CURRENCY);
			//AMP-2212
			formBean.setFiscalCalId(DbUtil.getGregorianCalendar().getAmpFiscalCalId());
			formBean.setFromYear(Constants.FROM_YEAR);
			formBean.setToYear(Constants.TO_YEAR);

			AmpCategoryValue modality = null;
			if (ampActivity != null) {
				modality = ampActivity.getModality();

				if (logger.isDebugEnabled())
					logger.debug("Modality name : "
							+ (modality != null ? modality.getValue()
									: "null"));
				Site site = RequestUtils.getSite(request);
				Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
				String siteId = site.getId().toString();
				String locale = navigationLanguage.getCode();
				
				if (modality != null)
					formBean.setTypeOfAssistance(TranslatorWorker.translateText((String) modality.getValue(),locale,siteId));

				formBean.setAmpId(ampActivity.getAmpId());

				temp = DbUtil.getAmpFunding(ampActivity.getAmpActivityId(),
						ampFundingId);
				if (logger.isDebugEnabled())
					logger.debug("temp ampFunding collection " + temp.size());
				iter = temp.iterator();
				AmpFunding ampFunding = null;
				if (iter.hasNext()) {
					ampFunding = (AmpFunding) iter.next();
				}

				if (ampFunding != null) {
					if (logger.isDebugEnabled())
						logger.debug("Financing id="
								+ ampFunding.getFinancingId());
					formBean.setDonorFundingId(ampFunding.getFinancingId());
					formBean.setAmpFundingId(ampFunding.getAmpFundingId()
							.longValue());

					AmpOrganisation ampOrg = ampFunding.getAmpDonorOrgId();
					if (ampOrg != null) {
						String goeId = DbUtil.getGoeId(id);
						logger.debug("GOE ID " + goeId);
						formBean.setGoeId(goeId);
						logger.debug("Donor: " + ampOrg.getName());
						formBean.setDonor(ampOrg.getName());
					}

					formBean.setConditions(ampFunding.getConditions());
					formBean.setDonorObjective(ampFunding.getDonorObjective());
					//AmpTermsAssist ampTermsAssist = ampFunding
					//		.getAmpTermsAssistId();
					AmpCategoryValue typeOfAssistance	= ampFunding.getTypeOfAssistance();
					if (typeOfAssistance != null) {
//						formBean.setTermsOfAssistance(ampTermsAssist
//								.getTermsAssistName());
					
						formBean.setTermsOfAssistance(TranslatorWorker.translateText((String) typeOfAssistance.getValue(),locale,siteId));
					}
					if (logger.isDebugEnabled())
						logger.debug("signature date : "
								+ ampFunding.getSignatureDate());
					tempDateStore = DateConversion
							.ConvertDateToString(ampFunding.getSignatureDate());
					formBean.setSignatureDate(tempDateStore);
					if (logger.isDebugEnabled())
						logger.debug("effective date : "
								+ ampFunding.getActualStartDate());
					tempDateStore = DateConversion
							.ConvertDateToString(ampFunding
									.getActualStartDate());
					formBean.setActualStartDate(tempDateStore);
					tempDate = DbUtil.getClosingDate(ampFundingId, new Integer(
							2));
					if (logger.isDebugEnabled())
						logger.debug("current closing date : " + tempDate);
					tempDateStore = DateConversion
							.ConvertDateToString(tempDate);
					formBean.setActualCompletionDate(tempDateStore);
					tempDateStore = DateConversion
							.ConvertDateToString(ampFunding.getReportingDate());
					formBean.setReportingDate(tempDateStore);
					if (logger.isDebugEnabled())
						logger.debug("comments : " + ampFunding.getComments());
					
					if(ampFunding.getComments() != null){
					formBean.setComments(ampFunding.getComments());
					}
					
					if(ampFunding.getConditions() != null){
						formBean.setConditions(ampFunding.getConditions().trim());
					}
					if(ampFunding.getDonorObjective() != null){
						formBean.setDonorObjective(ampFunding.getDonorObjective());
					}
					
					if (logger.isDebugEnabled())
						logger.debug("last audit date : "
								+ ampFunding.getLastAuditDate());
					tempDateStore = DateConversion
							.ConvertDateToString(ampFunding.getLastAuditDate());
					formBean.setLastAuditDate(tempDateStore);
					tempDate = DbUtil.getClosingDate(ampFundingId, new Integer(
							0));
					if (logger.isDebugEnabled())
						logger.debug("original completion date : " + tempDate);
					tempDateStore = DateConversion
							.ConvertDateToString(tempDate);
					formBean.setOriginalClosingDate(tempDateStore);
				}
			}
		}
		return null;
	}
}

