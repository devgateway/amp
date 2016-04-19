/**
 * 
 */
package org.digijava.module.dataExchange.util;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityBudgetStructure;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.type.AmpColumnEntry;
import org.digijava.module.dataExchange.type.IatiCode;
import org.digijava.module.dataExchange.util.DataExchangeConstants.IatiCodeTypeEnum;
import org.digijava.module.dataExchange.utils.DEConstants;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.ActivityDate;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.ActivityWebsite;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.Budget;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.CapitalSpend;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.CodeReqType;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.CodeType;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.ContactInfo;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.ContactInfo.MailingAddress;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.ContactInfo.Telephone;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.CurrencyType;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.DateType;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.Description;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.DocumentLink;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.IatiActivities;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.IatiActivity;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.IatiIdentifier;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.Location;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.Location.Administrative;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.Location.Coordinates;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.ObjectFactory;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.OtherIdentifier;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.ParticipatingOrg;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.PlainType;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.PlannedDisbursement;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.RecipientCountry;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.Sector;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.TextType;
import org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.Transaction;

/**
 * Transforms AMP data to IATI schema v 1.03 JAXB structure
 * 
 * Note: it may be suitable for other versions as well if there are no significant schema changes.
 * Adjust this class  
 * 
 * @author nmandrescu
 *
 */
public class ExportIatiBuilderV1_03 extends ExportIatiBuilderVX {
	private static final Logger logger = Logger.getLogger(ExportIatiBuilderV1_03.class) ;
	
	private ObjectFactory factory = null;
	
	private IatiActivities root = null;
	
	protected ExportIatiBuilderV1_03() {
		super();
		factory = new ObjectFactory();
		root = factory.createIatiActivities();
		initRoot();
	}
	
	protected void initRoot() {
		root.setVersion(new BigDecimal("1.03"));
		try {
			root.setGeneratedDatetime(ExportHelper.getGregorianCalendar(new Date(System.currentTimeMillis())));
		} catch(AmpExportException ex) {
			logger.error(ex.getMessage());
			this.exportLog.add("Could not set IatiActivities generatedDatetime"); //not critical to interrupt
		}
	}
	
	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	protected Object getNewIatiActivity() {
		return factory.createIatiActivity();
	}
	
	@Override
	protected void addIatiActivityToRoot(Object iAct, AmpActivity ampAct) throws AmpExportException {
		IatiActivity iatiAct = (IatiActivity)iAct;
		iatiAct.setLastUpdatedDatetime(ExportHelper.getGregorianCalendar(ampAct.getUpdatedDate()!=null ? ampAct.getUpdatedDate() : ampAct.getCreatedDate()));
		iatiAct.setVersion(new BigDecimal("1.03"));
		iatiAct.setLang(this.lang);
		iatiAct.setDefaultCurrency(ampAct.getCurrencyCode());
		//we don't have activity hierarchies in AMP, do we?
		iatiAct.setHierarchy(1);
		root.getIatiActivityOrAny().add(iatiAct);
	}

	@Override
	protected void addElement(Object iatiActIn, AmpColumnEntry elem, AmpActivity ampAct) throws AmpExportException {
		String path = elem.getName();
		IatiActivity iatiAct = (IatiActivity)iatiActIn;
		
		//logger.info(ampAct.getName() + " >>> " + path + " BEFORE");
		switch(path){
		case "iati-identifier": 	addActivityId(iatiAct, ampAct); break;		
		case "other-identifier": 	addOtherIdentifier(iatiAct, ampAct); break;
		case "activity-website":	addActivityWebsite(iatiAct, ampAct); break;
		case "title": 				addActivityTitle(iatiAct, ampAct); break;
		case "description":			addDescription(iatiAct, ampAct); break;
		case "activity-status":		addActivityStatus(iatiAct, ampAct); break;
		case "activity-date": 		addActivityDate(iatiAct, ampAct); break;
		case "contact-info": 		addContactInfo(iatiAct, ampAct, elem); break;
		case "participating-org": 	addParticipatingOrg(iatiAct, ampAct); break;
		case "activity-scope": 		addActivityScope(iatiAct, ampAct); break;
		case "recipient-country": 	addRecipientCountry(iatiAct, ampAct); break;
		case "location":			addLocation(iatiAct, ampAct, elem); break;
		case "sector": 				addSector(iatiAct, ampAct); break;
		case "collaboration-type":	addCollaborationType(iatiAct, ampAct); break;
		case "default-finance-type":addDefaultFinanceType(iatiAct, ampAct); break;
		case "budget": 				addBudget(iatiAct, ampAct, elem); break;
		case "planned-disbursement":addPlannedDisbursement(iatiAct, ampAct, elem); break;
		case "capital-spend":		addCapitalSpend(iatiAct, ampAct); break;
		case "transaction": 		addTransaction(iatiAct, ampAct, elem); break;
		case "document-link": 		addDocumentLink(iatiAct, ampAct, elem); break;
		
		case "reporting-org":		break; //N/A
		case "recipient-region": 	break; //this is higher level than national, like South of Sahara, that we do not store
		case "country-budget-items":break; //N/A
		case "policy-marker":		break; //N/A
		case "default-flow-type": 	break; //N/A
		case "default-aid-type": 	break; //N/A
		case "default-tied-status": break; //N/A
		case "related-activity": 	break; //we do not store relationship information with linked activites, but ref and type are required attributes //addRelatedActivity(iatiAct, ampAct); break;
		case "legacy-data": 		break; //N/A
		case "crs-add":				break; //N/A
		case "fss":					break; //N/A
		
		//TBD
		case "conditions":			break; //no conditions are attached in amp activity
		case "result":				break; //no specific info to provide in Result, or may be we can take something from Aid Effectiveness?
		default: 
			throw new AmpExportException("Not supported path="+elem.getPath(), AmpExportException.ACTIVITY_TRANSLATION);
		}
		//logger.info(ampAct.getName() + " >>> " + path + " AFTER");
	}
	
	/**
	 * Extracts IatiIdentifier from AmpActivityVersion
	 * @param act
	 * @return IatiIdentifier String
	 */
	public String getIatiIdentifier(AmpActivity act) {
		return (act==null || act.getProjectCode()==null ) ? null : act.getProjectCode();
	}

	//iati-identifier
	protected void addActivityId(IatiActivity iatiAct, AmpActivity ampAct) {
		String idStr = getIatiIdentifier(ampAct);
		if (StringUtils.isNotBlank(idStr)) {
			IatiIdentifier identifier = factory.createIatiIdentifier();
			identifier.setContent(idStr);
			iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(identifier);
		}
	}
	
	//other-identifier
	protected void addOtherIdentifier(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		if (ampAct.getInternalIds()!=null) {
			AmpActivityInternalId internalId = null;
			for (Iterator<AmpActivityInternalId> iter = ampAct.getInternalIds().iterator(); iter.hasNext(); ) {
				internalId = iter.next();
				OtherIdentifier oid = factory.createOtherIdentifier();
				oid.setContent(internalId.getInternalId());
				AmpOrganisation org = internalId.getOrganisation();
				if (org!=null) {
					String code = getIatiCodeItemCode(IatiCodeTypeEnum.OrganisationIdentifier, org.getName());
					if (code!=null) oid.setOwnerRef(code);
					oid.setOwnerName(org.getAcronym());
				}
				iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(oid);
			}
		}
	}
	
	//activity-website
	protected void addActivityWebsite(IatiActivity iatiAct, AmpActivity ampAct) {
		//should we provide full URL to AMP activity?
		ActivityWebsite website = factory.createActivityWebsite();
		String url = DgUtil.getSiteUrl(this.site, TLSUtils.getRequest());
		website.setValue(url); 
		iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(website);
	}
	
	//title
	protected void addActivityTitle(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		if (ampAct.getName() != null){
			iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(factory.createTitle(getTextType(ampAct.getName())));
		} else {
			String msg = "Name is empty";
			//this.addToLog(ampAct, msg);
			throw new AmpExportException(msg, AmpExportException.ACTIVITY_DATA_INEFFICIENT);
		}
	}

	//description
	protected void addDescription(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		if (StringUtils.isNotBlank(ampAct.getDescription())) {
			String descStr = DgUtil.cleanHtmlTags(ampAct.getDescription());//if not using AmpActivity, then need to call org.digijava.module.editor.util.DbUtil.getEditorBodyFiltered(this.site, ampAct.getDescription(), null);
			if (StringUtils.isNotBlank(descStr)) {
				Description desc = factory.createDescription();
				desc.getContent().add(descStr);
				iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(desc);
			}
		}
	}
	
	//activity-status
	protected void addActivityStatus(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		AmpCategoryValue categVal = findCategory(ampAct, DEConstants.CATEG_VALUE_ACTIVITY_STATUS);
		if (categVal!=null) {
			IatiCode iatiCode = getIatiCodeItemPair(DataExchangeConstants.IATI_ACTIVITY_STATUS, categVal.getValue(), IatiCodeTypeEnum.ActivityStatus);
			if (iatiCode.getCodeName()!=null)
				iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(factory.createActivityStatus(getCodeType(iatiCode.getCodeName(), iatiCode.getCodeValue())));
		}
	}

	//activity-date
	protected void addActivityDate(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		addActivityDate(iatiAct, "start-actual", ampAct.getActualStartDate());
		addActivityDate(iatiAct, "start-planned", ampAct.getProposedStartDate());
		addActivityDate(iatiAct, "end-actual", ampAct.getActualCompletionDate());
		addActivityDate(iatiAct, "end-planned", ampAct.getProposedCompletionDate());
	}
	
	protected void addActivityDate(IatiActivity iatiAct, String type, Date date) throws AmpExportException {
		if (date!=null) {
			ActivityDate iatiDate = factory.createActivityDate();
			iatiDate.setType(type);
			iatiDate.setIsoDate(ExportHelper.getGregorianCalendar(date)); 
			iatiDate.setValue(iatiDate.getIsoDate().toXMLFormat());
			iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(iatiDate);
		}
	}
	
	//contact-info
	protected void addContactInfo(IatiActivity iatiAct, AmpActivity ampAct, AmpColumnEntry parent) {
		if (ampAct.getActivityContacts()!=null) {
			AmpContact ampContact = null;
			boolean addPhone = false;
			boolean addEmail = false;
			for (Iterator<AmpActivityContact> iter = ampAct.getActivityContacts().iterator(); iter.hasNext();) {
				ampContact = iter.next().getContact();
				ContactInfo ci = factory.createContactInfo();
				for (AmpColumnEntry elem:parent.getElements()) {
					if (elem.canExport()) {
						switch(elem.getName()) {
						case "organisation" :
							if (StringUtils.isNotBlank(ampContact.getOrganisationName()))
								ci.getOrganisationOrPersonNameOrJobTitle().add(factory.createContactInfoOrganisation(getTextType(ampContact.getOrganisationName())));
							break;
						case "person-name":
							if (StringUtils.isNotBlank(ampContact.getFullname()))
								ci.getOrganisationOrPersonNameOrJobTitle().add(factory.createContactInfoPersonName(getTextType(ampContact.getFullname())));
							break;
						case "telephone": addPhone = true;
						break;
						case "email": addEmail = true; break;
						case "mailing-address": 
							if (StringUtils.isNotBlank(ampContact.getOfficeaddress())) {
								MailingAddress maddr = factory.createContactInfoMailingAddress();
								maddr.setContent(ampContact.getOfficeaddress());
								ci.getOrganisationOrPersonNameOrJobTitle().add(factory.createContactInfoMailingAddress(maddr));
							}
							break;
						case "job-title": break; //no such info available in AMP
						case "website": break; //no such info available in AMP
						}
					}
				}
				if (ampContact.getProperties()!=null && (addPhone || addEmail)) {
					AmpContactProperty ampContProp = null;
					for (Iterator<AmpContactProperty> iterProp = ampContact.getProperties().iterator(); iterProp.hasNext();) {
						ampContProp  = iterProp.next();
						//phone
						if (addPhone && Constants.CONTACT_PROPERTY_NAME_PHONE.equals(ampContProp.getName())) {
							Telephone phone = factory.createContactInfoTelephone();
							phone.setContent(ampContProp.getValue());
							ci.getOrganisationOrPersonNameOrJobTitle().add(getJAXBElementByQName(phone, "telephone"));
							//email
						} else if (addEmail && Constants.CONTACT_PROPERTY_NAME_EMAIL.equals(ampContProp.getName())) {
							ci.getOrganisationOrPersonNameOrJobTitle().add(factory.createContactInfoEmail(getPlainType(ampContProp.getValue())));
						}
					}
				}
				//add contact info if info is available
				if (ci.getOrganisationOrPersonNameOrJobTitle().size()>0 )
					iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(ci);
			}
		}
	}
	
	//participating-org
	protected void addParticipatingOrg(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		if (ampAct.getOrgrole()!=null) {
			for (Iterator<AmpOrgRole> iter = ampAct.getOrgrole().iterator(); iter.hasNext();) {
				AmpOrgRole ampOrgRole = iter.next();
				String role = null;
				//TODO: we should map this out as well
				switch(ampOrgRole.getRole().getRoleCode()) {
				case Constants.EXECUTING_AGENCY: role = "Extending"; break;
				case Constants.IMPLEMENTING_AGENCY: role = "Implementing"; break;
				case Constants.RESPONSIBLE_ORGANISATION: role = "Accountable"; break;
				case Constants.FUNDING_AGENCY: role = "Funding"; break;
				}
				if (role!=null) {
					ParticipatingOrg partOrg = factory.createParticipatingOrg();
					partOrg.setRole(role);
					partOrg.getContent().add(ampOrgRole.getOrganisation().getName());
					//org type
					IatiCode pair  = getIatiCodeItemPair(DataExchangeConstants.IATI_ORGANIZATION_TYPE,
							ampOrgRole.getOrganisation().getOrgGrpId().getOrgType().getOrgType(), IatiCodeTypeEnum.OrganisationType);
					if (pair.getCodeValue()!=null) partOrg.setType(pair.getCodeValue());
					//org ref, e.g. GB-1
					pair  = getIatiCodeItemPair(DataExchangeConstants.IATI_ORGANIZATION_IDENTIFIER,
							ampOrgRole.getOrganisation().getName(), IatiCodeTypeEnum.OrganisationIdentifier);
					if (pair.getCodeValue()!=null) partOrg.setRef(pair.getCodeValue());
					else {
						//try to find match directly by iati name
						String iatiRef = getIatiCodeItemCode(IatiCodeTypeEnum.OrganisationIdentifier, ampOrgRole.getOrganisation().getName());
						if (StringUtils.isNotBlank(iatiRef)) 
							partOrg.setRef(iatiRef);
					}
					iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(partOrg);
				}
			}
		}
	}
	
	//activity-scope
	protected void addActivityScope(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		AmpCategoryValue categVal = findCategory(ampAct, DEConstants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
		if (categVal!=null) {
			IatiCode pair = getIatiCodeItemPair(DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE, categVal.getValue(), IatiCodeTypeEnum.ActivityScope);
			iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(factory.createActivityScope(getCodeType(pair.getCodeName(), pair.getCodeValue())));
		}
	}
	
	//recipient-country
	protected void addRecipientCountry(IatiActivity iatiAct, AmpActivity ampAct) {
		AmpCategoryValue categVal = findCategory(ampAct, DEConstants.CATEG_VALUE_IMPLEMENTATION_LEVEL);
		if (categVal!=null) {
			if (CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL.getValueKey().equals(categVal.getValue())) {
				if (ampAct.getLocations()!=null) {
                    for (Object o : ampAct.getLocations()) {
                        AmpActivityLocation location = (AmpActivityLocation) o;
                        AmpCategoryValueLocations country = location.getLocation().getLocation();
                        iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(getCountry(country, new BigDecimal(location.getLocationPercentage())));
                    }
				}
			} else {
				AmpCategoryValueLocations defaultCountry = DynLocationManagerUtil.getDefaultCountry();
				iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(getCountry(defaultCountry, new BigDecimal("100")));
			}
		}
	}
	
	protected RecipientCountry getCountry(AmpCategoryValueLocations ampCountry, BigDecimal percentage) {
		RecipientCountry country = factory.createRecipientCountry();
		country.setLang(this.lang);
		country.setCode(ampCountry.getIso());
		country.setPercentage(percentage);
		country.getContent().add(ampCountry.getName());
		return country;
	}
	
	//location
	protected void addLocation(IatiActivity iatiAct, AmpActivity ampAct, AmpColumnEntry parent) {
		if (isNotEmpty(ampAct.getLocations())) {
			for (Iterator iter = ampAct.getLocations().iterator(); iter.hasNext(); ) {
				AmpActivityLocation location = (AmpActivityLocation) iter.next();
				AmpCategoryValueLocations categLocation = location.getLocation().getLocation();
				Location iatiLoc = factory.createLocation();
				iatiLoc.setPercentage(new BigDecimal(location.getLocationPercentage()));
				for (AmpColumnEntry elem : parent.getElements()) {
					if (elem.canExport()) {
						switch(elem.getName()) {
						case "name": 
							iatiLoc.getLocationTypeOrNameOrDescription().add(factory.createLocationName(getTextType(categLocation.getName()))); 
							break;
						case "description":
							if (StringUtils.isNotBlank(categLocation.getDescription()))
								iatiLoc.getLocationTypeOrNameOrDescription().add(factory.createLocationDescription(getTextType(categLocation.getDescription()))); 
							break;
						case "administrative":
							Administrative adm = factory.createLocationAdministrative();
							adm.getContent().add(categLocation.getHierarchicalName());
							Deque<AmpCategoryValueLocations> deque = new ArrayDeque<AmpCategoryValueLocations>();
							AmpCategoryValueLocations tempCategLoc = categLocation;
							deque.push(tempCategLoc);
							while (tempCategLoc.getParentLocation()!=null) {
								tempCategLoc = tempCategLoc.getParentLocation();
								deque.push(tempCategLoc);
							}
							//country
							tempCategLoc = deque.pop(); 
							adm.setCountry(tempCategLoc.getIso());
							//adm1
							if (!deque.isEmpty()) {
								tempCategLoc = deque.pop();
								adm.setAdm1(tempCategLoc.getCode());
							}
							//adm2
							if (!deque.isEmpty()) {
								tempCategLoc = deque.pop();
								adm.setAdm2(tempCategLoc.getCode());
							}
							iatiLoc.getLocationTypeOrNameOrDescription().add(getJAXBElementByQName(adm, "administrative"));
							break;
						case "coordinates": 
							if (StringUtils.isNotBlank(location.getLatitude()) && StringUtils.isNotBlank(location.getLongitude())) {
								Coordinates coordinates = factory.createLocationCoordinates();
								coordinates.setLatitude(new BigDecimal(location.getLatitude()));
								coordinates.setLongitude(new BigDecimal(location.getLongitude()));
								iatiLoc.getLocationTypeOrNameOrDescription().add(factory.createLocationDescription(getTextType(categLocation.getDescription())));
							}
						case "location-type": break; //N/A
						case "gazetteer-entry": break; //N/A
						}
					}
				}
				if (iatiLoc.getLocationTypeOrNameOrDescription().size()>0)
					iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(iatiLoc);
			}
		}
	}
	
	//sector
	protected void addSector(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		if (isNotEmpty(ampAct.getSectors())) {
			for (Iterator iter = ampAct.getSectors().iterator(); iter.hasNext();) {
				AmpActivitySector sector = (AmpActivitySector) iter.next();
				//add only primary sectors
				if (sector.getClassificationConfig().isPrimary()) {
					Sector iatiSector = factory.createSector();
					IatiCode pair = getIatiCodeItemPair(DataExchangeConstants.IATI_SECTOR, sector.getSectorId().getName(), IatiCodeTypeEnum.Sector);
					String sectorName = pair.getCodeName()!=null ? pair.getCodeName() : sector.getSectorId().getName(); //if not found, put what we have
					String sectorCode = pair.getCodeValue()!=null ? pair.getCodeValue() : sector.getSectorId().getSectorCodeOfficial();
					
					iatiSector.setCode(sectorCode);
					iatiSector.getContent().add(sectorName);
					iatiSector.setLang(this.lang);
					iatiSector.setPercentage(new BigDecimal(sector.getSectorPercentage()));
					iatiSector.setVocabulary(sector.getClassificationConfig().getClassification().getSecSchemeCode());
					iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(iatiSector);
				}
			}
		}
	}
	
	//collaboration-type
	protected void addCollaborationType(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		AmpCategoryValue categVal = findCategory(ampAct, CategoryConstants.TYPE_OF_COOPERATION_NAME);
		if (categVal!=null) {
			IatiCode pair = getIatiCodeItemPair(DataExchangeConstants.IATI_COLLABORATION_TYPE, categVal.getValue(), IatiCodeTypeEnum.CollaborationType);
			if (pair.getCodeName()!=null && pair.getCodeValue()!=null)
				iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(factory.createCollaborationType(getCodeReqType(pair.getCodeName(), pair.getCodeValue())));
		}
	}
	
	//default-finance-type
	/**
	 * Adds default finance type only if all fundings have same funding type 
	 * @param iatiAct
	 * @param ampAct
	 * @throws AmpExportException 
	 */
	protected void addDefaultFinanceType(IatiActivity iatiAct, AmpActivity ampAct) throws AmpExportException {
		if (isNotEmpty(ampAct.getFunding())) {
			String typeOfAssist = null;
			for (Iterator<AmpFunding> iter = ampAct.getFunding().iterator(); iter.hasNext(); ) {
				AmpFunding ampFunding = iter.next();
				if (ampFunding.getTypeOfAssistance()==null || 
						typeOfAssist!=null && !typeOfAssist.equalsIgnoreCase(ampFunding.getTypeOfAssistance().getValue())) {
					typeOfAssist = null;
					break;
				} else typeOfAssist = ampFunding.getTypeOfAssistance().getValue();
			}
			if (typeOfAssist!=null) {
				CodeReqType code = factory.createCodeReqType();
				IatiCode pair = getIatiCodeItemPair(DataExchangeConstants.IATI_FINANCE_TYPE_CATEGORY, typeOfAssist, IatiCodeTypeEnum.FinanceTypeCategory);
				if (pair.getCodeValue()!=null) {
					code.setCode(pair.getCodeValue());
					code.getContent().add(pair.getCodeName());
					code.setLang(this.lang);
					iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(factory.createDefaultFinanceType(code));
				}
			}
		}
	}
	
	//budget
	protected void addBudget(IatiActivity iatiAct, AmpActivity ampAct, AmpColumnEntry parent) throws AmpExportException {
		//both mandatory
		if (ampAct.getFunAmount()!=null && ampAct.getFunDate()!=null) {
			Budget budget = factory.createBudget();
			//budget.setType();//N/A
			for (AmpColumnEntry elem:parent.getElements()) {
				if (elem.canExport()) {
					switch(elem.getName()) {
					case "period-start": 
						budget.getPeriodStartOrPeriodEndOrValue().add(
								factory.createBudgetPeriodStart(getDateType(ampAct.getFunDate())));
						break;
					case "value":
						budget.getPeriodStartOrPeriodEndOrValue().add(
								factory.createBudgetValue(getCurrencyType(new BigDecimal(ampAct.getFunAmount()), ampAct.getFunDate(), ampAct.getCurrencyCode())));
						break;
					case "period-end": break; //N/A
					}
				}
			}
			if (budget.getPeriodStartOrPeriodEndOrValue().size()>0)
				iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(budget);
		}
	}
	
	//planned-disbursement
	protected void addPlannedDisbursement(IatiActivity iatiAct, AmpActivity ampAct, AmpColumnEntry parent) throws AmpExportException {
		if (isNotEmpty(ampAct.getFunding())) {
			for (Iterator<AmpFunding> iter = ampAct.getFunding().iterator(); iter.hasNext();) {
				AmpFunding ampFunding  = iter.next();
				if (isNotEmpty(ampFunding.getFundingDetails())) {
					for (Iterator<AmpFundingDetail> iter2 = ampFunding.getFundingDetails().iterator(); iter2.hasNext(); ) {
						AmpFundingDetail detail = iter2.next();
						if (Constants.DISBURSEMENT==detail.getTransactionType() && Constants.PLANNED==detail.getAdjustmentType().getIndex()
								&& detail.getTransactionDate()!=null && detail.getTransactionAmount()!=null) {
							PlannedDisbursement planned = factory.createPlannedDisbursement();
							planned.setUpdated(ExportHelper.getGregorianCalendar(detail.getReportingDate()));
							for (AmpColumnEntry elem:parent.getElements()) {
								if (elem.canExport()) {
									switch(elem.getName()) {
									case "period-start": 
										planned.getPeriodStartOrPeriodEndOrValue().add(
												factory.createPlannedDisbursementPeriodStart(getDateType(detail.getTransactionDate())));
										break;
									case "value":
										planned.getPeriodStartOrPeriodEndOrValue().add(
												factory.createPlannedDisbursementValue(getCurrencyType(
														new BigDecimal(detail.getTransactionAmount()), 
														detail.getTransactionDate(), 
														detail.getAmpCurrencyId().getCurrencyCode())));
										break;
									case "period-end": break; //N/A
									}
								}
							}
							if (planned.getPeriodStartOrPeriodEndOrValue().size()>0)
								iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(planned);
						}
					}
				}
			}
		}
	}
	
	//capital-spend
	protected void addCapitalSpend(IatiActivity iatiAct, AmpActivity ampAct) {
		if(ampAct.getActBudgetStructure()!=null && ampAct.getActBudgetStructure().size()>0){
        	for (Iterator<AmpActivityBudgetStructure> iter = ampAct.getActBudgetStructure().iterator(); iter.hasNext(); ) {
        		AmpActivityBudgetStructure budgetStruct = iter.next();
        		if (Constants.BUDGET_STRUCTURE_NAME_CAPITAL.equals(budgetStruct.getBudgetStructureName())) {
        			CapitalSpend cap = factory.createCapitalSpend();
        			cap.setPercentage(new BigDecimal(budgetStruct.getBudgetStructurePercentage()));
        			iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(cap);
        		}
        	}
        }
	}
	
	//transaction
	protected void addTransaction(IatiActivity iatiAct, AmpActivity ampAct, AmpColumnEntry parent) throws AmpExportException {
		if (isNotEmpty(ampAct.getFunding())) {
			for (Iterator<AmpFunding> iter = ampAct.getFunding().iterator(); iter.hasNext(); ) {
				AmpFunding ampFunding = iter.next();
				if (isNotEmpty(ampFunding.getFundingDetails())) {
					for (Iterator<AmpFundingDetail> iter2 = ampFunding.getFundingDetails().iterator(); iter2.hasNext();) {
						AmpFundingDetail detail = iter2.next();
						if (detail.getTransactionDate()!=null && detail.getTransactionAmount()!=null) {
							Transaction transaction = factory.createTransaction();
							transaction.setRef(detail.getAmpFundDetailId().toString());
							IatiCode pair = null;
							for (AmpColumnEntry elem:parent.getElements()) {
								if (elem.canExport()) {
									switch(elem.getName()) {
									case "aid-type": //N/A actually
										pair = getIatiCodeItemPair(DataExchangeConstants.IATI_AID_TYPE, 
												ampFunding.getTypeOfAssistance().getValue(), IatiCodeTypeEnum.AidType);
										if (pair.getCodeValue()!=null) {
											transaction.getValueOrDescriptionOrTransactionType().add(
													factory.createTransactionAidType(getCodeReqType(pair.getCodeName(), pair.getCodeValue())));
										}
										break;
									case "finance-type":
										pair = getIatiCodeItemPair(DataExchangeConstants.IATI_FINANCE_TYPE_CATEGORY, 
												ampFunding.getTypeOfAssistance().getValue(), IatiCodeTypeEnum.FinanceTypeCategory);
										if (pair.getCodeValue()!=null) {
											transaction.getValueOrDescriptionOrTransactionType().add(
													factory.createTransactionFinanceType(getCodeReqType(pair.getCodeName(), pair.getCodeValue())));
										}
										break;
									case "provider-org":
										pair = getIatiCodeItemPair(DataExchangeConstants.IATI_ORGANIZATION_IDENTIFIER,
												ampFunding.getAmpDonorOrgId().getName(), IatiCodeTypeEnum.OrganisationIdentifier);
										Transaction.ProviderOrg org = factory.createTransactionProviderOrg();
										org.setRef(pair.getCodeValue());
										org.getContent().add(pair.getCodeName());
										transaction.getValueOrDescriptionOrTransactionType().add(getJAXBElementByQName(org, "provider-org"));
										break;
									case "transaction-date":
										Transaction.TransactionDate trDate = factory.createTransactionTransactionDate();
										trDate.setIsoDate(ExportHelper.getGregorianCalendar(detail.getTransactionDate()));
										transaction.getValueOrDescriptionOrTransactionType().add(getJAXBElementByQName(trDate, "transaction-date"));
										break;
									case "transaction-type":
										switch(detail.getTransactionType()) {
										case Constants.COMMITMENT: pair = new IatiCode("Commitment", "C"); break;
										case Constants.DISBURSEMENT: pair = new IatiCode("Disbursement", "D"); break;
										case Constants.EXPENDITURE: pair = new IatiCode("Expenditure", "E"); break;
										default: pair = null;
										}
										if (pair!=null)
											transaction.getValueOrDescriptionOrTransactionType().add(
													factory.createTransactionTransactionType(getCodeReqType(pair.getCodeName(), pair.getCodeValue())));
										break;
									case "value":
										transaction.getValueOrDescriptionOrTransactionType().add(
											factory.createTransactionValue(getCurrencyType(
													new BigDecimal(detail.getTransactionAmount()), 
													detail.getTransactionDate(), 
													detail.getAmpCurrencyId().getCurrencyCode())));
										break;
									case "description": break; //N/A
									case "disbursement-channel": break; //N/A
									case "flow-type": break; //N/A
									case "receiver-org": break; //N/A
									case "tied-status": break; //N/A
									}
								}
							}
							if (transaction.getValueOrDescriptionOrTransactionType().size()>0)
								iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(transaction);
						}
					}
				}
			}
		}
	}
	
	//document-link
	protected void addDocumentLink(IatiActivity iatiAct, AmpActivity ampAct, AmpColumnEntry parent) throws AmpExportException {
		if (isNotEmpty(ampAct.getDocuments())) {
			for (Iterator iter = ampAct.getDocuments().iterator(); iter.hasNext(); ) {
				Documents doc = (Documents) iter.next();
				if (!doc.getIsFile()) {
					DocumentLink iatiDocLink = factory.createDocumentLink();
					iatiDocLink.setUrl(doc.getUrl());
					for (AmpColumnEntry elem:parent.getElements()) {
						if (elem.canExport()) {
							switch(elem.getName()) {
							case "title": 
								if (StringUtils.isNotBlank(doc.getTitle())) 
									iatiDocLink.getTitleOrCategoryOrLanguage().add(factory.createTitle(getTextType(doc.getTitle())));
								break;
							case "category":
								IatiCode pair = getIatiCodeItemPair(DataExchangeConstants.IATI_DOCUMENT_CATEGORY, 
										doc.getDocType(), IatiCodeTypeEnum.DocumentCategory);
								if (pair!=null)
									iatiDocLink.getTitleOrCategoryOrLanguage().add(
											factory.createDocumentLinkCategory(getCodeReqType(pair.getCodeName(), pair.getCodeValue())));
								break;
							case "language": break; //N/A
							}
						}
					}
					iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(iatiDocLink);
				}
			}
		}
	}
	
	protected void addRelatedActivity(IatiActivity iatiAct, AmpActivity ampAct) {
		/* linked Activities removed in 2.10
		if (StringUtils.isNotBlank(ampAct.getExpandedLinkedActivites())) {
			RelatedActivity relAct = factory.createRelatedActivity();
			String linkedActivities = ampAct.getExpandedLinkedActivites();
			relAct.getContent().add(linkedActivities);
			//we do not store other relationship information, but 
			//iatiAct.getActivityWebsiteOrReportingOrgOrParticipatingOrg().add(relAct);
		}*/
	}	
	
	protected CodeReqType getCodeReqType(String content, String code) {
		CodeReqType elem = factory.createCodeReqType();
		elem.setLang(this.lang);
		elem.setCode(code);
		elem.getContent().add(content);
		return elem;
	}
	
	protected CodeType getCodeType(String content, String code) {
		CodeType elem = factory.createCodeType();
		elem.setCode(code);
		elem.setLang(this.lang);
		elem.getContent().add(content);
		return elem;
	}
	
	protected TextType getTextType(String content) {
		TextType elem = factory.createTextType();
		elem.setLang(this.lang);
		elem.getContent().add(content);
		return elem;
	}
	
	protected PlainType getPlainType(String content) {
		PlainType elem = factory.createPlainType();
		elem.setContent(content);
		return elem;
	}
	
	protected DateType getDateType(Date date) throws AmpExportException {
		DateType elem = factory.createDateType();
		elem.setIsoDate(ExportHelper.getGregorianCalendar(date));
		return elem;
	}
	
	protected CurrencyType getCurrencyType(BigDecimal value, Date date, String currency) throws AmpExportException {
		CurrencyType elem = factory.createCurrencyType();
		elem.setCurrency(currency);
		elem.setValue(value);
		elem.setValueDate(ExportHelper.getGregorianCalendar(date));
		return elem;
	}
	
	//AMP-17797: wrapping into JAXBElement
	protected <T> JAXBElement<T> getJAXBElementByQName(T elem, String qName) {
		return new JAXBElement<T>(new QName(qName), (Class<T>) elem.getClass(), elem);
	}
}
