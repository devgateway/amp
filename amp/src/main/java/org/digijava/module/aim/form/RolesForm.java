package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;

public class RolesForm extends ActionForm {

          private Collection roles;
          private Collection pages;

          public Collection getRoles() {
                     return (this.roles);
          }

          public void setRoles(Collection roles) {
                     this.roles = roles;
          }

          public Collection getPages() {
                     return pages;
          }

          public void setPages(Collection pages) {
                     this.pages = pages;
          }

}



