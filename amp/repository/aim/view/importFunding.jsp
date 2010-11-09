<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>

<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>

<body >

<digi:instance property="aimEditActivityForm" />
<digi:form action="/importFundingDetail.do" type="aimEditActivityForm" name="aimEditActivityFormPopin" method="post" enctype="multipart/form-data" >
<input type="hidden" name="edit" value="true">
<html:hidden name="aimEditActivityForm" styleId="dupFunding"  property="funding.dupFunding"/>
<html:hidden name="aimEditActivityForm" styleId="event" property="funding.event"/>
<html:hidden name="aimEditActivityForm" styleId="transIndexId" property="funding.transIndexId"/>
<html:hidden name="aimEditActivityForm" styleId="numComm" property="funding.numComm" />
<html:hidden name="aimEditActivityForm" styleId="numDisb" property="funding.numDisb"/>
<html:hidden name="aimEditActivityForm" styleId="numExp" property="funding.numExp"/>
<html:hidden name="aimEditActivityForm" styleId="numDisbOrder" property="funding.numDisbOrder"/>
<html:hidden name="aimEditActivityForm" styleId="numProjections" property="funding.numProjections"/>
<html:hidden name="aimEditActivityForm" property="editAct"/>
<html:hidden name="aimEditActivityForm" property="funding.firstSubmit"/>
<html:hidden name="aimEditActivityForm" styleId="totDisbIsBiggerThanTotCom" property="totDisbIsBiggerThanTotCom"/>


<input type="hidden" name="funding.isEditFunding" value="true"/>
<style>
A.required_field
{
		color: #FF0000 !important;
		font-weight: bold;
}
DIV.red_notice
{
		color: #FF0000;	
		font-weight: bold;
		height:20px;
		border:1px solid black;
		padding:6px 8px 0px 8px;
		
}

.header_row TD A {
	color: #333333;	
}
.header_row TD A:hover {
	color: #006699;	
	background-color:#cecece;
}

.table_rows TD INPUT, .table_rows TD SELECT, .table_rows TD TEXTAREA {
	border:1px solid #bbbbbb !important;
}


</style>


<c:set var="errors"><digi:errors/></c:set>
${errors}

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
	<!-- funding -->
	<tr>
    	<td width="100%" vAlign="top">

			<table width="100%" cellpadding="0" cellspacing="1" vAlign="top" align="left" bgcolor="#006699">
			<tr><td>

			<table width="100%" cellPadding="1" cellspacing="0" bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="left">
						<digi:trn key="aim:ImportFunding">Import Funding</digi:trn>
					</td>
				</tr>
				<tr>
					<td vAlign="top" align="center" width="100%">
<c:if test="${empty aimEditActivityForm.funding.fundingDetails}">
						<table cellpadding="0" cellspacing="1" bgcolor="#ffffff" width="100%">
							<tr>
								<td>
				                	<b><digi:trn key="aim:organization">Organization</digi:trn></b>
								</td>
								<td>
				                	<bean:write name="aimEditActivityForm" property="funding.orgName"/>
								</td>
								<td>
			                	<b>
										<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">
										<font color="black">
										<digi:trn key="aim:fundingOrgId">
										Funding Organization Id</digi:trn></font></a>
									</b>
								</td>
								<td>
									<c:set var="contentDisabled"><field:display name="Funding Organization Id" feature="Funding Information">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
									<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation.
									This item may be useful when one project has two or more different financial instruments.
									If the project has a unique financial operation, the ID can be the same as the project ID
									</digi:trn>">
			   	             		<html:text  styleId="orgFundingId" property="funding.orgFundingId" size="10" disabled="${contentDisabled}"/>  </a>
								</td>
							</tr>
							<tr>
								<td>
									<a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>" class="required_field">
											<digi:trn key="aim:typeOfAssistance">Type of Assistance</digi:trn>
									</a>
								</td>
								<td>
									<c:set var="translation">
										<digi:trn key="aim:addActivityTypeOfAssistenceFirstLine">Please select from below</digi:trn>
									</c:set>
									
									<bean:define id="contentDisabled">false</bean:define>
									<c:set var="contentDisabled"><field:display name="Type Of Assistance" feature="Funding Information">false</field:display></c:set>
									<c:if test="${contentDisabled==''}">
										<c:set var="contentDisabled">true</c:set>
									</c:if>
									<c:if test="${contentDisabled=='false'}">
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm"   property="funding.assistanceType"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.TYPE_OF_ASSISTENCE_KEY %>" styleClass="inp-text"/>
									</c:if>
									
									<c:if test="${contentDisabled=='true'}">
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm"   property="funding.assistanceType"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.TYPE_OF_ASSISTENCE_KEY %>" styleClass="inp-text" outerdisabled="true"/>
									</c:if>
								</td>
								<td>
									<a title="<digi:trn key="aim:FinanceInstrument">Method by which aid is delivered to an activity</digi:trn>" class="required_field">
									<digi:trn key="aim:financingInstrument">Financing Instrument</digi:trn>
									</a>
								<td>
								<c:set var="contentDisabled"><field:display name="Financing Instrument" feature="Funding Information">false</field:display></c:set>
								<c:if test="${contentDisabled==''}">
									<c:set var="contentDisabled">true</c:set>
								</c:if>
								<c:if test="${contentDisabled=='false'}">
									<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="funding.modality" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" styleClass="inp-text" />
								</c:if>
								
								<c:if test="${contentDisabled=='true'}">
									<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="funding.modality" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" styleClass="inp-text"  outerdisabled="true"/>
								</c:if>
								</td>
							</tr>
							<field:display name="Funding Status" feature="Funding Information">
							<tr>
								<td>
									<a title="<digi:trn>The status of the funding</digi:trn>" class="required_field">
				                			<digi:trn>Funding Status</digi:trn>
									</a>
								</td>
								<td>
									<c:set var="translation">
										<digi:trn>Please select from below</digi:trn>
									</c:set>
									<category:showoptions firstLine="${translation}" name="aimEditActivityForm"   property="funding.fundingStatus"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.FUNDING_STATUS_KEY %>" styleClass="inp-text"/>
								</td>
								<td>
								</td>
								<td>
								</td>
							</tr>
							</field:display>
						</table>
					</td>
				</tr>
			</table>
			</td></tr>
            </table>

		</td>
	</tr>
	<tr>
	<td>
		<table cellpadding=4 cellspacing="1" bgcolor="#ffffff" width="100%">
			<tr>
				<td align="right" bgcolor="#ECF3FD" width="50%">
					<b><font color=black><digi:trn>CSV File to import:</digi:trn></font></b>
				</td>
				<td align="left" bgcolor="#ECF3FD" width="50%">
					<html:file property="fileImport" />
				</td>
			</tr>
			<tr>
				<td align="center" bgcolor="#ECF3FD" colspan="2">
					<div onclick="javascript:document.location = '/TEMPLATE/ampTemplate/imagesSource/samples/fundingImportSample.csv'" style="cursor:pointer"><img src="/TEMPLATE/ampTemplate/imagesSource/common/help.gif">&nbsp;&nbsp;&nbsp;<b><font color=black><digi:trn>Click here to download a sample of CSV file</digi:trn></font></b></a>
				</td>
			</tr>
		</table>
	</td>
  </tr>
    <c:set var="conditionsActivated"><field:display name="Conditions for Fund Release" feature="Funding Information">true</field:display></c:set>
	<c:if test="${conditionsActivated==''}">
		<c:set var="conditionsActivated">false</c:set>
	</c:if>
    <c:set var="objectiveActivated"><field:display name="Donor Objective" feature="Funding Information">true</field:display></c:set>
	<c:if test="${objectiveActivated==''}">
		<c:set var="objectiveActivated">false</c:set>
	</c:if>

    <c:choose>
    	<c:when test="${conditionsActivated == 'true' && objectiveActivated == 'true'}">
    		<c:set var="titleObjectiveConditions"><digi:trn>Objective and Conditions</digi:trn></c:set>
    	</c:when>
    	<c:when test="${conditionsActivated == 'true' && objectiveActivated == 'false'}">
    		<c:set var="titleObjectiveConditions"><digi:trn>Conditions</digi:trn></c:set>
    	</c:when>
    	<c:when test="${conditionsActivated == 'false' && objectiveActivated == 'true'}">
    		<c:set var="titleObjectiveConditions"><digi:trn>Objective</digi:trn></c:set>
    	</c:when>
    	<c:when test="${conditionsActivated == 'false' && objectiveActivated == 'false'}">
    		<c:set var="titleObjectiveConditions"></c:set>
    	</c:when>
    </c:choose>
    <tr>
		<td>
		<table width="100%" cellpadding="0" cellspacing="1" vAlign="top" align="left">
			<tr><td align="center">

			<c:if test="${!empty titleObjectiveConditions}">
			<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20">&nbsp;
						${titleObjectiveConditions}
						<img src="/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" align="absmiddle" hspace="5" title="${titleObjectiveConditions}"/>
					</td>
				</tr>
			</table>
			<br/>
		    </c:if>
			<field:display name="Donor Objective" feature="Funding Information">
			<table width="100%" cellspacing="1" cellPadding="1" bgcolor="#ffffff">
				<tr class="table_rows">
					<td align="left" valign="top" width="150">
						<b>
						<a title="<digi:trn key="aim:DonorObjectiveforFundRelease">Enter the donor objective attached to the release of the funds</digi:trn>">
						<font color=black>	<digi:trn key="aim:donorobjective">Donor Objective</digi:trn></font></b></a>
					</td>
					<td align="left">
					<a title="<digi:trn key="aim:DonorObjectiveforFundRelease">Enter the donor objective attached to the release of the funds</digi:trn>">
						<html:textarea property="funding.donorObjective" rows="3" cols="75" styleClass="inp-text"/>
					</a>
					</td>
				</tr>
			</table>
			</field:display>
		    <field:display name="Conditions for Fund Release" feature="Funding Information">
			<table width="100%" cellspacing="1" cellPadding="1">
				<tr class="table_rows">
					<td align="left" valign="top" width="150">
						<b>
						<a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">
						<font color=black><digi:trn key="aim:conditions">Conditions</digi:trn></font></a></b>
					</td>
					<td align="left">
					<a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">
						<html:textarea property="funding.fundingConditions" rows="3" cols="75" styleClass="inp-text"/>
					</a>
					</td>
				</tr>
			</table>
			</field:display>
            </td></tr>
        </table>
		</td>
	</tr>
</c:if>
<c:if test="${!empty aimEditActivityForm.funding.fundingDetails}">
<feature:display module="Funding" name="Commitments">
	<tr>
		<td width="100%" vAlign="top">

			<table width="100%" cellpadding="0" cellspacing="1" vAlign="top" align="left">
			<tr><td align="center">
			<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20">
					&nbsp;<digi:trn key="aim:commitments">Commitments</digi:trn>
					<img src="/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" align="absmiddle" hspace="5" title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient</digi:trn>"/>
					</td>
				</tr>
			</table>
			<br>
			<table width="98%" cellPadding="1" cellspacing="0">
				<tr class="textalb" align="center">
					<td>
						<table width="100%" border="0" cellspacing="2" cellpadding="1">
							<c:if test="${empty aimEditActivityForm.funding.commitmentsDetails}">
							<tr class="header_row">
								<td align="center" valign="middle">
								<digi:trn>No commitments found</digi:trn>
								</td>
							</tr>
							</c:if>
							<c:if test="${ !empty aimEditActivityForm.funding.commitmentsDetails}">
							<tr bgcolor="#cecece" class="header_row">
								<field:display name="Adjustment Type Commitment" feature="Commitments">
								<td align="center" valign="middle">
									<b><digi:trn key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn></b>
								</td>
								</field:display>
								<field:display name="Amount Commitment" feature="Commitments">
								<td align="center" valign="middle">
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><digi:trn key="aim:AmountFIE">Amount</digi:trn></b></a>
									</gs:test>
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommittedNoThousand">Full amount of expected transfer, irrespective of the time required for the completion of disbursements.</digi:trn>">
									<b><digi:trn key="aim:AmountFIE">Amount</digi:trn></b></a>
									</gs:test>
								</td>
								</field:display>
								<field:display name="Currency Commitment" feature="Commitments">
								<td align="center" valign="middle">
								<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">
								<b><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></b></a>
								<img src= "/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" align="absmiddle" title="${translation}" /></td>
								<td align="center" valign="middle">
									<a title="<digi:trn key="aim:CommitmentDate">The date (day, month, year) when funding commitment was signed</digi:trn>">
									<b><digi:trn key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn><br><digi:trn key="aim:CommitmentDateFIE">Commitment Date</digi:trn></b></a>
								</td>
								<td align="center" valign="middle" width="*">&nbsp;
									
								</td>
								</field:display>
							</tr>

							<c:set var="index" value="-1"/>
						    <c:forEach var="fundingDetail" items="${aimEditActivityForm.funding.fundingDetails}" varStatus="status">
						 	<c:if test="${fundingDetail.transactionType==0}">
									 	<tr class="table_rows">
									 	
                                            <c:set var="contentDisabled">true</c:set>
												<td valign="bottom" align="center">
													<c:set var="index" value="${index+1}"/>
													<c:if test="${aimEditActivityForm.identification.statusId==1}">
														<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text" disabled="${contentDisabled}">
															<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
														</html:select>
	                                                </c:if>
	
												<c:if test="${aimEditActivityForm.identification.statusId!=1}">
													<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text"  disabled="${contentDisabled}">
														<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
														<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
													</html:select>
												</c:if>
													<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
												</td>
										
												<td valign="bottom" align="center">
														<html:text name="fundingDetail" title="${formatTip}"  disabled="${contentDisabled}" indexed="true" property="transactionAmount" onchange="this.value=trim(this.value);" onclick="checkCurrency(this.name);" size="17" styleClass="amt"/>
												</td>
											<td valign="bottom" align="center">
												<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text"  disabled="${contentDisabled}" onchange="checkCurrency(this.name);" onfocus="checkCurrency(this.name);">
													<html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode"
													label="currencyName"/>
												</html:select>
												
											</td>
											<td align="center" vAlign="bottom">
												<table cellPadding="0" cellSpacing="0">
													<tr>
														<td>
															<html:text name="fundingDetail" indexed="true" property="transactionDate" readonly="true" size="10"/>
														</td>
													</tr>
												</table>												
											</td>
											</tr>	
									</c:if>
						 	</c:forEach>
						 	</c:if>
						</table>
					</td>
				</tr>
			</table>
			<br/>

			</td></tr>
			</table>
		</td>
	</tr>
</feature:display>
	<!-- disbursements -->
<feature:display module="Funding" name="Disbursement">
	<tr>
		<td width="100%" vAlign="top">

			<table width="100%" cellpadding="0" cellspacing="1" valign="top" align="left">
			<tr><td align="center">
			<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20">
					&nbsp;<digi:trn key="Disbursements">Disbursements</digi:trn>
					<img src="/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" align="absmiddle" hspace="5" title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>"/>
					</td>
				</tr>
				</table>
				<br/>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr class="textalb" align="center">
					<td>
						<table width="98%" border="0" cellspacing="1" cellpadding="2">
							<c:if test="${empty aimEditActivityForm.funding.disbursementsDetails}">
							<tr class="header_row">
								<td align="center" valign="middle">
								<digi:trn>No disbursement found</digi:trn>
								</td>
							</tr>
							</c:if>
							<c:if test="${ !empty aimEditActivityForm.funding.disbursementsDetails}">
							<tr bgcolor="#cecece" class="header_row">
								<field:display name="Adjustment Type Disbursement" feature="Disbursement">
								<td align="center" valign="middle">
									<b><digi:trn key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn></b>
								</td>
                                </field:display>
								<field:display name="Amount Disbursement" feature="Disbursement">
								<td align="center" valign="middle">
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><digi:trn key="aim:AmountFIE">Amount</digi:trn></b></a>
									</gs:test>
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommittedNoThousand">Full amount of expected transfer, irrespective of the time required for the completion of disbursements.</digi:trn>">
									<b><digi:trn key="aim:AmountFIE">Amount</digi:trn></b></a>
									</gs:test>
								</td>
                                </field:display>
								<field:display name="Currency Disbursement" feature="Disbursement">
								<td align="center" valign="middle">
									<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">
									<b><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></b></a>
								    <img src= "/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" align="absmiddle" title="${translation}" /></td>
                                </field:display>
								<field:display name="Date Disbursement" feature="Disbursement">
								<td align="center" valign="middle">
								<a title="<digi:trn key="aim:DateofDisbursement">Date of actual international transfer of financial resources</digi:trn>">
								<b>
                                  <digi:trn key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn><br/><digi:trn key="aim:DisbursementDateFIE">Disbursement Date</digi:trn></b></a>
								</td>
                                </field:display>
							</tr>
							<c:forEach var="fundingDetail" items="${aimEditActivityForm.funding.fundingDetails}">
						 	<c:if test="${fundingDetail.transactionType==1}">
										<tr class="table_rows">
											<td align="center" valign="top">
												<c:set var="index" value="${index+1}"/>
											<c:if test="${aimEditActivityForm.identification.statusId==1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="true" styleClass="inp-text">
													<html:option value="0">Planned</html:option>
												</html:select>
											</c:if>
											<c:if test="${aimEditActivityForm.identification.statusId!=1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text" disabled="${contentDisabled}">
													<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
													<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
											</c:if>
												<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
											</td>
											<td align="center" valign="top">
												<html:text name="fundingDetail" disabled="${contentDisabled}" indexed="true" title="${formatTip}"  property="transactionAmount" onchange="this.value=trim(this.value)" size="17" styleClass="amt"/>
											</td>
											<td align="center" valign="top">
												<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text" disabled="${contentDisabled}" onchange="checkCurrency(this.name);">
													<html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode"
													label="currencyName"/>
												</html:select>
											</td>
											<td align="center" valign="top">
												<table cellpadding="0" cellspacing="0">
													<tr>
														<td>
															<html:text name="fundingDetail" indexed="true" property="transactionDate" readonly="true" size="10"/>
														</td>
													</tr>
												</table>
											</td>
										</tr>
							</c:if>
						 	</c:forEach>
						 	</c:if>
						</table>
					</td>
				</tr>
			</table>
			<br/>
			</td></tr>
			</table>
		</td>
	</tr>
</feature:display>
	
						
	<feature:display module="Funding" name="Expenditures">
	<tr>
		<td width="100%" vAlign="top">
			<table width="100%" cellpadding="0" cellspacing="1" vAlign="top" align="left">
			<tr><td align="center">

			<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20">&nbsp;
						<digi:trn key="aim:expenditures">Expenditures</digi:trn>
						<img src="/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" align="absmiddle" hspace="5" title="<digi:trn key='aim:ExpenditureofFund'>Amount effectively spent by the implementing agency</digi:trn>"/>
					</td>
				</tr>
			</table>
			<br/>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr  class="textalb" align="center">
					<td align="center">
						<table width="98%" border="0" cellspacing="1" cellpadding="2">
							<c:if test="${empty aimEditActivityForm.funding.expendituresDetails}">
							<tr class="header_row">
								<td align="center" valign="middle">
								<digi:trn>No expenditures found</digi:trn>
								</td>
							</tr>
							</c:if>
							<c:if test="${ !empty aimEditActivityForm.funding.expendituresDetails}">
							<tr bgcolor="#cecece" class="header_row">
								<field:display name="Adjustment Type Expenditure" feature="Expenditures">
								<td align="center" valign="middle">
									<b><digi:trn key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn></b>
								</td>
								</field:display>
								<field:display name="Amount Expenditure" feature="Expenditures">
								<td align="center" valign="middle">
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><digi:trn key="aim:AmountFIE">Amount</digi:trn></b></a>
									</gs:test>
									<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
									<a title="<digi:trn key="aim:AmountCommittedNoThousand">Full amount of expected transfer, irrespective of the time required for the completion of disbursements.</digi:trn>">
									<b><digi:trn key="aim:AmountFIE">Amount</digi:trn></b></a>
									</gs:test>
								</td>
								</field:display>
								
								<field:display name="Currency Expenditure" feature="Expenditures">
									<td align="center" valign="middle">
										<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">								   		  
											<b><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></b>
										</a>
									    <img src= "/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" align="absmiddle"  title="${translation}" />
									</td>
								</field:display>
								<field:display name="Date Expenditure" feature="Expenditures">
								<td align="center" valign="middle">
									<a title="<digi:trn key="aim:DateofExpenditure">Date of actual expenditure</digi:trn>">
									<b><digi:trn key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn><br><digi:trn key="aim:ExpenditureDateFIE">Expenditure Date</digi:trn></font></b></a>
								</td>
								</field:display>
								<field:display name="Remove Expenditure Link" feature="Expenditures">
											<td>&nbsp;</td>
								</field:display>
							</tr>
						 	<c:forEach var="fundingDetail" items="${aimEditActivityForm.funding.fundingDetails}">
						 	<c:if test="${fundingDetail.transactionType==2}">
							 	<tr class="table_rows">

							 	
										<td align="center" valign="top">
											<c:set var="index" value="${index+1}"/>
											<c:if test="${aimEditActivityForm.identification.statusId==1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="true" styleClass="inp-text">
													<html:option value="0">Planned</html:option>
												</html:select>
											</c:if>
											<c:if test="${aimEditActivityForm.identification.statusId!=1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text" disabled="${contentDisabled}">
													<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
													<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
											</c:if>
											<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
										</td>
										<td align="center" valign="top">
											<html:text name="fundingDetail" disabled="${contentDisabled}" indexed="true" title="${formatTip}"  property="transactionAmount" onchange="this.value=trim(this.value)" size="17" styleClass="amt"/>
										</td>
										<td align="center" valign="top">
											<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text" disabled="${contentDisabled}" onchange="checkCurrency(this.name);">
												<html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode" label="currencyName"/>
											</html:select>
										</td>
										<td align="center" valign="top">
											<table cellpadding="0" cellspacing="0">
												<tr>
														<td>
															<html:text name="fundingDetail" indexed="true" property="transactionDate" readonly="true" size="10"/>
														</td>
												</tr>
											</table>
										</td>
								</tr>
							</c:if>
					 	</c:forEach>
				 	</c:if>
				</table>
			</td>
		</tr>
			</table>
			<br/>
			</td></tr>
			</table>
		</td>
	</tr>
    <!--end expenditures-->
    </feature:display>
</c:if>
	<tr>
		<td width="100%" vAlign="top">
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center">
						<table cellPadding="3">
							<tr>
								<c:if test="${empty aimEditActivityForm.funding.fundingDetails}">
								<td>
									<input type="button" value="<digi:trn key='btn:save'>Save</digi:trn>" class="inp-text" onClick="importFormFunding()">
								</td>
								</c:if>
								<c:if test="${!empty aimEditActivityForm.funding.fundingDetails}">
								<td>
<c:if test="${empty errors}">
									<input type="button" value="<digi:trn>Confirm data</digi:trn>" class="inp-text" onClick="submitImportForm()">
</c:if>
<c:if test="${!empty errors}">
									<input type="button" value="<digi:trn>Retry</digi:trn>" class="inp-text" onClick="importFunding('<bean:write name="aimEditActivityForm" property="funding.orgFundingId"/>')">
</c:if>
								</td>
								</c:if>
								<td>
									<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="inp-text" onClick="closeImport()">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>

</table>
</digi:form>
</body>


