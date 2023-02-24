package org.digijava.module.parisindicator.helper.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.parisindicator.helper.row.PIReport4Row;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;

public class PIReport4Export extends PIAbstractExport {

    public PIReport4Export(Site site, String langcode, String currency) {
        super(site, langcode, currency);
    }

    @Override
    public Collection generateDataSource(Collection<PIReportAbstractRow> rows) throws Exception {
        Iterator iter = rows.iterator();
        ArrayList<AuxRow4> list = new ArrayList();
        while (iter.hasNext()) {
            PIReport4Row row = (PIReport4Row) iter.next();
            AuxRow4 auxRow = new AuxRow4();
            auxRow.setDonorGroup(row.getDonorGroup().getOrgGrpName());
            auxRow.setYear(new Integer(row.getYear()).toString());
            auxRow.setColumn1(FormatHelper.getDecimalFormat().format(row.getColumn1()));
            auxRow.setColumn2(FormatHelper.getDecimalFormat().format(row.getColumn2()));
            auxRow.setColumn3(FormatHelper.getDecimalFormat().format(row.getColumn3()) + "%");
            list.add(auxRow);
        }

        return list;
    }

    public Map getParameters(int year) throws Exception {
        Map<String, String> parameters = new HashMap();
        parameters.put("PI_REPORT_NUMBER", TranslatorWorker.translateText("Paris Indicator", this.getLangCode(), this
                .getSite().getId())
                + " "
                + PIConstants.PARIS_INDICATOR_REPORT_4
                + " "
                + TranslatorWorker.translateText("Report", this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL1", TranslatorWorker.translateText("Donors", this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL2", TranslatorWorker.translateText("Disbursement Year", this.getLangCode(), this
                .getSite().getId()));
        parameters.put("PI_COL3", TranslatorWorker.translateText(
                "Volume of technical co-operation for capacity development provided through co-ordinated programmes",
                this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL4", TranslatorWorker.translateText("Total volume of technical co-operation provided",
                this.getLangCode(), this.getSite().getId()));
        parameters
                .put(
                        "PI_COL5",
                        TranslatorWorker
                                .translateText(
                                        "% of TC for capacity development provided through coordinated programmes consistent with national development strategies",
                                        this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_LAST_YEAR", new Integer(year).toString());
        parameters.put("PI_CURRENCY_MSG", TranslatorWorker.translateText("All the amounts are in thousands (000) ",
                this.getLangCode(), this.getSite().getId())
                + " " + this.getCurrency());
        return parameters;
    }

    public class AuxRow4 {
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

        public AuxRow4() {
        }
    }
}
