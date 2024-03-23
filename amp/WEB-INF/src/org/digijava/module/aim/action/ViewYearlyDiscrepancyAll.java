package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Deprecated
public class ViewYearlyDiscrepancyAll extends TilesAction   {
    private static Logger logger = Logger.getLogger(ViewYearlyDiscrepancyAll.class);
    
    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)  
                                 throws IOException,ServletException    {
                                    

        return null;                        
    }
}   
