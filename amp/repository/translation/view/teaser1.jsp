
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

	<digi:form action="/switchLanguage.do" method="post">
	<i>Translation TEST</i><BR><BR>
	<logic:present name="translationForm" property="languages">
	<html:select property="referUrl" onChange="SwithLanguage(this)">
	<bean:define id="lid" name="translationForm" property="languages" type="java.util.List"/>
	<logic:iterate id="languages" name="translationForm" property="languages" type="org.digijava.ampModule.translation.form.TranslationForm.TranslationInfo">
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