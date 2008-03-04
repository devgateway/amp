<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>



<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />


<digi:instance property="aimReportsFilterPickerForm" />

<digi:form action="/reportsFilterPicker.do">

	<html:hidden property="ampReportId" />
	<table align="center" cellpadding="1" cellspacing="1">
		<tr>
			<td height="30" colspan="4" style="font-size:14px"><b><digi:trn
				key="rep:pop:FilterReportName:">Filter Report:</digi:trn> <bean:write
				scope="session" name="reportMeta" property="name" /></b></td>
		</tr>



		<tr bgcolor="#EEEEEE">
			<td colspan="4"><font color=red><digi:trn
				key="rep:filter:SpecifyprojectsKeywords.">Specify keywords to look for in the project data.</digi:trn></font><br>
			</td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filter:Keywords">Keywords</digi:trn>
			</b> <html:text property="indexString" style="width: 300px"
				styleClass="inp-text" /></td>
		</tr>


		<tr bgcolor="#EEEEEE">
			<td colspan="4"><font color=red><digi:trn
				key="rep:filter:timePeriod">Specify the time period to limit your search within.</digi:trn></font><br>
			</td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<bean:define id="calendars" name="aimReportsFilterPickerForm"
				property="calendars" type="java.util.Collection" />
			<%
				    int size = ((java.util.Collection) calendars).size();
				    boolean condition = (size != 1);
				    if (condition) {
			%>
			<td><b><digi:trn key="rep:filer:fiscalCalendar">Fiscal Calendar</digi:trn></b></td>
			<%
			}
			%>
			<td align="center"><b><digi:trn key="rep:filer:FromYear">From Year</digi:trn></b></td>
			<td align="center"><b><digi:trn key="rep:filer:ToYear">To Year</digi:trn></b></td>
			<td>&nbsp;</td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<%
			if (condition) {
			%>
			<td><html:select property="calendar" style="width: 220px"
				styleClass="inp-text">
				<html:optionsCollection property="calendars" value="ampFiscalCalId"
					label="name" />
			</html:select></td>
			<%
			}
			%>
			<td align="center"><html:select property="fromYear"
				styleClass="inp-text">
				<html:option value="-1">
					<digi:trn key="rep:filer:All">All</digi:trn>
				</html:option>
				<html:optionsCollection property="fromYears" label="wrappedInstance"
					value="wrappedInstance" />
			</html:select></td>

			<td align="center"><html:select property="toYear"
				styleClass="inp-text">
				<html:option value="-1">
					<digi:trn key="rep:filer:All">All</digi:trn>
				</html:option>
				<html:optionsCollection property="toYears" label="wrappedInstance"
					value="wrappedInstance" />
			</html:select></td>
			<td>&nbsp;</td>
		</tr>

		<tr bgcolor="#EEEEEE">
			<td colspan="4"><font color=red><digi:trn
				key="rep:filter:financingDetails">Specify the financing details.</digi:trn></font><br>
			</td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filer:Currency">Currency</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><html:select property="currency"
				style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="currencies" value="ampCurrencyId"
					label="currencyName" />
			</html:select></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn
				key="rep:filer:financingInstrument">Financing Instrument</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="2"><category:showoptions size="3"
				outerstyle="width: 250px" styleClass="inp-text"
				name="aimReportsFilterPickerForm"
				property="selectedFinancingInstruments" multiselect="true"
				keyName="<%=org.digijava.module.aim.helper.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" />
			</td>
			<td colspan="2" valign="top"><field:display
				name="Joint Criteria" feature="Budget">
				<html:checkbox property="jointCriteria" value="true" /> &nbsp;<b><digi:trn
					key="rep:filter:jointCriteriaCheckDisplay">Display Only Projects Under Joint Criteria.</digi:trn></b>&nbsp;&nbsp;				</field:display>
			<br>
			<field:display name="Government Approval Procedures" feature="Budget">
				<html:checkbox property="governmentApprovalProcedures" value="true" />&nbsp;<digi:trn
					key="rep:filter:govAppProcCheck"> Display Only Projects Having Government Approval Procedures. </digi:trn>
			</field:display></td>
		</tr>


		<tr bgcolor="#EEEEEE">
			<td colspan="4"><font color=red><digi:trn
				key="rep:filter:regionandSector">Specify the region and sectors of interest.</digi:trn></font><br>
			</td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filter:Location">Region</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><html:select property="regionSelected"
				style="width: 400px" styleClass="inp-text">
				<html:option value="-1">
					<digi:trn key="rep:filer:All">All</digi:trn>
				</html:option>
				<html:optionsCollection property="regionSelectedCollection"
					label="region" value="regionId" />
			</html:select></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4"><b><digi:trn key="rep:filer:Sector">Sector</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="4" styleClass="inp-text"><html:select
				multiple="true" property="selectedSectors" size="3"
				style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="sectors" value="ampSectorId"
					label="name" />
			</html:select></td>
		</tr>


		<tr bgcolor="#EEEEEE">
			<td colspan="4"><font color=red><digi:trn
				key="rep:filter:DonorsandAgencies">Specify Donors and Cooperating Agencies.</digi:trn></font><br>
			</td>
		</tr>

		<%-- <tr>
			<td colspan="4"><b><digi:trn key="rep:filer:donor">Donor</digi:trn></b></td>
		</tr>
		<tr>
			<td colspan="4">
			<html:select multiple="true" property="selectedDonors" size="3" style="width: 400px" styleClass="inp-text">
				<html:optionsCollection property="donors"
					value="ampOrgGrpId" label="orgGrpName" />
			</html:select>
			</td>
		</tr>--%>
		<tr bgcolor="#EEEEEE">
			<td colspan="1"><b><digi:trn key="rep:filer:DonorType">Donor Type</digi:trn></b></td>
			<td colspan="3"><b><digi:trn key="rep:filer:DonorGroup">Donor Group</digi:trn></b></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td colspan="1"><html:select style="width: 190px"
				multiple="true" property="selectedDonorTypes" size="3"
				styleClass="inp-text">
				<html:optionsCollection property="donorTypes" value="ampOrgTypeId"
					label="orgType" />
			</html:select></td>
			<td colspan="3"><html:select multiple="true"
				property="selectedDonorGroups" size="3" styleClass="inp-text">
				<html:optionsCollection style="width: 195px" property="donorGroups"
					value="ampOrgGrpId" label="orgGrpName" />
			</html:select></td>
		</tr>
		<tr bgcolor="#EEEEEE">
         <td colspan="4">
			<feature:display name="Beneficiary Agency" module="Organizations">
				<table align="left" cellpadding="1" cellspacing="1">
                <tr>
                <td>
					<b><digi:trn key="rep:filer:beneficiaryAgency">Beneficiary Agency</digi:trn></b></td>
                </tr>
                <tr>
                  <td><html:select style="width: 190px"
						multiple="true" property="selectedBeneficiaryAgency" size="3"
						styleClass="inp-text">
					<html:optionsCollection property="beneficiaryAgency" label="name"
						value="ampOrgId" />
					</html:select></td>
                </tr>
                </table>
		   </feature:display>
			
			<feature:display name="Executing Agency" module="Organizations">
            	<table align="left" cellpadding="1" cellspacing="1">
                <tr>
				<td>
							<b><digi:trn key="rep:filer:executingAgency">Executing Agency</digi:trn></b></td>
                </tr>
                <tr>
                  <td><html:select style="width: 190px"
				multiple="true" property="selectedExecutingAgency" size="3"
				styleClass="inp-text">
			
						<html:optionsCollection property="executingAgency" label="name"
							value="ampOrgId" />
					</html:select></td>
                </tr>
              </table>
		   </feature:display>
			 
			<feature:display name="Implementing Agency" module="Organizations">
            <table cellpadding="1" cellspacing="1">
              <tr>
				<td >
					<b><digi:trn key="rep:filer:implementingAgency">Implementing Agency</digi:trn></b></td>
                </tr>
                <tr>
                  <td >	<html:select style="width: 190px" multiple="true"
					property="selectedImplementingAgency" size="3"
					styleClass="inp-text">
					<html:optionsCollection property="implementingAgency" label="name"
						value="ampOrgId" />
				</html:select></td>
                </tr>
              </table>
		   </feature:display>
          </td></tr>
		


		<tr bgcolor="#EEEEEE">
			<td colspan="4"><font color=red><digi:trn
				key="rep:filter:otherCriteria">Specify other criteria to filter with.</digi:trn></font><br>
			</td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td><b><digi:trn key="rep:filer:Status">Status</digi:trn></b></td>
			<field:display name="Risk" feature="Activity">
				<td><b><digi:trn key="rep:filer:Risks">Risks</digi:trn></b></td>
			</field:display>

			<td><field:display name="Line Ministry Rank" feature="Planning">
				<b><digi:trn key="rep:filer:LineMinRank">Line Min. Rank</digi:trn></b>
			</field:display></td>


			<td><field:display name="Planning Ministry Rank"
				feature="Planning">
				<b><digi:trn key="rep:filer:PlanningMinRank">Planning Min. Rank</digi:trn></b>
			</field:display></td>
		</tr>
		<tr bgcolor="#EEEEEE">
			<td valign="top"><category:showoptions
				outerstyle="width: 190px" styleClass="inp-text"
				property="selectedStatuses" size="3"
				name="aimReportsFilterPickerForm" multiselect="true"
				keyName="<%=org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY%>" />
			</td>
			<field:display name="Risk" feature="Activity">
				<td valign="top"><html:select multiple="true"
					property="selectedRisks" size="3" styleClass="inp-text">
					<html:optionsCollection property="risks"
						value="ampIndRiskRatingsId" label="ratingName" />
				</html:select></td>
			</field:display>
			<td valign="top"><field:display name="Line Ministry Rank"
				feature="Planning">
				<html:select property="lineMinRank" style="width: 50px"
					styleClass="inp-text">
					<html:option value="-1">
						<digi:trn key="rep:filer:All">All</digi:trn>
					</html:option>
					<html:optionsCollection property="actRankCollection"
						label="wrappedInstance" value="wrappedInstance" />
				</html:select>
			</field:display></td>


			<td valign="top"><field:display name="Planning Ministry Rank"
				feature="Planning">
				<html:select property="planMinRank" style="width: 50px"
					styleClass="inp-text">
					<html:option value="-1">
						<digi:trn key="rep:filer:All">All</digi:trn>
					</html:option>
					<html:optionsCollection property="actRankCollection"
						label="wrappedInstance" value="wrappedInstance" />
				</html:select>
			</field:display></td>
		</tr>


		<logic:notEqual name="widget" value="true" scope="request">

			<tr bgcolor="#EEEEEE">
				<td colspan="4"><font color=red><digi:trn
					key="rep:filter:pageSizeMesg">Specify the page size you want to format your report to print in.</digi:trn></font><br>
				</td>
			</tr>
			<tr bgcolor="#EEEEEE">
				<td colspan="4"><b><digi:trn key="rep:filter:pageSize"> Page Size</digi:trn></b></td>
			</tr>
			<tr bgcolor="#EEEEEE">
				<td colspan="4"><html:select property="pageSize"
					style="width: 100px" styleClass="inp-text">
					<html:optionsCollection property="pageSizes"
						label="wrappedInstance" value="wrappedInstance" />
				</html:select></td>
			</tr>
		</logic:notEqual>






		<tr>
			<td align="center" colspan="4"><html:submit styleClass="buton"
				property="apply">
				<digi:trn key="rep:filer:ApplyFiltersToReport">Apply Filters to the Report</digi:trn>
			</html:submit>&nbsp; <html:button onclick="resetFilter();" styleClass="buton"
				property="reset">
				<digi:trn key="rep:filer:ResetAndStartOver">Reset and Start Over</digi:trn>
			</html:button> <html:hidden property="defaultCurrency" /></td>
		</tr>
	</table>
</digi:form>
