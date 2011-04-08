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

<table width="100%" height="100%" cellpadding=5 cellspacing="0" valign="top" align=left bgcolor="#006699">
	<c:if test="${aimMainProjectDetailsForm.type == 'ProjCom'}">
		<tr height="20">
			<td class="textalb" align="center">
				<digi:trn key="aim:projectComments">Project Comments</digi:trn>
			</td>
		</tr>
		<tr bgcolor="#ffffff" valign="top">
			<td>
				<div style="height:400">
				<logic:notEmpty name="aimMainProjectDetailsForm" property="projectComments">
					<bean:define id="projcomKey">
						<c:out value="${aimMainProjectDetailsForm.projectComments}"/>
					</bean:define>
					<digi:edit key="<%=projcomKey%>"></digi:edit>
					</logic:notEmpty>
				</div>
			</td>
		</tr>		
	</c:if>
	<c:if test="${aimMainProjectDetailsForm.type == 'Desc'}">
		<tr height="20">
			<td class="textalb" align="center">
				<digi:trn key="aim:description">Description</digi:trn>
			</td>
		</tr>
		<tr bgcolor="#ffffff" valign="top">
			<td>
				<div style="height:400">
				<logic:notEmpty name="aimMainProjectDetailsForm" property="description">
					<bean:define id="descKey">
						<c:out value="${aimMainProjectDetailsForm.description}"/>
					</bean:define>
					<digi:edit key="<%=descKey%>"></digi:edit>
				</logic:notEmpty>
				</div>
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
				<div style="height:400">
				<logic:notEmpty name="aimMainProjectDetailsForm" property="objectives">
					<bean:define id="objKey">
						<c:out value="${aimMainProjectDetailsForm.objectives}"/>
					</bean:define>
					<div style="height:300px;">
					<digi:edit key="<%=objKey%>"></digi:edit>
					</div>
					</logic:notEmpty>
				</div>
			</td>
		</tr>		
	</c:if>
	<c:if test="${aimMainProjectDetailsForm.type == 'Res'}">
		<tr height="20">
			<td class="textalb" align="center">
				<digi:trn key="aim:results">Results</digi:trn>
			</td>
		</tr>
		<tr bgcolor="#ffffff" valign="top">
			<td>
				<div style="height:400">
				<logic:notEmpty name="aimMainProjectDetailsForm" property="results">
					<bean:define id="resKey">
						<c:out value="${aimMainProjectDetailsForm.results}"/>
					</bean:define>
					
					<digi:edit key="<%=resKey%>"></digi:edit>
					</logic:notEmpty>
				</div>
			</td>
		</tr>		
	</c:if>
	<c:if test="${aimMainProjectDetailsForm.type == 'Purp'}">
		<tr height="20">
			<td class="textalb" align="center">
				<digi:trn key="aim:purpose">Purpose</digi:trn>
			</td>
		</tr>
		<tr bgcolor="#ffffff" valign="top">
			<td>
				<div style="height:400">
				<logic:notEmpty name="aimMainProjectDetailsForm" property="purpose">
					<bean:define id="purpKey">
						<c:out value="${aimMainProjectDetailsForm.purpose}"/>
					</bean:define>
					<digi:edit key="<%=purpKey%>"></digi:edit>
					</logic:notEmpty>
				</div>
			</td>
		</tr>		
	</c:if>
	<tr bgcolor="#ffffff" height="20">
		<td align="center">
			<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="dr-menu" onclick="myPanelOverviewframe.hide()">
		</td>
	</tr>
</table>
