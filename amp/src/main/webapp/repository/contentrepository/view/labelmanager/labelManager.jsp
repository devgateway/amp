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

<%@page import="org.digijava.module.contentrepository.jcrentity.Label"%>
<%@page import="org.digijava.module.contentrepository.helper.CrConstants"%><digi:instance property="crLabelManagerForm" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dragDropTree.js"/>"></script>

<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/slider/assets/skins/sam/slider.css"> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/colorpicker/assets/skins/sam/colorpicker.css"> 
<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/slider/slider-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/colorpicker/colorpicker-min.js"></script> 


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

</style>

<script type="text/javascript">

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
	function deleteLabel(labelUUID) {
		if ( confirm("<digi:trn>Are you sure you want to delete this label or label category ?</digi:trn>") ) {
			var myForm							= document.getElementById("LabelManagerForm");
			myForm.deleteLabelUUID.value		= labelUUID;
			myForm.submit();
		}
	}


	function resetForm () {
		var myForm						= document.getElementById("addEditLabelForm");
		myForm.editParentUUID.value		= "";
		myForm.editUUID.value			= "";
		myForm.editLabelName.value		= "";
		myForm.editLabelType.value		= "";
		myForm.editLabelColor.value		= "";
		myForm.editLabelBackgroundColor.value		= "";
	}

	function addLabel( parentUUID, labelType ) {
		var lt							= '<%= CrConstants.LABEL_TYPE_LEAF %>';
		if ( labelType != null ) 
			lt 		= labelType;
		
		resetForm();
		var myForm						= document.getElementById("addEditLabelForm");
		myForm.editParentUUID.value		= parentUUID;
		myForm.editLabelType.value		= lt;

		showAddEditPanel();
		
		//myForm.event.value 				= "add";	
		//myForm.submit();
	}
	function editLabel( uuid ) {
		resetForm();
		var myForm						= document.getElementById("addEditLabelForm");
		var aEl							= document.getElementById("aid-" + uuid);
		if (aEl != null) {
			var color				= aEl.style.color;
			var backgroundColor		= aEl.style.backgroundColor;
			var name				= aEl.innerHTML;
			myForm.editUUID.value					= uuid;
			myForm.editLabelName.value				= name;
			myForm.editLabelColor.value				= color;
			myForm.editLabelBackgroundColor.value	= backgroundColor;

			showAddEditPanel();
		}
		
		//myForm.event.value 				= "edit";	
		//myForm.submit();
	}

	function showAddEditPanel() {
		var panel	= YAHOO.amp.addEditPanel; 
		if (panel == null) {
			var divEl			= document.getElementById('addEditLabelDiv');
			divEl.style.display	= "";
			
			panel 		= new YAHOO.widget.Panel("panelForLabels", { width:"400px", visible:true, draggable:true, close:true, modal:false } );
			panel.setHeader('<digi:trn jsFriendly="true">Label Manager</digi:trn>');
			panel.setBody( divEl );
			panel.render(document.body);
			YAHOO.amp.addEditPanel	= panel;
			panel.center();
		}
		panel.show();

	}
	function showColorPickerPanel(inputEl) {
		var panel	= YAHOO.amp.colorPickerPanel;
		if (panel == null) {
			var divEl			= document.getElementById('colorPickerDiv');
			divEl.style.display	= "block";
			panel 		= new YAHOO.widget.Panel("panelForColors", { width:"450px", visible:true, draggable:true, close:true, modal:false } );
			panel.setHeader('<digi:trn jsFriendly="true">Pick Color</digi:trn>');
			panel.setBody( divEl );
			panel.setFooter('<button id="okColorButton"><digi:trn>OK</digi:trn></button>');
			panel.render(document.body);
			
			YAHOO.util.Event.addListener("okColorButton", "click", 
					function(o){
						YAHOO.amp.picker.destinationEl.value = "#" +  YAHOO.amp.picker.get("hex");
						YAHOO.amp.colorPickerPanel.hide();
					} ) ;

			panel.beforeHideEvent.subscribe(function() {YAHOO.amp.addEditPanel.show();} );
			
			YAHOO.amp.colorPickerPanel	= panel;

			YAHOO.amp.picker = new YAHOO.widget.ColorPicker("colorPickerDiv", {
				images: {
					PICKER_THUMB: "/TEMPLATE/ampTemplate/images/yui/colorpicker/picker_thumb.png",
					HUE_THUMB: "/TEMPLATE/ampTemplate/images/yui/colorpicker/hue_thumb.png"
				},
				showhexcontrols: true
				
				//Here are some other configurations we could use for our Picker:
				//showcontrols: false,  // default is true, false hides the entire set of controls
				//showhexcontrols: true, // default is false
				//showhsvcontrols: true  // default is false
			});

			panel.center();
		}
		YAHOO.amp.addEditPanel.hide();
		YAHOO.amp.picker.destinationEl	= inputEl;
		panel.show();

	}
	YAHOO.namespace("YAHOO.amp");
	YAHOO.util.Event.addListener(window, "load", initTree) ;
        function validate(){
            var myForm= document.getElementById("addEditLabelForm");
            var valid=false;
            if(myForm.editLabelName.value==''){
                alert('<digi:trn  jsFriendly="true">Please enter name for this label</digi:trn>');
            }
            else{
               valid=true;
            }
            return valid;
        }
</script>
<h1 class="admintitle">
						<digi:trn>
								Resources Label Manager
							</digi:trn>
					</h1>
<bean:define id="myForm" toScope="request" name="crLabelManagerForm" />
<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="1000" align=center>
	<tr>
		<td align="left" class="r-dotted-lg" vAlign="top" width="750">
			<table cellPadding="5" cellSpacing="0" width="100%" border="0">
				<tr>
					<!-- Start Navigation -->
					<td height="33"><span class="crumb">
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn>
							Resources Label Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
			
				<tr>
					<td height="16" vAlign="center" width="571">
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td style="border:1px solid #CCCCCC;">
						<div style="background-color:#F2F2F2; padding:5px;">
							&nbsp;&nbsp;[<a class="functional" onclick="treeObj.expandAll();"><digi:trn>Expand</digi:trn></a>]
							&nbsp;&nbsp;[<a class="functional" onclick="treeObj.collapseAll();treeObj.showHideNode(false, 'DHTMLSuite_treeNode1');"><digi:trn>Collapse</digi:trn></a>]
						</div>

						<p class="invisible-item" id="p_tree">
							<font size="3">
								<ul id="dhtmlgoodies_tree" class="DHTMLSuite_tree">
									<c:set var="labelCollection" scope="request" value="${myForm.rootLabel.children}" />
									<li noDrag="true" id="li-root">
										<a class="atree" id="a-root"><digi:trn>ROOT</digi:trn></a>
										<ul>
											<li noDrag="true">
												<a class="atree"><digi:trn>Labels</digi:trn></a>
												<img src="/TEMPLATE/ampTemplate/images/green_plus.png" style="height: 13px; cursor: pointer;" 
														onclick="addLabel('${myForm.rootLabel.uuid}', '<%=CrConstants.LABEL_TYPE_FOLDER %>')" 
														title="<digi:trn>Add Label</digi:trn>" />
												<jsp:include page="listLabels.jsp" />
											</li>
										</ul>
									</li>
								</ul>
							</font>		
						</p>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<div id="addEditLabelDiv" style="display: none;">
	<form id="addEditLabelForm"  method="post" action="/contentrepository/labelManager.do?addOrEdit=true">
		<input type="hidden" name="editParentUUID" />
		<input type="hidden" name="editUUID" />
		<input type="hidden" name="editLabelType" />
		<table width=300 align=center style="font-size:12px;">
			<tr>
				<td><digi:trn>Name</digi:trn><font size="2" color="#FF0000">*</font></td>
				<td><input type="text" name="editLabelName" /></td>
			</tr>
			<tr>
				<td><digi:trn>Text Color</digi:trn></td>
				<td><input type="text" name="editLabelColor" onclick="showColorPickerPanel(this)" onfocus="showColorPickerPanel(this)" /></td>
			</tr>
			<tr>
				<td><digi:trn>Background Color</digi:trn></td>
				<td><input type="text" name="editLabelBackgroundColor" onclick="showColorPickerPanel(this)" onfocus="showColorPickerPanel(this)" /></td>
			</tr>
			<tr>
				<td colspan="2" align=center>
					<button type="submit" class="buttonx" onclick="return validate()"><digi:trn>Submit</digi:trn></button>
				</td>
			</tr>
                         <tr>
                            <td colspan="2"><digi:trn>All fields marked with</digi:trn> <font size="2" color="#FF0000">*</font> <digi:trn>are required</digi:trn></td>
                        </tr>
		</table>
		
	</form> 
</div>

<div style="display: none;">
	<form id="LabelManagerForm" action="/contentrepository/labelManager.do?delete=true" method="post">
		<input type="hidden" name="deleteLabelUUID" />
	</form>
</div>

<div class="yui-picker" id="colorPickerDiv" style="display:none; padding: 6px; background-color: #eeeeee; width: 420px; height:220px;">AAA</div>
	