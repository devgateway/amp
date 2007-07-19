/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import java.util.HashMap;
import java.util.Iterator;

import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;

/**
 * @author dan
 *
 */
public class AmpTreeVisibility {

	/**
	 * 
	 */
	private AmpObjectVisibility root;
	//key-> name
	//object-> AmpTreeVisibility(template,module,feature,field)
	
	private HashMap items;
	
	
	public HashMap getItems() {
		return items;
	}

	public void setItems(HashMap items) {
		this.items = items;
	}

	public AmpObjectVisibility getRoot() {
		return root;
	}

	public void setRoot(AmpObjectVisibility root) {
		this.root = root;
	}

	public AmpTreeVisibility() {
		super();
		items=new HashMap();
		//root=new AmpObjectVisibility();
		// TODO Auto-generated constructor stub
	}

	public void buildAmpTreeVisibility(AmpObjectVisibility ampObjVis)
	{
		this.root=ampObjVis;
		this.setItems(new HashMap());
		//System.out.println("all items:::::"+ampObjVis.getAllItems().size());
		//System.out.println("---- items:"+ampObjVis.getItems().size());
		
		for(Iterator it=ampObjVis.getAllItems().iterator();it.hasNext();)
		{
			AmpModulesVisibility module=(AmpModulesVisibility)it.next();
			AmpTreeVisibility moduleNode= new AmpTreeVisibility();
			moduleNode.setRoot(module);
			//attach the features
			//System.out.println("----	module:"+module.getName());
			for(Iterator jt=module.getItems().iterator();jt.hasNext();)
			{
				AmpFeaturesVisibility feature= (AmpFeaturesVisibility)jt.next();
				AmpTreeVisibility  featureNode=new AmpTreeVisibility();
				featureNode.setRoot(feature);
				//System.out.println("----		"+feature.getName());
				for(Iterator kt=feature.getItems().iterator();kt.hasNext();)
				{
					AmpFieldsVisibility field= (AmpFieldsVisibility)kt.next();
					AmpTreeVisibility  fieldNode=new AmpTreeVisibility();
					fieldNode.setRoot(field);
					fieldNode.setItems(null);
					featureNode.getItems().put(field.getName(),fieldNode);
				}
				//moduleNode.setItems(new HashMap());
				moduleNode.getItems().put(feature.getName(), featureNode);
			}
			this.getItems().put(module.getName(), moduleNode);
		}
		//attach the fields
		this.root=ampObjVis;
		//return this.root;
	}
	
	public boolean isVisibleObject(AmpObjectVisibility aov)
	{
		if(this.getRoot().getTemplates().contains(aov.getId()))
			{
				return true;
			}
		return false;
	}
	
	public boolean isVisibleId(Long id)
	{
		for(Iterator it=this.getRoot().getTemplates().iterator();it.hasNext();)
		{
			
			AmpTemplatesVisibility x=(AmpTemplatesVisibility) it.next();
			if(x.getId().compareTo(id)==0) return true;
			
		}
		return false;
	}
	
	public AmpFieldsVisibility getFieldByNameFromRoot(String fieldName)
	{
		
		for(Iterator it=this.getItems().values().iterator();it.hasNext();)
		{
			AmpTreeVisibility module=(AmpTreeVisibility) it.next();
			for(Iterator jt=module.getItems().values().iterator();jt.hasNext();)
			{
				AmpTreeVisibility feature=(AmpTreeVisibility) jt.next();
				if(feature.getItems().containsKey(fieldName)) return (AmpFieldsVisibility) ((AmpTreeVisibility) feature.getItems().get(fieldName)).getRoot();
			}
		}
		return null;
	}
	
	public AmpFeaturesVisibility getFeatureByNameFromRoot(String featureName)
	{
		for(Iterator it=this.getItems().values().iterator();it.hasNext();)
		{
			AmpTreeVisibility module=(AmpTreeVisibility) it.next();
			if(module.getItems().containsKey(featureName)) 
				return (AmpFeaturesVisibility)((AmpTreeVisibility) module.getItems().get(featureName)).getRoot();
			
		}
		return null;
	}
	public AmpTreeVisibility getFeatureTreeByNameFromRoot(String featureName)
	{
		for(Iterator it=this.getItems().values().iterator();it.hasNext();)
		{
			AmpTreeVisibility module=(AmpTreeVisibility) it.next();
			if(module.getItems().containsKey(featureName)) 
				return (AmpTreeVisibility) module.getItems().get(featureName);
			
		}
		return null;
	}
	
	public AmpModulesVisibility getModuleByNameFromRoot(String moduleName)
	{
		if(this.getItems().containsKey(moduleName)) 
			return (AmpModulesVisibility)((AmpTreeVisibility) this.getItems().get(moduleName)).getRoot();
		return null;
	}
	public AmpTreeVisibility getModuleTreeByNameFromRoot(String moduleName)
	{
		if(this.getItems().containsKey(moduleName)) 
			return (AmpTreeVisibility) this.getItems().get(moduleName);
		return null;
	}

}
