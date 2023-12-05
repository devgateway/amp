<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

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