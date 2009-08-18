package org.digijava.module.aim.fmtool.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.fmtool.types.*;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.dataExchange.type.AmpColumnEntry;

public class FeatureManagerTreeHelper {

	private Map<Long, FMVisibilityWrapper> moduleMap = null;

	private Map<Long, FMVisibilityWrapper> featureMap = null;
	
	private Map<Long, FMVisibilityWrapper> fieldMap = null;
	
	private Map<Long, AmpModulesVisibility> modulesVisibilityMap = null;
	private Map<Long, AmpFeaturesVisibility> featuresVisibilityMap = null;
	private Map<Long, AmpFieldsVisibility> fieldsVisibilityMap = null;
	
	public FeatureManagerTreeHelper(Map<Long, FMVisibilityWrapper> moduleMap, 
			Map<Long, FMVisibilityWrapper> featureMap,
			Map<Long, FMVisibilityWrapper> fieldMap){
		
		this.moduleMap = moduleMap;
		this.featureMap = featureMap;
		this.fieldMap = fieldMap;
		
//		if (! showAll){
			modulesVisibilityMap = AmpCollectionUtils.createMap(FeaturesUtil.getAMPModulesVisibility(), new FMEKeyResolver())  ;
			featuresVisibilityMap = AmpCollectionUtils.createMap(FeaturesUtil.getAMPFeaturesVisibility(), new FMEKeyResolver())  ;
			fieldsVisibilityMap = AmpCollectionUtils.createMap(FeaturesUtil.getAMPFieldsVisibility(), new FMEKeyResolver())  ;
//		}
		
	}
	
	public FMCheckTreeEntry getTree(){
		FMCheckTreeEntry retValue = new FMCheckTreeEntry("FME_Tree_Root","ROOT",true);
		retValue.setType(FMToolConstants.FEATURE_TYPE_MODULE);
		
		retValue.addElements(getAllChildModule(new FMVisibilityWrapper()));
		
		return retValue;
	}
	
	private List<FMCheckTreeEntry> getAllChildModule(FMVisibilityWrapper module){
		List<FMCheckTreeEntry> retValue = new ArrayList<FMCheckTreeEntry>();
		module.setHasLevel(new Boolean(true));
		
		for (FMVisibilityWrapper item : moduleMap.values()) {
			if ((module.getId() == null && item.getParentId() == null) ||
					module.getId() != null && item.getParentId() != null && item.getParentId().equals(module.getId())){
				item.setHasLevel(new Boolean(true));
				AmpModulesVisibility moduleVisibility =modulesVisibilityMap.get(item.getId());
				boolean hasSource = moduleVisibility !=null && moduleVisibility.getSources() !=null && !moduleVisibility.getSources().isEmpty();
				
				FMCheckTreeEntry treeIthem = new FMCheckTreeEntry(FMToolConstants.FEATURE_TYPE_MODULE+item.getId(), item.getName(), hasSource);
				treeIthem.setType(FMToolConstants.FEATURE_TYPE_MODULE);
				treeIthem.addElements(getAllChildModule(item));
				treeIthem.addElements(getAllChildFeature(item));
				
				retValue.add(treeIthem);
			} 
		}
	
		return retValue;
	}
	
	//#########################3 get Circularity Modules
	public List<FMProblemWrapper> getCircularityElements(){
		List<FMProblemWrapper> retValue = new ArrayList<FMProblemWrapper>();
		List<FMVisibilityWrapper> unusedModules = getUnusedModules();
		
		while (!unusedModules.isEmpty()){
			FMVisibilityWrapper item = getModuleWithMinID(unusedModules);
			if (item != null){
				FMProblemWrapper fvw = new FMProblemWrapper(item.getId(), item.getName(), FMToolConstants.FEATURE_TYPE_MODULE, "Circularity Module");
				retValue.add(fvw);
				
				item.setHasLevel(new Boolean(true));
				checkAllChildModule(item,unusedModules);
			}
			unusedModules = getUnusedModules();
		}
		
		return retValue;
	}
	
	private void checkAllChildModule(FMVisibilityWrapper module, List<FMVisibilityWrapper> list){
		
		for (FMVisibilityWrapper item : list) {
			if ( !item.getHasLevel().booleanValue() && item.getParentId() != null && item.getParentId().equals(module.getId())){
				item.setHasLevel(new Boolean(true));
				checkAllChildModule(item, list);
			}
		}
	}
	
	
	private List<FMVisibilityWrapper> getUnusedModules(){
		List<FMVisibilityWrapper> retValue = new ArrayList<FMVisibilityWrapper>();
		
		for (FMVisibilityWrapper item : moduleMap.values()) {
			if (!item.getHasLevel().booleanValue()){
				retValue.add(item);
			}
		}
		return retValue;
	}
	
	private FMVisibilityWrapper getModuleWithMinID(List<FMVisibilityWrapper> list){
		FMVisibilityWrapper retValue = null;
		
		for (FMVisibilityWrapper item : list) {
			if (retValue == null){
				retValue = item;
				continue;
			}
			if (item.getId().longValue() < retValue.getId().longValue()){
				retValue = item;
			}
		}		
		
		return retValue;
	}
	
	private List<FMCheckTreeEntry> getAllChildFeature(FMVisibilityWrapper module){
		List<FMCheckTreeEntry> retValue = new ArrayList<FMCheckTreeEntry>();

		for (FMVisibilityWrapper item : featureMap.values()) {
			if ( module.getId() != null && item.getParentId() != null && item.getParentId().equals(module.getId())){
				
				AmpFeaturesVisibility featureVisibility = featuresVisibilityMap.get(item.getId());
				boolean hasSource = featureVisibility !=null && featureVisibility.getSources() !=null && !featureVisibility.getSources().isEmpty();
				
				FMCheckTreeEntry treeIthem = new FMCheckTreeEntry(FMToolConstants.FEATURE_TYPE_FEATURE+item.getId(), item.getName(),hasSource);

				treeIthem.setType(FMToolConstants.FEATURE_TYPE_FEATURE);
				treeIthem.addElements(getAllChildField(item));
				retValue.add(treeIthem);
			} 
		}

		return retValue;
	}

	private List<FMCheckTreeEntry> getAllChildField(FMVisibilityWrapper feature){
		List<FMCheckTreeEntry> retValue = new ArrayList<FMCheckTreeEntry>();

		for (FMVisibilityWrapper item : fieldMap.values()) {
			if ( feature.getId() != null && item.getParentId() != null  && item.getParentId().equals(feature.getId())){
				
				AmpFieldsVisibility fieldVisibility = fieldsVisibilityMap.get(item.getId());
				boolean hasSource = fieldVisibility !=null && fieldVisibility.getSources() !=null && !fieldVisibility.getSources().isEmpty();
				
				FMCheckTreeEntry treeIthem = new FMCheckTreeEntry(FMToolConstants.FEATURE_TYPE_FIELD+item.getId(), item.getName(),hasSource);
				treeIthem.setType(FMToolConstants.FEATURE_TYPE_FIELD);
				retValue.add(treeIthem);
			} 
		}
		
		return retValue;
	}

/*	
	public static String renderHiddenElements(FMCheckTreeEntry tag){
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
//		if (tag.isSelect() || tag.isMandatory()){
//			retValue.append("true");
//		} else{
			retValue.append("false");
//		}
		retValue.append("\" ");
		retValue.append("/>");
		retValue.append("\n");
		
		if (tag.getElements() != null){
			for (FMCheckTreeEntry subNode : tag.getElements()) {
				retValue.append(renderHiddenElements(subNode));
			}
		}

		
		
		return retValue.toString();
	}	
*/	
	
	public static String renderTree(FMCheckTreeEntry node) {

		
		StringBuffer retValue = new StringBuffer();

		retValue.append(renderTreeNode(node, "tree.getRoot()", true));

		return retValue.toString();
	}
	
	private static String renderTreeNode(FMCheckTreeEntry node, String parentNode, boolean isRoot) {
		
		StringBuffer retValue = new StringBuffer();
		String nodeVarName = "fmn_"+ node.getKey();
		
		if(!isRoot){
			retValue.append("if (showAll || !"+node.isCorrect()+"){ \n");
		}
		
		retValue.append("var "+ nodeVarName +" = new YAHOOAmp.widget.TextNode(\"" + node.getName() + "\", " + parentNode + ", ");
		retValue.append("false );");
		retValue.append("\n");
		retValue.append(nodeVarName+ ".labelElId = \""+node.getType()+"\"; \n ");
//		retValue.append(nodeVarName+ ".correct = \""+node.isCorrect()+"\"; \n");
		
		if (node.getType().equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_MODULE)){
			retValue.append(nodeVarName+ ".labelStyle = \"icon-module\";  ");
		} else if (node.getType().equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_FEATURE)){
			retValue.append(nodeVarName+ ".labelStyle = \"icon-feature\";  ");
		} else {
			retValue.append(nodeVarName+ ".labelStyle = \"icon-field\";  ");
		}
//		retValue.append(nodeVarName+ ".labelStyle = \"background-color: none; color: red;\";  ");
		retValue.append("\n");

		if (node.getElements() != null){
			for (FMCheckTreeEntry subNode : node.getElements()) {
//				if (showAll || !subNode.isCorrect() ){
				retValue.append("\n");
				retValue.append(renderTreeNode(subNode, nodeVarName, false));
				retValue.append("\n");
//				}
			}
		}			
		if(!isRoot){
			retValue.append("}\n");
		}

		return retValue.toString();
	}
	

	private class FMEKeyResolver implements KeyResolver<Long, AmpObjectVisibility>{

		@Override
		public Long resolveKey(AmpObjectVisibility element) {
			return element.getId();
		}
		
	}
	
	
}
