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

	<digi:form action="/manageGPI.do?actionType=save" method="post">

		<table cellPadding=5 cellspacing="0" width="100%">
			<tr>
				<td height=16 align="center" vAlign="middle"><span class=subtitle-blue><b></b></span></td>
			</tr>
			<tr>
				<td noWrap width=650 vAlign="top">
					<table bgColor=#ffffff cellpadding="0" cellspacing="0" class="box-border-nopadding">
						<tr bgColor=#f4f4f2>
							<td>&nbsp;</td>
						</tr>
						<tr bgColor=#f4f4f2>
							<td valign="top">
								<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0">
									<tr>
										<td bgColor=#ffffff class=box-border>
											<table border="0" cellPadding=3 cellSpacing=5 class=box-border>
												<tr>
													<td>
														<h3>Indicator 5a: Development Cooperation is more predictable.</h3>
														<p>This indicator is calculated by dividing the sum of actual disbursements for a given year by the sum of scheduled disbursements.</p> 
														<label>Field to Use for Actual Disbursements:</label> 
														<html:select property="indicator5aActualDisbursement">
															<html:optionsCollection property="measures" value="measureId" label="measureName" />
														</html:select>
													</td>
												</tr>
												<tr>
													<td>	
														<label>Field to Use for Planned Disbursements:</label>
														<html:select property="indicator5aPlannedDisbursement">
															<html:optionsCollection property="measures" value="measureId" label="measureName" />
														</html:select>
													</td>
												</tr>
												<tr>
													<td>
														<h3>Indicator 6: Aid is on budgets which are subject to parliamentary scrutiny.</h3>
														<p>This indicator is calculated using the scheduled disbursements for a given year.</p> <label>Field to Use for Scheduled Disbursements:</label> 
														<html:select property="indicator6ScheduledDisbursements">
															<html:optionsCollection property="measures" value="measureId" label="measureName" />
														</html:select>
													</td>
												</tr>
												<tr>
													<td>
														<h3>Indicator 9b: Use of developing country PFM and procurement systems.</h3>
														<p>This indicator is calculated by comparing disbursements which use country systems with total disbursements.</p> <label>Field to Use for
															Disbursements:</label> 
														<html:select property="indicator9bDisbursements">
															<html:optionsCollection property="measures" value="measureId" label="measureName" />
														</html:select>
													</td>
												</tr>
												<tr>
													<td><input type="submit" name="saveChanges" id="saveChanges" value="<digi:trn key='buttonSaveGPIManager'>Save Changes</digi:trn>"/></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td bgColor=#f4f4f2>&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

	</digi:form>
</div>