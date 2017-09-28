package org.digijava.module.message.triggers;

import java.net.URLDecoder;

import javax.jcr.Node;

import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.message.helper.Event;

public abstract class AbstractResourceShare extends Trigger {
    
    public static final String PARAM_NAME="name";
    public static final String PARAM_SHARED_BY="sharedBy";
    public static final String PARAM_CREATOR_TEAM="team";
    public static final String PARAM_URL="url";
    
    public static final String [] parameterNames=new String[]{PARAM_NAME,PARAM_SHARED_BY,PARAM_URL};

    public AbstractResourceShare(Object source) {
         if(!(source instanceof Node)) throw new RuntimeException("Incompatible object. Source must be a Node!");
         this.source=source;
         forwardEvent();
    }
    
    @Override
    protected Event generateEvent() {
        Event e = getEvent(); 
        Node node=(Node) source;
        try {
            String name = URLDecoder.decode( DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_TITLE).getString() ,"UTF-8");
            e.getParameters().put(PARAM_NAME,name);
            e.getParameters().put(PARAM_SHARED_BY, DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CREATOR).getString());
            e.getParameters().put(PARAM_CREATOR_TEAM, new Long (DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CREATOR_TEAM).getLong()));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }       
        //e.getParameters().put(PARAM_URL,null);
        return e;
    }

    @Override
    public String[] getParameterNames() {
        return parameterNames;
    }
    
    protected abstract Event getEvent();
}
