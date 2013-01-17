<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>


<module:display name="/Activity Form/Line Ministry Observations" parentModule="/Activity Form">
	<fieldset>
	<legend>
		<span class=legend_label><digi:trn>Line Ministry Observations</digi:trn></span>	</legend>
		
	<div id="lineMinistryObs">
		<c:if test="${not empty aimEditActivityForm.lineMinistryObservations.issues}">
		<logic:iterate name="aimEditActivityForm" id="lineMinistryObs" property="lineMinistryObservations.issues">
			<table style="width: 100%;">
			<module:display name="/Activity Form/Line Ministry Observations/Regional Obsevation Field" parentModule="/Activity Form/Line Ministry Observations">
				<tr bgcolor="#F0F0F0">
				<td width="20%;">
					<b><digi:trn>Observation</digi:trn>:</b>
				</td>
				<td>
					<b><c:out value="${lineMinistryObs.name}"/></b>
				</td>	
				</tr>	
			</module:display>
			<module:display name="/Activity Form/Line Ministry Observations/Regional Obsevation Field/Date" parentModule="/Activity Form/Line Ministry Observations/Regional Obsevation Field">
				<tr bgcolor="#F0F0F0">
				<td>
					<digi:trn>Observation Date</digi:trn>:
					
				</td>
				<td>
					<c:out value="${lineMinistryObs.issueDate}"/>
				</td>
				</tr>
			</module:display>			
			<logic:iterate name="lineMinistryObs" id="measure" property="measures">
			<tr bgcolor="#F0F0F0">
			<td>
				<digi:trn>Measure</digi:trn>:
			</td>
			<td>	
				<c:out value="${measure.name}"/>
			</td>
			</tr>
			<c:if test="${not empty measure.actors }">
			<tr>
			<td colspan="2" bgcolor="#F0F0F0">
				<digi:trn>Actors</digi:trn>
			</td>
			</tr>
				<logic:iterate name="measure" id="actor" property="actors">
				<tr>
				<td>	
				
					<c:out value="${actor.name}"/>
				</td>
				</tr>
				</logic:iterate>
			</c:if>	
			</logic:iterate>
			</table>		
			<br />	
		</logic:iterate>
		</c:if>
	</div>
	</fieldset>
</module:display>