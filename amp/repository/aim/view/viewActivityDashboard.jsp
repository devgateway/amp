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

<div id="myContent" style="display: none;">
	<div id="myContentContent" class="content" style="overflow: scroll; height: 500px;">
	</div>
</div>
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




	YAHOOAmp.namespace("YAHOOAmp.amp");

	var myPanel = new YAHOOAmp.widget.Panel("myPreview", {
		width:"940px",
		fixedcenter: true,
	    constraintoviewport: false,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["showbtn", "tl", "bl"]
	    });
	var panelStart=0;

	var responseSuccess = function(o){
		/* Please see the Success Case section for more
		* details on the response object's properties.
		* o.tId
		* o.status
		* o.statusText
		* o.getResponseHeader[ ]
		* o.getAllResponseHeaders
		* o.responseText
		* o.responseXML
		* o.argument
		*/
		var response = o.responseText; 
		var content = document.getElementById("myContentContent");
		//response = response.split("<!")[0];
		content.innerHTML = response;
		//content.style.visibility = "visible";
		
		showContent();
	}

	function showPanelLoading(msg){
		   var content = document.getElementById("myContentContent");
		   content.innerHTML = "<div style='text-align: center'>" + "Loading..." +
		   "<br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";   
		   showContent();
		 }
		 
	var responseFailure = function(o){ 
		// Access the response object's properties in the 
		// same manner as listed in responseSuccess( ). 
		// Please see the Failure Case section and 
		// Communication Error sub-section for more details on the 
		// response object's properties.
		//alert("Connection Failure!"); 
	}  
	var callback = 
	{ 
		success:responseSuccess, 
		failure:responseFailure 
	};

	function showContent(){
		var element = document.getElementById("myContent");
		element.style.display = "inline";
		if (panelStart < 1){
			myPanel.setBody(element);
		}
		if (panelStart < 2){
			document.getElementById("myContent").scrollTop=0;
			myPanel.show();
			panelStart = 2;
		}
	}

	function preview(id)
	{
		showPanelLoading();
		var postString="&activityId=" + id+"&isPreview=2&previewPopin=true";
		//alert(postString);
		<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreviewPopin.do" />
		var url = "<%=addUrl %>?"+postString;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		
	}

	function initPopin() {
		var msg="\n<digi:trn>Activity Preview</digi:trn>";
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		
		myPanel.render(document.body);
	}

	window.onload=initPopin();

	function viewChanges(id){
		openNewWindow(650,200);
		<digi:context name="showLog" property="context/module/moduleinstance/showActivityLog.do" />
		popupPointer.document.location.href = "<%= showLog %>?activityId=" + id;
	}

	function expandAll() {
	   
		$("img[id$='_minus']").show();
		$("img[id$='_plus']").hide();	
		$("div[id$='_dots']").hide();
		$("div[id^='act_']").show('fast');
	}

	function collapseAll() {

		$("img[id$='_minus']").hide();
		$("img[id$='_plus']").show();	
		$("div[id$='_dots']").show();
		$("div[id^='act_']").hide();
	}

	function toggleGroup(group_id){
		var strId='#'+group_id;
		$(strId+'_minus').toggle();
		$(strId+'_plus').toggle();
		$(strId+'_dots').toggle();
		$('#act_'+group_id).toggle('fast');
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

						 actId,session,new PrintWriter(out),370,350,url);



	String actRiskChartUrl = null;



	if (actRiskChartFileName != null)  {

		actRiskChartUrl = request.getContextPath() + "/aim/DisplayChart.img?filename=" + actRiskChartFileName;

	}

%>

<digi:form action="/viewActivityDashboard.do" name="aimActivityForm" type="org.digijava.module.aim.form.aimActivityForm"

method="post">



<TABLE cellspacing="0" cellpadding="0" align="center" vAlign="top" border="0" width="100%">
	<TR>

		<TD vAlign="top" align="center">

			<TABLE width="99%" cellspacing="0" cellpadding="0" vAlign="top" align="center" class="box-border-nopadding">

				<TR><TD>

					<TABLE width="100%" cellspacing="2" cellpadding="2" valign="top" align="left" border="0">

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
                                                                

								<img src="<%= actPerfChartUrl %>" width=370 height=450 border="0" usemap="#<%= actPerfChartFileName %>"><br><br>
                                                                

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



								<img src="<%= actRiskChartUrl %>" width=370 height=350 border="0" usemap="#<%= actRiskChartFileName %>">

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
