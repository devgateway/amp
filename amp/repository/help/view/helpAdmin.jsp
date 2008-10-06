<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<%@page import="org.digijava.module.help.util.HelpUtil"%>

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
	<tr>
		<td align="right"  colspan="2" >
				<input type="button" onclick="exp()" value="Export"/>
		</td>
	</tr>
	<tr>
		<td width="100%">
		 <c:if test="${not empty helpForm.helpErrors}">
                <c:forEach var="error" items="${helpForm.helpErrors}"> <font color="red">${error}</font><br />
                </c:forEach>
              </c:if>
       </td>
  		<td align="right">
  			 <digi:form action="/helpActions.do?actionType=importing" method="post" enctype="multipart/form-data">
							  	<html:submit style="dr-menu" value="Import" property="import"/>
							 <br>
				 	           <input id="fileUploaded" name="fileUploaded" type="file" class="file"/>
			 </digi:form>
 	    </td>
    </tr>
	 <tr id="img_1" onclick="expandHelp(1);"  src="/ampTemplate/images/tree_plus.gif"/>
		<td colspan="2" bgcolor="silver">
			AMP Help
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table id="imgh_1"  width="772"  style="display: none;" border="0">
				<tr>
					<td colspan="2" bgcolor="silver" onclick="collapseHelp(1)">
							AMP Help
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
	<tr id="img_2" onclick="expandHelp(2);"  src="/ampTemplate/images/tree_plus.gif"/>
		<td colspan="2" bgcolor="silver">
			AMP Admin Help
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table id="imgh_2"  width="772"  style="display: none;" border="0">
				<tr>
					<td colspan="2" bgcolor="silver" onclick="collapseHelp(2)">
							AMP Admin Help
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
