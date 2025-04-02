package org.digijava.module.gpi.helper.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gpi.helper.row.GPIReport9bRow;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;
import org.digijava.module.gpi.util.GPIConstants;

public class GPIReport9bExport extends GPIAbstractExport {

    public GPIReport9bExport(Site site, String langcode, String currency) {
        super(site, langcode, currency);
    }

    @Override
    public Collection generateDataSource(Collection<GPIReportAbstractRow> rows) throws Exception {
        Iterator iter = rows.iterator();
        ArrayList<AuxRow3> list = new ArrayList();
        while (iter.hasNext()) {
            GPIReport9bRow row = (GPIReport9bRow) iter.next();
            AuxRow3 auxRow = new AuxRow3();
            auxRow.setDonorGroup(row.getDonorGroup().getOrgGrpName());
            auxRow.setYear(new Integer(row.getYear()).toString());
            auxRow.setColumn1(row.getColumn1DisplayValue());
            auxRow.setColumn2(row.getColumn2DisplayValue());            
            auxRow.setColumn3(row.getColumn3DisplayValue());
            list.add(auxRow);
        }

        return list;
    }

    public Map getParameters(int year) throws Exception {
        Map<String, String> parameters = new HashMap();
        parameters.put("GPI_REPORT_NUMBER", TranslatorWorker.translateText("GPI", this.getLangCode(), this
                .getSite().getId())
                + " "
                + GPIConstants.GPI_REPORT_9b
                + " "
                + TranslatorWorker.translateText("Report", this.getLangCode(), this.getSite().getId()));
        parameters.put("GPI_COL1", TranslatorWorker.translateText("Donors", this.getLangCode(), this.getSite().getId()));
        parameters.put("GPI_COL2", TranslatorWorker.translateText("Disbursement Year", this.getLangCode(), this
                .getSite().getId()));
        parameters.put("GPI_COL3", TranslatorWorker.translateText(
                "Disbursements Using Country PFM System", this.getLangCode(), this
                        .getSite().getId()));
        parameters.put("GPI_COL4", TranslatorWorker.translateText("All Disbursements",
                this.getLangCode(), this.getSite().getId()));
        parameters.put("GPI_COL5", TranslatorWorker.translateText(
                "Indicator 9b", this.getLangCode(),
                this.getSite().getId()));
        parameters.put("GPI_LAST_YEAR", new Integer(year).toString());
        
        String units = "";
        switch (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS)) {
        case "0":
            units = "All the amounts are in units";
            break;
        case "1":
            units = "All the amounts are in thousands (000)";
            break;
        case "2":
            units = "All the amounts are in millions (000000)";
            break;
        }
        parameters.put("GPI_CURRENCY_MSG", TranslatorWorker.translateText(units) + " " + this.getCurrency());
        
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
