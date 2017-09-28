package org.digijava.module.aim.helper;

import java.io.Serializable;

public class Sector implements Serializable{

          private Long sectorId;
          private String sectorName;
          private Long orgId;
          private String orgName;
          

          public Sector() {}

          public Sector(Long sectorId,
                                String sectorName,
                                Long orgId,
                                String orgName) {

                     setSectorId(sectorId);
                     setSectorName(sectorName);
                     setOrgId(orgId);
                     setOrgName(orgName);
          }

          public Long getSectorId() {
                     return (this.sectorId);
          }

          public void setSectorId(Long sectorId) {
                     this.sectorId = sectorId;
          }

          public String getSectorName() {
                     return (this.sectorName);
          }

          public void setSectorName(String sectorName) {
                     this.sectorName = sectorName;
          }
          
          public Long getOrgId() {
                     return (this.orgId);
          }
          
          public void setOrgId(Long orgId) {
                     this.orgId = orgId;
          }
          
          public String getOrgName() {
                     return (this.orgName);
          }
          
          public void setOrgName(String orgName) {
                     this.orgName = orgName;
          }
}
