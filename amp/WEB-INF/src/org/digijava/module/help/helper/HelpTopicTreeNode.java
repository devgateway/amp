package org.digijava.module.help.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.help.dbentity.HelpTopic;

/**
 * Helper for help topic tree node.
 * Currently used only for Hello Glossary rendering.
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class HelpTopicTreeNode {
    private static final long serialVersionUID = 1L;
    private Long parentNodeId;
    private HelpTopic origin;
    private List<HelpTopicTreeNode> children;
    private HelpTopicTreeNode parentNode;
    private Site site;
    private String locale;

    /**
     * Default constructor.
     */
    public HelpTopicTreeNode(){
        children = new ArrayList<HelpTopicTreeNode>();
    }

    /**
     * Constructs node from db bean. 
     * @param dbBean
     */
    public HelpTopicTreeNode(HelpTopic dbBean){
        this();
        this.origin = dbBean;
        if (dbBean.getParent()!=null){
            this.parentNodeId = this.origin.getParent().getHelpTopicId();
        }
    }
    
    /**
     * Constructs node from db bean with translated title.
     * @param dbBean
     * @param siteId
     * @param locale
     */
    public HelpTopicTreeNode(HelpTopic dbBean, Site site, String locale){
        this(dbBean);
        this.site = site;
        this.locale = locale;
    }
    
    public List<HelpTopicTreeNode> getNodeChildren() {
        return children;
    }
    
    public void setParentNode(HelpTopicTreeNode parentNode){
        this.parentNode = parentNode;
    }
    public HelpTopicTreeNode getParentNode(){
        return this.parentNode;
    }

    public Long getParentNodeId() {
        return parentNodeId;
    }
    
    /**
     * Generates YahooUI treeView JS structure.
     * @return
     */
    public String getYahooJSdefinition(){
        
        String trnTitle = trn(this.origin.getTopicKey());
        
        StringBuffer buf=new StringBuffer("{ type: 'text', label: '");
        buf.append(trnTitle);
        buf.append("', ampHelpTopicId: '");
        buf.append(this.origin.getHelpTopicId());
        buf.append("'");
        if (this.origin.getBodyEditKey()!=null){
            buf.append(", ampEditorKey: '");
            buf.append(this.origin.getBodyEditKey());
            buf.append("'");
        }
        
        if (this.getNodeChildren().size()>0){
            buf.append(",children: [");
            List<HelpTopicTreeNode> children = getNodeChildren();
            for (Iterator<HelpTopicTreeNode> iter = children.iterator(); iter.hasNext();) {
                HelpTopicTreeNode child = iter.next();
                buf.append(child.getYahooJSdefinition());
                if (iter.hasNext()){
                    buf.append(",");
                }
            }
            buf.append("]");
        }
        buf.append("}");
//      //System.out.println(buf.toString());
        return buf.toString();
    }
    
    /**
     * Generates search result links.
     * @return
     */
    public String getSearchResultLink(){
        StringBuffer buff = new StringBuffer("\n");
        buff.append("<div id=\"AMP_HelpNodeDiv_");
        buff.append(this.origin.getHelpTopicId());
        buff.append("\">\n\t<a href=\"javascript:showGlossary(");
        buff.append(this.origin.getHelpTopicId());
        buff.append(",'");
        buff.append(trn(this.origin.getTopicKey()));
        buff.append("','");
        buff.append(this.origin.getBodyEditKey());
        buff.append("')\">");
        buff.append(trn(this.origin.getTopicKey()));
        buff.append("</a>\n</div>\n");
        return buff.toString();
    }

    private String trn(String text){
        String result = text;
        if (this.site != null && this.locale != null){
            //translate if in translatable mode
            result = TranslatorWorker.translateText(text, this.locale, this.site);
            result = result.replaceAll("'", "\\\\'");
        }
        return result;
    }
}
