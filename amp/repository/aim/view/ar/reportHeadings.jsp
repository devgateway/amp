<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>
<bean:define id="filterBean" scope="session" name="ReportsFilter"  />

<%int rowIdx = 2;%>


<!-- generate report headings -->
<logic:equal name="columnReport" property="globalHeadingsDisplayed" value="false">
	<%
	int maxRowsp = 0;
	%>
    <logic:iterate name="columnReport" property="items" id="column" scope="page" type="org.dgfoundation.amp.ar.Column" indexId="colIndexId">
    <%
	  	if(maxRowsp < column.getCurrentRowSpan()) maxRowsp = column.getCurrentRowSpan();
    %>
	</logic:iterate>


<thead> 
  <%int maxDepth = columnReport.getMaxColumnDepth();
  columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
  %>
  <%for (int curDepth = 0; curDepth <= columnReport
  .getMaxColumnDepth(); curDepth++, rowIdx++) {%>
  <tr title="Report Headings">
  <%boolean first=true; %>


  <%
  if(curDepth == 0) {
  %>
	<td class="report_inside" background="img_2/ins_bg_1.gif" style="border-color:#FFFFFF;" rowspan="<%=maxRowsp%>" width="20">
		&nbsp;
    </td>
  <%
  }
  %>
    <logic:iterate name="columnReport" property="items" id="column" scope="page" type="org.dgfoundation.amp.ar.Column" indexId="colIndexId">
     <%
      column.setCurrentDepth(curDepth);
      	int rowsp = column.getCurrentRowSpan();
      	String token=null;
      	String total=null;
      	if (((org.dgfoundation.amp.ar.Column)column).getWorker()!=null){
      	 token=((org.dgfoundation.amp.ar.Column)column).getWorker().getRelatedColumn().getTokenExpression();
      	 total=((org.dgfoundation.amp.ar.Column)column).getWorker().getRelatedColumn().getTotalExpression();
      	}
        
      
      	if(first && (token!=null || total!=null)){%>
      	
      	
      	<c:set var="addFakeColumn" value="${true}" scope="request"></c:set>
      	  <td class="report_inside">&nbsp;</td>
        <%}
        	first=false;
        %>


      <logic:iterate name="column" property="subColumnList" id="subColumn" scope="page" type="org.dgfoundation.amp.ar.Column">
        <c:set var="reportHeading">
          <%=subColumn.getName(reportMeta.getHideActivities())%>
        </c:set>
        
        <td background="img_2/ins_bg_1.gif" class="report_inside" align="center" style="color:#000000; border-color:#FFFFFF;" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
            	<c:choose>
           		<c:when test="${filterBean.sortBy != null && filterBean.sortBy == subColumn.namePath}">
            			<c:set var="sortAscString">sortByAsc=${!filterBean.sortByAsc}</c:set>
            		</c:when>
            		<c:otherwise>
            			<c:set var="sortAscString">sortByAsc=true</c:set>
            		</c:otherwise>
            	</c:choose>				
            <logic:equal name="column" property="columnDepth" value="1">          
	            <logic:equal name="widget" scope="request" value="true">
	              <a class="ins_title_reg" style="cursor:pointer;color:#000000; text-align: center;" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~sortBy=<bean:write name="subColumn" property="namePath"/>~${sortAscString}');">
	              		<logic:notEmpty name="reportMeta" property="hierarchies">
			           		<c:if test="${colIndexId==0}">${reportMeta.hierarchiesPath}<br/> </c:if>
			           </logic:notEmpty>
		              <c:set var="portfTitle">
		                <%=subColumn.getName(reportMeta.getHideActivities())%>
		              </c:set>
		              <digi:trn key="aim:protfilio:${portfTitle}"><%=subColumn.getName(reportMeta.getHideActivities())%></digi:trn>
				  </a>
	            </logic:equal>
            
	            <logic:notEqual name="widget" scope="request" value="true">
	              <a class="ins_title_reg" style="cursor:pointer" href="/aim/viewNewAdvancedReport.do~sortBy=${subColumn.namePath}~${sortAscString}">
	              	<digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>
	              </a>
	            </logic:notEqual>
            
	            <c:if test="${subColumn.namePath == columnReport.sorterColumn}">
	              <logic:equal name="columnReport" property="sortAscending" value="false">
	                <img src= "../ampTemplate/images/down.gif" align="absmiddle" border="0"/>
	              </logic:equal>
	              <logic:equal name="columnReport" property="sortAscending" value="true">
	                <img src= "../ampTemplate/images/up.gif" align="absmiddle" border="0"/>
	              </logic:equal>
	            </c:if>
            
          </logic:equal>
            
          <logic:notEqual name="column" property="columnDepth" value="1">
			<logic:notEqual name="subColumn" property="width" value="1">
				<b class="ins_title_reg" style="color:#000000;">
					<digi:trn key="aim:reportBuilder:${reportHeading}">
						<c:out value="${reportHeading}"/>
					</digi:trn>
				</b>
          	</logic:notEqual>
          	<logic:equal name="subColumn" property="width" value="1"> 
	            <logic:equal name="widget" scope="request" value="true">				
	              <a class="ins_title_reg" style="cursor:pointer;color:#000000;" onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="name"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~sortBy=<bean:write name="subColumn" property="namePath"/>~${sortAscString}');">
		              <c:set var="portfTitle">
		                <%=subColumn.getName(reportMeta.getHideActivities())%>
		              </c:set>
		              <digi:trn key="aim:reportBuilder:${reportHeading}"><b><c:out value="${reportHeading}"/></b></digi:trn>
				  </a>
	            </logic:equal>
            
	            <logic:notEqual name="widget" scope="request" value="true">
	            <a class="ins_title_reg" style="cursor:pointer" href="/aim/viewNewAdvancedReport.do~sortBy=${subColumn.namePath}~${sortAscString}">
	              	<digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>
	             </a>
	            </logic:notEqual>  
	            
				<c:if test="${subColumn.namePath == columnReport.sorterColumn}">
	              <logic:equal name="columnReport" property="sortAscending" value="false">
	                <img src= "../ampTemplate/images/down.gif" align="absmiddle" border="0"/>
	              </logic:equal>
	              <logic:equal name="columnReport" property="sortAscending" value="true">
	                <img src= "../ampTemplate/images/up.gif" align="absmiddle" border="0"/>
	              </logic:equal>
	            </c:if>
	                    		
          	</logic:equal>            
          </logic:notEqual>
          
        </td>

      </logic:iterate>
      
    </logic:iterate>
    <td width="16" bgcolor=#FFFFFF><img src="img_2/t.gif" width="16" height="1" /></td>
  </tr>

  <%}
  %>
  </thead>
 
	<tbody>

  </logic:equal>
