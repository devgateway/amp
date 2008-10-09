<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<%@page import="org.digijava.module.help.util.HelpUtil"%>

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

<script type="text/javascript">
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn key="aim:browse">Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}
	}
</script>

<digi:instance property="helpForm" />
<script language="JavaScript">
  	function exp() {
        	<digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=export" />
			helpForm.action="${url}";
  			helpForm.submit();
  }
  
  	function imp(){
  			<digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=importing" />
			helpForm.action="${url}";
  			helpForm.submit();
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
	
</script>
<style type="text/css">

.silverThing {background-color:silver; }
.whiteThing { background-color: #FFF; }

</style>
<digi:form action="/helpActions.do" method="post" enctype="multipart/form-data">
<digi:instance property="helpForm" />
<table bgColor=#ffffff cellPadding=2 cellSpacing=2 width=772 border="0">
	<tr>
	<!-- Start Navigation -->
		<td  height=33 colspan="2" ><span class=crumb>
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
		</td>
	<!-- End navigation -->
	</tr>
		<tr>
			<td colspan="2" >
				 <span class=subtitle-blue>
                        <digi:trn key="aim:helptopicsadminHeader">
                      		 Help Topics Admin
                        </digi:trn>
                </span>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>&nbsp;
	<tr>
		<td align="left"  colspan="2" >
				<input type="button" onclick="exp()" value='<digi:trn key="aim:translationmanagerexportbutton">Export</digi:trn>'/>
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
  			<input type="button" onclick="imp()" value='<digi:trn key="aim:translationmanagerimportbutton">Import</digi:trn>'/>
<!--		<input id="fileUploaded" name="fileUploaded" type="file" class="file"/>-->
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
	
</table>
 </digi:form>
 
 <script type="text/javascript">
	initFileUploads();
	if ( document.crDocumentManagerForm.pageCloseFlag.value == "true" ) {
			window.opener.location.replace(window.opener.location.href); 
			window.close();
		}
</script>