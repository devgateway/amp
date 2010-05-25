<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script type="text/javascript">
    function showLargestProjectsHelpTooltip(count) {
        var div=document.getElementById("largestProjectsHelpTooltip"+count);
        var offset=getTopOffset(div.parentNode);
        div.style.top=(offset)+"px";
        div.style.display = "block";
    }
    function getTopOffset(element){
        var offset = 0;
        while( element != null ) {
            offset += element.offsetTop;
            element = element.offsetParent;
        }
        return offset;
    }

    function hideLargestProjectsHelpTooltip(count) {
        document.getElementById("largestProjectsHelpTooltip"+count).style.display = "none";
    }
</script>
<digi:instance property="orgProfLargestProjectsForm"/>
<table border="0"  bgcolor="#dddddd" width="100%"  class="tableElement" cellspacing="0" cellpadding="0">
    <tr>
        <th colspan="4" class="tableHeaderCls">
            <c:choose>
                <c:when test="${sessionScope.orgProfileFilter.largestProjectNumb==-1}">
                    <digi:trn>All</digi:trn>
                </c:when>
                <c:otherwise>
                    ${sessionScope.orgProfileFilter.largestProjectNumb}
                </c:otherwise>
            </c:choose><digi:trn>LARGEST PROJECTS</digi:trn>(${sessionScope.orgProfileFilter.year-1})</th>
    </tr>
    <tr>
        <td class="tableHeaderCls"><digi:trn>Project title</digi:trn></td>
        <td class="tableHeaderCls"><digi:trn>Commitment</digi:trn>(${sessionScope.orgProfileFilter.currName})</td>
        <c:if test="${sessionScope.orgProfileFilter.transactionType==2}">
            <td class="tableHeaderCls"><digi:trn>Disbursement</digi:trn>(${sessionScope.orgProfileFilter.currName})</td>
        </c:if>
        <td class="tableHeaderCls"><digi:trn>Sector</digi:trn></td>
    </tr>

    <c:forEach items="${orgProfLargestProjectsForm.projects}" var="project"  varStatus="status">
        <tr>
            <td nowrap>
                <c:choose>
                    <c:when test="${empty project.fullTitle}">
                        <digi:link module="aim" href="/selectActivityTabs.do~ampActivityId=${project.activityId}">${project.title}</digi:link>
                    </c:when>
                    <c:otherwise>
                        <digi:link module="aim" href="/selectActivityTabs.do~ampActivityId=${project.activityId}" onmouseover="showLargestProjectsHelpTooltip(${status.count})" onmouseout="hideLargestProjectsHelpTooltip(${status.count})">${project.title}</digi:link>
                    </c:otherwise>
                </c:choose>
                <div id="largestProjectsHelpTooltip${status.count}" style="display:none; z-index:10; width:200px; position: absolute; left:70px;  background-color: #ffffff; border: 1px solid silver;">
                    <TABLE WIDTH='100%' BORDER='0' CELLPADDING='0' CELLSPACING='0'>
                        <TR style="background-color:#cc0000"><TH style="color:#FFFFFF" nowrap><digi:trn>Project Title Full Text</digi:trn></TH></TR>
                        <TR style="background-color:#FFCC99"><TD>${project.fullTitle}</TD></TR>
                    </TABLE>
                </div>
            </td>
            <td align="center">${project.amount}</td>
            <c:if test="${sessionScope.orgProfileFilter.transactionType==2}">
                <td align="center">${project.disbAmount}</td>
            </c:if>
            <td>
                ${project.sectorNames}
            </td>
        </tr>
    </c:forEach>

</table>

