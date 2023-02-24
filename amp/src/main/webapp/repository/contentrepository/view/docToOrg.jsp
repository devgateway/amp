<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<%-- This file renders the "Add Organisations" popup table --%>


<digi:instance property="crDocToOrgForm" />
<bean:define id="myForm" name="crDocToOrgForm" toScope="page" type="org.digijava.module.contentrepository.form.DocToOrgForm" />

<logic:notEmpty name="myForm" property="messages">
	<c:forEach var="msg" items="${myForm.messages}">
		<div style="text-align: center;">	
			 <font color="#FF0000">${msg}</font>
		</div>
	</c:forEach>	
</logic:notEmpty>

<logic:notEmpty name="myForm" property="orgs">
	<ul>
		<c:forEach var="org" items="${myForm.orgs}">
			<li>
				<span class="t_sm">${org.acronym} ( ${org.name} )</span>
				<c:if test="${myForm.hasAddParticipatingOrgRights}">
					<img onClick="deleteDocToOrgObj('${myForm.uuidForOrgsShown}', ${org.ampOrgId});"  style="cursor:pointer; text-decoration:underline;"
					title="<digi:trn>Click here to remove this organisation</digi:trn>"
					hspace="2" src= "/repository/contentrepository/view/images/trash_12.gif" border="0" />
				</c:if>
			</li>
		</c:forEach>
	</ul>
</logic:notEmpty>

<logic:empty name="myForm" property="orgs">
	<span class="t_sm">
		<digi:trn>No organizations have participated in creating this document</digi:trn>
		<br />
	</span>	
</logic:empty>


<br />


<c:if test="${myForm.hasAddParticipatingOrgRights}">
	<aim:addOrganizationButton callBackFunction="showOrgsPanel();"  refreshParentDocument="false" collection="addedOrgs" 
				form="${crDocToOrgForm}" styleClass="buttonx"><digi:trn>Add Organizations</digi:trn></aim:addOrganizationButton>
	 
	 <c:set var="trnclose"><digi:trn>Close</digi:trn></c:set>
	 <input class="buttonx" type="button" name="close" value="${trnclose}" onclick="organisationPanel.hide(); window.location = window.location.href;">
</c:if>

