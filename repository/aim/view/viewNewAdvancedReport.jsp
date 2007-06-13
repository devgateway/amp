<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>


<div style='position:relative;display:none;' id="sorterPicker-<bean:write name="reportMeta" property="ampReportId"/>">
	
		<jsp:include page="/repository/aim/view/ar/levelSorterPicker.jsp" />
		<br/>
		<a href='#' onclick='closeMessage();return false'><b>Close</b></a>
</div>

<div style='position:relative;display:none;' id="filterPicker-<bean:write name="reportMeta" property="ampReportId"/>">
		<jsp:include page="/aim/reportsFilterPicker.do" />
		
		<br/>
		<a href='#' onclick='closeMessage();return false'><b>Close</b></a>

</div>

		
<table  cellSpacing="0" cellPadding="0" width="95%" border="0">

	<logic:notEqual name="widget" scope="request" value="true">
	<logic:notEqual name="viewFormat" scope="request" value="print">
		<!-- attach filters -->
		<tr>
			<td><jsp:include page="/repository/aim/view/ar/toolBar.jsp" /></td>
		</tr>
		<tr>
		<!-- filters -->
	
		
		</tr>
	

	</logic:notEqual>
	</logic:notEqual>



	<tr>
		<td><bean:define id="reportMeta" name="reportMeta"
			type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
			toScope="page" />
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

<logic:notEqual name="widget" scope="request" value="true">

	<tr>
		<td align="left"><font size="+1">Report Name: <b><bean:write
			scope="session" name="reportMeta" property="name" /></b></font></td>
	</tr>


	<tr>
		<td>Description: <i><bean:write scope="session" name="reportMeta"
			property="reportDescription" /></i></td>
	</tr>

</logic:notEqual>


	<tr>
		<td><logic:notEmpty name="reportMeta" property="hierarchies">
			<div id="menucontainer"><a style="cursor:pointer"
				onclick="displayStaticMessage(document.getElementById('sorterPicker-<bean:write name="reportMeta" property="ampReportId"/>').innerHTML,false,400,150);return false">
			<u>Change Sorting</u> </a>
		</logic:notEmpty>
<a style="cursor:pointer"
			onclick="displayStaticMessage(document.getElementById('filterPicker-<bean:write name="reportMeta" property="ampReportId"/>').innerHTML,false,400,460);return false">
		<u>Change Filters</u> </a></div>
		</td>
	</tr>



	<tr>
			<td align="right">
				<font size="-5" face="arial" color="red">
					<span  STYLE="font-style:  italic">
					<%=org.digijava.module.aim.dbentity.AmpReports.getNote(session)%>
					</span>			
				</font>
			</td>
		</tr>
<logic:notEmpty name="reportMeta" property="hierarchies">		
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
	<td width="500">
	Currently Selected Filters:
	<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" scope="session">
		<bean:write name="<%=org.dgfoundation.amp.ar.ArConstants.REPORTS_FILTER%>" filter="false"/>
	</logic:present>
	</td>
	</tr>

	<tr>
	


	<logic:notEqual name="report" property="totalUniqueRows" value="0">
		<tr>
			<td><!-- begin big report table --> 

			<table id='reportTable'  cellSpacing="0" cellPadding="1" width="100%" class="reportsBorderTable">
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


<div id="debug"></div>

