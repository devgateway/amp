package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * Returns XML of the tree of programs. Currently used in NPD page. This action
 * creates hierarchy from all programs found in DB. Members of the hierarchy are
 * objects of TreeItem class which extends HierarchyMember from kernel by adding
 * XML functionality - every item can return its own XML. Complete XML is
 * composed using this parts.
 *
 * @author Irakli Kobiashvili - ikobiashvili@picktek.com
 * @see org.digijava.module.aim.helper.TreeItem
 * @see org.digijava.kernel.util.collections.HierarchyMember
 *
 */
public class GetProgramTreeNode
    extends DispatchAction {
  private static Logger logger = Logger.getLogger(GetProgramTreeNode.class);

  protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws
      Exception {
    return doWork(mapping, form, request, response);
  }

  private ActionForward doWork(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    response.setContentType("text/xml");

    OutputStreamWriter outputStream = null;
    PrintWriter out = null;

    try {
      outputStream = new OutputStreamWriter(response.
                                            getOutputStream(), "UTF-8");
      // get All themes from DB
      Collection<AmpTheme> themes = ProgramUtil.getAllThemes(true);

      // Construct XML tree
      String xml = ProgramUtil.getThemesHierarchyXML(themes);

      // return XML
      out = new PrintWriter(outputStream, true);

      out.println(xml);

      out.close();

    }
    catch (UnsupportedEncodingException ex) {
      logger.error(ex.getMessage(), ex);
      throw ex;
    }
    catch (IOException ex) {
      logger.error(ex.getMessage(), ex);
      throw ex;
    }

    return null;
  }

}
