<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/amp.css">
<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css">
<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/yui_datatable.css">
<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css">
<link type="text/css" rel="stylesheet" media="print" href="/TEMPLATE/ampTemplate/css_2/comparePrint.css" />

<style type="text/css">
.tableEven {
	background-color: #dbe5f1;
	font-size: 8pt;
	padding: 2px;
}

.tableOdd {
	background-color: #FFFFFF;
	font-size: 8pt;
	padding: 2px;
}

.Hovered {
	background-color: #a5bcf2;
}

.notHovered {
	background-color: #FFFFFF;
}
@media print {
	.printPreview {visibility:visible;}
}
@media screen {
	.printPreview {visibility:hidden;}
}
</style>

<digi:instance property="aimCompareActivityVersionsForm" />
<digi:errors/>
<digi:form action="/compareActivityVersions.do" method="post" styleId="compareForm">
	<html:hidden property="showMergeColumn" styleId="showMergeColumn"/>
	<html:hidden property="method" styleId="method"/>
	<html:hidden property="ampActivityId" styleId="ampActivityId"/>

	<c:if test="${empty aimCompareActivityVersionsForm.outputCollectionGrouped and aimCompareActivityVersionsForm.method != 'compareAll'}">
		<c:set var="noPrevVer">
			<digi:trn>The activity you chose is the latest and has no previous version.</digi:trn>
		</c:set>
		<script type="text/javascript">
			alert("${noPrevVer}");
			window.history.back();
		</script>
	</c:if>
	<div id="content"  class="yui-skin-sam" style="padding: 5px;">
		<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;font-size:10px;">
			<ul id="MyTabs" class="yui-nav">
				<li class="selected">
					<c:if test="${aimCompareActivityVersionsForm.method == 'compareAll'}">
						<a><div><digi:trn>List of Activities Compared to their Previous Versions</digi:trn></div></a>
					</c:if>
					<c:if test="${aimCompareActivityVersionsForm.method != 'compareAll'}">
						<a><div><digi:trn>Compare Activities</digi:trn></div></a>
					</c:if>
					<a target="_blank" onclick="generateExport('pdfExport'); return false;" title="Export to PDF"
					   style="cursor: pointer;">
						<img src="/TEMPLATE/ampTemplate/images/icons/pdf.gif" border="0" hspace="2" vspace="2" alt="Export to PDF">
					</a>
					<a target="_blank" onclick="generateExport('xlsExport'); return false;" title="Export to Excel" style="cursor: pointer;">
						<img src="/TEMPLATE/ampTemplate/imagesSource/common/ico_exc.gif" border="0" hspace="2" vspace="2" alt="Export to Excel">
					</a>
					<a target="_blank" onclick="window.print();" style="cursor: pointer; color:#376091;" title="Print">
						<img id="Print" hspace="2" src="img_2/ico_print.gif" width="15" height="18">
						Print
					</a>
				</li>
			</ul>
		</div>

		<c:if test="${aimCompareActivityVersionsForm.method == 'viewDifferences'}">
			<div class="printPreview">
				<strong>Activity: <bean:write name="aimCompareActivityVersionsForm" property="activityName" /> </strong>
			</div>
		</c:if>

		<div style="border: 1px solid rgb(208, 208, 208); padding: 10px;font-size:12px; height: 100%;" class="contentstyle" id="ajaxcontentarea">
			<table border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF" id="dataTable" width="100%">
				<tr>
					<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" width="15%" class="inside" style="background-repeat: repeat-x; font-size: 12px; border-left-width: 1px; width: 13%">
	            		<div align="center">
	                		<strong><digi:trn>Value name</digi:trn></strong>
	            		</div>
	        		</td>
					<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" style="background-repeat: repeat-x; font-size: 12px; border-right-width: 0px; width: 29%">
	            		<div align="center">
	                		<strong><digi:trn>First version (Older)</digi:trn></strong>
	            		</div>
	        		</td>
	        		<logic:equal value="true" name="aimCompareActivityVersionsForm" property="showMergeColumn">
	        			<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" style="background-repeat: repeat-x; font-size: 12px;">
		            		<div align="center">
		                		&nbsp;
		            		</div>
		        		</td>
		        		<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" style="background-repeat: repeat-x; font-size: 12px; width: 29%">
		            		<div align="center">
		                		<strong><digi:trn>Merge</digi:trn></strong>
		            		</div>
		        		</td>
		        		<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" style="background-repeat: repeat-x; font-size: 12px; border-right-width: 0px;">
		            		<div align="center">
		                		&nbsp;
		            		</div>
		        		</td>
	        		</logic:equal>
	        		<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" style="background-repeat: repeat-x; font-size: 12px; border-left-width: 0px; width: 29%">
	            		<div align="center">
	                		<strong><digi:trn>Second version (Newer)</digi:trn></strong>
	            		</div>
	        		</td>
				</tr>

					<%-- Iterate through the list of output collections for compareAll method... --%>
				<c:if test="${aimCompareActivityVersionsForm.method == 'compareAll'}">
					<%int count = 0; %>
					<logic:iterate id="listItem" property="activityComparisonResultList" name="aimCompareActivityVersionsForm" type="org.digijava.module.aim.util.versioning.ActivityComparisonResult">
						<tr>
						<td colspan="100%"  class="inside" style="background-color:#E9ECC3; border-color: red; border-width: 1px; color:#0000A0; cursor: pointer; background-repeat: repeat-x; font-size: 13px;">

							<div style="line-height: 95%;"align="left" class="underline" title="<bean:write name="listItem" property="name" filter="false"/>">
								<strong><%out.print("<br> ["+(++count)+"]. "); %><bean:write name="listItem" property="name" filter="false"/></strong>
							</div>
							<bean:define id="beanGroupItem" name="listItem" property="compareOutput" scope="page" toScope="request"/>
							<jsp:include page="viewGroupedOutput.jsp"/>
						</td>
						</tr>
					</logic:iterate>
				</c:if>

					<%-- if the method isn't compareAll, for any method is to keep the existing functionality and fix null pointer exception --%>
				<c:if test="${(not empty aimCompareActivityVersionsForm.outputCollectionGrouped) and (aimCompareActivityVersionsForm.method != 'compareAll')}">
					<bean:define id="beanGroupItem" name="aimCompareActivityVersionsForm" property="outputCollectionGroupedAsSet" scope="page" toScope="request"/>
					<jsp:include page="viewGroupedOutput.jsp"/>
				</c:if>

			</table>
			<br/>
			<input id="backButton" type="button" value="<digi:trn>Back to current version of the activity</digi:trn>" onclick="javascript:back()" />
			<input id="exportButton" type="button" value="<digi:trn>Export to Excel</digi:trn>" onclick="javascript:generateExport('xlsExport');" />
			<logic:equal name="aimCompareActivityVersionsForm" property="advancemode" value="true">
				<input id="mergeButton" type="button" value="<digi:trn>Enable Merge Process</digi:trn>" onclick="javascript:enableMerge();" />
			</logic:equal>
			<input id="saveButton" type="button" value="<digi:trn>Save New Activity</digi:trn>" onclick="javascript:save();" />
		</div>	
	</div>
</digi:form>

<script language="Javascript">
function back() {
	if (document.aimCompareActivityVersionsForm.method.value === "viewDifferences") {
		window.history.back();
	} else if (document.aimCompareActivityVersionsForm.method.value === "pdfExport") {
		window.history.back();
	} else if (document.aimCompareActivityVersionsForm.method.value === "xlsExport") {
		window.history.back();
	} else {
		document.getElementById("method").value = "cancel";
		document.getElementById('compareForm').submit();
	}
}

function generateExport(method){
	document.aimCompareActivityVersionsForm.method.value = method;
	document.aimCompareActivityVersionsForm.submit();
}

function enableMerge() {
	document.getElementById('showMergeColumn').value = "true";
	document.getElementById('method').value = "enableMerge";
	document.getElementById('compareForm').submit();
}

function save() {
	document.getElementById('showMergeColumn').value = "false";
	document.getElementById('method').value = "saveNewActivity";
	document.getElementById('compareForm').submit();
}

function right(id) {
	document.getElementById("merge"+id).innerHTML = document.getElementById("right"+id).innerHTML;
	document.getElementById("mergedValues["+id+"]").value = "R";
}

function left(id) {
	document.getElementById("merge"+id).innerHTML = document.getElementById("left"+id).innerHTML;
	document.getElementById("mergedValues["+id+"]").value = "L";
}

function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}

function setHoveredTable(tableId, hasHeaders) {
	var tableElement = document.getElementById(tableId);
	if(tableElement) {
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');
		
		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');
			};
		}
		rows = null;
	}
}

function setHoveredRow(rowId) {
	var rowElement = document.getElementById(rowId);
	if(rowElement) {
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        cells      = rowElement.getElementsByTagName('td');

		for(var i = 0, n = cells.length; i < n; ++i) {
			cells[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			cells[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');
			};
		}
		cells = null;
	}
}

setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", true);
setHoveredRow("rowHighlight");

if(document.getElementById('method').value == "enableMerge") {
	document.getElementById('mergeButton').disabled = "disabled";
	document.getElementById('mergeButton').style.display = 'none';
	document.getElementById('saveButton').disabled = "";
	document.getElementById('saveButton').style.display = 'block';
} else if (document.aimCompareActivityVersionsForm.method.value === "viewDifferences"){
	document.getElementById('saveButton').disabled = "disabled";
	document.getElementById('saveButton').style.display = 'none';
	$('#backButton').prop('value', '<digi:trn>Back to Audit Logger</digi:trn>');
}else if (document.aimCompareActivityVersionsForm.method.value === "compareAll"){
	document.getElementById('saveButton').disabled = "disabled";
	document.getElementById('saveButton').style.display = 'none';
	document.getElementById('backButton').style.visibility = "hidden";
}else if (document.aimCompareActivityVersionsForm.method.value === "pdfExport" || document.aimCompareActivityVersionsForm.method.value === "xlsExport"){
	document.getElementById('saveButton').disabled = "disabled";
	document.getElementById('saveButton').style.display = 'none';
	$('#backButton').prop('value', '<digi:trn>Back to Audit Logger</digi:trn>');
}
else {
	document.getElementById('saveButton').disabled = "disabled";
	document.getElementById('saveButton').style.display = 'none';
}
</script>
		