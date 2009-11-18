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
                    <td width="30%"><digi:trn>Group</digi:trn>:</td><td>${orgGroup.orgGrpName}&nbsp;</td>
                </tr>
                 <tr>
                    <td width="30%"><digi:trn>Type</digi:trn>:</td>
                    <td><c:choose>
                            <c:when test="${not empty orgGroup.orgType}">${orgGroup.orgType}</c:when>
                            <c:otherwise>${organization.orgGrpId.orgType}</c:otherwise>&nbsp;
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Organization Name</digi:trn>:</td><td>${organization.name}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Organization Acronym</digi:trn>:</td><td>${organization.acronym}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Donor Group</digi:trn>:</td><td>${organization.orgGrpId.orgGrpName}&nbsp;</td>
                </tr>
                <tr>
                    <td width="30%"><digi:trn>Web Link</digi:trn>:</td><td>${organization.orgUrl}&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
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
                    
                    <c:forEach var="contact" items="${organization.contacts}">
                        <tr>
                            <td class="tdClass" nowrap>
                                ${contact.lastname}
                            </td>
                            <td class="tdClass" nowrap>
                                ${contact.name}
                            </td>
                            <td class="tdClass" nowrap>
                                ${contact.email}
                            </td>
                            <td class="tdClass">
                                ${contact.phone}&nbsp;
                            </td>
                            <td class="tdClass">
                                ${contact.fax}&nbsp;
                            </td>
                            <td class="tdClass">
                                ${contact.title}&nbsp;
                            </td>
                        </tr>
                        </c:forEach>
                </table>       
        </td>
    </tr>
    <tr>
        <td height="210px" valign="top">
            <jsp:include page="/orgProfile/showLargestProjects.do" flush="true"/>
        </td>
    </tr>
</table>


 
