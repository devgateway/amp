package org.digijava.module.aim.services.publicview.conf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * User: flyer
 * Date: 9/12/12
 * Time: 1:07 PM
 */

@XmlRootElement(name = "report-table-config")
public class Configuration {
    private String baseUrl;
    private String workFileDir;
    private List<Table> tables;

    @XmlElementWrapper(name="tables")
    @XmlElement(name="table")
    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    @XmlAttribute(name="baseUrl")
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @XmlAttribute(name="workFileDir")
    public String getWorkFileDir() {
        return workFileDir;
    }

    public void setWorkFileDir(String workFileDir) {
        this.workFileDir = workFileDir;
    }

    @XmlType(name = "table")
    public static class Table {
        private String name;
        private String reportUrl;
        private String xslFile;
        private String htmlFile;
        private boolean budgetExport;

        @XmlAttribute(name="budgetExport")
        public boolean isBudgetExport() {
            return budgetExport;
        }

        public void setBudgetExport(boolean budgetExport) {
            this.budgetExport = budgetExport;
        }

        @XmlAttribute(name="name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlElement(name = "xsl-file")
        public String getXslFile() {
            return xslFile;
        }

        public void setXslFile(String xslFile) {
            this.xslFile = xslFile;
        }

        @XmlElement(name = "html-file")
        public String getHtmlFile() {
            return htmlFile;
        }

        public void setHtmlFile(String htmlFile) {
            this.htmlFile = htmlFile;
        }

        @XmlElement(name = "report-url")
        public String getReportUrl() {
            return reportUrl;
        }

        public void setReportUrl(String reportUrl) {
            this.reportUrl = reportUrl;
        }
    }
}
