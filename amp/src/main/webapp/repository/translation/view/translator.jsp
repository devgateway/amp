
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="javascript" type="text/javascript">

     function copyText(objName) {
	  document.getElementsByName('translation')[0].value=document.getElementsByName(objName)[0].value;
     }

</script>

<digi:errors/>
<digi:form action="/saveTranslation.do" method="post">

<digi:context name="indexUrl" property="context" />
<html:hidden property="key" />
<html:hidden property="siteId" />
<html:hidden property="type" />
<html:hidden property="back_url" />


<table bgcolor="#FFFFFF" border="0" width="100%">
  <tr>
     <td align="left">
        <bean:define id="backUrl" name="translatorForm" property="back_url" type="java.lang.String"/>
	    <a href="<%= indexUrl+backUrl%>"><small><digi:trn key="translation:goBack">Go back to where you were</digi:trn></small></a>
        <p><small>(<digi:trn key="translation:goTo">Go to</digi:trn> <a href='<digi:site property="url" />'><b><digi:trn key="translation:indexPage">Index Page</digi:trn></b></a>)</small>
     </td>
  </tr>
</table>
<br >
<div align="center">
<table bgcolor="#EBEBEB" border="0" width="80%">
  <tr>
     <td align="center" width="50%">
        <b><digi:trn key="translation:targetLanguage">Target language:</digi:trn></b>
     </td>
     <td align="center" width="50%">
        <b><digi:trn key="translation:translationKey">Translation key:</digi:trn></b>
     </td>
  </tr>
  <tr bgcolor="FFFFFF">
     <td align="left" width="50%" height="30" valign="center">
      <c:if test="${!empty translatorForm.langKey}">
        <bean:define id="languageKey" name="translatorForm" property="langKey" type="java.lang.String"/>
	 	 <digi:trn key="<%=languageKey%>"><%=languageKey%></digi:trn>
 	  </c:if>
     </td>
     <td align="left" width="50%" height="30" valign="center">
        <c:out value="${translatorForm.key}" />
     </td>
  </tr>
  <tr>
     <td align="left" colspan=2>
        <b><digi:trn key="translation:typeYourTranslation">Type in your translation:</digi:trn></b>
     </td>
  </tr>
  <tr>
     <td align="left" colspan=2 valign="top">
        <html:textarea name="translatorForm" property="translation" rows="4" cols="100"/>
     </td>
  </tr>
  <tr>
     <td align="right">
     	<c:set var="translate">
     		<digi:trn key="translation:savetranslation">
     			Save Translation
     		</digi:trn>
     	</c:set>
		    
	     <html:submit value="${translate}" />
    
     </td>
     <td align="left">
		<c:set var="translate">
     		<digi:trn key="translation:deletetranslation">
     			Delete Translation
     		</digi:trn>
     	</c:set>
     	
             <html:submit property="deleteTranslation" value="${translate}" />
     </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
     <td align="left">
        <b><digi:trn key="translation:msgInEnglish">Message to translate from English:</digi:trn></b>
     </td>
     <td align="left">
        <a href="javascript:copyText('message')"><digi:trn key="translation:useTranslation">Use</digi:trn></a>
     </td>
  </tr>
  <tr bgcolor="FFFFFF">
     <td align="left" colspan=2  height="50" valign="top">
        <html:textarea name="translatorForm" property="message" rows="4" cols="100" disabled="true"/>
     </td>
  </tr>
<c:if test='${!empty translatorForm.groupTranslation}'>
  <tr><td>&nbsp;</td></tr>
  <tr>
     <td align="left">
        <b><digi:trn key="translation:msgGroupTrans">Message, translated in group:</digi:trn></b>
     </td>
     <td align="left">
        <a href="javascript:copyText('groupTranslation')"><digi:trn key="translation:useTranslation">Use</digi:trn></a>
     </td>
  </tr>
  <tr bgcolor="FFFFFF">
     <td align="left" colspan=2  height="50" valign="top">
        <html:textarea name="translatorForm" property="groupTranslation" rows="4" cols="100" disabled="true"/>
     </td>
  </tr>
</c:if>
<c:if test='${!empty translatorForm.globalTranslation}'>
  <tr><td>&nbsp;</td></tr>
  <tr>
     <td align="left">
        <b><digi:trn key="translation:msgGlobalTrans">Message, translated in global scope:</digi:trn></b>
     </td>
     <td align="left">
        <a href="javascript:copyText('globalTranslation')"><digi:trn key="translation:useTranslation">Use</digi:trn></a>
     </td>
  </tr>
  <tr bgcolor="FFFFFF">
     <td align="left" colspan=2  height="50" valign="top">
        <html:textarea name="translatorForm" property="globalTranslation" rows="4" cols="100" disabled="true"/>
     </td>
  </tr>
</c:if>
  <tr><td>&nbsp;</td></tr>
</table>
<br>
<c:if test="${!translatorForm.permitted}" >
  <table bgcolor="#FFFFFF" border="0" width="80%">
   <tr>
     <td align="center"><font color="red"><digi:trn key="translation:noRightOnAction">You don't have right for such translation</digi:trn></font></td>
   </tr>
   <tr>
     <td align="center"><small><digi:trn key="translation:useLinksAbove">Use links above either to go back or to the Index page</digi:trn></small></td>
   </tr>
  </table>
</c:if>
</div>
</digi:form>
