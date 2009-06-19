<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"> 
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>
<div id="reportSheet" style="display:none">
	<div class="hd">Report Sheet</div>
	<div align="center" class="bd" id="tableForReportSheet">
		<table>
				<tr>
					<td><strong>ID:</strong></td>
					<td id='reportId'>&nbsp;</td>
				</tr>
				<tr>
					<td><strong>Name:</strong></td>
					<td id='reportName'>&nbsp;</td>
				</tr>
				<tr>
					<td><strong>Type:</strong></td>
					<td id='reportType'>&nbsp;</td>
				</tr>
				<tr>
					<td><strong>Is drilldown:</strong></td>
					<td id='reportDrilldown'>&nbsp;</td>
				</tr>
				<tr>
					<td><strong>Is public:</strong></td>
					<td id='reportPublic'>&nbsp;</td>
				</tr>
				<tr>
					<td><strong>Description:</strong></td>
					<td id='reportDescription'>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><a style='cursor:pointer;color:#006699' onClick='openReportWindow()'><digi:trn key='aim:defaultTeamReportViewReportMessage'>View report</digi:trn></a></td>
				</tr>
				<tr>
					<td colspan="2"><button type="button" id="closePanelButton" onClick="YAHOO.reportsheet.myPanel.hide()">Close</button></td>
				</tr>
			</table>
	</div>
</div>



<div id="helpReports" style="display:none">
	<div class="hd"><digi:trn key="aim:reportsheet">Report Sheet</digi:trn></div>
	<div align="center" class="bd" id="tableForReportSheet">
		<table>
				<tr>
					<td id='reportId'>
					<digi:trn key="aim:defReportsPerPageHelp">
						to open all reports on one page, please enter the digit "0"
					</digi:trn>
					</td>
				</tr>
			
			</table>
	</div>
</div>
<div id="helpReports1" style="display:none">
	<div class="hd"><digi:trn key="aim:reportsheet">Report Sheet</digi:trn></div>
	<div align="center" class="bd" id="tableForReportSheet">
		<table>
				<tr>
					<td id='reportId'>
					<digi:trn key="aim:defReportsPerPageHelp">
						Please enter a digit greater that "1"
					</digi:trn>
					</td>
				</tr>
			
			</table>
	</div>
</div>
<script type="text/javascript">
	YAHOO.namespace("YAHOO.reportsheet");
	function initReportSheet() {
		var reportSheetElement				= document.getElementById("reportSheet");
		reportSheetElement.style.display	= "block";
		YAHOO.reportsheet.myPanel		= new YAHOO.widget.Panel("reportSheet", {
				width:"200px", 
				fixedcenter: true, 
				constraintoviewport: true, 
				underlay:"shadow", 
				close:true, 
				visible:false, 
				draggable:true} );
		//var div							= document.getElementById('tableForReportSheet');
		//YAHOO.reportsheet.myPanel.setBody( div );
		//YAHOO.reportsheet.myPanel.render(document.body);
		YAHOO.reportsheet.myPanel.render();
	}

	//YAHOO.util.Event.addListener(window, "load", initReportSheet) ;


	YAHOO.reportsheet.jsReports	= new Array();
	var i;
	<logic:iterate name="aimUpdateAppSettingsForm" property="reports" id="report" type="org.digijava.module.aim.dbentity.AmpReports"> 
		<%
			String descr	= report.getReportDescription();
			if ( descr != null ) {
				descr			= descr.replaceAll("\n"," ");
				descr			= descr.replaceAll("\r","");
			}
		%>
		i							= <bean:write name="report" property="ampReportId" />;
		YAHOO.reportsheet.jsReports[i]				= new Object();
		YAHOO.reportsheet.jsReports[i].name			= '<bean:write name="report" property="name" />';
		YAHOO.reportsheet.jsReports[i].description	= '<%= descr %>';
		YAHOO.reportsheet.jsReports[i].id			= '<bean:write name="report" property="ampReportId" />';
		YAHOO.reportsheet.jsReports[i].type			= '<bean:write name="report" property="type" />';
		YAHOO.reportsheet.jsReports[i].drilldown	= '<bean:write name="report" property="drilldownTab" />';
		YAHOO.reportsheet.jsReports[i].publicReport = '<bean:write name="report" property="publicReport" />';
		i++;
	</logic:iterate>
</script>
<script type="text/javascript">
	YAHOO.namespace("YAHOO.reportsheet");
	function openReportWindow () {
		var selectedReport	= document.getElementById('defaultReport').value;
		var reference		= '/viewNewAdvancedReport.do?view=reset&ampReportId=' + selectedReport;
		
		window.open(reference, "_blank");
	}
	
	
	function changePanel() {
		var selectedReport	= document.getElementById('defaultReport').value;
		//alert(selectedReport);
		//alert(document.getElementById('reportName').innerHTML);
		//alert(YAHOO.reportsheet.jsReports[selectedReport].name);
		document.getElementById('reportId').innerHTML			= YAHOO.reportsheet.jsReports[selectedReport].id;
		document.getElementById('reportName').innerHTML			= YAHOO.reportsheet.jsReports[selectedReport].name;
		var num_type											= YAHOO.reportsheet.jsReports[selectedReport].type;
		var str_type	= '';
		switch(num_type) {
			case '1': 
				str_type="Donor";
				break;
			case '2': 
				str_type="Component";
				break;
			case '3': 
				str_type="Regional";
				break;
			case '4': 
				str_type="Contribution";	
				break;
		}
		document.getElementById('reportType').innerHTML			= str_type;
		document.getElementById('reportDrilldown').innerHTML	= returnYesNo(YAHOO.reportsheet.jsReports[selectedReport].drilldown);
		document.getElementById('reportPublic').innerHTML		= returnYesNo(YAHOO.reportsheet.jsReports[selectedReport].publicReport);
		document.getElementById('reportDescription').innerHTML	= YAHOO.reportsheet.jsReports[selectedReport].description;
		
		YAHOO.reportsheet.myPanel.setHeader(YAHOO.reportsheet.jsReports[selectedReport].name);
	}
	function showMyPanel () {
		initReportSheet();
		changePanel();
		YAHOO.reportsheet.myPanel.show();
	}
	function returnYesNo(boolStr) {
		if (boolStr == 'true')
			return 'yes';
		return 'no';
	}
	
	
	
	
	
	function changePanelToHelp() {
		var reportSheetElement				= document.getElementById("helpReports");
		reportSheetElement.style.display	= "block";
		YAHOO.reportsheet.myPanel		= new YAHOO.widget.Panel("helpReports", {
				width:"200px", 
				fixedcenter: true, 
				constraintoviewport: true, 
				underlay:"shadow", 
				close:true, 
				visible:false, 
				draggable:true} );
		YAHOO.reportsheet.myPanel.render();
		
	}
	function showHelpPanel(){
		changePanelToHelp();
		YAHOO.reportsheet.myPanel.show();
	}
	
	function showHelpPanel_DefRecsPerPage(){
		var reportSheetElement				= document.getElementById("helpReports1");
		reportSheetElement.style.display	= "block";
		YAHOO.reportsheet.myPanel		= new YAHOO.widget.Panel("helpReports1", {
				width:"200px", 
				fixedcenter: true, 
				constraintoviewport: true, 
				underlay:"shadow", 
				close:true, 
				visible:false, 
				draggable:true} );
		YAHOO.reportsheet.myPanel.render();
		YAHOO.reportsheet.myPanel.show();
	}	

</script>
