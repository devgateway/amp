<%@ page pageEncoding="UTF-8" %>
<%@page import="org.digijava.module.aim.util.CurrencyUtil" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--

	function addEUActivity() {
			openNewWindow(700, 420);
			<digi:context name="addEUActivity" property="context/module/moduleinstance/editEUActivity.do?new" />
			document.aimEditActivityForm.action = "<%= addEUActivity %>";
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}

	function editEUActivity(indexId) {
			openNewWindow(700, 420);
			<digi:context name="editEUActivity" property="context/module/moduleinstance/editEUActivity.do?editEU&indexId=" />
			document.aimEditActivityForm.action = "<%=editEUActivity%>"+indexId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}

	function deleteEUActivity(indexId) {
			popupPointer=window.open('about:blank','popper','width=10,height=10,top=5000,left=5000,scrollbars=no,location=no,directories=no,status=no,menubar=no,toolbar=no,resizable=no');
			<digi:context name="editEUActivity" property="context/module/moduleinstance/editEUActivity.do?deleteEU&indexId=" />
			document.aimEditActivityForm.action = "<%=editEUActivity%>"+indexId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}


	function addRegionalFunding() {
		openNewWindow(650, 500);
		<digi:context name="addRegFunding" property="context/module/moduleinstance/addRegionalFunding.do?edit=true&regFundAct=show" />
		document.aimEditActivityForm.action = "<%= addRegFunding %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	}

	function editFunding(id) {
		openNewWindow(650, 500);
		<digi:context name="addRegFunding" property="context/module/moduleinstance/addRegionalFunding.do?edit=true&regFundAct=showEdit" />
		document.aimEditActivityForm.action = "<%= addRegFunding %>&fundId="+id;
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	}

	function removeRegFundings() {
		<digi:context name="rem" property="context/module/moduleinstance/removeRegionalFunding.do?edit=true" />
		document.aimEditActivityForm.action = "<%= rem %>";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
	}

	function validateForm() {
		return true;
	}
	-->
</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">
<html:hidden property="step"/>


<html:hidden property="editAct" />

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
	<tr>
		<td width="100%" vAlign="top" align="left">
		<!--  AMP Admin Logo -->
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
		<!-- End of Logo -->
		</td>
	</tr>
	<tr>
		<td width="100%" vAlign="top" align="left">
			<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
				<tr>
					<td class=r-dotted-lg width="10">&nbsp;</td>
					<td align=left vAlign=top class=r-dotted-lg>
						<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
							<tr><td>
								<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
									<tr>
										<td>
											<span class=crumb>
												<c:if test="${aimEditActivityForm.pageId == 0}">
													<c:set var="translation">
														<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
													</c:set>
													<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
														<digi:trn key="aim:AmpAdminHome">
															Admin Home
														</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
												</c:if>
												<c:if test="${aimEditActivityForm.pageId == 1}">
													<c:set var="translation">
														<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
													</c:set>
													<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>
													<digi:link href="/viewMyDesktop.do" styleClass="comment"
													onclick="return quitRnot1('${msg}')" title="${translation}" >
														<digi:trn key="aim:portfolio">
															Portfolio
														</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
												</c:if>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAddActivityStep1">
														Click here to go to Add Activity Step 1</digi:trn>
												</c:set>
												<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment"
												title="${translation}" >
													<c:if test="${aimEditActivityForm.editAct == true}">
														<digi:trn key="aim:editActivityStep1">
															Edit Activity - Step 1
														</digi:trn>
													</c:if>
													<c:if test="${aimEditActivityForm.editAct == false}">
														<digi:trn key="aim:addActivityStep1">
															Add Activity - Step 1
														</digi:trn>
													</c:if>
												</digi:link>&nbsp;&gt;&nbsp;

												<c:set var="translation">
													<digi:trn key="aim:clickToViewAddActivityStep2">
														Click here to goto Add Activity Step 2</digi:trn>
												</c:set>
												<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment"
												title="${translation}" >
													<digi:trn key="aim:addActivityStep2">
														Step 2
													</digi:trn>
												</digi:link>&nbsp;&gt;&nbsp;

												<c:set var="translation">
													<digi:trn key="aim:clickToViewAddActivityStep3">
														Click here to goto Add Activity Step 3</digi:trn>
												</c:set>
												<digi:link href="/addActivity.do?step=3&edit=true" styleClass="comment"
												title="${translation}" >
													<digi:trn key="aim:addActivityStep3">
														Step 3
													</digi:trn>
												</digi:link>&nbsp;&gt;&nbsp;
												<digi:trn key="aim:addActivityStep4">
													Step 4
												</digi:trn>
											</span>
										</td>
									</tr>
								</table>
							</td></tr>
							<tr><td>
								<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
									<tr>
										<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
											<c:if test="${aimEditActivityForm.editAct == false}">
												<digi:trn key="aim:addNewActivity">
													Add New Activity
												</digi:trn>
											</c:if>
											<c:if test="${aimEditActivityForm.editAct == true}">
												<digi:trn key="aim:editActivity">
													Edit Activity
												</digi:trn>:
													<bean:write name="aimEditActivityForm" property="title"/>
											</c:if>
										</td>
									</tr>
								</table>
							</td></tr>
							<tr> <td>
								<digi:errors/>
							</td></tr>
							<tr><td>
								<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
									<tr><td width="75%" vAlign="top">
										<table cellPadding=0 cellSpacing=0 width="100%" vAlign="top" >
											<tr>
												<td width="100%">
													<table cellPadding=0 cellSpacing=0 width="100%">
														<tr>
															<td width="13" height="20" background="module/aim/images/left-side.gif"></td>
															<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
																<digi:trn key="aim:step11">EU Project Costing</digi:trn>
															</td>
															<td width="13" height="20" background="module/aim/images/right-side.gif"></td>
														</tr>
													</table>
												</td>
											</tr>
											
											<feature:display name="Costing" module="Activity Costing">
											<tr valign="top">
												<td width="100%" bgcolor="#f4f4f2">
													<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left"
													bgcolor="#006699">
													<tr>
															<td bgColor=#f4f4f2 align="center" vAlign="top">
																<table width="95%">
																	<tr>
																		<td>
																			<IMG alt=Link height=10
																			src="../ampTemplate/images/arrow-014E86.gif" width=15>
																			<a title="<digi:trn key="aim:costing">Costing</digi:trn>">
																				<b><digi:trn key="aim:costing">Costing</digi:trn></b>
																			</a>
																		</td>
																	</tr>
																	<tr><td>&nbsp;</td></tr>

																	<tr><td valign="top">
																		<c:if test="${!empty aimEditActivityForm.costs}">
																		<bean:define id="costs" name="aimEditActivityForm" property="costs" toScope="request"/>
																		<bean:define id="mode" value="form" type="java.lang.String" toScope="request"/>
		                                                                  <digi:trn key="aim:amountsinthousands">Amounts in thousands (000) -</digi:trn>
		                                                                  <c:out value="${aimEditActivityForm.currCode}"/>
																		<jsp:include page="viewCostsSummary.jsp"/>
																		</c:if>
																	</td></tr>


																	<tr><td>
																	&nbsp;
																	</td></tr>
																	<tr><td>
															<input type="button" value="Add Activity" class="buton" onclick="addEUActivity()"/>

																	</td></tr>

																	<tr>
																		<td align="left">
																			<table width="100%" cellSpacing=5 cellPadding=0 border=0
																			class="box-border-nopadding">

<!--
																	<tr><td bgColor=#f4f4f2 align="center">
																		<table cellPadding=3>
																			<tr>
																				<td>
																					<input type="submit" value=" << Back " class="dr-menu"
																					onclick="gotoStep(3)">
																				</td>
																				<td>
																					<input type="submit" value="Next >> " class="dr-menu"
																					onclick="gotoStep(5)">
																				</td>
																				<td>
																					<input type="reset" value="Reset" class="dr-menu"
																					onclick="return resetAll()">
																				</td>
																			</tr>
																		</table>
																	</td></tr>
	-->
	
																</table>
															</td>
														</tr>
														
													</table>
												</td>
											</tr>
											</feature:display>
										</table>
									</td>
									<td width="25%" vAlign="top" align="right">
										<!-- edit activity form menu -->
											<jsp:include page="editActivityMenu.jsp" flush="true" />
										<!-- end of activity form menu -->
									</td></tr>
								</table>
							</td></tr>
							<tr><td>
								&nbsp;
							</td></tr>
						</table>
					</td>
					<td width="10">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>
