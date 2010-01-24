package org.digijava.module.aim.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.digijava.kernel.translator.TranslatorWorker;

public class ParisIndicator6Jrxml extends ParisIndicatorJrxml {
    public synchronized void createJrxml(String filePath, String reportName,String selCurr, int cols, int rows, String type) throws IOException {
        try {
            int a = (cols / 2);
            int b = 80 + (a * 150);
            a = (a + 1) * 150;

            FileOutputStream out; // declare a file output object
            PrintStream p2; // declare a print stream object

            out = new FileOutputStream(filePath);
            p2 = new PrintStream(out);

            p2.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            p2.println("<!-- Created with iReport - A designer for JasperReports -->");
            p2.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
            p2.println("<jasperReport");
            p2.println("		 name='" + reportName + "'");
            p2.println("		 columnCount='1'");
            p2.println("		 printOrder='Vertical'");
            p2.println("		 orientation='Portrait'");
            p2.println("		 pageWidth='" + a + "'");
            p2.println("		 pageHeight='842'");
            p2.println("		 columnWidth='535'");
            p2.println("		 columnSpacing='0'");
            p2.println("		 leftMargin='30'");
            p2.println("		 rightMargin='30'");
            p2.println("		 topMargin='20'");
            p2.println("		 bottomMargin='20'");
            p2.println("		 whenNoDataType='NoPages'");
            p2.println("		 isTitleNewPage='false'");
            p2.println("		 isSummaryNewPage='false'>");
            p2.println("	<property name='ireport.scriptlethandling' value='2' />");
            p2.println("<parameter name='qu' isForPrompting='false' class='java.lang.String'>");
            p2.println("<defaultValueExpression ><![CDATA[\"select * from my_table\"]]></defaultValueExpression>");
            p2.println("</parameter>");
            p2.println("<parameter name='nam' isForPrompting='false' class='java.lang.String'>");
            p2.println("<defaultValueExpression ><![CDATA[\"zzzz\"]]></defaultValueExpression>");
            p2.println("</parameter>");
            p2.println("<queryString><![CDATA[$P!{qu}]]></queryString>");

//							DYNAMIC CCCCCCCCC
            String dc;
            int colCnt = 13;
            ////System.out.println(" Cnt = " + colCnt + " colsssssssssssssssssssssss "+cols);
//							 gets the no of fields = 4 constants fields + YearCnt*3 + yearCnt
            for (int k = 1; k <= cols; k++) {
                ////System.out.println("k="+k);
                dc = "m" + k;
                p2.println("<field name='" + dc + "' class='java.lang.String'/>");
            }

            p2.println("<group  name='Data' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
            p2.println("<groupExpression><![CDATA[$F{m1}]]></groupExpression>");
            p2.println("<groupHeader>");

            p2.println("</groupHeader>");
            p2.println("<groupFooter>");
            p2.println("<band height='0'  isSplitAllowed='true' >");
            p2.println("</band>");
            p2.println("</groupFooter>");
            p2.println("</group>");

            p2.println("		<background>");
            p2.println("			<band height='0'  isSplitAllowed='true' >");
            p2.println("			</band>");
            p2.println("		</background>");
            p2.println("		<title>");
            p2.println("			<band height='22'  isSplitAllowed='true' >");
            p2.println("				<staticText>");
            p2.println("					<reportElement");
            p2.println("						mode='Opaque'");
            p2.println("						x='50'");
            p2.println("						y='0'");
            p2.println("						width='450'");
            p2.println("						height='22'");
            p2.println("						forecolor='#000000'");
            p2.println("						backcolor='#FFFFFF'");
            p2.println("						key='staticText-1'");
            p2.println("						stretchType='NoStretch'");
            p2.println("						positionType='FixRelativeToTop'");
            p2.println("						isPrintRepeatedValues='true'");
            p2.println("						isRemoveLineWhenBlank='false'");
            p2.println("						isPrintInFirstWholeBand='false'");
            p2.println("						isPrintWhenDetailOverflows='false'/>");
            p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
            p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='18' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
            p2.println("					</textElement>");
            p2.println("				<text><![CDATA["+ TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("Paris Indicator 6: Number of parallel PIUs", this.getLangCode(), this.getSite().getId())) + "]]></text>");
            p2.println("				</staticText>");
            p2.println("			</band>");
            p2.println("		</title>");
            p2.println("		<pageHeader>");
            p2.println("			<band height='49'  isSplitAllowed='true' >");
            p2.println("			</band>");
            p2.println("		</pageHeader>");
            p2.println("		<columnHeader>");
            p2.println("			<band height='30'  isSplitAllowed='true' >");
            p2.println("				<staticText>");
            p2.println("					<reportElement");
            p2.println("						mode='Opaque'");
            p2.println("						x='0'");
            p2.println("						y='0'");
            p2.println("						width='81'");
            p2.println("						height='30'");
            p2.println("						forecolor='#000000'");
            p2.println("						backcolor='#CCCCCC'");
            p2.println("						key='staticText-2'");
            p2.println("						stretchType='NoStretch'");
            p2.println("						positionType='FixRelativeToTop'");
            p2.println("						isPrintRepeatedValues='true'");
            p2.println("						isRemoveLineWhenBlank='false'");
            p2.println("						isPrintInFirstWholeBand='false'");
            p2.println("						isPrintWhenDetailOverflows='false'/>");
            p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
            p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
            p2.println("					</textElement>");
            p2.println("				<text><![CDATA["+ TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("Donors", this.getLangCode(), this.getSite().getId())) + "]]></text>");
            p2.println("				</staticText>");

            String ctextkey;
            int x = 81;
            int add = 0;
            for (int j = 2; j <= cols; j += 2) {
                add++;
                ctextkey = "m" + j;
                p2.println("				<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >");
                p2.println("					<reportElement");
                p2.println("						mode='Opaque'");
                p2.println("						x='" + x + "'");
                p2.println("						y='0'");
                p2.println("						width='150'");
                p2.println("						height='30'");
                p2.println("						forecolor='#000000'");
                p2.println("						backcolor='#CCCCCC'");
                p2.println("						key='textField-3'");
                p2.println("						stretchType='NoStretch'");
                p2.println("						positionType='FixRelativeToTop'");
                p2.println("						isPrintRepeatedValues='true'");
                p2.println("						isRemoveLineWhenBlank='false'");
                p2.println("						isPrintInFirstWholeBand='false'");
                p2.println("						isPrintWhenDetailOverflows='false'/>");
                p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
                p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
                p2.println("					</textElement>");
                p2.println("				<textFieldExpression   class='java.lang.String'><![CDATA[$F{" + ctextkey + "}]]></textFieldExpression>");
                p2.println("				</textField>");
                x = x + 150;

            }
            if (type.equals("pdf")) {
                p2.println("				<line direction='TopDown'>");
                p2.println("					<reportElement");
                p2.println("						mode='Opaque'");
                p2.println("						x='0'");
                p2.println("						y='0'");
                p2.println("						width='0'");
                p2.println("						height='30'");
                p2.println("						forecolor='#000000'");
                p2.println("						backcolor='#FFFFFF'");
                p2.println("						key='line-30'");
                p2.println("						stretchType='NoStretch'");
                p2.println("						positionType='FixRelativeToTop'");
                p2.println("						isPrintRepeatedValues='true'");
                p2.println("						isRemoveLineWhenBlank='false'");
                p2.println("						isPrintInFirstWholeBand='false'");
                p2.println("						isPrintWhenDetailOverflows='false'/>");
                p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
                p2.println("				</line>");

                x = 81;
                add = 0;
                for (int j = 1; j <= cols; j += 2) {
                    p2.println("				<line direction='TopDown'>");
                    p2.println("					<reportElement");
                    p2.println("						mode='Opaque'");
                    p2.println("						x='" + x + "'");
                    p2.println("						y='0'");
                    p2.println("						width='0'");
                    p2.println("						height='30'");
                    p2.println("						forecolor='#000000'");
                    p2.println("						backcolor='#FFFFFF'");
                    p2.println("						key='line-30'");
                    p2.println("						stretchType='NoStretch'");
                    p2.println("						positionType='FixRelativeToTop'");
                    p2.println("						isPrintRepeatedValues='true'");
                    p2.println("						isRemoveLineWhenBlank='false'");
                    p2.println("						isPrintInFirstWholeBand='false'");
                    p2.println("						isPrintWhenDetailOverflows='false'/>");
                    p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
                    p2.println("				</line>");
                    x = x + 150;
                }

                int f = b - 30;
                p2.println("				<line direction='TopDown'>");
                p2.println("					<reportElement");
                p2.println("						mode='Opaque'");
                p2.println("						x='1'");
                p2.println("						y='0'");
                p2.println("						width='" + b + "'");
                p2.println("						height='0'");
                p2.println("						forecolor='#000000'");
                p2.println("						backcolor='#FFFFFF'");
                p2.println("						key='line-51'");
                p2.println("						stretchType='NoStretch'");
                p2.println("						positionType='FixRelativeToTop'");
                p2.println("						isPrintRepeatedValues='true'");
                p2.println("						isRemoveLineWhenBlank='false'");
                p2.println("						isPrintInFirstWholeBand='false'");
                p2.println("						isPrintWhenDetailOverflows='false'/>");
                p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
                p2.println("				</line>");
                p2.println("				<line direction='TopDown'>");
                p2.println("					<reportElement");
                p2.println("						mode='Opaque'");
                p2.println("						x='0'");
                p2.println("						y='29'");
                p2.println("						width='" + b + "'");
                p2.println("						height='0'");
                p2.println("						forecolor='#000000'");
                p2.println("						backcolor='#FFFFFF'");
                p2.println("						key='line-51'");
                p2.println("						stretchType='NoStretch'");
                p2.println("						positionType='FixRelativeToTop'");
                p2.println("						isPrintRepeatedValues='true'");
                p2.println("						isRemoveLineWhenBlank='false'");
                p2.println("						isPrintInFirstWholeBand='false'");
                p2.println("						isPrintWhenDetailOverflows='false'/>");
                p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
                p2.println("				</line>");
            }
            p2.println("			</band>");
            p2.println("		</columnHeader>");
            p2.println("		<detail>");
            p2.println("			<band height='30'  isSplitAllowed='true' >");
            p2.println("				<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >");
            p2.println("					<reportElement");
            p2.println("						mode='Opaque'");
            p2.println("						x='0'");
            p2.println("						y='0'");
            p2.println("						width='81'");
            p2.println("						height='30'");
            p2.println("						forecolor='#000000'");
            p2.println("						backcolor='#FFFFFF'");
            p2.println("						key='textField-2'");
            p2.println("						stretchType='NoStretch'");
            p2.println("						positionType='FixRelativeToTop'");
            p2.println("						isPrintRepeatedValues='true'");
            p2.println("						isRemoveLineWhenBlank='false'");
            p2.println("						isPrintInFirstWholeBand='false'");
            p2.println("						isPrintWhenDetailOverflows='false'/>");
            p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
            p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
            p2.println("					</textElement>");
            p2.println("				<textFieldExpression   class='java.lang.String'><![CDATA[$F{m1}]]></textFieldExpression>");
            p2.println("				</textField>");

            x = 81;
            for (int j = 3; j <= cols; j += 2) {
                add++;
                ctextkey = "m" + j;
                p2.println("				<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >");
                p2.println("					<reportElement");
                p2.println("						mode='Opaque'");
                p2.println("						x='" + x + "'");
                p2.println("						y='0'");
                p2.println("						width='150'");
                p2.println("						height='30'");
                p2.println("						forecolor='#000000'");
                p2.println("						backcolor='#FFFFFF'");
                p2.println("						key='textField-3'");
                p2.println("						stretchType='NoStretch'");
                p2.println("						positionType='FixRelativeToTop'");
                p2.println("						isPrintRepeatedValues='true'");
                p2.println("						isRemoveLineWhenBlank='false'");
                p2.println("						isPrintInFirstWholeBand='false'");
                p2.println("						isPrintWhenDetailOverflows='false'/>");
                p2.println("					<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
                p2.println("						<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
                p2.println("					</textElement>");
                p2.println("				<textFieldExpression   class='java.lang.String'><![CDATA[$F{" + ctextkey + "}]]></textFieldExpression>");
                p2.println("				</textField>");
                x = x + 150;

            }
            if (type.equals("pdf")) {
                x = 81;
                for (int j = 1; j <= cols; j += 2) {
                    p2.println("				<line direction='TopDown'>");
                    p2.println("					<reportElement");
                    p2.println("						mode='Opaque'");
                    p2.println("						x='" + x + "'");
                    p2.println("						y='0'");
                    p2.println("						width='0'");
                    p2.println("						height='30'");
                    p2.println("						forecolor='#000000'");
                    p2.println("						backcolor='#FFFFFF'");
                    p2.println("						key='line-30'");
                    p2.println("						stretchType='NoStretch'");
                    p2.println("						positionType='FixRelativeToTop'");
                    p2.println("						isPrintRepeatedValues='true'");
                    p2.println("						isRemoveLineWhenBlank='false'");
                    p2.println("						isPrintInFirstWholeBand='false'");
                    p2.println("						isPrintWhenDetailOverflows='false'/>");
                    p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
                    p2.println("				</line>");
                    x = x + 150;
                }

                p2.println("				<line direction='TopDown'>");
                p2.println("					<reportElement");
                p2.println("						mode='Opaque'");
                p2.println("						x='0'");
                p2.println("						y='0'");
                p2.println("						width='0'");
                p2.println("						height='30'");
                p2.println("						forecolor='#000000'");
                p2.println("						backcolor='#FFFFFF'");
                p2.println("						key='line-30'");
                p2.println("						stretchType='NoStretch'");
                p2.println("						positionType='FixRelativeToTop'");
                p2.println("						isPrintRepeatedValues='true'");
                p2.println("						isRemoveLineWhenBlank='false'");
                p2.println("						isPrintInFirstWholeBand='false'");
                p2.println("						isPrintWhenDetailOverflows='false'/>");
                p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
                p2.println("				</line>");
                p2.println("				<line direction='TopDown'>");
                p2.println("					<reportElement");
                p2.println("						mode='Opaque'");
                p2.println("						x='1'");
                p2.println("						y='29'");
                p2.println("						width='" + b + "'");
                p2.println("						height='0'");
                p2.println("						forecolor='#000000'");
                p2.println("						backcolor='#FFFFFF'");
                p2.println("						key='line-53'");
                p2.println("						stretchType='NoStretch'");
                p2.println("						positionType='FixRelativeToTop'");
                p2.println("						isPrintRepeatedValues='true'");
                p2.println("						isRemoveLineWhenBlank='false'");
                p2.println("						isPrintInFirstWholeBand='false'");
                p2.println("						isPrintWhenDetailOverflows='false'/>");
                p2.println("					<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
                p2.println("				</line>");
            }
            p2.println("			</band>");
            p2.println("		</detail>");
            p2.println("		<columnFooter>");
            p2.println("			<band height='0'  isSplitAllowed='true' >");
            p2.println("			</band>");
            p2.println("		</columnFooter>");
            p2.println("<pageFooter>");
            p2.println("<band height='22'  isSplitAllowed='true' >");

//            p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
//            p2.println("mode='Transparent'");
//            p2.println("x='2'");
//            p2.println("y='4'");
//            p2.println("width='350'");
//            p2.println("height='16'");
//            p2.println("forecolor='#3333FF'");
//            p2.println("backcolor='#FFFFFF'");
//            p2.println("key='textField-4'");
//            p2.println("stretchType='NoStretch'");
//            p2.println("positionType='FixRelativeToTop'");
//            p2.println("isPrintRepeatedValues='true'");
//            p2.println("isRemoveLineWhenBlank='false'");
//            p2.println("isPrintInFirstWholeBand='false'");
//            p2.println("isPrintWhenDetailOverflows='false'/>");
//            p2.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
//            p2.println("<font fontName='Times-Roman' pdfFontName='Times-Roman' size='12' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='CP1252' isStrikeThrough='false' />");
//            p2.println("</textElement>");
//            p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\" * All the amounts are in thousands (000) "+selCurr+"\"]]></textFieldExpression>");
//            p2.println("</textField>");

            p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
            p2.println("mode='Transparent'");
            p2.println("x='464'");
            p2.println("y='4'");
            p2.println("width='174'");
            p2.println("height='14'");
            p2.println("forecolor='#000000'");
            p2.println("backcolor='#FFFFFF'");
            p2.println("key='textField-5'");
            p2.println("stretchType='NoStretch'");
            p2.println("positionType='FixRelativeToTop'");
            p2.println("isPrintRepeatedValues='true'");
            p2.println("isRemoveLineWhenBlank='false'");
            p2.println("isPrintInFirstWholeBand='false'");
            p2.println("isPrintWhenDetailOverflows='false'/>");
            p2.println("<textElement textAlignment='Right' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
            p2.println("<font fontName='Helvetica' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='CP1252' isStrikeThrough='false' />");
            p2.println("</textElement>");
            p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Page \" + $V{PAGE_NUMBER} + \" of \"]]></textFieldExpression>");
            p2.println("</textField>");
            p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Report' hyperlinkType='None' >					<reportElement");
            p2.println("mode='Transparent'");
            p2.println("x='641'");
            p2.println("y='4'");
            p2.println("width='36'");
            p2.println("height='14'");
            p2.println("forecolor='#000000'");
            p2.println("backcolor='#FFFFFF'");
            p2.println("key='textField-6'");
            p2.println("stretchType='NoStretch'");
            p2.println("positionType='FixRelativeToTop'");
            p2.println("isPrintRepeatedValues='true'");
            p2.println("isRemoveLineWhenBlank='false'");
            p2.println("isPrintInFirstWholeBand='false'");
            p2.println("isPrintWhenDetailOverflows='false'/>");
            p2.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
            p2.println("<font fontName='Helvetica' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='CP1252' isStrikeThrough='false' />");
            p2.println("</textElement>");
            p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"\"+$V{PAGE_NUMBER}]]></textFieldExpression>");
            p2.println("</textField>");
            p2.println("</band>");
            p2.println("</pageFooter>");
            p2.println("		<summary>");
            p2.println("			<band height='0'  isSplitAllowed='true' >");
            p2.println("			</band>");
            p2.println("		</summary>");
            p2.println("</jasperReport>");

            p2.close();
            out.close();
        }

        catch (Exception e) {

        }
    }
}
