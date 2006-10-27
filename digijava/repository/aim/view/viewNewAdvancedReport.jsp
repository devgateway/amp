<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<link rel="stylesheet" href="<digi:file src="module/aim/css/newamp.css"/>"/>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>



<table class=clsTable cellSpacing=0 cellPadding=0 width="100%" border=0>

<logic:notEqual name="viewFormat" scope="request" value="print">
	<!-- attach filters -->
	<tr>
		<td>
		 <jsp:include page="/repository/aim/view/ar/toolBar.jsp"/>
		</td>
	</tr>
	<tr>
		<td>
		 <jsp:include page="/repository/aim/view/ar/NewFilters.jsp"/>
		</td>
	</tr>
</logic:notEqual>	

<tr>
	<td>
			
	        <bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>
			<ul><li><%=reportMeta.getNote(session)%></li></ul>
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
		<td align="left">
			<font size="+1">Report Name: <b><bean:write scope="session" name="reportMeta" property="name"/></b></font>
		</td>
	</tr>


	<tr>
		<td>
			Description: <i><bean:write scope="session" name="reportMeta" property="reportDescription"/></i>
		</td>
	</tr>




	<logic:notEqual name="report" property="totalUniqueRows" value="0">
	<tr>
		<td><!-- begin big report table -->
		<table class=clsInnerTable cellSpacing=0 cellPadding=0 width="100%" border=0>
			<bean:define id="viewable" name="report"
				type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
			<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
		</table>
		<!-- end of big report table --></td>
	</tr>
	</logic:notEqual>
	<logic:equal name="report" property="totalUniqueRows" value="0">
	<tr>
		<td>
		<b>The specified filtered report does not hold any data. Either pick a different filter criteria or use another report.</b>
		</td>
	</tr>
	</logic:equal>	
</table>
