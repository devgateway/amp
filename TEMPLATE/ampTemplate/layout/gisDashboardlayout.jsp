<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

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
		<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	    <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
		<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
        <digi:ref href="css/gis.css" type="text/css" rel="stylesheet" />
	</HEAD>
	
    <BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=0 valign="top" align="left">
			<TBODY>
			<TR>
				<TD width="100%" vAlign="top" align="left">



<jsp:include page="/TEMPLATE/ampTemplate/layout/gisReportToolbar.jsp" />
<jsp:include page="/TEMPLATE/ampTemplate/layout/gisScripts.jsp" />

<h2 style="padding-left:10px;font-size:15pt;"><digi:trn key="gis:resultsmatrix">Results Matrix: </digi:trn><%=countryName%></h2>

					<TABLE bgColor=#ffffff cellPadding=0 cellSpacing=0 width="99%" vAlign="top" align="left" border=0>
						<TR>
							<TD  width="10">&nbsp;</td>
							<TD align=center vAlign=top>
								<TABLE width="100%" cellPadding="5" cellSpacing="0" vAlign="top" align="left" border="0">
									<TR>
										<TD vAlign="top" align="left" width="60%" >
											<digi:insert attribute="body" />
										</TD>
										<td valign="top">
											<digi:insert attribute="pieChart"/>											
										</td>										
									</TR>
									<tr>
										<td valign="top">
                                        <div id="content" class="yui-skin-sam" style="width:100%;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                                <li class="selected">
                                                    <div class="nohover">
                                                        <a style="cursor:default">
                                                            <div>
                                                                <digi:trn key="gis:millenniumdevelopmentgoals">Millennium Development Goals</digi:trn>
                                                            </div>
                                                        </a>
                                                    </div>
                                                </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">                                        
											<table cellpadding="5" border="0" width="100%">
												<tr>
													<td>
														<digi:insert attribute="widget1">
															<digi:put name="widget-teaser-param">chart_place1</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget2">
															<digi:put name="widget-teaser-param">chart_place2</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget3">
															<digi:put name="widget-teaser-param">chart_place3</digi:put>
														</digi:insert>
													
													</td>
												</tr>
												<tr>
													<td>
														<digi:insert attribute="widget4">
															<digi:put name="widget-teaser-param">chart_place4</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget5">
															<digi:put name="widget-teaser-param">chart_place5</digi:put>
														</digi:insert>
													
													</td>
													<td>
														<digi:insert attribute="widget6">
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
                                            </div>
                                          </div>
                                        </div> 
										</td>
										<td valign="top">
                                        <div id="content" class="yui-skin-sam" style="width:100%;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                              <li class="selected">
                                               <div class="nohover">
                                                <a style="cursor:default">
                                                <div>
                                                  <digi:trn key="gis:resourcesatglance">Resources at a glance</digi:trn>
                                                </div>
                                                </a>
                                            </div>
                                              </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">                                        
												<table cellpadding="5">
												<tr valign="top">
													<td>
														<digi:insert attribute="widget7">
															<digi:put name="widget-teaser-param">atGlanceTable_Place1</digi:put>
														</digi:insert>
													</td>
													<td rowspan="2">
														<digi:insert attribute="widget8">
															<digi:put name="widget-teaser-param">atGlanceTable_Place3</digi:put>
														</digi:insert>
													</td>
												</tr>
												<tr>
													<td>
														<digi:insert attribute="widget9">
															<digi:put name="widget-teaser-param">atGalnceTable_Place2</digi:put>
														</digi:insert>
													</td>
												</tr>
                                                                                                <tr>
													<td>
													  <digi:trn key="widget:SourceOECD">Source: OECD </digi:trn>
													</td>
												</tr>
											</table>

                                            </div>
                                          </div>
                                        </div> 
										</td>
									</tr>
									<tr>
										<td colspan="2">
                                        <div id="content" class="yui-skin-sam" style="width:1000px;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                              <li class="selected">
                                                  <div class="nohover">
                                                      <a  style="cursor:default">
                                                          <div>
                                                              <digi:trn key="gis:aideffectivenessindicators">Aid Effectiveness Process Indicators</digi:trn>
                                                          </div>
                                                      </a>
                                                  </div>
                                              </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                        
                                            <table>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget10">
                                                            <digi:put name="widget-teaser-param">table_place1</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget11">
                                                            <digi:put name="widget-teaser-param">table_place2</digi:put>
                                                        </digi:insert>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign="top">
                                                    <br><br>
                                                        <digi:insert attribute="widget12">
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
                                            
                                            </div>
                                            </div>
                                            </div>
                                        </td>
                                    </tr>
									<tr>
										<td colspan="2" valign="top">
                                        <div id="content" class="yui-skin-sam" style="width:1000px;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                                <li class="selected">
                                                    <div class="nohover">
                                                        <a  style="cursor:default">
                                                            <div>
                                                                <digi:trn key="gis:outputindicators">Output Indicators</digi:trn>
                                                            </div>
                                                        </a>
                                                    </div>
                                                </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                            <table>
                                                <tr>
                                                    <td>
                                                    <digi:insert attribute="widget13">
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
                                            </div>
                                            </div>
                                            </div>
    									</td>
									</tr>
									<tr>
										<td colspan="2">
                                        <div id="content" class="yui-skin-sam" style="width:1000px;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                              <li class="selected">
                                                  <div class="nohover">
                                                      <a  style="cursor:default">
                                                          <div>
                                                              <digi:trn key="gis:totalresources">Total resources</digi:trn>
                                                          </div>
                                                      </a>
                                                  </div>
                                              </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                            <table>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget15">
                                                            <digi:put name="widget-teaser-param">table_place6</digi:put>
                                                        </digi:insert>
                                                        <br>
                                                        <digi:trn key="widget:sourceministryoffinance">Source: Ministry of Finance</digi:trn><br>
                                                    </td>
                                                </tr>
                                            </table>
                                            </div>
                                            </div>
                                            </div>
    									</td>
									</tr>
									<tr>
										<td colspan="2">
                                        <div id="content" class="yui-skin-sam" style="width:1000px;">
                                          <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                            <ul class="yui-nav">
                                              <li class="selected">
                                                  <div class="nohover">
                                                      <a  style="cursor:default">
                                                          <div>
                                                              <digi:trn key="gis:externalaidresources">External Aid Resources</digi:trn>
                                                          </div>
                                                      </a>
                                                  </div>
                                              </li>
                                            </ul>
                                            <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                            <table>
                                                <tr>
                                                    <td valign="top">
                                                        <digi:insert attribute="widget14">
                                                            <digi:put name="widget-teaser-param">table_place5</digi:put>
                                                        </digi:insert>
                                                        <br>
                                                        <digi:trn key="widget:sourceAMPdatabase">Source: AMP database</digi:trn><br>
                                                    </td>
                                                </tr>
                                            </table>
                                            </div>
                                            </div>
                                            </div>
    									</td>
									</tr>
									<tr>
										<td colspan="2">
											<digi:insert attribute="widget16">
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

	</BODY>
</HTML>

