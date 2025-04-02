package org.digijava.module.gpi.helper.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.gpi.helper.row.GPIReport1Row;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;
import org.digijava.module.gpi.util.GPIConstants;

public class GPIReport1Export extends GPIAbstractExport {

    public GPIReport1Export(Site site, String langcode, String currency) {
        super(site, langcode, currency);
    }

    @Override
    public Collection generateDataSource(Collection<GPIReportAbstractRow> rows) throws Exception {
        Iterator iter = rows.iterator();
        ArrayList<AuxRow3> list = new ArrayList();
        while (iter.hasNext()) {
            GPIReport1Row row = (GPIReport1Row) iter.next();
            AuxRow3 auxRow = new AuxRow3();
            auxRow.setDonorGroup(row.getDonorGroup().getOrgGrpName());
            auxRow.setYear(new Integer(row.getYear()).toString());
            if(row.getColumn1() != null) {
                auxRow.setColumn1(row.getColumn1().toString());
            } else {
                auxRow.setColumn1(GPIConstants.NO_DATA);
            }
            if(row.getColumn2() != null) {
                auxRow.setColumn2(row.getColumn2().toString());
            } else {
                auxRow.setColumn2(GPIConstants.NO_DATA);
            }
            auxRow.setColumn3(new Integer(new Float(row.getColumn3()).intValue()).toString() + "%");
            list.add(auxRow);
        }

        return list;
    }

    public Map getParameters(int year) throws Exception {
        Map<String, String> parameters = new HashMap();
        parameters.put("GPI_REPORT_NUMBER", TranslatorWorker.translateText("GPI", this.getLangCode(), this
                .getSite().getId())
                + " "
                + GPIConstants.GPI_REPORT_1
                + " "
                + TranslatorWorker.translateText("Report", this.getLangCode(), this.getSite().getId()));
        parameters.put("GPI_COL1", TranslatorWorker.translateText("Donors", this.getLangCode(), this.getSite().getId()));
        parameters.put("GPI_COL2", TranslatorWorker.translateText("Disbursement Year", this.getLangCode(), this
                .getSite().getId()));
        parameters.put("GPI_COL3", TranslatorWorker.translateText(
                "Number of projects using country results framework", this.getLangCode(), this
                        .getSite().getId()));
        parameters.put("GPI_COL4", TranslatorWorker.translateText("Total number of projects",
                this.getLangCode(), this.getSite().getId()));
        parameters.put("GPI_COL5", TranslatorWorker.translateText(
                "Indicator 1", this.getLangCode(),
                this.getSite().getId()));
        parameters.put("GPI_LAST_YEAR", new Integer(year).toString());
        parameters.put("GPI_CURRENCY_MSG", "");
        return parameters;
    }

    public class AuxRow3 {
        private String donorGroup;
        private String year;
        private String column1;
        private String column2;
        private String column3;

        public String getDonorGroup() {
            return donorGroup;
        }

        public void setDonorGroup(String donorGroup) {
            this.donorGroup = donorGroup;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getColumn1() {
            return column1;
        }

        public void setColumn1(String column1) {
            this.column1 = column1;
        }

        public String getColumn2() {
            return column2;
        }

        public void setColumn2(String column2) {
            this.column2 = column2;
        }

        public String getColumn3() {
            return column3;
        }

        public void setColumn3(String column3) {
            this.column3 = column3;
        }

        public AuxRow3() {
        }
    }
}
