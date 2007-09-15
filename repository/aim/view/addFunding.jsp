<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<script language="JavaScript">

	<!--

	function useFixedRateClicked(field1,field2) {
		var fld1 = document.getElementById(field1);
		var fld2 = document.getElementById(field2);
		if (fld1.disabled == true) {
			fld1.disabled = false;
		} else {
			fld1.disabled = true;
		}
		}

	function trim(s) {
		return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
  }

	function addFunding() {
		var errmsg1='<digi:trn key="aim:addFunding:errmsg:assitanceType">Type Of Assistance not selected</digi:trn>';
		var errmsg2='\n<digi:trn key="aim:addFunding:errmsg:fundOrgId">Funding Id not entered</digi:trn>';
		var errmsg3='\n<digi:trn key="aim:addFunding:errmsg:financeInstrument">Financing Instrument not selected</digi:trn>';
		var flag = validateFundingTrn(errmsg1,errmsg2,errmsg3);
		if (flag == false) return false;
		<digi:context name="fundAdded" property="context/module/moduleinstance/fundingAdded.do?edit=true" />;
		document.aimEditActivityForm.action = "<%= fundAdded %>";
		document.aimEditActivityForm.target = "_self";
	  	document.aimEditActivityForm.submit();
		return true;
	}

	function addFundingDetail(type) {

		var flag = validateFundingExchangeRate();
		if (flag == false) return false;
		
		if (type == 0) {
			document.aimEditActivityForm.event.value = "addCommitments";
		} else if (type == 1) {
			document.aimEditActivityForm.event.value = "addDisbursements";
		} else if (type == 2) {
			document.aimEditActivityForm.event.value = "addExpenditures";
		}
	 	document.aimEditActivityForm.action="/addFundingDetail.do";
	 	document.aimEditActivityForm.submit();
	}
	
	function addMTEFProjection() {

	//	var flag = validateFundingExchangeRate();
	//	if (flag == false) return false;
		
	document.aimEditActivityForm.event.value = "addProjections";
 	document.aimEditActivityForm.action="/addMTEFProjection.do";
 	document.aimEditActivityForm.submit();
	}
	
	

	function removeFundingDetail(index,type) {
		var flag = confirm("Are you sure you want to remove the selected transaction ?");
		if(flag != false) {
			if (type == 0) {
				document.aimEditActivityForm.event.value = "delCommitments";
			} else if (type == 1) {
				document.aimEditActivityForm.event.value = "delDisbursements";
			} else if (type == 2) {
				document.aimEditActivityForm.event.value = "delExpenditures";
			}
			document.aimEditActivityForm.transIndexId.value=index;
		 	document.aimEditActivityForm.action="/addFundingDetail.do";
			document.aimEditActivityForm.submit();
		}
	}
	
	function removeMTEFProjection(index) {
		var flag = confirm("Are you sure you want to remove the selected projection ?");
		if(flag != false) {
			document.aimEditActivityForm.transIndexId.value=index;
		 	document.aimEditActivityForm.action="/addMTEFProjection.do";
			document.aimEditActivityForm.submit();
		}
	}

	function load()
	{
		if(document.aimEditActivityForm.dupFunding.value == "false")
		{
				<digi:context name="addAct" property="context/module/moduleinstance/addActivity.do?edit=true"/>
				document.aimEditActivityForm.action = "<%=addAct%>";
				document.aimEditActivityForm.target = window.opener.name;
				document.aimEditActivityForm.submit();
				window.close();
		}
		if(document.aimEditActivityForm.dupFunding.value == "true")
		{
			if(document.aimEditActivityForm.firstSubmit.value == "true")
			{
				if(confirm("This information is a duplicate of existing funding information. Do you wish to proceed?"))
				{
					<digi:context name="addAct" property="context/module/moduleinstance/addActivity.do?edit=true"/>
					document.aimEditActivityForm.action = "<%=addAct%>";
					document.aimEditActivityForm.target = window.opener.name;
					document.aimEditActivityForm.submit();
					window.close();
				}
			}
		}
	}

	function unload() {
	}

	function closeWindow() {
		window.close();
	}


	-->
</script>

<%! long t = System.currentTimeMillis(); %>

<body onload="load()">
<digi:instance property="aimEditActivityForm" />
<digi:form action="/addFundingDetail.do" method="post">

<input type="hidden" name="edit" value="true">
<html:hidden name="aimEditActivityForm" property="dupFunding"/>
<html:hidden property="event"/>
<html:hidden property="transIndexId"/>
<html:hidden property="numComm"/>
<html:hidden property="numDisb"/>
<html:hidden property="numExp"/>
<html:hidden property="editAct"/>
<html:hidden property="firstSubmit"/>
	
<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class=box-border-nopadding>
	<!-- funding -->
	<tr>
    	<td width="100%" vAlign="top">

			<table width="100%" cellpadding=0 cellspacing=1 vAlign="top" align="left" bgcolor="#006699">
			<tr><td>

			<table width="100%" cellpadding=1 cellspacing=0 bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
						<digi:trn key="aim:FundingItemEditor">Funding Item Editor</digi:trn>
					</td>
				</tr>
				<tr>
					<td vAlign="top" align="center" width="100%">
						<table cellpadding=4 cellspacing=1 bgcolor="#ffffff" width="100%">
							<tr>
								<td align="right" bgcolor=#ECF3FD width="50%">
			                	<b><digi:trn key="aim:organization">Organization</digi:trn></b>
								</td>
								<td align="left" bgcolor=#ECF3FD width="50%">
			                	<bean:write name="aimEditActivityForm" property="orgName"/>
								</td>
							</tr>
							<tr>
								<td align="right" bgcolor=#ECF3FD>
			                	<FONT color=red>*</FONT><b>
									<a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
									<digi:trn key="aim:typeOfAssistance">Type of Assistance</digi:trn></a>
									</b>
								</td>
								<td align="left" bgcolor=#ECF3FD>
									<c:set var="translation">
										<digi:trn key="aim:addActivityTypeOfAssistenceFirstLine">Please select from below</digi:trn>
									</c:set> 
									<c:if test="${aimEditActivityForm.donorFlag == true}">
				                		<category:showoptions firstLine="${translation}" outerdisabled="true" name="aimEditActivityForm" property="assistanceType" keyName="<%= org.digijava.module.aim.helper.CategoryConstants.TYPE_OF_ASSISTENCE_KEY %>" styleClass="inp-text" />
									</c:if>
									<c:if test="${aimEditActivityForm.donorFlag == false}">
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="assistanceType"  keyName="<%= org.digijava.module.aim.helper.CategoryConstants.TYPE_OF_ASSISTENCE_KEY %>" styleClass="inp-text" />
									</c:if>
								</td>
							</tr>
							<tr>
								<td align="right" bgcolor=#ECF3FD>
			                	<FONT color=red>*</FONT><b>
										<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">
										<digi:trn key="aim:fundingOrgId">
										Funding Organization Id</digi:trn></a>
									</b>
								</td>
								<td align="left" bgcolor=#ECF3FD>
									<c:if test="${aimEditActivityForm.donorFlag == true}">
										<html:text property="orgFundingId" size="10" disabled="true"/>
									</c:if>
									<c:if test="${aimEditActivityForm.donorFlag == false}">
										<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation.
										This item may be useful when one project has two or more different financial instruments.
										If the project has a unique financial operation, the ID can be the same as the project ID
										</digi:trn>">
			   	             	<html:text property="orgFundingId" size="10"/>  </a>
									</c:if>
								</td>
							</tr>
							<tr>
								<td align="right" bgcolor=#ECF3FD>
								<FONT color=red>*</FONT><b>
									<a title="<digi:trn key="aim:FinanceInstrument">Method by which aid is delivered to an activity</digi:trn>">
									<b><digi:trn key="aim:financingInstrument">Financing Instrument</digi:trn></b></a>
								</td>
								<td align="left" bgcolor=#ECF3FD>
									<%-- 	<html:select property="modality" styleClass="inp-text" disabled="${aimEditActivityForm.donorFlag}">
											<html:optionsCollection name="aimEditActivityForm" property="modalityCollection"
											value="ampModalityId" label="name" />
										</html:select>
									--%>
									<c:if test="${aimEditActivityForm.donorFlag == true}">
										<category:showoptions firstLine="${translation}" outerdisabled="${aimEditActivityForm.donorFlag}" name="aimEditActivityForm" property="modality" 
										keyName="<%= org.digijava.module.aim.helper.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" styleClass="inp-text" />
									</c:if>
									<c:if test="${aimEditActivityForm.donorFlag == false}">
										<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="modality" 
										keyName="<%= org.digijava.module.aim.helper.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" styleClass="inp-text" />
									</c:if>
								</td>
							</tr>

						</table>
					</td>
				</tr>
			</table>

			</td></tr>
			</table>

		</td>
	</tr>
	<tr><td>
		<FONT color=blue><B><BIG>*</BIG>
			<digi:trn key="aim:pleaseEnterTheAmountInThousands">
			Please enter amount in thousands (000)</digi:trn>
	</td></tr>
	
	<tr>
    	<td width="100%" vAlign="top">

			<table width="100%" cellpadding=0 cellspacing=1 vAlign="top" align="left" bgcolor="#006699">
			<tr><td>

			<table width="100%" cellpadding=1 cellspacing=0 bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center" colspan="4">
						<digi:trn key="aim:MTEF Projection">MTEF Projection</digi:trn>
					</td>
				</tr>
				<tr bgcolor="#003366" class="textalb" align="center">
					<td><b><font color="white"><digi:trn key="aim:Projected">Projected</digi:trn>/<br><digi:trn key="aim:Pipeline">Pipeline</digi:trn></font></b>
					</td>
					<td><b><font color="white"><digi:trn key="aim:Amount">Amount</digi:trn></font></b>
					</td>
					<td><b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b>
					</td>
					<td><b><font color="white"><digi:trn key="aim:ProjectionDate">Projection Date</digi:trn></font></b>
					</td>
				</tr>
				
				<% int tempIndex = 0; %>
				<% String tempIndexStr = ""; %>
				<c:if test="${ !empty aimEditActivityForm.fundingMTEFProjections}">
				<c:set var="indexMTEF" value="-1"/>
				<c:forEach var="mtefProjection" items="${aimEditActivityForm.fundingMTEFProjections}">
					
				 	<tr>
						<td valign="bottom"> 
						<c:set var="indexMTEF" value="${indexMTEF+1}"/>
						<html:select name="mtefProjection" property="projected"> 
							<logic:iterate name="aimEditActivityForm" property="projections" id="projection" type="org.digijava.module.aim.dbentity.AmpCategoryValue">
							<html:option value="${projection.id}" >
								<digi:trn key="<%= org.digijava.module.aim.helper.CategoryManagerUtil.getTranslationKeyForCategoryValue(projection) %>">
									<bean:write name="projection" property="value"/>
								</digi:trn>
							</html:option>
						
							</logic:iterate>
						</html:select>
						</td>
						<td valign="bottom">
							<html:text name="mtefProjection" indexed="true" property="amount" size="17" styleClass="amt"/>
						</td>
						<td valign="bottom">
						<html:select name="mtefProjection" indexed="true" property="currencyCode" styleClass="inp-text">
							<html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode"
												label="currencyName"/>
						</html:select>
						</td>
						<td vAlign="bottom">
							<table cellPadding=0 cellSpacing=0>
							<tr>
								<td>
								<% tempIndexStr = "" + tempIndex; tempIndex++;%>
								<html:text name="mtefProjection" indexed="true" property="projectionDate"
								styleId="<%=tempIndexStr%>" readonly="true" size="10"/>
								</td>
								<td align="left" vAlign="center">&nbsp;
									<a id="trans7Date<%=tempIndexStr%>" href='javascript:pickDateById("trans7Date<%=tempIndexStr%>",<%=tempIndexStr%>)'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
									</a>
									</td>
							</tr>
							</table>
						</td>

											<td>
												<a href="javascript:removeMTEFProjection(<bean:write name="mtefProjection" property="indexId"/>,0)">
												 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this projection"/>
												</a>
											</td>
										</tr>

				</c:forEach>
				</c:if>
				
				
				<tr bgcolor="#ffffff">
					<td align="center" colspan="4">
						<table cellPadding=3>
							<tr>
								<td>
									<input class="buton" type="button" name="addMTEFProj" value="<digi:trn key='aim:addMTEFProjection'>Add Projection</digi:trn>"
								   onclick="addMTEFProjection()">
								</td>
							</tr>
						</table>
					</td>
				</tr>
				
				
				</table>
				</td>
				</tr>
				</table>
				</td>
				</tr>


	<!-- commitments -->
	<tr>
		<td width="100%" vAlign="top">

			<table width="100%" cellpadding=0 cellspacing=1 vAlign="top" align="left" bgcolor="#006699">
			<tr><td>
			<table width="100%" cellpadding=0 cellspacing=0 border="1">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
					<a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>"><digi:trn key="aim:commitments">Commitments</digi:trn></a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="0" class=box-border-nopadding>
							<tr bgcolor="#003366" class="textalb">
								<td align="center" valign="middle" width="75">
									<b><font color="white"><digi:trn key="aim:Planned">Planned</digi:trn>/<br><digi:trn key="aim:Actual">Actual</digi:trn></font></b>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
								</td>
								<td align="center" valign="middle" width="170">
								<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">											  <b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b></a>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:CommitmentDate">The date (day, month, year) when funding commitment was signed</digi:trn>">
									<b><font color="white"><digi:trn key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn><br><digi:trn key="aim:CommitmentDateFIE">Commitment Date</digi:trn></font></b></a>
								</td>
								<td align="center" valign="middle" colspan="2">
									<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
										<a title="<digi:trn key="aim:PerpectiveofUser">Select perspective (Donor/ MoFED / implementing agency) of user entering the information</digi:trn>">
										<b><font color="white"><digi:trn key="aim:PerspectiveFIE">Perspective</digi:trn></font></b></a>
									</logic:equal>	
								</td>
							</tr>

							<c:if test="${ !empty aimEditActivityForm.fundingDetails}">
							<c:set var="index" value="-1"/>
						 	<c:forEach var="fundingDetail" items="${aimEditActivityForm.fundingDetails}">
						 	<c:if test="${fundingDetail.transactionType==0}">
								<c:if test="${aimEditActivityForm.donorFlag == true}">
								 	<c:if test="${fundingDetail.perspectiveCode=='DN'}">
									 	<tr>
											<td valign="bottom">
												<c:set var="index" value="${index+1}"/>
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text">
													<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
													<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
												<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
											</td>
											<td valign="bottom">
												<html:text name="fundingDetail" indexed="true" property="transactionAmount" size="17" styleClass="amt"/>
											</td>
											<td valign="bottom">
												<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text">
													<html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode"
													label="currencyName"/>
												</html:select>
											</td>
											<td vAlign="bottom">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<% tempIndexStr = "" + tempIndex; tempIndex++;%>
															<html:text name="fundingDetail" indexed="true" property="transactionDate"
															styleId="<%=tempIndexStr%>" readonly="true" size="10"/>
														</td>
														<td align="left" vAlign="center">&nbsp;
															<a id="trans2Date<%=tempIndexStr%>" href='javascript:pickDateById("trans2Date<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
															</a>
														</td>
													</tr>
												</table>
											</td>
											<td valign="bottom">
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<html:select name="fundingDetail" indexed="true" property="perspectiveCode" styleClass="inp-text"
													disabled="false">
														<html:option value="DN"><digi:trn key="aim:PerspectiveOp1">Donor</digi:trn></html:option>
														<c:if test="${sessionScope.currentMember.teamType != 'DONOR'}">
															<html:option value="MA"><digi:trn key="aim:PerspectiveOp2">Goverment</digi:trn></html:option>
														</c:if>
													</html:select>
												</logic:equal>
											</td>
											<td>
												<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,0)">
												 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
												</a>
											</td>
										</tr>
										<tr>
											<td>
												&nbsp;
											</td>
											<td>
												<digi:trn key="aim:FixedRate">Fixed Rate</digi:trn>
											</td>
											<td colspan="4">
												<digi:trn key="aim:ExchangeRate"> Rate</digi:trn>
											</td>
										</tr>
										<tr>
											<td>
												&nbsp;
											</td>
											<td align="right">
												<% String exchRatefldId = "exchFld"+(t++);
													String exchCurrfldId = "exchCurr"+(t++);
													String jsUrl = "useFixedRateClicked('" + exchRatefldId + "')";
												%>
												<html:checkbox name="fundingDetail" indexed="true" property="useFixedRate" onclick="<%=jsUrl%>"/>
											</td>
											<td colspan="4">

												<logic:equal name="fundingDetail" property="useFixedRate" value="true">
													<html:text name="fundingDetail" indexed="true" property="fixedExchangeRate" styleClass="amt" disabled="false" styleId="<%=exchRatefldId%>"/>
												</logic:equal>
												<logic:equal name="fundingDetail" property="useFixedRate" value="false">
													<html:text name="fundingDetail" indexed="true" property="fixedExchangeRate" styleClass="amt" disabled="true" styleId="<%=exchRatefldId%>"/>
												</logic:equal>
											</td>
										</tr>
									</c:if>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
								 	<%-- <c:if test="${fundingDetail.perspectiveCode=='MA'}"> --%>
									 	<tr>
											<td valign="bottom">
												<c:set var="index" value="${index+1}"/>
												<c:if test="${aimEditActivityForm.statusId==1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text">
													<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
                                                </c:if>
											</c:if>

											<c:if test="${aimEditActivityForm.statusId!=1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text">
													<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
													<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
											</c:if>
												<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
											<td valign="bottom">
												<html:text name="fundingDetail" indexed="true" property="transactionAmount" size="17" styleClass="amt"/>
											</td>
											<td valign="bottom">
												<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text">
													<html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode"
													label="currencyName"/>
												</html:select>
											</td>
											<td vAlign="bottom">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<% tempIndexStr = "" + tempIndex; tempIndex++;%>
															<html:text name="fundingDetail" indexed="true" property="transactionDate"
															styleId="<%=tempIndexStr%>" readonly="true" size="10"/>
														</td>
														<td align="left" vAlign="center">&nbsp;
															<a id="trans3Date<%=tempIndexStr%>" href='javascript:pickDateById("trans3Date<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
															</a>
														</td>
													</tr>
												</table>
											</td>
											<td valign="bottom">
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<html:select name="fundingDetail" indexed="true" property="perspectiveCode" styleClass="inp-text"
													disabled="false">
														<html:option value="DN"><digi:trn key="aim:PerspectiveOp1">Donor</digi:trn></html:option>
														<html:option value="MA"><digi:trn key="aim:PerspectiveOp2">Goverment</digi:trn></html:option>
													</html:select>
												</logic:equal>
											</td>
											<td>
												<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,0)">
												 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
												</a>
											</td>
										<tr>
											<td bgcolor="#ffff00">
												&nbsp;
											</td>
											<td align="left"  bgcolor="#ffff00">
												<b>
													<digi:trn key="aim:fixedRate">Fixed Rate</digi:trn>
												</b>
											</td>
											<td colspan="3"  bgcolor="#ffff00">
												<b>
													<digi:trn key="aim:fixedExchangeRate">Exchange Rate</digi:trn>
												</b>
											</td>
										</tr>
										<tr>
											<td>
												&nbsp;
											</td>
											<td align="right">
												<% String exchRatefldId = "exchFld"+ (t++);
													String exchCurrfldId = "exchCurr"+ (t++);
													String jsUrl = "useFixedRateClicked('" + exchRatefldId + "')";
												%>
												<html:checkbox name="fundingDetail" indexed="true" property="useFixedRate"
												onclick="<%=jsUrl%>"/>
											</td>
											<td colspan="4">
												<logic:equal name="fundingDetail" property="useFixedRate" value="true">
													<html:text name="fundingDetail" indexed="true" property="fixedExchangeRate" styleClass="amt"
													disabled="false" styleId="<%=exchRatefldId%>"/>
												</logic:equal>
												<logic:equal name="fundingDetail" property="useFixedRate" value="false">
													<html:text name="fundingDetail" indexed="true" property="fixedExchangeRate" styleClass="amt"
													disabled="true" styleId="<%=exchRatefldId%>"/>
												</logic:equal>
											</td>
											
										</tr>
									</c:if>
						 	</c:forEach>
						 	</c:if>
						</table>
					</td>
				</tr>
				<tr bgcolor="#ffffff">
					<td align="center">
						<table cellPadding=3>
							<tr>
								<%--
								<td>
									<input class="buton" type="button" name="xx1" value="Delete Commitments"
									onclick="this.form.event.value='delCommitments';addFundingDetail();">
								</td> --%>
								<td>
									<input class="buton" type="button" name="xx1" value="<digi:trn key='aim:addCommitment'>Add Commitment</digi:trn>"
								   onclick="addFundingDetail(0)">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

			</td></tr>
			</table>
		</td>
	</tr>

	<!-- disbursements -->
	<tr>
		<td width="100%" vAlign="top">

			<table width="100%" cellpadding=0 cellspacing=1 vAlign="top" align="left" bgcolor="#006699">
			<tr><td>
			<table width="100%" cellpadding=0 cellspacing=0>
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
					<a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>"><digi:trn key="Disbursements">Disbursements</digi:trn></a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="0" class=box-border-nopadding>
							<tr bgcolor="#003366" class="textalb">
								<td align="center" valign="middle" width="75">
									<b><font color="white"><digi:trn key="aim:Planned">Planned</digi:trn>/<br><digi:trn key="aim:Actual">Actual</digi:trn></font></b>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
								</td>
								<td align="center" valign="middle" width="170">
									<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">
									<b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b></a>
								</td>
								<td align="center" valign="middle" width="120">
								<a title="<digi:trn key="aim:DateofDisbursement">Date of actual international transfer of financial resources</digi:trn>">
								<b><font color="white">
                                  <digi:trn key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn><br><digi:trn key="aim:DisbursementDateFIE">Disbursement Date</digi:trn></font></b></a>
								</td>
								<td align="center" valign="middle" colspan="2">
									<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
										<a title="<digi:trn key="aim:PerpectiveofUser">Select perspective (Donor/ MoFED / implementing agency) of user entering the information</digi:trn>">
										<b><font color="white"><digi:trn key="aim:PerspectiveFIE">Perspective</digi:trn></font></b></a>
									</logic:equal>
								</td>
							</tr>
							<c:if test="${ !empty aimEditActivityForm.fundingDetails}">
							<c:forEach var="fundingDetail" items="${aimEditActivityForm.fundingDetails}">
						 	<c:if test="${fundingDetail.transactionType==1}">

								<c:if test="${aimEditActivityForm.donorFlag == true}">
								 	<c:if test="${fundingDetail.perspectiveCode=='DN'}">
										<tr>
											<td valign="bottom">
												<c:set var="index" value="${index+1}"/>
											<c:if test="${aimEditActivityForm.statusId==1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="true" styleClass="inp-text">
													<html:option value="0">Planned</html:option>
												</html:select>
											</c:if>

											<c:if test="${aimEditActivityForm.statusId!=1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text">
													<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
													<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
											</c:if>
												<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
											</td>
											<td valign="bottom">
												<html:text name="fundingDetail" indexed="true" property="transactionAmount" size="17" styleClass="amt"/>
											</td>
											<td valign="bottom">
												<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text" >
													<html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode"
													label="currencyName"/>
												</html:select>
											</td>
											<td valign="bottom">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<% tempIndexStr = "" + tempIndex; tempIndex++;%>
															<html:text name="fundingDetail" indexed="true" property="transactionDate" readonly="true"
															styleId="<%=tempIndexStr%>" size="10"/>
														</td>
														<td align="left" vAlign="center">&nbsp;
															<a id="trans4Date<%=tempIndexStr%>" href='javascript:pickDateById("trans4Date<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
															</a>
														</td>
													</tr>
												</table>
											</td>
											<td valign="bottom">
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<html:select name="fundingDetail" indexed="true" property="perspectiveCode" styleClass="inp-text"
													disabled="false">
														<html:option value="DN"><digi:trn key="aim:PerspectiveOp1">Donor</digi:trn></html:option>
														<html:option value="MA">MOFED</html:option>
													</html:select>
												</logic:equal>
											</td>
											<td>
												<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,1)">
												 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
												</a>
											</td>
										</tr>
									</c:if>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
								 	<%-- <c:if test="${fundingDetail.perspectiveCode=='MA'}"> --%>
										<tr>
											<td valign="bottom">
												<c:set var="index" value="${index+1}"/>
											<c:if test="${aimEditActivityForm.statusId==1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="true" styleClass="inp-text">
													<html:option value="0">Planned</html:option>
												</html:select>
											</c:if>

											<c:if test="${aimEditActivityForm.statusId!=1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text">
													<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
													<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
											</c:if>
												<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
											</td>
											<td valign="bottom">
												<html:text name="fundingDetail" indexed="true" property="transactionAmount" size="17" styleClass="amt"/>
											</td>
											<td valign="bottom">
												<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text" >
													<html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode"
													label="currencyName"/>
												</html:select>
											</td>
											<td valign="bottom">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<% tempIndexStr = "" + tempIndex; tempIndex++;%>
															<html:text name="fundingDetail" indexed="true" property="transactionDate" readonly="true"
															styleId="<%=tempIndexStr%>" size="10"/>
														</td>
														<td align="left" vAlign="center">&nbsp;
															<a id="transDate<%=tempIndexStr%>" href='javascript:pickDateById("transDate<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
															</a>
														</td>
													</tr>
												</table>
											</td>
											<td valign="bottom">
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<html:select name="fundingDetail" indexed="true" property="perspectiveCode" styleClass="inp-text"
													disabled="false">
														<html:option value="DN"><digi:trn key="aim:PerspectiveOp1">Donor</digi:trn></html:option>
														<html:option value="MA"><digi:trn key="aim:PerspectiveOp2">Goverment</digi:trn></html:option>
													</html:select>
												</logic:equal>
											</td>
											<td>
												<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,1)">
												 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
												</a>
											</td>
										</tr>
									<%-- </c:if> --%>
								</c:if>
							</c:if>
						 	</c:forEach>
						 	</c:if>
						</table>
					</td>
				</tr>
				<tr bgcolor="#ffffff">
					<td align="center">
						<table cellPadding=3>
							<tr>
								<%--
								<td>
									<input class="buton" type="button" name="xx1" value="Delete Disbursements"
								   onclick="this.form.event.value='delDisbursements';addFundingDetail();">
								</td>--%>
								<td>
									<input class="buton" type="button" name="xx1" value="<digi:trn key='aim:addDisbursement'>Add Disbursement</digi:trn>"
								   onclick="addFundingDetail(1)">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			</td></tr>
			</table>
		</td>
	</tr>

	<!-- expenditures -->
	<tr>
		<td width="100%" vAlign="top">
			<table width="100%" cellpadding=0 cellspacing=1 vAlign="top" align="left" bgcolor="#006699">
			<tr><td>

			<table width="100%" cellpadding=0 cellspacing=0 border="1">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
 						<a title="<digi:trn key='aim:ExpenditureofFund'>Amount effectively spent by the implementing agency</digi:trn>"><digi:trn key="aim:expenditures">Expenditures</digi:trn></a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="0" class=box-border-nopadding>
							<tr bgcolor="#003366" class="textalb">
								<td align="center" valign="middle" width="75">
									<b><font color="white"><digi:trn key="aim:Planned">Planned</digi:trn>/<br><digi:trn key="aim:Actual">Actual</digi:trn></font></b>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b></a>
								</td>
								<td align="center" valign="middle" width="170">
									<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">								   		  <b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b></a>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:DateofExpenditure">Date of actual expenditure</digi:trn>">
									<b><font color="white"><digi:trn key="aim:Planned">Planned</digi:trn>/<digi:trn key="aim:Actual">Actual</digi:trn><br><digi:trn key="aim:ExpenditureDateFIE">Expenditure Date</digi:trn></font></b></a>
								</td>
								<td align="center" valign="middle" colspan="2">
									<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
										<a title="<digi:trn key="aim:PerpectiveofUser">Select perspective (Donor/ MoFED / implementing agency) of user entering the information</digi:trn>">
										<b><font color="white"><digi:trn key="aim:PerspectiveFIE">Perspective</digi:trn></font></b></a>
									</logic:equal>
								</td>
							</tr>
							<c:if test="${ !empty aimEditActivityForm.fundingDetails}">
						 	<c:forEach var="fundingDetail" items="${aimEditActivityForm.fundingDetails}">
						 	<c:if test="${fundingDetail.transactionType==2}">

								<c:if test="${aimEditActivityForm.donorFlag == true}">
								 	<c:if test="${fundingDetail.perspectiveCode=='DN'}">

									 	<tr bgcolor="#ffffff">
														<td valign="bottom">
															<c:set var="index" value="${index+1}"/>
															<c:if test="${aimEditActivityForm.statusId==1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="true" styleClass="inp-text">
																<html:option value="0">Planned</html:option>
															</html:select>
											</c:if>

											<c:if test="${aimEditActivityForm.statusId!=1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text">
																<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
																<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
											</c:if>
															<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
														</td>
														<td valign="bottom">
															<html:text name="fundingDetail" indexed="true" property="transactionAmount" size="17" styleClass="amt"/>
														</td>
														<td valign="bottom">
															<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text" >
																<html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode"
																label="currencyName"/>
															</html:select>
														</td>
														<td valign="bottom">
															<table cellPadding=0 cellSpacing=0>
																<tr>
																	<td>
																		<% tempIndexStr = "" + tempIndex; tempIndex++;%>
																		<html:text name="fundingDetail" indexed="true" property="transactionDate"
																		styleId="<%=tempIndexStr%>" readonly="true" size="10"/>
																	</td>
																	<td align="left" vAlign="center">&nbsp;
																		<a id="trans5Date<%=tempIndexStr%>" href='javascript:pickDateById("trans5Date<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																			<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																		</a>
																		<% tempIndex++; %>
																	</td>
																</tr>
															</table>
														</td>
														<td valign="bottom">
															<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
																<html:select name="fundingDetail" indexed="true" property="perspectiveCode" styleClass="inp-text"
																disabled="false">
																	<html:option value="DN"><digi:trn key="aim:PerspectiveOp1">Donor</digi:trn></html:option>
																	<html:option value="MA">MOFED</html:option>
																</html:select>
															</logic:equal>
														</td>
														<td>
															<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,2)">
															 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
															</a>
														</td>
													</tr>
													<tr bgcolor="#ffffff">
														<td colspan="6">
															<b><digi:trn key="aim:classification">Classification</digi:trn></b>&nbsp;
															<html:text name="fundingDetail" indexed="true" property="classification"
															size="75" styleClass="inp-text"/>
														</td>
													</tr>
													<tr>
														<td colspan="6" bgcolor="#dddddd"></td>
													</tr>
									</c:if>
								</c:if>
								<c:if test="${aimEditActivityForm.donorFlag == false}">
								 	<%--  <c:if test="${fundingDetail.perspectiveCode=='MA'}"> --%>

									 	<tr bgcolor="#ffffff">
														<td valign="bottom">
															<c:set var="index" value="${index+1}"/>
													<c:if test="${aimEditActivityForm.statusId==1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" disabled="true" styleClass="inp-text">
																<html:option value="0">Planned</html:option>
															</html:select>
											</c:if>

											<c:if test="${aimEditActivityForm.statusId!=1}">
												<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text">
																<html:option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></html:option>
																<html:option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></html:option>
												</html:select>
											</c:if>
															<html:hidden name="fundingDetail" indexed="true" property="transactionType"/>
														</td>
														<td valign="bottom">
															<html:text name="fundingDetail" indexed="true" property="transactionAmount" size="17" styleClass="amt"/>
														</td>
														<td valign="bottom">
															<html:select name="fundingDetail" indexed="true" property="currencyCode" styleClass="inp-text" >
																<html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode"
																label="currencyName"/>
															</html:select>
														</td>
														<td valign="bottom">
															<table cellPadding=0 cellSpacing=0>
																<tr>
																	<td>
																		<% tempIndexStr = "" + tempIndex; tempIndex++;%>
																		<html:text name="fundingDetail" indexed="true" property="transactionDate"
																		styleId="<%=tempIndexStr%>" readonly="true" size="10"/>
																	</td>
																	<td align="left" vAlign="center">&nbsp;
																		<a id="trans6Date<%=tempIndexStr%>" href='javascript:pickDateById("trans6Date<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																			<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																		</a>
																		<% tempIndex++; %>
																	</td>
																</tr>
															</table>
														</td>
														<td valign="bottom">
															<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
																<html:select name="fundingDetail" indexed="true" property="perspectiveCode" styleClass="inp-text"
																disabled="false">
																	<html:option value="DN"><digi:trn key="aim:PerspectiveOp1">Donor</digi:trn></html:option>
																	<html:option value="MA"><digi:trn key="aim:PerspectiveOp2">Goverment</digi:trn></html:option>
																</html:select>
															</logic:equal>
														</td>
														<td>
															<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,2)">
															 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
															</a>
														</td>
													</tr>
													<tr bgcolor="#ffffff">
														<td colspan="6">
															<b><digi:trn key="aim:classification">Classification</digi:trn></b>&nbsp;
															<html:text name="fundingDetail" indexed="true" property="classification"
															size="75" styleClass="inp-text"/>
														</td>
													</tr>
													<tr>
														<td colspan="6" bgcolor="#dddddd"></td>
													</tr>

									<%--  </c:if> --%>
								</c:if>

							</c:if>
						 	</c:forEach>
						 	</c:if>
						</table>
					</td>
				</tr>
				<tr bgcolor="#ffffff">
					<td align="center">
						<table cellPadding=3>
							<tr>
								<td>
									<input class="buton" type="button" name="xx1" value="<digi:trn key='aim:addExpediture'>Add Expenditure</digi:trn>"
				   				onclick="addFundingDetail(2)">
								</td>
							</tr>
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
			<table width="100%" cellSpacing=1 cellPadding=1>
				<tr>
					<td align="left" bgcolor=#ECF3FD valign="top" width="80">
						<b>
						<a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">
						<digi:trn key="aim:conditions">Conditions</digi:trn></b></a>
					</td>
					<td align="left" bgcolor=#ECF3FD>
					<a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">
						<html:textarea property="fundingConditions" rows="3" cols="75" styleClass="inp-text"/>
					</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td width="100%" vAlign="top">
			<table width="100%" cellpadding=0 cellspacing=0>
				<tr>
					<td align="center">
						<table cellPadding=3>
							<tr>
								<td>
									<input type="button" value="<digi:trn key='btn:save'>Save</digi:trn>" class="inp-text" onclick="return addFunding()">
								</td>
								<td>
									<input type="reset" value="<digi:trn key='btn:reset'>Reset</digi:trn>" class="inp-text">
								</td>
								<td>
									<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="inp-text" onclick="closeWindow()">
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