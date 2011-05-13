<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<logic:present name="close">
<script type="text/javascript">
	window.opener.location.href = window.opener.location.href;
	window.close();
</script>	
</logic:present>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/addDisbOrderToDisb.do" method="post">
<table width="100%" cellpadding=1 cellspacing=3 bgcolor="#ffffff">
<tr>
<td colspan="2" width="100%" bgcolor="#006699" class="textalb" height="20" align="center">Select Disbursement Order</td>
</tr>
<tr />
<tr>
<td width="50%" align="right"><digi:trn key="aim:disbursementOrderId">Disbursement Order Id:&nbsp;</digi:trn></td>
<td align="left">
  <html:select name="aimEditActivityForm" property="disbOrderId" styleClass="inp-text">
  <c:forEach var="fundingDetail" items="${aimEditActivityForm.fundingDetails}">
  <c:if test="${fundingDetail.transactionType==4}">
    <html:option value="${fundingDetail.disbOrderId}">${fundingDetail.disbOrderId}</html:option>
   </c:if>
  </c:forEach>
  </html:select></td>
</tr>
<tr>
<td colspan="2" align="center">
<html:submit property="event" style="inp-text"><digi:trn key="aim:add">Add</digi:trn></html:submit>

<html:submit property="event" style="inp-text" onclick="javascript: window.close(); return false;"><digi:trn key="aim:cancel">Cancel</digi:trn></html:submit>
</td>
</tr>

</table>

</digi:form>
