
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

	<digi:instance property="translationForm"/>
	<logic:present name="translationForm" property="languages">
			<logic:iterate id="languages" name="translationForm" property="languages" type="org.digijava.module.translation.form.TranslationForm.TranslationInfo">
				<bean:define id="langReferUrl" name="languages" property="referUrl" type="java.lang.String"/>
                <c:choose>
                  <c:when test="${translationForm.referUrl == langReferUrl}">
                    <li class="yuiampmenuitem yuiampmenuitem-checked first-of-type">
                  </c:when>
                  <c:otherwise>
                    <li class="yuiampmenuitem">
                  </c:otherwise>
                </c:choose>                
				<a href="#" onclick='SwitchLanguageMenu("<%= langReferUrl %>")'>
					<digi:trn key="aim:${languages.langName}"><bean:write name="languages" property="langName"/></digi:trn>
				</a>
                </li>
			</logic:iterate>
	</logic:present>
	
<script>function SwitchLanguageMenu(value) {
  document.location.href = value;
}
</script>
