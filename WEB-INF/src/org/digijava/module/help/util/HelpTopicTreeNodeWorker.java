package org.digijava.module.help.util;

import java.util.List;

import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.helper.HelpTopicTreeNode;

/**
 * Implements node worker interface. 
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class HelpTopicTreeNodeWorker implements AmpCollectionUtils.NodeWorker<Long, HelpTopic, HelpTopicTreeNode>{

	private String siteId=null;
	private String locale=null;
	
	public HelpTopicTreeNodeWorker(){
		
	}
	
	public HelpTopicTreeNodeWorker(String siteId, String locale){
		this.siteId = siteId;
		this.locale = locale;
	}
	
	@Override
	public HelpTopicTreeNode createTreeNode(HelpTopic element) {
		if (this.siteId!=null && this.locale!=null){
			//create translatable tree nodes.
			return new HelpTopicTreeNode(element,this.siteId,this.locale);
		}
		return new HelpTopicTreeNode(element);
	}

	@Override
	public List<HelpTopicTreeNode> getNodeChildren(HelpTopicTreeNode node) {
		return node.getNodeChildren();
	}

	@Override
	public Long getParentNodeId(HelpTopicTreeNode node) {
		return node.getParentNodeId();
	}

	@Override
	public void setNodeParent(HelpTopicTreeNode child, HelpTopicTreeNode Parent) {
		child.setParentNode(Parent);
	}

	@Override
	public Long resolveKey(HelpTopic element) {
		return element.getHelpTopicId();
	}
	

}
