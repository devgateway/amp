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
      <digi:context name="addUrl" property="context/ampModule/moduleinstance/addDomain.do" />
      document.siteForm.action = "<%= addUrl %>";
      document.siteForm.submit();
  }
  function fnOnDeleteDomain( index, param ) {
      <digi:context name="deleteUrl" property="context/ampModule/moduleinstance/deleteDomain.do" />
      document.siteForm.action = "<%= deleteUrl %>?id=" + index;
      document.siteForm.submit();
  }

</script>


<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="ampModule/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle"><digi:trn key="admin:createSite">Create Site</digi:trn></font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">

		
		
<digi:errors/>
<html:messages id="null" message="true" />


      <TABLE align="center">
		<digi:form action="/createSite.do" method="post">
		<input type="hidden" name="referrer" value="createSite" />
		<c:if test="${ !empty siteForm.parentId}" >
			<html:hidden property="parentId" />
		</c:if>
		<html:hidden property="targetAction" />

        <TR>
          <c:if test="${ !empty siteForm.parentSiteName}" >
          <TD class=text align=left colSpan=2>
          <digi:trn key="admin:toCreateChildSite">
            To create a new child site of Site</digi:trn> <b><c:out value="${siteForm.parentSiteName}" /></b>
            <digi:trn key="admin:pleaseCompleteTheForm">please complete the form below.</digi:trn></TD>
          </c:if> 
          <c:if test="${ empty siteForm.parentSiteName}" >
          <TD class=text align=left colSpan=2><digi:trn key="admin:createNewSite">To create a new site, please complete the form below.</digi:trn></TD>
          </c:if> 
          </TR>
        <TR>
          <TD>&nbsp;</TD></TR>
        <TR>
          <TD class=title align=left colSpan=2><digi:trn key="admin:siteInfo">Site information</digi:trn> 
          </TD></TR>
        <TR>
          <TD class=text align=left colSpan=2>
          <digi:trn>All fields marked with an</digi:trn><FONT color=red><B><BIG> * </BIG></B></FONT><digi:trn>are required.</digi:trn>
        <TR>
          <TD colSpan=2>
            <TABLE cellspacing="1" cellPadding=2 width="95%" border="0">
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="admin:siteName">Site Name</digi:trn></TD>
            <TD class=text noWrap align=left><html:text styleClass="admin" property="siteName"/></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="admin:siteKey">Site Key</digi:trn></TD>
            <TD class=text noWrap align=left><html:text styleClass="admin" property="siteKey"/></TD>
          </TR>
          <TR bgColor=#f0f0f0>
            <TD class=text noWrap align=left>&nbsp;<FONT
                  color=red>*</FONT><digi:trn key="admin:siteFolder">Site Folder</digi:trn></TD>
            <TD class=text noWrap align=left><html:text styleClass="admin" property="folderName"/></TD>
          </TR>
          <TR>
            <TD align=left noWrap class="title"><digi:trn key="admin:siteDomains">
            Site Domains</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR>
            <TD align=left noWrap class=text>
          	<table cellpadding="0" cellspacing="0" border="0">

             <tr><td>&nbsp;</td><td><digi:trn key="admin:domain">Domain</digi:trn></td>
			 <td>&nbsp;<digi:trn key="admin:path">Path</digi:trn></td>
			 <td>&nbsp;<digi:trn key="admin:enableSecurity">Security</digi:trn></td>
             </tr>
    	     <logic:iterate indexId="index" name="siteForm" id="siteDomain" property="siteDomains" type="org.digijava.ampModule.admin.form.SiteForm.SiteDomainInfo">
             <tr><td>
	             <html:radio name="siteForm" property="defDomain" value='index' idName="siteDomain" />
             </td><td>
	     	  <html:hidden name="siteDomain" property="id" indexed="true" />
	     	  <html:hidden name="siteDomain" property="index" indexed="true" />
             <html:text styleClass="admin" name="siteDomain" property="domain" indexed="true" />
              </td><td>
              &nbsp;<html:text styleClass="admin" name="siteDomain" property="path" indexed="true" />
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
            <TD align=left noWrap class=text><digi:trn key="admin:siteTemplate">Site's template is</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor=#FFFFFF>
            <TD align=left noWrap class=text>
              <html:select  property="template">
                 <html:options  name="siteForm" property="templates" />    
              </html:select>
             </TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>

          <TR >
            <TD align=left noWrap class="text"><digi:trn key="admin:isSecure">Is site secure?</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
          <TR bgColor=#FFFFFF>
            <TD align=left noWrap class=text><html:radio  property ="secure" value="true"/><digi:trn key="admin:yes">Yes</digi:trn>
              <html:radio  property ="secure" value="false"/><digi:trn key="admin:no">No</digi:trn></TD>
            <TD align=left noWrap class=text>&nbsp;</TD>
          </TR>
        </TABLE >
        </td>

<TR>
<TD colspan=2 align="center">
<html:submit />
</TD>
</TR>
</table>
</digi:form>



</td>
	</tr>
</table>