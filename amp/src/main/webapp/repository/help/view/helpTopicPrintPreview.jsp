<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
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
