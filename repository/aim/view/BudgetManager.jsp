<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/aim" prefix="aim"%>

<script language="JavaScript" type="text/javascript">
<!--
function addBudgetsector(){
		<digi:context name="Url" property="context/module/moduleinstance/BudgetManager.do?new=true" />
    	document.BudgetManagerForm.action = "<%=Url%>";
    	document.BudgetManagerForm.submit();
}

function validatesector(){
	if (document.getElementById('secname').value==''){
		alert('Please enter the sector name');
		return false;	
	}
	if (document.getElementById('bcode').value==''){
		alert('Please enter the sector code');
		return false;	
	}
	addBudgetsector();
}

function editsector(id) {
	openNewWindow(450, 200);
	<digi:context name="edit" property="context/module/moduleinstance/editbudgetsector.do" />
	document.BudgetManagerForm.action = "<%=edit%>~id="+id;
	document.BudgetManagerForm.target = popupPointer.name;
	document.BudgetManagerForm.submit();
}

function setprogram() {
	openNewWindow(450, 150);
	<digi:context name="program" property="context/module/moduleinstance/setbudgetprogram.do" />
	document.BudgetManagerForm.action = "<%=program%>";
	document.BudgetManagerForm.target = popupPointer.name;
	document.BudgetManagerForm.submit();
}

</script>

<digi:form action="/BudgetManager.do" method="post">
	<!--  AMP Admin Logo -->
	<jsp:include page="teamPagesHeader.jsp" flush="true" />
	<!-- End of Logo -->
	<html:hidden property="event" value="view" />
	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
		<tr>
			<td class=r-dotted-lg width=14>&nbsp;</td>
			<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb> <c:set
						var="clickToViewAdmin">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set> <digi:link href="/admin.do" styleClass="comment"
						title="${clickToViewAdmin}">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp; <digi:trn key="aim:budgetManager">Budget Manager</digi:trn></td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span
						class=subtitle-blue> <digi:trn key="aim:budgetManager">Budget Manager</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><digi:errors /></td>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<td><font color="red" >*</font></td>
								<td><digi:trn>Sector Name</digi:trn>:</td>
								<td><html:text property="budgetsectorname" styleId="secname" size="30"></html:text></td>
							</tr>
							<tr>
								<td><font color="red" >*</font></td>
								<td><digi:trn>Sector Code</digi:trn>:</td>
								<td>
									<html:text property="budgetsectorcode" size="5" styleId="bcode"></html:text>
								</td>
							</tr>
							<tr>
								<td colspan="3" align="center"> 
									<html:button property="submitButton" onclick="validatesector();">
										<digi:trn>Add Sector</digi:trn>
									</html:button>
								</td>	
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
						<tr>
							<td noWrap width=600 vAlign="top">
							<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%"
								valign="top">
								<tr bgColor=#ffffff>
									<td vAlign="top" width="100%">
									<table cellSpacing="1" cellPadding="5" class="box-border-nopadding" id="selectedSectors" align="left" width="100%">		
										<tr>
										  <td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
										  	<!-- Table title --> 
										  	<digi:trn>Budget Sectors</digi:trn>
										    <!-- end table title -->
											</td>
										</tr>
										<logic:empty name="BudgetManagerForm" property="budgetsectors">
											<tr bgcolor="#ffffff">
												<td colspan="5" align="center">
													<b>
														<digi:trn> No Sector present</digi:trn>
													</b>
												</td>
											</tr>
										</logic:empty>
										<logic:notEmpty name="BudgetManagerForm" property="budgetsectors">
										<logic:iterate name="BudgetManagerForm" property="budgetsectors" id="sector" type="org.digijava.module.budget.dbentity.AmpBudgetSector">
											<tr> 
												<td bgcolor="#ffffff">
													<bean:write name="sector" property="code"/> - <bean:write name="sector" property="sectorname"/>
												</td>
												<td bgcolor="#ffffff" width="75" align="center">
													<c:set var="clickToEditSector">
														<digi:trn key="aim:clickToEditSector">Click here to Edit Sector</digi:trn>
													</c:set>
													<c:set var="edittext">
														<digi:trn key="aim:edit">Edit</digi:trn>
													</c:set>
													 [<a title="${clickToEditSector}" href="javascript:editsector('${sector.idsector}')">
														${edittext}
													  </a>
													 ]
												</td>
												<td bgcolor="#ffffff" width="75" align="center">
													<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams2}" property="sectorid">
															<bean:write name="sector" property="idsector" />
														</c:set>
														<c:set target="${urlParams2}" property="delete" value="true"/>
														<c:set var="clickToDeleteSector">
															<digi:trn key="aim:clickToDeleteSector">Click here to Delete Sector</digi:trn>
														</c:set>
														<c:set var="deletetext">
															<digi:trn key="aim:delete">Delete</digi:trn>
														</c:set>
														[ <digi:link href="/BudgetManager.do" name="urlParams2" 
																title="${clickToDeleteSector}" onclick="return onDelete()">${deletetext}</digi:link> ]
												</td>
											</tr>
											</logic:iterate>
											</logic:notEmpty>
									</table>
									</td>
								</tr>
							</table>
							</td>
							<td noWrap width=100% vAlign="top">
							<table align=center cellPadding=0 cellSpacing=0 width="90%"
								border=0>
								<tr>
									<td><!-- Other Links -->
									<table cellPadding=0 cellSpacing=0 width=200>
										<tr>
											<td bgColor=#c9c9c7 class=box-title><digi:trn
												key="aim:otherLinks">
												Other links
												</digi:trn></td>
											<td background="module/aim/images/corner-r.gif" height="17"
												width=17>&nbsp;</td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<field:display name="Add Scheme Link" feature="Sectors">
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10" /> 
													<a href="javascript:setprogram();">
													<digi:trn>
                                                  		Set Budget Program
                                                  	</digi:trn>
                                                  </a>
												</td>
											</tr>
										</field:display>
										<tr>
											<td><digi:img src="module/aim/images/arrow-014E86.gif"
												width="15" height="10" /> <c:set var="trnViewAdmin">
												<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
											</c:set> <digi:link href="/admin.do" title="${trnViewAdmin}">
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
											</digi:link></td>
										</tr>
										<!-- end of other links -->
									</table>
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
	</table>
</digi:form>