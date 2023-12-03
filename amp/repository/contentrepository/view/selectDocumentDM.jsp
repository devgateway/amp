<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>

<digi:instance property="crSelectDocumentForm" />

<bean:define id="myForm" name="crSelectDocumentForm" toScope="request" type="org.digijava.ampModule.contentrepository.form.SelectDocumentForm" />
<bean:define id="teamForm" name="myForm" property="teamInformationBeanDM" toScope="page" />
<bean:define id="isTeamLeader" name="teamForm" property="isTeamLeader" />
<bean:define id="meTeamMember" name="teamForm" property="meTeamMember" />
<bean:define id="tMembers" name="teamForm" property="myTeamMembers" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script> 
  		  
		
<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
	
<%@include file="documentManagerJsHelper.jsp" %>

<script type="text/javascript">
	function afterPageLoad(e) {		
		
		var showTheFollowingDocuments 	= 'ALL';
		<c:if test="${myForm.showTheFollowingDocuments!=null}">
			showTheFollowingDocuments		= '${myForm.showTheFollowingDocuments}';
		</c:if>
	    var select = '<digi:trn jsFriendly="true" key="contentrepository:SelectDocumentsTitle">Select Documents</digi:trn>';
	    var windowHandler;
	    if(showTheFollowingDocuments=='PUBLIC'){
	    	windowHandler	= newWindow( select,false,'otherDocumentsDiv');
	    }else{
	    	windowHandler	= newWindow( select,true,'otherDocumentsDiv');	    	
	    }		
		
		if (showTheFollowingDocuments == 'PUBLIC') {
			windowHandler.populateWithPublicDocs();
		}
	}
	YAHOO.util.Event.on(window, "load", afterPageLoad);
	
	var enterBinder	= new EnterHitBinder('addDocFromRepBtn');
	
	var enterBinder	= new EnterHitBinder('addDocFromRepBtn');
</script>	

<digi:ref href="css_2/desktop_yui_tabs.css" type="text/css" rel="stylesheet" /> 
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<digi:ref href="css/container.css" type="text/css" rel="stylesheet" />
<link href='/TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>

<style>
.yui-skin-sam a.yui-pg-page{
margin-left: 2px;
padding-right: 7px;
font-size: 11px;
border-right: 1px solid rgb(208, 208, 208);
}

.yui-skin-sam .yui-pg-pages{
border: 0px;
padding-left: 0px;
}
.yui-pg-current-page {
    background-color: #FFFFFF;
    color: rgb(208, 208, 208);
    padding: 0px;
}
.current-page {
    background-color: #FF6000;
    color: #FFFFFF;
    padding: 2px;
    font-weight: bold;
}

.yui-skin-sam span.yui-pg-first,
.yui-skin-sam span.yui-pg-previous,
.yui-skin-sam span.yui-pg-next,
.yui-skin-sam span.yui-pg-last {
display: none;
}

.yui-skin-sam a.yui-pg-first {
margin-left: 2px;
padding-right: 7px;
border-right: 1px solid rgb(208, 208, 208);
}

.resource_box {
    background-color: #FFFFFF;
    border: 1px solid #CCCCCC;
    font-size: 12px;
    padding: 10px;
}
</style>



<div id="otherDocumentsDiv" class="yui-skin-sam">&nbsp;</div>
<input type="hidden" name="type" id="typeId"/>
<html:button  styleClass="dr-menu" property="submitButton" onclick="saveSelectedDocuments()" styleId="addDocFromRepBtn">
	<digi:trn>Submit this</digi:trn>
</html:button>
<br /><br />
&nbsp;&nbsp;
<c:if test="${myForm.showTheFollowingDocuments=='ALL'}">
	<c:set var="translation">
			<digi:trn>Click here to open a new document window</digi:trn>
	</c:set>
	<a title="${translation}" style="cursor:pointer; text-decoration:underline; color: blue; font-size: x-small" onClick="newWindow('<digi:trn jsFriendly="true">Select Documents</digi:trn>', true, 'otherDocumentsDiv')" /> 
		<digi:trn>New window</digi:trn>
	</a>
</c:if>

<%@include file="documentManagerDivHelper.jsp" %>	