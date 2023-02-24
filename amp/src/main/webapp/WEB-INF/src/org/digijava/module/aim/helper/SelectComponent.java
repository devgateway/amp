/*
 * Created on 16/03/2005
 * @author akashs
 */

package org.digijava.module.aim.helper;

import java.util.Collection;

public class SelectComponent {

        private String title;
        private String amount;
        private String reportingDate;
        private String currency;
        private Long cid;
        private Collection physicalProgress;
        public SelectComponent() {}
        
        public SelectComponent(Long id) {
            cid = id;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getAmount() {
            return amount;
        }
        
        public String getReportingDate() {
            return reportingDate;
        }
        
        public String getCurrency() {
            return currency;
        }

        public Long getCid() {
            return cid;
        }
        
        public Collection getPhysicalProgress() {
            return physicalProgress;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        
        public void setAmount(String amount) {
            this.amount = amount;
        }
        
        public void setReportingDate(String date) {
            this.reportingDate = date;
        }
        
        public void setCurrency(String currency) {
            this.currency = currency;
        }
        
        public void setCid(Long cid) {
            this.cid = cid;
        }
        
        public void setPhysicalProgress(Collection physicalProgress) {
            this.physicalProgress = physicalProgress;
        }
}

