<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

	<!--

	function validate() {
		if (document.aimEditActivityForm.selFundingOrgs.checked != null) { // only one org. added
			if (document.aimEditActivityForm.selFundingOrgs.checked == false) {
				alert("Please choose a funding organization to remove");
				return false;
			}
		} else { // many org. present
			var length = document.aimEditActivityForm.selFundingOrgs.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimEditActivityForm.selFundingOrgs[i].checked == true) {
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
			openNewWindow(650, 500);
			<digi:context name="addFunding" property="context/module/moduleinstance/addFunding.do" />
			document.aimEditActivityForm.orgId.value = orgId;
			document.aimEditActivityForm.action = "<%= addFunding %>?orgId" + orgId+"&edit=true";
			document.aimEditActivityForm.prevOrg.value = orgId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}

    function addPropFunding() {
            openNewWindow(450, 148);
            <digi:context name="addProposedFunding" property="context/module/moduleinstance/editProposedFunding.do" />
            document.aimEditActivityForm.action = "<%= addProposedFunding %>";
            document.aimEditActivityForm.target = popupPointer.name;
            document.aimEditActivityForm.submit();
	}

    function delPropFunding() {
            <digi:context name="delProposedFunding" property="context/module/moduleinstance/removeProposedFunding.do" />
            var state=window.confirm("Are you sure about deleting the Proposed Project Cost ?");
            if(state==false){
              return false;
            }
            document.aimEditActivityForm.action = "<%= delProposedFunding %>";
            document.aimEditActivityForm.submit();
	}

	function selectOrganisation() {
			openNewWindow(650, 420);
			<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true" />
			document.aimEditActivityForm.action = "<%= selectOrganization %>";
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}

	function fnOnEditItem(index, orgId,fundId)	{
			openNewWindow(650, 500);
			<digi:context name="editItem" property="context/module/moduleinstance/editFunding.do"/>
			document.aimEditActivityForm.action = "<%= editItem %>?orgId=" + orgId + "&offset=" + index+"&edit=true";
			document.aimEditActivityForm.prevOrg.value = orgId;
			document.aimEditActivityForm.fundingId.value = fundId;
			document.aimEditActivityForm.target = popupPointer.name;
			document.aimEditActivityForm.submit();
	}

	function fnOnDeleteItem(orgId,fundId)	{
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

	-->

</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post">
<html:hidden property="step"/>
<html:hidden property="orgId"/>
<html:hidden property="fundingId"/>

<input type="hidden" name="prevOrg">
<input type="hidden" name="edit" value="true">

<html:hidden property="editAct" />

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
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 0}">
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
									</bean:define>
									<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>
									&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
									</bean:define>

									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot()" title="<%=translation%>" >
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>
									&nbsp;&gt;&nbsp;
								</c:if>
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewAddActivityStep1">Click here to go to Add Activity Step 1</digi:trn>
								</bean:define>

								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<digi:link href="/addActivity.do?step=1&edit=true" styleClass="comment" title="<%=translation%>" >
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
								</c:if>

								<c:if test="${aimEditActivityForm.donorFlag == true}">
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
								&nbsp;&gt;&nbsp;

								<c:if test="${aimEditActivityForm.donorFlag == false}">
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep2">Click here to goto Add Activity Step 2</digi:trn>
									</bean:define>
									<digi:link href="/addActivity.do?step=2&edit=true" styleClass="comment" title="<%=translation%>" >
										<digi:trn key="aim:addActivityStep2">
											Step 2
										</digi:trn>
									</digi:link>
								</c:if>

								<c:if test="${aimEditActivityForm.donorFlag == true}">
									<bean:define id="translation">
										<digi:trn key="aim:clickToViewAddActivityStep2">Click here to goto Add Activity Step 2</digi:trn>
									</bean:define>
									<digi:trn key="aim:addActivityStep2">
										Step 2
									</digi:trn>
								</c:if>
								&nbsp;&gt;&nbsp;
								<digi:trn key="aim:addActivityStep3">
								Step 3
								</digi:trn>
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
                <tr>
                  <td>
                    <table width="100%" cellSpacing="1" cellPadding="1" valign="top">
                      <tr>
                        <td height=16 vAlign="middle" width="100%"><span class=subtitle-blue>
                          <c:if test="${aimEditActivityForm.editAct == false}">
                            <digi:trn key="aim:addNewActivity">
                            Add New Activity
                            </digi:trn>
                          </c:if>
                          <c:if test="${aimEditActivityForm.editAct == true}">
                            <digi:trn key="aim:editActivity">
                            Edit Activity
                            </digi:trn>:
                            <bean:write name="aimEditActivityForm" property="title"/>
                          </c:if>
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
                          <table cellPadding=0 cellSpacing=0 width="100%" vAlign="top">
                            <tr>
                              <td width="100%">
                                <table cellPadding=0 cellSpacing=0 width="100%" border=0>
                                  <tr>
                                    <td width="13" height="20" background="module/aim/images/left-side.gif">
                                    </td>
                                    <td vAlign="middle" align ="center" class="textalb" height="20" bgcolor="#006699">
                                      <digi:trn key="aim:step3of9Funding">
                                      Step 3 of 9: Funding
                                      </digi:trn>
                                    </td>
                                    <td width="13" height="20" background="module/aim/images/right-side.gif">
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                            <tr>
                              <td bgColor=#f4f4f2 align="center" vAlign="top" width="100%">
                                <table width="100%" cellSpacing="0" vAlign="top" align="left" bgcolor="#006699">
                                  <tr>
                                    <td>
                                      <c:if test="${aimEditActivityForm.status==1}">
                                        <table width="100%" bgcolor="#f4f4f2" border="0" cellSpacing=0 cellPadding=0>
                                          <tr>
                                            <td>
                                            <br />
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                              <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
                                              <a title="<digi:trn key="aim:ProposedProjCost">Proposed Project Cost</digi:trn>">
                                              <b>
                                                <digi:trn key="aim:proposedPrjectCost">Proposed Project Cost</digi:trn>
                                              </b>
</a><br /><br />
                                            </td>
                                          </tr>
                                          <tr>
                                            <td align="left">
                                              <table width="100%" cellSpacing=0 cellPadding=0 border=0>
                                                <tr>
                                                  <td>
                                                    <table cellSpacing=8 cellPadding=0 border=0 width="95%" class="box-border-nopadding" align="center">
                                                      <c:if test="${aimEditActivityForm.proProjCost!=null}">
                                                        <tr>
                                                          <td>
                                                            <table cellSpacing=1 cellPadding="1" bgcolor="#dddddd" width="100%">
                                                              <tr bgcolor="#ffffff">
                                                                <td bgcolor="#FFFFFF" align="left" width="30">
                                                                Planned
                                                                </td>
                                                                <td bgcolor="#FFFFFF" align="left" width="10">
                                                                  <c:if test="${aimEditActivityForm.proProjCost.funAmount!=null}">
                                                                  ${aimEditActivityForm.proProjCost.funAmount}
                                                                  </c:if>
                                                                </td>
                                                                <td bgcolor="#FFFFFF" align="left" width="10">
                                                                  <c:if test="${aimEditActivityForm.proProjCost.currencyCode!=null}">
                                                                  ${aimEditActivityForm.proProjCost.currencyCode}
                                                                  </c:if>
                                                                </td>
                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                  <c:if test="${aimEditActivityForm.proProjCost.funDate!=null}">
                                                                  ${aimEditActivityForm.proProjCost.funDate}
                                                                  </c:if>
                                                                </td>
                                                              </tr>
                                                            </table>
                                                          </td>
                                                        </tr>
                                                      </c:if>
                                                      <tr>
                                                        <td>
                                                          <c:if test="${aimEditActivityForm.proProjCost==null}">
                                                            <input type="button" value="Add Funding" class="buton" onclick="addPropFunding()">
                                                          </c:if>
                                                          <c:if test="${aimEditActivityForm.proProjCost!=null}">
                                                            <input type="Button" value="Remove Funding" class="buton" onclick="delPropFunding()">
                                                            <input type="Button" value="Edit Funding" class="buton" onclick="addPropFunding()">
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
                                      </c:if>
                                      <table width="100%" bgcolor="#f4f4f2" border="0" cellSpacing=0 cellPadding=0 >
                                        <tr>
                                          <td>
                                          <br />
                                          &nbsp;&nbsp;&nbsp;&nbsp;
                                            <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
                                              <a title="<digi:trn key="aim:FundingOrgs">The country or agency that financed the project</digi:trn>">
                                              <b>
                                                <digi:trn key="aim:fundingOrganizations">Funding Organizations</digi:trn>
                                              </b>
</a><br /><br />
                                          </td>
                                        </tr>

                                        <tr>
                                          <td align="left">
                                            <table width="95%" cellSpacing=1 cellPadding=0 border=0 align="center">
                                              <tr>
                                                <td>
                                                  <table cellSpacing=8 cellPadding=0 border=0 width="100%" class="box-border-nopadding">
                                                    <logic:notEmpty name="aimEditActivityForm" property="fundingOrganizations">
                                                      <logic:iterate name="aimEditActivityForm" property="fundingOrganizations" id="fundingOrganization" type="org.digijava.module.aim.helper.FundingOrganization">
                                                        <tr>
                                                          <td>
                                                            <html:multibox property="selFundingOrgs">
                                                              <bean:write name="fundingOrganization" property="ampOrgId"/>
                                                            </html:multibox>
                                                            <bean:write name="fundingOrganization" property="orgName"/>
                                                          </td>
                                                        </tr>
                                                        <logic:notEmpty name="fundingOrganization" property="fundings">
                                                          <logic:iterate name="fundingOrganization" indexId="index" property="fundings" id="funding" type="org.digijava.module.aim.helper.Funding">
                                                            <tr>
                                                              <td>
                                                                <table cellSpacing=1 cellPadding=0 border=0 width="100%" class="box-border-nopadding">
                                                                  <tr>
                                                                    <td>
                                                                      <table cellSpacing=1 cellPadding=0 border=0 width="100%">
                                                                        <tr>
                                                                          <td>
                                                                            <table width="100%" border="0" cellpadding="1" bgcolor="#ffffff" cellspacing="1">
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
                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:AssitanceType">Specify whether the project was financed through a grant, a loan or in kind</digi:trn>">
                                                                                  <digi:trn key="aim:typeOfAssist">
                                                                                    Type of Assistance </digi:trn>
</a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="ampTermsAssist">
                                                                                    <bean:write name="funding"	property="ampTermsAssist.termsAssistName"/>
                                                                                  </logic:notEmpty>
                                                                                </td>
                                                                              </tr>

                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:Financing">Method by which aid is delivered to an activity</digi:trn>">
                                                                                  <digi:trn key="aim:financingInstrument">
                                                                                    Financing Instrument</digi:trn>
</a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <logic:notEmpty name="funding" property="modality">
                                                                                    <bean:write name="funding"	property="modality.name"/>
                                                                                  </logic:notEmpty>
                                                                                </td>
                                                                              </tr>

                                                                              <tr>
                                                                                <td bgcolor="#FFFFFF" align="left" width="150">
                                                                                  <a title="<digi:trn key="aim:ConditionsforFundRelease">Enter the conditions attached to the release of the funds</digi:trn>">		 <digi:trn key="aim:conditions"> Conditions
                                                                                  </digi:trn>
</a>
                                                                                </td>
                                                                                <td width="1">:</td>
                                                                                <td bgcolor="#FFFFFF" align="left">
                                                                                  <bean:write name="funding"	property="conditions"/>
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
                                                                      <table width="98%" border="0" cellpadding="1"                                                                      bgcolor="#ffffff" cellspacing="1">
                                                                        <tr>
                                                                          <td bgcolor="#FFFFFF" align="right" colspan="2">
                                                                            <table width="100%" border="0" cellSpacing="1" cellPadding="1" bgcolor="#dddddd">
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5"><b>
                                                                                  <a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>">				<digi:trn key="aim:commitments">			Commitments </digi:trn></b>
</a>
                                                                                </td>
                                                                              </tr>
                                                                              <logic:iterate name="funding" property="fundingDetails" id="fundingDetail"                                                                              type="org.digijava.module.aim.helper.FundingDetail">
                                                                                <logic:equal name="fundingDetail" property="transactionType" value="0">

                                                                                  <c:if test="${aimEditActivityForm.donorFlag == true}">
                                                                                    <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                      <tr bgcolor="#FFFF00">
                                                                                    </c:if>
                                                                                    <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
                                                                                      <tr bgcolor="#ffffff">
                                                                                    </c:if>
                                                                                    <td width="50">
                                                                                      <bean:write name="fundingDetail" property="adjustmentTypeName"/>
                                                                                    </td>
                                                                                    <td width="120" align="right">
                                                                                      <FONT color=blue>*</FONT>
                                                                                      <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
                                                                                    </td>
                                                                                    <td width="150">
                                                                                      <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                    </td>
                                                                                    <td width="70">
                                                                                      <bean:write name="fundingDetail" property="transactionDate"/>
                                                                                    </td>
                                                                                    <td>
                                                                                    	<digi:trn key='<%="aim:"+fundingDetail.getPerspectiveNameTrimmed() %>'>
																								<bean:write name="fundingDetail" property="perspectiveName"/>
																						</digi:trn>
                                                                                    
                                                                                    </td>
                                                                                      </tr>
                                                                                  </c:if>

                                                                                  <c:if test="${aimEditActivityForm.donorFlag == false}">
                                                                                    <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
                                                                                      <tr bgcolor="#ffffff">
                                                                                        <td width="50">
                                                                                          <bean:write name="fundingDetail" property="adjustmentTypeName"/>
                                                                                        </td>
                                                                                        <td width="120" align="right">
                                                                                          <FONT color=blue>*</FONT>
                                                                                          <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
                                                                                        </td>
                                                                                        <td width="150">
                                                                                          <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                        </td>
                                                                                        <td width="70">
                                                                                          <bean:write name="fundingDetail" property="transactionDate"/>
                                                                                        </td>
                                                                                        <td>
   																								<digi:trn key='<%="aim:"+fundingDetail.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="fundingDetail" property="perspectiveName"/>
																								</digi:trn>

                                                                                        </td>
                                                                                      </tr>
                                                                                    </c:if>
                                                                                     <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                      <tr bgcolor="#ffffff">
                                                                                        <td width="50">
                                                                                          <bean:write name="fundingDetail" property="adjustmentTypeName"/>
                                                                                        </td>
                                                                                        <td width="120" align="right">
                                                                                          <FONT color=blue>*</FONT>
                                                                                          <bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
                                                                                        </td>
                                                                                        <td width="150">
                                                                                          <bean:write name="fundingDetail" property="currencyCode"/>
                                                                                        </td>
                                                                                        <td width="70">
                                                                                          <bean:write name="fundingDetail" property="transactionDate"/>
                                                                                        </td>
                                                                                        <td>
   																								<digi:trn key='<%="aim:"+fundingDetail.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="fundingDetail" property="perspectiveName"/>
																								</digi:trn>
</td>
                                                                                      </tr>
                                                                                    </c:if>
                                                                                  </c:if>

                                                                                </logic:equal>
                                                                              </logic:iterate>
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">&nbsp;</td>
                                                                              </tr>
                                                                              <tr bgcolor="#ffffff">
                                                                                <td colspan="5">
                                                                                  <a title="<digi:trn key="aim:FundRelease">Release of funds to, or the purchase of goods or services for a recipient; by extension, the amount thus spent. Disbursements record the actual international transfer of financial resources, or of goods or services valued at the cost to the donor </digi:trn>"><b> <digi:trn key="aim:disbursements">			Disbursements </digi:trn></b>
</a>
                                                                                </td>
                                                                              </tr>
                                                                              <logic:iterate name="funding" property="fundingDetails"
                                                                              id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
                                                                              <logic:equal name="fundingDetail" property="transactionType" value="1">


                                                                                <c:if test="${aimEditActivityForm.donorFlag == true}">
                                                                                  <c:if test="${fundingDetail.perspectiveCode == 'DN'}">
                                                                                    <tr bgcolor="#FFFF00">
                                                                                  </c:if>
                                                                                  <c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																						</c:if>
																							<td width="50">
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																							</td>
																							<td width="120" align="right">
																								<FONT color=blue>*</FONT>
																								<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																							</td>
																							<td width="150">
																								<bean:write name="fundingDetail" property="currencyCode"/>
																							</td>
																							<td width="70">
																								<bean:write name="fundingDetail" property="transactionDate"/>
																							</td>
																							<td>
   																								<digi:trn key='<%="aim:"+fundingDetail.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="fundingDetail" property="perspectiveName"/>
																								</digi:trn>
																							</td>
																						</tr>
																						</c:if>

																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																						<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																							</td>
																							<td width="120" align="right">
																								<FONT color=blue>*</FONT>
																								<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																							</td>
																							<td width="150">
																								<bean:write name="fundingDetail" property="currencyCode"/>
																							</td>
																							<td width="70">
																								<bean:write name="fundingDetail" property="transactionDate"/>
																							</td>
																							<td>
																								<digi:trn key='<%="aim:"+fundingDetail.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="fundingDetail" property="perspectiveName"/>
																								</digi:trn>yyy
																							</td>
																						</tr>
																						</c:if>
																						<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																						<tr bgcolor="#ffffff">
																							<td width="50">
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																							</td>
																							<td width="120" align="right">
																								<FONT color=blue>*</FONT>
																								<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																							</td>
																							<td width="150">
																								<bean:write name="fundingDetail" property="currencyCode"/>
																							</td>
																							<td width="70">
																								<bean:write name="fundingDetail" property="transactionDate"/>
																							</td>
																							<td>
   																								<digi:trn key='<%="aim:"+fundingDetail.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="fundingDetail" property="perspectiveName"/>
																								</digi:trn>
																							</td>
																						</tr>
																						</c:if>
																						</c:if>

																						</logic:equal>
																						</logic:iterate>
																						<tr bgcolor="#ffffff">
																							<td colspan="5">&nbsp;</td>
																						</tr>
																						<tr bgcolor="#ffffff">
																							<td colspan="5">
																							<a title="<digi:trn key="aim:ExpenditureofFund">Amount effectively spent by the implementing agency</digi:trn>">	<b><digi:trn key="aim:expenditures"> Expenditures </digi:trn></b>
																							</a>
																							</td>
																						</tr>
																						<logic:iterate name="funding" property="fundingDetails"
																						id="fundingDetail" type="org.digijava.module.aim.helper.FundingDetail">
																						<logic:equal name="fundingDetail" property="transactionType" value="2">


																						<c:if test="${aimEditActivityForm.donorFlag == true}">
																						<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																						<tr bgcolor="#FFFF00">
																						</c:if>
																						<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																						<tr bgcolor="#ffffff">
																						</c:if>
																							<td width="50">
																								<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																							</td>
																							<td width="120" align="right">
																								<FONT color=blue>*</FONT>
																								<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																							</td>
																							<td width="150">
																								<bean:write name="fundingDetail" property="currencyCode"/>
																							</td>
																							<td width="70">
																								<bean:write name="fundingDetail" property="transactionDate"/>
																							</td>
																							<td>
   																								<digi:trn key='<%="aim:"+fundingDetail.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="fundingDetail" property="perspectiveName"/>
																								</digi:trn>
																							</td>
																						</tr>
																						<tr>
																							<td colspan=5 bgcolor="#ffffff">&nbsp;&nbsp;
																								<bean:write name="fundingDetail" property="classification"/>
																							</td>
																						</tr>
																						</c:if>

																						<c:if test="${aimEditActivityForm.donorFlag == false}">
																							<c:if test="${fundingDetail.perspectiveCode != 'DN'}">
																								<tr bgcolor="#ffffff">
																									<td width="50">
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</td>
																									<td width="120" align="right">
																										<FONT color=blue>*</FONT>
																										<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																									</td>
																									<td width="150">
																										<bean:write name="fundingDetail" property="currencyCode"/>
																									</td>
																									<td width="70">
																										<bean:write name="fundingDetail" property="transactionDate"/>
																									</td>
																									<td>
   																								<digi:trn key='<%="aim:"+fundingDetail.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="fundingDetail" property="perspectiveName"/>
																								</digi:trn>
																									</td>
																								</tr>
																								<tr>
																									<td colspan=5 bgcolor="#ffffff">&nbsp;&nbsp;
																										<bean:write name="fundingDetail" property="classification"/>
																									</td>
																								</tr>
																							</c:if>
																							<c:if test="${fundingDetail.perspectiveCode == 'DN'}">
																								<tr bgcolor="#ffffff">
																									<td width="50">
																										<bean:write name="fundingDetail" property="adjustmentTypeName"/>
																									</td>
																									<td width="120" align="right">
																										<FONT color=blue>*</FONT>
																										<bean:write name="fundingDetail" property="transactionAmount"/>&nbsp;
																									</td>
																									<td width="150">
																										<bean:write name="fundingDetail" property="currencyCode"/>
																									</td>
																									<td width="70">
																										<bean:write name="fundingDetail" property="transactionDate"/>
																									</td>
																									<td>
   																								<digi:trn key='<%="aim:"+fundingDetail.getPerspectiveNameTrimmed() %>'>
																									<bean:write name="fundingDetail" property="perspectiveName"/>
																								</digi:trn>
																									</td>
																								</tr>
																								<tr>
																									<td colspan=5 bgcolor="#ffffff">&nbsp;&nbsp;
																										<bean:write name="fundingDetail" property="classification"/>
																									</td>
																								</tr>
																							</c:if>
																						</c:if>



																						</logic:equal>
																						</logic:iterate>
																					</table>
																				</td>
																			</tr>
																		</table>

																	</td></tr>
																	<c:if test="${aimEditActivityForm.donorFlag == false}">
																	<tr><td>
																		<table border="0" cellpadding="8"
																		bgcolor="#ffffff" cellspacing="1">
																		<tr>
																			<td>
																				<a href='javascript:fnOnEditItem(<%= index %>,
																									 <bean:write name="fundingOrganization" property="ampOrgId"/>,
																								<bean:write name="funding" property="fundingId"/>)'>
																				<B><digi:trn key="aim:editFundingItem">Edit Item</digi:trn></B>
																				</a>
																			</td>
																			<td>
																				<a href='javascript:fnOnDeleteItem(<bean:write name="fundingOrganization"
																									 property="ampOrgId"/>,<bean:write name="funding"
																									 property="fundingId"/>)'>
																				<B><digi:trn key="aim:deleteFundingItem">Delete Item</digi:trn></B>
																				</a>
																			</td>
																		</tr>
																		</table>
																	</td></tr>
																	</c:if>

																	<c:if test="${aimEditActivityForm.donorFlag == true}">
																	<c:if test="${fundingOrganization.ampOrgId == aimEditActivityForm.fundDonor}">
																	<tr><td>
																		<table border="0" cellpadding="8"
																		bgcolor="#ffffff" cellspacing="1">
																		<tr>
																			<td>
																				<a href='javascript:fnOnEditItem(<%= index %>,
																									 <bean:write name="fundingOrganization" property="ampOrgId"/>,
																								<bean:write name="funding" property="fundingId"/>)'>
																				<B><digi:trn key="aim:editFundingItem">Edit Item</digi:trn></B>
																				</a>
																			</td>
																		</tr>
																		</table>
																	</td></tr>
																	</c:if>
																	</c:if>

																	<tr><td bgcolor="#ffffff">
																		<FONT color=blue>*
																			<digi:trn key="aim:theAmountEnteredAreInThousands">
																				The amount entered are in thousands (000)
		  																	</digi:trn>
																		</FONT>
																	</td></tr>
																	</table>
																	</td></tr>
																	</logic:iterate>
																	</logic:notEmpty>
																<c:if test="${aimEditActivityForm.donorFlag == false}">
																<tr>
																	<td>
																		<input type="button" value="Add Funding" class="buton"
																		onclick="addFunding('<bean:write name="fundingOrganization" property="ampOrgId"/>')">
																	</td>
																</tr>
																</c:if>
																</logic:iterate>
																<tr><td>&nbsp;</td></tr>
																<c:if test="${aimEditActivityForm.donorFlag == false}">
																<tr>
																	<td>
																		<table cellSpacing=2 cellPadding=2>
																			<tr>
																				<td>
																					<input type="button" value="Add Organizations" class="buton"
																				   onclick="selectOrganisation()">
																				</td>
																				<td>
																					<input type="button" value="Remove Organizations" class="buton"
																				   onclick="return removeSelOrganisations()">
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																</c:if>
																</logic:notEmpty>
																<logic:empty name="aimEditActivityForm" property="fundingOrganizations">
																<c:if test="${aimEditActivityForm.donorFlag == false}">
																<tr>
																	<td>
																		<input type="button" value="Add Organizations" class="buton"
																		onclick="selectOrganisation()">
																	</td>
																</tr>
																</c:if>
																</logic:empty>
															</table>
														</td>
													</tr>
												</table>
												</td>
											</tr>
											<tr><td>&nbsp;</td></tr>
                                            <tr>
                                              <td bgColor=#f4f4f2 align="center">
                                                <table cellPadding=3>
                                                  <tr>
                                                    <c:if test="${aimEditActivityForm.donorFlag == true}">
                                                      <td>
                                                        <input type="button" value="Preview" class="dr-menu" onclick="previewClicked()">
                                                      </td>
                                                    </c:if>
                                                    <c:if test="${aimEditActivityForm.donorFlag == false}">
                                                      <td>
                                                        <input type="submit" value=" << Back " class="dr-menu" onclick="gotoStep(2)">
                                                      </td>
                                                      <td>
                                                        <input type="submit" value="Next >> " class="dr-menu" onclick="gotoStep(4)">
                                                      </td>
                                                    </c:if>
                                                    <td>
                                                      <input type="reset" value="Reset" class="dr-menu" onclick="return resetAll()">
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                      </table>
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
  <c:if test="${aimEditActivityForm.donorFlag == false}">
    <jsp:include page="editActivityMenu.jsp" flush="true" />
  </c:if>
  <c:if test="${aimEditActivityForm.donorFlag == true}">
    <jsp:include page="donorEditActivityMenu.jsp" flush="true" />
  </c:if>
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
</td></tr>
</table>
</digi:form>

