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

<digi:instance property="helpForm" />

<table cellpadding="0" cellspacing="0">
	<tr>
		<td align="center"><span style="font: 20px bold;"><digi:trn>${helpForm.topicKey}</digi:trn></span></td>
	</tr>
	<tr height="5px"><td>&nbsp;</td></tr>
	<tr>
		<td>
			<bean:define id="descKey">
				<c:out value="${helpForm.bodyEditKey}"/>
			</bean:define>
			<digi:edit key="<%=descKey%>" displayText=""/>
		</td>
	</tr>
</table>
