<%@ page pageEncoding="UTF-8" %>

<%@ page import="org.digijava.module.aim.form.EditActivityForm, java.util.*, org.digijava.module.aim.dbentity.*" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<% int indexC = 0; 
 	int indexD = 0; 
	int indexE = 0; 

 	EditActivityForm eaForm = (EditActivityForm) session.getAttribute("siteampdefaultaimEditActivityForm");
  	String defPers = (String) request.getAttribute("defPerspective"); 
  	String defCurr = (String) request.getAttribute("defCurrency"); %>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addRegionalFunding.do" method="post">

<input type="hidden" name="edit" value="true">

<input type="hidden" name="regFundAct">

<c:set var="translationDelete">
	<digi:trn key="btn:addRegionalFundingDelete">Delete</digi:trn>
</c:set>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class=box-border-nopadding>
	<tr>
    	<td width="100%" vAlign="top">
			<table width="100%" cellpadding=1 cellspacing=0 bgcolor="#ffffff" class="box-border-nopadding">
				<tr>
					<td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
						<digi:trn key="aim:fundingItemEditor">
							Funding Item Editor
						</digi:trn>						
					</td>
				</tr>
				<tr>
					<td vAlign="top" align="center" width="100%">
						<table width="100%" cellspacing=1 cellpadding=5 valign=top align=left>
							<tr>
								<td align="right" width="50">
									<digi:trn key="aim:region">Region</digi:trn>
								</td>
								<td align="left">
									<c:if test="${aimEditActivityForm.fundingRegionId == -1}">
										<html:select property="fundingRegionId" styleClass="inp-text">
											<html:option value="-1">
												--- 
												<digi:trn key="aim:addActivityRegionalFundingSelectRegion">
												Select a region
												</digi:trn>
												 ---
											</html:option>
											<html:optionsCollection name="aimEditActivityForm" property="fundingRegions" 
											value="ampRegionId" label="name" />										
										</html:select>										
									</c:if>
									<c:if test="${aimEditActivityForm.fundingRegionId != -1}">
										<html:select property="fundingRegionId" styleClass="inp-text" disabled="true">
											<html:option value="-1">--- Select a region ---</html:option>
											<html:optionsCollection name="aimEditActivityForm" property="fundingRegions" 
											value="ampRegionId" label="name" />									
										</html:select>
									</c:if>
								</td>								
							</tr>
							<tr><td colspan=2>
								<FONT color=blue><BIG>*</BIG>
									<digi:trn key="aim:pleaseEnterTheAmountInThousands">
										Please enter amount in thousands (000)
		  							</digi:trn>
								</FONT>
							</td></tr>
							<c:set var="translationAdd">
									<digi:trn key="aim:addActivityRegionalFundingAddFundingItem">Add</digi:trn>
							</c:set>
							<logic:notEmpty name="aimEditActivityForm" property="selectedLocs">
							<tr bgcolor="#f4f4f2">
								<td colspan="2" class="box-border-alt1">
										<span class="f-names">						
										<digi:trn key="aim:commitments">Commitments</digi:trn>
										 - (
										 <digi:trn key="aim:totalActualAllocation">Total Actual Allocation</digi:trn> 
											<%=eaForm.getTotalCommitments()%> 
											<%=eaForm.getCurrCode()%>
											)
										</span>
										<field:display name="Add Regional Funding Link" feature="Regional Funding">
										<a href="javascript:addCommitments()">${translationAdd }</a>
										</field:display>
										<br><br>
											<digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn key="aim:ActualFIE">Actual</digi:trn>&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:AmountFIE">Amount</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:CurrencyFIE">Currency</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:DateFIE">Date</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<digi:trn key="aim:PerspectiveFIE">Perspective</digi:trn>
												</logic:equal>
												<br>
										<div id="comm">

										<c:if test="${aimEditActivityForm.fundingRegionId != -1}">
											<c:forEach var="fundReg" items="${aimEditActivityForm.regionalFundings}">

											<c:if test="${aimEditActivityForm.fundingRegionId == fundReg.regionId}">

											<c:forEach var="comm" items="${fundReg.commitments}">

											
											<% String tNameBase = "comm_" + indexC + "_"; 
												String divName = "comm_" + indexC;
												indexC++;
												%>
												<% String field1 = tNameBase + "1"; 
												 String field2 = tNameBase + "2"; 
												 String field3 = tNameBase + "3"; 
												 String field4 = tNameBase + "4";
												 String field5 = tNameBase + "5"; %>
												 <div id="<%=divName%>">
												<select name="<%=field1%>" class="inp-text">
													<c:if test="${comm.adjustmentType == 1}">
														<option value="1" selected="true"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0" selected="true"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>					
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=field2%>" 
												value="<c:out value="${comm.transactionAmount}"/>" size="15" class='amt'>&nbsp;<select name="<%=field3%>" class="inp-text">
													<c:forEach var="currency" items="${aimEditActivityForm.currencies}">
														<c:if test="${comm.currencyCode == currency.currencyCode}">
															<option selected="true" value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
														<c:if test="${comm.currencyCode != currency.currencyCode}">
															<option value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
															<c:out value="${currency.currencyName}"/>
														</option>
													</c:forEach>
												</select>&nbsp;
												<input type="text" readonly="true" name="<%=field4%>" id="<%=field4%>" value="<c:out value="${comm.transactionDate}"/>" size="10"  class="inp-text">
												&nbsp;
												<a id="date1<%=field4%>" href='javascript:pickDateById("date1<%=field4%>","<%=field4%>")'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
												</a>
												&nbsp;
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<select name="<%=field5%>" value="<c:out value="${comm.perspectiveCode}"/>" class="inp-text">
														<c:forEach var="pers" items="${aimEditActivityForm.perspectives}">
															<c:if test="${comm.perspectiveCode == pers.code}">
																<option selected="true" value="<c:out value="${pers.code}"/>">
															</c:if>
															<c:if test="${comm.perspectiveCode != pers.code}">
																<option value="<c:out value="${pers.code}"/>">
															</c:if>
															<c:out value="${pers.name}"/>
															</option>	
														</c:forEach>
													</select>
													&nbsp;
												</logic:equal>
												<field:display name="Delete Regional Funding Button" feature="Regional Funding">
												<input type='button' value='${translationDelete}' class='inp-text' 
												onclick="removeCommitment('<%=divName%>')">
												</field:display>
												</div>
											</c:forEach>	
											</c:if>
											</c:forEach>
										</c:if>
										</div>
								</td>
							</tr>
                             <feature:display module="Funding" name="Disbursement Orders">
							<tr bgcolor="#ffffff">
								<td colspan="2" class="box-border">
										<span class="f-names"><digi:trn key="aim:disbursement">Disbursement</digi:trn> 
										- (
											<digi:trn key="aim:totalActualToDate">Total actual to date</digi:trn> 
											<%=eaForm.getTotalDisbursements()%>
											<%=eaForm.getCurrCode()%>)
										</span>
										<field:display name="Add Regional Funding Link" feature="Regional Funding">										
											<a href="javascript:addDisbursement()">${translationAdd }</a>
										</field:display>														
											<br><br>
												
												<digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn key="aim:ActualFIE">Actual</digi:trn>&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:AmountFIE">Amount</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:CurrencyFIE">Currency</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:DateFIE">Date</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<digi:trn key="aim:PerspectiveFIE">Perspective</digi:trn>
												</logic:equal>
												<br>
									<div id="disb">
										<c:if test="${aimEditActivityForm.fundingRegionId != -1}">
											<c:forEach var="fundReg" items="${aimEditActivityForm.regionalFundings}">

											<c:if test="${aimEditActivityForm.fundingRegionId == fundReg.regionId}">

											<c:forEach var="comm" items="${fundReg.disbursements}">

											
											<% String tNameBase = "disb_" + indexD + "_"; 
												String divName = "disb_" + indexD;
												indexD++;
												%>
												<% String field1 = tNameBase + "1"; 
												 String field2 = tNameBase + "2"; 
												 String field3 = tNameBase + "3"; 
												 String field4 = tNameBase + "4";
												 String field5 = tNameBase + "5"; %>
												 <div id="<%=divName%>">
												<select name="<%=field1%>" class="inp-text">
													<c:if test="${comm.adjustmentType == 1}">
														<option value="1" selected="true"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0" selected="true"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>					
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=field2%>" 
												value="<c:out value="${comm.transactionAmount}"/>" size="15" class='amt'>&nbsp;<select name="<%=field3%>" class="inp-text">
													<c:forEach var="currency" items="${aimEditActivityForm.currencies}">
														<c:if test="${comm.currencyCode == currency.currencyCode}">
															<option selected="true" value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
														<c:if test="${comm.currencyCode != currency.currencyCode}">
															<option value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
															<c:out value="${currency.currencyName}"/>
														</option>
													</c:forEach>
												</select>&nbsp;<input type="text" name="<%=field4%>" id="<%=field4%>" readonly="true" 
												value="<c:out value="${comm.transactionDate}"/>" size="10"  class="inp-text">
												&nbsp;
												<a id="date1<%=field4%>" href='javascript:pickDateById("date1<%=field4%>","<%=field4%>")'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
												</a>
												&nbsp;
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<select name="<%=field5%>" 
													class="inp-text">
														<c:forEach var="pers" items="${aimEditActivityForm.perspectives}">
															<c:if test="${comm.perspectiveCode == pers.code}">
																<option selected="true" value="<c:out value="${pers.code}"/>">
															</c:if>
															<c:if test="${comm.perspectiveCode != pers.code}">
																<option value="<c:out value="${pers.code}"/>">
															</c:if>
															<c:out value="${pers.name}"/>
															</option>	
														</c:forEach>				
													</select>
													&nbsp;
												</logic:equal>
												<field:display name="Delete Regional Funding Button" feature="Regional Funding">
												<input type='button' value='${translationDelete}' class='inp-text' 
												onclick="removeDisbursement('<%=divName%>')">
												</field:display>
												</div>
											</c:forEach>	
											</c:if>
											</c:forEach>
										</c:if>
									</div>
								</td>
							</tr>
                         </feature:display>
						<feature:display module="Funding" name="Expenditures">
                        	<tr>
								<td colspan="2" class="box-border-alt1">
											<span class="f-names"><digi:trn key="aim:expenditure">Expenditure</digi:trn> 
											- 	(
												<digi:trn key="aim:totalActualToDate">Total actual to date</digi:trn>
												<%=eaForm.getTotalExpenditures()%>
												<%=eaForm.getCurrCode()%>
												)
											</span>
											<field:display name="Add Regional Funding Link" feature="Regional Funding">	
												<a href="javascript:addExpenditure()">${translationAdd }</a>&nbsp;&nbsp;
											</field:display>
											<br><br>
												<digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn key="aim:ActualFIE">Actual</digi:trn>&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:AmountFIE">Amount</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:CurrencyFIE">Currency</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:DateFIE">Date</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<digi:trn key="aim:PerspectiveFIE">Perspective</digi:trn>
												</logic:equal>
												<br>
									<div id="expn">
										<c:if test="${aimEditActivityForm.fundingRegionId != -1}">
											<c:forEach var="fundReg" items="${aimEditActivityForm.regionalFundings}">

											<c:if test="${aimEditActivityForm.fundingRegionId == fundReg.regionId}">

											<c:forEach var="comm" items="${fundReg.expenditures}">

											
											<% String tNameBase = "expn_" + indexE + "_"; 
												String divName = "expn_" + indexE;
												indexE++;
												%>
												<% String field1 = tNameBase + "1"; 
												 String field2 = tNameBase + "2"; 
												 String field3 = tNameBase + "3"; 
												 String field4 = tNameBase + "4";
												 String field5 = tNameBase + "5"; %>
												 <div id="<%=divName%>">
												<select name="<%=field1%>" class="inp-text">
													<c:if test="${comm.adjustmentType == 1}">
														<option value="1" selected="true"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0" selected="true"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>					
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=field2%>" 
												value="<c:out value="${comm.transactionAmount}"/>" size="15" class='amt'>&nbsp;<select name="<%=field3%>" class="inp-text">
													<c:forEach var="currency" items="${aimEditActivityForm.currencies}">
														<c:if test="${comm.currencyCode == currency.currencyCode}">
															<option selected="true" value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
														<c:if test="${comm.currencyCode != currency.currencyCode}">
															<option value="<c:out value="${currency.currencyCode}"/>">
														</c:if>
															<c:out value="${currency.currencyName}"/>
														</option>
													</c:forEach>
												</select>&nbsp;<input type="text" name="<%=field4%>" id="<%=field4%>" readonly="true" 
												value="<c:out value="${comm.transactionDate}"/>" size="10"  class="inp-text">&nbsp;
												<a id="date1<%=field4%>" href='javascript:pickDateById("date1<%=field4%>","<%=field4%>")'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
												</a>
												&nbsp;
												<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
													<select name="<%=field5%>" value="<c:out value="${comm.perspectiveCode}"/>" class="inp-text">
														<c:forEach var="pers" items="${aimEditActivityForm.perspectives}">
															<c:if test="${comm.perspectiveCode == pers.code}">
																<option selected="true" value="<c:out value="${pers.code}"/>">
															</c:if>
															<c:if test="${comm.perspectiveCode != pers.code}">
																<option value="<c:out value="${pers.code}"/>">
															</c:if>
															<c:out value="${pers.name}"/>
															</option>	
														</c:forEach>
													</select>
													&nbsp;
												</logic:equal>
												<field:display name="Delete Regional Funding Button" feature="Regional Funding">
												<input type='button' value='${translationDelete}' class='inp-text' 
												onclick="removeExpenditure('<%=divName%>')">
												</field:display>
												</div>
											</c:forEach>	
											</c:if>
											</c:forEach>
										</c:if>									
									</div>
								</td>									
							</tr>
                            </feature:display>
							<tr>
								<td colspan="2" align="center">
									<c:set var="translationSave">
										<digi:trn key="btn:addRegionalFundingSave">Save</digi:trn>
									</c:set>
									<c:set var="translationReset">
										<digi:trn key="btn:addRegionalFundingReset">Reset</digi:trn>
									</c:set>
									<c:set var="translationClose">
										<digi:trn key="btn:addRegionalFundingClose">Close</digi:trn>
									</c:set>
									
									<table cellPadding=3>
										<tr>
											<td>
												<input type="button" value="${translationSave}" class="inp-text" onclick="return addRegionalFunding()">
											</td>
											<td>
												<input type="reset" value="${translationReset}" class="inp-text">
											</td>
											<td>
												<input type="button" value="${translationClose}" class="inp-text" onclick="closeWindow()">
											</td>
										</tr>
									</table>
								</td>									
							</tr>							
							</logic:notEmpty>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>	
</table>
</digi:form>


<script language="JavaScript">


var numComm = <%=indexC%>;
var numExpn = <%=indexE%>;
var numDisb = <%=indexD%>;

	
function trim(s) {
	return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
  }

function load() {
}

function unload() {
}

function closeWindow() {
	window.close();
}

var tempComm = numComm;
var tempDisb = numDisb;
var tempExpn = numExpn;

function addCommitments() {
	var ni = document.getElementById('comm');
	var divname = "comm_" + numComm;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<select name='comm_" + numComm + "_1' class='inp-text'>";
	s += "<option value='1'><digi:trn key="aim:Actual">Actual</digi:trn></option>";
	s += "<option value='0'><digi:trn key="aim:Planned">Planned</digi:trn></option>";
	s += "</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	s += "<input type='text' name='comm_" + numComm + "_2' size='15' class='amt'>&nbsp;";
	s += "<select name='comm_" + numComm + "_3' class='inp-text'>&nbsp;";
	
	<% Collection col = eaForm.getCurrencies();
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
			AmpCurrency curr = (AmpCurrency) itr.next();	
			if (curr != null){
				if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) { %>
					s += "<option value='<%=curr.getCurrencyCode()%>' selected='true'><%=curr.getCurrencyName()%></option>";
				<% } else { %>
					s += "<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>";				  			
				<% }
			}
		 }%>
	
	s += "</select>&nbsp;";
	s += "<input type='text' name='comm_" + numComm + "_4' id='comm_" + numComm + "_4' size='10' class='inp-text'>&nbsp;";
	s += "<a id='date1comm_"+numComm+"_4' href='javascript:pickDateById(\"date1comm_"+numComm+"_4\",\"comm_"+numComm+"_4\")'><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border=0></a>&nbsp;";
	
	<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
	s += "<select name='comm_" + numComm + "_5' class='inp-text'>";
	<% itr = eaForm.getPerspectives().iterator();
		while (itr.hasNext()) { 
			AmpPerspective pers = (AmpPerspective) itr.next();
			if (pers.getCode().equalsIgnoreCase(defPers)) { %>
				s += "<option value='<%=pers.getCode()%>' selected='true'><%=pers.getNameTrimmedForTrn()%></option>";
			<% } else { %>
				s += "<option value='<%=pers.getCode()%>'><%=pers.getNameTrimmedForTrn()%></option>";
			<% } 
		} %>
	s += "</select>&nbsp;";
	</logic:equal>
	
	s += "<field:display name='Delete Regional Funding Button' feature='Regional Funding'>" + 
	"<input type='button' value='${translationDelete}' class='inp-text' onclick=removeCommitment('" + divname + "')>"+
	"</field:display>" +
	"<br>";
	
	newdiv.innerHTML = s;
	ni.appendChild(newdiv);
	numComm++;
	tempComm++;
}

function removeCommitment(divname)
{
var d = document.getElementById('comm');
var olddiv = document.getElementById(divname);
d.removeChild(olddiv);
tempComm--;
}


function addDisbursement() {
	var ni = document.getElementById('disb');
	var divname = "disb_" + numDisb;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<select name='disb_" + numDisb + "_1' class='inp-text'>";
	s += "<option value='1'><digi:trn key="aim:Actual">Actual</digi:trn></option>";
	s += "<option value='0'><digi:trn key="aim:Planned">Planned</digi:trn></option>";
	s += "</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	s += "<input type='text' name='disb_" + numDisb + "_2' size='15' class='amt'>&nbsp;";
	s += "<select name='disb_" + numDisb + "_3' class='inp-text'>&nbsp;";
	
	<% col = eaForm.getCurrencies();
		itr = col.iterator();
		while (itr.hasNext()) {
			AmpCurrency curr = (AmpCurrency) itr.next();	
			if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) { %>
				s += "<option value='<%=curr.getCurrencyCode()%>' selected='true'><%=curr.getCurrencyName()%></option>";
			<% } else { %>
				s += "<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>";				  			
			<% }
		 }%>
	
	s += "</select>&nbsp;";
	s += "<input type='text' name='disb_" + numDisb + "_4' id='disb_" + numDisb + "_4' size='10' class='inp-text'>&nbsp;";
	s += "<a id='date1disb_"+numDisb+"_4' href='javascript:pickDateById(\"date1disb_"+numDisb+"_4\",\"disb_"+numDisb+"_4\")'><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border=0></a>&nbsp;";

	<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
	s += "<select name='disb_" + numDisb + "_5' class='inp-text'>";
	<% itr = eaForm.getPerspectives().iterator();
		while (itr.hasNext()) { 
			AmpPerspective pers = (AmpPerspective) itr.next();
			if (pers.getCode().equalsIgnoreCase(defPers)) { %>10
				s += "<option value='<%=pers.getCode()%>' selected='true'><%=pers.getNameTrimmedForTrn()%></option>";
			<% } else { %>
				s += "<option value='<%=pers.getCode()%>'><%=pers.getNameTrimmedForTrn()%></option>";
			<% } 
		} %>
	s += "</select>&nbsp;";
	</logic:equal>
	
	s += "<field:display name='Delete Regional Funding Button' feature='Regional Funding'>" + 
	"<input type='button' value='${translationDelete}' class='inp-text' onclick=removeDisbursement('" + divname + "')>"+
	"</field:display>" +
	"<br>";
	
	newdiv.innerHTML = s;
	ni.appendChild(newdiv);
	numDisb++;
	tempDisb++;
}

function removeDisbursement(divname)
{
var d = document.getElementById('disb');
var olddiv = document.getElementById(divname);
d.removeChild(olddiv);
tempDisb--;
}


function addExpenditure() {
	var ni = document.getElementById('expn');
	var divname = "expn_" + numExpn;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<select name='expn_" + numExpn + "_1' class='inp-text'>";
	s += "<option value='1'><digi:trn key="aim:Actual">Actual</digi:trn></option>";
	s += "<option value='0'><digi:trn key="aim:Planned">Planned</digi:trn></option>";
	s += "</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	s += "<input type='text' name='expn_" + numExpn + "_2' size='15' class='amt'>&nbsp;";
	s += "<select name='expn_" + numExpn + "_3' class='inp-text'>&nbsp;";
	
	<% col = eaForm.getCurrencies();
		itr = col.iterator();
		while (itr.hasNext()) {
			AmpCurrency curr = (AmpCurrency) itr.next();	
			if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) { %>
				s += "<option value='<%=curr.getCurrencyCode()%>' selected='true'><%=curr.getCurrencyName()%></option>";
			<% } else { %>
				s += "<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>";				  			
			<% }
		 }%>
	
	s += "</select>&nbsp;";
	s += "<input type='text' name='expn_" + numExpn + "_4' id='expn_" + numExpn + "_4' size='10' class='inp-text'>&nbsp;";
	s += "<a id='date1expn_"+numExpn+"_4' href='javascript:pickDateById(\"date1expn_"+numExpn+"_4\",\"expn_"+numExpn+"_4\")'><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border=0></a>&nbsp;";
	
	<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
	s += "<select name='expn_" + numExpn + "_5' class='inp-text'>";
	<% itr = eaForm.getPerspectives().iterator();
		while (itr.hasNext()) { 
			AmpPerspective pers = (AmpPerspective) itr.next();
			if (pers.getCode().equalsIgnoreCase(defPers)) { %>
				s += "<option value='<%=pers.getCode()%>' selected='true'><%=pers.getNameTrimmedForTrn() %></option>";
			<% } else { %>
				s += "<option value='<%=pers.getCode()%>'><%=pers.getNameTrimmedForTrn()%></option>";
			<% } 
		} %>
	s += "</select>&nbsp;";
	</logic:equal>
	
	s += "<field:display name='Delete Regional Funding Button' feature='Regional Funding'>" + 
	"<input type='button' value='${translationDelete}' class='inp-text' onclick=removeExpenditure('" + divname + "')>"+
	"</field:display>" +
	"<br>";
	
	newdiv.innerHTML = s;
	ni.appendChild(newdiv);
	numExpn++;
	tempExpn++;
}

function removeExpenditure(divname)
{
var d = document.getElementById('expn');
var olddiv = document.getElementById(divname);
d.removeChild(olddiv);
tempExpn--;
}

function addRegionalFunding() {
	var flag = validate();
	if (flag == true) {
		document.aimEditActivityForm.target = window.opener.name;
		document.aimEditActivityForm.regFundAct.value = "update";
	   document.aimEditActivityForm.submit();
		window.close();			  
	}
	return flag;
}

function validate() {
	if (document.aimEditActivityForm.fundingRegionId.value < 1)	 {
		alert("Select a region");
		document.aimEditActivityForm.fundingRegionId.focus();
		return false;
	}
		  
	var x = document.aimEditActivityForm;

	if (tempComm == 0) {
		alert ("Commitment not entered.");
		return false;			  
	}

	if (tempExpn > 0 && tempDisb == 0) {
		alert ("Expenditure entered without entering disbursements.");
		return false;			  			  
	}
	
	for (index = 0;index < x.elements.length;index++) {
		var str = x.elements[index].name;
		if (str.match("comm_[0-9]*_2")) {
			// validate amount
			if (trim(x.elements[index].value) == "") {
				alert ("Amount not entered.");
				x.elements[index].focus();
				return false;
			}
			if (checkAmount(x.elements[index].value) == false) {
				alert ("Invalid amount entered.");
				x.elements[index].focus();
				return false;
			}
			
		} else if (str.match("comm_[0-9]*_4")) { 
			// validate date	  
			if (trim(x.elements[index].value) == "") {
				alert ("Date not entered.");
				x.elements[index].focus();
				return false;
			}
		} else if (str.match("disb_[0-9]*_2")) { 
			// validate amount
			if (trim(x.elements[index].value) == "") {
				alert ("Amount not entered.");
				x.elements[index].focus();
				return false;				  
			}
			if (checkAmount(x.elements[index].value) == false) {
				alert ("Invalid amount entered.");
				x.elements[index].focus();
				return false;
			}			
		} else if (str.match("disb_[0-9]*_4")) { 
			if (trim(x.elements[index].value) == "") {
				alert ("Date not entered.");
				x.elements[index].focus();
				return false;
			}				  
		} else if (str.match("expn_[0-9]*_2")) { 
			// validate amount
			if (trim(x.elements[index].value) == "") {
				alert ("Amount not entered.");
				x.elements[index].focus();
				return false;
			}
			if (checkAmount(x.elements[index].value) == false) {
				alert ("Invalid amount entered.");
				x.elements[index].focus();
				return false;
			}			
		} else if (str.match("expn_[0-9]*_4")) { 
			if (trim(x.elements[index].value) == "") {
				alert ("Date not entered.");
				x.elements[index].focus();
				return false;
			}				  
		}
				  
	}
	return true;	
}

	-->
</script>
