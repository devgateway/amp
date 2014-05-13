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
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>

<digi:instance property="aimEditActivityForm" />

<%
	EditActivityForm eaForm = (EditActivityForm) session.getAttribute("siteampdefaultaimEditActivityForm");
	String defPers = (String) request.getAttribute("defPerspective");
	String defCurr = (String) request.getAttribute("defCurrency");
	int indexC = 0;
	int indexD = 0;
	int indexE = 0;
%>
<digi:form action="/showAddComponent.do" method="post" name="aimAddComponentForm" type="EditActivityForm">
	<html:hidden property="componentReset" value="false" />
	<logic:notEmpty name="aimEditActivityForm" property="components.componentId">
		<logic:greaterThan name="aimEditActivityForm" property="components.componentId" value="0">
		<html:hidden property="components.componentId" />
		</logic:greaterThan>
	</logic:notEmpty>

	<input type="hidden" name="selectedDate">


	<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%"
		class=box-border-nopadding>
		<tr>
			<td align=left valign="top">
			<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%"
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
						<feature:display name="Admin - Component Type" module="Components">
							<tr>
								<td align="right"><digi:trn key="aim:comptype">
									              Component Type
									              </digi:trn></td>
								<td><html:select property="components.selectedType" styleClass="inp-text" styleId="selectedType">
									
									<html:option value="-1">
										<digi:trn key="aim:selecType">-Select Type-</digi:trn>
									</html:option>
									<c:forEach var="comp" items="${aimEditActivityForm.components.allCompsType}">
										<c:choose>
											<c:when test="${comp.selectable}">
												<html:option value="${comp.type_id}">${comp.name }</html:option>
											</c:when>
											<c:otherwise>
												<html:option disabled="true" value="${comp.type_id}">${comp.name }</html:option>
											</c:otherwise>
										</c:choose>	
									</c:forEach>
								</html:select></td>
							</tr>
						</feature:display>						
						<tr>
							<td align="right">
								<digi:trn key="aim:title">Title</digi:trn> 
							</td>
							<td>
								<html:text styleId="newCompoenentName" property="components.newCompoenentName" value="<%=eaForm.getComponents().getComponentTitle()%>" size="40" onkeypress="validateEnter(event)"/>
								<html:button property="components.addNewCompoenent" onclick="addComponent()" styleId="addComponentBtn">
									<digi:trn key="aim:add">Add</digi:trn>
								</html:button>
								
							</td>
						</tr>
						<field:display name="Admin - Component Description" feature="Admin - Component">
							<tr>
								<td align="right">
									<digi:trn key="aim:description">Description</digi:trn> 
								</td>
								<td>
									<html:textarea styleId="newCompoenentName" property="components.componentDesc" value="<%=eaForm.getComponents().getComponentDesc()%>" cols="40"></html:textarea>
								</td>
							</tr>
						</field:display>
												    
						
					</table>
					



					<table id="tblId"
						style="visibility: hidden; position: absolute; width: 100%;margin-top:10px;"
						name="tblId">
						<tr>
						  <td colspan=2 height="24" align="left" bgcolor="#FFFFFF"><div style="margin:4px" class="f-names" id="txtTitle"></div></td>
					  </tr>
						<tr>


							<td colspan=2>
								<FONT color='blue'>
  									<jsp:include page="utils/enterAmountUnitsUnformatted.jsp">
    									<jsp:param value="<BIG>* </BIG>" name="amount_prefix"/>
  									</jsp:include>
								</FONT>
							</td>
                        </tr>
<!--                            <tr>-->
<!--                            <td>-->
<!---->
<!--                               <digi:trn>Select currency </digi:trn>-->
<!---->
<!--                               <html:select property="components.fundingCurrCode" styleClass="inp-text" onchange="changeCurrency()" styleId="compFundCurr">-->
<!--                                <c:forEach var="currency" items="${aimEditActivityForm.funding.validcurrencies}">-->
<!--                                    <c:if test="${currency.currencyCode!=aimEditActivityForm.components.fundingCurrCode}">-->
<!--                                        <option value="<c:out value="${currency.currencyCode}"/>">-->
<!--                                    </c:if>-->
<!--                                    <c:if test="${currency.currencyCode==aimEditActivityForm.components.fundingCurrCode}">-->
<!--                                        <option value="<c:out value="${currency.currencyCode}"/>" selected="selected">-->
<!--                                        </c:if>-->
<!--                                        <c:out value="${currency.currencyName}" />-->
<!--                                    </option>-->
<!--                                </c:forEach>-->
<!---->
<!--                                </html:select>-->
<!---->
<!--							</td>-->
<!---->
<!--						</tr>-->
						<tr bgcolor="#f4f4f2">
							<td colspan="2" class="box-border-alt1">
                             <span class="f-names" id="total_comm">
                            <digi:trn key="aim:commitmentsTotalActAllocation">Commitments - (Total Actual Allocation</digi:trn>
                            ${sessionScope.totalComm}  ${aimEditActivityForm.fundingCurrCode} ) </span>
							<a href="javascript:document.addCommitments()" style="color:blue"><digi:trn key="btn:add">Add</digi:trn></a><br>
							
							<br>
							<div id="comm">
								<c:if test="${aimEditActivityForm.components.componentId != -1}">
								<c:forEach var="fundComp" items="${aimEditActivityForm.components.selectedComponents}">
									<c:if test="${aimEditActivityForm.components.componentId == fundComp.componentId}">
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
									<div id="<%=divName%>">
										<table>
											<tr>
												<td><p class="inp-text"><digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<br><digi:trn key="aim:ActualFIE">Actual</digi:trn></p></td>
												<td class="inp-text" valign='bottom'><digi:trn key="aim:AmountFIE">Amount</digi:trn></td>
												<td valign='bottom'><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></td>
												<td valign='bottom'><digi:trn key="aim:DateFIE">Date</digi:trn></td>
												<td>&nbsp;</td>
											</tr>
											<tr>
												<td>
													<select name="<%=field1%>" class="inp-text">													
														<c:if test="${comm.adjustmentTypeName.value == 'Actual'}">
															<option value="1" selected="selected"><digi:trn
																key="aim:actual">Actual</digi:trn></option>
															<option value="0"><digi:trn key="aim:planned">Planned</digi:trn></option>
														</c:if>
														<c:if test="$${comm.adjustmentTypeName.value == 'Planned'}">
															<option value="1"><digi:trn key="aim:actual">Actual</digi:trn></option>
															<option value="0" selected="selected"><digi:trn
																key="aim:planned">Planned</digi:trn></option>
														</c:if>
													</select>
										 		</td>
												<td>											
													<input type="text" name="<%=field2%>" value="<c:out value="${comm.transactionAmount}"/>" size="15" class='amt'>
												</td>
												<td>
													<select name="<%=field3%>" class="inp-text">
														<c:forEach var="currency"
															items="${aimEditActivityForm.funding.validcurrencies}">
															<c:if test="${comm.currencyCode == currency.currencyCode}">
																<option selected="selected" value="<c:out value="${currency.currencyCode}"/>">													
															</c:if>
															<c:if test="${comm.currencyCode != currency.currencyCode}">
																<option value="<c:out value="${currency.currencyCode}"/>">													
															</c:if>
															<c:out value="${currency.currencyName}" />
																</option>
														</c:forEach>
													</select>
												</td>
												<td>
													<input type="text" readonly="readonly" name="<%=field4%>" id="<%=field4%>" value="<c:out value="${comm.transactionDate}"/>" size="10" class="inp-text">&nbsp; 
													<a id="date1<%=field4%>" href='javascript:pickDateById_divContent("<%=field4%>","<%=field4%>")'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
													</a><input type="hidden" name="<%=field6%>" value="${comm.ampComponentFundingId}">
													<input type='button' value='<digi:trn key="btn:delete">Delete</digi:trn>' class='inp-text' onclick="document.removeCommitment('<%=divName%>')">
												</td>
											</tr>
										</table>
									</div>
									</c:forEach>
									</c:if>
								</c:forEach>
							</c:if></div>							
							</td>
						</tr>

						<tr bgcolor="#ffffff">
							<td colspan="2" class="box-border"><span class="f-names" id="total_disb"><digi:trn
								key="aim:disbursementTotalActToDate"> Disbursement - (Total actual to date </digi:trn>
							${sessionScope.totalDisb}  ${aimEditActivityForm.fundingCurrCode} ) </span> <a
								href="javascript:document.addDisbursement()" style="color:blue"><digi:trn key="btn:add">Add</digi:trn></a><br>
							<br>							
							<div id="disb"><c:if test="${aimEditActivityForm.components.componentId != -1}">
								<c:forEach var="fundComp" items="${aimEditActivityForm.components.selectedComponents}">
									<c:if test="${aimEditActivityForm.components.componentId == fundComp.componentId}">
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
											<div id="<%=divName%>">
												<table>
												<tr>
													<td><p class="inp-text"><digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<br><digi:trn key="aim:ActualFIE">Actual</digi:trn></p></td>
													<td class="inp-text" valign='bottom'><digi:trn key="aim:AmountFIE">Amount</digi:trn></td>
													<td valign='bottom'><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></td>
													<td valign='bottom'><digi:trn key="aim:DateFIE">Date</digi:trn></td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td>
														<select name="<%=field1%>" class="inp-text">
														<c:if test="${comm.adjustmentType == 1}">
															<option value="1" selected="selected"><digi:trn
																key="aim:actual">Actual</digi:trn></option>
															<option value="0"><digi:trn key="aim:planned">Planned</digi:trn></option>
														</c:if>
														<c:if test="${comm.adjustmentType == 0}">
															<option value="1"><digi:trn key="aim:actual">Actual</digi:trn></option>
															<option value="0" selected="selected"><digi:trn
																key="aim:planned">Planned</digi:trn></option>
														</c:if>
														</select>
													</td>
													<td>
														<input type="text" name="<%=field2%>" value="<c:out value="${comm.transactionAmount}"/>" size="15" class='amt'>
													</td>
													<td>
														<select name="<%=field3%>" class="inp-text">
															<c:forEach var="currency"
																items="${aimEditActivityForm.funding.validcurrencies}">
																<c:if test="${comm.currencyCode == currency.currencyCode}">
																	<option selected="selected" value="<c:out value="${currency.currencyCode}"/>">
																</c:if>
																<c:if test="${comm.currencyCode != currency.currencyCode}">
																	<option value="<c:out value="${currency.currencyCode}"/>">
																</c:if>
																<c:out value="${currency.currencyName}" />aaa
																</option>
															</c:forEach>
														</select>
													</td>
													<td>
														<input type="text" name="<%=field4%>" id="<%=field4%>" readonly="readonly" value="<c:out value="${comm.transactionDate}"/>" size="10" class="inp-text">
													</td>
													<td>
														<a id="date2<%=field4%>" href='javascript:pickDateById_divContent("<%=field4%>","<%=field4%>")'>
														<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0"></a>
														<input type="hidden" name="<%=field6%>" value="${comm.ampComponentFundingId}">
													</td>
													<td>
														<input type='button' value='<digi:trn key="btn:delete">Delete</digi:trn>' class='inp-text' onclick="document.removeDisbursement('<%=divName%>')">
													</td>
												</tr>
											</table>													
										</div>
										</c:forEach>
									</c:if>
								</c:forEach>
							</c:if></div>							
						</td>
						</tr>

						<!-- beging Expenditure  -->
						<feature:display module="Funding" name="Expenditures">
							<tr>
								<td colspan="2" class="box-border-alt1">
                                    <span class="f-names" id="total_expn">
                                    <digi:trn key="aim:expenditureTotalActToDate"> Expenditure - (Total actual to date</digi:trn>
                                    ${sessionScope.totalExpn}   ${aimEditActivityForm.fundingCurrCode})</span>
                                    <field:display name="Add Expenditure Button" feature="Expenditures">
									<a href="javascript:document.addExpenditure()" style="color:blue"> <digi:trn
										key="btn:add">Add</digi:trn></a>
								</field:display> &nbsp;&nbsp; <br>
								<br>
								<div id="expn"><c:if test="${aimEditActivityForm.components.componentId != -1}">
									<c:forEach var="fundComp" items="${aimEditActivityForm.components.selectedComponents}">
										<c:if test="${aimEditActivityForm.components.componentId == fundComp.componentId}">
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
												<div id="<%=divName%>">
													<table>
													<tr>
														<td><p class="inp-text"><digi:trn key="aim:PlannedFIE">Planned</digi:trn>/<br><digi:trn key="aim:ActualFIE">Actual</digi:trn></p></td>
														<td class="inp-text" valign='bottom'><digi:trn key="aim:AmountFIE">Amount</digi:trn></td>
														<td valign='bottom'><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></td>
														<td valign='bottom'><digi:trn key="aim:DateFIE">Date</digi:trn></td>
														<td>&nbsp;</td>
													</tr>
													<tr>
														<td>
															<select name="<%=field1%>" class="inp-text">
															<c:if test="${comm.adjustmentType == 1}"> 
																<option value="1" selected="selected"><digi:trn key="aim:actual">Actual</digi:trn></option>
																<option value="0"><digi:trn key="aim:planned">Planned</digi:trn></option>
															</c:if>
															<c:if test="${comm.adjustmentType == 0}">
																<option value="1"><digi:trn key="aim:actual">Actual</digi:trn></option>
																<option value="0" selected="selected"><digi:trn
																	key="aim:planned">Planned</digi:trn></option>
															</c:if>
															</select>
														</td>
														<td>
															<input type="text" name="<%=field2%>" value="<c:out value="${comm.transactionAmount}"/>" size="15" class='amt'>
														</td>
														<td>
															<select name="<%=field3%>" class="inp-text">
															<c:forEach var="currency" items="${aimEditActivityForm.funding.validcurrencies}">
																<c:if test="${comm.currencyCode == currency.currencyCode}">
																	<option selected="selected" value="<c:out value="${currency.currencyCode}"/>">														
																</c:if>
																<c:if test="${comm.currencyCode != currency.currencyCode}">
																	<option value="<c:out value="${currency.currencyCode}"/>">														
																</c:if>
																<c:out value="${currency.currencyName}" />
																</option>
															</c:forEach>
															</select>
														</td>
														<td>
															<input type="text" name="<%=field4%>" id="<%=field4%>" readonly="readonly" value="<c:out value="${comm.transactionDate}"/>" size="10" class="inp-text">
														</td>
														<td>
															<a id="date3<%=field4%>" href='javascript:pickDateById_divContent("<%=field4%>","<%=field4%>")'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0"> 
															</a>
														</td>
														<td>
															<input type="hidden" name="<%=field6%>" value="${comm.ampComponentFundingId}">
															<input type='button' value='<digi:trn key="btn:delete">Delete</digi:trn>' class='inp-text' onclick="document.removeExpenditure('<%=divName%>')">
														</td>
													</tr>
												</table>
											</div>
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
										class="inp-text" onclick="return document.addComponentsPopup()"></td>
									<td><input type="reset"
										value="<digi:trn key='btn:reset'>Reset</digi:trn>"
										class="inp-text"></td>
									<td><input type="button"
										value="<digi:trn key='btn:close'>Close</digi:trn>"
										class="inp-text" onclick="closePopup()"></td>
								</tr>
							</table>							</td>
						</tr>
					</table>

<div id="comm_gen"  style="visibility:hidden">
	<table>
		<tr>
			<td><p class='inp-text'><digi:trn key='aim:PlannedFIE'>Planned</digi:trn>/<br><digi:trn key='aim:ActualFIE'>Actual</digi:trn></p></td>
			<td class='inp-text' valign='bottom'><digi:trn key='aim:AmountFIE'>Amount</digi:trn></td>
			<td valign='bottom'><digi:trn key='aim:CurrencyFIE'>Currency</digi:trn></td>
			<td valign='bottom'><digi:trn key='aim:DateFIE'>Date</digi:trn></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<select name='comm_@@_1' class='inp-text'>
					<option value='1'><digi:trn key='aim:actual'>Actual</digi:trn></option>
					<option value='0'><digi:trn key='aim:planned'>Planned</digi:trn></option>
				</select>
			</td>
			<td><input type='text' name='comm_@@_2' size='15' class='amt'></td>
			<td>
				<select name='comm_@@_3' class='inp-text'>
				<% Collection col = eaForm.getFunding().getValidcurrencies();
					Iterator itr = col.iterator();
					while (itr.hasNext())
					{
						AmpCurrency curr = (AmpCurrency) itr.next();
						if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) {	%>
							<option value='<%=curr.getCurrencyCode()%>' selected='selected'><%=curr.getCurrencyName()%></option>
						<% } else { %>
							<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>
						<% }
					 }%>		
				</select>
			</td>
			<td><input type='text' name='comm_@@_4' id='comm_@@_4' size='10' class='inp-text' readonly="readonly"></td>
			<td><a id='date1comm_@@_4' href="javascript:pickDateById_divContent('comm_@@_4','comm_@@_4')"><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border="0"></a></td>
			<td><input type='hidden' name='comm_@@_6' value=''></td>
		    <c:set var='trnDeleteBtn'><digi:trn key='btn:delete'>delete</digi:trn></c:set>
			<td><input type="button" value="${trnDeleteBtn}" class="inp-text" onclick="document.removeCommitment('comm_@@')"></td>
		</tr>
	</table>
	<br>
</div>

<div id="disb_gen" style="visibility:hidden">
	<table>
		<tr>
			<td><p class='inp-text'><digi:trn key='aim:PlannedFIE'>Planned</digi:trn>/<br><digi:trn key='aim:ActualFIE'>Actual</digi:trn></p></td>
			<td class='inp-text' valign='bottom'><digi:trn key='aim:AmountFIE'>Amount</digi:trn></td>
			<td valign='bottom'><digi:trn key='aim:CurrencyFIE'>Currency</digi:trn></td>
			<td valign='bottom'><digi:trn key='aim:DateFIE'>Date</digi:trn></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<select name='disb_@@_1' class='inp-text'>
					<option value='1'><digi:trn key='aim:actual'>Actual</digi:trn></option>
					<option value='0'><digi:trn key='aim:planned'>Planned</digi:trn></option>
				</select>
			</td>
			<td><input type='text' name='disb_@@_2' size='15' class='amt'></td>
			<td>
				<select name='disb_@@_3' class='inp-text'>
				<% col = eaForm.getFunding().getValidcurrencies();
					itr = col.iterator();
					while (itr.hasNext()) {
						AmpCurrency curr = (AmpCurrency) itr.next();
						if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) { %>
							<option value='<%=curr.getCurrencyCode()%>' selected='selected'><%=curr.getCurrencyName()%></option>
						<% } else { %>
							<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>
						<% }
					 }%>		
				</select>
			</td>
			<td><input type='text' name='disb_@@_4' id='disb_@@_4' size='10' class='inp-text' readonly="readonly"></td>
			<td><a id='date1disb_@@_4' href="javascript:pickDateById_divContent('disb_@@_4','disb_@@_4')"><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border="0"></a></td>
			<td><input type='hidden' name='disb_@@_6' value=''></td>
		    <c:set var='trnDeleteBtn'><digi:trn key='btn:delete'>delete</digi:trn></c:set>
			<td><input type="button" value="${trnDeleteBtn}" class="inp-text" onclick="document.removeDisbursement('disb_@@')"></td>
		</tr>
	</table>
	<br>	
</div>

<div id="expn_gen" style="visibility:hidden">
	<table>
		<tr>
			<td><p class='inp-text'><digi:trn key='aim:PlannedFIE'>Planned</digi:trn>/<br><digi:trn key='aim:ActualFIE'>Actual</digi:trn></p></td>
			<td class='inp-text' valign='bottom'><digi:trn key='aim:AmountFIE'>Amount</digi:trn></td>
			<td valign='bottom'><digi:trn key='aim:CurrencyFIE'>Currency</digi:trn></td>
			<td valign='bottom'><digi:trn key='aim:DateFIE'>Date</digi:trn></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<select name='expn_@@_1' class='inp-text'>
					<option value='1'><digi:trn key='aim:actual'>Actual</digi:trn></option>
					<option value='0'><digi:trn key='aim:planned'>Planned</digi:trn></option>
				</select>
			</td>
			<td><input type='text' name='expn_@@_2' size='15' class='amt'></td>
			<td>
				<select name='expn_@@_3' class='inp-text'>
				<% col = eaForm.getFunding().getValidcurrencies();
					itr = col.iterator();
					while (itr.hasNext()) {
						AmpCurrency curr = (AmpCurrency) itr.next();
						if (curr.getCurrencyCode().equalsIgnoreCase(defCurr)) { %>
							<option value='<%=curr.getCurrencyCode()%>' selected='selected'><%=curr.getCurrencyName()%></option>
						<% } else { %>
							<option value='<%=curr.getCurrencyCode()%>'><%=curr.getCurrencyName()%></option>
						<% }
					 }%>		
				</select>
			</td>
			<td><input type='text' name='expn_@@_4' id='expn_@@_4' size='10' class='inp-text' readonly="readonly"></td>
			<td><a id='date1expn_@@_4' href="javascript:pickDateById_divContent('expn_@@_4','expn_@@_4')"><img src='../ampTemplate/images/show-calendar.gif' alt='Click to View Calendar' border="0"></a></td>
			<td><input type='hidden' name='expn_@@_6' value=''></td>
		    <c:set var='trnDeleteBtn'><digi:trn key='btn:delete'>delete</digi:trn></c:set>
			<td><input type="button" value="${trnDeleteBtn}" class="inp-text" onclick="document.removeExpenditure('expn_@@')"></td>
		</tr>
	</table>
	<br>
</div>

<script language="JavaScript">


	
</script>

 </digi:form>
