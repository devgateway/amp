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

 
<!-- this is for the nice tooltip widgets -->

<%@page import="org.digijava.module.aim.action.reportsimpexp.ReportsExportAction"%>
<%@page import="org.digijava.module.aim.util.reportsimpexp.ReportsImpExpConstants"%><DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script language="JavaScript1.2" type="text/javascript"
  src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"
  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
  
  <script language="JavaScript1.2" type="text/javascript">
  initObj.initialize		= function () {
		var winH;
		
		if (navigator.appName.indexOf("Microsoft")!=-1) {
			winH = document.body.offsetHeight;
		}else{
			winH=window.innerHeight;
		}
		var reporTable=new scrollableTable("reportsTable", winH-450);
		reporTable.debug=false;
		reporTable.maxRowDepth=1;
		reporTable.scroll();

		var srcEl		= document.getElementById("sourceOptions");
		var destEl		= document.getElementById("destinationOptions");

		destEl.style.width	= srcEl.clientWidth + "";
	};  

	function selectAllOptions(srcId) { 
		var srcEl		= document.getElementById(srcId);
		for ( i=0 ; i<srcEl.options.length; i++) {
			optionEl		= srcEl.options[i];
			if ( parseInt(optionEl.value) > 0 ) 
				optionEl.selected	= true;
		}
		
	}
	
	function moveOptions(srcId, destId) {
		var srcEl		= document.getElementById(srcId);
		var destEl		= document.getElementById(destId);

		var i;

		var list			= new Array();

		if ( srcEl.options[0].selected ) {
			selectAllOptions(srcId);
			srcEl.options[0].selected = false;
		}
		
		for ( i=0 ; i<srcEl.options.length; i++) {
			optionEl		= srcEl.options[i];
			if ( optionEl.selected ) {
				list.push( optionEl );
				optionEl.selected		= false;
			}
		}
		for ( i=0 ; i<list.length; i++) { 
			srcEl.removeChild( list[i] );
			destEl.appendChild( list[i] );
		}		

	}

	function check( selectId, btnId ) {
		var selectEl				= document.getElementById( selectId );
		if ( selectEl.options.length > 0 ) 
			enableButton ( btnId );
		else 
			disableButton( btnId );
	}
	
	function disableButton( btnId ) {
		var buttonEl				= document.getElementById( btnId );
		buttonEl.disabled		= true;
		buttonEl.style.color		= 'gray';
	}

	function enableButton( btnId ) {
		var buttonEl				= document.getElementById( btnId );
		buttonEl.disabled		= false;
		buttonEl.style.color		= 'black';
	}
	
  </script>
<digi:form action="/reportsImport.do" method="post"  enctype="multipart/form-data" >
	<html:hidden name="aimImpExpForm" property="showTabs"/>
	
	<c:if test="${aimImpExpForm.showTabs}">
		<c:set var="titleColumn">
			<digi:trn key="aim:tabTitle">
				Tab Title
			</digi:trn>
		</c:set>	
		<c:set var="selectedObjects">
			<digi:trn>
				Selected Tabs
			</digi:trn>
		</c:set>
	</c:if>
	
	<c:if test="${!aimImpExpForm.showTabs}">
	
		<c:set var="titleColumn">
			<digi:trn key="aim:reportTitle">
				Report Title
			</digi:trn>
			<c:set var="selectedObjects">
			<digi:trn>
				Selected Reports
			</digi:trn>
		</c:set>
		</c:set>
	</c:if>



				<table align=center cellPadding=0 cellSpacing=0 width="100%">
					<tr>
						<td style="text-align: center; padding: 5px;">
							<digi:trn>Import Reports</digi:trn>:
							<html:file property="formFileReports" />
							&nbsp;
							<button style="vertical-align: middle;" type="button" onclick="changePage2(false, '<%=ReportsImpExpConstants.ACTION_IMPORT_FILE %>')">
								<digi:trn>Show</digi:trn> 
							</button>
							&nbsp;&nbsp;&nbsp;
							<digi:trn>Import Tabs</digi:trn>:
							<html:file property="formFileTabs" />
							&nbsp;
							<button style="vertical-align: middle;" type="button" onclick="changePage2(true, '<%=ReportsImpExpConstants.ACTION_IMPORT_FILE %>')">
								<digi:trn>Show</digi:trn> 
							</button>
						</td>
					</tr>
					<logic:notEmpty name="aimImpExpForm" property="reportsList">
                    <tr>                    
                      <td>
                        <table id="reportsTable"  border=0 cellPadding=3 cellSpacing=3 width="100%" >
                        	<thead>
                          <tr bgColor=#999999>
                          	<td bgColor=#999999 align="center" height="20">&nbsp;</td>
                            <td bgColor=#999999 align="center" height="20">
                              <b>
                              ${titleColumn}
                              </b>
                            </td>
                            <td bgColor=#999999 align="center" height="20">
                              <b>
                              <digi:trn key="aim:reportType">
                                Type 
                              </digi:trn>
                              </b>
                            </td>
                            
                            <td bgColor=#999999 align="center" height="20">
                              <b>
                              <digi:trn key="aim:hierarchies">
                              Hierarchies
                              </digi:trn>
                              </b>
                            </td>
                            <td bgColor=#999999 align="center" height="20">
                              <b>
                              <digi:trn>Columns</digi:trn> / <digi:trn>Measures</digi:trn>
                              </b>
                            </td>
                           
                          </tr>  
                          </thead>                        
                          <c:if test="${reportNumber == 0}">
                          <tr>
                            <td colspan="4">
                            <digi:trn key="aim:noreportspresent">
                            No reports present
                            </digi:trn>
                            </td>
                          </tr>
                          </c:if>
                          <logic:iterate name="aimImpExpForm"  property="reportsList" id="report" indexId="idx"
                            type="org.digijava.module.aim.dbentity.AmpReports">
                            <tbody>
                              <tr bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
                              onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="" >
                              <td>
                              	<html:multibox name="aimImpExpForm" property="importReportIndexes" value="${idx}"/>
                              </td>                           
                              <td bgcolor="<%=(idx.intValue()%2==1?"#dbe5f1":"#ffffff")%>" class="reportsBorderTD">
                              <c:if test="${!aimImpExpForm.showTabs}">
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

                              <td>
                                <p style="white-space: nowrap">
                                  <li>
                                      <%
                                        if (report.getType()!=null && report.getType().equals(new Long(1))) {
                                      %>
                                          <digi:trn key="aim:donorType">donor</digi:trn>
	                                  <%
                                        }
                                        else if (report.getType()!=null && report.getType().equals(new Long (3))){
	                                  %>
                                          <digi:trn key="aim:regionalType">regional</digi:trn>
	                                  <%
                                        }
                                        else if (report.getType()!=null && report.getType().equals(new Long(2))){
	                                  %>
                                          <digi:trn key="aim:componentType">component</digi:trn>
	                                  <%
                                        }
                                        else if (report.getType()!=null && report.getType().equals(new Long(4))){
	                                  %>
                                          <digi:trn key="aim:contributionType">contribution</digi:trn>
	                                  <%}%>
	                              </li>
                                  <logic:equal name="report" property="drilldownTab" value="true">
                                    <li>
                                      <digi:trn key="aim:typeDrilldownTab">Desktop Tab</digi:trn>
                                    </li>
                                  </logic:equal>
                                  <logic:equal name="report" property="publicReport" value="true">
                                    <li>
                                      <digi:trn key="aim:typePublicReport">Public Report</digi:trn>
                                    </li>
                                  </logic:equal>
                                  <logic:equal name="report" property="hideActivities" value="true">
                                    <li>
                                      <digi:trn key="aim:typeSummaryReport">Summary Report</digi:trn>
                                    </li>
                                  </logic:equal>                                  
                                  <logic:equal name="report" property="options" value="A">
                                    <li>
                                    	<digi:trn key="aim:annualreport">Annual</digi:trn>
                                    </li>
                                  </logic:equal>
                                  <logic:equal name="report" property="options" value="Q">
                                    <li>
                                    	<digi:trn key="aim:quarterlyreport">Quarterly</digi:trn>
                                    </li>
                                  </logic:equal>
                                  <logic:equal name="report" property="options" value="M">
                                    <li>
                                    	<digi:trn key="aim:monthlyreport">Monthly</digi:trn>	
                                    </li>
                                  </logic:equal>
                                </p>
                              </td>
                              <td>
                               <c:forEach var="hierarchy" items="${report.hierarchies}" >
                                  <%-- <bean:write name="hierarchy" property="column.columnName"/> --%>
                                  <li>
                                  
	                                      	<digi:trn key="aim:report:${hierarchy.column.columnName}">
	                                        	<bean:write name="hierarchy" property="column.columnName" />
	                                      	</digi:trn>
                                  
                                  </li>
                                </c:forEach>
                              </td>

                              <td width="200">  
	                                <div style='position:relative;display:none;' id='report-<bean:write name="report" property="ampReportId"/>'> 
	                                  <logic:iterate name="report" property="columns" id="column" indexId="index"  >
	                                    <%if (index.intValue()%2==0){ %>
	                                      <li>                                      
	                                      	<digi:trn key="aim:report:${column.column.columnName}">
	                                        	<bean:write name="column" property="column.columnName" />
	                                      	</digi:trn>
	                                    <% } else {%>
	                                      ,
	                                      	<digi:trn key="aim:report:${column.column.columnName}">
	                                        	<bean:write name="column" property="column.columnName" />
	                                      	</digi:trn>
	                                      </li>
	                                    <%} %>
	                                  </logic:iterate>
	                                </div>
	                                                                
	                                <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn key="aim:teamreports:columns">columns</digi:trn>',document.getElementById('report-<bean:write name="report" property="ampReportId"/>').innerHTML],Style[0])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn key="aim:teamreports:columns">Columns</digi:trn></u> ]&nbsp;
	                                </span>
                                
	                                <div style='position:relative;display:none;' id='measure-<bean:write name="report" property="measures"/>'> 
	                                  <c:forEach var="measure" items="${report.measures}" >
	                                    <li>
	                                    	<digi:trn key="aim:reportBuilder:${measure.measure.aliasName}">                                      
	                                      		${measure.measure.aliasName}
	                                      	</digi:trn>
	                                    </li>
	                                   </c:forEach>
	                                </div>
	                                
	                                <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="report" property="measures"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br />
	                                </span>
                                </td>
                          </tr>
                          </tbody>
                          </logic:iterate>
                        </table>
                      </td>
                    </tr>      
                    <tr>
                   	    <td align="center">
                   	    	<hr />
                 	    	<strong><digi:trn>Select Teams</digi:trn><strong>:
                   	    	<table cellspacing="10px" cellpadding="10px">
                   	    		<tr>
                   	    			<td>
			                   	    	<html:select property="fakeIds" styleId="sourceOptions" multiple="true" size="4">
			                   	    		<html:option value="-1"><digi:trn>ALL</digi:trn> </html:option>
			                   	    		<html:option value="0" style="background-color: lightblue; font-weight: bold; color: black;" disabled="true"> --- <digi:trn>TEAMS</digi:trn> --- </html:option>
			                   	    		<html:optionsCollection name="aimImpExpForm" property="allAvailableTeams" label="value" value="key"/>
			                   	    	</html:select>
                   	    			</td>
                   	    			<td valign="middle" align="center">
										<button class="buton arrow" type="button" onClick="moveOptions('sourceOptions', 'destinationOptions'); check('destinationOptions', 'importButton');">
											<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
										</button>
										<br/> <br />
										<button class="buton arrow" type="button" onClick="moveOptions('destinationOptions', 'sourceOptions'); check('destinationOptions', 'importButton'); ">
											<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
										</button>
									</td>
                   	    			<td>
                   	    				<html:select property="importTeamIds" styleId="destinationOptions" multiple="true" size="4">
			                   	    		
			                   	    	</html:select>
                   	    			</td>
                   	    		</tr>
                   	    	</table>
	                     
							<button disabled="disabled" style="vertical-align: middle; color: gray" type="button" 
										id="importButton" onclick="selectAllOptions('destinationOptions');changePage2(${aimImpExpForm.showTabs}, '<%=ReportsImpExpConstants.ACTION_IMPORT %>')">
								<digi:trn>Import</digi:trn> 
							</button>	                      	
	                      </td>
                    </tr>
                    </logic:notEmpty>
                  </table>
 </digi:form>