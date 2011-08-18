<%@page import="org.digijava.kernel.translator.TranslatorWorker"%>
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
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm, org.digijava.module.categorymanager.dbentity.AmpCategoryValue, java.util.*, org.digijava.module.aim.dbentity.*, org.springframework.beans.BeanWrapperImpl" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<!--<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>-->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<jsp:include page="addSectors.jsp"  />
<jsp:include page="scripts/newCalendar.jsp"  />
<% int indexFund = 0; 
PledgeForm pledgeForm = (PledgeForm) session.getAttribute("pledgeForm");
%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />



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
function addProgram(programType) {
	
	openNewRsWindow(750, 550);
	<digi:context name="taddProgram" property="context/module/moduleinstance/selectPledgeProgram.do?edit=true"/>
	var url="<%= taddProgram %>&programType="+programType;
     //       alert(programType + " "+url);
  	document.pledgeForm.action =url ;
	document.pledgeForm.target = popupPointer.name;
	document.pledgeForm.submit();
}
function removeProgram() {
	<c:set var="confirmDelete">
	  <digi:trn key="aim:removeSelectedProgramsMessage">
	 	 Remove selected programs?
	  </digi:trn>
	</c:set>
	if (confirm("${confirmDelete}")){
		var i = 1
		var delStr = "deleteProgs="
		while (document.getElementById("checkProg"+i)!=null){
			if(document.getElementById("checkProg"+i).checked==true){
				delStr = delStr + "_" + i;
			}
			i++;
		}
		if (delStr.length < 14){
			alert ("Please, select a program first.");
		} else {
			document.pledgeForm.target = "_self";
			document.pledgeForm.action="/removePledgeProgram.do?"+delStr;
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
	 myAddSectors("edit=true&configId=-1");
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

function cancel(){
	<digi:context name="cancel" property="/savePledge.do" />
	document.pledgeForm.action = "<%=cancel%>?cancel=true";
	document.pledgeForm.target = "_self";

	document.pledgeForm.submit();
}

function savePledge() {

	if (validateData()){
		var i = 0;
		var param = "";
		while (i<=numFund){
			if(document.getElementById('fund_'+i)!=null){
				param += document.getElementsByName('fund_'+i+"_0")[0].value + "_";
				if (document.getElementsByName('fund_'+i+"_2")[0] == null){
					param += "-1_";
				}else{
					param += document.getElementsByName('fund_'+i+"_2")[0].value + "_";
				}
				if (document.getElementsByName('fund_'+i+"_3")[0] == null){
					param += "-1_";
				}else{
					param += document.getElementsByName('fund_'+i+"_3")[0].value + "_";
				}
				param += document.getElementsByName('fund_'+i+"_4")[0].value + "_";
				param += document.getElementsByName('fund_'+i+"_5")[0].value + "_";
				param += document.getElementsByName('fund_'+i+"_6")[0].value + "_";
				if (document.getElementsByName('fund_'+i+"_7")[0] == null){
					param += "-1_";
				}else{
					param += document.getElementsByName('fund_'+i+"_7")[0].value + "_";
				}
				param += ";";
			}
			i++;
		}
		
		<digi:context name="save" property="/savePledge.do" />
  	 	document.pledgeForm.action = "<%=save%>?fundings="+param;
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
	if (document.getElementsByName("pledgeTitleId")[0]==null || document.getElementsByName("pledgeTitleId")[0].value==-1 || document.getElementsByName("pledgeTitleId")[0].value==0){
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

	<c:set var="percentageProgramTotal">
	  <digi:trn key="aim:sumOfProgramPercentagesMustBe100">
	 	 Sum of program percentages must be 100.
	  </digi:trn>
	</c:set>
	i=0;
	percent = 100;
	while (document.getElementsByName("selectedProgs["+i+"].programpercentage")[0]!=null){
		var temp = 0;
		temp = temp + document.getElementsByName("selectedProgs["+i+"].programpercentage")[0].value;
		if (document.getElementsByName("selectedProgs["+i+"].programpercentage")[0].value.length==0 || temp==0){
			alert ("${insertPercentage}")
			return false;
		}
		i++;
		percent = percent - temp;
	}
	if(percent!=0 && percent!=100){
		alert ("${percentageProgramTotal}")
		return false;
	}	

	<c:set var="addFunding">
	  <digi:trn key="aim:addFunding">
	 	 Pledges should have at least one funding.
	  </digi:trn>
	</c:set>
	i = 0;

	
	
	<c:set var="insertAmount">
	  <digi:trn key="aim:insertAmount">
	  	Please, insert amount greater than 0 for each funding.
	  </digi:trn>
	</c:set>
	i = 0;
	while (i<=numFund){
		if (document.getElementsByName("fund_"+i+"_4")[0]!=null){
			var temp = 0;
			temp = temp + document.getElementsByName("fund_"+i+"_4")[0].value;
			if (document.getElementsByName("fund_"+i+"_4")[0].value.length==0 || temp==0){
				alert ("${insertAmount}")
				return false;
			}
		}
		i++;
	}
	
	<c:set var="selectCurrency">
	  <digi:trn key="aim:selectCurrency">
	  	Please, select a currency.
	  </digi:trn>
	</c:set>
	i = 0;
	while (i<=numFund){
		if (document.getElementsByName("fund_"+i+"_5")[0]!=null){
			var temp = 0;
			temp = document.getElementsByName("fund_"+i+"_5")[0].value;
			if (temp==-1){
				alert ("${selectCurrency}")
				return false;
			}
		}
		i++;
	}
	
	
	<c:set var="insertValidEmail">
	  <digi:trn key="aim:insertValidEmail">
	 	 Please, insert a valid Email.
	  </digi:trn>
	</c:set>
	
	if (document.getElementsByName("contact1Email")[0]!=null && document.getElementsByName("contact1Email")[0].value.length>0 && document.getElementsByName("contact1Email")[0].value.indexOf("@") == -1){
		alert ("${insertValidEmail}")
		return false;
	}
	
	if (document.getElementsByName("contactAlternate1Email")[0]!=null && document.getElementsByName("contactAlternate1Email")[0].value.length>0 && document.getElementsByName("contactAlternate1Email")[0].value.indexOf("@") == -1){
		alert ("${insertValidEmail}")
		return false;
	}

	if (document.getElementsByName("contact2Email")[0]!=null && document.getElementsByName("contact2Email")[0].value.length>0 && document.getElementsByName("contact2Email")[0].value.indexOf("@") == -1){
		alert ("${insertValidEmail}")
		return false;
	}
	
	if (document.getElementsByName("contactAlternate2Email")[0]!=null && document.getElementsByName("contactAlternate2Email")[0].value.length>0 && document.getElementsByName("contactAlternate2Email")[0].value.indexOf("@") == -1){
		alert ("${insertValidEmail}")
		return false;
	}
	
	return true;
}

function changeTitle(){
	var title = document.getElementById("pledgeTitleDropDown").value;
	document.getElementById("myTitle").value = title;
	//if (title=="-2"){
	//	document.getElementById("newTitle").style.display = "block";
	//	document.getElementById("myTitle").value = "";
	//} else {
	//	if (title!="-1"){
	//		document.getElementById("newTitle").style.display = "none";
	//		document.getElementById("myTitle").value = title;
	//	} else {
	//		document.getElementById("newTitle").style.display = "none";
	//		document.getElementById("myTitle").value = "";
	//	}
	//}
	
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

document.getElementsByTagName('body')[0].className='yui-skin-sam';

--></script>

<style  type="text/css">
<!--

.contentbox_border{
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}

#my_autoComplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#my_autoComplete div {
	padding: 0px;
	margin: 0px; 
}



#my_autoComplete,
#my_autoComplete2 {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#my_autoComplete {
    z-index:3; /* z-index needed on top instance for ie & sf absolute inside relative issue */
}
#my_input,
#my_input2 {
    _position:absolute; /* abs pos needed for ie quirks */
}
.charcounter {
    display: block;
}
#myImage {
    position:absolute; left:320px; margin-left:1em; /* place the button next to the input */
}

-->
</style>

<digi:instance property="pledgeForm" />

<digi:form action="/addPledge.do" method="post">

<html:hidden name="pledgeForm" styleId="event" property="fundingEvent"/>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="900" vAlign="top" align="center" border=0>
	
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
					<span style="font-family: Tahoma;font-size: 11px;"><digi:errors/></span>
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
												<td align="left" width="70%">
													<select id="pledgeTitleDropDown" name="pledgeTitleDropDown" class="inp-text" onchange="changeTitle();" style="width:400px">
														<option selected="selected" value="-1"><digi:trn key="selectTitle">---Select title---</digi:trn></option>
														<c:forEach var="titles" items="${pledgeForm.pledgeNames}">
															<c:if test="${pledgeForm.pledgeTitleId == titles.id}">
																<option selected="selected" value="${titles.id}"/>	
															</c:if>
															<c:if test="${pledgeForm.pledgeTitleId != titles.id}">
																<option value="${titles.id}"/>
															</c:if>
															<c:out value="${titles.value}" />
															</option>
														</c:forEach>
													</select>
												</td>
											</tr>
											<tr bgcolor="#ffffff">
												<td valign="middle" align="left" width="30%">
													
												</td>
												<td align="left" width="70%">
													 <div id="newTitle" style="display: none">											
													    <html:text property="pledgeTitleId" styleId="myTitle" styleClass="inp-text" style="width:400px"></html:text>	
													   
													</div>																	    	
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
							                              <input   name='selectedOrgId' type="hidden" id="${valueId}" style="text-align:left" value='${pledgeForm.selectedOrgId}' size="4"/>
							                              <input name="selectedOrgName" type='text' id="${nameId}" value="${pledgeForm.selectedOrgName}" style="text-align:left; width:400px" onKeyDown="return false" class="inp-text"/>
							                              <aim:addOrganizationButton useClient="true" htmlvalueHolder="${valueId}" htmlNameHolder="${nameId}" >...</aim:addOrganizationButton>
                            						</a>
													
												</td>											
											</tr>
											<field:display name="Who Authorized Pledge" feature="Pledge Donor Information">
												<tr bgcolor="#ffffff">											
													<td valign="middle" align="left" width="30%">
														<a>
														<digi:trn key="whoHasAuthorizedPledge">Who Has Authorized Pledge?</digi:trn>
														</a>
													
													</td>
													<td valign="middle" align="left" width="70%">
														<a>
															<html:text property="whoAuthorizedPledge" style="text-align:left; width:400px" styleClass="inp-text"/>
	                            						</a>
													</td>											
												</tr>
											</field:display>
											<field:display name="Further Approval Needed" feature="Pledge Donor Information">
												<tr bgcolor="#ffffff">											
													<td valign="middle" align="left" width="30%">
														<a>
														<digi:trn key="pleaseIndicateFurtherApprovalNeeded">Please Indicate any Further Approval Needed</digi:trn> 
														</a>
													
													</td>
													<td valign="middle" align="left" width="70%">
														<a>
															<html:text property="furtherApprovalNedded" style="text-align:left; width:400px" styleClass="inp-text"/>
	                            						</a>
													</td>											
												</tr>
											</field:display>
										</table>
									</td></tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								
								<feature:display name="Pledge Sector and Location" module="Pledges">
									<table width="95%" bgcolor="#f4f4f2" border=0>
										<tr>
										    <td>
										        <!-- contents -->
										        <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
										        <b><digi:trn key="aim:sectorAndLocation">Sector and Location</digi:trn></b>
										         
										    </td>
								        </tr>
							            <field:display name="Pledge Sector" feature="Pledge Sector and Location">
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
	                                                    	<field:display name="Add Pledge Sector Button" feature="Pledge Sector and Location">
	                                                           <html:button styleClass="dr-menu"  
	                                                                         property="submitButton" onclick="addSectors();" >
	                                                                <digi:trn key="btn:addSectors">Add Sectors</digi:trn>
	                                                            </html:button>
															</field:display>
															<field:display name="Remove Pledge Sector Button" feature="Pledge Sector and Location">
																 &nbsp;
		                                                 		<logic:notEmpty name="pledgeForm" property="pledgeSectors">
																	<html:button styleClass="dr-menu" property="submitButton" onclick="return removeSector()">
		                                                          	  <digi:trn key="btn:removeSector">Remove Sector</digi:trn>
		                                                        	</html:button>
																</logic:notEmpty>
															</field:display>
		                                                </td>
		                                            </tr>
		                                        </table>
										     
										    </td>
										</tr>
										</field:display>
										<field:display name="Pledge Location" feature="Pledge Sector and Location">
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
															<field:display name="Add Pledge Location Button" feature="Pledge Sector and Location">
	                                                           <html:button styleClass="dr-menu"  
	                                                                         property="submitButton" onclick="addLocation();">
	                                                                <digi:trn key="btn:addLocation">Add Location</digi:trn>
	                                                            </html:button>
															</field:display>
															<field:display name="Remove Pledge Location Button" feature="Pledge Sector and Location">
																 &nbsp;
		                                                 		<logic:notEmpty name="pledgeForm" property="selectedLocs">
																	<html:button styleClass="dr-menu" property="submitButton" onclick="return removeLocation()">
		                                                            <digi:trn key="btn:removeLocation">Remove Location</digi:trn>
		                                                        	</html:button>
																</logic:notEmpty>
															</field:display>
		                                                </td>
		                                            </tr>
		                                        </table>
										     
										    </td>
										</tr>
										</field:display>
										<field:display name="Pledge Program" feature="Pledge Sector and Location">
										<tr><td>&nbsp;</td></tr>
										<tr>
							                <td>
							                    <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
							                    	<tr>
							                            <td align="left">
							                                <b>
							                                    <digi:trn key="aim:Program">
							                                        Program
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
													<c:forEach var="selectedProgs" items="${pledgeForm.selectedProgs}" varStatus="index">
	                                                  <tr>
	                                                      <c:set var="indexProg" value="${indexProg+1}"/>
									                            <td align="center" width="3%">
																	<input type="checkbox" id="checkProg${indexProg}"  >
																</td>
	                                                            <td align="left" width="67%">
		                                                            [${selectedProgs.program.name}] 
	                                                            </td>
	                                                            <td align="right" width="15%" nowrap="nowrap">
	                                                            <FONT color="red">*</FONT>
	                                                            		<digi:trn key="aim:percentage">Percentage</digi:trn>:&nbsp;
																</td>
																<td align="left" width="15%" nowrap="nowrap">
	                                                            		<html:text name="selectedProgs" indexed="true" property="programpercentage" size="5"  onkeyup="fnChk(this, 'region')" styleClass="inp-text"/>
	                                                            </td>
	                                                          
	                                                    </tr>
	                                                  </c:forEach>
													</td></tr>
													<tr>
														<td colspan="2"> &nbsp;
															<field:display name="Add Pledge Program Button" feature="Pledge Sector and Location">
	                                                           <html:button styleClass="dr-menu"  
	                                                                         property="submitButton" onclick="addProgram(1);">
	                                                                <digi:trn key="btn:addProgram">Add Program</digi:trn>
	                                                            </html:button>
															</field:display>
															<field:display name="Remove Pledge Program Button" feature="Pledge Sector and Location">
																 &nbsp;
		                                                 		<logic:notEmpty name="pledgeForm" property="selectedProgs">
																	<html:button styleClass="dr-menu" property="submitButton" onclick="return removeProgram()">
		                                                            <digi:trn key="btn:removeProgram">Remove Program</digi:trn>
		                                                        	</html:button>
																</logic:notEmpty>
															</field:display>
		                                                </td>
		                                            </tr>
		                                        </table>
										     
										    </td>
										</tr>
										</field:display>
										<tr><td>&nbsp;</td></tr>
										<tr><td>&nbsp;</td></tr>
									</table>
								</feature:display>
								<feature:display name="Pledge Funding" module="Pledges">
									<table width="95%" bgcolor="#f4f4f2" border=0>
										<tr>
										    <td>
										        <!-- contents -->
										        <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
										        <b><digi:trn key="aim:pledgeInformation">Pledge Information</digi:trn></b>
										        
										    </td>
								        </tr>
							            <tr><td>&nbsp;</td></tr>
										<tr>
							                <td>
							                    <div id="fundTitle" style="display:block;">
												<table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
							                    	<tr>
														<td align="center" valign="bottom" width="20" />
														<td align="center" width="240">
						                                	<b><digi:trn key="aim:typeOfPledge">Type Of Pledge</digi:trn></b>
						                            	</td>
														<field:display name="Pledge Funding - Type Of Assistance" feature="Pledge Funding">
															<td align="center" width="150">
								                                <b><digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn></b>
								                            </td>
														</field:display>
														<td align="center" width="150">
							                                <b><digi:trn key="aim:amount">Amount</digi:trn></b>
							                            </td>
														<td align="center" width="170">
							                                <b><digi:trn key="aim:typeOfCurrency">Currency</digi:trn></b>
							                            </td>
														<td align="center" width="100">
							                                <b><digi:trn key="aim:year">Year</digi:trn></b>
							                            </td>
														<field:display name="Pledge Funding - Aid Modality" feature="Pledge Funding">
															<td align="center" width="200">
							                                	<b><digi:trn key="aim:aidModality">Aid Modality</digi:trn></b>
							                            	</td>
														</field:display>
							                        </tr>
												</table>
												</div>
											</td>
										</tr>
										<tr>
											<td>
												<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
													<tr>
														<td>
			                                             	<div id="fundingDiv">
			                                             	<c:forEach var="fundingPledgesDetails" items="${pledgeForm.fundingPledgesDetails}" varStatus="status">
															
															<% String tNameBase = "fund_" + indexFund + "_"; 
															String divName = "fund_" + indexFund;
															indexFund++;
															String field0 = tNameBase + "0"; 
															 String field1 = tNameBase + "1"; 
															 String field2 = tNameBase + "2"; 
															 String field3 = tNameBase + "3"; 
															 String field4 = tNameBase + "4";
															 String field5 = tNameBase + "5";
															 String field6 = tNameBase + "6";
															 String field7 = tNameBase + "7"; %>
															 <div id="<%=divName%>" >
																<table width='100%' bgcolor='#FFFFFF' cellPadding=5 cellSpacing=1>
																<tr>
										                            <td align="center" valign="bottom" width="30" >
																		<input name="<%=field0%>" type="hidden" id="<%=field0%>" value='${fundingPledgesDetails.id}'/>
											                        	<input type="checkbox" name="<%=field1%>" id="<%=field1%>" >
																	</td>
																	<td align="center" valign="bottom" width="170">
																		<select name="<%=field2%>" class="inp-text" style="max-width: 150px;">
																			<option selected="selected" value="-1">-<digi:trn>Select from below</digi:trn>-</option>
																			<c:forEach var="type" items="${pledgeForm.pledgeTypeCategory}">
																				<c:if test="${fundingPledgesDetails.pledgetypeid == type.id}">
																					<option selected="selected" value="<c:out value="${type.id}"/>">	
																				</c:if>
																				<c:if test="${fundingPledgesDetails.pledgetypeid != type.id}">
																					<option value="<c:out value="${type.id}"/>">
																				</c:if>
																				<digi:trn>${type.value}</digi:trn>
																				</option>
																			</c:forEach>
																		</select>
										                            </td>
																	<field:display name="Pledge Funding - Type Of Assistance" feature="Pledge Funding">
																		<td align="center" valign="bottom" width="200">
											                                <select name="<%=field3%>" class="inp-text" style="max-width: 150px;">
																				<option selected="selected" value="-1">-<digi:trn>Select from below</digi:trn>-</option>
																				<c:forEach var="type" items="${pledgeForm.assistanceTypeCategory}">
																					<c:if test="${fundingPledgesDetails.typeOfAssistanceid == type.id}">
																						<option selected="selected" value="<c:out value="${type.id}"/>">	
																					</c:if>
																					<c:if test="${fundingPledgesDetails.typeOfAssistanceid != type.id}">
																						<option value="<c:out value="${type.id}"/>">
																					</c:if>
																					<digi:trn>${type.value}</digi:trn>
																					</option>
																				</c:forEach>
																			</select>
											                            </td>
																	</field:display>
																	<td align="center" valign="bottom" width="150">
																		
																		<input type="text" name="<%=field4%>" value="<aim:formatNumber value="${fundingPledgesDetails.amount}"/>" style="width:90px" class="inp-text"/>
										                            </td>
																	<td align="center" valign="bottom" width="100">
										                                <select name="<%=field5%>" class="inp-text" style="max-width: 150px;">
																			<option selected="selected" value="-1">-<digi:trn>Select from below</digi:trn>-</option>
																			<c:forEach var="currency" items="${pledgeForm.validcurrencies}">
																				<c:if test="${fundingPledgesDetails.currencycode == currency.currencyCode}">
																					<option selected="selected" value="<c:out value="${currency.currencyCode}"/>">	
																				</c:if>
																				<c:if test="${fundingPledgesDetails.currencycode != currency.currencyCode}">
																					<option value="<c:out value="${currency.currencyCode}"/>">
																				</c:if>
																				<c:out value="${currency.currencyName}" />
																				</option>
																			</c:forEach>
																		</select>
										                            </td>
																	<td align="center" valign="bottom" width="150">
																		<select name="<%=field6%>" class="inp-text" style="max-width: 150px;">
																			<option value="unspecified"><digi:trn>unspecified</digi:trn></option>	
																			<c:forEach var="year" items="${pledgeForm.years}">
																				<c:if test="${fundingPledgesDetails.fundingYear == year}">
																					<option selected="selected" value="<c:out value="${year}"/>">	
																				</c:if>
																				<c:if test="${fundingPledgesDetails.fundingYear != year}">
																					<option value="<c:out value="${year}"/>">
																				</c:if>
																					${year}
																				</option>
																			</c:forEach>
																		</select>
										                                </td>
																	<field:display name="Pledge Funding - Aid Modality" feature="Pledge Funding">
																		<td align="center" valign="bottom" width="200">
											                               <select name="<%=field7%>" class="inp-text" style="max-width: 150px;">
																				<option selected="selected" value="-1">-<digi:trn>Select from below</digi:trn>-</option>
																				<c:forEach var="type" items="${pledgeForm.aidModalityCategory}">
																					<c:if test="${fundingPledgesDetails.aidmodalityid == type.id}">
																						<option selected="selected" value="<c:out value="${type.id}"/>">	
																					</c:if>
																					<c:if test="${fundingPledgesDetails.aidmodalityid != type.id}">
																						<option value="<c:out value="${type.id}"/>">
																					</c:if>
																					<digi:trn>${type.value}</digi:trn>
																					</option>
																				</c:forEach>
																			</select>
											                            </td>
																	</field:display>
										                        </tr>
															</table>
														</div>
														</c:forEach>
														</div>
														</td>
													</tr>
													<tr>
														<td colspan="4"> &nbsp;
															<table>
																<tr>
																	<td>
																		<field:display name="Add Pledge Funding Button" feature="Pledge Funding">
				                                                           <html:button styleClass="dr-menu"  
				                                                                         property="submitButton" onclick="addFunding();">
				                                                                <digi:trn key="btn:addFunding">Add Funding</digi:trn>
				                                                            </html:button>
																			&nbsp;
																		</field:display>
																	</td>
																	<td>
																		<field:display name="Remove Pledge Funding Button" feature="Pledge Funding">
																		<div id="remBut" style="display:block;">
																			<html:button styleClass="dr-menu" property="submitButton" onclick="return removeFunding()">
				                                                            <digi:trn key="btn:removeFunding">Remove Funding</digi:trn>
				                                                        	</html:button>
																		</div>
																		</field:display>
																	</td>
																</tr>
															</table>
		                                                </td>
		                                            </tr>
		                                        </table>
										     </td>
										</tr>
										<tr><td>&nbsp;</td></tr>
										<tr><td>&nbsp;</td></tr>
									</table>
								</feature:display>
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
												<field:display name="Pledge Contact 1 - Name" feature="Pledge Contact 1">								
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactName">Name</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact1Name" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
	                            						</a>
													</td>
												</field:display>
												<field:display name="Pledge Contact 1 - Title" feature="Pledge Contact 1">
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactTitle">Title</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact1Title" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
	                            						</a>
													</td>		
												</field:display>								
											</tr>
											<tr bgcolor="#ffffff">
												<field:display name="Pledge Contact 1 - Organization" feature="Pledge Contact 1">											
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactOrganization">Organization</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<c:set var="valueId1"> contact1OrgId </c:set>
								                              <c:set var="nameId1"> contact1OrgName </c:set>
								                              <input name="contact1OrgId" type="hidden" id="${valueId1}" style="text-align:left" value='${pledgeForm.contact1OrgId}' size="4"/>
								                              <input name="contact1OrgName" type="text" id="${nameId1}" style="text-align:left" value='${pledgeForm.contact1OrgName}' size="33" style="background-color:#CCCCCC" onKeyDown="return false" class="inp-text" onchange="setSameContact()"/>
								                              <aim:addOrganizationButton useClient="true" useAcronym="true" htmlvalueHolder="${valueId1}" htmlNameHolder="${nameId1}" >...</aim:addOrganizationButton>
	                            						
	                            						</a>
													</td>	
												</field:display>
												<field:display name="Pledge Contact 1 - Ministry" feature="Pledge Contact 1">
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactMinistry">Ministry</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact1Ministry" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
	                            						</a>
													</td>			
												</field:display>							
											</tr>
											<tr bgcolor="#ffffff">		
												<field:display name="Pledge Contact 1 - Address" feature="Pledge Contact 1">									
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactAddress">Address</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact1Address" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
	                            						</a>
													</td>	
												</field:display>
												<field:display name="Pledge Contact 1 - Telephone" feature="Pledge Contact 1">
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactTelephone">Telephone</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact1Telephone" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
	                            						</a>
													</td>		
												</field:display>								
											</tr>
											<tr bgcolor="#ffffff">
												<field:display name="Pledge Contact 1 - Email" feature="Pledge Contact 1">											
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactEmail">Email</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact1Email" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
	                            						</a>
													</td>	
												</field:display>
												<field:display name="Pledge Contact 1 - Fax" feature="Pledge Contact 1">
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactFax">Fax</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact1Fax" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
	                            						</a>
													</td>	
												</field:display>									
											</tr>
										</table>
										<tr><td>&nbsp;</td></tr>
									<field:display name="Pledge Contact 1 - Alternate Contact" feature="Pledge Contact 1">
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
															<html:text property="contactAlternate1Name" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
	                            						</a>
													</td>	
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactTelephone">Telephone</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contactAlternate1Telephone" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
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
															<html:text property="contactAlternate1Email" size="40" styleClass="inp-text" onkeyup="setSameContact()"/>
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
									</field:display>
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
												<field:display name="Pledge Contact 2 - Name" feature="Pledge Contact 2">											
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactName">Name</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact2Name" size="40" styleClass="inp-text"/>
	                            						</a>
													</td>	
												</field:display>
												<field:display name="Pledge Contact 2 - Title" feature="Pledge Contact 2">
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactTitle">Title</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact2Title" size="40" styleClass="inp-text"/>
	                            						</a>
													</td>	
												</field:display>									
											</tr>
											<tr bgcolor="#ffffff">	
												<field:display name="Pledge Contact 2 - Organization" feature="Pledge Contact 2">										
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactOrganization">Organization</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<c:set var="valueId2"> contact2OrgId </c:set>
								                              <c:set var="nameId2"> contact2OrgName </c:set>
								                              <input name="contact2OrgId" type="hidden" id="${valueId2}" style="text-align:left" value='${pledgeForm.contact2OrgId}' size="4"/>
								                              <input name="contact2OrgName" type='text' id="${nameId2}" style="text-align:left" value='${pledgeForm.contact2OrgName}' size="33" style="background-color:#CCCCCC" onKeyDown="return false" class="inp-text"/>
								                              <aim:addOrganizationButton useClient="true" useAcronym="true" htmlvalueHolder="${valueId2}" htmlNameHolder="${nameId2}" >...</aim:addOrganizationButton>
	                            						
	                            						</a>
													</td>	
												</field:display>
												<field:display name="Pledge Contact 2 - Ministry" feature="Pledge Contact 2">
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactMinistry">Ministry</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact2Ministry" size="40" styleClass="inp-text"/>
	                            						</a>
													</td>			
												</field:display>							
											</tr>
											<tr bgcolor="#ffffff">
												<field:display name="Pledge Contact 2 - Address" feature="Pledge Contact 2">											
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
												</field:display>
												<field:display name="Pledge Contact 2 - Telephone" feature="Pledge Contact 2">
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactTelephone">Telephone</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact2Telephone" size="40" styleClass="inp-text"/>
	                            						</a>
													</td>	
												</field:display>									
											</tr>
											<tr bgcolor="#ffffff">											
												<field:display name="Pledge Contact 2 - Email" feature="Pledge Contact 2">
													<td valign="middle" align="left" width="15%">
														<a>
															<digi:trn key="pointContactEmail">Email</digi:trn>
														</a>
													</td>
													<td valign="middle" align="left" width="35%">
														<a>
															<html:text property="contact2Email" size="40" styleClass="inp-text"/>
	                            						</a>
													</td>	
												</field:display>
												<field:display name="Pledge Contact 2 - Fax" feature="Pledge Contact 2">
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
												</field:display>									
											</tr>
										</table>
										<tr><td>&nbsp;</td></tr>
									<field:display name="Pledge Contact 2 - Alternate Contact" feature="Pledge Contact 2">
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
									</field:display>
								</table>
								</feature:display>
								
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
									<tr>
										<td align="right" width="50%">
											<html:button styleClass="dr-menu" property="submitButton" onclick="return savePledge()">
		                                         <digi:trn key="btn:savePlegde">Save Pledge</digi:trn>
											</html:button>
										</td>
										<td align="left" width="50%">
											<html:button styleClass="dr-menu" property="submitButton" onclick="return cancel()">
		                                         <digi:trn key="btn:cancel">Cancel</digi:trn>
											</html:button>
										</td>
									</tr>
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
<script type="text/javascript">

initFund();
var numFund = <%=indexFund%>;
var tempFund = numFund;

function initFund(){
	numFund = <%=indexFund%>;
	tempFund = <%=indexFund%>;
	if (tempFund==0){
		var titles = document.getElementById('fundTitle');
		titles.style.display="none";
		var remBut = document.getElementById('remBut');
		remBut.style.display="none";
	}
}

function addFunding() {
	var ni = document.getElementById('fundingDiv');
	var divname = "fund_" + numFund;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<table width='100%' bgcolor='#FFFFFF' cellPadding=5 cellSpacing=1> <tr> <td align='center' valign='bottom' width='30' >";
	s += "<input name='fund_"+ numFund +"_0' type='hidden' id='fund_"+ numFund +"_0' value=''/> <input type='checkbox' id='fund_"+ numFund +"_1'/></td>";

	s += "<td align='center' valign='bottom' width='170'> <select name='fund_"+ numFund +"_2' class='inp-text' style='max-width: 150px;'>";
	s += "<option selected='true' value='-1'>-<digi:trn>Select from below</digi:trn>-</option>";
	<% Collection col = pledgeForm.getPledgeTypeCategory();
	Iterator itr = col.iterator();
	while (itr.hasNext()) {
		AmpCategoryValue type = (AmpCategoryValue) itr.next();	
		if (type != null){ %>
				s += "<option value='<%=type.getId()%>'><%=type.getValue()%></option>";				  			
		<% }
	 }%>
	 s += "</select> </td>";
	 
	<field:display name="Pledge Funding - Type Of Assistance" feature="Pledge Funding">
	s += "<td align='center' valign='bottom' width='200'> <select name='fund_"+ numFund +"_3' class='inp-text' style='max-width: 150px;'>";
	s += "<option selected='true' value='-1'>-<digi:trn>Select from below</digi:trn>-</option>";
	<% Collection col2 = pledgeForm.getAssistanceTypeCategory();
	Iterator itr2 = col2.iterator();
	while (itr2.hasNext()) {
		AmpCategoryValue type = (AmpCategoryValue) itr2.next();	
		if (type != null){ %>
				s += "<option value='<%=type.getId()%>'><%=TranslatorWorker.translateText(type.getValue(),request)%></option>";				  			
		<% }
	 }%>
	 s += "</select> </td>";
	 </field:display>
	 
	s += "<td align='center' valign='bottom' width='150'> <input type='text' name='fund_"+ numFund +"_4' size='17' style='width:90px' class='inp-text'/> </td>";

	s += "<td align='center' valign='bottom' width='100'> <select name='fund_"+ numFund +"_5' class='inp-text' style='max-width: 150px;'>";
	s += "<option selected='true' value='-1'>-<digi:trn>Select from below</digi:trn>-</option>";
	<% Collection col3 = pledgeForm.getValidcurrencies();
	Iterator itr3 = col3.iterator();
	while (itr3.hasNext()) {
		AmpCurrency currency = (AmpCurrency) itr3.next();	
		if (currency != null){
			if (currency.getCurrencyCode().equals(pledgeForm.getDefaultCurrency())) {%>
				s += "<option selected='true' value='<%=currency.getCurrencyCode()%>'><%=currency.getCurrencyName()%></option>";				  			
		<% } else { %>
				s += "<option value='<%=currency.getCurrencyCode()%>'><%=currency.getCurrencyName()%></option>";
		<%}
		}
	 }%>
	 s += "</select> </td>";

	 s += "<td align='center' valign='bottom' width='100'> <select name='fund_"+ numFund +"_6' class='inp-text' style='max-width: 150px;'> <option value='unspecified'><digi:trn>unspecified</digi:trn></option>";
		<% Collection col5 = pledgeForm.getYears();
		Iterator itr5 = col5.iterator();
		while (itr5.hasNext()) {
			String year = (String) itr5.next();	
			
			if (year != null){
				if (year.equals(pledgeForm.getYear())) {%>
					s += "<option selected='true' value='<%=year%>'><%=TranslatorWorker.translateText(year,request)%></option>";				  			
			<% } else { %>
					s += "<option value='<%=year%>'><%=TranslatorWorker.translateText(year,request)%></option>";
			<%}
			}
		 }%>
		 s += "</select> </td>";

	<field:display name="Pledge Funding - Aid Modality" feature="Pledge Funding">
	s += "<td align='center' valign='bottom' width='200'> <select name='fund_"+ numFund +"_7' class='inp-text' style='max-width: 150px;'>";
	s += "<option selected='true' value='-1'>-<digi:trn>Select from below</digi:trn>-</option>";
	<% Collection col4 = pledgeForm.getAidModalityCategory();
	Iterator itr4 = col4.iterator();
	while (itr4.hasNext()) {
		AmpCategoryValue type = (AmpCategoryValue) itr4.next();	
		if (type != null){ %>
				s += "<option value='<%=type.getId()%>'><%=TranslatorWorker.translateText(type.getValue(),request)%></option>";				  			
		<% }
	 }%>
	 s += "</select> </td>";
	 </field:display>

	 s += "</tr> </table>";
	
	newdiv.innerHTML = s;
	ni.appendChild(newdiv);
	numFund++;
	tempFund++;
	var titles = document.getElementById('fundTitle');
	titles.style.display="block";
	var remBut = document.getElementById('remBut');
	remBut.style.display="block";
}

function removeFunding()
{
	<c:set var="confirmDelete">
	  <digi:trn key="aim:removeSelectedFundingMessage">
	 	 Remove selected fundings?
	  </digi:trn>
	</c:set>
	if (confirm("${confirmDelete}")){
		var d = document.getElementById('fundingDiv');
		var i = 0;
		var flag = false;
		while (i<=numFund){
			if(document.getElementById("fund_"+i+"_1")!=null && document.getElementById("fund_"+i+"_1").checked==true){
				var olddiv = document.getElementById("fund_"+i);
				d.removeChild(olddiv);
				tempFund--;
				flag = true;
			}
			i++;
		}
		if (!flag){
			alert ("Please, select a funding first.");
		}
	}
	if (tempFund==0){
		var titles = document.getElementById('fundTitle');
		titles.style.display="none";
		var remBut = document.getElementById('remBut');
		remBut.style.display="none";
	}
}

function addRowDefault(){
	if (numFund==0){
		addFunding();
	}
}

addRowDefault();
</script>

</digi:form>
