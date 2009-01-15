package org.digijava.module.dataExchange.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpNotes;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.jaxb.DateType;
import org.digijava.module.dataExchange.jaxb.FreeTextType;
import org.digijava.module.dataExchange.jaxb.FundingDetailType;
import org.digijava.module.dataExchange.jaxb.FundingType;
import org.digijava.module.dataExchange.jaxb.ObjectFactory;
import org.digijava.module.dataExchange.type.AmpColumnEntry;



public class ExportBuilder {

	private AmpActivity ampActivity = null;

	private ObjectFactory objectFactory = new ObjectFactory();

	private String siteId =  null; //RequestUtils.getSite(request).getSiteId()

	public ExportBuilder(AmpActivity ampActivity, String siteId){
		this.ampActivity = ampActivity;
		this.siteId = siteId;
	}

	public ExportBuilder(Long ampActivityId, String siteId) throws AmpExportException{
		try{
			this.ampActivity = ActivityUtil.loadActivity(ampActivityId);
			this.siteId = siteId;
		} catch (DgException ex){
			throw new AmpExportException(ex, AmpExportException.ACTIVITY_LOAD);
		}	
	}

	public ExportBuilder(long ampActivityId, String siteId) throws AmpExportException{
		this(new Long(ampActivityId), siteId);
	}

	public ActivityType getActivityType(AmpColumnEntry ampColumnEntry) throws AmpExportException{
		ActivityType retValue = objectFactory.createActivityType();
		retValue.setDbKey(ampActivity.getAmpActivityId().toString());
		retValue.setDate(ExportHelper.getGregorianCalendar(ampActivity.getCreatedDate()));
		
		for (AmpColumnEntry elem : ampColumnEntry.getElements()) {
			if (elem.isMandatory() || elem.isSelect()){
				buildActivityType(retValue, elem);
			}
		}

		return retValue;
	}


	private void buildActivityType(ActivityType parent, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		String path = ampColumnEntry.getPath();

		if (path.equalsIgnoreCase("activity.id")){
			if (ampActivity.getInternalIds() != null){
				for (Iterator iterator = ampActivity.getInternalIds().iterator(); iterator.hasNext();) {
					AmpActivityInternalId ids = (AmpActivityInternalId) iterator.next();
					parent.getId().add(buildActivityTypeId(ids));
				}
			}			
		} else if (path.equalsIgnoreCase("activity.title")){
			parent.getTitle().add(buildFreeText(null, ampActivity.getName()));
		} else if (path.equalsIgnoreCase("activity.objective")){
			if (ampActivity.getObjective() != null){
				for (Message msg : ExportHelper.getTranslations(ampActivity.getObjective(),ampActivity.getObjective(), siteId)) {
					parent.getObjective().add(buildFreeText(msg));
				}
			}
		} else if (path.equalsIgnoreCase("activity.description")){
			if (ampActivity.getDescription() != null){
				for (Message msg : ExportHelper.getTranslations(ampActivity.getDescription(),ampActivity.getDescription(), siteId)) {
					parent.getDescription().add(buildFreeText(msg));
				}
			}
		} else if (path.equalsIgnoreCase("activity.implementationLevels")){
			parent.setImplementationLevels(buildImplementationLevel());
		} else if (path.equalsIgnoreCase("activity.locations")){
			if (ampActivity.getLocations() != null){
				for (Iterator iterator = ampActivity.getLocations().iterator(); iterator.hasNext();) {
					AmpActivityLocation ampLocation = (AmpActivityLocation) iterator.next();
					parent.getLocations().add(buildLocation(ampLocation.getLocation(), ampColumnEntry.getElementByName("funding")));
				}
			}
		} else if (path.equalsIgnoreCase("activity.proposedApprovalDate")){
			parent.setProposedApprovalDate(buildDate(ampActivity.getProposedApprovalDate()));
		} else if (path.equalsIgnoreCase("activity.actualApprovalDate")){
			parent.setActualApprovalDate(buildDate(ampActivity.getActivityApprovalDate()));
		} else if (path.equalsIgnoreCase("activity.proposedStartDate")){
			parent.setProposedStartDate(buildDate(ampActivity.getProposedStartDate()));
		} else if (path.equalsIgnoreCase("activity.actualStartDate")){
			parent.setActualStartDate(buildDate(ampActivity.getActualStartDate()));
		} else if (path.equalsIgnoreCase("activity.modifiedClosingDate")){
//TODO			parent.setModifiedClosingDate(buildDate(ampActivity.getActivityCloseDate()));
		} else if (path.equalsIgnoreCase("activity.closingDate")){
			parent.setModifiedClosingDate(buildDate(ampActivity.getActivityCloseDate()));
		} else if (path.equalsIgnoreCase("activity.status")){
			parent.setStatus(buildCodeValue(ampActivity.getApprovalStatus()));
		} else if (path.equalsIgnoreCase("activity.statusReason")){
			parent.setStatusReason(buildFreeText(ampActivity.getStatusReason()));
		} else if (path.equalsIgnoreCase("activity.sectors")){
			if (ampActivity.getSectors() != null){
				for (Iterator iterator = ampActivity.getSectors().iterator(); iterator.hasNext();) {
					AmpActivitySector ampSector = (AmpActivitySector) iterator.next();
					parent.getSectors().add(buildCodeValue(ampSector.getSectorId().getSectorCode(),ampSector.getSectorId().getName()));
				}
			}
		} else if (path.equalsIgnoreCase("activity.programs")){
			if (ampActivity.getActivityPrograms()  != null){
				for (Iterator iterator = ampActivity.getActivityPrograms().iterator(); iterator.hasNext();) {
					AmpActivityProgram ampProgram = (AmpActivityProgram) iterator.next();
					parent.getPrograms().add(buildCodeValue(ampProgram.getProgram().getThemeCode(),ampProgram.getProgram().getName()));
				}
			} else {
				throw new AmpExportException("Prorgasm is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
		} else if (path.equalsIgnoreCase("activity.notes")){
			if (ampActivity.getNotes()  != null){
				for (Iterator iterator = ampActivity.getNotes().iterator(); iterator.hasNext();) {
					AmpNotes ampNotes = (AmpNotes) iterator.next();
					parent.getNotes().add(buildFreeText(ampNotes.getLanguage(),ampNotes.getDescription()));
				}
			}
		} else if (path.equalsIgnoreCase("activity.funding")){
			for (Iterator iterator = ampActivity.getFunding().iterator(); iterator.hasNext();) {
				AmpFunding ampFunding = (AmpFunding) iterator.next();
				parent.getFunding().add(buildFunding(ampFunding, ampColumnEntry));			
			}
		} else if (path.equalsIgnoreCase("activity.keywords")){
			// TODO can not found 
		} else if (path.equalsIgnoreCase("activity.components")){
			//TODO 
			/*
			for (Components<FundingDetail> component : ActivityUtil.getAllComponents(ampActivity.getAmpActivityId().longValue())) {
				parent.getComponents().add(buildComponent(component, ampColumnEntry));
			}
			*/
		} else if (path.equalsIgnoreCase("activity.issues")){
			if (ampActivity.getIssues()  != null){
				for (Iterator iterator = ampActivity.getIssues().iterator(); iterator.hasNext();) {
					AmpIssues issue = (AmpIssues) iterator.next();
					parent.getIssues().add(buildIssue(issue, ampColumnEntry));
				}
			}	
			
		} else if (path.equalsIgnoreCase("activity.documents")){
			if (ampActivity.getDocuments()  != null){
				for (Iterator iterator = ampActivity.getDocuments().iterator(); iterator.hasNext();) {
					Object issue = (Object) iterator.next();
//					parent.getIssues().add(buildIssue(issue, ampColumnEntry));
				}
			}	

			
		} else if (path.equalsIgnoreCase("activity.relatedLinks")){
		} else if (path.equalsIgnoreCase("activity.relatedOrgs")){
		} else if (path.equalsIgnoreCase("activity.donorContacts")){
		} else if (path.equalsIgnoreCase("activity.govContacts")){
		} else if (path.equalsIgnoreCase("activity.additional")){
			// TODO not implemented need more details
		}
	}

	private ActivityType.Id buildActivityTypeId(AmpActivityInternalId ids) throws AmpExportException{
		/* TODO  need correct implementation*/
		

		ActivityType.Id retValue = objectFactory.createActivityTypeId();
		retValue.setUniqID(ids.getInternalId());

		retValue.setAssigningOrg(buildCodeValue(ids.getOrganisation().getOrgCode(), ids.getOrganisation().getName()));

		return retValue;
	}

	private CodeValueType buildImplementationLevel() throws AmpExportException{

		AmpCategoryValue ampCategoryValue = 
			CategoryManagerUtil.getAmpCategoryValueFromListByKey(
					CategoryConstants.IMPLEMENTATION_LEVEL_KEY, 
					ampActivity.getCategories());		

		if (ampCategoryValue == null){
			throw new AmpExportException("IMPLEMENTATION_LEVEL is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
		}
		return buildCodeValue(ampCategoryValue);
	}


	private ActivityType.Locations buildLocation(AmpLocation location, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		/* TODO need correct implementation */
		ActivityType.Locations retValue = objectFactory.createActivityTypeLocations();
		retValue.setLang(location.getLanguage());
		//??		retValue.setLocationType(value);
		retValue.setGis(location.getGisCoordinates());
		retValue.setIso3(location.getIso3Code());
		retValue.setCountryName(location.getCountry());
		
		if (location.getAmpRegion() != null){
			retValue.setRegionCode(location.getAmpRegion().getRegionCode());
			retValue.setRegionName(location.getAmpRegion().getName());

			if (ampColumnEntry.isMandatory() || ampColumnEntry.isSelect()){
				if (ampActivity.getRegionalFundings() != null){
					Collection regFund = ampActivity.getRegionalFundings();
					for (Iterator iterator = regFund.iterator(); iterator.hasNext();) {
						AmpRegionalFunding regFunding = (AmpRegionalFunding) iterator.next();
						if (location.getAmpRegion().equals(regFunding.getRegion())){
							retValue.getFunding().add(buildRegionalFunding(regFunding, ampColumnEntry));
						}
					}
				}
			}
		}

		return retValue;
	}

	private FundingType buildRegionalFunding(AmpRegionalFunding ampfunding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		FundingType retValue = objectFactory.createFundingType();
		int subType = 0 ; // projections = 1; commitments = 2; disbursements = 3; expenditures = 4;
		
		retValue.setCode("TODO");
		retValue.setType("TODO");

		for (AmpColumnEntry elem : ampColumnEntry.getElements()) {
			if (elem.isMandatory() || elem.isSelect()){
				String path = elem.getPath();

				if (path.equalsIgnoreCase("activity.locations.funding.fundingOrg")){
					if (ampfunding.getReportingOrganization() != null){
						retValue.setFundingOrg(buildCodeValue(ampfunding.getReportingOrganization().getOrgCode(), ampfunding.getReportingOrganization().getName()));
					}
				} else if (path.equalsIgnoreCase("activity.locations.funding.assistanceType")){
					retValue.setAssistanceType(buildCodeValue(null,"TODO"));
				} else if (path.equalsIgnoreCase("activity.locations.funding.financingInstrument")){
					retValue.setFinancingInstrument(buildCodeValue("TODO"));
				} else if (path.equalsIgnoreCase("activity.locations.funding.conditions")){
					retValue.setConditions(buildFreeText(null, "TODO"));
				} else if (path.equalsIgnoreCase("activity.locations.funding.signatureDate")){
					retValue.setSignatureDate(buildDate(new Date())); //TODO
				} else if (path.equalsIgnoreCase("activity.locations.funding.projections")){
					// not present
					/*
					if (ampfunding.getTransactionType().intValue() == 0){
						subType = 1;
					}
					*/
				} else if (path.equalsIgnoreCase("activity.locations.funding.commitments")){
					if (ampfunding.getTransactionType().intValue() == 0){
						subType = 2;
					}
				} else if (path.equalsIgnoreCase("activity.locations.funding.disbursements")){
					if (ampfunding.getTransactionType().intValue() == 1){
						subType = 3;
					}
				} else if (path.equalsIgnoreCase("activity.locations.funding.expenditures")){
					if (ampfunding.getTransactionType().intValue() == 2){
						subType = 4;
					}
				}
			}
		}	
		String fDetailType = (ampfunding.getAdjustmentType() == 1) ? "Actual": "Planned";
		
		FundingDetailType fDetail = buildFundingDetail(fDetailType, ampfunding.getTransactionDate(), 
				ampfunding.getTransactionAmount().longValue(), ampfunding.getCurrency().getCurrencyCode());
		switch (subType) {
			case 2:
				retValue.getCommitments().add(fDetail);
				break;
			case 3:
				retValue.getDisbursements().add(fDetail);
				break;
			case 4:
				retValue.getExpenditures().add(fDetail);
				break;
		}
		return retValue;
	}	


	private FundingType buildFunding(AmpFunding funding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		FundingType retValue = objectFactory.createFundingType();
		retValue.setCode("TODO");
		retValue.setType("TODO");

		for (AmpColumnEntry elem : ampColumnEntry.getElements()) {
			if (elem.isMandatory() || elem.isSelect()){
				buildFundingSubElements(funding, retValue, elem);
			}
		}				
		return retValue;
	}

	private void buildFundingSubElements(AmpFunding ampfunding, FundingType funding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		String path = ampColumnEntry.getPath();

		if (path.equalsIgnoreCase("activity.funding.fundingOrg")){
			funding.setFundingOrg(buildCodeValue(ampfunding.getAmpDonorOrgId().getOrgCode(), ampfunding.getAmpDonorOrgId().getName()));
		} else if (path.equalsIgnoreCase("activity.funding.assistanceType")){
			funding.setAssistanceType(buildCodeValue(ampfunding.getTypeOfAssistance()));
		} else if (path.equalsIgnoreCase("activity.funding.financingInstrument")){
			funding.setFinancingInstrument(buildCodeValue(ampfunding.getFinancingInstrument()));
		} else if (path.equalsIgnoreCase("activity.funding.conditions")){
			funding.setConditions(buildFreeText(ampfunding.getLanguage(), ampfunding.getConditions()));
		} else if (path.equalsIgnoreCase("activity.funding.signatureDate")){
			funding.setSignatureDate(buildDate(ampfunding.getSignatureDate()));
		} else if (path.equalsIgnoreCase("activity.funding.projections")){
			for (AmpFundingMTEFProjection ampProj : ampfunding.getMtefProjections()) {
				FundingType.Projections proj = objectFactory.createFundingTypeProjections();
				proj.setType(ampProj.getProjected().getValue());
				proj.setAmount(ampProj.getAmount().longValue());
				proj.setCurrency(ampProj.getAmpCurrency().getCurrencyCode());

				Calendar cal = Calendar.getInstance();
				cal.setTime(ampProj.getProjectionDate());
				proj.setStartYear(cal.get(Calendar.YEAR));
//				proj.setEndYear(null); // do not have end date. 
				funding.getProjections().add(proj);
			}

		} else if (path.equalsIgnoreCase("activity.funding.commitments")){
			for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
				FundingDetail fDetail = (FundingDetail) iterator.next();
				if (fDetail.getTransactionType() == Constants.COMMITMENT){
					funding.getCommitments().add(buildFundingDetail(fDetail));
				}
			}
		} else if (path.equalsIgnoreCase("activity.funding.disbursements")){
			for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
				FundingDetail fDetail = (FundingDetail) iterator.next();
				if (fDetail.getTransactionType() == Constants.DISBURSEMENT){
					funding.getCommitments().add(buildFundingDetail(fDetail));
				}
			}
		} else if (path.equalsIgnoreCase("activity.funding.expenditures")){
			for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
				FundingDetail fDetail = (FundingDetail) iterator.next();
				if (fDetail.getTransactionType() == Constants.EXPENDITURE){
					funding.getCommitments().add(buildFundingDetail(fDetail));
				}
			}
		}
	}

	private ActivityType.Components buildComponent(Components<FundingDetail> component, AmpColumnEntry ampColumnEntry) throws AmpExportException{	
		ActivityType.Components retValue = objectFactory.createActivityTypeComponents();
		
		retValue.setComponentTitle(buildFreeText(component.getTitle()));
		retValue.setComponentDescription(buildFreeText(component.getDescription()));
		
		
		
		for (PhysicalProgress pProgress : component.getPhyProgress()) {
			ActivityType.Components.PhysicalProgress physicalProgress = objectFactory.createActivityTypeComponentsPhysicalProgress();
			physicalProgress.setTitle(buildFreeText(pProgress.getTitle()));
			physicalProgress.setDescription(buildFreeText(pProgress.getDescription()));
			physicalProgress.setReportingDate(buildDate(getDate(pProgress.getReportingDate())));
			retValue.getPhysicalProgress().add(physicalProgress);
		}
		
		return retValue;
	}

	
	private ActivityType.Issues buildIssue(AmpIssues ampIssue, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		ActivityType.Issues retValue = objectFactory.createActivityTypeIssues();
		
		
		
		return retValue;
	}
	
/*	
	private FundingType buildFunding(FundingDetail funding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		FundingType retValue = objectFactory.createFundingType();
		retValue.setCode("TODO"); 
		retValue.setType("TODO");
		

		for (AmpColumnEntry elem : ampColumnEntry.getElements()) {
			if (elem.isMandatory() || elem.isSelect()){
				buildFundingSubElements(funding, retValue, elem);
			}
		}				
		return retValue;
	}	
	
	private void buildFundingSubElements(FundingDetail ampfunding, FundingType funding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		String path = ampColumnEntry.getPath();

		if (path.equalsIgnoreCase("activity.components.funding.fundingOrg")){
			funding.setFundingOrg(buildCodeValue(ampfunding.getAmpDonorOrgId().getOrgCode(), ampfunding.getAmpDonorOrgId().getName()));
		} else if (path.equalsIgnoreCase("activity.components.funding.assistanceType")){
			funding.setAssistanceType(buildCodeValue(ampfunding.getTypeOfAssistance()));
		} else if (path.equalsIgnoreCase("activity.components.funding.financingInstrument")){
			funding.setFinancingInstrument(buildCodeValue(ampfunding.getFinancingInstrument()));
		} else if (path.equalsIgnoreCase("activity.components.funding.conditions")){
			funding.setConditions(buildFreeText(ampfunding.getLanguage(), ampfunding.getConditions()));
		} else if (path.equalsIgnoreCase("activity.components.funding.signatureDate")){
			funding.setSignatureDate(buildDate(ampfunding.getSignatureDate()));
		} else if (path.equalsIgnoreCase("activity.components.funding.projections")){
			for (AmpFundingMTEFProjection ampProj : ampfunding.getMtefProjections()) {
				FundingType.Projections proj = objectFactory.createFundingTypeProjections();
				proj.setType(ampProj.getProjected().getValue());
				proj.setAmount(ampProj.getAmount().longValue());
				proj.setCurrency(ampProj.getAmpCurrency().getCurrencyCode());
				proj.setStartYear(2222); //TODO proj.setStartYear(ampProj.getProjectionDate().getYear());
				//TODO 				proj.setEndYear(ampProj.getProjectionDate().getYear());
				funding.getProjections().add(proj);
			}

		} else if (path.equalsIgnoreCase("activity.components.funding.commitments")){
			for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
				FundingDetail fDetail = (FundingDetail) iterator.next();
				if (fDetail.getTransactionType() == Constants.COMMITMENT){
					funding.getCommitments().add(buildFundingDetail(fDetail));
				}
			}
		} else if (path.equalsIgnoreCase("activity.components.funding.disbursements")){
			for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
				FundingDetail fDetail = (FundingDetail) iterator.next();
				if (fDetail.getTransactionType() == Constants.DISBURSEMENT){
					funding.getCommitments().add(buildFundingDetail(fDetail));
				}
			}
		} else if (path.equalsIgnoreCase("activity.components.funding.expenditures")){
			for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
				FundingDetail fDetail = (FundingDetail) iterator.next();
				if (fDetail.getTransactionType() == Constants.EXPENDITURE){
					funding.getCommitments().add(buildFundingDetail(fDetail));
				}
			}
		}
	}

*/	
	private CodeValueType buildCodeValue(AmpCategoryValue ampCategoryValue) throws AmpExportException{
		/* TODO not sure that code is same */
		if (ampCategoryValue == null){
			return null;
		}
		return buildCodeValue(null, ampCategoryValue.getValue());
	}

	private CodeValueType buildCodeValue(String value) throws AmpExportException{
		return buildCodeValue(null, value);
	}
	
	private CodeValueType buildCodeValue(String code, String value) throws AmpExportException{
		CodeValueType retValue = objectFactory.createCodeValueType();
		if (code != null){
			retValue.setCode(code);
		}
		retValue.setValue(value);
		return retValue;
	}		

	private FreeTextType buildFreeText(String name) throws AmpExportException{
		return buildFreeText(null, name);
	}

	private FreeTextType buildFreeText(String lang, String name) throws AmpExportException{
		FreeTextType retValue = objectFactory.createFreeTextType();
		if (lang != null){
			retValue.setLang(lang);
		}
		retValue.setValue(name);
		return retValue;
	}	

	private FreeTextType buildFreeText(Message message) throws AmpExportException{
		if (message == null){
			return null;
		}
		return buildFreeText(message.getLocale(), message.getMessage());
	}

	private DateType buildDate(Date date) throws AmpExportException{
		if (date == null){
			date = new Date();
		}
		DateType retValue = objectFactory.createDateType();
		retValue.setDate(ExportHelper.getGregorianCalendar(date));
		return retValue;
	}	

	private FundingDetailType buildFundingDetail(FundingDetail fDetail) throws AmpExportException{
		long amount = 0;
		
		try {
			amount = Long.parseLong(fDetail.getTransactionAmount());
		} catch (Exception e) {
			throw new AmpExportException(e, AmpExportException.ACTIVITY_FORMAT);
		}

		return buildFundingDetail(fDetail.getAdjustmentTypeName(), getDate(fDetail.getTransactionDate()), amount, fDetail.getCurrencyCode());
	}

	private Date getDate(String stringDate) throws AmpExportException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date retValue = null;
		try{		
			retValue = format.parse(stringDate);
		} catch (Exception e) {
			throw new AmpExportException(e, AmpExportException.ACTIVITY_FORMAT);
		}

		return retValue;
	}
	
	private FundingDetailType buildFundingDetail(String type, Date date, long amount, String currency) throws AmpExportException{
		FundingDetailType retValue = objectFactory.createFundingDetailType();
		retValue.setType(type);
		retValue.setDate(ExportHelper.getGregorianCalendar(date));
		retValue.setAmount(amount);
		retValue.setCurrency(currency);
		
		return retValue;
	}

	

}
