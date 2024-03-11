<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/resources/tld/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/resources/tld/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/resources/tld/moduleVisibility.tld" prefix="module" %>
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
