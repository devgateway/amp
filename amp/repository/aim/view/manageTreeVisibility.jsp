<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script language="JavaScript">

	$(window).load(function() {
		    $("#loader_background").fadeOut('fast'); 
	});

	function changeLevel(id) {
		<digi:context name="urlVal" property="context/module/moduleinstance/visibilityManager.do" />			  
		document.aimVisibilityManagerForm.action = "<%= urlVal %>?changeLevel=true&action=edit&templateId="+id;
		document.aimVisibilityManagerForm.submit();		
	}

</script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<!-- dynamic tooltip -->
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-ajax.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-folder-tree-static.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-context-menu.js"/>"></script>

<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/folder-tree-static.css" />" />
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/context-menu.css" />" />

<script type="text/javascript">
function openFieldPermissionsPopup(fieldId) {
			<digi:context name="assignFieldPermissionsURL" property="context/module/moduleinstance/assignFieldPermissions.do?fieldId=" />
			openURLinWindow("<%=assignFieldPermissionsURL%>"+fieldId,280, 325);
}


function enterKeyIsPressed(el, e){
    if(typeof e == 'undefined'||e.keyCode == 13) {
        el.style.display = 'none';
    	var url = "/aim/saveFMDescription.do";
    	var id=el.id;
    	var params=id.split("_");// id consists of textarea word, id of AmpObjectVisibility object and objectvisibility type(module or feature or field)
    	var postString="objectVisibility="+params[2]+"&description="+el.value+"&id="+params[1];
    	var callBackFM={ 
    		      success: function (o) { 
    		    	  if(o.responseText=='success'){
    		    		  var linkId=params[2]+":"+params[1];
    	    		    	var link=document.getElementById(linkId);
    	    		    	link.title=el.value;  
    		    	  }
    		    	  else{
    		    		  alert("fail to update description!");
    		    	  }
    		      }, 
    		      failure: function (o) { 
    			        alert("Ajax request failed!"); 
    			     }
    	}
    	YAHOO.util.Connect.asyncRequest("POST", url, callBackFM, postString);
    	return false;
    }
    return true;
}
function showDescriptionToolbox(id){
	$("textarea[id^='textarea_']").css('display', 'none');
	 $('#'+id).css('display', 'block');
}
</script>

<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
<style type="text/css"> 
	#demo .yui-nav li {
		margin-right:0;
		margin-top:2pt;
 	}
	#demo .yui-content {
	min-height:200px;
	_height:200px;
	padding:10px 10px 10px 10px;
 	background-color: #FFFFFF;
}
</style>
<div id="loader_background" 
	style = 'background-color:grey; position:absolute; width:100%; height:2000px; top:81px; left:0;opacity:0.4;z-index:2; -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=40)"; filter: alpha(opacity=40);'>
	<img style="position:relative;top:300px;opacity:1;z-index:3; margin: 0 auto; display: block;" 
		src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-medium.gif">
</div>
<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>	
	<!--<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">-->
	<!-- Table title -->
	<!--<digi:trn key="aim:ampFeatureManager">
		Feature Manager 
	</digi:trn>-->
	<!-- end table title -->										
	<!--</td></tr> -->
	<tr>
		<td style="white-space:normal;">
<form name="searchBoxForm" onsubmit="searchFunction();return false;">
<div id="searchBox" style="background-color:#F2F2F2;  font-size:12px; padding:10px;">
<b><digi:trn key="fm:search:searchTitle">Search</digi:trn></b> 
<input type="Text" id="searchCriteria"/> 
<input type="submit" class="buttonx" value="<digi:trn key="fm:search:search">Search</digi:trn>" />
<input type="button" class="buttonx" onclick="resetSearch()" value="<digi:trn key="fm:search:reset">Reset</digi:trn>" />

<input type="button" id="prevSearchButton" class="buttonx" onclick="prevResult()" value="<<" disabled="true" style="display:none;"/>
<input type="button" id="nextSearchButton" class="buttonx" onclick="nextResult()" value=">>" disabled="true"  style="display:none;"/>
<span id="spanSearchMessage" style="color:red;font-weight:bold;"></span>
</div></form>

	<digi:instance property="aimVisibilityManagerForm" />
	<digi:form action="/visibilityManager.do" method="post" >

	<bean:define name="aimVisibilityManagerForm" property="ampTreeVisibility" id="template" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
	<bean:define name="template" property="items" id="modules" type="java.util.Map"  toScope="page"/>
	<bean:define name="template" property="root" id="currentTemplate" type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility" scope="page" toScope="request"/>
		
	 <c:set var="translation">
         <digi:trn key="aim:addNoLevel">No Level</digi:trn>
        </c:set>
	<p style="font-size:12px;"> &nbsp;<digi:trn key="aim:newFeatureTemplateNameBbl">Template Name:</digi:trn> <input type="text" name="templateName" size="30" class="inputx" value="<%=session.getAttribute("templateName")%>"/>
	<c:set var="templateId">
		<bean:write name="template" property="root.id"/>
	</c:set>

<script language="javascript">
<!--


var arrayLI = new Array();
var scrollArray = new Array();
var scrollTreeArray = new Array();
var currentScrollItem = 0;


function resetSearch(){
	disableSearchButtons();

	var dhtmlArray = document.getElementsByName("dhtmltreeArray");
	for(index=0; index < dhtmlArray.length;index++)
	{
		treeCollapseAll(dhtmlArray[index].id);
	}
	for(index=0; index < dhtmlArray.length;index++)
	{
		resetHighlight(dhtmlArray[index].id);
	}

	document.getElementById("searchCriteria").value = "";
	document.getElementById("spanSearchMessage").innerHTML = "";
	scrollArray = new Array();
}

function nextResult()
{
	highlightResultReset(scrollArray[currentScrollItem]);

	currentScrollItem++;
	if(currentScrollItem == scrollArray.length)
		currentScrollItem = 0;

	highlightResult(scrollArray[currentScrollItem])
	scrollArray[currentScrollItem].scrollIntoView(true);
	showTree(scrollTreeArray[currentScrollItem]);

}

function highlightResultReset(linkObject)
{
	linkObject.style.backgroundColor = '#EEEEEE';
	linkObject.style.color = '#0E69B3';
}

function highlightResult(linkObject)
{
	linkObject.style.backgroundColor = '#FF0000';
	linkObject.style.color = '#EEEEEE';
}

function prevResult()
{
	highlightResultReset(scrollArray[currentScrollItem]);

	currentScrollItem--;
	if(currentScrollItem == -1)
		currentScrollItem = scrollArray.length-1;
	scrollArray[currentScrollItem].scrollIntoView(true);
	showTree(scrollTreeArray[currentScrollItem]);
	highlightResult(scrollArray[currentScrollItem]);

}

function setBranch(objectLI, treeLI)
{
	var tempObject = objectLI;
	var tempString = "";

	while(tempObject.id != treeLI)
	{
		if(tempObject.tagName == "LI")
		{
			arrayLI[arrayLI.length] = tempObject.id;
		}
		tempObject = tempObject.parentNode;
	}
}

function resetHighlight(treeId)
{
	var treeObject = document.getElementById(treeId);
	var collectionA = treeObject.getElementsByTagName("A");
	for(var index = 0; index < collectionA.length; index++){
		if(collectionA[index].style.fontWeight == 'bold')
		{
			collectionA[index].style.fontWeight='normal';
			highlightResultReset(collectionA[index]);
		}
	}
}


function treeCollapseAll(treeId)
{
	var treeObject = document.getElementById(treeId);
	var collectionUL = treeObject.getElementsByTagName("UL");
	for(var index = 0; index < collectionUL.length; index++){
		if(collectionUL[index].style.display=='block')
		{
			collectionUL[index].style.display='none';
			var imageExpand = collectionUL[index].parentNode.getElementsByTagName("IMG");
			if(imageExpand[0].src.indexOf(minusImage)>=0){
				imageExpand[0].src = imageExpand[0].src.replace(minusImage, plusImage);
			}
		}
	}
	
}
function enableSearchButtons()
{
	if(document.getElementById("prevSearchButton").style.display == 'none' && document.getElementById("nextSearchButton").style.display == 'none' )
	{
		document.getElementById("prevSearchButton").style.display = '';
		document.getElementById("nextSearchButton").style.display = '';
		document.getElementById("prevSearchButton").disabled = false;
		document.getElementById("nextSearchButton").disabled = false;
	}
}
function disableSearchButtons()
{

	if(!document.getElementById("prevSearchButton").disabled && !document.getElementById("nextSearchButton").disabled )
	{
		document.getElementById("prevSearchButton").style.display = 'none';
		document.getElementById("nextSearchButton").style.display = 'none';
		document.getElementById("prevSearchButton").disabled = true;
		document.getElementById("nextSearchButton").disabled = true;
	}
}

function searchFunction()
{

	var searchCriteria = document.getElementById("searchCriteria").value;

	var dhtmlArray = document.getElementsByName("dhtmltreeArray");
	
	var idOfFolderTrees = new Array();
	var searchableObjectsLIObject = new Array();
	var searchableObjectsLITree = new Array();
	var contador = 0;
	for(index=0; index < dhtmlArray.length;index++)
	{
		var liArray = document.getElementById(dhtmlArray[index].id).getElementsByTagName("LI");
		for(index2 = 0; index2 < liArray.length; index2++)
		{
			searchableObjectsLIObject[contador] = liArray[index2];
			searchableObjectsLITree[contador] = dhtmlArray[index].id;
			contador++;
		}
	}
	
	isKeycaptureOn = true;
	if(searchCriteria.length < 3) {
		alert("<digi:trn key="fm:search:searchQuestion">Please, enter a search of more than 2 characters.</digi:trn>");
		return;
	}
	
	for(index=0; index < dhtmlArray.length;index++)
	{
		treeCollapseAll(dhtmlArray[index].id);
	}
	for(index=0; index < dhtmlArray.length;index++)
	{
		resetHighlight(dhtmlArray[index].id);
	}
    currentScrollItem = 0;
	scrollArray = new Array();
	scrollTreeArray = new Array();
	
	searchExpandedNodes = "";
	var countMatches = 0;
	for(a=0; a< searchableObjectsLIObject.length; a++){
		var text = searchableObjectsLIObject[a].title;
		if(text.toLowerCase().search(searchCriteria.toLowerCase()) != -1)
		{
			countMatches++;
			enableSearchButtons();

			var objectLI = searchableObjectsLIObject[a];
			var treeLI = searchableObjectsLITree[a];
			
			arrayLI = new Array();
			setBranch(objectLI, treeLI);
			searchExpandedNodes = treeLI;
			

			for(b=1;b<arrayLI.length;b++){
				searchExpandedNodes += "," + arrayLI[b];
			}
//			Set_Cookie('dhtmlgoodies_expandedNodes',initExpandedNodes,500);
			

			if(searchExpandedNodes){
				var nodes = searchExpandedNodes.split(',');
				for(var no=0;no<nodes.length;no++){
					if(nodes[no])showNodeObject(false,nodes[no], treeLI);	
				}		
			}
		
			if(objectLI.getElementsByTagName("A")[0]){
				objectLI.getElementsByTagName("A")[0].style.fontWeight = "bold";
				scrollArray[scrollArray.length] = objectLI.getElementsByTagName("A")[0];
				scrollTreeArray[scrollTreeArray.length] = treeLI;
			}

		}
	}
	document.getElementById('nextSearchButton').focus();

	if(countMatches > 0){
		scrollArray[0].style.backgroundColor = '#FF0000';
		scrollArray[0].style.color = '#EEEEEE';
		showTree(scrollTreeArray[0]);
		scrollArray[0].scrollIntoView(true);
		setSearchMessage( countMatches + " <digi:trn key="fm:search:matchesFound">items found</digi:trn>.");
	}
	else
	{
//		showNodeObject(false,"dhtmlgoodies_tree");
		setSearchMessage("<digi:trn key="fm:search:noMatches">No matches found</digi:trn>.");
	}	
	
}
function setSearchMessage(stringMessage){
	var spanSearchMessage = document.getElementById("spanSearchMessage");
	spanSearchMessage.innerHTML = stringMessage;
}
	function showNodeObject(e,inputId, treeLI)
	{
		thisNode = document.getElementById(inputId);
		if(!thisNode){
			thisNode = document.getElementById(treeLI);
		}
		var ul = thisNode.getElementsByTagName('UL')[0];
		ul.style.display='block';
		var img = thisNode.getElementsByTagName('IMG')[0];
		img.src = img.src.replace(plusImage,minusImage);
		return;
	}

	function showTree(treeLI)
	{
		idNode = treeLI.split(":")[1];
		if(idNode)
		{
			var indexTab = document.getElementById("treeId" + idNode).getAttribute("indextab")
			myTabs.set('activeIndex', indexTab);
		}
	}
	
	function sortTree(objectId, desc)
	{
		var oUl = document.getElementById("dhtmltree:" + objectId)
		sortUL(oUl, desc);
	
		for(i=0; i < oUl.getElementsByTagName("UL").length; i++)
		{
			if(oUl.getElementsByTagName("UL")[i].getElementsByTagName("LI").length > 0)
				sortUL(oUl.getElementsByTagName("UL")[i], desc);
		}
	}



  
/*
   var items = new Array();

   function sortUL(listid, desc) { 
   		//Change this bubble sort for quicksort or a better algorithm

		if(listid.nodeName != "UL") return false; 

		items = new Array();
		for(var index in listid.childNodes)
		{
			if(listid.childNodes[index].nodeName == "LI")
				items[items.length] = listid.childNodes[index];
		}

		var N = items.length; 
		var exchangeEvent = false;
		for(var j=N-1; j > 0; j--) { 
			for(var i=0; i < j; i++) { 
				if(compare(get(i+1), get(i), desc)) 
				{
					exchange(i+1, i, listid);
					exchangeEvent = true;
				}
			} 
		} 
		if(exchangeEvent)
			sortUL(listid, desc);
		return true; 
	}
	function exchange2(i, j, listObject) { 
		// exchange adjacent items 
		listObject.insertBefore(items[i], items[j]); 
	}

	*/

	function get(i) {
		var node = items[i]; 
		if(node.childNodes.length == 0) return ""; 
		var retval =node.title; 
		return retval; 
	}  
	
	function compare(val1, val2, desc) { 
		return (desc) ? val1 > val2 : val1 < val2; 
	}

	 // global variables 
	 var col = 0; 
	 var parent = null; 
	 var items = new Array(); 
	 var N = 0;
	
	function isort(m, k, desc) { 
		for(var j=m+k; j < N; j+= k) 
		{ 
			for(var i=j; i >= k && compare(get(i), get(i-k), desc); i-= k) 
			{ 
				exchange(i, i-k); 
			} 
		} 
	} 

	function sortUL(tableid, desc) { 
		exchangeEvent = false;
		parent = tableid; 
		if(parent.nodeName != "UL") return false; 
		items = new Array();
		for(var index in parent.childNodes)
		{
			if(parent.childNodes[index].nodeName == "LI")
				items[items.length] = parent.childNodes[index];
		}
		N = items.length; 

		if((k = Math.floor(N/5)) > 7) { 
			for(var m=0; m < k; m++) isort(m, k, desc); 
		} 
		if((k = Math.floor(N/7)) > 7) { 
			for(var m=0; m < k; m++) isort(m, k, desc); 
		} 
		for(k=7; k > 0; k -= 2) { 
			for(var m=0; m < k; m++) isort(m, k, desc); 
		} 
		if(exchangeEvent)
			sortUL(tableid, desc);
		
		return false;
	}
	var exchangeEvent = false;
	function exchange(i, j) { 
		exchangeEvent = true;
		if(i == j+1) { 
			parent.insertBefore(items[i], items[j]); 
		} else if(j == i+1) { 
			parent.insertBefore(items[j], items[i]); 
		} else { 
			var tmpNode = parent.replaceChild(items[i], items[j]); 
			if(typeof(items[i]) == "undefined") { 
				parent.appendChild(tmpNode); 
			} else { 
				parent.insertBefore(tmpNode, items[i]); 
			} 
		} 
	}

	//keyboard control keys
	var isCtrl = false; 
	document.onkeyup=function(e){
		e = e || window.event;
		var code = e.keyCode || e.which;		
	 	if(code==17) isCtrl=false;
	 }
	  
	document.onkeydown=function(e){
		e = e || window.event;
		var code = e.keyCode || e.which;
	 	if(code==17) isCtrl=true;
		if(code == 190 && isCtrl == true) { //CTRL+> for next result			
			if(document.getElementById('nextSearchButton').disabled==false){				
				nextResult();
			}
			return false; 
		} else if(code == 188 && isCtrl == true){ //CTRL+< for previous result
			if(document.getElementById('prevSearchButton').disabled==false){			
				prevResult();
			}			
			return false;
		}
	}  
 	 
	-->

</script>
<style>

</style>
	</p>
    <c:set scope="session" var="currentLevel">0</c:set>
    <c:set scope="session" var="firstSelected">false</c:set>
            <div id="demo" class="yui-navset" style="width:800px">
                <ul class="yui-nav">
                    <logic:iterate name="modules" id="module" type="java.util.Map.Entry" indexId="counter">
                    	
                        <bean:define id="counter" name="counter" type="Integer" scope="page" toScope="request"/>
                        <bean:define id="moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page" toScope="request"/>
                        <bean:define id="_moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility"/>
                        <bean:define id="_moduleAux2" name="_moduleAux" property="root" type="org.digijava.module.aim.dbentity.AmpModulesVisibility" scope="page"/>
                        <bean:define id="size" name="_moduleAux2" property="submodules"/>
                        <jsp:include page="generateTreeXLevelVisibilityTabs.jsp" />
                    </logic:iterate>
                </ul>
                <div class="yui-content"> 
                    <logic:iterate name="modules" id="module" type="java.util.Map.Entry" >
                        <bean:define id="moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page" toScope="request"/>
                        <bean:define id="_moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility"/>
                        <bean:define id="_moduleAux2" name="_moduleAux" property="root" type="org.digijava.module.aim.dbentity.AmpModulesVisibility" scope="page"/>
                        <bean:define id="size" name="_moduleAux2" property="submodules"/>
                        <jsp:include page="generateTreeXLevelVisibility.jsp" />
                    </logic:iterate>
                </div>
        </div>
	<c:set var="translation">
		<digi:trn key="aim:treeVisibilitiSaveChanges">Save Changes</digi:trn>
	</c:set>
	<html:submit style="dr-menu" value="${translation}" styleClass="buttonx" property="saveTreeVisibility" />
	</digi:form>
		</td>
	</tr>
	<tr><td>
				<TABLE width="800px" style="font-size:12px;">
					<TR>
						<TD COLSPAN="2"><strong><digi:trn >Shortcuts</digi:trn></strong></TD>
					</TR>
					<TR>
						<TD nowrap="nowrap" bgcolor="#E9E9E9">
							CTRL+> - <digi:trn>next result</digi:trn>&nbsp;
						<br />
						</TD>
					</TR>
					<TR>
						<TD nowrap="nowrap" bgcolor="#E9E9E9">
							CTRL+< - <digi:trn>previous result</digi:trn>&nbsp;
						<br />
						</TD>
					</TR>					
				</TABLE>
				</td></tr>
</table>
<!--[if IE]>
<script type="text/javascript">
document.getElementById=function(str){
str=new String(str);
var allEls=document.getElementsByTagName("*"),l=allEls.length;
for(var i=0;i<l;i++)if(allEls[i].id==str || allEls[i].getAttribute("id")==str)return allEls[i];
return null;
}

document.getElementsByName=function(str){
str=new String(str);
var myMatches=new Array();
var allEls=document.getElementsByTagName("*"),l=allEls.length;
for(var i=0;i<l;i++)if(allEls[i].name==str || allEls[i].getAttribute("name")==str)myMatches[myMatches.length]=allEls[i];
return myMatches;
}
</script>
<![endif]-->
<script type="text/javascript">
	var dhtmlArray = document.getElementsByName("dhtmltreeArray");
	var idOfFolderTrees = new Array();
	for(index=0; index < dhtmlArray.length;index++)
	{
		idOfFolderTrees[index] = dhtmlArray[index].id;
	}

	var myTabs = new YAHOOAmp.widget.TabView("demo");
	myTabs.set('activeIndex', 0);
	
</script>


