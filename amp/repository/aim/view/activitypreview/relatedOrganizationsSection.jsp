<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<ampModule:display name="/Activity Form/Organizations" parentModule="/Activity Form">
    <fieldset>
        <legend>
		<span class=legend_label id="relatedorglink" style="cursor: pointer;">
			<digi:trn>Related Organizations</digi:trn>
		</span>
        </legend>

        <div id="relateorgdiv" class="toggleDiv">
            <ampModule:display name="/Activity Form/Organizations/Donor Organization" parentModule="/Activity Form/Organizations">
                <logic:notEmpty name="aimEditActivityForm" property="funding.fundingOrganizations">
                    <digi:trn key="aim:donororganisation">Donor Organization</digi:trn>
                    <br/>
                    <div id="act_donor_organisation" style="display: block;">
                        <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding" >
                            <tr>
                                <td>
                                    <logic:iterate name="aimEditActivityForm" property="funding.fundingOrganizations" id="fundingOrganization" type="org.digijava.ampModule.aim.helper.FundingOrganization">
                                        <ul>
                                            <li>
                                                <span class="word_break bold"><bean:write name="fundingOrganization" property="orgName"/></span>
                                            </li>
                                        </ul>
                                    </logic:iterate>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <hr />
                </logic:notEmpty>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Organizations/Responsible Organization" parentModule="/Activity Form/Organizations">
                <logic:notEmpty name="aimEditActivityForm" property="agencies.respOrganisations" >
                    <div id="act_responsible_organisation" style="display: block;">
                        <digi:trn key="aim:responsibleorganisation">Responsible Organization</digi:trn>
                        <br />

                        <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding" >
                            <tr>
                                <td>
                                    <logic:iterate name="aimEditActivityForm" property="agencies.respOrganisations" id="respOrganisations"
                                                   type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
                                        <ul>
                                            <li>
                                                <span class="word_break bold"><bean:write name="respOrganisations" property="name"/></span>
                                                <c:set var="tempOrgId" scope="page">${respOrganisations.ampOrgId}</c:set>
                                                <logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgToInfo(${tempOrgId})">
                                                    <span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.respOrgToInfo[tempOrgId]}"/>)</span>
                                                </logic:notEmpty>
                                                <ampModule:display name="/Activity Form/Organizations/Responsible Organization/percentage" parentModule="/Activity Form/Organizations/Responsible Organization">
                                                    <logic:notEmpty name="aimEditActivityForm" property="agencies.respOrgPercentage(${tempOrgId})" >
                                                        <c:out value="${aimEditActivityForm.agencies.respOrgPercentage[tempOrgId]}" /> %
                                                    </logic:notEmpty>
                                                </ampModule:display>
                                            </li>
                                        </ul>
                                    </logic:iterate>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <hr />
                    <br/>
                </logic:notEmpty>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Organizations/Executing Agency" parentModule="/Activity Form/Organizations">
                <logic:notEmpty name="aimEditActivityForm" property="agencies.executingAgencies">
                    <digi:trn key="aim:executingAgency">Executing Agency</digi:trn>
                    <div id="act_executing_agency" style="display: block;">
                        <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                            <tr>
                                <td>
                                    <logic:iterate name="aimEditActivityForm" property="agencies.executingAgencies" id="execAgencies"
                                                   type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
                                        <ul>
                                            <li>
                                                <span class="word_break bold"><bean:write name="execAgencies" property="name" /></span>
                                                <c:set var="tempOrgId">${execAgencies.ampOrgId}</c:set>
                                                <logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgToInfo(${tempOrgId})">
                                                    <span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.executingOrgToInfo[tempOrgId]}"/>)</span>
                                                </logic:notEmpty>
                                                <ampModule:display name="/Activity Form/Organizations/Executing Agency/percentage" parentModule="/Activity Form/Organizations/Executing Agency">
                                                    <logic:notEmpty name="aimEditActivityForm" property="agencies.executingOrgPercentage(${tempOrgId})" >
                                                        <c:out value="${aimEditActivityForm.agencies.executingOrgPercentage[tempOrgId]}" /> %
                                                    </logic:notEmpty>
                                                </ampModule:display>
                                            </li>
                                        </ul>
                                    </logic:iterate>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <hr/>
                </logic:notEmpty>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Organizations/Implementing Agency" parentModule="/Activity Form/Organizations">
                <logic:notEmpty name="aimEditActivityForm" property="agencies.impAgencies" >
                    <digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn>
                    <br/>
                    <div id="act_implementing_agency" style="display: block;">
                        <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                            <tr>
                                <td>
                                    <logic:iterate name="aimEditActivityForm" property="agencies.impAgencies" id="impAgencies"
                                                   type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
                                        <ul>
                                            <li>
                                                <span class="word_break bold"><bean:write name="impAgencies" property="name" /></span>
                                                <c:set var="tempOrgId">${impAgencies.ampOrgId}</c:set>
                                                <logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgToInfo(${tempOrgId})">
                                                    <span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.impOrgToInfo[tempOrgId]}"/>)</span>
                                                </logic:notEmpty>
                                                <ampModule:display name="/Activity Form/Organizations/Implementing Agency/percentage" parentModule="/Activity Form/Organizations/Implementing Agency">
                                                    <logic:notEmpty name="aimEditActivityForm" property="agencies.impOrgPercentage(${tempOrgId})" >
                                                        <c:out value="${aimEditActivityForm.agencies.impOrgPercentage[tempOrgId]}" /> %
                                                    </logic:notEmpty>
                                                </ampModule:display>
                                            </li>
                                        </ul>
                                    </logic:iterate>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <hr />
                </logic:notEmpty>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Organizations/Beneficiary Agency" parentModule="/Activity Form/Organizations">
                <logic:notEmpty name="aimEditActivityForm" property="agencies.benAgencies">
                    <digi:trn key="aim:beneficiary2Agency">Beneficiary Agency</digi:trn>
                    <br />
                    <div id="act_benAgencies_agency" style="display: block;">
                        <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                            <tr>
                                <td>
                                    <logic:iterate name="aimEditActivityForm" property="agencies.benAgencies" id="benAgency"
                                                   type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
                                        <ul>
                                            <li>
                                                <span class="word_break bold"><bean:write name="benAgency" property="name" /></span>
                                                <c:set var="tempOrgId">${benAgency.ampOrgId}</c:set>
                                                <!-- Additional Info field not found in the new activity form-->
                                                <logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgToInfo(${tempOrgId})">
                                                    <span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.benOrgToInfo[tempOrgId]}"/>)</span>
                                                </logic:notEmpty>
                                                <ampModule:display name="/Activity Form/Organizations/Beneficiary Agency/percentage" parentModule="/Activity Form/Organizations/Beneficiary Agency">
                                                    <logic:notEmpty name="aimEditActivityForm" property="agencies.benOrgPercentage(${tempOrgId})" >
                                                        <c:out value="${aimEditActivityForm.agencies.benOrgPercentage[tempOrgId]}" /> %
                                                    </logic:notEmpty>
                                                </ampModule:display>
                                            </li>
                                        </ul>
                                    </logic:iterate>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <hr />
                </logic:notEmpty>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Organizations/Contracting Agency" parentModule="/Activity Form/Organizations">
                <logic:notEmpty name="aimEditActivityForm" property="agencies.conAgencies">
                    <digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn>
                    <br />
                    <div id="act_contracting_agency" style="display: block;">
                        <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                            <tr>
                                <td>
                                    <logic:iterate name="aimEditActivityForm" property="agencies.conAgencies" id="conAgencies"
                                                   type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
                                        <ul>
                                            <li>
                                                <span class="word_break bold"><bean:write name="conAgencies" property="name" /></span>
                                                <c:set var="tempOrgId">${conAgencies.ampOrgId}</c:set>
                                                <logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgToInfo(${tempOrgId})">
                                                    <span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.conOrgToInfo[tempOrgId]}"/> )</span>
                                                </logic:notEmpty>
                                                <ampModule:display name="/Activity Form/Organizations/Contracting Agency/percentage" parentModule="/Activity Form/Organizations/Contracting Agency">
                                                    <logic:notEmpty name="aimEditActivityForm" property="agencies.conOrgPercentage(${tempOrgId})" >
                                                        <c:out value="${aimEditActivityForm.agencies.conOrgPercentage[tempOrgId]}" /> %
                                                    </logic:notEmpty>
                                                </ampModule:display>
                                            </li>
                                        </ul>
                                    </logic:iterate>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <hr />
                </logic:notEmpty>
            </ampModule:display>


            <!--SECTOR GROUP SECTION -->
            <ampModule:display name="/Activity Form/Organizations/Sector Group" parentModule="/Activity Form/Organizations">
                <logic:notEmpty name="aimEditActivityForm" property="agencies.sectGroups">
                    <digi:trn key="aim:sectorGroup">Sector Group</digi:trn>
                    <br/>

                    <div id="act_sectGroups_agency" style="display: block;">
                        <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                            <tr>
                                <td>
                                    <logic:iterate name="aimEditActivityForm" property="agencies.sectGroups" id="sectGroup"
                                                   type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
                                        <ul>
                                            <li>
                                                <span class="word_break bold"><bean:write name="sectGroup" property="name" /></span>
                                                <c:set var="tempOrgId">${sectGroup.ampOrgId}</c:set>

                                                <logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgToInfo(${tempOrgId})">
                                                    <span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.sectOrgToInfo[tempOrgId]}"/> )</span>
                                                </logic:notEmpty>
                                                <ampModule:display name="/Activity Form/Organizations/Sector Group/percentage" parentModule="/Activity Form/Organizations/Sector Group">
                                                    <logic:notEmpty name="aimEditActivityForm" property="agencies.sectOrgPercentage(${tempOrgId})" >
                                                        <c:out value="${aimEditActivityForm.agencies.sectOrgPercentage[tempOrgId]}" /> %
                                                    </logic:notEmpty>
                                                </ampModule:display>
                                            </li>
                                        </ul>
                                    </logic:iterate></td>
                            </tr>
                        </table>
                    </div>
                    <hr />
                </logic:notEmpty>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Organizations/Regional Group" parentModule="/Activity Form/Organizations">
                <logic:notEmpty name="aimEditActivityForm" property="agencies.regGroups">
                    <digi:trn key="aim:regionalGroup">Regional Group</digi:trn>
                    <br/>

                    <div id="act_regGroups_agency" style="display: block;">
                        <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                            <tr>
                                <td>
                                    <logic:iterate name="aimEditActivityForm" property="agencies.regGroups" id="regGroup"
                                                   type="org.digijava.ampModule.aim.dbentity.AmpOrganisation">
                                        <ul>
                                            <li>
                                                <span class="word_break bold"><bean:write name="regGroup" property="name" /></span>
                                                <c:set var="tempOrgId" >${regGroup.ampOrgId}</c:set>
                                                <logic:notEmpty property="agencies.regOrgToInfo(${tempOrgId})" name="aimEditActivityForm">
                                                    <span class="word_break bold">(<c:out value="${aimEditActivityForm.agencies.regOrgToInfo[tempOrgId]}"/>)</span>
                                                </logic:notEmpty>
                                                <ampModule:display name="/Activity Form/Organizations/Regional Group/percentage" parentModule="/Activity Form/Organizations/Regional Group">
                                                    <logic:notEmpty name="aimEditActivityForm" property="agencies.regOrgPercentage(${tempOrgId})" >
                                                        <c:out value="${aimEditActivityForm.agencies.regOrgPercentage[tempOrgId]}" /> %
                                                    </logic:notEmpty>
                                                </ampModule:display>
                                            </li>
                                        </ul>
                                    </logic:iterate>								</td>
                            </tr>
                        </table>
                    </div>
                </logic:notEmpty>
            </ampModule:display>
        </div>
    </fieldset>
</ampModule:display>
