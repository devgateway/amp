package org.digijava.module.widget.action;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.widget.form.SectorByDonorTeaserForm;

public class GetSectoByDonorWidgetMap extends Action {

    private static Logger logger = Logger.getLogger(GetSectoByDonorWidgetMap.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws DgException {
  try {

        
     SectorByDonorTeaserForm tForm = (SectorByDonorTeaserForm)form;
           
        response.setContentType("text/xml");

        HttpSession session = request.getSession();

		ChartUtil.GraphMapRecord rec= (ChartUtil.GraphMapRecord) session.getAttribute(ChartUtil.LATEST_GRAPH_MAP);

		if (rec !=null){
			if (tForm.getTimestamp()==null||(tForm.getTimestamp() !=null && tForm.getTimestamp().equals(rec.timestamp))){
				try {
					response.getWriter().print(rec.map);
				} catch (IOException e) {
					// TODO handle this exception
					e.printStackTrace();
				}
			}

		}

      

     
        } catch (Exception ex) {
                logger.error(ex);
            // TODO handle this exception

            throw new DgException(ex);

        }


        return null;
    }
}
