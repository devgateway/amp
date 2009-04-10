<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

 <script langauage="JavaScript">
    var helpTitle="<digi:trn>Project Title Full Text</digi:trn>";
</script>

<digi:instance property="orgProfLargestProjectsForm"/>
<table width="100%">
<tr>
<td valign="top">
<table border="1" id="lgProjects" bgcolor="#dddddd" width="100%"  >
    <th colspan="3" class="tableHeaderCls">5 <digi:trn>Largest projects </digi:trn>(${sessionScope.orgProfileFilter.year-1})</th>
    <tr colspan="3">
        <td class="tableHeaderCls"><digi:trn>Project title</digi:trn></td>
        <td class="tableHeaderCls"><digi:trn>Commitment</digi:trn></td>
        <td class="tableHeaderCls"><digi:trn>Sector</digi:trn></td>
    </tr>
     
          <c:forEach items="${orgProfLargestProjectsForm.projects}" var="project"  >
               <tr>
                  <td nowrap>
                   <c:choose>
                       <c:when test="${empty project.fullTitle}">
                           <digi:link module="aim" href="/selectActivityTabs.do~ampActivityId=${project.activityId}">${project.title}</digi:link>
                       </c:when>
                       <c:otherwise>
                           <digi:link module="aim" href="/selectActivityTabs.do~ampActivityId=${project.activityId}" onmouseover="stm([helpTitle,'${project.fullTitle}'],Style[11])" onmouseout="htm()">${project.title}</digi:link>
                       </c:otherwise>
                   </c:choose>
                   </td>
               <td align="center">${project.amount}</td>
               <td>
                ${project.sectorNames}
               </td>
               </tr>
          </c:forEach>
    
 </table>
 </td>
</tr>
</table>
<script language="javascript">
setStripsTable("lgProjects", "tableEven", "tableOdd");
setHoveredTable("lgProjects");
</script>

