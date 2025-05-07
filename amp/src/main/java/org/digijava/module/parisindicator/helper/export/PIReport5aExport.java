package org.digijava.module.parisindicator.helper.export;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.parisindicator.helper.row.PIReport5aRow;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class PIReport5aExport extends PIAbstractExport implements PIExportExtraOperations {

    private String subReportDirectory;
    private Object[][] miniReportData;
    
    public PIReport5aExport(Site site, String langcode, String currency) {
        super(site, langcode, currency);
    }

    @Override
    public Collection generateDataSource(Collection<PIReportAbstractRow> rows) throws Exception {
        Iterator iter = rows.iterator();
        ArrayList<AuxRow5a> list = new ArrayList();
        while (iter.hasNext()) {
            PIReport5aRow row = (PIReport5aRow) iter.next();
            AuxRow5a auxRow = new AuxRow5a();
            auxRow.setDonorGroup(row.getDonorGroup().getOrgGrpName());
            auxRow.setYear(new Integer(row.getYear()).toString());
            auxRow.setColumn1(FormatHelper.getDecimalFormat().format(row.getColumn1()));
            auxRow.setColumn2(FormatHelper.getDecimalFormat().format(row.getColumn2()));
            auxRow.setColumn3(FormatHelper.getDecimalFormat().format(row.getColumn3()));
            auxRow.setColumn4(FormatHelper.getDecimalFormat().format(row.getColumn4()));
            auxRow.setColumn5(FormatHelper.getDecimalFormat().format(row.getColumn5()));
            auxRow.setColumn6(FormatHelper.getDecimalFormat().format(row.getColumn6()) + "%");
            auxRow.setColumn7(FormatHelper.getDecimalFormat().format(row.getColumn7()) + "%");
            list.add(auxRow);
        }
        return list;
    }

    public Map getParameters(int year) throws Exception {
        Map<String, Object> parameters = new HashMap();
        parameters.put("PI_REPORT_NUMBER", TranslatorWorker.translateText("Paris Indicator", this.getLangCode(), this
                .getSite().getId())
                + " "
                + PIConstants.PARIS_INDICATOR_REPORT_5a
                + " "
                + TranslatorWorker.translateText("Report", this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL1", TranslatorWorker.translateText("Donors", this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL2", TranslatorWorker.translateText("Disbursement Year", this.getLangCode(), this
                .getSite().getId()));
        parameters.put("PI_COL3", TranslatorWorker.translateText(
                "Aid flows to the goverment sector that use national budget execution procedures", this.getLangCode(),
                this.getSite().getId()));
        parameters.put("PI_COL4", TranslatorWorker.translateText(
                "Aid flows to the goverment sector that use national financial reporting procedures", this
                        .getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL5", TranslatorWorker.translateText(
                "Aid flows to the goverment sector that use national financial auditing procedures",
                this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL6", TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("ODA that uses all 3 national PFM",
                this.getLangCode(), this.getSite().getId())));
        parameters.put("PI_COL7", TranslatorWorker.translateText("Total Aid flows disbursed to the government sector",
                this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL8", TranslatorWorker.translateText(
                "Proportion of aid flows to the government sector using one of the 3 country PFM systems", this
                        .getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL9", TranslatorWorker.unicodeToUTF8((TranslatorWorker.translateText(
                "Proportion of aid flows to the government sector using all the 3 country PFM systems", this
                        .getLangCode(), this.getSite().getId()))));
        parameters.put("PI_LAST_YEAR", new Integer(year).toString());
        parameters.put("PI_SUBREPORT_DIR", this.subReportDirectory);
        parameters.put("PI_MINI_DATASOURCE", this.miniReportData);
        parameters.put("PI_CURRENCY_MSG", TranslatorWorker.translateText("All the amounts are in thousands (000) ",
                this.getLangCode(), this.getSite().getId())
                + " " + this.getCurrency());
        return parameters;
    }

    public class AuxRow5a {
        private String donorGroup;
        private String year;
        private String column1;
        private String column2;
        private String column3;
        private String column4;
        private String column5;
        private String column6;
        private String column7;

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

        public AuxRow5a() {
        }

        public String getColumn4() {
            return column4;
        }

        public void setColumn4(String column4) {
            this.column4 = column4;
        }

        public String getColumn5() {
            return column5;
        }

        public void setColumn5(String column5) {
            this.column5 = column5;
        }

        public String getColumn6() {
            return column6;
        }

        public void setColumn6(String column6) {
            this.column6 = column6;
        }

        public String getColumn7() {
            return column7;
        }

        public void setColumn7(String column7) {
            this.column7 = column7;
        }
    }

    @Override
    public void createJrxmlFromClass(String file, int startYear, int endYear) throws Exception {
        int cells = ((endYear - startYear + 1) * 2) + 1;
        FileOutputStream out = new FileOutputStream(file);
        PrintStream ps = new PrintStream(out);

        ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"  ?>");
        ps.println("<!-- Created with iReport - A designer for JasperReports -->");
        ps
                .println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
        ps.println("<jasperReport");
        ps.println("         name='ParisIndicator5a_sub'");
        ps.println("         columnCount='1'");
        ps.println("         printOrder='Vertical'");
        ps.println("         orientation='Portrait'");
        ps.println("         pageWidth='309'");
        ps.println("         pageHeight='802'");
        ps.println("         columnWidth='309'");
        ps.println("         columnSpacing='0'");
        ps.println("         leftMargin='0'");
        ps.println("         rightMargin='0'");
        ps.println("         topMargin='0'");
        ps.println("         bottomMargin='0'");
        ps.println("         whenNoDataType='AllSectionsNoDetail'");
        ps.println("         isTitleNewPage='false'");
        ps.println("         isSummaryNewPage='false'>");

        String fieldName;
        for (int k = 1; k <= cells; k++) {
            fieldName = "m" + k;
            ps.println("<field name='" + fieldName + "' class='java.lang.String'/>");
        }
        ps
                .println("<group name='Data' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
        ps.println("<groupExpression><![CDATA[$F{m1}]]></groupExpression>");
        ps.println("<groupHeader>");
        ps.println("</groupHeader>");
        ps.println("<groupFooter>");
        ps.println("<band height='0'  isSplitAllowed='true' >");
        ps.println("</band>");
        ps.println("</groupFooter>");
        ps.println("</group>");

        ps.println("        <background>");
        ps.println("            <band height='0'  isSplitAllowed='true' >");
        ps.println("            </band>");
        ps.println("        </background>");
        ps.println("        <title>");
        ps.println("            <band height='0'  isSplitAllowed='true' >");
        ps.println("            </band>");
        ps.println("        </title>");
        ps.println("        <pageHeader>");
        ps.println("            <band height='0'  isSplitAllowed='true' >");
        ps.println("            </band>");
        ps.println("        </pageHeader>");
        
        // Build column headers.
        int yearHeaderWidth = 58;
        ps.println("        <columnHeader>");
        ps.println("            <band height='41' isSplitAllowed='true' >");
        ps.println("                <staticText>");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='0'");
        ps.println("                        y='0'");
        ps.println("                        width='131'");
        ps.println("                        height='40'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#CCCCCC'");
        ps.println("                        key='staticText-1'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps.println("                    <box topBorder='1Point' topBorderColor='#000000' leftBorder='1Point' leftBorderColor='#000000' rightBorder='1Point' rightBorderColor='#000000' bottomBorder='1Point' bottomBorderColor='#000000'/>");
        ps.println("                    <textElement textAlignment='Center'>");
        ps.println("                        <font isBold='true'/>");
        ps.println("                    </textElement>");
        ps.println("                    <text><![CDATA["+TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("Percent of ODA using all three partner's PFM procedures", this.getLangCode(), this.getSite().getId()))+"]]></text>");
        ps.println("                </staticText>");
        ps.println("                <staticText>");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='131'");
        ps.println("                        y='0'");
        ps.println("                        width='" + (yearHeaderWidth * (endYear - startYear + 1)) + "'");
        ps.println("                        height='26'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#CCCCCC'");
        ps.println("                        key='staticText-2'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps.println("                    <box topBorder='1Point' topBorderColor='#000000' leftBorder='1Point' leftBorderColor='#000000' rightBorder='1Point' rightBorderColor='#000000' bottomBorder='1Point' bottomBorderColor='#000000'/>");
        ps.println("                    <textElement textAlignment='Center'>");
        ps.println("                        <font isBold='true'/>");
        ps.println("                    </textElement>");
        ps.println("                    <text><![CDATA["+ TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("Percent of donors that use all three partner's PFM procedures", this.getLangCode(), this.getSite().getId())) + "]]></text>");
        ps.println("                </staticText>");
        //Build header for each year.
        int k = 2;
        for(int i = 0; i < endYear - startYear + 1; i++) {
            if(k % 2 == 0) {
                ps.println("                <textField>");
                ps.println("                    <reportElement");
                ps.println("                        mode='Opaque'");
                ps.println("                        x='" + (131 + (yearHeaderWidth * i)) + "'");
                ps.println("                        y='26'");
                ps.println("                        width='" + yearHeaderWidth + "'");
                ps.println("                        height='14'");
                ps.println("                        forecolor='#000000'");
                ps.println("                        key='staticText-3'");
                ps.println("                        stretchType='NoStretch'");
                ps.println("                        positionType='FixRelativeToTop'");
                ps.println("                        isPrintRepeatedValues='true'");
                ps.println("                        isRemoveLineWhenBlank='false'");
                ps.println("                        isPrintInFirstWholeBand='false'");
                ps.println("                        isPrintWhenDetailOverflows='false'/>");
                ps.println("                    <box topBorder='1Point' topBorderColor='#000000' leftBorder='1Point' leftBorderColor='#000000' rightBorder='1Point' rightBorderColor='#000000' bottomBorder='1Point' bottomBorderColor='#000000'/>");
                ps.println("                    <textElement textAlignment='Center'>");
                ps.println("                        <font isBold='true'/>");
                ps.println("                    </textElement>");
                ps.println("                    <textFieldExpression><![CDATA[$F{m" + k + "}]]></textFieldExpression>");
                ps.println("                </textField>");
                i--;
            }
            k++;
        }
        ps.println("            </band>");
        ps.println("        </columnHeader>");
        
        //Build details band.
        ps.println("        <detail>");
        ps.println("            <band height='16' isSplitAllowed='true' >");
        k = 0;
        for(int i = 0; i <= ((endYear - startYear + 1) * 2) + 1; i++) {
            int offsetX = 131 + (yearHeaderWidth * k);
            if(i % 2 != 0) {
                if(i == 1) {
                    ps.println("                <textField>");
                    ps.println("                    <reportElement");
                    ps.println("                        x='0'");
                    ps.println("                        y='0'");
                    ps.println("                        width='131'");
                    ps.println("                        height='16'");
                    ps.println("                        key='staticText-" + i + "'/>");
                    ps.println("                    <box topBorder='1Point' topBorderColor='#000000' leftBorder='1Point' leftBorderColor='#000000' rightBorder='1Point' rightBorderColor='#000000' bottomBorder='1Point' bottomBorderColor='#000000'/>");
                    ps.println("                    <textElement textAlignment='Center'>");
                    ps.println("                        <font isBold='true'/>");
                    ps.println("                    </textElement>");
                    ps.println("                    <textFieldExpression><![CDATA[$F{m" + i + "}]]></textFieldExpression>");
                    ps.println("                </textField>");
                } else {
                    ps.println("                <textField>");
                    ps.println("                    <reportElement");
                    ps.println("                        x='" + offsetX + "'");
                    ps.println("                        y='0'");
                    ps.println("                        width='" + yearHeaderWidth + "'");
                    ps.println("                        height='16'");
                    ps.println("                        key='staticText-" + i + "'/>");
                    ps.println("                    <box topBorder='1Point' topBorderColor='#000000' leftBorder='1Point' leftBorderColor='#000000' rightBorder='1Point' rightBorderColor='#000000' bottomBorder='1Point' bottomBorderColor='#000000'/>");
                    ps.println("                    <textElement textAlignment='Center'>");
                    ps.println("                        <font isBold='true'/>");
                    ps.println("                    </textElement>");
                    ps.println("                    <textFieldExpression><![CDATA[$F{m" + i + "}]]></textFieldExpression>");
                    ps.println("                </textField>");
                    k++;
                }
            }
        }
        ps.println("            </band>");
        ps.println("        </detail>");
        ps.println("        <columnFooter>");
        ps.println("            <band height='0'  isSplitAllowed='true' >");
        ps.println("            </band>");
        ps.println("        </columnFooter>");
        ps.println("        <pageFooter>");
        ps.println("            <band height='0'  isSplitAllowed='true' >");
        ps.println("            </band>");
        ps.println("        </pageFooter>");
        ps.println("        <summary>");
        ps.println("            <band height='0'  isSplitAllowed='true' >");
        ps.println("            </band>");
        ps.println("        </summary>");
        ps.println("</jasperReport>");

        ps.close();
        out.close();
    }

    @Override
    public Object[][] generateDataSource(Collection<PIReportAbstractRow> rows, int startYear, int endYear)
            throws Exception {
        return null;
    }

    @Override
    public Object[][] generateDataSource(int[][] rows, int startYear, int endYear) throws Exception {
        int yearRange = endYear - startYear + 1;
        int nRows = 4;
        Object[][] data = new Object[nRows][(yearRange * 2) + 1];
        int yearCounter = startYear;
        for(int i = 0; i < nRows; i++) {
            String rowTitle = "";
            switch (i) {
            case 0:
                rowTitle = "Less than 10%";
                break;
            case 1:
                rowTitle = "From 10 to 50%";
                break;
            case 2:
                rowTitle = "From 50 to 90%";
                break;
            case 3:
                rowTitle = "More than 90%";
                break;
            }
            data[i][0] = TranslatorWorker.translateText(rowTitle, this.getLangCode(), this.getSite().getId());
            int k = 1;
            for (int j = 0; j < rows[i].length; j++) {
                if (k % 2 == 0) {
                    data[i][k] = "" + rows[i][j];
                } else {
                    data[i][k] = "" + yearCounter;
                    j--;
                    yearCounter++;
                }
                k++;
            }
        }
        return data;
    }

    public String getSubReportDirectory() {
        return subReportDirectory;
    }

    public void setSubReportDirectory(String subReportDirectory) {
        this.subReportDirectory = subReportDirectory;
    }

    public Object[][] getMiniReportData() {
        return miniReportData;
    }

    public void setMiniReportData(Object[][] miniReportData) {
        this.miniReportData = miniReportData;
    }
}
