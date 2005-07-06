package org.digijava.module.admin.form;

import org.apache.struts.action.ActionForm;

public class ConfigurationForm extends ActionForm {
  private String xmlString;

  public String getXmlString() {
    return xmlString;
  }
  public void setXmlString(String xmlString) {
    this.xmlString = xmlString;
  }
}