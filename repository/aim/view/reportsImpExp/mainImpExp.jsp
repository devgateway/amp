<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ page import="java.util.List"%>


<link href="css/tabview.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">

<digi:instance property="aimImpExpForm"  />

<script type="text/javascript" src="<digi:file src='module/aim/scripts/scrollableTable.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<script type="text/javascript" >
	initObj					= new Object();
	initObj.initialize		= function () {;};
	function init () {
		//alert("1");
		initObj.initialize();
	}
	YAHOO.util.Event.addListener(window, "load", init) ;

	function changePage ( showTabs, action ) {//export
		if(action=='export'){
			var checkedReports = $('input.selReportsIds:checked');
			if(checkedReports==null || checkedReports.length==0){
				alert('Please select at least one entry to export');
				return false;
			}
		}
		var myForm	= document.forms["aimImpExpForm"];
		myForm.showTabs.value	= ""+ showTabs;
		myForm.action	= "/aim/reportsExport.do~action="+action;
		myForm.submit();
	}
	
	//function changePage2 ( showTabs, action ) { //import
	//	var myForm	= document.forms["aimImpExpForm"];
		//myForm.showTabs.value	= ""+ showTabs;
	//	myForm.action= "/aim/reportsImport.do~action="+action;
	//	myForm.submit();
	//}

	function changePageFromImport(action){
		var myForm	= document.forms["aimImpExpForm"];
		myForm.action= "/aim/reportsImport.do~action="+action;
		myForm.submit();
	}
</script>

<!-- tabs styles -->
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

#tabs ul li.selected a { 
	background:#222E5D url(/TEMPLATE/ampTemplate/images/tableftcorner.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;
	cursor: pointer;

}

#tabs ul li.selected a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcorner.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs ul li.enabled a { 
	background:#3754A1 url(/TEMPLATE/ampTemplate/images/tableftcornerunsel.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;
	cursor: pointer;
}

#tabs ul li.enabled a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcornerunsel.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
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
	width:969px;
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

<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="90%">
	<tr>
		<td class="r-dotted-lg" width="14">&nbsp;</td>
		<td align="left" class="r-dotted-lg" vAlign="top" width="750">
			<table cellPadding="5" cellSpacing="0"width="100%" border="0">
				<tr>
					<!-- Start Navigation -->
					<td height="33">
					<span class="crumb">
						<c:set var="translation">
							<digi:trn>Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
						<digi:trn>Admin Home</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn>
							Reports Import - Export
						</digi:trn>
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height="16" vAlign="middle" width="571">
						<span class="subtitle-blue">
							<digi:trn>Reports Import - Export</digi:trn>					
						</span>
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="middle" width="571">
						<html:errors />
					</td>
				</tr>
				<tr>
					<td>
						<DIV id="tabs">
							<UL class="yui-nav">
						      	<LI class="${aimImpExpForm.exportReportsClass}">
						           	<a onclick="changePage(false,'${aimImpExpForm.exportReportsAction }')"><div ><digi:trn>Export Reports</digi:trn></div></a>
						        </LI>
								<LI class="${aimImpExpForm.exportTabsClass}">
						           	<a onclick="changePage(true,'${aimImpExpForm.exportTabsAction }')""><div><digi:trn>Export Tabs</digi:trn></div></a>
						        </LI>
						        <LI class="${aimImpExpForm.importClass}">
						           	<a href="/aim/reportsImport.do~action=new"><div><digi:trn>Import Reports And Tabs</digi:trn></div></a>
						        </LI>
							</UL>					
						</DIV>
						<div class="yui-content">	
							<div id="main"  class="yui-tab-content"  style="padding: 0px 0px 1px 0px; background-color: #EEEEEE">								
								<jsp:include page="${aimImpExpForm.includedJsp}" />
							</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
