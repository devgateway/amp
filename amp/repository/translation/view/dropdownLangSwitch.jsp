
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>

	<digi:instance property="translationForm"/>
	<logic:present name="translationForm" property="languages">
		<html:select property="referUrl" onchange="SwithLanguage(this)" styleClass="dropdwn_sm_lng">
			<logic:iterate id="languages" name="translationForm" property="languages" type="org.digijava.module.translation.form.TranslationForm.TranslationInfo">
				<bean:define id="langReferUrl" name="languages" property="referUrl" type="java.lang.String"/>
				<html:option value='<%= langReferUrl %>'>
					<digi:trn key="aim:${languages.langName}"><bean:write name="languages" property="langName"/></digi:trn>
				</html:option>
			</logic:iterate>
		</html:select>
	</logic:present>
	
<script>function SwithLanguage(obj) {
  var refer = obj.value;
  document.location.href = refer;
}
</script>
