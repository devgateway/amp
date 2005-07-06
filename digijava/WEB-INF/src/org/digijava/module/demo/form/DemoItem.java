package org.digijava.module.demo.form;

import org.apache.struts.action.ActionForm;

public class DemoItem extends ActionForm {

  private Long itemId;
  private String title;
  private String content;
  private String language;
  private java.util.Date releaseDate;
  private java.util.Date arcDate;
  private String instanceId;

  public DemoItem () {}

  public String getContent() { return content; }
  public void setContent(String content) {
    this.content = content;
  }


  public Long getItemId() { return itemId; }
  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  public String getLanguage() {
    return language;
  }
  public void setLanguage(String language) {
    this.language = language;
  }
  public java.util.Date getReleaseDate() {
    return releaseDate;
  }
  public void setReleaseDate(java.util.Date releaseDate) {
    this.releaseDate = releaseDate;
  }
  public java.util.Date getArcDate() {
    return arcDate;
  }
  public void setArcDate(java.util.Date arcDate) {
    this.arcDate = arcDate;
  }

  public String getInstanceId() { return instanceId; }
  public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
  }

  public boolean copy( DemoItem toObj ) {
      toObj.setItemId ( itemId );
      toObj.setTitle ( title );
      toObj.setContent ( content );
      toObj.setLanguage ( language );
      toObj.setReleaseDate ( releaseDate );
      toObj.setArcDate ( arcDate );
      toObj.setInstanceId ( instanceId );
      return true;
  }

}