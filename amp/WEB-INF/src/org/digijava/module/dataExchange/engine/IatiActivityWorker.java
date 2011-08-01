/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.dataExchange.dbentity.AmpMappedField;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.util.DataExchangeConstants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.ActivityDate;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.CodeReqType;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.CodeType;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.ContactInfo;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.CurrencyType;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.Description;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiActivity;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiIdentifier;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.Location;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.OtherIdentifier;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.ParticipatingOrg;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.PlainType;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.ReportingOrg;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.Sector;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.TextType;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.Transaction;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;

/**
 * @author dan
 *
 */
public class IatiActivityWorker {

	/**
	 * 
	 */
	private IatiActivity iActivity;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIatiID() {
		return iatiID;
	}

	public void setIatiID(String iatiID) {
		this.iatiID = iatiID;
	}

	private String title ="";
	private String iatiID="";
	private Long ampID = null;
	public Long getAmpID() {
		return ampID;
	}

	public void setAmpID(Long ampID) {
		this.ampID = ampID;
	}

	private String lang = "en";
	
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public IatiActivityWorker(IatiActivity iActivity, String title,	String iatiID, String log) {
		super();
		this.iActivity = iActivity;
		this.title = title;
		this.iatiID = iatiID;
		this.log = log;
	}

	public IatiActivityWorker(IatiActivity iActivity, String lang, String log) {
		super();
		this.iActivity = iActivity;
		this.lang = lang;
		this.log = log;
	}

	public IatiActivityWorker(IatiActivity iActivity, String log) {
		super();
		this.iActivity = iActivity;
		this.log = log;
	}

	public IatiActivity getiActivity() {
		return iActivity;
	}

	public void setiActivity(IatiActivity iActivity) {
		this.iActivity = iActivity;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	private String log;
	
	public IatiActivityWorker() {
		// TODO Auto-generated constructor stub
	}

	public boolean existActivityByTitleIatiId(String s){
		if(s==null || "".compareTo(s.trim()) ==0 ) return false;
		String title = "";
		String id = "";
		for (Iterator<Object> it = this.getiActivity().getActivityWebsiteOrReportingOrgOrParticipatingOrg().iterator(); it.hasNext();) {
			Object contentItem = (Object) it.next();
			if(contentItem instanceof JAXBElement){
				JAXBElement i = (JAXBElement)contentItem;
				if(i.getName().equals(new QName("title"))){
					JAXBElement<TextType> item = (JAXBElement<TextType>)i;
					title += printTextType(item);
				}
			}
			if(contentItem instanceof IatiIdentifier){
				IatiIdentifier item = (IatiIdentifier)contentItem;
				id += item.getContent();
			}
			
		}
		if( s.toLowerCase().compareTo((title+" "+id).toLowerCase()) == 0 ) 
			return true;
		return false;
	}
	
	public ArrayList<AmpMappedField> checkContent() { 
		// TODO Auto-generated method stub
		ArrayList<AmpMappedField> logs = new ArrayList<AmpMappedField>();
		try{
			for (Iterator<Object> it = this.getiActivity().getActivityWebsiteOrReportingOrgOrParticipatingOrg().iterator(); it.hasNext();) {
				Object contentItem = (Object) it.next();
				if(contentItem instanceof JAXBElement){
					JAXBElement i = (JAXBElement)contentItem;
	
					//title
					if(i.getName().equals(new QName("title"))){
						JAXBElement<TextType> item = (JAXBElement<TextType>)i;
						System.out.println("activity title:" + printTextType(item)+"#");
						this.title += printTextType(item);
					}
					//status
					if(i.getName().equals(new QName("activity-status"))){
						JAXBElement<CodeType> item = (JAXBElement<CodeType>)i;
						AmpMappedField existStatusCode = checkStatusCode(item);
						logs.add(existStatusCode);
					}
	
					//default-finance-type == type of assistance
					if(i.getName().equals(new QName("default-finance-type"))){
						JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
						AmpMappedField existFinanceType = checkFinanceType(item);
						logs.add(existFinanceType);
					}
	
					//default-aid-type == financing instrument
					if(i.getName().equals(new QName("default-aid-type"))){
						JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
						AmpMappedField existAidType = checkAidType(item);
						logs.add(existAidType);
					}
					
				}
	
				if(contentItem instanceof IatiIdentifier){
					IatiIdentifier item = (IatiIdentifier)contentItem;
					this.iatiID += item.getContent();
				}
				
				if(contentItem instanceof ReportingOrg){
					ReportingOrg item = (ReportingOrg)contentItem;
					AmpMappedField existOrganizationType = checkOrganizationType(item.getType());
					AmpMappedField existOrganization	  = checkOrganization(printList(item.getContent()),item.getLang(), item.getRef());
					logs.add(existOrganization);
					logs.add(existOrganizationType);
				}
				
				if(contentItem instanceof ParticipatingOrg){
					ParticipatingOrg item = (ParticipatingOrg)contentItem;
					AmpMappedField existOrganizationType = checkOrganizationType(item.getType());
					AmpMappedField existOrganization	  = checkOrganization(printList(item.getContent()),item.getLang(), item.getRef());
					logs.add(existOrganization);
					logs.add(existOrganizationType);
				}
				
				if(contentItem instanceof OtherIdentifier){
					OtherIdentifier item = (OtherIdentifier)contentItem;
					AmpMappedField existOrganization	  = checkOrganization(item.getOwnerName(),this.getLang(), item.getOwnerRef());
					logs.add(existOrganization);
				}
				
				if(contentItem instanceof Location){
					Location item = (Location)contentItem;
					AmpMappedField existLocation	  = checkLocation(item);
					logs.add(existLocation);
				}
				
				if(contentItem instanceof Sector){
					Sector item = (Sector)contentItem;
					AmpMappedField existVocabularyCode = checkVocabularyCode(item);
					AmpMappedField existSector			= checkSector(item);
					logs.add(existVocabularyCode);
					logs.add(existSector);
				}
				
				if(contentItem instanceof Transaction){
					Transaction item = (Transaction)contentItem;
					boolean ok 		 = false;
					ok 				 = checkIATITransaction(item,logs);
				}
			}
			AmpMappedField checkedActivity = checkActivity(this.title, this.iatiID, this.lang);
			
				if(checkedActivity.getItem().getAmpId() !=null)
					this.ampID = checkedActivity.getItem().getAmpId();
			}
		catch(Exception e){
			e.printStackTrace();
		}
		return logs;
	}
	


	public ArrayList<AmpMappedField> populateActivity(AmpActivityVersion a){
		ArrayList<AmpMappedField> logs = new ArrayList<AmpMappedField>();
		
		ArrayList<JAXBElement<TextType>> iatiTitleList = new ArrayList<JAXBElement<TextType>>();
		ArrayList<JAXBElement<CodeType>> iatiStatusList = new ArrayList<JAXBElement<CodeType>>();
		ArrayList<ParticipatingOrg> iatiPartOrgList = new ArrayList<ParticipatingOrg>();
		ArrayList<ReportingOrg> iatiRepOrgList = new ArrayList<ReportingOrg>();
		ArrayList<Sector> iatiSectorList = new ArrayList<Sector>();
		ArrayList<Transaction> iatiTransactionList = new ArrayList<Transaction>();
		ArrayList<Description> iatiDescriptionList = new ArrayList<Description>();
		IatiIdentifier iatiID = null;
		ArrayList<OtherIdentifier> iatiOtherIdList = new ArrayList<OtherIdentifier>();
		ArrayList<ActivityDate> iatiActDateList = new ArrayList<ActivityDate>();
		ArrayList<ContactInfo> iatiContactList = new ArrayList<ContactInfo>();
		ArrayList<Location> iatiLocationList = new ArrayList<Location>();
		
		ArrayList<AmpClassificationConfiguration> allClassificationConfiguration = (ArrayList<AmpClassificationConfiguration>) getAllClassificationConfiguration();
		
		JAXBElement<CodeReqType> iatiDefaultFinanceType = null;
		JAXBElement<CodeReqType> iatiDefaultAidType = null;
				
		
		
		if(this.getiActivity().getLang()!=null || "".compareTo(this.getiActivity().getLang().trim()) == 0)
			{
				this.lang = this.getiActivity().getLang();
			}
		
		//DatatypeFactory.newInstance().newXMLGregorianCalendar();
		
		String iatiDefaultCurrency = "USD";
		if(this.getiActivity().getDefaultCurrency()!=null || "".compareTo(this.getiActivity().getDefaultCurrency().trim()) == 0)
			iatiDefaultCurrency = this.getiActivity().getDefaultCurrency();
		
		XMLGregorianCalendar lastUpdatedDatetime = this.getiActivity().getLastUpdatedDatetime();
		
		//populate the lists with IATI values of the activity
		extractIATIEntities(iatiTitleList, iatiStatusList, iatiPartOrgList,
				iatiRepOrgList, iatiSectorList, iatiTransactionList,
				iatiDescriptionList, iatiOtherIdList, iatiActDateList,
				iatiContactList, iatiLocationList,iatiDefaultFinanceType, iatiDefaultAidType, iatiID);
		
		processIdentificationStep(a,iatiStatusList,iatiDescriptionList,iatiID);
		processPlanningStep(a,iatiActDateList);
		processSectorsStep(a,iatiSectorList,allClassificationConfiguration);
		processRelOrgsStep(a,iatiPartOrgList);
		processActInternalIdsStep(a,iatiOtherIdList);
		processFundingStep(a,iatiTransactionList,iatiDefaultFinanceType,iatiDefaultAidType, iatiDefaultCurrency);
		processLocationStep(a,iatiLocationList);
		processContactsStep(a,iatiContactList);
		return logs;
	}
	
	
	private void processContactsStep(AmpActivityVersion a, ArrayList<ContactInfo> iatiContactList) {
		if (iatiContactList.isEmpty()) return;
		Set<AmpActivityContact> activityContacts=new HashSet<AmpActivityContact>();
		for (Iterator<ContactInfo> it = iatiContactList.iterator(); it.hasNext();) {
			ContactInfo contactInfo = (ContactInfo) it.next();
			AmpContact ampContact = new AmpContact();
			setAmpContactDetails(contactInfo, ampContact);
			try {
				ContactInfoUtil.saveOrUpdateContact(ampContact);
			} catch (Exception e) {
				e.printStackTrace();
			}
			AmpActivityContact ampActContact = new AmpActivityContact();
			ampActContact.setActivity(a);
			ampActContact.setContact(ampContact);
			ampActContact.setContactType(org.digijava.module.aim.helper.Constants.DONOR_CONTACT);
			activityContacts.add(ampActContact);
			
		}
		
		a.setActivityContacts(activityContacts);
		
		
	}

	private void setAmpContactDetails(ContactInfo contactInfo, AmpContact ampContact) {
		Set<AmpContactProperty> contactProperties=new TreeSet<AmpContactProperty>();
		for (Iterator<Object> it = contactInfo.getOrganisationOrPersonNameOrTelephone().iterator(); it.hasNext();) {
			Object contentItem = (Object) it.next();
			if(contentItem instanceof JAXBElement){
				JAXBElement i = (JAXBElement)contentItem;

				//name
				if(i.getName().equals(new QName("person-name"))){
					JAXBElement<PlainType> item = (JAXBElement<PlainType>)i;
					setContactName(item.getValue().getContent().trim(),ampContact);
				}
				//organisation
				if(i.getName().equals(new QName("organisation"))){
					JAXBElement<PlainType> item = (JAXBElement<PlainType>)i;
					ampContact.setOrganisationName(item.getValue().getContent());
				}
				//phone
				if(i.getName().equals(new QName("telephone"))){
					ContactInfo.Telephone item = (ContactInfo.Telephone)i.getValue();
					AmpContactProperty acp = new AmpContactProperty();
					acp.setValue(item.getContent());
					acp.setName(Constants.CONTACT_PROPERTY_NAME_PHONE);
					contactProperties.add(acp);
				}

				//email
				if(i.getName().equals(new QName("email"))){
					JAXBElement<PlainType> item = (JAXBElement<PlainType>)i;
					AmpContactProperty acp = new AmpContactProperty();
					acp.setValue(item.getValue().getContent());
					acp.setName(Constants.CONTACT_PROPERTY_NAME_EMAIL);
					contactProperties.add(acp);
				}

				//mailing-address
				if(i.getName().equals(new QName("mailing-address"))){
					JAXBElement<PlainType> item = (JAXBElement<PlainType>)i;
					ampContact.setOfficeaddress(item.getValue().getContent());
				}
			}
		}
		ampContact.setProperties(contactProperties);
	}

	private void setContactName(String s, AmpContact ampContact) {
		// TODO Auto-generated method stub
		int i = s.indexOf(" ");
		ampContact.setName(s.substring(0,i));
		ampContact.setLastname(s.substring(i+1,s.length()));
	}

	private void processLocationStep(AmpActivityVersion a, ArrayList<Location> iatiLocationList) {
		
		// TODO Implementation Location and Implementation Level 
		
		if(iatiLocationList.isEmpty()) return;
		Set<AmpActivityLocation> locations = new HashSet<AmpActivityLocation>();
		for (Iterator it = iatiLocationList.iterator(); it.hasNext();) {
			Location location = (Location) it.next();
			String locationType	= null;
			String locationName	= null;
			String locationCountry = null;
			String adm1 = null;
			String adm2 = null;
			String adm3 = null;

			getLocationDetails(location,locationName,locationType,locationCountry,adm1,adm2,adm3);
			AmpLocation ampLocation = getAmpLocation(toIATIValues("locationName","locationType","locationCountry","adm1","adm2","adm3"),
						   toIATIValues(locationName,locationType,locationCountry,adm1,adm2,adm3));
			AmpActivityLocation actLoc	=	new AmpActivityLocation();
			actLoc.setActivity(a);
			actLoc.setLocation(ampLocation);
			Double percent=new Double(location.getPercentage().doubleValue());
            actLoc.setLocationPercentage(percent.floatValue());
			locations.add(actLoc);
			
		}
		if(a.getLocations() == null)
			a.setLocations(new HashSet<AmpActivityLocation>());
		else a.getLocations().clear();
		a.getLocations().addAll(locations);
		
	}

	
	
	private void processFundingStep(AmpActivityVersion a, ArrayList<Transaction> iatiTransactionList,	JAXBElement<CodeReqType> iatiDefaultFinanceType,
			JAXBElement<CodeReqType> iatiDefaultAidType, String iatiDefaultCurrency) {
		
		if(iatiTransactionList.isEmpty()) return;
		
		AmpCategoryValue typeOfAssistance = getAmpCategoryValue(iatiDefaultFinanceType, DataExchangeConstants.IATI_FINANCE_TYPE,
				toIATIValues("financeTypeValue","financeTypeCode"),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"active");
		AmpCategoryValue financingInstrument = getAmpCategoryValue(iatiDefaultFinanceType, DataExchangeConstants.IATI_AID_TYPE,
				toIATIValues("aidTypeValue","aidTypeCode"),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"active");
		Set<AmpFunding> fundings = new HashSet<AmpFunding>();
		for (Iterator<Transaction> it = iatiTransactionList.iterator(); it.hasNext();) {
			Transaction t = (Transaction) it.next();
			AmpFunding f = getFundingIATItoAMP(a,t,typeOfAssistance, financingInstrument, iatiDefaultCurrency);
			fundings.add(f);
		}
		
		if(a.getFunding() == null) 
			a.setFunding(new HashSet());
		else
			a.getFunding().clear();
		a.getFunding().addAll(fundings);

	}

	
	private AmpFunding getFundingIATItoAMP(AmpActivityVersion a, Transaction t,
			AmpCategoryValue iatiDefaultFinanceType,
			AmpCategoryValue iatiDefaultAidType,
			String iatiDefaultCurrency) {

		Set<AmpOrgRole> orgRole = new HashSet<AmpOrgRole>();
		AmpRole role = DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.FUNDING_AGENCY);
		AmpOrganisation ampOrg = null;
		AmpCategoryValue typeOfAssistance = iatiDefaultFinanceType;
		AmpCategoryValue modeOfPayment = null;
		AmpCategoryValue financingInstrument = iatiDefaultAidType;
		String transactionType = "";
		String description ="";
		Date tDate = new Date();
		Double currencyValue = new Double(0);
		String currencyName = iatiDefaultCurrency;
		
		for (Iterator<Object> it = t.getValueOrDescriptionOrTransactionType().iterator(); it.hasNext();) {
			Object contentItem = (Object) it.next();
			if(contentItem instanceof JAXBElement){
				JAXBElement i = (JAXBElement)contentItem;

				//receiver-org - usually in AMP gov is the receiver
				if(i.getName().equals(new QName("receiver-org"))){
				}

				//provider-org - usually in AMP gov is the receiver
				if(i.getName().equals(new QName("provider-org"))){
					Transaction.ProviderOrg item = (Transaction.ProviderOrg)(i.getValue());
					ampOrg = getAmpOrganization(printList(item.getContent()), this.getLang(), item.getRef());
				}
				
				//disbursement-channel == mode of payment
				if(i.getName().equals(new QName("disbursement-channel"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					modeOfPayment = getAmpCategoryValue(item, DataExchangeConstants.IATI_DISBURSEMENT_CHANNEL,toIATIValues("disbursementChannelValue","disbursementChannelCode"),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"active");
				}
				
				//finance-type == type of assistance
				if(i.getName().equals(new QName("finance-type"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					typeOfAssistance = getAmpCategoryValue(item, DataExchangeConstants.IATI_FINANCE_TYPE,
							toIATIValues("financeTypeValue","financeTypeCode"),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"active");
					if(typeOfAssistance == null ) typeOfAssistance = iatiDefaultFinanceType;
				}
				
				//aid-type == financing instrument
				if(i.getName().equals(new QName("aid-type"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					financingInstrument = getAmpCategoryValue(item, DataExchangeConstants.IATI_AID_TYPE,
							toIATIValues("aidTypeValue","aidTypeCode"),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"active");
					if( financingInstrument == null) financingInstrument = iatiDefaultAidType;
				}
				
				//transaction-type
				if(i.getName().equals(new QName("transaction-type"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					String tName = printList(item.getValue().getContent()).toLowerCase();
					String tCode = item.getValue().getCode().toLowerCase();
					if("commitment".compareTo(tName) ==0 || "c".compareTo(tCode) ==0)
						transactionType ="c";
					if("commitment".compareTo(tName) ==0 || "c".compareTo(tCode) ==0)
						transactionType ="d";
					if("commitment".compareTo(tName) ==0 || "c".compareTo(tCode) ==0)
						transactionType ="e";
				}
				
				//transaction-date
				if(i.getName().equals(new QName("transaction-date"))){
					Transaction.TransactionDate item = (Transaction.TransactionDate)i.getValue();
					tDate = DataExchangeUtils.XMLGregorianDateToDate(item.getIsoDate());
				}
				
				//transaction description
				if(i.getName().equals(new QName("description"))){
					JAXBElement<TextType> item = (JAXBElement<TextType>)i;
					description = printList(item.getValue().getContent());
				}
				
				//value and currency
				if(i.getName().equals(new QName("value"))){
					CurrencyType item = (CurrencyType)i.getValue();
					currencyValue = new Double(item.getValue().doubleValue());
					if(isValidString(item.getCurrency()))
						currencyName = item.getCurrency();
				}
			}
		}
		AmpFunding ampFunding = new AmpFunding();
		Set<AmpFundingDetail> ampFundDetails = new HashSet<AmpFundingDetail>();

		ampFunding.setActive(true);
		ampFunding.setAmpDonorOrgId(ampOrg);
		if("d".compareTo(transactionType) ==0)
			populateFundingDetails(currencyValue, currencyName, tDate, ampFundDetails, org.digijava.module.aim.helper.Constants.DISBURSEMENT, org.digijava.module.aim.helper.Constants.ACTUAL);
		else
			if(("e".compareTo(transactionType) ==0))
				populateFundingDetails(currencyValue, currencyName, tDate, ampFundDetails, org.digijava.module.aim.helper.Constants.EXPENDITURE, org.digijava.module.aim.helper.Constants.ACTUAL);
			else
				populateFundingDetails(currencyValue, currencyName, tDate, ampFundDetails, org.digijava.module.aim.helper.Constants.COMMITMENT, org.digijava.module.aim.helper.Constants.ACTUAL);
		
		ampFunding.setFundingDetails(ampFundDetails);
		ampFunding.setTypeOfAssistance(typeOfAssistance);
		ampFunding.setFinancingInstrument(financingInstrument);
		ampFunding.setModeOfPayment(modeOfPayment);
		
		if(a !=null ) ampFunding.setAmpActivityId(a);
		Set<AmpFunding> ampFundings = a.getFunding();
		for (AmpFunding af : ampFundings) {
			if(ampFunding.getAmpDonorOrgId().compareTo(af.getAmpDonorOrgId()) == 0){
				ampFunding.setFinancingId(af.getFinancingId());
				ampFunding.setModeOfPayment(af.getModeOfPayment());
				ampFunding.setFundingStatus(af.getFundingStatus());
				ampFunding.setActualStartDate(af.getActualStartDate());
				ampFunding.setActualCompletionDate(af.getActualCompletionDate());
				ampFunding.setPlannedStartDate(af.getPlannedStartDate());
				ampFunding.setPlannedCompletionDate(af.getPlannedCompletionDate());
				ampFunding.setConditions(af.getConditions());
				ampFunding.setDonorObjective(af.getDonorObjective());
				break;
			}
		}
		
		//TODO: the language - lang attribute
		if(isValidString(description)) ampFunding.setConditions(description);
		
		AmpOrgRole ampOrgRole = new AmpOrgRole();
		ampOrgRole.setActivity(a);
		ampOrgRole.setRole(role);
		ampOrgRole.setOrganisation(ampOrg);
		orgRole.add(ampOrgRole);
		
		if (a.getOrgrole()==null){
			a.setOrgrole(orgRole);
		}else{
			a.getOrgrole().addAll(orgRole);
		}
		
		return ampFunding;
	}

	
	private void populateFundingDetails(Double currencyValue, String currencyCode, Date tDate, Set<AmpFundingDetail> fundDetails, int transactionType, int adjustmentType) {
			//senegal
			if(currencyValue.doubleValue()==0) return;
			
			AmpFundingDetail ampFundDet = new AmpFundingDetail();
			ampFundDet.setTransactionType(new Integer(transactionType));
			ampFundDet.setTransactionDate(tDate);
			ampFundDet.setAdjustmentType(new Integer(adjustmentType));
			ampFundDet.setAmpCurrencyId(CurrencyUtil.getCurrencyByCode(currencyCode));
			
			//TODO how are the amounts? in thousands?
			//if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)))
				//ampFundDet.setTransactionAmount(new Double(fundDet.getAmount()*1000));
			//else 
			ampFundDet.setTransactionAmount(new Double(currencyValue.doubleValue()));
			
			fundDetails.add(ampFundDet);
		
	}
	
	private void processActInternalIdsStep(AmpActivityVersion a, ArrayList<OtherIdentifier> iatiOtherIdList) {
		// TODO Auto-generated method stub
		if(iatiOtherIdList.isEmpty()) return;
		Set<AmpActivityInternalId> internalIds = new HashSet<AmpActivityInternalId>();
		for (Iterator<OtherIdentifier> it = iatiOtherIdList.iterator(); it.hasNext();) {
			OtherIdentifier otherIdentifier = (OtherIdentifier) it.next();
			AmpActivityInternalId actInternalId = new AmpActivityInternalId();
			actInternalId.setInternalId(otherIdentifier.getContent());
			actInternalId.setAmpActivity(a);
			
			String orgName = otherIdentifier.getOwnerName();
			String orgCode = otherIdentifier.getOwnerRef();
			AmpOrganisation org = getAmpOrganization(orgName,lang,orgCode);
			if(org != null)
				actInternalId.setOrganisation(org);
			internalIds.add(actInternalId);
		}
		
		if(a.getInternalIds() == null) a.setInternalIds(new HashSet<AmpActivityInternalId>());
		else a.getInternalIds().clear();
		a.getInternalIds().addAll(internalIds);
		
	}

	private void processRelOrgsStep(AmpActivityVersion a, ArrayList<ParticipatingOrg> iatiPartOrgList) {
		// TODO Auto-generated method stub
		if(iatiPartOrgList.isEmpty()) return;
		Set<AmpOrgRole> orgRole = new HashSet<AmpOrgRole>();
		for (Iterator<ParticipatingOrg> it = iatiPartOrgList.iterator(); it.hasNext();) {
			ParticipatingOrg participatingOrg = (ParticipatingOrg) it.next();
			
			
			AmpRole role = null;
			if("extending".compareTo(participatingOrg.getRole().toLowerCase()) == 0)
				role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.EXECUTING_AGENCY);
			if("implementing".compareTo(participatingOrg.getRole().toLowerCase()) == 0)
				role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.IMPLEMENTING_AGENCY);
			if("accountable".compareTo(participatingOrg.getRole().toLowerCase()) == 0)
					role=DbUtil.getAmpRole(org.digijava.module.aim.helper.Constants.RESPONSIBLE_ORGANISATION);

			if(role !=null){
				String orgName = printList(participatingOrg.getContent());
				String orgCode = participatingOrg.getRef();

				AmpOrganisation org = getAmpOrganization(orgName, lang, orgCode);
				AmpOrgRole ampOrgRole = new AmpOrgRole();
				ampOrgRole.setActivity(a);
				ampOrgRole.setRole(role);
				ampOrgRole.setOrganisation(org);
				orgRole.add(ampOrgRole);
			}
			
		}
		
		if (a.getOrgrole()==null) a.setOrgrole(orgRole);
			else a.getOrgrole().addAll(orgRole);
		
	}

	private void processSectorsStep(AmpActivityVersion a, ArrayList<Sector> iatiSectorList, ArrayList<AmpClassificationConfiguration> allClassificationConfiguration) {
		if(iatiSectorList.isEmpty()) return;
		Set<AmpActivitySector> sectors = new HashSet<AmpActivitySector>();
		Long sectorId;
		for (Iterator<Sector> it = iatiSectorList.iterator(); it.hasNext();) {
			Sector sector = (Sector) it.next();
			String sectorName = printList(sector.getContent());
			String vocabulary = sector.getVocabulary()==null?"DAC":sector.getVocabulary();
			DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_SECTOR,toIATIValues("vocabularyName","sectorName","sectorCode"),toIATIValues(vocabulary,sectorName,sector.getCode()),this.getLang(),null,AmpSector.class.getName(),null,null,"active");
			
			AmpSector ampSector = SectorUtil.getAmpSector(checkMappedField.getAmpId());
			
			AmpActivitySector ampActSector = new AmpActivitySector();
			ampActSector.setActivityId(a);
			sectorId = ampSector.getAmpSectorId();
			if (sectorId != null && (!sectorId.equals(new Long(-1))))
				ampActSector.setSectorId(ampSector);
			ampActSector.setSectorPercentage(new Float(sector.getPercentage().floatValue()));
			
			AmpClassificationConfiguration primConf = null;
			//trying to find the classification
			primConf = getConfiguration(ampSector,allClassificationConfiguration);
			//if the classification doesn't exist we will put the sector under the primary classification!
			if(primConf == null)	primConf = getPrimaryClassificationConfiguration(allClassificationConfiguration);
			
			ampActSector.setClassificationConfig(primConf);
            sectors.add(ampActSector);

		}
	}

	private void processPlanningStep(AmpActivityVersion a, ArrayList<ActivityDate> iatiActDateList) {
		if(iatiActDateList.isEmpty()) return;
		for (Iterator<ActivityDate> it = iatiActDateList.iterator(); it.hasNext();) {
			ActivityDate date = (ActivityDate) it.next();
			
			//Proposed Start Date
			if("start-planned".compareTo(date.getType()) ==0 ){
				a.setProposedStartDate(DataExchangeUtils.XMLGregorianDateToDate(date.getIsoDate()));
			}
			//Date of Planned Completion
			if("end-planned".compareTo(date.getType()) ==0 ){
				a.setOriginalCompDate(DataExchangeUtils.XMLGregorianDateToDate(date.getIsoDate()));
			}
			//Date of Effective Agreement
			if("start-actual".compareTo(date.getType()) ==0 ){
				a.setActualStartDate(DataExchangeUtils.XMLGregorianDateToDate(date.getIsoDate()));
			}
			//Date of Actual Completion
			if("end-actual".compareTo(date.getType()) ==0 ){
				a.setActualCompletionDate(DataExchangeUtils.XMLGregorianDateToDate(date.getIsoDate()));
			}
		}
	}

	private void processIdentificationStep(AmpActivityVersion a, ArrayList<JAXBElement<CodeType>> iatiStatusList, ArrayList<Description> iatiDescriptionList, IatiIdentifier iatiIDList) {
		processStatus(a,iatiStatusList);
		processDescriptions(a,iatiDescriptionList);
		processIatiID(a,iatiIDList);
		
	}

	private void processIatiID(AmpActivityVersion a, IatiIdentifier iatiIDList) {
		//TODO process the iati-id
		a.setProjectCode(iatiIDList.getContent());
	}

	private void processDescriptions(AmpActivityVersion a, ArrayList<Description> iatiDescriptionList) {
		for (Iterator it = iatiDescriptionList.iterator(); it.hasNext();) {
			Description description = (Description) it.next();
			if("general".compareTo(description.getType().toLowerCase()) ==0 ){
				String d = setEditorDescription(description , "amp-iati-desc-");
				if(this.getLang().compareTo(description.getLang()) == 0)
					a.setDescription(d);
			}
			if("objectives".compareTo(description.getType().toLowerCase()) ==0 ){
				String d = setEditorDescription(description , "amp-iati-obj-");
				if(this.getLang().compareTo(description.getLang()) == 0)
					a.setObjective(d);
			}
		}
		
	}

	private String setEditorDescription(Description obj , String preKey){
			String key = preKey + System.currentTimeMillis();
			String value = printList(obj.getContent());
			if(obj!=null && isValidLanguage(obj.getLang()) && isValidString(value)){
				Editor ed = createEditor("amp", key, obj.getLang());
				ed.setLastModDate(new Date());
				ed.setGroupName(org.digijava.module.editor.util.Constants.GROUP_OTHER);
				ed.setBody(value);
				try {
					org.digijava.module.editor.util.DbUtil.saveEditor(ed);
				} catch (EditorException e) {
					e.printStackTrace();
				}
				return key;
			}
			
		return null;
	}
	
	private Editor createEditor(String siteId, String key, String language){
		Editor editor = new Editor();
		editor.setSiteId(siteId);
		editor.setEditorKey(key);
		editor.setLanguage(language);
		return editor;
	}
	
	private boolean isValidLanguage(String lang){
		//TODO: validate lang based on dg_locale code for languages
		if(isValidString(lang) && lang.length() == 2 )
			return true;
		return false;
		
	}
	
	
	private void processStatus(AmpActivityVersion activity, ArrayList<JAXBElement<CodeType>> iatiStatusList) {
		// TODO Auto-generated method stub
		if(iatiStatusList.isEmpty()) return;
		JAXBElement<CodeType> item = getElementByLang(iatiStatusList,this.getLang());
		if(item!=null)
		{
			String code = getAttributeCodeType(item, "code");
			String value = printCodeType(item);
			DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ACTIVITY_STATUS,toIATIValues("statusName","statusCode"),toIATIValues(value,code),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"active");
			//we are sure that activity id is not null - activity is mapped
			AmpCategoryValue acv = CategoryManagerUtil.getAmpCategoryValueFromDb(checkMappedField.getAmpId());
			if (activity.getCategories() == null) {
				activity.setCategories( new HashSet() );
			}
			for (Iterator it = activity.getCategories().iterator(); it.hasNext();) {
				AmpCategoryValue acvAux = (AmpCategoryValue) it.next();
				if(org.digijava.module.dataExchange.utils.Constants.CATEG_VALUE_ACTIVITY_STATUS.equals(acvAux.getAmpCategoryClass().getKeyName()))
					it.remove();
			}
			if(acv!=null)
				activity.getCategories().add(acv);
		}
	}

	private JAXBElement<CodeType> getElementByLang(ArrayList<JAXBElement<CodeType>> iatiStatusList, String lang) {
		// TODO Auto-generated method stub
		boolean isUnique = false;
		if(iatiStatusList.size() == 1)
			isUnique = true;
		for (Iterator it = iatiStatusList.iterator(); it.hasNext();) {
			JAXBElement<CodeType> je = (JAXBElement<CodeType>) it.next();
			if(isUnique) return je;
			if( je.getValue() !=null && je.getValue().getLang()!=null && lang.compareTo(je.getValue().getLang()) ==0 ) return je;
		}
		return null;
	}

	//populate the lists with IATI values of the activity
	private void extractIATIEntities(ArrayList<JAXBElement<TextType>> iatiTitleList,
			ArrayList<JAXBElement<CodeType>> iatiStatusList,
			ArrayList<ParticipatingOrg> iatiPartOrgList,
			ArrayList<ReportingOrg> iatiRepOrgList,
			ArrayList<Sector> iatiSectorList,
			ArrayList<Transaction> iatiTransactionList,
			ArrayList<Description> iatiDescriptionList,
			ArrayList<OtherIdentifier> iatiOtherIdList,
			ArrayList<ActivityDate> iatiActDateList,
			ArrayList<ContactInfo> iatiContactList,
			ArrayList<Location> iatiLocationList, JAXBElement<CodeReqType> iatiDefaultFinanceType, 
	JAXBElement<CodeReqType> iatiDefaultAidType, IatiIdentifier iatiID) {
		
		
		
		for (Iterator it = this.getiActivity().getActivityWebsiteOrReportingOrgOrParticipatingOrg().iterator(); it.hasNext();) {
			Object contentItem = (Object) it.next();
			if(contentItem instanceof JAXBElement){
				JAXBElement i = (JAXBElement)contentItem;

				//title
				if(i.getName().equals(new QName("title"))){
					JAXBElement<TextType> item = (JAXBElement<TextType>)i;
					System.out.println("activity title:" + printTextType(item)+"#");
					iatiTitleList.add(item);
				}
				
				//status
				if(i.getName().equals(new QName("activity-status"))){
					JAXBElement<CodeType> item = (JAXBElement<CodeType>)i;
					iatiStatusList.add(item);
				}

				//default-finance-type == type of assistance
				if(i.getName().equals(new QName("default-finance-type"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					iatiDefaultFinanceType = item;
				}

				//default-aid-type == financing instrument
				if(i.getName().equals(new QName("default-aid-type"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					iatiDefaultAidType = item;
				}
				
			}
			if(contentItem instanceof OtherIdentifier){
				OtherIdentifier item = (OtherIdentifier)contentItem;
				iatiOtherIdList.add(item);
			}
			
			if(contentItem instanceof IatiIdentifier){
				IatiIdentifier item = (IatiIdentifier)contentItem;
				iatiID = item;
			}
			
			if(contentItem instanceof ActivityDate){
				ActivityDate item = (ActivityDate)contentItem;
				iatiActDateList.add(item);
			}
			
			if(contentItem instanceof Description){
				Description item = (Description)contentItem;
				iatiDescriptionList.add(item);
			}
			
			if(contentItem instanceof ContactInfo){
				ContactInfo item = (ContactInfo)contentItem;
				iatiContactList.add(item);
			}
			
			if(contentItem instanceof Location){
				Location item = (Location)contentItem;
				iatiLocationList.add(item);
			}
			
			if(contentItem instanceof ReportingOrg){
				ReportingOrg item = (ReportingOrg)contentItem;
				iatiRepOrgList.add(item);
			}
			
			if(contentItem instanceof ParticipatingOrg){
				ParticipatingOrg item = (ParticipatingOrg)contentItem;
				iatiPartOrgList.add(item);
			}
			
			if(contentItem instanceof Sector){
				Sector item = (Sector)contentItem;
				iatiSectorList.add(item);
			}
			
			if(contentItem instanceof Transaction){
				Transaction item = (Transaction)contentItem;
				iatiTransactionList.add(item);
			}
		}
	}


	private AmpMappedField checkStatusCode(JAXBElement<CodeType> item) {
		// TODO Auto-generated method stub
		String code = getAttributeCodeType(item, "code");
		String value = printCodeType(item);
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ACTIVITY_STATUS,toIATIValues("statusName","statusCode"),toIATIValues(value,code),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"inactive");
		AmpMappedField log = new AmpMappedField(checkMappedField);
		logMappingField(DataExchangeConstants.IATI_ACTIVITY_STATUS,toIATIValues("statusName","statusCode"),toIATIValues(value,code),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"inactive", checkMappedField, log);
		return log;
	}


	private AmpMappedField checkOrganization(String content, String lang, String ref) {
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ORGANIZATION,toIATIValues("organizationName","organizationCode"),toIATIValues(content,ref),lang,null,AmpOrganisation.class.getName(),null,null,"inactive");
		AmpMappedField log = new AmpMappedField(checkMappedField);
		logMappingField(DataExchangeConstants.IATI_ORGANIZATION,toIATIValues("organizationName","organizationCode"),toIATIValues(content,ref),lang,null,AmpOrganisation.class.getName(),null,null,"inactive", checkMappedField, log);
		return log;
	}
	
	private AmpMappedField checkActivity(String title, String iatiID, String lang) {
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ACTIVITY,toIATIValues("activityName","iatiID"),toIATIValues(title,iatiID),lang,null,AmpActivityGroup.class.getName(),null,null,"inactive");
		AmpMappedField log = new AmpMappedField(checkMappedField);
		logMappingField(DataExchangeConstants.IATI_ACTIVITY,toIATIValues("activityName","iatiID"),toIATIValues(title,iatiID),lang,null,AmpActivityGroup.class.getName(),null,null,"inactive",checkMappedField, log);
		return log;
	}
	
	private AmpOrganisation getAmpOrganization(String orgName, String lang, String orgCode){
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ORGANIZATION,toIATIValues("organizationName","organizationCode"),toIATIValues(orgName,orgCode),lang,null,AmpOrganisation.class.getName(),null,null,"inactive");
		AmpOrganisation org = (AmpOrganisation) DataExchangeUtils.getOrganizationById(checkMappedField.getAmpId());
		return org;
	}
	
	private AmpLocation getAmpLocation(String iatiItems, String iatiValues){
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_LOCATION,iatiItems,iatiValues,lang,null,AmpCategoryValueLocations.class.getName(),null,null,"active");
		AmpCategoryValueLocations ampCVLoc = DynLocationManagerUtil.getLocationByIdRequestSession(checkMappedField.getAmpId());
		AmpLocation ampLoc = null;
		try {
			ampLoc = DynLocationManagerUtil.getAmpLocation(ampCVLoc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ampLoc;
	}
	
	private AmpMappedField checkLocation(Location l) {
		
		String locationType	= null;
		String locationName	= null;
		String locationCountry = null;
		String adm1 = null;
		String adm2 = null;
		String adm3 = null;

		getLocationDetails(l,locationName,locationType,locationCountry,adm1,adm2,adm3);
		
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_LOCATION,toIATIValues("locationName","locationType","locationCountry","adm1","adm2","adm3"),
				toIATIValues(locationName,locationType,locationCountry,adm1,adm2,adm3),lang,null,AmpCategoryValueLocations.class.getName(),null,null,"inactive");
		AmpMappedField log = new AmpMappedField(checkMappedField);
		logMappingField(DataExchangeConstants.IATI_LOCATION,toIATIValues("locationName","locationType","locationCountry","adm1","adm2","adm3"),
				toIATIValues(locationName,locationType,locationCountry,adm1,adm2,adm3),lang,null,AmpCategoryValueLocations.class.getName(),null,null,"inactive", checkMappedField, log);
		return log;
	}
	

	private void getLocationDetails(Location l,String locationName, String locationType,
			String locationCountry, String adm1, String adm2, String adm3) {

		for (Iterator<Object> it = l.getLocationTypeOrNameOrDescription().iterator(); it.hasNext();) {
			Object contentItem = (Object) it.next();
			if(contentItem instanceof JAXBElement){
				JAXBElement i = (JAXBElement)contentItem;

				//location-type
				if(i.getName().equals(new QName("location-type"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					locationType = item.getValue().getCode();
				}
				//location name
				if(i.getName().equals(new QName("name"))){
					JAXBElement<TextType> item = (JAXBElement<TextType>)i;
					locationName = printList(item.getValue().getContent());
				}
				
				//administrative
				if(i.getName().equals(new QName("administrative"))){
					Location.Administrative item = (Location.Administrative)i.getValue();
					locationCountry = item.getCountry();
					adm1 = item.getAdm1();
					adm2 = item.getAdm2();
					adm3 = item.getOtherAttributes().get(new QName("adm3"));
				}
				
			}
		}
		
	}

	private AmpMappedField checkOrganizationType(String type) {
		if(type==null) return null;
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ORGANIZATION_TYPE,"organization_code_type",type,this.getLang(),null,AmpOrgType.class.getName(),null,null,"inactive");
		AmpMappedField log = new AmpMappedField(checkMappedField);
		logMappingField(DataExchangeConstants.IATI_ORGANIZATION_TYPE,"organization_code_type",type,this.getLang(),null,AmpOrgType.class.getName(),null,null,"inactive",checkMappedField,log);
		return log;
	}
	
	private AmpMappedField checkVocabularyCode(Sector item) {
		DEMappingFields mf = null;
		String name = item.getVocabulary()==null?"DAC":item.getVocabulary();
		mf = checkMappedField(DataExchangeConstants.IATI_VOCABULARY_CODE,"sector_vocabulary_code",name,this.getLang(),null,AmpSectorScheme.class.getName(),null,null,"inactive");
		AmpMappedField log = new AmpMappedField(mf);
		logMappingField(DataExchangeConstants.IATI_VOCABULARY_CODE,"sector_vocabulary_code",name,this.getLang(),null,AmpSectorScheme.class.getName(),null,null,"inactive",mf,log);
		return log;
	}
	
	private AmpMappedField checkSector(Sector sector) {
		String sectorName = printList(sector.getContent());
		String vocabulary = sector.getVocabulary()==null?"DAC":sector.getVocabulary();
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_SECTOR,toIATIValues("vocabularyName","sectorName","sectorCode"),toIATIValues(vocabulary,sectorName,sector.getCode()),this.getLang(),null,AmpSector.class.getName(),null,null,"inactive");
		AmpMappedField log = new AmpMappedField(checkMappedField);
		logMappingField(DataExchangeConstants.IATI_SECTOR,toIATIValues("vocabularyName","sectorName","sectorCode"),toIATIValues(vocabulary,sectorName,sector.getCode()),this.getLang(),null,AmpSector.class.getName(),null,null,"inactive",checkMappedField,log);
		return log;
	}
	
	//mapped to Type of Assistance
	private AmpMappedField checkFinanceType(JAXBElement<CodeReqType> item) {
		return checkCodeReqType(item, DataExchangeConstants.IATI_FINANCE_TYPE,toIATIValues("financeTypeValue","financeTypeCode"),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"inactive");
	}
	
	
	
	//Financing Instrument
	private AmpMappedField checkAidType(JAXBElement<CodeReqType> item) {
		return checkCodeReqType(item, DataExchangeConstants.IATI_AID_TYPE,toIATIValues("aidTypeValue","aidTypeCode"),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"inactive");
	}
	
	//Mode of Payment
	private AmpMappedField checkDisbursementChannel(JAXBElement<CodeReqType> item) {
		return checkCodeReqType(item, DataExchangeConstants.IATI_DISBURSEMENT_CHANNEL,toIATIValues("disbursementChannelValue","disbursementChannelCode"),this.getLang(),null,AmpCategoryValue.class.getName(),null,null,"inactive");
	}
	
	private boolean checkIATITransaction(Transaction t, ArrayList<AmpMappedField> logs) {
		// TODO Auto-generated method stub
		
		for (Iterator it = t.getValueOrDescriptionOrTransactionType().iterator(); it.hasNext();) {
			Object contentItem = (Object) it.next();
			if(contentItem instanceof JAXBElement){
				JAXBElement i = (JAXBElement)contentItem;

				//flow-type, tied-status
				//TODO
				
				//receiver-org - usually in AMP gov is the receiver
				if(i.getName().equals(new QName("receiver-org"))){
					Transaction.ReceiverOrg item = (Transaction.ReceiverOrg)(i.getValue());
					AmpMappedField existReceiverOrg = checkOrganization(printList(item.getContent()), this.getLang(),item.getRef());
					//TODO logging
					logs.add(existReceiverOrg);
				}

				//provider-org - usually in AMP gov is the receiver
				if(i.getName().equals(new QName("provider-org"))){
					Transaction.ProviderOrg item = (Transaction.ProviderOrg)(i.getValue());
					AmpMappedField existReceiverOrg = checkOrganization(printList(item.getContent()), this.getLang(),item.getRef());
					//TODO logging
					logs.add(existReceiverOrg);
				}
				
				//disbursement-channel == mode of payment
				if(i.getName().equals(new QName("disbursement-channel"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					AmpMappedField existDisbursementChannel = checkDisbursementChannel(item);
					//TODO logging
					logs.add(existDisbursementChannel);
				}
				
				//finance-type == type of assistance
				if(i.getName().equals(new QName("finance-type"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					AmpMappedField existFinanceType = checkFinanceType(item);
					//TODO logging
					logs.add(existFinanceType);
				}
				
				//aid-type == financing instrument
				if(i.getName().equals(new QName("aid-type"))){
					JAXBElement<CodeReqType> item = (JAXBElement<CodeReqType>)i;
					AmpMappedField existAidType = checkAidType(item);
					logs.add(existAidType);
				}
				
			}
		}
		
		
		return false;
	}

	
	private AmpMappedField checkCodeReqType(JAXBElement<CodeReqType> item, String iatiPath, String iatiItems,
			String iatiLang, Long ampId, String ampClass,
			Long sourceId, String feedFileName, String status) {
		String code = getAttributeCodeReqType(item, "code");
		String value = printCodeReqType(item);
		DEMappingFields checkMappedField = checkMappedField(iatiPath,iatiItems,toIATIValues(value,code),iatiLang,ampId,ampClass,sourceId,feedFileName,status);
		AmpMappedField log = new AmpMappedField(checkMappedField);
		logMappingField(iatiPath,iatiItems,toIATIValues(value,code),iatiLang,ampId,ampClass,sourceId,feedFileName,status, checkMappedField, log);
		return log;
	}
	private AmpCategoryValue getAmpCategoryValue(JAXBElement<CodeReqType> item, String iatiPath, String iatiItems,
			String iatiLang, Long ampId, String ampClass,
			Long sourceId, String feedFileName, String status){
		String code = getAttributeCodeReqType(item, "code");
		String value = printCodeReqType(item);
		DEMappingFields checkMappedField = checkMappedField(iatiPath,iatiItems,toIATIValues(value,code),iatiLang,ampId,ampClass,sourceId,feedFileName,status);
		AmpCategoryValue acv = (AmpCategoryValue) CategoryManagerUtil.getAmpCategoryValueFromDb(checkMappedField.getAmpId());
		return acv;
	}

	private void logMappingField(String iatiPath, String iatiItems,
			String iatiValues, String iatiLang, Long ampId, String ampClass,
			Long sourceId, String feedFileName, String status,
			DEMappingFields checkMappedField, AmpMappedField log) {
		if(checkMappedField==null)
			{
				checkMappedField = addMappingField(iatiPath,iatiItems,iatiValues,iatiLang,ampId,ampClass,sourceId,feedFileName,status);
				log.add(iatiItems);
				log.add(iatiValues);
			}
		else{
			if(checkMappedField.getAmpId()==null)
			{
				log.add(iatiItems);
				log.add(iatiValues);
			}
		}
	}
	
	private String getAttributeCodeType(JAXBElement<CodeType> item, String key) {
		Map<QName, String> otherAttributes = item.getValue().getOtherAttributes();
		String code = otherAttributes.get(new QName(key));
		return code;
	}
	
	private String getAttributeCodeReqType(JAXBElement<CodeReqType> item, String key) {
		Map<QName, String> otherAttributes = item.getValue().getOtherAttributes();
		String code = otherAttributes.get(new QName(key));
		return code;
	}
	
	public String printTextType(JAXBElement<TextType> item){
		String result = "";
		result += printList(item.getValue().getContent());
		return result;
	}
	
	public String printCodeType(JAXBElement<CodeType> item){
		String result = "";
		result +=item.getValue().getCode()+" ";
		result += printList(item.getValue().getContent());
		return result;
	}
	
	public String printCodeReqType(JAXBElement<CodeReqType> item){
		String result = "";
		result +=item.getValue().getCode()+" ";
		result += printList(item.getValue().getContent());
		return result;
	}
	
	public String printList(List<Object> items){
		String result = "";
		for (Iterator it = items.iterator(); it.hasNext();) {
			Object o = (Object) it.next();
			if(o instanceof String){
				result +=(String)o;
			}
		}
		result = result.replace("\n","");
		result = result.replaceAll("\\s+", " ");
		return result;
	}
	
	private String toIATIValues(String a, String b){
		return a+"|||"+b;
	}

	private String toIATIValues(String a, String b, String c){
		return a+"|||"+b+"|||"+c;
	}

	private String toIATIValues(String a, String b, String c, String d, String e){
		return toIATIValues(a,b)+"|||"+toIATIValues(c,d,e);
	}
	
	private String toIATIValues(String a, String b, String c, String d, String e, String f){
		return toIATIValues(a,b,c)+"|||"+toIATIValues(d,e,f);
	}

	
	private DEMappingFields checkMappedField(String iatiPath, String iatiItems,
			String iatiValues, String iatiLang, Long ampId, String ampClass,
			Long sourceId, String feedFileName, String status) {
		Collection<DEMappingFields> allAmpDEMappingFields = DataExchangeUtils.getAllAmpDEMappingFields();
		DEMappingFields mf = new DEMappingFields(iatiPath, iatiItems, iatiValues, iatiLang==null?this.getLang():iatiLang, ampId, ampClass.toString(), sourceId, feedFileName, status);
		for (Iterator ot = allAmpDEMappingFields.iterator(); ot.hasNext();) {
			DEMappingFields deMappingFields = (DEMappingFields) ot.next();
			if(mf.compare(deMappingFields)) 
				return deMappingFields;
		}
		return null;
	}

	private DEMappingFields addMappingField(String iatiPath, String iatiItems,
			String iatiValues, String iatiLang, Long ampId, String ampClass,
			Long sourceId, String feedFileName, String status) {
		DEMappingFields mf = new DEMappingFields(iatiPath, iatiItems, iatiValues, iatiLang, ampId, ampClass.toString(), sourceId, feedFileName, status);
		DataExchangeUtils.insertDEMappingField(mf);
		return mf;
	}
	
	
	private AmpCategoryValue getAmpCategoryValue(String value, String code, String categoryKey ){
		AmpCategoryValue acv=null;
		Collection<AmpCategoryValue> allCategValues;
		String valueToCateg="";
		
		allCategValues = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(categoryKey);
		
		if( isValidString(value) ) 	valueToCateg=value;
		else if( isValidString(code) ) valueToCateg=code;

		if(valueToCateg == null || "".equals(valueToCateg.trim()) ) return null;
		
		for (Iterator itacv = allCategValues.iterator(); itacv.hasNext();) {
			acv = (AmpCategoryValue) itacv.next();
			if(acv.getValue().equals(valueToCateg)) return acv;
		}
		return null;
	}
	
	private AmpClassificationConfiguration getConfiguration(AmpSector ampSector, ArrayList<AmpClassificationConfiguration> allClassifConfigs) {
		// TODO Auto-generated method stub
		//allClassifConfigs = (ArrayList<AmpClassificationConfiguration>) getAllClassificationConfiguration();
		for (Iterator<AmpClassificationConfiguration> it2 = allClassifConfigs.iterator(); it2.hasNext();) {
			AmpClassificationConfiguration acc = (AmpClassificationConfiguration) it2.next();
			if(acc.getClassification().getAmpSecSchemeId().equals(ampSector.getAmpSecSchemeId().getAmpSecSchemeId()))
					return acc;
		}
		return null;
	}

	private AmpClassificationConfiguration getPrimaryClassificationConfiguration(ArrayList<AmpClassificationConfiguration> allClassifConfigs){
    	if(allClassifConfigs!=null){
        	Iterator<AmpClassificationConfiguration> it = allClassifConfigs.iterator();        	
        	while(it.hasNext()){
        		AmpClassificationConfiguration conf = it.next();        		
				if(conf.isPrimary()) return conf;
        	}
    	}
    	return null;
	}	

	//get all Amp Classification Configuration
	private List<AmpClassificationConfiguration> getAllClassificationConfiguration(){
    	List<AmpClassificationConfiguration> configs = new ArrayList();
		try {
			configs = SectorUtil.getAllClassificationConfigs();
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return configs;
	}
	
	
	private AmpCategoryValue getAmpCategoryValueByString(String element, String categoryKey ){
		if( !isValidString(element) ) return null; 
		AmpCategoryValue acv=null;
		Collection<AmpCategoryValue> allCategValues;
		
		allCategValues = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(categoryKey);
		
		
		for (Iterator itacv = allCategValues.iterator(); itacv.hasNext();) {
			acv = (AmpCategoryValue) itacv.next();
			if(acv.getValue().compareTo(element) == 0) return acv;
		}
		return null;
	}
	
	//****************************************** Other methods ******************************
	

	private boolean isValidString(String s ){
		if(s != null && !"".equals(s.trim()))
			return true;
		return false;
		
	}
	

}
