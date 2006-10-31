<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
				<bean:define id="translation">
					<digi:trn key="aim:clickToViewReport">Click here to view Report</digi:trn>
				</bean:define>				
				<logic:notEmpty name="myReports" scope="session">
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<TABLE border=0 cellPadding=1 cellSpacing=1 width="100%" >
					<logic:iterate name="myReports" id="report" scope="session" 
					type="org.digijava.module.aim.dbentity.AmpReports"> 
						<% if (rCount < 5) { rCount ++; %>
						<TR><TD title='<%=translation%>'>
							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
							<bean:define name="report" id="desc" property="description" type="java.lang.String"/>
							<% desc=desc.replaceFirst("viewAdvancedReport","viewNewAdvancedReport"); %>
				<%		//	<digi:link href="<%=desc " onclick="return popup(this,'');"> %>
							<a href="<%=desc%>" styleClass="h-box" onclick="return popup(this,'');">
							<bean:write name="report" property="name"/><%//</digi:link>%>
						</TD></TR>
						<% } %>
					</logic:iterate>
					<bean:size id="repCount" name="myReports" scope="session" />
					<c:if test="${repCount > 5}">
						<TR><TD title='<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>'>
							<digi:link href="/viewTeamReports.do">
								<digi:trn key="aim:more">..more</digi:trn>
							</digi:link>							
						</TD></TR>
					</c:if>
					</TABLE>
				</TD></TR>
				</logic:notEmpty>
				<logic:empty name="myReports" scope="session">
					<TR><TD bgColor=#ffffff class=box-border align=left>
						No reports for this workspace
					</TD></TR>
				</logic:empty>
	        		<TR><TD title='<digi:trn key="aim:createAdvancedReport">Create Advanced Report </digi:trn>'>
						<digi:link href="/advancedReportManager.do?clear=true">
							<b><digi:trn key="aim:advancedReportManager">Advanced Report Manager</digi:trn></b>
						</digi:link>
					</TD></TR>
	        		<TR><TD>
						<br>
					</TD></TR>
			
				<!-- Paris Indicators Reports Starts Here -->
				<c:if var="teamType" test="${currentMember.teamType == 'DONOR'}" scope="session">
					<logic:notEmpty name="PI" scope="application">
		      			<TR><TD title='<digi:trn key="aim:clickToViewParisIndcReports">Click here to view Paris Indicator Reports</digi:trn>'>
							<digi:link href="/parisIndicatorReport.do">
								<b><digi:trn key="aim:parisIndicatorReports">Paris Indicator Reports</digi:trn></b>
							</digi:link>
						</TD></TR>
					</logic:notEmpty>
				</c:if>
				<!-- Paris Indicators Reports ends Here -->
				
			</TABLE>	
		</TD>
	</TR>
</TABLE>

