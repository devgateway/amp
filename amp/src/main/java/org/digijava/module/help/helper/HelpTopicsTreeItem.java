package org.digijava.module.help.helper;

import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.module.help.dbentity.HelpTopic;

public class HelpTopicsTreeItem extends HierarchyMember{
    
    public HelpTopicsTreeItem(){
        super();
    }
    
    public String getTitleTrnKey(){
        HelpTopic topic=(HelpTopic)this.getMember();
        return topic.getTitleTrnKey();
    }
    
    public String getTopicKey(){
        HelpTopic topic=(HelpTopic)this.getMember();
        return topic.getTopicKey();
    }
    
    public Long getHelpTopicId(){
        HelpTopic topic=(HelpTopic)this.getMember();
        return topic.getHelpTopicId();
    }
    
    public HelpTopic getParent(){
        HelpTopic topic=(HelpTopic)this.getMember();
        return topic.getParent();
    }

    public String getModuleInstance(){
        HelpTopic topic=(HelpTopic)this.getMember();
        return topic.getModuleInstance();
    }
}
