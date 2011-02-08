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
	
</style>


<digi:instance property="aimCompareActivityVersionsForm" />
<digi:form action="/compareActivityVersions.do" method="post">
	<div id="content"  class="yui-skin-sam" style="padding: 5px;"> 
		<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;font-size:10px;">
			<ul id="MyTabs" class="yui-nav">
				<li class="selected">
					<a href=""/><div><digi:trn>Compare Activities</digi:trn></div></a>
				</li>
			</ul>
		</div>
		<div style="border: 1px solid rgb(208, 208, 208); padding: 10px;" class="contentstyle" id="ajaxcontentarea">
			<table border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF" id="dataTable" width="100%">
				<tr>
					<td height="33" background="img_2/ins_bg.gif" width="15%" class="inside" style="background-repeat: repeat-x; font-size: 12px;">
	            		<div align="center">
	                		<strong><digi:trn>Value name</digi:trn></strong>
	            		</div>
	        		</td>
					<td height="33" background="img_2/ins_bg.gif" width="15%" class="inside" style="background-repeat: repeat-x; font-size: 12px;">
	            		<div align="center">
	                		<strong><digi:trn>First version (Older)</digi:trn></strong>
	            		</div>
	        		</td>
	        		<td height="33" background="img_2/ins_bg.gif" width="15%" class="inside" style="background-repeat: repeat-x; font-size: 12px;">
	            		<div align="center">
	                		<strong><digi:trn>Second version (Newer)</digi:trn></strong>
	            		</div>
	        		</td>
	        		<td height="33" background="img_2/ins_bg.gif" width="15%" class="inside" style="background-repeat: repeat-x; font-size: 12px; visibility: hidden">
	            		<div align="center">
	                		<strong><digi:trn>Merge</digi:trn></strong>
	            		</div>
	        		</td>
				</tr>
				<logic:iterate id="iter" property="outputCollection" name="aimCompareActivityVersionsForm">
					<tr>
						<td align="left" valign="center" style="padding-left: 5px;" width="8%">
							<digi:trn><bean:write name="iter" property="descriptionOutput"/></digi:trn>
						</td>
						<td align="left" valign="top" style="padding-left: 5px;" width="46%">
							<logic:empty name="iter" property="stringOutput[1]">&nbsp;</logic:empty>
							<bean:write name="iter" property="stringOutput[1]" filter="false"/>
						</td>
						<td align="left" valign="top" style="padding-left: 5px;" width="46%">
							<logic:empty name="iter" property="stringOutput[0]">&nbsp;</logic:empty>
							<bean:write name="iter" property="stringOutput[0]" filter="false"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
			<br/>
			<input type="button" value="<digi:trn>Back to current version of the activity</digi:trn>" onclick="history.back()" />
		</div>	
	</div>
</digi:form>

<script language="Javascript">
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
	if(tableElement){
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
	if(rowElement){
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
</script>
		