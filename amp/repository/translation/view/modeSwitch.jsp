<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>

<digi:secure actions="ADMIN, TRANSLATE">

	<script>function switchTranslation(mode) {
	      <digi:context name="switchUrl" property="context/module/moduleinstance/switchMode.do" />
	      document.modeSwitchForm.action = "<%= switchUrl %>?translationMode="+mode;
	      document.modeSwitchForm.submit();
	  }
	</script>

	<digi:form method="post" action="/switchMode.do">
	<html:hidden property="backUrl"/>
	<table border="2" cellpadding="5" bordercolor="#B22222" bgcolor="FFD700" style="border-collapse: collapse;">
		<tr>
			<logic:equal name="modeSwitchForm" property="translationMode" value="false">
			<td><a   href="javascript:switchTranslation(true)"><i>
				<digi:trn key="trn:transview1">Translator View</digi:trn></i></a>
			</td>
			<td><i>
				<digi:trn key="trn:nontrans1">Non-translator View</digi:trn></i>
			</td></logic:equal>
			<logic:equal name="modeSwitchForm" property="translationMode" value="true">
			<td><i>
				<digi:trn key="trn:transview1">Translator View</digi:trn></i>
			</td>
			<td><a href="javascript:switchTranslation(false)"><i>
				<digi:trn key="trn:nontrans1">Non-translator View</digi:trn></i></a>
			</td></logic:equal>
			
			<td>
				<html:link page="/translation/showAdvancedTranslation.do">
		        <digi:trn key="trn:AdvancedMode">Advanced Mode</digi:trn></html:link>
			</td>
			<td>
				<html:link page="/translation/showEditPermissions.do">
				<digi:trn key="trn:TranslationAdmin">Translation Administration</digi:trn></html:link>
			</td>
		</tr>
	</table>
	</digi:form>

</digi:secure>
