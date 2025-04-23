package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;

public class AssignActivityForm extends ActionForm {

          private Collection activities = null;
          private Long selectedActivities[] = null;
          private Long teamId = null;
          private String teamName = null;

          public Collection getActivities() {
                     return this.activities;
          }

          public void setActivities(Collection activities) {
                     this.activities = activities;
          }

          public Long[] getSelectedActivities() {
                     return this.selectedActivities;
          }

          public void setSelectedActivities(Long selectedActivities[]) {
                     this.selectedActivities = selectedActivities;
          }

          public Long getTeamId() {
                     return this.teamId;
          }

          public void setTeamId(Long teamId) {
                     this.teamId = teamId;
          }

          public String getTeamName() {
                     return this.teamName;
          }

          public void setTeamName(String teamName) {
                     this.teamName = teamName;
          }
}
