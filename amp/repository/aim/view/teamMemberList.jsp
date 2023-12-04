<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>
<style>

.tableEven {
	background-color:#FFFFFF;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

</style>

<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	if (tableElement) {
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}
</script>
<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

<script language="JavaScript">
<!--

function checkSelMembers() {
	var msg='';
	if (document.aimTeamMemberForm.selMembers.checked != null) { 
		if (document.aimTeamMemberForm.selMembers.checked == false) {
			msg+='\n <digi:trn key="aim:members:selectMembersToRemove">Please choose a member to remove</digi:trn>';
			alert(msg);
			return false;
		}
	} else { // 
		var length = document.aimTeamMemberForm.selMembers.length;	  
		var flag = 0;
		for (i = 0;i < length;i ++) {
			if (document.aimTeamMemberForm.selMembers[i].checked == true) {
				flag = 1;
				break;
			}
		}

		if (flag == 0) {
			msg+='\n <digi:trn key="aim:members:selectMembersToRemove">Please choose a member to remove</digi:trn>';
			alert(msg);
			return false;					  
		}
	}
	return true;
}	

function addTeamMember(id) {
	<digi:context name="add" property="context/module/moduleinstance/showAddTeamMember.do" />
	document.aimTeamMemberForm.action = "<%= add %>~teamId="+id+"~fromPage=0";
	document.aimTeamMemberForm.target = "_self";
	document.aimTeamMemberForm.submit();
	return true;		  
}
-->
</script>

<digi:errors/>
<digi:instance property="aimTeamMemberForm" />
<digi:form action="/updateTeamMemberList.do" method="post">

<html:hidden property="teamId" />
<%--
<html:hidden property="action" />
--%>

<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left" >
<tr><td width="100%">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td width="100%">
<table bgColor=#ffffff cellpadding="0" cellspacing="0" style="width:780px;" >
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33>
                    	<span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}">
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:teamWorkspaceInfo">
						Team Workspace Info
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceInfo">Team Workspace Info</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%">
							
							<tr>
								<td valign="top">
									<br />
                                	<div style="width:100%;background:none repeat scroll 0 0 #FFFFFF;border-bottom: 1px solid #D0D0D0;border: 1px solid #D0D0D0;display: block;padding: 10px;">
									<table align="center" cellpadding="0" cellspacing="0" width="100%">	
										<tr>
											<td valign="top" align="center">
												<table border="0" cellPadding=3 cellspacing="0" width="100%">
													<logic:empty name="aimTeamMemberForm" property="teamMembers">
													<tr>
														<td>
															<digi:trn key="aim:noMembers">
															No members present
															</digi:trn>
														</td>
													</tr>
													<tr>
														<td>
															<html:submit  styleClass="dr-menu" property="addMember">
																<digi:trn key="btn:addMember">Add Member</digi:trn> 
															</html:submit>
															
														</td>
													</tr>
													</logic:empty>
							
													<logic:notEmpty name="aimTeamMemberForm" property="teamMembers">

													<tr>
														<td style="padding-left:10px;">
															<table width="99%" cellPadding=3 cellspacing="0" vAlign="top" align="left" 
															bgcolor="#fff" style="background:#f2f2f2;border: 1px solid #D0D0D0;padding:10px;" >
																<tr>	
																	<td  width="100">
																		<b>
                                                                            <digi:trn key="aim:teamname">
                                                                                Team Name  
                                                                            </digi:trn>
                                                                        </b>
																	</td>
																	<td  width="100">
																		<c:out value="${aimTeamMemberForm.teamName}"/>
																	</td>
																	<td  width="200">
																		<b>
																		<digi:trn key="aim:workspaceManager">
																			Workspace Manager
                                                                        </digi:trn>
                                                                        </b>
																	</td>
																	<c:forEach var="mem" items="${aimTeamMemberForm.teamMembers}" >
																		<c:if test="${mem.teamHead == true}">
																		<td >
																			<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParams1}" property="id">
																				<c:out value="${mem.memberId}"/>
																			</c:set>	
																			<c:set target="${urlParams1}" property="action" value="edit" />
																				<c:set var="translation">
																					<digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>
																				</c:set>
																			
																				<c:out value="${mem.memberName}"/>
																			
																		</td>
																		<td >
																			<c:out value="${mem.email}"/>
																		</td>																		
																		</c:if>
																	</c:forEach>																	
																</tr>
															</table>
														</td>
													</tr>

													<tr>
														<td align="center"  >
															<table width="98%" cellPadding=3 cellspacing="0" vAlign="top" 
															 id="dataTable">
																<tr>
																	<td class="inside" style="color:black; background:url(/TEMPLATE/ampTemplate/img_2/ins_bg.gif) repeat-x; border-bottom:1px solid #cccccc;border-left: 1px solid #D0D0D0;color:#767676;">
																		<b>
																			<digi:trn key="aim:teamMemberName">Name</digi:trn>
																		</b>
																	</td>
																	<td class="inside" style="color:black;background:url(/TEMPLATE/ampTemplate/img_2/ins_bg.gif) repeat-x; border-bottom:1px solid #cccccc;color:#767676;">
																		<b>
																			<digi:trn key="aim:userId">User Id</digi:trn>
																		</b>
																	</td>
																	<td class="inside" style="color:black;background:url(/TEMPLATE/ampTemplate/img_2/ins_bg.gif) repeat-x; border-bottom:1px solid #cccccc;border-right: 1px solid #D0D0D0;color:#767676;">
																		<b>
																			<digi:trn key="aim:role">Role</digi:trn>
																		</b>
																	</td>
																</tr>
																<c:forEach var="mem" items="${aimTeamMemberForm.teamMembers}">
																<c:if test="${mem.teamHead == false}">
																<bean:define id="myMemberRole" type="java.lang.String">${mem.roleName}</bean:define>
																<tr>
																	<td width="50%" style="color:black;border-bottom:1px solid #cccccc;border-left: 1px solid #D0D0D0;">
																		<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams}" property="id">
																			<c:out value="${mem.memberId}"/>
																		</c:set>	
																		<c:set target="${urlParams}" property="action" value="edit" />
																		<c:set var="translation">
																			<digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>
																		</c:set>
<!--																		<digi:link href="/viewMemberDetails.do" name="urlParams" title="${translation}" >-->
<!--																			<c:out value="${mem.memberName}"/>-->
<!--																		</digi:link>-->
																		<a href="javascript:showUserProfile(${mem.memberId})" title="${translation}">${mem.memberName}</a>
																	</td>
																	<td  style="color:black;border-bottom:1px solid #cccccc;border-left: 1px solid #D0D0D0;">
																		<c:out value="${mem.email}"/>
																	</td>
																	<td  style="color:black;border-bottom:1px solid #cccccc;border-right: 1px solid #D0D0D0;border-left: 1px solid #D0D0D0;">
																		<digi:trn key="<%=org.digijava.module.aim.dbentity.AmpTeamMemberRoles.getAmpTeamMemberKey( myMemberRole )%>">
																			${mem.roleName}
																		</digi:trn>
																	</td>																	
																</tr>
																</c:if>
																</c:forEach>																	
															</table>
														</td>
													</tr>
													
													</logic:notEmpty>
												</table>
											</td>
										</tr>
									</table>
                                    </div>
								</td>
							</tr>
						</table>			
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>




<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>

