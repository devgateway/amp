/*
 * AddAmpActivity.java
 */

package org.digijava.module.aim.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jcr.RepositoryException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityReferenceDoc;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.ActivityContactInfo;
import org.digijava.module.aim.form.ProposedProjCost;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.ReferenceDoc;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.SurveyFunding;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ChapterUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.EUActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.HibernateException;

/**
 * a shadow of the former page; remains active for some reason
 *
 * @author Priyajith
 */
public class AddAmpActivity extends Action {

  private static Logger logger = Logger.getLogger(AddAmpActivity.class);

  private ServletContext ampContext = null;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

	  HttpSession session = request.getSession();
	  request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.EDIT);
	  session.removeAttribute("returnSearch");
	  TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
	  PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
	  
	  Long idForOriginalActivity = null; // AMP-9633
	  EditActivityForm eaForm = (EditActivityForm) form;
	  if(request.getSession().getAttribute("idForOriginalActivity")!=null)
	  {
		  idForOriginalActivity = (Long) request.getSession().getAttribute("idForOriginalActivity") ;
		  request.getSession().removeAttribute("idForOriginalActivity");
		  if(idForOriginalActivity.equals(new Long(-1))){ //create case
			  eaForm.setActivityId(null);
		  }else{ //edit case
			  eaForm.setActivityId(idForOriginalActivity);
		  }
	  }
	  request.setAttribute("actId", eaForm.getActivityId());
	  
	  return mapping.findForward("preview");
  }  
}
