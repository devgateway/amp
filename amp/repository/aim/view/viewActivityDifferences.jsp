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

<digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
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
	<span class="subtitle-blue">
		<digi:trn>Compare Activities</digi:trn>
	</span>
<div style="text-align:center;width:1000px;">
	<br/>
	<br/>
	<table border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF" id="dataTable">
		<tr>
			<td bgcolor="#999999" align="left" style="padding-left: 5px;" width="8%">
				<strong><digi:trn>Value name</digi:trn></strong>
			</td>
			<td  bgcolor="#999999" align="left" valign="top" style="padding-left: 5px;" width="46%">
				<strong><digi:trn>First version (Older)</digi:trn></strong>
			</td>
			<td  bgcolor="#999999" align="left" valign="top" style="padding-left: 5px;" width="46%">
				<strong><digi:trn>Second version (Newer)</digi:trn></strong>
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
		