package org.digijava.module.help.util;

import java.util.List;

import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.request.Site;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.helper.HelpTopicTreeNode;

/**
 * Implements node worker interface. 
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class HelpTopicTreeNodeWorker implements AmpCollectionUtils.NodeWorker<Long, HelpTopic, HelpTopicTreeNode>{

    private Site site=null;
    private String locale=null;
    
    public HelpTopicTreeNodeWorker(){
        
    }
    
    public HelpTopicTreeNodeWorker(Site site, String locale){
        this.site = site;
        this.locale = locale;
    }
    
    @Override
    public HelpTopicTreeNode createTreeNode(HelpTopic element) {
        if (this.site!=null && this.locale!=null){
            //create translatable tree nodes.
            return new HelpTopicTreeNode(element,this.site,this.locale);
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
