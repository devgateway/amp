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
<%@ page import="org.digijava.ampModule.categorymanager.util.CategoryConstants"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
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
<digi:instance property="structuretypeform" />
<digi:form action="/structureTypeManager.do?action=save" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
<h1 class="admintitle" style="text-align:left;"><digi:trn>Add/Edit structure</digi:trn></h1>
<table bgColor=#ffffff cellPadding=5 cellspacing="1" width="1000" align=center>
  <tr>
    <td align=left valign="top" width="1000"><table cellPadding=5 cellspacing="0" width="100%">
        <tr>
          <!-- Start Navigation -->
          <td height=33><span class=crumb>
            <c:set	var="translation">
              <digi:trn>Click here to goto Admin Home</digi:trn>
            </c:set>
            <digi:link ampModule="aim" href="/admin.do" styleClass="comment" title="${translation}">
              <digi:trn> Admin Home </digi:trn>
            </digi:link>
            &nbsp;&gt;&nbsp;
            <c:set var="translation">
              <digi:trn>Click here to go to the structure type manager</digi:trn>
            </c:set>
            <digi:link href="/structureTypeManager.do" styleClass="comment"	title="${translation}">
              <digi:trn>List of Structure Types</digi:trn>
            </digi:link>
            &nbsp;&gt;&nbsp;
            <digi:trn>Edit structure</digi:trn>
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
				<table border="0" cellpadding="0" cellspacing="0" width=772>
                    <tr>
                      <td width=14>&nbsp;</td>
                      <td align=left valign="top" width=520>
                        <table border="0" cellPadding=5 cellspacing="0" width="100%" style="font-size:12px; margin-bottom:15px;">
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=left class=title noWrap colspan="2">
								<digi:errors/>                            
							</td>
                          </tr>
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=center class=title noWrap colspan="2">
								<digi:trn>All fields marked with an</digi:trn><span style='color: red; font-weight: bold; font-size: larger;'> * </span> <digi:trn>are required.</digi:trn>
                            </td>
                          </tr>
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=right>
    	                        <span style='color: red; font-weight: bold; font-size: larger;'>*</span> <digi:trn>Name</digi:trn>
                            </td>
                            <td align=left>
	                            <html:text property="name"></html:text>
                            </td>
                          </tr>
                        </table>
                        </td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td align="center">
                <table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                      <td style="background-color: #c7d4db;height: 18px; font-size:12px;" align=center><strong><digi:trn>Icon</digi:trn></strong> </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td align="center" style="font-size:12px; padding-top:15px; padding-bottom:15px;">
                <div style="width:50px;height:50px;background-color:#FFFFFF;">
                <img id="imgPlaceholder" src="/esrigis/structureTypeManager.do~action=displayIcon~id=${structuretypeform.ampStructureFormId}" style="border:1px solid black;"/>
                </div>
                </td>
              </tr>
              <tr>
                <td valign="top" align="center">
					<table cellpadding="3" cellspacing="3" style="font-size:12px;">
						<tr id="tr_path_thumbnail">
						<td><digi:trn>Select Icon to upload:</digi:trn><font color="red">*</font></td>
						<td>
                            <html:file property="iconFile" styleId="iconFile"/>
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
	var subForm				= document.forms["structuretypeform"];
	subForm.action			= "/esrigis/structureTypeManager.do";
	subForm.submit();
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
	var isnumeric = IsNumeric($("input[name=name]").val());
	if($("input[name=name]").val() == "" || isnumeric ){
		strError = "<digi:trn jsFriendly='true'>Name (Non Numeric Characteres)</digi:trn>\n";
	}

	var srcImg = $('#imgPlaceholder').attr('src');
	var n = srcImg.indexOf("~id=");
	var srcLength = srcImg.length;
	if(srcLength<=n+4){
		if($("input[name=iconFile]").val() == ""){
			if (strError==""){
				strError = "<digi:trn jsFriendly='true'>Icon</digi:trn>\n";
			}else{
				strError = strError +  "<digi:trn jsFriendly='true'>Icon</digi:trn>\n";
			}
		}
	}
	
	if (strError != ""){
		alert("<digi:trn jsFriendly='true'>Please complete the following fields:</digi:trn>\n" + strError);
		return false;
	}
	return true;
}
$("#iconFile").change(function(){
	var binaryImg = document.getElementById("iconFile").files.item(0).getAsDataURL();
	document.getElementById("imgPlaceholder").src = binaryImg;
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
</script>
</digi:form>
