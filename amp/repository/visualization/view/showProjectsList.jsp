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
		
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="inside">
  <tr>
    <td class="inside">N</td>
    <td class="inside">Title</td>
    <td class="inside">Amount</td>
  </tr>
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.itemProjectsList}" var="projectItem">
			<c:set var="index" value="${index+1}"/>
			<tr>
			<td><c:out value="${index}"/>.</td>
			<td><a href="/aim/viewActivityPreview.do~pageId=2~activityId=${projectItem.key.ampActivityId}~isPreview=1">${projectItem.key}</a></td>
			<td><b>($<c:out value="${projectItem.value}"/>)</b></td>
			</tr>
			</c:forEach>
			</table>
		<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="dr-menu" onclick="closeWindow()">
	</td>
	</tr>
</table>

</digi:form>
