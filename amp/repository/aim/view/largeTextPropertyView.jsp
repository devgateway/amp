<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>

<bean:define name="largeTextFeature" id="largeTextFeature"
	scope="request" />
<bean:define name="largeTextLabel" id="largeTextLabel" scope="request" />
<bean:define name="largeTextKey" id="largeTextKey" scope="request"/>


<field:display feature="${largeTextFeature}" name="${largeTextLabel}">
	<tr>
		<td class="t-name" width="30%" align="right" bgcolor="#f0f0f0"><digi:trn
			key="aim:${largeTextLabel}">${largeTextLabel}</digi:trn></td>
		<td bgcolor="#FFFFFF" style="border-bottom:2px solid #f0f0f0"><c:if
			test="${largeTextKey!=null}">
			<digi:edit key="${largeTextKey}"></digi:edit>
		</c:if>&nbsp;</td>
	</tr>
</field:display>
