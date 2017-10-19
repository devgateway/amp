package org.digijava.module.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.util.AmpMessageUtil;

public class ViewMessages extends TilesAction {
    
    public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        AmpMessageForm messageForm=(AmpMessageForm)form;
        //getting message settings
        AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
        if(settings!=null && settings.getMsgRefreshTime()!=null && settings.getMsgRefreshTime().longValue()>0){
            messageForm.setMsgRefreshTimeCurr(settings.getMsgRefreshTime());
        }  else{
            messageForm.setMsgRefreshTimeCurr(new Long(-1));
        }
        return null;
    }
    
}
