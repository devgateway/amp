<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>

<digi:instance property="translationForm" />
<logic:present name="translationForm" property="languages">
<html:select property="referUrl"  onchange="SwithLanguage(this)">
  <logic:iterate id="languages" name="translationForm" property="languages"  type="org.digijava.module.translation.form.TranslationForm.TranslationInfo">
    <bean:define id="langReferUrl" name="languages" property="referUrl" type="java.lang.String" />
    <html:option style="background-color: #DCE8ED;" value='<%= langReferUrl %>'><bean:write name="languages" property="langName" /></html:option>
  </logic:iterate>
</html:select>
</logic:present>

<script>
function SwithLanguage(obj) {
  var refer = obj.value;
  document.location.href = refer;
}
</script>