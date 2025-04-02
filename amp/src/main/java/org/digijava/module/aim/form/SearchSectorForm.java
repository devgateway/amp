package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class SearchSectorForm extends ActionForm {

          private String searchOn = null;
          private String searchKey = null;
          private Collection results = null;
          private Collection pages = null;

          public String getSearchOn() {
                     return (this.searchOn);
          }

          public void setSearchOn(String searchOn) {
                     this.searchOn = searchOn;
          }

          public String getSearchKey() {
                     return (this.searchKey);
          }

          public void setSearchKey(String searchKey) {
                     this.searchKey = searchKey;
          }

          public Collection getResults() {
                     return (this.results);
          }

          public void setResults(Collection results) {
                     this.results = results;
          }
          
          public Collection getPages() {
                     return (this.pages);
          }

          public void setPages(Collection pages) {
                     this.pages = pages;
          }
}
