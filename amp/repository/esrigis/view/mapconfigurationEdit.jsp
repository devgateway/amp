<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page import="java.util.List"%>
<%@ page import="org.digijava.module.categorymanager.util.CategoryConstants"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib prefix="httml" uri="http://struts.apache.org/tags-html" %>
<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />

<style>
.contentbox_border {
	width: 		1000px;
}
.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}
.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;
!important padding:2px;
}
.Hovered {
	background-color:#a5bcf2;
}
</style>

<style>
.layoutTable
{
	border: 1px dotted black !important;
}
.layoutTable TD
{
	border: 1px dotted green !important;
}
</style>

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
	-moz-opacity:0;
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
	width: 175px;
}
div.fakefile2 {
	position: absolute;
	top: 2px;
	left: 178px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input {
	width: 83px;
}
-->
</style>
<digi:instance property="mapsconfigurationform" />
<digi:form action="/MapsConfiguration.do?action=save" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
<h1 class="admintitle" style="text-align:left;"><digi:trn>Add/Edit Map Configuration</digi:trn></h1>
<table bgColor=#ffffff cellPadding=5 cellspacing="1" width="1000" align=center>
  <tr>
    <td align=left valign="top" width="1000"><table cellPadding=5 cellspacing="0" width="100%">
        <tr>
          <!-- Start Navigation -->
          <td height=33><span class=crumb>
            <c:set	var="translation">
              <digi:trn>Click here to goto Admin Home</digi:trn>
            </c:set>
            <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}">
              <digi:trn> Admin Home </digi:trn>
            </digi:link>
            &nbsp;&gt;&nbsp;
            <c:set var="translation">
              <digi:trn>Click here to go to the structure type manager</digi:trn>
            </c:set>
            <digi:link href="/MapsConfiguration.do" styleClass="comment"	title="${translation}">
              <digi:trn>List of Map Configurations</digi:trn>
            </digi:link>
            &nbsp;&gt;&nbsp;
            <digi:trn>Edit map configuration</digi:trn>
            </span> </td>
          <!-- End navigation -->
        </tr>
        <tr>
          <td noWrap vAlign="top">
          <table class="contentbox_border" width="100%" border="0" bgcolor="#f4f4f2" cellspacing=0 cellpadding=0>
              <tr>
                <td align="center"><table width="100%" cellspacing=0 cellpadding=0>
                    <tr>
                      <td style="background-color: #c7d4db;height: 18px; font-size:12px;" align=center><strong>Information</strong> </td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td valign="top" bgcolor="#f4f4f2" align="center">
				<table border="0" cellpadding="0" cellspacing="0" width=772 align="center">
                    <tr>
                      <td align=center valign="top" width=100%>
                        <table border="0" cellPadding=5 cellspacing="5" width="100%" style="font-size:12px;
                        margin-bottom:15px;">
                          <tr>
                            <td align=left class=title noWrap colspan="2">
								<digi:errors/>                            
							</td>
                          </tr>
                          <tr>
                            
                            <td align=center class=title noWrap colspan="2"><digi:trn>All fields marked with
                                an</digi:trn><FONT
                                    color=red><B> * </B></FONT><digi:trn>are required.</digi:trn>
                            </td>
                          </tr>
                          <tr>
                            
                            <td align=right class="map-configuration-cell">
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>URL</digi:trn>
                            </td>
                            <td align=left class="map-configuration-cell">
	                            <html:text property="url"></html:text>
                            </td>
                          </tr>
                          <tr>
                            
                            <td align=right class="map-configuration-cell">
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>Indicator/Base Map</digi:trn>
                            </td>
                            <td align=left class="map-configuration-cell">
                            	<html:select property="mapSubType" styleId="mapSubType" onchange="updateMapSubType()">
                            		<html:optionsCollection property="mapSubTypeList" value="key" label="value"/>
                            	</html:select>
                            </td>
                          </tr>
                          <tr>
                            
                            <td align=right class="map-configuration-cell">
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>Map Type List</digi:trn>
                            </td>
                            <td align=left class="map-configuration-cell">
                            	<span id="map_type_list"></span>
                            	<html:select property="mapType" styleId="mapType" onchange="updateMapType()">
                            		<html:optionsCollection property="mapTypeList"  value="key" label="value"/>
                            	</html:select>
                            </td>
                          </tr>
                          <tr id="layer_name">
                            
                            <td align=right class="map-configuration-cell">
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>Layer name</digi:trn>
                            </td>
                            <td align=left class="map-configuration-cell">
	                            <html:text property="configName"></html:text>
                            </td>
                          </tr>
                            <tr id="legend_notes">
                                
                                <td align="right" class="map-configuration-cell">Layer Notes</td>
                                <td align="left" class="map-configuration-cell">
                                    <httml:textarea property="legendNotes" style="width: 350px; height: 50px;"
                                    ></httml:textarea>
                                </td>
                            </tr>
                          <tr id="geo_id">
                            
                            <td align=right class="map-configuration-cell">
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>Geo Id</digi:trn>
                            </td>
                            <td align=left class="map-configuration-cell">
	                            <html:text property="geoId"></html:text>
                            </td>
                          </tr>
                          <tr id="count_id">
                            
                            <td align=right class="map-configuration-cell">
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>Count Field</digi:trn>
                            </td>
                            <td align=left class="map-configuration-cell">
	                            <html:text property="count"></html:text>
                            </td>
                          </tr>
                          <tr id="admin_1">
                            
                            <td align=right class="map-configuration-cell">
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>Admin 1</digi:trn>
                            </td>
                            <td align=left class="map-configuration-cell">
	                            <html:text property="admin1"></html:text>
                            </td>
                          </tr>
                          <tr id="admin_2">
                            
                            <td align=right class="map-configuration-cell">
    	                        <FONT color=red><B><BIG>*</BIG> </B></FONT> <digi:trn>Admin 2</digi:trn>
                            </td>
                            <td align=left class="map-configuration-cell">
	                            <html:text property="admin2"></html:text>
                            </td>
                          </tr>
                        </table>
                        </td>
                    </tr>
                  </table></td>
              </tr>
              <tr id="legend_title">
                <td align="center">
                <table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                      <td style="background-color: #c7d4db;height: 18px; font-size:12px;" align=center><strong><digi:trn>Legend</digi:trn></strong> </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr id="legend_placeholder">
                <td align="center" style="font-size:12px; padding-top:15px; padding-bottom:15px;">
                <img id="imgLegend" src="/esrigis/MapsConfiguration.do~action=displayLegend~id=${mapsconfigurationform.mapId}" style="border:1px solid black;"/>
                </td>
              </tr>
              <tr id="legend_upload_form">
                <td valign="top" align="center">
					<table cellpadding="3" cellspacing="3" style="font-size:12px;">
						<tr id="tr_path_thumbnail">
						<td><digi:trn>Select Legend to upload:</digi:trn><font color="red">*</font></td>
						<td>
                            <html:file property="legend" styleId="legendFile" accept="image/*"/>
						</td>
						</tr>
					</table>
				<br />
				<br />
                </td>
              </tr>

            </table></td>
        </tr>
      </table>
		<div align="center" style="margin-top:10px;">
			<table>
				<tr>
					<td>
						<html:submit styleClass="buttonx"><digi:trn>Save</digi:trn></html:submit>
					</td>
					<td>
						<html:submit styleClass="buttonx" onclick="return cancel();"><digi:trn>Cancel</digi:trn></html:submit>
					</td>
				</tr>
			</table>
		<%-- <html:submit styleClass="buttonx">Save</html:submit> --%>
		</div>
      </td>
  </tr>
</table>
<br />
<br />
<script language="javascript">

function edit(key) {
	document.structureTypeForm.action = "/esrigis/structureTypeManager.do?action=add";
	document.contentForm.target = "_self";
	document.contentForm.editKey.value = key;
	document.contentForm.submit();
}

function cancel()
{
	document.location		= "/esrigis/MapsConfiguration.do";
	return false;
}

function doAction(index, action, confirmation) {
	if(confirmation){
		var ret = confirm("<digi:trn jsFriendly='true'>Are you sure?</digi:trn>");
		if (!ret) return false; 
	}
	document.contentForm.action = "/content/structureTypeManager.do?action=" + action +"&index=" + index;
	document.contentForm.target = "_self";
	document.contentForm.submit();
}

function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	if (tableElement) {
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
}

function validateForm(){
	var strError = "";
	//Check Title and pageCode
	/* var isnumeric = IsNumeric($("input[name=name]").val());
	if($("input[name=name]").val() == "" || isnumeric ){
		strError = "<digi:trn jsFriendly='true'>Name (Non Numeric Characteres)</digi:trn>\n";
	} */
	if($("input[name=iconFile]").val() == ""){
		if (strError==""){
			strError = "<digi:trn jsFriendly='true'>Icon</digi:trn>\n";
		}else{
			strError = strError +  "<digi:trn jsFriendly='true'>Icon</digi:trn>\n";
		}
	}
	if (strError != ""){
		alert("<digi:trn jsFriendly='true'>Please complete the following fields:</digi:trn>\n" + strError);
		return false;
	}
	if (!$("input[name=mapSubType]").val()==3){
		if(!validateUrl($("input[name=url]").val())){
			alert("<digi:trn>Wrong format URL</digi:trn>\n");
			return false;
		}
	}
	return true;
}
$("#legendFile").change(function(){
	var fr = new FileReader;
	fr.onloadend = function (oFREvent) {
		  document.getElementById("imgLegend").src = oFREvent.target.result;
	};
	var binaryImg = document.getElementById("legendFile").files.item(0);
	fr.readAsDataURL(binaryImg);
		
});


function IsNumeric(strString)
//  check for valid numeric strings	
{
var strValidChars = "0123456789.-";
var strChar;
var blnResult = true;

if (strString.length == 0) return false;

//  test strString consists of valid characters listed above
for (i = 0; i < strString.length && blnResult == true; i++)
   {
   strChar = strString.charAt(i);
   if (strValidChars.indexOf(strChar) == -1)
      {
      blnResult = false;
      }
   }
return blnResult;
}


var updateMapType = function(){
	
	switch($("#mapType").val()){
		case "2": // For Main map, we need to enable additional fields
			$("#geo_id").show();
			$("#admin_1").show();
			$("#admin_2").show();
		break;
		default: 
			$("#geo_id").hide();
			$("#admin_1").hide();
			$("#admin_2").hide();
		break;
	}
	
}

var updateMapSubType = function(){
	
	switch($("#mapSubType").val()){
		case "1": // Base map
			//Hide legend
			$("#legend_upload_form").hide();
			$("#legend_placeholder").hide();
			$("#legend_title").hide();
			$("#layer_name").hide();
            $("#legend_notes").hide();
			//Hide mapType
			$("#mapType").show();
			$("#map_type_list").text("");
		break;
		case "2": // Indicator layer
			//Show legend
			$("#legend_upload_form").show();
			$("#legend_placeholder").show();
			$("#legend_title").show();
			$("#layer_name").show();
            $("#legend_notes").show();

            //Hide mapType
			$("#mapType").hide();
			$("#mapType").val("10");
			$("#map_type_list").text("<digi:trn jsFriendly='true'>Indicator</digi:trn>");
		break;
	}
	
}
$(document).ready(
		function(){
			updateMapType();
			updateMapSubType();
		}
);

function checkDot(str, cnt)
{
	var count = 0, index=0, flag;
	for (i = 0;  i < str.length;  i++)
	{
		if(str.charAt(i) == ".")
			count = count + 1;
		if(count == 2)
		{
			index = i + 1;
			break;
		}
			
	}
	var diff = str.length - index;
	if(count >= cnt && diff > 1)
		flag =  true;
	else
	{
		flag =  false;
	}
	return flag;
}

function validateUrl(str)
{
	str = trim(str);
	var flag;
	var temp="";
	if(str.substr(0,3) == "www")
		flag = checkDot(str, 2);
	else if(str.substr(0,7) == "http://" || str.substr(0,8) == "https://")
	{
		temp = str.substring(7,10);
		if(temp == "www")
			flag = checkDot(str, 2);
		else
			flag = checkDot(str, 1);	
	}
	else
	{
		flag = false;
	}
	return flag;
}
</script>
</digi:form>
