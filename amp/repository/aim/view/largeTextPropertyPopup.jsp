<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/fieldVisibility.tld" prefix="field"%>

<bean:define name="largeTextFeature" id="largeTextFeature"
	scope="request" />
<bean:define name="largeTextLabel" id="largeTextLabel" scope="request" />
<bean:define name="largeTextKey" id="largeTextKey" scope="request" />

<c:if test="${largeTextKey!=null}">
<TR>
	<TD bgcolor="#ffffff"><field:display feature="${largeTextFeature}"
		name="${largeTextLabel}">
		<i><digi:trn>${largeTextLabel}</digi:trn></i>:
		<digi:edit key="${largeTextKey}"/>
	</field:display></TD>
</TR>
</c:if>