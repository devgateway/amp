<%@ page language="java" %>

<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script>
  function fnOnAddDomain() {
      <digi:context name="addUrl" property="context/module/moduleinstance/addDomain.do" />
      document.siteForm.action = "<%= addUrl %>";
      document.siteForm.submit();
  }
  function fnOnDeleteDomain( index, param ) {
      <digi:context name="deleteUrl" property="context/module/moduleinstance/deleteDomain.do" />
      document.siteForm.action = "<%= deleteUrl %>?id=" + index;
      document.siteForm.submit();
  }

</script>
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="module/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:editSite">Edit Site</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">
		
<digi:errors/>
      <digi:form action="/editSite.do" method="post" >
      <input type="hidden" name="referrer" value="editSite" />
      <TABLE width="500px" align="center">
        <TR>
          <TD class=text align=left colSpan=2><digi:trn key="admin:createNewSite">To create a new site, please complete the form below.</digi:trn></TD></TR>
        <TR>
          <TD>&nbsp;</TD></TR>
        <TR>
          <TD class=title align=left colSpan=2><digi:trn key="admin:siteInfo">Site information</digi:trn> 
          </TD></TR>
        <TR>
          <TD class=text align=left colSpan=2>
            <digi:trn>All fields marked with an</digi:trn><FONT color=red><B><BIG> * </BIG></B></FONT><digi:trn>are required.</digi:trn>
          </TD></TR>
        <TR>
          <TD colSpan=2 td>
            <TABLE cellspacing="1" cellPadding=2 width="95%" border="0">
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="admin:siteName">Site Name</digi:trn></TD>
            <TD class=text noWrap align=left><html:text styleClass="admin" property="siteName"/></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="admin:siteKey">Site Key</digi:trn></TD>
            <TD class=text noWrap align=left><html:text styleClass="admin" property="siteKey" readonly="true"/></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="admin:siteFolder">Site Folder</digi:trn></TD>
            <TD class=text noWrap align=left><html:text styleClass="admin" property="folderName"/></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="admin:metaDescription">Meta 
            Description</digi:trn></TD>
            <TD class=text noWrap align=left><html:text styleClass="admin" property="metaDescription"/></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="admin:metaKeywords">Meta 
            Keywords</digi:trn></TD>
            <TD class=text noWrap align=left><html:text styleClass="admin" property="metaKeywords"/></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="admin:mission">Mission</digi:trn></TD>
            <TD class=text noWrap align=left><html:text styleClass="admin" property="mission"/></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD align=left noWrap class="text">&nbsp;<digi:trn key="admin:isPrivate">Is site private?</digi:trn></TD>
	    <TD align=left noWrap class="text">
	    <TABLE>
              <TR bgColor=#f0f0f0>
                 <TD align=left noWrap class=text><html:radio  property ="secure" value="true"/><digi:trn key="admin:yes">Yes</digi:trn></TD>
                 <TD align=left noWrap class=text>&nbsp;<html:radio  property ="secure" value="false"/><digi:trn key="admin:no">No</digi:trn></TD>
              </TR>
           </TABLE></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD align=left noWrap class="text">&nbsp;<digi:trn key="admin:doesInheritSecurity">Does site inherit security?</digi:trn></TD>
	    <TD align=left noWrap class="text">
	    <TABLE>
              <TR bgColor=#f0f0f0>
                 <TD align=left noWrap class=text><html:radio  property ="inheritSecurity" value="true"/><digi:trn key="admin:yes">Yes</digi:trn>
                 <TD align=left noWrap class=text>&nbsp;<html:radio  property ="inheritSecurity" value="false"/><digi:trn key="admin:no">No</digi:trn></TD>
              </TR>
           </TABLE></TD>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="admin:priority">Priority</digi:trn></TD>
            <TD class=text noWrap align=left>
              <html:select property="priority" style="text">
                <html:options name="siteForm" property="priorities" />
              </html:select>     
            </TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<digi:trn key="admin:template">Template</digi:trn></TD>
            <TD class=text noWrap align=left>
              <c:if test="${ !empty siteForm.template}">
                 <c:out value="${siteForm.template}" />
              </c:if>
              <c:if test="${empty siteForm.template}"><digi:trn key="admin:blank">blank</digi:trn></c:if>
            </TD>
          </TR>
          <TR>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          </TR>
		  <TR>
		  <TD colspan="2">
					<table width="100%">
				  		<TR>
							<TD>
								<b><digi:trn key="adminAdminsRecieveAlert">Do site Admins receive alert Emails?</digi:trn></b>
				           </TD>
		           		   <TD align=left noWrap class=text>
					           <table with="100%">
							   	<tr>
									<td>
										<html:radio name="siteForm" property="recieveEmailAlerts" value="1"/>
										<digi:trn key="admin:yes">Yes</digi:trn></td>
						       		<td>
										<html:radio name="siteForm" property="recieveEmailAlerts" value="2"/>
										<digi:trn key="admin:no">No</digi:trn>
									</td>
									<td>
										<html:radio name="siteForm" property="recieveEmailAlerts" value="3"/>
										<digi:trn key="admin:inherit">inherit</digi:trn>
									</td>
							    </tr>		
								</table>
		           			</TD>
		         		</TR>
					</table>		  
		  </TD>
		  </TR>
          <TR>
            <TD align=left noWrap class="title"><digi:trn key="admin:siteDomains">Site Domains</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>		  
          <TR>
            <TD align=left noWrap class=text>
          	<table cellpadding="0" cellspacing="0" border="0">

             <tr><td>&nbsp;</td><td><digi:trn key="admin:domain">Domain</digi:trn></td><td>&nbsp;<digi:trn key="admin:path">Path</digi:trn></td><td>&nbsp;<digi:trn key="admin:language">Language</digi:trn></td><td>&nbsp;<digi:trn key="admin:enableSecurity">Security</digi:trn></td></tr>
    	     <logic:iterate indexId="index" name="siteForm" id="siteDomain" property="siteDomains" type="org.digijava.module.admin.form.SiteForm.SiteDomainInfo">
             <tr><td>
             <html:radio name="siteForm" property="defDomain" value='index' idName="siteDomain" />
             </td><td>
	    		<html:hidden name="siteDomain" property="id" indexed="true" />
	    		 <html:hidden name="siteDomain" property="index" indexed="true" />
             <html:text styleClass="admin" name="siteDomain" property="domain" indexed="true" />
              </td><td>
             &nbsp;<html:text styleClass="admin" name="siteDomain" property="path" indexed="true" />
              </td><td>
              &nbsp;<html:select name="siteDomain" property="langCode" indexed="true" style="text">
			  <html:option value="">- None -</html:option>
			    <c:set var="languages" value="${siteForm.languages}" scope="page" />
                <html:options  collection="languages" property="code" labelProperty="name" />
              </html:select>
              </td><td>&nbsp;<html:select name="siteDomain" property="enableSecurity" indexed="true" style="text">
			  <html:option value="-1">Both</html:option>
			  <html:option value="0">Unsecure</html:option>
			  <html:option value="1">Secure</html:option>
                   </html:select>
              </td><td>
               <a href='javascript:fnOnDeleteDomain("<%= index %>")' ><digi:trn key="admin:delete">Delete</digi:trn></a>
              </td></tr>
             </logic:iterate>
	     <tr><td colspan="2"><a href='javascript:fnOnAddDomain()' ><digi:trn key="admin:add">Add</digi:trn></a></td></tr>
            </table>
            </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor=#FFFFFF>
            <TD align=left noWrap class=text>
             </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class="title"><digi:trn key="admin:childSites">Child sites</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD> 
            <table>
            <logic:iterate id="childSite" name="siteForm" property="children" indexId="index" type="org.digijava.module.admin.form.SiteForm.SiteInfo">
            <tr><td class="text"><c:out value="${childSite.site.name}" /></td>
            <td class="text">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<digi:link href="/removeChildSite.do" paramId="id" paramName="childSite" paramProperty="site.id"><digi:trn key="admin:remove">Remove</digi:trn></digi:link></td>      
            <td class="text">&nbsp;<a href='<%= childSite.getViewSite() %>'><digi:trn key="admin:view">View</digi:trn></a></td>
            <td class="text">&nbsp;<a href='<%= childSite.getAdmin() %>'><digi:trn key="admin:admin">Admin</digi:trn></a></td>
            </tr>
            </logic:iterate> 
            </table> </TD>      
          </TR>
          <TR bgColor=#FFFFFF>
            <digi:context name="editSiteLink" property="/module/moduleinstance/showEditSite.do" />
            <digi:context name="createChild" property="context/module/moduleinstance/showCreateSite.do" />
            <c:set var="siteId" value="${siteForm.id}" scope="page" />
            <TD align=left noWrap class=text><a href='<%= createChild %>?parentId=<c:out value="${pageScope.siteId}" />&targetAction=<%= editSiteLink %>'><digi:trn key="admin:createChildSite">Create child site</digi:trn></a></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor=#FFFFFF>
            <digi:context name="pickup" property="context/module/moduleinstance/showPickupSite.do" />
            <digi:context name="addChild" property="context/module/moduleinstance/addChildSite.do" />
            <TD align=left noWrap class=text><a href="<%= pickup %>?targetAction=<%= addChild %>"><digi:trn key="admin:addChildSite">Add a child site</digi:trn></a></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor=#FFFFFF>
            <TD align=left noWrap class=text>
             </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class="title"><digi:trn key="admin:parentSite">Parent site</digi:trn></TD>
          </TR>
          <TR>
            <TD align=left noWrap class="text">
            <html:hidden name="siteForm" property="parentSiteName" />
            <c:if test="${ !empty siteForm.parentSiteName}">
            	<c:out value="${siteForm.parentSiteName}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<digi:link href="/clearParentSite.do"><digi:trn key="admin:clear">Clear</digi:trn></digi:link>
	   		</c:if>
            <c:if test="${ empty siteForm.parentSiteName}"><digi:trn key="admin:notDefined">Not defined</digi:trn></c:if>
            </TD>
          </TR> 
          <TR bgColor=#FFFFFF>
            <TD align=left noWrap class=text>
             </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class="title"><digi:trn key="admin:siteLangSettings">Site language settings</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor=#FFFFFF>
            <TD align=left noWrap class=text><digi:trn key="admin:siteDefNavLanguage">Default navigation language for this site is</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor=#FFFFFF>
            <TD align=left noWrap class=text>
              <html:select  property="defaultLanguage">
				<html:option value="">- <digi:trn key="admin:useDefSettings">Use default settings</digi:trn> -</html:option>
				<c:set var="languages" value="${siteForm.languages}" scope="page" />				
                <html:options  collection="languages" property="code" labelProperty="name" />
              </html:select>
             </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class="text"><digi:trn key="admin:userLanguages">User languages</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class=text>
          	<table cellpadding="0" cellspacing="0" border="0">
		  	 <c:forEach items="${siteForm.languages}" var="item">
             <tr><td>
             <html:multibox property="userLanguages" ><c:out value="${item.code}"/></html:multibox>
             <c:out value="${item.name}"/>
              </td></tr>
             </c:forEach>
            </table>
            </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class="text"><digi:trn key="admin:translatLanguages">Translation languages</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class=text>
          	<table cellpadding="0" cellspacing="0" border="0">
		  	 <c:forEach items="${siteForm.languages}" var="item">
             <tr><td>
             <html:multibox property="transLanguages" ><c:out value="${item.code}"/></html:multibox>
             <c:out value="${item.name}"/>
             </td></tr>
             </c:forEach>
            </table>
            </TD>
          </TR>
          <TR>
            <TD align=left noWrap class="title"><digi:trn key="admin:siteCountrySettings">Site country settings</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
				<TD noWrap align="left">
					<html:select property="country">
					<c:set var="countries" value="${siteForm.countries}" scope="page" />					
					<html:options collection="countries" property="iso" labelProperty="name"/></html:select>
				</TD>
          </TR>
          
<TR>
<TD colspan=2 align="center">
<html:submit/>

</TD>
</TR>
</table>

</digi:form>

</td>
	</tr>
</table>