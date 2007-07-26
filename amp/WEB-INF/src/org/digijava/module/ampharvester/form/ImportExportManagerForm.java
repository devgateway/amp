package org.digijava.module.ampharvester.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpTeam;

public class ImportExportManagerForm extends ActionForm{

  private List<AmpTeam> teamList = null;
  private FormFile uploadFile = null;
  private String selectedAmpTeamId = null;
  private StringBuffer errorLog = null;


  public ImportExportManagerForm(){
  }

  public FormFile getUploadFile() {
    return uploadFile;
  }

  public List getTeamList() {
    return teamList;
  }

  public String getSelectedAmpTeamId() {
    return selectedAmpTeamId;
  }

  public StringBuffer getErrorLog() {
    return errorLog;
  }

  public void setUploadFile(FormFile uploadFile) {
    this.uploadFile = uploadFile;
  }

  public void setTeamList(List teamList) {
    this.teamList = teamList;
  }

  public void setSelectedAmpTeamId(String selectedAmpTeamId) {
    this.selectedAmpTeamId = selectedAmpTeamId;
  }

  public void setErrorLog(StringBuffer errorLog) {
    this.errorLog = errorLog;
  }
}
