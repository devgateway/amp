package org.digijava.module.aim.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;

@Deprecated
public class QuarterlyDiscrepancyFilter extends TilesAction {
    private static Logger logger = Logger.getLogger(QuarterlyDiscrepancyFilter.class);
    
    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)  
                                 throws IOException,ServletException    {
                                    

        return null;                        
    }
}   
