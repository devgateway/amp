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

										<logic:empty name="aimParisIndicatorReportForm"  property="indicatorsColl">

											<TR><TD>

												<b><font color="red">

													<digi:trn key="aim:noParisIndcRecordFound">No Aid Effectiveness Indicator found</digi:trn>

												</font></b>

											</TR></TD>

										</logic:empty>

										<logic:notEmpty name="aimParisIndicatorReportForm"  property="indicatorsColl">

											<logic:iterate id="report" name="aimParisIndicatorReportForm"  property="indicatorsColl"

													   type="org.digijava.module.aim.dbentity.AmpAhsurveyIndicator">

											<c:if test="${report.indicatorCode != '10b'}">

												<TR><TD>
                                                    <feature:display  name="PI report ${report.indicatorCode}" module="PI Reports">

													<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>

													<strong>[${report.indicatorCode}]</strong>

													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

													<c:set target="${urlParams}" property="indcId" value="${report.ampIndicatorId}" />

													<c:set target="${urlParams}" property="reset" value="true" />

													<c:set var="translation">

														<digi:trn key="aim:clickToViewReport">Click here view Report</digi:trn>

													</c:set>

													<digi:link href="/parisIndicatorReport.do" name="urlParams" styleClass="h-box" title="${translation}" >
														<digi:trn key="aim:${report.nameTrn}">
														<c:out value="${report.name}"/>
														</digi:trn>

													</digi:link>
                                                    </feature:display>
												</TD></TR>

											</c:if>

										</logic:iterate>

										</logic:notEmpty>

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




