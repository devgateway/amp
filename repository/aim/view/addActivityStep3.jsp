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
<%@page import="org.digijava.module.aim.helper.Constants" %>
<%@page import="org.digijava.module.categorymanager.util.CategoryManagerUtil" %>
<%@page import="org.digijava.module.aim.form.EditActivityForm" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<jsp:include page="addActivityStep3Popin.jsp" flush="true" />
<jsp:include page="addActivityStep3PopinImport.jsp" flush="true" />
<jsp:include page="addOrganizationPopin.jsp" flush="true" />
<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<script language="JavaScript">
	function  doNothing()
	{
		
	}
</script>

<script language="JavaScript">

	<!--

	function validateFunding() {
	
		if (document.getElementsByName('funding.selFundingOrgs').length == 1) { // only one org. added
			if (document.getElementById('selFundingOrgs').checked == false) {
				alert("Please choose a funding organization to remove");
				return false;
			}
		} else { // many org. present
			var length = document.getElementsByName('funding.selFundingOrgs').length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.getElementsByName('funding.selFundingOrgs')[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("Please choose a funding organization to remove");
				return false;
			}
		}
		return true;
	}

	function addFunding(orgId) {
			myAddFunding(orgId);
			/*
			openNewRsWindow(900, 500);
			<digi:context name="addFunding" property="context/module/moduleinstance/addFunding.do" />
			document.getElementById('orgId').value = orgId;
			document.aimEditActivityForm.action = "<%= addFunding %>?orgId" + orgId+"&edit=true";
			document.aimEditActivityForm.prevOrg.value = orgId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
			*/
	}

	function importFunding(orgId) {
			myImportFunding(orgId);
	}


	function changeOrganisation(orgId){
			openNewWindow(650, 420);
			<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&changeOrganisation=true&step=3" />		
			document.aimEditActivityForm.action = "<%= selectOrganization %>";
			document.aimEditActivityForm.prevOrg.value = orgId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();	
	}

	function selectOrganisation() {
			openNewWindow(650, 420);
			<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true&step=3" />
			document.aimEditActivityForm.action = "<%= selectOrganization %>";
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}


	function resetAll()
	{
		<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
		document.aimEditActivityForm.action = "<%= resetAll %>";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
		return true;
	}

	function removeOrganisation(orgId) {
		<digi:context name="remSelOrg" property="context/module/moduleinstance/remFundOrg.do?edit=true" />
		document.aimEditActivityForm.action = "<%= remSelOrg %>&fundOrgId=" + orgId;
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
		return true;
	}

	function validateForm() {
		return true;
	}

	function indexedCheckboxClick(name)
	{
		index1=name.indexOf("[");
		index2=name.indexOf("]");
		index=name.substring(index1+1, index2);
		propertyName=name.substring(index2+2, name.length);
		hiddenName="fundingOrganization["+index+"]."+propertyName+"String";
		checkboxName="fundingOrganization["+index+"]."+propertyName;
		inputHidden=document.getElementsByName(hiddenName);
		checkboxArray=document.getElementsByName(checkboxName);
		checkbox=checkboxArray[0];

		if(checkbox.checked==true )
		{	inputHidden[0].value="checked"; }
		if(checkbox.checked==false)
		{	inputHidden[0].value="unchecked";}

	}

	function delegatedCooperationClick(name)
	{
		index1=name.indexOf("[");
		index2=name.indexOf("]");
		index=name.substring(index1+1, index2);
		propertyName=name.substring(index2+2, name.length);
		hiddenName="fundingOrganization["+index+"]."+propertyName+"String";
		checkboxName="fundingOrganization["+index+"]."+propertyName;
		inputHidden=document.getElementsByName(hiddenName);
		checkboxArray=document.getElementsByName(checkboxName);
		checkbox=checkboxArray[0];

		for(i=0;;i++)
		{
			if(i!=index)
			{
				fundOrgCheckbox=document.getElementsByName("fundingOrganization["+i+"]."+propertyName);
				fundOrgHidden=document.getElementsByName("fundingOrganization["+i+"]."+propertyName+"String");
				if (fundOrgCheckbox.length==0) break;
				fundOrgCheckbox[0].checked=false; fundOrgCheckbox.value="off";
				fundOrgHidden[0].value="unchecked"
			}
		}

		if(checkbox.checked==true )
		{	inputHidden[0].value="checked";}
		if(checkbox.checked==false)
		{	inputHidden[0].value="unchecked";}
	}
	/*function selectOrg(params1, params2, params3, params4) {
		mySelectOrg(params1, params2, params3, params4);
	}*/
	function toggleGroup(group_id){
		var strId='#'+group_id;
		$(strId+'_minus').toggle();
		$(strId+'_plus').toggle();
		$(strId+'_dots').toggle();
		$('#act_'+group_id).toggle('fast');
	}
	function changeCurrency(currCode) {
		<digi:context name="changeCurrency" property="context/module/moduleinstance/changeCurrency.do?edit=true" />
		document.aimEditActivityForm.action = "<%= changeCurrency %>&changeCurrency=" + currCode.value;
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
		return true;
	}

    function addActionToURL(actionName){
        var fullURL=document.URL;
        var lastSlash=fullURL.lastIndexOf("/");
        var partialURL=fullURL.substring(0,lastSlash);
        return partialURL+"/"+actionName;
    }

    function totalsPage(currCode) {
       var currency=currCode.value;
       var url=addActionToURL('getFundingTotals.do')+'?edit=true&regFundingPageCurrCode='+currency+'&isRegcurr=true'+'&isStepPage=true';
	   var request = YAHOO.util.Connect.asyncRequest('GET', url, callbackTotals); 
    }
	var handleTotalsSuccess = function(o){
		if(o.responseText !== undefined){
			var root=o.responseXML.getElementsByTagName('total')[0];
			var comm=document.getElementById("comms");
			var disb=document.getElementById("disb");
			var expn=document.getElementById("expn");

			var curr=root.getAttribute("curr");
			var totalComm=root.getAttribute("totalComm");
			comm.value=totalComm+" "+curr;
			disb.value=root.getAttribute("disb")+' '+curr;
			expn.value=root.getAttribute("expn")+' '+curr;
		}
	}
	
	var handleTotalsFailure = function(o){
	}
	
	var callbackTotals =
	{
	  success:handleTotalsSuccess,
	  failure: handleTotalsFailure
	};

	function addRegionalFunding() {
		openNewWindow(650, 500);
		<digi:context name="addRegFunding" property="context/module/moduleinstance/addRegionalFunding.do?edit=true&regFundAct=show" />
		document.aimEditActivityForm.action = "<%= addRegFunding %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	}

	function editFunding(id) {
		openNewWindow(650, 500);
		<digi:context name="addRegFunding" property="context/module/moduleinstance/addRegionalFunding.do?edit=true&regFundAct=showEdit" />
		document.aimEditActivityForm.action = "<%= addRegFunding %>&fundId="+id;
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	}

	function removeRegFundings(regionId) {
		<digi:context name="rem" property="context/module/moduleinstance/removeRegionalFunding.do?edit=true" />
		document.aimEditActivityForm.action = "<%= rem %>&regId="+regionId;
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
	}

	-->
</script>

<link rel="stylesheet" href="/TEMPLATE/ampTemplate/css/activityform_style.css" type="text/css">

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">
<html:hidden property="step"/>
<html:hidden property="funding.orgId" styleId="orgId"/>
<html:hidden property="funding.fundingId" styleId="fundingId"/>


<input type="hidden" name="prevOrg">
<input type="hidden" name="edit" value="true">
<c:set var="stepNm">
    ${aimEditActivityForm.stepNumberOnPage}
</c:set>

<html:hidden property="editAct" />

<c:out value=""></c:out>

<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr>
<td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td>
</tr>
<tr>
<td width="100%" vAlign="top" align="left">
<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="100%" vAlign="top" align="center" border="0">
	<tr>
		<td class="r-dotted-lg" width="10">&nbsp;</td>
		<td align="left" valign="top" class="r-dotted-lg">
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class="crumb"  style="visibility: hidden">
								<c:if test="${aimEditActivityForm.pageId == 0}">
									<c:set var="translation">
										<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
									</c:set>
									<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>
									&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">
									<c:set var="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
									</c:set>

										<c:set var="message">
<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot1('${msg}')" title="${translation}" >
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>
									&nbsp;&gt;&nbsp;
								</c:if>
			               
                           <c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">
                               
                               <c:set property="translation" var="trans">
                                   <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                       Click here to goto Add Activity Step ${step.stepActualNumber}
                                   </digi:trn>
                               </c:set>
                               
                               
                               
                               <c:if test="${!index.last}">
                                   
                                   <c:if test="${index.first}">
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                           
                                           
                                           <c:if test="${aimEditActivityForm.editAct == true}">
                                               <digi:trn key="aim:editActivityStep1">
                                                   Edit Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                           <c:if test="${aimEditActivityForm.editAct == false}">
                                               <digi:trn key="aim:addActivityStep1">
                                                   Add Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                           
                                       </digi:link>
                                        &nbsp;&gt;&nbsp;
                                   </c:if>
                                   <c:if test="${!index.first}">
                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
                                           <digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn>
                                       </digi:link>
                                        &nbsp;&gt;&nbsp;
                                   </c:if>
                               </c:if>
                               
                               
                               
                               <c:if test="${index.last}">
                                   
                                   <c:if test="${index.first}">
                                       
                                       
                                       
                                       <c:if test="${aimEditActivityForm.editAct == true}">
                                           <digi:trn key="aim:editActivityStep1">
                                               Edit Activity - Step 1
                                           </digi:trn>
                                       </c:if>
                                       <c:if test="${aimEditActivityForm.editAct == false}">
                                           <digi:trn key="aim:addActivityStep1">
                                               Add Activity - Step 1
                                           </digi:trn>
                                       </c:if>
                                   </c:if>
                                   
                                   
                                   <c:if test="${!index.first}">
                                       <digi:trn key="aim:addActivityStep${step.stepActualNumber}"> Step ${step.stepActualNumber}</digi:trn>
                                   </c:if>
                                   
                                   
                                   
                               </c:if>
                               
                               
                               
                               
                               
                               
                               
                           </c:forEach>
                            
                           
				
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<jsp:include page="/repository/aim/view/activityForm_actions_menu.jsp" />
				</td></tr>
                <tr>
                  <td>
                    <table width="100%" cellSpacing="1" cellPadding="1" valign="top">
                      <tr>
                        <td height="16" vAlign="middle" width="100%"><span class="subtitle-blue">
                          <c:if test="${aimEditActivityForm.editAct == false}">
                            <digi:trn key="aim:addNewActivity">
                            Add New Activity
                            </digi:trn>
                          </c:if>
                          <c:if test="${aimEditActivityForm.editAct == true}">
                            <digi:trn>Title:</digi:trn>&nbsp;<bean:write name="aimEditActivityForm" property="identification.title"/>
                          </c:if>
                          </span>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
				<tr>
                  <td>
                    <digi:errors/>
                  </td>
                </tr>
                <tr valign="top">
                  <td>
                    <table width="100%" cellSpacing="0" cellPadding="1" vAlign="top">
                      <tr>
                        <td width="75%" vAlign="top">
                          <table cellPadding="0" cellSpacing="0" width="100%" vAlign="top">
                            
                            <tr>
                              <td bgColor="#f4f4f2" align="center" vAlign="top" width="100%">
                                <table width="100%" cellSpacing="0" vAlign="top" align="left">
                                  <tr>
                                    <td>
                                    	<bean:define name="aimEditActivityForm" id="myForm" type="org.digijava.module.aim.form.EditActivityForm"/>
	                             	  	<feature:display name="Funding Overview" module="Funding">
                                        <table width="100%" bgcolor="#f4f4f2" border="0" cellSpacing="0" cellPadding="0">
                                          <tr>
                                            <td class="separator1" title="<digi:trn>Funding Overview (To Date)</digi:trn>">&nbsp;
                                              <digi:trn>Funding Overview (To Date)</digi:trn>
                                            </td>
                                          </tr>
                                          <tr>
                                            <td align="left">
                                            <table width="100%" cellSpacing="0" cellPadding="0" border="0">
                                                <tr>
                                                  <td>
                                                    <table cellSpacing="8" cellPadding="0" border="0" width="95%" align="center">
                                                      <tr>
                                                        <td width="20">&nbsp;</td> 
                                                        <td>                                         
                                                            <table cellSpacing="1" cellPadding="1" width="100%">
                                                              <tr bgcolor="#ffffff">
                                                                <td bgcolor="#FFFFFF" align="left" width="33%" colspan="3">
                                                                <c:if test="!empty aimEditActivityForm.currCode">
	                                                                <digi:trn>Change Currency</digi:trn>
                                                                    <html:select name="aimEditActivityForm" property="currCode" styleClass="inp-text" onchange="totalsPage(this);">
                                                                        <html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode" label="currencyName"/>
                                                                    </html:select>
                                                                </c:if>
                                                                </td>
                                                              </tr>
                                                              <tr bgcolor="#ffffff">
                                                                <td bgcolor="#FFFFFF" align="left" width="33%"><strong>Total Actual Commitments</strong>
                                                                </td>
                                                                <td bgcolor="#FFFFFF" align="left" width="34%"><strong>Total Actual Disbursements</strong>
                                                                </td>
                                                                <td bgcolor="#FFFFFF" align="left" width="33%"><strong>Total Actual Expenditures</strong>
                                                                </td>
                                                              </tr>
                                                              <tr bgcolor="#ffffff">
                                                                <td bgcolor="#FFFFFF" align="left">
                                                                <input type="text" id="comms" class="dr-menu" value="${aimEditActivityForm.funding.totalCommitments} ${aimEditActivityForm.currCode}" disabled="disabled"/>
                                                                </td>
                                                                <td bgcolor="#FFFFFF" align="left">
                                                                <input type="text" id="disb" class="dr-menu" value="${aimEditActivityForm.funding.totalDisbursements} ${aimEditActivityForm.currCode}" disabled="disabled"/>
                                                                </td>
                                                                <td bgcolor="#FFFFFF" align="left">
                                                                <input type="text" id="expn" class="dr-menu" value="${aimEditActivityForm.funding.totalExpenditures} ${aimEditActivityForm.currCode}" disabled="disabled"/>
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
                                          <tr>
                                            <td>&nbsp;
                                              
                                            </td>
                                          </tr>
                                        </table>
                                      </feature:display>
                                      <br />
                             	  	<feature:display name="Proposed Project Cost" module="Funding">
                                        <table width="100%" bgcolor="#f4f4f2" border="0" cellSpacing="0" cellPadding="0">
                                          <tr>
                                            <td class="separator1">&nbsp;
                                              <digi:trn key="aim:proposedPrjectCost">Proposed Project Cost</digi:trn>
											  <img src="/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" align="absmiddle" hspace="5" title="<digi:trn key="aim:ProposedProjCost">Proposed Project Cost</digi:trn>"/>
                                            </td>
                                          </tr>
                                          <tr>
                                            <td align="left">
                                            <table width="100%" cellSpacing="0" cellPadding="0" border="0">
                                                <tr>
                                                  <td>
                                                    <table cellSpacing="8" cellPadding="0" border="0" width="95%" align="center">
                                                      <tr>
                                                        <td width="20">&nbsp;</td> 
                                                        <td>                                         
                                                            <table cellSpacing="1" cellPadding="1" width="100%">
                                                              <tr bgcolor="#ffffff">
                                                                <td align="left">
                                                                <strong><digi:trn>Amount</digi:trn></strong>
                                                                </td>
                                                                <td align="left">
                                                                <strong><digi:trn>Currency</digi:trn></strong>
                                                                </td>
                                                                <td align="left">
                                                                <strong><digi:trn>Planned Commitment Date</digi:trn></strong>
                                                                </td>
                                                              </tr>
                                                              <tr bgcolor="#ffffff">
                                                                <logic:notEmpty name="aimEditActivityForm" property="funding.proProjCost">
                                                                <field:display name="Proposed Project Amount" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="left" width="25%">
                                                                  <html:text name="aimEditActivityForm" property="funding.proProjCost.funAmount" styleId="funAmount" style="width:100px;"/>
                                                                </td>
                                                                </field:display>
                                                                <field:display name="Proposed Project Currency" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="left" width="25%">
                                                                    <html:select name="aimEditActivityForm" property="funding.proProjCost.currencyCode" styleClass="inp-text">
                                                                        <html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode" label="currencyName" style="width:100%;"/>
                                                                    </html:select>
                                                                </td>
                                                                </field:display>
                                                                <field:display name="Proposed Project Date" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="left" width="40%">
                                                                  <html:text name="aimEditActivityForm" property="funding.proProjCost.funDate" styleId="funDate" readonly="true" style="width:100px;vertical-align:middle;"/>
                                                                    <a id="date1" href='javascript:pickDateByIdDxDy("date1","funDate",210,30)'>
                                                                        <img src="/TEMPLATE/ampTemplate/imagesSource/calendar/show-calendar.gif" alt="Click to View Calendar" border=0>
                                                                    </a>
                                                                </td>
                                                                </field:display>
                                                                </logic:notEmpty>
                                                                <logic:empty name="aimEditActivityForm" property="funding.proProjCost">
                                                                <field:display name="Proposed Project Amount" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="left" width="25%">
                                                                  <html:text name="aimEditActivityForm" property="funding.proProjCost.funAmount" styleId="funAmount" style="width:100px;"/>
                                                                </td>
                                                                </field:display>
                                                                <field:display name="Proposed Project Currency" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="left" width="25%">
                                                                    <html:select name="aimEditActivityForm" property="funding.proProjCost.currencyCode" styleClass="inp-text">
                                                                        <html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode" label="currencyName" style="width:100%;"/>
                                                                    </html:select>
                                                                </td>
                                                                </field:display>
                                                                <field:display name="Proposed Project Date" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="left" width="40%">
                                                                  <html:text name="aimEditActivityForm" property="funding.proProjCost.funDate" styleId="funDate" readonly="true" style="width:100px;vertical-align:middle;"/>
                                                                    <a id="date1" href='javascript:pickDateByIdDxDy("date1","funDate",210,30)'>
                                                                        <img src="/TEMPLATE/ampTemplate/imagesSource/calendar/show-calendar.gif" alt="Click to View Calendar" border=0>
                                                                    </a>
                                                                </td>
                                                                </field:display>
                                                                </logic:empty>
                                                              </tr>
                                                            </table>
                                                        </td>
                                                      </tr>
                                                      <tr>
                                                        <td>
                                                        </td>
                                                      </tr>
                                                    </table>
                                                  </td>
                                                </tr>
                                              </table>
                                            </td>
                                          </tr>
                                          <tr>
                                            <td>&nbsp;
                                              
                                            </td>
                                          </tr>
                                        </table>
                                      </feature:display>
									  <feature:display name="Regional Funding" module="Funding">
                                        <table width="100%" bgcolor="#f4f4f2">
                                            <tr>
                                                <td class="separator1" title="<digi:trn key="aim:regionalFunding">Regional funding</digi:trn>">
                                                    &nbsp;&nbsp;<digi:trn key="aim:regionalFunding">Regional funding</digi:trn>
                                                </td>
                                            </tr>
                                            <tr><td>
                                            	<br />
                                                <table cellSpacing="2" cellPadding="2" width="100%">
                                                    <tr>
			                                            <td width="10"></td>
                                                        <td bgcolor="#bfd2df">
                                                        	<strong><digi:trn>Regions</digi:trn></strong>
                                                            <field:display name="Add Regional Fundings" feature="Regional Funding">
                                                            <a style="action_item" onclick="addRegionalFunding()">
                                                            <img src="/TEMPLATE/ampTemplate/imagesSource/common/add.gif" border="0" align="absmiddle" hspace="5" title="<digi:trn key="btn:addOrganizations">Add Organizations</digi:trn>" style="cursor:pointer" />
                                                            </a>
                                                            </field:display>
                                                        </td>
                                                    </tr>
                                                </table>
                                            	<br />
                                            </td></tr>
                                            <tr>
                                                <td align="left">
                                                    <table width="100%" cellSpacing=5 cellPadding=0>
                                                    <tr>
                                                    <td width="20">&nbsp;</td>
                                                    <td>
                                                    <logic:notEmpty name="aimEditActivityForm" property="funding.regionalFundings">
														<!-- L1 START-->
                                                        <logic:iterate indexId="counterRegions" name="aimEditActivityForm" property="funding.regionalFundings" id="regionalFunding" type="org.digijava.module.aim.helper.RegionalFunding"> 
                                                        <c:choose>
                                                            <c:when test="${counterRegions%2 == 0}"><c:set var="row_color_region">#ffffff</c:set></c:when>
                                                            <c:otherwise><c:set var="row_color_region">#dbe5f1</c:set></c:otherwise>
                                                        </c:choose>
														<table border="0" width="100%">
                                                        <tr><td bgcolor="${row_color_region}">
                                                            <img id="regional_funding_${regionalFunding.regionId}_plus"
                                                                onclick="toggleGroup('regional_funding_${regionalFunding.regionId}')"
                                                                src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_right.gif" /> <img
                                                                id="regional_funding_${regionalFunding.regionId}_minus" onclick="toggleGroup('regional_funding_${regionalFunding.regionId}')"
                                                                src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_down.gif"
                                                                style="display: none" />
                                                            <!-- Region name -->
                                                            <bean:write name="regionalFunding" property="regionName"/>
                                                            &nbsp;
                                                            <field:display name="Edit Funding Link" feature="Regional Funding">
                                                            <a class="action_item" href="javascript:editFunding('<bean:write name="regionalFunding" property="regionId"/>')"><digi:trn key="aim:editThisFunding">Edit</digi:trn></a>
                                                            </field:display>
                                                            <field:display name="Remove Fundings" feature="Regional Funding">
                                                            <a class="action_item" href="javascript:removeRegFundings('<bean:write name="regionalFunding" property="regionId"/>')"><digi:trn>Delete</digi:trn></a>
                                                            </field:display>
                                                        </td></tr>
                                                        <tr><td>
                                                            <!-- Regional funding details -->
                                                            <div id="regional_funding_${regionalFunding.regionId}_dots" style="display: block"></div>
                                                            <div id="act_regional_funding_${regionalFunding.regionId}"
                                                                style="display: ''; position: relative; left: 10px;">
                                                            <table width="98%" cellSpacing=1 cellPadding=3 border=0
                                                            bgcolor="#d7eafd">
                                                            <logic:notEmpty name="regionalFunding" property="commitments">
                                                                <tr bgcolor="#ffffff">
                                                                <td height="3"> </td></tr>
                                                                <tr bgcolor="#bfd2df">
                                                                <td><strong>Commitments</strong>
                                                                </td></tr>
                                                                <tr><td bgcolor=#ffffff>
                                                                    <table width="100%" cellSpacing=1 cellPadding=3 border=0>
                                                                        <tr bgcolor="#999999" style="color:black">
                                                                            <field:display name="Actual/Planned Commitments" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></strong></td></field:display>
                                                                            <field:display name="Total Amount Commitments" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:totalAmount">Total Amount</digi:trn></strong></td></field:display>
                                                                            <field:display name="Currency Commitments" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:currency">Currency</digi:trn></strong></td></field:display>
                                                                            <field:display name="Date Commitments" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:date">Date</digi:trn></strong></td></field:display>
                                                                        </tr>
                                                                        <logic:iterate name="regionalFunding"
                                                                        property="commitments" id="commitment"
                                                                        type="org.digijava.module.aim.helper.FundingDetail" indexId="counterRows">
                                                                        <!-- L2 START-->
                                                                        <c:choose>
                                                                            <c:when test="${counterRows%2 == 0}"><c:set var="row_color">#ffffff</c:set></c:when>
                                                                            <c:otherwise><c:set var="row_color">#dbe5f1</c:set></c:otherwise>
                                                                        </c:choose>
                                                                        <tr bgcolor="${row_color}">
                                                                            <field:display name="Actual/Planned Commitments" feature="Regional Funding">
                                                                                <td align="center"><digi:trn key="aim:${commitment.adjustmentTypeName}"><c:out value="${commitment.adjustmentTypeName}"/></digi:trn></td>
                                                                            </field:display>
                                                                            <field:display name="Total Amount Commitments" feature="Regional Funding"><td align="center">
                                                                            <FONT color=blue>*</FONT>
                                                                            <c:out value="${commitment.transactionAmount}"/></td></field:display>
                                                                            <field:display name="Currency Commitments" feature="Regional Funding"><td align="center"><c:out value="${commitment.currencyCode}"/></td></field:display>
                                                                            <field:display name="Date Commitments" feature="Regional Funding"><td align="center"><c:out value="${commitment.transactionDate}"/></td></field:display>																									
                                                                        </tr>
                                                                        </logic:iterate>	<!-- L2 END-->
                                                                    </table>
                                                                </td></tr>
                                                            </logic:notEmpty>
                                                            <logic:notEmpty name="regionalFunding" property="disbursements">
                                                                <tr bgcolor="#ffffff">
                                                                <td height="3"> </td></tr>
                                                                <tr bgcolor="#bfd2df">
                                                                <td><strong>Disbursements</strong>
                                                                </td></tr>
                                                                <tr><td bgcolor=#ffffff>
                                                                    <table width="100%" cellSpacing=1 cellPadding=3 border=0
                                                                    bgcolor="#eeeeee">
                                                                        <tr bgcolor="#999999" style="color:black">
                                                                            <field:display name="Actual/Planned Disbursements" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></strong></td></field:display>
                                                                            <field:display name="Total Amount Disbursements" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:totalAmount">Total Amount</digi:trn></strong></td></field:display>
                                                                            <field:display name="Currency Disbursements" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:currency">Currency</digi:trn></td></strong></field:display>
                                                                            <field:display name="Date Disbursements" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:date">Date</digi:trn></strong></td></field:display>																									
                                                                        </tr>
                                                                        <logic:iterate name="regionalFunding"
                                                                        property="disbursements" id="disbursement"
                                                                        type="org.digijava.module.aim.helper.FundingDetail" indexId="counterRows">
                                                                        <!-- L3 START-->
                                                                        <c:choose>
                                                                            <c:when test="${counterRows%2 == 0}"><c:set var="row_color">#ffffff</c:set></c:when>
                                                                            <c:otherwise><c:set var="row_color">#dbe5f1</c:set></c:otherwise>
                                                                        </c:choose>
                                                                        <tr bgcolor="${row_color}">
                                                                            <field:display name="Actual/Planned Disbursements" feature="Regional Funding"><td align="center"><digi:trn key="aim:${disbursement.adjustmentTypeName}"><c:out value="${disbursement.adjustmentTypeName}"/></digi:trn>
                                                                            </td></field:display>
                                                                            <field:display name="Total Amount Disbursements" feature="Regional Funding"><td align="center">
                                                                            <FONT color=blue>*</FONT>
                                                                            <c:out value="${disbursement.transactionAmount}"/>
                                                                            </td></field:display>
                                                                            <field:display name="Currency Disbursements" feature="Regional Funding"><td align="center"><c:out value="${disbursement.currencyCode}"/></td></field:display>
                                                                            <field:display name="Date Disbursements" feature="Regional Funding"><td align="center"><c:out value="${disbursement.transactionDate}"/></td></field:display>																									
                                                                        </tr>
                                                                        </logic:iterate>	<!-- L3 END-->
                                                                    </table>
                                                                </td></tr>
                                                            </logic:notEmpty>
                                                            <logic:notEmpty name="regionalFunding" property="expenditures">
																<tr bgcolor="#ffffff">
                                                                <td height="3"> </td></tr>
                                                                <tr bgcolor="#bfd2df">
                                                                <td ><strong>Expenditures</strong>
                                                                </td></tr>
                                                                <tr><td bgcolor=#ffffff>
                                                                    <table width="100%" cellSpacing=1 cellPadding=3 border=0
                                                                    bgcolor="#eeeeee">
                                                                        <tr bgcolor="#999999" style="color:black">
                                                                            <field:display name="Actual/Planned Expenditures" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></strong></td></field:display>
                                                                            <field:display name="Total Amount Expenditures" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:totalAmount">Total Amount</digi:trn></strong></td></field:display>
                                                                            <field:display name="Currency Expenditures" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:currency">Currency</digi:trn></strong></td></field:display>
                                                                            <field:display name="Date Expenditures" feature="Regional Funding"><td align="center"><strong><digi:trn key="aim:date">Date</digi:trn></strong></td></field:display>																									
                                                                        </tr>
                                                                        <logic:iterate name="regionalFunding"
                                                                        property="expenditures" id="expenditure"
                                                                        type="org.digijava.module.aim.helper.FundingDetail" indexId="counterRows">
                                                                        <!-- L4 START-->
                                                                        <c:choose>
                                                                            <c:when test="${counterRows%2 == 0}"><c:set var="row_color">#ffffff</c:set></c:when>
                                                                            <c:otherwise><c:set var="row_color">#dbe5f1</c:set></c:otherwise>
                                                                        </c:choose>
                                                                        <tr bgcolor="${row_color}">
                                                                            <field:display name="Actual/Planned Expenditures" feature="Regional Funding"><td align="center"><digi:trn key="aim:${expenditure.adjustmentTypeName}"><c:out value="${expenditure.adjustmentTypeName}"/></digi:trn>
                                                                            </td></field:display>
                                                                            <field:display name="Total Amount Expenditures" feature="Regional Funding"><td align="center">
                                                                            <FONT color=blue>*</FONT>
                                                                            <c:out value="${expenditure.transactionAmount}"/>
                                                                            </td></field:display>
                                                                            <field:display name="Currency Expenditures" feature="Regional Funding"><td align="center"><c:out value="${expenditure.currencyCode}"/></td></field:display>
                                                                            <field:display name="Date Expenditures" feature="Regional Funding"><td align="center"><c:out value="${expenditure.transactionDate}"/></td></field:display>																									
                                                                        </tr>
                                                                        </logic:iterate>	<!-- L4 END-->
                                                                    </table>
                                                                </td></tr>
                                                            </logic:notEmpty>
                                                            </table>
                                                            </div>
                                                        </td></tr>
														</table>
                                                        </logic:iterate>
														<!-- L1 END-->
                                                    </logic:notEmpty>
                                                    </td></tr>
                                                    <TR><TD colspan="2">
                                                        <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
                                                            <FONT color=blue>*
                                                                <digi:trn key="aim:allTheAmountsInThousands">
                                                                    All the amounts are in thousands (000)
                                                                </digi:trn>
                                                            </FONT>
                                                        </gs:test>
                                                     </TD></TR>
                                                    <logic:empty name="aimEditActivityForm" property="funding.fundingRegions">
                                                        <tr><td align="center" colspan="2">
                                                            <strong><digi:trn key="aim:noRegionsSelected">No regions selected</digi:trn></strong>
                                                        </td></tr>
                                                    </logic:empty>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                      </feature:display>
                                      <br />
                                      <feature:display name="Funding Information" module="Funding">
                                      
                                      <table width="100%" bgcolor="#f4f4f2" border="0" cellSpacing="0" cellPadding="0" >
                                        <tr>
                                          <td class="separator1">&nbsp;
											<digi:trn>Organization Funding</digi:trn>
										    <img src="/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" align="absmiddle" hspace="5" title="<digi:trn key="aim:FundingOrgs">The country or agency that financed the project</digi:trn>"/>
                                          </td>
                                        </tr>
                                        <tr>
                                            <td>
                                            	<br />
                                                <table cellSpacing="2" cellPadding="2" width="100%">
                                                    <tr>
			                                            <td width="10"></td>
                                                        <td bgcolor="#bfd2df">
                                                        	<strong><digi:trn>Organizations</digi:trn></strong>
                                                            <field:display name="Add Donor Organization" feature="Funding Information">
                                                            <aim:addOrganizationButton useLink="true" callBackFunction="doNothing();" delegateClass="org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate"  collection="fundingOrganizations" form="${aimEditActivityForm.funding}" refreshParentDocument="true" styleClass="dr-menu"><img src="/TEMPLATE/ampTemplate/imagesSource/common/add.gif" border="0" align="absmiddle" hspace="5" title="<digi:trn key="btn:addOrganizations">Add Organizations</digi:trn>" style="cursor:pointer" /></aim:addOrganizationButton>
                                                            </field:display>
                                                        </td>
                                                    </tr>
                                                </table>
                                            	<br />
                                            </td>
                                        </tr>
                                        <tr>
                                          <td>
<style>
.action_item {
	font-size: 10px;
	color:#006699;
	cursor:pointer;
}
A:hover.action_item {
	font-size: 10px;
	color:black;
	cursor:pointer;
}
A:link.action_item {
	font-size: 10px;
	color:#006699;
	cursor:pointer;
	text-decoration:none;
}
</style>
                                            <table width="95%" cellSpacing="1" cellPadding="0" border="0" align="center">
                                              <tr>
                                                <td width="20">&nbsp;</td>
                                                <td>
                                                  <table cellSpacing="8" cellPadding="0" border="0" width="100%">
                                                    <logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
                                                      <logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.module.aim.helper.FundingOrganization" indexId="counterOrganizations">
                                                        <c:choose>
                                                            <c:when test="${counterOrganizations%2 == 0}"><c:set var="row_color_org">#ffffff</c:set></c:when>
                                                            <c:otherwise><c:set var="row_color_org">#dbe5f1</c:set></c:otherwise>
                                                        </c:choose>
                                                        <tr>
                                                          <td>
	                                                          	<table width="100%" bgcolor="${row_color_org}" height="20">
	                                                        	  <tr>
		                                                          		<td>
<!--		                                                          			<field:display name="Organizations Selector" feature="Funding Information">
				                                                            	<html:multibox property="funding.selFundingOrgs" styleId="selFundingOrgs">
					                                                          		<bean:write name="fundingOrganization" property="ampOrgId"/>
					                                                        	</html:multibox>
					                                                        </field:display>-->
                                                                            <img id="group_funding_${fundingOrganization.ampOrgId}_plus"
                                                                                onclick="toggleGroup('group_funding_${fundingOrganization.ampOrgId}')"
                                                                                src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_right.gif" /> <img
                                                                                id="group_funding_${fundingOrganization.ampOrgId}_minus" onclick="toggleGroup('group_funding_${fundingOrganization.ampOrgId}')"
                                                                                src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_down.gif"
                                                                                style="display: none" />
			                                                            	<span style="font-size:8pt"><bean:write name="fundingOrganization" property="orgName"/></span>
				                                                            <field:display name="Organizations Selector" feature="Funding Information">
																				<aim:addOrganizationButton useLink="true" callBackFunction="doNothing();" aditionalRequestParameters="id=${fundingOrganization.ampOrgId}"  delegateClass="org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate"  property="fundingOrganizations"  form="${aimEditActivityForm.funding}" refreshParentDocument="true" styleClass="action_item"> <digi:trn>Edit</digi:trn> </aim:addOrganizationButton>&nbsp;|&nbsp;
																			</field:display>
                                                                            <field:display name="Remove Donor Organization" feature="Funding Information">
                                                                            	<a class="action_item" onclick="removeOrganisation(<bean:write name="fundingOrganization" property="ampOrgId"/>)"><digi:trn>Delete Organization</digi:trn></a>&nbsp;|&nbsp;
                                                                            </field:display>
																		<field:display name="Add Donor Funding Button" feature="Funding Information">
																			<a class="action_item" onclick="addFunding('<bean:write name="fundingOrganization" property="ampOrgId"/>')" /><digi:trn key="btn:addFunding">Add Funding</digi:trn></a>&nbsp;|&nbsp;
																		</field:display>
																		<field:display name="Import Donor Funding Button" feature="Funding Information">
																			<a class="action_item"  onclick="importFunding('<bean:write name="fundingOrganization" property="ampOrgId"/>')" /><digi:trn key="btn:importFunding">Import Funding</digi:trn></a>
																		</field:display>
															        	</td>
                                                                </tr>
                                                                </table>
                                                          </td>
                                                        </tr>
                                                        <tr>
                                                          <td>
                                                        <div id="group_funding_${fundingOrganization.ampOrgId}_dots" style="display: block"></div>
                                                        </b> <br />
                                                        <div id="act_group_funding_${fundingOrganization.ampOrgId}"
                                                            style="display: none; position: relative; left: 10px;">
                                                        <table width="100%">
                                                        <tr>
                                                            <field:display name="Active Funding Organization" feature="Funding Information">
                                                            <td valign="middle">
                                                                <html:select property="fundingActive" indexed="true" name="fundingOrganization">
                                                                    <html:option value="true">Active</html:option>
                                                                    <html:option value="false">Inactive</html:option>
                                                                </html:select>
                                                            </td>
                                                            </field:display>
                                                            <field:display name="Delegated Cooperation" feature="Funding Information">
                                                                <td valign="middle">
                                                                    <html:checkbox name="fundingOrganization" property="delegatedCooperation" indexed="true" onclick="delegatedCooperationClick(this.name);" style="vertical-align: middle;"/>
                                                                    <digi:trn key="aim:DelegatedCooperation">Delegated Cooperation</digi:trn>
                                                                    <html:hidden name="fundingOrganization" property="delegatedCooperationString" indexed="true"/>
                                                                </td>
                                                            </field:display>
                                                            <field:display name="Delegated Partner" feature="Funding Information">
                                                                <td valign="middle">
                                                                    <html:checkbox property="delegatedPartner" indexed="true" name="fundingOrganization" onclick="indexedCheckboxClick(this.name);" style="vertical-align: middle;"/>
                                                                    <digi:trn key="aim:DelegatedPartner">Delegated Partner</digi:trn>
                                                                    <html:hidden name="fundingOrganization" property="delegatedPartnerString" indexed="true"/>
                                                                </td>
                                                            </field:display>
                                                        </tr>
                                                     </table>
                                                        <logic:notEmpty name="fundingOrganization" property="fundings">
                                                        <table width="100%">
                                                          <logic:iterate name="fundingOrganization"  indexId="index" property="fundings" id="funding" type="org.digijava.module.aim.helper.Funding">
                                                            <tr>
                                                              <td>
                                                                <table cellSpacing="1" cellPadding="0" width="100%" style="border: 1px solid black;">
                                                                  <tr>
                                                                    <td>
                                                                            <table width="100%" cellpadding="1" bgcolor="#ffffff" cellspacing="1">
                                                                              <tr>
																				<field:display name="Funding Organization Id" feature="Funding Information">
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                    <a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">
	                                                                                    <digi:trn>Organization Id</digi:trn>
                                                                                    </a>
                                                                                </td>
                                                                                <td bgcolor="#FFFFFF" align="left">
	                                                                                <input type="text" class="dr-menu" disabled="disabled" value="<bean:write name='funding' property='orgFundingId'/>"/>
                                                                                </td>
																				</field:display>
                                                                              <!-- type of assistance -->
                                                                              	<field:display name="Type Of Assistance" feature="Funding Information">
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
                                                                                  <digi:trn key="aim:typeOfAssist">
                                                                                    Type of Assistance </digi:trn>
																					</a>
                                                                                </td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="typeOfAssistance">
                                                                                  	<input type="text" class="dr-menu" disabled="disabled"  value='<category:getoptionvalue categoryValueId="${funding.typeOfAssistance.id}" />'/>
                                                                                  </logic:notEmpty>
                                                                                </td>
                                                                                </field:display>
                                                                              </tr>


                                                                              <tr>
																				<field:display name="Financing Instrument" feature="Funding Information">
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <a title="<digi:trn key="aim:Financing">Method by which aid is delivered to an activity</digi:trn>">
                                                                                  <digi:trn key="aim:financingInstrument">Financing Instrument</digi:trn>
																				  </a>
                                                                                </td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="financingInstrument">
                                                                                  	<input type="text" class="dr-menu" disabled="disabled" value='<category:getoptionvalue categoryValueId="${funding.financingInstrument.id}"/>'/>
                                                                                  </logic:notEmpty>
                                                                                </td>
                                                                              	</field:display>
                                                                              	<field:display name="Funding Status" feature="Funding Information">
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <a title="<digi:trn key="aim:Financing">Method by which aid is delivered to an activity</digi:trn>">
                                                                                  <digi:trn>Funding Status</digi:trn>
																				</a>
                                                                                </td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="fundingStatus">
                                                                                  	<input type="text" class="dr-menu" disabled="disabled" value='<category:getoptionvalue categoryValueId="${funding.fundingStatus.id}"/>'/>
                                                                                  </logic:notEmpty>
                                                                                </td>
																				</field:display>
        	                                                                     </tr>

                                                                              <tr>
																				<field:display name="Conditions for Fund Release" feature="Funding Information">
                                                                                <td bgcolor="#FFFFFF" align="left" width="150" valign="top">
                                                                                  <a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">		 
                                                                                  	<digi:trn key="aim:conditions"> Conditions
                                                                                  	</digi:trn>
																				  </a>
                                                                                </td>
                                                                                <td bgcolor="#FFFFFF" align="left" colspan="3">
                                                                                  <textarea style="width:100%" disabled="disabled"><bean:write name="funding" property="conditions"/></textarea>
                                                                                </td>
	                                                                            </field:display>
                                                                                </tr>
                                                                                <tr>
	                                                                            <field:display name="Donor Objective" feature="Funding Information">
                                                                                <td bgcolor="#FFFFFF" align="left" valign="top">
                                                                                  <a title="<digi:trn key="aim:DonorObjectiveforFundRelease">Enter the donor objective attached to the release of the funds</digi:trn>"><digi:trn key="aim:donorobjective">Donor Objective</digi:trn>
																				</a>
                                                                                </td>
                                                                                <td bgcolor="#FFFFFF" align="left" colspan="3">
                                                                                  <textarea style="width:100%" disabled="disabled"><bean:write name="funding"	property="donorObjective"/></textarea>
                                                                                </td>
	                                                                            </field:display>
                                                                              </tr>
                                                                            </table>
                                   									 </td>
                                                                  </tr>
                                                                  <tr>
                                                                    <td>
                                                                      <table width="98%" border="0" cellpadding="1" bgcolor="#ffffff" cellspacing="1">
                                                                        <tr>
                                                                          <td bgcolor="#FFFFFF" align="right" colspan="2">
                                                                            <table width="100%" border="0" cellSpacing="1" cellPadding="1" >
                                                                              <%-- Rendering projections --%>
                                                                              	<feature:display module="Funding" name="MTEF Projections">
                                                                                        <tr bgcolor="#bfd2df">
	                                                                                 <td colspan="4">
	                                                                                 <b>
                                                                                     <a>
                                                                                     <digi:trn key="aim:funding:projections">Projections</digi:trn>
                                                                                     </a>
                                                                                     </b>
	                                                                                 </td>
	                                                                              	</tr>
	                                                                              	<tr bgcolor="#999999" style="color:black;">
	                                                                                 	<field:display name="Projection Name" feature="MTEF Projections"></field:display>
	                                                                                 	<td align="center">
	                                                                                 		<strong><digi:trn>Actual/Planned</digi:trn></strong>
	                                                                                 	</td>
	                                                                                 	<field:display name="Projection Amount" feature="MTEF Projections"></field:display>
	                                                                                 	<td align="center">
                                                                                        	<strong><digi:trn>Amount</digi:trn></strong>
	                                                                                 	</td>
	                                                                                 	<field:display name="Projection Currency Code" feature="MTEF Projections"></field:display>
	                                                                                 	<td align="center">
	                                                                                 		<strong><digi:trn>Currency</digi:trn></strong>
	                                                                                 	</td>
	                                                                                 	<field:display name="Projection Date" feature	="MTEF Projections"></field:display>
	                                                                                 	<td align="center">
                                                                                        <strong><digi:trn>Date</digi:trn></strong>
	                                                                                 	</td>
	                                                                              	</tr>
	                                                                                 <logic:notEmpty name="funding" property="mtefProjections">
	                                                                                 <logic:iterate name="funding" property="mtefProjections" id="projection" indexId="counter">
                                                                                        <c:choose>
                                                                                            <c:when test="${counter%2 == 0}"><c:set var="row_color">#ffffff</c:set></c:when>
	                                                                                        <c:otherwise><c:set var="row_color">#dbe5f1</c:set></c:otherwise>
                                                                                        </c:choose>
	                                                                                 <tr bgcolor="${row_color}">
	                                                                                 	<field:display name="Projection Name" feature="MTEF Projections"></field:display>
	                                                                                 	<td align="center">
	                                                                                 		<category:getoptionvalue categoryValueId="${projection.projected}" />
	                                                                                 	</td>
	                                                                                 	<field:display name="Projection Amount" feature="MTEF Projections"></field:display>
	                                                                                 	<td align="center">
	                                                                                 		<bean:write name="projection" property="amount" />
	                                                                                 	</td>
	                                                                                 	<field:display name="Projection Currency Code" feature="MTEF Projections"></field:display>
	                                                                                 	<td align="center">
	                                                                                 		<bean:write name="projection" property="currencyCode" />
	                                                                                 	</td>
	                                                                                 	<field:display name="Projection Date" feature	="MTEF Projections"></field:display>
	                                                                                 	<td align="center">
	                                                                                 		<bean:write name="projection" property="projectionDateLabel" />
	                                                                                 	</td>
	                                                                                 </tr>
	                                                                                 </logic:iterate>
	                                                                                 </logic:notEmpty>
																				</feature:display>
																			  <%-- Rendering projections --%>
                                                                       	<feature:display module="Funding" name="Commitments"> 
																			<tr bgcolor="#ffffff">
                                                                                 <td colspan="4">&nbsp;
                                                                                </td>
                                                                              </tr>
																			<tr bgcolor="#bfd2df">
                                                                                 <td colspan="4">
                                                                                	<b>
                                                                                  	<a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>" >
                                                                                  		<digi:trn key="aim:commitments">			Commitments </digi:trn>
																					</a></b>
                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.commitmentsDetails}">
                                                                                  <tr bgcolor="#999999" style="color:black;">
                                                                                    <field:display name="Adjustment Type Commitment" feature="Commitments">
                                                                                    <td align="center">
                                                                                    <strong><digi:trn>Actual/Planned</digi:trn></strong>
                                                                                    </td>
																					</field:display>
                                                                                    <field:display name="Amount Commitment" feature="Commitments">
                                                                                    <td align="center">
                                                                                    <strong><digi:trn>Amount</digi:trn></strong>
                                                                                    </td>
																					</field:display>
                                                                                    <field:display name="Currency Commitment" feature="Commitments">
                                                                                    <td align="center">
                                                                                    <strong><digi:trn>Currency</digi:trn></strong>
                                                                                    </td>
																					</field:display>
                                                                                    <field:display name="Date Commitment" feature="Commitments">
                                                                                    <td align="center">
                                                                                    <strong><digi:trn>Date</digi:trn></strong>
                                                                                    </td>
																					</field:display>
                                                                                  </tr>
                                                                              <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail" indexId="counter">
                                                                                <logic:equal name="fundingDetail" property="transactionType" value="0">
                                                                                        <c:choose>
                                                                                            <c:when test="${counter%2 == 0}"><c:set var="row_color">#ffffff</c:set></c:when>
                                                                                            <c:otherwise><c:set var="row_color">#dbe5f1</c:set></c:otherwise>
                                                                                        </c:choose>
                                                                                      <tr bgcolor="${row_color}">
                                                                                        <td align="center">
                                                                                        <field:display name="Adjustment Type Commitment" feature="Commitments"></field:display>
                                                                                          <digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																							</digi:trn>
                                                                                        </td>
                                                                                        <td align="center">
                                                                                        <field:display name="Amount Commitment" feature="Commitments"></field:display>
                                                                                          <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
                                                                                        </td>
                                                                                        <td align="center">
                                                                                        <field:display name="Currency Commitment" feature="Commitments"></field:display>
                                                                                          <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                        </td>
                                                                                        <td align="center">
                                                                                       	 	<field:display name="Date Commitment" feature="Commitments"></field:display>
                                                                                          		<bean:write name="fundingDetail" property="transactionDate"/>
                                                                                        </td>
                                                                                      </tr>


                                                                                </logic:equal>
                                                                              </logic:iterate>
                                                                              </c:if>
																			</feature:display>

                                                                              <!--Disbursement order-->
                                                                               <feature:display module="Funding" name="Disbursement Orders">
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">&nbsp;</td>
                                                                              </tr>
                                                                              <tr bgcolor="#bfd2df">
                                                                                 <td colspan="5">
                                                                                	<b>
                                                                                  	<a title="<digi:trn key="aim:disbursementOrdersMade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>" >
                                                                                  		<digi:trn key="aim:disbursementOrders">	Disbursement Orders </digi:trn>
																					</a></b>
                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.disbursementOrdersDetails}">
                                                                                <tr bgcolor="#999999">
                                                                                    <td align="center">
                                                                                    <field:display name="Adjustment Type of Disbursement Order" feature="Disbursement Orders"><strong><digi:trn>Actual/Planned</digi:trn></strong></field:display>
                                                                                    </td>
                                                                                    <td align="center">
                                                                                    <field:display name="Amount of Disbursement Order" feature="Disbursement Orders"><strong><digi:trn>Amount</digi:trn></strong></field:display>
                                                                                    </td>
                                                                                    <td align="center">
                                                                                    <field:display name="Currency of Disbursement Order" feature="Disbursement Orders"><strong><digi:trn>Currency</digi:trn></strong></field:display>
                                                                                    </td>
                                                                                    <td align="center">
                                                                                    <field:display name="Date of Disbursement Order" feature="Disbursement Orders"><strong><digi:trn>Date</digi:trn></strong></field:display>
                                                                                    </td>
                                                                                    <td align="center">
                                                                                    <field:display name="Contract of Disbursement Order" feature="Disbursement Orders"><strong><digi:trn>Contract</digi:trn></strong></field:display>
                                                                                    </td>
                                                                                </tr>
                                                                                <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail" indexId="counter">
                                                                                <logic:equal name="fundingDetail" property="transactionType" value="4">
                                                                                    <c:choose>
                                                                                        <c:when test="${counter%2 == 0}"><c:set var="row_color">#ffffff</c:set></c:when>
                                                                                        <c:otherwise><c:set var="row_color">#dbe5f1</c:set></c:otherwise>
                                                                                    </c:choose>


                                                                                      <tr bgcolor="${row_color}">


                                                                                    <td align="center">
	                                                                                    <field:display name="Adjustment Type of Disbursement Order" feature="Disbursement Orders"></field:display>
    	                                                                                	<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
                                                                                                <bean:write name="fundingDetail" property="adjustmentTypeName"/>
                                                                                                </digi:trn>
                                                                                    </td>


                                                                                    <td align="center">
                                                                                      <field:display name="Amount of Disbursement Order" feature="Disbursement Orders"></field:display>
                                                                                      	<FONT color="blue">*</FONT>
                                                                                      	<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
                                                                                    </td>

                                                                                    <td align="center">
	                                                                                    <field:display name="Currency of Disbursement Order" feature="Disbursement Orders"></field:display>
    	                                                                                  <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                    </td>
                                                                                    <td align="center">
                                                                                    	<field:display name="Date of Disbursement Order" feature="Disbursement Orders"></field:display>
		                                                                                      <bean:write name="fundingDetail" property="transactionDate"/>
                                                                                    </td>
                                                                                      <td align="center">
                                                                                          <field:display name="Contract of Disbursement Order" feature="Disbursement Orders"></field:display>
                                                                                              <c:if test="${not empty fundingDetail.contract}">
		                                                                                     ${fundingDetail.contract.contractName}
                                                                                                   </c:if>
                                                                                      </td>

                                                                                      </tr>

                                                                                </logic:equal>
                                                                              </logic:iterate>
                                                                              </c:if>
                                                                            </feature:display>
																			<feature:display module="Funding" name="Disbursement">
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">&nbsp;</td>
                                                                              </tr>
                                                                              <tr bgcolor="#bfd2df">
                                                                                <td colspan="5">
                                                                                  <a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>">
                                                                                  <b> <digi:trn key="aim:disbursements">Disbursements </digi:trn></b>
																				</a>
                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.disbursementsDetails}">
                                                                                    <tr bgcolor="#999999">
                                                                                    <td align="center">
                                                                                            <field:display name="Adjustment Type Disbursement" feature="Disbursement"><strong><digi:trn>Actual/Planned</digi:trn></strong></field:display>
                                                                                        </td>
                                                                                        <td align="center">
                                                                                            <field:display name="Amount Disbursement" feature="Disbursement"><strong><digi:trn>Amount</digi:trn></strong></field:display>
                                                                                        </td>
                                                                                        <td align="center">
                                                                                            <field:display name="Currency Disbursement" feature="Disbursement"><strong><digi:trn>Currency</digi:trn></strong></field:display>
                                                                                        </td>
                                                                                        <td align="center">
                                                                                            <field:display name="Date Disbursement" feature="Disbursement"><strong><digi:trn>Date</digi:trn></strong></field:display>
                                                                                        </td>
                                                                                        <td align="center">
	                                                                                        <field:display name="Contract of Disbursement Order" feature="Disbursement Orders"><strong><digi:trn>Contract</digi:trn></strong></field:display>
                                                                                        </td>																							
                                                                                    </tr>
	                                                                              <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail" indexId="counter">
		                                                                              <logic:equal name="fundingDetail" property="transactionType" value="1">		
                                                                                        <c:choose>
                                                                                            <c:when test="${counter%2 == 0}"><c:set var="row_color">#ffffff</c:set></c:when>
                                                                                            <c:otherwise><c:set var="row_color">#dbe5f1</c:set></c:otherwise>
                                                                                        </c:choose>
																									<tr bgcolor="${row_color}">
																										<td align="center">
																											<field:display name="Adjustment Type Disbursement" feature="Disbursement"></field:display>
																												<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																													<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																												</digi:trn>
																										</td>
																										<td align="center">
																											<field:display name="Amount Disbursement" feature="Disbursement"></field:display>
																												<FONT color="blue">*</FONT>
																												<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																										</td>
																										<td align="center">
																											<field:display name="Currency Disbursement" feature="Disbursement"></field:display>
																												<bean:write name="fundingDetail" property="currencyCode"/>
																										</td>
																										<td align="center">
																											<field:display name="Date Disbursement" feature="Disbursement"></field:display>
																												<bean:write name="fundingDetail" property="transactionDate"/>
																										</td>
																										<td align="center">
																										
																										<field:display name="Contract of Disbursement Order" feature="Disbursement Orders"></field:display>
			                                                                                             	 <c:if test="${not empty fundingDetail.contract}">
					                                                                                     		${fundingDetail.contract.contractName}
			                                                                                                   </c:if>
																										</td>																							
																									</tr>
																						</logic:equal>
																					</logic:iterate>
                                                                                 </c:if>
                                                                                 </feature:display>
                                                                                        
																						<tr bgcolor="#ffffff">
																							<td colspan="5">&nbsp;</td>
																						</tr>
																				<feature:display module="Funding" name="Expenditures">
																						<tr bgcolor="#bfd2df">
																							<td colspan="5">
																							<a title="<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>">	<b><digi:trn key="aim:expenditures"> Expenditures </digi:trn></b>
																							</a>
																							</td>
																						</tr>
                                                                                        <c:if test="${!empty funding.expendituresDetails}">
                                                                                        <tr bgcolor="#999999">
                                                                                            <td align="center">
                                                                                            <field:display name="Adjustment Type Expenditure" feature="Expenditures"><strong><digi:trn>Actual/Planned</digi:trn></strong></field:display>
                                                                                            </td>
                                                                                            <td align="center">
                                                                                                <field:display name="Amount Expenditure" feature="Expenditures"><strong><digi:trn>Amount</digi:trn></strong></field:display>
                                                                                            </td>
                                                                                            <td align="center">
                                                                                                <field:display name="Currency Expenditure" feature="Expenditures"><strong><digi:trn>Currency</digi:trn></strong></field:display>
                                                                                            </td>
                                                                                            <td align="center">
                                                                                                <field:display name="Date Expenditure" feature="Expenditures"><strong><digi:trn>Date</digi:trn></strong></field:display>
                                                                                            </td>
                                                                                        </tr>
																						<logic:iterate name="funding" property="fundingDetails"
																						id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail" indexId="counter">
																						<logic:equal name="fundingDetail" property="transactionType" value="2">																						
                                                                                            <c:choose>
                                                                                                <c:when test="${counter%2 == 0}"><c:set var="row_color">#ffffff</c:set></c:when>
                                                                                                <c:otherwise><c:set var="row_color">#dbe5f1</c:set></c:otherwise>
                                                                                            </c:choose>
																								<tr bgcolor="${row_color}">
																									<td align="center">
																									<field:display name="Adjustment Type Expenditure" feature="Expenditures"></field:display>
																										<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																											<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																										</digi:trn>
																									</td>
																									<td align="center">
																										<field:display name="Amount Expenditure" feature="Expenditures"></field:display>
																											<FONT color="blue">*</FONT>
																											<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																									</td>
																									<td align="center">
																										<field:display name="Currency Expenditure" feature="Expenditures"></field:display>
																											<bean:write name="fundingDetail" property="currencyCode"/>
																									</td>
																									<td align="center">
																										<field:display name="Date Expenditure" feature="Expenditures"></field:display>
																											<bean:write name="fundingDetail" property="transactionDate"/>
																									</td>
																								</tr>
																								<tr>
																									<td colspan="5" bgcolor="#ffffff">&nbsp;&nbsp;
																										<field:display name="Classification Expenditure" feature="Funding Information"></field:display>
																											<bean:write name="fundingDetail" property="classification"/>
																									</td>
																								</tr>																						
																						</logic:equal>
																						</logic:iterate>
                                                                                        </c:if>
                                                                                       </feature:display>
																					</table>
																				</td>
																			</tr>
																		</table>

																	</td></tr>
																	<tr><td>
																		<table border="0" cellpadding="8"
																		bgcolor="#ffffff" cellspacing="1">
																		<tr>
																			<td>
																				<field:display name="Edit Funding Link - Donor Organization" feature="Funding Information">
																					<a class="action_item" href='javascript:fnOnEditItem(<%= index %>,
																									 <bean:write name="fundingOrganization" property="ampOrgId"/>,
																									<bean:write name="funding" property="fundingId"/>)'>
																					<digi:trn key="aim:editFundingItem">Edit Item</digi:trn>
																					</a>
																				</field:display>
																			</td>
																			<td>
																				<field:display name="Delete Funding Link - Donor Organization" feature="Funding Information">
																					<a class="action_item" href='javascript:fnOnDeleteItem(<bean:write name="fundingOrganization"
																										 property="ampOrgId"/>,<bean:write name="funding"
																										 property="fundingId"/>)'>
																					<digi:trn key="aim:deleteFundingItem">Delete Item</digi:trn>
																					</a>
																				</field:display>
																			</td>
																		</tr>
																		</table>
																	</td></tr>

																	<tr>
																		<td bgcolor="#ffffff">
																			<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
																					<FONT color="blue">*
																						<digi:trn key="aim:theAmountEnteredAreInThousands">
																							The amount entered are in thousands (000)
					  																	</digi:trn>
																					</FONT>
																			</gs:test>
																		</td>
																	</tr>
																	</table>
																	</td></tr>
																	</logic:iterate>
                                                        </table>
														</logic:notEmpty>
														</div>
                                                          </td>
                                                        </tr>
																</logic:iterate>
																<tr><td>&nbsp;</td></tr>

																</logic:notEmpty>
															</table>
															
														</td>
											  </tr>
                                            </table>
										  </td>
										</tr>
										<tr><td>&nbsp;</td></tr>
                                      </table>
                                      </feature:display>
                                    </td>
                                  </tr>
                                </table>
                                      <!-- end contents -->
</td>
                                                                    </tr>

                                </table>
</td>
                                                                                    </tr>
                          </table>

</td>
						<td width="25%" vAlign="top" align="right">
						  <!-- edit activity form menu -->

						    <jsp:include page="editActivityMenu.jsp" flush="true" />

						  <!-- end of activity form menu -->
						</td></tr>
                    </table>
				</td></tr>
				<tr><td>&nbsp;
					
				</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>

</table>
</digi:form>

