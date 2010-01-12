<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>


<table width="100%" border="0" cellSpacing="1" cellPadding="5">
    <tr>
        <td height="110px" valign="top" >
            <table class="tableElement" border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="2" class="tableHeaderCls"><digi:trn>Organization Profile</digi:trn></th>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Type</digi:trn>:</td>
                    <td>
                        <c:choose>
                         <c:when test="${orgsCount==1}">
                            ${organization.orgGrpId.orgType}
                        </c:when>
                        <c:when test="${empty orgGroup&&orgsCount==0}">
                            <digi:trn>All</digi:trn>
                        </c:when>
                        <c:when test="${orgsCount>0}">
                             <digi:trn>Multiple Organizations Selected</digi:trn>
                        </c:when>
                        <c:otherwise>
                            <digi:trn>${orgGroup.orgType}</digi:trn>
                        </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Organization Name</digi:trn>:</td>
                    <td>
                        <c:choose>
                            <c:when test="${orgsCount>1}">
                                <digi:trn>Multiple Organizations Selected</digi:trn>
                            </c:when>
                            <c:when test="${orgsCount==1}">
                                ${organization.name}&nbsp;
                            </c:when>
                            <c:otherwise>
                                <digi:trn>N/A</digi:trn>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Organization Acronym</digi:trn>:</td>
                    <td>
                        <c:choose>
                            <c:when test="${orgsCount>1}">
                                <digi:trn>Multiple Organizations Selected</digi:trn>
                            </c:when>
                            <c:when test="${orgsCount==1}">
                                ${organization.acronym}&nbsp;
                            </c:when>
                            <c:otherwise>
                                <digi:trn>N/A</digi:trn>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Donor Group</digi:trn>:</td>
                    <td>
                        <c:choose>
                            <c:when test="${orgsCount==1}">
                                ${organization.orgGrpId.orgGrpName}
                            </c:when>
                            <c:when test="${empty orgGroup&&orgsCount==0}">
                                <digi:trn>All</digi:trn>
                            </c:when>
                            <c:when test="${orgsCount>0}">
                                <digi:trn>Multiple Organizations Selected</digi:trn>
                            </c:when>
                            <c:otherwise>
                                ${orgGroup.orgGrpName}
                            </c:otherwise>  
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Web Link</digi:trn>:</td>
                    <td>
                        <c:choose>
                            <c:when test="${orgsCount>1}">
                                <digi:trn>Multiple Organizations Selected</digi:trn>
                            </c:when>
                            <c:when test="${orgsCount==1}">
                                ${organization.orgUrl}&nbsp;
                            </c:when>
                            <c:otherwise>
                                <digi:trn>N/A</digi:trn>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <c:if test="${orgsCount!=1}">
                    <tr>
                        <td width="30%"><digi:trn>Contact</digi:trn>:</td>
                        <td>
                            <c:choose>
                                <c:when test="${orgsCount>1}">
                                    <digi:trn>Multiple Organizations Selected</digi:trn>
                                </c:when>
                                <c:otherwise>
                                    <digi:trn>N/A</digi:trn>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
    <c:if test="${orgsCount==1}">
        <tr>
            <td>

                <table width="100%" cellSpacing="0" cellPadding="0" align="left"  class="tableElement" border="0">
                    <thead>
                        <tr>
                            <th colspan="6" class="tableHeaderCls"><digi:trn>Contact Information</digi:trn></th>
                        </tr>
                        <tr>
                            <th class="tableHeaderCls">
                                <digi:trn>LAST NAME</digi:trn>
                            </th>
                            <th class="tableHeaderCls">
                                <digi:trn>FIRST NAME</digi:trn>
                            </th>
                            <th class="tableHeaderCls">
                                <digi:trn>EMAIL </digi:trn>
                            </th>
                            <th class="tableHeaderCls">
                                <digi:trn> TELEPHONE </digi:trn>
                            </th>
                            <th class="tableHeaderCls">
                                <digi:trn> FAX </digi:trn>
                            </th>
                            <th class="tableHeaderCls">
                                <digi:trn>TITLE </digi:trn>
                            </th>
                        </tr>
                    </thead>

                    <c:forEach var="orgContact" items="${organization.organizationContacts}">
                    	<c:if test="${not empty orgContact.primaryContact && orgContact.primaryContact==true}">
                    		 <tr>
	                            <td class="tdClass" nowrap>
	                                ${orgContact.contact.lastname}
	                            </td>
	                            <td class="tdClass" nowrap>
	                                ${orgContact.contact.name}
	                            </td>
	                            <td class="tdClass" nowrap>
	                                <c:forEach var="property" items="${orgContact.contact.properties}">
										<c:if test="${property.name=='contact email'}">
											<div>${property.value}</div>
										</c:if>
									</c:forEach>
	                            </td>
	                            <td class="tdClass">
	                            	<c:forEach var="property" items="${orgContact.contact.properties}">
										<c:if test="${property.name=='contact phone'}">
											<div>${property.value}</div>
										</c:if>
									</c:forEach>
	                            </td>
	                            <td class="tdClass">
	                            	<c:forEach var="property" items="${orgContact.contact.properties}">
										<c:if test="${property.name=='contact fax'}">
											<div>${property.value}</div>
										</c:if>
									</c:forEach>                               
	                            </td>
	                            <td class="tdClass">
	                                ${orgContact.contact.title}&nbsp;
	                            </td>
	                        </tr>
                    	</c:if>                       
                    </c:forEach>
                </table>
            </td>
        </tr>
    </c:if>

    <tr>
        <td height="210px" valign="top">
            <jsp:include page="/orgProfile/showLargestProjects.do" flush="true"/>
        </td>
    </tr>
</table>



