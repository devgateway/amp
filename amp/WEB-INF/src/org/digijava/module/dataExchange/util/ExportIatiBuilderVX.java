/**
 * 
 */
package org.digijava.module.dataExchange.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.dbentity.IatiCodeItem;
import org.digijava.module.dataExchange.iati.IatiVersion;
import org.digijava.module.dataExchange.type.AmpColumnEntry;
import org.digijava.module.dataExchange.type.IatiCode;
import org.digijava.module.dataExchange.util.DataExchangeConstants.IatiCodeTypeEnum;

/**
 * Builds JAXB structure for a specific IATI XML version  
 * @author nmandrescu
 */
abstract public class ExportIatiBuilderVX {
	protected IatiVersion version  = null;
	protected List<String> exportLog = null;
	
	protected Site site = null;
	protected AmpColumnEntry tree = null;
	protected String lang = null;
	
	//Map<iatiCodeType , <iatyCodeName,iatyCodeValue>>  
	protected Map<IatiCodeTypeEnum, Map<String, String>> nameToIatiCode = new HashMap<IatiCodeTypeEnum, Map<String,String>>(); 
	
	public static ExportIatiBuilderVX getInstance(IatiVersion version, Site site, AmpColumnEntry ace) {
		ExportIatiBuilderVX builder = null;
		switch(version) {
		case V_1_03: builder = new ExportIatiBuilderV1_03(); break;
		case V_1_04: 
		default: return null;
		}
		builder.initialise(site, ace, version);
		return builder;
	}
	
	protected void initialise(Site site, AmpColumnEntry tree, IatiVersion version) {
		this.site = site;
		this.tree = tree;
		this.version = version;
		this.lang = TLSUtils.getEffectiveLangCode();
	}
	
	/**
	 * Adds activity to export JAXB structure 
	 * @param act
	 * @throws AmpExportException 
	 */
	public void addActivity(AmpActivity ampAct) throws AmpExportException{
		Object iatiAct = getNewIatiActivity();
		for (AmpColumnEntry elem : tree.getElements()) {
			if (elem.canExport()){
				addElement(iatiAct, elem, ampAct);
			}
		}
		addIatiActivityToRoot(iatiAct, ampAct); 
	}
	
	abstract public Object getRoot();
	
	abstract protected Object getNewIatiActivity();
	abstract protected void addIatiActivityToRoot(Object iatiAct, AmpActivity ampAct) throws AmpExportException;
	
	abstract protected void addElement(Object iatiAct, AmpColumnEntry elem, AmpActivity ampAct) throws AmpExportException;
	
	protected String getIatiCodeItemCode(IatiCodeTypeEnum iatiCodeType, String iatiCodeName) throws AmpExportException {
		/* caching type->name->code mapping for the current export request to reduce DB accesses on huge exports
		 */
		if (!nameToIatiCode.containsKey(iatiCodeType)) {
			nameToIatiCode.put(iatiCodeType, new HashMap<String, String>());
		}
		Map<String, String> nameCode = nameToIatiCode.get(iatiCodeType);
		if (!nameCode.containsKey(iatiCodeName)) {
			IatiCodeItem codeItem = ExportHelper.getIatiCodeItem(iatiCodeType, iatiCodeName);
			if (codeItem!=null)
				nameCode.put(iatiCodeName, codeItem.getCode());
		}
		return nameCode.get(iatiCodeName);
	}
	
	/**
	 * Finds IatiCodeName mapping for ampValue based on ampType
	 * @param ampType constant type, e.g. DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE
	 * @param ampValue AMP value of the entry with the given ampType
	 * @return pair of (iatiCodeName, iatiCode)
	 * @throws AmpExportException
	 */
	protected IatiCode getIatiCodeItemPair(String ampType, String ampValue, IatiCodeTypeEnum iatiCodeType) throws AmpExportException {
		//TODO some caching?
		String iatiCodeName = ExportHelper.getIatiCodeName(ampType, ampValue);
		String iatiCode = null;
		if (StringUtils.isNotBlank(iatiCodeName) ) 
			iatiCode = getIatiCodeItemCode(iatiCodeType, iatiCodeName);
		return new IatiCode(iatiCodeName, iatiCode);
	}
	
	//TODO: move to some activity util class
	protected AmpCategoryValue findCategory(AmpActivity ampAct, String category) {
		if (ampAct.getCategories()!=null && ampAct.getCategories().size()>0) {
			for (Iterator<AmpCategoryValue> iter = ampAct.getCategories().iterator(); iter.hasNext();) {
				AmpCategoryValue categVal = iter.next();
				if (category.equals(categVal.getAmpCategoryClass().getKeyName())) 
					return categVal;
			}
		}
		return null;
	}
	
	protected boolean isNotEmpty(Collection coll) {
		return coll!=null && !coll.isEmpty();
	}
}
