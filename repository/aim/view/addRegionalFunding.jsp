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
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp"  />
</script>
<script language="JavaScript" type="text/javascript">
    function addActionToURL(actionName){
        var fullURL=document.URL;
        var lastSlash=fullURL.lastIndexOf("/");
        var partialURL=fullURL.substring(0,lastSlash);
        return partialURL+"/"+actionName;
    }

    function changeCurrency(){
       var currency=document.getElementById("regFundCurrCode").value;
       var url=addActionToURL('getFundingTotals.do')+'?edit=true&regFundingPageCurrCode='+currency+'&isRegcurr=true'+'&isStepPage=false';
       var async=new Asynchronous();
       async.complete=buildFundingTotals;
	   async.call(url);
    }
     function buildFundingTotals(status, statusText, responseText, responseXML){
        var root=responseXML.getElementsByTagName('total')[0];
        var comm=document.getElementById("total_comm");
        var disb=document.getElementById("total_disb");
        var expn=document.getElementById("total_expn");
        var curr=root.getAttribute("curr");
        comm.innerHTML="<digi:trn> Commitments - (Total Actual Allocation</digi:trn> "
            +root.getAttribute("comm")+' '+curr+')';
        disb.innerHTML="<digi:trn> Disbursement - (Total actual to date</digi:trn> "
            +root.getAttribute("disb")+' '+curr+')';
        expn.innerHTML="<digi:trn> Expenditure - (Total actual to date</digi:trn>   " +root.getAttribute("expn")+' '+curr+')';

    }

</script>

<% int indexC = 0; 
 	int indexD = 0; 
	int indexE = 0; 

 	EditActivityForm eaForm = (EditActivityForm) session.getAttribute("siteampdefaultaimEditActivityForm");
  	String defPers = (String) request.getAttribute("defPerspective"); 
  	String defCurr = (String) request.getAttribute("defCurrency"); %>


<jsp:include page="scripts/newCalendar.jsp"  />

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
								
									<c:if test="${aimEditActivityForm.funding.fundingRegionId == -1}">
										<html:select styleId="fundingRegionId" property="funding.fundingRegionId" styleClass="inp-text">
											<html:option value="-1">
												--- 
												<digi:trn key="aim:addActivityRegionalFundingSelectRegion">
												Select a region
												</digi:trn>
												 ---
											</html:option>
											<html:optionsCollection name="aimEditActivityForm" property="funding.fundingRegionsUnique" 
											value="id" label="name" />										
										</html:select>										
									</c:if>
									<c:if test="${aimEditActivityForm.funding.fundingRegionId != -1}">
										<html:select property="funding.fundingRegionId" styleClass="inp-text" styleId="fundingRegionId" disabled="true">
											<html:option value="-1">--- Select a region ---</html:option>
											<html:optionsCollection name="aimEditActivityForm" property="funding.fundingRegions" 
											value="id" label="name" />									
										</html:select>
									</c:if>
								</td>								
							</tr>
                           <tr>
                            <td colspan="2">
                               <digi:trn>Select currency </digi:trn>
                               <html:select property="funding.fundingCurrCode" styleClass="inp-text" onchange="changeCurrency()" styleId="regFundCurrCode">
                                <c:forEach var="currency" items="${aimEditActivityForm.funding.validcurrencies}">
                                    <c:if test="${currency.currencyCode!=aimEditActivityForm.funding.fundingCurrCode}">
                                        <option value="<c:out value="${currency.currencyCode}"/>">
                                    </c:if>
                                    <c:if test="${currency.currencyCode==aimEditActivityForm.funding.fundingCurrCode}">
                                        <option value="<c:out value="${currency.currencyCode}"/>" selected="selected">
                                        </c:if>
                                        <c:out value="${currency.currencyName}" />
                                    </option>
                                </c:forEach>
                                </html:select>

							</td>

						</tr>
							<tr><td colspan=2>
								<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
								<FONT color=blue><BIG>*</BIG>
									<digi:trn key="aim:pleaseEnterTheAmountInThousands">
										Please enter amount in thousands (000)
		  							</digi:trn>
								</FONT>
								</gs:test>
							</td></tr>
							<c:set var="translationAdd">
									<digi:trn key="aim:addActivityRegionalFundingAddFundingItem">Add</digi:trn>
							</c:set>
							<logic:notEmpty name="aimEditActivityForm" property="location.selectedLocs">
							<tr bgcolor="#f4f4f2">
								<td colspan="2" class="box-border-alt1">
                                    <span class="f-names" id="total_comm">
                                        <digi:trn key="aim:commitmentsTotalActAllocation">Commitments - (Total Actual Allocation</digi:trn>
                                    ${sessionScope.totalComm}  ${aimEditActivityForm.fundingCurrCode} ) </span>
										<field:display name="Add Regional Funding Link" feature="Regional Funding">
										<a href="javascript:addCommitments()">${translationAdd }</a>
										</field:display>
										<br><br>
											<digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn key="aim:ActualFIE">Actual</digi:trn>&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:AmountFIE">Amount</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:CurrencyFIE">Currency</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:DateFIE">Date</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<br>
										<div id="comm">

										<c:if test="${aimEditActivityForm.funding.fundingRegionId != -1}">
											<c:forEach var="fundReg" items="${aimEditActivityForm.funding.regionalFundings}">

											<c:if test="${aimEditActivityForm.funding.fundingRegionId == fundReg.regionId}">

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
														<option value="1" selected="selected"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0" selected="selected"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>					
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=field2%>" 
												value="<c:out value="${comm.transactionAmount}"/>" size="15" class='amt'>&nbsp;<select name="<%=field3%>" class="inp-text">
													<c:forEach var="currency"
													items="${aimEditActivityForm.funding.validcurrencies}">
													<c:if test="${comm.currencyCode == currency.currencyCode}">
														<option selected="selected"
															value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:if test="${comm.currencyCode != currency.currencyCode}">
														<option value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:out value="${currency.currencyName}" />
													</option>
												</c:forEach>
												</select>&nbsp;
												<input type="text" readonly="true" name="<%=field4%>" id="<%=field4%>" value="<c:out value="${comm.transactionDate}"/>" size="10"  class="inp-text">
												&nbsp;
												<a id="date1<%=field4%>" href='javascript:pickDateById("date1<%=field4%>","<%=field4%>")'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
												</a>
												&nbsp;
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
                             <feature:display module="Funding" name="Disbursement">
							<tr bgcolor="#ffffff">
								<td colspan="2" class="box-border">
										<span class="f-names" id="total_disb">
                                            <digi:trn key="aim:disbursementTotalActToDate"> Disbursement - (Total actual to date </digi:trn>
							${sessionScope.totalDisb}  ${aimEditActivityForm.fundingCurrCode} )
										</span>
										<field:display name="Add Regional Funding Link" feature="Regional Funding">										
											<a href="javascript:addDisbursement()">${translationAdd }</a>
										</field:display>														
											<br><br>
												
												<digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn key="aim:ActualFIE">Actual</digi:trn>&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:AmountFIE">Amount</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:CurrencyFIE">Currency</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:DateFIE">Date</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<br>
									<div id="disb">
										<c:if test="${aimEditActivityForm.funding.fundingRegionId != -1}">
											<c:forEach var="fundReg" items="${aimEditActivityForm.funding.regionalFundings}">

											<c:if test="${aimEditActivityForm.funding.fundingRegionId == fundReg.regionId}">

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
														<option value="1" selected="selected"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0" selected="selected"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>					
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=field2%>" 
												value="<c:out value="${comm.transactionAmount}"/>" size="15" class='amt'>&nbsp;<select name="<%=field3%>" class="inp-text">
													<c:forEach var="currency"
													items="${aimEditActivityForm.funding.validcurrencies}">
													<c:if test="${comm.currencyCode == currency.currencyCode}">
														<option selected="selected"
															value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:if test="${comm.currencyCode != currency.currencyCode}">
														<option value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:out value="${currency.currencyName}" />
													</option>
												</c:forEach>
												</select>&nbsp;<input type="text" name="<%=field4%>" id="<%=field4%>" readonly="true" 
												value="<c:out value="${comm.transactionDate}"/>" size="10"  class="inp-text">
												&nbsp;
												<a id="date1<%=field4%>" href='javascript:pickDateById("date1<%=field4%>","<%=field4%>")'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
												</a>
												&nbsp;
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
									<span class="f-names" id="total_expn">
                                    <digi:trn key="aim:expenditureTotalActToDate"> Expenditure - (Total actual to date</digi:trn>
                                    ${sessionScope.totalExpn}   ${aimEditActivityForm.fundingCurrCode})</span>
											<field:display name="Add Regional Funding Link" feature="Regional Funding">	
												<a href="javascript:addExpenditure()">${translationAdd }</a>&nbsp;&nbsp;
											</field:display>
											<br><br>
												<digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn key="aim:ActualFIE">Actual</digi:trn>&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:AmountFIE">Amount</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:CurrencyFIE">Currency</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<digi:trn key="aim:DateFIE">Date</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<br>
									<div id="expn">
										<c:if test="${aimEditActivityForm.funding.fundingRegionId != -1}">
											<c:forEach var="fundReg" items="${aimEditActivityForm.funding.regionalFundings}">

											<c:if test="${aimEditActivityForm.funding.fundingRegionId == fundReg.regionId}">

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
														<option value="1" selected="selected"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1"><digi:trn key="aim:Actual">Actual</digi:trn></option>
														<option value="0" selected="selected"><digi:trn key="aim:Planned">Planned</digi:trn></option>
													</c:if>					
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="<%=field2%>" 
												value="<c:out value="${comm.transactionAmount}"/>" size="15" class='amt'>&nbsp;<select name="<%=field3%>" class="inp-text">
													<c:forEach var="currency"
													items="${aimEditActivityForm.funding.validcurrencies}">
													<c:if test="${comm.currencyCode == currency.currencyCode}">
														<option selected="selected"
															value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:if test="${comm.currencyCode != currency.currencyCode}">
														<option value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:out value="${currency.currencyName}" />
													</option>
												</c:forEach>
												</select>&nbsp;<input type="text" name="<%=field4%>" id="<%=field4%>" readonly="true" 
												value="<c:out value="${comm.transactionDate}"/>" size="10"  class="inp-text">&nbsp;
												<a id="date1<%=field4%>" href='javascript:pickDateById("date1<%=field4%>","<%=field4%>")'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
												</a>
												&nbsp;
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
	
	<% Collection col = eaForm.getFunding().getValidcurrencies();
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
	s += "<input type='text' name='comm_" + numComm + "_4' id='comm_" + numComm + "_4' size='10' class='inp-text' readonly='true'>&nbsp;";
	s += "<a id='date1comm_"+numComm+"_4' href='javascript:pickDateById(\"date1comm_"+numComm+"_4\",\"comm_"+numComm+"_4\")'><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border=0></a>&nbsp;";
	
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
	
	<% col = eaForm.getFunding().getValidcurrencies();
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
	s += "<input type='text' name='disb_" + numDisb + "_4' id='disb_" + numDisb + "_4' size='10' class='inp-text' readonly='true'>&nbsp;";
	s += "<a id='date1disb_"+numDisb+"_4' href='javascript:pickDateById(\"date1disb_"+numDisb+"_4\",\"disb_"+numDisb+"_4\")'><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border=0></a>&nbsp;";
	
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
	
	<% col = eaForm.getFunding().getValidcurrencies();
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
	s += "<input type='text' name='expn_" + numExpn + "_4' id='expn_" + numExpn + "_4' size='10' class='inp-text' readonly='true'>&nbsp;";
	s += "<a id='date1expn_"+numExpn+"_4' href='javascript:pickDateById(\"date1expn_"+numExpn+"_4\",\"expn_"+numExpn+"_4\")'><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border=0></a>&nbsp;";
	
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
  var msgEnterAmount="<digi:trn key="aim:addRegionalFunding:errmsg:enterAmount">Amount not entered.</digi:trn>";
  var msgInvalidAmount="<digi:trn key="aim:addRegionalFunding:errmsg:invalidAmount">Invalid amount entered.</digi:trn>";
  var msgEnterDate="<digi:trn key="aim:addRegionalFunding:errmsg:enterDate">Date not entered.</digi:trn>";
  var msgSelectRegion="<digi:trn key="aim:addRegionalFunding:errmsg:selectRegion">Please Select a region.</digi:trn>";
  var msgEnterCommitment="<digi:trn key="aim:addRegionalFunding:errmsg:enterCommitment">Commitment not entered.</digi:trn>";
  var msgEnterExpenditure="<digi:trn key="aim:addRegionalFunding:errmsg:enterExpenditure">Expenditure entered without entering disbursements.</digi:trn>";

	if (document.getElementById('fundingRegionId').value < 1)	 {
		alert(msgSelectRegion);
		document.getElementById('fundingRegionId').focus();
		return false;
	}
		 
		  
	var x = document.aimEditActivityForm;

	if (tempComm == 0) {
		alert (msgEnterCommitment);
		return false;			  
	}

	if (tempExpn > 0 && tempDisb == 0) {
		alert (msgEnterExpenditure);
		return false;			  			  
	}
	
	for (index = 0;index < x.elements.length;index++) {
		var str = x.elements[index].name;
		if (str.match("comm_[0-9]*_2")) {
			// validate amount
			if (trim(x.elements[index].value) == "") {
				alert (msgEnterAmount);
				x.elements[index].focus();
				return false;
			}
			if (checkAmount(x.elements[index].value) == false) {
				alert (msgInvalidAmount);
				x.elements[index].focus();
				return false;
			}
			
		} else if (str.match("comm_[0-9]*_4")) { 
			// validate date	  
			if (trim(x.elements[index].value) == "") {
				alert (msgEnterDate);
				x.elements[index].focus();
				return false;
			}
		} else if (str.match("disb_[0-9]*_2")) { 
			// validate amount
			if (trim(x.elements[index].value) == "") {
				alert (msgEnterAmount);
				x.elements[index].focus();
				return false;				  
			}
			if (checkAmount(x.elements[index].value) == false) {
				alert (msgInvalidAmount);
				x.elements[index].focus();
				return false;
			}			
		} else if (str.match("disb_[0-9]*_4")) { 
			if (trim(x.elements[index].value) == "") {
				alert (msgEnterDate);
				x.elements[index].focus();
				return false;
			}				  
		} else if (str.match("expn_[0-9]*_2")) { 
			// validate amount
			if (trim(x.elements[index].value) == "") {
				alert (msgEnterAmount);
				x.elements[index].focus();
				return false;
			}
			if (checkAmount(x.elements[index].value) == false) {
				alert (msgInvalidAmount);
				x.elements[index].focus();
				return false;
			}			
		} else if (str.match("expn_[0-9]*_4")) { 
			if (trim(x.elements[index].value) == "") {
				alert (msgEnterDate);
				x.elements[index].focus();
				return false;
			}				  
		}
				  
	}
	return true;	
}

	-->
</script>
