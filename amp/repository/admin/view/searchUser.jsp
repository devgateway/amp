<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<digi:instance property="searchUserForm" />

<digi:form action="/searchUser.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="module/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:userAdministration">User Administration</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left" height="100%">

<digi:errors/>
<table border="0" class="border">
<tr>
 <td>
  <TABLE border="0" width="100%" >
   <TR>
     <TH align="right" nowrap="nowrap"><digi:trn key="admin:userInfo">User Info:</digi:trn></TH>
     <TD align="left"><html:text property="searchUserInfo"/></TD>
     <TD align="right" colspan=2><html:submit value="Search"/></TD>
   </TR>
   <tr>
   	 <td colspan="3"><html:checkbox property="hideBanned" /><digi:trn key="admin:hideBanned">Hide banned users</digi:trn></td>
   </tr>
  </TABLE>
 </td>
</tr>
</table>

<div align="center">
<c:if test="${!empty searchUserForm.userList}">
<HR>
<table border="0" cellpadding="0" cellspacing="0">
 <tr>
    <td nowrap="nowrap" width="30%" align="center"><b><digi:trn key="admin:email">Email</digi:trn></b></td>
    <td nowrap="nowrap" width="50%" align="center"><b><digi:trn key="admin:name">Name</digi:trn></b></td>
    <td nowrap="nowrap" width="20%">&nbsp;</td> 
 </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0">
 <c:forEach var="userList" items="${searchUserForm.userList}">
 <tr bgcolor="#EEEEEE">    
    <td width="30%" align="left">
     <c:out value="${userList.email}"/>
    </td>
    <td width="50%" align="center"> 
      <c:out value="${userList.firstNames}" /> 
      <c:out value="${userList.lastName}" />
    </td>
    <td noWrap width="20%" align="left">
    <digi:link href="/showAdministrateUser.do" paramName="userList" paramId="selectedUserId" paramProperty="id"><digi:trn key="admin:administerUser">Administrate user</digi:trn></digi:link></td>
 </tr>
</c:forEach>
</table>
</c:if>
</div>
		</td>
	</tr>
</table>
</digi:form>