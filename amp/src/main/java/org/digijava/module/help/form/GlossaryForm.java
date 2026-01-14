package org.digijava.module.help.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.help.helper.HelpTopicTreeNode;

import java.util.List;

/**
 * 
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class GlossaryForm extends ActionForm{

    private static final long serialVersionUID = 1L;
    private List<HelpTopicTreeNode> tree;
    private String searchTerm;
    private Long parentNodeId;
    private Long nodeId;
    private String nodeName;
    private String nodeParentName;
    
    public void setTree(List<HelpTopicTreeNode> tree) {
        this.tree = tree;
    }
    public List<HelpTopicTreeNode> getTree() {
        return tree;
    }
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    public String getSearchTerm() {
        return searchTerm;
    }
    public void setParentNodeId(Long parentNodeId) {
        this.parentNodeId = parentNodeId;
    }
    public Long getParentNodeId() {
        return parentNodeId;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
    public Long getNodeId() {
        return nodeId;
    }
    public void setNodeParentName(String nodeParentName) {
        this.nodeParentName = nodeParentName;
    }
    public String getNodeParentName() {
        return nodeParentName;
    }

}
