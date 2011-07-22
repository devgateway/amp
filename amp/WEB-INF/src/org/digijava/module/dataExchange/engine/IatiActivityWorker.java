/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.util.DataExchangeConstants;
import org.digijava.module.dataExchange.utils.Constants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.IatiActivity;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.ParticipatingOrg;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.ReportingOrg;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.Sector;
import org.digijava.module.dataExchangeIATI.iatiSchema.jaxb.TextType;

/**
 * @author dan
 *
 */
public class IatiActivityWorker {

	/**
	 * 
	 */
	private IatiActivity iActivity;
	private String lang;
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
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

	public boolean checkifActivityExists() {
		// TODO Auto-generated method stub
		boolean actExist = false;
		
		return actExist;
	}

	public void checkContent() {
		// TODO Auto-generated method stub
		
		for (Iterator it = this.getiActivity().getActivityWebsiteOrReportingOrgOrParticipatingOrg().iterator(); it.hasNext();) {
			Object contentItem = (Object) it.next();
			if(contentItem instanceof JAXBElement){
				JAXBElement<TextType> item = (JAXBElement<TextType>)contentItem;

				//title
				if(item.getName().equals(new QName("title"))){
					System.out.println("activity title:" + print(item)+"#");
				}
				//status
				if(item.getName().equals(new QName("activity-status"))){
					checkJAXBAddACV(item,"code",Constants.CATEG_VALUE_ACTIVITY_STATUS, DataExchangeConstants.IATI_ACTIVITY_STATUS,"statusName|||statusCode",this.getLang(),null,AmpCategoryValue.class,null,null,"inactive");
					//TODO logging
				}
				
			}
			
			if(contentItem instanceof ReportingOrg){
				ReportingOrg item = (ReportingOrg)contentItem;
				System.out.println("reporting-org" + printList(item.getContent()));
				boolean existOrganizationType = checkOrganizationType(item.getType());
				if(!existOrganizationType)
					//TODO log the exception!
					;
				checkOrganization(printList(item.getContent()),item.getLang(), item.getRef());
			}
			
			if(contentItem instanceof ParticipatingOrg){
				ParticipatingOrg item = (ParticipatingOrg)contentItem;
				System.out.println("participating-org" + printList(item.getContent()));
				boolean existOrganizationType = checkOrganizationType(item.getType());
				if(!existOrganizationType)
					//TODO log the exception!
					;
				checkOrganization(printList(item.getContent()),item.getLang(), item.getRef());
			}
			
			if(contentItem instanceof Sector){
				Sector item = (Sector)contentItem;
				System.out.println("sector" + printList(item.getContent()));
				Collection<AmpSectorScheme> allSectorSchemes = DataExchangeUtils.getAllSectorSchemes();
				DEMappingFields existVocabularyCode = checkVocabularyCode(item.getVocabulary(),allSectorSchemes);
				if(existVocabularyCode !=null)
					//TODO log the exception!
					;
				checkSector(printList(item.getContent()),item.getLang(), item.getCode(), item.getVocabulary(), existVocabularyCode, allSectorSchemes);
			}
		}
	}

	private void checkJAXBAddACV(JAXBElement<TextType> item,String attribute, String acvKey,String iatiPath, String iatiItems,
			String iatiLang, Long ampId, Class ampClass,
			Long sourceId, String feedFileName, String status) {
		String code = getAttributeJAXBElement(item, attribute);
		String value = print(item);
		AmpCategoryValue acv = getAmpCategoryValue(value,code, acvKey);
		if(acv==null) 
			addMappingField(iatiPath,iatiItems,value+"|||"+code,this.getLang(),null,ampClass,null,null,status);
	}

	private String getAttributeJAXBElement(JAXBElement<TextType> item, String key) {
		Map<QName, String> otherAttributes = item.getValue().getOtherAttributes();
		String code = otherAttributes.get(new QName(key));
		return code;
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
	
	private boolean isValidString(String s ){
		if(s != null && !"".equals(s.trim()))
			return true;
		return false;
		
	}
	
	private boolean checkOrganization(String content, String lang, String ref) {
		Collection<AmpOrganisation> allOrganizations = DataExchangeUtils.getAllOrganizations();
		boolean ok = true;
		for (Iterator iterator = allOrganizations.iterator(); iterator.hasNext();) {
			AmpOrganisation ampOrganisation = (AmpOrganisation) iterator.next();
			if(ampOrganisation.getName().compareTo(content) == 0 && ampOrganisation.getOrgCode().compareTo(ref) == 0)
				return ok;
		}
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ORGANIZATION,"organizationName|||organizationCode",content+"|||"+ref,this.getLang(),null,AmpOrganisation.class,null,null,"inactive");
		if(checkMappedField!=null)
			addMappingField(DataExchangeConstants.IATI_ORGANIZATION,"organizationName|||organizationCode",content+"|||"+ref,this.getLang(),null,AmpOrganisation.class,null,null,"inactive");
		return !ok;
	}

	private boolean checkOrganizationType(String type) {
		boolean ok = true;
		Collection<AmpOrgType> allOrgTypes = DbUtil.getAllOrgTypes();
		for (Iterator<AmpOrgType> iterator = allOrgTypes.iterator(); iterator.hasNext();) {
			AmpOrgType ampOrgType = (AmpOrgType) iterator.next();
			if(type.compareTo(ampOrgType.getOrgType()) == 0) return ok;
		}
		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_ORGANIZATION_TYPE,"organization_code_type",type,this.getLang(),null,AmpOrgType.class,null,null,"inactive");
		if(checkMappedField!=null)
			addMappingField(DataExchangeConstants.IATI_ORGANIZATION_TYPE,"organization_code_type",type,this.getLang(),null,AmpOrgType.class,null,null,"inactive");
		return !ok;
	}
	
	private boolean checkSector(String content, String lang, String code, String vocabularyName, DEMappingFields mappedVocabulary, Collection<AmpSectorScheme> allSectorSchemes) {
		
		AmpSectorScheme ampSS = null;
		boolean ok = true;
		if(mappedVocabulary == null) {
			for (Iterator<AmpSectorScheme> iterator = allSectorSchemes.iterator(); iterator.hasNext();) {
				ampSS = (AmpSectorScheme) iterator.next();
				if(vocabularyName.compareTo(ampSS.getSecSchemeName()) == 0) break;
			}
			List<AmpSector> allSectorsFromScheme = SectorUtil.getAllSectorsFromScheme(ampSS.getAmpSecSchemeId());
			for (Iterator<AmpSector> it = allSectorsFromScheme.iterator(); it.hasNext();) {
				AmpSector ampSector = (AmpSector) it.next();
				if(ampSector.getName().compareTo(content) == 0 && ampSector.getSectorCodeOfficial().compareTo(code) == 0) return ok;
			}
		}
		//it is a mapped sector scheme
		else{
			if(mappedVocabulary.getAmpId()!=null) {
				for (Iterator<AmpSectorScheme> iterator = allSectorSchemes.iterator(); iterator.hasNext();) {
					ampSS = (AmpSectorScheme) iterator.next();
					if(mappedVocabulary.getAmpId().compareTo(mappedVocabulary.getAmpId()) == 0) break;
				}
				List<AmpSector> allSectorsFromScheme = SectorUtil.getAllSectorsFromScheme(ampSS.getAmpSecSchemeId());
				for (Iterator<AmpSector> it = allSectorsFromScheme.iterator(); it.hasNext();) {
					AmpSector ampSector = (AmpSector) it.next();
					if(ampSector.getName().compareTo(content) == 0 && ampSector.getSectorCodeOfficial().compareTo(code) == 0) return ok;
				}
			}
			else {
				DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_SECTOR,toIATIValues("vocabularyName","sectorName","sectorCode"),toIATIValues(vocabularyName,content,code),this.getLang(),null,AmpSector.class,null,null,"inactive");
				if(checkMappedField !=null)
					addMappingField(DataExchangeConstants.IATI_SECTOR,toIATIValues("vocabularyName","sectorName","sectorCode"),toIATIValues(vocabularyName,content,code),this.getLang(),null,AmpSector.class,null,null,"inactive");
			}
		}

		DEMappingFields checkMappedField = checkMappedField(DataExchangeConstants.IATI_SECTOR,toIATIValues("vocabularyName","sectorName","sectorCode"),toIATIValues(vocabularyName,content,code),this.getLang(),null,AmpSector.class,null,null,"inactive");
		if(checkMappedField !=null)
			addMappingField(DataExchangeConstants.IATI_SECTOR,toIATIValues("vocabularyName","sectorName","sectorCode"),toIATIValues(vocabularyName,content,code),this.getLang(),null,AmpSector.class,null,null,"inactive");
		
		return !ok;
	}
	
	private DEMappingFields checkVocabularyCode(String name, Collection<AmpSectorScheme> allSectorSchemes) {
		boolean ok = true;
		DEMappingFields mf = null;
		
		for (Iterator<AmpSectorScheme> iterator = allSectorSchemes.iterator(); iterator.hasNext();) {
			AmpSectorScheme ampSS = (AmpSectorScheme) iterator.next();
			if(name.compareTo(ampSS.getSecSchemeName()) == 0) return mf;
		}
		mf = checkMappedField(DataExchangeConstants.IATI_VOCABULARY_CODE,"sector_vocabulary_code",name,this.getLang(),null,AmpSectorScheme.class,null,null,"inactive");
		if(mf != null)
			mf = addMappingField(DataExchangeConstants.IATI_VOCABULARY_CODE,"sector_vocabulary_code",name,this.getLang(),null,AmpSectorScheme.class,null,null,"inactive");
		return mf;
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

	
	private DEMappingFields checkMappedField(String iatiPath, String iatiItems,
			String iatiValues, String iatiLang, Long ampId, Class ampClass,
			Long sourceId, String feedFileName, String status) {
		Collection<DEMappingFields> allAmpDEMappingFields = DataExchangeUtils.getAllAmpDEMappingFields();
		DEMappingFields mf = new DEMappingFields(iatiPath, iatiItems, iatiValues, iatiLang, ampId, ampClass.toString(), sourceId, feedFileName, status);
		for (Iterator ot = allAmpDEMappingFields.iterator(); ot.hasNext();) {
			DEMappingFields deMappingFields = (DEMappingFields) ot.next();
			if(mf.compare(deMappingFields)) return deMappingFields;
		}
		return null;
	}

	private DEMappingFields addMappingField(String iatiPath, String iatiItems,
			String iatiValues, String iatiLang, Long ampId, Class ampClass,
			Long sourceId, String feedFileName, String status) {
		DEMappingFields mf = new DEMappingFields(iatiPath, iatiItems, iatiValues, iatiLang, ampId, ampClass.toString(), sourceId, feedFileName, status);
		DataExchangeUtils.insertDEMappingField(mf);
		return mf;
	}

	//return JAXBElement<TextType> content
	public String print(JAXBElement<TextType> item){
		String result = "";
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
	

}
