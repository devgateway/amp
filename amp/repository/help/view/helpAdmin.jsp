<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<%@page import="org.digijava.module.help.util.HelpUtil"%>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="helpForm" />
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
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
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
	
<digi:instance property="helpForm" />
<script language="JavaScript">

function IsEmpty(){
 	filed  = document.getElementById("fileUploaded").value;
		   if (filed.length==0) {
		      return false;
		   }
		   else { return true; }
}

  	function exp() {
        	<digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=export" />
			helpForm.action="${url}";
  			helpForm.submit();
  }
  
  	function imp(){
  		if(IsEmpty() == false){
  	  		
				alert("The content of the imported file is not ok. Please import a .zip file exported from this menu.");
  	  		}else{
	  			<digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=importing" />
				helpForm.action="${url}";
	  			helpForm.submit();
  	  		}
  		}

	function editTopic(topic,heltType){
			if(heltType == "admin"){
				openNewWindow(400,300);
				<digi:context name="edit" property="context/module/moduleinstance/../help~admin/helpActions.do?actionType=editHelpTopic"/>
				document.helpForm.action = "<%= edit %>&topicKey="+topic+"&wizardStep=0&page=admin";
				document.helpForm.target = popupPointer.name;
				document.helpForm.submit();
			
			}else{
				openNewWindow(400,300);
				<digi:context name="edit" property="context/module/moduleinstance/helpActions.do?actionType=editHelpTopic"/>
				document.helpForm.action = "<%= edit %>&topicKey="+topic+"&wizardStep=0&page=admin";
				document.helpForm.target = popupPointer.name;
				document.helpForm.submit();
				}
	}
		

	function toggleDiv(id,state){
		if (state==true){
			document.getElementById('uncollapse'+id).style.display='block';
			document.getElementById('collapse'+id).style.display='none';
		}
		if (state==false){
			document.getElementById('collapse'+id).style.display='block';
			document.getElementById('uncollapse'+id).style.display='none';
		}
	}

	 function expandProgram(progId){
	 	var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).show();
		$(imgId).hide();
		$(divId).show('fast');
	}       
	
	function collapseProgram(progId){
	
		var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).hide();
		$(imgId).show();
		$(divId).hide('fast');
	}
	
	 function expandHelp(progId){
	 	var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).show();
		$(imgId).hide();
		$(divId).show('fast');
	}       
	
	function collapseHelp(progId){
	
		var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).hide();
		$(imgId).show();
		$(divId).hide('fast');
	}

	function changeRelatedCheckboxesState(topicId, parentTopicIdPrefix){
		//get current chekcbox
		var currentChkboxId=''+parentTopicIdPrefix+topicId;
		var currentChkBox=document.getElementById(currentChkboxId);
		//if checked
		if(currentChkBox.checked==true){
			//if this checkbox has sub-checkboxes,all they should be selected
			checkOrUncheckAllSubCheckboxes(topicId,parentTopicIdPrefix);			
		}
		//if unchecked
		if(currentChkBox.checked==false){
			//if this checkbox has sub-checkboxes,all they should be deselected
			checkOrUncheckAllSubCheckboxes(topicId,parentTopicIdPrefix);
			//if this checkbox has parent, then unchecking should lead to unchecking parent checkbox too.
			var parentTopicId= parentTopicIdPrefix.substring(parentTopicIdPrefix.indexOf('_')+1, parentTopicIdPrefix.lastIndexOf('_')); // parentTopicIdPrefix looks like "chekcbox_0_"
			if(parentTopicId !='0'){				
				uncheckParentCheckbox(parentTopicId);
			}
		}
	}

	function checkOrUncheckAllSubCheckboxes(topicId,parentTopicIdPrefix){		
		var children= $("input[@id^='checkbox_"+topicId+"_']");
		if(children!=null){
			//get current checkbox for which the function was called
			var checkboxId=''+parentTopicIdPrefix+topicId;
			var chkBox=document.getElementById(checkboxId);
			for(var i=0;i<children.length;i++){			
				if(chkBox.checked==false){
					children[i].checked=false;
				}else{
					children[i].checked=true;
				}
				//if this child topic has children itself, then they also should be checked/unchecked
				var childChkBoxId=children[i].id;
				var tId=childChkBoxId.substring(childChkBoxId.lastIndexOf('_')+1); // child topic's id(will be used as parentId parameter)
				var topicsIdPrefix='checkbox_'+topicId+'_';
				if($("input[@id^='checkbox_"+tId+"_']")!=null && $("input[@id^='checkbox_"+tId+"_']").length>0){
					checkOrUncheckAllSubCheckboxes(tId,topicsIdPrefix);
				}
			}
		}		
	}

	function uncheckParentCheckbox(parentTopicId){		
		while(parentTopicId!='0'){
			var parent=$("input[@id$='"+parentTopicId+"']")[0];
			parent.checked=false;
			parentTopicId=getParenTopictId(parent.id);			
		}		
	}

	function getParenTopictId(checkBoxId){		
		var parentTopicId=checkBoxId.slice(checkBoxId.indexOf('_')+1, checkBoxId.lastIndexOf('_'));
		return parentTopicId;
	}

	function deleteMessages(){
		if(deleteMsgs()){
			 var chk=document.getElementsByTagName('input');
	         var tIds='';
	         for(var i=0;i<chk.length;i++){
	             if(chk[i].type == 'checkbox'&&chk[i].checked){
	            	 tIds+=chk[i].value+',';
	             }
	         }
	        if(tIds.length>0){
	        	tIds=tIds.substring(0,tIds.length-1);
	        	<digi:context name="deleteMsgs" property="context/module/moduleinstance/helpActions.do?actionType=deleteHelpTopics"/>
	    		document.helpForm.action = "<%=deleteMsgs %>&multi="+true+"&tIds="+tIds+"&page=admin";
	    		document.helpForm.target = "_self";
	    		document.helpForm.submit();	
	        }else{
	            alert('Please select at least one topic to be deleted');
	            return false;
	        }
		}	
	}

	function deleteMsgs(){
		return confirm("Are You Sure You Want To Remove Selected Topics ?");
	}

	function selectAll(){
		var allChkboxes=$('input:checkbox');
		if(allChkboxes!=null && allChkboxes.length>0){
			for(var i=0;i<allChkboxes.length;i++){
				allChkboxes[i].checked=true;
			}
		}
	}
	function deselectAll(){
		var allChkboxes=$('input:checkbox');
		if(allChkboxes!=null && allChkboxes.length>0){
			for(var i=0;i<allChkboxes.length;i++){
				allChkboxes[i].checked=false;
			}
		}
	}
</script>
<style type="text/css">

.silverThing {background-color:silver; }
.whiteThing { background-color: #FFF; }

</style>
<center>
<digi:form action="/helpActions.do" method="post" enctype="multipart/form-data">
<table bgColor="#ffffff" cellPadding="2" cellSpacing="2" width="772" border="0" align="left">
	<!--<tr> -->
	<!-- Start Navigation -->
		<!-- <td  height=33 colspan="2" ><span class=crumb>
			<c:set var="translation">
				<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
			</c:set>
			<digi:link href="../../aim/admin.do" styleClass="comment" title="${translation}" >
			<digi:trn key="aim:AmpAdminHome">
				Admin Home
			</digi:trn>
			</digi:link>&nbsp;&gt;&nbsp;
			<digi:trn key="aim:helptopicsadmin">
				Help Topics Admin
			</digi:trn>
		</td>-->
	<!-- End navigation -->
	<!--</tr>-->
		<tr>
			<td colspan="2" >
				         <h1 class="admintitle" style="text-align:left;">
                        <digi:trn key="aim:helptopicsadminHeader">
                      		 Help Topics Admin
                        </digi:trn>
                </h1>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>&nbsp;
	<tr>
		<td align="left"  colspan="2" >
				<input type="button" onclick="exp()" value='<digi:trn jsFriendly="true" key="aim:translationmanagerexportbutton">Export</digi:trn>'/>
				<hr>
		</td>
	</tr>
	<tr>
		<td width="100%">
		 <c:if test="${not empty helpForm.helpErrors}">
                <c:forEach var="error" items="${helpForm.helpErrors}"> <font color="red">${error}</font><br />
                </c:forEach>
              </c:if>
       </td>
    </tr>
	<tr align="left"><td>
		<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">
				<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
					<input id="fileUploaded" name="fileUploaded" type="file" class="file"/>
				</div>
			</a>
	</td></tr>
	<tr>
		<td align="left">
  			<input type="button" onclick="imp()" value='<digi:trn jsFriendly="true" key="aim:translationmanagerimportbutton">Import</digi:trn>'/>
<!--		<input id="fileUploaded" name="fileUploaded" type="file" class="file"/>--> <digi:errors/>
	    </td>
	</tr>
	 <tr id="img_-1" onclick="expandHelp(-1);"  src="/ampTemplate/images/tree_plus.gif"/>
		<td colspan="2" bgcolor="silver">
			<digi:trn key="aim:amphelp">AMP Help</digi:trn>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table id="imgh_-1"  width="772"  style="display: none;" border="0">
				<tr>
					<td colspan="2" bgcolor="silver" onclick="collapseHelp(-1)">
							<digi:trn key="aim:amphelp">AMP Help</digi:trn>
					</td>
				</tr>
				<tr>
					<td colspan="2">
								<bean:define id="tree" name="helpForm" property="topicTree" type="java.util.Collection"/>
								<%= HelpUtil.renderSelectTopicTree(tree,"default",request) %>		
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr id="img_-2" onclick="expandHelp(-2);"  src="/ampTemplate/images/tree_plus.gif"/>
		<td colspan="2" bgcolor="silver">
			<digi:trn key="aim:ampadminhelp">AMP Admin Help</digi:trn>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table id="imgh_-2"  width="772"  style="display: none;" border="0">
				<tr>
					<td colspan="2" bgcolor="silver" onclick="collapseHelp(-2)">
							<digi:trn key="aim:ampadminhelp">AMP Admin Help</digi:trn>
					</td>
				</tr>
				<tr>
					<td colspan="2">
								<bean:define id="tree" name="helpForm" property="adminTopicTree" type="java.util.Collection"/>
								<%= HelpUtil.renderSelectTopicTree(tree,"admin",request) %>		
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr id="img_-3" onclick="expandHelp(-3);"  src="/ampTemplate/images/tree_plus.gif"/>
		<td colspan="2" bgcolor="silver">
			<digi:trn>Glossary</digi:trn>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table id="imgh_-3"  width="772"  style="display: none;" border="0">
				<tr>
					<td colspan="2" bgcolor="silver" onclick="collapseHelp(-3)">
							<digi:trn>Glossary</digi:trn>
					</td>
				</tr>
				<tr>
					<td colspan="2">
								<bean:define id="glosTree" name="helpForm" property="glossaryTree" type="java.util.Collection"/>
								<%= HelpUtil.renderSelectTopicTree(glosTree,"default",request) %>		
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td colspan="2" align="right">
			<input type="button" onclick="selectAll()" value="<digi:trn>Select All</digi:trn>" class="dr-menu" />
			<input type="button" onclick="deselectAll()" value="<digi:trn>Deselect All</digi:trn>" class="dr-menu" />
			<input type="button" onclick="deleteMessages()" value="<digi:trn>Delete Selected Messages</digi:trn>" class="dr-menu" />
		</td>
	</tr>
	
</table>
 </digi:form>
 </center>
 <script  type="text/javascript" src="<digi:file src="module/aim/scripts/fileUpload.js"/>"></script>
 <script type="text/javascript">
	initFileUploads('<digi:trn jsFriendly="true" key="aim:browse">Browse...</digi:trn>');
</script>