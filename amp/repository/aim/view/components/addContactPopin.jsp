<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/aim" prefix="aim"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<div class="admin-content contact-form">
<digi:context name="digiContext" property="context" />
<digi:instance property="aimAddContactForm" />
<html:hidden name="aimAddContactForm" property="contactId"
	styleId="contactId" />
<html:hidden name="aimAddContactForm" property="temporaryId"
	styleId="temporaryId" />

<digi:form name="contactForm" type="aimAddContactForm"
	action="/addAmpContactInfo.do" method="post">
	<c:choose>
		<c:when test="${aimAddContactForm.action=='search'||aimAddContactForm.action=='checkDuplicationContacts'}">
		<div style="font-size:12px; margin-bottom:5px;"><digi:trn>Please search a contact before adding a new one to avoid contact duplications in database</digi:trn></div>
			<table style="margin-bottom:10px;">
				<tr>
					<td  style="font-size:12px;"><strong><digi:trn>Firstname</digi:trn></strong></td>
					<td ><html:text property="firstName" styleClass="inputx" size="30"
						styleId="name" onkeypress="checkKeyAndSearch(event);"/></td>

					<td  style="font-size:12px;"><strong><digi:trn>Lastname</digi:trn></strong></td>
					<td  ><html:text property="lastname" styleClass="inputx" size="30"
						styleId="lastname" onkeypress="checkKeyAndSearch(event);"/></td>
					<td >
					<html:button property="submitButton"
						onclick="return searchContact()" styleId="searchBtn" styleClass="buttonx_sm">
						<digi:trn>Search</digi:trn>
					</html:button></td>
					<td ><html:button property="submitButton"
						onclick="myPanelContact.hide()" styleClass="buttonx_sm">
						<digi:trn>Cancel</digi:trn>
					</html:button></td>
				</tr>
			</table>
			<center><c:if test="${aimAddContactForm.action=='search'}">
			<c:if test="${not empty aimAddContactForm.contacts}"></center>
				<hr />
				<center><b style="font-size:12px;"><digi:trn>Contact with same First/Last Names</digi:trn></b></center>
				<div>
				<table width="100%" cellPadding="0" cellspacing="0" border="0" class="inside">
				<tr>
				<td class="inside_header" style="border-style: none;width:5%;text-align: left;">&nbsp;</td>
				<td class="inside_header"  style="border-style: none;width:15%;text-align: left"><b><digi:trn>FirstName</digi:trn></b></td>
				<td class="inside_header"  style="border-style: none;width:15%;text-align: left"><b><digi:trn>LastName</digi:trn></b></td>
				<td class="inside_header" style="border-style: none;width:25%;text-align: left"><b><digi:trn>Email</digi:trn></b></td>
				<td class="inside_header" style="border-style: none;width:20%;text-align: left"><b><digi:trn>Organization</digi:trn></b></td>
				<td class="inside_header"  style="border-style: none;width:20%;text-align: left"><b><digi:trn>Phone</digi:trn></b></td>
				</tr>
				</table>	
				<div style="height:300px;overflow: auto;">
				<table width="100%" cellPadding="0" cellspacing="0" border="0"  class="inside">
					<c:forEach var="contact" items="${aimAddContactForm.contacts}">
						<tr>
							<td class="inside"  width="5%">
							<html:radio property="selContactIds" value="${contact.id}"></html:radio>
							</td>
							<td class="inside"  width="15%">
								<c:choose>
									<c:when test="${fn:length(contact.name)>15}">
										<span title="${contact.name}">${fn:substring(contact.name,0,12)}...</span> 	
									</c:when>
									<c:otherwise>
											<c:out value="${contact.name}"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td class="inside"  width="15%">
								<c:choose>
									<c:when test="${fn:length(contact.lastname)>15}">
										<span title="${contact.lastname}">${fn:substring(contact.lastname,0,12)}...</span> 	
									</c:when>
									<c:otherwise>
										<c:out value="${contact.lastname}"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td class="inside"  width="25%">
							<ul>
							<c:forEach var="email" items="${contact.properties}">
									<c:if test="${email.name=='contact email'}">
									<c:choose>
									<c:when test="${fn:length(email.value)>20}">
										<li title="${email.value}">${fn:substring(email.value,0,16)}...</li> 	
									</c:when>
									<c:otherwise>
										<li><c:out value="${email.value}"/></li>
									</c:otherwise>
									</c:choose>
									</c:if>
							</c:forEach>
							</ul>
							</td>
							<td class="inside" width="20%">
							<ul>
							 <c:if test="${not empty contact.organisationName}">
							 	<c:choose>
									<c:when test="${fn:length(contact.organisationName)>13}">
										<li title="${contact.organisationName}">${fn:substring(contact.organisationName,0,10)}...</li> 	
									</c:when>
									<c:otherwise>
										<li><c:out value="${contact.organisationName}"/></li>
									</c:otherwise>
								</c:choose>								
							</c:if>
							<c:forEach var="contOrg" items="${contact.organizationContacts}">
								<c:choose>
									<c:when test="${fn:length(contOrg.organisation.name)>13}">
										<li title="${contOrg.organisation.name}">${fn:substring(contOrg.organisation.name,0,10)}...</li> 	
									</c:when>
									<c:otherwise>
										<li><c:out value="${contOrg.organisation.name}"/></li>
									</c:otherwise>
								</c:choose>
										
							</c:forEach>
							</ul>
							</td>
							<td class="inside" width="20%">
							<ul>
							<c:forEach var="phone" items="${contact.properties}">
									<c:if test="${phone.name=='contact phone'}">
									<c:choose>
									<c:when test="${fn:length(phone.actualPhoneNumber)>13}">
										<li title="${phone.actualPhoneNumber}"><digi:trn>${phone.phoneCategory}</digi:trn>&nbsp;${fn:substring(phone.actualPhoneNumber,0,10)}...</li> 	
									</c:when>
									<c:otherwise>
										<li><digi:trn><c:out value="${phone.phoneCategory}"/></digi:trn>&nbsp;<c:out value="${phone.actualPhoneNumber}"/></li>
									</c:otherwise>
									</c:choose>
										
									</c:if>
							</c:forEach>
							</ul>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</div>
				</div>
				<center>
				<html:button styleClass="buttonx_sm" property="addButton"
					onclick="addSelectedContacts()">
					<digi:trn>Add</digi:trn>
				</html:button>
				</c:if>
				<html:button styleClass="buttonx_sm" property="addButton"
					onclick="createNewContact()">
					<digi:trn>Create New Contact</digi:trn>
				</html:button>
				
				<html:button styleClass="buttonx_sm" property="submitButton"
					onclick="myPanelContact.hide()">
					<digi:trn>Cancel</digi:trn>
				</html:button>
				</center>
			</c:if>
	
		</c:when>
		<c:otherwise>
			<table cellpadding="2" cellspacing="5" width="100%" height="100%"
				class="box-border-nopadding">
				<tr height="5px">
					<td colspan="2" />
				</tr>
				<tr>
					<td align="right"><strong><digi:trn>Title</digi:trn></strong></td>
					<td align="left"><c:set var="translation">
						<digi:trn>Please select from below</digi:trn>
					</c:set> <category:showoptions multiselect="false"
						firstLine="${translation}" name="aimAddContactForm"
						property="title"
						keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.CONTACT_TITLE_KEY%>"
						styleClass="selectStyle" outerid="contactTitle" /></td>
				</tr>
				<tr>
					<td align="right"><strong><digi:trn>Firstname</digi:trn></strong><font
						color="red">*</font></td>
					<td align="left"><html:text property="firstName" size="30"
						styleId="firstName" /></td>
				</tr>
				<tr>
					<td align="right"><strong><digi:trn>Lastname</digi:trn></strong><font
						color="red">*</font></td>
					<td align="left"><html:text property="lastname" size="30"
						styleId="lastname" /></td>
				</tr>
				<tr>
					<td align="right"><strong><digi:trn>Email</digi:trn></strong>
					<td align="left" nowrap="nowrap"><logic:notEmpty
						name="aimAddContactForm" property="emails">
						<logic:iterate name="aimAddContactForm" property="emails" id="foo"
							indexId="ctr">
							<div><html:text name="aimAddContactForm"
								property="emails[${ctr}].value" size="30" styleId="email_${ctr}" />
							<a href="javascript:removeData('email',${ctr})"> <img
								src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif"
								vspace="2" border="0" /> </a> <c:if
								test="${ctr==aimAddContactForm.emailsSize-1}">
								<c:set var="trnadd">
									<digi:trn>Add New</digi:trn>
								</c:set>
								<input id="addEmailBtn"
									style="font-family: verdana; font-size: 11px;" type="button"
									name="addValBtn" value="${trnadd}"
									onclick="addNewData('email')">
							</c:if></div>
						</logic:iterate>
					</logic:notEmpty> <logic:empty name="aimAddContactForm" property="emails">
						<c:set var="trnadd">
							<digi:trn>Add New</digi:trn>
						</c:set>
						<input id="addEmailBtn"
							style="font-family: verdana; font-size: 11px;" type="button"
							name="addValBtn" value="${trnadd}" onclick="addNewData('email')">
					</logic:empty></td>
				</tr>
				<tr>
					<td align="right"><strong><digi:trn>Function</digi:trn></strong></td>
					<td align="left"><html:text property="function" size="30"
						styleId="function" /></td>
				</tr>
				
				<tr>
					<td align="right" valign="top"><strong><digi:trn>Phone Number</digi:trn></strong></td>
					<td align="left" nowrap="nowrap">
						<logic:notEmpty	name="aimAddContactForm" property="phones">
							<logic:iterate name="aimAddContactForm" property="phones" id="foo" indexId="ctr">
								<div>
									<c:set var="translationNone"><digi:trn>None</digi:trn></c:set> 
									<category:showoptions multiselect="false"	firstLine="${translationNone}" name="aimAddContactForm"	property="phones[${ctr}].phoneTypeId"
									keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.CONTACT_PHONE_TYPE_KEY%>" styleClass="selectStyle" outerid="phoneType_${ctr}" /> <%--
											<html:text name="aimAddContactForm" property="phones[${ctr}].phoneType" size="10" styleId="phoneType_${ctr}"/>																															 																																	 	
											--%> 
									<html:text name="aimAddContactForm"	property="phones[${ctr}].value" size="16" styleId="phoneNum_${ctr}" /> 
									<a href="javascript:removeData('phone',${ctr})"> 
										<img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif"	vspace="2" border="0" />
									</a> 
									<c:if test="${aimAddContactForm.phonesSize==0 ||  ctr==aimAddContactForm.phonesSize-1}">
										<c:set var="trnadd">
											<digi:trn>Add New</digi:trn>
										</c:set>
										<input id="addPhoneBtn"	style="font-family: verdana; font-size: 11px;" type="button" name="addValBtn" value="${trnadd}"
											onclick="addNewData('phone')">
									</c:if>
								</div>
							</logic:iterate>
						</logic:notEmpty> 
						<logic:empty name="aimAddContactForm" property="phones">
							<c:set var="trnadd">
								<digi:trn>Add New</digi:trn>
							</c:set>
							<input id="addPhoneBtn"	style="font-family: verdana; font-size: 11px;" type="button"
								name="addValBtn" value="${trnadd}" onclick="addNewData('phone')">
						</logic:empty>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top"><strong><digi:trn>Fax</digi:trn></strong></td>
					<td align="left" nowrap="nowrap"><logic:notEmpty
						name="aimAddContactForm" property="faxes">
						<logic:iterate name="aimAddContactForm" property="faxes" id="foo"
							indexId="ctr">
							<div><html:text name="aimAddContactForm"
								property="faxes[${ctr}].value" size="30" styleId="fax_${ctr}" />
							<a href="javascript:removeData('fax',${ctr})"> <img
								src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif"
								vspace="2" border="0" /> </a> <c:if
								test="${ctr==aimAddContactForm.faxesSize-1}">
								<c:set var="trnadd">
									<digi:trn>Add New</digi:trn>
								</c:set>
								<input id="addFaxBtn"
									style="font-family: verdana; font-size: 11px;" type="button"
									name="addValBtn" value="${trnadd}" onclick="addNewData('fax')">
							</c:if></div>
						</logic:iterate>
					</logic:notEmpty> <logic:empty name="aimAddContactForm" property="faxes">
						<c:set var="trnadd">
							<digi:trn>Add New</digi:trn>
						</c:set>
						<input id="addFaxBtn"
							style="font-family: verdana; font-size: 11px;" type="button"
							name="addValBtn" value="${trnadd}" onclick="addNewData('fax')">
					</logic:empty></td>
				</tr>
				<tr>
					<td align="right" valign="top"><strong><digi:trn>Office Address</digi:trn></strong></td>
					<td align="left"><html:textarea property="officeaddress"
						cols="36" rows="3" styleId="officeaddress" /></td>
				</tr>
				<tr height="5px">
					<td colspan="2" />
				</tr>
				<tr>
					<td colspan="2" align="center"><html:button property=""
						styleClass="dr-menu" onclick="saveContact()">
						<digi:trn>Save</digi:trn>
					</html:button> <c:if test="${aimAddContactForm.action=='edit'}">
						<html:button styleClass="dr-menu" property="submitButton"
							onclick="myPanelContact.hide()">
							<digi:trn>Close</digi:trn>
						</html:button>
					</c:if></td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>
</digi:form>
</div>
