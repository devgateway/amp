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
<%@page import="org.digijava.module.aim.action.reportsimpexp.ReportsExportAction"%>
<%@page import="org.digijava.module.aim.util.reportsimpexp.ReportsImpExpConstants"%>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
  
<script language="JavaScript1.2" type="text/javascript">
  initObj.initialize	= function () {
		//var winH;
		//if (navigator.appName.indexOf("Microsoft")!=-1) {
		//	winH = document.body.offsetHeight;
		//}else{
		//	winH=window.innerHeight;
		//}
		var reporTable=new scrollableTable("reportsTable", 250);
		reporTable.debug=false;
		reporTable.maxRowDepth=1;
		reporTable.scroll();

		var srcEl	= document.getElementById("sourceOptions");
		var destEl	= document.getElementById("destinationOptions");

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

		var list = new Array();
		if ( srcEl.options[0].selected && srcEl.options[0].value==-1) {
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
		var selectEl	= document.getElementById( selectId );
		if ( selectEl.options.length > 0 ) 
			enableButton ( btnId );
		else 
			disableButton( btnId );
	}
	
	function disableButton( btnId ) {
		var buttonEl	= document.getElementById( btnId );
		buttonEl.disabled	= true;
		buttonEl.style.color	= 'gray';
	}

	function enableButton( btnId ) {
		var buttonEl	= document.getElementById( btnId );
		buttonEl.disabled	= false;
		buttonEl.style.backgroundColor= "#ECF3FD";
		buttonEl.style.color= 'black';
		//buttonEl.style.font= '11 px';
	}
	
  </script>
  <!-- translateable browse button -->
  
  <style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}
input.file {
	width: 300px;
	margin: 0;
}
input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}
-->
</style>
  
<script langauage="JavaScript">
	
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn key="aim:browse">Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}	
	}

</script>

<digi:form action="/reportsImport.do" method="post"  enctype="multipart/form-data" >
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

	<table align="center" cellPadding="0" cellSpacing="0" width="100%">
		<tr>
			<td style="text-align: center; padding: 5px;" nowrap="nowrap" align="center">
				<table>
					<tr>
						<td width="20%">&nbsp;</td>
						<td align="center" style="text-align: center;"><digi:trn>Import Tabs/Reports</digi:trn>:</td>
						<td>
							<div class="fileinputs">
								<input id="fileUploaded" name="formFileReportsOrTabs" type="file" class="file">
							</div>
							<button style="vertical-align: middle;background-color: #ECF3FD;text-decoration: none;font: 11px" type="button" onclick="changePageFromImport('<%=ReportsImpExpConstants.ACTION_IMPORT_FILE %>')">
								<digi:trn>Import</digi:trn> 
							</button>
						</td>
					</tr>
				</table>
				<%--
					<div class="fileinputs">
					<input id="fileUploaded" name="formFileReportsOrTabs" type="file" class="file">
					&nbsp;
				<button style="vertical-align: middle;" type="button" onclick="changePageFromImport('<%=ReportsImpExpConstants.ACTION_IMPORT_FILE %>')">
					<digi:trn>Import</digi:trn> 
				</button>
				</div>	
				
				<html:button style="dr-menu" property="" onclick="changePageFromImport('<%=ReportsImpExpConstants.ACTION_IMPORT_FILE %>')">
								<digi:trn>Import</digi:trn> 
							</html:button>		
				 --%>
							
			</td>
		</tr>
				<logic:notEmpty name="aimImpExpForm" property="reportsList">
                    <tr>                    
                    	<td>
                        	<table id="reportsTable"  border="0" cellPadding="3" cellSpacing="3" width="100%" >
                        		<thead>
                          			<tr bgColor="#999999">
                          				<td bgColor="#999999" align="center" height="20">&nbsp;</td>
                            			<td bgColor="#999999" align="center" height="20">
                              				<b>${titleColumn}</b>
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
                              			onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" style="height: 50px" >
                              				<td>
                              					<html:multibox name="aimImpExpForm" property="importReportIndexes" value="${idx}"/>
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
						                          	<b><p style="max-width: 400px;white-space: normal" title="${report.name}">
														<c:if test="${fn:length(report.name) > 120}" >
															<c:out value="${fn:substring(report.name, 0, 120)}" />...
														</c:if>
														<c:if test="${fn:length(report.name) < 120}" >
															<c:out value="${report.name}" />
														</c:if>
						                            </p></b>
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
	                                      						<digi:trn>
	                                        						<bean:write name="column" property="column.columnName" />
	                                      						</digi:trn>
	                                    				<% } else {%>  ,
	                                      						<digi:trn>
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
	                                    					<digi:trn>${measure.measure.aliasName}</digi:trn>
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
											<img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_right.gif"/>
										</button>
										<br/> <br />
										<button class="buton arrow" type="button" onClick="moveOptions('destinationOptions', 'sourceOptions'); check('destinationOptions', 'importButton'); ">
											<img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_left.gif"/>
										</button>
									</td>
                   	    			<td>
                   	    				<html:select property="importTeamIds" styleId="destinationOptions" multiple="true" size="4" style="width:200px">
			                   	    		
			                   	    	</html:select>
                   	    			</td>
                   	    		</tr>
                   	    	</table>	                     
							<button disabled="disabled" style="vertical-align: middle; color: gray" type="button" id="importButton" onclick="selectAllOptions('destinationOptions');changePageFromImport('<%=ReportsImpExpConstants.ACTION_IMPORT %>')">
								<digi:trn>Import</digi:trn> 
							</button>	                      	
						</td>
                    </tr>
				</logic:notEmpty>
			</table>
 </digi:form>

<script type="text/javascript">
	initFileUploads();
</script>