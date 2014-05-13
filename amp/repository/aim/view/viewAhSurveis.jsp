<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<div style="margin:0 auto;width:1000px;">
<h1 class="admintitle">Paris Indicator reports</h1>
<digi:instance property="aimViewAhSurveisForm" />

<jsp:include page="teamPagesHeader.jsp"  />


<table cellPadding=5 cellspacing="0" width="100%" border="0" style="font-size:12px;">
<tr>
<td>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000>
  <tr>
    <td align=left class=r-dotted-lg valign="top" width=1000>
      <table cellPadding=5 cellspacing="0" width="100%">
        <!--<tr>
          <td valign="bottom" class="crumb" >
            <c:set var="translation">
              <digi:trn key="aim:clickToViewMyDesktop">Click here to view admin home page</digi:trn>
            </c:set>
            <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
              <digi:trn key="aim:adminHomeLink">Admin home</digi:trn>
            </digi:link>
            &gt;
            <digi:trn key="aim:allParisReportsList">Paris Indicator Reports List</digi:trn>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>-->
        <tr>
          <td height=16 align="center" vAlign="middle">
            <span class=subtitle-blue>
              <digi:trn key="aim:parisIndcReports"><b>Paris Indicator Reports</b></digi:trn>
            </span>
          </td>
        </tr>
        <tr>
          <td noWrap width=650 vAlign="top">
            <table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
              <tr bgColor=#f4f4f2>
                <td>&nbsp;</td>
              </tr>
              <tr bgColor=#f4f4f2>
                <td valign="top">
                  <table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%">
                    <tr>
                      <td bgColor=#ffffff class=box-border>
                        <table border="0" cellPadding=3 cellSpacing=5 class=box-border width="100%" >
                       
                          <c:if test="${empty aimViewAhSurveisForm.surveis}">
                            <tr>
                              <td>
                                <b>
                                  <font color="red">
                                    <digi:trn key="aim:noParisIndcRecordFound">No Aid Effectiveness Indicator found</digi:trn>
                                  </font>
                                </b>
                              </td>
                            </tr>
                          </c:if>
                          <c:if test="${!empty aimViewAhSurveisForm.surveis}">
                            <c:forEach var="report" items="${aimViewAhSurveisForm.surveis}">
                              <c:if test="${report.indicatorCode != '4' && report.indicatorCode != '5aii' && report.indicatorCode != '5bii' && report.indicatorCode != '9'&& report.indicatorCode != '10a' && report.indicatorCode != '10b'}">
                                <tr>
                                  <td>
                                    <IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width="10" />
                                    <strong>[${report.indicatorCode}]</strong>
                                    <c:set var="translation">
                                      <digi:trn key="aim:clickToViewReport">Click here view Report</digi:trn>
                                    </c:set>
                                    <a href="/aim/viewAhSurveyFormulas.do?indId=${report.ampIndicatorId}">
                                      <digi:trn key="aim:${report.nameTrn}">
                                      ${report.name}
                                      </digi:trn>
                                    </a>
                                  </td>
                                </tr>
                              </c:if>
                            </c:forEach>
                          </c:if>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td bgColor=#f4f4f2>&nbsp;
                
                </td>
              </tr>
            </table>
</td>
        </tr>
      </table>
</td>
  </tr>
</table>


</td>


<!-- Other Links -->

 <td width=300 valign=top>
										<table align="center" cellpadding="0" cellspacing="0"
											width="300" border="0">
											<tr>
												<td>
													<!-- Other Links -->
													<table cellpadding="0" cellspacing="0" width="120">
														<tr>
															<td bgColor=#c9c9c7 class=box-title>
																<b style="font-weight: bold; font-size: 12px; padding-left:5px; color:#000000;">
																	<digi:trn>Other links</digi:trn>
																</b>
															</td>
															<td background="module/aim/images/corner-r.gif"
																height="17" width="17">&nbsp;</td>
														</tr>
													</table></td>
											</tr>
											<tr>
												<td bgColor=#ffffff>
													<table cellPadding=0 cellspacing="0" width="100%" class="inside">
														
														<tr>
															<td class="inside"><digi:img
																	src="module/aim/images/arrow-014E86.gif" width="15"
																	height="10" /> <digi:link module="aim" href="/admin.do">
																	<digi:trn key="aim:AmpAdminHome">
																Admin Home
																</digi:trn>
																</digi:link></td>
														</tr>
														
														<!-- end of other links -->
													</table></td>
											</tr>
										</table></td>


</tr>
</table>


</div>


