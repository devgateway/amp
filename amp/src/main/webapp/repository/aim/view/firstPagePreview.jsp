<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="org.digijava.module.categorymanager.util.CategoryConstants"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp"  />
<%@include file="addThumbnailPanel.jsp" %>
<digi:context name="displayThumbnail" property="context/aim/default/displayThumbnail.do" />
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
	-moz-opacity:0 ;
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
div.fakefile2 input{
	width: 83px;
}
-->
</style>
<script type="text/javascript">

function configPanel(panelNum){
	var title ='\n<digi:trn jsFriendly="true">Upload Thumbnails</digi:trn>';
	setPanelHeader(0, title);
	setPanelFooter(0, "");
	}

function validateAddThumbnail() {
	//alert( document.forms['crDocumentManagerForm'].fileData.value );
	var trnmsg ='\n<digi:trn jsFriendly="true">Please select a thumbnail to upload !</digi:trn>';
	var msg	= '';
	if (document.forms['aimWelcomePageForm'].thumbnail.value == ''){
		msg = msg + trnmsg ;
	}
	//document.forms['crDocumentManagerForm'].docDescription.value = escape(document.forms['crDocumentManagerForm'].docDescription.value);
	document.getElementById('uploadThumbnailErrorHolderDiv').innerHTML	= msg;
	if (msg.length == 0)
			return true;
	return false;	
}


function addThumbnail(){
	var ret = false;
	if(validateAddThumbnail() == true){
		hidePanel(0);
		ret = true;
	}
	return ret;
}

function deleteThumbnail(){
	var msg='<digi:trn jsFriendly="true">This action will remove all the data related to the thumbnail. Are you sure?</digi:trn>';
    if (confirm(msg)) {
    	document.aimWelcomePageForm.action = document.aimWelcomePageForm.action+"?action=deleteThumbnail";
		document.aimWelcomePageForm.target = "_self";
    	document.aimWelcomePageForm.submit();
	} 
}

   function downloadFile(placeholder) {
        if (placeholder != '0') {
            window.location='/aim/downloadFileFromHome.do?placeholder='+placeholder;
        }
    }

    function attachFuncToThumbnail(placeholder) {
        var id="displayThumbnail"+placeholder;
        var lastTimeStamp = new Date().getTime();
        var url='/aim/displayThumbnail.do?placeholder='+placeholder+'&relDocs=relDocs'+'&timestamp='+lastTimeStamp;
        $.get(url, function(data) {
            if(data!='true'){
                $("#"+id).click(function() {
                    var msg='<digi:trn jsFriendly="true">No related documents to download!</digi:trn>';
                    alert(msg);
                });
            }
            else{
                $("#"+id).click(function() {
                    downloadFile(placeholder);
                });

            }
        });
    }

</script>


<table width="100%" >
	<tr>
		<td  width="5%" />
		<td  width="60%" >
			<c:set var="lblEditText"><digi:trn>Edit Text</digi:trn></c:set>
			<digi:edit key="um:welcomeAmp" displayText="${lblEditText}"></digi:edit>
		</td>
		<td  width="5%" />
		<td width="70%" bgcolor="#dbe5f1">
			<table width="100%" height="400" cellpadding="3" cellspacing="0" >
			   <c:forEach var='placeholder' begin='1' end='2'>
			    <tr height="47%">
			        <td valign="middle" align="center" >
						<a style="cursor: pointer">
                            <img id="displayThumbnail${placeholder}" src="${displayThumbnail}?placeholder=${placeholder}" align="middle" border="0" height="150" width="230" onload="attachFuncToThumbnail(${placeholder})">
						</a>
                    </td>
				</tr>
              </c:forEach>
				<tr height="6%">
			        <td>
<!--			        	<button type="button" class="dr-menu buton" onClick="showMyPanel(0, 'uploadThumbnailDiv'); ">-->
					  		<a href="javascript:"  onClick=" configPanel(0); showMyPanel(0, 'uploadThumbnailDiv');">
					  		<digi:trn key="uploadThumbnails">
					 	    	       Upload Thumbnails   				
					  		</digi:trn>            
<!--				    	</button>-->
				    </td>
				</tr>
			</table>		

			<div id="uploadThumbnailDiv" class="invisible-item">
				<div align="center">
				<div id="uploadThumbnailErrorHolderDiv" style="font-size:11px; color: red"></div>
				<digi:form action="/uploadThumbnail.do" method="post" enctype="multipart/form-data" >
					<table cellpadding="3" cellspacing="3" border="0">
						<tr id="tr_path_thumbnail">
						<td><strong><digi:trn key="selectThumbnail">Select Thumbnail to upload:</digi:trn><font color="red"></font></strong></td>
						<td>
							<div class="fileinputs"> 
								<input id="thumbnail" name="thumbnail" type="file" class="file">
							</div>
						</td>
						</tr>
						<tr id="tr_path_optional">
						<td><strong><digi:trn>Select Optional File to upload:</digi:trn><font color="red"></font></strong></td>
						<td>
						<div class="fileinputs"> 
								<input id="optionalFile" name="optionalFile" type="file" class="file">
								
							</div>
						</td>
						</tr>
						<tr>
							<td> 
								<strong><digi:trn key="firstPlaceholder">First Placeholder</digi:trn></strong>
								<input name="placeholder" type="radio" value="1" checked="checked" />
							</td>
							<td> 
								<strong><digi:trn key="secondPlaceholder">Second Placeholder</digi:trn></strong>
								<input name="placeholder" type="radio" value="2" />
							</td>
						</tr>
						
						<tr>
							<td> 
								<strong><digi:trn key="selectOptionalFile">Thumbnail Label:</digi:trn><font color="red"></font></strong>
							</td>
							<td colspan="2"> 
								<input name="thumbnailLabel" type="text" style="width: 300"/>
							</td>
						</tr>
						
						<tr align="center">
							<td colspan="3">
								<table>
									<tr align="center">
										<td align="center">
											&nbsp;
											<html:submit styleClass="dr-menu buton" style="padding-bottom: 2px; padding-top: 2px;" onclick="return addThumbnail()"><digi:trn key="submit">Submit</digi:trn></html:submit>&nbsp;
										</td>
										<td align="center">
											&nbsp;
											<button class="dr-menu buton" type="button" style="padding-bottom: 2px; padding-top: 2px;"  
											onClick="hidePanel(0)">
												<digi:trn key="cancel">Cancel</digi:trn>
											</button>&nbsp;
										</td>
										<td align="center">
											&nbsp;
											<button class="dr-menu buton" type="button" style="padding-bottom: 2px; padding-top: 2px;" onclick="return deleteThumbnail()"><digi:trn key="delete">Delete</digi:trn></button>&nbsp;
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</digi:form>
				</div>			        
		    </div>
		    
		</td>
	<tr>
<table>
<script  type="text/javascript" src="<digi:file src="module/aim/scripts/fileUpload.js"/>"></script>
<script type="text/javascript">
initFileUploads();
</script>
