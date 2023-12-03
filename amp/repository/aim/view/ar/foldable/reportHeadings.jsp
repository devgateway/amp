<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<% 
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.ampModule.aim.dbentity.AmpReports" toScope="page"/>
<bean:define id="filterBean" name="reportCD" property="filter" />

<%int rowIdx = 2;%>


<!-- generate report headings -->
<logic:equal name="columnReport" property="globalHeadingsDisplayed" value="false">

<thead> 
  <%
  //int maxDepth = columnReport.getMaxColumnDepth();
  columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
  %>
  <%for (int curDepth = 0; curDepth < columnReport.getMaxColumnDepth(); curDepth++, rowIdx++) {%>
  <tr title="Report Headings">
  <%boolean first=true; %>


  <%
  if(curDepth == 0) {
  %>
	<td class="report_inside" background="img_2/ins_bg_1.gif" style="border-color:#FFFFFF;" rowspan="<%=columnReport.getMaxColumnDepth()%>" width="20">
		&nbsp;
    </td>
  <%
  }
  %>
    <logic:iterate name="columnReport" property="items" id="column" scope="page" type="org.dgfoundation.amp.ar.Column" indexId="colIndexId">
     <%
		column.setCurrentDepth(curDepth);
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
      <%
      	int rowsp = subColumn.getPositionInHeading().getRowSpan();
      %>
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
								<c:choose>
									<c:when test="${param.queryEngine!='true' }">
										<a class="ins_title_reg"
											style="cursor: pointer; color: #000000; text-align: center;"
											onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~sortBy=<bean:write name="subColumn" property="namePath"/>~${sortAscString}');">
									</c:when>
									<c:otherwise>
										<a class="ins_title_reg"
											style="cursor: pointer; color: #000000; text-align: center;"
											onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~sortBy=<bean:write name="subColumn" property="namePath"/>~${sortAscString}~queryEngine=true');">
									</c:otherwise>
								</c:choose>
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
									<c:choose>
										<c:when test="${param.queryEngine!='true' }">
											<a class="ins_title_reg"
												style="cursor: pointer; color: #000000;"
												onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~sortBy=<bean:write name="subColumn" property="namePath"/>~${sortAscString}');">
										</c:when>
										<c:otherwise>
											<a class="ins_title_reg"
												style="cursor: pointer; color: #000000; text-align: center;"
												onclick="changeStep('/aim/viewNewAdvancedReport.do~viewFormat=foldable~ampReportId=${reportMeta.ampReportId}~widget=true~sortBy=<bean:write name="subColumn" property="namePath"/>~${sortAscString}~queryEngine=true');">
										</c:otherwise>
									</c:choose>
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

  </tr>

  <%}
  %>
  </thead>
 
	<tbody>

  </logic:equal>
