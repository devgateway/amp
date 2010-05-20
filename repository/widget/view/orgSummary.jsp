<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<script type="text/javascript">
    function printLargestProjects(){
         openURLinResizableWindow("/orgProfile/printLargestProjects.do", 780, 500);
    }
</script>

<div>
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
    <c:if test="${orgsCount==1}">
        <tr>
            <td width="30%"><digi:trn>Contact Name</digi:trn>:</td><td>${organization.contactPersonName}&nbsp;</td>
        </tr>
        <tr>
            <td width="30%"><digi:trn>Contact Phone</digi:trn>:</td><td>${organization.phone}&nbsp;</td>
        </tr>
        <tr>
            <td width="30%"><digi:trn>Contact Email</digi:trn>:</td><td>${organization.email}&nbsp;</td>
        </tr>
    </c:if>
</table>
<div style="float:left">
<a href="javascript:printLargestProjects()">
    <img alt="print preview" src="/TEMPLATE/ampTemplate/images/print_icon.gif" border="0" >
</a>
</div>
<jsp:include page="/orgProfile/showLargestProjects.do" flush="true"/>

</div>

<script language="javascript" type="text/javascript">
    setStripsTable("tableEven", "tableOdd");
    setHoveredTable();
</script>

