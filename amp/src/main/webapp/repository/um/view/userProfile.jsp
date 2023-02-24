<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="userProfileForm" />
<digi:context name="showImage" property="context/showImage.do" />
<digi:errors/>
<table cellpadding="3" height="183" style="border-collapse: collapse" bordercolor="#111111" cellpadding="0" cellspacing="0">
 <tr>
   <td  colspan="2" width="571" height="1"><h2>
   <c:out value="${userProfileForm.firstNames}" /> 
   <c:out value="${userProfileForm.lastName}" />
 </h2></td>
 </tr>
 <tr bgcolor="#EEEEEE">
  <td  colspan="2" align="left" width="571" height="19">
  <digi:trn key="um:memberSince">  
    A member of the Development Gateway community since</digi:trn> 
    <c:out value="${userProfileForm.membersinceMonth}" /> 
    <c:out value="${userProfileForm.membersinceDay}" />,
    <c:out value="${userProfileForm.membersinceYear}" /></td>
 </tr>
<c:if test="${userProfileForm.publicProfile}">
<c:if test="${userProfileForm.owner}">
<tr><td colspan="2" width="571" height="19">&nbsp;</td></tr> 
<tr>
  <td align="right" height="19"><b><digi:trn key="um:bio">Bio:</digi:trn></b><td width="473" height="19">
</tr>
<tr>
  <td  align="right" height="19">&nbsp;<td width="477" height="19">
  <c:out value="${userProfileForm.bioText}" />
</tr>

<c:if test="${ !empty userProfileForm.organizationName}" >
<tr>
    <td align="right" height="19"><b><digi:trn key="um:organization">Organization:</digi:trn></b></td>
    <td align="left" height="19"><c:out value="${userProfileForm.organizationName}" /></td>
</tr>
</c:if>

<c:if test="${ !empty userProfileForm.mailingAddress}" >
<tr>
    <td nowrap align="left" height="19"><b><digi:trn key="um:mailingAddress">Mailing Address</digi:trn>:</b></td>
    <td align="left" height="19"><c:out value="${userProfileForm.mailingAddress}" /></td>
</tr>
</c:if>

<c:if test="${ !empty userProfileForm.url}" >
<tr>
    <td align="right" height="19"><b><digi:trn key="um:website">Website</digi:trn>:</b></td>
    <td align="left" height="19"><a href='<c:out value="${userProfileForm.url}" />'><c:out value="${userProfileForm.url}" /></a></td>
</tr>
</c:if>

<c:if test="${ !empty userProfileForm.activeUserImage}" >
<tr>
  <td align="right" height="19"><b><digi:trn key="um:photo">Photo:</digi:trn></b></td>
  <td align="right" height="19">&nbsp;</td>
</tr>
<tr>
  <td width="90" align="right" height="1"><td width="473" height="1">
  <div align="left">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="50%" id="AutoNumber1" height="9">
      <tr>
        <td width="100%" height="9"></td>
      </tr>
    </table>
  </div>
  <p><html:img src="<%=showImage%>" paramName="userProfileForm" paramId="id" paramProperty="activeUserImage" />
</tr>
</c:if>

<c:if test="${ !empty userProfileForm.sites}" >
<tr>
  <td align="right" height="19"><b><digi:trn key="um:membership">Membership:</digi:trn></b></td>
  <td align="left" height="19" valign="top">&nbsp;</td>
</tr>
<tr>
   <td align="right" height="19">&nbsp;</td>
   <td align="left">
   <table>
      <c:forEach var="item"  items="${userProfileForm.sites}">
      <tr bgcolor="#F0F0F0">
         <td align="left" class="text"><a href='<c:out value="${item.siteUrl}"/>' class="text"><c:out value="${item.siteDescription}"/></a></td>
      </tr>    
     </c:forEach>
    </table>
  </td>  
</tr>    
</c:if>
</c:if>

<c:if test="${! userProfileForm.owner}">
<c:if test="${!empty userProfileForm.activeUserImage}">
<tr>
  <td align="right" height="19"><b><digi:trn key="um:photo">Photo:</digi:trn></b></td>
  <td align="right" height="19">&nbsp;</td>
</tr>
<tr>
  <td width="90" align="right" height="1"><td width="473" height="1">
  <div align="left">
    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="50%" id="AutoNumber1" height="9">
      <tr>
        <td width="100%" height="9"></td>
      </tr>
    </table>
  </div>
  <p><html:img src="<%=showImage%>" paramName="userProfileForm" paramId="id" paramProperty="activeUserImage" />
</tr>
</c:if>

<c:if test="${ !empty userProfileForm.sites}" >
<tr>
  <td align="right" height="19"><b><digi:trn key="um:membership">Membership:</digi:trn></b></td>
  <td align="left" height="19" valign="top">&nbsp;</td>
</tr>
<tr>
   <td align="right" height="19">&nbsp;</td>
   <td align="left">
   <table>
      <c:forEach var="item"  items="${userProfileForm.sites}">   
      <tr bgcolor="#F0F0F0">
          <td align="left" class="text"><a href='<c:out value="${item.siteUrl}"/>' class="text"><c:out value="${item.siteDescription}"/></a></td>
      </tr>    
     </c:forEach>
    </table>
  </td>  
</tr>    
</c:if>
<tr><td colspan="2" width="571" height="19">&nbsp;</td></tr>

<c:if test="${userProfileForm.contact}">
<tr>
<td  colspan="2" width="571" height="19">
    <digi:link href="/showUserContact.do" paramName="userProfileForm" paramId="activeUserId" paramProperty="activeUserId" styleClass="text" onclick="newWindow(this.href,'Portrait_and_Bio', 'width=600,height=500,status=no, menusbar=no, toolbar=no');return false;"><digi:trn key="um:contactThisUser">Contact this user</digi:trn></digi:link></td>
</tr>
</c:if>
</c:if>
</c:if>
</table>

<script language="javascript">
<!--

function newWindow(file,window,features) {
    msgWindow=open(file,window,features);
    if (msgWindow.opener == null) msgWindow.opener = self;
}
// -->
      </script>