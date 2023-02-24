package org.dgfoundation.amp.globalsettings;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.module.aim.util.FeaturesUtil;

public class GlobalSettingsTagClass extends BodyTagSupport{

    private String name             = null;
    
    private String compareWith      = null;
    
    private boolean onTrueEvalBody  = true;
    
    private boolean shouldEval      = false;

    public boolean isOnTrueEvalBody() {
        return onTrueEvalBody;
    }

    public void setOnTrueEvalBody(boolean onTrueEvalBody) {
        this.onTrueEvalBody = onTrueEvalBody;
    }

    public String getCompareWith() {
        return compareWith;
    }

    public void setCompareWith(String compareWith) {
        this.compareWith = compareWith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
    public int doStartTag() throws JspException {
        String settingValue = FeaturesUtil.getGlobalSettingValue(name);
        
        if ( onTrueEvalBody && settingValue.equals( compareWith ) ) {
            shouldEval  = true;
            return EVAL_BODY_BUFFERED;
        }
        if ( (!onTrueEvalBody) && !settingValue.equals( compareWith ) ) {
            shouldEval  = true;
            return EVAL_BODY_BUFFERED;
        }
        
        shouldEval  = false;
        return SKIP_BODY;
    }
    
    public int doEndTag() throws JspException  {
        if (shouldEval) {
            
            
            if (bodyContent==null) return  SKIP_BODY;
            if (bodyContent.getString()==null) return SKIP_BODY;
            String bodyText = bodyContent.getString();
            try {
                pageContext.getOut().print(bodyText);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        return EVAL_PAGE;
    }
    
}
