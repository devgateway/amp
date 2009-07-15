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

<script type="text/javascript" >
	initObj					= new Object();
	initObj.initialize		= function () {;};
	function init () {
		//alert("1");
		initObj.initialize();
	}
	YAHOOAmp.util.Event.addListener(window, "load", init) ;

	function changePage ( showTabs, action ) {
			var myForm						= document.forms["aimImpExpForm"];
			myForm.showTabs.value	= ""+ showTabs;
			myForm.action					= "/aim/reportsExport.do~action="+action;
			myForm.submit ();
	}
	function changePage2 ( showTabs, action ) {
		var myForm						= document.forms["aimImpExpForm"];
		myForm.showTabs.value	= ""+ showTabs;
		myForm.action					= "/aim/reportsImport.do~action="+action;
		myForm.submit ();
	}
</script>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="90%">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33>
					<span class="crumb">
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn>
							Reports Import - Export
						</digi:trn>
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
							<digi:trn key="aim:categoryManagerCreator">
								Reports Import - Export
							</digi:trn>					
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<html:errors />
					</td>
				</tr>
				<tr>
					<td>
						<div id="wizard_container" class="yui-navset">
							<ul class="yui-nav">
								<li  class="${aimImpExpForm.exportReportsClass}"><a onclick="changePage(false,'${aimImpExpForm.exportReportsAction }')"><div><digi:trn>Export Reports</digi:trn></div></a> </li>
								<li  class="${aimImpExpForm.exportTabsClass}"><a onclick="changePage(true,'${aimImpExpForm.exportTabsAction }')""><div><digi:trn>Export Tabs</digi:trn></div></a> </li>
								<li  class="${aimImpExpForm.importClass }"><a href="/aim/reportsImport.do~action=new"><div><digi:trn>Import Reports And Tabs</digi:trn></div></a> </li>
							</ul>
							<div class="yui-content" style="background-color: #EEEEEE">
								<div id="hierarchies_step_div" class="yui-tab-content" style="padding: 0px 0px 1px 0px; " >
									<jsp:include page="${aimImpExpForm.includedJsp}" />
								</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
