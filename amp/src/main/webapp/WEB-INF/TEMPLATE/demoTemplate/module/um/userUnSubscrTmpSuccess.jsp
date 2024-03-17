<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>

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
