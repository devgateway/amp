package org.digijava.module.dataExchange.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.dgfoundation.amp.error.AMPException;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.dbentity.IatiCodeItem;
import org.digijava.module.dataExchange.dbentity.IatiCodeType;
import org.digijava.module.dataExchange.iati.IatiVersion;
import org.digijava.module.dataExchange.type.AmpColumnEntry;
import org.digijava.module.dataExchange.util.DataExchangeConstants.IatiCodeTypeEnum;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;

public class ExportHelper {


	public static String PATH_DELIM = ".";
	
	public static String renderHiddenElements(AmpColumnEntry tag){
//		<input type="hidden" name="activityTags[0].select" value="true">
	
		StringBuffer retValue = new StringBuffer();

		
		retValue.append("<input type=\"hidden\" ");

		Pattern pattern = Pattern.compile("[\\]\\[.]");
		Matcher matcher = pattern.matcher(tag.getKey());

		retValue.append("id=\"");
		retValue.append("id_"+ matcher.replaceAll(""));
		retValue.append("\" ");
		retValue.append("name=\"");
//			retValue.append("activityTags["+list.indexOf(tag)+"].select");
		retValue.append(tag.getKey());
		retValue.append("\" ");
		retValue.append("value=\"");
		if (tag.isSelect() || tag.isMandatory()){
			retValue.append("true");
		} else{
			retValue.append("false");
		}
		retValue.append("\" ");
		retValue.append("/>");
		retValue.append("\n");
		
		if (tag.getElements() != null){
			for (AmpColumnEntry subNode : tag.getElements()) {
				retValue.append(renderHiddenElements(subNode));
			}
		}		
		
		return retValue.toString();
	}
	
	public static String renderHiddenElements(AmpColumnEntry tag,Long sourceId){
		
		DESourceSetting setting = null ;
		if(sourceId != null && ! sourceId.equals(new Long(-1))){
			try {
				setting = new SessionSourceSettingDAO().getSourceSettingById( sourceId );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		StringBuffer retValue = new StringBuffer();		
		retValue.append("<input type=\"hidden\" ");

		Pattern pattern = Pattern.compile("[\\]\\[.]");
		Matcher matcher = pattern.matcher(tag.getKey());

		retValue.append("id=\"");
		retValue.append("id_"+ matcher.replaceAll(""));
		retValue.append("\" ");
		retValue.append("name=\"");
		retValue.append(tag.getKey());
		retValue.append("\" ");
		retValue.append("value=\"");
		
		if (setting!=null && setting.getFields().contains(tag.getPath())) {
			retValue.append("true");
		}else{
			if (tag.isSelect() || tag.isMandatory()){
				retValue.append("true");
			} else{
				retValue.append("false");
			}
		}
		
		retValue.append("\" ");
		retValue.append("/>");
		retValue.append("\n");
		
		if (tag.getElements() != null){
			for (AmpColumnEntry subNode : tag.getElements()) {
				retValue.append(renderHiddenElements(subNode,sourceId));
			}
		}		
		
		return retValue.toString();
	}
	
	
	public static String renderActivityTree(AmpColumnEntry node, HttpServletRequest request) {
        return renderActivityTreeNode(node, "tree.getRoot()", null, request);
	}
	
	public static String renderActivityTree(AmpColumnEntry node, Long sourceId, HttpServletRequest request) {
		DESourceSetting setting = null ;
		if (sourceId != null && sourceId != -1) {
			try {
				setting = new SessionSourceSettingDAO().getSourceSettingById( sourceId );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

        return renderActivityTreeNode(node, "tree.getRoot()", setting, request);
	}

	private static String renderActivityTreeNode(AmpColumnEntry node, String parentNode,DESourceSetting setting, HttpServletRequest request) {
	
//		String nodeName		= (request!=null)?TranslatorWorker.translateText(node.getName()):node.getName();
			
		Pattern pattern = Pattern.compile("[\\]\\[.]");
		Matcher matcher = pattern.matcher(node.getKey());
		String key = matcher.replaceAll("");
		StringBuffer retValue = new StringBuffer();
		String nodeVarName = "atn_"+ key;
		retValue.append("var "+ nodeVarName +" = new YAHOO.widget.TaskNode(\"" + node.getName() + "\", " + parentNode + ", ");
		retValue.append("false , ");
		if (setting!=null && setting.getFields().contains(node.getPath())) {
			retValue.append(Boolean.toString(true) + ", ");
		}else{
			retValue.append(Boolean.toString(node.isSelect()) + ", ");
		}		
		retValue.append(Boolean.toString(node.isMandatory()) + ", ");
		retValue.append("\""+key+"\"");
		retValue.append("); ");
		retValue.append("\n");

		if (node.getElements() != null){
			for (AmpColumnEntry subNode : node.getElements()) {
				retValue.append("\n");
				retValue.append(renderActivityTreeNode(subNode, nodeVarName,setting,request));
				retValue.append("\n");
			}
		}			
		return retValue.toString();
	}	
	
	/**
	 * Build Iati Activity tree 
	 * @param version is Iati XML Schema version (e.g. 1.03)
	 * @param depth is the maximum depth of the schema tree to generate
	 * @return
	 */
	public static AmpColumnEntry getActivityStruc(IatiVersion version, int depth) {
		switch(version) {
		case V_1_03: return getActivityStruct1_03("iati-activity","activityTree","iati-activity", 
				org.digijava.module.dataExchangeIATI.iatiSchema.v1_03.jaxb.IatiActivity.class,
				true, depth);
		case V_1_04:
		default: return null;
		}
	}
	
	private static AmpColumnEntry getActivityStruct1_03(String name, String key, String path, Class clazz, boolean required, int depth) {
		AmpColumnEntry retValue = new AmpColumnEntry(key + ".select", name, path);
		retValue.setSelect(required);
		retValue.setMandatory(required);
		
		//check if we reached maximum allowed depth 
		if (depth==0) return retValue;

		Field[] fields = clazz.getDeclaredFields();
		int index = -1;
		
		for (Field field : fields) {
			XmlElementRefs xmlElementRefs  = field.getAnnotation(XmlElementRefs.class);
			if (xmlElementRefs!=null) {
				for (XmlElementRef xmlElementRef: xmlElementRefs.value()) {
					String newPath = path + ExportHelper.PATH_DELIM + xmlElementRef.name();
					String newKey = key + ".elements[" ;
					retValue.getList().add(getActivityStruct1_03(xmlElementRef.name(), newKey + (++index) + "]", newPath, xmlElementRef.type(), false, depth-1)); 
					//requires normally should be read from xmlElementRef.required(), but in 1.03 minOccurance is not specified and JAXBs are thought to be all required by default
				}
			}
		}

		return retValue;
	}
	
	/**
	 * Old XML schema tree structure generation
	 * @param name
	 * @param key
	 * @param path
	 * @param clazz
	 * @param required
	 * @return
	 */
								//getActivityStruct("activity","activityTree","activity",ActivityType.class,true)
	public static AmpColumnEntry getActivityStruct(String name, String key, String path,  Class clazz, boolean required) {
		AmpColumnEntry retValue = new AmpColumnEntry(key + ".select", name, path);
		retValue.setSelect(required);
		retValue.setMandatory(required);

		Field[] fields = clazz.getDeclaredFields();
		int index = -1;
		
		for (Field field : fields) {
			XmlElement xmlElement = field.getAnnotation(XmlElement.class);
			boolean mandatory = false;
			if (xmlElement != null) {
				mandatory = xmlElement.required();
			}

			String newPath = path + PATH_DELIM + field.getName();
			String newKey = key + ".elements[" ;

			if (field.getGenericType() instanceof ParameterizedType) {
				ParameterizedType type = (ParameterizedType) field.getGenericType();
				Class claaa = (Class) (type.getActualTypeArguments()[0]);
				if (claaa.getName().startsWith("org.digijava.module.dataExchange.iatiSchema.jaxb")) {
					retValue.getList().add(getActivityStruct(field.getName(), newKey + (++index) + "]", newPath, claaa, mandatory));
				}
			} else {
				Class claaa = (Class) (field.getGenericType());
				if (claaa.getName().startsWith("org.digijava.module.dataExchange.iatiSchema.jaxb")) {
					retValue.getList().add(getActivityStruct(field.getName(), newKey + (++index) + "]", newPath, claaa, mandatory));
				}
			}
		}

		return retValue;
	}
	//							 getIATIActivityStruct("activity","activityTree","activity")
	public static AmpColumnEntry getIATIActivityStruct(String name, String key, String path, HttpServletRequest request) {
		AmpColumnEntry retValue = new AmpColumnEntry(key + ".select", name, path);
		retValue.setSelect(true);
		retValue.setMandatory(true);

		String[] fields = {"Title","Objective","Description","Status","Activity Dates","Funding","Sector","Related Organizations","Location","Regional Funding","Programs","Contacts","Related Documents"};
		
//		Field[] fields = clazz.getDeclaredFields();
		int index = -1;
		
		for (String field : fields) {
	//		boolean mandatory = false;
			
			String newPath = path + PATH_DELIM + field.replace(' ', '_');
			String newKey = key + ".elements["+ (++index) + "]" ;
			AmpColumnEntry item = new AmpColumnEntry(newKey + ".select", TranslatorWorker.translateText(field), newPath);
			
			item.setMandatory(false);
			item.setSelect(true);
			retValue.getList().add(item);
		}

		return retValue;
	}
	
	public static String translate(String value, HttpServletRequest request){
		HttpSession session = request.getSession();
		
		Site site = RequestUtils.getSite(request);
		
		String genKey = TranslatorWorker.generateTrnKey(value);
		String translatedValue;
		try {
			translatedValue = TranslatorWorker.getInstance(genKey).
									translateFromTree(genKey, site, request.getLocale().getLanguage(), 
											value, TranslatorWorker.TRNTYPE_LOCAL, null,session.getServletContext());
			return translatedValue;
		} catch (WorkerException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static List<Message> getTranslations(String key, String body, Long siteId) throws AmpExportException{

		List<Message> retValue = new ArrayList<Message>();
		try{
			if (key != null && !key.isEmpty()){
				retValue = (List<Message>)TranslatorWorker.getAllTranslationsOfKey(key, siteId);
			}
			if (body != null && !body.isEmpty()){
				retValue = (List<Message>)TranslatorWorker.getAllTranslationOfBody(body, siteId);
			}
		} catch (WorkerException ex){
			throw new AmpExportException(ex, AmpExportException.ACTIVITY_TRANSLATION);
		}
		return retValue;

	}

	public static XMLGregorianCalendar getGregorianCalendar(Date date) throws AmpExportException {
		XMLGregorianCalendar retValue = null;
		if (date != null) {
			try {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(date); 
				retValue = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(cal);
			} catch (Exception e) {
				throw new AmpExportException(e, AmpExportException.ACTIVITY_FORMAT);
			}
		}
		return retValue;
	}
	
	/**
	 * Get IATI Code Item for a specific code type and code name
	 * @param typeName - IatiCodeTypeEnum type (e.g. IatiCodeTypeEnum.OrganisationType) 
	 * @param iatiCodeName - code name like 'Government' 
	 * @return IatiCodeItem (e.g. code='10' name='Government')
	 * @throws AmpExportException
	 */
	public static IatiCodeItem getIatiCodeItem(IatiCodeTypeEnum typeName, String iatiCodeName) throws AmpExportException {
		try {
			IatiCodeType codeType = DbUtil.getIatiCodeTypeByName(typeName);
			IatiCodeItem codeItem = codeType==null ? null : DbUtil.getIatiCodeByName(iatiCodeName, codeType.getId());
			return codeItem;
		} catch (AMPException e) {
			throw new AmpExportException(e.getCause(), AmpExportException.ACTIVITY_DATA_INEFFICIENT);
		}
	}
	
	/**
	 * Finds IatiCodeName mapping for ampValue based on ampType
	 * @param ampType constant type, e.g. DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE
	 * @param ampValue AMP value of the entry with the given ampType
	 * @return
	 */
	public static String getIatiCodeName(String ampType, String ampValue) {
		String iatiCodeName = null;
		//let's try to find the mapping to IATI the way we can right now - hack
		List<DEMappingFields> fields = DataExchangeUtils.getDEMappingFieldsByAmpClassAndAmpValues(ampType,ampValue);
		if (fields.size()>0 && fields.get(0).getIatiValues()!=null)
			iatiCodeName = fields.get(0).getIatiValues().split("\\|\\|\\|")[0];
		return iatiCodeName;
	}
}
