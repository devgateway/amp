<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>


<script type="text/javascript">
<!--
	var donorWidget=false;
	var lastTimeStamp;
	
	function donorChanged(){
		rechart();
	}
	function yearChanged(){
		var selectedFromYearNumber = parseInt($("select[name='selectedFromYear']").val());
		var selectedToYearNumber = parseInt($("select[name='selectedToYear']").val());

		if (selectedFromYearNumber > selectedToYearNumber) {
			alert ("<digi:trn>Start year can not be greater then End year</digi:trn>");
			$("select[name='selectedFromYear']").val(selectedToYearNumber);
		}

		
		rechart();
		 if(donorWidget){
             getTopTenDonorTable();
        }
	}
	function rechart(){
            lastTimeStamp = new Date().getTime();
            var sectorByDonorChartImageDiv=document.getElementById('sectorByDonorChartImageDiv');
            var sectorByDonorChartImageLoadDiv=document.getElementById('sectorByDonorChartImageDivLoad');
            sectorByDonorChartImageLoadDiv.style.display="block";
            sectorByDonorChartImageDiv.style.display="none";
            var chartImageMap=document.getElementById('sectorByDonorChartImageMap');
            removeAllChildren(chartImageMap);
            var chartImage=document.getElementById('sectorByDonorChartImage');
            //from year
            var fy=document.getElementsByName('selectedFromYear')[0].value;
            //to year
            var ty=document.getElementsByName('selectedToYear')[0].value;
            //donors
            var d=document.getElementsByName('selectedDonor')[0].value;
            //currency
            var curr=$("#selectedCurrency").val();
            var myUrl = chartURL+'~selectedFromYear='+fy+'~selectedToYear='+ty+'~selectedDonor='+d+'~selectedCurrency='+curr+'~timestamp='+lastTimeStamp;
            myUrl+=getLegendState();
            myUrl+=getLabelState();
            chartImage.src=myUrl;
            removeAllChildren(sectorByDonorChartImageDiv);
            sectorByDonorChartImageDiv.appendChild(chartImage);
            //alert(myUrl);
        }

            function loadSectorDonorMap(){
                getSectorDonorGraphMap(lastTimeStamp);
            }

            function constructSectorDonorMapUrl(timestmp){
<digi:context name="donorSectorMap" property="context/widget/getDonorSectorMap.do" />
        var url="${donorSectorMap}";
        if (typeof lastTimeStamp != 'undefined') {
            url+='~timestamp='+timestmp;
        }
        return url;
    }

    function getSectorDonorGraphMap(timestamp){
        var url=constructSectorDonorMapUrl(timestamp);
        var async=new Asynchronous();
        async.complete=updateSectorDonorMapCallBack;
        async.call(url);
    }
    function removeAllChildren(parent){
        var childrenAreas=parent.childNodes;
        if(childrenAreas!=null){
            while (parent.firstChild) {
                parent.removeChild(parent.firstChild);
            }
        }
    }

    function updateSectorDonorMapCallBack(status, statusText, responseText, responseXML){
        var sectorByDonorChartImageLoadDiv=document.getElementById('sectorByDonorChartImageDivLoad');
        var sectorByDonorChartImageDiv=document.getElementById('sectorByDonorChartImageDiv');
        var chartImageMap=document.getElementById('sectorByDonorChartImageMap');
        removeAllChildren(chartImageMap);
        var areas=responseXML.getElementsByTagName('area');
        for(var i=0;i<areas.length;i++){
            var area=document.createElement('area');
            area.setAttribute("title", areas[i].getAttribute("title"));
            area.setAttribute("alt", areas[i].getAttribute("alt"));
            area.setAttribute("shape", areas[i].getAttribute("shape"));
            area.setAttribute("coords", areas[i].getAttribute("coords"));
            if(areas[i].getAttribute("href")!=null){
                area.setAttribute("href", "javascript:showSectorDonorWidgetReport('"+areas[i].getAttribute("href")+"')");
            }
            chartImageMap.appendChild(area);
        }
        sectorByDonorChartImageLoadDiv.style.display="none";
        sectorByDonorChartImageDiv.style.display="block";
    }


    function showSectorDonorWidgetReport(url) {
    		if ($("#selectedCurrency").val() != null) {
    			url += "&selectedCurrency=" + $("#selectedCurrency").val();
    		}
        var popup = window.open(url, "SectorDonorWidgetReport", "height=500,width=750,status=yes,resizable=yes,toolbar=no,menubar=no,location=no");
        popup.focus();
    }
	function getLegendState(){
		var chkLegend = document.getElementsByName('showLegends')[0];
		if (chkLegend.checked){
			return '~showLegend=true'
		}else{
			return '~showLegend=false'
		}
	}
	function getLabelState(){
		var chkLabel = document.getElementsByName('showLabels')[0];
		if (chkLabel.checked){
			return '~showLabel=true'
		}else{
			return '~showLabel=false'
		}
	}
	
//-->
</script>


<style>
	#content{
		height: 100%;
	}
	#demo{
		height: 100%;
	}
	#div1{
		height: 96%;
	}
</style>

<%
String countryName = "";
String ISO = null;
java.util.Iterator itr1 = org.digijava.module.aim.util.FeaturesUtil.getDefaultCountryISO().iterator();
while (itr1.hasNext()) {
	org.digijava.module.aim.dbentity.AmpGlobalSettings ampG = (org.digijava.module.aim.dbentity.AmpGlobalSettings) itr1.next();
	ISO = ampG.getGlobalSettingsValue();
}

if(ISO != null && !ISO.equals("")){
	org.digijava.kernel.dbentity.Country cntry = org.digijava.module.aim.util.DbUtil.getDgCountry(ISO);
	countryName = " " + cntry.getCountryName();
}
else {
	countryName = "";
}
%>


<HTML>
	<digi:base />
	<digi:context name="digiContext" property="context"/>
	<HEAD>
		<TITLE><digi:trn key="gis:resultsmatrix">Results Matrix: </digi:trn><%=countryName%></TITLE>
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">		
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
     	<META HTTP-EQUIV="EXPIRES" CONTENT="0">		
     	<!--
		<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	    <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
        -->
        <digi:ref href="css_2/amp.css" type="text/css" rel="stylesheet" />
		<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
        <digi:ref href="css/gis.css" type="text/css" rel="stylesheet" />
	</HEAD>
	
    <BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">    	
    
 <jsp:include page="/TEMPLATE/ampTemplate/layout/gisScripts.jsp" />
 	   
<div style="width:100%;" align="center">
    <table>
    	<tr>
    		<td class="main_side_1">
    				
		<TABLE cellspacing="0" cellpadding="0" width="100%" height="100%" border="0" valign="top" align="left" style="font-size: 12px;">
			<TBODY>
                
				<TD width="800" vAlign="top" align="left">








						<br>
						&nbsp;<b class="bread_sel"><digi:trn key="gis:resultsmatrix">Results Matrix: </digi:trn><%=countryName%></b>

					<TABLE cellpadding="0" cellspacing="0" width="99%" vAlign="top" align="left" border="0">
						<tr>
							<td>&nbsp;</td>
							<td colspan="2" align="center">
								<jsp:include page="/TEMPLATE/ampTemplate/layout/gisReportToolbar.jsp" />
							</td>
						</tr>
							
						<TR>
							<TD  width="10">&nbsp;</td>
							<TD align="center" valign="top">
								<TABLE width="100%" cellPadding="0" cellSpacing="5" vAlign="top" align="left" border="0" style="border-collapse:collapse">
									<tr>
										<TD vAlign="top" align="left" height="100%" width="527" >
											<digi:insert attribute="body" />
										</TD>
										<td valign="top">
											<table width="100%" border="0" height="90%" cellPadding="0" cellSpacing="0"><tr><td valign="top">
												<digi:insert attribute="pieChart"/>
											</td></tr><tr><td valign="top">
												<feature:display name="Widgets" module="GIS DASHBOARD">
												<field:display name="Resources at a glance" feature="Widgets">
														<fieldset>
															<legend>
																<span class="legend_label">
																	<digi:trn key="gis:resourcesatglance">Resources at a glance</digi:trn>
																</span>
															</legend>
															<table cellpadding="5" cellspacing="0">
																	<tr valign="top">
																		<td>
																			
																				<field:display name="Widget7" feature="Widgets">
																					<digi:insert attribute="widget7" flush="false">
																						<digi:put name="widget-teaser-param">atGlanceTable_Place1</digi:put>
																					</digi:insert>
																				</field:display>
																		</td>
																		<td rowspan="2">
																				<field:display name="Widget8" feature="Widgets">
																					<digi:insert attribute="widget8" flush="false">
																						<digi:put name="widget-teaser-param">atGlanceTable_Place3</digi:put>
																					</digi:insert>
																				</field:display>
																		</td>
																	</tr>
																	<tr>
																		<td>
																				<field:display name="Widget8" feature="Widgets">
																					<digi:insert attribute="widget9" flush="false">
																						<digi:put name="widget-teaser-param">atGalnceTable_Place2</digi:put>
																					</digi:insert>
																				</field:display>
																		</td>
																	</tr>
					                                                <tr>
																		<td>
																		  <digi:trn key="widget:SourceOECD">Source: OECD </digi:trn>
																		</td>
																	</tr>
																</table>	
															</fieldset>
	                        </field:display>
												</feature:display>
											</td></tr><tr><td valign="top">
												<feature:display name="Widgets" module="GIS DASHBOARD">
												<field:display name="Millennium Development Goals" feature="Widgets">
													<fieldset>
                      			<legend>
															<span class="legend_label">
																<digi:trn key="gis:millenniumdevelopmentgoals">Millennium Development Goals</digi:trn>
															</span>
														</legend>
                                                  
                        
														<table cellpadding="5" border="0" width="100%">
															<tr>
																<td>
																	<digi:insert attribute="widget1" flush="false">
																		<digi:put name="widget-teaser-param">chart_place1</digi:put>
																	</digi:insert>
																</td>
																<td>
																	<digi:insert attribute="widget2" flush="false">
																		<digi:put name="widget-teaser-param">chart_place2</digi:put>
																	</digi:insert>
																</td>
																<td>
																	<digi:insert attribute="widget3" flush="false">
																		<digi:put name="widget-teaser-param">chart_place3</digi:put>
																	</digi:insert>
																
																</td>
															</tr>
															<tr>
																<td>
																	<digi:insert attribute="widget4" flush="false">
																		<digi:put name="widget-teaser-param">chart_place4</digi:put>
																	</digi:insert>
																
																</td>
																<td>
																	<digi:insert attribute="widget5" flush="false">
																		<digi:put name="widget-teaser-param">chart_place5</digi:put>
																	</digi:insert>
																
																</td>
																<td>
																	<digi:insert attribute="widget6" flush="false">
																		<digi:put name="widget-teaser-param">chart_place6</digi:put>
																	</digi:insert>
																</td>
															</tr>
															<tr>
																<td colspan="2" width="100%">
																	<digi:trn key="gis:sixChartLegend:desctiption">&nbsp;</digi:trn>
																</td>
																<td align="right">
																	<digi:img src="images/legend1.jpg"/>
																</td>
															</tr>
                              <tr>
                              	<td colspan="3">
                                  	<digi:trn key="widget:SourceOfficialgovernmentsources">Source: Official government sources </digi:trn>
                                  </td>
                              </tr>
												</table>
	                    </fieldset>
										</td>
										
										</field:display>
										</feature:display>
											
											</td></tr></table>
										</td>
									</tr>
	
									<tr>
										<td colspan="2">
											<feature:display name="Widgets" module="GIS DASHBOARD">
												<field:display name="Aid Effectiveness Process Indicators" feature="Widgets">
                        		<fieldset>          
                              <legend>
																<span class="legend_label">
																	<digi:trn key="gis:aideffectivenessindicators">Aid Effectiveness Process Indicators</digi:trn>
																</span>
															</legend>
                                                  
                                        
                                            <table>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget10" flush="false">
                                                            <digi:put name="widget-teaser-param">table_place1</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget11" flush="false">
                                                            <digi:put name="widget-teaser-param">table_place2</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign="top">
                                                    <br><br>
                                                        <digi:insert attribute="widget12" flush="false">
                                                            <digi:put name="widget-teaser-param">table_place3</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                    <td>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <digi:trn key="widget:SourceParisDeclaration">Source: 2006 Paris Declaration Survey</digi:trn>
                                                    </td>
                                                </tr>
                                            </table>
                                            
                                          </fieldset>
                                            </field:display>
																						</feature:display>
                                        </td>
                                        
                                    </tr>
									<tr>
										<td colspan="2" valign="top">
											<feature:display name="Widgets" module="GIS DASHBOARD">
												<field:display name="Output Indicators" feature="Widgets">
                                        <fieldset>          
                              <legend>
																<span class="legend_label">
																	<digi:trn key="gis:outputindicators">Output Indicators</digi:trn>
																</span>
															</legend>
                                                  
                                            <table>
                                                <tr>
                                                    <td>
                                                    <digi:insert attribute="widget13" flush="false">
                                                        <digi:put name="widget-teaser-param">table_place4</digi:put>
                                                    </digi:insert>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <digi:trn key="widget:SourceOfficialgovernmentsources">Source: Official government sources </digi:trn>
                                                    </td>
                                                </tr>
                                            </table>
                                          </fieldset>
                                            </field:display>
												</feature:display>
    									</td>
									</tr>
									<tr>
										<td colspan="2">
											<feature:display name="Widgets" module="GIS DASHBOARD">
												<field:display name="Total resources" feature="Widgets">
														<fieldset>          
                              <legend>
																<span class="legend_label">
																	<digi:trn key="gis:totalresources">Total resources</digi:trn>
																</span>
															</legend>
                                                  
                                            <table>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget15" flush="false">
                                                            <digi:put name="widget-teaser-param">table_place6</digi:put>
                                                        </digi:insert>
                                                        <br>
                                                        <digi:trn key="widget:sourceministryoffinance">Source: Ministry of Finance</digi:trn><br>
                                                    </td>
                                                </tr>
                                            </table>
                                          </fieldset>
												</field:display>
												</feature:display>
    									</td>
									</tr>
									<tr>
										<td colspan="2">
											<feature:display name="Widgets" module="GIS DASHBOARD">
												<field:display name="External Aid Resources" feature="Widgets">
                            <fieldset>          
                              <legend>
																<span class="legend_label">
																	<digi:trn key="gis:externalaidresources">External Aid Resources</digi:trn>
																</span>
															</legend>
                                                  
                                            <table>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget14" flush="false">
                                                            <digi:put name="widget-teaser-param">table_place5</digi:put>
                                                        </digi:insert>
                                                        <br>
                                                        <digi:trn key="widget:sourceAMPdatabase">Source: AMP database</digi:trn><br>
                                                    </td>
                                                </tr>
                                            </table>
                           </fieldset>
												</field:display>
												</feature:display>
    									</td>
									</tr>
									<tr>
										<td colspan="2">
											<digi:insert attribute="widget16" flush="false">
											    <digi:put name="widget-teaser-param">additional_table</digi:put>
											</digi:insert>
										</td>
									</tr>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
					
					
					
					
				</TD>
			</TR>
			</TBODY>
		</TABLE>
			

		</tr>
	</td>
</table>

</div>

	</BODY>
</HTML>

