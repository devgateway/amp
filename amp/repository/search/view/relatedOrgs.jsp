<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<logic:present name="resultActivitiesWithOrgs" scope="request">
    <tr>
        <td bgcolor="#dadada">
            <img id="activityRows${requestScope.relatedOrgIndex}_plus"  onclick="toggleResultsGroup('activityRows'+${requestScope.relatedOrgIndex})" src="/TEMPLATE/ampTemplate/images/arrow_right.gif" align="absmiddle" style="float:left;"/>
            <img id="activityRows${requestScope.relatedOrgIndex}_minus" onclick="toggleResultsGroup('activityRows'+${requestScope.relatedOrgIndex})" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display:none;float:left;" align="absmiddle"/>
            &nbsp;&nbsp;${fn:length(requestScope.resultActivitiesWithOrgs)} <strong><digi:trn>activities using this organization as ${requestScope.relatedOrgType}. Please try another keyword</digi:trn></strong>
        </td>
    </tr>
    <tr>
        <td width="100%">
            <div id="div_activityRows${requestScope.relatedOrgIndex}" style="display : none;width:100%;">
                <table width="100%" cellpadding="2" cellspacing="2" id="dataTable1">
                    <c:forEach var="item" items="${requestScope.resultActivitiesWithOrgs}">
                        <tr>
                            <td>
                        <digi:link module="search" href="/search.do?ampActivityId=${item.ampActivityId}"><bean:write name="item" property="objectName" /></digi:link><br />
                        </td>
                        </tr>
                       </c:forEach> 
                </table>
            </div>
        </td>
    </tr>
</logic:present>