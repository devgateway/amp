<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://digijava.org/CategoryManager" prefix="category"%>

<%@ taglib uri="http://digijava.org/fields" prefix="field"%>
<%@ taglib uri="http://digijava.org/features" prefix="feature"%>
<%@ taglib uri="http://digijava.org/modules" prefix="module"%>
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
