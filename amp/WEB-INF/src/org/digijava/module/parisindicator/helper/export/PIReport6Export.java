package org.digijava.module.parisindicator.helper.export;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.parisindicator.helper.row.PIReport6Row;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PIReport6Export extends PIAbstractExport implements PIExportExtraOperations {

    public PIReport6Export(Site site, String langcode) {
        super(site, langcode, null);
    }

    @Override
    public Object[][] generateDataSource(Collection<PIReportAbstractRow> rows, int startYear, int endYear)
            throws Exception {
        int yearRange = endYear - startYear + 1;
        Object[][] data = new Object[rows.size()][(yearRange * 2) + 1];
        int yearCounter = startYear;
        Iterator iter = rows.iterator();
        int i = 0;
        while (iter.hasNext()) {
            PIReport6Row auxRow = (PIReport6Row) iter.next();
            data[i][0] = TranslatorWorker.translateText(auxRow.getDonorGroup().getOrgGrpName(), this.getLangCode(),
                    this.getSite().getId());
            int k = 1;
            for (int j = 0; j < auxRow.getYears().length; j++) {
                if (k % 2 == 0) {
                    data[i][k] = "" + auxRow.getYears()[j];
                } else {
                    data[i][k] = "" + yearCounter;
                    j--;
                    yearCounter++;
                }
                k++;
            }
            i++;
        }
        return data;
    }

    public Map getParameters(int year) throws Exception {
        Map<String, String> parameters = new HashMap();
        parameters.put("PI_REPORT_NUMBER", TranslatorWorker.translateText("Paris Indicator", this.getLangCode(), this
                .getSite().getId())
                + " "
                + PIConstants.PARIS_INDICATOR_REPORT_6
                + " "
                + TranslatorWorker.translateText("Report", this.getLangCode(), this.getSite().getId()));
        parameters.put("PI_COL1", TranslatorWorker.translateText("Donors", this.getLangCode(), this.getSite().getId()));
        return parameters;
    }

    @Override
    public synchronized void createJrxmlFromClass(String file, int startYear, int endYear) throws Exception {
        int cells = ((endYear - startYear + 1) * 2) + 1;
        int a = (cells / 2);
        int b = 80 + (a * 150);
        a = (a + 1) * 150;

        FileOutputStream out;
        PrintStream ps;

        out = new FileOutputStream(file);
        ps = new PrintStream(out);

        ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        ps
                .println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
        ps.println("<jasperReport");
        ps.println("         name='ParisIndicator6'");
        ps.println("         columnCount='1'");
        ps.println("         printOrder='Vertical'");
        ps.println("         orientation='Portrait'");
        ps.println("         pageWidth='" + a + "'");
        ps.println("         pageHeight='842'");
        ps.println("         columnWidth='535'");
        ps.println("         columnSpacing='0'");
        ps.println("         leftMargin='30'");
        ps.println("         rightMargin='30'");
        ps.println("         topMargin='20'");
        ps.println("         bottomMargin='20'");
        ps.println("         whenNoDataType='NoPages'");
        ps.println("         isTitleNewPage='false'");
        ps.println("         isSummaryNewPage='false'>");
        ps.println("    <property name='ireport.scriptlethandling' value='2' />");
        ps.println("<parameter name='qu' isForPrompting='false' class='java.lang.String'>");
        ps.println("<defaultValueExpression ><![CDATA[\"select * from my_table\"]]></defaultValueExpression>");
        ps.println("</parameter>");
        ps.println("<parameter name='PI_REPORT_NUMBER' class='java.lang.String'/>");
        ps.println("<parameter name='PI_COL1' class='java.lang.String'/>");
        ps.println("<parameter name='nam' isForPrompting='false' class='java.lang.String'>");
        ps.println("<defaultValueExpression ><![CDATA[\"zzzz\"]]></defaultValueExpression>");
        ps.println("</parameter>");
        ps.println("<queryString><![CDATA[$P!{qu}]]></queryString>");

        String fieldName;
        for (int k = 1; k <= cells; k++) {
            fieldName = "m" + k;
            ps.println("<field name='" + fieldName + "' class='java.lang.String'/>");
        }
        ps
                .println("<group  name='Data' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
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
        ps.println("            <band height='30'  isSplitAllowed='true' >");
        ps.println("                <textField>");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='2'");
        ps.println("                        y='8'");
        ps.println("                        width='250'");
        ps.println("                        height='21'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#FFFFFF'");
        ps.println("                        key='staticText-1'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps
                .println("                  <textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
        ps
                .println("                      <font fontName='DejaVu Sans' pdfFontName='Helvetica' size='18' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
        ps.println("                    </textElement>");
        ps
                .println("              <textFieldExpression class='java.lang.String'><![CDATA[$P{PI_REPORT_NUMBER}]]></textFieldExpression>");
        ps.println("                </textField>");
        ps.println("            </band>");
        ps.println("        </title>");
        ps.println("        <pageHeader>");
        ps.println("            <band height='10'  isSplitAllowed='true' >");
        ps.println("            </band>");
        ps.println("        </pageHeader>");
        ps.println("        <columnHeader>");
        ps.println("            <band height='30'  isSplitAllowed='true' >");
        ps.println("                <textField>");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='2'");
        ps.println("                        y='2'");
        ps.println("                        width='79'");
        ps.println("                        height='27'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#CCCCCC'");
        ps.println("                        key='staticText-2'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps
                .println("                  <textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
        ps
                .println("                      <font fontName='DejaVu Sans' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
        ps.println("                    </textElement>");
        ps.println("                <textFieldExpression class='java.lang.String'><![CDATA[$P{PI_COL1}]]></textFieldExpression>");
        ps.println("                </textField>");

        String ctextkey;
        int x = 81;
        int add = 0;
        for (int j = 2; j <= cells; j += 2) {
            add++;
            ctextkey = "m" + j;
            ps
                    .println("              <textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >");
            ps.println("                    <reportElement");
            ps.println("                        mode='Opaque'");
            ps.println("                        x='" + (x + 1) + "'");
            ps.println("                        y='2'");
            ps.println("                        width='149'");
            ps.println("                        height='27'");
            ps.println("                        forecolor='#000000'");
            ps.println("                        backcolor='#CCCCCC'");
            ps.println("                        key='textField-3'");
            ps.println("                        stretchType='NoStretch'");
            ps.println("                        positionType='FixRelativeToTop'");
            ps.println("                        isPrintRepeatedValues='true'");
            ps.println("                        isRemoveLineWhenBlank='false'");
            ps.println("                        isPrintInFirstWholeBand='false'");
            ps.println("                        isPrintWhenDetailOverflows='false'/>");
            ps
                    .println("                  <textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
            ps
                    .println("                      <font fontName='DejaVu Sans' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
            ps.println("                    </textElement>");
            ps.println("                <textFieldExpression   class='java.lang.String'><![CDATA[$F{" + ctextkey
                    + "}]]></textFieldExpression>");
            ps.println("                </textField>");
            x = x + 150;
        }
        ps.println("                <line direction='TopDown'>");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='1'");
        ps.println("                        y='2'");
        ps.println("                        width='0'");
        ps.println("                        height='27'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#FFFFFF'");
        ps.println("                        key='line-30'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps.println("                    <graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
        ps.println("                </line>");
        x = 81;
        add = 0;
        for (int j = 1; j <= cells; j += 2) {
            ps.println("                <line direction='TopDown'>");
            ps.println("                    <reportElement");
            ps.println("                        mode='Opaque'");
            ps.println("                        x='" + x + "'");
            ps.println("                        y='2'");
            ps.println("                        width='0'");
            ps.println("                        height='27'");
            ps.println("                        forecolor='#000000'");
            ps.println("                        backcolor='#FFFFFF'");
            ps.println("                        key='line-30'");
            ps.println("                        stretchType='NoStretch'");
            ps.println("                        positionType='FixRelativeToTop'");
            ps.println("                        isPrintRepeatedValues='true'");
            ps.println("                        isRemoveLineWhenBlank='false'");
            ps.println("                        isPrintInFirstWholeBand='false'");
            ps.println("                        isPrintWhenDetailOverflows='false'/>");
            ps.println("                    <graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
            ps.println("                </line>");
            x = x + 150;
        }
        int f = b - 30;
        ps.println("                <line direction='TopDown'>");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='1'");
        ps.println("                        y='1'");
        ps.println("                        width='" + b + "'");
        ps.println("                        height='0'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#FFFFFF'");
        ps.println("                        key='line-51'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps.println("                    <graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
        ps.println("                </line>");
        ps.println("                <line direction='TopDown'>");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='1'");
        ps.println("                        y='29'");
        ps.println("                        width='" + b + "'");
        ps.println("                        height='0'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#FFFFFF'");
        ps.println("                        key='line-51'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps.println("                    <graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
        ps.println("                </line>");

        ps.println("            </band>");
        ps.println("        </columnHeader>");
        ps.println("        <detail>");
        ps.println("            <band height='30'  isSplitAllowed='true' >");
        ps
                .println("              <textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='2'");
        ps.println("                        y='2'");
        ps.println("                        width='79'");
        ps.println("                        height='27'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#FFFFFF'");
        ps.println("                        key='textField-2'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps
                .println("                  <textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
        ps
                .println("                      <font fontName='DejaVu Sans' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
        ps.println("                    </textElement>");
        ps.println("                <textFieldExpression   class='java.lang.String'><![CDATA[$F{m1}]]></textFieldExpression>");
        ps.println("                </textField>");
        x = 81;
        for (int j = 3; j <= cells; j += 2) {
            add++;
            ctextkey = "m" + j;
            ps
                    .println("              <textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >");
            ps.println("                    <reportElement");
            ps.println("                        mode='Opaque'");
            ps.println("                        x='" + (x+1) + "'");
            ps.println("                        y='0'");
            ps.println("                        width='149'");
            ps.println("                        height='29'");
            ps.println("                        forecolor='#000000'");
            ps.println("                        backcolor='#FFFFFF'");
            ps.println("                        key='textField-3'");
            ps.println("                        stretchType='NoStretch'");
            ps.println("                        positionType='FixRelativeToTop'");
            ps.println("                        isPrintRepeatedValues='true'");
            ps.println("                        isRemoveLineWhenBlank='false'");
            ps.println("                        isPrintInFirstWholeBand='false'");
            ps.println("                        isPrintWhenDetailOverflows='false'/>");
            ps
                    .println("                  <textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
            ps
                    .println("                      <font fontName='DejaVu Sans' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
            ps.println("                    </textElement>");
            ps.println("                <textFieldExpression   class='java.lang.String'><![CDATA[$F{" + ctextkey
                    + "}]]></textFieldExpression>");
            ps.println("                </textField>");
            x = x + 150;

        }
        x = 81;
        for (int j = 1; j <= cells; j += 2) {
            ps.println("                <line direction='TopDown'>");
            ps.println("                    <reportElement");
            ps.println("                        mode='Opaque'");
            ps.println("                        x='" + x + "'");
            ps.println("                        y='0'");
            ps.println("                        width='0'");
            ps.println("                        height='29'");
            ps.println("                        forecolor='#000000'");
            ps.println("                        backcolor='#FFFFFF'");
            ps.println("                        key='line-30'");
            ps.println("                        stretchType='NoStretch'");
            ps.println("                        positionType='FixRelativeToTop'");
            ps.println("                        isPrintRepeatedValues='true'");
            ps.println("                        isRemoveLineWhenBlank='false'");
            ps.println("                        isPrintInFirstWholeBand='false'");
            ps.println("                        isPrintWhenDetailOverflows='false'/>");
            ps.println("                    <graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
            ps.println("                </line>");
            x = x + 150;
        }
        ps.println("                <line direction='TopDown'>");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='1'");
        ps.println("                        y='0'");
        ps.println("                        width='0'");
        ps.println("                        height='29'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#FFFFFF'");
        ps.println("                        key='line-30'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps.println("                    <graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
        ps.println("                </line>");
        ps.println("                <line direction='TopDown'>");
        ps.println("                    <reportElement");
        ps.println("                        mode='Opaque'");
        ps.println("                        x='1'");
        ps.println("                        y='29'");
        ps.println("                        width='" + b + "'");
        ps.println("                        height='0'");
        ps.println("                        forecolor='#000000'");
        ps.println("                        backcolor='#FFFFFF'");
        ps.println("                        key='line-53'");
        ps.println("                        stretchType='NoStretch'");
        ps.println("                        positionType='FixRelativeToTop'");
        ps.println("                        isPrintRepeatedValues='true'");
        ps.println("                        isRemoveLineWhenBlank='false'");
        ps.println("                        isPrintInFirstWholeBand='false'");
        ps.println("                        isPrintWhenDetailOverflows='false'/>");
        ps.println("                    <graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
        ps.println("                </line>");
        ps.println("            </band>");
        ps.println("        </detail>");
        ps.println("        <columnFooter>");
        ps.println("            <band height='0'  isSplitAllowed='true' >");
        ps.println("            </band>");
        ps.println("        </columnFooter>");
        ps.println("<pageFooter>");
        ps.println("<band height='22'  isSplitAllowed='true' >");
        ps
                .println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >                   <reportElement");
        ps.println("mode='Transparent'");
        ps.println("x='1'");
        ps.println("y='4'");
        ps.println("width='173'");
        ps.println("height='14'");
        ps.println("forecolor='#000000'");
        ps.println("backcolor='#FFFFFF'");
        ps.println("key='textField-5'");
        ps.println("stretchType='NoStretch'");
        ps.println("positionType='FixRelativeToTop'");
        ps.println("isPrintRepeatedValues='true'");
        ps.println("isRemoveLineWhenBlank='false'");
        ps.println("isPrintInFirstWholeBand='false'");
        ps.println("isPrintWhenDetailOverflows='false'/>");
        ps.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
        ps
                .println("<font fontName='DejaVu Sans' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='CP1252' isStrikeThrough='false' />");
        ps.println("</textElement>");
        ps
                .println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Page \" + $V{PAGE_NUMBER} + \" of \" + $V{PAGE_COUNT}]]></textFieldExpression>");
        ps.println("</textField>");
        ps.println("</band>");
        ps.println("</pageFooter>");
        ps.println("        <summary>");
        ps.println("            <band height='0'  isSplitAllowed='true' >");
        ps.println("            </band>");
        ps.println("        </summary>");
        ps.println("</jasperReport>");

        ps.close();
        out.close();
    }

    @Override
    public Collection generateDataSource(Collection<PIReportAbstractRow> rows) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[][] generateDataSource(int[][] rows, int startYear, int endYear) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
