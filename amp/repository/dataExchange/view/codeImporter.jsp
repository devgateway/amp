<%@ page pageEncoding="UTF-8" %>
<%@page import="org.digijava.module.dataExchange.action.CodeImporter"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>

<digi:instance property="codeImporterForm"/>

<link href="/repository/dataExchange/view/css/iati.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.submitButtonClass {
display: inline;
margin-top:30px;
margin-right:100px;
}
#uploadBtn{
	   width: 90px;
	   height:20px;
	   padding: 10px;
	   text-align: center;
	   background-color: #EEE;
	   cursor:pointer;
	   border: 1px solid #BBB;
	   background-image: -moz-linear-gradient(top left, #707070 -50%, #FCFCFC 110.00000000000001%);
	
	  }
	
div.flowContainer {
	height:500px;
	overflow-x:hidden;
	overflow-y:auto;
}



td.typesListItem {
	border: 1px solid transparent;
	cursor:pointer;
	font-family: Arial,Helvetica,sans-serif;
  font-size: 11px !important;
  padding-left:5px;
  padding-right:5px;
  color:black;
  
}

td.typesListItem:hover {
	border: 1px solid #c9cedb;
	background-color:#eaedf3;
}

td.typesListItem:active {
	border: 1px solid #c9cedb;
	background-color:#4c6b7d;
	color:white;
}

td.typesListItemActive {
	border: 1px solid #c9cedb;
	background-color:#364b58;
	color:white;
	cursor:pointer;
	font-family: Arial,Helvetica,sans-serif;
  font-size: 11px !important;
  padding-left:5px;
  padding-right:5px;
}

table.defaultTable {
	border: 1px solid #B8B7B7;
	border-collapse:collapse;
}

td.header {
	color: #47608E;
}


tr.listOdd {
	background-color: #eaeaeb;
}

tr.listEven {
	background-color: #f9f9f9;
}

tr.bottomDelimiter {
	background-color: #f2f2f2;
	height:5px;
	max-height:5px;
}


td.defaultTab {
		background-color:#f2f2f2;
		border: 1px solid #b8b7b7;
		border-bottom: 0px;
		color: #47608e;
		width: 100px;
		height: 30px;
		text-align:center;
    font-size: 11px;
    font-weight: bold;
	}
	
	td.inactiveTab {
		background-color:#4c6b7f;
		border: 1px solid #b8b7b7;
		border-bottom: none;
		color: white;
		width: 100px;
		height: 30px;
		text-align:center;
		cursor: pointer;
    font-size: 11px;
    font-weight: bold;
	}

	tr.ampNameDifferent {
		background-color:#f6f598;
	}
	
	tr.ampNameModified {
		background-color:#fac5c5;
	}
	
-->
</style>

<script language="javascript">
	function getFile(){
	        $("#upfile").click();
	  }
	
	 function setLabel(obj){
		    var file = obj.value;
		    var fileName = file.split("\\");
		    var text = fileName[fileName.length-1];
		    var suffix=".zip";
	        var suffix2=".xml";
	        if(text.indexOf(suffix, text.length - suffix.length) == -1 &&
	        		text.indexOf(suffix2, text.length - suffix2.length) == -1){
	        	 var errorMsg='<font color="red"><digi:trn>File type not allowed.\nAllowed extensions: *.zip,*.xml</digi:trn></font>';
	        	$("#uploadLabel").html(errorMsg);
	        	$("#upfile").val("");
	        	return;
	        }
		    if (text.length > 33) {
		    	text = text.substring (0,30) + "....";
		    }
		    $("#uploadLabel").text(text);
	}
	 
	function validate() {
		if ($("#upfile").val()=="") {
			return false;
		}
		return true;
	}
</script>

<digi:form action="/codeImporter.do?action=upload" method="post" enctype="multipart/form-data">
	<span class="navigationLinks">
	<a href="/dataExchange/codeImporter.do?action=view" class="t_sm"><b><digi:trn>Import IATI Codes</digi:trn></b></a>&nbsp;
	<a href="/dataExchange/importActionNew.do" class="t_sm"><b><digi:trn>Import IATI XML File</digi:trn></b></a>&nbsp;
	<a href="/dataExchange/createEditSource.do?action=gotoCreatePage&htmlView=true&displaySource=false" class="t_sm"><b><digi:trn>Create New Import Configuration</digi:trn></b></a>
	</span>
	<table class="defaultTable" width=1000 border=1 bordercolor="#B8B7B7" cellpadding="0" cellspacing="0" style="margin:10px;">
		<tr>
			<td colspan=2 align=center background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class="inside"><b><digi:trn>IATI Codes</digi:trn></b></td>
		</tr>
		<tr>
			<td class="inside" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" align="center" width="300"><digi:trn>Code Types</digi:trn></td>
			<td class="inside" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" align="center">Code Items</td>
		</tr>
		<tr>
			<td>
				<div id="typesFlowContainer" class="flowContainer">
					<table cellpadding="2" cellspacing="2" width="100%">
						<logic:iterate name="codeImporterForm" property="types" id="type">
							<tr>
								<bean:size name="type" property="items" id="itemCount"/>
								<td class="typesListItem" title="Items: <bean:write name="itemCount"/>. Update date: <bean:write name="type" property="importDateFormated"/> ">
									<input class="idHolder" type="hidden" value="<bean:write name="type" property="id"/>">
									<input class="nameHolder" type="hidden" value="<bean:write name="type" property="name"/>">
									<input class="ampNameHolder" type="hidden" value="<bean:write name="type" property="ampName"/>">
									<input class="itemCountHolder" type="hidden" value="<bean:write name="itemCount"/>">
									<bean:write name="type" property="name"/>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</div>
			</td>
			<td valign="top">
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td width="20" style="border-bottom: 1px solid #b8b7b7">
						&nbsp;
						</td>
						<td class="innerNavTabs defaultTab" id="itemListTab" height="20" width="70">
							<digi:trn>Item List</digi:trn>
						</td>
						<td width="5" style="border-bottom: 1px solid #b8b7b7">
						&nbsp;
						</td>
						<td class="innerNavTabs inactiveTab" id="propertiesTab" height="20" width="70">
							<digi:trn>Properties</digi:trn>
						</td>
						<td width="460" style="border-bottom: 1px solid #b8b7b7">
							&nbsp;
						</td>
					</tr>
					<tr class="bottomDelimiter">
						<td>
							&nbsp;
						</td>
						<td style="border-top:none;">
							&nbsp;
						</td>
						<td>
						&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="5">
							<div id="codeItemTableContainer" class="flowContainer" style="height:480px;"></div>
							<div id="propertiesTableContainer" class="flowContainer" style="height:480px; display:none"></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr class="bottomDelimiter">
			<td colspan="2">
					
			</td>
		</tr>
		<tr style="height: 100px;">
			<td colspan="2" nowrap align="right">
			<span>
				<digi:trn>Upload IATI codes (xml or zip)</digi:trn> 
				 <span id="uploadBtn" onclick="getFile()"><digi:trn>Browse</digi:trn></span>
			 	<span id="uploadLabel" style="display:inline;width:100px;margin-right: 100px;"><digi:trn>No file chosen</digi:trn></span>
				
<!--    											 this is file input tag, so i hide it! -->
  			<div style='height: 0px;width:0px; overflow:hidden;'><html:file property="file"  styleId="upfile" name="codeImporterForm" onchange="setLabel(this)" /></div>
			
			<c:set var="uploadLabel"><digi:trn>Upload</digi:trn></c:set>
			</span>
			<html:submit value="${uploadLabel}" onclick="return validate();" styleClass="submitButtonClass" />
		
			</td>
		</tr>
	</table>
	
	<script language="javascript">
		$("td.typesListItem").click (function(evt) {
			var targetTd = $(evt.target);
			$("td.typesListItemActive").removeClass("typesListItemActive").addClass("typesListItem");
			targetTd.removeClass("typesListItem").addClass("typesListItemActive");
			var typeId = targetTd.parent().find(".idHolder").val();
			loadTypeItems(typeId);
		});
		
		loadTypeItems = function (typeId) {
			var url = "../../dataExchange/codeImporter.do?action=getCodeItems";
			$.ajax({
			  type: 'GET',
			  url: url,
			  data:{typeId:typeId},
			  success: loadTypeItemsSuccess,
			  dataType: "json",
			});
		}
		
		loadTypeItemsSuccess = function (data, textStatus, jqXHR) {
			var objects = data.objects;
			
			//Item list		
			var codeItemsMarkup = [];
			codeItemsMarkup.push("<table width='100%' style='border-collapse:collapse;' border=1 bordercolor='#cacbcd'>");
			$(objects).each(function(index, element) {
				var rowClassOddEven = index%2==0 ? "listOdd" : "listEven"
				codeItemsMarkup.push("<tr class='");
				codeItemsMarkup.push(rowClassOddEven);
				codeItemsMarkup.push("'>");
				codeItemsMarkup.push("<td width='70%'>");
				codeItemsMarkup.push(element.name);
				codeItemsMarkup.push("</td>");
				codeItemsMarkup.push("<td width='30%'>");
				codeItemsMarkup.push(element.code);
				codeItemsMarkup.push("</td>");
				codeItemsMarkup.push("</tr>");
			});
			codeItemsMarkup.push("</table>");
			$("#codeItemTableContainer").html(codeItemsMarkup.join(''));
			
			
			//Properties
			var codePropertiesMarkup = [];

			codePropertiesMarkup.push("<table width='100%' style='border-collapse:collapse;' border=1 bordercolor='#cacbcd'>");
			codePropertiesMarkup.push("<tr>");
			codePropertiesMarkup.push("<td width='30%'>");
			codePropertiesMarkup.push("<input type='hidden' id='selCodeTypeId' value='");
			codePropertiesMarkup.push(data.id);
			codePropertiesMarkup.push("'>");			
			codePropertiesMarkup.push("IATI Name");
			codePropertiesMarkup.push("</td>");
			codePropertiesMarkup.push("<td width='70%' aligh='right'>");
			codePropertiesMarkup.push(data.name);
			codePropertiesMarkup.push("</td>");
			codePropertiesMarkup.push("</tr>");
			
			codePropertiesMarkup.push("<tr>");
			codePropertiesMarkup.push("<td width='30%'>");
			codePropertiesMarkup.push("Description");
			codePropertiesMarkup.push("</td>");
			codePropertiesMarkup.push("<td width='70%' aligh='right'>");
			codePropertiesMarkup.push(data.description);
			codePropertiesMarkup.push("</td>");
			codePropertiesMarkup.push("</tr>");
			
			codePropertiesMarkup.push("<tr>");
			codePropertiesMarkup.push("<td width='30%'>");
			codePropertiesMarkup.push("Import Date");
			codePropertiesMarkup.push("</td>");
			codePropertiesMarkup.push("<td width='70%' aligh='right'>");
			codePropertiesMarkup.push(data.date);
			codePropertiesMarkup.push("</td>");
			codePropertiesMarkup.push("</tr>");

			codePropertiesMarkup.push("<tr id='typeAmpNameRow' ");
			if (data.ampName != data.name) {
				codePropertiesMarkup.push(" class='ampNameDifferent'");
			}
			codePropertiesMarkup.push(">");
			codePropertiesMarkup.push("<td width='30%'>");
			codePropertiesMarkup.push("AMP Name");
			codePropertiesMarkup.push("</td>");
			codePropertiesMarkup.push("<td width='70%' aligh='right'>");
			codePropertiesMarkup.push("<table width='100%' border='0' cellpadding='0' cellspacing='0'><tr><td>");
			codePropertiesMarkup.push("<input type='hidden' id='typeAmpNameCellOriginal' value='");
			codePropertiesMarkup.push(data.ampName);
			codePropertiesMarkup.push("'>");
			codePropertiesMarkup.push("<input type='hidden' id='typeIatiNameCellOriginal' value='");
			codePropertiesMarkup.push(data.name);
			codePropertiesMarkup.push("'>");
			codePropertiesMarkup.push("<input type='text' id='typeAmpNameCell'  class='inputx' size='70' value='");
			codePropertiesMarkup.push(data.ampName);
			codePropertiesMarkup.push("'>");
			codePropertiesMarkup.push("</td><td align='right'>");
			codePropertiesMarkup.push("<img id='typeAmpNameSave' src='/TEMPLATE/ampTemplate/images/save_dis.png'>");
			codePropertiesMarkup.push("</td><tr></table>");
			codePropertiesMarkup.push("</td>");
			codePropertiesMarkup.push("</tr>");
			
			codePropertiesMarkup.push("</table>");
			$("#propertiesTableContainer").html(codePropertiesMarkup.join(''));
			
			$("#typeAmpNameCell").keyup(function (evt) {
				var targetInput = $(evt.target);
				var row = $("#typeAmpNameRow");
				
				var ampName = targetInput.val();
				var existingAmpName = $("#typeAmpNameCellOriginal").val();
				var existingIatiName = $("#typeIatiNameCellOriginal").val();
				
				var saveIcon = $("#typeAmpNameSave");
				if (ampName != existingAmpName) {
					saveIcon.attr("src","/TEMPLATE/ampTemplate/images/save.png");
					saveIcon.css("cursor","pointer");
				} else {
					saveIcon.attr("src","/TEMPLATE/ampTemplate/images/save_dis.png");
					saveIcon.css("cursor","default");
				}
				
				if (ampName != existingAmpName && ampName != existingIatiName) {
					row.removeAttr('class');
					row.addClass('ampNameModified');
				} else if (ampName == existingAmpName && ampName != existingIatiName) {
					row.removeAttr('class');
					row.addClass('ampNameDifferent');
				} else {
					row.removeAttr('class');
				}
			});
			
			$("#typeAmpNameSave").click(saveUpdatedAmpName);
			
		}
		
		var saveUpdatedAmpName = function() {
			var saveIcon = $("#typeAmpNameSave");
			if(saveIcon.css("cursor") == "pointer") {
				var url = "../../dataExchange/codeImporter.do?action=updateAmpName";
				var typeId = $("#selCodeTypeId").val();
				var newVal = $("#typeAmpNameCell").val();
				$.ajax({
				  type: 'POST',
				  url: url,
				  data:{typeId:typeId, newVal:newVal},
				  success: saveUpdatedAmpNameSuccess,
				  dataType: "json",
				});
			} else {
				//Do nothing
			}
		}
		
		saveUpdatedAmpNameSuccess = function() {
			var saveIcon = $("#typeAmpNameSave");
			saveIcon.attr("src","/TEMPLATE/ampTemplate/images/save_dis.png");
			saveIcon.css("cursor","default");
			
			var row = $("#typeAmpNameRow")
			var ampName = $("#typeAmpNameCell").val();
			$("#typeAmpNameCellOriginal").val(ampName);
			var existingIatiName = $("#typeIatiNameCellOriginal").val();
			
			if (ampName != existingIatiName) {
				row.removeAttr('class');
				row.addClass('ampNameDifferent');
			} else {
				row.removeAttr('class');
			}
		}
		
		
		$("td.innerNavTabs").click(function(evt) {
			var targetTd = $(evt.target);
			var isActive = targetTd.hasClass(".defaultTab");
			if (!isActive) {
				$("td.innerNavTabs").removeClass("defaultTab").addClass("inactiveTab");
				targetTd.removeClass("inactiveTab").addClass("defaultTab");
				if (targetTd.attr("id") == "itemListTab") {
					$("#codeItemTableContainer").css("display","block");
					$("#propertiesTableContainer").css("display","none");
				} else if (targetTd.attr("id") == "propertiesTab") {
					$("#codeItemTableContainer").css("display","none");
					$("#propertiesTableContainer").css("display","block");
				}
			}
		});
		
	</script>
	
</digi:form>