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
<digi:instance property="aimDynLocationManagerForm" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dragDropTree.js"/>"></script>


<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/folder-tree-static.css" />" />
<link rel="stylesheet" href="<digi:file src="module/aim/css/css_dhtmlsuite/context-menu.css" />" />

<style type="text/css">
	a.atree {
		font-size: 12px;
	}
	span.spantree {
		font-size: 12px;
	}
	a.functional {
		cursor:pointer; 
		color: blue; 
		font-size: 11px;
	}
	
	.topAlign {
	vertical-align: top;
	}
	.leftButton {
	margin-left:-15px;
	}

</style>

<script type="text/javascript">
	locationsUlId			= "tree_ul_1";
	unorgLocsUlId			= "tree_ul_2";
	<logic:empty name="aimDynLocationManagerForm" property="unorganizedLocations">
	unorgLocsUlId			= "";
	</logic:empty>

	treeObj					= null; 

	
	function initTree(){
		treeObj = new DHTMLSuite.JSDragDropTree();
		treeObj.setTreeId('dhtmlgoodies_tree');
		treeObj.init();
		hideRootNode();
		treeObj.expandAll();
		document.getElementById("p_tree").style.display	= "";
		//treeObj.showHideNode(false, "DHTMLSuite_treeNode1");
	}
	function hideRootNode() {
		var rootEl	= document.getElementById("DHTMLSuite_treeNode1");
		var i;
		for (i=0; i<rootEl.childNodes.length; i++) {
			var el	= rootEl.childNodes[i];
			if ( el.nodeName.toLowerCase() == "a" || el.nodeName.toLowerCase() == "img")
				el.style.display	= "none";
		}
	}
	function deleteLocation(deleteLocationId) {
		if ( confirm("<digi:trn>Are you sure you want to delete this location and all the locations it contains ?</digi:trn>") ) {
			var myForm							= document.getElementById("dynLocationManagerForm");
		    <digi:context name="delete" property="context/module/moduleinstance/dynLocationManager.do"/>;
			myForm.action="${delete}?hideEmptyCountriesAction="+document.getElementById("hide_empty_countries").checked;
			myForm.deleteLocationId.value		= deleteLocationId;
			myForm.target="_self";
			myForm.submit();
		}
	}
	function submitTreeStructure () {
		if ( !checkStructure(null, -1) ) {
			alert("<digi:trn>There is an error in the tree structure</digi:trn>");
			return false;
		} else {
			alert("<digi:trn>Tree structure checked</digi:trn>");
			return true;
		}
		
		//var myForm						= document.getElementById("dynLocationManagerForm");
		//myForm.treeStructure.value		= getTreeStruct(locationsUlId);
		//myForm.unorgLocations.value		= getTreeStruct(unorgLocsUlId);
		//myForm.target="_self";
		//myForm.submit();
		
	}
	function getTreeStruct (ulId) {
		if ( ulId == null || ulId.length == 0 )
			return ""; 
		var returnString	= "";
		rootUlEl			= document.getElementById(ulId);
		returnString		= findNextLevelStructure( rootUlEl, 0, 0, returnString );
		//alert(returnString);
		return returnString;
	}
	function simpleSubmit() {
		var myForm		= document.getElementById("dynLocationManagerForm");
		myForm.action	= "/aim/dynLocationManager.do?hideEmptyCountriesAction="+document.getElementById("hide_empty_countries").checked ; 
		myForm.target="_self";
		myForm.submit();
	}

	
	/** 
	 * We presume that each "li" element has a child "a". 
	 * If there is also a "ul" element it must be inside the "li" and after the "a" 
     */
	function findNextLevelStructure(parentEl, parentDbId, layer, returnString) {
		var i, j;
		for (i=0; i<parentEl.childNodes.length; i++) {
			var locationDbId;
			if ( parentEl.childNodes[i].nodeName.toLowerCase() == "li" ) {
				var childLiEl	= parentEl.childNodes[i];
				for (j=0; j<childLiEl.childNodes.length; j++) {
					if ( childLiEl.childNodes[j].nodeName.toLowerCase() == "a" ) {
						var childAEl	= childLiEl.childNodes[j];
						locationDbId	= childAEl.id.substr(4);
						if ( locationDbId != "-1" ) // This is to avoid hitting an Unspecified Node
							returnString	+= locationDbId + "p" + parentDbId + "h" + layer + "|"; 
					}
					if ( childLiEl.childNodes[j].nodeName.toLowerCase() == "ul" ) {
						if ( locationDbId != "-1" ) // This is to avoid hitting an Unspecified Node
							returnString	= findNextLevelStructure( childLiEl.childNodes[j], locationDbId, layer+1, returnString );
						else
							returnString	= findNextLevelStructure( childLiEl.childNodes[j], parentDbId, layer+1, returnString );
					} 
				}
			}
		}

		return returnString;
	}

	function checkStructure(parentEl, requiredLevel) {
		var returnValue	= true;
		var i, j;
		if (parentEl == null)
			parentEl	= document.getElementById("tree_ul_0");
		for (i=0; i<parentEl.childNodes.length; i++) {
			var locationDbId;
			if ( parentEl.childNodes[i].nodeName.toLowerCase() == "li" ) {
				var childLiEl	= parentEl.childNodes[i];
				if ( requiredLevel != getLevel(childLiEl) ){
					returnValue		= false;
					markNode(childLiEl);
				}
				else
					unMarkNode(childLiEl);
				for (j=0; j<childLiEl.childNodes.length; j++) {
					if ( childLiEl.childNodes[j].nodeName.toLowerCase() == "ul" ) {
						returnValue		= returnValue&checkStructure( childLiEl.childNodes[j], requiredLevel+1 );
					} 
				}
			}
		}
		return returnValue;
	}

	function markNode (liEl) {
		liEl.getElementsByTagName("span")[2].style.display	= "inline";
	}
	function unMarkNode (liEl) {
		liEl.getElementsByTagName("span")[2].style.display	= "none";
	}
	function getLevel (liEl) {
		for ( var i=0; i<liEl.childNodes.length; i++ ) {
			if ( liEl.childNodes[i].nodeName.toLowerCase() == "span" )
				return parseInt( liEl.childNodes[i].innerHTML );
		}
		return -1;		
	}

	function addLocation(parentDbId, locCategDbId ) {
		var myForm						= document.getElementById("addNewLocationForm");
		myForm.parentCatValId.value		= locCategDbId;
		myForm.parentLocationId.value 	= parentDbId;
		myForm.event.value 				= "add";
		myForm.target="_self";
		myForm.submit();
	}
	
	function editLocation(locationDbId) {
		var myForm						= document.getElementById("addNewLocationForm");
		myForm.editedId.value			= locationDbId;
		myForm.event.value 				= "edit";
		myForm.target="_self";
		myForm.submit();
	}

	YAHOOAmp.util.Event.addListener(window, "load", initTree) ;
	 function exportXSL(){
	        <digi:context name="exportUrl" property="context/module/moduleinstance/exportDynLocationManager2XSL.do"/>;
			var myForm= document.getElementById("dynLocationManagerForm");
	        myForm.action="${exportUrl}?hideEmptyCountriesAction="+document.getElementById("hide_empty_countries").checked;
	        myForm.target="_blank";
	        myForm.submit();
	    }
	 
	 
	 var myPanelExport = new YAHOO.widget.Panel("export", {
			width :"250px",
			fixedcenter :true,
			constraintoviewport :true,
			underlay :"none",
			close :true,
			visible :false,
			modal :true,
			draggable :true
		});
	
	 function closeExportPanel () {
		 myPanelExport.hide(); 
	 }
	 
</script>
 <div style="margin:0 auto;width:1000px;" class="admin-content">
<bean:define id="myForm" toScope="request" name="aimDynLocationManagerForm" />
<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="90%" class="box-border-nopadding">
	<tr>
		<td class="r-dotted-lg" width="14">&nbsp;</td>
		<td align="left" class="r-dotted-lg" vAlign="top" width="750">
			<table cellPadding="5" cellSpacing="0" width="100%" border="0">
				<!--<tr> -->
					<!-- Start Navigation -->
					<!-- <td height="33"><span class="crumb">
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						
						
						<digi:trn key="aim:regionManager">
							Region Manager
						</digi:trn>
					</td> -->
					<!-- End navigation -->
				<!-- </tr> -->
				<tr>
					<td height="16" vAlign="center" width="571">
					<h1 class="admintitle" >
							<digi:trn key="aim:regionManager">
								Region Manager
							</digi:trn>
						</h1>
					</td>
				</tr>
					<tr>
						<td align="left">
						<jsp:include
									page="/repository/aim/view/adminXSLExportToolbar.jsp" /></td>
					</tr>
				<tr>
					<td height="16" vAlign="center" width="571">
						<digi:errors />
					</td>
				</tr>
				<c:if test="${!aimDynLocationManagerForm.importantErrorAppeared}" >
				<tr>
					<td>
						<p>
							&nbsp;&nbsp;[<a class="functional" onclick="treeObj.expandAll();"><digi:trn>Expand</digi:trn></a>]
							&nbsp;&nbsp;[<a class="functional" onclick="treeObj.collapseAll();treeObj.showHideNode(false, 'DHTMLSuite_treeNode1');"><digi:trn>Collapse</digi:trn></a>]
							&nbsp;&nbsp;[<a class="functional"  href="/aim/importIntoDynLocationManagerXSL.do"><digi:trn>Import Regions</digi:trn></a>]
						</p>
						<p>
							<digi:trn>Hide empty countries</digi:trn> <html:checkbox property="hideEmptyCountries" onchange="simpleSubmit();" onclick="simpleSubmit();" styleId="hide_empty_countries"/>
						</p>
						<p class="invisible-item" id="p_tree">
							<font size="3">
								<ul id="dhtmlgoodies_tree" class="DHTMLSuite_tree">
									<c:set var="locCollection" scope="request" value="${aimDynLocationManagerForm.firstLevelLocations}" />
									<c:set var="locationLevels" scope="request" value="${aimDynLocationManagerForm.locationLevels}" />
									<c:set var="lvlIndex" value="${0}"/>
									<li noDrag="true" id="li-root">
										<a class="atree" id="a-root"><digi:trn>ROOT</digi:trn></a>
										<ul>
											<li noDrag="true">
												<a class="atree" id="a-locations"><digi:trn>Locations</digi:trn></a>
												<img src="/TEMPLATE/ampTemplate/images/green_plus.png" style="height: 13px; cursor: pointer;" 
														onclick="addLocation(-1, ${aimDynLocationManagerForm.firstLayerId})" 
														title="Add ${locationLevels[lvlIndex]}" />
												<jsp:include page="listLocations.jsp" />
											</li>
											<logic:notEmpty name="aimDynLocationManagerForm" property="unorganizedLocations">
											<li noDrag="true">
												<a class="atree" id="a-unorganized-locations"><digi:trn>Unorganized Locations</digi:trn></a>
												<bean:define id="unorgLocCollection" toScope="request" name="aimDynLocationManagerForm" property="unorganizedLocations" />
												<jsp:include page="listUnorgLocations.jsp" />
											</li>
											</logic:notEmpty>
										</ul>
									</li>
								</ul>
							</font>		
						</p>
						<button type="button" class="buton" onclick="submitTreeStructure()" ><digi:trn>Check Structure</digi:trn></button> &nbsp; &nbsp;
<!--						<button type="button" class="buton" onclick=""><digi:trn>Save</digi:trn></button> -->
					</td>
				</tr>
				</c:if>
			</table>
		</td>
	</tr>
</table>

<div id="exportIndicatorTableContent" style="display: none;">
	<div id="exportPanelErrors"></div>
	<form id="exportIndicatorTableForm" method="post" style="margin-left:80px;">
		<c:forEach var="categoryValue" items="${aimDynLocationManagerForm.locationLevels}">
			 <input type="radio" name="admLevel" value="${categoryValue.id}"><digi:trn>${categoryValue.value}</digi:trn><br>
		</c:forEach>
		<br>
		<input type="button" value="Export" class="buttonx leftButton" onclick="exportIndicatorTable();"/>
		<input type="button" value="Cancel" class="buttonx" onclick="closeExportPanel();"/>
	</form>
</div>
<div style="display: none;">
	<form id="addNewLocationForm"  method="post" action="/aim/addNewLocation.do">
		<input type="hidden" name="parentCatValId" />
		<input type="hidden" name="parentLocationId" />
		<input type="hidden" name="editedId" />		
		<input type="hidden" name="event" />
	</form> 
</div>
<div style="display: none;">
	<form id="dynLocationManagerForm" action="/aim/dynLocationManager.do" method="post">
		<input type="hidden" name="treeStructure" />
		<input type="hidden" name="unorgLocations" />
		<input type="hidden" name="deleteLocationId" />
	</form>
</div>
<div style="display: none;">
	<form id="indicatorLayerForm"  method="post" action="/aim/indicatorLayerManager.do">
		<input type="hidden" name="event" />
	</form> 
</div>
</div>