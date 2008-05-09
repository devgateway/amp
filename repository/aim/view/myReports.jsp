<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<SCRIPT TYPE="text/javascript">
<!--
function popup(mylink, windowname)
{
if (! window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
return false;
}
//-->
</SCRIPT>




<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">

	<TR>

		<TD class=r-dotted-lg-buttom vAlign=top>

			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >

        		<TR><TD>

              	<TABLE border=0 cellPadding=0 cellSpacing=0 >

              		<TR bgColor=#f4f4f2>

                 		<TD bgColor=#c9c9c7 class=box-title

							title='<digi:trn key="aim:PortfolioOfReports">Portfolio Reports </digi:trn>'>

								<digi:trn key="aim:portfolioReports">Reports</digi:trn>

							</TD>

                    	<TD background="module/aim/images/corner-r.gif"

							height=17 width=17></TD>

						</TR>

					</TABLE>

				</TD></TR>

				<% int rCount = 0; %>

				<c:set var="translation">

					<digi:trn key="aim:clickToViewReport">Click here to view Report</digi:trn>

				</c:set>
				<logic:empty name="myReports" scope="session">

					<TR><TD bgColor=#ffffff class=box-border align=left>

						<digi:trn key="aim:noPublicReports">No public reports</digi:trn>

					</TD></TR>

				</logic:empty>
				<logic:notEmpty name="myReports" scope="session">

				<TR><TD bgColor=#ffffff class=box-border align=left>

					<TABLE border=0 cellPadding=1 cellSpacing=1 width="100%" >

					<logic:iterate name="myReports" id="report" scope="session"

					type="org.digijava.module.aim.dbentity.AmpReports" length="5">



						<TR><TD title='${translation}'>

							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>

							<digi:link href="/viewNewAdvancedReport.do?view=reset&widget=false" paramName="report"  paramId="ampReportId" paramProperty="ampReportId" onclick="return popup(this,'');">
								<bean:write name="report" property="name"/>
							</digi:link>

						</TD></TR>



					</logic:iterate>

					<bean:size id="repCount" name="myReports" scope="session" />

						<TR><TD title='<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>'>

							<digi:link href="/viewTeamReports.do">

								<digi:trn key="aim:more">..more</digi:trn>

							</digi:link>

						</TD></TR>

					</TABLE>

				</TD></TR>

				</logic:notEmpty>
				<logic:present name="currentMember" scope="session">

				<logic:empty name="myReports" scope="session">

					<TR><TD bgColor=#ffffff class=box-border align=left>

						<digi:trn key="aim:noReportForThisWorkspace">No reports for this workspace</digi:trn>

					</TD></TR>

				</logic:empty>

	        		<TR><TD title='<digi:trn key="aim:createReport">Create Report</digi:trn>'>

						<digi:link href="/advancedReportManager.do?clear=true&reportEdit=false">

							<b><digi:trn key="aim:reportGenerator">Report Generator</digi:trn></b>

						</digi:link>

					</TD></TR>

	        		<TR><TD>

						<br>

					</TD></TR>



				<!-- Paris Indicators Reports Starts Here -->

				<%-- <c:if var="teamType" test="${currentMember.teamType == 'DONOR'}" scope="session"> --%>


                <feature:display  name="Paris Indicators Reports" module="Portfolio">
                        <TR><TD title='<digi:trn key="aim:clickToViewParisIndcReports">Click here to view Paris Indicator Reports</digi:trn>'>
                            <digi:link href="/parisIndicatorReport.do">
                                <b><digi:trn key="aim:parisIndicatorReports">Paris Indicator Reports</digi:trn></b>
                            </digi:link>
						</TD></TR>
                </feature:display>


				<%-- </c:if>  --%>

				<!-- Paris Indicators Reports ends Here -->

				</logic:present>

			</TABLE>

		</TD>

	</TR>

</TABLE>






