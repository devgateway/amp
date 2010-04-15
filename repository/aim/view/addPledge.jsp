<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<!--<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>-->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<jsp:include page="addSectors.jsp" flush="true" />
<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<script language="JavaScript" type="text/javascript"><!--

var quitRnot1 = 0;

function fnChk(frmContrl, f){
	  <c:set var="errMsgAddSectorNumericValue">
	  <digi:trn key="aim:addSecorNumericValueErrorMessage">
	  Please enter numeric value only
	  </digi:trn>
	  </c:set>
	  
	  if (isNaN(frmContrl.value)) {
	    alert("${errMsgAddSectorNumericValue}");
	    frmContrl.value = "";
	    //frmContrl.focus();
	    return false;
	  }  
	  if (frmContrl.value > 100) {    
	      if (f == "sector") {
		     <c:set var="errMsgAddSumExceed">
			  <digi:trn key="aim:addSecorSumExceedErrorMessage">
			  Sector percentage can not exceed 100
			  </digi:trn>
			  </c:set>
			  alert("${errMsgAddSumExceed}");
		  } else if (f == "program") {
		     <c:set var="errMsgAddSumExceed">
			  <digi:trn key="aim:addProgramSumExceedErrorMessage">
			  Program percentage can not exceed 100
			  </digi:trn>
			  </c:set>  
			  alert("${errMsgAddSumExceed}");
		  } else if (f == "region") {
		      <c:set var="errMsgAddSumExceed">
			  <digi:trn key="aim:addRegionSumExceedErrorMessage">
			  Region percentage can not exceed 100
			  </digi:trn>
			  </c:set> 
			  alert("${errMsgAddSumExceed}");
		  }
	    frmContrl.value = "";
	    return false;
	  }
	  return true;
	}

function addLocation() {
	  openNewWindow(600, 300);
	  <digi:context name="selectLoc" property="context/module/moduleinstance/selectPledgeLocation.do?edit=false" />
	  document.pledgeForm.action = "<%= selectLoc %>";
	  document.pledgeForm.target = popupPointer.name;
	  document.pledgeForm.submit();
	}

function removeLocation() {
	<c:set var="confirmDelete">
	  <digi:trn key="aim:removeSelectedLocationsMessage">
	 	 Remove selected locations?
	  </digi:trn>
	</c:set>
	if (confirm("${confirmDelete}")){
		var i = 1
		var delStr = "deleteLocs="
		while (document.getElementById("checkLoc"+i)!=null){
			if(document.getElementById("checkLoc"+i).checked==true){
				delStr = delStr + "_" + i;
			}
			i++;
		}
		if (delStr.length < 13){
			alert ("Please, select a location first.");
		} else {
			document.pledgeForm.target = "_self";
			document.pledgeForm.action="/removePledgeLocation.do?"+delStr;
			document.pledgeForm.submit();
		}
	}			
}

function addSectors(editAct,configId) {
/*  openNewWindow(600, 450);
  document.aimEditActivityForm.action = "/selectSectors.do?edit=true&configId="+configId;
  document.aimEditActivityForm.target = popupPointer.name;
  document.aimEditActivityForm.submit();
*/ 	
	initSectorScript();
	 myAddSectors("edit=true&configId=1");	  
}

function addSector(param)
{
    
    <digi:context name="addSec" property="context/addPledge.do?addSector=true&edit=param" />
    document.pledgeForm.action = "<%= addSec %>";
    document.pledgeForm.target = "_self";
    document.pledgeForm.submit();
}

function removeSector() {
	<c:set var="confirmDelete">
	  <digi:trn key="aim:removeSelectedSectorsMessage">
	 	 Remove selected sectors?
	  </digi:trn>
	</c:set>
	if (confirm("${confirmDelete}")){
		var i = 1
		var delStr = "deleteSect="
		while (document.getElementById("checkSect"+i)!=null){
			if(document.getElementById("checkSect"+i).checked==true){
				delStr = delStr + "_" + i;
			}
			i++;
		}
		if (delStr.length < 13){
			alert ("Please, select a sector first.");
		} else {
			document.pledgeForm.target = "_self";
			document.pledgeForm.action="/removePledgeSector.do?"+delStr;
			document.pledgeForm.submit();
		}	
	}	
}

function addFunding() {
	document.getElementsByName("fundingEvent")[0].value = "addFunding";
	
	document.pledgeForm.target = "_self";
	document.pledgeForm.action="/addFundingPledgeDetail.do";
	document.pledgeForm.submit();
	
}


function removeFunding() {
	<c:set var="confirmDelete">
	  <digi:trn key="aim:removeSelectedFundingMessage">
	 	 Remove selected fundings?
	  </digi:trn>
	</c:set>
	if (confirm("${confirmDelete}")){
		document.getElementsByName("fundingEvent")[0].value = "delFunding";
		var i = 1
		var delStr = "deleteFunds="
		while (document.getElementById("checkFund"+i)!=null){
			if(document.getElementById("checkFund"+i).checked==true){
				delStr = delStr + "_" + i;
			}
			i++;
		}
		if (delStr.length < 13){
			alert ("Please, select a funding first.");
		} else {
			document.pledgeForm.target = "_self";
			document.pledgeForm.action="/addFundingPledgeDetail.do?"+delStr;
			document.pledgeForm.submit();
		}	
	}	
}

function savePledge() {

	if (validateData()){
		<digi:context name="save" property="/savePledge.do" />
  	 	document.pledgeForm.action = "<%= save %>?edit=true";
  	  	document.pledgeForm.target = "_self";

    	document.pledgeForm.submit();
	}
}

function validateData(){
	<c:set var="pleaseInsertTitle">
	  <digi:trn key="aim:pleaseInsertTitle">
	 	 Please, insert a pledge title.
	  </digi:trn>
	</c:set>
	if (document.getElementsByName("pledgeTitle")[0]==null || document.getElementsByName("pledgeTitle")[0].value.length==0){
		alert ("${pleaseInsertTitle}")
		return false;
	}
	<c:set var="pleaseSelectOrg">
	  <digi:trn key="aim:pleaseSelectOrg">
	 	 Please, select organisation using organisation selector.
	  </digi:trn>
	</c:set>
	if (document.getElementById("contrDonorId")==null || document.getElementById("contrDonorId").value.length==0){
		alert ("${pleaseSelectOrg}")
		return false;
	}
	<c:set var="insertPercentage">
	  <digi:trn key="aim:insertPercentage">
	 	 Percentages can not be empty or 0.
	  </digi:trn>
	</c:set>
	<c:set var="percentageSectorTotal">
	  <digi:trn key="aim:sumOfSectorPercentagesMustBe100">
	 	 Sum of sector percentages must be 100.
	  </digi:trn>
	</c:set>
	var i = 0;
	var percent = 100;
	while (document.getElementsByName("pledgeSectors["+i+"].sectorPercentage")[0]!=null){
		var temp = 0;
		temp = temp + document.getElementsByName("pledgeSectors["+i+"].sectorPercentage")[0].value;
		if (document.getElementsByName("pledgeSectors["+i+"].sectorPercentage")[0].value.length==0 || temp==0){
			alert ("${insertPercentage}")
			return false;
		}
		i++;
		percent = percent - temp;
	}
	if(percent!=0 && percent!=100){
		alert ("${percentageSectorTotal}")
		return false;
	}	

	<c:set var="percentageLocationTotal">
	  <digi:trn key="aim:sumOfLocationPercentagesMustBe100">
	 	 Sum of location percentages must be 100.
	  </digi:trn>
	</c:set>
	i=0;
	percent = 100;
	while (document.getElementsByName("selectedLocs["+i+"].locationpercentage")[0]!=null){
		var temp = 0;
		temp = temp + document.getElementsByName("selectedLocs["+i+"].locationpercentage")[0].value;
		if (document.getElementsByName("selectedLocs["+i+"].locationpercentage")[0].value.length==0 || temp==0){
			alert ("${insertPercentage}")
			return false;
		}
		i++;
		percent = percent - temp;
	}
	if(percent!=0 && percent!=100){
		alert ("${percentageLocationTotal}")
		return false;
	}	

	<c:set var="addFunding">
	  <digi:trn key="aim:addFunding">
	 	 Pledges should have at least one funding.
	  </digi:trn>
	</c:set>
	i = 0;
	if (document.getElementsByName("fundingPledgesDetails[0].pledgetypeid")[0] == null){
		alert ("${addFunding}")
		return false;
	}
	
	<c:set var="insertAmount">
	  <digi:trn key="aim:insertAmount">
	  	Please, insert amount greater than 0 for each funding.
	  </digi:trn>
	</c:set>
	i = 0;
	while (document.getElementsByName("fundingPledgesDetails["+i+"].amount")[0]!=null){
		var temp = 0;
		temp = temp + document.getElementsByName("fundingPledgesDetails["+i+"].amount")[0].value;
		if (document.getElementsByName("fundingPledgesDetails["+i+"].amount")[0].value.length==0 || temp==0){
			alert ("${insertAmount}")
			return false;
		}
		i++;
	}
	
	<c:set var="selectFunding_date">
	  <digi:trn key="aim:selectFunding_date">
	 	 Please, select a date for each funding.
	  </digi:trn>
	</c:set>
	i = 0;
	while (document.getElementsByName("fundingPledgesDetails["+i+"].fundingDate")[0]!=null){
		if (document.getElementsByName("fundingPledgesDetails["+i+"].fundingDate")[0].value.length ==0){
			alert ("${selectFunding_date}")
			return false;
		}
		i++;
	}

	<c:set var="insertValidEmail">
	  <digi:trn key="aim:insertValidEmail">
	 	 Please, insert a valid Email.
	  </digi:trn>
	</c:set>
	if (document.getElementsByName("contact1Email")[0].value.length>0 && document.getElementsByName("contact1Email")[0].value.indexOf("@") == -1){
		alert ("${insertValidEmail}")
		return false;
	}
	if (document.getElementsByName("contactAlternate1Email")[0].value.length>0 && document.getElementsByName("contactAlternate1Email")[0].value.indexOf("@") == -1){
		alert ("${insertValidEmail}")
		return false;
	}
	if (document.getElementsByName("contact2Email")[0].value.length>0 && document.getElementsByName("contact2Email")[0].value.indexOf("@") == -1){
		alert ("${insertValidEmail}")
		return false;
	}
	if (document.getElementsByName("contactAlternate2Email")[0].value.length>0 && document.getElementsByName("contactAlternate2Email")[0].value.indexOf("@") == -1){
		alert ("${insertValidEmail}")
		return false;
	}
	
	return true;
}

function setSameContact(){
	if(document.getElementById("sameContact").checked==true){
		document.getElementsByName("contact2Name")[0].value = document.getElementsByName("contact1Name")[0].value;
		//document.getElementsByName("contact2Name")[0].disabled = true;
		document.getElementsByName("contact2Title")[0].value = document.getElementsByName("contact1Title")[0].value;
		//document.getElementsByName("contact2Title")[0].disabled = true;
		document.getElementsByName("contact2OrgName")[0].value = document.getElementsByName("contact1OrgName")[0].value;
		//document.getElementsByName("contact2OrgName")[0].disabled = true;
		document.getElementsByName("contact2OrgId")[0].value = document.getElementsByName("contact1OrgId")[0].value;
		//document.getElementsByName("contact2OrgId")[0].disabled = true;
		document.getElementsByName("contact2Ministry")[0].value = document.getElementsByName("contact1Ministry")[0].value;
		//document.getElementsByName("contact2Ministry")[0].disabled = true;
		document.getElementsByName("contact2Address")[0].value = document.getElementsByName("contact1Address")[0].value;
		//document.getElementsByName("contact2Address")[0].disabled = true;
		document.getElementsByName("contact2Telephone")[0].value = document.getElementsByName("contact1Telephone")[0].value;
		//document.getElementsByName("contact2Telephone")[0].disabled = true;
		document.getElementsByName("contact2Email")[0].value = document.getElementsByName("contact1Email")[0].value;
		//document.getElementsByName("contact2Email")[0].disabled = true;
		document.getElementsByName("contactAlternate2Email")[0].value = document.getElementsByName("contactAlternate1Email")[0].value;
		//document.getElementsByName("contactAlternate2Email")[0].disabled = true;
		document.getElementsByName("contact2Fax")[0].value = document.getElementsByName("contact1Fax")[0].value;
		//document.getElementsByName("contact2Fax")[0].disabled = true;
		document.getElementsByName("contactAlternate2Name")[0].value = document.getElementsByName("contactAlternate1Name")[0].value;
		//document.getElementsByName("contactAlternate2Name")[0].disabled = true;
		document.getElementsByName("contactAlternate2Telephone")[0].value = document.getElementsByName("contactAlternate1Telephone")[0].value;
		//document.getElementsByName("contactAlternate2Telephone")[0].disabled = true;
		
	} else {
		document.getElementsByName("contact2Name")[0].value = "";
		document.getElementsByName("contact2Name")[0].disabled = false;
		document.getElementsByName("contact2Title")[0].value = "";
		document.getElementsByName("contact2Title")[0].disabled = false;
		document.getElementsByName("contact2OrgName")[0].value = "";
		document.getElementsByName("contact2OrgName")[0].disabled = false;
		document.getElementsByName("contact2OrgId")[0].value = "";
		document.getElementsByName("contact2OrgId")[0].disabled = false;
		document.getElementsByName("contact2Ministry")[0].value = "";
		document.getElementsByName("contact2Ministry")[0].disabled = false;
		document.getElementsByName("contact2Address")[0].value = "";
		document.getElementsByName("contact2Address")[0].disabled = false;
		document.getElementsByName("contact2Telephone")[0].value = "";
		document.getElementsByName("contact2Telephone")[0].disabled = false;
		document.getElementsByName("contact2Email")[0].value = "";
		document.getElementsByName("contact2Email")[0].disabled = false;
		document.getElementsByName("contactAlternate2Email")[0].value = "";
		document.getElementsByName("contactAlternate2Email")[0].disabled = false;
		document.getElementsByName("contact2Fax")[0].value = "";
		document.getElementsByName("contact2Fax")[0].disabled = false;
		document.getElementsByName("contactAlternate2Name")[0].value = "";
		document.getElementsByName("contactAlternate2Name")[0].disabled = false;
		document.getElementsByName("contactAlternate2Telephone")[0].value = "";
		document.getElementsByName("contactAlternate2Telephone")[0].disabled = false;
		
	}
		
}
--></script>

<digi:instance property="pledgeForm" />

<digi:form action="/addPledge.do" method="post">

<body>
<html:hidden name="pledgeForm" styleId="event" property="fundingEvent"/>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="1024" vAlign="top" align="center" border=0>
	
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" >
										<digi:trn key="aim:desktop">Desktop</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:link href="/viewPledgesList.do" styleClass="comment" >
										<digi:trn key="aim:pledges">Pledges</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:addPledge">Add Pledge</digi:trn>
								
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height="50" vAlign="middle" width="100%"><span class=subtitle-blue>
								<digi:trn key="aim:addNewPledge">Add New Pledge</digi:trn>
								
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border=0>
						<tr><td width="75%" vAlign="top">
						<table cellPadding=0 cellSpacing=0 width="100%" border=0>
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												<digi:trn key="pledgeInformation">Pledge Information</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgcolor="#f4f4f2" width="100%">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG  height="10" src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:pledgeIdentification">Pledge Identification</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="30%">
													<FONT color=red>*</FONT>
													<a>
													<digi:trn key="pledgeTitle">Pledge Title</digi:trn>
													</a>
												
												</td>
												<td valign="middle" align="left" width="70%">
													<a>
														<html:text property="pledgeTitle" size="60" styleClass="inp-text"/>
                            						</a>
												</td>											
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG  height="10" src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:donorInformation">Donor Information</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="30%">
													<FONT color=red>*</FONT>
													<a>
													<digi:trn key="donorCountryInstitution">Donor (Country/Institution)</digi:trn>
													</a>
												
												</td>
												<td valign="middle" align="left" width="70%">
													<a>
<!--														<html:hidden property="selectedOrgId" />-->
<!--														<html:text property="selectedOrgName" readonly="true"/>-->
<!--														<aim:addOrganizationButton refreshParentDocument="true"  form="${pledgeForm}" htmlvalueHolder="selectedOrgId" htmlNameHolder="selectedOrgName" useClient="true" styleClass="dr-menu">...</aim:addOrganizationButton>-->
															<c:set var="valueId"> contrDonorId </c:set>
							                              <c:set var="nameId"> nameContrDonorId </c:set>
							                              <input   name='selectedOrgId' type="hidden" id="${valueId}" style="text-align:right" value='${pledgeForm.selectedOrgId}' size="4"/>
							                              <input name="selectedOrgName" type='text' id="${nameId}" style="text-align:right" value='${pledgeForm.selectedOrgName}' size="53" style="background-color:#CCCCCC" onKeyDown="return false" class="inp-text"/>
							                              <aim:addOrganizationButton useClient="true" htmlvalueHolder="${valueId}" htmlNameHolder="${nameId}" >...</aim:addOrganizationButton>
                            						</a>
													
												</td>											
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="30%">
													<a>
													<digi:trn key="whoHasAuthorizedPledge">Who Has Authorized Pledge?</digi:trn>
													</a>
												
												</td>
												<td valign="middle" align="left" width="70%">
													<a>
														<html:text property="whoAuthorizedPledge" size="60" styleClass="inp-text"/>
                            						</a>
												</td>											
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="30%">
													<a>
													<digi:trn key="pleaseIndicateFurtherApprovalNeeded">Please Indicate any Further Approval Needed</digi:trn> 
													</a>
												
												</td>
												<td valign="middle" align="left" width="70%">
													<a>
														<html:text property="furtherApprovalNedded" size="60" styleClass="inp-text"/>
                            						</a>
												</td>											
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<feature:display name="Pledge Contact 1" module="Pledges">
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG height="10" src="../ampTemplate/images/arrow-014E86.gif" width="15">
										<b><digi:trn key="aim:pointContactDonorsConferenceMarch31st">Point of Contact at Donors Conference on March 31st</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactName">Name</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact1Name" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactTitle">Title</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact1Title" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactOrganization">Organization</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<c:set var="valueId1"> contact1OrgId </c:set>
							                              <c:set var="nameId1"> contact1OrgName </c:set>
							                              <input name="contact1OrgId" type="hidden" id="${valueId1}" style="text-align:right" value='${pledgeForm.contact1OrgId}' size="4"/>
							                              <input name="contact1OrgName" type="text" id="${nameId1}" style="text-align:right" value='${pledgeForm.contact1OrgName}' size="33" style="background-color:#CCCCCC" onKeyDown="return false" class="inp-text" onchange="setSameContact()"/>
							                              <aim:addOrganizationButton useClient="true" htmlvalueHolder="${valueId1}" htmlNameHolder="${nameId1}" >...</aim:addOrganizationButton>
                            						
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactMinistry">Ministry</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact1Ministry" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactAddress">Address</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact1Address" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactTelephone">Telephone</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact1Telephone" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactEmail">Email</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact1Email" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactFax">Fax</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact1Fax" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>										
											</tr>
										</table>
										<tr><td>&nbsp;</td></tr>
									<tr><td><b><digi:trn key="alternateContactPerson">Alternate Contact Person</digi:trn></b></td></tr>
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactName">Name</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contactAlternate1Name" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactTelephone">Telephone</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contactAlternate1Telephone" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactEmail">Email</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contactAlternate1Email" size="40" styleClass="inp-text" onchange="setSameContact()"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<a>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
													</a>
												</td>										
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								</feature:display>
								<feature:display name="Pledge Contact 2" module="Pledges">
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG height="10" src="../ampTemplate/images/arrow-014E86.gif" width="15">
										<b><digi:trn key="aim:pointContactFollowUp">Point of Contact for Follow Up</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr>
												<td valign="middle" align="left" width="15%" colspan="2">
													<a>
														<digi:trn key="sameAsOriginalPointOfContact">Same As Original Point Of Contact</digi:trn>
													</a>
													<input type="checkbox" id="sameContact" onclick="setSameContact()">
												</td>
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactName">Name</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact2Name" size="40" styleClass="inp-text"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactTitle">Title</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact2Title" size="40" styleClass="inp-text"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactOrganization">Organization</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<c:set var="valueId2"> contact2OrgId </c:set>
							                              <c:set var="nameId2"> contact2OrgName </c:set>
							                              <input name="contact2OrgId" type="hidden" id="${valueId2}" style="text-align:right" value='${pledgeForm.contact2OrgId}' size="4"/>
							                              <input name="contact2OrgName" type='text' id="${nameId2}" style="text-align:right" value='${pledgeForm.contact2OrgName}' size="33" style="background-color:#CCCCCC" onKeyDown="return false" class="inp-text"/>
							                              <aim:addOrganizationButton useClient="true" htmlvalueHolder="${valueId2}" htmlNameHolder="${nameId2}" >...</aim:addOrganizationButton>
                            						
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactMinistry">Ministry</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact2Ministry" size="40" styleClass="inp-text"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactAddress">Address</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact2Address" size="40" styleClass="inp-text"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactTelephone">Telephone</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact2Telephone" size="40" styleClass="inp-text"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<FONT color=red>*</FONT>
													<a>
														<digi:trn key="pointContactEmail">Email</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact2Email" size="40" styleClass="inp-text"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactFax">Fax</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contact2Fax" size="40" styleClass="inp-text"/>
                            						</a>
												</td>										
											</tr>
										</table>
										<tr><td>&nbsp;</td></tr>
									<tr><td><b><digi:trn key="alternateContactPerson">Alternate Contact Person</digi:trn></b></td></tr>
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactName">Name</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contactAlternate2Name" size="40" styleClass="inp-text"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactTelephone">Telephone</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contactAlternate2Telephone" size="40" styleClass="inp-text"/>
                            						</a>
												</td>										
											</tr>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="15%">
													<a>
														<digi:trn key="pointContactEmail">Email</digi:trn>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
														<html:text property="contactAlternate2Email" size="40" styleClass="inp-text"/>
                            						</a>
												</td>	
												<td valign="middle" align="left" width="15%">
													<a>
													</a>
												</td>
												<td valign="middle" align="left" width="35%">
													<a>
													</a>
												</td>										
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								</feature:display>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr>
									    <td>
									        <!-- contents -->
									        <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
									        <b><digi:trn key="aim:sectorAndLocation">Sector and Location</digi:trn></b>
									         
									    </td>
							        </tr>
						            <feature:display name="Pledge Sector" module="Pledges">
									<tr><td>&nbsp;</td></tr>
									<tr>
						                <td>
						                    <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
						                    	<tr>
						                            <td align="left">
						                                <b>
						                                    <digi:trn key="aim:sector">
						                                        Sector
						                                    </digi:trn>
						                                </b>
						                            </td>
						                        </tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
									       <table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
                                             	<tr><td>
													<c:forEach var="pledgeSectors" items="${pledgeForm.pledgeSectors}" varStatus="index">
                                                            <tr> 
                                                                   <c:set var="indexSect" value="${indexSect+1}"/>
										                            <td align="center" width="3%">
																		<input type="checkbox" id="checkSect${indexSect}"  >
																	</td>
                                                                    <td  width="67%" valign="middle" align="left">
                                                                        
                                                                        [${pledgeSectors.sectorScheme}]
                                                                        <c:if test="${!empty pledgeSectors.sectorName}">
                                                                            [${pledgeSectors.sectorName}]
                                                                        </c:if>
									                               		<c:if test="${!empty pledgeSectors.subsectorLevel1Name}">
                                                                            [${pledgeSectors.subsectorLevel1Name}]
                                                                        </c:if>
																		<c:if test="${!empty pledgeSectors.subsectorLevel2Name}">
                                                                            [${pledgeSectors.subsectorLevel2Name}]
                                                                        </c:if>
																		
                                                                    </td>
                                                                    <td width="15%" valign="middle" align="right">
                                                                       
                                                                    <FONT color="red">*</FONT><digi:trn key="aim:percentage">Percentage</digi:trn>:&nbsp;</td>
                                                                    <td width="15%" valign="middle" align="left">
                                                                        <html:text name="pledgeSectors" indexed="true" property="sectorPercentage"size="5" onkeyup="fnChk(this, 'sector')" styleClass="inp-text"/>
                                                                    </td>
                                                                </tr>
                                                                <c:set var="sectorAdded" value="true"/>
                                                           </c:forEach>
												</td></tr>
												<tr>
													<td colspan="2"> &nbsp;
                                                    
                                                           <html:button styleClass="dr-menu"  
                                                                         property="submitButton" onclick="addSectors();" >
                                                                <digi:trn key="btn:addSectors">Add Sectors</digi:trn>
                                                            </html:button>
															 &nbsp;
	                                                 		<logic:notEmpty name="pledgeForm" property="pledgeSectors">
																<html:button styleClass="dr-menu" property="submitButton" onclick="return removeSector()">
	                                                          	  <digi:trn key="btn:removeSector">Remove Sector</digi:trn>
	                                                        	</html:button>
															</logic:notEmpty>
	                                                </td>
	                                            </tr>
	                                        </table>
									     
									    </td>
									</tr>
									</feature:display>
									<feature:display name="Pledge Location" module="Pledges">
									<tr><td>&nbsp;</td></tr>
									<tr>
						                <td>
						                    <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
						                    	<tr>
						                            <td align="left">
						                                <b>
						                                    <digi:trn key="aim:Location">
						                                        Location
						                                    </digi:trn>
						                                </b>
						                            </td>
						                        </tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
									       <table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
                                             	<tr><td>
												<c:forEach var="selectedLocs" items="${pledgeForm.selectedLocs}" varStatus="index">
                                                  <tr>
                                                      <c:set var="indexLoc" value="${indexLoc+1}"/>
								                            <td align="center" width="3%">
																<input type="checkbox" id="checkLoc${indexLoc}"  >
															</td>
                                                            <td align="left" width="67%">
	                                                            [${selectedLocs.location.name}] 
                                                            </td>
                                                            <td align="right" width="15%" nowrap="nowrap">
                                                            <FONT color="red">*</FONT>
                                                            		<digi:trn key="aim:percentage">Percentage</digi:trn>:&nbsp;
															</td>
															<td align="left" width="15%" nowrap="nowrap">
                                                            		<html:text name="selectedLocs" indexed="true" property="locationpercentage" size="5"  onkeyup="fnChk(this, 'region')" styleClass="inp-text"/>
                                                            </td>
                                                          
                                                    </tr>
                                                  </c:forEach>
												</td></tr>
												<tr>
													<td colspan="2"> &nbsp;
                                                           <html:button styleClass="dr-menu"  
                                                                         property="submitButton" onclick="addLocation();">
                                                                <digi:trn key="btn:addLocation">Add Location</digi:trn>
                                                            </html:button>
															 &nbsp;
	                                                 		<logic:notEmpty name="pledgeForm" property="selectedLocs">
																<html:button styleClass="dr-menu" property="submitButton" onclick="return removeLocation()">
	                                                            <digi:trn key="btn:removeLocation">Remove Location</digi:trn>
	                                                        	</html:button>
															</logic:notEmpty>
	                                                </td>
	                                            </tr>
	                                        </table>
									     
									    </td>
									</tr>
									</feature:display>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr>
									    <td>
									        <!-- contents -->
									        <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
									        <b><digi:trn key="aim:pledgeInformation">Pledge Information</digi:trn></b>
									         
									    </td>
							        </tr>
						            <tr><td>&nbsp;</td></tr>
									<logic:notEmpty name="pledgeForm" property="fundingPledgesDetails">
									<tr>
						                <td>
						                    <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
						                    	<tr>
													<td align="center" valign="bottom" width="3%" />
						                            <td align="center" width="17%">
						                                <b><digi:trn key="aim:typeOfPledge">Type Of Pledge</digi:trn></b>
						                            </td>
													<td align="center" width="20%">
						                                <b><digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn></b>
						                            </td>
													<td align="center" width="15%">
						                                <b><digi:trn key="aim:amount">Amount</digi:trn></b>
						                            </td>
													<td align="center" width="10%">
						                                <b><digi:trn key="aim:typeOfCurrency">Currency</digi:trn></b>
						                            </td>
													<td align="center" width="15%">
						                                <b><digi:trn key="aim:date">Date</digi:trn></b>
						                            </td>
													<td align="center" width="20%">
						                                <b><digi:trn key="aim:aidModality">Aid Modality</digi:trn></b>
						                            </td>
						                        </tr>
											</table>
										</td>
									</tr>
									</logic:notEmpty>
									<tr>
										<td>
											<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
                                             	<c:forEach var="fundingPledgesDetails" items="${pledgeForm.fundingPledgesDetails}" varStatus="status">
												<tr>
													<c:set var="indexFund" value="${indexFund+1}"/>
						                            <c:set var="translation">
														<digi:trn key="aim:selectFromBelow">Please select from below</digi:trn>
													</c:set>	
													<td align="center" valign="bottom" width="3%" >
														<input type="checkbox" id="checkFund${indexFund}"  >
													</td>
													<td align="center" valign="bottom" width="17%">
														<html:select name="fundingPledgesDetails" indexed="true" property="pledgetypeid" styleClass="inp-text">
															<html:optionsCollection name="pledgeForm" property="pledgeTypeCategory" value="id"
															label="value"/>
														</html:select>
						                            </td>
													<td align="center" valign="bottom" width="20%">
						                                <html:select name="fundingPledgesDetails" indexed="true" property="typeOfAssistanceid" styleClass="inp-text">
															<html:optionsCollection name="pledgeForm" property="assistanceTypeCategory" value="id"
															label="value"/>
														</html:select>
						                            </td>
													<td align="center" valign="bottom" width="15%">
						                                <html:text name="fundingPledgesDetails" indexed="true" property="amount" size="17" styleClass="inp-text"/>
						                            </td>
													<td align="center" valign="bottom" width="10%">
						                                <html:select name="fundingPledgesDetails" indexed="true" property="currencycode" styleClass="inp-text">
															<html:optionsCollection name="pledgeForm" property="validcurrencies" value="currencyCode"
															label="currencyName"/>
														</html:select>
						                            </td>
													<td align="center" valign="bottom" width="15%">
						                                <table cellPadding="0" cellSpacing="0">
															<tr>
																<td align="left" vAlign="bottom">
																	<html:text name="fundingPledgesDetails" indexed="true" property="fundingDate" styleClass="inp-text"
																	styleId="${indexFund}" readonly="true" size="10" />
																</td>
																<td align="left" vAlign="bottom">&nbsp;
																	<a id="transDate${indexFund}" href='javascript:pickDateById("transDate${indexFund}",${indexFund})'>
																			<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0" align="top">
																		</a>
																</td>														
															</tr>
														</table>
						                            </td>
													<td align="center" valign="bottom" width="20%">
						                               <html:select name="fundingPledgesDetails" indexed="true" property="aidmodalityid" styleClass="inp-text">
															<html:optionsCollection name="pledgeForm" property="aidModalityCategory" value="id"
															label="value"/>
														</html:select>
						                            </td>
						                        </tr>
												</c:forEach>

									    		</div>
												<tr>
													<td colspan="4"> &nbsp;
                                                           <html:button styleClass="dr-menu"  
                                                                         property="submitButton" onclick="addFunding();">
                                                                <digi:trn key="btn:addFunding">Add Funding</digi:trn>
                                                            </html:button>
															 &nbsp;
	                                                 		<logic:notEmpty name="pledgeForm" property="fundingPledgesDetails">
																<html:button styleClass="dr-menu" property="submitButton" onclick="return removeFunding()">
	                                                            <digi:trn key="btn:removeFunding">Remove Funding</digi:trn>
	                                                        	</html:button>
															</logic:notEmpty>
	                                                </td>
	                                            </tr>
	                                        </table>
									     </td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<feature:display name="Pledge Additional Information" module="Pledges">
								<table width="95%" bgcolor="#f4f4f2" border=0>
									<tr><td>
										<IMG  height="10" src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:additionalInformation">Additional Information</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr><td>
									<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<tr bgcolor="#ffffff">											
												<td valign="middle" align="left" width="90%">
													<a>
														<html:textarea property="additionalInformation" rows="6" cols="80" styleClass="inp-text"/>
                            						</a>
												</td>											
											</tr>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								</feature:display>
								<table width="95%" bgcolor="#f4f4f2" border="0">
									<tr><td align="center">
										<html:button styleClass="dr-menu" property="submitButton" onclick="return savePledge()">
	                                         <digi:trn key="btn:savePlegde">Save Pledge</digi:trn>
										</html:button>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<!-- end contents -->
							</td></tr>
							</table>
							</td></tr>
						</table>
						</td>
					</tr>	
					</table>
				</td></tr>
			</table>
		</td>
	</tr>
</table>

</body>
</digi:form>
