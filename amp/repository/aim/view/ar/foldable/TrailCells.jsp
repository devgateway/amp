<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="reportData" name="viewable" type="org.dgfoundation.amp.ar.ReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>
<% String display=reportData.getLevelDepth()>2?"display:none":"";%>
<!-- generate total row -->
<c:set var="depthRelatVar">
	<bean:write name="reportData" property="levelDepth"/>@<bean:write name="reportData" property="relativeRowNo"/>
</c:set>
<c:set var="reportDataNameVar">
    <bean:write name="reportData" property="name"/>
</c:set>
<tr id='<bean:write name="reportData" property="absoluteReportName"/>' title="${depthRelatVar}" style="<%=display%>;" onmouseover='this.title="${reportDataNameVar}"' onmouseout='this.title="${depthRelatVar}"'>	
<% if (reportData.getLevelDepth() < 3) { %>		
		<td class="report_inside" style="padding-left:<%=10*(reportData.getLevelDepth()-1)%>;">
	<% } else { %>	
		<td class="report_inside" style="padding-left:<%=10*(reportData.getLevelDepth()-1)%>;">
	<% } %>			

	<% if(reportData.getName().indexOf(':')!=-1) { %>
	<!-- *************************************************** 
			WARNING:
				Do not add Translations here!
		 ***************************************************-->
		  <c:if test="${!(reportData.name == reportMeta.name)}">   
		  	<div style="width: auto;float: left;margin-right: 10px;margin-bottom: 3px">   
                <img id="toggleImage" name="<bean:write name="reportData" property="absoluteReportName"/>" style="cursor:pointer" src="img_2/ico_plus.gif" alt='hidden' onclick="document.getElementById(&quot;<bean:write name="reportData" property="absoluteReportName"/>&quot;).title='${depthRelatVar}';toggleRows(this,&quot;<bean:write name="reportData" property="absoluteReportName"/>&quot;)" title='<digi:trn key="aim:report:expandcollapse">Expand/Collapse</digi:trn>' border="0"/>
          	</div>
         </c:if>
	   	<div class="desktop_project_name">
           <%=reportData.getName().substring(reportData.getName().indexOf(':')+1,reportData.getName().length())%>
            <b>(<bean:write name="reportData" property="totalUniqueRows"/>)&nbsp;</b>
           	    
      	 </div>
	<% } else { %>
        <b class="desktop_project_name">
        	<bean:write name="reportData" property="name"/>
        	(<bean:write name="reportData" property="totalUniqueRows"/>)&nbsp;
        </b>
    <% } %> 
</td>
	
	<c:set var="firstCell" value="${true}"></c:set>
		<logic:iterate name="reportData" property="trailCells" id="cell" scope="page">
		<c:if test="${cell!=null}">
		<% if (reportData.getLevelDepth() < 3) { %>
			<td class="report_inside">
		<% } else { %>	
			<td class="report_inside">
		<% } %>
			<bean:define id="viewable" name="cell" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
			<bean:define id="caller" name="reportData" type="org.dgfoundation.amp.ar.ReportData" scope="page" toScope="request" />	
			<jsp:include page="<%=viewable.getViewerPath()%>"/>
			
			</td>
		</c:if>
		
		<c:if test="${cell==null}">
			<c:if test="${firstCell==false}">
					<% if (reportData.getLevelDepth() < 3) { %>
						<td class="report_inside">
					<% } else { %>	
						<td class="report_inside">
					<% } %>
					
					</td>
			</c:if>
		</c:if>
		<c:set var="firstCell" value="${false}"></c:set>
		</logic:iterate>
</tr>