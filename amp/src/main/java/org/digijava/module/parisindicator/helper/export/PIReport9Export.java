package org.digijava.module.parisindicator.helper.export;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.parisindicator.helper.row.PIReport9Row;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;

import java.util.*;

public class PIReport9Export extends PIAbstractExport {

    public PIReport9Export(Site site, String langcode, String currency) {
        super(site, langcode, currency);
    }

    @Override
    public Collection generateDataSource(Collection<PIReportAbstractRow> rows) throws Exception {
        Iterator iter = rows.iterator();
        ArrayList<AuxRow9> list = new ArrayList();
        while (iter.hasNext()) {
            PIReport9Row row = (PIReport9Row) iter.next();
            AuxRow9 auxRow = new AuxRow9();
            auxRow.setDonorGroup(row.getDonorGroup().getOrgGrpName());
            auxRow.setYear(new Integer(row.getYear()).toString());
            auxRow.setColumn1(FormatHelper.getDecimalFormat().format(row.getColumn1()));
            auxRow.setColumn2(FormatHelper.getDecimalFormat().format(row.getColumn2()));
            auxRow.setColumn3(FormatHelper.getDecimalFormat().format(row.getColumn3()));
            auxRow.setColumn4(FormatHelper.getDecimalFormat().format(row.getColumn4()) + "%");
            list.add(auxRow);
        }
        return list;
    }

    public Map getParameters(int year) throws Exception {
        Map<String, String> parameters = new HashMap();
        parameters.put("PI_REPORT_NUMBER", TranslatorWorker.translateText("Paris Indicator", this.getLangCode(), this
                .getSite().getId())
                + " "
                + PIConstants.PARIS_INDICATOR_REPORT_9
                + " "
                + TranslatorWorker.translateText("Report", this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL1", TranslatorWorker.translateText("Donors", this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL2", TranslatorWorker.translateText("Disbursement Year", this.getLangCode(), this
                .getSite().getId()));
        parameters.put("PI_COL3", TranslatorWorker.translateText(
                "Budget support aid flows provided in the context of programme based approach", this.getLangCode(),
                this.getSite().getId()));
        parameters.put("PI_COL4", TranslatorWorker.translateText(
                "Other aid flows provided in the context of programme based approach", this.getLangCode(), this
                        .getSite().getId()));
        parameters.put("PI_COL5", TranslatorWorker.translateText("Total aid flows provided", this.getLangCode(), this
                .getSite().getId()));
        parameters.put("PI_COL6", TranslatorWorker.translateText(
                "Proportion of aid flows provided in the context of programme based approach", this.getLangCode(), this
                        .getSite().getId()));
        parameters.put("PI_LAST_YEAR", new Integer(year).toString());
        parameters.put("PI_CURRENCY_MSG", TranslatorWorker.translateText("All the amounts are in thousands (000) ",
                this.getLangCode(), this.getSite().getId())
                + " " + this.getCurrency());
        return parameters;
    }

    public class AuxRow9 {
        private String donorGroup;
        private String year;
        private String column1;
        private String column2;
        private String column3;
        private String column4;

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

        public AuxRow9() {
        }

        public String getColumn4() {
            return column4;
        }

        public void setColumn4(String column4) {
            this.column4 = column4;
        }
    }
}
