<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivityStep3.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>

<script language="JavaScript">

	<!--
	function trim(s) {
		return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
   }

	function addFunding() {
		var flag = validateFunding();
		if (flag == false) return false;
		<digi:context name="fundAdded" property="context/module/moduleinstance/fundingAdded.do?edit=true" />;
		document.aimEditActivityForm.action = "<%= fundAdded %>";
		document.aimEditActivityForm.target = window.opener.name;
	   document.aimEditActivityForm.submit();
		window.close();
		return true;
	}	

	
	function addFundingDetail(type) {
		if (type == 0) {
			document.aimEditActivityForm.event.value = "addCommitments";
		} else if (type == 1) {
			document.aimEditActivityForm.event.value = "addDisbursements";
		} else if (type == 2) {
			document.aimEditActivityForm.event.value = "addExpenditures";
		}
	   	document.aimEditActivityForm.submit();		
	}

	function removeFundingDetail(index,type) {
		var flag = confirm("Are you sure you want to remove the selected transaction");
		if(flag != false) {
			if (type == 0) {
				document.aimEditActivityForm.event.value = "delCommitments";
			} else if (type == 1) {
				document.aimEditActivityForm.event.value = "delDisbursements";
			} else if (type == 2) {
				document.aimEditActivityForm.event.value = "delExpenditures";
			}
			document.aimEditActivityForm.transIndexId.value=index;
			document.aimEditActivityForm.submit();
		}

	}

	function load() {
		if (window.opener.document.aimEditActivityForm.currUrl.value == "") {
			window.opener.document.aimEditActivityForm.currUrl.value = "/addFunding";
		}			  
	}

	function unload() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
	}

	function closeWindow() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
		window.close();
	}

	-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addFundingDetail.do" method="post">

<html:hidden property="event"/>
<html:hidden property="transIndexId"/>

<html:hidden property="numComm"/>
<html:hidden property="numDisb"/>
<html:hidden property="numExp"/>
<html:hidden property="editAct"/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class=box-border-nopadding>
	<!-- funding -->
	<tr>
    	<td width="100%" vAlign="top">

			<table width="100%" cellpadding=0 cellspacing=1 vAlign="top" align="left" bgcolor="#006699">
			<tr><td>
			
			<table width="100%" cellpadding=1 cellspacing=0 bgcolor="#ffffff">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
						Funding Item Editor
					</td>
				</tr>
				<tr>
					<td vAlign="top" align="center" width="100%">
						<table cellpadding=4 cellspacing=1 bgcolor="#ffffff" width="100%">
							<tr>
								<td align="right" bgcolor=#ECF3FD width="50%">
			                	<b>Organization</b>
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
			                	<html:select property="assistanceType" styleClass="inp-text">
										<html:optionsCollection name="aimEditActivityForm" property="assistanceTypes" 
										value="ampTermsAssistId" label="termsAssistName"/>
									</html:select>  
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
										<a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">										
			                	<html:text property="orgFundingId" size="10"/>  </a>
								</td>								
							</tr>
							<tr>
								<td align="right" bgcolor=#ECF3FD>
									<a title="<digi:trn key="aim:FinanceInstrument">Method by which aid is delivered to an activity</digi:trn>">																									                																	 <b><digi:trn key="aim:financingInstrument">Financing Instrument</digi:trn></b></a>
								</td>
								<td align="left" bgcolor=#ECF3FD>
									<html:select property="modality" styleClass="inp-text">
									<html:optionsCollection name="aimEditActivityForm" property="modalityCollection" 
									value="ampModalityId" label="name" />
									</html:select>
								</td>								
							</tr>
							
							<%--
							<tr>
								<td align="right" bgcolor=#ECF3FD>
			                	<FONT color=red>*</FONT><b>Signature Date</b>
								</td>
								<td align="left" bgcolor=#ECF3FD>
									<table cellPadding=0 cellSpacing=0>
										<tr>
											<td>
						                	<html:text property="signatureDate" readonly="true" size="10"/>
											</td>
											<td align="left" vAlign="center">&nbsp;
												<a href="javascript:newWindow(1,0)">
												<img src="../ampTemplate/images/show-calendar.gif" width=24 height=20 border=0></a>
											</td>
										</tr>
									</table>
								</td>								
							</tr>
							--%>
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
			

	<!-- commitments -->
	<tr>
		<td width="100%" vAlign="top">
			
			<table width="100%" cellpadding=0 cellspacing=1 vAlign="top" align="left" bgcolor="#006699">
			<tr><td>
			<table width="100%" cellpadding=0 cellspacing=0>
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
					<a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>">Commitments</a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="0" class=box-border-nopadding>
							<tr bgcolor="#003366" class="textalb">
								<td align="center" valign="middle" width="75">
									<b><font color="white">Planned/<br>Actual</font></b>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white">Amount</font></b></a>
								</td>
								<td align="center" valign="middle" width="170">
								<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">											  <b><font color="white">Currency</font></b></a>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:CommitmentDate">The date (day, month, year) when funding commitment was signed</digi:trn>">									
									<b><font color="white">Planned/Actual<br>Commitment Date</font></b></a>
								</td>
								<td align="center" valign="middle" colspan="2" width="120">
									<a title="<digi:trn key="aim:PerpectiveofUser">Select perspective (Donor/ MoFED / implementing agency) of user entering the information</digi:trn>">											  
									<b><font color="white">Perspective</font></b></a>
								</td>
							</tr>

							<c:if test="${ !empty aimEditActivityForm.fundingDetails}">
							<c:set var="index" value="-1"/>
						 	<c:forEach var="fundingDetail" items="${aimEditActivityForm.fundingDetails}"> 
						 	<c:if test="${fundingDetail.transactionType==0}">
						 	<tr>
								<td valign="bottom">
									<c:set var="index" value="${index+1}"/>
									<%--	
									<html:checkbox name="fundingDetail" indexed="true" property="checked" value="off"/>  
									--%>
									<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text">
										<html:option value="1">Actual</html:option>	
										<html:option value="0">Planned</html:option>
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
												<html:text name="fundingDetail" indexed="true" property="transactionDate" readonly="true" size="10"/>
											</td>
											<td align="left" vAlign="center">&nbsp;
			             					<a href='javascript:newWindow(2,<bean:write name="fundingDetail" property="index"/>)'>	
		   			          			<img src="../ampTemplate/images/show-calendar.gif" border="0"></a>
											</td>
										</tr>
									</table>
								</td>																
								<td valign="bottom">
									<html:select name="fundingDetail" indexed="true" property="perspectiveCode" styleClass="inp-text" >
										<html:option value="DN">Donor</html:option>	
										<html:option value="MA">MOFED</html:option>
									</html:select>
								</td>
								<td>
									<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,0)">
								 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
									</a>
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
									<input class="buton" type="button" name="xx1" value="Add Commitment"
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
					<a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor</digi:trn>">Disbursements</a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="0" class=box-border-nopadding>
							<tr bgcolor="#003366" class="textalb">
								<td align="center" valign="middle" width="75">
									<b><font color="white">Planned/<br>Actual</font></b>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white">Amount</font></b></a>
								</td>
								<td align="center" valign="middle" width="170">
									<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">
									<b><font color="white">Currency</font></b></a>
								</td>
								<td align="center" valign="middle" width="120">
								<a title="<digi:trn key="aim:DateofDisbursement">Date of actual international transfer of financial resources</digi:trn>">
								<b><font color="white">Planned/Actual<br>Disbursement Date</font></b></a>
								</td>
								<td align="center" valign="middle" colspan="2" width="120">
									<a title="<digi:trn key="aim:PerpectiveofUser">Select perspective (Donor/ MoFED / implementing agency) of user entering the information</digi:trn>">											  
									<b><font color="white">Perspective</font></b></a>
								</td>
							</tr>
							<c:if test="${ !empty aimEditActivityForm.fundingDetails}">
							<c:forEach var="fundingDetail" items="${aimEditActivityForm.fundingDetails}"> 
						 	<c:if test="${fundingDetail.transactionType==1}">
						 	<tr>
								<td valign="bottom">
								<c:set var="index" value="${index+1}"/>
									<%--
									<html:checkbox name="fundingDetail" indexed="true" property="checked" value="true"/> --%>
									<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text" >
										<html:option value="1">Actual</html:option>	
										<html:option value="0">Planned</html:option>
									</html:select>
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
												<html:text name="fundingDetail" indexed="true" property="transactionDate" readonly="true" size="10"/>
											</td>
											<td align="left" vAlign="center">&nbsp;
					             			<a href='javascript:newWindow(2,<bean:write name="fundingDetail" property="index"/>)'>	
      					       			<img src="../ampTemplate/images/show-calendar.gif" border="0"></a>
											</td>
										</tr>
									</table>								
								</td>
								<td valign="bottom">
									<html:select name="fundingDetail" indexed="true" property="perspectiveCode" styleClass="inp-text" >
										<html:option value="DN">Donor</html:option>	
										<html:option value="MA">MOFED</html:option>
									</html:select>
								</td>
								<td>
									<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,1)">
								 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
									</a>
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
									<input class="buton" type="button" name="xx1" value="Delete Disbursements"
								   onclick="this.form.event.value='delDisbursements';addFundingDetail();">
								</td>--%>
								<td>
									<input class="buton" type="button" name="xx1" value="Add Disbursement"
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
		
			<table width="100%" cellpadding=0 cellspacing=0>
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
 						<a title="<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>">Expenditures</a>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="0" class=box-border-nopadding>
							<tr bgcolor="#003366" class="textalb">
								<td align="center" valign="middle" width="75">
									<b><font color="white">Planned/<br>Actual</font></b>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:AmountCommitted">Full amount of expected transfer, irrespective of the time required for the completion of disbursements, in thousands</digi:trn>">
									<b><font color="white">Amount</font></b></a>
								</td>
								<td align="center" valign="middle" width="170">
									<a title="<digi:trn key="aim:TransactionCurrency">Currency used in the project</digi:trn>">								   		  <b><font color="white">Currency</font></b></a>
								</td>
								<td align="center" valign="middle" width="120">
									<a title="<digi:trn key="aim:DateofExpenditure">Date of actual expenditure</digi:trn>">
									<b><font color="white">Planned/Actual<br>Expenditure Date</font></b></a>
								</td>
								<td align="center" valign="middle" colspan="2" width="120">
									<a title="<digi:trn key="aim:PerpectiveofUser">Select perspective (Donor/ MoFED / implementing agency) of user entering the information</digi:trn>">											  
									<b><font color="white">Perspective</font></b></a>
								</td>
							</tr>
							<c:if test="${ !empty aimEditActivityForm.fundingDetails}">
						 	<c:forEach var="fundingDetail" items="${aimEditActivityForm.fundingDetails}"> 
						 	<c:if test="${fundingDetail.transactionType==2}">
						 	<tr>
								<td colspan="6">
									<table width="100%" cellSpacing=1 cellPadding=0 vAlign="top" align="left">
										<tr bgcolor="#ffffff">

								<td valign="bottom">
								<c:set var="index" value="${index+1}"/>
									<html:select name="fundingDetail" indexed="true" property="adjustmentType" styleClass="inp-text" >
										<html:option value="1">Actual</html:option>	
										<html:option value="0">Planned</html:option>
									</html:select>
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
												<html:text name="fundingDetail" indexed="true" property="transactionDate" readonly="true" size="10"/>
											</td>
											<td align="left" vAlign="center">&nbsp;
			             					<a href='javascript:newWindow(2,<bean:write name="fundingDetail" property="index"/>)'>	
		   			          			<img src="../ampTemplate/images/show-calendar.gif" border="0"></a>
											</td>
										</tr>
									</table>																
								</td>
								<td valign="bottom">
									<html:select name="fundingDetail" indexed="true" property="perspectiveCode" styleClass="inp-text" >
										<html:option value="DN">Donor</html:option>	
										<html:option value="MA">MOFED</html:option>
									</html:select>
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
									</table>
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
								<td>
									<input class="buton" type="button" name="xx1" value="Add Expenditure"
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
									<input type="button" value="Save" class="inp-text" onclick="return addFunding()">
								</td>
								<td>
									<input type="reset" value="Reset" class="inp-text">
								</td>
								<td>
									<input type="button" value="Close" class="inp-text" onclick="closeWindow()">
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
