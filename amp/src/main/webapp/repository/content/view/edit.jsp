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
<jsp:include page="/repository/aim/view/teamPagesHeader.jsp"  />

<digi:context name="displayThumbnail" property="context/aim/default/displayThumbnail.do" />
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
	top: 0px;
	left: 178px;
	width: 300px;
	padding-left: 3px;
	margin: 0px;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input {
	width: 83px;
}

#thumbnails_table tr td {
	text-align : left;
	
}
-->
</style>
<script language="javascript" type="text/javascript">
    function downloadFile(index, pageCode) {
        if (index != '0') {
            window.location='/content/downloadFile.do?index='+index+'&pageCode=' + pageCode;
//            window.location='/aim/downloadFileFromHome.do?index='+index;
        }
    }

	var labelText = [];
	
    function attachFuncToThumbnail(index, pageCode) {
		var idThumbnail = "displayThumbnail_" + index;
        var lastTimeStamp = new Date().getTime();
        var url = '/content/displayThumbnail.do?index='+index+'&pageCode='+pageCode+'&labelText=true&timestamp='+lastTimeStamp;
        $.get(url, function(data) {
        	returnData = data.split('*');
			hasRelDoc = returnData[0];
			labelText[index] = returnData[1];

            if (hasRelDoc != 'true')
			{
                $("#"+idThumbnail).click(function() {
                    var msg="<digi:trn>No related documents to download!</digi:trn>"
                    alert(msg);
                });
            }
            else
			{
                $("#"+idThumbnail).click(function() {
                    downloadFile(index, pageCode);
                });
            }
            
            if(labelText[index] != null && labelText[index] != "null")
			{
				if (labelText[index].length > 0){
					$("#"+idThumbnail).mouseover(function() {
						 stm(['<digi:trn jsFriendly="true">Description</digi:trn>',labelText[index]],Style[1]);
					});
					$("#"+idThumbnail).mouseout(function() {
						 htm();
					});
				}
            }
        });
    }

</script>
<digi:instance property="contentForm" />
<digi:form action="/contentManager.do?action=save" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
<table bgColor=#ffffff cellPadding=5 cellspacing="1" width="1000" align=center>
  <tr>
    <td align=left valign="top" width="1000"><table cellPadding=5 cellspacing="0" width="100%">
      <!--  <tr> -->
          <!-- Start Navigation -->
          <!-- <td height=33><span class=crumb>
            <c:set	var="translation">
              <digi:trn>Click here to goto Admin Home</digi:trn>
            </c:set>
            <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}">
              <digi:trn> Admin Home </digi:trn>
            </digi:link>
            &nbsp;&gt;&nbsp;
            <c:set var="translation">
              <digi:trn>Click here to go to the content manager</digi:trn>
            </c:set>
            <digi:link href="/contentManager.do" styleClass="comment"	title="${translation}">
              <digi:trn>List of Public View Content</digi:trn>
            </digi:link>
            &nbsp;&gt;&nbsp;
            <digi:trn>Edit content</digi:trn>
            </span> </td>-->
          <!-- End navigation -->
        <!-- </tr> -->
        <tr>
          <td colspan="2" align=center><span class=subtitle-blue>
            <b style="font-size:12px;"><digi:trn>Add/Edit content</digi:trn></b>
            </span> </td>
        </tr>
        <tr>
          <td noWrap vAlign="top">
          <table class="contentbox_border" width="100%" border="0" bgcolor="#f4f4f2" cellspacing=0 cellpadding=0>
              <tr>
                <td align="center"><table width="100%" cellspacing=0 cellpadding=0>
                    <tr>
                      <td style="background-color: #c7d4db;height: 18px; font-size:12px;" align=center><strong><digi:trn>Information</digi:trn></strong> </td>
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
    	                        <span style='color: red; font-weight: bold; font-size: larger;'>*</span> <digi:trn>Title</digi:trn>
                            </td>
                            <td align=left>
	                            <html:text property="title"></html:text>
                            </td>
                          </tr>
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=right>
    	                        <span style='color: red; font-weight: bold; font-size: larger;'>*</span> <digi:trn>Page Code</digi:trn>
                            </td>
                            <td align=left>
	                            <html:text property="pageCode"></html:text>
                            </td>
                          </tr>
                          <tr>
                            <td width="3%">&nbsp;</td>
                            <td align=right>
    	                        <digi:trn>Description</digi:trn>
                            </td>
                            <td align=left>
	                            <html:text property="description"></html:text>
                            </td>
                          </tr>
                        </table>
                        
                        </td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td align="center"><table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                      <td style="background-color: #c7d4db;height: 18px; font-size:12px;" align=center><strong><digi:trn>Layout</digi:trn></strong> </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td align="center"><table width="100%">
                    <tr>
                      <td align="center">
                          <table cellpadding="5" cellspacing="5">
                            <tr>
                              <td align="center">
                              <img src="/repository/content/view/layout_1.png"/><br />
                              <html:radio name="contentForm" property="contentLayout" value="1" disabled="false" />
                              <br />
                              </td>
                              <td align="center">
                              <img src="/repository/content/view/layout_2.png" /><br />
                              <html:radio property="contentLayout" value="2" disabled="false" />
                              <br />
                              </td>
                              <td align="center">
                              <img src="/repository/content/view/layout_3.png" /><br />
                              <html:radio property="contentLayout" value="3" disabled="false" />
                              <br />
                              </td>
                              <td align="center">
                              <img src="/repository/content/view/layout_4.png" /><br />
                              <html:radio property="contentLayout" value="4" disabled="false" />
                              <br />
                              </td>
                            </tr>
                          </table>
					</td>                      
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td align="center"><table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                      <td style="background-color: #c7d4db;height: 18px; font-size:12px;" align=center><strong><digi:trn>Content</digi:trn></strong> </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td valign="top" bgcolor="#f4f4f2" align="center">
                </td>
              </tr>
              <tr>
                <td valign="top" bgcolor="#f4f4f2" align="left" style="border:1px dotted black;">
                	<c:forEach var="layoutNumber" begin="1" end="4">
                    <c:set var="displayLayout">
                        <c:choose>
                            <c:when test="${contentForm.contentLayout eq layoutNumber}">
                            display:block
                            </c:when>
                            <c:otherwise>
                            display:none;
                            </c:otherwise>
                        </c:choose>
                    </c:set>
                    <div style="width:1000px;background-color:#ffffff;${displayLayout}" id="layout_${layoutNumber}" name="layoutGroup">
                    <c:import url="/repository/content/view/layout_${layoutNumber}.jsp">
					  <c:param name="pageCode" value="${contentForm.pageCode}"/>
                      <c:param name="htmlblock_1" value="${contentForm.htmlblock_1}"/>
                      <c:param name="htmlblock_2" value="${contentForm.htmlblock_2}"/>
                    </c:import>
                    </div>
                    </c:forEach>
                </td>
              </tr>
              <tr>
                <td align="center"><table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                      <td style="background-color: #c7d4db;height: 18px; font-size:12px;" align=center><strong><digi:trn>Thumbnails</digi:trn></strong> </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td valign="top" align="center">
					<table cellpadding="3" cellspacing="3" border="0" width="75%"  bgcolor="#FFFFFF" id="dataTable" class="inside" style="margin-top:15px;">
						<tr id="tr_path_thumbnail">
                            <td bgcolor="#C7D4DB" width="20%" class="inside"><digi:trn><b>Thumbnail</b></digi:trn></td>
                            <td bgcolor="#C7D4DB" width="40%" class="inside"><digi:trn><b>Label</b></digi:trn></td>
                            <td bgcolor="#C7D4DB" width="20%" class="inside"><digi:trn><b>Related File</b></digi:trn></td>
                            <td bgcolor="#C7D4DB" width="20%" class="inside"><digi:trn><b>Action</b></digi:trn></td>
                        </tr>
                    <c:forEach  var="content" items="${contentForm.sortedContentThumbnails}" varStatus="loop">
						<tr id="tr_path_thumbnail">
                        <td class="inside"><img src="/content/displayThumbnail.do?index=${loop.index}&pageCode=${contentForm.pageCode}&isAdmin=true" align="middle" width="20" style="border:1px solid #cecece">
                        </td>
                        <td class="inside">${content.thumbnailLabel}</td>
                        <td class="inside">${content.optionalFileName}</td>
                        <td class="inside">
                        	<c:if test="${loop.index != 0}">
                          	<a onclick="doAction(${loop.index}, 'moveup', false)">
                              <img src="/TEMPLATE/ampTemplate/images/arrow_up.gif" border="0" title="<digi:trn>Move up</digi:trn>"/>
                            </a>
                            </c:if>
                        	<c:if test="${loop.index == 0}">
                            	&nbsp;&nbsp;&nbsp;
                            </c:if>
                        	<c:if test="${loop.index != fn:length(contentForm.sortedContentThumbnails)-1}">
                          	<a onclick="doAction(${loop.index}, 'movedown', false)">
                              <img src="/TEMPLATE/ampTemplate/images/arrow_down.gif" border="0" title="<digi:trn>Move down</digi:trn>"/>
                            </a>
                            </c:if>
                        	<c:if test="${loop.index == fn:length(contentForm.sortedContentThumbnails)-1}">
                            	&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                          	<a onclick="doAction(${loop.index}, 'deleteThumb', true)">
                              <img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" title="<digi:trn>Delete thumbnail</digi:trn>"/>
                            </a>
                        </td>
                        </tr>
                    </c:forEach>
                    </table>
                    <br />
                    <br />
                    
					<table id="thumbnails_table" cellpadding="3" cellspacing="3" style="font-size:12px;">
						<tr id="tr_path_thumbnail">
						<td><digi:trn>Select Thumbnail to upload:</digi:trn><font color="red">*</font></td>
						<td>
							<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
								<input id="tempContentThumbnail" name="tempContentThumbnail" type="file" class="file"/>
							</div>
                            
						</td>
						</tr>
						<tr id="tr_path_optional">
						<td><digi:trn>Select Optional File to upload:</digi:trn><font color="red"></font></td>
						<td>
						<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
								<input id="tempContentFile" name="tempContentFile" type="file" class="file"/>
							</div>
						</td>
						</tr>
						<tr>
							<td> 
								<digi:trn>Thumbnail Label:</digi:trn><font color="red"></font>
							</td>
							<td> 
                            	<html:text property="tempContentThumbnailLabel"/>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center"> 
	                        <input type="button" class="buttonx" onclick="upload()" value="<digi:trn>Upload File</digi:trn>"/>&nbsp;
							</td>
						</tr>
						</tr>
					</table>
				<br />
				<br />
                </td>
              </tr>

            </table></td>
        </tr>
      </table>
		<div align="center">
		<table>
			<tr>
				<td>
					<html:submit styleClass="buttonx" styleId="saveContent"><digi:trn>Save</digi:trn></html:submit>
				</td>
				<td>
					<html:submit styleClass="buttonx" styleId="saveContent" onclick="return cancel()"><digi:trn>Cancel</digi:trn></html:submit>
				</td>
			</tr>
		</table>
		<%-- <html:submit styleClass="buttonx" styleId="saveContent"><digi:trn>Save</digi:trn></html:submit> --%>
		</div>
      </td>
  </tr>
</table>
<br />
<br />
<html:hidden property="htmlblock_1"></html:hidden>
<html:hidden property="htmlblock_2"></html:hidden>
<html:hidden property="editKey"></html:hidden>


<script language="javascript">
$(document).ready( function() {
	$('input:radio[name=contentLayout]').each( function(){
		$(this).click(function () {
			//When clicking the radio buttons hide every div with layout
			$('div[name=layoutGroup]').hide();
			//Show the selected one
			$('#layout_'+$(this).val()).show();
			
		});
	});

	//Search all editor links and adapt them
	$('div[name=layoutGroup]').find("A:contains('Edit HTML')").each( function(){
		$(this).text("<digi:trn jsFriendly='true'>Edit</digi:trn>");
		//Extract editKey
		var href = $(this).attr("href");
		href = href.substring(href.indexOf("?id=")+4, href.length)
		editKey = href.substring(0, href.indexOf("&"));
		$(this).attr("href", "javascript:edit('" + editKey + "');");
	});
	try
	{
		setStripsTable("dataTable", "tableEven", "tableOdd");
		setHoveredTable("dataTable", true);
	}
	catch(e) {}
});
</script>
<script  type="text/javascript" src="<digi:file src="module/aim/scripts/fileUpload.js"/>"></script>
<script language="javascript">

function edit(key) {
	document.contentForm.action = "/content/contentManager.do?action=add";
	document.contentForm.target = "_self";
	document.contentForm.editKey.value = key;
	document.contentForm.submit();
}

function upload() {
	if(validateUpload()){
		document.contentForm.action = "/content/contentManager.do?action=upload";
		document.contentForm.target = "_self";
		document.contentForm.submit();
	}
}
function doAction(index, action, confirmation) {
	if(confirmation){
		var ret = confirm("<digi:trn jsFriendly='true'>Are you sure?</digi:trn>");
		if (!ret) return false; 
	}
	document.contentForm.action = "/content/contentManager.do?action=" + action +"&index=" + index;
	document.contentForm.target = "_self";
	document.contentForm.submit();
}

function cancel()
{
	var subForm				= document.forms["contentForm"];
	subForm.action			= "/content/contentManager.do";
	subForm.submit();
}

function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	if (tableElement) {
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
	if($("input[name=title]").val() == ""){
		strError = "<digi:trn jsFriendly='true'>Title</digi:trn>\n";
	}
	if($("input[name=pageCode]").val() == ""){
		strError = strError + "<digi:trn jsFriendly='true'>Page Code</digi:trn>\n";
	}
	if (strError != ""){
		alert("<digi:trn jsFriendly='true'>Please complete the following fields:</digi:trn>\n" + strError);
		return false;
	}
	return true;
}
function validateUpload(){
	if($("input[name=tempContentThumbnail]").val() == ""){
		alert("<digi:trn jsFriendly='true'>Please select thumbnail to upload</digi:trn>\n");
		return false;
	}
	return true;
}
initFileUploads('<digi:trn jsFriendly="true" key="aim:browse">Browse...</digi:trn>');
var enterBinder	= new EnterHitBinder('saveContent');
</script>
</digi:form>
