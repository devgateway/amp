<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">


function openOrgWindow(wndWidth, wndHeight,location){
  window.name = "opener" + new Date().getTime();
  if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
    wndWidth = window.screen.availWidth/2;
    wndHeight = window.screen.availHeight/2;
  }

 
  popupPointer = window.open(location, "orgPopup", "height=" + wndHeight + ",width=" + wndWidth + ",menubar=no,scrollbars=no");
}

function loadPage(){
	document.aimAddOrgForm.actionFlag.value="editOrgGroup";
	document.aimAddOrgForm.submit();
}
	function orgTypeChanged() {
		var flag = false;
		var index = document.aimAddOrgForm.ampOrgTypeId.selectedIndex;
		if (document.aimAddOrgForm.ampOrgTypeId.options[index].value != "-1") {
			document.aimAddOrgForm.regionId.style.display = 'none';
			if (document.aimAddOrgForm.ampOrgTypeId.options[index].text == "Ethiopian Government"
					|| document.aimAddOrgForm.ampOrgTypeId.options[index].text == "National NGO") {
				if (document.aimAddOrgForm.orgTypeFlag.value != "national") {
					//if (document.aimAddOrgForm.orgTypeFlag.value == "multilateral")
						flag = true;
					document.aimAddOrgForm.orgTypeFlag.value = "national";
				}
				else
					return false;
			}
			else if (document.aimAddOrgForm.ampOrgTypeId.options[index].text == "Regional Government") {
              if (document.aimAddOrgForm.orgTypeFlag.value != "regional") {
						/*
						if (document.aimAddOrgForm.orgTypeFlag.value == "multilateral")
							flag = true;
						else
							document.aimAddOrgForm.regionId.style.display = '';
						*/
						flag = true;
						document.aimAddOrgForm.orgTypeFlag.value = "regional";
					}
					else
						return false;
				 }
			 	else if (document.aimAddOrgForm.ampOrgTypeId.options[index].text == "Multilateral") {
							if (document.aimAddOrgForm.orgTypeFlag.value != "multilateral") {
								document.aimAddOrgForm.orgTypeFlag.value = "multilateral";
								flag = true;
							}
							else
								return false;
				 		}
				 		else if (document.aimAddOrgForm.orgTypeFlag.value != "others") {
				 				//if (document.aimAddOrgForm.orgTypeFlag.value == "multilateral")
									flag = true;
			 					document.aimAddOrgForm.orgTypeFlag.value = "others";
			 				}
			 		 		else
			 		 			return false;

			if (flag == true) {
                document.aimAddOrgForm.submit();
			}
		}
		else {
			if (document.aimAddOrgForm.orgTypeFlag.value == "regional")
				document.aimAddOrgForm.regionId.style.display = 'none';
			document.aimAddOrgForm.orgTypeFlag.value = "none";
			return false;
		}
	}

	// defunct
	function countryChanged() {	/*
		var index = document.aimAddOrgForm.levelId.selectedIndex;
		document.aimAddOrgForm.regionFlag.value = "changed";
		document.aimAddOrgForm.regionId.style.display = 'none';
		if (document.aimAddOrgForm.levelId.options[index].text == "REGIONAL") {
			document.aimAddOrgForm.submit();
		} */
	}
	// defunct
	function levelChanged() { /*
		var index = document.aimAddOrgForm.levelId.selectedIndex;
		if (document.aimAddOrgForm.levelId.options[index].text == "REGIONAL") {
			document.aimAddOrgForm.levelFlag.value = "regional";
			if (document.aimAddOrgForm.regionFlag.value == "changed") {
				document.aimAddOrgForm.submit();
			}
			else
				document.aimAddOrgForm.regionId.style.display = '';
		}
		else {
				document.aimAddOrgForm.regionId.style.display = 'none';
				document.aimAddOrgForm.levelFlag.value = "others";
		} */
	}

	function msg() {
		if (confirm("Are you sure about deleting this organization ?")) {
			document.aimAddOrgForm.actionFlag.value = "delete";
			document.aimAddOrgForm.saveFlag.value = "yes";
			document.aimAddOrgForm.submit();
		}
		else
			return false;
	}

	function move() {
		<digi:context name="selectLoc" property="context/module/moduleinstance/organisationManager.do" />
		url = "<%= selectLoc %>?orgSelReset=true";
		document.location.href = url;
	}

	function check() {
		var str1 = document.aimAddOrgForm.name.value;
		str1 = trim(str1);
		var index1 = document.aimAddOrgForm.ampOrgTypeId.selectedIndex;
		var val1 = document.aimAddOrgForm.ampOrgTypeId.options[index1].value;
		var index2 = document.aimAddOrgForm.ampOrgGrpId.selectedIndex;
		var val2 = document.aimAddOrgForm.ampOrgGrpId.options[index2].value;
		//var index3 = document.aimAddOrgForm.levelId.selectedIndex;
		//var val3 = document.aimAddOrgForm.levelId.options[index3].value;
		var index4 = document.aimAddOrgForm.regionId.selectedIndex;
		var val4 = document.aimAddOrgForm.regionId.options[index4].value;
		var val5 = trim(document.aimAddOrgForm.acronym.value);
		var val6 = trim(document.aimAddOrgForm.orgCode.value);

		if (str1.length == 0 || str1 == null) {
			alert("Please enter name for this Organization.");
			document.aimAddOrgForm.name.focus();
			return false;
		}
		if (val5.length == 0 || val5 == null) {
			alert("Please enter acronym for this Organization.");
			document.aimAddOrgForm.acronym.focus();
			return false;
		}
		if (val6.length == 0 || val6 == null) {
			alert("Please enter code for this Organization.");
			document.aimAddOrgForm.orgCode.focus();
			return false;
		}
		if ( val1 == "-1") {
			alert("Please select type of this Organization.");
			document.aimAddOrgForm.ampOrgTypeId.focus();
			return false;
		}
		//if ( val3 == "-1" && val4 != "-1") {
			//alert("Please specify level [Federal/Regional] for this Organization.");
			//document.aimAddOrgForm.levelId.focus();
			//return false;
		//}
		document.aimAddOrgForm.saveFlag.value = "yes";
		document.aimAddOrgForm.name.value = str1;
		document.aimAddOrgForm.submit();
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

</script>

<digi:instance property="aimAddOrgForm" />
<digi:context name="digiContext" property="context"/>

<digi:form action="/editOrganisation.do" method="post">
<html:hidden property="actionFlag" />
<html:hidden property="ampOrgId" />
<html:hidden property="regionFlag" />
<html:hidden property="orgTypeFlag" />
<html:hidden property="levelFlag" />
<html:hidden property="saveFlag" />
<input type="hidden" name="currUrl" value="">

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=5 cellSpacing=1 width=705>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=752>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>

						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/organisationManager.do?orgSelReset=true" styleClass="comment">
						<digi:trn key="aim:organizationManager">
                        Organization Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimAddOrgForm" property="actionFlag" value="create" >
							<digi:trn key="aim:addOrganization">Add Organizations</digi:trn>
						</logic:equal>
						<logic:equal name="aimAddOrgForm" property="actionFlag" value="editOrgGroup" >
							<digi:trn key="aim:addOrganization">Add Organizations</digi:trn>
						</logic:equal>
						<logic:equal name="aimAddOrgForm" property="actionFlag" value="edit" >
							<digi:trn key="aim:editOrganization">Edit Organization</digi:trn>
						</logic:equal>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:organizationManager">Organization Manager
						</digi:trn>
						</span>
						<br>
						<logic:equal name="aimAddOrgForm" property="flag" value="orgReferences" >
							<b><digi:trn key="aim:cannotDeleteOrgMsg"><font color="#FF0000">
                               		Can not delete this organization as it is currently in use !</font>
							   </digi:trn>
							</b>
						</logic:equal>
						<logic:equal name="aimAddOrgForm" property="flag" value="orgCodeExist" >
							<b><digi:trn key="aim:orgCodeExistMsg"><font color="#FF0000">
                               		Please choose other organization code as it is currently in use by some other organization !</font>
							   </digi:trn>
							</b>
						</logic:equal>
					</td>
				</tr>
				<tr>
					<td>
						<digi:trn key="um:allMarkedRequiredField">All fields marked with <font size="2" color="#FF0000">*</font> are required.</digi:trn>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="740" cellspacing=1 cellSpacing=1>
					<tr>
						<td noWrap width=616 vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class="box-border-nopadding" width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="562" border=0>
											<tr>
												<td bgColor=#ffffff class=box-border width="560">
													<table border=0 cellPadding=1 cellSpacing=1 class="box-border" width="100%">
														<tr bgColor=#dddddb>
															<td bgColor=#dddddb height="20" align="center" colspan="5">
																<logic:equal name="aimAddOrgForm" property="actionFlag" value="create" >
																	<b><digi:trn key="aim:addOrganization">Add
                                                                    Organization</digi:trn></b>
																</logic:equal>
																<logic:equal name="aimAddOrgForm" property="actionFlag" value="edit" >
                                                          			<b><digi:trn key="aim:editOrganization">Edit
                                                                    Organization</digi:trn></b>
																</logic:equal>
															</td>
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">
																<table width="563" border=0	 bgColor=#f4f4f2 height="363">
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:organizationName">Organization Name</digi:trn><font size="2" color="#FF0000">*</font>
																		</td>
																	    <td width="380" height="30" colspan="2" >
																	          <html:text name="aimAddOrgForm" property="name" size="54" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgAcronym">Organization Acronym</digi:trn><font size="2" color="#FF0000">*</font>
																		</td>
																	    <td width="380" height="30" colspan="2" >
																	          <html:text name="aimAddOrgForm" property="acronym" size="54" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:organizationType">Organization Type</digi:trn><font size="2" color="#FF0000">*</font>
																		</td>
																		<td width="190" height="30">
																           <html:select property="ampOrgTypeId" onchange="return orgTypeChanged()">
																				<html:option value="-1">-- Organization Type --</html:option>
																				<logic:notEmpty name="aimAddOrgForm" property="orgType">
																					<html:optionsCollection name="aimAddOrgForm" property="orgType"
																		   				value="ampOrgTypeId" label="orgType" />
																		   		</logic:notEmpty>
																			</html:select>
																		</td>
																		<td width="190" height="30">
																		<logic:equal name="aimAddOrgForm" property="regionFlag" value="hide" >
																			<html:select property="regionId" style="display:none">
																	    			<html:option value="-1">-- Specify Region --</html:option>
																					<logic:notEmpty name="aimAddOrgForm" property="region">
																						<html:optionsCollection name="aimAddOrgForm" property="region"
																		   					value="ampRegionId" label="name" />
																		   			</logic:notEmpty>
																			</html:select>
																		</logic:equal>
																		<logic:equal name="aimAddOrgForm" property="regionFlag" value="show" >
																			<html:select property="regionId" style="display:block">
																	    			<html:option value="-1">-- Specify Region --</html:option>
																					<logic:notEmpty name="aimAddOrgForm" property="region">
																						<html:optionsCollection name="aimAddOrgForm" property="region"
																		   					value="ampRegionId" label="name" />
																		   			</logic:notEmpty>
																			</html:select>
																		</logic:equal>
																	    </td>
																	</tr>
                                                          			<tr>
																		<td width="169" align="right" height="30">
                                                                    		<digi:trn key="aim:organizationGroup">Organization Group</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:select property="ampOrgGrpId">
                                                                    			<html:option value="-1">-- Select Group --</html:option>
																	    		<logic:notEmpty name="aimAddOrgForm" property="orgGroup">
																					<html:optionsCollection name="aimAddOrgForm" property="orgGroup"
																		   				value="ampOrgGrpId" label="orgGrpName" />
																		   		</logic:notEmpty>
																			</html:select>
																		</td>
                                                          			</tr>
                                                          			<tr>
																		<td width="169" align="right" height="2">
                                                                            &nbsp;
																		</td>
																		<td width="380" height="1" colspan="2">
                                                                 				<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																				<a href="javascript:loadPage()">
																					<digi:trn key="aim:addOrganizationGroup">Add a Group</digi:trn>
																				</a>
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:organizationDac">DAC Code</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2">
																	          <html:text property="dacOrgCode" size="15" />
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:organizationIsoCode">ISO Code</digi:trn>
																		</td>
																		<td width="380" height="30" colspan="2">
																	          <html:text name="aimAddOrgForm" property="orgIsoCode" size="15" />
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                   			<digi:trn key="aim:organizationCode">Organization Code</digi:trn><font size="2" color="#FF0000">*</font>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    		<html:text property="orgCode" size="15" />
                                                               			</td>
																	</tr>
																<%--<logic:notEqual name="aimAddOrgForm" property="actionFlag" value="edit" >
                                                          			<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:organizationCountry">Country</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
																	    	<html:select property="countryId" onchange="countryChanged()">
																	    		<logic:notEmpty name="aimAddOrgForm" property="country">
																					<html:optionsCollection name="aimAddOrgForm" property="country"
																		   				value="iso" label="countryName" />
																		   		</logic:notEmpty>
																			</html:select>
																		 </td>
                                                          			</tr>
                                                          			</logic:notEqual>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:organizationLevel">Federal / Regional</digi:trn>
																		</td>
																	    <td width="168" height="30">
																	    	<html:select property="levelId" onchange="levelChanged()">
																	    		<html:option value="-1">-- Select Level --</html:option>
																	    		<logic:notEmpty name="aimAddOrgForm" property="level">
																					<html:optionsCollection name="aimAddOrgForm" property="level"
																		   				value="ampLevelId" label="name" />
																		   		</logic:notEmpty>
																			</html:select>
																		</td>
																	    <td width="206" height="30">
																	    		<html:select property="regionId">
																	    			<html:option value="-1">-- Specify Region --</html:option>
																					<logic:notEmpty name="aimAddOrgForm" property="region">
																						<html:optionsCollection name="aimAddOrgForm" property="region"
																		   					value="ampRegionId" label="name" />
																		   			</logic:notEmpty>
																				</html:select>
																	    </td>
																	</tr>--%>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                 			<digi:trn key="aim:fiscalCalendar">Fiscal Calendar</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    			<html:select property="fiscalCalId">
																					<html:option value="-1">-- Fiscal Calendar --</html:option>
																					<logic:notEmpty name="aimAddOrgForm" property="fiscalCal">
																						<html:optionsCollection name="aimAddOrgForm" property="fiscalCal"
																		   					value="ampFiscalCalId" label="name" />
																		   			</logic:notEmpty>
																				</html:select>&nbsp;&nbsp;
																			<%--<digi:link href="/addFiscalCalendar.do">
																				<digi:trn key="aim:addFiscalCalendar">Add Fiscal Calendar</digi:trn>
																			</digi:link>--%>
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
                                                                     		<digi:trn key="aim:sectorScheme">Sector Scheme</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                    			<html:select property="ampSecSchemeId">
																					<html:option value="-1">-- Sector Scheme --</html:option>
																					<logic:notEmpty name="aimAddOrgForm" property="sectorScheme">
																						<html:optionsCollection name="aimAddOrgForm" property="sectorScheme"
																		   				value="ampSecSchemeId" label="secSchemeName" />
																		   			</logic:notEmpty>
																				</html:select>
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactName">Contact Person Name</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                              				<html:text property="contactPersonName" size="35" />
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactTitle">Contact Person Title</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                              				<html:text property="contactPersonTitle" size="20" />
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactPhone">Contact Phone</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                 			<html:text property="phone" size="35" />
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactFax">Contact Fax</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                 			<html:text property="fax" size="35" />
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgContactEmail">Contact Email</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                 			<html:text property="email" size="35" />
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:organizationUrl">Organization URL</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                 			<html:text property="orgUrl" size="54" />
																		</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																			<digi:trn key="aim:orgAddress">Address</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
                                                                 			<html:textarea property="address" cols="40" rows="3" />
                                                              			</td>
																	</tr>
																	<tr>
																		<td width="169" align="right" height="30">
																	        <digi:trn key="aim:organizationDescription">Description</digi:trn>
																		</td>
																	    <td width="380" height="30" colspan="2">
																              <html:textarea property="description"  cols="40" rows="3"/>
																		</td>
																	</tr>
																	<tr>
																		<td colspan="3" width="555"  align="center" height="30">
																			<table width="100%" cellspacing="5">
																				<tr>
																					<td width="42%" align="right">
																						<input type="button" value="Save" class="dr-menu" onclick="return check()">
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
																	<logic:equal name="aimAddOrgForm" property="flag" value="delete" >
																		<tr>
																			<td colspan="3" width="555"  align="center" height="27">
																				<input type="button" value="Delete this Organization" class="dr-menu" onclick="return msg()">
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
						<td noWrap width=110 vAlign="top">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</td>
	</tr>
</table>

<script language="JavaScript">
if(document.aimAddOrgForm.actionFlag.value == "editOrgGroup") {
	document.aimAddOrgForm.actionFlag.value="";
   <digi:context name="selectLoc" property="context/module/moduleinstance/editOrgGroup.do" />
	 url = "<%= selectLoc %>?action=createGroup&ampOrgId=" + document.aimAddOrgForm.ampOrgId.value;
	window.open(url, "orgPopup", "height=" + 190 + ",width=" + 610 + ",menubar=no,scrollbars=no");
}
</script>
</digi:form>


