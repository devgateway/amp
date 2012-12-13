package org.digijava.module.aim.helper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.util.FeaturesUtil;

public class PbdJrxml
{
	public void createJrxml(int cnt, String filePath, int height)
	throws IOException
	{
		try
		{
			//////System.out.println( cnt +"DYNAMIC Multi-JRXML..");
			FileOutputStream out2; // declare a file output  object
			PrintStream p2; // declare a print stream object
			File fopen = new File(filePath);	
			out2 = new FileOutputStream(fopen);
			p2 = new PrintStream(out2);
			p2.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			//////System.out.println("creating now- dynamic trend...");

			int n=cnt;//cnt;
			int center=0;//(12-n)*30;
			int x=(0+center),x1=0,y=0,y1=0,xl=0,yl=0;
			int textkey=11,linekey=21,c=0;
			String ctextkey;
			int pagesize=(60*n)+120;


			if(height > 70)
				height = height;
			else
				height = 70;



			p2.println("<!-- Created with iReport - A designer for JasperReports -->");
			p2.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
			p2.println("<jasperReport");
			p2.println("name='ProjectbyDonorPdf'");
			
			p2.println("columnCount='1'");
			p2.println("printOrder='Vertical'");
			p2.println("orientation='Landscape'");
			p2.println("pageWidth='842'");
			p2.println("pageHeight='595'");
			p2.println("columnWidth='535'");
			p2.println("columnSpacing='0'");
			p2.println("leftMargin='21'");
			p2.println("rightMargin='21'");
			p2.println("topMargin='14'");
			p2.println("bottomMargin='14'");
			p2.println("whenNoDataType='NoPages'");
			p2.println("isTitleNewPage='false'");
			p2.println("isSummaryNewPage='false'>");
			p2.println("<property name='ireport.scriptlethandling' value='2' />");
			p2.println("<parameter name='qu' isForPrompting='false' class='java.lang.String'>");
			p2.println("<defaultValueExpression ><![CDATA[\"select * from my_table\"]]></defaultValueExpression>");
			p2.println("</parameter>");
			p2.println("<parameter name='nam' isForPrompting='false' class='java.lang.String'>");
			p2.println("<defaultValueExpression ><![CDATA[\"pbd\"]]></defaultValueExpression>");
			p2.println("</parameter>");
			p2.println("<queryString><![CDATA[$P!{qu}]]></queryString>");
			
			//DYNAMIC CCCCCCCCC
			String dc;
			for(int k=1;k<=(10+5+(5*n));k++)
			{
				
				dc="c"+k;
				p2.println("<field name='"+dc+"' class='java.lang.String'/>");
			}
			
			p2.println("<group  name='Data' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
			p2.println("<groupExpression><![CDATA[$F{c5}]]></groupExpression>");
			p2.println("<groupHeader>");
			p2.println("<band height='20'  isSplitAllowed='true' >");
			
			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='0'");
//			p2.println("width='"+(800-(3-n)*180)+"'");
			p2.println("width='"+(185 + 80*n + 80)+"'");
			p2.println("height='20'");
			p2.println("forecolor='#FFFFFF'");
			p2.println("backcolor='#666666'");
			p2.println("key='textField'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Agency: \"+$F{c5}]]></textFieldExpression>");
			p2.println("</textField>");
			
			
			
			
			
			//--
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
			p2.println("<band height='35'  isSplitAllowed='true' >");

			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='0'");
//			p2.println("width='727'");
			p2.println("width='"+(185 + 80*n + 80)+"'");
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
			p2.println("<text><![CDATA[Annual Report By Project]]></text>");
			p2.println("</staticText>");


			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >	<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='20'");
			p2.println("width='787'");
			p2.println("height='15'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-28'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Justified' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Workspace : \"+$F{c1}]]></textFieldExpression>");
			p2.println("</textField>");

			p2.println("</band>");
			p2.println("</title>");
			p2.println("<pageHeader>");
			p2.println("<band height='60'  isSplitAllowed='true' >");

			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='0'");
//			p2.println("width='"+(800-(3-n)*180)+"'");
			p2.println("width='"+(185 + 80*n + 80)+"'");
			p2.println("height='14'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField-28'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Justified' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Filters: \"+$F{c2}]]></textFieldExpression>");
			p2.println("</textField>");

			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='14'");
//			p2.println("width='"+(800-(3-n)*180)+"'");
			p2.println("width='"+(185 + 80*n + 80)+"'");
			p2.println("height='15'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField-28'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Justified' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"              \"+$F{c3}]]></textFieldExpression>");
			p2.println("</textField>");

			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='30'");
			p2.println("width='185'");
			p2.println("height='29'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-6'");
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


			x=(185+center);y=0;
			x1=(185+center);y1=0;
			c=7;
			for(int j=0;j<n;j++)
			{
			ctextkey="c"+c;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='"+x+"'");
			p2.println("y='30'");
			p2.println("width='80'");
			p2.println("height='28'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-11'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Year:  \"+$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			x += 80;
			c +=5;

			x1 +=	180;
			
			}// for1
			
			//---

			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='"+x+"'");
			p2.println("y='30'");
			p2.println("width='80'");
			p2.println("height='28'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='staticText-99'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Total  ]]></text>");
			p2.println("</staticText>");

			x1 += 180;
			
			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='30'");
//			p2.println("width='"+(800-(3-n)*180)+"'");
			p2.println("width='"+(185 + 80*n + 80)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#000000'");
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

			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='59'");
//			p2.println("width='"+(800-(3-n)*180)+"'");
			p2.println("width='"+(185 + 80*n + 80)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#000000'");
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

/*
			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='30'");
			p2.println("width='800'");
			p2.println("height='15'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField-28'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Justified' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"              \"+$F{c4}]]></textFieldExpression>");
			p2.println("</textField>");
*/
			p2.println("</band>");
			p2.println("</pageHeader>");
			p2.println("<columnHeader>");
			p2.println("<band height='0'  isSplitAllowed='true' >");
			p2.println("</band>");
			p2.println("</columnHeader>");

			p2.println("<detail>");
			
			p2.println("<band height='"+(height)+"' isSplitAllowed='true' >");
			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='0'");
			p2.println("width='120'");
			p2.println("height='"+height+"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-10'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c6}]]></textFieldExpression>");
			p2.println("</textField>");
			
//-------------------------------------------------------------
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='120'");
			p2.println("y='0'");
			p2.println("width='65'");
			p2.println("height='"+ (height/5) +"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-8'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Commitment]]></text>");
			p2.println("</staticText>");
			
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='120'");
			p2.println("y='"+((height/5))+"'");
			p2.println("width='65'");
			p2.println("height='"+ (height/5) +"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-8'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Planned Disb.]]></text>");
			p2.println("</staticText>");

			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='120'");
			p2.println("y='"+((height/5) * 2)+"'");
			p2.println("width='65'");
			p2.println("height='"+ (height/5) +"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-8'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Disbursment]]></text>");
			p2.println("</staticText>");

			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='120'");
			p2.println("y='"+((height/5) * 3)+"'");
			p2.println("width='65'");
			p2.println("height='"+ (height/5) +"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-8'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Expenditure]]></text>");
			p2.println("</staticText>");

			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='120'");
			p2.println("y='"+((height/5) * 4)+"'");
			p2.println("width='65'");
			p2.println("height='"+ (height/5) +"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-8'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[UnDisbursment]]></text>");
			p2.println("</staticText>");

//--------
			x=(185+center);y=0;
			c=8;
			for(int j=0;j<n;j++)
			{
			ctextkey="c"+c;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x+"'");
			p2.println("y='0'");
			p2.println("width='80'");
			p2.println("height='"+(height/5)+"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-11'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			c +=1;
			ctextkey="c"+c;
			x +=0;
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x+"'");
//			p2.println("y='0'");
			p2.println("y='"+((height/5)*1)+"'");
			p2.println("width='80' ");
			p2.println("height='"+ (height/5) +"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-11'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			c +=1;
			ctextkey="c"+c;
//			x +=80;
			x +=0;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x+"'");
			p2.println("y='"+((height/5)*2)+"'");
			p2.println("width='80'");
			p2.println("height='"+ (height/5) +"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-27'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			c +=1;
			ctextkey="c"+c;
			x +=0;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x+"'");
			p2.println("y='"+((height/5)*3)+"'");
			p2.println("width='80'");
			p2.println("height='"+ (height/5) +"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-27'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");

			x+=0;
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='"+x+"'");
			p2.println("y='"+((height/5)*4)+"'");
			p2.println("width='80'");
			p2.println("height='"+ (height/5) +"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='staticText-8'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[ -- ]]></text>");
			p2.println("</staticText>");

			x = x + 80;
			c += 2;
			
			}//for2

	
			c=7+(5*n) ;
			int yht = 0;
			for(int j=0;j<5;j++)
			{
			ctextkey="c"+c;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x+"'");
//			p2.println("y='0'");
			p2.println("y='"+yht+"'");
			p2.println("width='80'");
			p2.println("height='"+(height/5)+"'");
//			p2.println("height='62'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-11'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			yht  = yht + (height/5);
			c +=1;
//			x +=60;
					
			}//for3

			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='"+(height-1)+"'");
//			p2.println("width='"+(800-(3-n)*180)+"'");
			p2.println("width='"+(185 + 80*n + 80)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='line-5'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
			p2.println("</line>");

			p2.println("</band>");
			p2.println("</detail>");
			p2.println("<columnFooter>");
			p2.println("<band height='0'  isSplitAllowed='true' >");
			p2.println("</band>");
			p2.println("</columnFooter>");

			p2.println("<pageFooter>");
			p2.println("<band height='22'  isSplitAllowed='true' >");
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='2'");
			p2.println("y='4'");
			p2.println("width='300'");
			p2.println("height='16'");
			p2.println("forecolor='#3333FF'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-4'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Times-Roman' pdfFontName='Times-Roman' size='12' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='CP1252' isStrikeThrough='false' />");
			p2.println("</textElement>");

			int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));
			switch(amountsUnitCode)
			{
			case AmpARFilter.AMOUNT_OPTION_IN_UNITS:
				p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"  \"]]></textFieldExpression>");
				break;
				
			case AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS:
				p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\" * All the amounts are in thousands (000) \"]]></textFieldExpression>");
				break;

			case AmpARFilter.AMOUNT_OPTION_IN_MILLIONS:
				p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\" * All the amounts are in millions (000 000) \"]]></textFieldExpression>");
				break;
			}

			p2.println("</textField>");
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='575'");
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
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Report' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='757'");
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

			
			p2.println("<summary>");
			p2.println("<band height='20'  isSplitAllowed='true' >");
			p2.println("</band>");
			p2.println("</summary>");
			p2.println("</jasperReport>");

			p2.close();

		}
		catch (Exception e)
		{
			//System.err.println("File error");
		}
	}//CreateJrxml
}
			
/*
			p2.println("<!-- Created with iReport - A designer for JasperReports -->");
			p2.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
			p2.println("<jasperReport");
			p2.println("name='ProjectbyDonorPdf'");
			p2.println("columnCount='1'");
			p2.println("printOrder='Vertical'");
			p2.println("orientation='Landscape'");
			p2.println("pageWidth='842'");
			p2.println("pageHeight='595'");
			p2.println("columnWidth='535'");
			p2.println("columnSpacing='0'");
			p2.println("leftMargin='21'");
			p2.println("rightMargin='21'");
			p2.println("topMargin='14'");
			p2.println("bottomMargin='14'");
			p2.println("whenNoDataType='NoPages'");
			p2.println("isTitleNewPage='false'");
			p2.println("isSummaryNewPage='false'>");
			p2.println("<property name='ireport.scriptlethandling' value='2' />");
			p2.println("<parameter name='qu' isForPrompting='false' class='java.lang.String'>");
			p2.println("<defaultValueExpression ><![CDATA[\"select * from my_table\"]]></defaultValueExpression>");
			p2.println("</parameter>");
			p2.println("<parameter name='nam' isForPrompting='false' class='java.lang.String'>");
			p2.println("<defaultValueExpression ><![CDATA[\"pbd\"]]></defaultValueExpression>");
			p2.println("</parameter>");
			p2.println("<queryString><![CDATA[$P!{qu}]]></queryString>");
			
			//DYNAMIC CCCCCCCCC
			String dc;
			for(int k=1;k<=(10+5+(4*n));k++)
			{
				
				dc="c"+k;
				p2.println("<field name='"+dc+"' class='java.lang.String'/>");
			}
			
			p2.println("<group  name='Data' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
			p2.println("<groupExpression><![CDATA[$F{c5}]]></groupExpression>");
			p2.println("<groupHeader>");
			p2.println("<band height='52'  isSplitAllowed='true' >");
			
			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='67'");
			p2.println("y='30'");
			p2.println("width='"+(180+180+(n-1)*180)+"'");
			p2.println("height='19'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Agency: \"+$F{c5}]]></textFieldExpression>");
			p2.println("</textField>");
			
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='0'");
			p2.println("width='67'");
			p2.println("height='49'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-6'");
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
			
			
			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='67'");
			p2.println("y='30'");
			p2.println("width='"+(180 + 180+(n-1)*180)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#000000'");
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
			
			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='67'");
			p2.println("y='15'");
			p2.println("width='"+(180+180+(n-1)*180)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#000000'");
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
			
			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='50'");
			p2.println("width='"+(180 + 247+(n-1)*180)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#000000'");
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
			
			x=(67+center);y=0;
			x1=(67+center);y1=0;
			c=7;
			for(int j=0;j<n;j++)
			{
			ctextkey="c"+c;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='"+x+"'");
			p2.println("y='0'");
			p2.println("width='180'");
			p2.println("height='14'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField-11'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Year:  \"+$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			x += 180;
			c +=4;
			
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x1+"'");
			p2.println("y='15'");
			p2.println("width='58'");
			p2.println("height='14'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-7'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Planned Disb  ]]></text>");
			p2.println("</staticText>");
			
			x1 +=	60;
			
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x1+"'");
			p2.println("y='15'");
			p2.println("width='58'");
			p2.println("height='14'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-8'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Disb  ]]></text>");
			p2.println("</staticText>");
			
			x1 += 60;
			
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+(x1)+"'");
			p2.println("y='15'");
			p2.println("width='58'");
			p2.println("height='14'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-9'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Exp  ]]></text>");
			p2.println("</staticText>");
			
			x1 += 56;
			}// for1
			
			//---
					p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x1+"'");
			p2.println("y='15'");
			p2.println("width='58'");
			p2.println("height='14'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-7'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Planned Disb  ]]></text>");
			p2.println("</staticText>");
			
			x1 +=	60;
			
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x1+"'");
			p2.println("y='15'");
			p2.println("width='58'");
			p2.println("height='14'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-8'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Disb  ]]></text>");
			p2.println("</staticText>");
			
			x1 += 60;
			
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+(x1)+"'");
			p2.println("y='15'");
			p2.println("width='58'");
			p2.println("height='14'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-9'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Exp  ]]></text>");
			p2.println("</staticText>");



			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='"+x+"'");
			p2.println("y='0'");
			p2.println("width='180'");
			p2.println("height='14'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='staticText-99'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA[Total  ]]></text>");
			p2.println("</staticText>");

			//--
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
			p2.println("<band height='35'  isSplitAllowed='true' >");


			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='0'");
			p2.println("width='727'");
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
			p2.println("<text><![CDATA[Annual Report By Project]]></text>");
			p2.println("</staticText>");

			p2.println("</band>");
			p2.println("</title>");
			p2.println("<pageHeader>");
			p2.println("<band height='46'  isSplitAllowed='true' >");

			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='46'");
			p2.println("width='"+(180+247+(n-1)*180)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='line-5'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
			p2.println("</line>");

			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >	<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='0'");
			p2.println("width='800'");
			p2.println("height='15'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField-28'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Justified' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Workspace : \"+$F{c1}]]></textFieldExpression>");
			p2.println("</textField>");

			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='15'");
			p2.println("width='800'");
			p2.println("height='15'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField-28'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Justified' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Filters: \"+$F{c2}]]></textFieldExpression>");
			p2.println("</textField>");

			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='30'");
			p2.println("width='800'");
			p2.println("height='15'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#CCCCCC'");
			p2.println("key='textField-28'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Justified' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"              \"+$F{c3}]]></textFieldExpression>");
			p2.println("</textField>");
			p2.println("</band>");
			p2.println("</pageHeader>");
			p2.println("<columnHeader>");
			p2.println("<band height='0'  isSplitAllowed='true' >");
			p2.println("</band>");
			p2.println("</columnHeader>");
			p2.println("<detail>");
			p2.println("<band height='"+height+"' isSplitAllowed='true' >");
			p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='0'");
			p2.println("y='0'");
			p2.println("width='65'");
			p2.println("height='"+height+"'");
//			p2.println("height='65'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-10'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='Float'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c6}]]></textFieldExpression>");
			p2.println("</textField>");
			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='"+(height - 1)+"'");
//			p2.println("y='64'");
			p2.println("width='"+(180+240+(n-1)*185)+"'");
			p2.println("height='0'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='line-4'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
			p2.println("</line>");
			
			x=(67+center);y=0;
			c=8;
			for(int j=0;j<n;j++)
			{
			ctextkey="c"+c;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x+"'");
			p2.println("y='0'");
			p2.println("width='59'");
//			p2.println("height='62'");
			p2.println("height='"+height+"'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-11'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			c +=1;
			ctextkey="c"+c;
			x +=59;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x+"'");
			p2.println("y='0'");
			p2.println("width='59' ");
			p2.println("height='"+height+"'");
//			p2.println("height='62'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-11'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			c +=1;
			ctextkey="c"+c;
			x +=59;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x+"'");
			p2.println("y='0'");
			p2.println("width='59'");
			p2.println("height='"+height+"'");
//			p2.println("height='62'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-27'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			x += 59;
			c += 2;
			
			}//for2
	
			c=7+(4*n);
			for(int j=0;j<3;j++)
			{
			ctextkey="c"+c;
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='"+x+"'");
			p2.println("y='0'");
			p2.println("width='59'");
			p2.println("height='"+height+"'");
//			p2.println("height='62'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-11'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
			p2.println("</textField>");
			
			c +=1;
			x +=59;
					
			}//for3

			
			p2.println("</band>");
			p2.println("</detail>");
			p2.println("<columnFooter>");
			p2.println("<band height='0'  isSplitAllowed='true' >");
			p2.println("</band>");
			p2.println("</columnFooter>");

			p2.println("<pageFooter>");
			p2.println("<band height='22'  isSplitAllowed='true' >");
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='2'");
			p2.println("y='4'");
			p2.println("width='300'");
			p2.println("height='16'");
			p2.println("forecolor='#3333FF'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-4'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Times-Roman' pdfFontName='Times-Roman' size='12' isBold='false' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='CP1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\" * All the amounts are in thousands (000) \"]]></textFieldExpression>");
			p2.println("</textField>");
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='575'");
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
			
			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='true' evaluationTime='Report' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Transparent'");
			p2.println("x='757'");
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

			
			p2.println("<summary>");
			p2.println("<band height='20'  isSplitAllowed='true' >");
			p2.println("</band>");
			p2.println("</summary>");
			p2.println("</jasperReport>");

			p2.close();

		}
		catch (Exception e)
		{
			//System.err.println("File error");
		}
	}//CreateJrxml
}
*/
