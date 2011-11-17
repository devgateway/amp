<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<style>
table.inside, td.inside,td.report_inside {border-color: #CCC; border-style: solid; font-size:11px;}
table.inside1, td.inside1 {border: 0; font-size:12px;}
table.inside, td.inside_zebra {}
table.inside {border-width: 0 0 1px 1px; border-spacing: 0; border-collapse: collapse;}
td.inside {margin: 0; padding: 4px; border-width: 1px 1px 0 0;}

</style>

<script language="JavaScript">
	
	function closeWindow() {
		window.close();
	}
</script>

<digi:instance property="visualizationform" />

<digi:form action="/showProjectsList.do" method="post">

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr>	
		<td vAlign="top">
		
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="inside" style="margin-bottom:15px;">
		<c:forEach items="${visualizationform.itemProjectsList}" var="item"> 
			<tr bgcolor=#555555>
		    <td class="inside" align=center colspan="3"><font color=#C7D4DB><b>${item.key}</b></font></td>
		  </tr>
		  <tr bgcolor=#C7D4DB>
		    <td class="inside" align=center><b>N</b></td>
		    <td class="inside"><b><digi:trn>Title</digi:trn></b></td>
		    <td class="inside" align=center><b><digi:trn>Amount</digi:trn></b></td>
		  </tr>
			<c:set var="index" value="0"/>
			<c:forEach items="${item.value}" var="projectItem">
			<c:set var="index" value="${index+1}"/>
			<tr>
			<td class="inside" align=center><c:out value="${index}"/>.</td>
			<td class="inside"><a href="/aim/viewActivityPreview.do~pageId=2~activityId=${projectItem.key.ampActivityId}~isPreview=1" style="color:#376091;">${projectItem.key}</a></td>
			<td class="inside" align=center width="90"><b>(<c:out value="${projectItem.value}"/> <c:out value="${visualizationform.filter.currencyCode}"/>)</b></td>
			</tr>
			</c:forEach>
			<tr>
		    <td class="inside" align=center colspan="3"></td>
		  </tr>
		</c:forEach>
	</table>
	<center><input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="dr-menu" onclick="closeWindow()"></center>
	</td>
	</tr>
</table>

</digi:form>
