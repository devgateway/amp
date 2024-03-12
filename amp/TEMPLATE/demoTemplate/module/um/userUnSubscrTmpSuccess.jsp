<%@ page language="java" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>

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
