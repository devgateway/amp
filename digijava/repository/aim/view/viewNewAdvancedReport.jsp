<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<link rel="stylesheet"
	href="<digi:file src="module/aim/css/newamp.css"/>" />

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<script language="JavaScript1.2" type="text/javascript"
	src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<!-- script for tree-like view (drilldown reports) -->

<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<script type="text/javascript" src="<digi:file src="module/aim/scripts/ajax.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

<!-- dynamic tooltip -->
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicContent.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dynamicTooltip.js"/>"></script>

<link rel="stylesheet"
	href="<digi:file src="module/aim/view/css/css_dhtmlsuite/modal-message.css"/>" />

<div style='position:relative;display:none;' id="sorterPicker-<bean:write name="reportMeta" property="ampReportId"/>">
		<b>Please select hierarchy sorter criteria:</b><br/>
		<jsp:include page="/repository/aim/view/ar/levelSorterPicker.jsp" />
		<br/>
		<a href='#' onclick='closeMessage();return false'>Close</a>
</div>
		
<table class=clsTable cellSpacing=0 cellPadding=0 width="100%" border=0>

	<logic:notEqual name="widget" scope="request" value="true">
	<logic:notEqual name="viewFormat" scope="request" value="print">
		<!-- attach filters -->
		<tr>
			<td><jsp:include page="/repository/aim/view/ar/toolBar.jsp" /></td>
		</tr>
		<tr>
			<td><jsp:include page="/repository/aim/view/ar/NewFilters.jsp" /></td>
		</tr>
	

	</logic:notEqual>
	</logic:notEqual>

	<logic:notEmpty name="reportMeta" property="hierarchies">
	<tr>
		

			<td>
			<div id="menucontainer">
			<input type="button" value="Hierarchy Sorting" 
			onclick="displayStaticMessage(document.getElementById('sorterPicker-<bean:write name="reportMeta" property="ampReportId"/>').innerHTML,false);return false"
			/>

			</div>
			
		</tr>
		
		<tr>
			<td>
				<logic:notEmpty name="report" property="levelSorters">
				<logic:iterate name="report" property="levelSorters" id="sorter" indexId="levelId">
				<logic:present name="sorter">
					Level <bean:write name="levelId"/> sorted by <bean:write name="sorter"/><br/>
				</logic:present>					
				</logic:iterate>
				</logic:notEmpty>
			</td>		
		</tr>
	<tr> <td>&nbsp;</td></tr>
	</logic:notEmpty>
	
	<tr>
		<td><bean:define id="reportMeta" name="reportMeta"
			type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
			toScope="page" />
		<ul>
			<li><%=org.digijava.module.aim.dbentity.AmpReports
							.getNote(session)%></li>
		</ul>
		</td>

	</tr>

	<logic:equal name="viewFormat" scope="request" value="print">
		<script language="JavaScript">
	function load() 
	{
		window.print();
	}

	function unload() {}
</script>
	</logic:equal>

	<tr>
		<td align="left"><font size="+1">Report Name: <b><bean:write
			scope="session" name="reportMeta" property="name" /></b></font></td>
	</tr>


	<tr>
		<td>Description: <i><bean:write scope="session" name="reportMeta"
			property="reportDescription" /></i></td>
	</tr>




	<logic:notEqual name="report" property="totalUniqueRows" value="0">
		<tr>
			<td><!-- begin big report table -->

			<table class=clsInnerTable cellSpacing=0 cellPadding=0 width="100%"
				border=0>
				<bean:define id="viewable" name="report"
					type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
				<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
			</table>

			<!-- end of big report table --></td>
		</tr>
	</logic:notEqual>
	<logic:equal name="report" property="totalUniqueRows" value="0">
		<tr>
			<td><b>The specified filtered report does not hold any data. Either
			pick a different filter criteria or use another report.</b></td>
		</tr>
	</logic:equal>
</table>


<script type="text/javascript">
messageObj = new DHTMLSuite.modalMessage();	// We only create one object of this class
messageObj.setWaitMessage('Loading message - please wait....');
messageObj.setShadowOffset(5);	// Large shadow

DHTMLSuite.commonObj.setCssCacheStatus(false);

function displayMessage(url)
{	
	messageObj.setSource(url);
	messageObj.setCssClassMessageBox(false);
	messageObj.setSize(400,200);
	messageObj.setShadowDivVisible(true);	// Enable shadow for these boxes
	messageObj.display();
}

function displayStaticMessage(messageContent,cssClass)
{
	messageObj.setHtmlContent(messageContent);
	messageObj.setSize(400,150);
	messageObj.setCssClassMessageBox(cssClass);
	messageObj.setSource(false);	// no html source since we want to use a static message here.
	messageObj.setShadowDivVisible(false);	// Disable shadow for these boxes	
	messageObj.display();
	
	
}

function closeMessage()
{
	messageObj.close();	
}
</script>

<div id="debug"></div>
