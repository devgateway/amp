<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>
<%@ taglib uri="http://digijava.org/modules" prefix="module" %>
<style>
body{
	background: none;
}
</style>

<digi:instance property="helpForm" />
<div style="width:100%;text-align:center;">
	<span style="font-family:arial; font-size: 20px; font-weight: bold; color:#F1F1F1;"><digi:trn>${helpForm.topicKey}</digi:trn></span>
</div>
<br />
<div style="background: white;">
		<bean:define id="descKey">
			<c:out value="${helpForm.bodyEditKey}"/>
		</bean:define>
		<digi:edit key="<%=descKey%>" displayText=""/>
</div>
