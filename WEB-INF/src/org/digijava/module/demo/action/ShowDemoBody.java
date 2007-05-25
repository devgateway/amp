package org.digijava.module.demo.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.demo.data.DataSource;
import org.digijava.module.demo.form.DemoItem;

public class ShowDemoBody
    extends Action {

  private static Logger logger = Logger.getLogger(ShowDemoBody.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws
      java.lang.Exception {

    DemoItem demoForm = (DemoItem) form;
    Long itemId = (Long) demoForm.getItemId();

    DemoItem tmpForm = (DemoItem) DataSource.getItem( itemId.longValue() );
    tmpForm.copy ( demoForm );

    String instanceId = RequestUtils.getModuleInstance(request).getInstanceName();

    if ( ! demoForm.getInstanceId().equals( instanceId ) ) {

         throw new JspException("ERROR: item # " + itemId +
                              "does not belong to module instance " +
                              instanceId);
    }

    return mapping.findForward("success");
  }
}
