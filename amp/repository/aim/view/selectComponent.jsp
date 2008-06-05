<%@ page pageEncoding="UTF-8"%>

<%@ page
	import="org.digijava.module.aim.form.EditActivityForm,java.util.*,org.digijava.module.aim.dbentity.*"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<%
	EditActivityForm eaForm = (EditActivityForm) session.getAttribute("siteampdefaultaimEditActivityForm");
	String defPers = (String) request.getAttribute("defPerspective");
	String defCurr = (String) request.getAttribute("defCurrency");
	int indexC = 0;
	int indexD = 0;
	int indexE = 0;
%>



<script type="text/javascript">
function switchType(){

  <digi:context name="switchType" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=switchType" />
  document.aimEditActivityForm.action = "<%= switchType%>";
  document.aimEditActivityForm.submit();
}

function switchComponent(){
if (document.aimEditActivityForm.componentTitle.value!=-1){
  	 document.getElementById('tblId').style.visibility="visible";                         		
     document.getElementById('tblId').style.position="relative";  
     document.aimEditActivityForm.newCompoenentName.value="";
		document.getElementById("txtTitle").innerHTML="<digi:trn key="aim:msgAddfunding">Add funding information for </digi:trn> " + document.aimEditActivityForm.componentTitle.value;
  
  }else{
  	 document.getElementById('tblId').style.visibility="hidden";                         		
     document.getElementById('tblId').style.position="absolute";  
  
  }
  
  if (document.aimEditActivityForm.selectedType.value==-1){
   document.aimEditActivityForm.newCompoenentName.disabled=true;
    document.aimEditActivityForm.newCompoenentName.style.bgColor="#EEEEEE";
  }else{
   document.aimEditActivityForm.newCompoenentName.disabled=false;
   document.aimEditActivityForm.newCompoenentName.style.bgColor="#EEEEEE";
  }
  
}


function addComponent(){
	<digi:context name="addNewComponent" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=addNewComponent" />

	if (document.aimEditActivityForm.newCompoenentName.value==""){
		var msg="<digi:trn key="aim:msgErrorNoName">You have to enter the component name</digi:trn>"
		alert(msg);
		return false;
	}
	
	if (document.aimEditActivityForm.selectedType.value=="-1"){
		var msg="<digi:trn key="aim:msgErrorSelctType">You have to select the component type</digi:trn>"
		alert(msg);
		return false;
	}
  	document.aimEditActivityForm.action = "<%= addNewComponent%>";
  	document.aimEditActivityForm.submit();
}
</script>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />


<digi:instance property="aimEditActivityForm" />


<digi:form action="/showAddComponent.do" method="post">
	<html:hidden property="componentReset" value="false" />
	<html:hidden property="componentId" />


	<input type="hidden" name="selectedDate">


	<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%"
		class=box-border-nopadding>
		<tr>
			<td align=left vAlign=top>
			<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%"
				class=box-border-nopadding>
				<tr bgcolor="#006699">
					<td vAlign="center" width="100%" align="center" class="textalb"
						height="20"><a
						title="<digi:trn key="aim:ComponentofProject">A smaller sub project of a donor approved project</digi:trn>">
					<digi:trn key="aim:addComponent">Add Component</digi:trn></a></td>
				</tr>
				<tr>
					<td align="center" bgcolor=#ECF3FD>
					<table cellSpacing=2 cellPadding=2>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td align="right">Component Type</td>
							<td><html:select property="selectedType"
								styleClass="inp-text" onchange="switchType()">
								<html:option value="-1">
									<digi:trn key="aim:selecType">-Select Type-</digi:trn>
								</html:option>
								<html:optionsCollection property="allCompsType" label="name"
									value="type_id" />
							</html:select></td>
						</tr>
						<tr>
							<td align="right"><a
								title="<digi:trn key="aim:TitleforComponent">Title of the project component specified</digi:trn>">
							<digi:trn key="aim:title">Title</digi:trn> </ a> </td>
							<td><html:select property="componentTitle"
								styleClass="inp-text" onchange="switchComponent()">
								<html:option value="-1">
									<digi:trn key="aim:selectComponent">-Select Component-</digi:trn>
								</html:option>
								<c:forEach var="comp" items="${aimEditActivityForm.allComps}">
									<html:option value="${comp.name}">${comp.shortName}</html:option>
								</c:forEach>
							</html:select></td>
						</tr>
						<tr>
						
							<td align="right">Add a New Compoenent</td>
							<td><html:text property="newCompoenentName" onchange="componentTitle.value=-1;switchComponent()"  /> <html:button
								property="addNewCompoenent" onclick="addComponent()">
								<digi:trn key="aim:add">Add</digi:trn>
							</html:button></td>
						</tr>
					</table>




					<table id="tblId"
						style="visibility: hidden; position: absolute; width: 100%;margin-top:10px;"
						name="tblId">
						<tr>
						  <td colspan=2 height="24" align="left" bgcolor="#FFFFFF"><div style="margin:4px" class="f-names" id="txtTitle"></div></td>
					  </tr>
						<tr>


							<td colspan=2><FONT color=blue><BIG>*</BIG> <digi:trn
								key="aim:pleaseEnterTheAmountInThousands">
												Please enter amount in thousands (000)		  									</digi:trn> </FONT></td>
						</tr>
						<tr bgcolor="#f4f4f2">
							<td colspan="2" class="box-border-alt1"><span
								class="f-names"> <digi:trn
								key="aim:commitmentsTotalActAllocation">Commitments - (Total Actual Allocation</digi:trn>
							<%=eaForm.getTotalCommitments()%> <%=eaForm.getCurrCode()%> ) </span> <!-- Commented added by mouhamad for burkina AMP-2709 -->
							<!-- <digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn key="aim:ActualFIE">Actual</digi:trn> -->&nbsp;&nbsp;&nbsp;
							<a href="javascript:addCommitments()"><digi:trn key="btn:add">Add</digi:trn></a><br>
							<br>
							<digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn
								key="aim:ActualFIE">Actual</digi:trn>&nbsp;&nbsp;&nbsp; <digi:trn
								key="aim:AmountFIE">Amount</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<digi:trn key="aim:CurrencyFIE">Currency</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<digi:trn key="aim:DateFIE">Date</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							
							<div id="comm"><c:if
								test="${aimEditActivityForm.componentId != -1}">
								<c:forEach var="fundComp"
									items="${aimEditActivityForm.selectedComponents}">
									<c:if
										test="${aimEditActivityForm.componentId == fundComp.componentId}">
										<c:forEach var="comm" items="${fundComp.commitments}">
											<%
												String tNameBase = "comm_" + indexC + "_";
												String divName = "comm_" + indexC;
												indexC++;
											%>
											<%
												String field1 = tNameBase + "1";
												String field2 = tNameBase + "2";
												String field3 = tNameBase + "3";
												String field4 = tNameBase + "4";
												String field5 = tNameBase + "5";
												String field6 = tNameBase + "6";
											%>
											<div id="<%=divName%>"><select name="<%=field1%>"
												class="inp-text">
												<c:if test="${comm.adjustmentType == 1}">
													<option value="1" selected="true"><digi:trn
														key="aim:actual">Actual</digi:trn></option>
													<option value="0"><digi:trn key="aim:planned">Planned</digi:trn></option>
												</c:if>
												<c:if test="${comm.adjustmentType == 0}">
													<option value="1"><digi:trn key="aim:actual">Actual</digi:trn></option>
													<option value="0" selected="true"><digi:trn
														key="aim:planned">Planned</digi:trn></option>
												</c:if>
											</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="text"
												name="<%=field2%>"
												value="<c:out value="${comm.transactionAmount}"/>" size="15"
												class='amt'>&nbsp; <select name="<%=field3%>"
												class="inp-text">
												<c:forEach var="currency"
													items="${aimEditActivityForm.currencies}">
													<c:if test="${comm.currencyCode == currency.currencyCode}">
														<option selected="true"
															value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:if test="${comm.currencyCode != currency.currencyCode}">
														<option value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:out value="${currency.currencyName}" />
													</option>
												</c:forEach>
											</select>&nbsp; <input type="text" readonly="true" name="<%=field4%>"
												id="<%=field4%>"
												value="<c:out value="${comm.transactionDate}"/>" size="10"
												class="inp-text">&nbsp; <a id="date1<%=field4%>"
												href='javascript:pickDateById("date1<%=field4%>","<%=field4%>")'>
											<img src="../ampTemplate/images/show-calendar.gif"
												alt="Click to View Calendar" border=0> </a>  <input type="hidden"
												name="<%=field6%>" value="${comm.ampComponentFundingId}">
											<input type='button' value='Delete' class='inp-text'
												onclick="removeCommitment('<%=divName%>')"></div>
										</c:forEach>
									</c:if>
								</c:forEach>
							</c:if></div>							</td>
						</tr>

						<tr bgcolor="#ffffff">
							<td colspan="2" class="box-border"><span class="f-names"><digi:trn
								key="aim:disbursementTotalActToDate"> Disbursement - (Total actual to date</digi:trn>
							<%=eaForm.getTotalDisbursements()%> <%=eaForm.getCurrCode()%>) </span> <a
								href="javascript:addDisbursement()"><digi:trn key="btn:add">Add</digi:trn></a><br>
							<br>
							<digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn
								key="aim:ActualFIE">Actual</digi:trn>&nbsp;&nbsp;&nbsp; <digi:trn
								key="aim:AmountFIE">Amount</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<digi:trn key="aim:CurrencyFIE">Currency</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<digi:trn key="aim:DateFIE">Date</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							
							<div id="disb"><c:if
								test="${aimEditActivityForm.componentId != -1}">
								<c:forEach var="fundComp"
									items="${aimEditActivityForm.selectedComponents}">
									<c:if
										test="${aimEditActivityForm.componentId == fundComp.componentId}">
										<c:forEach var="comm" items="${fundComp.disbursements}">
											<%
												String tNameBase = "disb_" + indexD + "_";
												String divName = "disb_" + indexD;
												indexD++;
											%>
											<%
												String field1 = tNameBase + "1";
												String field2 = tNameBase + "2";
												String field3 = tNameBase + "3";
												String field4 = tNameBase + "4";
												String field5 = tNameBase + "5";
												String field6 = tNameBase + "6";
											%>
											<div id="<%=divName%>"><select name="<%=field1%>"
												class="inp-text">
												<c:if test="${comm.adjustmentType == 1}">
													<option value="1" selected="true"><digi:trn
														key="aim:actual">Actual</digi:trn></option>
													<option value="0"><digi:trn key="aim:planned">Planned</digi:trn></option>
												</c:if>
												<c:if test="${comm.adjustmentType == 0}">
													<option value="1"><digi:trn key="aim:actual">Actual</digi:trn></option>
													<option value="0" selected="true"><digi:trn
														key="aim:planned">Planned</digi:trn></option>
												</c:if>
											</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text"
												name="<%=field2%>"
												value="<c:out value="${comm.transactionAmount}"/>" size="15"
												class='amt'>&nbsp;<select name="<%=field3%>"
												class="inp-text">
												<c:forEach var="currency"
													items="${aimEditActivityForm.currencies}">
													<c:if test="${comm.currencyCode == currency.currencyCode}">
														<option selected="true"
															value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:if test="${comm.currencyCode != currency.currencyCode}">
														<option value="<c:out value="${currency.currencyCode}"/>">													</c:if>
													<c:out value="${currency.currencyName}" />
													</option>
												</c:forEach>
											</select>&nbsp;<input type="text" name="<%=field4%>" id="<%=field4%>"
												readonly="true"
												value="<c:out value="${comm.transactionDate}"/>" size="10"
												class="inp-text">&nbsp; <a id="date2<%=field4%>"
												href='javascript:pickDateById("date2<%=field4%>","<%=field4%>")'>
											<img src="../ampTemplate/images/show-calendar.gif"
												alt="Click to View Calendar" border=0> </a> &nbsp;  <input type="hidden" name="<%=field6%>"
												value="${comm.ampComponentFundingId}"> <input
												type='button'
												value='<digi:trn key="btn:delete">Delete</digi:trn>'
												class='inp-text'
												onclick="removeDisbursement('<%=divName%>')"></div>
										</c:forEach>
									</c:if>
								</c:forEach>
							</c:if></div>							</td>
						</tr>

						<!-- beging Expenditure  -->
						<feature:display module="Funding" name="Expenditures">
							<tr>
								<td colspan="2" class="box-border-alt1"><span
									class="f-names"><digi:trn
									key="aim:expenditureTotalActToDate"> Expenditure - (Total actual to date</digi:trn>
								<%=eaForm.getTotalExpenditures()%> <%=eaForm.getCurrCode()%>)</span> <field:display
									name="Add Expenditure Button" feature="Expenditures">
									<a href="javascript:addExpenditure()"> <digi:trn
										key="btn:add">Add</digi:trn></a>
								</field:display> &nbsp;&nbsp; <br>
								<br>
								<digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<digi:trn
									key="aim:ActualFIE">Actual</digi:trn>&nbsp;&nbsp;&nbsp; <digi:trn
									key="aim:AmountFIE">Amount</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<digi:trn key="aim:CurrencyFIE">Currency</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<digi:trn key="aim:DateFIE">Date</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<br>
								<div id="expn"><c:if
									test="${aimEditActivityForm.componentId != -1}">
									<c:forEach var="fundComp"
										items="${aimEditActivityForm.selectedComponents}">

										<c:if
											test="${aimEditActivityForm.componentId == fundComp.componentId}">

											<c:forEach var="comm" items="${fundComp.expenditures}">


												<%
													String tNameBase = "expn_" + indexE + "_";
													String divName = "expn_" + indexE;
													indexE++;
												%>
												<%
													String field1 = tNameBase + "1";
													String field2 = tNameBase + "2";
													String field3 = tNameBase + "3";
													String field4 = tNameBase + "4";
													String field5 = tNameBase + "5";
													String field6 = tNameBase + "6";
												%>
												<div id="<%=divName%>"><select name="<%=field1%>"
													class="inp-text">
													<c:if test="${comm.adjustmentType == 1}">
														<option value="1" selected="true"><digi:trn
															key="aim:actual">Actual</digi:trn></option>
														<option value="0"><digi:trn key="aim:planned">Planned</digi:trn></option>
													</c:if>
													<c:if test="${comm.adjustmentType == 0}">
														<option value="1"><digi:trn key="aim:actual">Actual</digi:trn></option>
														<option value="0" selected="true"><digi:trn
															key="aim:planned">Planned</digi:trn></option>
													</c:if>
												</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text"
													name="<%=field2%>"
													value="<c:out value="${comm.transactionAmount}"/>"
													size="15" class='amt'>&nbsp;<select
													name="<%=field3%>" class="inp-text">
													<c:forEach var="currency"
														items="${aimEditActivityForm.currencies}">
														<c:if test="${comm.currencyCode == currency.currencyCode}">
															<option selected="true"
																value="<c:out value="${currency.currencyCode}"/>">														</c:if>
														<c:if test="${comm.currencyCode != currency.currencyCode}">
															<option value="<c:out value="${currency.currencyCode}"/>">														</c:if>
														<c:out value="${currency.currencyName}" />
														</option>
													</c:forEach>
												</select>&nbsp;<input type="text" name="<%=field4%>" id="<%=field4%>"
													readonly="true"
													value="<c:out value="${comm.transactionDate}"/>" size="10"
													class="inp-text">&nbsp; <a id="date3<%=field4%>"
													href='javascript:pickDateById("date3<%=field4%>","<%=field4%>")'>
												<img src="../ampTemplate/images/show-calendar.gif"
													alt="Click to View Calendar" border=0> </a> &nbsp;  <input type="hidden" name="<%=field6%>"
													value="${comm.ampComponentFundingId}"> <input
													type='button'
													value='<digi:trn key="btn:delete">Delete</digi:trn>'
													class='inp-text'
													onclick="removeExpenditure('<%=divName%>')"></div>
											</c:forEach>
										</c:if>
									</c:forEach>
								</c:if></div>								</td>
							</tr>
						</feature:display>
						<tr>
							<td colspan="2" align="center">
							<table cellPadding=3>
								<tr>
									<td><input type="button"
										value="<digi:trn key='btn:save'>Save</digi:trn>"
										class="inp-text" onclick="return addComponents()"></td>
									<td><input type="reset"
										value="<digi:trn key='btn:reset'>Reset</digi:trn>"
										class="inp-text"></td>
									<td><input type="button"
										value="<digi:trn key='btn:close'>Close</digi:trn>"
										class="inp-text" onclick="closeWindow()"></td>
								</tr>
							</table>							</td>
						</tr>
					</table>





				  <script language="JavaScript">
<!--

var numComm = <%=indexC%>;
var numExpn = <%=indexE%>;
var numDisb = <%=indexD%>;

var tempComm = numComm;
var tempDisb = numDisb;
var tempExpn = numExpn;

function addCommitments()
{
	var ni = document.getElementById('comm');
	var divname = "comm_" + numComm;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<select name='comm_" + numComm + "_1' class='inp-text'>";
	s += "<option value='1'><digi:trn key='aim:actual'>Actual</digi:trn></option>";
	s += "<option value='0'><digi:trn key='aim:planned'>Planned</digi:trn></option>";
	s += "</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	s += "<input type='text' name='comm_" + numComm + "_2' size='15' class='amt'>&nbsp;";
	s += "<select name='comm_" + numComm + "_3' class='inp-text'>&nbsp;";

	<% Collection col = eaForm.getCurrencies();
		Iterator itr = col.iterator();
		while (itr.hasNext())
		{
			AmpCurrency curr = (AmpCurrency) itr.next();
			if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) { %>
				s += "<option value='<%=curr.getCurrencyCode()%>' selected='true'><%=curr.getCurrencyName()%></option>";
			<% } else { %>
				s += "<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>";
			<% }
		 }%>

	s += "</select>&nbsp;";
	s += "<input type='text' name='comm_" + numComm + "_4' id='comm_" + numComm + "_4' size='10' class='inp-text'>&nbsp;";
	s += "<a id='date1comm_"+numComm+"_4' href='javascript:pickDateById(\"date1comm_"+numComm+"_4\",\"comm_"+numComm+"_4\")'><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border=0></a>&nbsp;";
	
	s += "<input type='hidden' name='comm_" + numComm + "_6' value=''>";
    <c:set var='trnDeleteBtn'><digi:trn key='btn:delete'>delete</digi:trn></c:set>
	s += '<input type="button" value="${trnDeleteBtn}" class="inp-text" onclick=removeCommitment("'+ divname + '")><br>';

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

function addDisbursement()
{
	var ni = document.getElementById('disb');
	var divname = "disb_" + numDisb;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<select name='disb_" + numDisb + "_1' class='inp-text'>";
	s += "<option value='1'><digi:trn key='aim:actual'>Actual</digi:trn></option>";
	s += "<option value='0'><digi:trn key='aim:planned'>Planned</digi:trn></option>";
	s += "</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	s += "<input type='text' name='disb_" + numDisb + "_2' size='15'  class='amt'>&nbsp;";
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
	
	s += "<input type='hidden' name='disb_" + numDisb + "_6' value=''>";
	<c:set var='trnDeleteBtn'><digi:trn key='btn:delete'>delete</digi:trn></c:set>
	s += '<input type="button" value="${trnDeleteBtn}" class="inp-text" onclick=removeDisbursement("'+ divname + '")><br>';


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

function addExpenditure()
{
	var ni = document.getElementById('expn');
	var divname = "expn_" + numExpn;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<select name='expn_" + numExpn + "_1' class='inp-text'>";
	s += "<option value='1'><digi:trn key='aim:actual'>Actual</digi:trn></option>";
	s += "<option value='0'><digi:trn key='aim:planned'>Planned</digi:trn></option>";
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
	
	s += "<input type='hidden' name='expn_" + numExpn + "_6' value=''>";
	<c:set var='trnDeleteBtn'><digi:trn key='btn:delete'>delete</digi:trn></c:set>
	s += '<input type="button" value="${trnDeleteBtn}" class="inp-text" onclick=removeExpenditure("'+ divname + '")><br>';


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



function addComponents()
{
	var flag = validate();
	if (flag == true){
		<digi:context name="addComp" property="context/module/moduleinstance/showAddComponent.do?edit=true&compFundAct=save"/>
		document.aimEditActivityForm.action = "<%= addComp %>";
		document.aimEditActivityForm.target = window.opener.name;
		document.aimEditActivityForm.submit();
		window.close();
	}
	return flag;
}

function validate()
{
  var msgEnterAmount="<digi:trn key="aim:selectComponent:errmsg:enterAmount">Amount not entered.</digi:trn>";
  var msgInvalidAmount="<digi:trn key="aim:selectComponent:errmsg:invalidAmount">Invalid amount entered.</digi:trn>";
  var msgEnterDate="<digi:trn key="aim:selectComponent:errmsg:enterDate">Date not entered.</digi:trn>";
  var msgEnterTitle="<digi:trn key="aim:selectComponent:errmsg:enterTitle">Please enter title.</digi:trn>";
  var msgSelectComponent="<digi:trn key="aim:selectComponent:errmsg:selectComponent">Please select a Component before Saving.</digi:trn>";
  var msgEnterCommitment="<digi:trn key="aim:selectComponent:errmsg:enterCommitment">Commitment not entered.</digi:trn>";
  var msgEnterExpenditure="<digi:trn key="aim:selectComponent:errmsg:enterExpenditure">Expenditure entered without entering disbursements.</digi:trn>";

	var titleFlag = isEmpty(document.aimEditActivityForm.componentTitle.value);
	if(titleFlag == true) {
		alert(msgEnterTitle);
		document.aimEditActivityForm.componentTitle.focus();
		return false;
	}

	var x = document.aimEditActivityForm;
	if(document.aimEditActivityForm.componentTitle.value == -1)
	{
		alert(msgSelectComponent);
			return false;
	}

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

function checkAmount(val){
  if(val.match("[^0-9., ]")){
    return false;
  }
  return true;
}



	function load() {
		document.aimEditActivityForm.componentTitle.focus();
		switchComponent();
	}

	
	function addLoadEvent(func) {
  		var oldonload = window.onload;
 		 if (typeof window.onload != 'function') {
    			window.onload = func;
  		} else {
    			window.onload = function() {
    		 	 if (oldonload) {
     			   oldonload();
     			 }
     	 		func();
   			 }
 		 }
	}
	addLoadEvent(load)
	
</script>

 </digi:form>