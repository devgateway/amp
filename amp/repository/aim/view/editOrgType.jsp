<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	function msg() {
          var msg="<digi:trn>Are you sure about deleting this Organization Type ?</digi:trn>"
		if (confirm(msg)) {
			document.aimAddOrgTypeForm.action.value = "delete";
			document.aimAddOrgTypeForm.submit();
		}
		else
			return false;
	}

	function move() {
		<digi:context name="selectLoc" property="context/module/moduleinstance/orgTypeManager.do" />
		url = "<%= selectLoc %>";
		document.location.href = url;
	}

	function check() {
		var str = document.aimAddOrgTypeForm.orgType.value;
		var code = trim(document.aimAddOrgTypeForm.orgTypeCode.value);
		str = trim(str);

		if (str == null || str.length == 0) {
			alert("Please enter name for this Type");
			document.aimAddOrgTypeForm.orgType.focus();
			return false;
		}
		else if (code == null || code.length == 0) {
			alert("Please enter code for this Type");
			document.aimAddOrgTypeForm.orgTypeCode.focus();
			return false;
		}
		else {
			document.aimAddOrgTypeForm.orgType.value = str;
			document.aimAddOrgTypeForm.submit();
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

	function resetRadioButtons(){
        $("input:radio").attr("checked", false);
    }

	var enterBinder	= new EnterHitBinder('addOrgTypeBtn');
</script>

<digi:instance property="aimAddOrgTypeForm" />
<digi:context name="digiContext" property="context"/>
<div class="admin-content">
<digi:form action="/editOrgType.do" method="post">
<html:hidden property="action" />
<html:hidden property="ampOrgTypeId" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
	<tr>
	  <td align=left valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
		  <tr>
					<!-- Start Navigation -->
					<td height=33 colspan="5"><span class=crumb>

						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/orgTypeManager.do" styleClass="comment">
						<digi:trn key="aim:orgTypeManager">
						Oraganization Type Manager						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimAddOrgTypeForm" property="action" value="create" >
							<digi:trn key="aim:addOrgType">Add Organization Type</digi:trn>
						</logic:equal>
						<logic:equal name="aimAddOrgTypeForm" property="action" value="edit" >
							<digi:trn key="aim:editOrgType">Edit Organization Type</digi:trn>
						</logic:equal>
                      </span>					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571 colspan="5"><span class=subtitle-blue>
						<!--<digi:trn key="aim:orgTypeManager">
						Oraganization Type Manager						</digi:trn>-->
						</span>
						<digi:trn>All fields marked with</digi:trn> <font size="2" color="#FF0000">*</font> <digi:trn>are required</digi:trn>						
						<digi:errors/>					</td>
				</tr>
				<tr>
					<td noWrap width="571" vAlign="top">
					<tr>
						<td colspan="2" align="center" vAlign="top" noWrap>
					  <table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td valign="top">
										<table cellPadding="0" cellSpacing="0" width="100%" border="0">
											<tr>
												<td bgColor=#ffffff>
													<table border="0" cellpadding="1" cellspacing="1" width="100%">
														<tr>
															<td height="25" bgcolor=#c7d4db align="center" colspan="5" style="font-size:12px; font-weight:bold; ">
																<logic:equal name="aimAddOrgTypeForm" property="action" value="create" >
																	<b><digi:trn key="aim:addOrgType">Add Organization Type</digi:trn></b>																</logic:equal>
																<logic:equal name="aimAddOrgTypeForm" property="action" value="edit" >
																	<b><digi:trn key="aim:editOrgType">Edit Organization Type</digi:trn></b>																</logic:equal>															</td>
														</tr>
														<!-- Page Logic -->
														<tr>
															<td  bgColor=#f4f4f2 style="border:1px solid #ccc;">
																<table width="500px" cellPadding=3 cellSpacing=3 border="0" style="margin-top:15px;" align="center">
															  		<tr>
																		<td width="100" >
																		<digi:trn key="aim:orgTypeName">Name</digi:trn><font size="2" color="#FF0000">*</font></td>																
																	    <td width="400" >
																	          <html:text property="orgType" size="60" style="border:1px solid #CCCCCC;" />																	    </td>
																	</tr>
																	<tr>
																		<td width="100" >
																        <digi:trn key="aim:orgTypeCode">Type Code</digi:trn><font size="2" color="#FF0000">*</font></td>
																		<td width="400">
																           <html:text property="orgTypeCode" size="60" style="border:1px solid #CCCCCC;" />																		</td>
																	</tr>
																	<tr>
                                                                        <td colspan="2" align="center">                                   
                                                                         <feature:display name="Organization Manager Type" module="Organization Manager" >
                                                                            <fieldset class="org-type-fieldset">
                                                                            <html:radio property="classification" value="GOVERNMENTAL"><digi:trn>Governmental</digi:trn></html:radio><br>
                                                                            <html:radio property="classification" value="NGO"><digi:trn>NGO</digi:trn></html:radio><br>
                                                                            <html:radio property="classification" value="REGIONAL"><digi:trn>Regional</digi:trn></html:radio><br>
                                                                            <html:radio property="classification" value="FUND"><digi:trn>FUND</digi:trn></html:radio><br>
                                                                            <input style="margin-top:10px;" type="button" onclick="resetRadioButtons()" value="<digi:trn>Deselect</digi:trn>" class="buttonx"/>
                                                                            </fieldset> 
                                                                          </feature:display> 
                                                                      </td>
																	</tr>
																																		
																	<tr align="center">
																		<td colspan="2" width="60%">
																			<table width="100%" cellspacing="5">
																				<tr>
																					<td align="center">
																						<c:set var="translation">
																							<digi:trn key="aim:btnSave">Save</digi:trn>
																						</c:set>
																						<input type="button" value="${translation}" class="buttonx" onclick="check()" id="addOrgTypeBtn">																					
																						<c:set var="translation">
																							<digi:trn key="aim:btnCancel">Cancel</digi:trn>
																						</c:set>
																						<input type="button" value="${translation}" class="buttonx" onclick="move()">	
                                                                                        		<logic:equal name="aimAddOrgTypeForm" property="deleteFlag" value="delete" >
																		
																				<c:set var="translation">
																					<digi:trn key="aim:btnDeleteThisType">Delete this Type</digi:trn>
																				</c:set>
																				<input type="button" value="${translation}" class="buttonx" onclick="msg()">
																	</logic:equal>																				</td>
																				</tr>
																			</table>																	  </td>
																  </tr>
															
																	<logic:equal name="aimAddOrgTypeForm" property="deleteFlag" value="orgReferences" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<b><digi:trn key="aim:cannotDeleteOrgTypeMsg"><font color="#FF0000">
																						Can not delete this Type as some organization/group references it!</font>
																					</digi:trn>
																				</b>																			</td>
																		</tr>
																	</logic:equal>
																</table>														  </td>
														</tr>
													<!-- end page logic -->
													</table>												</td>
											</tr>
										</table>									</td>
								</tr>
						  </table>						</td>
			  </tr>
				</table>
	  </td>
		</tr>
	</table>
	</td>
	</tr>
</digi:form>
</div>