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
<digi:form action="/addDisbOrderToContract.do" method="post">
<table width="100%" cellpadding=1 cellspacing=3 bgcolor="#ffffff">
<tr>
<td colspan="2" width="100%" bgcolor="#006699" class="textalb" height="20" align="center"><digi:trn key="aim:selectContract">Select Contract:&nbsp;</digi:trn></td>
</tr>
<tr />
<tr>
<c:if test="${empty aimEditActivityForm.contracts}">
<td align="center" colspan="2">
    <digi:trn key="aim:noContract">There are not contracts for Activity to select</digi:trn>
</td>
</c:if>
<c:if test="${not empty aimEditActivityForm.contracts}">
<td width="50%" align="right"><digi:trn key="aim:selectContract">Select Contract:&nbsp;</digi:trn></td>


<td align="left">
  <html:select name="aimEditActivityForm" property="selContractId" styleClass="inp-text">
  <c:forEach var="contract" items="${aimEditActivityForm.contracts}" varStatus="conId">
  
    <html:option value="${conId.count}">${contract.contractName}</html:option>
  
  </c:forEach>
  </html:select></td>
</tr>
<tr>
<td colspan="2" align="center">
<html:submit property="event" style="inp-text"><digi:trn key="aim:add">Add</digi:trn></html:submit>

<html:submit property="event" style="inp-text" onclick="javascript: window.close(); return false;"><digi:trn key="aim:cancel">Cancel</digi:trn></html:submit>
</td>
</c:if>
</tr>

</table>

</digi:form>
