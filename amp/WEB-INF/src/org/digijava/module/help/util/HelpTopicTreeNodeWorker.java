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

	@Override
	public HelpTopicTreeNode createTreeNode(HelpTopic element) {
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
