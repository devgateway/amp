<%@ page pageEncoding="UTF-8"%>
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

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />
<jsp:include page="addSectors.jsp" flush="true" />

<script language="JavaScript" type="text/javascript">
	window.onload=initSectorScript();
	var oldSchemeValue = -1;
	function checkScheme(){
		if(oldSchemeValue != -1 && document.aimAddOrgForm.ampSecSchemeId.value != oldSchemeValue)
		{
			//If the selected scheme is different, the sectors selected should be reset
			if(confirm("<digi:trn key="aim:editOrganisation:sectorReset">If you change Sector Scheme, the sectors selected will be deleted. Are you sure?</digi:trn>")){
				resetSelectedSectors();
			}
			else
			{
				document.aimAddOrgForm.ampSecSchemeId.value = oldSchemeValue;
			}
		}
	}
	function resetSelectedSectors(){
		
		var sectorsRows = document.getElementsByName("selSectors");
		for(idx=0; idx < sectorsRows.length; idx++)
			sectorsRows[idx].checked = true;
			removeSelSectors();

	}
	function addPledge() {
		<digi:context name="addSec" property="context/module/moduleinstance/editOrganisation.do" />
		document.aimAddOrgForm.action = "<%= addSec %>"+"~ampOrgId="+document.aimAddOrgForm.ampOrgId.value+"~actionFlag="+document.aimAddOrgForm.actionFlag.value+"~addPledge=true";
		document.aimAddOrgForm.target = "_self";
		document.aimAddOrgForm.submit();
	}
	
	function addGroup() {
		openNewWindow(600, 400);
	 	<digi:context name="selectLoc" property="context/module/moduleinstance/editOrgGroup.do" />
        var id = document.aimAddOrgForm.ampOrgId.value;
		url = "<%= selectLoc %>?action=createGroup";
		document.aimAddOrgForm.action = url;
	 	document.aimAddOrgForm.target = popupPointer.name;
		document.aimAddOrgForm.submit();
	}

	function removeFundingDetail(index) {
		var flag = confirm('<digi:trn key="aim:areYouSureRemoveTransaction">Are you sure you want to remove the selected transaction ?</digi:trn>');
		if(flag != false) {
			<digi:context name="addSec" property="context/module/moduleinstance/editOrganisation.do" />
			document.aimAddOrgForm.action = "<%= addSec %>"+"~ampOrgId="+document.aimAddOrgForm.ampOrgId.value+"~actionFlag="+document.aimAddOrgForm.actionFlag.value+"~delPledge=true";
			document.aimAddOrgForm.target = "_self";
			document.aimAddOrgForm.transIndexId.value=index;
			document.aimAddOrgForm.submit();
		}
	}

	function addSector()
	{
		//alert("editOrganisations.addSector()");
		<digi:context name="addSec" property="context/module/moduleinstance/editOrganisation.do" />
		document.aimAddOrgForm.action = "<%= addSec %>"+"~ampOrgId="+document.aimAddOrgForm.ampOrgId.value+"~actionFlag="+document.aimAddOrgForm.actionFlag.value+"~addSector=true";
		document.aimAddOrgForm.target = "_self";
		document.aimAddOrgForm.submit();
	}
	function addSectors(editAct,configId) {
		//alert("editOrganisation.addSectors");
		var schemeId = document.aimAddOrgForm.ampSecSchemeId.value;
		if(schemeId == -1){
			alert('<digi:trn key="aim:editOrganisation:selectScheme">Please select a sector scheme before adding sectors.</digi:trn>');
			return false;
		}
/*
		openNewWindow(600, 450);
		<digi:context name="addSector" property="context/module/moduleinstance/selectSectors.do?edit=true" />
	  	document.aimAddOrgForm.action = "<%= addSector %>&sectorScheme=" +schemeId;
		document.aimAddOrgForm.target = popupPointer.name;
		document.aimAddOrgForm.submit();
*/		
		myAddSectors("edit=true&sectorScheme="+schemeId+"&configId="+configId);
	}
	function removeSelSectors() {
		var flag = validate();
		if (flag == false) return false;

	    <digi:context name="addSec" property="context/module/moduleinstance/editOrganisation.do" />
		document.aimAddOrgForm.action = "<%= addSec %>"+"~ampOrgId="+document.aimAddOrgForm.ampOrgId.value+"~actionFlag=edit~remSectors=true";
		document.aimAddOrgForm.target = "_self";
	    document.aimAddOrgForm.submit();
	    return true;
	}
	function validate(){
		if (document.aimAddOrgForm.selSectors.checked != null) {
			if (document.aimAddOrgForm.selSectors.checked == false) {
				alert("Please choose a sector to remove");
				return false;
			}
		} else {
			var length = document.aimAddOrgForm.selSectors.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimAddOrgForm.selSectors[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("Please choose a sector to remove");
				return false;
			}
		}
		return true;
	}
	
	function addDocumentsDM(documentsType, showTheFollowingDocuments) {
		if (showTheFollowingDocuments==null){
			showTheFollowingDocuments="ALL";
		}
		var url		= "/contentrepository/selectDocumentDM.do?documentsType="+documentsType+"&showTheFollowingDocuments="+showTheFollowingDocuments;
		var popupName	= 'my_popup';
		window.open(url, popupName, 'width=900, height=300');
		document.forms[0].action=url;
		document.forms[0].target=popupName;
		document.forms[0].submit();
	}

</script>
<script language="JavaScript">


function openOrgWindow(wndWidth, wndHeight,location){
  window.name = "opener" + new Date().getTime();
  if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
    wndWidth = window.screen.availWidth/2;
    wndHeight = window.screen.availHeight/2;
  }


  popupPointer = window.open(location, "orgPopup", "height=" + wndHeight + ",width=" + wndWidth + ",menubar=no,scrollbars=no");
}

function newWin() {
  if (document.aimAddOrgForm.currUrl.value == "") {
    loadPage();
  } else
  if(!popupPointer==null){
    popupPointer.focuce();
  }else{
    loadPage();
  }
}

function loadPage()
{
	openNewWindow(600, 400);
 	<digi:context name="selectLoc" property="/aim/editOrganisation.do" />
	  var id = document.aimAddOrgForm.ampOrgId.value;
	url = "<%= selectLoc %>?actiona=createGroup";
	  document.aimAddOrgForm.action = url;
 	 document.aimAddOrgForm.target = popupPointer.name;
 	//alert(document.aimAddOrgForm.actionGroup.value);
	document.aimAddOrgForm.submit();
}


/*
function loadPage()
{
	openNewWindow(600, 400);
 	<digi:context name="selectLoc" property="context/module/moduleinstance/editOrgGroup.do" />
	  var id = document.aimAddOrgForm.ampOrgId.value;
	url = "<%= selectLoc %>?action=createGroup&ampOrgId=" + id;
	  document.aimAddOrgForm.action = url;
 	document.aimAddOrgForm.target = popupPointer.name;
	document.aimAddOrgForm.submit();
}
*/
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
				 }
			 	else if (document.aimAddOrgForm.ampOrgTypeId.options[index].text == "Multilateral") {
							if (document.aimAddOrgForm.orgTypeFlag.value != "multilateral") {
								document.aimAddOrgForm.orgTypeFlag.value = "multilateral";
								flag = true;
							}
				 		}
				 		else if (document.aimAddOrgForm.orgTypeFlag.value != "others") {
				 				//if (document.aimAddOrgForm.orgTypeFlag.value == "multilateral")
									flag = true;
			 					document.aimAddOrgForm.orgTypeFlag.value = "others";
			 				}

		}
		else {
			if (document.aimAddOrgForm.orgTypeFlag.value == "regional")
				document.aimAddOrgForm.regionId.style.display = 'none';
			document.aimAddOrgForm.orgTypeFlag.value = "none";
			return false;
		}

		<digi:context name="addSec" property="context/module/moduleinstance/editOrganisation.do" />
		document.aimAddOrgForm.action = "<%= addSec %>;
		document.aimAddOrgForm.target = "_self";
	    document.aimAddOrgForm.submit();
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
		if (confirm('<digi:trn key="aim:organization:deleteQuestion">Are you sure about deleting this organization?</digi:trn>')) {
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

		var index3 = document.aimAddOrgForm.ampOrgGrpId.selectedIndex;
		var val7 = document.aimAddOrgForm.ampOrgGrpId.options[index3].value;

		if (str1.length == 0 || str1 == null) {		
			alert('<digi:trn key="aim:editOrganisation:enterOrganisationName">Please enter name for this Organization.</digi:trn>');
			document.aimAddOrgForm.name.focus();
			return false;
		}
		if (val5.length == 0 || val5 == null) {		
			alert('<digi:trn key="aim:editOrganisation:enterOrganisationAcronym">Please enter acronym for this Organization.</digi:trn>');
			document.aimAddOrgForm.acronym.focus();
			return false;
		}
		if (val6.length == 0 || val6 == null) {		
			alert('<digi:trn key="aim:editOrganisation:enterOrganisationCode">Please enter code for this Organization.</digi:trn>');
			document.aimAddOrgForm.orgCode.focus();
			return false;
		}
		if ( val1 == "-1") {		
			alert('<digi:trn key="aim:editOrganisation:enterOrganisationType">Please select type of this Organization.</digi:trn>');
			document.aimAddOrgForm.ampOrgTypeId.focus();
			return false;
		}
		if ( val7 == "-1") {		
			alert('<digi:trn key="aim:editOrganisation:enterOrganisationType">Please select group of this Organization.</digi:trn>');
			document.aimAddOrgForm.ampOrgGrpId.focus();
			return false;
		}
		//if ( val3 == "-1" && val4 != "-1") {
			//alert("Please specify level [Federal/Regional] for this Organization.");
			//document.aimAddOrgForm.levelId.focus();
			//return false;
		//}
		document.aimAddOrgForm.saveFlag.value = "yes";
		document.aimAddOrgForm.name.value = str1;
		<digi:context name="addSec" property="context/module/moduleinstance/editOrganisation.do" />
		document.aimAddOrgForm.action = "<%= addSec %>";<!-- +"~ampOrgId="+document.aimAddOrgForm.ampOrgId.value+"~actionFlag=edit~remSectors=true"; -->
		document.aimAddOrgForm.target = "_self";
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
<digi:context name="digiContext" property="context" />

<digi:form action="/editOrganisation.do" method="post">
	<html:hidden property="actionFlag" />
	<html:hidden property="ampOrgId" />
	<html:hidden property="regionFlag" />
	<html:hidden property="orgTypeFlag" />
	<html:hidden property="levelFlag" />
	<html:hidden property="saveFlag" />
	<html:hidden property="transIndexId" />

	<input type="hidden" name="currUrl" value="">

	<!--  AMP Admin Logo -->
	<jsp:include page="teamPagesHeader.jsp" flush="true" />
	<!-- End of Logo -->

	<table bgColor=#ffffff cellPadding=5 cellSpacing=1 >
		<tr>
			<td class=r-dotted-lg width=14>&nbsp;</td>
			<td align=left class=r-dotted-lg vAlign=top width=752>
			<table bgcolor="#ffffff" cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb> <digi:link
						href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp; <digi:link
						href="/organisationManager.do?orgSelReset=true"
						styleClass="comment">
						<digi:trn key="aim:organizationManager">
                        Organization Manager
						</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp; <logic:equal name="aimAddOrgForm"
						property="actionFlag" value="create">
						<digi:trn key="aim:addOrganization">Add Organizations</digi:trn>
					</logic:equal> <logic:equal name="aimAddOrgForm" property="actionFlag"
						value="editOrgGroup">
						<digi:trn key="aim:addOrganization">Add Organizations</digi:trn>
					</logic:equal> <logic:equal name="aimAddOrgForm" property="actionFlag"
						value="edit">
						<digi:trn key="aim:editOrganization">Edit Organization</digi:trn>
					</logic:equal> </span></td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=700><span
						class=subtitle-blue> <digi:trn
						key="aim:organizationManager">Organization Manager
						</digi:trn> </span> <br>
					<logic:equal name="aimAddOrgForm" property="flag"
						value="activityReferences">
						<b><digi:trn key="aim:cannotDeleteOrgMsg">
							<font color="#FF0000"> Can not delete this organization as
							it is currently rererenced by Activities !</font>
						</digi:trn> </b>
					</logic:equal> 

					<logic:equal name="aimAddOrgForm" property="flag"
						value="orgReferences">
						<b><digi:trn key="aim:cannotDeleteOrgMsg">
							<font color="#FF0000"> Can not delete this organization as
							it is currently in use !</font>
						</digi:trn> </b>
					</logic:equal> 
					<logic:equal name="aimAddOrgForm" property="flag"
						value="fundReferences">
						<b><digi:trn key="aim:cannotDeleteOrgMsg">
							<font color="#FF0000"> Can not delete this organization as
							it is currently referenced by fundings !</font>
						</digi:trn> </b>
					</logic:equal> 
					<logic:equal name="aimAddOrgForm" property="flag"
						value="orgCodeExist">
						<b><digi:trn key="aim:orgCodeExistMsg">
							<font color="#FF0000"> Please choose other organization
							code as it is currently in use by some other organization !</font>
						</digi:trn> </b>
					</logic:equal> 
					<logic:equal name="aimAddOrgForm" property="flag"
						value="completePledges">
						<b><digi:trn key="aim:completePledges">
							<font color="#FF0000"> Please complete the fields program,
							ammount and date for each Pledge!</font>
						</digi:trn> </b>
					</logic:equal> 
					<logic:equal name="aimAddOrgForm" property="flag"
						value="orgNameExist">
						<b><digi:trn key="aim:orgNameExistMsg">
							<font color="#FF0000"> Please choose other organization
							name as it is currently in use by some other organization !</font>
						</digi:trn> </b>
					</logic:equal></td>
				</tr>
				<tr>
					<td><digi:trn key="um:allMarkedRequiredField">All fields marked with <font
							size="2" color="#FF0000">*</font> are required.</digi:trn></td>
				</tr>
				<tr>
					<td><digi:errors /></td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table bgcolor="#ffffff" border=0 >
						<tr>
							<td>
							<table cellspacing=1 cellSpacing=1>
								<tr>
									<td noWrap width=690 vAlign="top">
									<table bgColor=#ffffff cellPadding=0 cellSpacing=0
										class="box-border-nopadding" width="100%">
										<tr bgColor=#f4f4f2>
											<td vAlign="top" width="100%">&nbsp;</td>
										</tr>
										<tr bgColor=#ffffff>
											<td valign="top">
											<table align=center bgColor=#f4f4f2 cellPadding=0
												cellSpacing=0 border=0>
												<tr>
													<td bgColor=#ffffff class=box-border width="680">
													<table border=0 cellPadding=1 cellSpacing=1
														class="box-border" width="100%">
														<tr bgColor=#dddddb>
															<td bgColor=#dddddb height="20" align="center"
																colspan="5"><logic:equal name="aimAddOrgForm"
																property="actionFlag" value="create">
																<b><digi:trn key="aim:addOrganization">Add
                                                                    Organization</digi:trn></b>
															</logic:equal> <logic:equal name="aimAddOrgForm" property="actionFlag"
																value="edit">
																<b><digi:trn key="aim:editOrganization">Edit
                                                                    Organization</digi:trn></b>
															</logic:equal></td>
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">
															<table border=0 bgColor=#f4f4f2 height="363">
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:organizationName">Organization Name</digi:trn><font
																		size="2" color="#FF0000">*</font></td>
																	<td width="500" height="30" colspan="2"><html:text
																		name="aimAddOrgForm" property="name" size="54" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:orgAcronym">Organization Acronym</digi:trn><font
																		size="2" color="#FF0000">*</font></td>
																	<td width="500" height="30" colspan="2"><html:text
																		name="aimAddOrgForm" property="acronym" size="54" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:organizationType">Organization Type</digi:trn><font
																		size="2" color="#FF0000">*</font></td>
																	<td width="190" height="30"><html:select
																		property="ampOrgTypeId"
																		onchange="return orgTypeChanged()">
																		<c:set var="translation">
																			<digi:trn
																				key="aim:editOrganisationSelectOrganizationType">Organization Type</digi:trn>
																		</c:set>
																		<html:option value="-1">-- ${translation} --</html:option>
																		<logic:notEmpty name="aimAddOrgForm"
																			property="orgType">
																			<html:optionsCollection name="aimAddOrgForm"
																				property="orgType" value="ampOrgTypeId"
																				label="orgType" />
																		</logic:notEmpty>
																	</html:select></td>
																	<td width="190" height="30"><logic:equal
																		name="aimAddOrgForm" property="regionFlag"
																		value="hide">
																		<html:select property="regionId" style="display:none">
																			<c:set var="translation">
																				<digi:trn
																					key="aim:editOrganisationSelectSpecifyRegion">Specify Region</digi:trn>
																			</c:set>
																			<html:option value="-1">-- ${translation} --</html:option>
																			<logic:notEmpty name="aimAddOrgForm"
																				property="region">
																				<html:optionsCollection name="aimAddOrgForm"
																					property="region" value="ampRegionId" label="name" />
																			</logic:notEmpty>
																		</html:select>
																	</logic:equal> <logic:equal name="aimAddOrgForm"
																		property="regionFlag" value="show">
																		<html:select property="regionId" style="display:block">
																			<c:set var="translation">
																				<digi:trn
																					key="aim:editOrganisationSelectSpecifyRegion">Specify Region</digi:trn>
																			</c:set>
																			<html:option value="-1">-- ${translation} --</html:option>
																			<logic:notEmpty name="aimAddOrgForm"
																				property="region">
																				<html:optionsCollection name="aimAddOrgForm"
																					property="region" value="ampRegionId" label="name" />
																			</logic:notEmpty>
																		</html:select>
																	</logic:equal></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:organizationGroup">Organization Group</digi:trn><font
																		size="2" color="#FF0000">*</font></td>
																	<td width="500" height="30" colspan="2"><html:select
																		property="ampOrgGrpId">
																		<c:set var="translation">
																			<digi:trn key="aim:editOrganisationSelectGroup">Select Group</digi:trn>
																		</c:set>
																		<html:option value="-1">-- ${translation} --</html:option>
																		<logic:notEmpty name="aimAddOrgForm"
																			property="orgGroup">
																			<html:optionsCollection name="aimAddOrgForm"
																				property="orgGroup" value="ampOrgGrpId"
																				label="orgGrpName" />
																		</logic:notEmpty>
																	</html:select></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="2">&nbsp;</td>
																	<td width="500" height="1" colspan="2"><digi:img
																		src="module/aim/images/arrow-014E86.gif" width="15"
																		height="10" /> <a href="javascript:addGroup()"> <digi:trn
																		key="aim:addOrganizationGroup">Add a Group</digi:trn>
																	</a></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:organizationDac">DAC Code</digi:trn></td>
																	<td width="500" height="30" colspan="2"><html:text
																		property="dacOrgCode" size="15" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:organizationIsoCode">ISO Code</digi:trn></td>
																	<td width="500" height="30" colspan="2"><html:text
																		name="aimAddOrgForm" property="orgIsoCode" size="15" />
																	</td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:organizationCode">Organization Code</digi:trn><font
																		size="2" color="#FF0000">*</font></td>
																	<td width="500" height="30" colspan="2"><html:text
																		property="orgCode" size="15" /></td>
																</tr>
																
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:budgetOrganizationCode">Budget Organization Code</digi:trn><font
																		size="2" color="#FF0000">*</font></td>
																	<td width="500" height="30" colspan="2"><html:text
																		property="budgetOrgCode" size="15" /></td>
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
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:fiscalCalendar">Fiscal Calendar</digi:trn></td>
																	<td width="500" height="30" colspan="2"><html:select
																		property="fiscalCalId">
																		<c:set var="translation">
																			<digi:trn
																				key="aim:editOrganisationSelectFiscalCalendar">Fiscal Calendar</digi:trn>
																		</c:set>
																		<html:option value="-1">-- ${translation} --</html:option>
																		<logic:notEmpty name="aimAddOrgForm"
																			property="fiscalCal">
																			<html:optionsCollection name="aimAddOrgForm"
																				property="fiscalCal" value="ampFiscalCalId"
																				label="name" />
																		</logic:notEmpty>
																	</html:select>&nbsp;&nbsp; <%--<digi:link href="/addFiscalCalendar.do">
																				<digi:trn key="aim:addFiscalCalendar">Add Fiscal Calendar</digi:trn>
																			</digi:link>--%></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:sectorScheme">Sector Scheme</digi:trn></td>
																	<td width="500" height="30" colspan="2"><html:select
																		property="ampSecSchemeId" onclick="oldSchemeValue=this.value" onchange="checkScheme()">
																		<c:set var="translation">
																			<digi:trn
																				key="aim:editOrganisationSelectSectorScheme">Sector Scheme</digi:trn>
																		</c:set>
																		<html:option value="-1">-- ${translation} --</html:option>
																		<logic:notEmpty name="aimAddOrgForm"
																			property="sectorScheme">
																			<html:optionsCollection name="aimAddOrgForm"
																				property="sectorScheme" value="ampSecSchemeId"
																				label="secSchemeName" />
																		</logic:notEmpty>
																	</html:select></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:sectorPreferences">Sector Preferences</digi:trn>
																	</td>
																	<td width="500" height="30" colspan="2">
																	<table cellPadding=5 cellSpacing=1 border=0
																		width="100%" bgcolor="#d7eafd"
																		class=box-border-nopadding>
																		<tr>
																			<td bgcolor="#ffffff" width="100%">
																			<table cellPadding=1 cellSpacing=1 border=0
																				bgcolor="#ffffff" width="100%">
																				<c:if test="${empty aimAddOrgForm.sectors}">
																					<tr>
																						<td bgcolor="#ffffff"><input type="button"
																							class="dr-menu" onclick="javascript:addSectors(0,1);"
																							value='<digi:trn key="btn:addSectors">Add Sectors</digi:trn>' />
																						</td>
																					</tr>
																				</c:if>
																				<c:if test="${!empty aimAddOrgForm.sectors}">
																					<tr>
																						<td>
																						<table cellSpacing=0 cellPadding=0 border=0
																							bgcolor="#ffffff" width="100%">
																							<%
																								int i = 0;
																							%>
																							<c:if test="${aimAddOrgForm.sectors != null}">
																								<c:forEach var="sectorash"
																									items="${aimAddOrgForm.sectors}">
																									<%
																										i++;
																									%>
																									<tr name="sectorsRow">
																										<td>
																										<table width="100%" cellSpacing=1
																											cellPadding=1 vAlign="top" align="left">
																											<tr>
																												<td width="3%" vAlign="center"><html:multibox
																													property="selSectors">
																													<c:out value="${sectorash.sectorId}" />
																												</html:multibox></td>
																												<td width="87%" vAlign="center" align="left">
																												<c:if test="${!empty sectorash.sectorName}">
									                                                                        [${sectorash.sectorName}]
									                                                                      </c:if>
																												<c:if
																													test="${!empty sectorash.subsectorLevel1Name}">
									                                                                        [${sectorash.subsectorLevel1Name}]
									                                                                      </c:if>
																												<c:if
																													test="${!empty sectorash.subsectorLevel2Name}">
									                                                                        [${sectorash.subsectorLevel2Name}]
									                                                                      </c:if>
																												</td>
																											</tr>
																										</table>
																										</td>
																									</tr>
																								</c:forEach>
																							</c:if>
																							<tr>
																								<td>
																								<table cellSpacing=2 cellPadding=2>
																									<tr>
																										<c:if test="<%=i<5 %>">
																											<field:display name="Add Sectors Button"
																												feature="Sectors">
																												<td><input type="button" class="dr-menu"
																													onclick="javascript:addSectors(0,1);"
																													value='<digi:trn key="btn:addSectors">Add Sectors</digi:trn>' />
																												</td>
																											</field:display>
																										</c:if>
																										<td><input type="button" class="dr-menu"
																											onclick="return removeSelSectors()"
																											value='<digi:trn key="btn:removeSector">Remove Sector</digi:trn>' />
																										</td>
																									</tr>
																								</table>
																								</td>
																							</tr>
																						</table>
																						</td>
																					</tr>
																				</c:if>
																			</table>
																			</td>
																		</tr>
																	</table>
																	</td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:pledges">Pledges</digi:trn></td>
																	<td width="500" height="30" colspan="2">&nbsp; <%
 	String tempIndexStr = "";
 %> <%
																		int tempIndex = 0;
																	%> <!-- ############################## -->
																	<table width="100%" border="0" bgcolor="#f4f4f2"
																		cellspacing="1" cellpadding="0"
																		class=box-border-nopadding>
																		<c:if test="${ aimAddOrgForm.fundingDetails != null}">
																			<c:set var="index" value="-1" />
																			<tr>
																				<td align="center" valign="bottom"><digi:trn
																					key="aim:org:program">Program</digi:trn></td>
																				<td align="center" valign="bottom"><digi:trn
																					key="aim:org:planned">Planned</digi:trn></td>
																				<td align="center" valign="bottom"><digi:trn
																					key="aim:org:amount">Amount</digi:trn></td>
																				<td align="center" valign="bottom"><digi:trn
																					key="aim:org:currency">Currency</digi:trn></td>
																				<td align="center" valign="bottom"><digi:trn
																					key="aim:org:date">Date</digi:trn></td>
																			</tr>
																			<c:forEach var="fundingDetail"
																				items="${aimAddOrgForm.fundingDetails}">
																				<tr>
																					<td valign="bottom"><html:text
																						name="fundingDetail" indexed="true"
																						property="program" styleClass="inp-text" size="10" />
																					</td>
																					<td valign="bottom"><c:set var="index"
																						value="${index+1}" /> <html:select
																						name="fundingDetail" indexed="true"
																						property="adjustmentType" styleClass="inp-text">
																						<html:option value="0">
																							<digi:trn key="aim:Planned">Planned</digi:trn>
																						</html:option>
																					</html:select></td>
																					<td valign="bottom"><html:text
																						name="fundingDetail" indexed="true"
																						property="amount" size="17" styleClass="amt" /></td>
																					<td valign="bottom"><html:select
																						name="fundingDetail" indexed="true"
																						property="currencyCode" styleClass="inp-text">
																						<html:optionsCollection name="aimAddOrgForm"
																							property="currencies" value="currencyCode"
																							label="currencyName" />
																					</html:select></td>
																					<td vAlign="bottom">
																					<table cellPadding=0 cellSpacing=0>
																						<tr>
																							<td valign="bottom">
																							<%
																								tempIndexStr = "" + tempIndex;
																											tempIndex++;
																							%> <html:text name="fundingDetail" indexed="true"
																								property="date" styleId="<%=tempIndexStr%>"
																								styleClass="inp-text" readonly="true" size="10" />
																							</td>
																							<td align="left" vAlign="center">&nbsp; <a
																								id="transDate<%=tempIndexStr%>"
																								href='javascript:pickDateById("transDate<%=tempIndexStr%>",<%=tempIndexStr%>)'>
																							<img
																								src="../ampTemplate/images/show-calendar.gif"
																								alt="Click to View Calendar" border=0> </a></td>
																						</tr>
																					</table>
																					</td>
																					<td valign="bottom"><a
																						href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>)">
																					<digi:img src="../ampTemplate/images/deleteIcon.gif"
																						border="0" alt="Delete this transaction" /> </a></td>
																				</tr>
																			</c:forEach>
																		</c:if>
																		<tr>
																			<td>&nbsp;</td>
																		</tr>
																		<tr valign="baseline">
																			<td colspan="1" width="10px"><input
																				type="button" class="dr-menu" onclick="addPledge();"
																				value='<digi:trn key="btn:addPledge">Add Pledge</digi:trn>' />
																			</td>
																			<td colspan="5" align="right">
																				<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
																				<FONT color=blue>*
																				<digi:trn key="aim:allTheAmountsInThousands">
																							All the amounts are in thousands (000)
				  																		</digi:trn> </FONT>
																				</gs:test>
				  															</td>
																		</tr>
																	</table>
																	<!-- ############################## --></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:orgContactName">Contact Person Name</digi:trn>
																	</td>
																	<td width="500" height="30" colspan="2"><html:text
																		property="contactPersonName" size="35" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:orgContactTitle">Contact Person Title</digi:trn>
																	</td>
																	<td width="500" height="30" colspan="2"><html:text
																		property="contactPersonTitle" size="20" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:orgContactPhone">Contact Phone</digi:trn></td>
																	<td width="500" height="30" colspan="2"><html:text
																		property="phone" size="35" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:orgContactFax">Contact Fax</digi:trn></td>
																	<td width="500" height="30" colspan="2"><html:text
																		property="fax" size="35" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:orgContactEmail">Contact Email</digi:trn></td>
																	<td width="500" height="30" colspan="2"><html:text
																		property="email" size="35" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:organizationUrl">Organization URL</digi:trn></td>
																	<td width="500" height="30" colspan="2"><html:text
																		property="orgUrl" size="54" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:orgAddress">Address</digi:trn></td>
																	<td width="500" height="30" colspan="2"><html:textarea
																		property="address" cols="40" rows="3" /></td>
																</tr>
																<tr>
																	<td width="169" align="right" height="30"><digi:trn
																		key="aim:organizationDescription">Description</digi:trn>
																	</td>
																	<td width="500" height="30" colspan="2"><html:textarea
																		property="description" cols="40" rows="3" /></td>
																</tr>
																<tr>
																	<td colspan="3" align="center" style="padding: 3px">
																	<bean:define toScope="request" id="windowName">
																		<digi:trn
																			key="cr:selectedDocs:organizationDocuments:windowName">Documents</digi:trn>
																	</bean:define> <bean:define toScope="request" id="showRemoveButton"
																		value="true" /> <bean:define toScope="request"
																		id="documentsType"
																		value="<%=org.digijava.module.aim.dbentity.AmpOrganisationDocument.SESSION_NAME %>" />
																	<bean:define toScope="request" id="versioningRights"
																		value="false" /> <bean:define toScope="request"
																		id="makePublicRights" value="false" /> <bean:define
																		toScope="request" id="showVersionsRights"
																		value="false" /> <bean:define toScope="request"
																		id="deleteRights" value="false" /> <bean:define
																		toScope="request" id="crRights" value="true" /> <jsp:include
																		page="/repository/contentrepository/view/showSelectedDocumentsDM.jsp" />
																	</td>
																</tr>
																<tr>
																	<td colspan="3" align="center"><c:set
																		var="showTheFollowingDocuments" value="PUBLIC" /> <c:set
																		var="documentsType"><%=org.digijava.module.aim.dbentity.AmpOrganisationDocument.SESSION_NAME%></c:set>
																	<html:button styleClass="dr-menu" property="submitButton"
																		onclick="addDocumentsDM('${documentsType}','${showTheFollowingDocuments}')">
																		<digi:trn key="btn:addDocumentsFromRepository">Add Documents From Repository</digi:trn>
																	</html:button> <br />
																	<br />
																	</td>
																</tr>
																<tr>
																	<td colspan="3" width="555" align="center" height="30">
																	<table width="100%" cellspacing="5">
																		<tr>
																			<td width="42%" align="right"><html:button
																				styleClass="dr-menu" property="submitButton"
																				onclick="return check()">
																				<digi:trn key="btn:save">Save</digi:trn>
																			</html:button></td>
																			<td width="8%" align="left"><input type="reset"
																				value='<digi:trn key="btn:reset">Reset</digi:trn>'
																				class="dr-menu"></td>
																			<td width="45%" align="left"><input
																				type="button"
																				value="<digi:trn key="btn:cancel">Cancel</digi:trn>"
																				class="dr-menu" onclick="move()"></td>
																		</tr>
																	</table>
																	</td>
																</tr>
																<logic:equal name="aimAddOrgForm" property="flag"
																	value="delete">
																	<tr>
																		<td colspan="3" width="555" align="center" height="27">
																		<html:button styleClass="dr-menu"
																			property="submitButton" onclick="return msg()">
																			<digi:trn key="btn:deleteThisOrganization">Delete this Organization</digi:trn>
																		</html:button></td>
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
											<td bgColor=#f4f4f2>&nbsp;</td>
										</tr>
									</table>
									</td>
									<td noWrap width=10 vAlign="top"></td>
								</tr>
							</table>
							</td>
						</tr>
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
