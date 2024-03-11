<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.dgfoundation.amp.ar.ArConstants"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%@page import="java.util.HashMap"%>

<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" toScope="page"/>
<bean:define id="filterBean" name="reportCD" type="org.dgfoundation.amp.ar.AmpARFilter" property="filter" />

<c:set var="categoryYear"><%=ArConstants.YEAR%></c:set>
<c:set var="categoryQuarter"><%=ArConstants.QUARTER%></c:set>


<%int rowIdx = 2;%>

<!-- generate report headings -->
<logic:equal name="columnReport" property="globalHeadingsDisplayed" value="false">
 <thead class="fixedHeader"> 
  <%
	//int maxDepth = columnReport.getMaxColumnDepth();
	columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
	HashMap linkMap = new HashMap();
	linkMap.put("reportContextId", ReportContextData.getCurrentReportContextId(request, true));
	pageContext.setAttribute("linkMap", linkMap);
  %>
  <%for (int curDepth = 0; curDepth < columnReport.getMaxColumnDepth(); curDepth++, rowIdx++) {%>
  <tr class="reportHeader" title='<digi:trn jsFriendly="true" key="reports.ReportHeadings">Report Headings</digi:trn>'>
  <%boolean first=true;
  	if (curDepth == 0) {
  %>
  	<logic:iterate name="reportMeta" property="hierarchies" type="org.digijava.module.aim.dbentity.AmpReportHierarchy" 
  						indexId="hIdx" id="repHierarchy" scope="page">
  		<c:set var="hSortOrder">descending</c:set>
  		<c:set var="sortIconPath"></c:set>
  		<logic:notEmpty name="reportCD" property="generatedReport">
  			<bean:define id="generatedReport" name="reportCD" property="generatedReport" type="org.dgfoundation.amp.ar.GroupReportData" toScope="page"/>
  			<logic:notEmpty name="generatedReport" property="levelSorters">
				<logic:iterate name="generatedReport" property="levelSorters" id="sorter" indexId="levelId">
					<c:if test="${levelId==hIdx}">
						<c:set var="hSortOrder">${sorter.value}</c:set>
						<c:set var="sortIconPath">/TEMPLATE/ampTemplate/imagesSource/common/up_red${levelId}.gif</c:set>
						<c:if test="${sorter.value=='descending'}">
							<c:set var="sortIconPath">/TEMPLATE/ampTemplate/imagesSource/common/down_red${levelId}.gif</c:set>
						</c:if>
					</c:if>
				</logic:iterate>
			</logic:notEmpty>
		</logic:notEmpty>
  		<td nowrap="nowrap" class="reportHeader" onclick="sortHierarchy( $(this).find('.headerName').html(), '${hSortOrder}' )"
  		rowspan="${columnReport.maxColumnDepth+1}">
  			<span class="headerName"><digi:colNameTrn>${repHierarchy.column.columnName}</digi:colNameTrn></span>

  			<c:if test="${sortIconPath!=''}">
  				<img src= "${sortIconPath}" align="absmiddle" border="0"/>
  			</c:if>
  		</td>
  	</logic:iterate>
  <% } %>
    <logic:iterate name="columnReport" property="items" id="column" scope="page" type="org.dgfoundation.amp.ar.Column">
       
<%
		column.setCurrentDepth(curDepth);
      	String token=null;
      	String total=null;
      	if (column.getWorker()!=null){
      	 token = column.getWorker().getRelatedColumn().getTokenExpression();
      	 total = column.getWorker().getRelatedColumn().getTotalExpression();
      	}
        
      	if(first && (token!=null || total!=null)){%>
      	
      		<c:set var="addFakeColumn" value="${true}" scope="request"></c:set>
      	  		<td style="background-color:#EAEAEA;padding-left: 2px; padding-right: 2px "> </td>	
        	<%}
        first=false;
%>
	<logic:iterate name="column" property="subColumnList" id="subColumn" scope="page" type="org.dgfoundation.amp.ar.Column" >
		<!-- subcolumn name: ${subColumn.name}, width: ${subColumn.width}, columnDepth: ${subColumn.columnDepth}, columnSpan: ${subColumn.positionInHeading.colSpan}, newRowSpan: ${subColumn.positionInHeading.rowSpan} %> -->
<%
	int rowsp = subColumn.getPositionInHeading().getRowSpan();
%>		
        <c:set var="reportHeading">
          <%=subColumn.getName(reportMeta.getHideActivities())%>
          <%= ("percentageOfTotalCommitments".equals (subColumn.getExpression())?"(%)":"")  %>
        </c:set>
        
		<c:choose>
			<c:when test="${filterBean.sortBy != null && filterBean.sortBy == subColumn.namePath}">
				<% ((HashMap)pageContext.getAttribute("linkMap")).put("sortByAsc", ! filterBean.getSortByAsc() ); %>
			</c:when>
			<c:otherwise>
				<% ((HashMap)pageContext.getAttribute("linkMap")).put("sortByAsc", "true" ); %>
			</c:otherwise>
		</c:choose>	
	    <% ((HashMap)pageContext.getAttribute("linkMap")).put("sortBy", subColumn.getNamePath() ); %>
        
        
        <logic:equal name="column" property="columnDepth" value="1">
        	
        	<td height="20px" nowrap="nowrap" align="center" class="reportHeader" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
        	  		<logic:notEmpty name="reportMeta" property="hierarchies">
		           		<c:if test="${colIndexId==0 && (reportMeta.hideActivities == null || !reportMeta.hideActivities)}">
		           			${reportMeta.hierarchiesPath}<br/> 
		           		</c:if>
		           </logic:notEmpty>
        		
	        		<c:choose>
	        			<c:when test="${subColumn.sortableBy}">
			            	<html:link  style="font-family: Arial;font-size: 11px;text-decoration: none;color: black;cursor:pointer;" page="/viewNewAdvancedReport.do" name="linkMap">
								<digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>
							</html:link>							
						</c:when>
						<c:otherwise>
							<digi:trn key="aim:reportBuilder:${reportHeading}"><c:out value="${reportHeading}"/></digi:trn>
			             </c:otherwise>
	              	</c:choose>
            	
	            <%@include file="columnSortingImages.jspf" %>
	         </td>
          </logic:equal>
          
        <logic:notEqual name="column" property="columnDepth" value="1">
        	<c:choose>
        		<c:when test="${not subColumn.sortableBy}">
        		<%
        			if(subColumn.getName().length()<5){%>        			
        				<td style="margin-left: 2px; margin-right: 2px;" class="reportHeader" height="20px" nowrap="nowrap" align="center" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>        				        						
					<%}else{%>
						<td class="reportHeader" height="15px" nowrap="nowrap" align="center" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
          		<%}%>
					<digi:trn>${reportHeading}</digi:trn>
          		</c:when>
          		<c:otherwise>
	      			<td class="reportHeader" valign="bottom" height="15px" nowrap="nowrap" align="center" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>	      			
	      				<html:link style="font-size: 11px;text-decoration: none; font-weight:bold; color: #000000" page="/viewNewAdvancedReport.do" name="linkMap">
	        				<digi:trn key="aim:reportBuilder:${reportHeading}">
	            				<c:out value="${reportHeading}"/>
	            			</digi:trn>
						</html:link>
		  			  	          
					 <%@include file="columnSortingImages.jspf" %>
	     		</c:otherwise>
	     		</c:choose>
	     				<%
			if (subColumn.getDescription() != null)
			{
				String text=subColumn.getDescription();
				if (text != null){ %> 
					&nbsp;<img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn  key="aim:report:tip:${ampColumnFromTree.columnName}:${ampColumnFromTree.description}"><%=text%></digi:trn>">
		    <%}
			}%>
	     		
	     	</td>	            
        </logic:notEqual>    	
	</logic:iterate>
   </logic:iterate>
  </tr>
  <%} %>
    </thead>
 
	<tbody>
  
  </logic:equal>
