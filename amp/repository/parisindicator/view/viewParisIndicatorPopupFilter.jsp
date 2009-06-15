<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>

<digi:instance property="parisIndicatorForm" />

<digi:form action="/parisindicator.do?reportId=${parisIndicatorForm.piReport.indicatorCode}">
	<html:hidden property="piReport.indicatorCode"/>
	<div id="tabview_container" class="yui-navset">
		<ul class="yui-nav">
			<li class="selected">
				<a href="#keyword"><div><digi:trn key="aim:filter:tab:Calendar">Calendar</digi:trn></div></a>
			</li>
			<li>
				<a href="#financing"><div><digi:trn key="aim:filter:tab:Financing">Financing & Indicators</digi:trn></div></a> 
			</li>
			<li>
				<a href="#sectorsgroups"><div><digi:trn key="aim:filter:tab:SectorsGroups">Sectors & Groups</digi:trn></div></a>
			</li>
			<li>
				<a href="#donorstatus"><div><digi:trn key="aim:filter:tab:DonorsStatus">Donors & Status</digi:trn></div></a>
			</li>
		</ul>
		<div class="yui-content" style="background-color: #EEEEEE">
			<div id="keyword" >
				<br />
					<table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
						<tr valign="top">
							<td align="center">
								<c:set var="tooltip_translation">
										<digi:trn key="rep:filter:timePeriod">Specify the time period to limit your search within.</digi:trn>
								</c:set>
								<table align="center" cellpadding="2" cellspacing="2" onmouseout="UnTip()" onmouseover="Tip('${tooltip_translation}');">
									<tr bgcolor="#EEEEEE">
										<td colspan="5">
											<b><digi:trn key="rep:filter:CalendarTitle">Calendar</digi:trn></b>
										</td>
										<td colspan="4">
											<html:select property="selectedCalendar" name="parisIndicatorForm" styleClass="dr-menu" >
												<logic:notEmpty name="parisIndicatorForm" property="calendars">
													<html:optionsCollection name="parisIndicatorForm" property="calendars" value="ampFiscalCalId" label="name"/>
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr>
										<td colspan="5">
											<b><digi:trn key="rep:filter:StartYear">Start Year</digi:trn></b>&nbsp;
										</td>
										<td colspan="4">
											<bean:define id="syear" property="selectedStartYear" name="parisIndicatorForm" />
											<select name="startYear" value="<%=syear%>" class="dr-menu" onchange="chkYear('start')">

											</select>
										</td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr>
										<td colspan="5">
											<b><digi:trn key="rep:filter:StartYear">End Year</digi:trn></b>&nbsp;
										</td>
										<td colspan="4">
											<bean:define id="cyear" property="selectedEndYear" name="parisIndicatorForm"/>
											<select name="closeYear" value="<%=cyear%>" class="dr-menu" onchange="chkYear('close')">

											</select>
										</td>
									</tr>
								</table>
							</td> 
						</tr>
						<tr bgcolor="#EEEEEE">
							<td colspan="5" styleClass="inp-text">&nbsp;<br/><br/></td>
						</tr>
				</table>
			</div>
			<div id="financing">
				<br />
					<table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
						<tr valign="top">
							<td align="center">
								<table align="center" cellpadding="2" cellspacing="2">
									<tr bgcolor="#EEEEEE">
										<td colspan="5">
											<b><digi:trn key="rep:pop:CurrencyType">Currency Type:</digi:trn></b>
										</td>
									</tr>
									<tr bgcolor="#EEEEEE">
										<td colspan="4">
											<html:select property="selectedCurrency" name="parisIndicatorForm" styleClass="dr-menu" >
												<logic:notEmpty name="parisIndicatorForm" property="currencyTypes">
													<html:optionsCollection name="parisIndicatorForm" property="currencyTypes" value="currencyCode" label="currencyName"/>
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr bgcolor="#EEEEEE">
										<td colspan="5"><b><digi:trn key="rep:filer:financingInstrument">Financing Instrument</digi:trn></b></td>
									</tr>
									<tr bgcolor="#EEEEEE">
										<td colspan="5">

										</td>
									</tr>
								</table>
							</td> 
						</tr>
						<tr bgcolor="#EEEEEE">
							<td colspan="5" styleClass="inp-text">&nbsp;<br/><br/></td>
						</tr>
				</table>
			</div>
			<div id="sectorsgroups">
				<br />
					<table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
						<tr valign="top">
							<td align="center">
								<table align="center" cellpadding="2" cellspacing="2">
									<tr bgcolor="#EEEEEE">
										<td colspan="5"><b><digi:trn key="rep:pop:sectors">Sectors:</digi:trn></b></td>
										<td>&nbsp;</td>
										<td colspan="5"><b><digi:trn key="rep:pop:Groups">Groups:</digi:trn></b></td>
									</tr>
									<tr bgcolor="#EEEEEE">
										<td colspan="5" styleClass="inp-text">
											<html:select  property="selectedSectors" size="8" style="width: 300px" styleClass="inp-text">
												<html:option value="All"><digi:trn key="aim:allSectors">All Sectors</digi:trn></html:option>
												<html:optionsCollection property="sectors" value="ampSectorId" label="name" />
											</html:select>
										</td>
										<td>&nbsp;</td>									
										<td colspan="5" styleClass="inp-text">
											<html:select property="selectedDonorGroups" name="parisIndicatorForm" styleClass="dr-menu"  size="8">
												<html:option value="all"><digi:trn key="aim:allGroups">All Groups</digi:trn></html:option>
												<logic:notEmpty name="parisIndicatorForm" property="donorGroups">
													<html:optionsCollection name="parisIndicatorForm" property="donorGroups" value="orgGrpCode" label="orgGrpName"/>
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
								</table>
							</td> 
						</tr>
						<tr bgcolor="#EEEEEE">
							<td colspan="5" styleClass="inp-text">&nbsp;<br/><br/></td>
						</tr>
				</table>
			</div>
			<div id="donorstatus">
				<br />
					<table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
						<tr valign="top">
							<td align="center">
								<table align="center" cellpadding="2" cellspacing="2">
									<tr bgcolor="#EEEEEE">
										<td colspan="5"><b><digi:trn key="rep:pop:Donors">Donors:</digi:trn></b></td>
										<td>&nbsp;</td>
										<td colspan="5"><b><digi:trn key="rep:pop:StatusType">Status Type:</digi:trn></b></td>
									</tr>
									<tr bgcolor="#EEEEEE">
										<td colspan="5" styleClass="inp-text">
											<html:select  property="selectedDonors" size="6" style="width: 300px" styleClass="inp-text">
												<html:option value="all"><digi:trn key="aim:allDonors">All Donors</digi:trn></html:option>
												<html:optionsCollection property="donors" value="ampOrgId" label="acronym" />
											</html:select>
										</td>
										<td>&nbsp;</td>									
										<td colspan="5" styleClass="inp-text">

										</td>
									</tr>
								</table>
							</td> 
						</tr>
						<tr bgcolor="#EEEEEE">
							<td colspan="5" styleClass="inp-text">&nbsp;<br/><br/></td>
						</tr>
				</table>
			</div>
		</div>
	</div>
	<div style="background-color: #EEEEEE; ">
		<br />
		<table width="100%">
			<tr>
				<td align="center" colspan="5">
					<input type="submit" value="<digi:trn key="rep:filer:ApplyFiltersToReport">Apply Filters to the Report</digi:trn>" class="dr-menu" onclick="hideFilter();clearFilter();submit();">
				</td>
			</tr>
		</table>
	</div>
<!--</digi:form>-->