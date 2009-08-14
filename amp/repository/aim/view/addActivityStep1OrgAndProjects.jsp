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
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<script language="JavaScript">
	function  submitAfterSelectingOrg()
	{
		<digi:context name="nextTarget" property="context/module/moduleinstance/addActivity.do" />
		
    	document.aimEditActivityForm.action = "<%= nextTarget %>";
    	document.aimEditActivityForm.target = "_self";
    	document.aimEditActivityForm.editKey.value = null;
  		document.aimEditActivityForm.step.value = "1";
    	document.aimEditActivityForm.submit();
    	//return true;
	}
</script>
<digi:instance property="aimEditActivityForm" />
<bean:define id="identification" name="aimEditActivityForm" property="identification"></bean:define>

										<table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#d7eafd">
											<tr>
												<td align="left"><b>
													<a title="<digi:trn key="aim:TrackActivitiesintheDonorsInternalDatabase">Facilitates tracking activities in donors' internal databases </digi:trn>">
													<digi:trn key="aim:orgsAndProjectIds">Organizations and Project IDs</digi:trn>
													</a></b>
												</td>
											</tr>
											<tr>
												<td bgcolor="#ffffff" width="100%">

										<table cellPadding=1 cellSpacing=1 border=0 bgcolor="#ffffff" width="100%">
											<logic:empty name="identification" property="selectedOrganizations">
												<td>
													<a title="<digi:trn key="aim:TrackActivitiesintheDonorsInternalDatabase">Facilitates tracking activities in donors' internal databases </digi:trn>">
													
												<aim:addOrganizationButton collection="selectedOrganizations" delegateClass="org.digijava.module.aim.uicomponents.ProjectIdPostProcessDelegate" form="${identification}" refreshParentDocument="false"  callBackFunction="submitAfterSelectingOrg();" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
														
													
													</td>
											</logic:empty>
											<logic:notEmpty name="identification" property="selectedOrganizations">
											<td>
												<table cellSpacing=1 cellPadding=1 border=0 width="500">
												<c:forEach items="${identification.selectedOrganizations}" var="selectedOrganizations">
													<tr>

														<c:if test="${!empty selectedOrganizations.id}">
															<td align="left" width=3>
																<html:multibox styleId="selOrgs" property="identification.selOrgs">
																	<c:out value="${selectedOrganizations.id}"/>
																</html:multibox>
															</td>
															<td align="left" width="367">
																<c:out value="${selectedOrganizations.organisation.name}"/>
															</td>															
															<td align="left" width="130">
																<html:text name="selectedOrganizations"
																property="projectId" indexed="true"
																styleClass="inp-text" size="15"/>
															</td>														
													  </c:if>	
													</tr>
													</c:forEach>
													<tr><td colspan="3">
														<table cellSpacing=2 cellPadding=2>
															<tr>
																<td>
																	<aim:addOrganizationButton collection="selectedOrganizations" delegateClass="org.digijava.module.aim.uicomponents.ProjectIdPostProcessDelegate" form="${identification}" refreshParentDocument="false" callBackFunction="submitAfterSelectingOrg();" styleClass="dr-menu"> <digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
																</td>
																<td>
																	<input type="button" value="<digi:trn key="btn:removeOrganizations">Remove Organizations</digi:trn>" class="dr-menu"
																	onclick="return removeSelOrganisations()">
																</td>
															</tr>
														</table>
													</td></tr>
												</table>
											</td>
											</logic:notEmpty>
											</tr>
										</table>

												</td>
											</tr>
										</table>

