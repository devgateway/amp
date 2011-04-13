/*
 * ViewChannelOverview.java
 */

package org.digijava.module.aim.action;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.ChannelOverviewForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Currency;
import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.FinancingBreakdown;
import org.digijava.module.aim.helper.FinancingBreakdownWorker;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.helper.RelOrganization;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.Logic;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
public class ViewChannelOverview extends TilesAction {

	private static Logger logger = Logger.getLogger(ViewChannelOverview.class);


	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		PermissionUtil.resetScope(session);
		PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
		
		ChannelOverviewForm formBean = (ChannelOverviewForm) form;
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		String debugFM=request.getParameter("debugFM");
		
		if(debugFM!=null && "true".compareTo(debugFM)==0)
			{
				formBean.setDebugFM("true");
				session.setAttribute("debugFM", "true");
			}
		else {
				formBean.setDebugFM("false");
				session.setAttribute("debugFM", "false");
		}
		boolean debug = (request.getParameter("debug")!=null)?true:false;
		
		if (teamMember == null) {
			formBean.setValidLogin(false);
		} else {
			formBean.setValidLogin(true);
			Long id = null;
			if (request.getParameter("ampActivityId") != null) {
				id = new Long(request.getParameter("ampActivityId"));
                formBean.setId(id);
			}
			else {
				id = formBean.getId();
			}
			
			Collection<AmpCategoryValue> implLocationLevels	= null;
			try {
				implLocationLevels = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY, null, request);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if ( implLocationLevels != null){
				try{
					formBean.setNumImplLocationLevels( implLocationLevels.size() );
					AmpCategoryValue country		= 
						CategoryManagerUtil.getAmpCategoryValueFromDB(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
					formBean.setCountryIndex( country.getIndex() );
					
					formBean.setNumOfLocationsPerLayer(new ArrayList<Integer>(formBean.getNumImplLocationLevels()) );
					for (int i=0; i<formBean.getNumImplLocationLevels(); i++) {
						Integer numOfLocations	= DynLocationManagerUtil.getNumOfLocations(i);
						formBean.getNumOfLocationsPerLayer().add(numOfLocations);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}

			try{
			    if(formBean.getClassificationConfigs()==null){
			    	formBean.setClassificationConfigs(SectorUtil.getAllClassificationConfigs());
			    }
			}
			catch(Exception e)
			{
			    logger.debug("Classification Config Not Found.");
			}
			
			AmpActivity activity = null;
			try {
				activity = ActivityUtil.loadActivity(id);
			} catch (DgException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			formBean.clearMessages();
			AmpTeam ampTeam=TeamUtil.getAmpTeam(teamMember.getTeamId());
			boolean hasTeamLead=true;
			AmpTeamMember teamHead = TeamMemberUtil.getTeamHead(teamMember.getTeamId());
			if(teamHead==null) hasTeamLead=false;
			createWarnings(activity,teamMember.getTeamHead(), formBean,hasTeamLead);

			
			PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.ACTIVITY, activity);

			Set orgProjIdsSet = activity.getInternalIds();
	          if (orgProjIdsSet != null) {
	            Iterator projIdItr = orgProjIdsSet.iterator();
	            Collection temp = new ArrayList();
	            while (projIdItr.hasNext()) {
	              AmpActivityInternalId actIntId = (
	                  AmpActivityInternalId) projIdItr
	                  .next();
	              OrgProjectId projId = new OrgProjectId();
	              projId.setId(actIntId.getId());
	              projId.setOrganisation(actIntId.getOrganisation());
	              projId.setProjectId(actIntId.getInternalId());
	              temp.add(projId);
	            }
	            if (temp != null && temp.size() > 0) {
	              OrgProjectId orgProjectIds[] = new OrgProjectId[
	                  temp
	                  .size()];
	              Object arr[] = temp.toArray();
	              for (int i = 0; i < arr.length; i++) {
	                orgProjectIds[i] = (OrgProjectId) arr[i];
	              }
	              formBean.setSelectedOrganizations(orgProjectIds);
	            }
	          }

	          AmpCategoryValue ampCategoryValue = CategoryManagerUtil
					.getAmpCategoryValueFromListByKey(
							CategoryConstants.IMPLEMENTATION_LEVEL_KEY,
							activity.getCategories());

			if (ampCategoryValue != null)
				formBean.setImplemLocationLevel(ampCategoryValue.getId());


			//PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.ACTIVITY,activity);
			ArrayList colAux=new ArrayList();
			Collection ampFields=DbUtil.getAmpFields();
			HashMap allComments=new HashMap();
			for(Iterator itAux=ampFields.iterator(); itAux.hasNext();)
			{
				AmpField field = (AmpField) itAux.next();
				////System.out.println(field.getFieldName());
				colAux = DbUtil.getAllCommentsByField(field.getAmpFieldId(),id);
				allComments.put(field.getFieldName(),colAux);
			}
			formBean.setAllComments(allComments);

			
			try {
				AmpCategoryValue budgetCV =  
					CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_BUDGET_KEY, activity.getCategories() );
				
				formBean.setBudgetCV(budgetCV);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			// added by Akash
			// desc: approval status check
			// start
			/* this section was moved to ViewMainProjectDetails.java

			String actApprovalStatus = DbUtil.getActivityApprovalStatus(id);
			Long ampTeamId = teamMember.getTeamId();
			boolean teamLeadFlag    = teamMember.getTeamHead();
			boolean workingTeamFlag = TeamUtil.checkForParentTeam(ampTeamId);

		 	if (!(activity.getDraft()!=null && activity.getDraft()) && ( actApprovalStatus != null &&
		 			Constants.ACTIVITY_NEEDS_APPROVAL_STATUS.contains(actApprovalStatus.toLowerCase())  ))
		 	{
		 		if (workingTeamFlag && teamLeadFlag && teamMember.getTeamId().equals(activity.getTeam().getAmpTeamId()))
		 			formBean.setButtonText("validate");
		 		else
		 			formBean.setButtonText("approvalAwaited");
		 		
		 	}
		 	else {
		 		if (workingTeamFlag && teamMember.getWrite())
		 			formBean.setButtonText("edit");	// In case of regular working teams
		 		else
		 			formBean.setButtonText("none");	// In case of management-workspace
		 	}*/
		 	// end

			String currCode = null;

			if (teamMember.getAppSettings() != null) {
				ApplicationSettings appSettings = teamMember.getAppSettings();
				if (appSettings.getCurrencyId() != null) {
					currCode = CurrencyUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
				} else {
					currCode = Constants.DEFAULT_CURRENCY;
				}
				formBean.setWrite(teamMember.getWrite());
				formBean.setDelete(teamMember.getDelete());
				formBean.setCurrCode(currCode);

    			// call the logic instance to perform the caculations, so it will depend of each implementancion how we will calculate the total inlcuding or not planned 
    			DecimalWraper total = Logic.getInstance().getTotalDonorFundingCalculator().getTotalCommtiments(activity.getAmpActivityId(), currCode);
            		if (!debug) {
            		    formBean.setGrandTotal(total.toString());
            		} else {
            		    formBean.setGrandTotal(total.getCalculations());
            		}
				
			}
			
			//New Donors code (begin)
			Collection ampFundings = DbUtil.getAmpFunding(id);
		    ApplicationSettings apps = null;
			apps = teamMember.getAppSettings();
		    FilterParams fp = (FilterParams) session.getAttribute(Constants.FILTER_PARAMS);
		    if (fp == null) {
		    	fp = new FilterParams();
		    }
		    if (fp.getCurrencyCode() == null)
			{
				Currency curr = CurrencyUtil.getCurrency(apps.getCurrencyId());
				if (curr != null) {
					fp.setCurrencyCode(curr.getCurrencyCode());
				}
			}
		    if (fp.getFiscalCalId() == null) {
				if (apps.getFisCalId() !=null){
					fp.setFiscalCalId(apps.getFisCalId());
				}else{
					fp.setFiscalCalId(FeaturesUtil.getGlobalSettingValueLong("Default Calendar"));
				}
			}
		    if (fp.getFromYear() == 0 || fp.getToYear() == 0) {
				int year = new GregorianCalendar().get(Calendar.YEAR);
				fp.setFromYear(year-Constants.FROM_YEAR_RANGE);
				fp.setToYear(year+Constants.TO_YEAR_RANGE);
			}
		    Collection fb = FinancingBreakdownWorker.getFinancingBreakdownList(id, ampFundings, fp,debug);
			formBean.setFinancingBreakdown(fb);
			//New Donors code (end)

			AmpTeam team = TeamUtil.getAmpTeam(teamMember.getTeamId());
			if(team.getAccessType().equals("Team"))
				formBean.setWrite(true);
			else
				formBean.setWrite(false);

			formBean.setActivity(activity);
			formBean.setCanView(true);

                        //set programs by setting name

                        formBean.setNationalPlanObjectivePrograms(ActivityUtil.
                            getActivityProgramsByProgramType
                            (activity.getAmpActivityId(),
                             ProgramUtil.NATIONAL_PLAN_OBJECTIVE));

                        formBean.setPrimaryPrograms(ActivityUtil.
                            getActivityProgramsByProgramType
                            (activity.getAmpActivityId(),
                             ProgramUtil.PRIMARY_PROGRAM));

                        formBean.setSecondaryPrograms(ActivityUtil.
                            getActivityProgramsByProgramType
                            (activity.getAmpActivityId(),
                             ProgramUtil.SECONDARY_PROGRAM));

                		//Refactoring
                		//Sectors are set here, copied from EditActivityForm, maybe we can unify both Forms
                		//After the merge, put this code in methods
                		formBean=setSectorsToForm(formBean, activity);
                		formBean=setTypesOfAssistanceToForm(formBean, activity);
                		formBean=setUniqueModalitiesToForm(formBean, activity);
                		setFundingStatusesToForm(formBean, activity);
                		setModesOfPaymentToForm(formBean, activity);
                		setCrossCuttingIssuesToForm(formBean, activity);
                		
                		
                                AmpCategoryValue impLocation=CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY,activity.getCategories());
                                if(impLocation!=null){
                                    formBean.setImpLocation(impLocation.getId());
                                }
						

						formBean.setProjectCategory(
						    CategoryManagerUtil.getStringValueOfAmpCategoryValue(
						        CategoryManagerUtil.getAmpCategoryValueFromList(
						    CategoryConstants.PROJECT_CATEGORY_NAME, activity.getCategories())
						    ));
						
						formBean.setGovAgreementNumber(activity.getGovAgreementNumber());
				        TreeSet relOrgsAux = new TreeSet();
				        Collection relOrgs = new ArrayList();
				        if (activity.getOrgrole() != null) {
				          Iterator orgItr = activity.getOrgrole().iterator();
				          while (orgItr.hasNext()) {
				            AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
				            AmpOrganisation auxOrgRel = orgRole.getOrganisation();
				            if ( Constants.FUNDING_AGENCY.equals(orgRole.getRole().getRoleCode()) ) {
				            	continue;
				            }
				            if(auxOrgRel!=null)
				            {
				            	RelOrganization relOrg = new RelOrganization();
				                relOrg.setOrgName(auxOrgRel.getName());
				                relOrg.setRole(orgRole.getRole().getRoleCode());
				                relOrg.setAcronym(auxOrgRel.getAcronym());
				                relOrg.setOrgCode(auxOrgRel.getOrgCode());
				                relOrg.setBudgetOrgCode(auxOrgRel.getBudgetOrgCode());
				                relOrg.setOrgGrpId(auxOrgRel.getOrgGrpId());
				                relOrg.setOrgTypeId(auxOrgRel.getOrgGrpId().getOrgType());
				                relOrg.setOrgId(auxOrgRel.getAmpOrgId());
				                relOrg.setAdditionalInformation( orgRole.getAdditionalInfo() );
//				                if (!relOrgs.contains(relOrg)) {
//				                	relOrgs.add(relOrg);
				                	relOrgsAux.add(relOrg);
//				                }
				            }
				          }
				          if ( formBean.getFinancingBreakdown() != null ) {
				        	  for ( FinancingBreakdown fb1: formBean.getFinancingBreakdown() ) {
				        		  AmpOrganisation auxOrgRel = fb1.getOrganisation();
				        		  RelOrganization relOrg = new RelOrganization();
					                relOrg.setOrgName(auxOrgRel.getName());
					                relOrg.setRole( Constants.FUNDING_AGENCY );
					                relOrg.setAcronym(auxOrgRel.getAcronym());
					                relOrg.setOrgCode(auxOrgRel.getOrgCode());
					                relOrg.setBudgetOrgCode(auxOrgRel.getBudgetOrgCode());
					                relOrg.setOrgGrpId(auxOrgRel.getOrgGrpId());
					                relOrg.setOrgTypeId(auxOrgRel.getOrgGrpId().getOrgType());
					                relOrg.setOrgId(auxOrgRel.getAmpOrgId());
//					                if (!relOrgs.contains(relOrg)) {
//					                	relOrgs.add(relOrg);
					                	relOrgsAux.add(relOrg);
//					                }
				        	  }
				          }
				        }

				        formBean.setRelOrgs(relOrgsAux);
				        
				        
				        Collection col = activity.getClosingDates();
				        List dates = new ArrayList();
				        if (col != null && col.size() > 0) {
				          Iterator itr = col.iterator();
				          while (itr.hasNext()) {
				            AmpActivityClosingDates cDate = (AmpActivityClosingDates) itr
				                .next();
				            if (cDate.getType().intValue() == Constants.REVISED.intValue()) {
				              dates.add(cDate.getClosingDate());
				            }
				          }
				        }
				       // Collections.sort(dates, DateConversion.dtComp);
				        formBean.setClosingDates(dates);

				        formBean.setStatus(
			            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
			                CategoryManagerUtil.getAmpCategoryValueFromListByKey(
			            CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories())
			            )
			            );
				        
				        formBean.setProjectImplUnit(
					            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
					                CategoryManagerUtil.getAmpCategoryValueFromListByKey(
					            CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY, activity.getCategories())
					            )
					    );

				        formBean.setFinancialInstrument(CategoryManagerUtil.getStringValueOfAmpCategoryValue(
		                CategoryManagerUtil.getAmpCategoryValueFromListByKey(
		                		CategoryConstants.FINANCIAL_INSTRUMENT_KEY, activity.getCategories())
				        ));

				        formBean.setProcurementSystem(
		                CategoryManagerUtil.getStringValueOfAmpCategoryValue(
		                    CategoryManagerUtil.getAmpCategoryValueFromList(
		                CategoryConstants.PROCUREMENT_SYSTEM_NAME, activity.getCategories())
		                )
		                );
						        
				        formBean.setReportingSystem(
		                CategoryManagerUtil.getStringValueOfAmpCategoryValue(
		                    CategoryManagerUtil.getAmpCategoryValueFromList(
		                CategoryConstants.REPORTING_SYSTEM_NAME, activity.getCategories())
		                )
		                );
						        
				        formBean.setAuditSystem(
		                CategoryManagerUtil.getStringValueOfAmpCategoryValue(
		                    CategoryManagerUtil.getAmpCategoryValueFromList(
		                CategoryConstants.AUDIT_SYSTEM_NAME, activity.getCategories())
		                )
		                );
						        
				        formBean.setInstitutions(
		                CategoryManagerUtil.getStringValueOfAmpCategoryValue(
		                    CategoryManagerUtil.getAmpCategoryValueFromList(
		                CategoryConstants.INSTITUTIONS_NAME, activity.getCategories())
		                )
		                );
						        
				        formBean.setAccessionInstrument(
		                CategoryManagerUtil.getStringValueOfAmpCategoryValue(
		                    CategoryManagerUtil.getAmpCategoryValueFromList(
		                CategoryConstants.ACCESSION_INSTRUMENT_NAME, activity.getCategories())
		                )
		                );
						        
					formBean.setAcChapter(
		            CategoryManagerUtil.getStringValueOfAmpCategoryValue(
		                CategoryManagerUtil.getAmpCategoryValueFromList(
		            CategoryConstants.ACCHAPTER_NAME, activity.getCategories())
		            )
		            );


					//contact information
			    AmpActivityContact primaryDonorContact=null;
			    AmpActivityContact primaryMofedContact=null;
			    AmpActivityContact primaryProjCoordContact=null;
			    AmpActivityContact primarySecMinContact=null;
			    AmpActivityContact primaryImplExAgencyCont=null;
			    try {
						primaryDonorContact=ContactInfoUtil.getActivityPrimaryContact(activity.getAmpActivityId(),Constants.DONOR_CONTACT);
						primaryMofedContact=ContactInfoUtil.getActivityPrimaryContact(activity.getAmpActivityId(),Constants.MOFED_CONTACT);
						primaryProjCoordContact=ContactInfoUtil.getActivityPrimaryContact(activity.getAmpActivityId(),Constants.PROJECT_COORDINATOR_CONTACT);
						primarySecMinContact=ContactInfoUtil.getActivityPrimaryContact(activity.getAmpActivityId(),Constants.SECTOR_MINISTRY_CONTACT);
						primaryImplExAgencyCont=ContactInfoUtil.getActivityPrimaryContact(activity.getAmpActivityId(),Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT);
					} catch (Exception e) {
						e.printStackTrace();
					}
					formBean.setPrimaryDonorContact(primaryDonorContact);
					formBean.setPrimaryMofedContact(primaryMofedContact);
					formBean.setPrimaryprojCoordinatorContact(primaryProjCoordContact);
					formBean.setPrimarySectorMinistryContact(primarySecMinContact);
					formBean.setPrimaryImplExecutingAgencyContact(primaryImplExAgencyCont);



					//Budget Classification
					if (activity.getBudgetsector()!=null && !activity.getBudgetsector().equals(0L)){
						AmpBudgetSector bsector = BudgetDbUtil.getBudgetSectorById(activity.getBudgetsector());
						formBean.setBudgetsector(bsector.getCode() + " - " + bsector.getSectorname());
					}
					if (activity.getBudgetorganization()!=null && !activity.getBudgetorganization().equals(0L)){
						AmpOrganisation org = DbUtil.getOrganisation(activity.getBudgetorganization());
						formBean.setBudgetorganization(org.getBudgetOrgCode() + " - " + org.getName());
					}
					if (activity.getBudgetdepartment()!=null && !activity.getBudgetdepartment().equals(0L)){
						AmpDepartments dep = BudgetDbUtil.getBudgetDepartmentById(activity.getBudgetdepartment());
						formBean.setBudgetdepartment(dep.getCode()+ " - " + dep.getName());
					}
					if (activity.getBudgetprogram()!=null && !activity.getBudgetprogram().equals(0L)){
						AmpTheme prog;
						try {
							prog = ProgramUtil.getThemeById(activity.getBudgetprogram());
							formBean.setBudgetprogram(prog.getThemeCode()+ " - " + prog.getName());
						} catch (DgException e) {
							e.printStackTrace();
						}
					}
					
					
					// queryString = "select distinct f.typeOfAssistance.value from " +
//                      AmpFunding.class.getName() + " f where f.ampActivityId=:actId";
//          
//                  qry = session.createQuery(queryString);
//                  qry.setParameter("actId", actId, Hibernate.LONG);
//          
//                  Collection temp = new ArrayList();
//                  Iterator typesItr = qry.list().iterator();
//                  while (typesItr.hasNext()) {
//                    String code = (String) typesItr.next();
//                    temp.add(code);
//                  }
//                  activity.setAssistanceType(temp);
          
		
		}
		
		return null;
	}

	private ChannelOverviewForm setUniqueModalitiesToForm(
			ChannelOverviewForm formBean, AmpActivity activity) {
		Set<AmpFunding> fundings = activity.getFunding();
		Iterator<AmpFunding> fundingsIterator = fundings.iterator();
		ArrayList<AmpCategoryValue> modalities = new ArrayList<AmpCategoryValue>();
		while(fundingsIterator.hasNext()){
			AmpFunding ampFunding = fundingsIterator.next();
			if(!modalities.contains(ampFunding.getFinancingInstrument()))
				modalities.add(ampFunding.getFinancingInstrument());
		}
		formBean.setUniqueModalities(modalities);

//      Collection modalities = new ArrayList();
//      queryString = "select fund from " + AmpFunding.class.getName() +
//          " fund " +
//          "where (fund.ampActivityId=:actId)";
//      qry = session.createQuery(queryString);
//      qry.setParameter("actId", actId, Hibernate.LONG);
//      Iterator itr = qry.list().iterator();
//      while (itr.hasNext()) {
//        AmpFunding fund = (AmpFunding) itr.next();
//        if (fund.getFinancingInstrument() != null)
//      	  modalities.add( fund.getFinancingInstrument() );
//      }
//      activity.setModalities(modalities);
//      activity.setUniqueModalities(new TreeSet(modalities));
		
		return formBean;
	}
	
	private ChannelOverviewForm setFundingStatusesToForm(
			ChannelOverviewForm formBean, AmpActivity activity) {
		Set<AmpFunding> fundings = activity.getFunding();
		Iterator<AmpFunding> fundingsIterator = fundings.iterator();
		ArrayList<AmpCategoryValue> fundingStatuses = new ArrayList<AmpCategoryValue>();
		while(fundingsIterator.hasNext()){
			AmpFunding ampFunding = fundingsIterator.next();
			if(!fundingStatuses.contains(ampFunding.getFundingStatus()))
				fundingStatuses.add(ampFunding.getFundingStatus());
		}
		formBean.setFundingStatuses(fundingStatuses);
		return formBean;
	}
	
	private ChannelOverviewForm setModesOfPaymentToForm(
			ChannelOverviewForm formBean, AmpActivity activity) {
		Set<AmpFunding> fundings = activity.getFunding();
		Iterator<AmpFunding> fundingsIterator = fundings.iterator();
		ArrayList<AmpCategoryValue> modesOfPayment = new ArrayList<AmpCategoryValue>();
		while(fundingsIterator.hasNext()){
			AmpFunding ampFunding = fundingsIterator.next();
			if(!modesOfPayment.contains(ampFunding.getModeOfPayment()))
				modesOfPayment.add(ampFunding.getModeOfPayment());
		}
		formBean.setModesOfPayment(modesOfPayment);
		return formBean;
	}

	private ChannelOverviewForm setCrossCuttingIssuesToForm(
			ChannelOverviewForm formBean, AmpActivity activity) {

		formBean.setEqualOpportunity(activity.getEqualOpportunity());
		formBean.setMinorities(activity.getMinorities());
		formBean.setEnvironment(activity.getEnvironment());
		return formBean;
	}

	private ChannelOverviewForm setTypesOfAssistanceToForm(
			ChannelOverviewForm formBean, AmpActivity activity) {
		Set<AmpFunding> fundings = activity.getFunding();
		Iterator<AmpFunding> fundingsIterator = fundings.iterator();
		ArrayList<AmpCategoryValue> typesOfAssistance = new ArrayList<AmpCategoryValue>();
		while(fundingsIterator.hasNext()){
			AmpFunding ampFunding = fundingsIterator.next();
			if(!typesOfAssistance.contains(ampFunding.getTypeOfAssistance()))
				typesOfAssistance.add(ampFunding.getTypeOfAssistance());
		}
		formBean.setTypesOfAssistance(typesOfAssistance);
		return formBean;
	}

	private void createWarnings (AmpActivity activity, boolean isTeamHead, ChannelOverviewForm formBean, boolean hasTeamLead) {
		if (activity.getDraft()!=null && activity.getDraft()) {
			formBean.addError("error.aim.draftActivity", 
					"This is a draft activity");

		}
		else //we are not checking for TL because of AMP-2705
		{
			////System.out.println("the team member is not the TEAM LEADER!!!!!!!!");
			if ( Constants.ACTIVITY_NEEDS_APPROVAL_STATUS.contains(activity.getApprovalStatus()) ) {
				if(hasTeamLead)	formBean.addError("error.aim.activityAwaitingApproval",	"The activity is awaiting approval.");
					else formBean.addError("error.aim.activityAwaitingApprovalNoWorkspaceManager",	"This activity cannot be validated because there is no Workspace Manager.");
			}
		}
	}
	  private ChannelOverviewForm setSectorsToForm(ChannelOverviewForm form, AmpActivity activity) {
			Collection sectors = activity.getSectors();

			if (sectors != null && sectors.size() > 0) {
				List<ActivitySector> activitySectors = new ArrayList<ActivitySector>();
				Iterator sectItr = sectors.iterator();
				while (sectItr.hasNext()) {
					AmpActivitySector ampActSect = (AmpActivitySector) sectItr.next();
					if (ampActSect != null) {
						AmpSector sec = ampActSect.getSectorId();
						if (sec != null) {
							AmpSector parent = null;
							AmpSector subsectorLevel1 = null;
							AmpSector subsectorLevel2 = null;
							if (sec.getParentSectorId() != null) {
								if (sec.getParentSectorId().getParentSectorId() != null) {
									subsectorLevel2 = sec;
									subsectorLevel1 = sec.getParentSectorId();
									parent = sec.getParentSectorId().getParentSectorId();
								} else {
									subsectorLevel1 = sec;
									parent = sec.getParentSectorId();
								}
							} else {
								parent = sec;
							}
							ActivitySector actSect = new ActivitySector();
	                                                actSect.setConfigId(ampActSect.getClassificationConfig().getId());
							if (parent != null) {
								actSect.setId(parent.getAmpSectorId());
								String view = FeaturesUtil.getGlobalSettingValue("Allow Multiple Sectors");
								if (view != null)
									if (view.equalsIgnoreCase("On")) {
										actSect.setCount(1);
									} else {
										actSect.setCount(2);
									}

								actSect.setSectorId(parent.getAmpSectorId());
								actSect.setSectorName(parent.getName());
								if (subsectorLevel1 != null) {
									actSect.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
									actSect.setSubsectorLevel1Name(subsectorLevel1.getName());
									if (subsectorLevel2 != null) {
										actSect.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
										actSect.setSubsectorLevel2Name(subsectorLevel2.getName());
									}
								}
								actSect.setSectorPercentage(ampActSect.getSectorPercentage());
	                                                        actSect.setSectorScheme(parent.getAmpSecSchemeId().getSecSchemeName());
	                                                        
							}
	                                               
							activitySectors.add(actSect);
						}
					}
				}
				Collections.sort(activitySectors);
				form.setActivitySectors(activitySectors);
			}
			return form;
		}

}
