
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>

	<digi:form action="/switchLanguage.do" method="post">
	<i>Translation TEST</i><BR><BR>
	<logic:present name="translationForm" property="languages">
	<html:select property="referUrl" onChange="SwithLanguage(this)">
	<bean:define id="lid" name="translationForm" property="languages" type="java.util.List"/>
	<logic:iterate id="languages" name="translationForm" property="languages" type="org.digijava.module.translation.form.TranslationForm.TranslationInfo">
	<bean:define id="referUrl" name="languages" property="referUrl" type="java.lang.String"/>
	<bean:define id="langCode" name="languages" property="langCode" type="java.lang.String"/>
	<bean:define id="langKey" name="languages" property="key" type="java.lang.String"/>
	<html:option value='<%=referUrl%>'>
	<bean:write name="languages" property="langName"/></html:option>
	</logic:iterate></html:select></logic:present></digi:form>
	
<script>function SwithLanguage(obj) {
  var refer = obj.value;
  document.location.href = refer;
}
</script>