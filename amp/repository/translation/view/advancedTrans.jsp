<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/displaytag" prefix="display" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language=JavaScript type=text/javascript>
<digi:context name="updateMsg" property="context/module/moduleinstance/updateMessage.do" />

function fnOnSearch() {
	<digi:context name="searchMsg" property="context/module/moduleinstance/showAdvancedTranslation.do" />
	var form   = document.forms['advancedTranslationForm'];
	form.action = "<%=searchMsg%>";
        form.submit();
        return true;

}

function onChangeList(paramName, paramValue) {
	<digi:context name="changeTLang" property="context/module/moduleinstance/showAdvancedTranslation.do" />
	var form  = document.forms['advancedTranslationForm'];
	var query = "<%=changeTLang%>?selected"+paramName+"="+paramValue+"&showExpired="+form.showExpired.checked;

	document.location.href = query;

}

function saveAll() {
	<digi:context name="saveAllAction" property="context/module/moduleinstance/saveAllMessages.do" />
	
	var form   		= document.forms['advancedTranslationForm'];
	var targetValue = '';
	var targetObj 	= null;
	var pageId 		= new Number(getPageId());
	var start		= 1;
	var offset		= 25;

	if (pageId > 1) {
		start = 25 * (pageId-1) + 1;
		offset = start + 24;
	}

	for (var i=start; i<=offset; i++) {

		targetObj = form['message['+i+'].targetValue'];

		if (targetObj != 'undefined' && targetObj && targetObj.value) {
			if (form['messageOrig['+i+'].targetValue']) {
				var origValue   = form['messageOrig['+i+'].targetValue'].value;
				var targetValue = targetObj.value;
				if (origValue == targetValue) {
					form['message['+i+'].key'].value = '';
				}
			}
		}

	}
	
	form.action = "<%=saveAllAction%>"+ "?d-1338053-p="+pageId+"&showExpired="+form.showExpired.checked;
	
	return true;
	
}


function fnOnUpdateMessage(transId) {

	if (!transId) {
		transId = '';
	}

	var key  = '';
	var form = document.forms['advancedTranslationForm'];

	if (transId && (key=getKey(form, transId))) {
	  form.action = getFormAction(form, key);
	  return true;
	}

	return false;

}

function getKey(form, transId) {
	var key    = '';
	var keyObj = form['message['+transId+'].key'];
	if (keyObj != 'undefined' && keyObj && (key=trim(keyObj.value))) {
		return key;
	}
	return '';
}

function expireOneKey(transId) {

	var form = document.forms['advancedTranslationForm'];
	var key  = getKey(form, transId);
	if (key) {
		form.action = getFormAction(form, key);
		return true;
	}
	return false;
}

function expireKeys() {

	var form     = document.forms['advancedTranslationForm'];
	var keyObj   = form.selectedKeys;
	var selected = false;
	var length   = keyObj.length;

	if (keyObj != 'undefined') {
		if (length != 'undefined' && length>0) {
			for (var i=0; i<keyObj.length; i++) {
				if (keyObj[i].checked) {
					selected = true;
					break;
				}
			}
		} else {
			if (keyObj.value != null && keyObj.checked) {
			 	selected = true;
			}
		}
	}

	if (!selected) {
		alert('<digi:trn jsFriendly="true" key="translation:keysNotSelected">Please select at least one key</digi:trn>');
		return false;
	}

	form.action = getFormAction(form, '');

	return true;

}

function getFormActionURL(form){
	var path  = document.location.href;
    var param = '';

	if (path.indexOf("d-1338053-p") != -1) {

  	  param = path.substring(path.indexOf("d-1338053-p"));

  	  if (param.indexOf("&") != -1) {
  	  	  param = param.substring(0, param.indexOf("&"));
  	  }

	} else {
		param = 'd-1338053-p=1';
	}

	return "<%=updateMsg%>?"+param+"&showExpired="+form.showExpired.checked;

}


function getFormAction(form, key) {

	return getFormActionURL(form) + "&key="+key;
	
}

function getPageId() {

	var path  = document.location.href;
    var param = '';
	var keyValue = new Array();

	if (path.indexOf("d-1338053-p") != -1) {

  	  param = path.substring(path.indexOf("d-1338053-p"));

  	  if (param.indexOf("&") != -1) {
  	  	  param = param.substring(0, param.indexOf("&"));
  	  }

  	  keyValue = param.split("=");
  	  param = keyValue[1];

	} else {
		param = 1;
	}

	return param;

}

function onShowExpired() {
	<digi:context name="changePref" property="context/module/moduleinstance/showAdvancedTranslation.do" />
	var szQuery = "<%=changePref%>?selectedPrefix="+document.advancedTranslationForm.selectedPrefix.value+"&showExpired="+document.advancedTranslationForm.showExpired.checked;
	document.location.href = szQuery;
}

function fnOnDeleteKey(key) {
	<digi:context name="deleteKey" property="context/module/moduleinstance/deleteKey.do" />
	document.advancedTranslationForm.action = "<%=deleteKey%>?key="+key;
	alert("<%=deleteKey%>?key="+key);
	return;
	document.advancedTranslationForm.submit();
}

function fnOnSwitchToGlobal() {
	<digi:context name="switchToGlobal" property="context/module/moduleinstance/showAdvancedTranslation.do" />
	document.advancedTranslationForm.action = "<%=switchToGlobal%>?switchToGlobal=true";
	document.advancedTranslationForm.submit();
}

function fnOnSwitchToSite() {
	<digi:context name="switchToSite" property="context/module/moduleinstance/showAdvancedTranslation.do" />
	document.advancedTranslationForm.action = "<%=switchToSite%>?switchToGlobal=false";
	document.advancedTranslationForm.submit();
}

function fnOnAddKey() {
	<digi:context name="addKey" property="context/module/moduleinstance/addKey.do" />
	document.advancedTranslationForm.action = "<%=addKey%>";
	document.advancedTranslationForm.submit();
}

function Next(param) {
	<digi:context name="next" property="context/module/moduleinstance/showAdvancedTranslation.do" />
	document.advancedTranslationForm.action = "<%=next%>?nav="+param;
	document.advancedTranslationForm.submit();
}

function Prev(param) {
	<digi:context name="prev" property="context/module/moduleinstance/showAdvancedTranslation.do" />
	document.advancedTranslationForm.action = "<%=prev%>?nav="+param;
	document.advancedTranslationForm.submit();
}

function gotoNavigation (actionUrl) {
	window.location.href=actionUrl;
}

function showHideAddNewKey() {

	var linkDiv = document.getElementById('addnewkeylink');
	var div = document.getElementById('addkeydiv');

	if (div.style.display == 'none') {
		div.style.display = '';
		linkDiv.innerHTML = '<digi:trn jsFriendly="true" key="translation:hideAddNewKey">Hide add new key</digi:trn>';
	} else {
		div.style.display = 'none';
		linkDiv.innerHTML = '<digi:trn jsFriendly="true" key="translation:addNewKey">Add new key</digi:trn>';
	}
	return false;
}

function setTextareaHeights() {

	var form   		= document.forms['advancedTranslationForm'];
	var targetValue = '';
	var targetObj 	= null;
	var pageId 		= new Number(getPageId());
	var start		= 1;
	var offset		= 25;

	if (pageId > 1) {
		start = 25 * (pageId-1) + 1;
		offset = start + 24;
	}

	for (var i=start; i<=offset; i++) {

		targetObj = form['message['+i+'].targetValue'];

		if (targetObj != 'undefined' && targetObj && (targetValue=trim(targetObj.value))) {
			targetObj.rows = Math.max((targetValue.length / 26), 3);
		}

	}

}

function trim (str) {
	retVal = str;
	if (retVal != null && retVal.length > 0) {
		//From start
		while (retVal.substring (0,1) == " " ||
			   retVal.substring (0,1) == "\n" ||
			   retVal.substring (0,1) == "\r" ||
			   retVal.substring (0,1) == "\t") {
			retVal = retVal.substring (1, retVal.length);
		}

		//From end
		while (retVal.substring (retVal.length - 1, retVal.length) == " " ||
			   retVal.substring (retVal.length - 1, retVal.length) == "\n" ||
			   retVal.substring (retVal.length - 1, retVal.length) == "\r" ||
			   retVal.substring (retVal.length - 1, retVal.length) == "\t") {
			retVal = retVal.substring (0, retVal.length - 1);
		}
	}
	return retVal;
}

function init() {
	setTextareaHeights();
	cleanUpSearchFields();
	cleanUpAddKeyFields();
}

function cleanUpSearchFields() {
	var form   = document.forms['advancedTranslationForm'];
	form.searchKey.value = '';
	form.searchTarget.value = '';
	form.searchSource.value = '';
}

function cleanUpAddKeyFields() {
	var form   = document.forms['advancedTranslationForm'];
	form.key.value = '';
	form.messageText.value = '';
}


	<%-- flag for indicating changes in textareas--%>
	var textChanged = false;

	$(document).ready(function(){

		<%-- set change handler for all textareas --%>
		$("textarea").bind('keypress',function(){
			textChanged=true;
		});
		
		<%-- set click handler for document --%>
		$(document).bind('click',function(e){
			var myEvent=e;
			if (myEvent==null) myEvent=event;
			var target=myEvent.target;
			<%-- check if link was clicked --%>
			var tagName=target.tagName.toLowerCase();
			if (tagName=='a' || (tagName!='a' && target.parentNode.tagName.toLowerCase()=='a')){ <%-- || (target.tagName.toLowerCase()=='input' && (target.type.toLowerCase()=='button' || target.type.toLowerCase()=='submit'))){ --%>
				<%-- if any textarea has been changed then ask user --%>
				if (textChanged==true){
					var exitWithoutSave=confirm('One or more translations has been changed, do you want to leave page without saving?');
					if (exitWithoutSave) return true;
					return false;
				}
			}
		    return true;
		  });
		
	});



</script>

<style type="text/css">
table.trnrow thead tr {
  background-color: #FFFFFF;
}
table.trnrow tr.even {
  background-color: #EFEFEF;
}
table.trnrow {
	border: 1px solid #ccc;
	border-collapse: collapse;
}
</style>

<body onload="init()">

<digi:errors/>
<digi:form action="/saveAllMessages.do" method="post">

<digi:context name="indexUrl" property="context" />
<digi:secure globalAdmin="true">
	<c:set var="globalAdminAccount" value="true" />
</digi:secure>
<table bgcolor="#FFFFFF" border="0" width="100%">
  <tr>
    <c:if test="${! advancedTranslationForm.globalTranslation}">
     <td align="left">
        <h3><digi:trn key="translation:interfaceForSite">Translation interface for site {siteName}</digi:trn></h3>
        (
        	<digi:trn key="translation:goTo">Go to</digi:trn>
        	<a href='<digi:site property="url" />'>
        		<b><digi:trn key="translation:indexPage">Index Page</digi:trn></b>
        	</a>
        )
     </td>
     <td align="right">
     	<c:set var="trn">
	  		<digi:trn key="translation:switchToGlobalTranslations">Switch to Global translations</digi:trn>
	  	</c:set>
        <c:if test="${advancedTranslationForm.globalTranslationGranted}">
     		<html:submit value="${trn}" onclick="javascript:fnOnSwitchToGlobal()" />
	 	</c:if>
	 	<c:if test="${advancedTranslationForm.groupTranslationGranted}">
	        <logic:present name="advancedTranslationForm" property="rootSiteId">
	    		<bean:define id="rootSiteId" name="advancedTranslationForm" property="rootSiteId" type="String"/>
	        	<br>
	        	<c:set var="trn">
	  				<digi:trn key="translation:switchToGroupTranslations">Switch to Group translations</digi:trn>
	  			</c:set>
	  			<input type="button" value="${trn}" onclick='<%= "javascript:document.location.href(\"" %><digi:site siteId="<%= rootSiteId %>" property="url" /><%= "/translation/\");" %>' />
	       	</logic:present>
	    </c:if>
     </td>
    </c:if>
    <c:if test="${advancedTranslationForm.globalTranslation}">
     <td align="left">
        <h3><digi:trn key="translation:interfaceForGlobal">Translation interface for Global translations</digi:trn></h3>
     </td>
     <td align="right">
        <c:set var="trn">
			<digi:trn key="translation:switchToSiteTranslations">Switch to Site translations</digi:trn>
		</c:set>
		<html:submit value="${trn}" onclick="javascript:fnOnSwitchToSite()" />
     </td>
    </c:if>
  </tr>
  <c:if test="${advancedTranslationForm.globalTranslation}">
   <tr>
	 <td colspan="4" align="left">

	 <digi:trn key="translation:goTo">Go to</digi:trn>
      <a href='<digi:site property="url" />'>
      <b><digi:trn key="translation:indexPage">Index Page</digi:trn></b>
     </a>
     </td>
   </tr>
  </c:if>
  <tr><td></td></tr>
</table>
<c:if test="${!empty advancedTranslationForm.languages}">
<table bgcolor="#EBEBEB" border="0" width="100%">
  <tr>
    <td  width="27%" align="center"><digi:trn key="translation:sourceLang">Source language</digi:trn>:
    	<html:select property="selectedLangSource"  onchange="onChangeList('LangSource', this.value)">
			<c:forEach var="lng" items="${advancedTranslationForm.languages}">
				<c:set var="trn">
					<digi:trn key="translation:sourceLanguage:${lng.name}">${lng.name}</digi:trn>
				</c:set>
				<html:option value="${lng.code}">${trn}</html:option>
			</c:forEach>
		</html:select>		
    </td> 
    <td width="46%" >&nbsp;</td>
    <td width="27%" align="center"><digi:trn key="translation:targetLang">Target language</digi:trn>:
		<html:select property="selectedLangTarget" onchange="onChangeList('LangTarget', this.value)">
			<c:forEach var="lng" items="${advancedTranslationForm.dstLanguages}">
				<c:set var="trn">
					<digi:trn key="translation:targetLanguage:${lng.name}">${lng.name}</digi:trn>
				</c:set>
				<html:option value="${lng.code}">${trn}</html:option>
			</c:forEach>
		</html:select>		
    </td>
  </tr>
</table>
</c:if>

<table border="0" width="100%" bgcolor="#EBEBEB">
  <tr>
   <td align="left">

   	   	<digi:trn key="translation:showExpired">Show Expired:</digi:trn>
   		<html:checkbox name="advancedTranslationForm" property="showExpired" onclick="onShowExpired()"/>
   	   	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <c:if test="${!empty advancedTranslationForm.keyPrefixes}">
   	   	<digi:trn key="translation:keyPrefix">Key prefix</digi:trn>:
		<html:select property="selectedPrefix" onchange="onChangeList('Prefix', this.value)">
		 <c:forEach var="keyPrefixes" items="${advancedTranslationForm.keyPrefixes}">
		 <bean:define id="vid"><c:out value="${keyPrefixes.key}"  escapeXml="false" /></bean:define>
		 <bean:define id="dvid" name="keyPrefixes" property="value" type="java.lang.String"/>
			 <html:option value='<%=vid%>'><c:out value="${keyPrefixes.value}"  escapeXml="false" /></html:option>
		 </c:forEach>
		</html:select>
   </c:if>
   </td>
   <digi:secure authenticated="true">
   <td align="left">
  	    <digi:link href="#" onclick="return showHideAddNewKey()">
  	    	 <div id="addnewkeylink">
  	    	 	<digi:trn key="translation:addNewKey">Add new key</digi:trn>
  	    	 </div>
  	    </digi:link>
   </td>
   </digi:secure>
 </tr>
</table>
<div id="addkeydiv" style="display:none; padding-top: 12px">
   <table bgcolor="#F8F8FF" border="0" width="100%">
   <tr>
    <td align="right">
      <digi:trn key="translation:key">Key:</digi:trn>
    </td>
    <td align="left">
	  <html:text name="advancedTranslationForm" property="key"/>
    </td>
    <td align="right">
      <digi:trn key="translation:message">Message:</digi:trn>
    </td>
    <td align="left">
  	  <html:text name="advancedTranslationForm" property="messageText"/>
    </td>
    <td align="right">
       <digi:trn key="translation:locale">Locale:</digi:trn>
    </td>
    <td align="left">
		<html:select property="locale">
		<bean:define id="lid" name="advancedTranslationForm" property="languages" type="java.util.List"/>
		<html:options collection="lid" property="code" labelProperty="name"/></html:select>
    </td>
   <td align="center">
     <c:set var="trn">
		<digi:trn key="translation:save">Save</digi:trn>
	</c:set>
	<input type="button" value="${trn}" onClick="fnOnAddKey()">
   </td>
  </tr>
  </table>
</div>
<div id="addkeydiv" style="padding-top: 12px">
<table border="0" width="100%" bgcolor="#F8F8FF">
  <tr>
   <td align="right">
     <digi:trn key="translation:key">Key:</digi:trn>
	 &nbsp;
	 <html:text name="advancedTranslationForm" property="searchKey" size="20" />
   </td>
   <td align="right">
     <digi:trn key="translation:sourceText">Source text:</digi:trn>
	 &nbsp;
	 <html:text name="advancedTranslationForm" property="searchSource" size="20" />
   </td>
   <td align="right">
     <digi:trn key="translation:targetText">Target text:</digi:trn>
   	 &nbsp;
     <html:text name="advancedTranslationForm" property="searchTarget" size="20" />
   </td>
   <td align="right">
   	<c:set var="trn">
   		<digi:trn key="translation:search">Search</digi:trn>
   	</c:set>
     <input type="button" value="${trn}" onclick="fnOnSearch()" />
   </td>
  </tr>
</table>
</div>
<c:if test="${!empty advancedTranslationForm.translations}">
  <table border="0" width="100%">

  <tr>
  	  <td colspan="6"><hr width="100%"></td>
  </tr>
  <tr>
  <td>
  	  <c:if test="${!empty globalAdminAccount}" >
  	  	<c:set var="trn">
	  		<digi:trn key="translation:expireSelected">Expire selected</digi:trn>
	  	</c:set> 
  	  	 <c:if test="${!advancedTranslationForm.showExpired}">
	     	<html:submit value="${trn}" property="expireSelected" onclick="return expireKeys()" />
	     </c:if>
	     <c:set var="trn">
	  		<digi:trn key="translation:unExpireSelected">Unexpire selected</digi:trn>
	  	</c:set> 
	     <c:if test="${advancedTranslationForm.showExpired}">
	     	<html:submit value="${trn}" property="unExpireSelected" onclick="return expireKeys()" />
	     </c:if>
	  </c:if>
  </td>
  <td align="right" colspan="5">
  <c:if test="${advancedTranslationForm.permitted}" >
  	<c:set var="trn">
  		<digi:trn key="translation:updateAll">Update all</digi:trn>
  	</c:set> 	
     <html:submit value="${trn}" onclick="return saveAll()" />
   </c:if>
   <c:if test="${!advancedTranslationForm.permitted}" >
     &nbsp;
   </c:if>
   </td>
  </tr>
  <tr>
  	  <td colspan="6"><hr width="100%"></td>
  </tr>
  <!-- Start of Content -->
<tr><td colspan=6>
  <bean:define id="trns" name="advancedTranslationForm" property="translations" scope="page" />
  <bean:define id="trnForm" name="advancedTranslationForm" scope="page" type="org.digijava.module.translation.form.AdvancedTranslationForm"/>
  <digi:context name="pagination" property="context/module/moduleinstance/showAdvancedTranslation.do" />
  
  				<c:set var="banerFirst">&nbsp;<span class="pagelinks">[<digi:trn key="displayTag:first">First</digi:trn>/<digi:trn key="displayTag:prev">Prev</digi:trn>] {0} [<a href="{3}"><digi:trn key="displayTag:next">Next</digi:trn></a>/<a href="{4}"><digi:trn key="displayTag:last">Last</digi:trn></a>]</span></c:set>
				<c:set var="banerFull">&nbsp;<span class="pagelinks">[<a href="{1}"><digi:trn key="displayTag:first">First</digi:trn></a>/<a href="{2}"><digi:trn key="displayTag:prev">Prev</digi:trn></a>] {0} [<a href="{3}"><digi:trn key="displayTag:next">Next</digi:trn></a>/<a href="{4}"><digi:trn key="displayTag:last">Last</digi:trn></a>]</span></c:set>
				<c:set var="banerLast">&nbsp;<span class="pagelinks">[<a href="{1}"><digi:trn key="displayTag:first">First</digi:trn></a>/<a href="{2}"><digi:trn key="displayTag:prev">Prev</digi:trn></a></a>] {0} [<digi:trn key="displayTag:next">Next</digi:trn>/<digi:trn key="displayTag:last">Last</digi:trn>]</span></c:set>
				<c:set var="banerOnePage">&nbsp;<span class="pagelinks">{0}</span></c:set>
				
				<c:set var="banerOneItem">&nbsp;<span class="pagebanner"><digi:trn key="displayTag:oneItemFound">One item found</digi:trn>.</span></c:set>
				<c:set var="banerAllItems">&nbsp;<span class="pagebanner">{0} <digi:trn key="displayTag:itemsFound">items found</digi:trn>, <digi:trn key="displayTag:displaingAll">displaying all items</digi:trn>.</span></c:set>
				<c:set var="banerSomeItems">&nbsp;<span class="pagebanner">{0} <digi:trn key="displayTag:itemsFound">items found</digi:trn>, <digi:trn key="displayTag:displaing">displaying</digi:trn> {2} <digi:trn key="displayTag:to">to</digi:trn> {3}.</span></c:set>
  
  
  <display:table class="trnrow" list="<%= trns %>" id="message" pagesize="25"
      requestURI="<%= pagination + trnForm.getParameters() %>"
      excludedParams="*" style="width:100%">
  <display:setProperty name="paging.banner.placement" value="both" />
  
  					<display:setProperty name="paging.banner.full" value="${banerFull}"/>
					<display:setProperty name="paging.banner.first" value="${banerFirst}"/>
					<display:setProperty name="paging.banner.last" value="${banerLast}"/>
					<display:setProperty name="paging.banner.onepage" value="${banerOnePage}"/>
					
					<display:setProperty name="paging.banner.one_item_found" value="${banerOneItem}"/>
					<display:setProperty name="paging.banner.all_items_found" value="${banerAllItems}"/>
					<display:setProperty name="paging.banner.some_items_found" value="${banerSomeItems}"/>
  
  
   <c:if test="${!empty globalAdminAccount}" >
    <display:column align="center" width="15">
       <html:multibox property="selectedKeys"><c:out value="${message.key}" escapeXml="false" /></html:multibox>
    </display:column>
    </c:if>
   <display:column align="left" width="275">
    <input type="Hidden" name='message[<c:out value="${message_rowNum}" />].key' value='<c:out value="${message.key}" />' />
	<c:if test="${message.srcChanged}">
	  <font color="red"><c:out value="${message.key}" escapeXml="false" /></font>
	</c:if>
	<c:if test="${!message.srcChanged}">
        <c:out value="${message.key}"  escapeXml="false" />
	</c:if>
   </display:column>
  <c:if test="${!empty globalAdminAccount}">
   <display:column align="center">
   	   	<c:set var="trn">
	  		<digi:trn key="translation:expireKey">Expire</digi:trn>
	  	</c:set> 
  	  	<c:if test="${!advancedTranslationForm.showExpired}">
        	<input type="submit" name="expireKey" value="${trn}" onClick="return expireOneKey(<c:out value="${message_rowNum}" />)">
        </c:if>
        <c:set var="trn">
	  		<digi:trn key="translation:unexpireKey">Unexpire</digi:trn>
	  	</c:set> 
  	  	<c:if test="${advancedTranslationForm.showExpired}">
        	<input type="submit" name="unexpireKey" value="${trn}" onClick="return expireOneKey(<c:out value="${message_rowNum}" />)">
        </c:if>
   </display:column>
  </c:if>
  <c:if test="${message.sourceType == 0}">
   <c:set var="srcColor" value="lightblue" />
  </c:if>
  <c:if test="${message.sourceType == 1}">
   <c:set var="srcColor" value="lightblue" />
  </c:if>
  <c:if test="${message.sourceType == 2}">
   <c:set var="srcColor" value="#FFFFFF" />
  </c:if>

   <display:column align="left" width="300">
   	   <div style="padding-left: 4px; padding-top: 2px; padding-bottom: 2px">
	   		<span><c:out value="${message.sourceValue}" escapeXml="false" /></span>
	   </div>
   </display:column>
   	   <a name='#trans<c:out value="${message_rowNum}" />'></a>
   	   <c:if test="${message.targetType == 0}">
    <c:set var="dstColor" value="lightblue" />
	   </c:if>
	   <c:if test="${message.targetType == 1}">
    <c:set var="srcColor" value="lightblue" />
	   </c:if>
	   <c:if test="${message.targetType == 2}">
    <c:set var="srcColor" value="#FFFFFFF" />
	   </c:if>
  <c:if test="${advancedTranslationForm.permitted}" >
   <display:column align="center">
   	   <input type="hidden" name="messageOrig[<c:out value="${message_rowNum}" />].targetValue" value="<c:out value="${message.targetValue}" />">
   	   <c:if test="${advancedTranslationForm.globalTranslation}">
	    	<textarea style='width: 280' name='message[<c:out value="${message_rowNum}" />].targetValue' rows="4" cols="30"><c:out value="${message.targetValue}" /></textarea>
	   </c:if>
	   <c:if test="${!advancedTranslationForm.globalTranslation}">
	    	<textarea style='background-color: <c:out value="${srcColor}" />; width: 280' name='message[<c:out value="${message_rowNum}" />].targetValue' rows="4" cols="30"><c:out value="${message.targetValue}" /></textarea>
	   </c:if>
   </display:column>
   <display:column align="center">
   	<c:set var="trn">
   		<digi:trn key="translation:update">Update </digi:trn>
   	</c:set>
      <input type="submit" value="${trn}" onClick="return fnOnUpdateMessage(<c:out value="${message_rowNum}" />)">
   </display:column>
  </c:if>
  <c:if test="${!advancedTranslationForm.permitted}" >
   <display:column align="center">
    <textarea style='color: <c:out value="${srcColor}" />' rows="4" cols="30" readonly="true"><c:out value="${message.targetValue}" /></textarea>
   </display:column>
   </c:if>
 </display:table>
</td></tr>
  <!-- End of Content -->
  <tr>
  	  <td colspan="6"><hr width="100%"></td>
  </tr>
  <tr>
  <td>
      <c:if test="${!empty globalAdminAccount}" >
  	  	 <c:set var="trn">
	  		<digi:trn key="translation:expireSelected">Expire selected</digi:trn>
	  	</c:set> 
  	  	 <c:if test="${!advancedTranslationForm.showExpired}">
	     	<html:submit value="${trn}" property="expireSelected" onclick="return expireKeys()" />
	     </c:if>
	     <c:set var="trn">
	  		<digi:trn key="translation:unExpireSelected">Unexpire selected</digi:trn>
	  	</c:set> 
	     <c:if test="${advancedTranslationForm.showExpired}">
	     	<html:submit value="${trn}" property="unExpireSelected" onclick="return expireKeys()" />
	     </c:if>
	  </c:if>
  </td>
   <td colspan="5" align="right">
   <c:if test="${advancedTranslationForm.permitted}" >
   	<c:set var="trn">
   		<digi:trn key="translation:updateAll">Update all</digi:trn>
   	</c:set>
     <html:submit value="   ${trn}   " onclick="return saveAll()" />
   </c:if>
   </td>
  </tr>
  <tr>
  	  <td colspan="6"><hr width="100%"></td>
  </tr>
 </table>
</c:if>


</digi:form>
</body>
