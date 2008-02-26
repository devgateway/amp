<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>



<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />


<digi:instance property="aimReportsFilterPickerForm"/>

<digi:form action="/reportsFilterPicker.do">

<html:hidden property="ampReportId"/>
	<table>
		<tr>
			<td colspan="4" align="center">
			<font size="+1">
				<digi:trn key="rep:popFilterReportName">Filter </digi:trn> "<bean:write scope="session" name="reportMeta" property="name" />"
			</font>
			</td>
		</tr>
		<tr><td colspan="4"><i><digi:trn key="rep:filter:multipleSelect">Note: to select multiple items in a list, hold down the ctrl key while clicking.</digi:trn></i></td></tr>
		<tr><td colspan="4" height="10">&nbsp;</td></tr>		
		<tr bgcolor="#EEEEEE">
			<td colspan="4">		
			<b><font color="red"><digi:trn key="aim:chanheFilters:SpecifyprojectID">Specify the Project ID. The ID must be numeric.</digi:trn></font></b>
			<br><b><digi:trn key="rep:filter:projectid">Project ID</digi:trn> </b>
		</td>			
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4">
			<html:text  property="text" style="width: 400px" styleClass="inp-text" onchange="checkProjectId(this)"/>
			</td>
		</tr>
		
		<tr><td colspan="4" height="10">&nbsp;</td></tr>
		<tr bgcolor="#EEEEEE"><td colspan="4" height="15"><b><font color=red><digi:trn key="rep:filter:SpecifytheProjectTimePeriod">Specify the Project Time-period</digi:trn></font></b></td></tr>
		
		<tr bgcolor="#EEEEEE">
			<bean:define id="calendars" name="aimReportsFilterPickerForm" property="calendars" type="java.util.Collection"/>
			<% int size =  ((java.util.Collection)calendars).size();
			   boolean condition = (size != 1);
			   if (condition){%>
			<td colspan="2"><b><digi:trn key="rep:filer:fiscalCalendar">Fiscal Calendar</digi:trn></b></td>
			<% } %>
			<td align="center"><b><digi:trn key="rep:filer:FromYear">From Year</digi:trn></b></td>
			<td align="center"><b><digi:trn key="rep:filer:ToYear">To Year</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<% if (condition){%>
			<td colspan="2">
			<html:select property="calendar" style="width: 270px" styleClass="inp-text">
				<html:optionsCollection property="calendars"
					value="ampFiscalCalId" label="name" />
			</html:select>
			</td>
			<% } %>
			<td align="center">
			<html:select property="fromYear" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="fromYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>

			<td align="center">
			<html:select property="toYear" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="toYears" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
		</tr>

		<tr><td colspan="4" height="15">&nbsp;</td></tr>
		<tr bgcolor="#EEEEEE"><td colspan="4" height="15"><b><font color=red><digi:trn key="rep:filter:FinanceCriteria">Specify the Financing Options</digi:trn></font></b></td></tr>		
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filer:Currency">Currency</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4">
			<html:select property="currency" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="currencies"
					value="ampCurrencyId" label="currencyName" />

			</html:select>
			</td>
		</tr>


		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filer:financingInstrumenst">Financing Instruments</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4">
			<category:showoptions size="3" outerstyle="width: 250px" styleClass="inp-text" name="aimReportsFilterPickerForm" property="selectedFinancingInstruments" multiselect="true" keyName="<%=org.digijava.module.aim.helper.CategoryConstants.FINANCING_INSTRUMENT_KEY %>"/>
			</td>
		</tr>
	
	<field:display name="Government Approval Procedures" feature="Budget">
		<t bgcolor="#EEEEEE"r>
			<td colspan="4">
				<b><digi:trn key="rep:filter:govAppProc?"> Government Approval Procedures? </digi:trn></b>
			</td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4" valign="top" align="left">
				<html:select property="governmentApprovalProcedures" style="width: 400px" styleClass="inp-text">
					<html:option value="true"><digi:trn key="rep:filter:Yes">Yes</digi:trn></html:option>
					<html:option value="false"><digi:trn key="rep:filter:No">No</digi:trn></html:option>
				</html:select>			
			</td>
		</tr>
	</field:display>

	<field:display name="Joint Criteria" feature="Budget">
		<tr bgcolor="#EEEEEE">
			<td colspan="4">
				<digi:trn key="rep:filter:jointCriteria?"><b>Joint Criteria?</b></digi:trn>
			</td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4" valign="top" align="left">
				<html:select property="jointCriteria" style="width: 400px" styleClass="inp-text">
					<html:option value="Yes"><digi:trn key="rep:filter:Yes">Yes</digi:trn></html:option>
					<html:option value="No"><digi:trn key="rep:filter:No">No</digi:trn></html:option>
				</html:select>
			</td>
		</tr>
	</field:display>
		
		<tr><td colspan="4" height="10">&nbsp;</td></tr>
		<tr bgcolor="#EEEEEE"><td colspan="4" height="15"><b><font color=red><digi:trn key="rep:filter:SpecifyDonorsSectorsRegion">Specify Donors, Sectors and Region</digi:trn></font></b></td></tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filer:donosr">Donors</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4">
			<html:select multiple="true" property="selectedDonors" size="3" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="donors"
					value="ampOrgGrpId" label="orgGrpName" />
			</html:select>
			</td>
		</tr>		
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filer:Sectors">Sectors</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4" styleClass="inp-text">
			<html:select multiple="true" property="selectedSectors" size="3" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="sectors"
					value="ampSectorId" label="name" />
			</html:select>
			</td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filter:Location">Region</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4">
			<html:select property="regionSelected" style="width: 400px" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="regionSelectedCollection" label="region" value="regionId" />
			</html:select>
			</td>
		</tr>		

		<tr><td colspan="4" height="10">&nbsp;</td></tr>
		<tr bgcolor="#EEEEEE"><td colspan="4" height="15"><b><font color=red><digi:trn key="rep:filter:otherFilterCriteria">Specify Other Filter Criteria</digi:trn></font></b></td></tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="2"><b><digi:trn key="rep:filer:projectStatus">Project Status</digi:trn></b></td>
			<field:display name="Risk" feature="Activity">
			<td colspan="2"><b><digi:trn key="rep:filer:projectRisks">Project Risks (hold down the ctrl key and click multiple risks to select)</digi:trn></b></td>
			</field:display>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td valign="top" colspan="2">
					<category:showoptions outerstyle="width: 190px" styleClass="inp-text" property="selectedStatuses" size="3" name="aimReportsFilterPickerForm" multiselect="true" keyName="<%=org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY%>"/>
			</td>
			<field:display name="Risk" feature="Activity">
			<td valign="top"colspan="2" >
			<html:select multiple="true" property="selectedRisks" size="3" styleClass="inp-text">
				<html:optionsCollection property="risks"
					value="ampIndRiskRatingsId" label="ratingName" />
			</html:select>
			</td>
			</field:display>
		</tr>
		
		<tr bgcolor="#EEEEEE">		
			<field:display  name="Line Ministry Rank" feature="Planning">
			<td colspan="2">
			<b><digi:trn key="rep:filer:LineMinRank">Line Min. Rank</digi:trn></b>
			</td>
			</field:display>
			<field:display  name="Planning Ministry Rank" feature="Planning">
			<td colspan="2">
			<b><digi:trn key="rep:filer:PlanningMinRank">Planning Min. Rank</digi:trn></b>
			</td>
			</field:display>
		</tr>
		<tr bgcolor="#EEEEEE">		
		<field:display  name="Line Ministry Rank" feature="Planning">
			<td valign="top"colspan="2" >
			<html:select property="lineMinRank" style="width: 50px" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="actRankCollection" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
			</field:display>
			<field:display  name="Planning Ministry Rank" feature="Planning">
			<td valign="top" colspan="2">
			<html:select property="planMinRank" style="width: 50px" styleClass="inp-text">
				<html:option value="-1"><digi:trn key="rep:filer:All">All</digi:trn></html:option>
				<html:optionsCollection property="actRankCollection" label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
			</field:display>
		</tr>			

		<tr><td colspan="4" height="10">&nbsp;</td></tr>
		<tr bgcolor="#EEEEEE"><td colspan="4" height="15"><b><font color=red><digi:trn key="rep:filter:formatReportSize">Format Report Size to Fit</digi:trn></font></b></td></tr>
<logic:notEqual name="widget" value="true" scope="request">
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filter:pageSize"> Page Size</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4">
			<html:select property="pageSize" style="width: 100px" styleClass="inp-text">
				<html:optionsCollection property="pageSizes"
					 label="wrappedInstance" value="wrappedInstance" />
			</html:select>
			</td>
		</tr>
</logic:notEqual>	
	<tr><td colspan="4">&nbsp;</td></tr>

	<tr>
	<td align="center"  colspan="4">
	
	<html:submit styleClass="buton" property="apply"><digi:trn key="rep:filer:Apply">Apply</digi:trn></html:submit>&nbsp;
	
	<html:button  onclick="resetFilter();" styleClass="buton"  property="reset"><digi:trn key="rep:filer:Reset">Reset</digi:trn></html:button>
	
	<html:hidden  property="defaultCurrency"/>
		
	 
	</td>

	</tr>
	</table>
</digi:form>
