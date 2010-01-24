package org.digijava.module.aim.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class AdvancedReportPdfJrxml
{

//static String cols[];
public static void createJRXML(String filePath, boolean undis, String labels[],Object array[][], int cols, int measureCount, String reportName, String reportType,int hierarchy)
{
	int mcnt = measureCount;
		String arr[][] = new String[array.length][array[0].length];
		try
			{
			FileOutputStream out2; // declare a file output object
			PrintStream p2; // declare a print stream object
			File fopen = new File(filePath);	

			out2 = new FileOutputStream(fopen);
			p2 = new PrintStream(out2);
			p2.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		//	////System.out.println("creating now- dynamic trend...");

			int pgwidth = (cols*60)+((measureCount+1)*4*60);
			int n=3;//cnt;
			int center=0;//(12-n)*30;
			int x=(0+center),x1=0,y=0,y1=0,xl=0,yl=0;
			int textkey=11,linekey=21,c=0;
			String ctextkey="";
			int m=0,yr=0,yrwidth=0,temp=0,bandheight=0,w=0;
			int loop=1,hcnt=0;
			int des = 0;
			if(reportType.equals("csv") || reportType.equals("xls"))
				{
					des=120;
					if(undis)
					{
						pgwidth = (cols*des)+((measureCount-1)*60*4)+measureCount*60 +60;
						////System.out.println("pgwidthxlstrue" + pgwidth);
					}
					else
					{
						pgwidth = (cols*des)+((measureCount)*60*4)+measureCount*60 +60;
						////System.out.println("pgwidthxlsfalse	" + pgwidth);
					}
				}
				else 
				{
					
					des = 120;
					if(undis)
					{
						pgwidth = (cols*des)+((measureCount-1)*60*4)+measureCount*60 +60;
					}
					else
					{
							pgwidth = (cols*des)+((measureCount)*60*4)+measureCount*60 +60;
					}
				}
			if(hierarchy>0)
				hcnt=hierarchy;

			p2.println("<!-- Created with iReport - A designer for JasperReports -->");
			p2.println("<!DOCTYPE jasperReport PUBLIC '//JasperReports//DTD Report Design//EN' 'http://jasperreports.sourceforge.net/dtds/jasperreport.dtd'>");
			p2.println("<jasperReport");
			p2.println("name='"+reportName+"'");
			p2.println("columnCount='1'");
			p2.println("printOrder='Vertical'");
			p2.println("orientation='Landscape'");
			p2.println("pageWidth='"+pgwidth+"'");
			p2.println("pageHeight='842'");
			p2.println("columnWidth='500'");
			p2.println("columnSpacing='0'");
			p2.println("leftMargin='21'");
			p2.println("rightMargin='21'");
			p2.println("topMargin='21'");
			p2.println("bottomMargin='21'");
			p2.println("whenNoDataType='NoPages'");
			p2.println("isTitleNewPage='false'");
			p2.println("isSummaryNewPage='false'>");
			p2.println("<property name='ireport.scriptlethandling' value='2' />");
			p2.println("<queryString><![CDATA[$P!{qu}]]></queryString>");
//			DYNAMIC CCCCCCCCC
			String dc;
			int colCnt = 20+cols+4*mcnt;
	//		////System.out.println(" Cnt = " + colCnt);
//			 gets the no of fields = 4 constants fields + YearCnt*3 + yearCnt
			for(int k=0; k<=colCnt; k++)
			{
//				////System.out.println("k="+k);
				dc="c"+k;
				p2.println("<field name='"+dc+"' class='java.lang.String'/>");
			}
		

			colCnt = (20+cols+4*mcnt);
			for(int k=0; k<=colCnt; k++)
			{
//				////System.out.println("k="+k);
				dc="m"+k;
				p2.println("<field name='"+dc+"' class='java.lang.String'/>");
			}

			int lwidth=0;
			if(undis)
				{
							//	////System.out.println("des "+des); 
					//lwidth=(cols+((mcnt-1)*3)+mcnt)*60*2-150;
					//lwidth = ((des*cols)+((measureCount-1)*4*60));
					lwidth = pgwidth -60;
				}

			else
				{
				//////System.out.println("des "+des);
									//lwidth = ((des*cols)+((measureCount)*4*60));
									lwidth = pgwidth -60;
					//lwidth=(cols+(mcnt*3)+mcnt)*60*2-150;
				}

				if(hierarchy > 0)
				{
/*p2.println("<group  name='Hierarchy' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
p2.println("<groupExpression><![CDATA[$F{c4}]]></groupExpression>");
p2.println("<groupHeader>");
p2.println("<band height='20' isSplitAllowed='true' >");

p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
p2.println("mode='Opaque'");
p2.println("x='0'");
p2.println("y='0'");
p2.println("width='"+(lwidth)+"'");
p2.println("height='20'");
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
p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
p2.println("</textElement>");
p2.println("<textFieldExpression class='java.lang.String'><![CDATA[\" \"+ $F{c4}]]></textFieldExpression>");
p2.println("</textField>");

p2.println("</band>");
p2.println("</groupHeader>");
p2.println("<groupFooter>");
p2.println("<band height='0'  isSplitAllowed='true' >");
p2.println("</band>");
p2.println("</groupFooter>");
p2.println("</group>");

*/			
					String htext="";
					int h=3+1;
						if(hierarchy > 0)
						{
						for (int i=0;i<hierarchy;i++)
						{
								htext="h"+i;
								//htextkey="h"+i;
								ctextkey="c"+(h++);
							//	////System.out.println("grr"+ctextkey);
								p2.println("<group  name='"+htext+"' isStartNewColumn='false' isStartNewPage='false' isResetPageNumber='false' isReprintHeaderOnEachPage='false' minHeightToStartNewPage='0' >");
								p2.println("<groupExpression><![CDATA[$F{"+ctextkey+"}]]></groupExpression>");
								p2.println("<groupHeader>");
								p2.println("<band height='20' isSplitAllowed='true' >");

								p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
								p2.println("mode='Opaque'");
								p2.println("x='0'");
								p2.println("y='0'");
								p2.println("width='"+(lwidth)+"'");
								p2.println("height='20'");
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
								p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
								p2.println("</textElement>");
								p2.println("<textFieldExpression class='java.lang.String'><![CDATA[\" \"+ $F{"+ctextkey+"}]]></textFieldExpression>");
								p2.println("</textField>");

								p2.println("</band>");
								p2.println("</groupHeader>");
								p2.println("<groupFooter>");
								p2.println("<band height='0'  isSplitAllowed='true' >");
								p2.println("</band>");
								p2.println("</groupFooter>");
								p2.println("</group>");
//								h++;
							}
					}
					
				}

			p2.println("<background>");
			p2.println("<band height='0'  isSplitAllowed='true' >");
			p2.println("</band>");
			p2.println("</background>");
			p2.println("<title>");

			if(reportType.equals("csv") || reportType.equals("xls"))
				bandheight=80+35;
			else
				bandheight=80;

			p2.println("<band height='"+bandheight+"'  isSplitAllowed='true' >");

			String reportName2=reportName;
			reportName2=reportName2.replaceAll("_"," ");
			
			p2.println("<staticText>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='0'");
			p2.println("width='519'");
			p2.println("height='20'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='staticText-1'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='14' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<text><![CDATA["+reportName2+"]]></text>");
			p2.println("</staticText>");

			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='20'");
			p2.println("width='519'");
			p2.println("height='20'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-12'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"WorkSpace Details:\"+$F{c1}]]></textFieldExpression>");
			p2.println("</textField>");

			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='40'");
			p2.println("width='"+lwidth+"'");
			p2.println("height='18'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-13'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[\"Filters: \"+$F{c2}]]></textFieldExpression>");
			p2.println("</textField>");

			p2.println("<textField isStretchWithOverflow='false' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='60'");
			p2.println("width='"+lwidth+"'");
			p2.println("height='18'");
			p2.println("forecolor='#000000'");
			p2.println("backcolor='#FFFFFF'");
			p2.println("key='textField-13'");
			p2.println("stretchType='NoStretch'");
			p2.println("positionType='FixRelativeToTop'");
			p2.println("isPrintRepeatedValues='true'");
			p2.println("isRemoveLineWhenBlank='false'");
			p2.println("isPrintInFirstWholeBand='false'");
			p2.println("isPrintWhenDetailOverflows='false'/>");
			p2.println("<textElement textAlignment='Left' verticalAlignment='Top' rotation='None' lineSpacing='Single'>");
			p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
			p2.println("</textElement>");
			p2.println("<textFieldExpression   class='java.lang.String'><![CDATA[$F{c3}]]></textFieldExpression>");
			p2.println("</textField>");

			if(!reportType.equals("csv")){
			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='79'");
			p2.println("width='"+lwidth+"'");
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
			}


			if(reportType.equals("csv")){	
				x=0;y=80;
				w=0;
				m=1;
				m--;/////////m------------
				for(int i=0;i< cols;i++)
				{
					w=60;
					ctextkey="m"+m;
					p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
					p2.println("mode='Opaque'");
					p2.println("x='"+x+"'");
					p2.println("y='"+y+"'");
					p2.println("width='60'");
					p2.println("height='33'");
					p2.println("forecolor='#000000'");
					p2.println("backcolor='#CCCCCC'");
					p2.println("key='textField-171'");
					p2.println("stretchType='NoStretch'");
					p2.println("positionType='Float'");
					p2.println("isPrintRepeatedValues='true'");
					p2.println("isRemoveLineWhenBlank='false'");
					p2.println("isPrintInFirstWholeBand='false'");
					p2.println("isPrintWhenDetailOverflows='false'/>");
					p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("</textElement>");
					p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
					p2.println("</textField>");

					x +=60;
					m++;
				}

				yr=2000;
//				x+=60;
				x1+=x;
				temp=cols;
				

					yrwidth=(measureCount*60);
					//////System.out.println("****"+chek(labels));
					
					if(undis){
//						////System.out.println("grrr.."+m);
						yrwidth=(measureCount-1)*60;
					}

				c= 3+cols+1;

					m=cols+2;
					for(int j=0;j<3;j++){
					//	////System.out.println("grrr.."+arr[0][cols+j]);
							ctextkey= "c"+c;
								p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
								p2.println("mode='Opaque'");
								p2.println("x='"+x+"'");
								p2.println("y='"+y+"'");
								p2.println("width='"+yrwidth+"'");
								p2.println("height='15'");
								p2.println("forecolor='#000000'");
								p2.println("backcolor='#CCCCCC'");
								p2.println("key='textField-77'");
								p2.println("stretchType='NoStretch'");
								p2.println("positionType='Float'");
								p2.println("isPrintRepeatedValues='true'");
								p2.println("isRemoveLineWhenBlank='false'");
								p2.println("isPrintInFirstWholeBand='false'");
								p2.println("isPrintWhenDetailOverflows='false'/>");
								p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
								p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
								p2.println("</textElement>");
								p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
								p2.println("</textField>");
								
								if( undis )
									c+=measureCount;
								else
									c+=measureCount+1;

						x+=yrwidth;
								
						if(undis)
							mcnt = measureCount-1;
						m--;	
						//////System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&" +mcnt);
						for(int i=0;i<mcnt;i++){
							//if(!labels[(temp)+i].equals("Undisbursed"))
							{	
								ctextkey="m"+m;
								//////System.out.println("com found.."+i);
								p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
								p2.println("mode='Transparent'");
								p2.println("x='"+x1+"'");
								p2.println("y='"+(y+15)+"'");
								p2.println("width='60'");
								p2.println("height='18'");
								p2.println("forecolor='#000000'");
								p2.println("backcolor='#FFFFFF'");
								p2.println("key='textField-172'");
								p2.println("stretchType='NoStretch'");
								p2.println("positionType='Float'");
								p2.println("isPrintRepeatedValues='true'");
								p2.println("isRemoveLineWhenBlank='false'");
								p2.println("isPrintInFirstWholeBand='false'");
								p2.println("isPrintWhenDetailOverflows='false'/>");
								p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
								p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
								p2.println("</textElement>");
								p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
								p2.println("</textField>");
							
							x1+=60;
							m++;
							}
							//////System.out.println("&&&&&&&&&&------------------" + ctextkey);
						}
						//////System.out.println("temp:"+temp++);
						m++;
						////System.out.println("mmm "+m);
					}
					//// m--------------- m--;
					temp=0;
					if( undis ){
						mcnt = measureCount;
						temp=(3+cols+(mcnt*3))+1;
						ctextkey = "c"+temp;
						 //////System.out.println("inside here..@@@@@@@@@@@@@@@@@@"+cols+":"+mcnt+":::"+temp+"-----"+ctextkey);
					}
					else{
						mcnt = measureCount;
						temp=(3+cols+((mcnt+1)*3))+1;
						ctextkey = "c"+temp;
						//////System.out.println("inside here..$$$$$$$$$$$$$$$$$$$$"+cols+":"+mcnt+":::"+temp+"-----"+ctextkey);
					}
					
					p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
					p2.println("mode='Opaque'");
					p2.println("x='"+x+"'");
					p2.println("y='"+y+"'");
					p2.println("width='"+(measureCount*60)+"'");
					p2.println("height='15'");
					p2.println("forecolor='#000000'");
					p2.println("backcolor='#CCCCCC'");
					p2.println("key='textField-173'");
					p2.println("stretchType='NoStretch'");
					p2.println("positionType='Float'");
					p2.println("isPrintRepeatedValues='true'");
					p2.println("isRemoveLineWhenBlank='false'");
					p2.println("isPrintInFirstWholeBand='false'");
					p2.println("isPrintWhenDetailOverflows='false'/>");
					p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("</textElement>");
					p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
					p2.println("</textField>");

					int measureBlocks=measureCount;
					if(undis){
						//measureBlocks=3*(measureCount-1);
					}
					else{
						//measureBlocks=3*(measureCount);
					}
				m--;
					
					//////System.out.println("BLOCKS............."+measureBlocks+" M Value ============================" + m);
					for(int i=1;i<=measureBlocks;i++){
					
						ctextkey="m"+m;
						p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
						p2.println("mode='Transparent'");
						p2.println("x='"+x1+"'");
						p2.println("y='"+(y+15)+"'");
						p2.println("width='60'");
						p2.println("height='18'");
						p2.println("forecolor='#000000'");
						p2.println("backcolor='#FFFFFF'");
						p2.println("key='textField-174'");
						p2.println("stretchType='NoStretch'");
						p2.println("positionType='Float'");
						p2.println("isPrintRepeatedValues='true'");
						p2.println("isRemoveLineWhenBlank='false'");
						p2.println("isPrintInFirstWholeBand='false'");
						p2.println("isPrintWhenDetailOverflows='false'/>");
						p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
						p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
						p2.println("</textElement>");
//						p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+arr[0][temp+i]+"}]]></textFieldExpression>");
						p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
						p2.println("</textField>");
						
						m++;
						x1+=60;

					}

				if(!reportType.equals("csv")){	
				p2.println("<line direction='TopDown'>");
				p2.println("<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='0'");
				p2.println("y='"+(y+34)+"'");
				p2.println("width='"+lwidth+"'");
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
				}

				
			}
			
			// non repeating header for XLS
			if(reportType.equals("xls")){
			x=0;y=80;
			w=0;
			m=1;
			
			if(!reportType.equals("csv")){
			for(int i=0;i< cols;i++)
			{
				
				w=60;
				ctextkey="m"+m;
				p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='"+x+"'");
				p2.println("y='"+y+"'");
				p2.println("width='"+des+"'");
				p2.println("height='33'");
				p2.println("forecolor='#000000'");
				p2.println("backcolor='#CCCCCC'");
				p2.println("key='textField-171'");
				p2.println("stretchType='NoStretch'");
				p2.println("positionType='Float'");
				p2.println("isPrintRepeatedValues='true'");
				p2.println("isRemoveLineWhenBlank='false'");
				p2.println("isPrintInFirstWholeBand='false'");
				p2.println("isPrintWhenDetailOverflows='false'/>");
				p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
				p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
				p2.println("</textElement>");
				p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
				p2.println("</textField>");
///hereeeeeee
				x+=des;
				m++;
			}

			yr=2000;
//			x+=60;
			x1+=x;
			temp=cols;

				yrwidth=(measureCount*60);
				//////System.out.println("****"+chek(labels));
				
				if(undis){
//					////System.out.println("grrr.."+m);
					yrwidth=(measureCount-1)*60;
				}

			c= 3+cols+1+hcnt;

				m=cols+2;
				for(int j=0;j<3;j++){
					//////System.out.println("flag test.."+arr[0][cols+j]);
						ctextkey= "c"+c;
							p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
							p2.println("mode='Opaque'");
							p2.println("x='"+x+"'");
							p2.println("y='"+y+"'");
							p2.println("width='"+yrwidth+"'");
							p2.println("height='15'");
							p2.println("forecolor='#000000'");
							p2.println("backcolor='#CCCCCC'");
							p2.println("key='textField-77'");
							p2.println("stretchType='NoStretch'");
							p2.println("positionType='Float'");
							p2.println("isPrintRepeatedValues='true'");
							p2.println("isRemoveLineWhenBlank='false'");
							p2.println("isPrintInFirstWholeBand='false'");
							p2.println("isPrintWhenDetailOverflows='false'/>");
							p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
							p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
							p2.println("</textElement>");
							p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
							p2.println("</textField>");
							
							if( undis )
								c+=measureCount;
							else
								c+=measureCount+1;

					x+=yrwidth;
					m--;
					if(undis)
						mcnt = measureCount-1;
					y1 =95;
						
					//////System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&" +mcnt);
					for(int i=0;i<mcnt;i++){
						//if(!labels[(temp)+i].equals("Undisbursed"))
						{	
							ctextkey="m"+m;
							//////System.out.println("com found.."+i);
							p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
							p2.println("mode='Transparent'");
							p2.println("x='"+x1+"'");
							p2.println("y='"+y1+"'");
							p2.println("width='60'");
							p2.println("height='18'");
							p2.println("forecolor='#000000'");
							p2.println("backcolor='#FFFFFF'");
							p2.println("key='textField-172'");
							p2.println("stretchType='NoStretch'");
							p2.println("positionType='Float'");
							p2.println("isPrintRepeatedValues='true'");
							p2.println("isRemoveLineWhenBlank='false'");
							p2.println("isPrintInFirstWholeBand='false'");
							p2.println("isPrintWhenDetailOverflows='false'/>");
							p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
							p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
							p2.println("</textElement>");
							p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
							p2.println("</textField>");
						
						x1+=60;
						m++;
						}
						//////System.out.println("&&&&&&&&&&------------------" + ctextkey);
					}
					////System.out.println("temp:"+m);
					m++;
				}
				
				temp=0;
				if( undis ){
					mcnt = measureCount;
					temp=(3+cols+(mcnt*3))+1;
					ctextkey = "c"+temp;
					//////System.out.println("inside here..@@@@@@@@@@@@@@@@@@"+cols+":"+mcnt+":::"+temp+"-----"+ctextkey);
				}
				else{
					mcnt = measureCount;
					temp=(3+cols+((mcnt+1)*3))+1;
					ctextkey = "c"+temp;
					//////System.out.println("inside here..$$$$$$$$$$$$$$$$$$$$"+cols+":"+mcnt+":::"+temp+"-----"+ctextkey);
				}
			
				p2.println("<staticText>");
				p2.println("<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='"+x+"'");
				p2.println("y='"+y+"'");
				p2.println("width='"+(measureCount*60)+"'");
				p2.println("height='15'");
				p2.println("forecolor='#000000'");
				p2.println("backcolor='#CCCCCC'");
				p2.println("key='staticText-h1'");
				p2.println("stretchType='NoStretch'");
				p2.println("positionType='FixRelativeToTop'");
				p2.println("isPrintRepeatedValues='true'");
				p2.println("isRemoveLineWhenBlank='false'");
				p2.println("isPrintInFirstWholeBand='false'");
				p2.println("isPrintWhenDetailOverflows='false'/>");
				p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
				p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
				p2.println("</textElement>");
				p2.println("<text><![CDATA[Total]]></text>");
				p2.println("</staticText>");

/*				p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='"+x+"'");
				p2.println("y='0'");
				p2.println("width='"+(measureCount*60)+"'");
				p2.println("height='15'");
				p2.println("forecolor='#000000'");
				p2.println("backcolor='#CCCCCC'");
				p2.println("key='textField-173'");
				p2.println("stretchType='NoStretch'");
				p2.println("positionType='Float'");
				p2.println("isPrintRepeatedValues='true'");
				p2.println("isRemoveLineWhenBlank='false'");
				p2.println("isPrintInFirstWholeBand='false'");
				p2.println("isPrintWhenDetailOverflows='false'/>");
				p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
				p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
				p2.println("</textElement>");
				p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
				p2.println("</textField>");
*/
				int measureBlocks=measureCount;
				if(undis){
					//measureBlocks=3*(measureCount-1);
				}
				else{
					//measureBlocks=3*(measureCount);
				}
				m--;
				//////System.out.println("BLOCKS............."+measureBlocks+" M Value ============================" + m);
				for(int i=1;i<=measureBlocks;i++){
				
					ctextkey="m"+m;
					p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
					p2.println("mode='Transparent'");
					p2.println("x='"+x1+"'");
					p2.println("y='"+(y+15)+"'");
					p2.println("width='60'");
					p2.println("height='18'");
					p2.println("forecolor='#000000'");
					p2.println("backcolor='#FFFFFF'");
					p2.println("key='textField-174'");
					p2.println("stretchType='NoStretch'");
					p2.println("positionType='Float'");
					p2.println("isPrintRepeatedValues='true'");
					p2.println("isRemoveLineWhenBlank='false'");
					p2.println("isPrintInFirstWholeBand='false'");
					p2.println("isPrintWhenDetailOverflows='false'/>");
					p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("</textElement>");
//					p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+arr[0][temp+i]+"}]]></textFieldExpression>");
					p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
					p2.println("</textField>");
					
					m++;
					x1+=60;

				}
			}
			}// end xls non repeating bar.
			
			p2.println("</band>");
			p2.println("</title>");
	if(!reportType.equals("csv") && !reportType.equals("xls")){
			p2.println("<pageHeader>");

			bandheight=35;
			p2.println("<band height='"+bandheight+"'  isSplitAllowed='true' >");
			x=0;
			w=0;
			m=1;
			if(!reportType.equals("csv")){
			for(int i=0;i< cols;i++)
			{
				w=60;
				ctextkey="m"+m;
				p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='"+x+"'");
				p2.println("y='0'");
				p2.println("width='"+des+"'");
				p2.println("height='33'");
				p2.println("forecolor='#000000'");
				p2.println("backcolor='#CCCCCC'");
				p2.println("key='textField-171'");
				p2.println("stretchType='NoStretch'");
				p2.println("positionType='Float'");
				p2.println("isPrintRepeatedValues='true'");
				p2.println("isRemoveLineWhenBlank='false'");
				p2.println("isPrintInFirstWholeBand='false'");
				p2.println("isPrintWhenDetailOverflows='false'/>");
				p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
				p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
				p2.println("</textElement>");
				p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
				p2.println("</textField>");
				x+=des;
				//x +=60*3;
				m++;
			}

			yr=2000;
//			x+=60;
			x1+=x;
			temp=cols;

				yrwidth=(measureCount*60);
				//////System.out.println("****"+chek(labels));
				
				if(undis){
//					////System.out.println("grrr.."+m);
					yrwidth=(measureCount-1)*60;
				}
		
			c= 3+cols+1+hcnt;

				m=cols+2;
				for(int j=0;j<3;j++){
					//////System.out.println("flag test.."+arr[0][cols+j]);
						ctextkey= "c"+c;
							p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
							p2.println("mode='Opaque'");
							p2.println("x='"+x+"'");
							p2.println("y='0'");
							p2.println("width='"+yrwidth+"'");
							p2.println("height='15'");
							p2.println("forecolor='#000000'");
							p2.println("backcolor='#CCCCCC'");
							p2.println("key='textField-77'");
							p2.println("stretchType='NoStretch'");
							p2.println("positionType='Float'");
							p2.println("isPrintRepeatedValues='true'");
							p2.println("isRemoveLineWhenBlank='false'");
							p2.println("isPrintInFirstWholeBand='false'");
							p2.println("isPrintWhenDetailOverflows='false'/>");
							p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
							p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
							p2.println("</textElement>");
							p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
							p2.println("</textField>");
							
							if( undis )
								c+=measureCount;
							else
								c+=measureCount+1;

					x+=yrwidth;
					m--;			
					if(undis)
						mcnt = measureCount-1;
						
					//////System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&" +mcnt);
					for(int i=0;i<mcnt;i++){
						//if(!labels[(temp)+i].equals("Undisbursed"))
						{	
							ctextkey="m"+m;
							//////System.out.println("com found.."+i);
							p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
							p2.println("mode='Transparent'");
							p2.println("x='"+x1+"'");
							p2.println("y='15'");
							p2.println("width='60'");
							p2.println("height='18'");
							p2.println("forecolor='#000000'");
							p2.println("backcolor='#FFFFFF'");
							p2.println("key='textField-172'");
							p2.println("stretchType='NoStretch'");
							p2.println("positionType='Float'");
							p2.println("isPrintRepeatedValues='true'");
							p2.println("isRemoveLineWhenBlank='false'");
							p2.println("isPrintInFirstWholeBand='false'");
							p2.println("isPrintWhenDetailOverflows='false'/>");
							p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
							p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
							p2.println("</textElement>");
							p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
							p2.println("</textField>");
						
						x1+=60;
						m++;
						}
						//////System.out.println("&&&&&&&&&&------------------" + ctextkey);
					}
					//////System.out.println("temp:"+temp++);
					m++;
				}
				
				temp=0;
				if( undis ){
					mcnt = measureCount;
					temp=(3+cols+(mcnt*3))+1;
					ctextkey = "c"+temp;
					//////System.out.println("inside here..@@@@@@@@@@@@@@@@@@"+cols+":"+mcnt+":::"+temp+"-----"+ctextkey);
				}
				else{
					mcnt = measureCount;
					temp=(3+cols+((mcnt+1)*3))+1;
					ctextkey = "c"+temp;
					//////System.out.println("inside here..$$$$$$$$$$$$$$$$$$$$"+cols+":"+mcnt+":::"+temp+"-----"+ctextkey);
				}

				p2.println("<staticText>");
				p2.println("<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='"+x+"'");
				p2.println("y='0'");
				p2.println("width='"+(measureCount*60)+"'");
				p2.println("height='15'");
				p2.println("forecolor='#000000'");
				p2.println("backcolor='#CCCCCC'");
				p2.println("key='staticText-h1'");
				p2.println("stretchType='NoStretch'");
				p2.println("positionType='FixRelativeToTop'");
				p2.println("isPrintRepeatedValues='true'");
				p2.println("isRemoveLineWhenBlank='false'");
				p2.println("isPrintInFirstWholeBand='false'");
				p2.println("isPrintWhenDetailOverflows='false'/>");
				p2.println("<textElement textAlignment='Center' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
				p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='10' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
				p2.println("</textElement>");
				p2.println("<text><![CDATA[Total]]></text>");
				p2.println("</staticText>");

				int measureBlocks=measureCount;
				if(undis){
					//measureBlocks=3*(measureCount-1);
				}
				else{
					//measureBlocks=3*(measureCount);
				}
				m--;
				//////System.out.println("BLOCKS............."+measureBlocks+" M Value ============================" + m);
				for(int i=1;i<=measureBlocks;i++){
				
					ctextkey="m"+m;
					p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
					p2.println("mode='Transparent'");
					p2.println("x='"+x1+"'");
					p2.println("y='15'");
					p2.println("width='60'");
					p2.println("height='18'");
					p2.println("forecolor='#000000'");
					p2.println("backcolor='#FFFFFF'");
					p2.println("key='textField-174'");
					p2.println("stretchType='NoStretch'");
					p2.println("positionType='Float'");
					p2.println("isPrintRepeatedValues='true'");
					p2.println("isRemoveLineWhenBlank='false'");
					p2.println("isPrintInFirstWholeBand='false'");
					p2.println("isPrintWhenDetailOverflows='false'/>");
					p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
					p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
					p2.println("</textElement>");
//					p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+arr[0][temp+i]+"}]]></textFieldExpression>");
					p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
					p2.println("</textField>");
					
					m++;
					x1+=60;

				}

			if(!reportType.equals("csv")){
			p2.println("<line direction='TopDown'>");
			p2.println("<reportElement");
			p2.println("mode='Opaque'");
			p2.println("x='0'");
			p2.println("y='34'");
			p2.println("width='"+lwidth+"'");
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
			}

			}
			
			
			p2.println("</band>");
			//////System.out.println("Page..........Header " + c);
			p2.println("</pageHeader>");}
			p2.println("<columnHeader>");
			p2.println("<band height='0'  isSplitAllowed='true' >");
			p2.println("</band>");
			p2.println("</columnHeader>");
			p2.println("<detail>");
			p2.println("<band height='83'  isSplitAllowed='true' >");

			x=0;x1=0;
			w=0;
			c=4+hcnt;


				for(int i=0;i<cols;i++)
				{
					ctextkey = "c"+c;
							p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
							p2.println("mode='Transparent'");
							p2.println("x='"+x+"'");
							p2.println("y='0'");
							p2.println("width='"+des+"'");
							p2.println("height='80'");
							p2.println("forecolor='#000000'");
							p2.println("backcolor='#FFFFFF'");
							p2.println("key='textField-175'");
							p2.println("stretchType='NoStretch'");
							p2.println("positionType='Float'");
							p2.println("isPrintRepeatedValues='true'");
							p2.println("isRemoveLineWhenBlank='false'");
							p2.println("isPrintInFirstWholeBand='false'");
							p2.println("isPrintWhenDetailOverflows='false'/>");
							p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
							p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
							p2.println("</textElement>");
							p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
							p2.println("</textField>");

					c++;
					////hereeeeeeeeeeee
					x+=des;
					//x+=60*3;
				}

			int dcnt=0;
			if(undis)
				dcnt=(measureCount-1);
			else
				dcnt=measureCount;

			c=3+cols+2+hcnt;

				for(int i=0;i<3;i++)
				{
					for(int j=0;j<dcnt;j++)
					{		
					ctextkey = "c"+c;
//					////System.out.println("########LOOP:: "+i+" ::"+x+":"+w+":"+arr[0][i]);
					w=60;

							p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
							p2.println("mode='Transparent'");
							p2.println("x='"+x+"'");
							p2.println("y='0'");
							p2.println("width='"+w+"'");
							p2.println("height='80'");
							p2.println("forecolor='#000000'");
							p2.println("backcolor='#FFFFFF'");
							p2.println("key='textField-1766'");
							p2.println("stretchType='NoStretch'");
							p2.println("positionType='Float'");
							p2.println("isPrintRepeatedValues='true'");
							p2.println("isRemoveLineWhenBlank='false'");
							p2.println("isPrintInFirstWholeBand='false'");
							p2.println("isPrintWhenDetailOverflows='false'/>");
							p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
							p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
							p2.println("</textElement>");
							p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
							p2.println("</textField>");

						x +=60;
						c++;
					}
						c++;

				}

			c=3+cols+3*dcnt+3+2+hcnt;
			//////System.out.println("----------------->"+c);

					for(int j=0;j<mcnt;j++)
					{		
					ctextkey = "c"+c;
					w=60;
							p2.println("<textField isStretchWithOverflow='true' pattern='' isBlankWhenNull='false' evaluationTime='Now' hyperlinkType='None' >					<reportElement");
							p2.println("mode='Transparent'");
							p2.println("x='"+x+"'");
							p2.println("y='0'");
							p2.println("width='"+w+"'");
							p2.println("height='80'");
							p2.println("forecolor='#000000'");
							p2.println("backcolor='#FFFFFF'");
							p2.println("key='textField-176'");
							p2.println("stretchType='NoStretch'");
							p2.println("positionType='Float'");
							p2.println("isPrintRepeatedValues='true'");
							p2.println("isRemoveLineWhenBlank='false'");
							p2.println("isPrintInFirstWholeBand='false'");
							p2.println("isPrintWhenDetailOverflows='false'/>");
							p2.println("<textElement textAlignment='Right' verticalAlignment='Middle' rotation='None' lineSpacing='Single'>");
							p2.println("<font fontName='Arial' pdfFontName='Helvetica' size='8' isBold='true' isItalic='false' isUnderline='false' isPdfEmbedded ='false' pdfEncoding ='Cp1252' isStrikeThrough='false' />");
							p2.println("</textElement>");
							p2.println("<textFieldExpression class='java.lang.String'><![CDATA[$F{"+ctextkey+"}]]></textFieldExpression>");
							p2.println("</textField>");

						x +=60;
						c++;
					}

			yr=2000;
//			x+=60;
			x1+=x;
			temp=cols;

				yrwidth=(measureCount*60);
				//////System.out.println("****"+chek(labels));
				
				if( undis ){
					yrwidth=(measureCount-1)*60;
				}
				
				if(!reportType.equals("csv")){
				p2.println("<line direction='TopDown'>");
				p2.println("<reportElement");
				p2.println("mode='Opaque'");
				p2.println("x='0'");
				p2.println("y='82'");
				p2.println("width='"+lwidth+"'");
				p2.println("height='0'");
				p2.println("forecolor='#000000'");
				p2.println("backcolor='#FFFFFF'");
				p2.println("key='RecordEndLine'");
				p2.println("stretchType='NoStretch'");
				p2.println("positionType='FixRelativeToTop'");
				p2.println("isPrintRepeatedValues='true'");
				p2.println("isRemoveLineWhenBlank='false'");
				p2.println("isPrintInFirstWholeBand='false'");
				p2.println("isPrintWhenDetailOverflows='false'/>");
				p2.println("<graphicElement stretchType='NoStretch' pen='Thin' fill='Solid' />");
				p2.println("</line>");
				}

				
			p2.println("</band>");
			//////System.out.println("Detail..........Header" + c);
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
			p2.println("<band height='50'  isSplitAllowed='true' >");

				


			p2.println("</band>");
			p2.println("</summary>");
			p2.println("</jasperReport>");


			p2.close();
			}
						catch (Exception e)
						{
							//System.err.println("File error:::"+e);
							e.printStackTrace(System.out);
						}
			}//end foo

			/*public static boolean chekCol(String str)
			{
				int temp=0;
				boolean b=false;
				for(int i=0;i<cols.length;i++)
				{
//					////System.out.println("Temp---"+temp);
					if(cols[i].equals(str))
					{
//						////System.out.println("aaya..");
						b = true;
						break;
					}
					else
						b = false;
				}	
				return b;
			}
			*/

			public static boolean chek(String[] arr){
				boolean flag=false;
			/*	for(int i=0;i<arr.length;i++){
					if(arr[i].equals("Comulative"))
					{
						////System.out.println("Comulative found... :::"+i);
						flag=true;
					}
//					////System.out.println("chek.."+arr[i]);
				}
			*/	
				return flag;
			}


/*	public static void main(String args[])
	{
		////System.out.println("inside main...");
		int i=2;
//		String arr[]={"donorName","status","region","startDate"};
		String arr2[]={
						"Status",
						"Donor",
						"Actual Start Date",
						"Activity Name",
						"Type Of Assistance",
						"Sector","Level",
						"Actual Completion Date",
						"Region","Funding Instrument",
						"Objective",
						"Project Id",
						"Contact Name",
						"Description"
					};
					
		String arr[][]={{
						"Status",
						"Donor",
						"region",
						"hmmm",
						
						"2000",
						"Planned Comm",
						"Comulative",
						"m2",
						
						"2001",
						"Planned Comm",
						"Comulative",
						"m2",
						
						"2002",
						"Planned Comm",
						"Comulative",
						"m2",
						
						"Total",
						"Planned Comm",
						"Comulative",
						"m2"

					}};
		String labels[]={"Status","Donor","region","hmmm",

						"2000",
						"Planned Comm",
//						"Comulative",
						"m2",
						
						"2001",
						"Planned Comm",
//						"Comulative",
						"m2",
						
						"2002",
						"Planned Comm",
//						"Comulative",
						"m2",
						
						"Total",
						"Planned Comm",
//						"Comulative",
						"m2"

					};
					
		//arr={"col1","col2","col3","col4"};
		
	//		createJRXML(String filePath, boolean undis, String labels[],Object array[][], int cols, int measureCount, String reportName, String reportType)
		createJRXML("c:\\jrxml.jrxml",false,labels,arr, 4, 2,"test","pdf", true);

	}*/

}