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

	function changeLevel(id) {
	// alert("aaaaaaaaaaa "+id);
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

	<script type="text/javascript">
		var idOfFolderTrees = ['dhtmlgoodies_tree'];
	</script>
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/folder-tree-static.css" />" />
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/context-menu.css" />" />

<script type="text/javascript">
function openFieldPermissionsPopup(fieldId) {
			<digi:context name="assignFieldPermissionsURL" property="context/module/moduleinstance/assignFieldPermissions.do?fieldId=" />
			openURLinWindow("<%=assignFieldPermissionsURL%>"+fieldId,280, 230);
}
</script>



<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampFeatureManager">
		Feature Manager 
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	
	<tr>
		<td><br/>
		</td>
	</tr>
	<tr>
		<td>
<div id="searchBox" style="border:1px solid black;padding:2px 2px 2px 2px;"  style="position: fixed;">
<form name="searchBoxForm" onsubmit="searchFunction();return false;">
<digi:trn key="fm:search:searchTitle">Search</digi:trn> 
<input type="Text" id="searchCriteria"/> 
<input type="submit" value="<digi:trn key="fm:search:search">Search</digi:trn>" />
<input type="button" onclick="resetSearch()" value="<digi:trn key="fm:search:reset">Reset</digi:trn>" />

<input type="button" id="prevSearchButton" onclick="prevResult()" value="<<" disabled="disabled" style="display:none;"/>
<input type="button" id="nextSearchButton" onclick="nextResult()" value=">>" disabled="disabled"  style="display:none;"/>
<span id="spanSearchMessage" style="color:red;font-weight:bold;"></span>
</form>
</div>

	<digi:instance property="aimVisibilityManagerForm" />
	<digi:form action="/visibilityManager.do" method="post" >

	<bean:define name="aimVisibilityManagerForm" property="ampTreeVisibility" id="template" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page"/>
	<bean:define name="template" property="items" id="modules" type="java.util.Map"  toScope="page"/>
	<bean:define name="template" property="root" id="currentTemplate" type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility" scope="page" toScope="request"/>
		
	 <c:set var="translation">
         <digi:trn key="aim:addNoLevel">No Level</digi:trn>
        </c:set>
	<p><digi:trn key="aim:newFeatureTemplateNameBbl">Template Name:</digi:trn> <input type="text" name="templateName" size="30" value="<%=session.getAttribute("templateName")%>"/>
	<c:set var="templateId">
		<bean:write name="template" property="root.id"/>
	</c:set>
	 <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.ACTIVITY_LEVEL %>" compareWith="true" onTrueEvalBody="true">
		<category:showoptions name="aimVisibilityManagerForm" firstLine="${translation}" outeronchange="javascript:changeLevel('${templateId}')" property="levelCategory"  keyName="<%= org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_LEVEL_KEY %>" styleClass="inp-text" />
	</gs:test>

<script language="javascript">
<!--


var arrayLI = new Array();
var scrollArray = new Array();
var currentScrollItem = 0;


function resetSearch(){
	disableSearchButtons();
	treeCollapseAll("dhtmlgoodies_tree");
	resetHighlight("dhtmlgoodies_tree");
	showNodeObject(false,"dhtmlgoodies_tree");
	document.getElementById("searchCriteria").value = "";
	document.getElementById("spanSearchMessage").innerHTML = "";
}

function nextResult()
{
	highlightResultReset(scrollArray[currentScrollItem]);

	currentScrollItem++;
	if(currentScrollItem == scrollArray.length)
		currentScrollItem = 0;

	highlightResult(scrollArray[currentScrollItem])
	scrollArray[currentScrollItem].scrollIntoView(false);
}

function highlightResultReset(linkObject)
{
	linkObject.style.backgroundColor = '#FFFFFF';
	linkObject.style.color = '#0E69B3';
}

function highlightResult(linkObject)
{
	linkObject.style.backgroundColor = '#FF0000';
	linkObject.style.color = '#FFFFFF';
}

function prevResult()
{
	highlightResultReset(scrollArray[currentScrollItem]);

	currentScrollItem--;
	if(currentScrollItem == -1)
		currentScrollItem = scrollArray.length-1;
	scrollArray[currentScrollItem].scrollIntoView(false);
	highlightResult(scrollArray[currentScrollItem]);

}

function setBranch(objectLI)
{
	var tempObject = objectLI;
	var tempString = "";
	var segui = 0;
	while(tempObject.parentNode.id != "dhtmlgoodies_tree")
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
	if(document.getElementById("prevSearchButton").disabled && document.getElementById("prevSearchButton").disabled )
	{
		document.getElementById("prevSearchButton").style.display = '';
		document.getElementById("nextSearchButton").style.display = '';
		document.getElementById("prevSearchButton").disabled = false;
		document.getElementById("nextSearchButton").disabled = false;
	}
}
function disableSearchButtons()
{
	if(!document.getElementById("prevSearchButton").disabled && !document.getElementById("prevSearchButton").disabled )
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
	var searchableObjectsLI = document.getElementById("dhtmlgoodies_tree").getElementsByTagName("LI");
	isKeycaptureOn = true;
	if(searchCriteria.length < 3) {
		alert("<digi:trn key="fm:search:searchQuestion">Please, enter a search of more than 2 characters.</digi:trn>");
		return;
	}
	
	treeCollapseAll("dhtmlgoodies_tree");
	resetHighlight("dhtmlgoodies_tree");
    currentScrollItem = 0;
	scrollArray = new Array();
	
	searchExpandedNodes = "dhtmlgoodies_tree";
	var countMatches = 0;
	for(a=0; a< searchableObjectsLI.length; a++){
		var text = searchableObjectsLI[a].title;
		if(text.toLowerCase().search(searchCriteria.toLowerCase()) != -1)
		{
			countMatches++;
			enableSearchButtons();

			var objectLI = searchableObjectsLI[a];
			
			arrayLI = new Array();
			setBranch(objectLI);

			searchExpandedNodes = "dhtmlgoodies_tree";

			for(b=1;b<arrayLI.length;b++){
				searchExpandedNodes += "," + arrayLI[b];
			}
			//alert(searchExpandedNodes);
//			Set_Cookie('dhtmlgoodies_expandedNodes',initExpandedNodes,500);
			
			if(searchExpandedNodes){
				var nodes = searchExpandedNodes.split(',');
				for(var no=0;no<nodes.length;no++){
					if(nodes[no])showNodeObject(false,nodes[no]);	
				}		
			}
		
			if(objectLI.getElementsByTagName("A")[0]){
				objectLI.getElementsByTagName("A")[0].style.fontWeight = "bold";
				scrollArray[scrollArray.length] = objectLI.getElementsByTagName("A")[0];
			}

		}
	}
	document.getElementById('nextSearchButton').focus();

	if(countMatches > 0){
		scrollArray[0].scrollIntoView(false);
		scrollArray[0].style.backgroundColor = '#FF0000';
		scrollArray[0].style.color = '#FFFFFF';
		setSearchMessage( countMatches + " <digi:trn key="fm:search:matchesFound">items found</digi:trn>.");
	}
	else
	{
		showNodeObject(false,"dhtmlgoodies_tree");
		setSearchMessage("<digi:trn key="fm:search:noMatches">No matches found</digi:trn>.");
	}	
	
}
function setSearchMessage(stringMessage){
	var spanSearchMessage = document.getElementById("spanSearchMessage");
	spanSearchMessage.innerHTML = stringMessage;
}
	function showNodeObject(e,inputId)
	{
		thisNode = document.getElementById(inputId);
		if(!thisNode){
			thisNode = document.getElementById("dhtmlgoodies_tree");
		}
		var ul = thisNode.getElementsByTagName('UL')[0];
		ul.style.display='block';
		var img = thisNode.getElementsByTagName('IMG')[0];
		img.src = img.src.replace(plusImage,minusImage);
		return;
	}

-->

</script>
	</p>
	<ul id="dhtmlgoodies_tree" class="dhtmlgoodies_tree">
	<li><a href="#" id="<bean:write name="template" property="root.id"/>" style="font-size: 12px;color:#0e69b3;text-decoration:none" ><bean:write name="template" property="root.name"/></a>
			<ul id="liRoot">
				<logic:iterate name="modules" id="module" type="java.util.Map.Entry" >
					<bean:define id="moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="page" toScope="request"/>
					<bean:define id="_moduleAux" name="module" property="value" type="org.dgfoundation.amp.visibility.AmpTreeVisibility"/>
					<bean:define id="_moduleAux2" name="_moduleAux" property="root" type="org.digijava.module.aim.dbentity.AmpModulesVisibility" scope="page"/>
					<bean:define id="size" name="_moduleAux2" property="submodules"/>
					<logic:equal name="aimVisibilityManagerForm" property="existSubmodules" value="false">
						<jsp:include page="generateTreeXLevelVisibility.jsp" />
					</logic:equal>
					<logic:equal name="aimVisibilityManagerForm" property="existSubmodules" value="true">
							<jsp:include page="generateTreeXLevelVisibility.jsp" />
					</logic:equal>
				</logic:iterate>
				
			</ul>
		</li>
	</ul>
	<c:set var="translation">
		<digi:trn key="aim:treeVisibilitiSaveChanges">Save Changes</digi:trn>
	</c:set>
	<html:submit style="dr-menu" value="${translation}" property="saveTreeVisibility" />
	</digi:form>
		</td>
	</tr>
</table>



