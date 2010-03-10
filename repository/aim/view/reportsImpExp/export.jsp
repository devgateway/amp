<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@page import="org.digijava.module.aim.util.reportsimpexp.ReportsImpExpConstants"%>

<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
  
  <script language="JavaScript1.2" type="text/javascript">
  initObj.initialize		= function () {
		var winH;
		//if (navigator.appName.indexOf("Microsoft")!=-1) {
		//	winH = document.body.offsetHeight;
		//}else{
		//	winH=window.innerHeight;
		//}
		
		var trs=$("tr.myClass");
		var tableHeight=0;
		if(trs!=null && trs.length>5){
			for(var i=0;i<5;i++){
				tableHeight+=trs[i].clientHeight;
			}
		}
		if(tableHeight==0){
			tableHeight=250;
		}

		var reporTable=new scrollableTable("reportsTable",tableHeight);
		reporTable.debug=false;
		reporTable.maxRowDepth=1;
		reporTable.scroll();
	};  

	function disableOptions (optionsList ) {
		if ( optionsList != null ) {
			for (var i=0; i<optionsList.length; i++) {
				optionsList[i].selected	= false;
			}
		}
	}
	function resetFilters () {
		disableOptions (document.forms["aimImpExpForm"].selectedTeamIds.options);
		disableOptions (document.forms["aimImpExpForm"].selectedUserIds.options);
	}
  </script>
<digi:form action="/reportsExport.do" method="post">
	<html:hidden name="aimImpExpForm" property="showTabs"/>
	
	<c:if test="${aimImpExpForm.showTabs}">
		<c:set var="titleColumn">
			<digi:trn>Tab Title</digi:trn>
		</c:set>	
		<c:set var="selectedObjects">
			<digi:trn>Selected Tabs</digi:trn>
		</c:set>
	</c:if>
	
	<c:if test="${!aimImpExpForm.showTabs}">	
		<c:set var="titleColumn">
			<digi:trn>Report Title</digi:trn>			
		</c:set>
		<c:set var="selectedObjects">
				<digi:trn>Selected Reports</digi:trn>
		</c:set>
	</c:if>

	<table align="center" cellPadding="0" cellSpacing="0" width="100%" >
		<tr>
			<td style="text-align: center; padding: 5px;" nowrap="nowrap">
				<span style="vertical-align: middle; font-weight: bold"><digi:trn>Teams</digi:trn>:</span>
				<html:select styleClass="inp-text" property="selectedTeamIds" multiple="true" size="4" style="vertical-align: middle;">
					<html:optionsCollection style=" font-size: 11px;" property="availableTeams" label="value" value="key"/>
				</html:select>
				&nbsp;&nbsp;
				<span style="vertical-align: middle; font-weight: bold"><digi:trn>Users</digi:trn>:</span>
				<html:select styleClass="inp-text" style="vertical-align: middle; " property="selectedUserIds" multiple="true" size="4">
					<html:optionsCollection style=" font-size: 11px;" property="availableUsers" label="value" value="key"/>
				</html:select>
				&nbsp;&nbsp;
				<button style="vertical-align: middle;" type="button" class="buton" onclick="resetFilters()">
					<digi:trn>Reset Selection</digi:trn> 
				</button>
				&nbsp;&nbsp;
				<button style="vertical-align: middle;" type="button" class="buton" onclick="changePage(${aimImpExpForm.showTabs}, '<%=ReportsImpExpConstants.ACTION_SELECTION_STEP %>')">
					<digi:trn>Apply Filters</digi:trn> 
				</button>
			</td>
		</tr>
        <tr>                    
        	<td>
            	<table  id="reportsTable" border="0" cellPadding="3" cellSpacing="3" width="100%" >
                	<thead>
                    	<tr bgColor="#999999">
                          	<td bgColor="#999999" align="center" height="20">&nbsp;</td>
                            <td bgColor="#999999" align="center" height="20">
                            	<b>${titleColumn}</b>
                            </td>
                            <td bgColor="#999999" align="center" height="20">
                            	<b><digi:trn>Owner</digi:trn></b>
                            </td>
                            <td bgColor="#999999" align="center" height="20">
                            	<b><digi:trn>Creation Date</digi:trn></b>
                            </td>
                            <td bgColor="#999999" align="center" height="20">
                            	<b><digi:trn>Type</digi:trn></b>
                            </td>                            
                            <td bgColor="#999999" align="center" height="20">
                            	<b><digi:trn>Hierarchies</digi:trn></b>
                            </td>
                            <td bgColor="#999999" align="center" height="20">
                            	<b><digi:trn>Columns</digi:trn> / <digi:trn>Measures</digi:trn></b>
                            </td>                           
                        </tr>
                    </thead>                        
                    <c:if test="${reportNumber == 0}">
                    	<tr>
                            <td colspan="4">
                            	<digi:trn>No reports present</digi:trn>
                            </td>
                        </tr>
                     </c:if>
                     <logic:iterate name="aimImpExpForm"  property="reportsList" id="report" indexId="idx" type="org.digijava.module.aim.dbentity.AmpReports">
                     	<tbody>
                              <tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
                              onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" id="repTr_${idx}" class="myClass" style="">
                              	<td>                              		  
                              		<html:multibox name="aimImpExpForm" property="selectedReportIds" value="${report.ampReportId}" styleClass="selReportsIds" />
                              	</td>                           
                              	<td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD">
                              		<c:if test="${!aimImpExpForm.showTabs}">
	                              		<b><p style="max-width: 400px;white-space: normal" title="${report.name}">
											<c:if test="${fn:length(report.name) > 120}" >
												<c:out value="${fn:substring(report.name, 0, 120)}" />...
											</c:if>
											<c:if test="${fn:length(report.name) < 120}" >
												<c:out value="${report.name}" />
											</c:if>
	                                	</p></b>
	                          		</c:if>
	                          		<c:if test="${aimImpExpForm.showTabs}">
	                          			<b>
	                                		<p style="max-width: 400px;white-space: normal" title="${report.name}">
												<c:if test="${fn:length(report.name) > 120}" >
													<c:out value="${fn:substring(report.name, 0, 120)}" />...
												</c:if>
												<c:if test="${fn:length(report.name) < 120}" >
													<c:out value="${report.name}" />
												</c:if>
	                                		</p>  
	                              		</b>
	                          		</c:if>
                             
                              		<logic:present name="report" property="reportDescription" >
                                		<p style="max-width: 400px;white-space: normal" title="${report.reportDescription}">
											<c:if test="${fn:length(report.reportDescription) > 120}" >
												<c:out value="${fn:substring(report.reportDescription, 0, 120)}" />...
											</c:if>
											<c:if test="${fn:length(report.reportDescription) < 120}" >
												<c:out value="${report.reportDescription}" />
											</c:if>
                                		</p>
                              		</logic:present>
                              	</td>

                                <td align="center">
                                	<p style="white-space: nowrap">
		                                <logic:present name="report" property="ownerId">
		                                   <i><bean:write name="report" property="ownerId.user.name" /></i>
		                                </logic:present>
                                	</p>
                              	</td>
                              	<td align="center">
                                	<p style="white-space: nowrap">
	                                  <logic:present name="report" property="updatedDate">
	                                      <bean:write name="report" property="formatedUpdatedDate" />
	                                  </logic:present>
                                	</p>
                              	</td>
                              	<td>
                                	<p style="white-space: nowrap">
                                  		<li>
		                                      <%
		                                        if (report.getType()!=null && report.getType().equals(new Long(1))) {
		                                      %>
		                                          <digi:trn>donor</digi:trn>
			                                  <%
		                                        }
		                                        else if (report.getType()!=null && report.getType().equals(new Long (3))){
			                                  %>
		                                          <digi:trn>regional</digi:trn>
			                                  <%
		                                        }
		                                        else if (report.getType()!=null && report.getType().equals(new Long(2))){
			                                  %>
		                                          <digi:trn>component</digi:trn>
			                                  <%
		                                        }
		                                        else if (report.getType()!=null && report.getType().equals(new Long(4))){
			                                  %>
		                                          <digi:trn>contribution</digi:trn>
			                                  <%}%>
		                              	</li>
                                  		<logic:equal name="report" property="drilldownTab" value="true">
		                                    <li>
		                                      <digi:trn>Desktop Tab</digi:trn>
		                                    </li>
		                                </logic:equal>
                                  		<logic:equal name="report" property="publicReport" value="true">
		                                    <li>
		                                      <digi:trn>Public Report</digi:trn>
		                                    </li>
		                                </logic:equal>
                                  		<logic:equal name="report" property="hideActivities" value="true">
                                    		<li>
                                      			<digi:trn>Summary Report</digi:trn>
                                    		</li>
                                  		</logic:equal>                                  
                                  		<logic:equal name="report" property="options" value="A">
                                    		<li>
                                    			<digi:trn>Annual</digi:trn>
                                    		</li>
                                  		</logic:equal>
                                  		<logic:equal name="report" property="options" value="Q">
                                    		<li>
                                    			<digi:trn>Quarterly</digi:trn>
                                    		</li>
                                  		</logic:equal>
                                  		<logic:equal name="report" property="options" value="M">
                                    		<li>
                                    			<digi:trn>Monthly</digi:trn>	
                                    		</li>
                                  		</logic:equal>
                                	</p>
                              	</td>
                              	<td>
                                	<logic:iterate name="report" property="hierarchies" id="hierarchy" >
                                  	<%-- <bean:write name="hierarchy" property="column.columnName"/> --%>
                                  		<li>                                  
	                                      	<digi:trn>
	                                        	<bean:write name="hierarchy" property="column.columnName" />
	                                      	</digi:trn>                                  
                                  		</li>
                                	</logic:iterate>
                              </td>
                              <td width="200">  
                              	
                              		<div style='position:relative;display:none;' id='report-<bean:write name="report" property="ampReportId"/>'> 
		                                <logic:iterate name="report" property="columns" id="column" indexId="index"  >
		                                    <%if (index.intValue()%2==0){ %>
		                                      <li>                                      
		                                      	<digi:trn>
		                                        	<bean:write name="column" property="column.columnName" />
		                                      	</digi:trn>
		                                    <% } else {%>
		                                      ,
		                                      	<digi:trn>
		                                        	<bean:write name="column" property="column.columnName" />
		                                      	</digi:trn>
		                                      </li>
		                                    <%} %>
		                                </logic:iterate>
	                                </div>
                              	
	                                
	                                                                
	                                <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn>columns</digi:trn>',document.getElementById('report-<bean:write name="report" property="ampReportId"/>').innerHTML],Style[0])" onMouseOut="htm()">
	                                [ <u style="text-transform:capitalize;" ><digi:trn>Columns</digi:trn></u> ]&nbsp;
	                                </span>
                                
                                
                                	<div style='position:relative;display:none;' id='measure-<bean:write name="report" property="measures"/>'> 
	                                  <logic:iterate name="report" property="measures" id="measure" indexId="index"  >
	                                    <li>
	                                    	<digi:trn>                                      
	                                      		${measure.measure.aliasName}
	                                      	</digi:trn>
	                                    </li>
	                                  </logic:iterate>
	                                </div>    
	                                
	                                <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn>measures</digi:trn>',document.getElementById('measure-<bean:write name="report" property="measures"/>').innerHTML],Style[1])" onMouseOut="htm()">
	                                [ <u><digi:trn>Measures</digi:trn></u> ]<br>
	                                </span>
	                               
                                </td>                              
                          </tr>
                        </tbody>
                     </logic:iterate>
				</table>
          </td>
        </tr>     
        <tr>
        	<td align="right">
        		<br>
        		<button style="vertical-align: middle;" type="button" class="buton" onclick="changePage(${aimImpExpForm.showTabs}, '<%=ReportsImpExpConstants.ACTION_EXPORT %>')">
						<digi:trn>Export</digi:trn> 
				</button>          		                      	
	         </td>
          </tr>
	</table>
 </digi:form>