package org.digijava.module.dataExchange.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.newreports.AmountsUnits;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.jaxb.AdditionalFieldType;
import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.jaxb.ComponentFundingType;
import org.digijava.module.dataExchange.jaxb.ContactType;
import org.digijava.module.dataExchange.jaxb.DateType;
import org.digijava.module.dataExchange.jaxb.FreeTextType;
import org.digijava.module.dataExchange.jaxb.FundingDetailType;
import org.digijava.module.dataExchange.jaxb.FundingType;
import org.digijava.module.dataExchange.jaxb.LocationFundingType;
import org.digijava.module.dataExchange.jaxb.ObjectFactory;
import org.digijava.module.dataExchange.jaxb.PercentageCodeValueType;
import org.digijava.module.dataExchange.type.AmpColumnEntry;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;


/**
 * @deprecated Use {@link ExportIatiBuilderVX} instead
 * This Builder was meant for old IATI schema export 
 * (probably not even 1.01)
 */
public class ExportBuilder {

	private AmpActivityVersion ampActivity = null;

	private ObjectFactory objectFactory = new ObjectFactory();

	private Site site =  null; //RequestUtils.getSite(request).getSiteId()

	private String[] exportLog = null;
	
	public ExportBuilder(AmpActivityVersion ampActivity, Site site){
		this.ampActivity = ampActivity;
		this.site = site;
	}

	public ExportBuilder(Long ampActivityId, Site site) throws AmpExportException{
		try{
			this.ampActivity = ActivityUtil.loadActivity(ampActivityId);
			this.site = site;
		} catch (DgException ex){
			throw new AmpExportException(ex, AmpExportException.ACTIVITY_LOAD);
		}	
	}

	public ExportBuilder(long ampActivityId, Site site) throws AmpExportException{
		this(new Long(ampActivityId), site);
	}

	public ActivityType getActivityType(AmpColumnEntry ampColumnEntry) throws AmpExportException{
		ActivityType retValue = objectFactory.createActivityType();
		retValue.setDbKey(ampActivity.getAmpId());
		retValue.setDate(ExportHelper.getGregorianCalendar(ampActivity.getCreatedDate()));
		
		for (AmpColumnEntry elem : ampColumnEntry.getElements()) {
			if (elem.canExport()){
                try {
                    buildActivityType(retValue, elem);
                } catch (EditorException ex) {
                    throw new  AmpExportException(ex.getMessage());

                }
			}
		}

		return retValue;
	}


	private void buildActivityType(ActivityType parent, AmpColumnEntry ampColumnEntry) throws AmpExportException, EditorException{
		String path = ampColumnEntry.getPath();

		if (path.equalsIgnoreCase("activity.id")){
			if (ampActivity.getInternalIds() != null && ampActivity.getInternalIds().size() > 0){
				for (Iterator iterator = ampActivity.getInternalIds().iterator(); iterator.hasNext();) {
					AmpActivityInternalId ids = (AmpActivityInternalId) iterator.next();
					parent.getId().add(buildActivityTypeId(ids));
				}
//			} else {
//				throw new AmpExportException("Id is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
		} else if (path.equalsIgnoreCase("activity.title")){
			if (ampActivity.getName() != null){
				parent.getTitle().add(buildFreeText(null, ampActivity.getName()));
			} else {
				String msg = "Name is empty";
				this.addToLog(ampActivity, msg);
				throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
				
		} else if (path.equalsIgnoreCase("activity.objective")){
			if (ampActivity.getObjective() != null){
                    for (Editor editor : org.digijava.module.editor.util.DbUtil.getEditorList(ampActivity.getObjective(), site)) {
                        if(editor.getBody()!=null&&!editor.getBody().trim().equals("")){
                        parent.getObjective().add(buildFreeText(editor.getLanguage(), editor.getBody()));
                        }
                    }
              
			}
		} else if (path.equalsIgnoreCase("activity.description")){
			if (ampActivity.getDescription() != null){
				 for (Editor editor : org.digijava.module.editor.util.DbUtil.getEditorList(ampActivity.getDescription(), site)) {
                        if(editor.getBody()!=null&&!editor.getBody().trim().equals("")){
                        parent.getDescription().add(buildFreeText(editor.getLanguage(), editor.getBody()));
                        }
                    }
			}
		} else if (path.equalsIgnoreCase("activity.implementationLevels")){
			parent.setImplementationLevels(buildImplementationLevel());
		} else if (path.equalsIgnoreCase("activity.location")){
			if (ampActivity.getLocations() != null){
				for (Iterator iterator = ampActivity.getLocations().iterator(); iterator.hasNext();) {
					AmpActivityLocation ampLocation = (AmpActivityLocation) iterator.next();
					parent.getLocation().add(buildLocation(ampLocation.getLocation(), ampColumnEntry.getElementByName("locationFunding")));
				}
			}
		} else if (path.equalsIgnoreCase("activity.proposedApprovalDate")){
			parent.setProposedApprovalDate(buildDate(ampActivity.getProposedApprovalDate(), ampColumnEntry.isMandatory() ));
		} else if (path.equalsIgnoreCase("activity.proposedStartDate")){
			parent.setProposedStartDate(buildDate(ampActivity.getProposedStartDate(), ampColumnEntry.isMandatory()));
		} else if (path.equalsIgnoreCase("activity.actualStartDate")){
			parent.setActualStartDate(buildDate(ampActivity.getActualStartDate(), ampColumnEntry.isMandatory()));
		} else if (path.equalsIgnoreCase("activity.modifiedClosingDate")){
			parent.setModifiedClosingDate(buildDate(ampActivity.getActualCompletionDate(), ampColumnEntry.isMandatory()));
		}  else if (path.equalsIgnoreCase("activity.status")){
			//if (ampActivity.getApprovalStatus() != null){
			//AMP-9364
			CodeValueType buildStatus = buildStatus();
			if (buildStatus != null){
				parent.setStatus(buildStatus);
			} else {
				String msg = "Status is empty";
				this.addToLog(ampActivity, msg);
				throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
		} else if (path.equalsIgnoreCase("activity.statusReason")){
			parent.setStatusReason(buildFreeText(ampActivity.getStatusReason()));
		} else if (path.equalsIgnoreCase("activity.sectors")){
			if (ampActivity.getSectors() != null && ampActivity.getSectors().size() > 0){
				for (Iterator iterator = ampActivity.getSectors().iterator(); iterator.hasNext();) {
					AmpActivitySector ampSector = (AmpActivitySector) iterator.next();
					if (ampSector.getSectorPercentage() != null){
//						parent.getSectors().add(buildPercentageCodeValue(ampSector.getSectorId().getSectorCode(),
//								ampSector.getSectorId().getName(),
//								ampSector.getSectorPercentage().floatValue()));
						//AMP-9212
						parent.getSectors().add(buildPercentageCodeValue(ampSector.getSectorId().getSectorCodeOfficial(),
								ampSector.getSectorId().getName(),
								ampSector.getSectorPercentage().floatValue()));
					} else {
						String msg = "Sector Precent is empty";
						this.addToLog(ampActivity, msg);
						throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
					}
				}
			} else {
				String msg = "Sector is empty";
				this.addToLog(ampActivity, msg);
				throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
		} else if (path.equalsIgnoreCase("activity.funding")){
			for (Iterator iterator = ampActivity.getFunding().iterator(); iterator.hasNext();) {
				AmpFunding ampFunding = (AmpFunding) iterator.next();
				parent.getFunding().add(buildFunding(ampFunding, ampColumnEntry));			
			}
		} else if (path.equalsIgnoreCase("activity.keywords")){
			// at this moment do not have in amp
		} else if (path.equalsIgnoreCase("activity.components")){
			for (Components<FundingDetail> component : ActivityUtil.getAllComponents(ampActivity.getAmpActivityId().longValue())) {
				parent.getComponent().add(buildComponent(component, ampColumnEntry));
			}
		} else if (path.equalsIgnoreCase("activity.issues")){
			if (ampActivity.getIssues()  != null){
				for (Iterator iterator = ampActivity.getIssues().iterator(); iterator.hasNext();) {
					AmpIssues issue = (AmpIssues) iterator.next();
					parent.getIssues().add(buildIssue(issue, ampColumnEntry));
				}
			}	
		} else if (path.equalsIgnoreCase("activity.documents")){
//			if (ampActivity.getDocuments()  != null){
//				for (Iterator iterator = ampActivity.getDocuments().iterator(); iterator.hasNext();) {
//					CMSContentItem item = (CMSContentItem) iterator.next();
//					if (item.getIsFile()){
//						ActivityType.Documents doc = objectFactory.createActivityTypeDocuments();
//						doc.setDescription(item.getDescription());
//						doc.setTitle(item.getTitle());
//						if (item.getLanguage() != null){
//							doc.setLang(item.getLanguage().getCode());
//						}
//						parent.getDocuments().add(doc);
//					}
//				}
//			}	
		} else if (path.equalsIgnoreCase("activity.relatedLinks")){
//			if (ampActivity.getDocuments()  != null){
//				for (Iterator iterator = ampActivity.getDocuments().iterator(); iterator.hasNext();) {
//					CMSContentItem item = (CMSContentItem) iterator.next();
//					if (!item.getIsFile()){
//						ActivityType.RelatedLinks link = objectFactory.createActivityTypeRelatedLinks();
//						link.setDescription(item.getDescription());
//						link.setLabel(item.getTitle());
//						if (item.getLanguage() != null){
//							link.setLang(item.getLanguage().getCode());
//						}
//						parent.getRelatedLinks().add(link);
//					}
//				}
//			}	
		} else if (path.equalsIgnoreCase("activity.relatedOrgs")){
			if (ampActivity.getOrgrole() != null){
				for (AmpOrgRole ampOrgRole : ampActivity.getOrgrole()) {
					ActivityType.RelatedOrgs org = objectFactory.createActivityTypeRelatedOrgs();
					
					org.setValue(ampOrgRole.getOrganisation().getName());
					org.setCode(ampOrgRole.getOrganisation().getOrgCode());

					if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.REPORTING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_REPORTING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.FUNDING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_FUNDING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.IMPLEMENTING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_IMPLEMENTING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.BENEFICIARY_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_BENEFICIARY_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.CONTRACTING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_CONTRACTING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.REGIONAL_GROUP)){
						org.setType(DataExchangeConstants.ORG_ROLE_REGIONAL_GROUP);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.SECTOR_GROUP)){
						org.setType(DataExchangeConstants.ORG_ROLE_SECTOR_GROUP);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.EXECUTING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_EXECUTING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.RESPONSIBLE_ORGANISATION)){
						org.setType(DataExchangeConstants.ORG_ROLE_RESPONSIBLE_ORGANIZATION);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.CONTRACTOR)){
						org.setType(DataExchangeConstants.ORG_ROLE_CONTRACTOR);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.RELATED_INSTITUTIONS)){
						org.setType(DataExchangeConstants.ORG_ROLE_RELEATED_INSTITUTIONS);
					} else {
						String msg = "Releated Organization type is unknown";
						this.addToLog(ampActivity, msg);
						throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
					}
					

					parent.getRelatedOrgs().add(org);
				}
			}
			
		} else if (path.equalsIgnoreCase("activity.donorContacts")){
			Long actId = ampActivity.getAmpActivityId();
			try {
				List<AmpActivityContact> actConts = ContactInfoUtil.getActivityContactsForType(actId,Constants.DONOR_CONTACT);
				if(actConts!=null){
					for (AmpActivityContact ampActivityContact : actConts) {
						ContactType cont = buildContactType(ampActivityContact);
						if (cont != null){
							parent.getDonorContacts().add(cont);
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (path.equalsIgnoreCase("activity.govContacts")){			
				Long actId = ampActivity.getAmpActivityId();
				try {
					List<AmpActivityContact> actConts = ContactInfoUtil.getActivityContactsForType(actId,Constants.MOFED_CONTACT);
					if(actConts!=null){
						for (AmpActivityContact ampActivityContact : actConts) {
							ContactType cont = buildContactType(ampActivityContact);
							if (cont != null){
								parent.getGovContacts().add(cont);
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			
		}else if (path.equalsIgnoreCase("activity.sectMinContacts")){			
			Long actId = ampActivity.getAmpActivityId();
			try {
				List<AmpActivityContact> actConts = ContactInfoUtil.getActivityContactsForType(actId,Constants.SECTOR_MINISTRY_CONTACT);
				if(actConts!=null){
					for (AmpActivityContact ampActivityContact : actConts) {
						ContactType cont = buildContactType(ampActivityContact);
						if (cont != null){
							parent.getSectMinContacts().add(cont);
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		
	}else if (path.equalsIgnoreCase("activity.projCoordinatorContacts")){			
		Long actId = ampActivity.getAmpActivityId();
		try {
			List<AmpActivityContact> actConts = ContactInfoUtil.getActivityContactsForType(actId,Constants.PROJECT_COORDINATOR_CONTACT);
			if(actConts!=null){
				for (AmpActivityContact ampActivityContact : actConts) {
					ContactType cont = buildContactType(ampActivityContact);
					if (cont != null){
						parent.getProjCoordinatorContacts().add(cont);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	
	}else if (path.equalsIgnoreCase("activity.impExecAgencyContacts")){			
		Long actId = ampActivity.getAmpActivityId();
		try {
			List<AmpActivityContact> actConts = ContactInfoUtil.getActivityContactsForType(actId,Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT);
			if(actConts!=null){
				for (AmpActivityContact ampActivityContact : actConts) {
					ContactType cont = buildContactType(ampActivityContact);
					if (cont != null){
						parent.getImpExecAgencyContacts().add(cont);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	
	} else if (path.equalsIgnoreCase("activity.additional")){
			// TODO not implemented need more details
			
			//AMP-9220 - ptip number for Senegal
			AdditionalFieldType ptip = buildAdditionalFieldType("en", ampActivity.getCrisNumber(), "String");
			parent.getAdditional().add(ptip);
		}
		
		else if (path.equalsIgnoreCase("activity.physicalProgress")){
			
			//Amplify
			if (ampActivity.getRegionalObservations()  != null){
				for (Iterator iterator = ampActivity.getRegionalObservations().iterator(); iterator.hasNext();) {
					AmpRegionalObservation issue = (AmpRegionalObservation) iterator.next();
			//		parent.getPhysicalProgress().add(buildPhysicalProgress(issue, ampColumnEntry));
				}
			}
			
		}
	}

	private AdditionalFieldType buildAdditionalFieldType(String lang, String field, String type) {
		// TODO Auto-generated method stub
		AdditionalFieldType ptip = objectFactory.createAdditionalFieldType();
		ptip.setLang(lang);
		ptip.setField(field);
		ptip.setType(type);
		return ptip;
	}
	
	private ActivityType.Id buildActivityTypeId(AmpActivityInternalId ids) throws AmpExportException{
		ActivityType.Id retValue = objectFactory.createActivityTypeId();
		retValue.setUniqID(ids.getInternalId());
		if (ids.getOrganisation() != null){
			retValue.setAssigningOrg(buildCodeValue(ids.getOrganisation().getOrgCode(), ids.getOrganisation().getName()));
		} else {
			String msg = "Id.Org is empty";
			this.addToLog(ampActivity, msg);
			throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
		}

		return retValue;
	}

	private CodeValueType buildImplementationLevel() throws AmpExportException{

		AmpCategoryValue ampCategoryValue = 
			CategoryManagerUtil.getAmpCategoryValueFromListByKey(
					CategoryConstants.IMPLEMENTATION_LEVEL_KEY, 
					ampActivity.getCategories());		

//		if (ampCategoryValue == null){
//			throw new AmpExportException("IMPLEMENTATION_LEVEL is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
//		}
		return buildCodeValue(ampCategoryValue);
	}

	private CodeValueType buildStatus() throws AmpExportException{

		AmpCategoryValue ampCategoryValue = 
			CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY,	ampActivity.getCategories());		
		return buildCodeValue(ampCategoryValue);
	}
	
	private ActivityType.Location buildLocation(AmpLocation location, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		ActivityType.Location retValue = objectFactory.createActivityTypeLocation();
		retValue.setLang(location.getLanguage());
		
		AmpCategoryValueLocations acvLoction = location.getLocation();
		

		if (acvLoction != null){

			AmpCategoryValueLocations  acvCountry = getCountryLocation(acvLoction);
			
			if (acvCountry != null){
				retValue.setIso3(""+acvCountry.getIso3());
				retValue.setCountryName(acvCountry.getName());
				retValue.setGis(location.getGisCoordinates());
			}

			
			retValue.setLocationName(buildCodeValue(acvLoction.getCode(),acvLoction.getName()));
			AmpCategoryValue categoryValue = acvLoction.getParentCategoryValue();
			if (CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey().equalsIgnoreCase(categoryValue.getValue()) ){
				retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_COUNTRY); 
			}
			if (CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT.getValueKey().equalsIgnoreCase(categoryValue.getValue()) ){
				retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_DISTRICT); 
			}
			if (CategoryConstants.IMPLEMENTATION_LOCATION_ZONE.getValueKey().equalsIgnoreCase(categoryValue.getValue()) ){
				retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_ZONE); 
			}
			if (CategoryConstants.IMPLEMENTATION_LOCATION_REGION.getValueKey().equalsIgnoreCase(categoryValue.getValue()) ){
				retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_REGION); 

				if (ampActivity.getRegionalFundings() != null){
					Collection regFund = ampActivity.getRegionalFundings();
					LocationFundingType fFundingType = objectFactory.createLocationFundingType();
					for (Iterator iterator = regFund.iterator(); iterator.hasNext();) {
						AmpRegionalFunding regFunding = (AmpRegionalFunding) iterator.next();
						if (acvLoction.getName().equalsIgnoreCase(regFunding.getRegionLocation().getName())){
							buildLocationFunding(fFundingType, regFunding, ampColumnEntry);
						}
					}
					if (!(fFundingType.getCommitments().isEmpty() &&
							fFundingType.getDisbursements().isEmpty() &&
							fFundingType.getExpenditures().isEmpty())) {
						retValue.getLocationFunding().add(fFundingType);
					}
				}
			
			}
		} else {
			String msg = "Location.LocationName is null";
			this.addToLog(ampActivity, msg);
			throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
		}

		return retValue;
	}

	private AmpCategoryValueLocations getCountryLocation(AmpCategoryValueLocations value){
		AmpCategoryValueLocations retValue = value;
		if (value.getParentLocation() != null){
			retValue = getCountryLocation(value.getParentLocation());
		}
		return retValue;
	}
	
	private void buildLocationFunding(LocationFundingType lFundingType, AmpRegionalFunding ampfunding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
//		LocationFundingType retValue = objectFactory.createLocationFundingType();
		
		String fDetailType = ampfunding.getAdjustmentType().getValue();
		
		FundingDetailType fDetail = buildFundingDetail(fDetailType, ampfunding.getTransactionDate(), 
				ampfunding.getTransactionAmount().longValue(), ampfunding.getCurrency().getCurrencyCode());


		switch (ampfunding.getTransactionType().intValue()) {
		case Constants.COMMITMENT:
			if (ampColumnEntry.getElementByName("commitments").canExport()){
				lFundingType.getCommitments().add(fDetail);
			}
			break;
		case Constants.DISBURSEMENT:
			if (ampColumnEntry.getElementByName("disbursements").canExport()){
				lFundingType.getDisbursements().add(fDetail);
			}
			break;
		case Constants.EXPENDITURE:
			if (ampColumnEntry.getElementByName("expenditures").canExport()){
				lFundingType.getExpenditures().add(fDetail);
			}
			break;

		}
		
//		return retValue;
	}	

	

	private FundingType buildFunding(AmpFunding funding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		FundingType retValue = objectFactory.createFundingType();
		retValue.setCode(""+funding.getFinancingId());

		for (AmpColumnEntry elem : ampColumnEntry.getElements()) {
			if (elem.canExport()){
				buildFundingSubElements(funding, retValue, elem);
			}
		}				
		return retValue;
	}

	private void buildFundingSubElements(AmpFunding ampfunding, FundingType funding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		String path = ampColumnEntry.getPath();

		if (path.equalsIgnoreCase("activity.funding.fundingOrg")){
			if (ampfunding.getAmpDonorOrgId()  != null){
				funding.setFundingOrg(buildCodeValue(ampfunding.getAmpDonorOrgId().getOrgCode(), ampfunding.getAmpDonorOrgId().getName()));
			} else {
				String msg = "Funding.getAmpDonorOrg is null";
				this.addToLog(ampActivity, msg);
				throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
		} else if (path.equalsIgnoreCase("activity.funding.assistanceType")){
			CodeValueType cValue =  buildCodeValue(ampfunding.getTypeOfAssistance());
			if (cValue == null){
				String msg = "Funding.assistanceType is null";
				this.addToLog(ampActivity, msg);
				throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
			funding.setAssistanceType(cValue);
		} else if (path.equalsIgnoreCase("activity.funding.financingInstrument")){
			CodeValueType cValue =  buildCodeValue(ampfunding.getFinancingInstrument());
			if (cValue == null){
				String msg = "Funding.financingInstrument is null";
				this.addToLog(ampActivity, msg);
				throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
			funding.setFinancingInstrument(cValue);
		} else if (path.equalsIgnoreCase("activity.funding.conditions")){
			funding.setConditions(buildFreeText(ampfunding.getLanguage(), ampfunding.getConditions()));
		} else if (path.equalsIgnoreCase("activity.funding.signatureDate")){
			funding.setSignatureDate(buildDate(ampfunding.getSignatureDate(), ampColumnEntry.isMandatory()));
		} else if (path.equalsIgnoreCase("activity.funding.projections")){
			for (AmpFundingMTEFProjection ampProj : ampfunding.getMtefProjections()) {
				FundingType.Projections proj = objectFactory.createFundingTypeProjections();
				proj.setType(ampProj.getProjected().getValue());
				proj.setAmount(new BigDecimal(ampProj.getAmount().longValue()));
				proj.setCurrency(ampProj.getAmpCurrency().getCurrencyCode());

				Calendar cal = Calendar.getInstance();
				cal.setTime(ampProj.getProjectionDate());
				proj.setStartYear(cal.get(Calendar.YEAR));
//				proj.setEndYear(null); // TODO 
				funding.getProjections().add(proj);
			}

		} else if (path.equalsIgnoreCase("activity.funding.commitments")){
			if (ampfunding.getFundingDetails() != null) {
				for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
					AmpFundingDetail  ampFDetail = (AmpFundingDetail) iterator.next();
					if (ampFDetail.getTransactionType() == Constants.COMMITMENT){
						funding.getCommitments().add(buildFundingDetail(ampFDetail));
					}
				}
			}
		} else if (path.equalsIgnoreCase("activity.funding.disbursements")){
			if (ampfunding.getFundingDetails() != null) {
				for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
					AmpFundingDetail  ampFDetail = (AmpFundingDetail) iterator.next();
					if (ampFDetail.getTransactionType() == Constants.DISBURSEMENT){
						funding.getDisbursements().add(buildFundingDetail(ampFDetail));
					}
				}
			}
		} else if (path.equalsIgnoreCase("activity.funding.expenditures")){
			if (ampfunding.getFundingDetails() != null) {
				for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
					AmpFundingDetail  ampFDetail = (AmpFundingDetail) iterator.next();
					if (ampFDetail.getTransactionType() == Constants.EXPENDITURE){
						funding.getExpenditures().add(buildFundingDetail(ampFDetail));
					}
				}
			}
		}
	}

	private ActivityType.Component buildComponent(Components<FundingDetail> component, AmpColumnEntry ampColumnEntry) throws AmpExportException{	
		ActivityType.Component retValue = objectFactory.createActivityTypeComponent();
		
		retValue.setComponentName(component.getTitle());
		
		AmpComponentType type = ComponentsUtil.getComponentTypeById(component.getType_Id());
		retValue.setComponentType(buildCodeValue(type.getCode(), type.getName()));
		
		ComponentFundingType componentFunding = objectFactory.createComponentFundingType();
		retValue.getComponentFunding().add(componentFunding);
		
		AmpColumnEntry componentFundingEntry = ampColumnEntry.getElementByName("componentFunding");
		if (componentFundingEntry.getElementByName("commitments").canExport() &&
				component.getCommitments() != null){
			for (FundingDetail fDetail : component.getCommitments()) {
				componentFunding.getCommitments().add(buildFundingDetail(fDetail));
			}
		}
		if (componentFundingEntry.getElementByName("disbursements").canExport() &&
				component.getDisbursements() != null){
			for (FundingDetail fDetail : component.getDisbursements()) {
				componentFunding.getDisbursements().add(buildFundingDetail(fDetail));
			}
		}
		if (componentFundingEntry.getElementByName("expenditures").canExport() &&
				component.getExpenditures() != null){
			for (FundingDetail fDetail : component.getExpenditures()) {
				componentFunding.getExpenditures().add(buildFundingDetail(fDetail));
			}
		}
		
		
		return retValue;
	}

	private ActivityType.PhysicalProgress buildPhysicalProgress(AmpRegionalObservation ampRegObs, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		ActivityType.PhysicalProgress retValue = objectFactory.createActivityTypePhysicalProgress();
		retValue.setTitle(buildFreeText(ampRegObs.getName()));
		retValue.setReportingDate(buildDate(getDate(ampRegObs.getObservationDate().toString()),false));
		return retValue;
	}
	
	private ActivityType.Issues buildIssue(AmpIssues ampIssue, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		ActivityType.Issues retValue = objectFactory.createActivityTypeIssues();
		
		retValue.setTitle(buildFreeText(ampIssue.getName()));
		
		if (ampIssue.getMeasures() != null){
			for (Iterator iterator = ampIssue.getMeasures().iterator(); iterator.hasNext();) {
				AmpMeasure ampMeasure = (AmpMeasure) iterator.next();
				ActivityType.Issues.Measure mesure = new ActivityType.Issues.Measure();
				mesure.setTitle(buildFreeText(ampMeasure.getName()));
				
				if (ampMeasure.getActors() != null){
					for (Iterator iterator2 = ampMeasure.getActors().iterator(); iterator2.hasNext();) {
						AmpActor ampActor = (AmpActor) iterator2.next();
						mesure.getActor().add(buildFreeText(ampActor.getName()));
					}
				}
				retValue.getMeasure().add(mesure);
			}
		}
		
		return retValue;
	}
	
	private ContactType buildContactType (AmpActivityContact actContact) throws AmpExportException{
		ContactType retValue = null;
		AmpContact contact = actContact.getContact();
		if (contact.getName()!=null && contact.getName().length()>0 && contact.getLastname()!=null && contact.getLastname().length() >0) {
			retValue = objectFactory.createContactType();
			retValue.setFirstName(contact.getName());
			retValue.setLastName(contact.getLastname());
			retValue.setPrimary(actContact.getPrimaryContact());
			if (contact.getEmails()!=null) {
				for (String email : contact.getEmails()) {
					retValue.getEmail().add(email);
				}
			}			
		}		
		return retValue;
	}
	
	
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
		CodeValueType retValue = null;
		if (value != null && value.trim().length() > 0){
			retValue = objectFactory.createCodeValueType();
			if (code != null && !code.isEmpty()){
				retValue.setCode(code);
			}
			retValue.setValue(value);
		}
		return retValue;
	}		

	private PercentageCodeValueType buildPercentageCodeValue(String code, String value, float precent) throws AmpExportException{
		PercentageCodeValueType retValue = objectFactory.createPercentageCodeValueType();
		if (code != null && !code.isEmpty()){
			retValue.setCode(code);
		}
		retValue.setValue(value);
		retValue.setPercentage(precent);
		return retValue;
	}		
	
	
	private FreeTextType buildFreeText(String name) throws AmpExportException{
		return buildFreeText(null, name);
	}

	private FreeTextType buildFreeText(String lang, String name) throws AmpExportException{
		FreeTextType retValue = null;
		if (name != null && name.trim().length() > 0 ){
			retValue = objectFactory.createFreeTextType();
			if (lang != null && !lang.isEmpty()){
				retValue.setLang(lang);
			}
			retValue.setValue(name);
		}
		return retValue;
	}	

	private FreeTextType buildFreeText(Message message) throws AmpExportException{
		if (message == null){
			return null;
		}
		return buildFreeText(message.getLocale(), message.getMessage());
	}

	private DateType buildDate(Date date, boolean require) throws AmpExportException{
		DateType retValue = null;
		if (date == null){
			if (require){
				throw new AmpExportException("Date is null", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			} 
		} else {
			retValue = objectFactory.createDateType();
			retValue.setDate(ExportHelper.getGregorianCalendar(date));
		}
		return retValue;
	}	

	private FundingDetailType buildFundingDetail(AmpFundingDetail detail) throws AmpExportException{
		String fDetailType = detail.getAdjustmentType().getValue(); 
		long amount = 0;
		
		if (detail.getTransactionAmount() != null){
			amount = detail.getTransactionAmount().longValue();
		} else {
			String msg = "AmpFundingDetail.getTransactionAmount is null";
			this.addToLog(ampActivity, msg);
			throw new AmpExportException(msg, AmpExportException.ACTIVITY_FORMAT);
		}
		
		return buildFundingDetail(fDetailType, detail.getTransactionDate(), amount, detail.getAmpCurrencyId().getCurrencyCode());
	}

	private FundingDetailType buildFundingDetail(FundingDetail fDetail) throws AmpExportException{
		long amount = 0;
		
		try {
			amount = Long.parseLong(fDetail.getTransactionAmount());
		} catch (Exception e) {
			String msg = "TransactionAmount is null or not correct";
			this.addToLog(ampActivity, msg);
			throw new AmpExportException(msg, e, AmpExportException.ACTIVITY_FORMAT);
		}

		return buildFundingDetail(fDetail.getAdjustmentTypeName().getValue(), getDate(fDetail.getTransactionDate()), amount, fDetail.getCurrencyCode());
	}

	private Date getDate(String stringDate) throws AmpExportException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date retValue = null;
		try{		
			retValue = format.parse(stringDate);
		} catch (Exception e) {
			String msg = "Date is null or not correct";
			this.addToLog(ampActivity, msg);
			throw new AmpExportException(msg, e, AmpExportException.ACTIVITY_FORMAT);
		}

		return retValue;
	}
	
	private FundingDetailType buildFundingDetail(String type, Date date, long amount, String currency) throws AmpExportException{
		FundingDetailType retValue = objectFactory.createFundingDetailType();
		retValue.setType(type);
		retValue.setDate(ExportHelper.getGregorianCalendar(date));
		
		amount = amount * AmountsUnits.getDefaultValue().divider;
		NumberFormat formatter = new DecimalFormat("#0.00");
		BigDecimal d = new BigDecimal(formatter.format(amount));
		retValue.setAmount(d);
		retValue.setCurrency(currency);
		
		return retValue;
	}
	
	private void addToLog(AmpActivityVersion activity, String error){
		this.exportLog = new String[3];
		
		this.exportLog[0] = activity.getAmpId();
		this.exportLog[1] = activity.getName();
		this.exportLog[2] = error;
	}
	
	public String[] getEroor(){
		return this.exportLog;
	}
}
