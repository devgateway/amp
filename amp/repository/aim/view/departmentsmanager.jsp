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
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/aim" prefix="aim"%>

<script language="JavaScript" type="text/javascript">
<!--
function addDepartment(){
		<digi:context name="Url" property="context/ampModule/moduleinstance/departmentsmanager.do?new=true" />
    	document.DepartmentsManagerForm.action = "<%=Url%>";
    	document.DepartmentsManagerForm.target = "_self";
    	document.DepartmentsManagerForm.submit();
}

function validatesector(){
	if (document.getElementById('dname').value==''){
		alert('Please enter name');
		return false;	
	}
	if (document.getElementById('dcode').value==''){
		alert('Please enter code');
		return false;	
	}
	addDepartment();
}

function onDelete() {
	if (!confirm('Are you sure you want to delete this Department ?')){		
		return false;
	}
}

function Edit(id) {
	openNewWindow(450, 200);
	<digi:context name="edit" property="context/ampModule/moduleinstance/editdepartment.do" />
	document.DepartmentsManagerForm.action = "<%=edit%>~id="+id;
	document.DepartmentsManagerForm.target = popupPointer.name;
	document.DepartmentsManagerForm.submit();
}

var enterBinder	= new EnterHitBinder('saveDeptsBtn');
-->
</script>

<digi:form action="/departmentsmanager.do" method="post">
	<!--  AMP Admin Logo -->
	<jsp:include page="teamPagesHeader.jsp"  />
	<!-- End of Logo -->
	<html:hidden property="event" value="view" />
	<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
		<tr>
			<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<tr>
					<!-- Start Navigation -->
					<td height=33 colspan=5><span class=crumb> <c:set
						var="clickToViewAdmin">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set> <digi:link href="/admin.do" styleClass="comment"
						title="${clickToViewAdmin}">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp; <digi:trn>Departments Manager</digi:trn></td>
					<!-- End navigation -->
				</tr>
				<!--<tr>
					<td height=16 valign="center" width=571><span
						class=subtitle-blue> <digi:trn>Departments Manager</digi:trn></span>
					</td>
				</tr>-->
				<tr>
					<td height=16 valign="center" width=571 colspan=5><digi:errors /></td>
				</tr>
				<tr>
					<td colspan="5">
						<table style="font-size:12px;">
							<tr>
								<td style="padding-right:5px;"><font color="red" >*</font></td>
								<td style="padding-right:10px;"><digi:trn>Department Name</digi:trn>:</td>
								<td style="padding-right:20px;"><html:text property="departmentname" styleId="dname" size="30"></html:text></td>
							    <td style="padding-right:5px;"><font color="red" >*</font></td>
							    <td style="padding-right:10px;"><digi:trn>Department Code</digi:trn>:</td>
							    <td style="padding-right:20px;">
									<html:text property="departmentcode" size="5" styleId="dcode"></html:text>								</td>
							    <td><html:button property="submitButton" onclick="validatesector();" styleClass="buttonx" styleId="saveDeptsBtn">
										<digi:trn>Add</digi:trn>
									</html:button>								</td>
							</tr>

					  </table>
					</td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellpadding="1" cellspacing="1" border="0">
						<tr>
							<td noWrap width=750 vAlign="top">
							<table cellpadding="1" cellspacing="1" width="100%"
								valign="top">
								<tr bgColor=#ffffff>
									<td vAlign="top" width="100%">
									<table cellSpacing="1" cellPadding="5" class="inside" id="selectedSectors" align="left" width="100%">		
										<tr>
										  <td height="20" align="center" bgcolor="#c7d4db" colspan="3" style="font-size:12px;">
										  	<!-- Table title --> 
										  	<digi:trn><b>Departments</b></digi:trn>
										    <!-- end table title -->
											</td>
										</tr>
										<logic:empty name="DepartmentsManagerForm" property="departments">
											<tr bgcolor="#ffffff">
												<td colspan="5" align="center" class="inside">
													<b>
														<digi:trn> No Departments present</digi:trn>
													</b>
												</td>
											</tr>
										</logic:empty>
										<logic:notEmpty name="DepartmentsManagerForm" property="departments">
										<logic:iterate name="DepartmentsManagerForm" property="departments" id="department" type="org.digijava.ampModule.budget.dbentity.AmpDepartments">
											<tr> 
												<td bgcolor="#ffffff" style="margin-left: 10px" class="inside">
													<bean:write name="department" property="code"/> - <bean:write name="department" property="name"/>
												</td>
												<td bgcolor="#ffffff" width="75" align="center" class="inside">
													<c:set var="clickToEdit">
														<digi:trn>Click here to Edit</digi:trn>
													</c:set>
													<c:set var="edittext">
														<digi:trn key="aim:edit">Edit</digi:trn>
													</c:set>
													 [<a title="${clickToEdit}" href="javascript:Edit('${department.id}')">
														${edittext}
													  </a>]
												</td>
												<td bgcolor="#ffffff" width="75" align="center" class="inside">
													<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams2}" property="id">
															<bean:write name="department" property="id" />
														</c:set>
														<c:set target="${urlParams2}" property="delete" value="true"/>
														<c:set var="clickToDelete">
															<digi:trn key="aim:clickToDeleteSector">Click here to Delete Sector</digi:trn>
														</c:set>
														<c:set var="deletetext">
															<digi:trn key="aim:delete">Delete</digi:trn>
														</c:set>
														[ <digi:link href="/departmentsmanager.do" name="urlParams2" 
																title="${clickToDelete}" onclick="return onDelete()">${deletetext}</digi:link> ]
												</td>
											</tr>
											</logic:iterate>
											</logic:notEmpty>
									</table>
									</td>
								</tr>
							</table>
							</td>
							<td noWrap width="100%" vAlign="top">
							<table align="center" cellpadding="0" cellspacing="0" width="90%"
								border="0">
								<tr>
									<td><!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="120">
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<b style="font-size:12px; padding-left:5px;">
													<digi:trn key="aim:otherLinks">
														Other links
													</digi:trn>
												</b>
											</td>
											<td background="ampModule/aim/images/corner-r.gif" height="17" width=17></td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%" class="inside">
											<tr>
												<td class="inside">
													<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10" />
													<c:set var="OrgLink">
														<digi:trn>Click here to view Organization Manager</digi:trn>
													</c:set>
													 <digi:link href="/organisationManager.do~orgSelReset=true" title="${OrgLink}">
													<digi:trn>
                                                  		Organization Manager
                                                  	</digi:trn>
                                                  </digi:link>
												</td>
											</tr>
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10" />
												<c:set var="trnViewAdmin">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set> 
												<digi:link href="/admin.do" title="${trnViewAdmin}">
													<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
												</digi:link>
											</td>
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