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
				<digi:trn key="admin:searchSite">Search site</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">
		
		
<digi:errors />
<digi:instance property="searchSiteForm" />
<digi:form action="/searchSite.do" >

<table border="0" class="border">
<tr>
 <td>
  <TABLE border="0" width="100%" >
   <TR>
     <TH align="right"><digi:trn key="admin:siteKey1">Site Key:</digi:trn></TH>
     <TD align="left"><html:text styleClass="admin" property="siteKey"/></TD>
     <TD align="right" colspan=2><html:submit value="Search"/></TD>
   </TR>
  </TABLE>
 </td>
</tr>
</table>

<div align="center">
<c:if test="${ !empty searchSiteForm.sites}">
<HR>
<table border="0" cellpadding="0" cellspacing="0">
 <tr>
    <td noWrap width="20%" align="left"><b><digi:trn key="admin:name">Name</digi:trn></b></td>
    <td noWrap width="20%" align="left"><b><digi:trn key="admin:ID">ID</digi:trn></b></td>
    <td noWrap width="50%" align="center">&nbsp;</td>
    <td noWrap width="10%" align="left">&nbsp;</td>
 </tr>
 <c:forEach var="sites" items="${searchSiteForm.sites}" >
 <tr bgcolor="#F0F0F0">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
</tr> 
 <tr bgcolor="#EEEEEE">    
    <td width="20%" align="left" valign="center">
     <c:out value="${sites.siteName}"/>
    </td>
    <td width="20%" align="left" valign="center">  
     <b><c:out value="${sites.siteId}"/></b>
    </td>
    <td width="50%" align="left" >
      <table width="100%" border="0" class="border">
         <c:if test="${! empty sites.siteDomains}">
         <tr>
           <td noWrap width="60%" align="left"><b><digi:trn key="admin:domains">Domains</digi:trn></b></td>
           <td noWrap width="40%" align="left"><b><digi:trn key="admin:paths">Paths</digi:trn></b></td>
         </tr>
         <c:forEach var="siteDomains" items="${sites.siteDomains}" >
           <tr>
              <td width="60%" align="left">
               <c:out value="${siteDomains.siteDomain}"/>
              </td>
              <td width="40%" align="left">
               <c:out value="${siteDomains.sitePath}"/>
              </td>
           </tr>
         </c:forEach>
         </c:if>
      </table> 
    </td>
    <td width="10%" align="center">
     <table>
        <tr>
             <c:if test="${ !empty sites.adminLink}">
     	          <td noWrap width="50%" align="center"><a href='<c:out value="${sites.adminLink}" />'><digi:trn key="admin:admin">Admin</digi:trn></a></td>
             </c:if> 
			 <c:if test="${sites.delete}">
                  <td noWrap width="50%" align="center"><digi:link href="/deleteSite.do" paramName="sites" paramId="selectedSiteId" paramProperty="id"><digi:trn key="admin:delete">Delete</digi:trn></digi:link></td> 
             </c:if>
        </tr>			
     </table>
   </td>
 </tr>
 </c:forEach>
</table>
 </c:if>
</digi:form>
</div>

</td>
	</tr>
</table>