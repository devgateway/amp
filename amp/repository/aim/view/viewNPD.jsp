<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>


<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/tree.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/yahoo.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/event.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/treeview.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/jktreeview.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/yahoo-dom-event.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/container-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/connection-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/dragdrop-min.js"/>" ></script>

<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/amptabs.css"/>"/>

<c:set var="showCurrSettings">
	<digi:trn key="rep:showCurrSettings">Show current settings</digi:trn>
</c:set>
<c:set var="hideCurrSettings">
	<digi:trn key="rep:hideCurrSettings">Hide current settings</digi:trn>
</c:set>

<script language="JavaScript">
var filter; // Filter panel

/*
 *    method to expand or colapse
 *    filter settings
 */
function toggleSettings(){
	var currentDisplaySettings = document.getElementById('currentDisplaySettings');
	var displaySettingsButton = document.getElementById('displaySettingsButton');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = "${showCurrSettings} &gt;&gt;";
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "${hideCurrSettings} &lt;&lt;";
	}
}

// show filter window
function initFilterPanel(){
    var filterDiv = document.getElementById('filter');
    filterDiv.style.display="block";
    filterDiv.stytelvisibility="visible";
    filter=new YAHOO.widget.Panel("filter",{
            width:"400px",
            fixedcenter: true,
            constraintoviewport: true,
            Underlay:"shadow",
            modal: true,
            close:true,
            visible:false,
            draggable:true} );
    filter.render();
}

function showFilter(){
  filter.show();
}


</script>

<style type="text/css">
	a { text-decoration: underline; color: #46546C; }
	a:hover { text-decoration: underline; color: #4d77c3; }
	#tree {width:250px;padding: 10px;float:left;}
	.treeIndShow{
		display : block;
		height : 1%;
		vertical-align: top;
	}
	.treeIndHide{
		display : none;
		height : 1%;
		vertical-align: top;
	}
	SELECT {
		font-size:8pt;
	}
</style>
<digi:instance property="aimNPDForm" />
<digi:form action="/nationalPlaningDashboard.do">

<c:set var="noProgSelected">
	<digi:trn key="aim:npd:noProgSelected">
			Please select a program before selecting a filter !
	</digi:trn>
</c:set>
<html:hidden property="defaultProgram" styleId="defaultProgram"/>
<script language="javascript" type="text/javascript"><!--

	var ptree;
	var curProgId;
	var curProgNodeIndex;
	var curNodeId;
	var curProgramName;
	var curGraphURL = null;
	var lastTreeUpdate=0;
	var line=new Array();
	var lineIter=0;
	var openNodes=new Array();
	var treeXML=null;
	var activityXML=null;
	var p1d='?';
	var pd='&';
	var curIndicatorIDs=[];
	var curIndicatorNames=[];
	var selIndicatorIDs=[];
	var selYear =[];
	var selActStatus = null;
	var selActDonors = null;
	var selActYearTo = null;
	var selActYearFrom = null;
	var progIdHistory = [];
	var progNodeHistory = [];
    var pr;
    var lastTimeStamp;
	var strNoActivities="<digi:trn key='aim:NPD:noActivitisLabel'>No Activities</digi:trn>";
	var strTotal="<digi:trn key='aim:NPD:totalLabels'>Totals:</digi:trn>";
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
	var strThousands="<digi:trn key='aim:NPD:amountThousandsOfDollarsLabel'>All amounts are in thousands (000) of</digi:trn> ${aimNPDForm.defCurrency}";
</gs:test>
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
	var strThousands="${aimNPDForm.defCurrency}";
</gs:test>
	var strPlanned="<digi:trn key='aim:NPD:sumplanedCommitments'>Planned Commitments</digi:trn>";
	var strActual="<digi:trn key='aim:NPD:sumactualCommitments'>Actual Commitments</digi:trn>";
    var strActualDisb="<digi:trn>Actual Disbursements</digi:trn>";
	var strProposed="<digi:trn key='aim:NPD:sumproposedPrjCost'>Proposed Project Cost</digi:trn>";
	var actCurrPage=1;
	var actMaxPages=0;
	var pgNext='<digi:trn key="aim:npd:pagination:next">Next</digi:trn>';
	var pgPrev='<digi:trn key="aim:npd:pagination:prev">Prev</digi:trn>';
	var pgLast='<digi:trn key="aim:npd:pagination:last">Last</digi:trn>';
	var pgFirst='<digi:trn key="aim:npd:pagination:first">First</digi:trn>';
	var pgPagesLabel='<digi:trn key="aim:npd:pagination:pageslabel">Pages:</digi:trn>';
	var status='<digi:trn key="aim:npd:status">Status</digi:trn>';
	var title='<digi:trn key="aim:npd:titl">Title</digi:trn>';
	var strDate='<digi:trn key="aim:npd:strdate">Start Date</digi:trn>';
	var donor='<digi:trn key="aim:npd:donor">Donor</digi:trn>';


	function changeOptions(indics,years,locations){
		
        selIndicatorIDs=new Array();
        for (var i = 0; i < indics.length; i++) {
          selIndicatorIDs[i]=indics[i];
        }

		selYear=new Array();
        for (var i = 0; i < years.length; i++) {
          selYear[i]=years[i];
        }

		getNewGraph();
	}

    function openOptionsWindow(){
      if(curProgId==null){
      	var msg='<digi:trn key="aim:plsSelectProgram">please first select program in the tree</digi:trn>';
        alert(msg);
      }else{
        var url=addActionToURL('npdOptions.do');
        url+= p1d+'programId='+curProgId;

        if (selIndicatorIDs != null && selIndicatorIDs.length > 0 ){
          for (var i=0; i<selIndicatorIDs.length; i++){
            url+= pd + 'selIndicators='+ selIndicatorIDs[i];
          }
        }

        if(selYear!=null){
          for (var y=0; y<selYear.length; y++){
            url += pd + 'selYears=' + selYear[y];
          }
        }
        var win=openURLinResizableWindow(url,600,400);
      }
    }

	function getInidcatorsParam(){
		var params = p1d + 'programId='+curProgId;
		if(selYear!=null){
			for (var y=0; y<selYear.length; y++){
				params += pd + 'selYears=' + selYear[y];
			}
		}
		
		if (selIndicatorIDs != null) {
			for (var y=0; y<selIndicatorIDs.length; y++){
				params += pd + 'selIndicators=' + selIndicatorIDs[y];
			}
		}
		
		return params;
	}

	function openGridWindow(showGraph){
		if (curProgId !=null){
			var url=addActionToURL('npdGrid.do');
			var params = getInidcatorsParam();
			if (showGraph == true ){
				params += pd + 'mode=1';
			}
			url += params;
			var win = openURLinResizableWindow(url,600,600);
		}
	}

	function getNewGraph(){
		//var now = new Date().getTime();
		lastTimeStamp = new Date().getTime();
		var url=constructGraphUrl(lastTimeStamp);
		var graphTag=document.getElementById('graphImage');
		graphTag.src=url;
	}

	function constructGraphUrl(timestmp){
		var url=addActionToURL('npdGraph.do');
		url+=p1d+'actionMethod=displayChart';
		url+=pd+'timestamp='+timestmp;
		if (curProgId != null){
			url += pd + 'currentProgramId=' + curProgId;
			if ( (selIndicatorIDs != null) && (selIndicatorIDs.length > 0)){
				for (var i=0; i<selIndicatorIDs.length; i++){
					url += pd + 'selectedIndicators=' + selIndicatorIDs[i];
				}
			}
			if ( selYear != null && selYear.length>0) {
				for (var y=0; y<selYear.length; y++) {
					url += pd + 'selectedYears=' + selYear[y];
				}
			}
		}
		curGraphURL = url;
		return url;
	}

	function mapCallBack(status, statusText, responseText, responseXML){
		updateMap(responseText);
	}

	function updateMap(resp){
		var mapHolder= document.getElementById('graphMapPlace');
		var map= document.getElementById('npdChartMap');
		var image = document.getElementById('graphImage');
		image.removeAttribute('usemap');
		mapHolder.innerHTML=resp;
		image.setAttribute('usemap','#npdChartMap');
	}

	function constructMapUrl(timestmp){
		var url=addActionToURL('getNpdGraphMap.do');
		url+=p1d+'timestamp='+timestmp;
		return url;
	}

	function getGraphMap(timestamp){
		var url=constructMapUrl(timestamp);

		var async=new Asynchronous();
		async.complete=mapCallBack;
		async.call(url);
	}

	function graphLoaded(){
			getGraphMap(lastTimeStamp);
			setGraphVisibility(true);
	}

	function setGraphVisibility(show){
		var loadingDiv=document.getElementById('divGraphLoading');
		var graphDiv=document.getElementById('divGraphImage');
		if(show){
			loadingDiv.style.display='none';
			graphDiv.style.display='block';
		}else{
			loadingDiv.style.display='block';
			graphDiv.style.display='none';
		}
	}

	function setCurProgData(progId,nodeId){
		if (curProgId == null || curProgId != progId ){
			//var newNode=YAHOO.widget.TreeView.getTree('tree').collapseAll();
			progIdHistory[progIdHistory.length]=curProgId;
			progNodeHistory[progNodeHistory.length]=curNodeId;
			setTreeIndicatorsVisibility(curProgId,false);

			var prog=findProgWithID(treeXML,progId);
			var gHeader=document.getElementById('graphHeader');
			var aListHeader = document.getElementById('actListProgname');
			gHeader.innerHTML=prog.getAttribute('name');
			aListHeader.innerHTML=prog.getAttribute('name');
			curIndicatorIDs=[];
			curIndicatorNames=[];
			selIndicatorIDs=[];
			setAllIndicatorsOf(prog,true);
			setTreeIndicatorsVisibility(progId,true);
		}
		curProgId=progId;
		curNodeId=nodeId;
		//var newNode=YAHOO.widget.TreeView.getNode('tree',nodeId);
		//newNode.expand();
	}

	function setAllIndicatorsOf(prog, recursive){
		var allIndics=null;
		var subNodes=prog.childNodes;
		if (subNodes != null){
			var children=null;
			//check all subnodes, they may be children or indicators
			for (var i=0;i<subNodes.length;i++){
				//if indicators, then add all to the indicators array
				if (subNodes[i].tagName == 'indicators'){
					var myIndicators=subNodes[i].childNodes;
					for (var ind=0; ind<myIndicators.length; ind++){
						if (myIndicators[ind].tagName=='indicator'){
							curIndicatorIDs[curIndicatorIDs.length]=myIndicators[ind].getAttribute('id');
							curIndicatorNames[curIndicatorNames.length]=myIndicators[ind].getAttribute('name');
							//this is temporary
							selIndicatorIDs[selIndicatorIDs.length]=myIndicators[ind].getAttribute('id');
						}
					}

				}
				if (subNodes[i].tagName == 'children'){
					//just store children progs for later use
					children = subNodes[i].childNodes;
				}
			}
			//if recursive then also run on all children progs
			if (recursive && children != null) {
				for(var ch=0;ch<children.length;ch++){
					if (children[ch].tagName=='program'){
						setAllIndicatorsOf(children[ch],recursive);
					}
				}
			}
		}
	}

	function getAllIndicatorsOf(prog, recursive){
		var result=[];
		var subNodes=prog.childNodes;
		if (subNodes != null){
			var children=null;
			//check all subnodes, they may be children or indicators
			for (var i=0;i<subNodes.length;i++){
				//if indicators, then add all to the indicators array
				if (subNodes[i].tagName == 'indicators'){
					var myIndicators=subNodes[i].childNodes;
					for (var ind=0; ind<myIndicators.length; ind++){
						if (myIndicators[ind].tagName=='indicator'){
							result[result.length]=myIndicators[ind];
						}
					}

				}
				if (subNodes[i].tagName == 'children'){
					//just store children progs for later use
					children = subNodes[i].childNodes;
				}
			}
			//if recursive then also run on all children progs
			if (recursive && children != null) {
				for(var ch=0;ch<children.length;ch++){
					if (children[ch].tagName=='program'){
						setAllIndicatorsOf(children[ch],recursive);
					}
				}
			}
		}
		return result;
	}

	/* ========  Tree view methods START ======== */

	/* Node label click */
	function browseProgram(programId, nodeId) {
		setGraphVisibility(false);
		setCurProgData(programId,nodeId);
		//setTreeIndicators(nodeId);
		getNewGraph();
		getActivities();
		//setGraphVisibility(true);
		//return false;
	}

	function setTreeIndicatorsVisibility(progId,visible){
		var node=document.getElementById('indTreeList'+progId);
		if (node !=null){
			if (visible){
				node.style.display='block';
				//node.className='treeIndShow';
			}else{
				node.style.display='none';
				//node.className='treeIndHide';
				//var par1 = node.parentNode.parentNode.parentNode.parentNode.parentNode;
				//redrowOld(par1);
			}
		}
	}

	function redrowOld(elem){
		var ch = (elem!=null)?elem.childNodes : null;
		if (ch !=null){
			for(var i=0;i<ch.length;i++){
				ch[i].style.display='none';
			}
			for(var i=0;i<ch.length;i++){
				if (ch[i]!=elem){
					ch[i].style.display='block';
				}
			}
		}
	}

	function getFirstProg(progTree){
		var allProgs=progTree.getElementsByTagName("program");
        return allProgs[0];
	}

	/* finds programs with specified ID */
	function findProgWithID(progTree, toFindID){
		if (toFindID==null) return null;
		var allProgs=progTree.getElementsByTagName("program");
		for(var i=0;i<allProgs.length;i++){
			var id=allProgs[i].getAttribute("id");
			if(id==toFindID) return allProgs[i];
		}
        return null;
	}

	function getNodeIndexByProgID(progTree, progId){
		if (progId==null) return null;
		var allProgs=progTree.getElementsByTagName("program");
		for(var i=0;i<allProgs.length;i++){
			var id=allProgs[i].getAttribute("id");
			if(id==progId) return i;
		}
        return null;
	}

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


	/*determines if node should be openned.
	Nod should be opened if it is parent of the current node.
	This function uses array that was filled with fillLine() functioin */
	function toBeOpened(progID){
		for(var i=0;i<line.length;i++){
			if (line[i]==progID) return true;
		}
//		return false;
	}

	function treeNodeLabelWasClicked(node){
		var pId=node.programId;
		return browseProgram(pid);
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

		<c:set var="translation">
			<digi:trn key="aim:noProgramsPresent">No Programs present</digi:trn>
		</c:set> 
		if (programList==null || programList.length==0){
			targetDiv.innerHTML="<i>${translation}</i>";
			return;  
		}

		//store XML of tree;
		treeXML=progListRoot;

		// this value now is uddated not from form but by click on the tree node
        var defProgId=document.getElementById("defaultProgram").value;

		var curProg=findProgWithID(progListRoot,defProgId);
        if(curProg==null){
          curProg=getFirstProg(progListRoot);
          defProgId=curProg.getAttribute("id");
        }
        curNodeId=getNodeIndexByProgID(progListRoot,defProgId);
		//determine which programs should be openned
		fillLine(progListRoot,curProg);

		//create TreeView Object for specified with ID HTML object.
		ptree=new jktreeview("tree");

		//setup click event
		ptree.labelCkick=treeNodeLabelWasClicked;

		//build nodes
		buildTree(programList,ptree,"");

		//draw tree
		ptree.treetop.draw();

		openNodeIter=0;

		//open nodes
		goToCurrentNode();

		//highlight current Node
		//highlightNode(curProgNodeIndex);

		setNumOfPrograms(myXML);
		addRootListener();
		addEventListeners();

		createPanel("Info","<i>info</i>");

        browseProgram(defProgId,curNodeId);
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
				var prgName=prg.getAttribute("name");
				var prgURL="../../aim/nationalPlaningDashboard.do";//URL is not used currently //"javascript:browseProgram('"+prgID+"')";
				var prgParent=target;
				//create tree view node object
				var thisNode=treeObj.addItem(prgName,prgParent,prgURL);
				//set new field, this is used in node HTML geeration
				thisNode.programId=prgID;
				//save node if it is parent of current or current program
				if(toBeOpened(prgID)==true){
					openNodes[openNodes.length]=thisNode;
				}
				//save index in tree for current program
				if(prgID==curProgId){
					curProgNodeIndex=thisNode.index;
				}
				thisNode.indicators=getIndicatorsHTML(prg);
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

	function getIndicatorsHTML(prog){
		var indics=getAllIndicatorsOf(prog,false);
		<c:set var="translation">
			<digi:trn key="aim:noindicator">No Indicators</digi:trn>
		</c:set>		
		var result="${translation}";
		if (indics.length>0){
			result='<table border="0" cellpadding="0" cellspacing="0">';
			for(var i=0;i<indics.length;i++){
				result+= '<tr><td>';
				//result+= '<input type="checkbox" name="selectedIndicators" onchange="return doFilter()" value="'+indIdArr[i]+'" ';
				//if(isSelectedIndicator(indIdArr[i])){
				//	result+='checked ';
				//}
				//result+=' >';
				var name = indics[i].getAttribute('name');
				result+='&gt;&nbsp;'+name+'</td>';//<td>&nbsp;&nbsp;</td>';
				result+='</tr>';
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

	/* callback function which is registered for asynchronouse responce.
		this function should have special signature to be used as callback in the asynchronous class
	*/
	function treeCallBack(status, statusText, responseText, responseXML){
		updateTree(responseXML);
	}

	/* ========  Tree view methods END ========= */
    
    
    /* ========  Activities list filters Settings methods START ========= */

    function getFilterSettings(){
		var url=addActionToURL('getNPDFilters.do');
                url+=getURL()+'&timestamp=' +new Date().getTime();
		var async=new Asynchronous();
		async.complete=filterSettingsCallBack;
		async.call(url);
	}

    function getURL(){
        var url='';
         if (curProgId != null ){
			url+=p1d+'programId='+curProgId;
		}
        if (selActStatus != null && selActStatus != '0'&& selActStatus != ''){
			url += pd + 'statusId='+ selActStatus;
		}
		if(selActDonors !=null && selActDonors.match('-1') == null){
			url+= pd+ 'donorIds='+selActDonors;
		}
		if (selActYearTo != null && selActYearTo != -1){
			url+= pd + 'endYear='+selActYearTo;
		}
		if (selActYearFrom != null && selActYearFrom != -1){
			url+= pd + 'startYear='+selActYearFrom;
		}
        return url;
    }

     function filterSettingsCallBack(status, statusText, responseText, responseXML){
       var tblBody= document.getElementById('filterSettingsTable');
       var root=responseXML.getElementsByTagName('Settings')[0];
       while (tblBody.firstChild){
            tblBody.removeChild(tblBody.firstChild);
        }
        var newTRTitle=document.createElement('TR');
        var newTDTitle=document.createElement('TD');
        newTDTitle.innerHTML= '<strong><digi:trn>Selected Filters:</digi:trn></strong>';
        newTRTitle.appendChild(newTDTitle);
        tblBody.appendChild(newTRTitle);

        var newTRStatus=document.createElement('TR');
        var newTDStatus=document.createElement('TD');
        newTDStatus.innerHTML='<b><digi:trn>Status</digi:trn></b>:'+' '+root.getAttribute('status');
        newTRStatus.appendChild(newTDStatus);
        tblBody.appendChild(newTRStatus);

        var newTRDonor=document.createElement('TR');
        var newTDDonor=document.createElement('TD');
        newTDDonor.innerHTML='<b><digi:trn>Donor</digi:trn></b>:'+' '+root.getAttribute('donor');
        newTRDonor.appendChild(newTDDonor);
        tblBody.appendChild(newTRDonor);

        var newTRFrom=document.createElement('TR');
        var newTDFrom=document.createElement('TD');
        newTDFrom.innerHTML='<b><digi:trn>From</digi:trn></b>:'+' '+root.getAttribute('startYear');
        newTRFrom.appendChild(newTDFrom);
        tblBody.appendChild(newTRFrom);
        
        var newTREndYear=document.createElement('TR');
        var newTDEndYear=document.createElement('TD');
        newTDEndYear.innerHTML='<b><digi:trn>To</digi:trn></b>:'+' '+root.getAttribute('endYear');
        newTREndYear.appendChild(newTDEndYear);
        tblBody.appendChild(newTREndYear);

	}


 /* ========  Activities list filter Settings methods END ========= */

	/* ========  Activities list methods START ======== */

	function getActivities(){
		if (curProgId == null) {
			alert('${noProgSelected}');
			return;
		}
		var actList=document.getElementById('activityResultsPlace');
		//actList.innerHTML="<i>Loading...</i>"
		setActivityLoading(actList);
		var url=getActivitiesURL();
		var async=new Asynchronous();
		async.complete=activitiesCallBack;
		async.call(url);
	}

	function getActivitiesURL(){
		var result = addActionToURL('getActivities.do');
		result+=getURL();
		result+= pd + 'currentPage='+actCurrPage+'&timestamp=' +new Date().getTime();  ;
		return result;
	}

	function activitiesCallBack(status, statusText, responseText, responseXML){
		activityXML=responseXML;
		setUpActivityList(responseXML);
	}

	function setUpActivityList(xml){
		var tr= document.getElementById('activityResultsPlace');
		var paginationTr=document.getElementById('paginationPlace');
		var tbl= tr.parentNode;

		clearActivityTable(tr);
		var root=xml.getElementsByTagName('activityList')[0];
		if(root==null){
			root=xml.getElementsByTagName('error')[0];
			if (root!=null){
				showError(root,tbl);
			}else{
				var newTR=document.createElement('TR');
				newTR.innerHTML='<td colspan="8">Unknown Error</td>';
				tbl.appendChild(newTR);
			}
			return;
		}
		//total pages
		actMaxPages=root.getAttribute('totalPages');

		//get activities array
		var actList = root.childNodes;
		if (actList == null || actList.length == 0){
			var newTR=document.createElement('TR');
            var newTD=document.createElement('TD');
            newTD.innerHTML=strNoActivities;
            newTD.setAttribute("colSpan","8");
            newTR.appendChild(newTD);
			tr.parentNode.appendChild(newTR);
            var spn=document.getElementById("spnAmountText");
            if(spn!=null){
              spn.innerHTML="";
            }
			return;
		}



		//sum labels
		var labelsTR1 = document.createElement('TR');
		var titleLabelTD=document.createElement('TD');
		titleLabelTD.innerHTML='<b>'+title+' </b>';
		labelsTR1.appendChild(titleLabelTD);

		var statusLabelTD=document.createElement('TD');
		statusLabelTD.innerHTML='<b>'+status+' </b>';
		labelsTR1.appendChild(statusLabelTD);

		var donorLabelTD=document.createElement('TD');
		donorLabelTD.innerHTML='<b>'+donor+' </b>';
		labelsTR1.appendChild(donorLabelTD);

		var strDateLabelTD=document.createElement('TD');
		strDateLabelTD.innerHTML='<b>'+strDate+' </b>';
		labelsTR1.appendChild(strDateLabelTD);

		var labelTD1 = document.createElement('TD');
		labelTD1.innerHTML='<feature:display name="Proposed Project Cost" module="Funding"><b>'+strProposed+' </b></feature:display> ';
		labelsTR1.appendChild(labelTD1);

		
		var labelTD3 = document.createElement('TD');
		labelTD3.innerHTML='<b>'+strActual+' </b>';
		labelsTR1.appendChild(labelTD3);

        var labelTD2 = document.createElement('TD');
		labelTD2.innerHTML='<b>'+strActualDisb+' </b>';
		labelsTR1.appendChild(labelTD2);

        labelsTR1.bgColor='Silver';
		tbl.appendChild(labelsTR1);
		//end of sum labels


		for (var i=0; i< actList.length; i++) {
			if (actList[i].tagName=='activity'){
				var actTR = document.createElement('TR');
                if(i%2==1){
                    actTR.bgColor='#CCCCCC';
                }
                
				//name
				var actTDname = document.createElement('TD');
				var actAname = document.createElement('a');
				actAname.innerHTML=actList[i].getAttribute('name');
				var actURL = addActionToURL('aim/showPrinterFriendlyPage.do~edit=true~activityid=');
				actURL+=actList[i].getAttribute('id');
				actAname.href=actURL;
				actAname.target='_blank';
				actTDname.appendChild(actAname);
				actTR.appendChild(actTDname);
				//status
				var actTDstatus = document.createElement('TD');
				actTDstatus.innerHTML=actList[i].getAttribute('status');
				actTR.appendChild(actTDstatus);
				//donor
				var actTDdonor = document.createElement('TD');
				getDonorsHTML(actList[i].childNodes,actTDdonor);
				actTR.appendChild(actTDdonor);
				//sart year
				var actTDfromYear = document.createElement('TD');
				actTDfromYear.innerHTML=actList[i].getAttribute('date');
				actTR.appendChild(actTDfromYear);
                                
                           
                            
				//amount
				var actTDproposedAmount = document.createElement('TD');
				actTDproposedAmount.innerHTML = '<feature:display name="Proposed Project Cost" module="Funding">'+actList[i].getAttribute('proposedAmount')+'</feature:display>';
				if(actTDproposedAmount.innerHTML == "N/A"){
				   actTDproposedAmount.innerHTML = "--"
				}
				actTR.appendChild(actTDproposedAmount);

				

				var actTDActualAmount = document.createElement('TD');
				actTDActualAmount.innerHTML = actList[i].getAttribute('actualAmount');
				if(actTDActualAmount.innerHTML == "N/A"){
				   actTDActualAmount.innerHTML = "--"
				}
				actTR.appendChild(actTDActualAmount);

                var actualDisbAmountTD = document.createElement('TD');
				actualDisbAmountTD.innerHTML = actList[i].getAttribute('actualDisbAmount');
				if(actualDisbAmountTD.innerHTML == "N/A"){
				   actualDisbAmountTD.innerHTML = "--"
				}
				actTR.appendChild(actualDisbAmountTD);

				//row to table
				tbl.appendChild(actTR);
			}
		}//for


		//show Sum

		if (strTotal==null || strTotal==''){
			//if no translations
			strTotal='Total:';
		}
		//totals tr
		var lastTR = document.createElement('TR');

		var lastTD = document.createElement('TD');
		lastTD.colSpan=4;
		lastTD.align='right';
		lastTD.innerHTML='<strong>'+strTotal+' </strong>';
		lastTR.appendChild(lastTD);

		var propSumTD = document.createElement('TD');
		propSumTD.innerHTML= '<feature:display name="Proposed Project Cost" module="Funding">'+root.getAttribute('proposedSum')+'</feature:display>';
		lastTR.appendChild(propSumTD);

		

		var actSumTD = document.createElement('TD');
		actSumTD.innerHTML=root.getAttribute('actualSum');
		lastTR.appendChild(actSumTD);

        var actDisbSumtTD = document.createElement('TD');
		actDisbSumtTD.innerHTML=root.getAttribute('actualDisbSum');
		lastTR.appendChild(actDisbSumtTD);

		tbl.appendChild(lastTR);



		//tousands label
		if (strThousands==null || strThousands==''){
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
			strThousands='All amounts are in thousands (000)';
</gs:test>
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="false" onTrueEvalBody="true">
			strThousands='';
</gs:test>
		}

        var spn=document.getElementById("spnAmountText");
        if(spn!=null){
          spn.innerHTML=strThousands;
        }

        //var lastTR1 = document.createElement('TR');
		//var lastTD1 = document.createElement('TD');
		//lastTD1.colSpan=8;
		//lastTD1.align='right';
		//lastTD1.innerHTML='<font color="blue">'+strThousands+' </font>';
		//lastTR1.appendChild(lastTD1);
		//tbl.appendChild(lastTR1);

		setupPagination(paginationTr);

	}

	function setupPagination(placeToAdd){
		//alert(actMaxPages);
		if (actMaxPages<=1) return;
		//TODO clear tr first
		while(placeToAdd.firstChild != null){
			placeToAdd.removeChild(placeToAdd.firstChild);
		}


		var tooManyPages=actMaxPages>=11;
		var middleStart=-1;
		var middleEnd=actMaxPages;
		if (tooManyPages==true){
			middleStart=actCurrPage-4;
			if (middleStart<1){
				middleStart=1;
			}
			middleEnd=actCurrPage+4;
			if(middleEnd>actMaxPages){
				middleEnd=actMaxPages;
			}
		}

		var tdLabel = document.createElement('TD');
		tdLabel.innerHTML=pgPagesLabel;
		placeToAdd.appendChild(tdLabel);

		if (actCurrPage>1){
			var tdFirst = document.createElement('TD');
			tdFirst.innerHTML='<a href="javascript:gotoActListPage(1)">'+pgFirst+'</a>';
			placeToAdd.appendChild(tdFirst);

			var tdSp1 = document.createElement('TD');
			tdSp1.innerHTML='&nbsp;';
			placeToAdd.appendChild(tdSp1);

			var tdPrev = document.createElement('TD');
			tdPrev.innerHTML='<a href="javascript:gotoActListPage('+(actCurrPage-1)+')">'+pgPrev+'</a>';
			placeToAdd.appendChild(tdPrev);

			var tdSp2 = document.createElement('TD');
			tdSp2.innerHTML='&nbsp;';
			placeToAdd.appendChild(tdSp2);
		}

		for (var i=1; i <= actMaxPages; i++){
			if (tooManyPages){
				if (middleStart==i && middleStart>1){
					var tddots1 = document.createElement('TD');
					tddots1.innerHTML=' ... ';
					placeToAdd.appendChild(tddots1);
					var tdsp = document.createElement('TD');
					tdsp.innerHTML='&nbsp;';
					placeToAdd.appendChild(tdsp);
				}
				if (i>=middleStart && i<=middleEnd){
					//pages
					var td = document.createElement('TD');
					if (i==actCurrPage){
						td.innerHTML='<strong>'+i+'</strong>';
					}else{
						td.innerHTML='<a href="javascript:gotoActListPage('+i+')">'+i+'</a>';
					}
					placeToAdd.appendChild(td);
					//space
					var tdsp = document.createElement('TD');
					tdsp.innerHTML='&nbsp;';
					placeToAdd.appendChild(tdsp);

				}
				if (middleEnd==i && middleEnd<actMaxPages){
					var tddots2 = document.createElement('TD');
					tddots2.innerHTML=' ... ';
					placeToAdd.appendChild(tddots2);
					var tdsp = document.createElement('TD');
					tdsp.innerHTML='&nbsp;';
					placeToAdd.appendChild(tdsp);
				}
			}else{
				var td = document.createElement('TD');
				if (i==actCurrPage){
					td.innerHTML='<strong>'+i+'</strong>';
				}else{
					td.innerHTML='<a href="javascript:gotoActListPage('+i+')">'+i+'</a>';
				}
				placeToAdd.appendChild(td);
				var tdsp = document.createElement('TD');
				tdsp.innerHTML='&nbsp;';
				placeToAdd.appendChild(tdsp);
			}
		}//for each age ends

		if (actCurrPage<actMaxPages){
			var tdNext = document.createElement('TD');
			tdNext.innerHTML='<a href="javascript:gotoActListPage('+(actCurrPage+1)+')">'+pgNext+'</a>';
			placeToAdd.appendChild(tdNext);

			var tdSp3 = document.createElement('TD');
			tdSp3.innerHTML='&nbsp;';
			placeToAdd.appendChild(tdSp3);

			var tdLast = document.createElement('TD');
			tdLast.innerHTML='<a href="javascript:gotoActListPage('+actMaxPages+')">'+pgLast+'</a>';
			placeToAdd.appendChild(tdLast);
		}

	}

	/* pagination link handler */
	function gotoActListPage(pageNum){
		actCurrPage=pageNum;
		getActivities();
		//return false;
	}

	function getDonorsHTML(donors,target){
		if (donors !=null && donors.length >0 && donors[0].tagName=='donors'){
			var donorList = donors[0].childNodes;
			if (donorList !=null && donorList.length>0){
				var donorTable = document.createElement('table');
                                var donorTableBD = document.createElement('TBODY');
				donorTable.cellSpacing=0;
				for (var i=0; i<donorList.length; i++){
					var donorTr = document.createElement('tr');
					var donorTd = document.createElement('td');
					donorTd.innerHTML = donorList[i].getAttribute('name');
					donorTr.appendChild(donorTd);
                                        donorTableBD.appendChild(donorTr);
				}
                                donorTable.appendChild(donorTableBD);
				target.appendChild(donorTable);
			}else{
				target.innerHTML = ' ';
			}
		}else{
			target.innerHTML = ' ';
		}
	}

	function showError(stackList,where){
		if (stackList !=null && stackList.childNodes !=null){
			for (var i=0; i<stackList.childNodes.length; i++){
				if (stackList.childNodes[i].tagName=='frame'){
					var tr=document.createElement('TR');
					var td=document.createElement('TD');
					td.colSpan=6;
					td.innerHTML=stackList.childNodes[i].textContent;

					tr.appendChild(td);
					where.appendChild(tr);
				}
			}
		}
	}

	function clearActivityTable(firstTR){
		var par=firstTR.parentNode;
		while(firstTR.nextSibling != null){
			par.removeChild(firstTR.nextSibling);
		}
	}

	function setActivityLoading(firstTR){
		var par=firstTR.parentNode;
		clearActivityTable(firstTR);
		var tr=document.createElement('tr');
		var str='<td align="center" colspan="6"><img src="/TEMPLATE/ampTemplate/images/amploading.gif" alt="loading..."/>loading...</td>';
		tr.innerHTML=str;
		par.appendChild(tr);
	}
        
	function filterStatus(){
		var stat = document.getElementsByName('selectedStatuses')[0];
		selActStatus = stat.value;
		getActivities();
	}

	function filterDonor(){
		var donors = document.getElementsByName('selectedDonors')[0];
		selActDonors = donors.value;
		getActivities();
	}
        function applyFilter(){
            selActStatus="";
            selActDonors="";
            var stat = document.getElementsByName('selectedStatuses')[0];
            var donors = document.getElementsByName('selectedDonors')[0];
            var from = document.getElementsByName('yearFrom')[0];
            selActYearFrom = from.value;
            var to= document.getElementsByName('yearTo')[0];
            selActYearTo = to.value;
            for (var i=0; i<stat.length; i++) {
                       if(stat.options[i].selected ){
                           if(stat.options[i].value=='0'){
                               selActStatus='0'+',';
                               break;
                           }
                           selActStatus+=stat.options[i].value+',';
                       }
             } 
              for (var j=0; j<donors.length; j++) {
               
                       if(donors.options[j].selected ){
                           selActDonors+=donors.options[j].value+',';
                       }
             } 
             if(selActStatus.length>1){
                 selActStatus=selActStatus.substring(0,selActStatus.length-1)
             }
              if(selActDonors.length>1){
                 selActDonors=selActDonors.substring(0,selActDonors.length-1)
             }
             getFilterSettings(); // Getting filter settings
             getActivities();
             
             // hide filter panel
             if(filter!=null){
                filter.hide();
            }
        }

	

	/* ========  Activities list methods END ========= */



	/* function to be called at page load to asynchronously call server for tree data.
		Later we may convert all page to asynchronous style to not refresh page at all.
	 */
	function initTree(){
		var treeList=document.getElementById('tree');
		treeList.innerHTML="<i>Loading...</i>"

		var url=addActionToURL("getThemeTreeNode.do");
		var async=new Asynchronous();
		async.complete=treeCallBack;
		async.call(url);
	}

	function setupYears(){
		var ys=document.getElementsByName('myYears'); //document.forms['aimNPDForm'].selYears;
		for (var i=0; i<ys.length; i++){
			selYear[selYear.length]=ys[i].value;
		}
	}

	function loadInitial(){
        initFilterPanel();
		setupYears();
		initTree();
	}

	/**
	*The code below is related to the DHTML panel
	*
	*/
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
        var x = 0;
        var y = 0;
        if (e.pageX && e.pageY) 	{
            x = e.pageX;
            y = e.pageY;
        }
        else if (e.clientX &&e.clientY) 	{
            x = e.clientX + document.body.scrollLeft;
            y = e.clientY + document.body.scrollTop;
        }

		showPanel(this.innerHTML, getIdFromHref(this.href), x,y);
	}
	/* Extracts the id (database id of AmpTheme) from the href property */
	function getIdFromHref( href ) {
		var start	= href.indexOf("('");
		var end		= href.indexOf("',");
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


		informationPanel	= new YAHOO.widget.Panel("infoPanel", { width:"300px", visible:false, draggable:false, close:false } );
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
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:ProgramName'>Program Name</digi:trn>:</b>&nbsp;</td><td>"+ programName +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Description'>Description</digi:trn>:</b>&nbsp;</td><td>"+ description +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:LeadAgency'>Lead Agency</digi:trn>:</b>&nbsp;</td><td>"+ leadAgency +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:ProgramCode'>Program Code</digi:trn>:</b>&nbsp;</td><td>"+ programCode +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:ProgramType'>Program Type</digi:trn>:</b>&nbsp;</td><td>"+ programType +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:TargetGroups'>Target Groups</digi:trn>:</b>&nbsp;</td><td>"+ targetGroups +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Background'>Background</digi:trn>:</b>&nbsp;</td><td>"+ background +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Objectives'>Objectives</digi:trn>:</b>&nbsp;</td><td>"+ objectives +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Outputs'>Outputs</digi:trn>:</b>&nbsp;</td><td>"+ outputs +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Beneficiaries'>Beneficiaries</digi:trn>:</b>&nbsp;</td><td>"+ beneficiaries +"</td></tr>";
 			panelBody += "<tr><td align='left'><b><digi:trn key='aim:NPD:Environment'>Environment Considerations</digi:trn>:</b>&nbsp;</td><td>"+ environmentConsiderations +"</td></tr>";
 			panelBody += '</table>';

 			themeArray[pid]	= panelBody;

 	}
	window.onload=loadInitial;
--></script>
<script language="javascript" type="text/javascript">
    <c:forEach var="theme" items="${aimNPDForm.allThemes}">
    <c:set var="name">
    ${theme.name}
    </c:set>
    <c:set var="quote">'</c:set>
    <c:set var="escapedQuote">\'</c:set>
    <c:set var="escapedName">
    ${fn:replace(message,quote,escapedQuote)}
    </c:set>

		addProgramInformation(	'${theme.ampThemeId}',
								 '${escapedName}',
								'${theme.description}',
								'${theme.leadAgency}',
								'${theme.themeCode}',
								'${theme.typeCategoryValue.value}',
								'${theme.targetGroups}',
								'${theme.background}',
								'${theme.objectives}',
								'${theme.outputs}',
								'${theme.beneficiaries}',
			 					'${theme.environmentConsiderations}'
                                );

    </c:forEach>
</script>
<input type="hidden" id="hdYears" value=""/>
<input type="hidden" id="hdIndicators" value=""/>
<c:forEach var="sys" items="${aimNPDForm.selYears}">
	<html:hidden property="myYears" value="${sys}"/>
</c:forEach>

<div style="padding-left:10px;padding-top:10px;width:96%;">
    <div id="content" class="yui-skin-sam" style="width:100%;">
        <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
            <ul class="yui-nav">
                <module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
                <li class="selected">
                    <a style="cursor:default">
                        <div>
	                        <digi:trn key="aim:nplDashboard">National Planning Dashboard</digi:trn>
                        </div>
                    </a>
                </li>
                </module:display>
                <feature:display name="Portfolio Dashboard" module="M & E">
                <li>
                    <digi:link href="/viewPortfolioDashboard.do~actId=-1~indId=-1">
                        <div>
                            <digi:trn key="aim:portfolioDashboard">Dashboard</digi:trn>
                        </div>
                    </digi:link>
                </li>
                </feature:display>
            </ul>
        </div>
	</div>
<div class="yui-content" style="background-color:#ffffff;border:1px solid black;">
<table id="topParttable" width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="2">
            <DIV id="subtabs">
                <UL>
                    <LI>
                        <div>
                            <span>
                                <a href="JavaScript:openGridWindow(false);">
                                    <digi:trn key="aim:NPD:viewTable_Link">View table</digi:trn>
                                </a>&nbsp;&nbsp;|
                            </span>
                        </div>
                    </LI>
                    <LI>
                        <div>
                            <span>
                                <a href="JavaScript:openGridWindow(true);">
                                    <digi:trn key="aim:NPD:viewAllLink">View All</digi:trn>
                                </a>
                            </span>
                        </div>

                    </LI>
                </UL>
                &nbsp;
            </DIV>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            &nbsp;
        </td>
    </tr>
				<tr>
					<td style="border-bottom:1px solid black;" valign="top">
						<table id="topLeftTable" border="0" width="100%" cellspacing="0" cellpadding="5">
							<tr bgcolor="silver">
								<td>
									<span id="graphHeader">&nbsp;</span>
								</td>
							</tr>
							<tr>
								<td valign="top">
									<div id="divGraphLoading" style="vertical-align: middle; display: none; width: ${aimNPDForm.graphWidth}px; height: ${aimNPDForm.graphHeight}px" align="center">
										<digi:img src="images/amploading.gif"/><digi:trn key="aim:NPD:loadingGraph">Loading...</digi:trn>
									</div>
									<div id="divGraphImage" style="display: block">
										<digi:context name="showChart" property="/module/moduleinstance/npdGraph.do"/>
				                        <c:url var="fullShowChartUrl" scope="page" value="${showChart}">
				                          <c:param name="actionMethod" value="displayChart" />
				                          <c:param name="currentProgramId" value="${aimNPDForm.programId}" />
				                          <c:forEach var="selVal" items="${aimNPDForm.selIndicators}">
				                            <c:param name="selectedIndicators" value="${selVal}" />
				                          </c:forEach>
				                        </c:url>
				                        <img id="graphImage" onload="graphLoaded()" alt="chart" src="${fullShowChartUrl}" width="${aimNPDForm.graphWidth}" height="${aimNPDForm.graphHeight}" usemap="#npdChartMap" border="0"/>
									</div>
								</td>
							</tr>
						</table>
					</td>
					<td valign="top" width="100%" style="border-left:1px solid black;border-bottom:1px solid black;">
						<div id="tree" style="width: 100%;"></div>
					</td>
				</tr>
                <tr>
                    <td colspan="2">
                        <table width="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td style="padding-left:-2px;">
                                    <div style="width:99.7%;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
                                        <span style="cursor:pointer;float:left;">
                                            <DIV id="subtabs">
                                                <UL>
                                                    <LI>
                                                        <div>
                                                            <span>
                                                                <a href="JavaScript:showFilter();">
                                                                    <digi:trn>Filter</digi:trn>
                                                                </a>&nbsp;&nbsp;|
                                                            </span>
                                                        </div>
                                                    </LI>
                                                    <LI>
                                                        <div>
                                                            <span>
                                                                <a href="JavaScript:openOptionsWindow();">
                                                                    <digi:trn key="aim:NPD:changeOptionsLink">Change Options</digi:trn>
                                                                </a>&nbsp;&nbsp;|
                                                            </span>
                                                        </div>
                                                    </LI>
                                                    <LI>
                                                        <div>
                                                            <span>
                                                                <digi:link href="/reportWizard.do"><digi:trn>Reports</digi:trn></digi:link>
                                                            </span>
                                                        </div>

                                                    </LI>
                                                </UL>
                                            </DIV>
                                        </span>
                                        <span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleSettings();" id="displaySettingsButton"><digi:trn>Show Current Settings</digi:trn>  &gt;&gt;</span>
                                        &nbsp;
                                    </div>
                                    <div style="display:none;background-color:#FFFFCC;padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
                                        <table cellpadding="0" cellspacing="0" border="0" width="80%" >
                                           <tbody id="filterSettingsTable">
                                            <tr>
                                                <td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
                                                    <strong>
                                                        <digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn>
                                                    </strong>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><b><digi:trn>Status</digi:trn></b>: ${aimNPDForm.selectedStatuses} </td>
                                            </tr>
                                            <tr>
                                                <td><b><digi:trn>Donors</digi:trn></b>: ${aimNPDForm.selectedDonors} </td>
                                            </tr>
                                            <tr>
                                                <td><b><digi:trn>From</digi:trn></b>: ${aimNPDForm.yearFrom} </td>
                                            </tr>
                                            <tr>
                                                <td><b><digi:trn>To</digi:trn></b>: ${aimNPDForm.yearTo} </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                        
                                </td>
                            </tr>
                        </table>
                    </td>

                </tr>
			</table>
            <table width="100%">
                <tr>
                    <td>
                        <span id="spnAmountText" style="color:blue">
                        </span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="filter" style="visibility:hidden;display:none;">
                            <table width="100%" border="0" cellpadding="5" cellspacing="0">
                                <tr>
                                    <td>
                                        <c:set var="translation">
                                            <digi:trn key="aim:npd:dropDownAnyStatus">Any Status</digi:trn>
                                        </c:set>
                                        <category:showoptions  firstLine="${translation}" name="aimNPDForm" property="selectedStatuses"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_STATUS_KEY%>"  multiselect="true" size="5" ordered="true"/>

                                    </td>
                                    <td>
                                        <html:select multiple="true" size="5" property="selectedDonors" >
                                            <option value="-1"><digi:trn key="aim:npd:dropDownAnyDonor">Any Donor</digi:trn></option>
                                            <html:optionsCollection name="aimNPDForm" property="donors" value="value" label="label" />
                                        </html:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <html:select property="yearFrom">
                                            <option value="-1"><digi:trn key="aim:npd:dropDownFromYear">From Year</digi:trn></option>
                                            <html:optionsCollection name="aimNPDForm" property="years" value="value" label="label" />
                                        </html:select>
                                    </td>
                                    <td>
                                        <html:select property="yearTo">
                                            <option value="-1"><digi:trn key="aim:npd:dropDownToYear">To Year</digi:trn></option>
                                            <html:optionsCollection name="aimNPDForm" property="years" value="value" label="label" />
                                        </html:select>
                                    </td>
                                   
                                </tr>
                                <tr>
                                     <td nowrap="nowrap" align="center" colspan="2">
                                        <input type="button" onclick="applyFilter()" value="<digi:trn key='aim:npd:applyFilter'>Apply Filter</digi:trn>"/>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="5" cellspacing="3">
                            <tr id="activityListPlace" bgcolor="silver">
                                <td width="100%" colspan="9">
                                    <digi:trn key="aim:npd:activitesFor">Activites for:</digi:trn>
                                    &nbsp;<span id="actListProgname">&nbsp</span>
                                </td>
                            </tr>
                            <tr id="activityResultsPlace">
                                <td colspan="8">
                                    &nbsp;

                                </td>
                            </tr>
                        </table>
		</td>
	</tr>
	<tr>
		<td align="right">
			<table>
				<tr id="paginationPlace">
					<td>&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<span id="graphMapPlace">
	<map name="npdChartMap" id="npdChartMap">
	</map>
</span>
</div>
</div>

</digi:form>
