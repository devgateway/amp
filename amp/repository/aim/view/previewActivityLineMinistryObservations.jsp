<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/moduleVisibility.tld" prefix="module"%>


<module:display name="/Activity Form/Line Ministry Observations" parentModule="/Activity Form">
	<fieldset>
	<legend>
		<span class=legend_label id="lineMinistryObsLink" style="cursor: pointer;">
            <digi:trn>Line Ministry Observations</digi:trn>
        </span>
    </legend>
		
	<div id="lineMinistryObsDiv" class="toggleDiv">
		<c:if test="${not empty aimEditActivityForm.lineMinistryObservations.issues}">
		<logic:iterate name="aimEditActivityForm" id="lineMinistryObs" property="lineMinistryObservations.issues">
			<table style="width: 100%;">
			<module:display name="/Activity Form/Line Ministry Observations/Observation" parentModule="/Activity Form/Line Ministry Observations">
				<tr bgcolor="#F0F0F0">
				<td width="20%;">
					<b><digi:trn>Observation</digi:trn>:</b>
				</td>
				<td>
					<span class="word_break bold"><c:out value="${lineMinistryObs.name}"/></span>
				</td>	
				</tr>	
			</module:display>
			<module:display name="/Activity Form/Line Ministry Observations/Observation/Date" parentModule="/Activity Form/Line Ministry Observations/Observation">
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
				<span class="word_break "><c:out value="${measure.name}"/></span>
			</td>
			</tr>
			<c:if test="${not empty measure.actors }">
			<tr>
				<td bgcolor="#F0F0F0">
					<digi:trn>Actors</digi:trn>
				</td>
				<td bgcolor="#F0F0F0">
					<table style="border-collapse:collapse" cellpadding="0" cellspacing="0" widht="100%">
						<logic:iterate name="measure" id="actor" property="actors">
							<tr>
							<td bgcolor="#F0F0F0">	
								<span class="word_break bold"><c:out value="${actor.name}"/></span>
							</td>
							</tr>
							</logic:iterate>
						</table>	
				</td>
			</tr>
			</c:if>	
			</logic:iterate>
			</table>
			<br />
		</logic:iterate>
		</c:if>
	</div>
	</fieldset>
</module:display>