<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>
  function fnOnExpand( param ) {
      <digi:context name="expandUrl" property="context/module/moduleinstance/showPickupSite.do" />
      document.pickupForm.expand.value=param;
      document.pickupForm.action = "<%= expandUrl %>";

      document.pickupForm.submit();
  }
  function fnOnPickup( param ) {
     document.pickupForm.action = document.pickupForm.targetAction.value + "?id=" + param;

     document.pickupForm.submit();
  }

</script>

<digi:errors/>
<digi:form action="/showPickupSite.do" method="post">
<html:hidden property="targetAction" />
<input type="hidden" name="expand" />
<table><tr><td noWrap align="center"><h3><b><digi:trn key="admin:selectSite">Select Site</digi:trn></h3></b></td></tr></table>
<table border="0">
<c:forEach var="item" items="${pickupForm.items}">
<tr><td align="left">
<c:forEach var="i" begin="0" end="${item.level}" step="1">
  <% out.print("&nbsp;&nbsp;"); %>
</c:forEach>
<html:hidden name="item" property="expand" indexed="true" />
<html:hidden name="item" property="key" indexed="true" />
<a href="javascript:fnOnExpand('<c:out value="${item.key}" />')">+</a>
&nbsp;
<c:if test="${!empty item.id}">
<a href="javascript:fnOnPickup('<c:out value="${item.id}" />')"><c:out value="${item.name}" /></a>
</c:if>
<c:if test="${empty item.id}">
  <c:out value="${item.name}" />
</c:if></td></tr>
</c:forEach>
</table>
</digi:form>