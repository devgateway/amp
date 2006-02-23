<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	function msg() {
		if (confirm("Are you sure about deleting this Organization Group ?")) {
			document.aimAddOrgGroupForm.action.value = "delete";
			document.aimAddOrgGroupForm.submit();
		}
		else
			return false;
	}
	
	function move() {
		<digi:context name="selectLoc" property="context/module/moduleinstance/orgGroupManager.do" />
		url = "<%= selectLoc %>";
		document.location.href = url;
	}
	
	function check() {
		var str = document.aimAddOrgGroupForm.orgGrpName.value;
		var type = trim(document.aimAddOrgGroupForm.orgTypeId.value);
		str = trim(str);
		
		if (str == null || str.length == 0) {
			alert("Please enter name for this Group");
			document.aimAddOrgGroupForm.orgGrpName.focus();
			return false;
		}
		else if (type == null || type == "-1" || type.length == 0) {
			alert("Please select type for this Group");
			document.aimAddOrgGroupForm.orgTypeId.focus();
			return false;
		}
		else {
			document.aimAddOrgGroupForm.orgGrpName.value = str;
			document.aimAddOrgGroupForm.submit();
		}
	}
	
	function trim ( inputStringTrim ) {
		fixedTrim = "";
		lastCh = " ";
		for (x=0; x < inputStringTrim.length; x++) {
			ch = inputStringTrim.charAt(x);
			if ((ch != " ") || (lastCh != " ")) { fixedTrim += ch; }
				lastCh = ch;
		}
		if (fixedTrim.charAt(fixedTrim.length - 1) == " ") {
			fixedTrim = fixedTrim.substring(0, fixedTrim.length - 1); }
		return fixedTrim
	}

</script>

<digi:instance property="aimAddOrgGroupForm" />
<digi:context name="digiContext" property="context"/>

<digi:form action="/editOrgGroup.do" method="post">
<html:hidden property="action" />
<html:hidden property="ampOrgGrpId" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
	
						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/orgGroupManager.do" styleClass="comment">
						<digi:trn key="aim:orgGroupManager">
						Oraganization Group Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimAddOrgGroupForm" property="action" value="create" >
							<digi:trn key="aim:addOrgGroup">Add Organization Group</digi:trn>	
						</logic:equal>
						<logic:equal name="aimAddOrgGroupForm" property="action" value="edit" >
							<digi:trn key="aim:editOrgGroup">Edit Organization Group</digi:trn>	
						</logic:equal>
                      </span>	
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:orgGroupManager">
						Oraganization Group Manager
						</digi:trn>
						</span>
						<br>
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr>
						<td noWrap width=600 vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>	
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
														<tr bgColor=#dddddb>
															<td bgColor=#dddddb height="20" align="center" colspan="5">
																<logic:equal name="aimAddOrgGroupForm" property="action" value="create" >
																	<b><digi:trn key="aim:addOrgGroup">Add Organization Group</digi:trn></b>	
																</logic:equal>
																<logic:equal name="aimAddOrgGroupForm" property="action" value="edit" >
																	<b><digi:trn key="aim:editOrgGroup">Edit Organization Group</digi:trn></b>	
																</logic:equal>
															</td>
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">	
																<table width="100%" border=0	 bgColor=#f4f4f2>
																	<tr>
																		<td width="30%" align="right">
																		<digi:trn key="aim:orgGroupName">Name</digi:trn>	</td>
																	    <td width="30%" >
																	          <html:text property="orgGrpName" size="35" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:orgGroupCode">Group
                                                                            Code</digi:trn>
																		</td>
																		<td width="30%">
																           <html:text property="orgGrpCode" size="15" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:orgGroupType">Type</digi:trn>
																		</td>
																		<td width="30%">
																	          <html:select property="orgTypeId">
																	    		<html:option value="-1">-- Select Type --</html:option>
																	    		<logic:notEmpty name="aimAddOrgGroupForm" property="orgTypeColl">
																					<html:optionsCollection name="aimAddOrgGroupForm" property="orgTypeColl" 
																		   									value="ampOrgTypeId" label="orgType" />
																		   		</logic:notEmpty>
																			</html:select>
																		</td>
																	</tr>
																	<%--
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:orgGroupLevel">Level</digi:trn>
																		</td>
																		<td width="30%">
																	          <html:select property="levelId">
																	    		<html:option value="-1">-- Select Level --</html:option>
																	    		<logic:notEmpty name="aimAddOrgGroupForm" property="level">
																					<html:optionsCollection name="aimAddOrgGroupForm" property="level" 
																		   				value="ampLevelId" label="name" />
																		   		</logic:notEmpty>
																			</html:select>
																		</td>
																	</tr> --%>
																	<tr>
																		<td colspan="2" width="60%"  align="center">
																			<table width="100%" cellspacing="5">
																				<tr>
																					<td width="45%" align="right">
																						<input type="button" value="Save" class="dr-menu" onclick="check()">
																					</td>
																					<td width="8%" align="left">
																						<input type="reset" value="Reset" class="dr-menu">
																					</td>
																					<td width="45%" align="left">
																						<input type="button" value="Cancel" class="dr-menu" onclick="move()">
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<logic:equal name="aimAddOrgGroupForm" property="flag" value="delete" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<input type="button" value="Delete this Group" class="dr-menu" onclick="msg()">
																			</td>
																		</tr>
																	</logic:equal>
																	<logic:equal name="aimAddOrgGroupForm" property="flag" value="orgReferences" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<b><digi:trn key="aim:cannotDeleteOrgGrpMsg"><font color="#FF0000">
																						Can not delete this group since some organization references it !</font>
																					</digi:trn>
																				</b>
																			</td>
																		</tr>
																	</logic:equal>
																</table>	
															</td>
														</tr>
													<!-- end page logic -->
													</table>
												</td>
											</tr>
											
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#f4f4f2>
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
						<td noWrap width=100% vAlign="top">
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
