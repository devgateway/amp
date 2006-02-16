<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
<!--

	function load() {}
	function unload() {}

-->
</script>

<digi:instance property="aimMainProjectDetailsForm" />

<table width="100%" height="100%" cellpadding=5 cellspacing=0 valign=top align=left bgcolor="#006699">
	<c:if test="${aimMainProjectDetailsForm.type == 'Desc'}">
		<tr height="20">
			<td class="textalb" align="center">
				<digi:trn key="aim:description">Description</digi:trn>
			</td>
		</tr>
		<tr bgcolor="#ffffff" valign="top">
			<td>
				<bean:define id="descKey">
					<c:out value="${aimMainProjectDetailsForm.description}"/>
				</bean:define>
				<digi:edit key="<%=descKey%>"></digi:edit>
			</td>
		</tr>	
	</c:if>
	<c:if test="${aimMainProjectDetailsForm.type == 'Obj'}">
		<tr height="20">
			<td class="textalb" align="center">
				<digi:trn key="aim:objectives">Objectives</digi:trn>
			</td>
		</tr>
		<tr bgcolor="#ffffff" valign="top">
			<td>
				<bean:define id="objKey">
					<c:out value="${aimMainProjectDetailsForm.objectives}"/>
				</bean:define>
				<digi:edit key="<%=objKey%>"></digi:edit>
			</td>
		</tr>		
	</c:if>
	<tr bgcolor="#ffffff" height="20">
		<td align="center">
			<input type="button" value="Close" class="buton" onclick="window.close()">
		</td>
	</tr>
</table>
