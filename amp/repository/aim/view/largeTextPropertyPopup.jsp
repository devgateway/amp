<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>

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