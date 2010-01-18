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

<body onLoad="load()">

<c:set var="formatTip">
	<digi:trn key="aim:decimalforma">Format has to be: </digi:trn> <%=FormatHelper.formatNumber(FormatHelper.parseDouble("100000"+FormatHelper.getDecimalSymbol()+"150"))%>
</c:set>

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
<html:hidden name="aimEditActivityForm" styleId="ignoreDistBiggerThanComm" property="ignoreDistBiggerThanComm"/>
<html:hidden name="aimEditActivityForm" styleId="totDisbIsBiggerThanTotCom" property="totDisbIsBiggerThanTotCom"/>


<input type="hidden" name="funding.isEditFunding" value="true"/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
	<!-- funding -->
	<tr>
    	<td width="100%" vAlign="top">

			<table width="100%" cellpadding="0" cellspacing="1" vAlign="top" align="left" bgcolor="#006699">
			<tr><td>

			<table width="100%" cellPadding="1" cellspacing="0" bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
						<digi:trn key="aim:ImportFunding">Import Funding</digi:trn>
					</td>
				</tr>
				<tr>
					<td vAlign="top" align="center" width="100%">
						<table cellpadding=4 cellspacing="1" bgcolor="#ffffff" width="100%">
							<tr>
								<td align="right" bgcolor="#ECF3FD" width="50%">
			                	<b><digi:trn key="aim:organization">Organization</digi:trn></b>
								</td>
								<td align="left" bgcolor="#ECF3FD" width="50%">
			                	<bean:write name="aimEditActivityForm" property="funding.orgName"/>
								</td>
							</tr>
							<tr>
								<td align="right" bgcolor="#ECF3FD">
			                	<FONT color=red>*</FONT><b>
									<a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
			                	<font color=black>									
									<digi:trn key="aim:typeOfAssistance">Type of Assistance</digi:trn>
								</font>	
									</a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
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
							</tr>
							<tr>
								<td align="right" bgcolor="#ECF3FD">
			                	<%-- FONT color=red>*</FONT--%><b>
										<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">
										<font color=black>
										<digi:trn key="aim:fundingOrgId">
										Funding Organization Id</digi:trn></font></a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
								
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
								<td align="right" bgcolor="#ECF3FD">
								<FONT color=red>*</FONT><b>
									<a title="<digi:trn key="aim:FinanceInstrument">Method by which aid is delivered to an activity</digi:trn>">
									<b><font color=black><digi:trn key="aim:financingInstrument">Financing Instrument</digi:trn></font></b></a>
								</td>
								<td align="left" bgcolor="#ECF3FD">
								
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
								<td align="right" bgcolor="#ECF3FD">
			                	<FONT color=red>*</FONT><b>
									<a title="<digi:trn>The status of the funding</digi:trn>">
									<b><font color=black><digi:trn>Funding Status</digi:trn></font></b></a>
									</b>
								</td>
								<td align="left" bgcolor="#ECF3FD">
									<c:set var="translation">
										<digi:trn>Please select from below</digi:trn>
									</c:set>
									
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm"   property="funding.fundingStatus"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.FUNDING_STATUS_KEY %>" styleClass="inp-text"/>
									
								</td>
							</tr>
							</field:display>
							<tr>
							
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
	<tr>
		<td>
			<table width="100%" cellspacing="1" cellPadding="1">
				<tr>
					<td align="left" bgcolor="#ECF3FD" valign="top" width="80">
						<b>
						<a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">
						<font color=black>	<digi:trn key="aim:conditions">Conditions</digi:trn></font></a></b>
					</td>
					<td align="left" bgcolor="#ECF3FD">
					<a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">
						<html:textarea property="funding.fundingConditions" rows="3" cols="75" styleClass="inp-text"/>
					</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<field:display name="Donor Objective" feature="Funding Information">
	<tr>
		<td>
			<table width="100%" cellspacing="1" cellPadding="1">
				<tr>
					<td align="left" bgcolor="#ECF3FD" valign="top" width="80">
						<b>
						<a title="<digi:trn key="aim:DonorObjectiveforFundRelease">Enter the donor objective attached to the release of the funds</digi:trn>">
						<font color=black>	<digi:trn key="aim:donorobjective">Donor Objective</digi:trn></font></b></a>
					</td>
					<td align="left" bgcolor="#ECF3FD">
					<a title="<digi:trn key="aim:DonorObjectiveforFundRelease">Enter the donor objective attached to the release of the funds</digi:trn>">
						<html:textarea property="funding.donorObjective" rows="3" cols="75" styleClass="inp-text"/>
					</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</field:display>
	<tr>
		<td width="100%" vAlign="top">
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center">
						<table cellPadding="3">
							<tr>
								<td>
									<input type="button" value="<digi:trn key='btn:save'>Save</digi:trn>" class="inp-text" onClick="importFormFunding()">
								</td>
								<td>
									<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="inp-text" onClick="closeWindow()">
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


