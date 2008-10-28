<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="userUnSubscribeForm" />
<table>
<tr>
  <td><h3> Vacation Information Updated </h3></td>
</tr>
<tr>
   <td>in  <digi:link href="/showLayout.do">Development Gateway</digi:link></td> 
</tr>
<tr>
 <td><hr></td>
</tr
<tr>
 <td>You won't get any email until after
 <bean:write name="userUnSubscribeForm" property="selectedMonth" />.
 <bean:write name="userUnSubscribeForm" property="day"/>. 
 <bean:write name="userUnSubscribeForm" property="year"/></td>
</tr>  
<tr>
 <td><hr></td>
</tr
</table>
