
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
	<digi:instance property="translationForm"/>
	<i>Translation TEST</i><BR><BR>
	<logic:present name="translationForm" property="languages">
	<logic:iterate id="languages" name="translationForm" property="languages" type="org.digijava.ampModule.translation.form.TranslationForm.TranslationInfo">
	<bean:define id="langCode" name="languages" property="langCode" type="java.lang.String"/>
	<bean:define id="langKey" name="languages" property="key" type="java.lang.String"/><a href='<bean:write name="languages" property="referUrl" />'>
	<bean:write name="languages" property="langName"/></a>&nbsp;
	</logic:iterate></logic:present>