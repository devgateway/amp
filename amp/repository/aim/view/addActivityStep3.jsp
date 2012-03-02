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
<%@ taglib uri="/taglib/gateperm" prefix="gateperm" %>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>
<script type="text/javascript" src='<digi:file src="module/aim/scripts/panel/connection-min.js"/>' ></script>
<jsp:include page="addActivityStep3PopinImport.jsp" flush="true" />
<script language="JavaScript">
	function  doNothing()
	{
		
	}
</script>

<script language="JavaScript">

	<!--

	function validate() {
	
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
			openNewRsWindow(900, 500);
			<digi:context name="addFunding" property="context/module/moduleinstance/addFunding.do" />
			document.getElementById('orgId').value = orgId;
			document.aimEditActivityForm.action = "<%= addFunding %>?orgId" + orgId+"&edit=true";
			document.aimEditActivityForm.prevOrg.value = orgId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}

    function addPropFunding() {
            openNewWindow(600, 180);
            <digi:context name="addProposedFunding" property="context/module/moduleinstance/editProposedFunding.do" />
            document.aimEditActivityForm.action = "<%= addProposedFunding %>";
            document.aimEditActivityForm.target = popupPointer.name;
            document.aimEditActivityForm.submit();
	}

    function delPropFunding() {
            <digi:context name="delProposedFunding" property="context/module/moduleinstance/removeProposedFunding.do" />
            var state=window.confirm("<digi:trn key="aim:deleteProposedProjectCost">Are you sure about deleting the Proposed Project Cost ?</digi:trn>");
            if(state==false){
              return false;
            }
            document.aimEditActivityForm.action = "<%= delProposedFunding %>";
            document.aimEditActivityForm.submit();
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

	function fnOnEditItem(index, orgId,fundId)	{
			openNewWindow(990, 500);
			<digi:context name="editItem" property="context/module/moduleinstance/editFunding.do"/>
			document.aimEditActivityForm.action = "<%= editItem %>?funding.orgId=" + orgId + "&funding.offset=" + index+"&edit=true";
			document.aimEditActivityForm.prevOrg.value = orgId;
			document.getElementById('fundingId').value = fundId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}

	function fnOnDeleteItem(orgId,fundId)	{
		var msg	= "<digi:trn>Are you sure you want to remove the funding item ?</digi:trn>";
		if ( !confirm(msg) ) return;
		<digi:context name="remItem" property="context/module/moduleinstance/removeFunding.do"/>
		document.aimEditActivityForm.action = "<%= remItem %>?fundOrgId=" + orgId + "&fundId=" + fundId+"&edit=true";
		document.aimEditActivityForm.target = "_self";
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

	function removeSelOrganisations() {
		var flag = validate();
		if (flag == false) return false;
		<digi:context name="remSelOrg" property="context/module/moduleinstance/remSelFundOrgs.do?edit=true" />
		document.aimEditActivityForm.action = "<%= remSelOrg %>";
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


	function importFunding(orgId) {
		myImportFunding(orgId);
	}

	-->
</script>

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
<jsp:include page="teamPagesHeader.jsp"  />
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
								<span class="crumb">
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
                            <digi:trn key="aim:editActivity">
                            Edit Activity
                            </digi:trn>:
                            <bean:write name="aimEditActivityForm" property="identification.title"/>
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
                              <td width="100%">
                                <table cellPadding="0" cellSpacing="0" width="100%" border="0">
                                  <tr>
                                    <td width="13" height="20" background="module/aim/images/left-side.gif">
                                    </td>
                                    <td vAlign="middle" align ="center" class="textalb" height="20" bgcolor="#006699">
                             
                                    	<digi:trn>
													Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn> ${fn:length(aimEditActivityForm.steps)}:
                                      <digi:trn key="aim:activity:Funding">
                                          Funding
                                      </digi:trn>
                                    </td>
                                    <td width="13" height="20" background="module/aim/images/right-side.gif">
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                            <tr>
                              <td bgColor="#f4f4f2" align="center" vAlign="top" width="100%">
                                <table width="100%" cellSpacing="0" vAlign="top" align="left" bgcolor="#006699">
                                  <tr>
                                    <td>
                                    	<bean:define name="aimEditActivityForm" id="myForm" type="org.digijava.module.aim.form.EditActivityForm"/>
                             	  	<feature:display name="Proposed Project Cost" module="Funding">
                                        <table width="100%" bgcolor="#f4f4f2" border="0" cellSpacing="0" cellPadding="0">
                                          <tr>
                                            <td>
                                            <br />
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                              <IMG alt="Link" height="10" src="../ampTemplate/images/arrow-014E86.gif" width="15" />
                                              <a title="<digi:trn key="aim:ProposedProjCost">Proposed Project Cost</digi:trn>">
                                              <b>
                                                <digi:trn key="aim:proposedPrjectCost">Proposed Project Cost</digi:trn>
                                              </b>
											</a><br /><br />
                                            </td>
                                          </tr>
                                          <tr>
                                            <td align="left">
                                            <table width="100%" cellSpacing="0" cellPadding="0" border="0">
                                                <tr>
                                                  <td>
                                                    <table cellSpacing="8" cellPadding="0" border="0" width="95%" class="box-border-nopadding" align="center">
                                                      <tr>
                                                        <td>                                         
                                                                                                                                                                                                                                 
                                                          <c:if test="${not empty aimEditActivityForm.funding.proProjCost && aimEditActivityForm.funding.proProjCost!=''}">
                                                            <table cellSpacing="1" cellPadding="1" width="100%">
                                                              <tr bgcolor="#ffffff">
                                                              	<field:display name="Proposed Project Planned" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="center" width="10%">
                                                                <digi:trn key="aim:AvtivityFundingPlanned">
                                                  					Planned
                                                  				</digi:trn>
                                                                </td>
                                                                </field:display>
                                                                <field:display name="Proposed Project Amount" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="left" width="25%">
                                                                  <c:if test="${not empty aimEditActivityForm.funding.proProjCost.funAmount && aimEditActivityForm.funding.proProjCost.funAmount!=''}">                                                                         
                                                                  ${aimEditActivityForm.funding.proProjCost.funAmountFormated}
                                                                  </c:if>
                                                                </td>
                                                                </field:display>
                                                                <field:display name="Proposed Project Currency" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="left" width="25%">
                                                                  <c:if test="${not empty aimEditActivityForm.funding.proProjCost.currencyCode && aimEditActivityForm.funding.proProjCost.currencyCode!=''}">
                                                                  ${aimEditActivityForm.funding.proProjCost.currencyCode}
                                                                  </c:if>
                                                                </td>
                                                                </field:display>
                                                                <field:display name="Proposed Project Date" feature="Proposed Project Cost">
                                                                <td bgcolor="#FFFFFF" align="left" width="40%">
                                                                  <c:if test="${not empty aimEditActivityForm.funding.proProjCost.funDate && aimEditActivityForm.funding.proProjCost.funDate!=''}">
                                                                  ${aimEditActivityForm.funding.proProjCost.funDate}
                                                                  </c:if>
                                                                </td>
                                                                </field:display>
                                                              </tr>
                                                            </table>
                                                          </c:if>
                                                         
                                                        </td>
                                                      </tr>
                                                      <tr>
                                                        <td>

                                                          <c:if test="${aimEditActivityForm.funding.proProjCost==null}">
                                                          	<c:set var="translation">
                                                            	<digi:trn key="btn:addFundings">Add Fundings</digi:trn>
                                                            </c:set>
                                                           <field:display name="Add Funding Button - Proposed Project Cost" feature="Proposed Project Cost">
                                                            <input type="button" value="${translation}" class="dr-menu" onclick="addPropFunding()">
                                                           </field:display>
                                                          </c:if>

                                                          <c:if test="${aimEditActivityForm.funding.proProjCost!=null}">
                                                          	<c:set var="translation">
                                                            	<digi:trn key="btn:editFundings">Edit Funding</digi:trn>
                                                            </c:set>
                                                            <field:display name="Edit Funding Button- Proposed Project Cost" feature="Proposed Project Cost">
                                                            <input type="Button" value="${translation}" class="dr-menu" onclick="addPropFunding()">
                                                            </field:display>
                                                          	<c:set var="translation">
                                                            	<digi:trn key="btn:removeFundings">Remove Funding</digi:trn>
                                                            </c:set>
                                                            <field:display name="Remove Funding Button - Proposed Project Cost" feature="Proposed Project Cost">
	                                                            <input type="Button" value="${translation}" class="dr-menu" onclick="delPropFunding()">
	                                                        </field:display>
                                                          </c:if>
                                                        </td>
                                                      </tr>
                                                    </table>
                                                  </td>
                                                </tr>
                                              </table>
                                            </td>
                                          </tr>
                                          <tr>
                                            <td>
                                              &nbsp;
                                            </td>
                                          </tr>
                                        </table>
                                      </feature:display>
                                     
                                     
                                    <feature:display name="Funding Information" module="Funding">
                                      <table width="100%" bgcolor="#f4f4f2" border="0" cellSpacing="0" cellPadding="0" >
                                        <tr>
                                          <td>
                                          <br />
                                          &nbsp;&nbsp;&nbsp;&nbsp;
                                            <IMG alt="Link" height="10" src="../ampTemplate/images/arrow-014E86.gif" width="15">
                                              <a title="<digi:trn key="aim:FundingOrgs">The country or agency that financed the project</digi:trn>">
	                                              <b>
	                                                <digi:trn key="aim:fundingOrganizations">Funding Information</digi:trn>
	                                              </b>
											  </a><br /><br />
                                          </td>
                                        </tr>

                                        <tr>
                                          <td align="left">
                                            <table width="95%" cellSpacing="1" cellPadding="0" border="0" align="center">
                                              <tr>
                                                <td>
                                                  <table cellSpacing="0" cellPadding="0" border="0" width="100%" class="box-border-nopadding">
                                                    <logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
                                                      <gateperm:putInScope key="currentOrgRole" value="DN"/>
                                                      <logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.module.aim.helper.FundingOrganization">
                                                        <gateperm:putInScope key="currentOrg" name="fundingOrganization">
                                                        <tr>
                                                          <td>
	                                                          	<table width="100%">
	                                                        	  <tr>
		                                                          		<td>
		                                                          			<field:display name="Organizations Selector" feature="Funding Information">
				                                                            	<html:multibox property="funding.selFundingOrgs" styleId="selFundingOrgs">
					                                                          		<bean:write name="fundingOrganization" property="ampOrgId"/>
					                                                        	</html:multibox>
					                                                        </field:display>
			                                                            	<b><bean:write name="fundingOrganization" property="orgName"/></b>
			                                                            	<br/>
				                                                            <field:display name="Organizations Selector" feature="Funding Information">
																				<aim:addOrganizationButton callBackFunction="doNothing();" aditionalRequestParameters="id=${fundingOrganization.ampOrgId}"  delegateClass="org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate"  property="fundingOrganizations"  form="${aimEditActivityForm.funding}" refreshParentDocument="true" styleClass="dr-menu"> <digi:trn key="btn:changeOrganizations">Change Organization</digi:trn> </aim:addOrganizationButton>
																			</field:display>
															        	</td>


		                                                            <field:display name="Active Funding Organization" feature="Funding Information">
		                                                            <td valign="top"> &nbsp;&nbsp;
		                          										<html:select property="fundingActive" indexed="true" name="fundingOrganization" styleClass="inp-text">
		                          											<html:option value="true">Active</html:option>
		                          											<html:option value="false">Inactive</html:option>
		                          										</html:select>
		                                                            </td>
		                                                            </field:display>
		                                                            
		                                                            <field:display name="Delegated Cooperation" feature="Funding Information">
			                                                            <td valign="top" align="right">
				                          									<digi:trn key="aim:DelegatedCooperation">Delegated Cooperation</digi:trn><html:checkbox name="fundingOrganization" property="delegatedCooperation" indexed="true" onclick="delegatedCooperationClick(this.name);"/>
										   									<html:hidden name="fundingOrganization" property="delegatedCooperationString" indexed="true"/>
			                                                            </td>
		                                                            </field:display>
		                                                            <field:display name="Delegated Partner" feature="Funding Information">
			                                                            <td valign="top">
				                          									<digi:trn key="aim:DelegatedPartner">Delegated Partner</digi:trn><html:checkbox property="delegatedPartner" indexed="true" name="fundingOrganization" onclick="indexedCheckboxClick(this.name);"/>
				                          									<html:hidden name="fundingOrganization" property="delegatedPartnerString" indexed="true"/>
			                          							        </td>
		                                                            </field:display>
																</tr>
	                                                         </table>
                                                          </td>
                                                        </tr>

                                                        <logic:notEmpty name="fundingOrganization" property="fundings">
                                                          <logic:iterate name="fundingOrganization"  indexId="index" property="fundings" id="funding" type="org.digijava.module.aim.helper.Funding">
                                                            <tr>
                                                              <td>
                                                                <table cellSpacing="1" cellPadding="0" border="0" width="100%" class="box-border-nopadding">
                                                                  <tr>
                                                                    <td>
                                                                      <table cellSpacing="1" cellPadding="0" border="0" width="100%">
                                                                        <tr>
                                                                          <td>
                                                                            <table width="100%" border="0" cellpadding="1" bgcolor="#ffffff" cellspacing="1">
                                                                            <field:display name="Funding Organization Id" feature="Funding Information"></field:display>
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:FundOrgId">This ID is specific to the financial operation. This item may be useful when one project has two or more different financial instruments. If the project has a unique financial operation, the ID can be the same as the project ID</digi:trn>">																																<digi:trn key="aim:fundingOrgId">
                                                                                    Funding Organization Id</digi:trn></a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <bean:write name="funding"	property="orgFundingId"/>
                                                                                </td>
                                                                              </tr>
                                                                              
                                                                              <!-- type of assistance -->
                                                                              <field:display name="Type Of Assistance" feature="Funding Information"></field:display>
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
                                                                                  <digi:trn key="aim:typeOfAssist">
                                                                                    Type of Assistance </digi:trn>
																					</a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="typeOfAssistance">
                                                                                  	<category:getoptionvalue categoryValueId="${funding.typeOfAssistance.id}"/>
                                                                                  </logic:notEmpty>
                                                                                </td>
                                                                              </tr>


																			<field:display name="Financing Instrument" feature="Funding Information"></field:display>
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:Financing">Method by which aid is delivered to an activity</digi:trn>">
                                                                                  <digi:trn key="aim:financingInstrument">
                                                                                    Financing Instrument</digi:trn>
																				</a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="financingInstrument">
                                                                                  	<category:getoptionvalue categoryValueId="${funding.financingInstrument.id}"/>
                                                                                  </logic:notEmpty>
                                                                                </td>
                                                                              </tr>
                                                                              
                                                                              <field:display name="Funding start date" feature="Funding Information">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn>Funding start date</digi:trn>">
																					<digi:trn>Funding start date</digi:trn></a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <bean:write name="funding" property="actStartDate"/>
                                                                                </td>
         	                                                                     </tr>
																			</field:display>
                                                                              
                                                                              <field:display name="Funding end date" feature="Funding Information">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn>Funding end date</digi:trn>">
																					<digi:trn>Funding end date</digi:trn></a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <bean:write name="funding" property="actCloseDate"/>
                                                                                </td>
         	                                                                     </tr>
																			</field:display>
																			
                                                                              <field:display name="Mode of Payment" feature="Funding Information">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:Financing">Mode of Payment</digi:trn>">
                                                                                  <digi:trn>Mode of Payment</digi:trn>
																				</a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="modeOfPayment">
                                                                                  	<category:getoptionvalue categoryValueId="${funding.modeOfPayment.id}"/>
                                                                                  </logic:notEmpty>
                                                                                </td>
         	                                                                     </tr>
																			</field:display>

																			<field:display name="Conditions for Fund Release" feature="Funding Information">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">		 
                                                                                  	<digi:trn key="aim:conditions"> Conditions
                                                                                  	</digi:trn>
																				  </a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <bean:write name="funding"	property="conditions"/>
                                                                                </td>
                                                                              </tr>
                                                                            </field:display>
                                                                              
                                                                            <field:display name="Donor Objective" feature="Funding Information">
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:DonorObjectiveforFundRelease">Enter the donor objective attached to the release of the funds</digi:trn>"><digi:trn key="aim:donorobjective">Donor Objective</digi:trn>
																				</a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <bean:write name="funding"	property="donorObjective"/>
                                                                                </td>
                                                                              </tr>
                                                                            </field:display>
                                                                            </table>
                                         								  </td>
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

	                                                                              	<tr bgcolor="#ffffff">
	                                                                                 <td colspan="5">
	                                                                                 <b><digi:trn key="aim:funding:projections">Projections</digi:trn></b>
	                                                                                 </td>
	                                                                              	</tr>
	                                                                                 <logic:notEmpty name="funding" property="mtefProjections">
	                                                                                 <logic:iterate name="funding" property="mtefProjections" id="projection">
	                                                                                 <tr bgcolor="#ffffff">
	                                                                                 	<field:display name="Projection Name" feature="MTEF Projections"></field:display>
	                                                                                 	<td width="50">
	                                                                                 		<category:getoptionvalue categoryValueId="${projection.projected}" />
	                                                                                 	</td>
	                                                                                 	<field:display name="Projection Amount" feature="MTEF Projections"></field:display>
	                                                                                 	<td width="120" align="right">
	                                                                                 		<FONT color="blue">*</FONT>
	                                                                                 		<bean:write name="projection" property="amount" />
	                                                                                 	</td>
	                                                                                 	<field:display name="Projection Currency Code" feature="MTEF Projections"></field:display>
	                                                                                 	<td>
	                                                                                 		<bean:write name="projection" property="currencyCode" />
	                                                                                 	</td>
	                                                                                 	<field:display name="Projection Date" feature	="MTEF Projections"></field:display>
	                                                                                 	<td>
	                                                                                 		<bean:write name="projection" property="projectionDateLabel" />
	                                                                                 	</td>
	                                                                                 	<td>&nbsp;</td>
	                                                                                 </tr>
	                                                                                 </logic:iterate>
	                                                                                 </logic:notEmpty>
																				</feature:display>
																			  <%-- Rendering projections --%>
                                                                       	<feature:display module="Funding" name="Commitments"> 
																			<tr bgcolor="#ffffff">
                                                                                 <td colspan="5">
                                                                                	<b>
                                                                                  	<a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>" >
                                                                                  		<digi:trn key="aim:commitments">			Commitments </digi:trn>
																					</a></b>
                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.fundingDetails}">
                                                                              <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
                                                                                <logic:equal name="fundingDetail" property="transactionType" value="0">

                                                                                      <tr bgcolor="#ffffff">
                                                                                        <td width="50">
                                                                                        <field:display name="Adjustment Type Commitment" feature="Commitments"></field:display>
                                                                                          <digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																								<bean:write name="fundingDetail" property="adjustmentTypeName.value"/>
																							</digi:trn>
                                                                                        </td>
                                                                                        <td width="120" align="right">
                                                                                        <field:display name="Amount Commitment" feature="Commitments"></field:display>
                                                                                          <FONT color="blue">*</FONT>
                                                                                          <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
                                                                                        </td>
                                                                                        <td width="150">
                                                                                        <field:display name="Currency Commitment" feature="Commitments"></field:display>
                                                                                          <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                        </td>
                                                                                        <td width="70">
                                                                                       	 	<field:display name="Date Commitment" feature="Commitments"></field:display>
                                                                                          	<bean:write name="fundingDetail" property="transactionDate"/>
                                                                                        </td>
                                                                                        <td>
                                                                                        </td>
                                                                                      </tr>
                                                                                       <logic:notEqual name="fundingDetail" property="pledgename" value="">
                                                                                      <tr>
                                                                                      	<td colspan="5">
                                                                                       	 	<field:display name="Related Pledge" feature="Commitments">
                                                                                       	 	<digi:trn>Related Pledge</digi:trn> :
                                                                                       	 	<bean:write name="fundingDetail" property="pledgename"/>
                                                                                       	 	</field:display>
                                                                                        </td>
                                                                                      </tr>
																					</logic:notEqual>

                                                                                </logic:equal>
                                                                              </logic:iterate>
                                                                              </c:if>
																			</feature:display>

                                                                              <!--Disbursement order-->
                                                                               <feature:display module="Funding" name="Disbursement Orders">
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">&nbsp;</td>
                                                                              </tr>
                                                                              <tr bgcolor="#ffffff">
                                                                                 <td colspan="5">
                                                                                	<b>
                                                                                  	<a title="<digi:trn key="aim:disbursementOrdersMade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>" >
                                                                                  		<digi:trn key="aim:disbursementOrders">	Disbursement Orders </digi:trn>
																					</a></b>
                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.fundingDetails}">
                                                                                <logic:iterate name="funding" property="fundingDetails" id="fundingDetail"     type="org.digijava.module.aim.helper.FundingDetail">
                                                                                <logic:equal name="fundingDetail" property="transactionType" value="4">


                                                                                      <tr bgcolor="#ffffff">


                                                                                    <td width="50">
	                                                                                    <field:display name="Adjustment Type of Disbursement Order" feature="Disbursement Orders"></field:display>
    	                                                                                	<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
                                                                                                <bean:write name="fundingDetail" property="adjustmentTypeName.value"/>
                                                                                                </digi:trn>
                                                                                    </td>


                                                                                    <td width="120" align="right">
                                                                                      <field:display name="Amount of Disbursement Order" feature="Disbursement Orders"></field:display>
                                                                                      	<FONT color="blue">*</FONT>
                                                                                      	<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
                                                                                    </td>

                                                                                    <td width="150">
	                                                                                    <field:display name="Currency of Disbursement Order" feature="Disbursement Orders"></field:display>
    	                                                                                  <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                    </td>
                                                                                    <td width="70">
                                                                                    	<field:display name="Date of Disbursement Order" feature="Disbursement Orders"></field:display>
		                                                                                      <bean:write name="fundingDetail" property="transactionDate"/>
                                                                                    </td>
                                                                                      <td>
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
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">
                                                                                  <a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>">
                                                                                  <b> <digi:trn key="aim:disbursements">Disbursements </digi:trn></b>
																				</a>
                                                                                </td>
                                                                              </tr>
                                                                              <c:if test="${!empty funding.fundingDetails}">
	                                                                              <logic:iterate name="funding" property="fundingDetails" id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
		                                                                              <logic:equal name="fundingDetail" property="transactionType" value="1">		
																									<tr bgcolor="#ffffff">
																										<td width="50">
																											<field:display name="Adjustment Type Disbursement" feature="Disbursement"></field:display>
																												<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																													<bean:write name="fundingDetail" property="adjustmentTypeName.value"/>
																												</digi:trn>
																										</td>
																										<td width="120" align="right">
																											<field:display name="Amount Disbursement" feature="Disbursement"></field:display>
																												<FONT color="blue">*</FONT>
																												<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																										</td>
																										<td width="150">
																											<field:display name="Currency Disbursement" feature="Disbursement"></field:display>
																												<bean:write name="fundingDetail" property="currencyCode"/>
																										</td>
																										<td width="70">
																											<field:display name="Date Disbursement" feature="Disbursement"></field:display>
																												<bean:write name="fundingDetail" property="transactionDate"/>
																										</td>
																										<td>
																										
																										<field:display name="Contract of Disbursement Order" feature="Disbursement Orders"></field:display>
			                                                                                             	 <c:if test="${not empty fundingDetail.contract}">
					                                                                                     		${fundingDetail.contract.contractName}
			                                                                                                   </c:if>
																										</td>																							
																									</tr>
																									 <logic:notEqual name="fundingDetail" property="pledgename" value="">
																									 <tr>
                                                                                      					<td  colspan="5">
                                                                                       	 					<field:display name="Related Pledge" feature="Disbursement">
                                                                                       	 						<digi:trn>Related Pledge</digi:trn> :
                                                                                       	 						<bean:write name="fundingDetail" property="pledgename"/>
                                                                                       	 					</field:display>
                                                                                        				</td>
                                                                                      				</tr>
                                                                                      				</logic:notEqual>
																						</logic:equal>
																					</logic:iterate>
                                                                                 </c:if>
                                                                                 </feature:display>
                                                                                        
																						<tr bgcolor="#ffffff">
																							<td colspan="5">&nbsp;</td>
																						</tr>
																				<feature:display module="Funding" name="Expenditures">
																						<tr bgcolor="#ffffff">
																							<td colspan="5">
																							<a title="<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>">	<b><digi:trn key="aim:expenditures"> Expenditures </digi:trn></b>
																							</a>
																							</td>
																						</tr>
                                                                                        <c:if test="${!empty funding.fundingDetails}">
																						<logic:iterate name="funding" property="fundingDetails"
																						id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
																						<logic:equal name="fundingDetail" property="transactionType" value="2">																						

																								<tr bgcolor="#ffffff">
																									<td width="50">
																									<field:display name="Adjustment Type Expenditure" feature="Expenditures"></field:display>
																										<digi:trn key='<%="aim:"+fundingDetail.getAdjustmentTypeNameTrimmed() %>'>
																											<bean:write name="fundingDetail" property="adjustmentTypeName.value"/>
																										</digi:trn>
																									</td>
																									<td width="120" align="right">
																										<field:display name="Amount Expenditure" feature="Expenditures"></field:display>
																											<FONT color="blue">*</FONT>
																											<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																									</td>
																									<td width="150">
																										<field:display name="Currency Expenditure" feature="Expenditures"></field:display>
																											<bean:write name="fundingDetail" property="currencyCode"/>
																									</td>
																									<td width="70">
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
																					<a href='javascript:fnOnEditItem(<%= index %>,
																									 <bean:write name="fundingOrganization" property="ampOrgId"/>,
																									<bean:write name="funding" property="fundingId"/>)'>
																					<B><digi:trn key="aim:editFundingItem">Edit Item</digi:trn></B>
																					</a>
																				</field:display>
																			</td>
																			<td>
																				<field:display name="Delete Funding Link - Donor Organization" feature="Funding Information">
																					<a href='javascript:fnOnDeleteItem(<bean:write name="fundingOrganization"
																										 property="ampOrgId"/>,<bean:write name="funding"
																										 property="fundingId"/>)'>
																					<B><digi:trn key="aim:deleteFundingItem">Delete Item</digi:trn></B>
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
																	</logic:notEmpty>

																<tr>
																	<td style="padding:10px 0px 10px 4px;">
																		<field:display name="Add Donor Funding Button" feature="Funding Information">
																			<input type="button" class="dr-menu" onclick="addFunding('<bean:write name="fundingOrganization" property="ampOrgId"/>')" value='<digi:trn>Add Funding</digi:trn>' />
																		</field:display>
																		<field:display name="Import Donor Funding Button" feature="Funding Information">
																			<input type="button" class="dr-menu" onclick="importFunding('<bean:write name="fundingOrganization" property="ampOrgId"/>')" value='<digi:trn>Import Funding</digi:trn>' />																			
																		</field:display>
																	</td>
																</tr>
																</gateperm:putInScope>
																</logic:iterate>
															
																<tr><td>&nbsp;</td></tr>

																<tr>
																	<td>
																		<table cellSpacing="2" cellPadding="2">
																			<tr>
																				<td>
																					<field:display name="Add Donor Organization" feature="Funding Information">
																				   		
																					<aim:addOrganizationButton  callBackFunction="doNothing();" delegateClass="org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate"  collection="fundingOrganizations" form="${aimEditActivityForm.funding}" refreshParentDocument="true" styleClass="dr-menu-special"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
													
																					
																					</field:display>
																				</td>
																				<td>
																					<field:display name="Remove Donor Organization" feature="Funding Information">
																					   <html:button  styleClass="dr-menu-special" property="submitButton" onclick="return removeSelOrganisations()">
																							<digi:trn key="btn:removeOrganizations">Remove Organizations</digi:trn>
																					   </html:button>
																					</field:display>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>

																</logic:notEmpty>
																<logic:empty name="aimEditActivityForm" property="funding.fundingOrganizations">
																<tr>
																	<td>
																		<field:display name="Add Donor Organization" feature="Funding Information">
																			<aim:addOrganizationButton  callBackFunction="doNothing();" delegateClass="org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate"  collection="fundingOrganizations" form="${aimEditActivityForm.funding}" refreshParentDocument="true" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
													
																		</field:display>
																	</td>
																</tr>

																</logic:empty>
															</table>
															
														</td>
													</tr>
												</table>
												</td>
											</tr>
											<tr><td>&nbsp;</td></tr>
<!--
                                            <tr>
                                              <td bgColor="#f4f4f2" align="center">
                                                <table cellPadding=3>
                                                  <tr>
                                                      <td>
													<html:submit  styleClass="dr-menu" property="submitButton" onclick="gotoStep(2)">
															<< <digi:trn key="btn:back">Back</digi:trn>
													</html:submit>

                                                      </td>
                                                      <td>
													<html:submit  styleClass="dr-menu" property="submitButton" onclick="gotoStep(4)">
															<digi:trn key="btn:next">Next</digi:trn> >>
													</html:submit>

                                                      </td>
                                                    <td>
													<html:reset  styleClass="dr-menu" property="submitButton" onclick="return resetAll()">
														<digi:trn key="btn:reset">Reset</digi:trn>
													</html:reset>

                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
 -->
                                      </table></feature:display>
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

						    <jsp:include page="editActivityMenu.jsp"  />

						  <!-- end of activity form menu -->
						</td></tr>
                    </table>
				</td></tr>
				<tr><td>
					&nbsp;
				</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>

</table>
</digi:form>

