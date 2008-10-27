<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<digi:instance property="orgProfLargestProjectsForm"/>
    <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

<table border="1">
    <th colspan="3">5 Largest projects</th>
    <tr>
        <td>Project title</td>
        <td>Commitment (USD million)</td>
        <td>Sector</td>
    </tr>
     
          <c:forEach items="${orgProfLargestProjectsForm.projects}" var="project"  >
               <tr>
                   <td><digi:link module="aim" href="/selectActivityTabs.do~ampActivityId=${project.activityId}">${project.title}</digi:link></td>
               <td>${project.amount}</td>
               <td>
                <c:forEach var="sector" items="${project.sectors}">
                   ${sector.sectorId.name},
               </c:forEach>
               </td>
               </tr>
          </c:forEach>
    
  
    
	
</table>
    </div>
 

