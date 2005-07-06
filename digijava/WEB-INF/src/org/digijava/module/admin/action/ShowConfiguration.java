package org.digijava.module.admin.action;




import java.io.*;

import org.apache.struts.action.*;
import org.digijava.module.admin.form.*;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import javax.servlet.ServletException;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.kernel.util.RequestUtils;

public class ShowConfiguration extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
      ConfigurationForm configurationForm = (ConfigurationForm) form;
      String forward = "configPage";

      Site site = RequestUtils.getSite(request);

          //SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
          //    CURRENT_SITE);
          if (site == null) {
            throw new ServletException("Unknown site");
          }

           // Get context parameters from configuration XML file and prepare them
          // to insert into tiles context
          ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);
          configurationForm.setXmlString(viewConfig.getXML());



      return mapping.findForward(forward);
    }
}