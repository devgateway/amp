package org.digijava.module.demo.action;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.tiles.*;
import org.apache.struts.tiles.actions.*;
import org.digijava.kernel.*;
import org.digijava.module.demo.data.*;
import org.digijava.module.demo.form.*;
import java.util.Iterator;


public class FirstTeaser
    extends TilesAction {

  public ActionForward execute(ComponentContext context,
                               ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {

        DemoItemList demoItemList = (DemoItemList) form;
        String instanceId = (String)context.getAttribute(Constants.MODULE_INSTANCE);

        if (instanceId == null) {
          throw new ServletException ("module instance must be set for the demo items");
        }

        // set news data source in form bean
        demoItemList.setItems(DataSource.getItems( instanceId ));

        return null;

  }
}
