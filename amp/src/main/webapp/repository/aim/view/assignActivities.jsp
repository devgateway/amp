<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="javascript">
 function validate(){
	if (document.aimAssignActivityForm.selectedActivities.type=="checkbox"){
		if (document.aimAssignActivityForm.selectedActivities.checked==false){
			alert("Please select activities to add");
			return false;

			}
	}else{
		var items = document.aimAssignActivityForm.selectedActivities;
		var selected=false;
			for(i=0;i<items.length;i++){
		 		if (document.aimAssignActivityForm.selectedActivities[i].checked == true){
		 			selected=true		
				 }
			}
			if (!selected){
			 	alert("Please select activities to add");
				return false;
			}
	}
}

function checkall() {
	var selectbox = document.aimAssignActivityForm.checkAll;
	if (document.aimAssignActivityForm.selectedActivities.type=="checkbox"){
		document.aimAssignActivityForm.selectedActivities.checked=selectbox.checked;
	}else{
		var items = document.aimAssignActivityForm.selectedActivities;
		for(i=0;i<items.length;i++){
		 	document.aimAssignActivityForm.selectedActivities[i].checked = selectbox.checked;
		}
	}
  }

</script>


<digi:instance property="aimAssignActivityForm" />
<digi:form action="/assignActivity.do" method="post" onsubmit="return validate()">

<html:hidden property="teamId" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->




<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=872 border="0">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=850>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
						</c:set>
						<digi:link href="/workspaceManager.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						
						<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams1}" property="id">
						<bean:write name="aimAssignActivityForm" property="teamId" />
						</c:set>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewTeamActivities">Click here to view Team Activities</digi:trn>
						</c:set>
						<digi:link href="/teamActivities.do" name="urlParams1" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:activities">
						Activities
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:assignActivity">
						Assign Activity
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<bean:write name="aimAssignActivityForm" property="teamName" />
					</span></td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr><td noWrap width=700 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">&nbsp;
									
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="95%" border="0">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr bgColor=#f4f4f2>
														<td width="5%" height=20 align="center" bgColor=#d7eafd class=box-title>
															<!-- Table title -->
													    <input type="checkbox" name="checkAll" onclick="checkall()">
															<!-- end table title -->														</td>
													    <td width="95%" align="center" bgColor=#d7eafd class=box-title><digi:trn key="aim:assignActivityTo">Assign Activities to</digi:trn>
                                                          <bean:write name="aimAssignActivityForm" property="teamName" /></td>
													</tr>
												</table>
										  </td>
										</tr>
										<tr>
											<td align="center" bgColor=#ffffff class=box-border>
												<logic:empty name="aimAssignActivityForm" property="activities">
													<b><digi:trn key="aim:noActivitiesToAssign">No activities to assign</digi:trn>
													</b>
											  </logic:empty>
												<logic:notEmpty name="aimAssignActivityForm" property="activities">
														<table width="100%" cellpadding=5 cellspacing="0" border="0">
														<logic:iterate name="aimAssignActivityForm" property="activities" id="activities" 
														>
															<tr>
																<td align="right" width=3>
																	<html:multibox property="selectedActivities" >
																		<bean:write name="activities" property="ampActivityId" />
																	</html:multibox>
																</td>
																<td align="left">
																	<bean:write name="activities" property="name" />
																</td>
															</tr>
														</logic:iterate>
														<tr>
															<td align="center" colspan="2">
																<html:submit value="Assign" styleClass="dr-menu"/>
															</td>
														</tr>
														</table>
												</logic:notEmpty>
											</td>
									  </tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;
								
							</td></tr>
						</table>
					</td>
					<td noWrap width="100%" vAlign="top">
						<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="120">
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%">
										<tr>
											<td class="inside">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" >
												<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
												</digi:link>
											</td>
										</tr>
										<!-- end of other links -->
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>




