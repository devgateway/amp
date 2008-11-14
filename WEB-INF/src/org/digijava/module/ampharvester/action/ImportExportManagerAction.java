package org.digijava.module.ampharvester.action;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.ampharvester.api.ExportManager;
import org.digijava.module.ampharvester.api.ImportManager;
import org.digijava.module.ampharvester.form.ImportExportManagerForm;
import org.digijava.module.ampharvester.jaxb10.ActivityType;
import org.digijava.module.ampharvester.util.DbUtil;
import org.digijava.module.ampharvester.util.XmlTransformerHelper;

public class ImportExportManagerAction extends DispatchAction {
  private static Logger log = Logger.getLogger(ImportExportManagerAction.class);

  public ActionForward load(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) {
    String forward = "default";
    ImportExportManagerForm bean = (ImportExportManagerForm)form;
    bean.setErrorLog(null);
    try {
      bean.setTeamList(DbUtil.getAmpTeam(null));
    } catch (Exception ex) {
      log.error(ex);
    }

    return mapping.findForward(forward);
  }

  public ActionForward error(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) {
    ImportExportManagerForm bean = (ImportExportManagerForm)form;

    response.setContentType("text/file");
    response.setHeader("content-disposition", "attachment; filename=errorLog.txt");
    ServletOutputStream outputStream = null;
    try {
      outputStream = response.getOutputStream();
      outputStream.println(bean.getErrorLog().toString());

    } catch (Exception ex) {
      log.error(ex);
    }

    return null;
  }

  public ActionForward export(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) {

    ImportExportManagerForm bean = (ImportExportManagerForm)form;
    bean.setErrorLog(null);

    List activityList = null;

    response.setContentType("text/xml");
    response.setHeader("content-disposition", "attachment; filename=exportActivities.xml"); // file neme will generate by date
    ServletOutputStream outputStream = null;
    try {
      outputStream = response.getOutputStream();

//      activityList = new LinkedList(ActivityUtil.getAllActivitiesList());
      String oql = " from " + AmpActivity.class.getName() + " as obj "; //where obj.ampActivityId=1";
      activityList = DbUtil.loadList(oql, null);
      List<ActivityType> activities = new LinkedList<ActivityType>();

      for (Object elem : activityList) {
        activities.add(ExportManager.getXmlActivity((AmpActivity)elem, null));
      }

      outputStream.println(XmlTransformerHelper.marshal(activities));
    } catch (Exception ex) {
      try {
        StringBuffer sp = new StringBuffer("error ocure while exporting Activityes");
        sp.append("\n");
        sp.append(ex.getMessage());
        sp.append("\n");

        for (int i = 0; i < ex.getStackTrace().length; i++) {
          sp.append(ex.getStackTrace()[i].toString());
          sp.append("\n");
        }

        sp.append("\n");
        outputStream.println(sp.toString());
      } catch (Exception ex1) {
        log.error("ImportExportManagerAction.export.error.out", ex1);
      }
      log.error("ImportExportManagerAction.export.error", ex);
    }
    return null;

  }

  public ActionForward upload(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response) {

    ImportExportManagerForm bean = (ImportExportManagerForm)form;
    bean.setErrorLog(null);
    String forward = "default";

    try {
      ImportManager iManager = new ImportManager(bean.getUploadFile().getFileData());
      if (bean.getSelectedAmpTeamId() != null) {
        AmpTeam ampTeam = DbUtil.getAmpTeamById(Long.parseLong(bean.getSelectedAmpTeamId()), null);
        bean.setErrorLog(iManager.startImportHttp(bean.getSelectedAmpTeamId(), ampTeam));
      }
    } catch (Exception ex) {
      log.error("ImportExportManagerAction.import.error", ex);
      StringBuffer sp = new StringBuffer("Unknow Error ocure while import");
      sp.append(ex.getMessage());
      sp.append("\n");

      for (int i = 0; i < ex.getStackTrace().length; i++) {
        sp.append(ex.getStackTrace()[i].toString());
        sp.append("\n");
      }

      sp.append("\n");
      bean.setErrorLog(sp);
    }

    return mapping.findForward(forward);
  }

}
