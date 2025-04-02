package org.digijava.module.aim.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.NpdGraphForm;
import org.digijava.module.aim.util.ChartUtil;

public class GetNPDGraphMap extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
        NpdGraphForm npdForm = (NpdGraphForm) form;
        
        response.setContentType("text/plain");
        
        HttpSession session = request.getSession();

        ChartUtil.GraphMapRecord rec= (ChartUtil.GraphMapRecord) session.getAttribute(ChartUtil.LATEST_GRAPH_MAP);
        
        if (rec !=null && npdForm.getTimestamp() !=null){
            if (npdForm.getTimestamp() !=null && npdForm.getTimestamp().equals(rec.timestamp)){
                try {
                    response.getWriter().print(rec.map);
                } catch (IOException e) {
                    // TODO handle this exception
                    e.printStackTrace();
                }
            }
            
        }
        
        return null;
    }

}
