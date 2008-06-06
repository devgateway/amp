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

	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/element/element-beta.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>">.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/animation-min.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dom-min.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/tab/tabview.js'/>" >.</script>
	<%-- <script type="text/javascript" src=".<digi:file src='module/aim/scripts/logger/logger-min.js'/>">.</script> --%>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/ajaxconnection/connection-min.js'/>" > .</script>
	
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop.js'/>" >.</script>
	
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportManager.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" >.</script>
	
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	
	<c:set var="failureMessage">
		<digi:trn key="aim:reportwizard:connectionProblems">Apparently there are some connection problems. Please try again in a few moments.</digi:trn>
	</c:set>
	<c:set var="savingMessage">
		<digi:trn key="aim:reportwizard:savingReport">Saving report</digi:trn>
	</c:set>

	<script type="text/javascript">
		YAHOO.namespace("YAHOO.amp.reportwizard");
		YAHOO.amp.reportwizard.tabView 		= new YAHOO.widget.TabView('wizard_container');
		YAHOO.amp.reportwizard.tabLabels	= new Array("type_tab_label", "columns_tab_label", 
											"hierarchies_tab_label", "measures_tab_label", "details_tab_label");
		selectedCols						= new Array();
		selectedHiers						= new Array();
		selectedMeas						= new Array();
		
		function addOnDragOverAction ( obj, id ) {
			obj.objectdId			= id;
			obj.lastY				= 0;
			obj.goingUp				= false;
			obj.onDragOver	= function (e, tId) {
				var destObj			= document.getElementById(tId);
				
				var srcObj			= document.getElementById(this.id);
				if (destObj.nodeName.toLowerCase()=="li") {
					if ( this.goingUp ) {
						destObj.parentNode.insertBefore(srcObj, destObj);
					}
					else
						destObj.parentNode.insertBefore(srcObj, destObj.nextSibling);
// 					alert(destObj.parentNode.id);
				}
			}
			obj.startDrag	= function(x, y) {
				var realObj	= this.getEl();
				var dragObj	= this.getDragEl();
				dragObj.innerHTML	= realObj.innerHTML;
			}
			obj.endDrag		= function(e) {
				var realObj	= this.getEl();
				var dragObj	= this.getDragEl();
				YAHOO.util.Dom.setStyle(dragObj, "visibility", ""); 
				var a = new YAHOO.util.Motion(  
					dragObj, {  
						points: {  
							to: YAHOO.util.Dom.getXY(realObj) 
						} 
					},  
					0.5,
					YAHOO.util.Easing.easeOut  
				);
				a.onComplete.subscribe(function() { 
					YAHOO.util.Dom.setStyle(dragObj.id, "visibility", "hidden"); 
					YAHOO.util.Dom.setStyle(realObj.id, "visibility", ""); 
				}); 
				a.animate();
			}
			obj.onDrag		= function(e) {
        // Keep track of the direction of the drag for use during onDragOver
				var y = YAHOO.util.Event.getPageY(e);
// 				alert(this.lastY + " -- " + y);
				if (y < this.lastY) {
					this.goingUp = true;
				} else if (y > this.lastY) {
					this.goingUp = false;
				}
				this.lastY = y;
			}
			
			return obj;
		}
		function createDragAndDropItems( parentId ) {
			var src_container	= document.getElementById( parentId );
			var i=0;
			for (i=0; i<src_container.childNodes.length; i++) {
				var elId		= src_container.childNodes[i].id;
				if (elId != null) {
					var draggableItem	= new YAHOO.util.DDProxy(elId);
					draggableItem		= addOnDragOverAction(draggableItem, elId);
					//YAHOO.util.Event.addListener(draggableItem, "onDragOver", draggableItem.onDragOverAction);

				}
			}
		}
		function initializeDragAndDrop() {
// 			if (YAHOO.widget.Logger) {
// 				var reader = new YAHOO.widget.LogReader( "logDiv", 
// 							{ newestOnTop: true, height: "400px" } );
// 			}
			if ( aimReportWizardForm.desktopTab.value == "true" )
				repManager		= new TabReportManager();
			else
				repManager		= new NormalReportManager();
				
			var colObj		= document.getElementById('columnTree');
			var sourceObj	= document.getElementById('source_col_div');
			colObj.parentNode.removeChild(colObj);
			sourceObj.appendChild(colObj);

			columnsDragAndDropObject	= new ColumnsDragAndDropObject('source_col_div');
			columnsDragAndDropObject.createDragAndDropItems();
			new YAHOO.util.DDTarget('source_measures_ul');
			new YAHOO.util.DDTarget('dest_measures_ul');
			new YAHOO.util.DDTarget('source_hierarchies_ul');
			new YAHOO.util.DDTarget('dest_hierarchies_ul');
			measuresDragAndDropObject	= new MyDragAndDropObject('source_measures_ul');
			measuresDragAndDropObject.createDragAndDropItems();
			
			//createDragAndDropItems('source_ul');
			//createDragAndDropItems('dest_col_ul');
			//new YAHOO.util.DDTarget('dest_li_1');
			//new YAHOO.util.DD('logDiv');
			for (i=1; i<5; i++) {
				tab		= YAHOO.amp.reportwizard.tabView.getTab(i);
				tab.set("disabled", true);
			}
			tab2	= YAHOO.amp.reportwizard.tabView.getTab(2);
			tab2.addListener("beforeActiveChange", generateHierarchies);
			
			ColumnsDragAndDropObject.selectObjsByDbId ("source_col_div", "dest_col_ul", selectedCols);
			generateHierarchies();
			MyDragAndDropObject.selectObjsByDbId ("source_hierarchies_ul", "dest_hierarchies_ul", selectedHiers);
			MyDragAndDropObject.selectObjsByDbId ("source_measures_ul", "dest_measures_ul", selectedMeas);
			
			repManager.checkSteps();
			
			saveReportEngine			= new SaveReportEngine("${savingMessage}","${failureMessage}");
		}
		YAHOO.util.Event.addListener(window, "load", initializeDragAndDrop) ;
	</script>


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="75%"
	class="box-border-nopadding">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td valign="bottom" class="crumb" >
			&nbsp;&nbsp;&nbsp;
			<c:set var="translation">
					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
			</c:set>
			<digi:link href="/../aim/viewMyDesktop.do" styleClass="comment" title="${translation}" >
               	<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
               </digi:link> &gt; <digi:trn key="aim:reportwizard:reportgenerator">Report Generator</digi:trn>
			<br />
			<br />
		</td>
	</tr>
	<tr>
		<td class=r-dotted-lg width=14 >&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top>

		<digi:instance property="aimReportWizardForm" />
		<bean:define name="aimReportWizardForm" id="myForm" type="org.digijava.module.aim.form.reportwizard.ReportWizardForm" toScope="request"/>
		<digi:form action="/reportWizard.do" method="post">
		
		<script type="text/javascript">
			<c:forEach items="${aimReportWizardForm.selectedColumns}" var="dbId">
				selectedCols.push('${dbId}');
			</c:forEach>	
			<c:forEach items="${aimReportWizardForm.selectedHierarchies}" var="dbId">
				selectedHiers.push('${dbId}');
			</c:forEach>	
			<c:forEach items="${aimReportWizardForm.selectedMeasures}" var="dbId">
				selectedMeas.push('${dbId}');
			</c:forEach>	
		</script>
		
		
		<html:hidden name="aimReportWizardForm" property="desktopTab"/>
		<div style="color: red; text-align: center; visibility: hidden" id="savingReportDiv">
		</div>
		<br />
		<div id="wizard_container" class="yui-navset">
		<ul class="yui-nav">
			<li id="type_tab_label" class="selected"><a href="#type_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:reporttype">1. Report Type</digi:trn></div></a> </li>
			<li id="columns_tab_label" class="disabled"><a href="#columns_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:columns">2. Columns</digi:trn></div></a> </li>
			<li id="hierachies_tab_label" class="disabled"><a href="#hierarchies_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:hierarchies">3. Hierarchies</digi:trn></div></a> </li>
			<li id="measures_tab_label" class="disabled"><a href="#measures_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:measures">4. Measures</digi:trn></div></a> </li>
			<li id="details_tab_label" class="disabled"><a href="#details_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:details">5. Details</digi:trn></div></a> </li>
		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="type_step_div" class="yui-tab-content" >
					<table cellpadding="5px" width="100%" align="center" >
						<tr>
							<%-- 
							<td width="50%">
								<font color="#3754A1">
									<b><digi:trn key="rep:wizard:typeOfReport">Type of Report</digi:trn></b>
								</font>
								<div id="reportTypeDiv" style="border: 1px solid gray; background-color: white; vertical-align: bottom; height: 120px">
									<html:checkbox property="desktopReport" value="true" onclick="checkSteps()" />
									<digi:trn key="rep:wizard:hint:createDesktopReport"> Create a Desktop Report </digi:trn>
									<br />
									<html:checkbox property="normalReport" value="true"  onclick="checkSteps()"/>
									<digi:trn key="rep:wizard:hint:createNormalReport"> Create a Standard Report </digi:trn>
									<br /><br />
									<font>* 
										<digi:trn key="rep:wizard:hint:bothreporttypes">
											To create both (Standard and Desktop Report) select both checkboxes
										</digi:trn>
									</font>
									<br /><br />
									<span id="reportTypeMust">
										<font color="red">* 
											<digi:trn key="rep:wizard:hint:mustselect">
												Must select at least one type
											</digi:trn>
										</font>
									</span>
									
								</div>
							</td> --%>
							<td width="25%">&nbsp;</td>
							<td width="50%">
								<font color="#3754A1">
									<b><digi:trn key="rep:wizard:fundingGrouping"> Funding Grouping </digi:trn></b>
								</font>
								<div id="reportGroupDiv" style="border: 1px solid gray; background-color: white; 
											vertical-align: bottom;height: 130px; padding-top: 15px; padding-left: 30px; padding-bottom: 5px; padding-right: 5px;">
								<table>
								<feature:display name="Donor Report" module="Reports">
                                             <tr>
                                               <td>
                                                 <html:radio property="reportType" value="donor"  onclick="repManager.checkSteps()">
                                                   <digi:trn key="aim:donorReport">
                                                   Donor Report (Donor Funding)
                                                   </digi:trn>
                                                 </html:radio>
                                               </td>
                                             </tr>
                                             </feature:display>
                                             <feature:display name="Regional Report" module="Reports">										
                                             <tr>
                                               <td>
                                                 <html:radio property="reportType" value="regional"  onclick="repManager.checkSteps()">
                                                   <digi:trn key="aim:regionalReport">
                                                   Regional Report (Regional Funding)
                                                   </digi:trn>
                                                 </html:radio>
                                               </td>
                                             </tr>
                                             </feature:display>
                                             <feature:display name="Component Report" module="Reports">
                                             <tr>
                                               <td>
                                 	                <html:radio property="reportType" value="component"  onclick="repManager.checkSteps()">
                                                   <digi:trn key="aim:componentReport">
                                                   Component Report (Component Funding)
                                                   </digi:trn>
                                                 </html:radio>
                                               </td>
                                             </tr>
                                             </feature:display>
                                               <feature:display module="Reports" name="Contribution Report">
                                                 <tr>
                                                   <td>
                                                     <html:radio property="reportType" value="contribution"  onclick="repManager.checkSteps()">
                                                       <digi:trn key="aim:contributionReport">
                                                       Contribution Report (Activity Contributions)
                                                       </digi:trn>
                                                     </html:radio>
                                                   </td>
                                                 </tr>
                                               </feature:display>
                                     </table>
									<br />
									<span id="fundingGroupingMust">
										<font color="red">* 
											<digi:trn key="rep:wizard:hint:mustselect">
												Must select at least one type
											</digi:trn>
										</font>
									</span>
								</div>
							</td>
							<td width="25%">&nbsp;</td>
						</tr>
					</table>				
				</div>
				<div id="columns_step_div"  class="yui-tab-content" style="display: none; width: " align="center">
					<table cellpadding="5px" style="vertical-align: middle" width="100%">
						<tr>
						<td width="47%" align="center">
							<font color="#3754A1">
								<b><digi:trn key="rep:wizard:availableColumns">Available Columns</digi:trn></b>
							</font>	
							<div id="source_col_div" class="draglist">
							</div>
						</td>
						<td valign="middle" align="center">
							<button class="buton" type="button" onClick="ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
							</button>
							<br/> <br />
							<button class="buton" type="button" onClick="ColumnsDragAndDropObject.deselectObjs('dest_col_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
							</button>
						</td>
						<td width="47%" align="center">
							<font color="#3754A1">
								<b><digi:trn key="rep:wizard:selectedColumns">Selected Columns</digi:trn></b>
							</font>
							<ul id="dest_col_ul" class="draglist">
							
							</ul>
						</td>
						</tr>
						<tr>
							<td>
								<span id="columnsMust">
								<font color="red">* 
									<digi:trn key="rep:wizard:hint:mustselectcolumn">
										Must select at least one column
									</digi:trn>
								</font>
								</span>
							</td>
						</tr>
					</table>
				</div>
				<div id="hierarchies_step_div"  class="yui-tab-content" style="display: none;">
					<table cellpadding="5px" style="vertical-align: middle" >
						<tr>
						<td width="47%" align="center">
							<font color="#3754A1">
								<b><digi:trn key="rep:wizard:availableHierarchies">Available Hierarchies</digi:trn></b>
							</font>
							<ul id="source_hierarchies_ul" class="draglist">
							</ul>
						</td>
						<td valign="middle" align="center">
							<button class="buton" type="button" onClick="MyDragAndDropObject.selectObjs('source_hierarchies_ul', 'dest_hierarchies_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
							</button>
							<br/> <br />
							<button class="buton" type="button" onClick="MyDragAndDropObject.deselectObjs('dest_hierarchies_ul', 'source_hierarchies_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
							</button>
						</td>
						<td width="47%" align="center">
							<font color="#3754A1">
								<b><digi:trn key="rep:wizard:selectedHierarchies">Selected Hierarchies</digi:trn></b>
							</font>
							<ol id="dest_hierarchies_ul" class="draglist">
							</ol>					
						</tr>
					</table>
				</div>
				<div id="measures_step_div"  class="yui-tab-content" style="display: none;">
					<table cellpadding="5px" style="vertical-align: middle">
						<tr>
						<td width="47%" align="center">
							<font color="#3754A1">
								<b><digi:trn key="rep:wizard:availableMeasures">Available Measures</digi:trn></b>
							</font>
							<ul id="source_measures_ul" class="draglist">
								<jsp:include page="setMeasures.jsp" />
							</ul>
						</td>
						<td valign="middle"  align="center">
							<button class="buton" type="button" onClick="MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
							</button>
							<br/> <br />
							<button class="buton" type="button" onClick="MyDragAndDropObject.deselectObjs('dest_measures_ul', 'source_measures_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
							</button>
						</td>
						<td width="47%" align="center">
							<font color="#3754A1">
								<b><digi:trn key="rep:wizard:selectedMeasures">Selected Measures</digi:trn></b>
							</font>
							<ul id="dest_measures_ul" class="draglist">
							</ul>					
						</tr>
						<tr>
							<td colspan="3">
								<span id="measuresMust">
								<font color="red">* 
									<digi:trn key="rep:wizard:hint:mustselectmeasure">
										Must select at least one measure
									</digi:trn>
								</font>
								</span>
							</td>
						</tr>
					</table>
				</div>
				<div id="details_step_div"  class="yui-tab-content" style="display: none;">
					<table cellpadding="5px" style="vertical-align: middle">
						<tr>
							<td width="20%">&nbsp;</td>
							<td>
								<font color="#3754A1">
								<b>	<digi:trn key="aim:reportBuilder:ReportTitle">Report Title</digi:trn> </b>
								</font>
								<br />
								<span id="reportTitleSpan">
								<html:textarea onkeyup="repManager.checkSteps()" property="reportTitle" cols="60" rows="2" styleClass="inp-text" />
								</span>
							</td>
							<td width="20%">&nbsp;</td>
						</tr>
						<tr>
							<td width="20%">&nbsp;</td>
							<td>
								<font color="#3754A1">
								<b>	<digi:trn key="aim:reportBuilder:ReporDescription">Report Description</digi:trn> </b>
								</font>
								<br/>
								<html:textarea property="reportDescription" cols="60" rows="10" styleClass="inp-text" />
							</td>
							<td width="20%">&nbsp;</td>
						</tr>
						<tr>
							<td width="20%">&nbsp;</td>
							<td>
								<font color="#3754A1">
								<b>	<digi:trn key="aim:reportBuilder:TotalsGrouping">Totals Grouping</digi:trn> </b>
								</font>
								<div id="totalsGroupingDiv" style="border: 1px solid gray; background-color: white; vertical-align: bottom;">
									<html:checkbox property="hideActivities" value="true">
										<digi:trn key="aim:summaryReport">
											Summary Report
										</digi:trn>
									</html:checkbox>
									<br />
									<html:radio property="reportPeriod" value="A">
										<digi:trn key="aim:AnnualReport">
											Annual Report
										</digi:trn>
									</html:radio>								
									<html:radio property="reportPeriod" value="Q">
										<digi:trn key="aim:QuarterlyReport">
											Quarterly Report
										</digi:trn>
									</html:radio>
									<html:radio property="reportPeriod" value="M">
										<digi:trn key="aim:MonthlyReport">
											Monthly Report
										</digi:trn>
									</html:radio>
								</div>
							</td>
							<td width="20%">&nbsp;</td>
						</tr>
						<tr>
							<td width="20%">&nbsp;</td>
							<td>
								<span id="detailsMust">
								<font color="red">* 
									<digi:trn key="rep:wizard:hint:mustDetails">
										The report must have a title
									</digi:trn>
								</font>
								</span>
							</td>
							<td width="20%">&nbsp;</td>
						</tr>
						<tr>
							<td align="right" colspan="3">
								<button 
								id="saveReportButton" type="button" class="buton" disabled="disabled" 
								style="color: lightgray" onclick="saveReportEngine.saveReport()">
									<digi:trn key="btn:saveReport">Save Report</digi:trn>
								</button>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<%-- <div id="logDiv" style="border: medium solid red; width: 20%;">
		</div> --%>
		<span style="display: none">
			<jsp:include page="setColumns.jsp" />
		</span>
		</digi:form>
	</td>
	</tr>
</table>
	