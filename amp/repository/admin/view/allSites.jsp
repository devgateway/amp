<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors />
<digi:instance property="allSitesForm" />

<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="module/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:allSites">All sites</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">

<c:if test="${ !empty allSitesForm.sites}">
<HR>
<c:if test="${ !empty allSitesForm.parentSiteName}" >
 <table>
    <tr><td><digi:trn key="admin:childsOfSite">Childs of Site</digi:trn> <b><c:out value="${allSitesForm.parentSiteName}" /></b></td></tr>
 </table>
 <br>
</c:if> 
<div align="center">
<table width="60%" border="0" cellpadding="0" cellspacing="0" class="border">
 <tr>
    <td width="30%" align="left"><b><digi:trn key="admin:siteName">Site Name</digi:trn></b></td>
    <td width="10%" align="center">&nbsp;</td>
    <td width="10%" align="center">&nbsp;</td>
    <td width="10%" align="center">&nbsp;</td>
 </tr>
<c:forEach var="sites" items="${allSitesForm.sites}">
 <tr bgcolor="#F0F0F0">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
 </tr> 
 <tr bgcolor="#EEEEEE"> 
 <td width="30%" align="left"> 
   	<bean:define id="siteName" name="sites" property="siteName" type="java.lang.String" />
   	<bean:define id="siteId" name="sites" property="siteId" type="java.lang.String" />
    <c:if test="${sites.hasChildren}">
       <digi:link href="/showAllSites.do" paramName="sites" paramId="selectedSiteId" paramProperty="id" >
	 	<digi:trn key='<%= "site:" + siteName %>' siteId='<%=siteId%>'>	
	      <c:out value="${sites.siteName}" />
		</digi:trn>	 
	  </digi:link>
    </c:if>
    <c:if test="${!sites.hasChildren}">
	 <digi:trn key='<%= "site:" + siteName %>' siteId='<%=siteId%>'>	
         <c:out value="${sites.siteName}" />
	</digi:trn>	 
    </c:if>
    </td>
    <td nowrap width="10%" align="left" valign="center">  
  	  <a href='<c:out value="${sites.viewSite}" />'><digi:trn key="admin:viewSite">View Site</digi:trn></a>
    </td>
    <td nowrap width="10%" align="left" >
       <c:if test="${sites.editableSite}">
  	     <a href='<c:out value="${sites.editSite}" />'><digi:trn key="admin:editSite">Edit Site</digi:trn></a> </c:if>
   </td>	
    <td nowrap width="10%" align="left" valign="center">  
       <c:if test="${sites.editableSite}">
  	  <a href='<c:out value="${sites.admin}" />'><digi:trn key="admin:admin">Admin</digi:trn></a> </c:if>
    </td>
 </tr>			
</c:forEach>
</table>
</c:if>
<HR>
</div>
<c:if test="${! allSitesForm.children}">
 <c:if test="${allSitesForm.addSite}">
  <digi:context name="showSiteLink" property="/module/showAllSites.do"/>
  <digi:context name="createSite" property="context/module/moduleinstance/showCreateSite.do" />
  <a href="<%= createSite %>?targetAction=<%= showSiteLink %>"><digi:trn key="admin:addSite">Add Site</digi:trn></a>
 </c:if>
</c:if>
<c:if test="${allSitesForm.children}">
<table>
<tr>
 <digi:context name="showChildLink" property="/module/showAllSites.do"/>
 <digi:context name="createChild" property="context/module/moduleinstance/showCreateSite.do" />
 <c:set var="siteId" value="${allSitesForm.selectedSiteId}" scope="page" />
   <td align="left" noWrap class="text"><a href='<%= createChild %>?parentId=<c:out value="${pageScope.siteId}" />&targetAction=<%= showChildLink %>?selectedSiteId=<c:out value="${pageScope.siteId}" />'><digi:trn key="admin:addSite">Add Site</digi:trn></a></td>
</tr>
</table>
</c:if>
		</td>
	</tr>
</table>