<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>

<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<style type="text/css">


#tabs {
	font-family: Arial,Helvetica,sans-serif;
	font-size: 8pt;
	clear: both;
	text-align: center;
}

#tabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#tabs li { 
	 float: left;
}



#tabs a, #tabs span { 
	font-size: 8pt;
}

#tabs ul li a { 
	background:#222E5D url(/TEMPLATE/ampTemplate/images/tableftcorner.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcorner.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs ul li span a { 
	background:#3754A1 url(/TEMPLATE/ampTemplate/images/tableftcornerunsel.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li span a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcornerunsel.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs a:hover {
    background: #455786 url(/TEMPLATE/ampTemplate/images/tableftcornerhover.gif) left top no-repeat;  
}

#tabs a:hover span {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}
#tabs a:hover div {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}

#tabs a.active {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 4px 10px 4px 10px;
	text-decoration: none;
	color: #333;
}

#tabs a.active:hover {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 6px 10px 6px 10px;
	text-decoration: none;
	color: #333;
}


#subtabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#subtabs li {
	float: left;
	padding: 0px 4px 0px 4px;
}

#subtabs a, #subtabs span { 
	font-size: 8pt; 
}

#subtabs a {
}

#subtabs ul li span {
	text-decoration: none;
}

#subtabs ul li div span {
	text-decoration: none;
}

#subtabs {
	text-align: center;
	font-family:Arial,Helvetica,sans-serif;
	font-size: 8pt;
	padding: 2px 4px 2px 4px;
	background-color:#CCDBFF;
}

#main {
	clear:both;
	text-align: left;
	border-top: 2px solid #222E5D;
	border-left: 1px solid #666;
	border-right: 1px solid #666;
	padding: 2px 4px 2px 4px;
}
html>body #main {
	width:742px;
}

#mainEmpty {
	border-top: 2px solid #222E5D;
	width: 750px;
	clear:both;
}
html>body #mainEmpty {
	clear:both;
	width:752px;
}

</style>

<digi:instance property="aimParisIndicatorReportForm" />



<jsp:include page="teamPagesHeader.jsp" flush="true" />

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>

	<tr>

		<td class=r-dotted-lg width=14>&nbsp;</td>



		<td align=left class=r-dotted-lg vAlign=top width=750>



			<table cellPadding=5 cellSpacing=0 width="100%">

				<tr>

				<td valign="bottom" class="crumb" >

<c:set var="translation">

	<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>

</c:set>

                <digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >

                	<digi:trn key="aim:portofolio">Portfolio</digi:trn>

                </digi:link> &gt; <digi:trn key="aim:allParisReportsList">Paris Indicator Reports List</digi:trn>

                </td></tr>

				<tr>

		<td>&nbsp;</td></tr>

				<tr>

					<td height=16 align="center" vAlign=center><span class=subtitle-blue>

						<digi:trn key="aim:parisIndcReports">Paris Indicator Reports</digi:trn>

						</span>

					</td>

				</tr>

				<tr>

					<td noWrap width=650 vAlign="top">

						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">

							<tr bgColor=#f4f4f2>

								<td>&nbsp;

								</td>

							</tr>

							<tr bgColor=#f4f4f2>

								<td valign="top">

									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">

										<tr>

											<td bgColor=#ffffff class=box-border>

												<table border=0 cellPadding=3 cellSpacing=5 class=box-border width="100%" >

													<tr bgColor=#dddddb>

														<td bgColor=#dddddb align="center" height="20">

															<b>

															<digi:trn key="aim:parisIndcReportsList">

																List of Paris Indicator reports

															</digi:trn></b>

														</td>

													</tr>
										
										<logic:notEmpty name="aimParisIndicatorReportForm"  property="indicatorsColl">
											<div style="padding-left:5px;width:900px;">
											<DIV id="tabs">
											<UL>
											<logic:iterate id="report" name="aimParisIndicatorReportForm"  property="indicatorsColl" type="org.digijava.module.aim.dbentity.AmpAhsurveyIndicator">
												<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
												<c:if test="${report.indicatorCode != '10b'}">
                                                    <feature:display  name="PI report ${report.indicatorCode}" module="PI Reports">
														<c:set target="${urlParams}" property="indcId" value="${report.ampIndicatorId}" />
														<c:set target="${urlParams}" property="reset" value="true" />
														<LI>
						                               		<a name="node"><div>
																<digi:trn key="aim:parisIndicator:${report.indicatorCode}"><c:out value="Paris Indicator ${report.indicatorCode}"/></digi:trn>
						                                    </div></a>
														</LI>
														<LI><span>
															<digi:link href="/parisIndicatorReport.do" name="urlParams" styleClass="h-box" title="${translation}" target="_self">
															<div>
																<digi:trn key="aim:parisIndicator:${report.indicatorCode}"><c:out value="Paris Indicator ${report.indicatorCode}"/></digi:trn>
							                                </div>
															</digi:link>
							                            </span></LI>
                                                    </feature:display>
												</c:if>
											</logic:iterate>
											</UL>
											</DIV>
											</div>
										</logic:notEmpty>

										<logic:empty name="aimParisIndicatorReportForm"  property="indicatorsColl">

											<TR><TD>

												<b><font color="red">

													<digi:trn key="aim:noParisIndcRecordFound">No Aid Effectiveness Indicator found</digi:trn>

												</font></b>

											</TR></TD>

										</logic:empty>

										

												</table>

											</td>

										</tr>

									</table>

								</td>

							</tr>

							<tr><td bgColor=#f4f4f2>

								&nbsp;

							</td></tr>

						</table>

					</td>

				</tr>

			</table>

		</td>

	</tr>

</table>




