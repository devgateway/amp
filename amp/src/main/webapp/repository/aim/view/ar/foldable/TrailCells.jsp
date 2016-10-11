<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>

<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>
<% String display=reportData.getLevelDepth()>2?"display:none":"";%>

<!-- generate total row -->
<c:set var="depthRelatVar">
	<bean:write name="reportData" property="levelDepth"/>@<bean:write name="reportData" property="relativeRowNo"/>
</c:set>
<c:set var="reportDataNameVar">
    <bean:write name="reportData" property="name"/>
</c:set>
<tr id='<bean:write name="reportData" property="absoluteReportNameMD5"/>' class="row_data_header"  depthRelatVar="${depthRelatVar}" style="<%=display%>;" title='${reportDataNameVar}'>
	<td class="report_inside" width="25">&nbsp;</td>
	<td class="report_inside" style="padding-left:<%=10*(reportData.getLevelDepth()-1)%>;">
	<% if(reportData.getName().indexOf(':')!=-1) { %>
	<!-- *************************************************** 
			WARNING:
				Do not add Translations here!
		 ***************************************************-->
		  <c:if test="${!(reportData.name == reportMeta.name)}">   
		  	<div style="width: auto;float: left;margin-right: 10px;margin-bottom: 3px">   
                <img id="toggleImage" 
                name="<bean:write name="reportData" property="absoluteReportNameMD5"/>"
                style="cursor:pointer" src="img_2/ico_plus.gif" alt='hidden' 
             	onclick="document.getElementById(&quot;<bean:write name="reportData" property="absoluteReportNameMD5"/>&quot;).depthRelatVar='${depthRelatVar}';toggleRows(this,&quot;<bean:write name="reportData" property="absoluteReportNameMD5"/>&quot;)" 
                title='<digi:trn jsFriendly="true" key="aim:report:expandcollapse">Expand/Collapse</digi:trn>' 
               border="0"/>
          	</div>
         </c:if>
	   	<div class="desktop_project_name">
           <%=reportData.getName().substring(reportData.getName().indexOf(':')+1,reportData.getName().length())%>
            <b>&nbsp;(<bean:write name="reportData" property="totalUniqueRows"/>)&nbsp;</b>
           	    
      	 </div>
	<% } else { %>
         <b class="desktop_project_name">
            <c:choose>
            	<c:when test="${fn:length(reportData.name)>25}">
            		<span title='<c:out value="${reportData.name}"/>' >
                		<c:out value="${fn:substring(reportData.name,0,25)}"/>...
            		</span>
            	</c:when>
            	<c:otherwise>
                	<bean:write name="reportData" property="name"/>
            	</c:otherwise>
        	</c:choose>&nbsp;(<bean:write name="reportData" property="totalUniqueRows"/>)&nbsp;
        </b>
    <% } %> 
</td>
	
	<c:set var="firstCell" value="${true}"></c:set>
		<logic:iterate name="reportData" property="trailCells" id="cell" scope="page">
		<c:if test="${cell!=null}">
			<td class="report_inside">
				<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
				<bean:define id="caller" name="reportData" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="request" />	
				<jsp:include page="<%=viewable.getViewerPath()%>"/>
			</td>
		</c:if>
		
		<c:if test="${cell==null}">
			<c:if test="${firstCell==false}">
				<td class="report_inside"></td>
			</c:if>
		</c:if>
		<c:set var="firstCell" value="${false}"></c:set>
		</logic:iterate>
</tr>
