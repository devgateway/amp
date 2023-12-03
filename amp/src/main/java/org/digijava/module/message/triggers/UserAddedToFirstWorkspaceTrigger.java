package org.digijava.module.message.triggers;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.message.helper.Event;
import org.digijava.module.message.helper.MessageConstants;

import java.util.List;

public class UserAddedToFirstWorkspaceTrigger extends Trigger {

    public static final String PARAM_NAME="name";
    public static final String PARAM_TRIGGER_SENDER="sentBy";
    public static final String PARAM_CREATION_DATE="Creation date";
    public static final String PARAM_URL="User profile url";
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_ORGANIZATION = "organization";
    public static final String PARAM_TEAM = "team";
    
    private static final String [] parameterNames=new String[]{
        PARAM_NAME, PARAM_TRIGGER_SENDER, PARAM_CREATION_DATE,
        PARAM_URL, PARAM_LOGIN, PARAM_ORGANIZATION, PARAM_TEAM};

    public UserAddedToFirstWorkspaceTrigger(Object source) {
        this.source=source;
        forwardEvent();
    }

    protected Event generateEvent() {
        Event e=new Event(UserAddedToFirstWorkspaceTrigger.class);
        
//      User user=(User) source;
        List<Object> paramList = (List<Object>) source;
        User user = (User) paramList.get(0);
        AmpTeam team = (AmpTeam) paramList.get(1);
        if ((user == null) || (team == null))
        {
            throw new RuntimeException();
        }
        
        
        e.getParameters().put(PARAM_NAME,user.getName());
        e.getParameters().put(PARAM_TRIGGER_SENDER,MessageConstants.SENDER_TYPE_USER_MANAGER);
        e.getParameters().put(PARAM_CREATION_DATE, System.currentTimeMillis());
        e.getParameters().put(PARAM_URL, "aim/default/userProfile.do~edit=true~id="+user.getId());
        e.getParameters().put(PARAM_LOGIN, user.getEmail());
        e.getParameters().put(PARAM_ORGANIZATION, team.getName());
        
        String orgName = "<none>";
        if (user.getUserExtension() != null && user.getUserExtension().getOrganization() != null)
            orgName = user.getUserExtension().getOrganization().getAcronymAndName();        
        e.getParameters().put(PARAM_ORGANIZATION, orgName);
        return e;
    }


    public String[] getParameterNames() {
        return parameterNames;
    }

}
