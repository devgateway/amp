<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:secure actions="ADMIN, TRANSLATE">

	<script type="text/javascript">
	function switchTranslation(mode) {
	      <digi:context name="switchUrl" property="context/module/moduleinstance/switchMode.do" />
	      document.modeSwitchForm.action = "<%= switchUrl %>?translationMode="+mode;
	      document.modeSwitchForm.submit();
	  }
	</script>
	<span>
	<digi:form method="post" action="/switchMode.do" style="display:none;" >
	<html:hidden property="backUrl"/>
	</digi:form>
	</span>
			<logic:equal name="modeSwitchForm" property="translationMode" value="false">
				<li class="yuiampmenuitem_drop">
                    <a id="switchTranslationMode" class="yuiampmenuitemlabel" onclick="return canExit()" href="javascript:switchTranslation(true)">
                        <digi:trn key="trn:transview1">Translator View</digi:trn>
                    </a>
				</li>
            </logic:equal>
			<logic:equal name="modeSwitchForm" property="translationMode" value="true">
				<li class="yuiampmenuitem_drop">
                	<a id="switchTranslationMode" class="yuiampmenuitemlabel"  onclick="return canExit()" href="javascript:switchTranslation(false)">
                    	<i><digi:trn key="trn:nontrans1">Non-translator View</digi:trn></i>
                    </a>
				</li>
            </logic:equal>
            <li class="yuiampmenuitem_drop">
				<a class="yuiampmenuitemlabel" onclick="return canExit()" href="/translation/showNewAdvancedTranslation.do">
		        <digi:trn key="trn:AdvancedMode">Advanced Mode</digi:trn>
                </a>
			</li>         
</digi:secure>
