package org.digijava.module.aim.helper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class QdrJrxml
{
	  public void createJrxml(int cnt, String filePath)
	throws IOException
       {		
		  try
			{
			////System.out.println("DYNAMIC Multi-JRXML..");
			////System.out.println(filePath);

//			File fopen = new File("TrendAnalysisPdf_new.jrxml");
			FileOutputStream out2; // declare a file output object
			PrintStream p2; // declare a print stream object
			File fopen = new File(filePath);	

					out2 = new FileOutputStream(fopen);
					p2 = new PrintStream(out2);
					p2.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");

			////System.out.println("creating now- dynamic trend...");

int n=cnt;//cnt;
int center=0;//(12-n)*30;
int x=(0+center),x1=0,y=0,y1=0,xl=0,yl=0;
int textkey=11,linekey=21,c=0;
String ctextkey;
int pagesize=(60*n)+120;

p2.println("<!-- Created with iReport - A designer for JasperReports -->");
p2.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
p2.println("<jasperReport");
p2.println("name='QuarterlyDateRangePdf'");
p2.println("columnCount='1'");
p2.println("printOrder='Horizontal'");
p2.println("orientation='Landscape'");
p2.println("pageWidth='842'");
p2.println("pageHeight='595'");
p2.println("columnWidth='535'");
p2.println("columnSpacing='0'");
p2.println("leftMargin='15'");
p2.println("rightMargin='15'");
p2.println("topMargin='21'");
p2.println("bottomMargin='21'");
p2.println("whenNoDataType='NoPages'");
p2.println("isTitleNewPage='false'");
p2.println("isSummaryNewPage='false'>");
p2.println("<property name='ireport.scriptlethandling' value='0' />");
p2.println("<parameter name='qu' isForPrompting='false' class='java.lang.String'>");
p2.println("<defaultValueExpression ><![CDATA[\"select * from my_table\"]]></defaultValueExpression>");
p2.println("</parameter>");
p2.println("<parameter name='nam' isForPrompting='false' class='java.lang.String'>");
p2.println("<defaultValueExpression ><![CDATA[\"zzzz\"]]></defaultValueExpression>");
p2.println("</parameter>");
p2.println("<queryString><![CDATA[$P!{qu}]]></queryString>");


//DYNAMIC CCCCCCCCC
String dc;
for(int k=1;k<=((6*n)+8);k++)
{
	
	dc="c"+k;
	p2.println("<field name='"+dc+"' class='java.lang.String'/>");
	
}

p2.println("<group  name='Data' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
p2.println("<groupExpression><![CDATA[\"qdr\"]]></groupExpression>");
p2.println("<groupHeader>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</groupHeader>");
p2.println("<groupFooter>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</groupFooter>");
p2.println("</group>");
p2.println("<background>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</background>");
p2.println("<title>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</title>");
p2.println("<pageHeader>");

p2.println("<band height='66'  isSplitAllowed='true' >");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='0'");
p2.println("width='680'");
p2.println("height='20'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='staticText-4'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='14' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Quarterly Department Project Detail Report]]></text>");
p2.println("</staticText>");

p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='20'");
p2.println("width='500'");
p2.println("height='15'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-31'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Workspace  : \"+$F{c1}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='36'");
p2.println("width='800'");
p2.println("height='14'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-31'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Filters : \"+$F{c2}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='49'");
p2.println("width='800'");
p2.println("height='14'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-31'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"             \"+$F{c3}]]></textFieldExpression>");
p2.println("</textField>");


p2.println("<line direction='TopDown'>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='65'");
p2.println("width='"+(380+n*75)+"'");
p2.println("height='0'");
p2.println("forecolor='#333333'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='line-3'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
p2.println("</line>");

p2.println("</band>");
p2.println("</pageHeader>");
p2.println("<columnHeader>");

p2.println("<band height='31'  isSplitAllowed='true' >");

x=(380+center);y=0;
x1=(0+center);y1=0;
c = 9+ (n*4) + 1;
for(int j=0;j<n;j++)
{
	ctextkey="c"+c;
	p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
	p2.println("mode='Opaque'");
	p2.println("x='"+x+"'");
	p2.println("y='"+y+"'");
	p2.println("width='75'");
	p2.println("height='30'");
	p2.println("forecolor='#000000'");
	p2.println("backcolor='#CCCCCC'");
	p2.println("key='textField-28'");
	p2.println("stretchType='NoStretch'");
	p2.println("positionType='Float'");
	p2.println("isPrintRepeatedValues='true'");
	p2.println("isRemoveLineWhenBlank='false'");
	p2.println("isPrintInFirstWholeBand='false'");
	p2.println("isPrintWhenDetailOverflows='false'/>");
	p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
	p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
	p2.println("</textElement>");
	p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
	p2.println("</textField>");
	c +=1;
	x += 75;
}// for1

p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='0'");
p2.println("width='65'");
p2.println("height='30'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-5'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Donor Name]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='125'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='30'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-8'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Sector]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='300'");
p2.println("y='0'");
p2.println("width='80'");
p2.println("height='30'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-11'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Close Date]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='65'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='30'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-21'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Title]]></text>");
p2.println("</staticText>");

p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='185'");
p2.println("y='0'");
p2.println("width='50'");
p2.println("height='30'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-27'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Status]]></text>");
p2.println("</staticText>");
p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='235'");
p2.println("y='0'");
p2.println("width='65'");
p2.println("height='30'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-28'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Start Date]]></text>");
p2.println("</staticText>");
p2.println("<line direction='TopDown'>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='30'");
p2.println("width='"+(380+n*75)+"'");
p2.println("height='0'");
p2.println("forecolor='#333333'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='line-1'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
p2.println("</line>");

p2.println("</band>");
p2.println("</columnHeader>");
p2.println("<detail>");
p2.println("<band height='60'  isSplitAllowed='true' >");

p2.println("<line direction='TopDown'>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='59'");
p2.println("width='"+(380+n*75)+"'");
p2.println("height='0'");
p2.println("forecolor='#333333'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='line-2'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
p2.println("</line>");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='0'");
p2.println("y='0'");
p2.println("width='65'");
p2.println("height='60'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField'");
p2.println("stretchType='RelativeToTallestObject'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{c4}]]></textFieldExpression>");
p2.println("</textField>");

// c8
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='300'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='60'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-14'");
p2.println("stretchType='RelativeToTallestObject'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c9}]]></textFieldExpression>");
p2.println("</textField>");

// c5
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='125'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='60'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-18'");
p2.println("stretchType='RelativeToTallestObject'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c6}]]></textFieldExpression>");
p2.println("</textField>");


p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='65'");
p2.println("y='0'");
p2.println("width='60'");
p2.println("height='60'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-20'");
p2.println("stretchType='RelativeToTallestObject'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c5}]]></textFieldExpression>");
p2.println("</textField>");

//c6
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='185'");
p2.println("y='0'");
p2.println("width='50'");
p2.println("height='60'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-29'");
p2.println("stretchType='RelativeToTallestObject'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c7}]]></textFieldExpression>");
p2.println("</textField>");

//c7
p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Transparent'");
p2.println("x='235'");
p2.println("y='0'");
p2.println("width='65'");
p2.println("height='60'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#FFFFFF'");
p2.println("key='textField-30'");
p2.println("stretchType='RelativeToTallestObject'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c8}]]></textFieldExpression>");
p2.println("</textField>");
x=(380+center);y=0;
x1=(0+center);y1=0;

c=10;
for(int j=0;j<n;j++)
{
	
	ctextkey="c"+c;
	y = 0;
	p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
	p2.println("mode='Transparent'");
	p2.println("x='"+x+"'");
	p2.println("y='"+y+"'");
	p2.println("width='75'");
	p2.println("height='15'");
	p2.println("forecolor='#000000'");
	p2.println("backcolor='#FFFFFF'");
	p2.println("key='textField-23'");
	p2.println("stretchType='RelativeToBandHeight'");
	p2.println("positionType='Float'");
	p2.println("isPrintRepeatedValues='true'");
	p2.println("isRemoveLineWhenBlank='false'");
	p2.println("isPrintInFirstWholeBand='false'");
	p2.println("isPrintWhenDetailOverflows='false'/>");
	p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
	p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
	p2.println("</textElement>");
	p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
	p2.println("</textField>");
	////System.out.println(x + "1 : "+ctextkey);
	
	c += 1;
	ctextkey="c"+c;
	y = y+15;
	p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
	p2.println("mode='Transparent'");
	p2.println("x='"+x+"'");
	p2.println("y='"+y+"'");
	p2.println("width='75'");
	p2.println("height='15'");
	p2.println("forecolor='#000000'");
	p2.println("backcolor='#FFFFFF'");
	p2.println("key='textField-23'");
	p2.println("stretchType='RelativeToBandHeight'");
	p2.println("positionType='Float'");
	p2.println("isPrintRepeatedValues='true'");
	p2.println("isRemoveLineWhenBlank='false'");
	p2.println("isPrintInFirstWholeBand='false'");
	p2.println("isPrintWhenDetailOverflows='false'/>");
	p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
	p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
	p2.println("</textElement>");
	p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
	p2.println("</textField>");
	
	////System.out.println(x+ " 2 : "+ctextkey);
	
	c += 1;
	ctextkey="c"+c;
	y = y+15;
	p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
	p2.println("mode='Transparent'");
	p2.println("x='"+x+"'");
	p2.println("y='"+y+"'");
	p2.println("width='75'");
	p2.println("height='15'");
	p2.println("forecolor='#000000'");
	p2.println("backcolor='#FFFFFF'");
	p2.println("key='textField-23'");
	p2.println("stretchType='RelativeToBandHeight'");
	p2.println("positionType='Float'");
	p2.println("isPrintRepeatedValues='true'");
	p2.println("isRemoveLineWhenBlank='false'");
	p2.println("isPrintInFirstWholeBand='false'");
	p2.println("isPrintWhenDetailOverflows='false'/>");
	p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
	p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
	p2.println("</textElement>");
	p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
	p2.println("</textField>");
	////System.out.println(x + "3 : "+ctextkey);
	
	
	c += 1;
	ctextkey="c"+c;
	y = y+15;
	p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
	p2.println("mode='Transparent'");
	p2.println("x='"+x+"'");
	p2.println("y='"+y+"'");
	p2.println("width='75'");
	p2.println("height='15'");
	p2.println("forecolor='#000000'");
	p2.println("backcolor='#FFFFFF'");
	p2.println("key='textField-23'");
	p2.println("stretchType='RelativeToBandHeight'");
	p2.println("positionType='Float'");
	p2.println("isPrintRepeatedValues='true'");
	p2.println("isRemoveLineWhenBlank='false'");
	p2.println("isPrintInFirstWholeBand='false'");
	p2.println("isPrintWhenDetailOverflows='false'/>");
	p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
	p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
	p2.println("</textElement>");
	p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
	p2.println("</textField>");
	////System.out.println( x + "4 : "+ctextkey);
	
	x += 75;
	c += 1;
	////System.out.println(" c : " + c);
}//for2


p2.println("<staticText>");
p2.println("<reportElement");
p2.println("mode='Opaque'");
p2.println("x='360'");
p2.println("y='0'");
p2.println("width='20'");
p2.println("height='58'");
p2.println("forecolor='#000000'");
p2.println("backcolor='#CCCCCC'");
p2.println("key='staticText-37'");
p2.println("stretchType='NoStretch'");
p2.println("positionType='FixRelativeToTop'");
p2.println("isPrintRepeatedValues='true'");
p2.println("isRemoveLineWhenBlank='false'");
p2.println("isPrintInFirstWholeBand='false'");
p2.println("isPrintWhenDetailOverflows='false'/>");
p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<text><![CDATA[Q1 Q2 Q3 Q4]]></text>");
p2.println("</staticText>");

p2.println("</band>");
p2.println("</detail>");
p2.println("<columnFooter>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</columnFooter>");
p2.println("<pageFooter>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</pageFooter>");
p2.println("<summary>");
p2.println("<band height='69'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</summary>");
p2.println("</jasperReport>");

		p2.close();
			}
			catch (Exception e)
			{
				System.err.println("File error");
			}
	}//CreateJrxml
}