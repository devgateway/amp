<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.dgfoundation.amp.ar.ArConstants"%>

<%@page import="java.util.HashMap"%>
<bean:define id="columnReport" name="viewable" type="org.dgfoundation.amp.ar.ColumnReportData" scope="request" toScope="page"/>
<bean:define id="reportMeta" name="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports" scope="session" toScope="page"/>
<bean:define id="filterBean" scope="session" type="org.dgfoundation.amp.ar.AmpARFilter" name="ReportsFilter"  />

<c:set var="categoryYear"><%=ArConstants.YEAR%></c:set>
<c:set var="categoryQuarter"><%=ArConstants.QUARTER%></c:set>

<%
String lastColor="#F2F2F2";
String color="#F2F2F2";
HashMap<String,String> colors=new HashMap<String,String>();
int rowIdx = 2;%>

<!-- generate report headings -->
<logic:equal name="columnReport" property="globalHeadingsDisplayed" value="false">
 <thead class="fixedHeader"> 
  <%int maxDepth = columnReport.getMaxColumnDepth();
  	columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
  	pageContext.setAttribute("linkMap", new HashMap());
  %>
  <%for (int curDepth = 0; curDepth <= columnReport.getMaxColumnDepth(); curDepth++, rowIdx++) {%>
  <tr title='<digi:trn key="reports.ReportHeadings">Report Headings</digi:trn>'>
  <%boolean first=true; %>
    <logic:iterate name="columnReport" property="items" id="column" scope="page" type="org.dgfoundation.amp.ar.Column">
       
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
      	  <td style="background-color:#EAEAEA;padding-left: 2px; padding-right: 2px "> </td>	
        <%}
        first=false;
        %>
		<logic:iterate name="column" property="subColumnList" id="subColumn" scope="page" type="org.dgfoundation.amp.ar.Column">
       
          <%
          String[] headings;
          if (subColumn.getName(reportMeta.getHideActivities()).indexOf("-") > -1){
        	  headings=subColumn.getName(reportMeta.getHideActivities()).split(",");%>
          	<c:set var="reportHeading1">
        	<%=headings[0]%>
        	</c:set>
	        <c:set var="reportHeading">
	        <%=headings[1]%>
	        </c:set>
          <%}else{ %>
       		 <c:set var="reportHeading1" value="${null}"/>
	        <c:set var="reportHeading"><%=subColumn.getName(reportMeta.getHideActivities())%></c:set>  
          <%}%>
       
        
        <logic:equal name="column" property="columnDepth" value="1">
        	
        	<td style="border-bottom:#E2E2E2 0px solid;background-color:#EAEAEA;padding-left: 2px; padding-right: 2px " height="20px" nowrap="nowrap" align="center" class="clsTableTitleColHtml" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>          
	        		<c:choose>
	            		<c:when test="${filterBean.sortBy != null && filterBean.sortBy == column.name}">
	            			<% ((HashMap)pageContext.getAttribute("linkMap")).put("sortByAsc", ! filterBean.getSortByAsc() ); %>
	            		</c:when>
	            		<c:otherwise>
	            			<% ((HashMap)pageContext.getAttribute("linkMap")).put("sortByAsc", "true" ); %>
	            		</c:otherwise>
	            	</c:choose>	
	        		<% ((HashMap)pageContext.getAttribute("linkMap")).put("sortBy", column.getName() ); %>
	        	<logic:notEqual name="widget" scope="request" value="true">
	            	<html:link  style="font-family: Arial;font-size: 11px;text-decoration: none;color: black;cursor:pointer;" page="/viewNewAdvancedReport.do" name="linkMap">
	              		      
	              		      <digi:trn><c:out value="${reportHeading}"/></digi:trn>
                              <%
                              if (subColumn.getDescription()!=null){
                              String text=subColumn.getDescription();
                              if (text!=null){ %> 
                       			<img src= "/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" title="<digi:trn  key="aim:report:tip:${ampColumnFromTree.columnName}:${ampColumnFromTree.description}"><%=text%></digi:trn>">
                    		 	<%}
                     		  }%>
	              	</html:link>
	            </logic:notEqual>
            
	            <c:if test="${column.name == columnReport.sorterColumn}">
	            	<logic:equal name="columnReport" property="sortAscending" value="false">
	                	<img src= "/TEMPLATE/ampTemplate/imagesSource/common/down.gif" align="absmiddle" border="0"/>
	              	</logic:equal>
	              	<logic:equal name="columnReport" property="sortAscending" value="true">
	                	<img src= "/TEMPLATE/ampTemplate/imagesSource/common/up.gif" align="absmiddle" border="0"/>
	              	</logic:equal>
	            </c:if>
	         </td>
          </logic:equal>
       
        <logic:notEqual name="column" property="columnDepth" value="1">
        	<c:choose>
        		<c:when test="${subColumn.width!=1 || subColumn.contentCategory==categoryYear}">
        		
        		<% if (subColumn.getContentCategory()!=null){
        		if (subColumn.getContentCategory().equalsIgnoreCase(ArConstants.YEAR)){
        			if (lastColor.equalsIgnoreCase("#F2F2F2")){
        				lastColor="#FBFBFB";
        			}else{
        				lastColor="#F2F2F2";
        			}
        			color=lastColor;
        			colors.put(subColumn.getName(reportMeta.getHideActivities()),color);
        		}
        		}
        		%>
        		<%if(subColumn.getName().length()<5){%>
        				<td style="background-color:<%=color%>; margin-left: 2px; margin-right: 2px;" class="clsTableTitleColHtml" height="20px" nowrap="nowrap" align="center" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
							   <c:if test="${reportHeading1!=null}"><digi:trn><c:out value="${reportHeading1}"/></digi:trn></c:if> <digi:trn><c:out value="${reportHeading}"/></digi:trn>
					<%}else{%>
						<td class="clsTableTitleColHtml" style="background-color:<%=color%>;text-decoration: none;border-right: #FFFFFF 1px solid;border-bottom: #FFFFFF 1px solid" height="15px" nowrap="nowrap" align="center" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
							   <c:if test="${reportHeading1!=null}"><digi:trn><c:out value="${reportHeading1}"/></digi:trn></c:if> <digi:trn><c:out value="${reportHeading}"/></digi:trn>
          		<%}%>
          		</c:when>
          		<c:otherwise>
          		<% if (subColumn.getContentCategory()!=null){
        			if (subColumn.getContentCategory().equalsIgnoreCase(ArConstants.MONTH)){
        				color=colors.get((((org.dgfoundation.amp.ar.Column)subColumn.getParent()).getName(reportMeta.getHideActivities())));
        			}
        			
        			if (subColumn.getContentCategory().equalsIgnoreCase(ArConstants.QUARTER)){
        				color=colors.get((((org.dgfoundation.amp.ar.Column)subColumn.getParent()).getName(reportMeta.getHideActivities())));
        			}
        			
        			if (subColumn.getContentCategory().equalsIgnoreCase(ArConstants.FUNDING_TYPE)){
        				org.dgfoundation.amp.ar.Column parent=(org.dgfoundation.amp.ar.Column)subColumn.getParent();
        				color=colors.get(((org.dgfoundation.amp.ar.Column)parent.getParent()).getName(reportMeta.getHideActivities()));
        			}
  				}
        		%>
        		
	      			<td style="background-color:<%=color%>;border-right: #FFFFFF 1px solid;border-bottom: #FFFFFF 1px solid; font:9px Arial;" valign="bottom" height="15px" nowrap="nowrap" align="center" rowspan="<%=rowsp%>" colspan='<bean:write name="subColumn" property="width"/>'>
	      				
	      				<html:link style="font-family: Arial;font-size: 11px;text-decoration: none;color: black ;cursor:pointer;" page="/viewNewAdvancedReport.do" paramName="subColumn" paramProperty="name" paramId="sortBy">
	        				 <digi:trn><c:out value="${reportHeading}"/></digi:trn>
						</html:link>
		  			  
	            
					<c:if test="${subColumn.name == columnReport.sorterColumn}">
	        			<logic:equal name="columnReport" property="sortAscending" value="false">
	                		<img src= "/TEMPLATE/ampTemplate/imagesSource/common/down.gif" align="absmiddle" border="0"/>
	            		</logic:equal>
	            		<logic:equal name="columnReport" property="sortAscending" value="true">
	                		<img src= "/TEMPLATE/ampTemplate/imagesSource/common/up.gif" align="absmiddle" border="0"/>
	            		</logic:equal>
	       			</c:if>
	     		</c:otherwise>
	     		</c:choose>
	     	 <%
                              if (subColumn.getDescription()!=null){
                              String text=subColumn.getDescription();
                              if (text!=null){ %> 
                       			<img src= "/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" title="<digi:trn  key="aim:report:tip:${ampColumnFromTree.columnName}:${ampColumnFromTree.description}"><%=text%></digi:trn>">
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
