<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>

<style type="text/css">
	#infoPanel .hd {
 		white-space: normal;
	}
</style>

<script type="text/javascript">
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
			$(".dashboard_tab_opt").css("visibility", "visible");
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
		hideGridTable();
		setCurProgData(programId,nodeId);
		//setTreeIndicators(nodeId);
		getNewGraph();
		getIndicators();
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
		ptree.treetop.collapseAll();
		


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
                                prgName=prgName.replace(/</g, "&lt;").replace(/>/g, "&gt;");
				var prgURL="javascript:browseProgram('"+prgID+"')";
				var prgParent=target;
				//create tree view node object
                var progHTML="<div><a href=\""+prgURL+"\" id=\"prog_"+prgID+"\" class=\"ygtvlabel\">"+prgName+"</a></div><div id=\"indTreeList"+prgID+"\" style=\"display:none\">"+getIndicatorsHTML(prg)+"</div>"
                var thisNode=treeObj.addHTMLItem(progHTML,prgParent,prgURL);
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
		 var translation='<digi:trn key="aim:noindicator" jsFriendly="true">No Indicators</digi:trn>';		
		var result='<span  class="ygtvlabel">'+translation+'</span>';
		if (indics.length>0){
			result='<table border="0" cellpadding="0" cellspacing="0">';
			for(var i=0;i<indics.length;i++){
				result+= '<tr><td class="ygtvlabel">';
				//result+= '<input type="checkbox" name="selectedIndicators" onchange="return doFilter()" value="'+indIdArr[i]+'" ';
				//if(isSelectedIndicator(indIdArr[i])){
				//	result+='checked ';
				//}
				//result+=' >';
				var name = indics[i].getAttribute('name');
				result+='&gt;&nbsp;'+name.replace(/</g, "&lt;").replace(/>/g, "&gt;")+'</td>';//<td>&nbsp;&nbsp;</td>';
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
	/* callback function which is registered for asynchronouse responce.
	this function should have special signature to be used as callback in the asynchronous class
*/
function treeCallBack(status, statusText, responseText, responseXML){
	updateTree(responseXML);
}

/* function to be called at page load to asynchronously call server for tree data.
	Later we may convert all page to asynchronous style to not refresh page at all.
 */
function initTree(){
	var treeList=document.getElementById('tree');
	treeList.innerHTML="<i><digi:trn>Loading...</digi:trn></i>"

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
     $("a[id^='prog_']").each(function(index){
						YAHOO.util.Event.addListener(jQuery(this), "mouseover", eventFunction);
						YAHOO.util.Event.addListener(jQuery(this), "mouseout", hidePanel);
   });
}

/* Function that is executed when mouse over an element */
function eventFunction(e) {
    var x = 0;
    var y = 0;
    if (e.pageX || e.pageY) 	{
        x = e.pageX;
        y = e.pageY;
    }
    else if (e.clientX || e.clientY) 	{
        x = e.clientX + document.body.scrollLeft;
        y = e.clientY + document.body.scrollTop;
    }

	showPanel(this.innerHTML, getIdFromHref(this.href), x,  y);
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
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:ProgramName'>Program Name</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+  programName.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:Description'>Description</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ description.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:LeadAgency'>Lead Agency</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ leadAgency.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:ProgramCode'>Program Code</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ programCode.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:ProgramType'>Program Type</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ programType.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:TargetGroups'>Target Groups</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ targetGroups.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:Background'>Background</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ background.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:Objectives'>Objectives</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ objectives.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:Outputs'>Outputs</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ outputs.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:Beneficiaries'>Beneficiaries</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ beneficiaries +"</td></tr>";
			panelBody += "<tr><td align='left' class='l_sm'><b><digi:trn key='aim:NPD:Environment'>Environment Considerations</digi:trn>:</b>&nbsp;</td><td class='l_sm'>"+ environmentConsiderations.replace(/</g, "&lt;").replace(/>/g, "&gt;") +"</td></tr>";
			panelBody += '</table>';

			themeArray[pid]	= panelBody;

	}
	</script>
