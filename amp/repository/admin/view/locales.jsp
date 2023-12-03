<%@ page language="java" %>

<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="ampModule/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:locales">Locales</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">
<digi:errors />
<digi:form action="/editLocales.do" method="post" >
<c:if test="${!empty localesForm.locales}" >
  <table width="50%" border="0" class="border" height="72">
   <tr>
       <td noWrap width="10%" align="center" height="19"><b><digi:trn key="admin:code">Code</digi:trn></b></td>
       <td noWrap width="30%" align="center" height="19"><b><digi:trn key="admin:name">Name</digi:trn></b></td>
       <td noWrap width="10%" align="center" height="19"><b><digi:trn key="admin:ltrLocale">Left-to-right?</digi:trn></b></td>
       <td noWrap width="10%" align="center" height="19"><b><digi:trn key="admin:available">Available</digi:trn></b></td>
   </tr>
  <c:forEach var="locale" items="${localesForm.locales}">
   <tr bgcolor="#F2F4F8"> 
      <td width="10%" align="left" height="19"><c:out value="${locale.code}" /></td>
      <td width="40%" align="left" height="19"><c:out value="${locale.name}" /></td>
      <td width="20%" align="center" height="19">
        <html:hidden name="locale" indexed="true" property="code" />  
        <html:hidden name="locale" indexed="true" property="name" />
        <html:select name="locale" indexed="true" property="leftToRight">
  	    	<html:option  value="true" >true</html:option>
  	    	<html:option  value="false" >false</html:option>
        </html:select>
       </td>
       <td>	
        <html:select name="locale" indexed="true" property="available">
  	    	<html:option  value="false" >false</html:option>
  	    	<html:option  value="true" >true</html:option>
        </html:select>
      </td>
   </tr>
  </c:forEach>
  </table>
  <table border="0">
     <tr><td><html:submit value="submit" /></td></tr>
  </table>    
</c:if>
</digi:form>
		</td>
	</tr>
</table>