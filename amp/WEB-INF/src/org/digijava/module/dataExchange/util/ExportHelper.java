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
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.type.AmpColumnEntry;

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
		
		StringBuffer retValue = new StringBuffer();
		
		DESourceSetting setting = null ;
		
			try {
				setting = new SessionSourceSettingDAO().getSourceSettingById( new Long(2) );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		retValue.append(renderActivityTreeNode(node, "tree.getRoot()" , setting, request));

		return retValue.toString();
	}
	
	public static String renderActivityTree(AmpColumnEntry node,Long sourceId, HttpServletRequest request) {
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

		retValue.append(renderActivityTreeNode(node, "tree.getRoot()" , setting,request));

		return retValue.toString();
	}

	private static String renderActivityTreeNode(AmpColumnEntry node, String parentNode,DESourceSetting setting, HttpServletRequest request) {
	
		String nodeName		= null;
		try {
			nodeName	= (request!=null)?TranslatorWorker.translateText(node.getName(), request):node.getName();
		} catch (WorkerException e) {
			nodeName	= node.getName();
			e.printStackTrace();
		}
	
		
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
				if (claaa.getName().startsWith("org.digijava.module.dataExchange.jaxb")) {
					retValue.getList().add(getActivityStruct(field.getName(), newKey + (++index) + "]", newPath, claaa, mandatory));
				}
			} else {
				Class claaa = (Class) (field.getGenericType());
				if (claaa.getName().startsWith("org.digijava.module.dataExchange.jaxb")) {
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
			boolean mandatory = false;
			
			String newPath = path + PATH_DELIM + field.replace(' ', '_');
			String newKey = key + ".elements["+ (++index) + "]" ;
			AmpColumnEntry item = null;
			try {
				item = new AmpColumnEntry(newKey + ".select", TranslatorWorker.translateText(field,request), newPath);
			} catch (WorkerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
									translateFromTree(genKey, site.getId().longValue(), request.getLocale().getLanguage(), 
											value, TranslatorWorker.TRNTYPE_LOCAL, null,session.getServletContext());
			return translatedValue;
		} catch (WorkerException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static List<Message> getTranslations(String key, String body, String siteId) throws AmpExportException{

		List<Message> retValue = new ArrayList<Message>();
		TranslatorWorker tw = new TranslatorWorker();
		try{
			if (key != null && !key.isEmpty()){
				retValue = (List<Message>)tw.getAllTranslationsOfKey(key, siteId);
			}
			if (body != null && !body.isEmpty()){
				retValue = (List<Message>)tw.getAllTranslationOfBody(body, siteId);
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
	
	
}
