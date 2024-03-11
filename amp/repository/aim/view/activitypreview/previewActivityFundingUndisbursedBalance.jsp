<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/resources/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/src/main/resources/tld/category.tld" prefix="category"%>

<%@ taglib uri="/src/main/resources/tld/fieldVisibility.tld" prefix="field"%>
<%@ taglib uri="/src/main/resources/tld/featureVisibility.tld" prefix="feature"%>
<%@ taglib uri="/src/main/resources/tld/moduleVisibility.tld" prefix="module"%>
<digi:instance property="aimEditActivityForm" />

<c:if test="${not empty funding.undisbursementbalance}">
	<tr>
		<td colspan="2" class="preview-funding-total">
			<digi:trn key="aim:undisbursedBalance">Undisbursed Balance</digi:trn>:
		</td>
		<td colspan="2" nowrap="nowrap" class="preview-align preview-funding-total">
			<b><span dir="ltr">${funding.undisbursementbalance}</span> ${aimEditActivityForm.currCode}</b>
			&nbsp;
		</td>
		<td class="preview-funding-total">&nbsp;</td>
	</tr>
</c:if> 
