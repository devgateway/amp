<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/moduleVisibility.tld" prefix="module" %>
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
