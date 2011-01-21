<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:secure actions="ADMIN, TRANSLATE">

	<script>
	function switchTranslation(mode) {
	      <digi:context name="switchUrl" property="context/module/moduleinstance/switchMode.do" />
	      document.modeSwitchForm.action = "<%= switchUrl %>?translationMode="+mode;
	      document.modeSwitchForm.submit();
	  }
	</script>

	<digi:form method="post" action="/switchMode.do" style="display:none;" >
	<html:hidden property="backUrl"/>
	</digi:form>
			<logic:equal name="modeSwitchForm" property="translationMode" value="false">
				<li>
                    <a onclick="return canExit()" href="javascript:switchTranslation(true)">
                        <digi:trn key="trn:transview1">Translator View</digi:trn>
                    </a>
				</li>
            </logic:equal>
			<logic:equal name="modeSwitchForm" property="translationMode" value="true">
				<li>
                	<a onclick="return canExit()" href="javascript:switchTranslation(false)">
                    	<i><digi:trn key="trn:nontrans1">Non-translator View</digi:trn></i>
                    </a>
				</li>
            </logic:equal>
            <li>
				<a onclick="return canExit()" href="/translation/showAdvancedTranslation.do">
		        <digi:trn key="trn:AdvancedMode">Advanced Mode</digi:trn>
                </a>
			</li>         
</digi:secure>
