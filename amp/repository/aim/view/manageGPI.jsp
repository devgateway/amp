<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>


<div style="margin: 0 auto; width: 1000px;">
	<h1 class="admintitle">
		<digi:trn key="aim:gpiManager">Global Partnership Indicators Manager</digi:trn>
	</h1>
	<digi:instance property="manageGPIForm" />	

	<jsp:include page="teamPagesHeader.jsp" />

	<digi:form action="/manageGPI.do?actionType=save" method="post" name="gpiForm">
		<bean:define id="donorTypes" property="selectedDonorTypes" name="manageGPIForm" type="java.util.Collection<String>"/>
		<h2><digi:trn>Default Organization Types</digi:trn></h2>
		<table bgColor=#ffffff cellpadding="0" cellspacing="0" class="box-border-nopadding" style="width: 90%;">
			<logic:iterate id="donorType" name="manageGPIForm" property="availableDonorTypes" type="org.digijava.ampModule.aim.dbentity.AmpOrgType">
				<tr>
					<td style="width: 1%;">
						<%
						boolean selected = false;
						if(donorTypes.contains(donorType.getAmpOrgTypeId().toString())) {
							selected = true;
						}
						%>
						<input type="checkbox" name="donorTypes_<%=donorType.getAmpOrgTypeId()%>" <%=(selected)?"checked='checked'":"" %>/>
					</td>
					<td style="width: 99%;">
						<span><%=donorType.getLabel()%></span>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<hr>
		<h2><digi:trn>Indicator Descriptions</digi:trn></h2>
		<table bgColor=#ffffff cellpadding="0" cellspacing="0" class="box-border-nopadding" style="width: 90%;">
			<logic:iterate id="indicator" name="manageGPIForm" property="indicators" type="org.digijava.ampModule.aim.dbentity.AmpGPISurveyIndicator">
				<logic:equal value="true" property="showAsIndicator" name="indicator">
					<tr>
						<td style="width: 30px;">
							<p><digi:trn><%=indicator.getName()%>: </digi:trn></p>
						</td>
						<td style="width: 300px;">
							<textarea name="indicator_<%=indicator.getAmpIndicatorId()%>" id="indicator_<%=indicator.getAmpIndicatorId()%>" rows="2" cols="50"><%= indicator.getDescription()%></textarea>
						</td>
					</tr>
				</logic:equal>
			</logic:iterate>
		</table>
		<hr>
		<h2><digi:trn>Indicator Fields</digi:trn></h2>		
		<table cellpadding="1" cellspacing="1">
			<tr>
				<td>
					<strong><digi:trn>Indicator 5a: Development Cooperation is more predictable.</digi:trn></strong>
					<p><digi:trn>This indicator is calculated by dividing the sum of actual disbursements for a given year by the sum of scheduled disbursements.</digi:trn></p> 
					<label><digi:trn>Field to Use for Actual Disbursements:</digi:trn></label> 
					<!-- TODO: Translates the content -->
					<html:select property="indicator5aActualDisbursement">
						<html:optionsCollection property="measures" value="key" label="value" />
					</html:select>														
				</td>
			</tr>
			<tr>
				<td>	
					<label><digi:trn>Field to Use for Planned Disbursements:</digi:trn></label>
					<html:select property="indicator5aPlannedDisbursement">
						<html:optionsCollection property="measures" value="key" label="value" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<strong><digi:trn>Indicator 6: Aid is on budgets which are subject to parliamentary scrutiny.</digi:trn></strong>
					<p><digi:trn>This indicator is calculated using the scheduled disbursements for a given year</digi:trn>.</p> 
					<label><digi:trn>Field to Use for Scheduled Disbursements:</digi:trn></label> 
					<html:select property="indicator6ScheduledDisbursements">
						<html:optionsCollection property="measures" value="key" label="value" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<strong><digi:trn>Indicator 9b: Use of developing country PFM and procurement systems.</digi:trn></strong>
					<p><digi:trn>This indicator is calculated by comparing disbursements which use country systems with total disbursements.</digi:trn></p> 
					<label><digi:trn>Field to Use for Disbursements:</digi:trn></label> 
					<html:select property="indicator9bDisbursements">
						<html:optionsCollection property="measures" value="key" label="value" />
					</html:select>
				</td>
			</tr>
		</table>
		<input type="submit" name="saveChanges" id="saveChanges" value="<digi:trn key='buttonSaveGPIManager'>Save Changes</digi:trn>"/>
	</digi:form>
</div>