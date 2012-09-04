<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script>
  function fnOnMultiSelect() {
      var users;
      var i = 0;
      if (document.searchUserForm.selectedUsers == null) {
          alert("please select at least one user(s)");
      } else {
        if (document.searchUserForm.selectedUsers.length==null) {
           if (document.searchUserForm.selectedUsers.checked) {
              users= document.searchUserForm.selectedUsers.value;
           }
        } else {
          for(var j=0;j<document.searchUserForm.selectedUsers.length;j++) {
            if (document.searchUserForm.selectedUsers[j].checked) {
               if (users == null) {
                   users = document.searchUserForm.selectedUsers[j].value;
               } else {
                   users += ',' + document.searchUserForm.selectedUsers[j].value;
               }
            }
          }
        }
        if (users == null) {
          alert("please select at least one user(s)");
        } else {
          window.opener.selectUsers(users);
          window.close();
        }
      }
  }

  function fnOnSelect(id) {
      window.opener.selectUsers(id);
      window.close();
  }
</script>

<digi:instance property="searchUserForm" />

<digi:form action="/searchUser.do">
<html:hidden name="searchUserForm" property="targetAction" />
<digi:errors/>
<table border="0" class="border">
<tr>
 <td>
  <TABLE border="0" width="100%" >
   <TR>
     <TH align="right"><digi:trn key="admin:userInfo">User Info:</digi:trn></TH>
     <TD align="left"><html:text property="searchUserInfo"/></TD>
     <TD align="right" colspan=2><html:submit value="Search"/></TD>
   </TR>
  </TABLE>
 </td>
</tr>
</table>

<div align="center">
<c:if test="${!empty searchUserForm.userList}">
<HR>
<table border="0" cellpadding="0" cellspacing="0">
 <tr>
    <td width="10%">&nbsp;</td> 
    <td noWrap width="20%" align="center"><b><digi:trn key="admin:email">Email</digi:trn></b></td>
    <td noWrap width="50%" align="center"><b><digi:trn key="admin:name">Name</digi:trn></b></td>
    <td width="20%">&nbsp;</td> 
 </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0">
<logic:iterate id="userList" property="userList" name="searchUserForm" type="org.digijava.kernel.user.User">
 <tr bgcolor="#EEEEEE">    
    <td width="10%"><html:multibox property="selectedUsers"><bean:write name="userList" property="id" /></html:multibox></td>
    <td width="20%" align="left">
     <c:out value="${userList.email}"/>
    </td>
    <td width="50%" align="center"> 
      <c:out value="${userList.firstNames}" /> 
      <c:out value="${userList.lastName}" />
    </td>
    <td noWrap width="20%" align="left">
     <a href="javascript:fnOnSelect(<%= userList.getId() %>)"><digi:trn key="admin:select">Select</digi:trn></a>
    </td>
 </tr>
</logic:iterate>
</table>
</c:if>
</digi:form>
</div>
<a href="javascript:fnOnMultiSelect()"><digi:trn key="admin:select">Select</digi:trn></a>