<%@ page pageEncoding="UTF-8" %>

<%@ page import = "org.digijava.module.aim.helper.ChartGenerator" %>

<%@ page import = "java.io.PrintWriter, java.util.*" %>



<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<digi:instance property="aimActivityDashboardForm" />


<script language="Javascript">

	function showPrinterFriendly(actId,chartType) {

		<digi:context name="ptUrl" property="context/module/moduleinstance/printableActivityChart.do" />

		var url = "<%=ptUrl%>?ampActivityId="+actId+"&cType="+chartType;

	 	openURLinWindow(url,650,500);

	}



	function actPerfWithoutBaseline(actId,chartType) {

		<digi:context name="ptUrl" property="context/module/moduleinstance/viewActWithoutBase.do" />

		var url = "<%=ptUrl%>?ampActivityId="+actId+"&cType="+chartType;

	 	openURLinWindow(url,650,500);

	}



function projectFiche(id)

{

	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />

	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");

}



	function fnEditProject(id)

{

	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />

   document.aimActivityForm.action = "<%=addUrl%>~pageId=1~action=edit~surveyFlag=true~activityId=" + id+"~step=10";

	document.aimActivityForm.target = "_self";

   document.aimActivityForm.submit();

}



function preview(id)

{

	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />

    document.aimActivityForm.action = "<%=addUrl%>~pageId=2~activityId=" + id;

	document.aimActivityForm.target = "_self";

    document.aimActivityForm.submit();

}



</script>





<%

	Long actId = (Long) request.getAttribute("actId");



	String url = "/aim/viewIndicatorValues.do?ampActivityId="+actId+"&tabIndex=7";



	String actPerfChartFileName = ChartGenerator.getActivityPerformanceChartFileName(

						 actId,session,new PrintWriter(out),370,450,url,true,request);



	String actPerfChartUrl = null;

	if (actPerfChartFileName != null) {

		actPerfChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actPerfChartFileName;

	}





	String actRiskChartFileName = ChartGenerator.getActivityRiskChartFileName(

						 actId,session,new PrintWriter(out),370,350,url,request);



	String actRiskChartUrl = null;



	if (actRiskChartFileName != null)  {

		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;

	}

%>

<digi:form action="/viewActivityDashboard.do" name="aimActivityForm" type="org.digijava.module.aim.form.aimActivityForm"

method="post">



<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR>

		<TD vAlign="top" align="center">

			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" class="box-border-nopadding">

				<TR><TD>

					<TABLE width="100%" cellspacing="2" cellpadding="2" valign="top" align="left" border=0>

						<TR><TD class="highlight" colspan="2">

						</TD></TR>

						<TR>

							<field:display name="Activity Performance"  feature="Activity Dashboard">
							<TD width="50%" align="center" class="textalb" height="20" bgcolor="#336699">

								<digi:trn key="aim:meActivityPerformance">

								Activity - Performance</digi:trn>

							</TD>
							</field:display>
							<field:display name="Project Risk"  feature="Activity Dashboard">

							<TD width="50%" align="center" class="textalb" height="20" bgcolor="#336699">

								<digi:trn key="aim:meActivityRisk">

								Activity - Risk</digi:trn>

							</TD>
							</field:display>

						</TR>

						<TR>
							<field:display name="Activity Performance"  feature="Activity Dashboard">
							
							<TD width="50%">

								<% if (actPerfChartUrl != null) { %>
                                                                

								<img src="<%= actPerfChartUrl %>" width=370 height=450 border=0 usemap="#<%= actPerfChartFileName %>"><br><br>
                                                                

								<div align="center">
                                                                     <field:display name="Activity Printer Friendly Button Performance" feature="Activity Dashboard">

							<html:button  styleClass="dr-menu" property="submitButton" 	onclick="javascript:showPrinterFriendly(${actId},'P')">
								<digi:trn key="btn:printerFriendly">Printer Friendly</digi:trn>
							</html:button>
                                                         </field:display>
                                                         <field:display name="Activity Without Baseline Button Performance" feature="Activity Dashboard">
							<html:button  styleClass="dr-menu" property="submitButton" onclick="javascript:actPerfWithoutBaseline(${actId},'P')">
								<digi:trn key="btn:withoutBaseline">Without Baseline</digi:trn>
							</html:button>
                                                         </field:display>
								</div>

								<% } else { %>

								<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>

							  <digi:trn key="aim:activityPerformanceChart">Activity-Performance chart</digi:trn>

							  </span><br><br>

								<% } %>

							</TD>
							</field:display>
							<field:display name="Project Risk"  feature="Activity Dashboard">
							
							<TD width="50%" valign="top">

								<% if (actRiskChartUrl != null) { %>

								<digi:trn key="aim:overallActivityRisk">Overall Risk</digi:trn>:

								<font color="<bean:write name="aimActivityDashboardForm" property="riskColor" />">



								<bean:define id="riskName" name="aimActivityDashboardForm" property="overallRisk" toScope="page"

								type="java.lang.String"/>

								<b><digi:trn key="<%=riskName%>"><%=riskName%></digi:trn></b>



								<img src="<%= actRiskChartUrl %>" width=370 height=350 border=0 usemap="#<%= actRiskChartFileName %>">

								<br><br>
                    <field:display name="Activity Printer Friendly Button Risk" feature="Activity Dashboard">

								<div align="center">
                                                                    
							<html:button  styleClass="dr-menu" property="submitButton" onclick="javascript:showPrinterFriendly(${actId},'R')">
								<digi:trn key="btn:printerFriendly">Printer Friendly</digi:trn>
							</html:button>
                                                         </field:display>

								</div>

								<% } else { %>

								<br><span class="red-log"><digi:trn key="aim:noDataPresentFor">No data present for</digi:trn>

							  <digi:trn key="aim:activityRiskChart">Activity-Risk chart</digi:trn>

							  </span><br><br>

								<% } %>

							</TD>
							</field:display>
						

						</TR>

					</TABLE>

				</TD></TR>

				<TR><TD>&nbsp;</TD></TR>

			</TABLE>

		</TD>

	</TR>

	<TR><TD>&nbsp;</TD></TR>

</TABLE>

</digi:form>
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>
