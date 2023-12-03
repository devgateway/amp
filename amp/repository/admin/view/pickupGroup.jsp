<%@ page language="java" %>

<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script>
  function fnOnExpand( param ) {
      <digi:context name="expandUrl" property="context/ampModule/moduleinstance/showPickupGroup.do" />
      document.pickupForm.expand.value=param;
      document.pickupForm.action = "<%= expandUrl %>";
      document.pickupForm.submit();
  }
  function fnOnPickup( param ) {
      window.opener.pickupGroup(param);
      window.close();
  }

</script>
<digi:base />
<digi:errors/>
<digi:form action="/showPickupGroup.do" method="post">
<html:hidden property="targetAction" />
<input type="hidden" name="expand" />
<table><tr><td noWrap align="center"><h3><b><digi:trn key="admin:selectGroups">Select Groups</digi:trn></h3></b></td></tr></table>
<table border="0">
<logic:iterate id="item" name="pickupForm" property="items" type="org.digijava.ampModule.admin.helper.pickup.PickupItem">
<tr><td align="left">
   <table><tr>
      <td>
<c:forEach var="i" begin="0" end="${item.level}" step="1">
  <% out.print("&nbsp;&nbsp;"); %>
</c:forEach>
      </td>
 <td style="text-decoration: none; color: blue;">
<a href="javascript:fnOnExpand('<%= item.getKey()%>')">+</a></td>
 <td>
<c:if test="${!empty item.id}"><digi:img src="ampModule/admin/images/group.gif" border="0"/></c:if>
<c:if test="${empty item.id}"><digi:img src="ampModule/admin/images/site.gif" border="0"/></c:if>
</td>
 <td>
<html:hidden name="item" property="expand" indexed="true" />
<html:hidden name="item" property="key" indexed="true" />
</td>
<td>&nbsp;
<c:if test="${!empty item.id}">
  <a href="javascript:fnOnPickup('<c:out value="${item.id}" />')"><c:out value="${item.name}" /></a>
</c:if>
<c:if test="${empty item.id}">
  <digi:trn key='<%= "groups:" + item.getName() %>'><c:out value="${item.name}" /></digi:trn>
</c:if></td></tr>
</table></tr></td>
</logic:iterate>
</table>
</digi:form>