<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">
    function check() {
		var str = document.aimAddOrgGroupForm.orgGrpName.value;
		var type = trim(document.aimAddOrgGroupForm.orgTypeId.value);
		str = trim(str);
		if (str == null || str.length == 0) {
			alert("Please enter name for this Group");
			document.aimAddOrgGroupForm.orgGrpName.focus();
			return false;
		}
		else if (type == "-1" || type == null || type.length == 0) {
			alert("Please select type for this Group");
			document.aimAddOrgGroupForm.orgTypeId.focus();
			return false;
		}
		else {
			document.aimAddOrgGroupForm.orgGrpName.value = str;
			document.aimAddOrgGroupForm.target = "_self";
			document.aimAddOrgGroupForm.submit();
			return true;
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
		return fixedTrim;
	}

	function load() {
		document.aimAddOrgGroupForm.orgGrpName.focus();
	}
</script>
<digi:instance property="aimAddOrgGroupForm" />
<digi:context name="digiContext" property="context"/>


<digi:form action="/editOrgGroup.do" method="post">	


<c:if test="${aimAddOrgGroupForm.flag=='refreshParent'}">
<script language="JavaScript">
	<digi:context name="selectLoc" property="/aim/editOrganisation.do" />
	url = "<%= selectLoc %>?orgGroupAdded=true&ampOrgId="+window.opener.document.aimAddOrgForm.ampOrgId.value+"&actionFlag="+window.opener.document.aimAddOrgForm.actionFlag.value;
	window.opener.document.aimAddOrgForm.action = url;
	window.opener.document.aimAddOrgForm.target = window.opener.name;
	window.opener.document.aimAddOrgForm.submit();
	window.close();
</script>
</c:if>


<html:hidden property="action" />
<html:hidden property="ampOrgGrpId" />
<html:hidden property="ampOrgId" />

					<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="612">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="610">
                                        &nbsp;<digi:errors/>
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top" width="610">
										<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%" border="0">
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
														<tr bgColor=#dddddb>
															<td bgColor=#dddddb height="20" align="center" colspan="5">
																<b><digi:trn key="aim:addOrgGroup">Add
                                                                    Organization
                                                                    Group</digi:trn></b>
															</td>
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">
																<table width="100%" border="0"	 bgColor=#f4f4f2>
																	<tr>
																		<td width="30%" align="right">
																		<digi:trn key="aim:orgGroupName">Name</digi:trn><font color="#ff0000">*</font>	</td>
																	    <td width="30%" >
																	          <html:text property="orgGrpName" size="45" />
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
																	        <digi:trn key="aim:orgGroupType">Type</digi:trn><font color="#ff0000">*</font>
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
																						<input type="button" value="Cancel" class="dr-menu" onclick="window.close()">
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
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
									<td bgColor=#f4f4f2 width="610">
                                        &nbsp;
									</td>
								</tr>
							</table>

</digi:form>
