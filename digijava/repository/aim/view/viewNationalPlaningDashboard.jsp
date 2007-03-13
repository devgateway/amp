<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>


<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/tree.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/yahoo.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/event.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/treeview.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/jktreeview.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<style type="text/css">
	a { text-decoration: underline; color: #46546C; }
	a:hover { text-decoration: underline; color: #4d77c3; }
	#tree {width:250px;padding: 10px;float:left;}
</style>

<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/container.css"/>">
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/yahoo-dom-event.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/container-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/connection-min.js"/>" ></script>


<script type="text/javascript">
	function newWin(val) {
		<digi:context name="selectLoc" property="context/module/moduleinstance/viewOrganisation.do" />
		var url = "<%=selectLoc%>?ampOrgId=" + val;
		openNewWindow(635, 600);
		//obj.target = popupPointer.name;
		popupPointer.document.location.href = url;
		//obj.href = url;
	}
</script>

<digi:form action="/nationalPlaningDashboard.do">

<script language="javascript" type="text/javascript">
function setActionMethod(methodName) {
  document.getElementsByName('actionMethod')[0].value=methodName;
  return true;
}

function browseProgram(programId) {
  setActionMethod('display');
  document.getElementsByName('currentProgramId')[0].value=programId;
  document.forms['aimNationalPlaningDashboardForm'].submit();
  return false;
}

function showChart(argument) {
  setActionMethod('displayWithFilter');
  document.getElementsByName('showChart')[0].value=argument;
  document.forms['aimNationalPlaningDashboardForm'].submit();
  return false;
}

function doFilter() {
  setActionMethod('displayWithFilter');
  
  document.forms['aimNationalPlaningDashboardForm'].submit();
  return false;
}

	var indNameArr=[];	
	var indIdArr=[];
	<c:if test="${!empty aimNationalPlaningDashboardForm.indicators}">
		<c:forEach varStatus="st" var="ind" items="${aimNationalPlaningDashboardForm.indicators}">
			indIdArr[${st.index}]=${ind.ampThemeIndId};
			indNameArr[${st.index}]='${ind.name}';
		</c:forEach>
	</c:if>

	var selIndics=[];
	<c:if test="${!empty aimNationalPlaningDashboardForm.selectedIndicators}">
		<c:forEach varStatus="sl" var="ind" items="${aimNationalPlaningDashboardForm.selectedIndicators}">
			selIndics[${sl.index}]=${ind};
		</c:forEach>
	</c:if>


	var curProgId;
	var curProgNodeIndex;
	var lastTreeUpdate=0;
	var line=new Array();
	var lineIter=0;
	var openNodes=new Array();
	var openNodeIter=0;
	
	/* fill array with program and its parents*/
	function fillLine(progTree, curProg){
		if (curProg==null){
			return;
		}
		
		line[lineIter]=curProg.getAttribute("id");
	
		lineIter++;
		var parentID=curProg.getAttribute("parentID");
		if (parentID!=-1){
			var parentProg=findProgWithID(progTree,parentID);
			fillLine(progTree,parentProg);
		}
	}

	/* finds programs with specified ID */
	function findProgWithID(progTree, toFindID){
		if (toFindID==null) return null;
		var allProgs=progTree.getElementsByTagName("program");
		for(var i=0;i<allProgs.length;i++){
			var id=allProgs[i].getAttribute("id");
			if(id==toFindID) return allProgs[i];
		}
	}

	/*determines if node should be openned.
	Nod should be opened if it is parent of the current node.
	This function uses array that was filled with fillLine() functioin */
	function toBeOpened(progID){
		for(var i=0;i<line.length;i++){
			if (line[i]==progID) return true;
		}
		return false;
	}

	/* creates tree view object from XML. this is called form callback*/
	function updateTree(myXML){
		if (myXML==null) {
			return;
		}
		
		var targetDiv=document.getElementById("tree");
		
		var progListRoot=myXML.getElementsByTagName("progTree")[0];
		
		if (progListRoot==null){
			targetDiv.innerHTML=" "+myXML;
			return;
		}

		clearTree(targetDiv);
		var programList=progListRoot.childNodes;
	
		if (programList==null){
			targetDiv.innerHTML="<i>No Programs</i>";
			return;
		}else{
			if(programList.length==0){
				targetDiv.innerHTML="<i>No Programs</i>";
				return;
			}
		}

		curProgId=document.getElementsByName('currentProgramId')[0].value;
		
		var curProg=findProgWithID(progListRoot,curProgId);	
		
		//determine which programs should be openned
		fillLine(progListRoot,curProg);

		//create TreeView Object for specified with ID HTML object.
		var ptree=new jktreeview("tree");

		//build nodes
		buildTree(programList,ptree,"");
		
		//draw tree
		ptree.treetop.draw();
		
		openNodeIter=0;
		
		//open nodes
		goToCurrentNode();
		
		//highlight current Node
		highlightNode(curProgNodeIndex);
		
		setNumOfPrograms (myXML);
		addRootListener();
		addEventListeners();
		
		createPanel("Info","<i>info</i>");
	}

	/* recursivly builds tree nodes */
	function buildTree(programs,treeObj,target){
		if(programs==null || programs.length==0 || target==null){
			return null;
		}
		//for every nod in this level:
		for(var i=0;i<programs.length;i++){
			var prg=programs[i];
			if(prg.tagName=="program"){
				//prepare node data
				var prgID=prg.getAttribute("id");
				var prgURL="javascript:browseProgram(\'"+prgID+"\')";
				var prgName=prg.getAttribute("name");
				var prgParent=target;
				//create tree view node object
				var thisNode=treeObj.addItem(prgName,prgParent,prgURL);
				//save node if it is parent of current or current program
				if(toBeOpened(prgID)==true){
					openNodes[openNodeIter++]=thisNode;
				}
				//save index in tree for current program
				if(prgID==curProgId){
					curProgNodeIndex=thisNode.index;
					thisNode.indicators=getIndicatorsHTML();
				}
				var subNodes=prg.childNodes;
				//recurs on children programs
				if(subNodes != null){
					for (var j=0;j<subNodes.length;j++){
						if (subNodes[j].tagName=="children"){
							var subProgs=subNodes[j].childNodes;
							buildTree(subProgs,treeObj,thisNode);
						}
					}
				}
			}
		}
	}

	/* opens previously saved nodes */
	function goToCurrentNode(){
		for(var i=0;i<openNodes.length;i++){
			openNodes[i].toggle();
		}
	}
	
	function getIndicatorsHTML(){
		var indics=document.getElementsByName('indicators');
		var result='No Indicators';
		if (indIdArr.length>0){
			result='<table border="0" cellpadding="0" cellspacing="0">';
			for(var i=0;i<indIdArr.length;i++){
				result+='<tr><td><input type="checkbox" name="selectedIndicators" onchange="return doFilter()" value="'+indIdArr[i]+'" ';
				if(isSelectedIndicator(indIdArr[i])){
					result+='checked ';
				}
				result+=' >'+indNameArr[i]+'</td><td>&nbsp;&nbsp;</td></tr>';
			}
			result+='</table>';
		}
		return result;
	}	
	
	/* highlights node for current program 	*/
	function highlightNode(nodeIndex){
		if(nodeIndex!=null){
			var nodTD=document.getElementById('ygtvlabelel'+nodeIndex);
			if(nodTD!=null){
				nodTD.style.fontWeight='bolder';
//				var color=getRealColor(nodTD);
//				nodTD.style.color=nodTD.style.backgrundColor;
//				nodTD.style.backgrundColor=color;
			}
		}
		
	}

	function getRealColor(element){
		if (element==null) return;
		if (element.style.color==''){
			if (element.parentNode!=null){
				return getRealColor(element.parentNode);
			}else{
				return;
			}
		}else {
			return element.style.color;
		}
	}

	function isSelectedIndicator(indicId){
		for(var i=0;i<selIndics.length;i++){
			if (selIndics[i]==indicId) return true;
		}
		return false;
	}

	/* callback function which is registered for asynchronouse responce.
		this function should have special signature to be used as callback in the asynchronous class
	*/
	function myCallBack(status, statusText, responseText, responseXML){
		updateTree(responseXML);		
	}

	function clearTree(myTree){
		while (myTree.childNodes.length>0){
			myTree.removeChild(myTree.childNodes[0]);
		}
	}

	function addActionToURL(actionName){
		var fullURL=document.URL;
		var lastSlash=fullURL.lastIndexOf("/");
		var partialURL=fullURL.substring(0,lastSlash);
		return partialURL+"/"+actionName;
	}
	function treeLoadSuccess(o) {
		//alert("I am in treeLoadSuccess");
		//alert(o.responseXML);
		myCallBack(o.status, o.statusText, o.responseText, o.responseXML);
	}
	function treeLoadFailure(o) {
		alert("Error loading tree: " + o);
	}

	/* function to be called at page load to asynchronously call server for tree data.
		Later we may convert all page to asynchronous style to not refresh page at all.
	 */	
	function initTree(){
		var treeList=document.getElementById('tree');
		treeList.innerHTML="<i>Loading...</i>"
		
		var url=addActionToURL("getThemeTreeNode.do");
		//var async=new Asynchronous();
		//async.complete=myCallBack;
		//async.call(url);
		
		var callObject	= {
			success: treeLoadSuccess,
			failure: treeLoadFailure,
			timeout: 5000
		}
		YAHOO.util.Connect.asyncRequest('POST', url, callObject);
	}

	function previewActivity(actId){
		
	}
	var numOfPrograms;				// Number of programs (themes) displayed
	var informationPanel;			// The panel on which the information is displayed
	var themeArray	= new Array();  // Array containing the formatted information for the themes

	/*Gets total number of programs from the xml tree */
	function setNumOfPrograms (xml) {
		var elements	=	xml.getElementsByTagName("program");
		numOfPrograms	= elements.length;
		//alert ("xml: " + numOfPrograms);
	}
	/* When clicking on a '+' to expand the tree the listeners for the other elements are refreshed */
	function addRootListener () {
		var tree		= document.getElementById('tree');
		YAHOO.util.Event.addListener(tree, "click", addEventListeners);
	}
	/* Adds listeners for all elemets in the tree */
	function addEventListeners () {
		for(var j=1;j<=numOfPrograms;j++){
							var n	= document.getElementById('ygtvlabelel'+j);
							YAHOO.util.Event.addListener(n, "mouseover", eventFunction);
							YAHOO.util.Event.addListener(n, "mouseout", hidePanel);
		}
	}
	/* Function that is executed when mouse over an element */
	function eventFunction(e) {
		//alert('S-a apelat eventFunction	' + this.href + '||' + getIdFromHref(this.href) );
		showPanel(this.innerHTML, getIdFromHref(this.href), e.clientX, e.clientY);
		
	}
	/* Extracts the id (database id of AmpTheme) from the href property */
	function getIdFromHref( href ) {
		var start	= href.indexOf("('");
		var end		= href.indexOf("')");
		return href.substring(start+2, end);
 	}
 	/* Creates the panel used to show information */
 	function createPanel(headerText, bodyText) {
 		//YAHOO.namespace("amp.container");
 		//alert('Create Panels');
 		//YAHOO.amp.container.panel2 = new YAHOO.widget.Panel("panel2", { width:"300px", visible:true, draggable:false, close:true } );
		//YAHOO.amp.container.panel2.setHeader("Panel #2 from Script");
		//YAHOO.amp.container.panel2.setBody("This is a dynamically generated Panel.");
		//YAHOO.amp.container.panel2.setFooter("End of Panel #2");
		//YAHOO.amp.container.panel2.render(document.body);
		
		//YAHOO.amp.container.panel2.moveTo(50,50);
		
		
		informationPanel	= new YAHOO.widget.Panel("infoPanel", { width:"300px", visible:false, draggable:false, close:true } );
		informationPanel.setHeader(headerText);
		informationPanel.setBody(bodyText);
		informationPanel.render(document.body);
		
		infoPanelObj	= document.getElementById('infoPanel');
		
		YAHOO.util.Event.addListener(infoPanelObj, "mouseover", makePanelVisible);
		YAHOO.util.Event.addListener(infoPanelObj, "mouseout", hidePanel);
		
 	}
 	/* Updates the panels header, body and position and makes it visible */
 	function showPanel(headerText, id, posX, posY) {
 		informationPanel.setHeader(headerText);
		informationPanel.setBody(themeArray[id]);
		informationPanel.moveTo(posX+2, posY+2);
		informationPanel.show();
 	}
 	/* Just makes the panel visible */
 	function makePanelVisible() {
 		informationPanel.show();
 	}
 	/* Just makes the panel invisible */
 	function hidePanel() {
 		informationPanel.hide();
 	}
 	/* Adds the information for a theme to the themeArray array in the corresponding position (=pid). */
 	function addProgramInformation(pid, programName, description, leadAgency, programCode, programType, targetGroups,
 				background, objectives, outputs, beneficiaries, environmentConsiderations) {
 			var panelBody =	"";
 			panelBody += '<table border="0">';
 			panelBody += '<tr><td align="left"><b>Program Name:</b>&nbsp;</td><td>'+ programName +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Description:</b>&nbsp;</td><td>'+ description +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Lead Agency:</b>&nbsp;</td><td>'+ leadAgency +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Program Code:</b>&nbsp;</td><td>'+ programCode +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Program Type:</b>&nbsp;</td><td>'+ programType +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Target Groups:</b>&nbsp;</td><td>'+ targetGroups +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Background:</b>&nbsp;</td><td>'+ background +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Objectives:</b>&nbsp;</td><td>'+ objectives +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Outputs:</b>&nbsp;</td><td>'+ outputs +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Beneficiaries:</b>&nbsp;</td><td>'+ beneficiaries +'</td></tr>';
 			panelBody += '<tr><td align="left"><b>Environment Considerations:</b>&nbsp;</td><td>'+ environmentConsiderations +'</td></tr>';
 			panelBody += '</table>'; 
 			
 			themeArray[pid]	= panelBody;
 	
 	}
	window.onload=initTree;
</script>
<script language="javascript" type="text/javascript">
	<digi:instance property="aimNationalPlaningDashboardForm" />
	<logic:iterate id="theme" name="aimNationalPlaningDashboardForm" property="allThemes" type="org.digijava.module.aim.dbentity.AmpTheme" >
		addProgramInformation(	'<bean:write name="theme" property="ampThemeId" />',
								'<bean:write name="theme" property="name" />',
								'<bean:write name="theme" property="description" />',
								'<bean:write name="theme" property="leadAgency" />',
								'<bean:write name="theme" property="themeCode" />',
								'<bean:write name="theme" property="type" />',
								'<bean:write name="theme" property="targetGroups" />',
								'<bean:write name="theme" property="background" />',
								'<bean:write name="theme" property="objectives" />',
								'<bean:write name="theme" property="outputs" />',
								'<bean:write name="theme" property="beneficiaries" />',
			 					'<bean:write name="theme" property="environmentConsiderations" />'
		);
	</logic:iterate>
</script>
  <html:hidden property="actionMethod"/>
  <html:hidden property="currentProgramId"/>
  <html:hidden property="showChart"/>
  <TABLE width="100%" cellspacing="1" cellpadding="4" valign="top" align="left">
    <TR>
      <TD>
        <TABLE border="0" cellPadding="0" cellSpacing="0" width="100%">
          <TR>
            <TD>
              <TABLE border="0" cellPadding="0" cellSpacing="0">
                <TR>
                  <TD bgColor="#c9c9c7" class="box-title" width="80">                    &nbsp;
                    <digi:link href="/viewPortfolioDashboard.do~actId=-1~indId=-1">
                      <digi:trn key="aim:portfolioDashboard">Dashboard</digi:trn>
                    </digi:link>
                  </TD>
                  <TD background="module/aim/images/corner-r.gif" height="17" width="17">                  </TD>
                  <TD bgColor="#c9c9c7" class="box-title" width="220" >                    &nbsp;
                    <digi:trn key="aim:npDashboard">National Planing Dashboard</digi:trn>
                  </TD>
                  <TD background="module/aim/images/corner-r.gif" height="17" width="17">                  </TD>
                </TR>
              </TABLE>
            </TD>
          </TR>
          <TR>
            <TD bgColor="#ffffff" class="box-border" align="left">
              <table width="100%" cellspacing="5" cellpadding="5">
                <tr>
                  <td align="center" class="textalb" bgcolor="#336699" width="50%">
                    ${aimNationalPlaningDashboardForm.currentProgram.name}
                  </td>
                  <td align="center" class="textalb" bgcolor="#336699" width="50%">
                    <digi:trn key="aim:npObjectives">National Planing Objectives</digi:trn>
                  </td>
                </tr>
                <tr>
                  <td valign="top" width="50%">
                  
                  <!-- This is main left Table -->
                    <table cellspacing="0" border="0">
                    
					<!-- Sub Tabs start -->                    
                      <tr>
                      	<td bgColor="#c9c9c7" class="box-title" width="40">
                          <c:if test="${aimNationalPlaningDashboardForm.showChart}">
                            <a href="JavaScript:showChart(false);">
                              <digi:trn key="aim:npData">Data</digi:trn>
                            </a>
                          </c:if>
                          <c:if test="${!aimNationalPlaningDashboardForm.showChart}">
                            <digi:trn key="aim:npData">Data</digi:trn>
                          </c:if>
                        </td>                        
                        <td background="module/aim/images/corner-r.gif" style="background-repeat: no-repeat" height="17" width="17"></td>
                        <td bgColor="#c9c9c7" class="box-title" width="40">
                          <c:if test="${!aimNationalPlaningDashboardForm.showChart}">
                            <a href="JavaScript:showChart(true);">
                              <digi:trn key="aim:npChart">Chart</digi:trn>
                            </a>
                          </c:if>
                          <c:if test="${aimNationalPlaningDashboardForm.showChart}">
                            <digi:trn key="aim:npChart">Chart</digi:trn>
                          </c:if>
                        </td>
                        <td background="module/aim/images/corner-r.gif" style="background-repeat: no-repeat"  height="17">&nbsp;</td>
                      </tr>
                    <!-- Sub tabs End -->

                      <!-- chart -->
                      <tr>
                      	<td colspan="4">&nbsp;</td>
                      </tr>
                    <c:if test="${aimNationalPlaningDashboardForm.showChart}">
                      <tr>
                        <td colspan="4" align="center">

                        <digi:context name="showChart" property="/module/moduleinstance/nationalPlaningDashboard.do"/>
                        <c:url var="fullShowChartUrl" scope="page" value="${showChart}">
                          <c:param name="actionMethod" value="displayChart" />
                          <c:param name="currentProgramId" value="${aimNationalPlaningDashboardForm.currentProgramId}" />
                          <c:forEach var="selVal" items="${aimNationalPlaningDashboardForm.selectedIndicators}">
                            <c:param name="selectedIndicators" value="${selVal}" />
                          </c:forEach>
                        </c:url>
                        <img alt="chart" src="${fullShowChartUrl}" width="300" />
                        </td>
                      </tr>
                    </c:if>

                      <!-- end of chart-->
                      <%-- start of data --%>
                    <c:if test="${! aimNationalPlaningDashboardForm.showChart}">

						<tr>
                        	<td colspan="4" align="center">
							<%-- data grid outer table starts--%>
								<table border="0" width="100%">
									<tr>
										<td>
											<%-- data grid year dropdowns starts
											<table>
												<tr>
													<td> from: </td>
													<td>
														<html:select property="fromYear">
															<option value="-1"><digi:trn key="aim:npSelectYear">Select</digi:trn></option>
															<html:optionsCollection name="aimNationalPlaningDashboardForm" property="years" value="value" label="label" />
														</html:select>
													</td>
													<td> to: </td>
													<td>
														<html:select property="toYear">
															<option value="-1"><digi:trn key="aim:npSelectYear">Select</digi:trn></option>
															<html:optionsCollection name="aimNationalPlaningDashboardForm" property="years" value="value" label="label" />
														</html:select>
													</td>
													<td><input type="Button" value="Go" onclick="return doFilter()"></td>
												</tr>
											</table>
											 data grid year dropdowns ends--%>
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;
										</td>
									</tr>
									<tr>
										<td>
											<%-- data grid table starts--%>
											<table width="90%" border="1" cellpadding="0" cellspacing="0">
												<tr>
													<td>&nbsp;</td>
													<td	width="100"><strong>Indicator Name</strong></td>
													<td width="100"><strong>Actual Value</strong></td>
													<td width="100"><strong>Target Value</strong></td>
												</tr>
												<tr>
													<c:forEach var="themeMember" items="${aimNationalPlaningDashboardForm.programs}">
														<c:if test="${aimNationalPlaningDashboardForm.currentProgramId == themeMember.member.ampThemeId}">
								                            <c:forEach var="indicator" items="${themeMember.member.indicators}">
																<tr>
																<!-- td>
																	<html:multibox property="selectedIndicators" onchange="return doFilter()" value="${indicator.ampThemeIndId}" />
																</td-->
																<td width="100">
																	${indicator.name}
																	&nbsp;
																</td>
																<td width="100">
																
																	<c:if test="${! empty aimNationalPlaningDashboardForm.valuesForSelectedIndicators}">
																		<c:forEach var="indicValue" items="${aimNationalPlaningDashboardForm.valuesForSelectedIndicators}">
																			<c:if test="${indicValue.indicatorId == indicator.ampThemeIndId}">
																				<c:if test="${indicValue.valueTypeId == 1}">
																					${indicValue.value}
																				</c:if>
																			</c:if>
																		</c:forEach>
																	</c:if>
																	<c:if test="${empty aimNationalPlaningDashboardForm.valuesForSelectedIndicators}">
																		N/A
																	</c:if>
																	&nbsp;
																</td>

																<td width="100">
																
																	<c:if test="${! empty aimNationalPlaningDashboardForm.valuesForSelectedIndicators}">
																		<c:forEach var="indicValue" items="${aimNationalPlaningDashboardForm.valuesForSelectedIndicators}">
																			<c:if test="${indicValue.indicatorId == indicator.ampThemeIndId}">
																				<c:if test="${indicValue.valueTypeId == 0}">
																					${indicValue.value}
																				</c:if>
																			</c:if>
																		</c:forEach>
																	</c:if>
																	<c:if test="${empty aimNationalPlaningDashboardForm.valuesForSelectedIndicators}">
																		N/A
																	</c:if>
																	&nbsp;
																</td>

															</tr>
							                            </c:forEach>
													</c:if>
					                            </c:forEach>
											</tr>
										</table>
										<%-- data grid table ends--%>
									</td>
								</tr>
							</table>
							<!-- data outer table ends-->
                        </td>
					</tr>
                    </c:if>

					<%-- end of data--%>
				</table><!-- this is main left Table -->
			</td>
			<td valign="top" class="highlight" align="left" width="100%">
				<div id="tree" style="width: 100%;"></div>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right">
				<digi:link href="/advancedReportManager.do~clear=true"><digi:trn key="aim:NPD:advancedReportsLink">Advanced Reports</digi:trn></digi:link>
			</td>
		</tr>
        <tr>
                  <td colspan="2">
                    <table width="100%" cellpadding="4" cellSpacing="1" bgcolor="#ffffff" valign="top" align="left">
                      <tr bgcolor="#DDDDDD">
                        <td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
                        onMouseOut="this.className='colHeaderLink'" width=50%>
                        <digi:trn key="aim:npPlannedActivitiesFor">Planned activities for</digi:trn>: ${aimNationalPlaningDashboardForm.currentProgram.name}
                        </td>

                        			<td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
		                     		onMouseOut="this.className='colHeaderLink'" width="25%" nowrap="nowrap">
									<html:select property="selectedDonors" onchange="return doFilter()">
										<option value="-1"><digi:trn key="aim:npd:dropDownAnyDonor">Any Donor</digi:trn></option>
										<html:optionsCollection name="aimNationalPlaningDashboardForm" property="donors" value="value" label="label" />
									</html:select>
                			        </td>
		
    		                	    <td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
        		                	onMouseOut="this.className='colHeaderLink'" nowrap="nowrap">
									<html:select property="fromyearActivities" onchange="return doFilter()">
										<option value="-1"><digi:trn key="aim:npd:dropDownFromYear">From Year</digi:trn></option>
										<html:optionsCollection name="aimNationalPlaningDashboardForm" property="years" value="value" label="label" />
									</html:select>
                        			<%--<digi:trn key="aim:npFromYear">From Year</digi:trn>--%>
		                        	</td>
	
    		                    	<td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
        		                	onMouseOut="this.className='colHeaderLink'" nowrap="nowrap">
									<html:select property="toYearActivities" onchange="return doFilter()">
										<option value="-1"><digi:trn key="aim:npd:dropDownToYear">To Year</digi:trn></option>
										<html:optionsCollection name="aimNationalPlaningDashboardForm" property="years" value="value" label="label" />
									</html:select>
                        			<%--<digi:trn key="aim:npToYear">To Year</digi:trn>--%>
                        			</td>

                     			   <%--<td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
			                        onMouseOut="this.className='colHeaderLink'" width="40%" nowrap="nowrap">
									<html:select property="selectedStatuses" onchange="return doFilter()">
										<html:option value="-1"><digi:trn key="aim:npd:dropDownAnyStatus">Any Status</digi:trn></html:option>
										<html:optionsCollection name="aimNationalPlaningDashboardForm" property="activityStatuses" value="statusCode" label="name" />
									</html:select>
                    			    </td>--%>
						
                        			<td class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
			                        onMouseOut="this.className='colHeaderLink'" width="15%" nowrap="nowrap">
            			            <digi:trn key="aim:npAmountAndCurrency">Amount&amp;Currency</digi:trn>
                        			</td>
                        </tr>
                      <c:forEach var="activity" items="${aimNationalPlaningDashboardForm.activities}">
                        <tr>
                          <td valign="top">
	                          <digi:link module="aim" target="_blank" href="/viewActivityPreview.do~pageId=2~activityId=${activity.ampActivityId}">${activity.name}</digi:link>
                          </td>
                          <td>
								<c:if test="${!empty activity.funding}">
											<TABLE cellspacing=1 cellpadding=1>
												<c:forEach var="dnr" items="${activity.funding}">
													<TR><TD>
														<a href="javascript:newWin('${dnr.ampDonorOrgId.ampOrgId}')">
														${dnr.ampDonorOrgId.name}</a>
													</TD></TR>
												</c:forEach>
											</TABLE>
										</c:if>
										<c:if test="${empty activity.funding}">
											<digi:trn key="aim:unspecified">Unspecified</digi:trn>
										</c:if>
                          </td>
                          <td valign="top">${activity.activityStartDate}</td>
                          <td valign="top">${activity.originalCompDate}</td>
                          <td valign="top" align="right">
                          
                          <fmt:formatNumber var="funAmount" maxFractionDigits="2" minFractionDigits="2" groupingUsed="true" value="${activity.funAmount}" />
                          ${funAmount} ${activity.currencyCode}
                          </td>
                        </tr>
                      </c:forEach>
                      <c:if test="${!empty aimNationalPlaningDashboardForm.fundingSum}">
                        <c:if test="${aimNationalPlaningDashboardForm.fundingSum!='$0'}">
                          <tr>
                            <td colspan="10">
                              <div align="right">
                              Sum ${aimNationalPlaningDashboardForm.fundingSum} USD
                              </div>
                            </td>
                          </tr>
                        </c:if>
                      </c:if>
                    </table>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>
