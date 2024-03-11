
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>

	<digi:instance property="translationForm"/>
	<logic:present name="translationForm" property="languages">
	<logic:iterate id="languages" name="translationForm" property="languages" type="org.digijava.module.translation.form.TranslationForm.TranslationInfo">
	<bean:define id="langCode" name="languages" property="langCode" type="java.lang.String"/>
	<bean:define id="langKey" name="languages" property="key" type="java.lang.String"/><a href='<bean:write name="languages" property="referUrl" />'>
	<bean:write name="languages" property="langName"/></a>&nbsp;
	</logic:iterate></logic:present>