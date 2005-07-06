<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language=JavaScript type=text/javascript>
function fnOnSearch(search) {
      <digi:context name="searchMsg" property="context/module/moduleinstance/showAdvancedTranslation.do" />
      document.advancedTranslationForm.action = "<%=searchMsg%>?search="+search;
      document.advancedTranslationForm.submit();
  }
function changePrefix(obj) {
  var param = obj.value;
  <digi:context name="changePref" property="context/module/moduleinstance/showAdvancedTranslation.do" />
  var szQuery = "<%=changePref%>?prefix="+param;
  document.location.href = szQuery;
}
function changeSourceLang(obj) {
  var param = obj.value;
  <digi:context name="changeSLang" property="context/module/moduleinstance/showAdvancedTranslation.do" />
  var szQuery = "<%=changeSLang%>?slang="+param;
  document.location.href = szQuery;
}
function changeTargetLang(obj) {
  var param = obj.value;
  <digi:context name="changeTLang" property="context/module/moduleinstance/showAdvancedTranslation.do" />
  var szQuery = "<%=changeTLang%>?tlang="+param;
  document.location.href = szQuery;
}
function fnOnUpdateMessage(key) {
      <digi:context name="updateMsg" property="context/module/moduleinstance/updateMessage.do" />
      document.advancedTranslationForm.action = "<%=updateMsg%>?key="+key;
      document.advancedTranslationForm.submit();
 }
function fnOnDeleteKey(key) {
      <digi:context name="deleteKey" property="context/module/moduleinstance/deleteKey.do" />
      document.advancedTranslationForm.action = "<%=deleteKey%>?key="+key;
      document.advancedTranslationForm.submit();
 }
function fnOnSwitchToGlobal() {
      <digi:context name="switchToGlobal" property="context/module/moduleinstance/showAdvancedTranslation.do" />
      document.advancedTranslationForm.action = "<%=switchToGlobal%>?global=true";
      document.advancedTranslationForm.submit();
 }
function fnOnSwitchToSite() {
      <digi:context name="switchToSite" property="context/module/moduleinstance/showAdvancedTranslation.do" />
      document.advancedTranslationForm.action = "<%=switchToSite%>?global=false";
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
</script> 

<digi:errors/>
<digi:form action="/saveAllMessages.do" method="post">

<digi:context name="indexUrl" property="context" />

<table bgcolor="#FFFFFF" border="0" width="100%">
  <tr>
    <c:if test="${! advancedTranslationForm.globalTranslation}">
     <td align="left">
        <h3><digi:trn key="translation:interfaceForSite">Translation interface for site {siteName}</digi:trn></h3>
        (<digi:trn key="translation:goTo">Go to</digi:trn> <a href="<%= indexUrl%>"><b><digi:trn key="translation:indexPage">Index Page</digi:trn></b></a>)
     </td>
     <digi:secure globalAdmin="true">
     <td align="right">
        <html:submit value="Switch to Global translations" onclick="javascript:fnOnSwitchToGlobal()" />
     </td>
     </digi:secure>
    </c:if> 
    <c:if test="${advancedTranslationForm.globalTranslation}">
     <td align="left">
        <h3><digi:trn key="translation:interfaceForGlobal">Translation interface for Global translations</digi:trn></h3>
     </td>
     <td align="right">
        <html:submit value="Switch to Site translations" onclick="javascript:fnOnSwitchToSite()" />
     </td>
    </c:if> 
  </tr>
  <c:if test="${advancedTranslationForm.globalTranslation}">
   <tr>  
	 <td colspan=4 align="left">
	 <digi:trn key="translation:goTo">Go to</digi:trn> 
      <a href="<%= indexUrl%>">
      <b><digi:trn key="translation:indexPage">Index Page</digi:trn></b>
     </a> 
     </td>
   </tr>
  </c:if>  
  <tr><td>&nbsp;</td></tr>
</table>
<c:if test="${!empty advancedTranslationForm.languages}">
<table bgcolor="#EBEBEB" border="0" width="100%">
  <tr>
    <td  width="27%" align="center"><digi:trn key="translation:sourceLang">Source language</digi:trn>
		:<html:select property="selectedLangSource"  onchange="changeSourceLang(this)">
		<bean:define id="lid" name="advancedTranslationForm" property="languages" type="java.util.List"/>
		<html:options collection="lid" property="code" labelProperty="name"/></html:select>
    :</td>
    <td width="46%" >&nbsp;</td> 
    <td width="27%" align="center"><digi:trn key="translation:targetLang">Target language</digi:trn>
		:<html:select property="selectedLangTarget" onchange="changeTargetLang(this)">
		<bean:define id="lid" name="advancedTranslationForm" property="languages" type="java.util.List"/>
		<html:options collection="lid" property="code" labelProperty="name"/></html:select>
    :</td>
  </tr>
</table>
</c:if>  
<table border="0" width="100%">
  <tr>
   <c:if test="${!empty advancedTranslationForm.keyPrefixes}">
   <td align="left"><digi:trn key="translation:keyPrefix">Key prefix</digi:trn>:
		<html:select property="selectedPrefix" onchange="changePrefix(this)">
		 <c:forEach var="keyPrefixes" items="${advancedTranslationForm.keyPrefixes}">
		 <bean:define id="vid" name="keyPrefixes" property="value" type="java.lang.String"/>
			 <html:option value='<%=vid%>'><c:out value="${keyPrefixes.value}"  escapeXml="false" /></html:option>
		 </c:forEach>
		</html:select>
   :</td>
   </c:if>
   <digi:secure authenticated="true">
   <td align="left">
  	    <digi:link href="/showAddKey.do"><digi:trn key="translation:addNewKey">Add new key</digi:trn></digi:link>
   </td>
   </digi:secure>
 </tr>
 <c:if test="${advancedTranslationForm.addKey}">
 <tr><td colspan=2>
    <br>
 </td></tr>
 <tr><td align="center" colspan=2>
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
    <td align="left">:
		<html:select property="locale">
		<bean:define id="lid" name="advancedTranslationForm" property="languages" type="java.util.List"/>
		<html:options collection="lid" property="code" labelProperty="name"/></html:select>
    :</td>
   <td align="center">
     <input type="button" value="Save" onClick="fnOnAddKey()">
   </td>  
  </tr>
  </table>   
 </td></tr>
 <tr><td colspan=2>
     <br>
 </td></tr>
 </c:if>
</table>
<table border="0" width="100%">
  <tr>
   <td align="right">
     <digi:trn key="translation:key">Key:</digi:trn>
   </td>
   <td align="right">
     <html:text name="advancedTranslationForm" property="searchKey" size="20" />
   </td>
   <td align="right">
     <html:submit value="Search" onclick="javascript:fnOnSearch('key')" />
   </td>
   <td align="right">
     <digi:trn key="translation:sourceText">Source text:</digi:trn>
   </td>
   <td align="right">
     <html:text name="advancedTranslationForm" property="searchSource" size="40" />
   </td>
   <td align="right">
     <html:submit value="Search" onclick="javascript:fnOnSearch('source')" />
   </td>
   <td align="right">
     <digi:trn key="translation:targetText">Target text:</digi:trn>
   </td>
   <td align="right">
     <html:text name="advancedTranslationForm" property="searchTarget" size="40" />
   </td>
   <td align="right">
     <html:submit value="Search" onclick="javascript:fnOnSearch('target')" />
   </td>
  </tr>
</table>
<c:if test="${!empty advancedTranslationForm.messages}">
<table border="0" width="100%">
  <!--pagination -->
  <tr>
    <td align="right" colspan=5>
			<c:if test="${!empty advancedTranslationForm.prev}">
				<a href="javascript:Prev('<c:out value="${advancedTranslationForm.prev}" />')">
					<small>&lt;&lt;&nbsp;</small><small><digi:trn key="translation:previous">Previous</digi:trn></small>&nbsp;
				</a>
			</c:if>
			<c:if test="${!empty advancedTranslationForm.prev}">
			<c:if test="${!empty advancedTranslationForm.next}">
				::
			</c:if>
			</c:if>
			<c:if test="${!empty advancedTranslationForm.next}">
				<a href="javascript:Next(<c:out value="${advancedTranslationForm.next}" />)">
					&nbsp;<small><digi:trn key="translation:next">Next</digi:trn></small><small>&nbsp;&gt;&gt;&nbsp;</small>
				</a>
			</c:if>
    </td>
  </tr>
  <!--pagination -->
  <tr><td align="right" colspan=5><html:submit value="   Save all   " /></td></tr>
  <logic:iterate indexId="ind" id="message" name="advancedTranslationForm" property="messages" type="org.digijava.module.translation.form.AdvancedTranslationForm.MessageInfo">
    <html:hidden name="message" indexed="true" property="key" />
    <html:hidden name="message" indexed="true" property="sourceValue" />
    <html:hidden name="message" indexed="true" property="encodedKey" />
    <html:hidden name="message" indexed="true" property="translation" />
    
    <c:set var="isEven" value="${ind % 2}"/>
  <tr 
    <c:if test="${isEven == 0}">bgcolor="#EBEBEB"</c:if>
    <c:if test="${isEven == 1}">bgcolor="#D2D2DB"</c:if>
  >
   <td align="left">
	<c:if test="${! message.translation}">
	  <font color="red"><c:out value="${message.key}" escapeXml="false" /></font>
	</c:if>    
	<c:if test="${message.translation}">
        <c:out value="${message.key}"  escapeXml="false" />
	</c:if>    
   </td>	
   <td align="center">
        <input type="button" value="Expire" onClick="fnOnDeleteKey('<c:out value="${message.encodedKey}" escapeXml="false" />')">
   </td>	
   <td align="left"><c:out value="${message.sourceValue}" escapeXml="false" /></td>	
   <td align="center">
		<html:textarea name="message" indexed="true" property="targetValue" rows="4" cols="30"/>
   </td>	
   <td align="center">
      <input type="button" value="Update" onClick="fnOnUpdateMessage('<c:out value="${message.encodedKey}" escapeXml="false" />')">
   </td>	
   </tr>
  </logic:iterate>
  <!--pagination -->
  <tr>
    <td align="right" colspan=5>
			<c:if test="${!empty advancedTranslationForm.prev}">
				<a href="javascript:Prev(<c:out value="${advancedTranslationForm.prev}" />)">
					<small>&lt;&lt;&nbsp;</small><small><digi:trn key="translation:previous">Previous</digi:trn></small>&nbsp;
				</a>
			</c:if>
			<c:if test="${!empty advancedTranslationForm.prev}">
			<c:if test="${!empty advancedTranslationForm.next}">
				::
			</c:if>
			</c:if>
			<c:if test="${!empty advancedTranslationForm.next}">
				<a href="javascript:Next(<c:out value="${advancedTranslationForm.next}" />)">
					&nbsp;<small><digi:trn key="translation:next">Next</digi:trn></small><small>&nbsp;&gt;&gt;&nbsp;</small>
				</a>
			</c:if>
    </td>
  </tr>
  <!--pagination -->
</table>
</c:if>
<c:if test="${empty advancedTranslationForm.messages}">
<table border="0" width="100%">
  <tr><td>&nbsp;</td></tr>
  <tr><td align="center"><digi:trn key="translation:noMessagesToView">The are no messages to view</digi:trn></td></tr>
</table>
</c:if>

</digi:form>